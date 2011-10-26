package org.mitre.eren.protocol.edxl_rm;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;

public interface MeasuredQuantity extends ExtensibleElement, Element, EDXLRMConstants {

	public String getResourceType();
	public void setResourceType(String rt);
	
 //Used when part of QuantityType
	public double getAmount();
	public void setAmount(Double value);
	public ValueListType getUnitOfMeasure();
	public ValueListType addUnitOfMeasure();
}
