package org.mitre.eren.protocol.dialogue;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.mitre.eren.protocol.edxl_rm.Employer;
import org.mitre.eren.protocol.edxl_rm.NameElement;
import org.mitre.eren.protocol.edxl_rm.Occupation;
import org.mitre.eren.protocol.edxl_rm.OccupationElement;
import org.mitre.eren.protocol.edxl_rm.PersonName;
import org.mitre.eren.protocol.scenario.Location;
import org.opencare.lib.model.BaseWrapper;

public class UserMessage extends BaseWrapper implements DialogueConstants,EDXLRMConstants {

  public UserMessage(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public UserMessage(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }
  
  public void setFollowup (boolean followup) { 
    setAttributeValue(DLG_FOLLOWUP,Boolean.toString(followup));
  }
  
  public boolean isFollowup () { 
    return Boolean.parseBoolean(getAttributeValue(DLG_FOLLOWUP));
  }

  public Sender addSender () { 
    return addExtension(DLG_SENDER);
  }
  
  public Sender getSender () { 
    return getExtension(DLG_SENDER);
  }
  
  public void setMessageText (String messageText) { 
    setElementText(DLG_MESSAGETEXT,messageText);
  }
  
  public String getMessageText () {
    return getSimpleExtension(DLG_MESSAGETEXT);
  }
  
  public void setMessageID (String messageID) { 
    setElementText(DLG_MESSAGEID,messageID);
  }
  
  public String getMessageID () { 
    return getSimpleExtension(DLG_MESSAGEID);
  }
  
  public void setPrecedingMessageID (String precedingMessageID) { 
    setElementText(DLG_PRECEDINGMESSAGEID,precedingMessageID);
  }
  
  public String getPrecedingMessageID () { 
    return getSimpleExtension(DLG_PRECEDINGMESSAGEID);
  }
  
  public String getSummary () { 
    return getSimpleExtension(DLG_SUMMARY);
  }
  
  public void setSummary (String summary) { 
    setElementText(DLG_SUMMARY, summary);
  }

  public String getPriority () { 
    return getSimpleExtension(DLG_PRIORITY);
  }
  
  public void setPriority (String priority) { 
    setElementText(DLG_PRIORITY,priority);
  }
  
  public String getMessageType () { 
    return getSimpleExtension(DLG_MESSAGETYPE);
  }
  
  public void setMessageType (String messageType) { 
    setElementText(DLG_MESSAGETYPE,messageType);
  }
  
  public Integer getTimeout () { 
    String to = getSimpleExtension(DLG_TIMEOUT);
    return to == null ? null : Integer.parseInt(to);
  }
  
  public void setTimeout (Integer timeout) { 
    setElementText(DLG_TIMEOUT,timeout.toString());
  }
  
  public Location addLocation () { 
    return addExtension(DLG_LOCATION);
  }
  
  public Location getLocation () { 
    return getExtension(DLG_LOCATION);
  }

  public ResponseOption addResponseOption () { 
    return addExtension(DLG_RESPONSEOPTION);
  }
  
  public List<ResponseOption> getResponseOptions () { 
    return getExtensions(DLG_RESPONSEOPTION);
  }
  
  public KMLLayer addKMLLayer () { 
    return addExtension(DLG_KMLLAYER);
  }
  
  public List<KMLLayer> getKMLLayers() { 
    return getExtensions(DLG_KMLLAYER);
  }

  public ResponseRange getResponseRange () { 
    return getExtension(DLG_RESPONSERANGE);
  }
  
  public ResponseRange addResponseRange () { 
    return addExtension(DLG_RESPONSERANGE);
  }
  
  // Convenience methods
  
  public Sender setSender (String id,String firstName,String lastName,String organization,String role,String photoUrl) { 
    Sender sender = addSender();
    sender.setID(id);
    sender.setPhotoUrl(photoUrl);
    PersonName name = sender.addPartyName().addPersonName();
    name.setFirstName(firstName);
    name.setLastName(lastName);
    Occupation occ = sender.addOccupations().addOccupation();
    OccupationElement occElem = occ.addOccupationElement();
    occElem.setAttributeValue(XPIL_TYPE, "Role");
    occElem.setText(role);
    Employer emp = occ.addEmployer();
    NameElement orgName = emp.addNameElement();
    orgName.setText(organization);
    return sender;
  }

}
