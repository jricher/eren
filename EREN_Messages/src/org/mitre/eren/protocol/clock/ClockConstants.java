package org.mitre.eren.protocol.clock;

import javax.xml.namespace.QName;

import org.mitre.eren.protocol.ERENConstants;

public interface ClockConstants extends ERENConstants {

	// outbound clock
	public static final QName EREN_CLOCKTICK = new QName(EREN_NS, "clocktick", EREN_PFX);
	public static final QName EREN_CLOCKSYNC = new QName(EREN_NS, "clocksync", EREN_PFX);
	public static final QName EREN_CLOCKSPEED = new QName(EREN_NS, "clockspeed", EREN_PFX);
	
	// inbound clock
	public static final QName EREN_CLOCKSTART = new QName(EREN_NS, "clockstart", EREN_PFX);
	public static final QName EREN_CLOCKPAUSE = new QName(EREN_NS, "clockpause", EREN_PFX);
	public static final QName EREN_CLOCKRESET = new QName(EREN_NS, "clockreset", EREN_PFX);
	public static final QName EREN_SETCLOCKSPEED = new QName(EREN_NS, "setclockspeed", EREN_PFX);
	public static final QName EREN_SETCLOCKSYNC = new QName(EREN_NS, "setclocksync", EREN_PFX);
	
	
}
