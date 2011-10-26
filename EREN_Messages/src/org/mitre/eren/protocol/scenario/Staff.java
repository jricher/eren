package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class Staff extends BaseWrapper implements ScenarioConstants {

	public Staff(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public Staff(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Staff.class);
	
    public void setID(String val) {
    	setAttributeValue("ID", val);
    }
    
    public String getID() {
    	return getAttributeValue("ID");
    }
    
    public String getFunction() {
		return getSimpleExtension(EREN_FUNCTION);
	}

	public void setFunction(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_FUNCTION, val);
		} else {
			removeElement(EREN_FUNCTION);
		}
	}
	
	public String getMin() {
		return getSimpleExtension(EREN_MIN);
	}

	public void setMin(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_MIN, val);
		} else {
			removeElement(EREN_MIN);
		}
	}
	
	
	public String getTarget() {
		return getSimpleExtension(EREN_TARGET);
	}

	public void setTarget(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_TARGET, val);
		} else {
			removeElement(EREN_TARGET);
		}
	}
	
	public String getQuantity() {
		return getSimpleExtension(EREN_QUANTITY);
	}

	public void setQuantity(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_QUANTITY, val);
		} else {
			removeElement(EREN_QUANTITY);
		}
	}
	
	
	 public String getCurrent() {
	    return getSimpleExtension(EREN_CURRENT);
	  }

	  public void setCurrent(String val) {
	    // TODO Auto-generated method stub
	    if (val != null) {
	      setElementText(EREN_CURRENT, val);
	    } else {
	      removeElement(EREN_CURRENT);
	    }
	  }
	  
	  
	public String getStatus() {
		return getSimpleExtension(EREN_STATUS);
	}

	public void setStatus(String val) {
		if (val != null) {
			setElementText(EREN_STATUS, val);
		} else {
			removeElement(EREN_STATUS);
		}
	}
	
	public String getDisplayName() {
		return getSimpleExtension(EREN_DISPLAYNAME);
	}
	
	public void setDisplayName(String val) {
		if (val != null) {
			setElementText(EREN_DISPLAYNAME, val);
		} else {
			removeElement(EREN_DISPLAYNAME);
		}
	}
	
}
