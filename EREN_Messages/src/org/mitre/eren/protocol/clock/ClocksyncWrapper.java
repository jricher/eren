package org.mitre.eren.protocol.clock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.AtomDate;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class ClocksyncWrapper extends BaseWrapper implements ClockConstants,
		Clocksync {

	public ClocksyncWrapper(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public ClocksyncWrapper(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Date getDate() {
		// TODO Auto-generated method stub
		String val = getSimpleExtension(EREN_DATE);
		return val == null ? null : AtomDate.parse(val);
	}

	@Override
	public void setDate(Date val) {
		
		if (val == null) {
			return;
		}
		
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ", new Locale("en", "US"));

		StringBuffer dateBuff = new StringBuffer();
		dateBuff.append(format.format(val));

		dateBuff.insert(dateBuff.length() - 2, ':');

		setElementText(EREN_DATE, dateBuff.toString());
	}
}
