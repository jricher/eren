package org.mitre.eren.protocol.kml;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class Coordinates extends BaseWrapper implements KMLConstants {

	public Coordinates(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public Coordinates(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Coordinates.class);
}
