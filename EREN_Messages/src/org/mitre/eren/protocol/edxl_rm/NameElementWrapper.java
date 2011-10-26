package org.mitre.eren.protocol.edxl_rm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class NameElementWrapper extends BaseWrapper implements NameElement {

  public NameElementWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public NameElementWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }

  @Override
  public PersonNameElementList getElementType() {
    String et = getAttributeValue(XNL_ELEMENTTYPE);
    return PersonNameElementList.fromValue(et);
  }
  
  @Override
public void setElementType (PersonNameElementList value) { 
    setAttributeValue(XNL_ELEMENTTYPE,value.value());
  }

  @Override
  public Map<QName, String> getOtherAttributes() {
    List<QName> attr = getExtensionAttributes();
    Map<QName,String> values = new HashMap<QName,String>();
    for (QName a : attr) { 
      values.put(a,getAttributeValue(a));
    }
    return values;
  }

  @Override
  public Boolean isAbbreviation() {
    String abbrev = getAttributeValue(RM_ABBREVIATION);
    return Boolean.valueOf(abbrev);
  }

  @Override
  public void setAbbreviation(Boolean value) {
    setAttributeValue(RM_ABBREVIATION,value.toString());
  }



}
