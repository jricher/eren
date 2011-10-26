package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class Location extends BaseWrapper implements ScenarioConstants {

	public Location(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public Location(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Location.class);
	public void setRef(String val) {
		setAttributeValue("ref", val);
	}

	public String getRef() {
		return getAttributeValue("ref");
	}


	public Line addLine() {
		return addExtension(EREN_LINE);
	}
	
	public Line getLine() {
		return getExtension(EREN_LINE);
	}
	
	public KMLLocation addKMLLocation() {
		return addExtension(EREN_KMLLOCATION);
	}

	public KMLLocation getKMLLocation() {
		return getExtension(EREN_KMLLOCATION);
	}
	
	public String getFacility() {
		return getSimpleExtension(EREN_FACILITY);
	}

	public void setFacility(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_FACILITY, val);
		} else {
			removeElement(EREN_FACILITY);
		}
	}
	
}
