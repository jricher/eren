package org.mitre.eren.clock.control;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.apache.abdera.factory.ExtensionFactory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.edxl.EDXLDistribution;

import org.mitre.eren.message.MessageProcessor;
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.clock.ClockExtensionFactory;
import org.mitre.eren.protocol.clock.Clockspeed;
import org.mitre.eren.protocol.clock.Clocksync;
import org.mitre.eren.protocol.clock.Clocktick;
import org.mitre.javautil.logging.LoggingUtils;

public class InboundClockPanelHttp implements MessageProcessor, ClockConstants {
	private static Logger log = LoggingUtils.getLogger(InboundClockPanelHttp.class);
	private ClockControlApp app;
	
	public InboundClockPanelHttp(ClockControlApp app) {
		// TODO Auto-generated constructor stub
		this.app = app;
	}

	@Override
    public <T extends ExtensionFactory> List<T> getExtensions() {
		return (List<T>) Collections.singletonList(new ClockExtensionFactory());
    }

	@Override
	public void processMessage(Element e, String gameID, String sender,
			List<String> roles, List<String> userID) {

		EDXLDistribution edxl = (EDXLDistribution) e.getDocument().getRoot();
		
		//System.err.println("from: " + edxl.getSenderID());
		
		if (e.getQName().equals(EREN_CLOCKTICK)) {
			// tick
			Clocktick c = (Clocktick)e;
			app.setTick(c.getTime());
		} else if (e.getQName().equals(EREN_CLOCKSYNC)) {
			// time/date sync
			Clocksync c = (Clocksync)e;
			app.setSync(c.getDate());
		} else if (e.getQName().equals(EREN_CLOCKSPEED)) {
			// ratio sync
			Clockspeed c = (Clockspeed)e;
			app.setSpeed(c.getRatio());
		}
    }
}
