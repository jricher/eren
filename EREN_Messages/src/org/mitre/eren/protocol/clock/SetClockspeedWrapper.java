package org.mitre.eren.protocol.clock;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class SetClockspeedWrapper extends BaseWrapper implements SetClockspeed,
		ClockConstants {

	/**
	 * @param internal
	 */
	public SetClockspeedWrapper(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public SetClockspeedWrapper(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Integer getRatio() {
		// TODO Auto-generated method stub
		String val = getSimpleExtension(EREN_RATIO);
		return val == null ? null : Integer.parseInt(val);
	}

	@Override
	public void setRatio(Integer val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_RATIO, val.toString());
		} else {
			removeElement(EREN_RATIO);
		}

	}
}
