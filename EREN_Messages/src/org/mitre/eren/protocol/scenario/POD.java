package org.mitre.eren.protocol.scenario;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class POD extends BaseWrapper implements ScenarioConstants {

	public POD(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public POD(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(POD.class);
	
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

	    public String getLocalPopulation() {
	    	return getSimpleExtension(EREN_LOCALPOPULATION);
	    }
	    
	    public void setLocalPopulation(String val) {
		    if (val != null) {
				setElementText(EREN_LOCALPOPULATION, val);
			} else {
				removeElement(EREN_LOCALPOPULATION);
			}
	    }
	    
	    public String getExposedPopulation() {
	    	return getSimpleExtension(EREN_EXPOSEDPOPULATION);
	    }

	    public void setExposedPopulation(String val) {
		    if (val != null) {
				setElementText(EREN_EXPOSEDPOPULATION, val);
			} else {
				removeElement(EREN_EXPOSEDPOPULATION);
			}
	    }
	    /*
	    public String getThroughput() {
	    	return getSimpleExtension(EREN_THROUGHPUT);
	    }
	    
	    public void setThroughput(String val) {
		    if (val != null) {
				setElementText(EREN_THROUGHPUT, val);
			} else {
				removeElement(EREN_THROUGHPUT);
			}
	    }
*/
	    
      public String getManager() {
        return getSimpleExtension(EREN_MANAGER);
      }
      
      public void setManager(String val) {
        if (val != null) {
        setElementText(EREN_MANAGER, val);
      } else {
        removeElement(EREN_MANAGER);
      }
      }

      public String getOwner() {
        return getSimpleExtension(EREN_OWNER);
      }
      
      public void setOwner(String val) {
        if (val != null) {
        setElementText(EREN_OWNER, val);
      } else {
        removeElement(EREN_OWNER);
      }
      }

	    public String getTimeToOpen() {
	    	return getSimpleExtension(EREN_TIMETOOPEN);
	    }
	    
	    public void setTimeToOpen(String val) {
		    if (val != null) {
				setElementText(EREN_TIMETOOPEN, val);
			} else {
				removeElement(EREN_TIMETOOPEN);
			}
	    }
	    
	    public Location getLocation() {
	    	return getExtension(EREN_LOCATION);
	    }
	    
	    public Location addLocation() {
	    	return addExtension(EREN_LOCATION);
	    }

	    public Staff addStaff() {
	    	return addExtension(EREN_STAFF);
	    }
	    
	    public List<Staff> getStaff() {
	    	return getExtensions(EREN_STAFF);
	    }
	    
	    
}
