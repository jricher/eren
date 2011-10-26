package org.mitre.eren.shuttle;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.xsocket.connection.INonBlockingConnection;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class UserMapping {
	private HashBiMap<String, INonBlockingConnection> userConnection;
	private Multimap<String, String> roleUsers;
	private String gID;
	private String name;
	private static Logger log;

	/**
	 * Creates a new usermap to keep track of connections
	 * @param gID Game ID
	 * @param name The name of the game
	 */
	public UserMapping(String gID) {
		userConnection = HashBiMap.create();
		roleUsers = TreeMultimap.create();
		this.gID = gID;
		//this.name = name;
		FileHandler fh;
		try {
			Calendar now = Calendar.getInstance();
			fh = new FileHandler(gID, 10000000, 5);
			log = Logger.getLogger(gID);
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
	 * Remove a connection from this game
	 * 
	 * @param con
	 *            The connection you are removing from the game
	 */
	public void removeConnection(INonBlockingConnection con) {
		if (userConnection.containsValue(con)) {
			userConnection.inverse().remove(con);
		} else {
			log.warning("Someone tried to remove a connection that did not exist in this game");
		}

	}

	/**
	 * Removes a user from a game
	 * 
	 * @param user
	 *            The user you are removing from the game
	 */
	public void removeUser(String user) {
		if (userConnection.containsKey(user)) {
			userConnection.remove(user);
			Collection<String> c = roleUsers.get(user);
			c.remove(user);
		}

		else {
			log.warning("Someone tried to remove a user in this game that did not exist");
		}
	}

	/**
	 * Adds a user to the game into a specific role. All users must have a role
	 * in a game We need three things to add a user. a user name, a role, and a
	 * connection
	 * 
	 * @param user
	 *            The user you are adding
	 * @param role
	 *            The role you are adding
	 * @param con
	 *            The connection to their Flash Client
	 */
	public void addUserConnection(String user, String role,
			INonBlockingConnection con) {
		userConnection.put(user, con);
		roleUsers.put(role, user);
		log.info("adding user " + user + "in role: " + role + " at connection "
				+ con);
	}

	/**
	 * Returns a connection from a given user
	 * 
	 * @param con
	 *            The connection you are lookng up
	 * @return The username in String form
	 */
	public String getUserFromConnection(INonBlockingConnection con) {
		String user = "";
		if (userConnection.containsValue(con)) {
			user = userConnection.inverse().get(con);
			log.info("User " + user + " requsted from " + con);
		} else {
			user = null;
		}
		return user;
	}

	/**
	 * If you have a user in this game, this will return his connection
	 * 
	 * @param user
	 *            The user you are lookng up
	 * @return The connections to the Flash client from the user
	 */
	public INonBlockingConnection getConnectionFromUser(String user) {
		log.info("connection user " + user + " requsted");
		if (userConnection.containsKey(user)) {
			return userConnection.get(user);
		}
		log.warning("CONNECTION FOR " + user + " NOT FOUND in game " + gID);
		return null;
	}

	/**
	 * Returns a set of users that occupie a role.
	 * 
	 * @param role
	 *            The role you want the users from
	 * @return a set of users in string from
	 */
	public Set<String> getUsersFromRole(String role) {

		HashSet<String> set = new HashSet<String>(roleUsers.get(role));
		log.info("set of usres for role: " + role + " requsted");
		log.info("set is: " + set.toString());
		return set;
	}

	/**
	 * Get connections from roles. A short circut way to get all the roles
	 * 
	 * @param role
	 *            The role you want the connections from
	 * @return At set of INonBlockingConnections
	 */
	public Set<INonBlockingConnection> getConnectionsFromRole(String role) {
		Set<String> set = getUsersFromRole(role);
		HashSet<INonBlockingConnection> cons = new HashSet<INonBlockingConnection>();
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String user = (String) iterator.next();
			cons.add(userConnection.get(user));
		}
		return cons;
	}
	public Set<INonBlockingConnection>getAllConnections(){
		return userConnection.values();
	}
}
