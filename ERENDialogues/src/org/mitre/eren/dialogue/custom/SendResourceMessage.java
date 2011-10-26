package org.mitre.eren.dialogue.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.abdera.model.Element;
import org.apache.abdera.protocol.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;
import org.mitre.eren.dialogue.DialogueManager;
import org.mitre.eren.dialogue.ResourceMessageType;
import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.mitre.eren.protocol.dialogue.ResponseOption;
import org.mitre.eren.protocol.dialogue.UserMessage;
import org.mitre.eren.protocol.edxl_rm.AssignmentInformation;
import org.mitre.eren.protocol.edxl_rm.ContactInformationType;
import org.mitre.eren.protocol.edxl_rm.ContactRoleType;
import org.mitre.eren.protocol.edxl_rm.ERENDatatypeFactory;
import org.mitre.eren.protocol.edxl_rm.Employer;
import org.mitre.eren.protocol.edxl_rm.LocationType;
import org.mitre.eren.protocol.edxl_rm.MeasuredQuantity;
import org.mitre.eren.protocol.edxl_rm.NameElement;
import org.mitre.eren.protocol.edxl_rm.Occupation;
import org.mitre.eren.protocol.edxl_rm.OccupationElement;
import org.mitre.eren.protocol.edxl_rm.OccupationElementList;
import org.mitre.eren.protocol.edxl_rm.PartyType;
import org.mitre.eren.protocol.edxl_rm.PersonNameType;
import org.mitre.eren.protocol.edxl_rm.Resource;
import org.mitre.eren.protocol.edxl_rm.ResourceInformation;
import org.mitre.eren.protocol.edxl_rm.ResourceMessage;
import org.mitre.eren.protocol.edxl_rm.ResourceTypes;
import org.mitre.eren.protocol.edxl_rm.ResponseInformationType;
import org.mitre.eren.protocol.edxl_rm.ResponseTypeType;
import org.mitre.eren.protocol.edxl_rm.ScheduleInformation;
import org.mitre.eren.protocol.edxl_rm.ScheduleTypeType;
import org.mitre.eren.protocol.edxl_rm.ValueListType;
import org.mitre.eren.protocol.edxl_rm.WhereType;
import org.mitre.eren.protocol.scenario.Location;
import org.mitre.eren.protocol.scenario.NPC;
import org.opencare.lib.model.edxl.EDXLDistribution;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SendResourceMessage extends Action implements DialogueConstants {

  private static final String MESSAGE_ID = "messageID";
  private static final String ADDRESSEE = "addressee";
  private static final String MESSAGE_TYPE = "messageType";
  private static final String ORIG_MESSAGE = "originatingRsrcMsg";
  private static final String PREC_MESSAGE = "precedingRsrcMsg";
  private static final String INCIDENT = "incident";
  private static final String MESSAGE_SRC = "msgSrc";
  private static final String REQUESTER_ID = "requesterID";
  private static final String APPROVER_ID = "approverID";
  private static final String PRECEDING_REID = "precedingREID";
  private static final String RESPONSE_STATUS = "responseStatus";
  private static final String RESOURCE_ID = "resourceID";
  private static final String RESOURCE_NAME = "resourceName";
  private static final String RESOURCE_TYPE_URN = "resourceTypeURN";
  private static final String RESOURCE_TYPE_VALUE = "resourceTypeValue";
  private static final String RESOURCE_DESCRIPTION = "resourceDescription";
  private static final String RESOURCE_DEPLOYMENT_STATUS = "resourceStatus";
  private static final String DEPLOYMENT_STATUS_URN = "deploymentStatusUrn";
  private static final String RESOURCE_QUANTITY = "quantity";
  private static final String RESOURCE_UNITS = "units";
  private static final String SCHEDULE_TYPE = "scheduleType";
  private static final String SCHEDULE_DATETIME = "scheduleDateTime";
  private static final String SCHEDULE_LOCATION = "locationDescription";
  private static final String SCHEDULE_LAT = "latitude";
  private static final String SCHEDULE_LON = "longitude";
  
  private static final String DEFAULT_RESOURCE_TYPE_URN = "urn:x-hazard:vocab:resourceTypes";
  private static final String DEFAULT_DEPLOYMENT_TYPE_URN = "urn:x-hazard:vocab:deploymentStatusTypes";
  
  @Override
  public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
      SCInstance scInstance, Log appLog, Collection derivedEvents)
  throws ModelException, SCXMLExpressionException {

    Logger log = Logger.getLogger(getClass().getName());
    
    DialogueManager dm = (DialogueManager) scInstance.getRootContext().get("dm");
    Context ctx = scInstance.getContext(getParentTransitionTarget()); 
    EDXLDistribution edxl = dm.makeEDXL();
    edxl.addExplicitAddress("eren:role",(String) ctx.get(ADDRESSEE));
    String messageType = (String) ctx.get(MESSAGE_TYPE);
    if (messageType == null) return;
    ResourceMessageType rmt = ResourceMessageType.get(messageType);
    ResourceMessage message = dm.makeResourceMessage(edxl,rmt);
    
    message.setMessageID((String) ctx.get(MESSAGE_ID));
    message.setSentDateTime(dm.getCurrentDate());
    message.setMessageContentType(rmt.getQname().getLocalPart());

    message.setOriginatingMessageID((String) ctx.get(ORIG_MESSAGE));
    message.setPrecedingMessageID((String) ctx.get(PREC_MESSAGE));
    message.addIncident((String) ctx.get(INCIDENT));
    message.addFunding().setFundCode("EREN");

    String npcId = (String) ctx.get(MESSAGE_SRC);

    NPC npc = dm.getNpc(npcId);

    String senderFirstName = npc.getFirstName();
    String senderLastName = npc.getLastName();
    String senderRole = npc.getNpcRole();
    String senderOrg = npc.getOrgname() != null ? npc.getOrgname() 
        : npc.getFacility();
    
    if (senderFirstName != null || senderLastName != null || senderRole != null || senderOrg != null) {
      ContactInformationType ci = message.addContactInformation();
      ci.setContactRole(ContactRoleType.SENDER);
      addParty(ci,senderFirstName,senderLastName,senderRole,senderOrg);
    }

    String requesterId = (String) ctx.get(REQUESTER_ID);
    NPC requester = dm.getNpc(requesterId);
    if (requester != null) { 

      String requesterFirstName = requester.getFirstName();
      String requesterLastName = requester.getLastName();
      String requesterRole = requester.getNpcRole();
      String requesterOrg = requester.getOrgname() != null ? requester.getOrgname() : requester.getFacility();

      if (requesterFirstName != null || requesterLastName != null || requesterRole != null || requesterOrg != null) {
        ContactInformationType ci = message.addContactInformation();
        ci.setContactRole(ContactRoleType.REQUESTER);
        addParty(ci,requesterFirstName,requesterLastName,requesterRole,requesterOrg);
      }
    }

    String approverId = (String) ctx.get(APPROVER_ID);
    NPC approver = dm.getNpc(approverId);
    if (approver != null) {
      String approverFirstName = approver.getFirstName();
      String approverLastName = approver.getLastName();
      String approverRole = approver.getNpcRole();
      String approverOrg = approver.getOrgname() != null ? approver.getOrgname() : approver.getFacility();

      if (approverFirstName != null || approverLastName != null || approverRole != null || approverOrg != null) {
        ContactInformationType ci = message.addContactInformation();
        ci.setContactRole(ContactRoleType.APPROVER);
        addParty(ci,senderFirstName,senderLastName,senderRole,senderOrg);
      }
    }

    int rieCount = 0;
    ResourceInformation ri = message.addResourceInformation();
    ri.setResourceInfoElementID(Integer.toString(rieCount++));
    if (ctx.get(RESPONSE_STATUS) != null) { 
      ResponseInformationType respInfo = ri.addResponseInformation();
      respInfo.setResponseType(ResponseTypeType.fromValue((String)ctx.get(RESPONSE_STATUS)));
      respInfo.setPrecedingResourceInfoElementID((String)ctx.get(PRECEDING_REID));
    }
    
    Resource r = ri.addResource();
    r.setName((String) ctx.get(RESOURCE_NAME));
    if (ctx.get(RESOURCE_ID) != null) { 
      r.setResourceID((String) ctx.get(RESOURCE_ID));
    }
    if (ctx.get(RESOURCE_TYPE_VALUE) != null) {
      ValueListType rt = r.addTypeStructure();
      if (ctx.get(RESOURCE_TYPE_URN) !=null) { 
        rt.setValueListURN((String)ctx.get(RESOURCE_TYPE_URN));
      } else { 
        rt.setValueListURN(DEFAULT_RESOURCE_TYPE_URN);
      }
      String resourceType = (String)ctx.get(RESOURCE_TYPE_VALUE);
      try {
        ResourceTypes type = ResourceTypes.valueOf(resourceType);
        rt.addValue(type.toString());
      } catch (IllegalArgumentException e) { 
        log.warning("Unknown value for resource type: " + resourceType);
      }
    }
    
    if (ctx.get(RESOURCE_DESCRIPTION) != null) { 
      r.setDescription((String)ctx.get(RESOURCE_DESCRIPTION));
    }

    if (ctx.get(RESOURCE_DEPLOYMENT_STATUS) != null) { 
      ValueListType status = r.addResourceStatus().addDeploymentStatus();
      if (ctx.get(DEPLOYMENT_STATUS_URN) != null) { 
        status.setValueListURN((String)ctx.get(DEPLOYMENT_STATUS_URN));
      } else { 
        status.setValueListURN(DEFAULT_DEPLOYMENT_TYPE_URN);
      }
      status.addValue((String)ctx.get(RESOURCE_DEPLOYMENT_STATUS));
    }
    
    Double quantity = ctx.get(RESOURCE_QUANTITY) == null ? 1.0 : 
      new Double(ctx.get(RESOURCE_QUANTITY).toString());
    MeasuredQuantity quant = ri.addAssignmentInformation().addQuantity().addMeasuredQuantity();
    quant.setAmount(quantity);
    if (ctx.get(RESOURCE_UNITS) != null) { 
      quant.addUnitOfMeasure().addValue((String)ctx.get(RESOURCE_UNITS));
    }
    
    ScheduleInformation si = ri.addScheduleInformation();
    if (ctx.get(SCHEDULE_TYPE) != null) { 
      si.setScheduleType(ScheduleTypeType.fromValue((String)ctx.get(SCHEDULE_TYPE)));
      XMLGregorianCalendar c = ERENDatatypeFactory.factory.newXMLGregorianCalendar((String)ctx.get(SCHEDULE_DATETIME));
      si.setDateTime(c);
    }
    
    LocationType loc = si.addLocation();
    if (ctx.get(SCHEDULE_LOCATION) != null) { 
      loc.setLocationDescription((String)ctx.get(SCHEDULE_LOCATION));
    } else if (ctx.get(SCHEDULE_LAT) != null && ctx.get(SCHEDULE_LON) != null) { 
      List<Double> values = new ArrayList<Double>();
      values.add(Double.parseDouble((String) ctx.get(SCHEDULE_LAT)));
      values.add(Double.parseDouble((String) ctx.get(SCHEDULE_LON)));
      loc.addTargetArea().addPoint().addPos().setValue(values);
    }
    // Update the status before sending the message because subsequent states in the dialogue could depend
    // on having an up to date status before the message comes back to us from the bus
    dm.updateResourceStatus(message);
    dm.sendMessage(edxl);
  }

  /**
   * Constructs a "PartyType" element from information about the party.
   * A party is a person or organization, or a combination of multiple
   * people and/or organizations.
   * 
   * @param message
   * @param firstName
   * @param lastName
   * @param role
   * @param org
   * @return
   */
  private PartyType addParty (ContactInformationType ci, String firstName, String lastName, String role, String org) {
    PartyType party = ci.addAdditionalContactInformation();
    if (firstName != null || lastName != null) {
      PersonNameType personName = party.addPartyName().addPersonName();
      if (firstName != null) { 
        personName.setFirstName(firstName);
      }
      if (lastName != null) { 
        personName.setLastName(lastName);
      }
    }
    if (org != null || role != null) { 
      Occupation occupation = party.addOccupations().addOccupation();
      if (role != null) {
        OccupationElement oe = occupation.addOccupationElement(); 
        oe.setType(OccupationElementList.ROLE);        
        oe.setText(role);
      }
      if (org != null) { 
        Employer emp = occupation.addEmployer();
        NameElement empName = emp.addNameElement();
        empName.setText(org);
      }
    }
    return party;
  }
  
  
}