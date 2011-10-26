package org.mitre.eren.dialogue.custom;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.abdera.model.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.Evaluator;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;
import org.mitre.eren.dialogue.DMConstants;
import org.mitre.eren.dialogue.DialogueManager;
import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.mitre.eren.protocol.dialogue.OpenPod;
import org.mitre.eren.protocol.dialogue.ResponseOption;
import org.mitre.eren.protocol.dialogue.UserMessage;
import org.mitre.eren.protocol.scenario.Location;
import org.mitre.eren.protocol.scenario.NPC;
import org.opencare.lib.model.edxl.EDXLDistribution;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SendOpenPod extends Action implements DialogueConstants,DMConstants {
  
  private static final String POD_ID = "podID";
  private static final String TIMESTAMP = "timestamp";
  private static final String MESSAGE_SRC = "msgSrc";

  
  @Override
  public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
      SCInstance scInstance, Log appLog, Collection derivedEvents)
  throws ModelException, SCXMLExpressionException {

    DialogueManager dm = (DialogueManager) scInstance.getRootContext().get("dm");
    Context ctx = scInstance.getContext(getParentTransitionTarget()); 
    EDXLDistribution edxl = dm.makeEDXL();

    OpenPod message = dm.getClient().attachElement(edxl, DLG_OPENPOD);

    String npcId = (String) ctx.get(MESSAGE_SRC);
    NPC npc = dm.getNpc(npcId);
    String photoUrl = npc.getImage();

    if (photoUrl == null) { 
      photoUrl = "default.png";
    }
    
    message.setSender(npcId,npc.getFirstName(),npc.getLastName(),
        npc.getOrgname() != null ? npc.getOrgname() : npc.getFacility(),
        npc.getNpcRole(),photoUrl);
    
    message.setPodId((String)ctx.get(POD_ID));
    message.setTimestamp((String)ctx.get(TIMESTAMP));
    dm.sendMessage(edxl);
  }
}
