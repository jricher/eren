package org.mitre.eren.protocol.dialogue;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class ResponseRange extends BaseWrapper implements DialogueConstants {

  public ResponseRange(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public ResponseRange(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }

  public void setResponseMin (String min) { 
    setElementText(DLG_RESPONSEMIN,min);
  }
  
  public String getResponseMin () { 
    return getSimpleExtension(DLG_RESPONSEMIN);
  }
  
  public void setResponseMax (String max) { 
    setElementText(DLG_RESPONSEMAX,max);
  }
  
  public String getResponseMax () { 
    return getSimpleExtension(DLG_RESPONSEMAX);
  }
  
}
