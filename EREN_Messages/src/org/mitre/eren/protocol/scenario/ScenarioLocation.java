package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class ScenarioLocation extends BaseWrapper implements ScenarioConstants {

	public ScenarioLocation(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public ScenarioLocation(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}

	// private static Logger log = LoggingUtils.getLogger(County.class);

	public String getName() {
		return getSimpleExtension(EREN_NAME);
	}

	public void setName(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_NAME, val);
		} else {
			removeElement(EREN_NAME);
		}
	}

	public String getState() {
		return getSimpleExtension(EREN_STATE);
	}

	public void setState(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_STATE, val);
		} else {
			removeElement(EREN_STATE);
		}
	}
	
	public Location addLocation() {
		return addExtension(EREN_LOCATION);
	}
	
	public Location getLocation() {
		return getExtension(EREN_LOCATION);
	}

	public String getPopulation() {
		return getSimpleExtension(EREN_POPULATION);
	}

	public void setPopulation(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_POPULATION, val);
		} else {
			removeElement(EREN_POPULATION);
		}
	}
}
