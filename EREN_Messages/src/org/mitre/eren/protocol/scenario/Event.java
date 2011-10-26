package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;

public class Event extends BaseWrapper implements ScenarioConstants, EDXLRMConstants {
	/**
     * @param internal
     */
    public Event(Element internal) {
	    super(internal);
	    // TODO Auto-generated constructor stub
    }

	/**
     * @param factory
     * @param qname
     */
    public Event(Factory factory, QName qname) {
	    super(factory, qname);
	    // TODO Auto-generated constructor stub
    }
    
    public void setID(String val) {
    	setAttributeValue("ID", val);
    }
    
    public String getID() {
    	return getAttributeValue("ID");
    }
    
    public void setDescription(String val) {
	    if (val != null) {
			setElementText(EREN_DESCRIPTION, val);
		} else {
			removeElement(EREN_DESCRIPTION);
		}
    }    
    
    public String getDescription() {
    	return getSimpleExtension(EREN_DESCRIPTION);
    }
    
    public Location getLocation() {
    	return getExtension(EREN_LOCATION);
    }
    
    public Location addLocation() {
    	return addExtension(EREN_LOCATION);
    }
    
    public void setTime(String val) {
	    if (val != null) {
			setElementText(EREN_TIME, val);
		} else {
			removeElement(EREN_TIME);
		}
    }    
    
    public String getTime() {
    	return getSimpleExtension(EREN_TIME);
    }
    
}
