/**
 * 
 */
package org.mitre.eren.protocol.startup;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.mitre.eren.protocol.ERENConstants;
import org.mitre.eren.protocol.scenario.ScenarioConstants;
import org.opencare.lib.model.BaseWrapper;

/**
 * @author AANGANES
 *
 */
public class GameList extends BaseWrapper implements StartupConstants,
		ScenarioConstants, ERENConstants {

	/**
	 * @param internal
	 */
	public GameList(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public GameList(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
    
	public List<Game> getGames() {
		return getExtensions(EREN_GAME);
	}
	
	public Game addGame() {
		return addExtension(EREN_GAME);
	}

}
