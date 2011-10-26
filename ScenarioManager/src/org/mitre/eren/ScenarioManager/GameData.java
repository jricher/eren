package org.mitre.eren.ScenarioManager;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.Role;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class GameData {
	private Map <String ,Integer> roleCount; //Number of people in  the role currently
	private GAMESTATE gameState; 
	private String GID; //Game ID
	private BiMap <String,Role> roleMap; //a map of Role IDS to the roles
	private String scenarioID; //The scenario we're playing 
	private ERENscenario scenario; 
	private Multimap <String, String> roleToUser; //maps a role to a set of users.
	private static Logger log ; //TODO 
	private Map <String,Boolean> userReady; //a list of users and a mapping if they are  ready
	private Map <String,String> userToRole; //a map of users to role (one to one instead of one to many in roletouser)
	private String name;
	/**
	 * Constructor that also adds a player to a game
	 * @param gID The new game ID 
	 * @param scenario An ErenScenario that
	 * @param logfile The location of the logfile you want to use
	 * @param username The username of the first player you want to use
	 * @param roleID The role that 'username' is assinged.
	 * @param name The user generated name of the game.	  
	 */
	public GameData(String gID, String name, ERENscenario scenario, String logfile, String username, String roleID) {
		createGame(gID,name, scenario,logfile);
		addPlayer(username, roleID);
	}
	
	/**
	 * 
	 * @param gID The Game ID
	 * @param scenario The scenario you are using
	 * @param logfile The location of the logfile you want to use
	 */
	public GameData(String gID, String name, ERENscenario scenario, String logfile){
		createGame(gID,name,scenario,logfile);
	}
	
	/**
	 * Creates a game 
	 * @param gID The game ID
	 * @param name 
	 * @param scenario The scenario file that the game will be based off of
	 * @param logfile The location of the logfile you want to use
	 */
	private void createGame(String gID, String name2, ERENscenario scenario, String logfile){
		//super();
		GID = gID;
		this.name = name2;
		//set up the file handler for the logger
		//TODO set up better file handler nameing scheme
		FileHandler fh;
		try {
			//Calendar now = Calendar.getInstance();
			fh = new FileHandler(logfile +System.getProperty("file.separator") + gID+name2+".log" , 10000000, 5);
			log = Logger.getLogger(GameData.class.getName());
			log.addHandler(fh);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("Creating game with GID = " + GID);
		this.scenario = scenario;
		this.scenarioID = this.scenario.getID();
		log.info("set scenarioID to" + this.scenarioID);
		List<Role> roles = scenario.getRoles().getRoles();
		log = Logger.getLogger(GameData.class.getName());
		
		roleCount = new HashMap<String, Integer>();
		log.finest("Created roleCountMap");
		roleMap = HashBiMap.create();
		for (Role role : roles) {
			roleCount.put(role.getID(),0);
			roleMap.put(role.getID(), role);
		}		
		log.finest("Populated unfilled roles and role count");
		
		roleToUser = HashMultimap.create();
		log.finest("Created roleToUser");
		userToRole = new HashMap<String, String>();
		log.finest("Created userToRole");
		userReady = new HashMap<String, Boolean>();		
		log.finest("Created userReady");
		
		setGameState(GAMESTATE.WAITING_ON_PLAYERS);
		
		log.finest("gameState is: " + GAMESTATE.WAITING_ON_PLAYERS.toString());
	}//end constructor
	
	/**
	 * Starts a game right now it just switches the GAMESTATE variable
	 * @return true if successful, false otherwise 
	 */
	public boolean Start(){
		if (this.getGameState().equals(GAMESTATE.READY_TO_START)){
			setGameState(GAMESTATE.RUNNING);
			return true;
		}
		return false;
	}//end START
	
	/**
	 * This updates the state of the game and 
	 * @return true if the game is in a sane state, false otherwise
	 */
	public boolean checkState(){
		//Check if we have enough users
		for (Map.Entry<String, Role> entry : roleMap.entrySet()){
			String rid = entry.getKey();
			Role role = roleMap.get(rid);
			log.info("Veryifying " + rid);			
			if(!roleCount.containsKey(rid)){
				log.severe("Unsure of game state." + rid + " not found in roleCount");
				System.exit(-1);
			
			} else if(role.getMin() > roleCount.get(rid)) {
					log.info("minimum number for roleID: " + rid +  " not achived");
					return false;
			
			} else if(role.getMax() < roleCount.get(rid)) { //probably don't need this one but cycles are cheap
				log.severe("OVER MAXIMUM number for roleID: " + rid + " not achived");
				System.exit(-1);
			}
		}
		//if we do reach here  we've reached a critical mass of players. But we still need them to load the screen
		
		if((this.getGameState() != GAMESTATE.RUNNING) ||(this.gameState !=GAMESTATE.READY_TO_START) ) {
			setGameState(GAMESTATE.CRITICAL_MASS);
			log.info("have enough players for game");
		}
		//if the users are done loading their screen, let us know and then we can start
		if (!userReady.containsValue(false)){
			setGameState(GAMESTATE.READY_TO_START);
			log.info("Gamestate is running!!");
		}
		return true;
	}
	
	/**
	 * Adds a player in a role. It is impossible to add a player with out a role.
	 * @param user The username you are adding
	 * @param roleID The Role they are to be assinged
	 * @return true if successful. 
	 */
	public boolean addPlayer(String user, String roleID) {
		log.fine("Role: " + roleID + "requsted in " + this.GID.toString());
		//First sanity check to make sure role actually exists in this game.
		if (! roleMap.containsKey(roleID)){
			log.severe("WARNING: " + roleID + " is not in "+this.GID);
			return false;
		}
		//else grab the role, check if it's full. If not, add to the maps.
		Role myRole = roleMap.get(roleID);
		Integer max = myRole.getMax();
		Integer currentCount = roleCount.get(roleID);
		if((currentCount + 1 ) <= max ){ //we have not hit the max so we can keep adding
			roleToUser.put(roleID, user);
			roleCount.put(roleID,currentCount + 1);
			userToRole.put(user, roleID);
			userReady.put(user, false);
			checkState(); //update the state of the world			
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a player by username. This causes the GAMESTATE to change to STOP which is slightly undefined
	 * @param user The user you want to remove
	 * @return True if sucesful
	 */
	public boolean removePlayer(String user){ 
		//TODO //What does this mean in the context of the game? do we stop the game? do we check if 
		//there is the right number of players...
		if(userToRole.containsKey(user)){
			String rid = userToRole.get(user);
			roleToUser.remove(rid,user);
			log.info("removed " + user + " from roleToUser");
			Integer x = roleCount.get(rid);
			roleCount.put(rid,x - 1);
			log.info("removed " + user + " from roleCount");
			userReady.remove(user);
			log.info ("removed " + user + " from userReady");
			//Do we want to stop the game, pause the game...	
			setGameState(GAMESTATE.STOP);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Lets the game know a user is ready
	 * @param user The user that is ready
	 */
	public void clientReady(String user){
		userReady.put(user, true);	
		this.checkState();
	}

	/**
	 * Wraps the gamestate so it can be logged
	 * @param gameState
	 */
	private void setGameState(GAMESTATE gameState) {
		log.info("GID " + this.GID + " GAME STATE has changed to " + gameState );
		this.gameState = gameState;
		
	}

	/**
	 * 
	 * @return returns gamestate
	 */
	public GAMESTATE getGameState() {
		return gameState;
	}
	
	/**\
	 * 
	 * @return The scenario being used
	 */
	public ERENscenario getScenario() {
		return scenario;
	}
	
	/**
	 * 
	 * @return A Set of users in the game
	 */
	public Set<String> getListOfUsers(){
		return userToRole.keySet();
	}	
	
	/**
	 * 
	 * @return returns a map of the number of people in a role
	 */
	public Map<String, Integer> getRoleCount(){
		return roleCount;
		
	}
	
	/**
	 * 
	 * @return The scenario ID
	 */
	public String getScenarioID(){
		return this.scenarioID;
	}
	
	/**
	 * 
	 * @return The user generated name of the game.
	 */
	public String getName(){
		return this.name;
	}
}