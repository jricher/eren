/**
 * 
 */
package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

/**
 * @author jricher
 *
 */
public class Role extends BaseWrapper implements ScenarioConstants {

	/**
	 * @param internal
	 */
	public Role(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public Role(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Role.class);
    
	public void setID(String val) {
    	setAttributeValue("ID", val);
    }
    
    public String getID() {
    	return getAttributeValue("ID");
    }
    
    public String getDescription() {
    	return getSimpleExtension(EREN_DESCRIPTION);
    }
    
    public void setDescription(String val) {
	    if (val != null) {
			setElementText(EREN_DESCRIPTION, val);
		} else {
			removeElement(EREN_DESCRIPTION);
		}
    }    
    
    public String getTitle() {
    	return getSimpleExtension(EREN_TITLE);
    }
    
    public void setTitle(String val) {
	    if (val != null) {
			setElementText(EREN_TITLE, val);
		} else {
			removeElement(EREN_TITLE);
		}
    }    
    
	public Integer getMin() {
		return Integer.parseInt(getSimpleExtension(EREN_MIN));
	}

	public void setMin(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_MIN, val);
		} else {
			removeElement(EREN_MIN);
		}
	}
	
	public Integer getMax() {
		return Integer.parseInt(getSimpleExtension(EREN_MAX));
	}

	public void setMax(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_MAX, val);
		} else {
			removeElement(EREN_MAX);
		}
	}
	
    public String getBriefing() {
    	return getSimpleExtension(EREN_BRIEFING);
    }
    
    public void setBriefing(String val) {
	    if (val != null) {
			setElementText(EREN_BRIEFING, val);
		} else {
			removeElement(EREN_BRIEFING);
		}
    }
    
    public String getImage() {
    	return getSimpleExtension(EREN_IMAGE);
    }
    
    public void setImage(String val) {
	    if (val != null) {
			setElementText(EREN_IMAGE, val);
		} else {
			removeElement(EREN_IMAGE);
		}
    }    
        
}
