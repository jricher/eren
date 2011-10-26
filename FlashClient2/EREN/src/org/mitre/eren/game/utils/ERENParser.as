package org.mitre.eren.game.utils 
{	
	import flash.events.EventDispatcher;
	import org.mitre.eren.data.actions.Input;
	import org.mitre.eren.data.dialog.UserMessage;
	import org.mitre.eren.data.game.Game;
	import org.mitre.eren.data.game.Score;
	import org.mitre.eren.data.scenario.PODupdate;
	import org.mitre.eren.data.scenario.RDStatus;
	import org.mitre.eren.data.scenario.ResourceUsageStats;
	import org.mitre.eren.data.scenario.ScenarioFile;
	import org.mitre.eren.data.scenario.ScenarioListItem;
	import org.mitre.eren.events.GameEvent;
	import org.mitre.eren.events.MessageCenterEvent;
	import org.mitre.eren.events.TimeEvent;
	
	/**
	 * XML Parser for EREN game. The parser performs two main functions: 
	 * 1) it can be used to create and return all of the various messages
	 * that the UI must send throughout the course of the game, and
	 * 2) it can be handed a blob of XML, which it will then parse and 
	 * fire an event for when done. 
	 * 
	 * @author Amanda Anganes
	 */
	public class ERENParser extends EventDispatcher {
		
		/**
		 * The root URL of the currently running Flash app.
		 */
		private var baseURL:String;
		
		/**
		 * Constructor
		 */
		public function ERENParser() {
		}
		
		/**
		 * Set the root URL of the currently running EREN deployment. This
		 * is used during parsing to convert URL fragments into whole URLs, 
		 * and should be set before asking the Parser to parse anything.
		 * 
		 * @param	baseURL the base or root URL of the application
		 */
		public function setBaseURL(baseURL:String) {
			this.baseURL = baseURL;
		}
		
		/**
		 * Wrap an XML document with the eren:mssg and eren:sendtobus wrappers.
		 * 
		 * @param	x_xml the XML document to wrap
		 * @return the wrapped object
		 */
		public function wrap(x_xml:XML):XML {
			var newXML:XML = 
				<eren:mssg xmlns:eren="urn:mitre:eren:1.0">
					<eren:sendtobus>
				{x_xml }
			</eren:sendtobus>
		</eren:mssg>;
			
			return newXML;
		}
		
		/**
		 * Create a login message with the given information.
		 * 
		 * @param	username username
		 * @param	password password
		 * @return the wrapped XML object
		 */
		public function createLogin(username:String, password:String):XML {
			var x:XML = 
				<eren:login xmlns:eren="urn:mitre:eren:1.0">
					<eren:username>{username}</eren:username>
					<eren:password>{password}</eren:password>
					<eren:client>"Flash Client v1.0"</eren:client>
				</eren:login>;
			return (wrap(x));
		}
		
		/**
		 * Create a join game request message.
		 * 
		 * @param	gameId ID of the game to join
		 * @param	roleId player role ID
		 * @return the wrapped XML object
		 */
		public function createJoinGameRequest(gameId:String, roleId:String):XML {
			var x:XML =
				<eren:joinGame xmlns:eren="urn:mitre:eren:1.0">
					<eren:gameId>{gameId}</eren:gameId>
					<eren:roleId>{roleId}</eren:roleId>
				</eren:joinGame>;
			return (wrap(x));
		}
		
		/**
		 * Create a "create new game" message.
		 * 
		 * @param	scenarioId ID of the scenario to use
		 * @param	roleId     ID of the role the first player wants
		 * @param	gameName   human-readable game name
		 * @return the wrapped XML object
		 */
		public function createNewGameMssg(scenarioId:String, roleId:String, gameName:String):XML {
			var x:XML =
				<eren:createGame xmlns:eren="urn:mitre:eren:1.0">
					<eren:scenarioId>{scenarioId}</eren:scenarioId>
					<eren:roleId>{roleId}</eren:roleId>
					<eren:gameName>{gameName}</eren:gameName>
				</eren:createGame>;
			return (wrap(x));	
		}
		
		/**
		 * Create a scenairo list request message.
		 * 
		 * @return the wrapped XML object
		 */
		public function createScenarioListRequest():XML {
			var x:XML = <eren:scenariolistrequest xmlns:eren="urn:mitre:eren:1.0"/>;
			return (wrap(x));
		}
		
		/**
		 * Create a regular dialog response message.
		 * 
		 * @param	mssgID the ID of the UserMessage this response is replying to
		 * @param	rID    the ID of the option the user selected
		 * @return the wrapped XML object
		 */
		public function createDialogResponse(mssgID:String, rID:String):XML 
		{
			var response:XML =
				<eren:mssg xmlns:eren="urn:mitre:eren:1.0">
					<eren:sendtobus>
						<dlg:userResponse xmlns:dlg="urn:mitre:eren:dlg:1.0">
							<dlg:messageID>{mssgID}</dlg:messageID>
							<dlg:responseID>{rID}</dlg:responseID>
						</dlg:userResponse>
					</eren:sendtobus>
				</eren:mssg>;
			
			return response;
		}
		
		/**
		 * Create a dialog multi-response.
		 * 
		 * @param	mssgID the ID of the UserMessage this response is replying to
		 * @param	rIDs   a Vector of response IDs
		 * @return the wrapped XML object
		 */
		public function createDialogMultiResponse(mssgID:String, rIDs:Vector.<String>):XML
		{
			var response:XML = 
				<eren:mssg xmlns:eren="urn:mitre:eren:1.0">
					<eren:sendtobus>
						<dlg:userResponse xmlns:dlg="urn:mitre:eren:dlg:1.0">
						</dlg:userResponse>
					</eren:sendtobus>
				</eren:mssg>;
			
			var dlg:Namespace = new Namespace("urn:mitre:eren:dlg:1.0");
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			
			response.eren::sendtobus.dlg::userResponse.appendChild(<dlg:messageID xmlns:dlg="urn:mitre:eren:dlg:1.0">{mssgID}</dlg:messageID>);
			
			for each (var id:String in rIDs) 
			{
				var temp:XML = <dlg:responseID xmlns:dlg="urn:mitre:eren:dlg:1.0">{id}</dlg:responseID>;
				response.eren::sendtobus.dlg::userResponse.appendChild(temp);
			}
			
			return response;
		}
		
		/**
		 * Create a dialog value response.
		 * 
		 * @param	mssgID the ID of the UserMessage this response is replying to
		 * @param	val    the value selected
		 * @return the wrapped XML object
		 */
		public function createDialogValueResponse(mssgID:String, val:Number):XML
		{
			var response:XML =
				<eren:mssg xmlns:eren="urn:mitre:eren:1.0">
					<eren:sendtobus>
						<dlg:userResponse xmlns:dlg="urn:mitre:eren:dlg:1.0">
							<dlg:messageID>{mssgID}</dlg:messageID>
							<dlg:responseValue>{val}</dlg:responseValue>
						</dlg:userResponse>
					</eren:sendtobus>
				</eren:mssg>;
			
			return response;
		}
		
		/**
		 * Create a commit aciton message.
		 * 
		 * @param	type       the Action type
		 * @param	role       the role of the user committing the action
		 * @param	facilityId ID of the facility this action is to be applied to
		 * @param	resourceId ID of the resource this action is to be applied to
		 * @param	inputs     Vector of Input objects, which if included will be broken down and inserted into the message
		 * @return the wrapped XML object
		 */
		public function createCommitAction(type:String, role:String, facilityId:String, resourceId:String = "", inputs:Vector.<Input> = null):XML 
		{
			var response:XML =
				<eren:commitAction xmlns:eren="urn:mitre:eren:1.0" xmlns:act="urn:mitre:eren:actions:1.0">
					<act:type>{type}</act:type>
					<act:role>{role}</act:role>
					<act:facilityId>{facilityId}</act:facilityId>
				</eren:commitAction>;
			
			if (inputs != null && inputs.length >= 1) 
			{	
				var outerNode:XML = new XML("<act:parameters xmlns:act='urn:mitre:eren:actions:1.0'/>");
				
				for each (var inp in inputs)
				{
					var input:Input = inp as Input;
					var name:String = input.name;
					var value:String = "";
					if (input.type == Input.INTEGER) {
						value = input.integerValue.toString();
					}
					else if (input.type == Input.CHOOSE) {
						value = input.choiceValue;
					}
					var node:XML = new XML("<act:parameter act:name='" + name + "' xmlns:act='urn:mitre:eren:actions:1.0'/>");
					node.appendChild(value);
					outerNode.appendChild(node);
				}
				response.appendChild(outerNode);
			}
			
			if (resourceId.length >= 1) 
			{
				var temp2:XML = <act:resource xmlns:act="urn:mitre:eren:actions:1.0">{resourceId}</act:resource>;
				response.appendChild(temp2);
			}
				
			return wrap(response);	
		}
		
		/**
		 * Create a client ready message.
		 * 
		 * @return the wrapped XML object
		 */
		public function createReadyMessage():XML {
			var x:XML = <eren:clientready xmlns:eren="urn:mitre:eren:1.0" />;
			return wrap(x);
		}
		
		/**
		 * Event callbacks
		 **/ 
		
		/**
		 * Notify others that the parser has received a clock sync message.
		 * 
		 * @param	sync the clock sync text
		 */
		private function fireGotSync(sync:String):void 
		{
			//Parse sync string into year, month, day, time
			var year:Number = Number(sync.substring(0,4));
			var month:Number = Number(sync.substring(5,7));
			var day:Number = Number(sync.substring(8,10));
			var time:String = sync.substring(11,19);
			
			var hours:Number = Number(time.substring(0, 2));
			var minutes:Number = Number(time.substring(3, 5));
			var seconds:Number = Number(time.substring(6, 8));
			
			var d:Date = new Date(year, month, day, hours, minutes, seconds);
			
			//Fire event back to any listeners
			var t:TimeEvent = new TimeEvent(TimeEvent.GOT_DATE);
			t._date = d;
			dispatchEvent(t);
		}
		
		/**
		 * Notify others that the parser has received a clock ratio message
		 * 
		 * @param	s the new clock ratio, as a string
		 */
		private function fireGotRatio(s:String):void 
		{
			var t:TimeEvent = new TimeEvent(TimeEvent.GOT_RATIO);
			t._ratio = parseInt(s);
			dispatchEvent(t);
		}
		
		/**
		 * Notify others that the parser has received a clock tick message
		 * 
		 * @param	s the tick number (# seconds since game start)
		 */
		private function fireGotTick(s:String):void 
		{
			var t:TimeEvent = new TimeEvent(TimeEvent.GOT_TICK);
			t._seconds = parseInt(s);
			dispatchEvent(t);
		}
		
		/**
		 * Notify others that the parser has received a dialogue user message
		 * 
		 * @param	x the XML UserMessage
		 */
		private function fireGotUserMessage(x:XML):void 
		{
			var m:MessageCenterEvent = new MessageCenterEvent(MessageCenterEvent.GOT_USER_MSSG);
			m._userMssg = new UserMessage(x, baseURL);
			dispatchEvent(m);
		}
		
		/**
		 * Notify others that the parser has received a role filled message for
		 * the given username and role.
		 * 
		 * @param	username
		 * @param	role
		 */
		private function fireGotRoleFilled(username:String, role:String):void 
		{
			var g:GameEvent = new GameEvent(GameEvent.ROLE_FILLED);
			g._role = role;
			g._username = username;
			dispatchEvent(g);
		}
		
		/**
		 * Notify others that the parser has received a role denied message for the 
		 * given username and role.
		 * 
		 * @param	username
		 * @param	role
		 */
		private function fireGotRoleDenied(username:String, role:String):void 
		{
			var g:GameEvent = new GameEvent(GameEvent.ROLE_DENIED);
			g._role = role;
			g._username = username;
			dispatchEvent(g);
		}
		
		/**
		 * Notify others that the parser has received a scenario list
		 * 
		 * @param	x the XML scenario list
		 */
		private function fireGotScenarioList(x:XML):void {
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			var sList:Vector.<ScenarioListItem> = new Vector.<ScenarioListItem>();
			var xScns:XMLList = x.eren::scenario;
			
			for each (var xScn in xScns) {
				var scn:ScenarioListItem = new ScenarioListItem(xScn);
				sList.push(scn);
			}
			
			var g:GameEvent = new GameEvent(GameEvent.GOT_SCENARIO_LIST);
			g._scenarioList = sList;
			dispatchEvent(g);
		}
		
		/**
		 * Notify others that the parser has received a game list.
		 * 
		 * @param	x the XML game list
		 */
		private function fireGotGameList(x:XML):void 
		{
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			var gList:Vector.<Game> = new Vector.<Game>();
			
			var xGames:XMLList = x.eren::game;
			
			for each (var xGame:XML in xGames) 
			{
				var g:Game = new Game(xGame);
				gList.push(g);
			}
			
			var ge:GameEvent = new GameEvent(GameEvent.GOT_GAME_LIST);
			ge._gameList = gList;
			dispatchEvent(ge);
			
		}
		
		/**
		 * Notify others that the parser has received a scenario file
		 * 
		 * @param	x the XML scenairo file
		 */
		private function fireGotScenarioFile(x:XML):void 
		{
			var g:GameEvent = new GameEvent(GameEvent.GOT_SCENARIO_FILE);
			g._scenarioFile = new ScenarioFile(x);
			dispatchEvent(g);
		}
		
		/**
		 * Notify others that the parser has received a game start message
		 */
		private function fireGotGameStart():void 
		{
			dispatchEvent(new GameEvent(GameEvent.GOT_GAMESTART));
		}
		
		/**
		 * Notify others that the parser has received a score message
		 * 
		 * @param	x the XML message
		 */
		private function fireGotScore(x:XML):void {
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			var exp:int = parseInt(x.eren::exposure.text());
			var morb:int = parseInt(x.eren::morbidity.text());
			var mort:int = parseInt(x.eren::mortality.text());
			var t:int = parseInt(x.eren::treated.text());
			
			var ge:GameEvent = new GameEvent(GameEvent.GOT_SCORE);
			ge._score = new Score(exp, morb, mort, t);
			dispatchEvent(ge);
		}
		
		/**
		 * Notify others that the parser has received a resource deployment status message
		 * 
		 * @param	x the XML message
		 */
		private function fireGotResourceDeploymentStatus(x:XML):void {
			var msg:Namespace = new Namespace("urn:oasis:names:tc:emergency:EDXL:RM:1.0:msg");
			var rm:Namespace = new Namespace("urn:oasis:names:tc:emergency:EDXL:RM:1.0");
			
			var fid:String = x.msg::ResourceInformation.msg::Resource.msg::ResourceID.text();
			var stat:String = x.msg::ResourceInformation.msg::Resource.msg::ResourceStatus.msg::DeploymentStatus.rm::Value.text();;
			
			var thestatus = new RDStatus(fid, stat);
			
			var m:MessageCenterEvent = new MessageCenterEvent(MessageCenterEvent.GOT_RDSTATUS);
			m._rdStatus = thestatus;
			dispatchEvent(m);
		}
		
		/**
		 * Notify others that the parser has received a POD status message
		 * 
		 * @param	x the XML message
		 */
		private function fireGotPODStatus(x:XML):void {
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			var dlg:Namespace = new Namespace("urn:mitre:eren:dlg:1.0"); 
			var id:String = x.@dlg::podId;
			var medStaff:int = parseInt(x.eren::medicalStaff.text());
			var supStaff:int = parseInt(x.eren::supportStaff.text());
			var secStaff:int = parseInt(x.eren::securityStaff.text());
			var qsize:int = parseInt(x.eren::queueSize.text());
			var tp:Number = parseInt(x.eren::throughput.text());
			var hasm:Boolean = (x.eren::hasMeds.text() == "true") ? true : false;
			var hase:Boolean = (x.eren::hasEquipment.text() == "true") ? true : false;
			var stc:int = parseInt(x.eren::standardOfCare.text());
			
			var thestatus = new PODupdate(id, medStaff, supStaff, secStaff, qsize, tp, hasm, hase, stc);
			
			var m:MessageCenterEvent = new MessageCenterEvent(MessageCenterEvent.GOT_PODSTATUS);
			m._podUpdate = thestatus;
			dispatchEvent(m);			
		}
		
		/**
		 * Notify others that the parser has received a resource usage message
		 * 
		 * @param	x the XML message
		 */
		private function fireGotResourceUsage(x:XML):void {
			
			var theResourceStats:ResourceUsageStats = new ResourceUsageStats(x);
			
			var m:MessageCenterEvent = new MessageCenterEvent(MessageCenterEvent.GOT_RESOURCE_USAGE_STATS);
			m._resourceStats = theResourceStats;
			dispatchEvent(m);
		}
		
		/**
		 * Notify others that the parser has recieved an unknown chunk of XML
		 * 
		 * @param	x the unknown XML
		 */
		private function fireGotUnknown(x:XML) {
			var ge:GameEvent = new GameEvent(GameEvent.GOT_UNKNOWN_XML);
			ge._xml = x;
			dispatchEvent(ge);
		}
		
		/**
		 * Parse the given XML object into a data structure if it is recognized,
		 * and fire the corresponding ParseEvent so that other modules can handle
		 * the message.
		 * 
		 * @param	x_xml the XML to be parsed
		 */
		public function parse(x_xml:XML):void 
		{
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			var cmd:String = x_xml.name().localName as String;
			//trace("parser got command " + cmd);
			
			if (cmd == "clocktick")
			{
				var time:String = x_xml.children()[0].text();
				fireGotTick(time);
			}
			else if (cmd == "clocksync")
			{
				fireGotSync(x_xml.children()[0].text());
			}
			else if (cmd == "clockspeed")
			{
				fireGotRatio(x_xml.children()[0].text());				
			}
			else if (cmd == "userMessage")
			{
				fireGotUserMessage(x_xml);
			} 
			else if (cmd == "gamelist") 
			{
				fireGotGameList(x_xml);
			}
			else if (cmd == "scenariolist") 
			{
				fireGotScenarioList(x_xml);
				
			} else if (cmd == "rolefilled") {
				
				fireGotRoleFilled(x_xml.eren::username.text(), x_xml.eren::roleId.text());
				
			} else if (cmd == "roledenied") {
				
				fireGotRoleDenied(x_xml.eren::username.text(), x_xml.eren::roleId.text());
				
			} else if (cmd == "scenario") {
				
				fireGotScenarioFile(x_xml);
				
			} else if (cmd == "gamestart") {
				
				fireGotGameStart();
				
			} else if (cmd == "score") {
				
				fireGotScore(x_xml);
				
			} else if (cmd == "podStatus") {
				
				fireGotPODStatus(x_xml);
				
			} else if (cmd == "ReportResourceDeploymentStatus") {
				
				fireGotResourceDeploymentStatus(x_xml);
				
			} else if (cmd == "resourceUsage") {
				
				fireGotResourceUsage(x_xml);
			}
			else {
				fireGotUnknown(x_xml);
			}
		}
	}
}