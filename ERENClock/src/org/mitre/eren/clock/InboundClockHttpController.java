package org.mitre.eren.clock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.apache.abdera.factory.ExtensionFactory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.edxl.EDXLDistribution;

import org.mitre.eren.message.MessageProcessor;
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.clock.ClockExtensionFactory;
import org.mitre.eren.protocol.clock.Clockreset;
import org.mitre.eren.protocol.clock.SetClockspeed;
import org.mitre.eren.protocol.clock.SetClocksync;
import org.mitre.javautil.logging.LoggingUtils;

public class InboundClockHttpController implements MessageProcessor, ClockConstants {
	private static Logger log = LoggingUtils.getLogger(InboundClockHttpController.class);

	private ERENClock clock;

	public InboundClockHttpController(ERENClock clock) {
		// TODO Auto-generated constructor stub
		this.clock = clock;

	}

	@Override
    public void processMessage(Element e, String gameID, String sender, List<String> roles, List<String> userID) {
		if (e.getQName().equals(EREN_CLOCKSTART)) {
			//System.err.println("Start clock!!");
			clock.start();
		} else if (e.getQName().equals(EREN_CLOCKPAUSE)) {
			// pause the clock
			clock.pause();
		} else if (e.getQName().equals(EREN_CLOCKRESET)) {
			// reset the clock
			Clockreset reset = (Clockreset) e;
			clock.reset(reset.getTime());
		} else if (e.getQName().equals(EREN_SETCLOCKSPEED)) {

			SetClockspeed clockspeed = (SetClockspeed) e;
			//System.err.println("Clockspeed:" + clockspeed.getRatio());

			clock.setRatio(clockspeed.getRatio());
			
		} else if (e.getQName().equals(EREN_SETCLOCKSYNC)) {

			SetClocksync clocksync = (SetClocksync) e;
			//System.err.println("Clocksync:" + clocksync.getDate().toString());
			clock.setSyncDate(clocksync.getDate());
			
		}
	    
    }

	@Override
    public List<ClockExtensionFactory> getExtensions() {
		return Collections.singletonList(new ClockExtensionFactory());
    }

}
