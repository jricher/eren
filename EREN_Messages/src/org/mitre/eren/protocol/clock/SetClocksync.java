package org.mitre.eren.protocol.clock;

import java.util.Date;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;

public interface SetClocksync extends ExtensibleElement, Element, ClockConstants {

	public Date getDate();
	public void setDate(Date val);
	

}
