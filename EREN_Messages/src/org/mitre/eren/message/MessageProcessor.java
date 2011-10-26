package org.mitre.eren.message;

import java.util.List;

import org.apache.abdera.factory.ExtensionFactory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.edxl.ValueList;

/** 
 * This is the main MessageProcessor class. 
 */

public interface MessageProcessor {
	
	
	/**
	 * Process a single element inside of an EDXL message's contentObject/embeddedXMLContent container
	 * @param e The XML element
	 * @param gameID The game ID that this message is for
	 * @param sender The author of the message
	 * @param roles The roles the message is addressed to
	 * @param userID The userIDs the message is addressed to
	 */
	public void processMessage(Element e, String gameID, String sender, List<String> roles, List<String> userID);

	/**
	 * Get a list of extensions to install in the handler that calls us.
	 * @return
	 */
	public <T extends ExtensionFactory> List<T> getExtensions();
}

