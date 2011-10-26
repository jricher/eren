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
 * @author AANGANES
 *
 */
public class ActiveRole extends BaseWrapper implements StartupConstants, ScenarioConstants,
		ERENConstants {

	/**
	 * @param internal
	 */
	public ActiveRole(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public ActiveRole(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
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
    
    public String getNumFilled() {
    	return getSimpleExtension(EREN_NUMFILLED);
	}
	    
	public void setNumFilled(Integer val) {
	    if (val != null) {
			setElementText(EREN_NUMFILLED, Integer.toString(val));
		} else {
			removeElement(EREN_NUMFILLED);
		}
	}
	
	public String getMin() {
		return getSimpleExtension(EREN_MIN);
	}
	
	public void setMin(Integer val) {
		if (val != null) {
			setElementText(EREN_MIN, Integer.toString(val));
		} else {
			removeElement(EREN_MIN);
		}
	}
	
	public String getMax() {
		return getSimpleExtension(EREN_MAX);
	}
	
	public void setMax(Integer val) {
		if (val != null) {
			setElementText(EREN_MAX, Integer.toString(val));
		} else {
			removeElement(EREN_MAX);
		}
	}
	
}
