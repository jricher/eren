package org.mitre.eren.game.utils
{
	import com.distriqt.gmaps.kml.utils.SimpleKmlLoader;
	import com.google.maps.interfaces.IPaneManager;
	import com.google.maps.LatLng;
	import flash.display.MovieClip;
	import flash.events.MouseEvent;
	import flash.filters.DropShadowFilter;
	import org.mitre.eren.Constants;
	import org.mitre.eren.data.dialog.KmlLayer;
	import org.mitre.eren.data.scenario.facility;
	import org.mitre.eren.data.scenario.Hospital;
	import org.mitre.eren.data.scenario.POD;
	import org.mitre.eren.data.scenario.ScenarioFile;
	import org.mitre.eren.display.dialog.DialogWindow;
	import org.mitre.eren.display.dialog.notifications.Notification;
	import org.mitre.eren.display.map.LayerControl;
	import org.mitre.eren.events.ActionEvent;
	import org.mitre.eren.events.ErenEvent;
	import org.mitre.eren.events.MapEvent;
	import org.mitre.eren.events.MessageCenterEvent;
	import org.mitre.eren.events.SendMessageEvent;
	import org.mitre.eren.game.Engine;
	import org.mitre.eren.game.Main;
	import org.mitre.eren.game.utils.MssgQueue;
	
	/**
	 * The MessageCenterManager manages the Message Center (instantiated on Stage).
	 * The Message Center includes a MssgQueue of incoming messages, a window for viewing 
	 * and replying to open messages, a chat window (stub, not implemented), and a 
	 * resource vizualization panel.
	 * 
	 * @see org.mitre.eren.game.utils.MssgQueue.as
	 * @see org.mitre.eren.game.utils.ResourceManager.as
	 * @see org.mitre.eren.display.dialog.DialogWindow.as
	 * 
	 * @author Amanda Anganes
	 **/
	public class MessageCenterManager extends MovieClip
	{
		
		/// Enum ///
		public static const RESOURCE_TAB:int = 0;
		public static const MESSAGING_TAB:int = 1;
		public static const CHAT_TAB:int = 2;
		
		/// Components ///
		private var mainInstance:Main;
		private var engine:Engine;
		private var dialogManager:DialogWindow;
		private var mssgQueue:MssgQueue;
		private var _resourceManager:ResourceManager;
		private var theMap:MapUtil;
		private var chatTab:MovieClip;
		private var mssgTabBttn:MovieClip;
		private var chatTabBttn:MovieClip;
		private var rscTabBttn:MovieClip;
		private var scenFile:ScenarioFile;
		private var paneManager:IPaneManager;
		private var layerControl:LayerControl;
		
		/// State ///
		public var hasBeenInited:Boolean = false;
		private var roleId:String;
		
		private var selectedDropShadow:DropShadowFilter;
		private var deselectedDropShadow:DropShadowFilter;
		
		/**
		 * Constructor. Initialize the selected and deselected tab drop shadow filters.
		 */
		public function MessageCenterManager() 
		{ 
			selectedDropShadow = new DropShadowFilter();
			selectedDropShadow.distance = 2;
			selectedDropShadow.angle = 225;
			selectedDropShadow.color = 0x999999;
			selectedDropShadow.alpha = 1;
			selectedDropShadow.blurX = 4;
			selectedDropShadow.blurY = 4;
			selectedDropShadow.inner = false;
			
			deselectedDropShadow = new DropShadowFilter();
			deselectedDropShadow.distance = 3;
			deselectedDropShadow.angle = 225;
			deselectedDropShadow.color = 0x000033;
			deselectedDropShadow.alpha = 1;
			deselectedDropShadow.blurX = 20;
			deselectedDropShadow.blurY = 20;
			deselectedDropShadow.inner = true;
			deselectedDropShadow.strength = .7;
		}
		
		/**
		 * Initialize the MCM.
		 * 
		 * @param	s        the scenario file
		 * @param	map      the MapUtil instance created by the Engine
		 * @param	roleId   the player's role id
		 */
		public function init(s:ScenarioFile, map:MapUtil, roleId:String):void 
		{	
			_resourceManager = getChildByName("resourceTab_mc") as ResourceManager;
			dialogManager = getChildByName("messagingTab_mc") as DialogWindow;
			chatTab = getChildByName("chatTab_mc") as MovieClip;
			mssgQueue = getChildByName("mssgQ") as MssgQueue;
			mssgTabBttn = getChildByName("mssgTab") as MovieClip;
			chatTabBttn = getChildByName("cTab") as MovieClip;
			rscTabBttn = getChildByName("rscTab") as MovieClip;
			
			mssgTabBttn.addEventListener(MouseEvent.CLICK, mssgTabClick);
			chatTabBttn.addEventListener(MouseEvent.CLICK, chatTabClick);
			rscTabBttn.addEventListener(MouseEvent.CLICK, rscTabClick);
			
			theMap = map;
			mainInstance = main;
			scenFile = s;
			this.roleId = roleId;
			dialogManager.addEventListener(MapEvent.DISPLAY_KML, this.handleMapEvent);
			
			mssgQueue.setMain(mainInstance);
			//Listen for events on the message queue
			mssgQueue.addEventListener(MessageCenterEvent.MESSAGE_CENTER_EVENT, this.handleMCMEvent);
			
			initDW();
			initMap();
			//initRM gets called after the map is done loading (fires ready event)
			
			bringToTop(MESSAGING_TAB);
			//TODO: Check who is using this
			hasBeenInited = true;
			
			//TODO: does this go somewhere else?
			layerControl = new LayerControl();
			addChild(layerControl);
			layerControl.visible = false;
			
			//Hook up listeners for action events
			_resourceManager.addEventListener(ActionEvent.GET_INPUT, dialogManager.getInput);
			_resourceManager.addEventListener(ActionEvent.FIRE_ACTION, engine.fireCommitActionHandler);
			dialogManager.addEventListener(ActionEvent.FIRE_ACTION, engine.fireCommitActionHandler, false, 0, true);
		}
		
		/**
		 * Bring the specified tab to the top
		 * 
		 * @param	i   enum value specifying which tab 
		 * 				to bring to the top
		 */
		public function bringToTop(i:int):void 
		{
			switch (i) 
			{
				//TODO: change tabs to coded movie clips rather than doing this
				case RESOURCE_TAB: 
				//change all frames to be named rather than numbered
					this.setChildIndex(_resourceManager, this.numChildren - 1);
					mssgTabBttn.gotoAndStop(2);
					mssgTabBttn.filters = new Array(deselectedDropShadow);
					dialogManager.visible = false;
					chatTabBttn.gotoAndStop(2);
					chatTabBttn.filters = new Array(deselectedDropShadow);
					chatTab.visible = false;
					rscTabBttn.gotoAndStop(1);
					rscTabBttn.filters = new Array(selectedDropShadow);
					_resourceManager.visible = true;
					break;
				case MESSAGING_TAB:
					this.setChildIndex(dialogManager, this.numChildren - 1);
					mssgTabBttn.gotoAndStop(1);
					mssgTabBttn.filters = new Array(selectedDropShadow);
					dialogManager.visible = true;
					chatTabBttn.gotoAndStop(2);
					chatTabBttn.filters = new Array(deselectedDropShadow);
					chatTab.visible = false;
					rscTabBttn.gotoAndStop(2);
					rscTabBttn.filters = new Array(deselectedDropShadow);
					_resourceManager.visible = false;
					break;
				case CHAT_TAB:
					this.setChildIndex(chatTab, this.numChildren - 1);
					mssgTabBttn.gotoAndStop(2);
					mssgTabBttn.filters = new Array(deselectedDropShadow);
					dialogManager.visible = false;
					chatTabBttn.gotoAndStop(1);
					chatTabBttn.filters = new Array(selectedDropShadow);
					chatTab.visible = true;
					rscTabBttn.gotoAndStop(2);
					rscTabBttn.filters = new Array(deselectedDropShadow);
					_resourceManager.visible = false;
			}
		}
		
		private function initDW():void 
		{
			//Listen for events from the dialog window, firing 
			//them back to Engine if necessary
			dialogManager.addEventListener(SendMessageEvent.SEND, sendMessage);
			dialogManager.addEventListener(MessageCenterEvent.MESSAGE_CENTER_EVENT, handleMCMEvent);
			dialogManager.addEventListener(MapEvent.MAP_EVENT, handleMapEvent);
			
			dialogManager.setBaseURL(scenFile.baseURL);
			dialogManager.setPeopleDictionary(scenFile.people);
		}
		
		private function initRM(roleId:String):void 
		{		
			trace("Init Resource Manager, from MCM");
			_resourceManager.setMap(theMap);
			_resourceManager.setActions(scenFile.actions, roleId);
			_resourceManager.initFacilities(scenFile.facilities);
			_resourceManager.initStaffStats(scenFile.staffResources);
			dialogManager.setResourceManager(_resourceManager);
		}
		
		private function initMap():void
		{	
			theMap.initializeMap(scenFile.scenarioLocation.loc);
			theMap.addEventListener(MapEvent.MAP_EVENT, handleMapEvent);
		}
		
		/**
		 * Handle events meant for the MCM. Public so the Engine can wire this up
		 * for us.
		 * 
		 * @param	e
		 */
		public function handleMCMEvent(e:MessageCenterEvent):void
		{
			if (e._type == MessageCenterEvent.GOT_USER_MSSG) 
			{
				mssgQueue.add(e._userMssg, true);
			}
			else if (e._type == MessageCenterEvent.GOT_PODSTATUS) 
			{
				_resourceManager.updatePOD(e._podUpdate.id, e._podUpdate);
			}
			else if (e._type == MessageCenterEvent.GOT_RDSTATUS) 
			{
				_resourceManager.updateRDStatus(e._rdStatus);			
			}
			else if (e._type == MessageCenterEvent.DESELECT_NOTIFICATION) 
			{
				trace("Deselecting notification...");
				var n:Notification = mssgQueue.findByID(e._userMssg.getID());
				if (n == null) 
				{
					mssgQueue.add(e._userMssg, false);
				}
				else
				{
					mssgQueue.deselectNotification(n);
				}
			}
			else if (e._type == MessageCenterEvent.DISPLAY_USER_MSSG)
			{
				bringToTop(MESSAGING_TAB);
				dialogManager.displayMessage(e._userMssg);
			}
			else if (e._type == MessageCenterEvent.GOT_RESOURCE_USAGE_STATS)
			{
				_resourceManager.updateResourceStats(e._resourceStats);
			}
		}
		
		private function sendMessage(e:SendMessageEvent):void 
		{	
			mssgQueue.remove(mssgQueue.findByID(e._messageID));
			dispatchEvent(e);
		}
		
		private function handleMapEvent(e:MapEvent):void
		{
			if (e._type == MapEvent.DISPLAY_KML) 
			{
				displayKML(e._kmlLayers);
			}
			else if (e._type == MapEvent.FLY_TO_LOCATION) 
			{
				showLocation(e._facID);
			}
			else if (e._type == MapEvent.MAP_READY)
			{
				if (scenFile != null) 
				{
					trace(scenFile.scenarioLocation.loc.lat() + ", " + scenFile.scenarioLocation.loc.lng());
					theMap.flytocenter(scenFile.scenarioLocation.loc);
					
					initRM(roleId);	
				} 
				fireReady();
				paneManager = theMap.getMap().getPaneManager();
			}
		}
		
		private function showLocation(facilityID:String):void 
		{		
			var fac:facility = _resourceManager.getFacilities[facilityID];
			
			var newloc:LatLng = new LatLng(fac.Location.lat(), fac.Location.lng() + .04);
			theMap.setZoom(Constants.DEFAULT_ZOOM_LEVEL + 2);
			theMap.flytocenter(newloc);
		}
		
		private function displayKML(layers:Vector.<KmlLayer>):void 
		{
			//TODO: this doesn't work
			layerControl.clear();
			
			layerControl.visible = true;
			
			for each (var k:KmlLayer in layers)
			{
				//TODO: mapUtil.createPane() instead
				k.pane = paneManager.createPane();
				k.setMap(theMap.getMap());
				k.loadKML();
				layerControl.addLayer(k);
			}
			layerControl.x = -20 - layerControl.width;
			layerControl.y = 12.75;
		}
		
		private function fireReady():void 
		{
			dispatchEvent(new ErenEvent(ErenEvent.READY));
		}
		
		private function mssgTabClick(e:MouseEvent):void 
		{
			bringToTop(MESSAGING_TAB);
		}
		
		private function chatTabClick(e:MouseEvent):void 
		{
			bringToTop(CHAT_TAB);
		}
		
		private function rscTabClick(e:MouseEvent):void 
		{
			bringToTop(RESOURCE_TAB);
		}
		
		/**
		 * Allow other to access the resource manager
		 */
		public function get resourceManager():ResourceManager
		{
			return this._resourceManager;
		}
		
	}
}