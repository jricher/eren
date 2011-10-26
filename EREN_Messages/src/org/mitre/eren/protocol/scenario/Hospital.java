package org.mitre.eren.protocol.scenario;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class Hospital extends BaseWrapper implements ScenarioConstants {

	public Hospital(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public Hospital(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}

	// private static Logger log = LoggingUtils.getLogger(Hospital.class);

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

	public String getCapacity() {
		return getSimpleExtension(EREN_CAPACITY);
	}

	public void setCapacity(String val) {
		if (val != null) {
			setElementText(EREN_CAPACITY, val);
		} else {
			removeElement(EREN_CAPACITY);
		}
	}

	public String getFilled() {
		return getSimpleExtension(EREN_FILLED);
	}

	public void setFilled(String val) {
		if (val != null) {
			setElementText(EREN_FILLED, val);
		} else {
			removeElement(EREN_FILLED);
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
}
