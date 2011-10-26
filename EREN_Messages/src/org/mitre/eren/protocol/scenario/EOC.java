package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class EOC extends BaseWrapper implements ScenarioConstants {

	public EOC(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public EOC(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(EOC.class);
	
    public void setID(String val) {
    	setAttributeValue("ID", val);
    }
    
    public String getID() {
    	return getAttributeValue("ID");
    }
    
    public void setName(String val) {
    	setAttributeValue("Name", val);
    }
    
    public String getName() {
    	return getAttributeValue("Name");
    }
    
    public void setStatus(String val) {
	    if (val != null) {
			setElementText(EREN_STATUS, val);
		} else {
			removeElement(EREN_STATUS);
		}
    }    
    
    public String getStatus() {
    	return getSimpleExtension(EREN_STATUS);
    }
    
    public Location getLocation() {
    	return getExtension(EREN_LOCATION);
    }
    
    public Location addLocation() {
    	return addExtension(EREN_LOCATION);
    }
}
