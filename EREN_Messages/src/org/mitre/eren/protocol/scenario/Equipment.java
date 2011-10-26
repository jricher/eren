package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class Equipment extends BaseWrapper implements ScenarioConstants {

	public Equipment(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public Equipment(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Equipment.class);
	
	public PODEquipment addPODEquipment() {
		return addExtension(EREN_PODEQUIPMENT);
	}
	
	public PODEquipment getPODEquipment() {
		return getExtension(EREN_PODEQUIPMENT);
	}
	
	public Medication addMedication() {
		return addExtension(EREN_MEDICATION);
	}
	
	public Medication getMedication() {
		return getExtension(EREN_MEDICATION);
	}
	
	public RSSEquipment addRssEquipment() {
		return addExtension(EREN_RSSEQUIPMENT);
	}
	
	public RSSEquipment getRssEquipment() {
		return getExtension(EREN_RSSEQUIPMENT);
	}

}
