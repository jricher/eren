package org.mitre.eren.protocol.exposure;

import javax.xml.namespace.QName;

import org.mitre.eren.protocol.ERENConstants;


public interface ExposureConstants extends ERENConstants{

	// population exposure messages
	public static final QName EREN_POPULATION = new QName(EREN_NS, "population", EREN_PFX);
	
	//plume footprint message
	public static final QName EREN_PLUME = new QName(EREN_NS, "plume", EREN_PFX);	

}
