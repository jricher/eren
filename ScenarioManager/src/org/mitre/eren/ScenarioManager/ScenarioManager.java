package org.mitre.eren.ScenarioManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.ExtensionFactory;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.parser.Parser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.mitre.eren.http.InboundHttpEndpoint;
import org.mitre.eren.http.InboundMessageHandler;
import org.mitre.eren.http.OutboundHttpEndpoint;
import org.mitre.eren.message.MessageProcessor;
import org.mitre.eren.protocol.action.ActionsFile;
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.clock.ClockExtensionFactory;
import org.mitre.eren.protocol.clock.ClockresetWrapper;
import org.mitre.eren.protocol.clock.ClockstartWrapper;
import org.mitre.eren.protocol.clock.SetClockspeed;
import org.mitre.eren.protocol.clock.SetClocksync;
import org.mitre.eren.protocol.kml.KMLExtensionFactory;
import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.Role;
import org.mitre.eren.protocol.scenario.Roles;
import org.mitre.eren.protocol.scenario.ScenarioConstants;
import org.mitre.eren.protocol.scenario.ScenarioExtensionFactory;
import org.mitre.eren.protocol.scenario.ScenarioLocation;
import org.mitre.eren.protocol.scenario.Timing;
import org.mitre.eren.protocol.startup.ActiveRole;
import org.mitre.eren.protocol.startup.ActiveRoles;
import org.mitre.eren.protocol.startup.ClientReady;
import org.mitre.eren.protocol.startup.CreateGame;
import org.mitre.eren.protocol.startup.Game;
import org.mitre.eren.protocol.startup.GameCreated;
import org.mitre.eren.protocol.startup.GameList;
import org.mitre.eren.protocol.startup.GameOver;
import org.mitre.eren.protocol.startup.GameStart;
import org.mitre.eren.protocol.startup.JoinGame;
import org.mitre.eren.protocol.startup.Login;
import org.mitre.eren.protocol.startup.Logout;
import org.mitre.eren.protocol.startup.RoleDenied;
import org.mitre.eren.protocol.startup.RoleFilled;
import org.mitre.eren.protocol.startup.ScenarioList;
import org.mitre.eren.protocol.startup.Sending;
import org.mitre.eren.protocol.startup.StartupConstants;
import org.mitre.eren.protocol.startup.StartupExtensionFactory;
import org.opencare.lib.model.edxl.EDXLDistribution;

