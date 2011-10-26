package org.mitre.eren.protocol.edxl_rm;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;

public interface TypeStructure extends ExtensibleElement, Element, EDXLRMConstants {

	public String getResourceType();
	public void setResourceType(String rt);
	
}
