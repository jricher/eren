package org.mitre.eren.ScenarioManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Lobby {
	private Set<String> setOfUsers; //list of users
	private static Logger log ; //the log for things
	
	/**
	 * Default constructor
	 * @param logfile The location of the logfile
	 */
	public Lobby(String logfile) {
		
		this.setOfUsers = new HashSet<String>();
		FileHandler fh;
		try {
			fh = new FileHandler(logfile + System.getProperty("file.separator")+"lobby.log", 10000000, 5);
			log = Logger.getLogger(ScenarioManager.class.getName());
			log.addHandler(fh);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The set of users that are in the lobby and not in the game
	 */
	public Set<String> getSetOfUsers() {
		return setOfUsers;
	}
	
	/**
	 * Adds a user to the Lobby
	 * @param user The username being added to the lobby
	 */
	public void addUser(String user){
		setOfUsers.add(user);
		log.info("added " + user + " to Lobby");
	}
	
	/**
	 * Removes a user to the lobby. This should mean they quit, or were put into a game.
	 * @param user The user that is being removed.
	 */
	public void removeUser(String user) {
		this.setOfUsers.add(user);
		log.info("removed " + user + " from lobby");
	}


}
