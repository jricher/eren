package org.mitre.eren.protocol.edxl_rm;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class MeasuredQuantityWrapper extends BaseWrapper implements MeasuredQuantity {

	public MeasuredQuantityWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }
  public MeasuredQuantityWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }
  @Override
public String getResourceType() {
    return getSimpleExtension(RM_RESOURCETYPE);
  }
	@Override
	public void setResourceType(String rt) {
	  setElementText(RM_RESOURCETYPE,rt);
	}
	
	
 //Used when part of QuantityType
	@Override
	public double getAmount() { 
	  String as = getSimpleExtension(RM_AMOUNT);
	  return as == null ? null : Double.parseDouble(as); 
	}
	
	@Override
	public void setAmount(Double value) { 
	  if (value == null) {
	    removeElement(RM_AMOUNT);
	  } else { 
	    setElementText(RM_AMOUNT,value.toString());
	  }
	    
	}
	@Override
	public ValueListType getUnitOfMeasure() {
	  return getExtension(RM_UNITOFMEASURE);
	}
	@Override
	public ValueListType addUnitOfMeasure() { 
	  return addExtension(RM_UNITOFMEASURE);
	}
}
