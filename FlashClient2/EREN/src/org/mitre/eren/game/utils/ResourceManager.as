package org.mitre.eren.game.utils 
{
	import com.google.maps.InfoWindowOptions;
	import com.google.maps.MapMouseEvent;
	import com.google.maps.overlays.Marker;
	import com.google.maps.overlays.MarkerOptions;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.utils.Dictionary;
	import org.as3commons.collections.framework.core.LinkedMapIterator;
	import org.as3commons.collections.LinkedMap;
	import org.mitre.eren.Constants;
	import org.mitre.eren.data.actions.Action;
	import org.mitre.eren.data.actions.ActionsFile;
	import org.mitre.eren.data.scenario.EOC;
	import org.mitre.eren.data.scenario.facility;
	import org.mitre.eren.data.scenario.Hospital;
	import org.mitre.eren.data.scenario.POD;
	import org.mitre.eren.data.scenario.PODupdate;
	import org.mitre.eren.data.scenario.RDStatus;
	import org.mitre.eren.data.scenario.ResourceUsageStat;
	import org.mitre.eren.data.scenario.ResourceUsageStats;
	import org.mitre.eren.data.scenario.RSS;
	import org.mitre.eren.data.scenario.Staff;
	import org.mitre.eren.data.scenario.StaffResource;
	import org.mitre.eren.display.dialog.expander.ExpanderItemGroup;
	import org.mitre.eren.display.dialog.expander.ExpanderItemScrollBox;
	import org.mitre.eren.display.dialog.expander.IExpanderItem;
	import org.mitre.eren.display.dialog.expander.Resource_EI;
	import org.mitre.eren.display.elements.ImageLoader;
	import org.mitre.eren.display.map.ActionsMenu;
	import org.mitre.eren.display.map.resourceInfoWindow;
	import org.mitre.eren.display.resources.ResourceBar;
	import org.mitre.eren.events.ActionEvent;
	import org.mitre.eren.events.ExpanderItemEvent;
	
	/**
	 * Manages resources, as viewed in the Resources tab, displayed on the Map and
	 * interacted with via Info Windows on the Map.
	 * 
	 * @author Amanda Anganes
	 */
	public class ResourceManager extends MovieClip
	{
		private var markers:Dictionary;
		private var infoWindowOptions:Dictionary;
		private var infoWindows:Dictionary;
		private var facilities:Vector.<facility>;
		private var actions:ActionsFile;
		private var actionsMenus:Dictionary;
		private var theMap:MapUtil;
		private var roleId:String;
		
		private var facilityEIG:ExpanderItemGroup;
		private var facilityBox:ExpanderItemScrollBox;
		private var resourceBars:Dictionary;
		private var staff:LinkedMap;
		
		/** 
		 * Constant value for use in actions menu items, 
		 * used for the "view details" entry to trigger the 
		 * details info window to pop up, instead of actually
		 * committing an action. 
		 */
		public static const VIEWDETAILS_ACTIONID:String = "View details action id";
		
		/**
		 * Constructor
		 */
		public function ResourceManager() 
		{
			markers = new Dictionary();
			infoWindowOptions = new Dictionary();
			infoWindows = new Dictionary();
			facilities = new Dictionary();
			facilityEIG = new ExpanderItemGroup(Constants.FACILITIES_SCROLLBOX_SIZE.x, ExpanderItemGroup.SINGLE);
			actionsMenus = new Dictionary();
			resourceBars = new Dictionary();
		}
		
		/**
		 * Set the reference to the game's Map object. This is needed
		 * so that the resource manager can add facilities to the map.
		 * 
		 * @param	m the MapUtil object for this game
		 */
		public function setMap(m:MapUtil):void 
		{
			this.theMap = m;
		}
		
		/**
		 * Actions is a dictionary, indexed by facility id, 
		 * of dictionaries of Actions, indexed by action id.
		 * @param	actions
		 * @param	roleId
		 */
		public function setActions(actions:ActionsFile, roleId:String) 
		{
			trace("ResourceManager - setActions, my role = " + roleId);
			this.actions = actions;
			trace(actions.toString());
			this.roleId = roleId;
		}
		
		/**
		 * Initialize the facilities list. The manager must know about all facilities
		 * at the start of the game, whether they are in use or not. Therefore
		 * there is no public "addFacility(f)" method.
		 * This method should only be called once the RM has been created on the stage.
		 * Calling it before the RM has been added to the display list will result in error.
		 * 
		 * @param	a   an array of Facility objects to manage
		 */
		public function initFacilities(a:Vector.<facility>):void 
		{	
			for each (var f:facility in a) {
				addFacility(f);
				//TODO: move constructResourceTabString to resource_ei
				var r:Resource_EI = new Resource_EI(f.ID, constructResourceTabString(f), Constants.FACILITIES_SCROLLBOX_SIZE.x, Constants.EIG_PADDING);
				r.addEventListener(ExpanderItemEvent.RESOURCE_SELECTION, selectionEventHandler);
				facilityEIG.addItem(r);
			}
			
			facilityBox = new ExpanderItemScrollBox(Constants.FACILITIES_SCROLLBOX_SIZE, 
						Constants.DIALOG_SCROLLBAR_WIDTH, facilityEIG, Constants.DIALOG_SCROLLBOX_BUFFER, this.stage);
			addChild(facilityBox);
			facilityBox.x = Constants.FACILITIES_SCROLLBOX_POS.x;
			facilityBox.y = Constants.FACILITIES_SCROLLBOX_POS.y;
		}
		
		/**
		 * Update the POD facility with the given ID.
		 * 
		 * @param   id  ID of the POD to update
		 * @param	p   Object containing update parameters
		 */
		public function updatePOD(id:String, p:PODupdate):void 
		{
			var pod:POD = facilities[id] as POD;
			pod.hasEquipment = p.hasEq;
			pod.hasMeds = p.hasMds;
			pod.queueSize = p.qSize;
			pod.standardOfCare = p.soc;
			pod.throughput = p.tp;
			
			for each (var s:Staff in pod.staff) {
				if (s.staffFunction == "CLINICAL_STAFF") {
					s.current = p.medStaff;
				} else if (s.staffFunction == "LE_STAFF") {
					s.current = p.secStaff;
				} else if (s.staffFunction == "OPS_STAFF") {
					s.current = p.supStaff;
				}
			}
			
			//update infoWindowOptions dictionary
			var window:resourceInfoWindow = infoWindows[pod.ID] as resourceInfoWindow;
			window.updateContent(constructFacilityStatusString(pod));
			
			var r:Resource_EI = getResourceEiById(id);
			r.updateText(constructResourceTabString(pod));
			//trace("updated POD " + id + ", " + pod.Name);
		}
		
		/**
		 * Update the status of a facility. Updating the status also updates 
		 * the available actions in the facility's menu, if it has one, and 
		 * updates the facility's icon, if it has an icon corresponding to
		 * the new status.
		 * 
		 * @param	rds the RDStatus update message
		 */
		public function updateRDStatus(rds:RDStatus):void 
		{
			var fac:facility = facilities[rds.facilityID] as facility;
			//trace("Status update: " + rds.facilityID + " is now " + rds.status);
			if (fac.Status != translateStatusToEnglish(rds.status)) {
				fac.Status = translateStatusToEnglish(rds.status);
				var stat:String = rds.status.toLowerCase();
				var path:String = fac.StatusToIconMap[stat] as String;
				if (path && (path.length > 1)) {
					var il:ImageLoader = markers[rds.facilityID].getOptions().icon as ImageLoader;
					il.displayImage(path);
				}
				
				//Update actions menu
				var menu:ActionsMenu = actionsMenus[fac.ID];
				//Get this facilities set of actions
				var facilityActions:LinkedMap = actions.facilityToActionsMap;
				var facActions:LinkedMap = facilityActions.itemFor(fac.ID) as LinkedMap;
				var iterator:LinkedMapIterator = facActions.iterator() as LinkedMapIterator;
				while (iterator.hasNext()) {
					var act:Action = iterator.next();
					if (act.type != VIEWDETAILS_ACTIONID) {
						var thebool:Boolean = false;
						for each (var status:String in act.stateTriggers) {
							//trace("fac.Status = " + fac.Status + ", checking against " + translateStatusToEnglish(status));
							if (fac.Status == translateStatusToEnglish(status)) {
								thebool = true;
							}
						}
						menu.enableOption(thebool, act.type);
					}
				}
				menu.enableOption(true, VIEWDETAILS_ACTIONID);
			}
			
			var r:Resource_EI = getResourceEiById(rds.facilityID);
			r.updateText(constructResourceTabString(fac));
			
			var window:resourceInfoWindow = infoWindows[rds.facilityID] as resourceInfoWindow;
			window.updateContent(constructFacilityStatusString(fac));
		}
		
		/**
		 * Get the number available of a given staff type (corresponds to staffFunction enum in EREN_Messages)
		 * @param	staffType
		 * @return
		 */
		public function getAvailable(staffType:String):int
		{
			var sr:StaffResource = staff.itemFor(staffType);
			return sr.available;
		}
		
		/**
		 * Get the Resource_Ei with the given ID.
		 * 
		 * @param	id the ID
		 * @return the Resource_Ei
		 */
		private function getResourceEiById(id:String):Resource_EI 
		{
			var items:Vector.<IExpanderItem> = facilityEIG.expanderItems;
			for each (var e:IExpanderItem in items) {
				var r:Resource_EI = e as Resource_EI;
				if (r.facilityID == id) {
					return r;
				}
			}
			return null;
		}
		
		/**
		 * Initialize the staff stats. This creates ResourceBar widgets for
		 * each staff type given, and initializes their min and max values.
		 * 
		 * @param	s a LinkedMap holding StaffResource objects.
		 */
		public function initStaffStats(s:LinkedMap) 
		{
			this.staff = s;
			var iterator:LinkedMapIterator = s.iterator() as LinkedMapIterator;
			//trace("iterator = " + iterator);
			while(iterator.hasNext())
			{
				var sta:StaffResource = iterator.next() as StaffResource;
				//trace("Adding resource bar for " + sta.displayName);
				var rBar:ResourceBar = new ResourceBar(0, sta.quantity, sta.displayName, sta.staffFunction, 0, ResourceBar.RESOURCE_TYPE);
				resourceBars[sta.staffFunction] = rBar;
				rBar.x = Constants.RESOURCE_BAR_START_POS.x;
				rBar.y = Constants.RESOURCE_BAR_START_POS.y + ((ErenUtilities.getLength(resourceBars) - 1) * Constants.RESOURCE_BAR_OFFSET);
				addChild(rBar);
			}
		}
		
		/**
		 * Update the currently available resource visualizations.
		 * 
		 * @param	r
		 */
		public function updateResourceStats(r:ResourceUsageStats):void 
		{
			//trace("Resource Manager: update resource stats");
			for each (var sta in r.stats) 
			{
				//update bars
				var stat:ResourceUsageStat = sta as ResourceUsageStat;
				//trace("Attempting to update stats for staff type " + stat.type);
				var rBar:ResourceBar = resourceBars[stat.type];
				//trace("Resource bar = " + rBar);
				if (rBar != null) 
				{
					rBar.value = (stat.total - stat.available);
				}
				
				//update model
				if (staff.hasKey(stat.type)) {
					(staff.itemFor(stat.type) as StaffResource).available = stat.available;
				}
			}
			
		}
		
		/**
		 * Place a facility under the ResourceManager's management.
		 * 
		 * @param	f   the facility to add
		 */
		private function addFacility(f:facility):void 
		{
			//trace("Adding facility " + f.ID + ", " + f.Name);
			//add f to facilities, keyed by ID
			facilities[f.ID] = f;
			
			//create a marker for it			
			var markerOptions:MarkerOptions = new MarkerOptions();
			var nameString:String = (f is POD) ? "POD - " + f.Name : f.Name;
			
			var il:ImageLoader = new ImageLoader();
			var path:String = f.StatusToIconMap[f.Status.toLowerCase()];
			il.displayImage(path);
			markerOptions.icon = il;
			
			var hasAnyActions:Boolean = false;
			
			if (this.actions != null) 
			{
				trace("We have actions, processing");
				var menu:ActionsMenu = new ActionsMenu(f.ID);
				//Get this facilities set of actions
				var facilityActions:LinkedMap = actions.facilityToActionsMap;
				if (facilityActions.hasKey(f.ID)) 
				{
					var facActions:LinkedMap = facilityActions.itemFor(f.ID) as LinkedMap;
					trace("got map for facility id " + f.ID + ": " + facActions);
					var actionIterator:LinkedMapIterator = facActions.iterator() as LinkedMapIterator;
					trace("iterator " + actionIterator);
					while(actionIterator.hasNext())
					{
						var act:Action = actionIterator.next() as Action;
						trace("Processing action with type " + act.type + " and display name " + act.displayName);
						var thebool:Boolean = false;
						var roleBool:Boolean = false;
						for each (var status:String in act.stateTriggers) {
							if (f.Status == status) {
								thebool = true;
							}
						}
						for each (var role:String in act.roles) {
							trace("Action role = " + role + ", my role = " + roleId);
							if (role == this.roleId) {
								roleBool = true;
								trace("I can do this action");
							}
						}
						if (roleBool) 
						{
							f.hasAnyActions = true;
							trace("Resource manager - adding some options!");
							menu.addOption(act, thebool);
						}
					}
					
					menu.addOption(Action.createDummyAction(VIEWDETAILS_ACTIONID, "View Details"), true);
					menu.addEventListener(ActionEvent.FIRE_ACTION, onCommitAction, false, 0, true);
					menu.addEventListener(ActionEvent.GET_INPUT, onGetInput, false, 0, true);
					actionsMenus[f.ID] = menu;
				}
			}
			
			//markerOptions.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER;
			var m:Marker = new Marker(f.Location, markerOptions);
			
			m.addEventListener(MapMouseEvent.ROLL_OVER, function(e:Event):void {
				var riw:resourceInfoWindow = new resourceInfoWindow(nameString, nameString);
				var iwo:InfoWindowOptions = new InfoWindowOptions();
				iwo.customContent = riw;
				iwo.customOffset = new Point(20, riw.height);
				m.openInfoWindow(iwo);
				
				var m_o:MarkerOptions = new MarkerOptions();
				m_o.radius = 0;
				m.setOptions(m_o);
			});
			m.addEventListener(MapMouseEvent.ROLL_OUT, function(e:Event):void {
				if (m.getOptions().radius == 0) {
					theMap.getMap().closeInfoWindow();
				}
			});
			
			//store marker in markers dictionary,
			markers[f.ID] = m;
			
			//create info window for f
			//TODO: resourceInfoWindow => ResourceInfoWindow, move construct...string to RIW
			var riw:resourceInfoWindow = new resourceInfoWindow(f.ID, constructFacilityStatusString(f));
			riw.addEventListener(MouseEvent.CLICK, resourceInfoWindowClick);
			
			var iwo:InfoWindowOptions = new InfoWindowOptions();
			iwo.customContent = riw;
			iwo.customOffset = new Point(20, riw.height);
			
			//add options to infoWindowOptions dictionary
			infoWindowOptions[f.ID] = iwo;
			infoWindows[f.ID] = riw;
			
			//Add click event listener
			m.addEventListener(MapMouseEvent.CLICK, function(e:Event):void {
				var m_o:MarkerOptions = new MarkerOptions();
				m_o.radius = 1;
				m.setOptions(m_o);
				var iwo:InfoWindowOptions = infoWindowOptions[f.ID];
				if (f.hasAnyActions) {
					trace("Facility has actions, displaying!");
					iwo.customContent = actionsMenus[f.ID];
					iwo.customOffset = new Point(0, 0);
					m.openInfoWindow(iwo);
				}
				else {
					trace("Facility has NO actions!");
					iwo.customContent = infoWindows[f.ID];
					iwo.customOffset = new Point(20, (infoWindows[f.ID] as resourceInfoWindow).height);
					m.openInfoWindow(iwo);
				}
			});
			
			theMap.addOverlay(m);
		}
		
		/**
		 * Listener for the COMMIT_ACTION event. Check to see if 
		 * the action's type corresponds to VEWDETAILS_ACTIONID -
		 * this means that the user clicked on "view details" on
		 * their actions menu, and are not trying to actually 
		 * commit an action.
		 * 
		 * @param	e
		 */
		private function onCommitAction(e:ActionEvent):void 
		{
			if (e.action.type == VIEWDETAILS_ACTIONID) 
			{
				trace("Got commit action for VIEWDETAILS");
				//User clicked on "view details" for this facility
				raiseInfoWindow(e.facilityId, true);
			}
			else 
			{
				trace("Got commit action for other message");
				dispatchEvent(e);
			}
		}
		
		private function onGetInput(e:ActionEvent):void
		{
			dispatchEvent(e);
		}
		
		private function resourceInfoWindowClick(e:MouseEvent):void
		{
			var r:resourceInfoWindow = e.currentTarget as resourceInfoWindow;
			closeInfoWindow(r.getID());
		}
		
		/**
		 * Listener for Resource_EI's selection event.
		 * 
		 * @param e the ErenEvent associated with the selection
		 */
		private function selectionEventHandler(e:ExpanderItemEvent) 
		{
			if (e._selected) {
				raiseInfoWindow(e._facilityID, true);
			} else {
				closeInfoWindow(e._facilityID);
			}
		}
		
		/**
		 * Open a particular facility's info window.
		 * 
		 * @param	facilityID    the ID of the facility
		 */
		private function raiseInfoWindow(facilityID:String, viewDetails:Boolean = false ):void 
		{
			var mark:Marker = markers[facilityID];
			
			var iwo:InfoWindowOptions = infoWindowOptions[facilityID];
			if (viewDetails) 
			{
				iwo.customOffset = new Point(20, (infoWindows[facilityID] as resourceInfoWindow).height);
				iwo.customContent = infoWindows[facilityID];
			}
			else 
			{
				iwo.customOffset = new Point(0, 0);
				iwo.customContent = actionsMenus[facilityID];
			}
			
			mark.openInfoWindow(iwo);
		}
		
		private function closeInfoWindow(facilityID:String):void
		{
			var mark:Marker = markers[facilityID];
			mark.closeInfoWindow();
		}
		
		/**
		 * Construct a content string for f's infoWindow.
		 * 
		 * @param	f   the facility
		 * @return the constructed string
		 */
		private function constructFacilityStatusString(f:facility):String 
		{
			var result:String = "";
			
			if (f is EOC) 
			{	
				result = "Status: " + translateStatusToEnglish(f.Status);
			}
			else if (f is Hospital)
			{
				var hosp:Hospital = f as Hospital;
				result = "Status: " + translateStatusToEnglish(hosp.Status) + "\n";
				for each (var st:Staff in hosp.staff) 
				{
					result += staffFunctionToEnglish(st.staffFunction) + "\n\tCurrent: " + st.current + "\n\tTarget: " + 
					st.target + "\n\tMin: " + st.min + "\n";
				}
				result +=  "Maximum capacity: " + hosp.capacity + "\nFilled beds: " + hosp.filled;
			}
			else if (f is RSS)
			{
				var rss:RSS = f as RSS;
				result = "Status: " + translateStatusToEnglish(rss.Status) + "\n";
				for each (var rst:Staff in rss.staff) 
				{
					result += staffFunctionToEnglish(rst.staffFunction) + "\n\tCurrent: " + rst.current + "\n\tTarget: " + 
					rst.target + "\n\tMin: " + rst.min + "";
				}
			}
			else if (f is POD)
			{
				var pod:POD = f as POD;
				result = "Status: " + translateStatusToEnglish(pod.Status) + "\n";
				for each (var sta:Staff in pod.staff) {
					result += staffFunctionToEnglish(sta.staffFunction) + "\n\tCurrent: " + sta.current + "\n\tTarget: " + 
					sta.target + "\n\tMin: " + sta.min + "\n";
				}
				result += "Queue size: " + pod.queueSize + "\nThroughput: " + pod.throughput + 
				"\nHas meds: " + pod.hasMeds.toString() + "\nHas equipment: " + pod.hasEquipment.toString() +
				"\nStandard of care: " + pod.standardOfCare;
			}
			
			return result;
		}
		
		
		/**
		 * Construct a content string for f's EIG entry.
		 * 
		 * @param	f   the facility
		 * @return the constructed string
		 */
		private function constructResourceTabString(f:facility):String 
		{
			var result:String = "";
			
			if (f is EOC) 
			{	
				result = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\"><span fontWeight=\"bold\">     " 
				+ f.Name + " </span><span>\nStatus: " + translateStatusToEnglish(f.Status) + "</span></TextFlow>";
			}
			else if (f is Hospital)
			{
				var hosp:Hospital = f as Hospital;
				result = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\"><span fontWeight=\"bold\">     " 
					+ hosp.Name + " </span><span>\nStatus: " + translateStatusToEnglish(hosp.Status) + "\n";
					
				for each (var st:Staff in hosp.staff) 
				{
					result += staffFunctionToEnglish(st.staffFunction) + "\n\tCurrent: " + st.current + "\n\tTarget: " + 
					st.target + "\n\tMin: " + st.min + "\n";
				}
				result += "Maximum capacity: " + hosp.capacity + "\nFilled beds: " + hosp.filled + "</span></TextFlow>";
			}
			else if (f is RSS)
			{
				var rss:RSS = f as RSS;
				result = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\"><span fontWeight=\"bold\">     " 
					+ rss.Name + " </span><span>\nStatus: " + translateStatusToEnglish(rss.Status) + "\n";
					
				for each (var rst:Staff in rss.staff) 
				{
					result += staffFunctionToEnglish(rst.staffFunction) + "\n\tCurrent: " + rst.current + "\n\tTarget: " + 
					rst.target + "\n\tMin: " + rst.min + "\n";
				}
				result += "</span></TextFlow>";
			}
			else if (f is POD)
			{
				var pod:POD = f as POD;
				result = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\"><span fontWeight=\"bold\">     POD - " 
					+ pod.Name + " </span><span>\nStatus: " + translateStatusToEnglish(pod.Status) + "\n";
				
				for each (var sta:Staff in pod.staff) 
				{
					result += staffFunctionToEnglish(sta.staffFunction) + "\n\tCurrent: " + sta.current + "\n\tTarget" + 
					sta.target + "\n\tMin: " + sta.min + "\n";
				}
				result += "Queue size: " + pod.queueSize + "\nThroughput: " + pod.throughput + 
				"\nHas meds: " + pod.hasMeds.toString() + "\nHas equipment: " + pod.hasEquipment.toString() +
				"\nStandard of care: " + pod.standardOfCare + "</span></TextFlow>";
			}
			
			return result;
		}
		
		/**
		 * Translate the given status string into plain English
		 * 
		 * @param	s  the status to translate
		 * @return the translated status
		 */
		private function translateStatusToEnglish(s:String):String
		{
			if (s == "IN_USE") 
			{
				return "Currently in use";
			} 
			else if (s == "AVAILABLE") 
			{
				return "Available";
			} 
			else if (s == "COMMITTED") 
			{
				return "Committed for use";
			} 
			else if (s == "NON_FUNCTIONAL") 
			{
				return "Riot";
			} 
			else if (s == "REQUISITIONED") 
			{
				return "Requisitioned";
			} 
			else if (s == "READY") 
			{
				return "Ready to be used";
			} 
			else if (s == "RELEASED") 
			{
				return "Released";
			}
			else 
			{
				return s;
			}
		}
		
		/**
		 * Translate a staff function enum into plain English
		 * 
		 * @param	s the status to translate
		 * @return the translated status
		 */
		private function staffFunctionToEnglish(s:String):String
		{
			if (s == "CLINICAL_STAFF") 
			{
				return "Clinical Staff";
			}
			else if (s == "LE_STAFF")
			{
				return "Law Enforcement Staff";
			}
			else if (s == "OPS_STAFF") 
			{
				return "Operational Staff";
			}
			else return s;
		}
		
		/**
		 * Get the vector of facilities under the RM's management.
		 * 
		 * @return the facilities Vector
		 */
		public function getFacilities():Vector.<facility>
		{
			return facilities;
		}
		
	}

}