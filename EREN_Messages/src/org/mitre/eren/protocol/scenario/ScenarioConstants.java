package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.mitre.eren.protocol.ERENConstants;

public interface ScenarioConstants extends ERENConstants {

	public static final QName EREN_SCENARIO = new QName(EREN_NS, "scenario", EREN_PFX);
	public static final QName EREN_EVENT = new QName(EREN_NS, "event", EREN_PFX);
	public static final QName EREN_DESCRIPTION = new QName(EREN_NS, "description", EREN_PFX);
	public static final QName EREN_LOCATION = new QName(EREN_NS, "location", EREN_PFX);
	public static final QName EREN_LINE = new QName(EREN_NS, "line", EREN_PFX);
	public static final QName EREN_SCENARIOLOCATION = new QName(EREN_NS, "scenariolocation", EREN_PFX);
	public static final QName EREN_NAME = new QName(EREN_NS, "name", EREN_PFX);
	public static final QName EREN_STATE = new QName(EREN_NS, "state", EREN_PFX);
	public static final QName EREN_KMLLOCATION = new QName(EREN_NS, "kmlLocation", EREN_PFX);
	public static final QName EREN_POPULATION = new QName(EREN_NS, "population", EREN_PFX);
	public static final QName EREN_FACILITIES = new QName(EREN_NS, "facilities", EREN_PFX);
	public static final QName EREN_EOC = new QName(EREN_NS, "eoc", EREN_PFX);
	public static final QName EREN_STATUS = new QName(EREN_NS, "status", EREN_PFX);
	public static final QName EREN_DISPLAYNAME = new QName(EREN_NS, "displayName", EREN_PFX);
	//public static final QName EREN_AVAILABILITY = new QName(EREN_NS, "availability", EREN_PFX);
	public static final QName EREN_POD = new QName(EREN_NS, "pod", EREN_PFX);
	public static final QName EREN_STAFF = new QName(EREN_NS, "staff", EREN_PFX);
	public static final QName EREN_FUNCTION = new QName(EREN_NS, "function", EREN_PFX);
	public static final QName EREN_MIN = new QName(EREN_NS, "min", EREN_PFX);
	public static final QName EREN_MAX = new QName(EREN_NS, "max", EREN_PFX);
	public static final QName EREN_TARGET = new QName(EREN_NS, "target", EREN_PFX);
	public static final QName EREN_CURRENT = new QName(EREN_NS, "current", EREN_PFX);
	public static final QName EREN_LOCALPOPULATION = new QName(EREN_NS, "localPopulation", EREN_PFX);
	public static final QName EREN_EXPOSEDPOPULATION = new QName(EREN_NS, "exposedPopulation", EREN_PFX);
	public static final QName EREN_THROUGHPUT = new QName(EREN_NS, "throughput", EREN_PFX);
	public static final QName EREN_HOSPITAL = new QName(EREN_NS, "hospital", EREN_PFX);
	public static final QName EREN_CAPACITY = new QName(EREN_NS, "capacity", EREN_PFX);
	public static final QName EREN_FILLED = new QName(EREN_NS, "filled", EREN_PFX);
	public static final QName EREN_AIRPORT = new QName(EREN_NS, "airport", EREN_PFX);
	public static final QName EREN_RSS = new QName(EREN_NS, "rss", EREN_PFX); // this will probably want a separate name, "RSS" is confusing here
	public static final QName EREN_RSSEQUIPMENT = new QName(EREN_NS, "rssEquipment", EREN_PFX);
	//public static final QName EREN_VEHICLES = new QName(EREN_NS, "vehicles", EREN_PFX);
	//public static final QName EREN_POLICECAR = new QName(EREN_NS, "policeCar", EREN_PFX);
	public static final QName EREN_PEOPLE = new QName(EREN_NS, "people", EREN_PFX);
	//public static final QName EREN_MEDICALSTAFF = new QName(EREN_NS, "medicalStaff", EREN_PFX);
	public static final QName EREN_QUANTITY = new QName(EREN_NS, "quantity", EREN_PFX);
	//public static final QName EREN_SECURITYSTAFF = new QName(EREN_NS, "securityStaff", EREN_PFX);
	//public static final QName EREN_SUPPORTSTAFF = new QName(EREN_NS, "supportStaff", EREN_PFX);
	//public static final QName EREN_TRUCKDRIVERSTAFF = new QName(EREN_NS, "truckDriverStaff", EREN_PFX);
	//public static final QName EREN_NATIONALGUARD = new QName(EREN_NS, "nationalGuard", EREN_PFX);
	public static final QName EREN_KMLFILES = new QName(EREN_NS, "kmlFiles", EREN_PFX);
	public static final QName EREN_KMLFILE = new QName(EREN_NS,"kmlFile", EREN_PFX);
	public static final QName EREN_PATH = new QName(EREN_NS,"path",EREN_PFX);
	public static final QName EREN_EQUIPMENT = new QName(EREN_NS, "equipment", EREN_PFX);
	public static final QName EREN_FACILITY = new QName(EREN_NS, "facility", EREN_PFX);
	public static final QName EREN_STATECHART = new QName(EREN_NS, "statechart", EREN_PFX);
	public static final QName EREN_PODEQUIPMENT = new QName(EREN_NS, "podEquipment", EREN_PFX);
	public static final QName EREN_MEDICATION = new QName(EREN_NS, "medication", EREN_PFX);
	public static final QName EREN_ROLES = new QName(EREN_NS, "roles", EREN_PFX);
	public static final QName EREN_ROLE = new QName(EREN_NS, "role", EREN_PFX);
	public static final QName EREN_TITLE = new QName(EREN_NS, "title", EREN_PFX);
	public static final QName EREN_BRIEFING = new QName(EREN_NS, "briefing", EREN_PFX);
	public static final QName EREN_TIMING = new QName(EREN_NS, "timing", EREN_PFX);
	public static final QName EREN_GAMETIME = new QName(EREN_NS, "gametime", EREN_PFX);
	public static final QName EREN_WALLTIME = new QName(EREN_NS, "walltime", EREN_PFX);
	public static final QName EREN_IMAGE = new QName(EREN_NS, "image", EREN_PFX);
	public static final QName EREN_BASEURL = new QName(EREN_NS, "baseUrl", EREN_PFX);
	public static final QName EREN_ACTIONSFILE = new QName(EREN_NS, "actionsFile", EREN_PFX);
	public static final QName EREN_EVENTS = new QName(EREN_NS, "events", EREN_PFX);
	public static final QName EREN_EVENTTIME = new QName(EREN_NS, "eventtime", EREN_PFX);
	public static final QName EREN_STARTTIME = new QName(EREN_NS, "starttime", EREN_PFX);
	public static final QName EREN_MANAGER = new QName(EREN_NS, "manager", EREN_PFX);
	public static final QName EREN_OWNER = new QName(EREN_NS, "owner", EREN_PFX);
	public static final QName EREN_NPC = new QName(EREN_NS, "npc", EREN_PFX);
	public static final QName EREN_FIRSTNAME = new QName(EREN_NS, "firstname", EREN_PFX);
	public static final QName EREN_LASTNAME = new QName(EREN_NS, "lastname", EREN_PFX);
	public static final QName EREN_ORGNAME = new QName(EREN_NS, "orgname", EREN_PFX);
	public static final QName EREN_NPCROLE = new QName(EREN_NS, "npcRole", EREN_PFX);
	public static final QName EREN_TIMETOOPEN = new QName(EREN_NS, "timetoopen", EREN_PFX);

}
