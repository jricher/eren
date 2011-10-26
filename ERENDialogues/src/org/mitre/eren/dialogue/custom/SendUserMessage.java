package org.mitre.eren.dialogue.custom;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

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
import org.mitre.eren.protocol.dialogue.KMLLayer;
import org.mitre.eren.protocol.dialogue.ResponseOption;
import org.mitre.eren.protocol.dialogue.ResponseRange;
import org.mitre.eren.protocol.dialogue.UserMessage;
import org.mitre.eren.protocol.scenario.ERENscenario;
import org.mitre.eren.protocol.scenario.KMLFile;
import org.mitre.eren.protocol.scenario.KMLFiles;
import org.mitre.eren.protocol.scenario.Location;
import org.mitre.eren.protocol.scenario.NPC;
import org.opencare.lib.model.edxl.EDXLDistribution;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SendUserMessage extends Action implements DialogueConstants,DMConstants {
  
  private static final String ADDRESSEE = "addressee";
  private static final String MESSAGE_ID = "messageID";
  private static final String MESSAGE_FOLLOWUP = "messageFollowup";
  private static final String PRECEDING_MSG = "precedingUserMsg";
  private static final String PRIORITY = "priority";
  private static final String MESSAGE_SRC = "msgSrc";
  private static final String MESSAGE_TYPE = "messageType";
  private static final String SUMMARY = "messageSummary";
  private static final String LOCATION = "messageLocation";
  private static final String TEXT = "messageText";
  private static final String NUM_LAYERS = "numLayers";
  private static final String KML_LABEL = "kmlLabel";
  private static final String KML_LAYER = "kmlLayer";
  private static final String NUM_RESPONSES = "numResponses";
  private static final String RESPONSE_ID = "responseID";
  private static final String SHORT_RESPONSE = "shortResponse";
  private static final String LONG_RESPONSE = "longResponse";
  private static final String FOLLOWUP = "responseFollowup";
  private static final String RESOURCE_LIST = "resourceList";
  private static final String FILTER = "filter";
  private static final String DESCRIPTION = "description";
  private static final String ALT_LONG = "altLongResponse";
  private static final String ALT_SHORT = "altShortResponse";
  private static final String ALT_FOLLOWUP = "altFollowup";
  private static final String RANGE_MIN = "rangeMin";
  private static final String RANGE_MAX = "rangeMax";
  
  private Logger log = Logger.getLogger(SendUserMessage.class.getName());
  
  @Override
  public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
      SCInstance scInstance, Log appLog, Collection derivedEvents)
  throws ModelException, SCXMLExpressionException {

    Evaluator evaluator = scInstance.getEvaluator();
    
    DialogueManager dm = (DialogueManager) scInstance.getRootContext().get("dm");
    Context ctx = scInstance.getContext(getParentTransitionTarget()); 
    EDXLDistribution edxl = dm.makeEDXL();
    edxl.addExplicitAddress("eren:role",(String) ctx.get(ADDRESSEE));
    UserMessage message = dm.makeUserMessage(edxl);

    String npcId = (String) ctx.get(MESSAGE_SRC);

    NPC npc = dm.getNpc(npcId);
    
    message.setSender(npcId, npc.getFirstName(), npc.getLastName(), 
        npc.getOrgname() != null ? npc.getOrgname() : dm.getFacilityName(npc.getFacility()), 
            npc.getNpcRole(), npc.getImage());
    
    message.setMessageID((String) ctx.get(MESSAGE_ID));
    if (ctx.get(PRECEDING_MSG) != null) { 
      message.setPrecedingMessageID((String) ctx.get(PRECEDING_MSG));
    } 
    
    if (ctx.get(MESSAGE_FOLLOWUP) != null) { 
      message.setFollowup((Boolean) ctx.get(MESSAGE_FOLLOWUP));
    }

    String priority = ctx.get(PRIORITY) == null ? PRIORITY_NORMAL : (String) ctx.get(PRIORITY);
    message.setPriority(priority);

    message.setMessageType((String) ctx.get(MESSAGE_TYPE));
    message.setSummary(((String) ctx.get(SUMMARY)).replaceAll("\\s+", " "));
    message.setMessageText((String) ctx.get(TEXT));
    Location location = message.addLocation();  
    location.setFacility((String) ctx.get(LOCATION));

    Integer numResponses = (Integer) ctx.get(NUM_RESPONSES);
    if (numResponses != null) { 
      ResponseOption ro;
      for (int r = 0; r < numResponses; r++) {
        String responseID = (String) ctx.get(RESPONSE_ID+r);
        if (responseID != null) {  
          ro = message.addResponseOption();
          ro.setResponseID(responseID);
          Boolean followup = ctx.get(FOLLOWUP+r) == null ? false : (Boolean) ctx.get(FOLLOWUP+r);
          ro.setFollowup(followup);
          ro.setShortResponse((String) ctx.get(SHORT_RESPONSE+r));
          String longResponse = (String) ctx.get(LONG_RESPONSE+r); 
          if (longResponse != null) {  
            ro.setLongResponse(longResponse);
          }
        }
      }
    } else { 
      // Creating response list on the fly from available resources of given type
      String resourceListType = (String) ctx.get(RESOURCE_LIST);
      if (resourceListType != null) {
        String filter = (String) ctx.get(FILTER);
        Boolean followup = ctx.get(FOLLOWUP) == null ? false : (Boolean) ctx.get(FOLLOWUP);
        String descriptionExpr = (String) ctx.get(DESCRIPTION);
        String shortResponse = (String) ctx.get(SHORT_RESPONSE);
        String longResponse = (String) ctx.get(LONG_RESPONSE);
        for (String id : dm.getResources(resourceListType)) {
          if (filter != null) { 
            String expr = filter.replace("%RID%", id).replace("%TYPE%", resourceListType);
            if ((Boolean) evaluator.eval(ctx,expr)) { 

              ResponseOption ro = message.addResponseOption();
              ro.setFollowup(followup);
              ro.setResponseID(id);

              String de = descriptionExpr.replace("%RID%",id);
              String description = (String) evaluator.eval(ctx,de);

              String sr = shortResponse.replace("%DESCRIPTION%",description);
              ro.setShortResponse(sr);

              if (longResponse != null) { 
                String lr = longResponse.replace("%DESCRIPTION%", description);
                ro.setLongResponse(lr);
              }
            }
          }
        }
        String altShort = (String) ctx.get(ALT_SHORT);
        String altLong = (String) ctx.get(ALT_LONG);
        Boolean altFollowup = ctx.get(ALT_FOLLOWUP) == null ? false : (Boolean) ctx.get(ALT_FOLLOWUP);
        ResponseOption ro = message.addResponseOption();
        ro.setFollowup(altFollowup);
        ro.setResponseID(message.getMessageID() + ":alt");
        ro.setShortResponse(altShort);
        ro.setLongResponse(altLong);
      }
    }
    
    ERENscenario scenario = dm.getScenario();

    Integer numLayers = (Integer) ctx.get(NUM_LAYERS);
    if (numLayers != null) { 
      KMLLayer kmllayer;
      for (int k = 0; k < numLayers; k++) {
        String layerID = (String) ctx.get(KML_LABEL+k);
        if (layerID != null) { 
          kmllayer = message.addKMLLayer();
          kmllayer.setLayerID(layerID);
          boolean filefound;
          for (String fileid : ((String) ctx.get(KML_LAYER+k)).split(",")) {
            filefound = false;
            for (KMLFile kmlfile : scenario.getKMLFiles().getKMLFile())  { 
              if (fileid.equals(kmlfile.getID())) { 
                filefound = true;
                kmllayer.addKMLFile(kmlfile.getPath());
              }
            }
            if (!filefound) { 
              log.warning("Path not found for KML file: " + fileid);
            }
          }
        }
      }
    }

    String rangeMin = (String) ctx.get(RANGE_MIN);
    String rangeMax = (String) ctx.get(RANGE_MAX);
    ResponseRange range = message.addResponseRange();
    range.setResponseMin(rangeMin);
    range.setResponseMax(rangeMax);
    
    dm.sendMessage(edxl);
  }
}
