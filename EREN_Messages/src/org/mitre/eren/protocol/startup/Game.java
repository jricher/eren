/**
 * 
 */
package org.mitre.eren.protocol.startup;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.mitre.eren.protocol.ERENConstants;
import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.ScenarioConstants;
import org.opencare.lib.model.BaseWrapper;

/**
 * @author AANGANES
 *
 */
public class Game extends BaseWrapper implements StartupConstants,
		ScenarioConstants, ERENConstants {

	/**
	 * @param internal
	 */
	public Game(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public Game(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	
	// ADD AN ATTRIBUTE
	
	public void setID(String val) {
		setAttributeValue("ID", val);
	}
	    
	public String getID() {
		return getAttributeValue("ID");
	}  
	
	public void setName(String val) {
		setAttributeValue("name", val);
	}
	
	public String getName() {
		return getAttributeValue("name");
	}
	
	// ADDING A LIST OF SUB OBJECT
	
	public ActiveRoles addActiveRoles() {
    	return addExtension(EREN_ACTIVEROLES);
    }
    
    public ActiveRoles getActiveRoles() {
    	return getExtension(EREN_ACTIVEROLES);
    }
    
    // ADDING A SUB OBJECT
    public ERENscenario getScenario() {
    	return getExtension(EREN_SCENARIO);
    }
    
    public ERENscenario addScenario() {
    	return addExtension(EREN_SCENARIO);
    }

}
