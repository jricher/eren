package org.mitre.eren.protocol.scenario;

import org.apache.abdera.util.AbstractExtensionFactory;

public class ScenarioExtensionFactory extends AbstractExtensionFactory implements ScenarioConstants {
	public ScenarioExtensionFactory() {
		super(EREN_NS);
		
		addImpl(EREN_SCENARIO, ERENscenario.class);
		addImpl(EREN_EVENTS, Events.class);
		addImpl(EREN_EVENT, Event.class);
		//addImpl(EREN_DESCRIPTION, Description.class);
		addImpl(EREN_LOCATION, Location.class);
		addImpl(EREN_LINE, Line.class);
		addImpl(EREN_SCENARIOLOCATION, ScenarioLocation.class);
//		addImpl(EREN_NAME, Name.class);
//		addImpl(EREN_STATE, State.class);
		addImpl(EREN_KMLLOCATION, KMLLocation.class);
//		addImpl(EREN_POPULATION, Population.class);
		addImpl(EREN_FACILITIES, Facilities.class);
		addImpl(EREN_EOC, EOC.class);
//		addImpl(EREN_STATUS, Status.class);
		//addImpl(EREN_AVAILABILITY, Availability.class);
		addImpl(EREN_POD, POD.class);
		addImpl(EREN_STAFF, Staff.class);
//		addImpl(EREN_FUNCTION, Function.class);
////		addImpl(EREN_MIN, Min.class);
//		addImpl(EREN_MAX, Max.class);
//		addImpl(EREN_CURRENT, Current.class);
//		addImpl(EREN_LOCALPOPULATION, LocalPopulation.class);
//		addImpl(EREN_THROUGHPUT, Throughput.class);
		addImpl(EREN_HOSPITAL, Hospital.class);
//		addImpl(EREN_CAPACITY, Capacity.class);
//		addImpl(EREN_FILLED, Filled.class);
		addImpl(EREN_AIRPORT, Airport.class);
		addImpl(EREN_RSS, RSS.class);
		//addImpl(EREN_VEHICLES, Vehicles.class);
		//addImpl(EREN_POLICECAR, PoliceCar.class);
		addImpl(EREN_PEOPLE, People.class);
		//addImpl(EREN_MEDICALSTAFF, MedicalStaff.class);
//		addImpl(EREN_QUANTITY, Quantity.class);
		//addImpl(EREN_SECURITYSTAFF, SecurityStaff.class);
		//addImpl(EREN_SUPPORTSTAFF, SupportStaff.class);
		//addImpl(EREN_TRUCKDRIVERSTAFF, TruckDriverStaff.class);
		//addImpl(EREN_NATIONALGUARD, NationalGuard.class);
//		addImpl(EREN_FACILITY, Facility.class);
		addImpl(EREN_KMLFILES, KMLFiles.class);
		addImpl(EREN_KMLFILE, KMLFile.class);
		addImpl(EREN_EQUIPMENT, Equipment.class);
		addImpl(EREN_PODEQUIPMENT, PODEquipment.class);
		addImpl(EREN_RSSEQUIPMENT, RSSEquipment.class);
		addImpl(EREN_MEDICATION, Medication.class);
		addImpl(EREN_ROLES, Roles.class);
		addImpl(EREN_ROLE, Role.class);
		addImpl(EREN_TIMING, Timing.class);
		addImpl(EREN_EVENTTIME, EventTime.class);
		addImpl(EREN_NPC, NPC.class);
                addImpl(EREN_STATUS, Status.class);
	}
}