public class ScenarioManager<V> implements MessageProcessor, ScenarioConstants,
StartupConstants, ClockConstants {

	private OutboundHttpEndpoint esb; // How to send to the ESB
	private InboundHttpEndpoint server; // How get messages from the ESB.
	private String serverAddr = "http://erenbus.mitre.org:3732/"; //address to send stuff to
	private int busPort; //where to listen
	private String scenarioDir; //where the scenarios are 
	private static Logger log ;
	private Map<String,ERENscenario> idToScenario;  //a map between scenario ids and a scenario
	private Map<String, Map<String,Role>> scenarioRoleList; //<SceiarniID, <RoleID,Role>>
	private Factory factory; //it's a factory
	private Map<String,Long> scenarioFileSize; //File size
	private Map<String,GameData> mapGames; //A map of Game IDs to currently running games
	private Lobby lobby; //The lobby, where people hang out
	private String logfile;
	private static final String lby = "Lobby";
	private static final String urnGID = "eren:GameID";
	private static int gameIDCounter = 0;
	
	private static String fakeGameID = "IamNotAGameID"; //Hack untill we get the game creation works

	/**
	 * Default constructor
	 * @param args Args from the command line
	 */
	public ScenarioManager(String[] args) {
		try {
			Options options = new Options();
			options.addOption("p", "port", true,
			"Server port to listen on for incoming HTTP messages from the bus");
			options.addOption("h", "help", false, "Prints this help message");
			options.addOption("b", "bus", true, "Port to send stuff to the bus");
			options.addOption("d", "dir", true, "Scenerio Directory");
			options.addOption("l", "log", true, "Path to logfiles");

			CommandLineParser parser = new PosixParser();
			CommandLine cmd = parser.parse(options, args);

			//Set up logging
			if (cmd.hasOption("l")){
				logfile = cmd.getOptionValue("l");
				FileHandler fh;
				try {
					fh = new FileHandler(logfile + System.getProperty("file.separator")+ "SM%g.log", 10000000, 5);
					log = Logger.getLogger(ScenarioManager.class.getName());
					log.addHandler(fh);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//If the help option is used
			if (cmd.hasOption("h")) {
				HelpFormatter hf = new HelpFormatter();
				hf.printHelp("Shuttle.jar", options);
				System.exit(1);
			}
			//The port where the bus lives 
			if (cmd.hasOption("p")) {
				serverAddr= cmd.getOptionValue("p");
				log.info("Bus address is " + serverAddr);
			}
			//Where the messages from the bus are being sent
			if (cmd.hasOption("b")) {
				busPort = Integer.parseInt(cmd.getOptionValue("b"));
				log.info("Incomeing Bus port is " + busPort);
			}
			//The directory where the scenarios live
			if (cmd.hasOption("d")) {
				scenarioDir = cmd.getOptionValue("d");
			}				

		} catch (ParseException e) {
			log.severe(e.getMessage());
		}
		
		log.setLevel(Level.FINE);
		// Create an incoming connection with the ESB
		log.info("Creating inbound connection on port " + busPort);
		InboundHttpEndpoint server = new InboundHttpEndpoint(busPort,
				new InboundMessageHandler(this));
		Thread st = new Thread(server, "EREN HTTP Inbound Endpoint");
		st.start();
		log.info("Finished creating inbound connection");

		// Create an outgoing connection with the ESB.
		log.info("Creating outgoing connection to MULE ");
		esb = new OutboundHttpEndpoint(serverAddr,null);
		esb.registerExtension(new ClockExtensionFactory());
		esb.registerExtension(new StartupExtensionFactory());
		esb.registerExtension(new ScenarioExtensionFactory());

		log.info("Finished outgoing connection to MULE");		

		//Boiler plate for Abdera
		Abdera abdera = Abdera.getInstance();
		factory = abdera.getFactory();
		factory.registerExtension(new ScenarioExtensionFactory());
		factory.registerExtension(new KMLExtensionFactory());
		factory.registerExtension(new StartupExtensionFactory());
		log.info("finished factory configuration");

		//Populate the data structures
		mapGames = new HashMap<String,GameData>();
		log.info("Created empty list of games");
		idToScenario = new HashMap<String, ERENscenario>();
		log.info("Created idToScenario");
		scenarioFileSize = new HashMap<String, Long>();
		log.info("Created scenarioFileSize");
		scenarioRoleList = new HashMap<String, Map<String,Role>>();
		log.info("created scenarioRoleList");
		lobby = new Lobby(logfile);
		log.info("creating new lobby");		
		getScenarioList();
	}// End of ScenarioManager()

	// Boilerplate for message processing
	@Override
	public <T extends ExtensionFactory> List<T> getExtensions() {
		//return (List<T>) Collections.singletonList(new ClockExtensionFactory());
		List<T> efs = new ArrayList<T>();
		efs.add((T) new ClockExtensionFactory());
		efs.add((T) new StartupExtensionFactory());
		return efs;
	}
	 
	@Override
	public void processMessage(Element e, String gameID, String sender,
			List<String> roles, List<String> userID) {
	if (e.getQName().equals(EREN_ROLEREQUEST)){
			//roleRequsted((RoleRequest)e);
			log.finest("Role Requested");
		}
		else if (e.getQName().equals(EREN_CLIENTREADY)){
			clientReady((ClientReady)e);
			log.info("Client ready");
		}
		else if(e.getQName().equals(EREN_LOGOUT)){
			logOut((Logout) e);
		}	
		else if(e.getQName().equals(EREN_LOGIN)){
			logIn((Login)e);
		}
		else if(e.getQName().equals(EREN_JOINGAME)){
			joinGame(e);
		}
		else if(e.getQName().equals(EREN_SCENARIOLISTREQUEST)){
			sendList(e);
		}
		else if(e.getQName().equals(EREN_CREATEGAME)){
			CreateGame cg = (CreateGame)e;
			EDXLDistribution source = (EDXLDistribution) e.getDocument().getRoot();
			createNewGame(newGameID(), cg.getGameName(), cg.getScenarioId(), logfile, source.getSenderID(), cg.getRoleId());
		}
			
	}


	/**
	 * Generates a new game ID
	 * @return The new gameID
	 */
	private String newGameID() {
		gameIDCounter ++;
		return Integer.toString(gameIDCounter);
	}

	/**
	 * Called when a joinGame message is processed
	 * @param e The join game message
	 */
	private void joinGame(Element e) {
		JoinGame jg = (JoinGame)e; //convert the XML element to a joingame message
		String gID = jg.getGameId(); //get the game ID
		String rID = jg.getRoleId(); //and the roleID 
		EDXLDistribution source = (EDXLDistribution) e.getDocument().getRoot();
		String userName = source.getSenderID();  //get the username who sent it
		String sID = mapGames.get(gID).getScenarioID();
		
		assignRole(gID, rID, userName, sID);
	}
	
	/**
	 * Assigns a role in a given game
	 * @param gID The game ID 
	 * @param rID The role ID
	 * @param userName The username that is taking the role
	 * @param sID the scenario ID.
	 */
	private void assignRole(String gID, String rID, String userName, String sID){	
		//check if we can add them
		log.info("Role of " +  rID + " in "+ gID + " requested by " + userName);
		if(mapGames.get(gID).addPlayer(userName, rID)){
			EDXLDistribution edxl = esb.makeEdxl();
			RoleFilled rf = esb.attachElement(edxl, EREN_ROLEFILLED);
			rf.setRoleId(rID);
			rf.setUsername(userName);
			rf.setGameId(gID);
			edxl.addSenderRole(urnGID, lby); //send it out to the lobby 
			esb.send(edxl);
			log.info("Giving role of " + rID + " in " + gID + " to " +  userName);
			lobby.removeUser(userName);
			messageLobby(updateGameList());
			
		}else{  //Reject the role because it has already been filled
			EDXLDistribution edxl = esb.makeEdxl();
			RoleDenied rd = esb.attachElement(edxl, EREN_ROLEDENIED);
			rd.setRoleId(rID);
			rd.setUsername(userName);
			edxl.addSenderRole(urnGID, lby); 
			esb.send(edxl);
			log.info("!!!!ROLE REJECTED" );
		}

		//check the game state to see if we have enough players to kick off the game
		if (mapGames.get(gID).getGameState()== GAMESTATE.CRITICAL_MASS){

			log.info("Enough users, sending SENDING message");
			EDXLDistribution edxl = esb.makeEdxl();
			Sending sd = esb.attachElement(edxl, EREN_SENDING);
			sd.setFileName(idToScenario.get(sID).getName());			
			sd.setFileSize(scenarioFileSize.get(sID).toString());
			esb.send(edxl);

			edxl = esb.makeEdxl();
			ERENscenario scene = idToScenario.get(sID);

			Parser p = factory.newParser();
			StringWriter sw = new StringWriter();
			try {

				scene.writeTo(sw);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.info("sending scenario File");
			StringReader sr = new StringReader(sw.toString());
			Document<ERENscenario> doc = p.parse(sr);
			esb.attachElement(edxl, doc.getRoot());
			edxl.addSenderRole(urnGID, gID); 
			esb.send(edxl);


                        String actionsFileName = scene.getActionsFile();
                        if (actionsFileName != null) {
                            log.info("sending actions file");
                            edxl = esb.makeEdxl();
                            try {
                                File f = new File(actionsFileName);
                                p = factory.newParser();
                                Document<ActionsFile> afdoc = p.parse(new FileInputStream(f));
                                esb.attachElement(edxl, afdoc.getRoot());
                                edxl.addSenderRole(urnGID, gID);
                                esb.send(edxl);
                            } catch (IOException e1) {
                                log.log(Level.WARNING, "Error parsing actions file", e1.fillInStackTrace());
                                e1.printStackTrace();
                            }
                        } else {
                            log.info("no actions file");
                        }
		}
	}

	/**
	 * Creates an updated list of Active games  
	 * @return a gamelist message wraped in EDXL.
	 */
	private EDXLDistribution updateGameList(){
		EDXLDistribution edxl = esb.makeEdxl();
		GameList gl = esb.attachElement(edxl, EREN_GAMELIST);

//		Map<String, GameData> fakeGames = new HashMap<String, GameData>(mapGames);
//		
//		fakeGames.put("FAKEGAME1", new GameData(gID, name, scenario, logfile))
		
		//over the list of  active games
		for (Entry<String,GameData> entry : mapGames.entrySet()){
			//grab all the information
			Game game = gl.addGame();
			game.setID(entry.getKey());
			game.setName(entry.getValue().getName());
			//ALL of this this is needed to copy the scenario, it really should go in it's own function
			ERENscenario tempScenario = game.addScenario(); //get a scenario to add
			ERENscenario scenario = entry.getValue().getScenario(); //get the scenario to copy

			tempScenario.setID(scenario.getID());
			tempScenario.setDescription(scenario.getDescription());
			tempScenario.setName(scenario.getName());
			tempScenario.setBaseUrl(scenario.getBaseUrl());
			tempScenario.setImage(scenario.getImage());

			log.info("building roles");
			Roles oldRoles = scenario.getRoles();
			Roles newRoles = tempScenario.addRoles();
			for (Role oldRole : oldRoles.getRoles()) {
				Role newRole = newRoles.addRole();
				newRole.setID(oldRole.getID());
				newRole.setTitle(oldRole.getTitle());
				newRole.setDescription(oldRole.getDescription());
				//log.info(oldRole.getBriefing());
				newRole.setBriefing(oldRole.getBriefing());
				newRole.setImage(oldRole.getImage());
			}

			log.info("buliding location");
			Parser p = factory.newParser();
			StringWriter sw = new StringWriter();
			try {
				scenario.getScenarioLocation().writeTo(sw);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			StringReader sr = new StringReader(sw.toString());
			Document<ScenarioLocation> doc = p.parse(sr);
			tempScenario.addExtension(doc.getRoot());

			sw = new StringWriter();
			try {
				scenario.getTiming().writeTo(sw);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.info("building timing");
			sr = new StringReader(sw.toString());
			Document<Timing> doc2 = p.parse(sr);
			tempScenario.addExtension(doc2.getRoot());			
		
			//get a list of roles in each game and how many people are in them			
			ActiveRoles ars = game.addActiveRoles();			
			for(Entry<String, Integer> entry2 : entry.getValue().getRoleCount().entrySet()){
				ActiveRole ar = ars.addRole(); 
				ar.setRoleId(entry2.getKey());
				int min = scenarioRoleList.get(entry.getValue().getScenarioID()).get(entry2.getKey()).getMin();
				int max = scenarioRoleList.get(entry.getValue().getScenarioID()).get(entry2.getKey()).getMax();
				ar.setMax(max);
				ar.setMin(min);
				ar.setNumFilled(entry2.getValue());
			}				
		}
		edxl.addSenderRole(urnGID, lby); 
		//demoWare(gl);
		return edxl;
	}//end update game list

	/**
	 * Called when a login is prossed. It responds when the active game list
	 * @param e the login message
	 */
	private void logIn(Login e) {
		lobby.addUser(e.getUsername());
		EDXLDistribution edxl = updateGameList();
		edxl.addExplicitAddress("eren:username",e.getUsername());
		edxl.addSenderRole(urnGID, lby); 
		esb.send(edxl);
	}

	/**
	 * Handles log out messages. The effectively should end the game
	 * @param e the logout message
	 */
	private void logOut(Logout e){
		//Get the username and gameID
		String uname = e.getUsername(); 
		String gid = extractGID(e);
		if(mapGames.containsKey(gid)){
			GameData game = mapGames.get(gid);
			if(game.removePlayer(uname)){
				log.info(e.getUsername() + " has left the game: " + gid);
				log.info("Ending game: " + gid);
				EDXLDistribution edxl = esb.makeEdxl();
				GameOver go = esb.attachElement(edxl, EREN_GAMEOVER);
				go.setGameId(gid);
				esb.send(edxl);
			}else{
				log.info("Tried to remove " + e.getUsername() + " but couldn't find them");
			}
			//since the game has ended lets not care about it anymore
			mapGames.remove(gid);
		}else{
			log.info("tried to remove a player from a game that doesn't exist");
		}
	}

	//pull the GID out of a message, where ever it is...
	//or fake it for now
	private String extractGID(Element e) {
		EDXLDistribution edxl = (EDXLDistribution) e.getDocument().getRoot();
		String gID = edxl.getSenderRole(urnGID);
		if (gID == null){
			log.warning("message doe not contain GID");
			//return null;
			return fakeGameID;
		}
		else{
			return gID;
		}
	}


	/**
	 * Process a client ready message. This lets the game know a client is ready.
	 * Once the game has been alerted the client is ready, this method checks  
	 * the game state. If the gamestate is ReadyToStart then it kicks off the game
	 * with the proper series of messages
	 * @param e
	 */
	private void clientReady(ClientReady e) {
		EDXLDistribution edxl = (EDXLDistribution) e.getDocument().getRoot();
		String gID = extractGID(edxl); 
		GameData game = mapGames.get(gID);
		ERENscenario scene = game.getScenario();
		game.clientReady(edxl.getSenderID());
		if(game.getGameState().equals(GAMESTATE.READY_TO_START)){
			game.Start();	
			log.info("Starting game from client ready");
			//Send out the game start sequence including setting and sycing the
			//clock
			EDXLDistribution edxl2 = esb.makeEdxl();
			GameStart gs = esb.attachElement(edxl2, EREN_GAMESTART);
			edxl2.addSenderRole(urnGID, gID); 
			esb.send(edxl2);
			edxl2 = esb.makeEdxl();						
			Timing d = scene.getTiming();			
			Integer gr = Integer.parseInt(d.getRatio());

			log.info("Syncing clock time to " + d.getDate());
			SetClocksync cs = esb.attachElement(edxl2, EREN_SETCLOCKSYNC);
			cs.setDate(d.getDate());
			SetClockspeed ratio = esb.attachElement(edxl2, EREN_SETCLOCKSPEED);
			ratio.setRatio(gr);
			edxl2.addSenderRole(urnGID, gID); 
			esb.send(edxl2);

			log.info("Clock reset");
			edxl2 = esb.makeEdxl();
			ClockresetWrapper  cr = esb.attachElement(edxl2, EREN_CLOCKRESET);
			edxl2.addSenderRole(urnGID, gID); 
			esb.send(edxl2);

			log.info("Clock go");
			edxl2 = esb.makeEdxl();
			ClockstartWrapper csw = esb.attachElement(edxl2, EREN_CLOCKSTART);
			edxl2.addSenderRole(urnGID, gID); 
			esb.send(edxl2);			
		}
		else if (game.getGameState().equals(GAMESTATE.RUNNING)){ //Game is already running or defunct. For now lets assume running
			log.info("Sending directly to recepient");
			EDXLDistribution edxl1 = esb.makeEdxl();
			GameStart gs = esb.attachElement(edxl1, EREN_GAMESTART);
			edxl1.addExplicitAddress("eren:username",edxl.getSenderID());
			edxl1.addSenderRole(urnGID, gID); 
			esb.send(edxl1);
		}
	}
	


	/**
	 * Send a list of scenarios to a single users 
	 */
	private void sendList(Element e) {
		EDXLDistribution incomeingEdxl = (EDXLDistribution) e.getDocument().getRoot();
		//List<ValueScheme> addresses = edxl.getExplicitAddresses();
		String username = incomeingEdxl.getSenderID();
		log.info(username+" requsted the scenario List");
		EDXLDistribution edxl = esb.makeEdxl();
		ScenarioList sl = esb.attachElement(edxl, EREN_SCENARIOLIST);
		log.info("Building scenarioList");
		for (ERENscenario scenario : idToScenario.values()) {
			ERENscenario tempScenario = sl.addScenario();

			//tempScenario = factory.newExtensionElement(EREN_SCENARIO);
			tempScenario.setID(scenario.getID());
			tempScenario.setDescription(scenario.getDescription());
			tempScenario.setName(scenario.getName());
			tempScenario.setBaseUrl(scenario.getBaseUrl());
			tempScenario.setImage(scenario.getImage());

			log.info("building roles");
			Roles oldRoles = scenario.getRoles();
			Roles newRoles = tempScenario.addRoles();
			for (Role oldRole : oldRoles.getRoles()) {
				Role newRole = newRoles.addRole();
				newRole.setID(oldRole.getID());
				newRole.setTitle(oldRole.getTitle());
				newRole.setDescription(oldRole.getDescription());
				//log.info(oldRole.getBriefing());
				newRole.setBriefing(oldRole.getBriefing());
				newRole.setImage(oldRole.getImage());
			}

			log.info("buliding location");
			Parser p = factory.newParser();
			StringWriter sw = new StringWriter();
			try {
				scenario.getScenarioLocation().writeTo(sw);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			StringReader sr = new StringReader(sw.toString());
			Document<ScenarioLocation> doc = p.parse(sr);
			tempScenario.addExtension(doc.getRoot());

			sw = new StringWriter();
			try {
				scenario.getTiming().writeTo(sw);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.info("building timing");
			sr = new StringReader(sw.toString());
			Document<Timing> doc2 = p.parse(sr);
			tempScenario.addExtension(doc2.getRoot());			
		}

		log.info("adding explicit address");
		edxl.addExplicitAddress("eren:username",username);
		edxl.addSenderRole(urnGID, lby); 
		esb.send(edxl);
		log.info("sending");
	}//end SendList

	/**
	 * Creates a list of scenarios that are sent out
	 */
	public void getScenarioList(){		
		List<ERENscenario> listOfScenarios = new ArrayList<ERENscenario>();
		File dir = new File(scenarioDir);
		String [] files = dir.list(); 

		for (int i = 0 ; i < files.length; i++){
			if (files[i].endsWith(".xml")){
				try {
					log.info("Processing Scenerio File: " + files[i]);
					File f = new File(dir, files[i]);
					Parser p = factory.newParser();
					Document <ERENscenario> doc = p.parse(new FileInputStream(f));

					ERENscenario scenario = doc.getRoot();
					listOfScenarios.add(scenario);
					idToScenario.put(scenario.getID(),scenario);
		//			Set<String> s = new HashSet<String>() ;

					scenarioFileSize.put(scenario.getID(), f.length());
					Map<String,Role> roleMap = new HashMap<String,Role>();				
					scenarioRoleList.put(scenario.getID(), roleMap);
					Map<String,Integer> roleCount = new HashMap<String, Integer>();

					List<Role> roles = scenario.getRoles().getRoles();
					for (Role role : roles) {
						roleMap.put(role.getID(), role);
						roleCount.put(role.getID(), 0);						
					}					
				} catch (org.apache.abdera.parser.ParseException e) {

					log.warning("Parse Exception");
					log.warning(e.getStackTrace().toString());
					continue;

				} catch (FileNotFoundException e) {
					log.warning("File Not Found Exception");
					log.warning(e.getStackTrace().toString());
					continue;
				}
				
			}			
		}		
		log.info("Scenarios: " + idToScenario.size());		
	}//End getScenarioList
	
	private void demoWare(GameList gl) {
		int index = 0;
		for (Iterator<ERENscenario> iterator = idToScenario.values().iterator(); iterator.hasNext();) {
			ERENscenario es = (ERENscenario) iterator.next();	

			Game g = 	gl.addGame();
			g.setID("game" + index);
			g.setName(es.getName());
			ERENscenario s = g.addScenario();
			s.setID(es.getID());
			s.setDescription(es.getDescription());


			s.setName(es.getName());
			s.setBaseUrl(es.getBaseUrl());
			s.setImage(es.getImage());

			Roles oldRoles = es.getRoles();
			Roles newRoles = s.addRoles();
			ActiveRoles ars = g.addActiveRoles();	
			for (Role oldRole : oldRoles.getRoles()) {
				Role newRole = newRoles.addRole();
				newRole.setID(oldRole.getID());
				newRole.setTitle(oldRole.getTitle());
				newRole.setDescription(oldRole.getDescription());
				//log.info(oldRole.getBriefing());
				newRole.setBriefing(oldRole.getBriefing());
				newRole.setImage(oldRole.getImage());
				ActiveRole ar = ars.addRole();
				ar.setRoleId(newRole.getID());
				int x = scenarioRoleList.get(s.getID()).get(oldRole.getID()).getMin();
				ar.setMin(x);
				ar.setMax(x);
				ar.setNumFilled(x);
			}
			index++;
			Parser p = factory.newParser();
			StringWriter sw = new StringWriter();
			try {
				es.getScenarioLocation().writeTo(sw);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			StringReader sr = new StringReader(sw.toString());
			Document<ScenarioLocation> doc = p.parse(sr);
			s.addExtension(doc.getRoot());

			sw = new StringWriter();
			try {
				es.getTiming().writeTo(sw);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.info("building timing");
			sr = new StringReader(sw.toString());
			Document<Timing> doc2 = p.parse(sr);
			s.addExtension(doc2.getRoot());		

		}


	}

	/**
	 * Sends a message to all players who are in a lobby.
	 * @param edxl The edxl message you want to send to all players
	 */
	private void messageLobby(EDXLDistribution edxl){
		Set<String> users = lobby.getSetOfUsers();
		for (Iterator<String> iterator = users.iterator(); iterator.hasNext();) {
			String user = iterator.next();
			edxl.addExplicitAddress("eren:username",user);
		}
		edxl.addSenderRole("eren::gameID", lby);
		esb.send(edxl);
	}
	
	/**
	 * Preferred way of creating a game. it creates a game that assigns a user (username) to a role (roleID
	 * @param gID The GameID for creating the game
	 * @param sID The scenarioID for the game
	 * @param logfile Location of the logfile 
	 * @param username The first user in the game
	 * @param roleID The role the first user is assigned
	 * @param name 
	 */
	private void createNewGame(String gID, String name, String sID, String logfile, String username, String roleID ){
		if(!mapGames.containsKey(gID)){
			EDXLDistribution edxl = esb.makeEdxl();
			GameCreated gc = esb.attachElement(edxl, EREN_GAMECREATED);
			gc.setGameId(gID);
			gc.setScenarioId(sID);
			esb.send(edxl);
			GameData gd = new GameData(gID, name, idToScenario.get(sID), logfile);
			mapGames.put(gID, gd);
			assignRole(gID, roleID, username, sID);
		}
		else{
			log.info("Someone tried to create a game that already exists! gameID is: " + gID);
		}
		//update lobby with new game;
		messageLobby(updateGameList());
	}
	
	/**
	 * It's main
	 * @param args
	 */
	public static void main(String[] args) {
		ScenarioManager sm = new ScenarioManager(args);
	}


}
