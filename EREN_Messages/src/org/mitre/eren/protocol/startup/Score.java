package org.mitre.eren.protocol.startup;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class Score extends BaseWrapper implements StartupConstants {

  public Score(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public Score(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }
  
  public String getExposure() {
    return getSimpleExtension(EREN_EXPOSURE);
  }

  public void setExposure(String val) {
    // TODO Auto-generated method stub
    if (val != null) {
      setElementText(EREN_EXPOSURE, val);
    } else {
      removeElement(EREN_EXPOSURE);
    }
  }  
  public String getMorbidity() {
    return getSimpleExtension(EREN_MORBIDITY);
  }

  public void setMorbidity(String val) {
    // TODO Auto-generated method stub
    if (val != null) {
      setElementText(EREN_MORBIDITY, val);
    } else {
      removeElement(EREN_MORBIDITY);
    }
  }  
  public String getMortality() {
    return getSimpleExtension(EREN_MORTALITY);
  }

  public void setMortality(String val) {
    // TODO Auto-generated method stub
    if (val != null) {
      setElementText(EREN_MORTALITY, val);
    } else {
      removeElement(EREN_MORTALITY);
    }
  }  
  public String getTreated() {
    return getSimpleExtension(EREN_TREATED);
  }

  public void setTreated(String val) {
    // TODO Auto-generated method stub
    if (val != null) {
      setElementText(EREN_TREATED, val);
    } else {
      removeElement(EREN_TREATED);
    }
  }

}
