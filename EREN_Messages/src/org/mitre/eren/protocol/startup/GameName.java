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
public class GameName extends BaseWrapper implements StartupConstants,
		ERENConstants, ScenarioConstants {

	/**
	 * @param internal
	 */
	public GameName(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public GameName(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}

}
