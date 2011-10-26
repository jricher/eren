package org.mitre.eren.protocol.dialogue;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class KMLLayer extends BaseWrapper implements DialogueConstants {

  public KMLLayer(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public KMLLayer(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }

  public void setLayerID (String value) { 
    setAttributeValue(DLG_LAYERID,value);
  }
  
  public String getLayerID () { 
    return getAttributeValue(DLG_LAYERID);
  }

  public KMLURL addKMLFile(String value) {
    KMLURL kmlurl = addExtension(DLG_KMLURL);
    kmlurl.setText(value);
    return kmlurl;
  }
  
  public List<KMLURL> getKMLFile() {
    return getExtensions(DLG_KMLURL);
  }  
  
}
