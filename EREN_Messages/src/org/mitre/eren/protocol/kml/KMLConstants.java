package org.mitre.eren.protocol.kml;

import javax.xml.namespace.QName;

import org.mitre.eren.protocol.ERENConstants;

public interface KMLConstants extends ERENConstants {

	// maybe move KML stuff elsewhere?
    
    public static final String KML_NS = "http://www.opengis.net/kml/2.2";
	public static final String KML_PFX = "kml";
	public static final QName KML_TESSELLATE = new QName(KML_NS, "tessellate", KML_PFX);
	public static final QName KML_COORDINATES = new QName(KML_NS, "coordinates", KML_PFX);
	public static final QName KML_LATITUDE = new QName(KML_NS, "latitude", KML_PFX);
	public static final QName KML_LONGITUDE = new QName(KML_NS, "longitude", KML_PFX);

}
