package org.mitre.eren.protocol.clock;

import org.apache.abdera.util.AbstractExtensionFactory;

public class ClockExtensionFactory extends AbstractExtensionFactory implements ClockConstants {

	public ClockExtensionFactory() {
		super(EREN_NS);
		
		// clock controls
		addImpl(EREN_CLOCKTICK, ClocktickWrapper.class);
		addImpl(EREN_CLOCKSPEED, ClockspeedWrapper.class);
		addImpl(EREN_CLOCKSYNC, ClocksyncWrapper.class);

		addImpl(EREN_CLOCKSTART, ClockstartWrapper.class);
		addImpl(EREN_CLOCKPAUSE, ClockpauseWrapper.class);
		addImpl(EREN_CLOCKRESET, ClockresetWrapper.class);
		addImpl(EREN_SETCLOCKSYNC, SetClocksyncWrapper.class);
		addImpl(EREN_SETCLOCKSPEED, SetClockspeedWrapper.class);
	}


}
