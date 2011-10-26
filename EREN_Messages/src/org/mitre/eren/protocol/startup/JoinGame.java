/**
 * 
 */
package org.mitre.eren.protocol.startup;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.mitre.eren.protocol.ERENConstants;
import org.mitre.eren.protocol.scenario.ScenarioConstants;
import org.opencare.lib.model.BaseWrapper;

/**
 * @author TMLEWIS
 *
 */
public class JoinGame extends BaseWrapper implements ERENConstants,
ScenarioConstants, StartupConstants {

	/**
	 * @param internal
	 */
	public JoinGame(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public JoinGame(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	public String getGameId() {
		return getSimpleExtension(EREN_GAMEID);
	}

	public void setGameId(String val) {
		if (val != null) {
			setElementText(EREN_GAMEID, val);
		} else {
			removeElement(EREN_GAMEID);
		}
	}
	public String getRoleId() {
		return getSimpleExtension(EREN_ROLEID);
	}

	public void setRoleId(String val) {
		if (val != null) {
			setElementText(EREN_ROLEID, val);
		} else {
			removeElement(EREN_ROLEID);
		}
	}

}
