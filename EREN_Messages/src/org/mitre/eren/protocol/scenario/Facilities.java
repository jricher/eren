package org.mitre.eren.protocol.scenario;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class Facilities extends BaseWrapper implements ScenarioConstants {

	public Facilities(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public Facilities(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Facilities.class);
	
	
	public EOC addEoc() {
		return addExtension(EREN_EOC);
	}
	
	public EOC getEoc() {
		return getExtension(EREN_EOC);
	}
	
	public POD addPod() {
		return addExtension(EREN_POD);
	}
	
	public List<POD> getPods() {
		return getExtensions(EREN_POD);
	}
	
	public Hospital addHospital() {
		return addExtension(EREN_HOSPITAL);
	}
	
	public List<Hospital> getHospitals() {
		return getExtensions(EREN_HOSPITAL);
	}
	public Airport addAirport() {
		return addExtension(EREN_AIRPORT);
	}
	
	public List<Airport> getAirports() {
		return getExtensions(EREN_AIRPORT);
	}
	public RSS addRss() {
		return addExtension(EREN_RSS);
	}
	
	public List<RSS> getRSSes() {
		return getExtensions(EREN_RSS);
	}
		
}
