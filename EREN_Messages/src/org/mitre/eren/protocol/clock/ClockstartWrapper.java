package org.mitre.eren.protocol.clock;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class ClockstartWrapper extends BaseWrapper implements ClockConstants,
		Clockstart {

	public ClockstartWrapper(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public ClockstartWrapper(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
}
