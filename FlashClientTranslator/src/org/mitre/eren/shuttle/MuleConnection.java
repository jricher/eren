package org.mitre.eren.shuttle;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.HashSet;
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
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.clock.ClockExtensionFactory;
import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.mitre.eren.protocol.dialogue.DialogueExtensionFactory;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.mitre.eren.protocol.edxl_rm.EDXLRMExtensionFactory;
import org.mitre.eren.protocol.scenario.ScenarioConstants;
import org.mitre.eren.protocol.startup.CreateGame;
import org.mitre.eren.protocol.startup.GameCreated;
import org.mitre.eren.protocol.startup.Login;
import org.mitre.eren.protocol.startup.Logout;
import org.mitre.eren.protocol.startup.Message;
import org.mitre.eren.protocol.startup.RoleFilled;
import org.mitre.eren.protocol.startup.SendToBus;
import org.mitre.eren.protocol.startup.StartupConstants;
import org.mitre.eren.protocol.startup.StartupExtensionFactory;
import org.opencare.lib.model.edxl.EDXLDistribution;
import org.opencare.lib.model.edxl.ValueScheme;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.ConnectionUtils;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.Server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class MuleConnection implements MessageProcessor, ClockConstants,
		IDataHandler, DialogueConstants, StartupConstants, ScenarioConstants,
		EDXLRMConstants, IDisconnectHandler {

	public static void main(String[] args) {
		new MuleConnection(args);
	}
	private OutboundHttpEndpoint esb; // How to send to the ESB
	private InboundHttpEndpoint server; // How get messages from the ESB.
	private static Server srv; // how we talk to flash
	private int serverPort;
	private int busPort;
	private String policyFile;
	private static Logger log;
	private String endpoint = "http://erenbus.mitre.org:3732/";
	private String logfile;
	private Map<String, UserMapping> userMap;
	private final String fakeGameID = "1";
	private final String lobby = "Lobby";
	private final String urnGID = "eren:GameID";

	public MuleConnection(String[] args) {
		try {
			Options options = new Options();
			options.addOption(
					"p",
					"port",
					true,
					"Server port to listen on for incoming HTTP messages from Flash client XMLSockets");
			options.addOption("h", "help", false, "Prints this help message");
			options.addOption("b", "bus", true, "Port to listen to the bus");
			options.addOption("f", "file", true, "Policy file Path");
			options.addOption("u", "url", true,
					"Outbound URL to post HTTP messages to");
			options.addOption("l", "logFile", true, "path to Logfile");

			CommandLineParser parser = new PosixParser();
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("l")) {
				logfile = cmd.getOptionValue("l");
				FileHandler fh;
				try {
					fh = new FileHandler(logfile + "proxy%g.log", 10000000, 5);
					log = Logger.getLogger(MuleConnection.class.getName());
					log.addHandler(fh);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (cmd.hasOption("h")) {
				HelpFormatter hf = new HelpFormatter();
				hf.printHelp("Shuttle.jar", options);
				System.exit(1);
			}
			if (cmd.hasOption("p")) {
				serverPort = Integer.parseInt(cmd.getOptionValue("p"));
			}
			if (cmd.hasOption("b")) {
				busPort = Integer.parseInt(cmd.getOptionValue("b"));
				log.info("Incomeing Bus port is " + busPort);
			}
			if (cmd.hasOption("f")) {
				policyFile = cmd.getOptionValue("f");
			}
			if (cmd.hasOption("u")) {
				endpoint = cmd.getOptionValue("u");
			}

		} catch (ParseException e) {
			log.severe(e.getMessage());
			System.exit(1);
		}

		userMap = new HashMap<String, UserMapping>();
		createNewGame(lobby);
		// TODO remove once we create real games.
		//createNewGame(fakeGameID);

		log.setLevel(Level.FINE);
		// Create an incoming connection with the ESB
		log.info("Creating inbound connection on port " + busPort);
		server = new InboundHttpEndpoint(busPort, new InboundMessageHandler(
				this));
		Thread st = new Thread(server, "EREN HTTP Inbound Endpoint");
		st.start();
		log.info("Finished creating inbound connection");

		// Create an outgoing connection with the ESB.
		log.info("Creating outgoing connection to MULE ");
		esb = new OutboundHttpEndpoint(endpoint);
		esb.registerExtension(new ClockExtensionFactory());
		esb.registerExtension(new StartupExtensionFactory());
		esb.registerExtension(new DialogueExtensionFactory());
		esb.registerExtension(new EDXLRMExtensionFactory());
		log.info("Finished outgoing connection to MULE");

		log.info("Creating Maps");
		// userToConnection = new
		// ConcurrentHashMap<String,INonBlockingConnection>();
		// userToConnection = HashBiMap.create();
		// roleToUser = new ConcurrentHashMap<String, Set<String>>();
		// connectionToUser = new
		// ConcurrentHashMap<INonBlockingConnection,String>();
		log.info("finished Creating Maps");

		// Start a xsocket server to connect to flash
		log.info("Starting xsocket server");
		try {
			srv = new Server(serverPort, this);
			srv.run();
		} catch (Exception ex) {
			log.severe(ex.getMessage());
		}
		log.info("Started xsocket server");

	}// End of MuleConnection()

	// Data from flash going to bus

	/**
	 * Validates the XML we have received and then puts it in a 'send to bus'
	 * package. Wraps it up in an EDXL message and sends it out.
	 * 
	 * @param s
	 *            The message that will be processed from the connection
	 * @param con
	 *            The connection the string game from
	 */
	public synchronized void clientToMule(String s, INonBlockingConnection con) {
		log.info("client to mule with message: " + s);
		Abdera abdera = new Abdera();
		Factory factory = abdera.getFactory();
		factory.registerExtension(new org.opencare.lib.model.ExtensionFactory());

		List<ExtensionFactory> extensions = getExtensions();
		for (ExtensionFactory ef : extensions) {
			factory.registerExtension(ef);
		}
		Parser parser = factory.newParser();
		Document<Message> document = parser.parse(new StringReader(s));
		SendToBus stb = document.getRoot().getSendToBus();
		List<Element> l = stb.getElements();
		String uname = null;
		for (Entry<String, UserMapping> e : userMap.entrySet()) {
			uname = e.getValue().getUserFromConnection(con);
			if (uname != null) {
				break;
			}
		}
		for (Element element : l) {
			// Rebroadcast everything we get from the client
			EDXLDistribution edxl = esb.makeEdxl();
			esb.attachElement(edxl, element);
			if (uname != null) {
				edxl.setSenderID(uname);
				edxl.addSenderRole(urnGID, findGIDFromConnection(con));
			}
			esb.send(edxl);

			if (element.getQName().equals(EREN_LOGIN)) {
				// TODO validate password
				Login login = (Login) element;
				String username = login.getUsername();
				userMap.get(lobby).addUserConnection(username, "user", con);
				// userToConnection.put(username, nbc);
				// connectionToUser.put(nbc, username);
				log.info(username + " Just loged in");

				// send scenario list request on login
				// TODO don't make this static
				edxl = esb.makeEdxl();
				edxl.setSenderID(username);
				edxl.addSenderRole(urnGID, findGIDFromConnection(con));
				esb.send(edxl);
			}
		}
	}

	// Boilerplate for message processing
	@Override
	public <T extends ExtensionFactory> List<T> getExtensions() {
		// return (List<T>) Collections.singletonList(new
		// ClockExtensionFactory());
		List<T> efs = new ArrayList<T>();
		efs.add((T) new ClockExtensionFactory());
		efs.add((T) new StartupExtensionFactory());
		efs.add((T) new DialogueExtensionFactory());
		efs.add((T) new EDXLRMExtensionFactory());
		return efs;
	}

	/**
	 * This function validates the XML and sends it out to the list of
	 * connections provided
	 * 
	 * @param e
	 *            The message to be sent from the bus to a set of connections
	 * @param cons
	 *            The list of connections e should be sent to
	 */
	public void muleToClient(Element e, Set<INonBlockingConnection> cons) {
		// Send the message out to all clients
		StringWriter sw = new StringWriter();
		try {
			e.writeTo(sw);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		String msg = sw.toString();
		msg = msg.concat("\0");
		Iterator<INonBlockingConnection> iter = cons.iterator();
		while (iter.hasNext()) {
			try {
				INonBlockingConnection connection = iter.next();
				// log.info("MuleToClient: Sending message to " +
				// userToConnection.inverse().get(connection) +
				// "\n message is: ||"+ msg + "||");

				// make this connection call threadsafe
				connection = ConnectionUtils.synchronizedConnection(connection);

				connection.write(msg);
				connection.flush();

			} catch (BufferOverflowException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public synchronized boolean onData(INonBlockingConnection con)
			throws IOException, BufferUnderflowException,
			MaxReadSizeExceededException {

		String res = con.readStringByDelimiter("\0");
		res = res.trim();
		if (res.equals("<policy-file-request/>")) {
			log.fine("Policy file requsted");
			con.write("<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"8090\"/></cross-domain-policy>\0");
		} else { // It is not requesting a policy file...
			clientToMule(res, con);
		}
		return true;
	}

	/**
	 * Called when someone disconnects
	 */
	@Override
	public boolean onDisconnect(INonBlockingConnection con) throws IOException {
		String uname = null;
		if (userMap == null) {
			log.info("UserMap is null");
		}
		if (userMap.entrySet() == null) {
			log.info("entrySetis null");
		}

		for (Entry<String, UserMapping> e : userMap.entrySet()) {
			uname = e.getValue().getUserFromConnection(con);
			if (uname != null) {
				e.getValue().removeConnection(con);

				log.info("******Removing : " + uname);
				log.info("Sending logout message");
				EDXLDistribution edxl = esb.makeEdxl();
				Logout lo = esb.attachElement(edxl, EREN_LOGOUT);
				lo.setUsername(uname);
				esb.send(edxl);
			}
		}
		return true;
	}

	// Process the incoming (from mule) messages me we care about.
	// This filters out messages the client doesn't know or care about.
	@Override
	public synchronized void processMessage(Element e) {
		//String gameID = extractGID(e);
		Set<INonBlockingConnection> conns = filterConnections(e);
		log.info("Sending message " + e.getQName() + " to " + conns);
		if (conns != null && !conns.isEmpty()) {
			if (e.getQName().equals(EREN_CLOCKTICK)) {
				muleToClient(e, conns);
				log.finest("Clocktick");
			} else if (e.getQName().equals(EREN_CLOCKSYNC)) {
				muleToClient(e, conns);
				log.finest("clockSync");
			} else if (e.getQName().equals(EREN_CLOCKSPEED)) {
				muleToClient(e, conns);
				log.finest("clockspeed");
			} else if (e.getQName().equals(DLG_USERMESSAGE)) {
				log.finest("dialog");
				// sendDialog(e);
				muleToClient(e, conns);
			} else if (e.getQName().equals(EREN_ROLEFILLED)) {
					muleToClient(e, conns);
					putRoleToUser(e);					
			} else if (e.getQName().equals(EREN_SCENARIOLIST)) {
				muleToClient(e, conns);
			} else if (e.getQName().equals(EREN_GAMESTART)) {
				muleToClient(e, conns);
				log.info("GAMESTART");
			} else if (e.getQName().equals(EREN_SENDING)) {
				muleToClient(e, conns);
			} else if (e.getQName().equals(EREN_ROLEDENIED)) {
				muleToClient(e, conns);
			} else if (e.getQName().equals(EREN_SCENARIO)) {
				muleToClient(e, conns);
			} else if (e.getQName().equals(EREN_SCORE)) {
				muleToClient(e, conns);
			} else if (e.getQName().equals(EREN_PODSTATUS)) {
				muleToClient(e, conns);
			} else if (e.getQName().equals(RM_REPORTRESOURCEDEPLOYMENTSTATUS)) {
				muleToClient(e, conns);
			} else if (e.getQName().equals(EREN_GAMELIST)) {
				muleToClient(e, conns);
			} else if (e.getQName().equals(EREN_GAMECREATED)){
				GameCreated gc = (GameCreated) e;
				createNewGame(gc.getGameId());
				muleToClient(e,conns);
			}
			

			// else if (e.getQName().equals(arg0))

		}
	}

	/**
	 * Creates a new game with the ID of gameID
	 * 
	 * @param gID
	 *            the ID of the game you want to create. Eventually this will be
	 *            created by...soemone else someplace.
	 * 
	 */
	private void createNewGame(String gID) {
		userMap.put(gID, new UserMapping(gID));

	}

	/**
	 * 
	 * @param e
	 *            the element of the message you want to extract the game ID
	 *            from
	 * @return The game ID. For now it is fakeGameID untill we finish implmented
	 *         game IDs
	 */
	private String extractGID(Element e) {
		EDXLDistribution edxl = (EDXLDistribution) e.getDocument().getRoot();
		String gid = edxl.getSenderRole(urnGID);
		if (gid == null) {
			log.warning("message dose not contain GID! message is " + e );
			return null;
			//return fakeGameID;
		} else {
			return gid;
		}
	}

	/**
	 * This method creates a set of connections that the message shold be sent
	 * to Currently if the message is not addressed to anyone, than it is sent
	 * to everyone
	 * 
	 * @param e
	 *            The edxl message you are routing. This message will have all
	 *            routing information in it.
	 * @param gID
	 *            The game from which this message is addressed
	 * @return a set of connections to send the message to
	 */
	private Set<INonBlockingConnection> filterConnections(Element e) {
		EDXLDistribution edxl = (EDXLDistribution) e.getDocument().getRoot();
		String gID = extractGID(e);
		List<ValueScheme> addresses = edxl.getExplicitAddresses();
		if (edxl.getSenderRole(urnGID)== null)
		{
			
			//return srv.getOpenConnections();
			log.warning("Message has no gID assuming it belongs to game: " +fakeGameID);
			gID = fakeGameID;
			
		}
		if (addresses.isEmpty()) {
			// no addresses, send to everybody
			UserMapping um = userMap.get(gID);
			if (um != null){
				return userMap.get(gID).getAllConnections();				
			}else {
				//we have a message with no gameID and no address
				//send to everyone
				return srv.getOpenConnections();
			}
			
		} else {
			Set<INonBlockingConnection> conns = new HashSet<INonBlockingConnection>();
			for (ValueScheme a : addresses) {
				// log.info("scheme = "+ a.getExplicitAddressScheme());
				// log.info("value = " +
				// a.getExplicitAddressValue().toString());

				if (a.getExplicitAddressScheme().equals("eren:role")) {
					String[] vals = a.getExplicitAddressValue();
					for (int i = 0; i < vals.length; i++) {
						// if (roleToUser.containsKey(vals[i])) {
						// log.info("FilterConnection: " +
						// roleToUser.get(vals[i]));
						// Set<String> users = roleToUser.get(vals[i]);
						// for (String u : users) {
						// conns.add(userToConnection.get(u));
						conns.addAll(userMap.get(gID).getConnectionsFromRole(vals[i]));
						// }
					}
				} else if (a.getExplicitAddressScheme().equals("eren:username")) {
					String[] vals = a.getExplicitAddressValue();
					for (int i = 0; i < vals.length; i++) {
						if (userMap.get(gID).getConnectionFromUser(vals[i]) != null) {
							log.info("sending message to " + vals[i]);
							conns.add(userMap.get(gID)
									.getConnectionFromUser(vals[i]));
						}
					}
				}
			}
			// DEBUG
			// log.fine("Users in connectionToUser are: " + connectionToUser);
			// log.fine("Users in connectionToUser are: " + userToConnection.);
			// log.fine("Users in userToConnection are: " + userToConnection);
			// log.fine("Role to user is: " + roleToUser);

			return conns;
		}
	}

	private String findGIDFromConnection(INonBlockingConnection con) {
		for (Entry<String, UserMapping> entry : userMap.entrySet()) {
			if (entry.getValue().getUserFromConnection(con) != null) {
				return entry.getKey();
			}
		}
		return null;

	}

	/**
	 * Assigns a role to a user in a game
	 * 
	 * @param e
	 *            The role assigned message
	 */
	private void putRoleToUser(Element e) {
//		String gID = extractGID(e);
		RoleFilled r = (RoleFilled) e;
		String user = r.getUsername();
		String role = r.getRoleId();
		UserMapping l = userMap.get(lobby);
		UserMapping um = userMap.get(r.getGameID());

		um.addUserConnection(user, role, l.getConnectionFromUser(user));
		l.removeUser(user);
		// Set<String> l = roleToUser.get(r.getUsername());
		// if (l == null){
		// l = new HashSet<String>();
		// roleToUser.put(role, l);
		// }
		// l.add(userName);
	}
}
