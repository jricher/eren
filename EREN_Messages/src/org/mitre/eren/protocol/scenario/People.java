package org.mitre.eren.protocol.scenario;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class People extends BaseWrapper implements ScenarioConstants {

	public People(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public People(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(People.class);
	
	public Staff addStaff() {
		return addExtension(EREN_STAFF);
	}
	
	public List<Staff> getStaff() {
		return getExtensions(EREN_STAFF);
	}

	public NPC addNPC() {
		return addExtension(EREN_NPC);
	}
	
	public List<NPC> getNPC() {
		return getExtensions(EREN_NPC);
	}
}
