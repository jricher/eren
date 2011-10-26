package org.mitre.eren.protocol.clock;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;

public interface Clocktick extends ExtensibleElement, Element, ClockConstants {

	public Long getTime();
	public void setTime(Long time);
	
}
