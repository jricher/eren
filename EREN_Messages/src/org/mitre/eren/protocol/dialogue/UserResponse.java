package org.mitre.eren.protocol.dialogue;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class UserResponse extends BaseWrapper implements DialogueConstants {

  public UserResponse(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public UserResponse(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }
  
  public void setMessageID (Integer messageID) { 
    setElementText(DLG_MESSAGEID,messageID.toString());
  }
  
  public String getMessageID () { 
    return getSimpleExtension(DLG_MESSAGEID);
  }
  
  public void setResponseID (String responseID) { 
    setElementText(DLG_RESPONSEID,responseID);
  }

  public String getResponseID () {
    return getSimpleExtension(DLG_RESPONSEID);
  }
  
  public List<String> getResponseIDs () {
    List<String> values = new ArrayList<String>();
    for (Element element : getExtensions(DLG_RESPONSEID)) { 
      values.add(element.getText());
    }
    return values;
  }
  
  public void setResponseValue (String responseID) { 
    setElementText(DLG_RESPONSEVALUE,responseID);
  }

  public String getResponseValue () {
    return getSimpleExtension(DLG_RESPONSEVALUE);
  }
  

  
  
}
