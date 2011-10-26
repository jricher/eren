package org.mitre.eren.protocol.dialogue;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.mitre.eren.protocol.edxl_rm.Employer;
import org.mitre.eren.protocol.edxl_rm.NameElement;
import org.mitre.eren.protocol.edxl_rm.Occupation;
import org.mitre.eren.protocol.edxl_rm.OccupationElement;
import org.mitre.eren.protocol.edxl_rm.PersonName;
import org.opencare.lib.model.BaseWrapper;

public class OpenPod extends BaseWrapper implements DialogueConstants,EDXLRMConstants {

  public OpenPod(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public OpenPod(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }
  
  public Sender addSender () { 
    return addExtension(DLG_SENDER);
  }
  
  public Sender getSender () { 
    return getExtension(DLG_SENDER);
  }
  
  public void setPodId (String podId) { 
    setElementText(DLG_PODID,podId);
  }
  
  public String getPodId () {
    return getSimpleExtension(DLG_PODID);
  }
  
  public void setTimestamp (String timestamp) { 
    setElementText(DLG_TIMESTAMP,timestamp);
  }
  
  public String getTimestamp () { 
    return getSimpleExtension(DLG_TIMESTAMP);
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
