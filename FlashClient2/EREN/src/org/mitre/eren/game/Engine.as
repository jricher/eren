package org.mitre.eren.game 
{
	import fl.controls.ComboBox;
	import fl.text.TLFTextField;
	import flash.display.InteractiveObject;
	import flash.display.MovieClip;
	import flash.events.DataEvent;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IOErrorEvent;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.XMLSocket;
	import flash.text.TextFormat;
	import flash.ui.Keyboard;
	import flash.utils.Dictionary;
	import flash.utils.Timer;
	import org.mitre.eren.Constants;
	import org.mitre.eren.data.game.Game;
	import org.mitre.eren.data.game.Score;
	import org.mitre.eren.data.scenario.RoleObject;
	import org.mitre.eren.data.scenario.ScenarioFile;
	import org.mitre.eren.data.scenario.ScenarioListItem;
	import org.mitre.eren.display.elements.EREN_Button;
	import org.mitre.eren.display.elements.EREN_Button2;
	import org.mitre.eren.display.elements.ImageLoader;
	import org.mitre.eren.display.elements.newBttn;
	import org.mitre.eren.display.elements.selector.GameSelector;
	import org.mitre.eren.display.elements.selector.RoleSelector;
	import org.mitre.eren.display.elements.selector.ScenarioSelector;
	import org.mitre.eren.display.elements.selector.SelectorBar;
	import org.mitre.eren.display.elements.selector.SelectorGroup;
	import org.mitre.eren.display.map.timeline.Timeline;
	import org.mitre.eren.events.ActionEvent;
	import org.mitre.eren.events.ErenEvent;
	import org.mitre.eren.events.GameEvent;
	import org.mitre.eren.events.MessageCenterEvent;
	import org.mitre.eren.events.SendMessageEvent;
	import org.mitre.eren.events.TimeEvent;
	import org.mitre.eren.game.utils.ERENParser;
	import org.mitre.eren.game.utils.ErenUtilities;
	import org.mitre.eren.game.utils.MapUtil;
	import org.mitre.eren.game.utils.MessageCenterManager;

	/**
	 * Game engine for EREN.  
	 * 
	 * @author Amanda Anganes
	 */
	public class Engine extends EventDispatcher
	{
		/** Components **/
		private var myMap:MapUtil;
		private var myTimeline:Timeline;
		private var myParser:ERENParser;
		private var myMCM:MessageCenterManager;
		private var mySocket:XMLSocket;
		private var myMain:Main; //Reference to the main movie, for playhead control ONLY
		
		/** State **/
		private var socketIsConnected:Boolean = false;
		private var usingBus:Boolean = false;
		private var callbacks:Dictionary;
		private var theHost:String;
		private var thePort:Number;
		private var theMapKey:String;
		//For some reason frame 6 gets constructed many times.  Make sure we do initialization only on the first time.
		private var firstsix:Boolean = true;
		private var creatingNewGame:Boolean = false;
		
		private var loadingTimer:Timer;
		private var gotgamestart:Boolean = false;
		private var timerTimedOut:Boolean = false;
		
		/** User data **/
		private var username:String = "";
		private var role:String = "";
		
		/** Scenario information **/
		private var scenarioFile:ScenarioFile;
		private var scenarioArray:Vector.<ScenarioListItem> = new Vector.<ScenarioListItem>();
		private var gameArray:Vector.<Game> = new Vector.<Game>();

		private var roles:Dictionary = new Dictionary();
		private var roleID:String = ""; //reference to the role ID string for the selected role
		
		/** Scenario selection **/
		private var scenarioListGroup:SelectorGroup;
		private var newGameScenario:ScenarioListItem;
		private var newGameName:String;
		
		/** Game selection **/
		private var game:Game;
		private var haveGameList:Boolean = false; //This will be true once we recieve the first game list - subsequent game lists are updates
		private var numGameListsRecieved:Number = 0;
		private var gameListGroup:SelectorGroup;
		
		/** Role selection **/
		private var roleListGroup:SelectorGroup;
		private var roleDesc_txt:TLFTextField;
		private var roleBriefing_txt:TLFTextField;
		private var roleImgLoader:ImageLoader;
		
		/** Stage buttons **/
		private var login_bttn:EREN_Button;
		private var select_bttn:EREN_Button;
		private var back_bttn:EREN_Button;
		private var continue_bttn:EREN_Button;
		private var start_bttn:EREN_Button;
		private var new_bttn:newBttn;
		
		/** other stage objects **/
		private var networkError_mc:MovieClip;
		private var username_txt:TLFTextField;
		private var connection_mc:MovieClip;
		private var scenario_mc:MovieClip;
		private var role_box:ComboBox;
		private var desc_txt:TLFTextField;
		private var sImage:ImageLoader;
		private var info_mc:MovieClip;
		private var scoreField:TLFTextField;
		
		private var scenPicImgLoader:ImageLoader;
		
		private var loadingOverlay_mc:MovieClip;
		private var gameCreationOverlay:MovieClip;
		
		private var regularWhiteFormat:TextFormat;
		private var regularBlueFormat:TextFormat;
		
		/**
		 * Constructor. Constructing the Engine initializes all other game components,
		 * and sets up the UI to begin playing games.
		 * 
		 * @param	m       reference to Main
		 * @param	key     Google Maps API key
		 * @param	usebus  Boolean, used for debugging. If false, the UI will not attempt to make a bus connection.
		 * @param	host    URL of the server host
		 * @param	port    port number to connect to on the server host
		 */
		public function Engine(m:Main, key:String, usebus:Boolean, host:String, port:Number) 
		{
			superTrace("engine constructed");
			
			myMain = m;
			theHost = host;
			thePort = port;
			theMapKey = key;
			
			/** Start on login screen **/
			gotoFrame("login");
			
			usingBus = usebus;
			
			//The XML parser 
			myParser = new ERENParser();
			myParser.addEventListener(TimeEvent.TIME_EVENT, this.processTimeEvent);
			myParser.addEventListener(GameEvent.GAME_EVENT, this.processGameEvent);
		}
		
		/** Scene transition logic **/
		
		private function gotoFrame(s:String):void 
		{
			myMain.gotoAndStop(s);
			myMain.addEventListener(Event.FRAME_CONSTRUCTED, onEnterFrame);
		}
		
		private function onEnterFrame(e:Event) : void 
		{
			superTrace("frame " + myMain.currentFrameLabel + " was constructed");
			myMain.removeEventListener(Event.FRAME_CONSTRUCTED, onEnterFrame);
			if (myMain.currentFrameLabel == "login") 
			{
				onSceneLogin();
			} 
			else if (myMain.currentFrameLabel == "scenarioselect") 
			{
				onSceneChooseGame();
			}
			else if (myMain.currentFrameLabel == "roleselect")
			{
				onSceneRole();
			} 
			else if (myMain.currentFrameLabel == "gameplay") 
			{
				if (firstsix) 
				{
					onSceneGameplay();
					firstsix = false;
				}
			}			
			else 
			{
				superTrace("I don't have an init function for this frame: " + myMain.currentFrameLabel);
			}
		}
		
		/**
		 * Scene 1: Login page
		 */	
		internal function onSceneLogin(): void 
		{
			networkError_mc = myMain.getChildByName("networkError_mc") as MovieClip;
			networkError_mc.visible = false;
			login_bttn = myMain.getChildByName("login_bttn") as EREN_Button;
			login_bttn.setContent("login");
			login_bttn.setEnabled();
			
			connection_mc = myMain.getChildByName("connecting_mc") as MovieClip;
			
			var field:MovieClip = myMain.getChildByName("unField") as MovieClip;		
			field.addEventListener(MouseEvent.CLICK, usernameClick);
			//Following is a funky trick to attach a key listener to a single-line TLFTextField so that
			//it can receive the event when the enter key is pressed.  If you attach the key listener directly
			//to the TLFTextField, it will not receive anything when the enter key is pressed.
			var txt:TLFTextField = field.un_txt;
			InteractiveObject(txt.getChildAt(1)).addEventListener(KeyboardEvent.KEY_DOWN, onUsernameKey, false, 0, true);
			
			//Hide the login button until we have socket connection
			login_bttn.visible = false;
			login_bttn.addEventListener(MouseEvent.CLICK, loginClicked, false, 0, true);
			
			if (usingBus) 
			{
				try 
				{
					mySocket = new XMLSocket(theHost, thePort);
					mySocket.addEventListener(Event.CONNECT, connected);
					mySocket.addEventListener(DataEvent.DATA, onXMLData);
					mySocket.addEventListener(SecurityErrorEvent.SECURITY_ERROR, onTimeout);
					mySocket.addEventListener(IOErrorEvent.IO_ERROR, onTimeout);
				} 
				catch (e:Error) 
				{
					superTrace("caught networking error!");
					superTrace("error = " + e.message);
					networkError_mc.visible = true;
				}
			}
		}
		
		/**
		 * Scene 2: Game selection
		 */	
		private function onSceneChooseGame(): void 
		{
			trace("Entered game selection frame");
			gameCreationOverlay= myMain.getChildByName("newGameOverlay_mc") as MovieClip;
			gameCreationOverlay.visible = false;
			
			select_bttn = myMain.getChildByName("select_bt") as EREN_Button;
			select_bttn.setContent("select");
			
			new_bttn = myMain.getChildByName("newBttn") as newBttn;
			new_bttn.setEnabled();
			new_bttn.addEventListener(MouseEvent.CLICK, createNewGame, false, 0, true);
			
			//Don't do this until the game list has been populated
			//select_bttn.setEnabled();
			
			//Populate game items if we have them already
			if (gameArray.length >= 1) 
			{
				populateGameList(gameArray);
			}
			
			select_bttn.addEventListener(MouseEvent.CLICK, selectClicked, false, 0, true);
			
			username_txt = myMain.getChildByName("username_txt") as TLFTextField;
			username_txt.text = username;
		}
		
		/**
		 * Populate the game list
		 * 
		 * @param	glist the list of games
		 */
		private function populateGameList(glist:Vector.<Game>) 
		{
			this.gameArray = glist;
			
			superTrace("Populating game list");
			
			gameListGroup = new SelectorGroup();
			
			var i:int = 0;
			for each (var game:Game in glist) 
			{
				var gs:GameSelector = new GameSelector();
				gs.build(game, i);
				
				gs.x = Constants.GAME_BAR_POS.x;
				gs.y = Constants.GAME_BAR_POS.y + (i * gs.height);
				
				gameListGroup.add(gs);
				gs.addEventListener(MouseEvent.CLICK, gameSelectorClick, false, 0, true);
				myMain.addChild(gs);
				
				if (i == 0) 
				{
					//first item starts out selected
					gs.selected = true;
					
					//Display the picture for the first item					
					var imgLoader:ImageLoader = myMain.getChildByName("scenpic_mc") as ImageLoader;
					imgLoader.displayImage(game.scenario.scenPicURL);
					
					//Display the description for the first item
					var scenDesc_txt:TLFTextField = myMain.getChildByName("scenDesc_txt") as TLFTextField;
					scenDesc_txt.text = game.scenario.description;
				}
				i++;
			}
			
			if (glist != null && glist.length >= 1) 
			{
				select_bttn.setEnabled();
			}
		}
		
		private function gameSelectorClick(e:MouseEvent):void
		{
			var gs:GameSelector = e.currentTarget as GameSelector;
			var imgLoader:ImageLoader = myMain.getChildByName("scenpic_mc") as ImageLoader;
			imgLoader.displayImage(gs.game.scenario.scenPicURL);
			var scenDesc_txt:TLFTextField = myMain.getChildByName("scenDesc_txt") as TLFTextField;
			scenDesc_txt.text = gs.game.scenario.description;
		}
		
		/**
		 * Update the game list. Check for updated, added, and deleted games.
		 * 
		 * @param	newlist the new list of games
		 */
		private function updateGameList(newlist:Vector.<Game>):void
		{
			var currentGames:Vector.<SelectorBar> = gameListGroup.getItems();
			
			//Check for new games and update existing ones
			for each (var newgame:Game in newlist) 
			{
				trace("updating game list - looking for " + newgame.gameName + " with id " + newgame.gameID);
				var found:Boolean = false;
				for each (var sb:SelectorBar in currentGames) 
				{
					var gs:GameSelector = sb as GameSelector;
					trace("Checking against " + gs.game.gameName + ", " + gs.game.gameID);
					if (gs.game.gameID == newgame.gameID) 
					{
						//same game, do an update
						gs.updatePlayers(newgame);
						found = true;
					}
				}
				if (!found) 
				{
					//this is a new game, add it
					trace("Adding new game");
					var gse:GameSelector = new GameSelector();
					var newIndex:int = gameListGroup.getNumItems() + 1;
					
					if (newIndex == 1) 
					{
						gse.selected = true;
						//Display the picture for the first item					
						var imgLoader:ImageLoader = myMain.getChildByName("scenpic_mc") as ImageLoader;
						imgLoader.displayImage(newgame.scenario.scenPicURL);
						
						//Display the description for the first item
						var scenDesc_txt:TLFTextField = myMain.getChildByName("scenDesc_txt") as TLFTextField;
						scenDesc_txt.text = newgame.scenario.description;
					} 
					else 
					{
						gse.selected = false;
					}
					
					gse.build(newgame, newIndex);
					gse.x = Constants.GAME_BAR_POS.x;
					gse.y = Constants.GAME_BAR_POS.y + ((gse.index - 1) * gse.height);
					trace("build and positioned new game selector");
					gameListGroup.add(gse);
					gse.addEventListener(MouseEvent.CLICK, gameSelectorClick, false, 0, true);
					myMain.addChild(gse);
					
					//Ensure the gamecreationoverlay is on top
					trace("adding overlay again");
					gameCreationOverlay= myMain.getChildByName("newGameOverlay_mc") as MovieClip;
					myMain.addChild(gameCreationOverlay);
				}
			}
			
			//Check for deleted games
			for each (var currentGame:GameSelector in currentGames) 
			{
				found = false;
				for each (var ng:Game in newlist) 
				{
					trace("looking for deleted games");
					if (currentGame.game.gameID == ng.gameID) 
					{
						found = true;
					}
				}
				if (!found) 
				{
					trace("Removing deleted game");
					//This item no longer exists; delete it
					gameListGroup.removeItem(currentGame);
				}
			}
			trace("end updateGameList");
			
			if (newlist != null && newlist.length >= 1) 
			{
				select_bttn.setEnabled();
			} 
		}
		
		/**
		 * Create a new game. Raises the game creation overlay to the top of the 
		 * visual stack, and makes it visible. The user interacts with the overlay
		 * to choose the options for their new game.
		 * 
		 * @param	e
		 */
		private function createNewGame(e:MouseEvent):void
		{
			gameCreationOverlay.visible = true;
			//TODO: this is a hack to make sure the overlay STAYS on top, even though the 
			//game list may be updating in the background (and thus adding more MCs which 
			//will automatically be placed at the top).
			myMain.addChild(gameCreationOverlay);
			
			var createBttn_mc:EREN_Button2 = gameCreationOverlay.getChildByName("create_bttn") as EREN_Button2;
			createBttn_mc.setContent("Create");
			createBttn_mc.setEnabled();
			createBttn_mc.addEventListener(MouseEvent.CLICK, createClicked, false, 0, true);
			
			var cancel_bttn:EREN_Button2 = gameCreationOverlay.getChildByName("cancel_bttn") as EREN_Button2;
			cancel_bttn.setContent("Cancel");
			cancel_bttn.setEnabled();
			cancel_bttn.addEventListener(MouseEvent.CLICK, onCancelClick, false, 0, true);
			
			creatingNewGame = true;
			scenarioListGroup = new SelectorGroup();
			
			sendMssg(myParser.createScenarioListRequest());
			
		}
		
		/**
		 * Populate the scenario list chooser for game creation.
		 * 
		 * @param	list a Vector of ScenarioListItem objects
		 */
		private function populateScenarioList(list:Vector.<ScenarioListItem>):void
		{
			//superTrace("populateScenarioList");
			
			var i:int = 0;
			for each (var item:ScenarioListItem in list) 
			{
				var scenSelector:ScenarioSelector = new ScenarioSelector();
				scenSelector.build(item, i);
				
				scenSelector.x = Constants.SCENARIO_BAR_POS.x;
				scenSelector.y = Constants.SCENARIO_BAR_POS.y + (i * scenSelector.height);
				scenSelector.addEventListener(MouseEvent.CLICK, scenarioSelectorClick, false, 0, true);
				
				scenarioListGroup.add(scenSelector);
				gameCreationOverlay.addChild(scenSelector);
				
				
				if (i == 0) 
				{
					//first item starts out selected
					scenSelector.selected = true;
					trace("displaying image");
					//Display the picture for the first item					
					var imgLoader:ImageLoader = gameCreationOverlay.getChildByName("scenarioPicture_mc") as ImageLoader;
					imgLoader.displayImage(item.scenPicURL, true, 0.75);
					
					//Display the description for the first item
					var scenarioText_txt:TLFTextField = gameCreationOverlay.getChildByName("scenarioText_txt") as TLFTextField;
					scenarioText_txt.text = item.description;
					
				}
				i++;
			}
		}
		
		private function scenarioSelectorClick(e:MouseEvent):void
		{
			var scenSel:ScenarioSelector = e.currentTarget as ScenarioSelector;
			var imgLoader:ImageLoader = gameCreationOverlay.getChildByName("scenarioPicture_mc") as ImageLoader;
			imgLoader.displayImage(scenSel.scenario.scenPicURL, true, 0.75);
			var scenarioText_txt:TLFTextField = gameCreationOverlay.getChildByName("scenarioText_txt") as TLFTextField;
			scenarioText_txt.text = scenSel.scenario.description;
		}
		
		//Button listener for "create" button
		private function createClicked(e:MouseEvent):void
		{
			var gameName_txt:TLFTextField = gameCreationOverlay.getChildByName("gameName_txt") as TLFTextField;
			this.newGameName = gameName_txt.text;
			this.newGameScenario = (scenarioListGroup.getSelected() as ScenarioSelector).scenario;
			gotoFrame("roleselect");
		}
		
		//Allow the user to calcel out of game creation
		private function onCancelClick(e:MouseEvent):void
		{
			var scenarioItems:Vector.<SelectorBar> = scenarioListGroup.getItems();
			while (scenarioItems.length > 0) {
				gameCreationOverlay.removeChild(scenarioItems.pop());
			}
			gameCreationOverlay.visible = false;
		}
		
		/**
		 * Role selection screen
		 */		
		private function onSceneRole(): void 
		{
			//Remove dynamically added game list
			var gameItems:Vector.<SelectorBar> = gameListGroup.getItems();
			while (gameItems.length > 0) 
			{
				var gi:SelectorBar = gameItems.pop();
				trace("On scene roll - removing gamelist item " + gi.toString());
				myMain.removeChild(gi);
			}
			
			username_txt = myMain.getChildByName("username_txt") as TLFTextField;
			var scnRoles:Array;
			
			//If a new game was created, cleanup
			if (gameCreationOverlay.visible) 
			{
				var scenarioItems:Vector.<SelectorBar> = scenarioListGroup.getItems();
				while (scenarioItems.length > 0) 
				{
					gameCreationOverlay.removeChild(scenarioItems.pop());
				}
				gameCreationOverlay.visible = false;
				
				scnRoles = newGameScenario.roles;
				username_txt.text = username + " > " + newGameName;
			} 
			else 
			{
				scnRoles = game.scenario.roles;
				username_txt.text = username + " > " + game.scenario.name;
			}
			
			
			var roledenied_mc = myMain.getChildByName("roleDenied_mc") as MovieClip;
			roledenied_mc.visible = false;
			roleDesc_txt = myMain.getChildByName("roleDesc_txt") as TLFTextField;
			roleBriefing_txt = myMain.getChildByName("roleBriefing_txt") as TLFTextField;
			back_bttn = myMain.getChildByName("back_bt") as EREN_Button;
			back_bttn.setContent("back");
			back_bttn.setEnabled();
			continue_bttn = myMain.getChildByName("continue_bt") as EREN_Button;
			continue_bttn.setContent("start");
			continue_bttn.setEnabled();
			
			roleListGroup = new SelectorGroup();
			
			for (var i:int = 0; i < scnRoles.length; i++) 
			{
				var rs:RoleSelector = new RoleSelector();
				rs.build(scnRoles[i], i);
				rs.addEventListener(MouseEvent.CLICK, roleSelectorClick, false, 0, true);
				rs.x = Constants.ROLE_BAR_POS.x;
				rs.y = Constants.ROLE_BAR_POS.y + (i * rs.height);
				
				roleListGroup.add(rs);
				myMain.addChild(rs);
				
				if (i == 0) 
				{
					rs.selected = true;
				}
				else
				{
					rs.selected = false;
				}
			}
			
			//Display info for the first role, which is automatically selected.
			roleDesc_txt.text = (scnRoles[0] as RoleObject).description;
			roleBriefing_txt.text = (scnRoles[0] as RoleObject).briefing;
			
			roleImgLoader = myMain.getChildByName("rolepic_mc") as ImageLoader;
			roleImgLoader.displayImage( (scnRoles[0] as RoleObject).picURL );
			
			back_bttn.addEventListener(MouseEvent.CLICK, backClicked);
			continue_bttn.addEventListener(MouseEvent.CLICK, continueClicked);
		}
		
		
		private function roleSelectorClick(e:MouseEvent):void
		{
			var rs:RoleSelector = e.currentTarget as RoleSelector;
			
			roleDesc_txt.text = (rs.role).description;
			roleBriefing_txt.text = (rs.role).briefing;
			roleImgLoader.displayImage( (rs.role).picURL );
		}
		
		/**
		 * The final scene: gameplay.
		 */
		private function onSceneGameplay(): void 
		{	
			superTrace("*******************************************************************************");
			superTrace("entering onScene6");
			superTrace("*******************************************************************************");
			
			//Store a reference to the loading overlay - once we get a game start, remove it
			loadingOverlay_mc = myMain.getChildByName("loadingOverlay_mc") as MovieClip;
			superTrace("got overlay");
			myMap = myMain.getChildByName("theMap") as MapUtil; 
			myMap.setKey(theMapKey);
			superTrace("set up map");
			myMCM = myMain.getChildByName("messageCenter_mc") as MessageCenterManager;
			trace("mcm = " + myMCM.toString());
			myMCM.addEventListener(ErenEvent.READY, mcmReady);
			myMCM.addEventListener(SendMessageEvent.SEND, sendMessage);
			myParser.addEventListener(MessageCenterEvent.MESSAGE_CENTER_EVENT, myMCM.handleMCMEvent);
			
			superTrace("set up mcm");
			username_txt = myMain.getChildByName("username_txt") as TLFTextField;
			trace("username_txt = " + username_txt);
			
			//Populate breadcrumbs
			if (creatingNewGame) 
			{
				username_txt.text = username + " > " + newGameName  + " > " + role; 
			} 
			else 
			{
				username_txt.text = username + " > " + game.gameName  + " > " + role; 
			}
			superTrace("set up username_txt");
			
			//Set up timeline
			myTimeline = myMain.getChildByName("theTimeline") as Timeline;
			if (myTimeline != null) 
			{
				myTimeline.init();
			}
			superTrace("set up timeline");
			scoreField = myMain.getChildByName("scoreField") as TLFTextField;
			superTrace("End scene 6 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		}
		
		/** Message center manager code **/
		
		private function sendMessage(e:SendMessageEvent):void
		{
			var x:XML;
			
			if (e._type == SendMessageEvent.MULTI_RESPONSE) 
			{
				x = myParser.createDialogMultiResponse(e._messageID, e._responseIDs);
			}
			else if (e._type == SendMessageEvent.SINGLE_RESPONSE)
			{
				x = myParser.createDialogResponse(e._messageID, e._singleResponseID);
			}
			else if (e._type == SendMessageEvent.VALUE_RESPONSE)
			{
				x = myParser.createDialogValueResponse(e._messageID, e._value);
			}
			mySocket.send(x);
			myMap.setZoom(Constants.DEFAULT_ZOOM_LEVEL);
		}
		
		private function mcmReady(e:ErenEvent):void 
		{
			sendMssg(myParser.createReadyMessage());
			//TODO: name frame
			loadingOverlay_mc.gotoAndStop(3);
		}
		
		private function commitActionHandler(e:MessageCenterEvent):void
		{
			if (e._type == MessageCenterEvent.COMMIT_ACTION) 
			{
				var x:XML = myParser.createCommitAction(e._actionId, this.role, e._facilityId, e._resourceId);
				superTrace("Engine: sending commit action " + x);
				sendMssg(x);
			}
		}
		
		/** Parser code **/
		
		private function processTimeEvent(e:TimeEvent):void
		{
			
			if (e._type == TimeEvent.GOT_DATE) 
			{
				if (myMain.currentFrameLabel == "gameplay" && myTimeline != null) 
				{
					myTimeline.setSync(e._date);
				}
			}
			else if (e._type == TimeEvent.GOT_RATIO) 
			{
				if (myMain.currentFrameLabel == "gameplay" && myTimeline != null) 
				{
					myTimeline.setRatio(e._ratio);
				}
			}
			else if (e._type == TimeEvent.GOT_TICK) 
			{
				if (myMain.currentFrameLabel == "gameplay" && myTimeline != null) 
				{
					myTimeline.tick(e._seconds);
				}
			}
		}
		
		private function processMCMEvent(e:MessageCenterEvent):void 
		{
			dispatchEvent(e);
		}
		
		private function processGameEvent(e:GameEvent):void
		{
			if (e._type == GameEvent.GOT_GAME_LIST) 
			{
				numGameListsRecieved++;
				if (myMain.currentFrameLabel == "scenarioselect") 
				{
					superTrace("----------------Got gamelist " + numGameListsRecieved + ", populating game selection----------------");
					if (numGameListsRecieved > 1) 
					{
						updateGameList(e._gameList);
					} 
					else 
					{
						populateGameList(e._gameList);
					}
				}
			}
			else if (e._type == GameEvent.GOT_GAMESTART) 
			{
				if (!gotgamestart) 
				{				
					superTrace("Got game start!");
					loadingOverlay_mc.visible = false;
					myMain.removeChild(loadingOverlay_mc);
					gotgamestart = true;
				} 
			}
			else if (e._type == GameEvent.GOT_SCENARIO_LIST) 
			{
				if (myMain.currentFrameLabel == "scenarioselect") 
				{
					superTrace("Got scenario l, populating list");
					//this.scenarioArray = e._scenarioList;
					populateScenarioList(e._scenarioList);
				}
			}
			else if (e._type == GameEvent.GOT_SCENARIO_FILE) 
			{
				if (this.scenarioFile == null) 
				{
					this.scenarioFile = e._scenarioFile;
					processScenarioFile();
				}
			}
			else if (e._type == GameEvent.GOT_SCORE) 
			{
				if (myMain.currentFrameLabel == "gameplay") 
				{
					var thescore:Score = e._score;
					scoreField.text = "#sick: " + thescore.morbidity + 
					", #dead: " + thescore.mortality + ", #treated: " + thescore.treated;
				}
			}
			else if (e._type == GameEvent.ROLE_DENIED) 
			{
				if (e._username == this.username) 
				{
					var roledenied_mc = myMain.getChildByName("roleDenied_mc") as MovieClip;
					roledenied_mc.visible = true;
				}
			}
			else if (e._type == GameEvent.ROLE_FILLED) 
			{
				if (e._username == this.username && myMain.currentFrameLabel == "roleselect") 
				{
					fillRole(e._role);
				}
			}
			else if (e._type == GameEvent.GOT_UNKNOWN_XML) 
			{
				error("Got unknown xml: " + e._xml);
			}
			
		}
		
		private function fillRole(r:String):void 
		{			
			role = r;
			continue_bttn.removeEventListener(MouseEvent.CLICK, continueClicked);
			continue_bttn.removeListeners();
			
			//transition to next scene
			var items:Vector.<SelectorBar> = roleListGroup.getItems();
			while (items.length > 0) 
			{
				myMain.removeChild(items.pop());
			}
			
			superTrace("going to loading screen");
			gotoFrame("gameplay");
		}

		
		private function processScenarioFile():void 
		{
			superTrace("Processing Scenario File");
			myParser.setBaseURL(scenarioFile.baseURL);
			scenarioFile.addEventListener(ErenEvent.READY, actionsReady, false, 0, false);
			
			if (myMain.currentFrameLabel == "gameplay") 
			{
				loadingOverlay_mc.gotoAndStop(2);
				if (myTimeline != null) 
				{
					myTimeline.setNumGameSeconds(scenarioFile.dur_gametime);
					myTimeline.setSync(scenarioFile.syncDate);
					myTimeline.display(scenarioFile.syncDate);
				}
				superTrace("PSF: init'd timeline");
				//Initialize and center the map
				if (myMap == null) 
				{
					superTrace("PSF: map = null");
					myMap = myMain.getChildByName("theMap") as MapUtil; 
					superTrace("PSF: map = " + myMap);
					if (myMap == null) 
					{
						//ignore
						superTrace("still null, ignoring");
					} 
					else 
					{
						superTrace("map is not null, setting keys");
						myMap.setKey(theMapKey);
					}
				}
			}
		}
		
		private function actionsReady(e:ErenEvent):void
		{
			superTrace("initializing mcm - init");
			myMCM.init(scenarioFile, myMap, this, role);
			superTrace("initializing mcm - event listener");
			myMCM.resourceManager.addEventListener(MessageCenterEvent.MESSAGE_CENTER_EVENT, this.commitActionHandler);
		}
		
		public function fireCommitActionHandler(e:ActionEvent):void
		{
			//fire the commit action mssg
			trace("fireCommitActionHandler");
			var xml:XML = myParser.createCommitAction(e.action.type, this.role, e.facilityId, e.action.resourceId, e.action.inputsVector);
			sendMssg(xml);
		}
		
		/** Stage button listeners **/
		
		private function loginClicked(e:MouseEvent): void 
		{
			login_bttn.removeEventListener(MouseEvent.CLICK, loginClicked);
			login_bttn.removeListeners();
			var field:MovieClip = myMain.getChildByName("unField") as MovieClip;
			
			var text:TLFTextField = field.un_txt;
			var un:String = text.text;
			
			if (un.charAt(un.length - 1) == '/n') 
			{
				var un2:String = un.substr(0, un.length - 1); //take off the extra carriage return
				un = un2;
			}
			this.username = un;
			//superTrace("Trying to access parser and create loging message for user " + username);
			var mssg:XML = myParser.createLogin(username, "password");
			
			sendMssg(mssg);
			
			gotoFrame("scenarioselect");
		}
		
		private function selectClicked(e:MouseEvent): void 
		{
			select_bttn.removeEventListener(MouseEvent.CLICK , selectClicked);
			select_bttn.removeListeners();
			game = (gameListGroup.getSelected() as GameSelector).game;
			
			var gameItems:Vector.<SelectorBar> = gameListGroup.getItems();
			for each (var g:SelectorBar in gameItems) 
			{
				superTrace("-------------------Removing " + (g as GameSelector).game.gameName + "-------------------");
			}
			while (gameItems.length > 0) 
			{
				var gi:SelectorBar = gameItems.pop();
				trace("On scene roll - removing gamelist item " + gi.toString());
				myMain.removeChild(gi);
			}
			trace("Going ot frame roleselect");
			gotoFrame("roleselect");
		}
		
		private function backClicked(e:MouseEvent): void 
		{
			back_bttn.removeEventListener(MouseEvent.CLICK, backClicked);
			back_bttn.removeListeners();
			var items:Vector.<SelectorBar> = roleListGroup.getItems();
			while (items.length > 0) 
			{
				myMain.removeChild(items.pop());
			}
			gotoFrame("scenarioselect");
		}
		
		//TODO: rename; this is actually the start button
		private function continueClicked(e:MouseEvent): void 
		{
			roleID = (roleListGroup.getSelected() as RoleSelector).role.ID
			trace("Got role Id " + roleID);
			
			if (creatingNewGame) 
			{
				sendMssg(
					myParser.createNewGameMssg(
						newGameScenario.ID,
						roleID,
						newGameName)
				);
			}
			else 
			{
				if (usingBus) 
				{
					superTrace("sending join game message");
					sendMssg(myParser.createJoinGameRequest(game.gameID, roleID));
				} 
				else 
				{
					continue_bttn.removeEventListener(MouseEvent.CLICK, continueClicked);
					gotoFrame("loading");
				}
			}
		}
		
		private function onUsernameKey(e:KeyboardEvent):void 
		{
			var c:Number = e.keyCode;
			superTrace("c = " + c);
			if (e.keyCode == Keyboard.ENTER && socketIsConnected) 
			{
				loginClicked(new MouseEvent(MouseEvent.CLICK));
			}
		}
		
		private function usernameClick(e:MouseEvent):void 
		{
			(e.currentTarget as MovieClip).removeEventListener(MouseEvent.CLICK, usernameClick);
			var field:MovieClip = myMain.getChildByName("unField") as MovieClip;
			var txt:TLFTextField = field.un_txt;		
			txt.text = "";
		}
		
		/** Helpers **/
		
		private function getMinPlayers(g:Game):int 
		{
			return ErenUtilities.getLength(g.filledRoles);
		}
		
		private function getActivePlayers(g:Game):int
		{
			var count:int = 0;
			for each (var r:int in g.filledRoles) 
			{
				count += r;
			}
			return count;
		}
		
		private function onTimeout(e:SecurityErrorEvent):void 
		{
			superTrace("caught timeout");
			superTrace("Error id is " + e.errorID + ", type = " + e.type + ", text " + e.text + ", toString " + e.toString());
			networkError_mc.visible = true;
		}
		
		private function connected(e:Event): void 
		{
			login_bttn.visible = true;
			connection_mc.visible = false;
			socketIsConnected = true;
			superTrace("Socket successfully connected");
		}
		
		private function onXMLData(e:DataEvent): void 
		{
			var data:XML = new XML(e.data);
			//superTrace("got xml: " + data);
			myParser.parse(data);
		}
		
		private function sendMssg(mssg:XML): void 
		{
			if (socketIsConnected) 
			{
				mySocket.send(mssg);
				superTrace("Sent: " + mssg);			
			}
			else 
			{
				superTrace("Generated message: " + mssg);
			}
		}
		
		/**
		 * Mask the trace function so that it will print out the player's username before 
		 * whatever is to be printed. This is useful because Firebug displays the output from 
		 * ALL open Flash apps in the same console, without saying where the trace statement
		 * comes from.
		 * 
		 * @param	s the string to be printed
		 */
		private function superTrace(s:String):void 
		{
			if (username != null)
			{
				trace(username + ": " +  s);
			}
			else 
			{
				trace(s);
			}
		}
		
		private function error(s:String):void 
		{
			superTrace("Error occured");
			superTrace("Message:" + s);
		}
	}

}