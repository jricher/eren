package org.mitre.eren.protocol.clock;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class ClockresetWrapper extends BaseWrapper implements ClockConstants, Clockreset {
	public ClockresetWrapper(Element internal) {
	    super(internal);
	    // TODO Auto-generated constructor stub
    }

	/**
     * @param factory
     * @param qname
     */
    public ClockresetWrapper(Factory factory, QName qname) {
	    super(factory, qname);
	    // TODO Auto-generated constructor stub
    }

	@Override
	public Long getTime() {
		// TODO Auto-generated method stub
		String val = getSimpleExtension(EREN_TIME);
		return val == null ? null : Long.parseLong(val);
	}

	@Override
	public void setTime(Long time) {
		// TODO Auto-generated method stub
		if (time != null) {
			setElementText(EREN_TIME, time.toString());
		} else {
			removeElement(EREN_TIME);
		}
	}
}
