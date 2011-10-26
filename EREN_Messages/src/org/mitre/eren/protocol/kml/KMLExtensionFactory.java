package org.mitre.eren.protocol.kml;

import org.apache.abdera.util.AbstractExtensionFactory;

public class KMLExtensionFactory extends AbstractExtensionFactory implements KMLConstants {

	public KMLExtensionFactory() {
		super(KML_NS);
		addImpl(KML_TESSELLATE, Tessellate.class);
		addImpl(KML_COORDINATES, Coordinates.class);
		addImpl(KML_LATITUDE, Latitude.class);
		addImpl(KML_LONGITUDE, Longitude.class);
	}
	//private static Logger log = LoggingUtils.getLogger(KMLExtensionFactory.class);
}
