package org.mitre.eren.protocol;

import javax.xml.namespace.QName;

public interface ERENConstants {

	public static final String EREN_NS = "urn:mitre:eren:1.0";
	
	public static final String EREN_PFX = "eren";
	
	// basic data types
	// TODO: handle these w/o EREN prefixing
	public static final QName EREN_TIME = new QName(EREN_NS, "time", EREN_PFX);
	public static final QName EREN_DATE = new QName(EREN_NS, "date", EREN_PFX);
	public static final QName EREN_RATIO = new QName(EREN_NS, "ratio", EREN_PFX);
}
