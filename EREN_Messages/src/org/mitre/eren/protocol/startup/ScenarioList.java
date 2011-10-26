/**
 * 
 */
package org.mitre.eren.protocol.startup;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.ScenarioConstants;

/**
 * @author jricher
 *
 */
public class ScenarioList extends BaseWrapper implements StartupConstants, ScenarioConstants {

	/**
	 * @param internal
	 */
	public ScenarioList(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public ScenarioList(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(ScenarioList.class);
	
	public List<ERENscenario> getScenarios() {
		return getExtensions(EREN_SCENARIO);
	}
	
	public ERENscenario addScenario() {
		return addExtension(EREN_SCENARIO);
	}
}
