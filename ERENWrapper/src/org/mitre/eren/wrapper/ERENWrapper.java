package org.mitre.eren.wrapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.abdera.factory.ExtensionFactory;
import org.apache.abdera.model.Element;
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
import org.mitre.eren.protocol.action.ActionExtensionFactory;
import org.mitre.eren.protocol.clock.ClockExtensionFactory;
import org.mitre.eren.protocol.dialogue.DialogueExtensionFactory;
import org.mitre.eren.protocol.edxl_rm.EDXLRMExtensionFactory;
import org.mitre.eren.protocol.kml.KMLExtensionFactory;
import org.mitre.eren.protocol.scenario.ScenarioExtensionFactory;
import org.mitre.eren.protocol.startup.GameCreated;
import org.mitre.eren.protocol.startup.GameOver;
import org.mitre.eren.protocol.startup.StartupConstants;
import org.mitre.eren.protocol.startup.StartupExtensionFactory;
import org.mitre.eren.wrapper.WrappedERENModule;
import org.mitre.javautil.logging.LoggingUtils;
import org.opencare.lib.model.edxl.EDXLDistribution;

public class ERENWrapper implements MessageProcessor, StartupConstants {
	private static Logger log = Logger.getLogger(ERENWrapper.class.getName());; 
	private InboundHttpEndpoint server; //handles connections from the bus to the the module
	private Map<String, WrappedERENModule> idToWrapedObject; //maps gameIDs to modules
	private String logfile;  //where to write the log file
	private final String urnGID = "eren:GameID"; //I'm sure there is a better way to do this bus it assigns gameIS
	private String[] classArgs; //the arguments to pass to the class
	private String endpoint = "http://erenbus-tmlewis.mitre.org:3732/"; //where to send stuff

	private static String cls = null; 
	// the wrapped object to should an initializeWrapedObject method that thaes
	// a String[] as an argument. This transitions nicely into the constructor
	private static final String mth = "initalizeWrapedObject"; //the method called to get a new module

	public ERENWrapper(String[] args) {

		idToWrapedObject = new HashMap<String, WrappedERENModule>();
		classArgs = args; 

		// parse the arguments

		int serverPort = 3737;
		Options options = new Options();
		options.addOption("p", "port", true,
				"Server port to listen on for incoming HTTP messages from the bus");
		options.addOption("u", "url", true,
				"Outbound URL to post HTTP messages to");
		options.addOption("l", "logFile", true, "path to Logfile");
		options.addOption("c", "class", true, "Class path to wrap");

		try {
			CommandLineParser parser = new PosixParser();
			CommandLine cmd = parser.parse(options, args, true); 
			if (cmd.hasOption("p")) {
				serverPort = Integer.parseInt(cmd.getOptionValue("p"));
			}
			if (cmd.hasOption("u")) {
				endpoint = cmd.getOptionValue("u");
			}
			if (cmd.hasOption("l")) {
				logfile = cmd.getOptionValue("l");
				FileHandler fh;
				try {
					fh = new FileHandler(logfile + "proxy%g.log", 10000000, 5);
					
					log.addHandler(fh);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (cmd.hasOption("c")) { //This is the class that is to be wrapped
				cls = cmd.getOptionValue("c");
				log.info("using class: " + cls);
			} else {
				log.info("Need a class to wrap. Please use the -c option ");
				System.exit(0);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.log(Level.WARNING, "Caught Exception", e);
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp("ERENWrapper.jar", options);
			System.exit(1);
		}
		
		server = new InboundHttpEndpoint(serverPort, new InboundMessageHandler(
				this));
		Thread st = new Thread(server, "EREN HTTP Inbound Endpoint");
		st.start();
	}

	public static void main(String[] args) {
		new ERENWrapper(args);
	}

	@Override
	public void processMessage(Element e, String gameID, String sender,
			List<String> roles, List<String> userIds) {
		
		if (idToWrapedObject.containsKey(gameID)) {
			idToWrapedObject.get(gameID).getMessageProcessor()
					.processMessage(e, gameID, sender, roles, userIds);
		}
		if (e.getQName().equals(EREN_GAMEOVER)) {
			GameOver go = (GameOver) e;
			log.info("GameOver for: " + go.getGameId());
			idToWrapedObject.remove(go.getGameId());
		}

		else if (e.getQName().equals(EREN_GAMECREATED)) {
			GameCreated gc = (GameCreated) e;
			log.info("Createing game with gID: " + gc.getGameId());
			createNewGame(gc);
		}
	}

	private void createNewGame(GameCreated gc) {
		Class c;
		try {
			c = Class.forName(cls);
			Method m = c.getMethod(mth, String[].class);
			WrappedERENModule wrapedClass = (WrappedERENModule) m.invoke(c,
					(Object) classArgs);
			OutboundHttpEndpoint outtie = new OutboundHttpEndpoint(endpoint,
					gc.getGameId());
			wrapedClass.setOutboundEndpoint(outtie);
			idToWrapedObject.put(gc.getGameId(), wrapedClass);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public <T extends ExtensionFactory> List<T> getExtensions() {
		// TODO Auto-generated method stub
		List<T> efs = new ArrayList<T>();
		efs.add((T) new ClockExtensionFactory());
		efs.add((T) new StartupExtensionFactory());
        efs.add((T) new EDXLRMExtensionFactory());
        efs.add((T) new ActionExtensionFactory());
        efs.add((T) new DialogueExtensionFactory());
        efs.add((T) new ScenarioExtensionFactory());
        efs.add((T) new KMLExtensionFactory());
        
		return efs;
	}
}
