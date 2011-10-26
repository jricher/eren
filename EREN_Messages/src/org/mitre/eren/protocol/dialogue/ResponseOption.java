package org.mitre.eren.protocol.dialogue;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class ResponseOption extends BaseWrapper implements DialogueConstants {

  public ResponseOption(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public ResponseOption(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }

  public void setResponseID (String responseID) { 
    setElementText(DLG_RESPONSEID,responseID);
  }
  
  public String getResponseID () { 
    return getSimpleExtension(DLG_RESPONSEID);
  }
  
  public void setShortResponse (String shortResponse) {
    setElementText(DLG_SHORTRESPONSE,shortResponse);
  }
  
  public String getShortResponse () { 
    return getSimpleExtension(DLG_SHORTRESPONSE);
  }
  
  public void setLongResponse (String longResponse) {
    setElementText(DLG_LONGRESPONSE,longResponse);
  }
  
  public String getLongResponse () { 
    return getSimpleExtension(DLG_LONGRESPONSE);
  }
  
  public void setFollowup (boolean followup) { 
    setAttributeValue(DLG_FOLLOWUP,Boolean.toString(followup));
  }
  
  public boolean isFollowup () { 
    return Boolean.parseBoolean(getAttributeValue(DLG_FOLLOWUP));
  }
  
}
