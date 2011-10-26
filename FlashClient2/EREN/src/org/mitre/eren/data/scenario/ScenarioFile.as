package org.mitre.eren.data.scenario 
{
	import fl.controls.listClasses.ImageCell;
	import flash.display.Loader;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	import flash.system.LoaderContext;
	import flash.utils.Dictionary;
	import flash.xml.XMLDocument;
	import org.as3commons.collections.LinkedMap;
	import org.mitre.eren.data.actions.Action;
	import org.mitre.eren.data.actions.ActionsFile;
	import org.mitre.eren.display.elements.EREN_Button;
	import org.mitre.eren.events.ErenEvent;
	
	/**
	 * Scenario File data structure
	 * 
	 * @author Amanda Anganes
	 */
	public class ScenarioFile extends EventDispatcher
	{
		
		public var name:String;
		private var x:XML;
		public var description:String;
		public var dur_gametime:Number;
		public var dur_walltime:Number;
		public var baseURL:String;
		public var scenPic:String;
		//Access role objects by their IDs
		public var roles:Dictionary;
		//Access people by their IDs
		public var people:Dictionary;
		//public var event:EventObject;
		public var scenarioLocation:ScenarioLocation;
		public var eoc:EOC;
		public var pods:Dictionary;
		public var hospitals:Dictionary;
		public var rss:RSS;
		public var syncDate:Date;
		public var staffResources:LinkedMap;
		
		public var scenPicURL:String = "resources/images/Locations/Norfolk.png";
		
		private var _facilities:Vector.<facility>;
		
		public var hasActions:Boolean = false;
		public var actions:ActionsFile;
		private var xmlLoader:URLLoader;
		
		public function ScenarioFile(xtemp:XML) 
		{
			this.x = xtemp;
			
			roles = new Dictionary();
			people = new Dictionary();
			pods = new Dictionary();
			hospitals = new Dictionary();
			_facilities = new Vector.<facility>();
			staffResources = new LinkedMap();
			
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			var kml:Namespace = new Namespace("http://www.opengis.net/kml/2.2");
			var xsi:Namespace = new Namespace("http://www.w3.org/2001/XMLSchema-instance");
			
			name = x.eren::name.text();
			description = x.eren::description.text();
			//DEBUG
			baseURL = x.eren::baseUrl.text();
			trace("*********************************************Got scenario file: base url is " + baseURL);
			scenPicURL = baseURL + x.eren::image.text();
			
			dur_gametime = x.eren::timing.eren::gametime.text();
			dur_walltime = x.eren::timing.eren::walltime.text();
			
			//TODO: process date string
			//for now...
			var da:Date = new Date();
			
			syncDate = da;
			
			//Get the actions file, if it exists
			var actionsQName:QName = new QName(eren, "actionsFile");
			if (x.hasOwnProperty(actionsQName)) {
				hasActions = true;
				var actionsURL:String = baseURL + x.eren::actionsFile.text();
				xmlLoader = new URLLoader();
				xmlLoader.addEventListener(Event.COMPLETE, parseActions);
				xmlLoader.load(new URLRequest(actionsURL));
			}
			
			//trace("Got name, desc, and duration " + name + ", " + description + ", " + dur_gametime + ", " + dur_walltime);
			
			//Store roles in the role dictionary, keyed by their IDs
			var xRoles:XMLList = x.eren::roles.eren::role;
			
			for each (var xrole in xRoles) {
				
				var min:Number = parseInt(xrole.eren::min.text());
				var max:Number = parseInt(xrole.eren::max.text());
				var t:String = xrole.eren::title.text();
				var d:String = xrole.eren::description.text();
				var b:String = xrole.eren::briefing.text();
				var i:String = xrole.attribute("ID");
				var p:String = xrole.eren::image.text();
				
				//trace("Got role with title " + t + ", description " + d + ", brief " + b + ", ID " + i);
				var rObj:RoleObject = new RoleObject(min, max, i, t, d, b, p);
				
				roles[xrole.attribute("ID")] = rObj;
			}
			
			trace("getting people");
			
			//Grab person (NPC) data before we handle facilities, since facilities are linked to person IDs
			//Store people in person dictionary, keyed by IDs
			var xnpcs:XMLList = x.eren::people.eren::npc;
			for each (var xnpc in xnpcs) {
				var nid:String = xnpc.attribute("ID");
				var nfn:String = xnpc.eren::firstname.text();
				var nln:String = xnpc.eren::lastname.text();
				var noname:String = xnpc.eren::orgname;
				var nrole:String = xnpc.eren::npcRole;
				var nimgurl:String = xnpc.eren::image.text();
				
				var tempPerson:Person = new Person(nid, nfn, nln, noname, nrole, nimgurl);
				//trace("created person " + nid + ", " + nfn + " " + nln + " from org " + noname + " with role " + nrole + " and image URL " + nimgurl);
				people[tempPerson.ID] = tempPerson;
				
			}
			
			//Event data
			//event = new EventObject(x.eren::event.attribute("ID"), x.eren::event.attribute("type"));
			
			//Location data
			var cn:String = x.eren::scenariolocation.eren::name.text();
			var cs:String = x.eren::scenariolocation.eren::state.text();
			var clat:Number = parseFloat(x.eren::scenariolocation.eren::location.eren::kmlLocation.kml::longitude.text());
			var clng:Number = parseFloat(x.eren::scenariolocation.eren::location.eren::kmlLocation.kml::latitude.text());
			var cpop:Number = parseInt(x.eren::scenariolocation.eren::population.text());
			
			scenarioLocation = new ScenarioLocation(cn, cs, clat, clng, cpop);
			
			trace("Got county with name: " + cn + ", population: " + cpop + ", location: " + clat + ", " + clng);
			
			//facilities
			
			//  EOC  //
			var x_eoc:XMLList = x.eren::facilities.eren::eoc;
			
			var eid:String = x_eoc.attribute("ID");
			var ename:String = x_eoc.attribute("Name");
			var estat:String = x_eoc.eren::status.text();
			var elat:Number = parseFloat(x_eoc.eren::location.eren::kmlLocation.kml::longitude.text());
			var elng:Number = parseFloat(x_eoc.eren::location.eren::kmlLocation.kml::latitude.text());
			
			//Add facility status-to-icon mapping
			var esti:Dictionary = new Dictionary();
			
			var facilityIconsQName:QName = new QName(eren, "facilityIcons");
			if (x_eoc.hasOwnProperty(facilityIconsQName)) 
			{
				trace("**************Scenario file has facility icons**************");
				var efacIcons:XMLList = x_eoc.eren::facilityIcons.eren::facilityIcon;
				for each (var efacIcon in efacIcons) {
					var e_path:String = baseURL + efacIcon.eren::path.text();
					var e_fstat:String = efacIcon.eren::status.text();
					
					//Add entry
					esti[e_fstat.toLowerCase()] = e_path;
				}
			} 
			else 
			{
				trace("**************Scenario file has NO facility icons**************");
				esti["in_use"] = baseURL + "/resources/images/icons/eoc.png";
				esti["available"] = baseURL + "/resources/images/icons/eoc.png";
				esti["requisitioned"] = baseURL + "/resources/images/icons/eoc.png";
				esti["committed"] = baseURL + "/resources/images/icons/eoc.png";
				esti["ready"] = baseURL + "/resources/images/icons/eoc.png";
				esti["non_functional"] = baseURL + "/resources/images/icons/eoc.png";
			}
			
			eoc = new EOC(eid, ename, estat, elat, elng, esti);
			
			_facilities.push(eoc);
			
			// RSS //
			var x_rss:XMLList = x.eren::facilities.eren::rss;
			trace("Got RSS node");
			var rid:String = x_rss.attribute("ID");
			var rname:String = x_rss.attribute("Name");
			var rstat:String = x_rss.eren::status.text();
			var rlat:Number = parseFloat(x_rss.eren::location.eren::kmlLocation.kml::longitude.text());
			var rlng:Number = parseFloat(x_rss.eren::location.eren::kmlLocation.kml::latitude.text());
			//trace("got basic rss properties");
			var rsti:Dictionary = new Dictionary();
			if (x_rss.hasOwnProperty(facilityIconsQName)) {
				trace("RSS has facility icons");
				var rfacIcons:XMLList = x_rss.eren::facilityIcons.eren::facilityIcon;
				for each (var rfacIcon in rfacIcons) {
					//trace("Adding facility icon entry");
					var r_path:String = baseURL + rfacIcon.eren::path.text();
					var r_fstat:String = rfacIcon.eren::status.text();
					
					//Add entry
					rsti[r_fstat.toLowerCase()] = r_path;
				}
			}
			else
			{
				trace("RSS has no facility icons");
				rsti["COMMITTED"] = baseURL + "/resources/images/icons/rss.png";
			}
				trace("Getting RSS staff");
				var rstaffVec:Vector.<Staff> = new Vector.<Staff>();
				var rstaffs:XMLList = x_rss.eren::staff;
				//trace("Got staff, iterating...");
				for each (var rstaff in rstaffs) {
					//trace("Got a kind of staff, proessing it");
					var rfqn:QName = new QName(eren, "function");
					var rf:String = rstaff.descendants(rfqn).text();
					var rm:Number = parseInt(rstaff.eren::min.text());
					var rtar:Number = parseInt(rstaff.eren::target.text());
					var rc:Number = parseInt(rstaff.eren::current.text());
					
					var rStaff:Staff = new Staff(rf, rm, rtar, rc);
					rstaffVec.push(rStaff);
				}
				
			//trace("creating rss");
			rss = new RSS(rid, rname, rstat, rlat, rlng, rstaffVec, rsti);
			_facilities.push(rss);
			//trace("created rss");
			
			//  PODs  //
			var xpods:XMLList = x.eren::facilities.eren::pod;
			//Store pod items in the pods dictionary
			for each (var xpod in xpods) {
				
				
				var pid:String = xpod.attribute("ID");
				var pname:String = xpod.attribute("Name");
				var pstat:String = xpod.eren::status.text();
				var plat:Number = parseFloat(xpod.eren::location.eren::kmlLocation.kml::longitude.text());
				var plng:Number = parseFloat(xpod.eren::location.eren::kmlLocation.kml::latitude.text());
				
				var pstaff:Vector.<Staff> = new Vector.<Staff>();
				var xstaffs:XMLList = xpod.eren::staff;
				
				for each (var xstaff in xstaffs) {
					var fqn:QName = new QName(eren, "function");
					var pf:String = xstaff.descendants(fqn).text();
					var pm:Number = parseInt(xstaff.eren::min.text());
					var ptar:Number = parseInt(xstaff.eren::target.text());
					var pc:Number = parseInt(xstaff.eren::current.text());
					
					var tStaff:Staff = new Staff(pf, pm, ptar, pc);
					pstaff.push(tStaff);
					//trace("Got POD staff " + pf + ", min " + pm + ", target " + ptar + ", current " + pc);
				}
				
				//Add facility status-to-icon mapping
				var psti:Dictionary = new Dictionary();
				
				if (xpod.hasOwnProperty(facilityIconsQName)) 
				{
					var pfacIcons:XMLList = xpod.eren::facilityIcons.eren::facilityIcon;
					for each (var pfacIcon in pfacIcons) {
						var p_path:String = baseURL + pfacIcon.eren::path.text();
						var p_fstat:String = pfacIcon.eren::status.text();
						
						//Add entry
						psti[p_fstat.toLowerCase()] = p_path;
					}
				} 
				else 
				{
					psti["ready"] = baseURL + "/resources/images/icons/pod_committed.png";
					psti["in_use"] = baseURL + "/resources/images/icons/pod_inuse.png";
					psti["open"] = baseURL + "/resources/images/icons/pod_inuse.png";
					psti["available"] = baseURL + "/resources/images/icons/pod_available.png";
					psti["committed"] = baseURL + "/resources/images/icons/pod_committed.png";
					psti["released"] = baseURL + "/resources/images/icons/pod_nonfunctional.png";
					psti["non_functional"] = baseURL + "/resources/images/icons/pod_riot.png";
					psti["riot"] = baseURL + "/resources/images/icons/pod_riot.png";
				}
				var temppod:POD = new POD(pid, pname, pstat, plat, plng, pstaff, psti);
				_facilities.push(temppod);
				
				pods[pid] = temppod;
				
				//trace("Got pod with ID: " + pid + ", name" + pname + ", status: " + pstat + ", availability: " + pavail + ", location: " + plat + ", " + plng);

			}
			
			//  Hospitals  //
			var xhospitals:XMLList = x.eren::facilities.eren::hospital;
			//trace("hospitals: " + xhospitals);
			//Store hospital items in hospitals directory
			for each (var xhosp in xhospitals) {
				
				var hid:String = xhosp.attribute("ID");
				var hname:String = xhosp.attribute("Name");
				var hstatus:String = xhosp.eren::status.text();
				var hlat:Number = parseFloat(xhosp.eren::location.eren::kmlLocation.kml::longitude.text());
				var hlng:Number = parseFloat(xhosp.eren::location.eren::kmlLocation.kml::latitude.text());
				
				var hospstaff:Vector.<Staff> = new Vector.<Staff>();
				var hstaffs:XMLList = xhosp.eren::staff;
				
				for each (var hstaff in hstaffs) {
					var hqn:QName = new QName(eren, "function");
					var hf:String = hstaff.descendants(hqn).text();
					var hm:Number = parseInt(hstaff.eren::min.text());
					var htar:Number = parseInt(hstaff.eren::target.text());
					var hc:Number = parseInt(hstaff.eren::current.text());
					
					var tempStaff:Staff = new Staff(hf, hm, htar, hc);
					hospstaff.push(tempStaff);
					//trace("Got hospital staff " + hf + ", min " + hm + ", target " + htar + ", current " + hc);
				}
				
				var hmID:String = xhosp.eren::manager.text();
				var hmanager:Person = people[hmID];
				var hcapacity:int = parseInt(xhosp.eren::capacity.text());
				var hfilled:int = parseInt(xhosp.eren::filled.text());
				
				trace("Got manager with tag " + hmID + ", found person " + hmanager);
				trace("Hospital has " + hfilled + " beds filled out of " + hcapacity);
				
				//...etc...//
				
				//Add facility status-to-icon mapping
				var hsti:Dictionary = new Dictionary();
				
				if (xhosp.hasOwnProperty(facilityIconsQName)) 
				{
					var hfacIcons:XMLList = xhosp.eren::facilityIcons.eren::facilityIcon;
					for each (var hfacIcon in hfacIcons) {
						var h_path:String = baseURL + hfacIcon.eren::path.text();
						var h_fstat:String = hfacIcon.eren::status.text();
						
						//Add entry
						hsti[h_fstat.toLowerCase()] = h_path;
					}
				}
				else
				{
					hsti["in_use"] = baseURL + "/resources/images/icons/hospital.png";
					hsti["available"] = baseURL + "/resources/images/icons/hospital.png";
					hsti["committed"] = baseURL + "/resources/images/icons/hospital.png";
					hsti["ready"] = baseURL + "/resources/images/icons/hospital.png";
					hsti["non_functional"] = baseURL + "/resources/images/icons/hospital.png";
				}
				
				var temphosp:Hospital = new Hospital(hid, hname, hstatus, hlat, hlng, hospstaff, hmanager, hcapacity, hfilled, hsti);
				_facilities.push(temphosp);
				
				hospitals[hid] = temphosp;
				
				//trace("Got hospital with ID: " + hid + ", name: " + hname + ", status: " + hstatus + ", avail: " + havail + ", location " + hlat + ", " + hlng);
			}
			
			trace("getting staff resources");
			//Staff resources
			var xstaffResources:XMLList = x.eren::people.eren::staff;
			for each (var xstaffResource in xstaffResources) {
				var sr_status:String = xstaffResource.eren::status.text();
				var sr_quantity:int = parseInt(xstaffResource.eren::quantity.text());
				
				var sr_funcQName:QName = new QName(eren, "function");				
				var sr_function:String = xstaffResource.descendants(sr_funcQName).text();
				
				var sr_displayName:String = xstaffResource.eren::displayName.text();
				
				var sr:StaffResource = new StaffResource(sr_status, sr_quantity, sr_function, sr_displayName);
				trace("Scenario file: processing staff resource " + sr_displayName);
				staffResources.add(sr_function, sr);
			}
		}
		
		public function get facilities():Vector.<facility>
		{
			return _facilities;
		}
		
		private function parseActions(e:Event):void
		{
			var xml:XML = new XML(xmlLoader.data);
			var actionsFile:ActionsFile = new ActionsFile(xml);
			this.actions = actionsFile;
			dispatchEvent(new ErenEvent(ErenEvent.READY));
		}
		
	}


}