package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

import org.mitre.eren.protocol.kml.KMLConstants;

public class Line extends BaseWrapper implements ScenarioConstants, KMLConstants {

	public Line(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public Line(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Line.class);
	
	public Integer getTessellate() {
		String val = getSimpleExtension(KML_TESSELLATE);
		return val == null ? null : Integer.parseInt(val);
	}
	
	public void setTessellate(Integer val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(KML_TESSELLATE, val.toString());
		} else {
			removeElement(KML_TESSELLATE);
		}
	}
	
	public String getCoordinates() {
		return getSimpleExtension(KML_TESSELLATE);
	}

	/**
	 * val is a space-delimited set of comma-delimited lat/lon/alt coordinate values
	 * @param val
	 */
	public void setCoordinates(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(KML_COORDINATES, val);
		} else {
			removeElement(KML_COORDINATES);
		}
	}
	
}
