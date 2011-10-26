package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

import org.mitre.eren.protocol.kml.KMLConstants;

public class KMLLocation extends BaseWrapper implements ScenarioConstants, KMLConstants {

	public KMLLocation(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public KMLLocation(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(KMLLocation.class);

	public String getLongitude() {
		return getSimpleExtension(KML_LONGITUDE);
	}

	/**
	 * val is a space-delimited set of comma-delimited lat/lon/alt coordinate values
	 * @param val
	 */
	public void setLongitude(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(KML_LONGITUDE, val);
		} else {
			removeElement(KML_LONGITUDE);
		}
	}
	
	public String getLatitude() {
		return getSimpleExtension(KML_LATITUDE);
	}

	public void setLatitude(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(KML_LATITUDE, val);
		} else {
			removeElement(KML_LATITUDE);
		}
	}
	
}
