package org.mitre.eren.protocol.dialogue;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.mitre.eren.protocol.edxl_rm.PartyTypeWrapper;

public class Sender extends PartyTypeWrapper implements DialogueConstants {

  public Sender(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public Sender(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }
  
  public String getID() { 
    return getAttributeValue(XPIL_ID);
  }
  
  public void setID(String value) {
    setAttributeValue(XPIL_ID,value);
  }

  
  public void setPhotoUrl (String photoUrl) { 
    setAttributeValue(DLG_PHOTOURL,photoUrl);
  }
  
  public String getPhotoUrl () {
    return getAttributeValue(DLG_PHOTOURL);
  }


}
