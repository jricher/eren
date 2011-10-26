/**
 * 
 */
package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

/**
 * 
 * TODO!!
 * 
 * @author jricher
 *
 */
public class NPC extends BaseWrapper implements ScenarioConstants {

	/**
	 * @param internal
	 */
	public NPC(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public NPC(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(NPC.class);

	public void setID(String val) {
		setAttributeValue("ID", val);
	}
	    	
	public String getID() {
		return getAttributeValue("ID");
	}
	
	public void setFirstName(String val) {
		if (val != null) {
			setElementText(EREN_FIRSTNAME, val);
		} else {
			removeElement(EREN_FIRSTNAME);
		}
	}

	public String getFirstName() {
		return getSimpleExtension(EREN_FIRSTNAME);
	}

  public void setLastName(String val) {
    if (val != null) {
      setElementText(EREN_LASTNAME, val);
    } else {
      removeElement(EREN_LASTNAME);
    }
  }

  public String getLastName() {
    return getSimpleExtension(EREN_LASTNAME);
  }
  
  public void setNpcRole(String val) {
    if (val != null) {
      setElementText(EREN_NPCROLE, val);
    } else {
      removeElement(EREN_NPCROLE);
    }
  }

  public String getNpcRole() {
    return getSimpleExtension(EREN_NPCROLE);
  }
  
	

	public void setImage(String val) {
		if (val != null) {
			setElementText(EREN_IMAGE, val);
		} else {
			removeElement(EREN_IMAGE);
		}
	}

	public String getImage() {
		return getSimpleExtension(EREN_IMAGE);
	}

	public void setOrgname(String val) { 
	  if (val != null) { 
	    setElementText(EREN_ORGNAME, val);
	  } else { 
	    removeElement(EREN_ORGNAME);
	  }
	}
	
	public String getOrgname () { 
	  return getSimpleExtension(EREN_ORGNAME);
	}
	
	public void setFacility(String val) {
		if (val != null) {
			setElementText(EREN_FACILITY, val);
		} else {
			removeElement(EREN_FACILITY);
		}
	}

	public String getFacility() {
		return getSimpleExtension(EREN_FACILITY);
	}

	
	public void setStatechart (String val) { 
	  if (val != null) { 
	    setElementText(EREN_STATECHART, val);
	  } else { 
	    removeElement(EREN_STATECHART);
	  }
	}

	public String getStatechart () { 
	  return getSimpleExtension(EREN_STATECHART);
	}
	    
}
