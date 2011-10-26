package org.mitre.eren.dialogue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.abdera.factory.ExtensionFactory;
import org.apache.abdera.model.Element;

import org.mitre.eren.message.MessageProcessor;
import org.mitre.eren.protocol.ERENConstants;
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.clock.ClockExtensionFactory;
import org.mitre.eren.protocol.clock.Clocksync;
import org.mitre.eren.protocol.clock.Clocktick;
import org.mitre.eren.protocol.dialogue.DialogueExtensionFactory;
import org.mitre.eren.protocol.dialogue.UserMessage;
import org.mitre.eren.protocol.edxl_rm.CommitResource;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.mitre.eren.protocol.edxl_rm.EDXLRMExtensionFactory;
import org.mitre.eren.protocol.edxl_rm.ReportResourceDeploymentStatus;
import org.mitre.eren.protocol.edxl_rm.RequisitionResource;
import org.mitre.eren.protocol.edxl_rm.ResourceMessage;
import org.mitre.eren.protocol.kml.KMLExtensionFactory;
import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.ScenarioConstants;
import org.mitre.eren.protocol.scenario.ScenarioExtensionFactory;
import org.mitre.eren.protocol.startup.StartupConstants;
import org.mitre.eren.protocol.dialogue.DialogueConstants;

import org.mitre.javautil.logging.LoggingUtils;
import org.opencare.lib.model.edxl.EDXLDistribution;

public class InboundDialogueHttpController implements MessageProcessor, EDXLRMConstants, ClockConstants, DialogueConstants, ScenarioConstants, StartupConstants {
	private static Logger log = LoggingUtils.getLogger(InboundDialogueHttpController.class);

	private DialogueManager dm;

	public InboundDialogueHttpController(DialogueManager dm, FileHandler fh) {
		// TODO Auto-generated constructor stub
		this.dm = dm;
		if (fh != null) { 
		  log.addHandler(fh);
		}
	}

  @Override
	public void processMessage (Element e, String gameID, String sender, List<String> roles, List<String> userID) {
    if (e instanceof UserMessage || e instanceof ResourceMessage){
      log.info("Processing message: " + e);
    }
    ResourceMessageType rmt = ResourceMessageType.getMessageByQName(e.getQName());
    if (rmt != null) { 
      dm.updateResourceStatus((ResourceMessage) e);
      dm.queueEvent(rmt.toString(),e);
	  } else if (e.getQName().equals(DLG_USERRESPONSE)) { 
	    dm.queueEvent(MessageType.USERINPUT_TYPE.toString(),e);
	  } else if (e.getQName().equals(EREN_CLOCKSYNC)) {
	    Clocksync sync = (Clocksync) e; 
	    dm.setStartDate(sync.getDate()); 
	  } else if (e.getQName().equals(EREN_CLOCKTICK)) { 
	    Clocktick tick = (Clocktick) e;
	    dm.setTick(tick.getTime());
    } else if (e.getQName().equals(EREN_GAMESTART)) {
      log.info("Got game start message");
      dm.startGame();
      //dm.queueEvent(MessageType.STARTUP_TYPE.toString(),e);
	  } else if (e.getQName().equals(EREN_SCENARIO)) { 
	    log.info("Got scenario message");
	    dm.loadScenario((ERENscenario) e);
    } else if (e.getQName().equals(EREN_SHUTDOWN)) {
      log.info("Shutting down game");
      dm.queueEvent(MessageType.SHUTDOWN_TYPE.toString(),e);
	  }
	 
	}

  @Override
  public <T extends ExtensionFactory> List<T> getExtensions() {
    List<T> l = new ArrayList<T>();
    l.add((T) new EDXLRMExtensionFactory());
    l.add((T) new DialogueExtensionFactory());
    l.add((T) new ClockExtensionFactory());
    l.add((T) new ScenarioExtensionFactory());
    l.add((T) new KMLExtensionFactory());

    return l;
  }




}
