package org.mitre.eren.events 
{
	import flash.events.Event;
	import org.mitre.eren.data.game.Game;
	import org.mitre.eren.data.game.Score;
	import org.mitre.eren.data.scenario.ScenarioFile;
	import org.mitre.eren.data.scenario.ScenarioListItem;
	
	/**
	 * Custom event class for game events.
	 * 
	 * @author Amanda Anganes
	 */
	public class GameEvent extends Event 
	{
		public static const GAME_EVENT:String = "GameEvent";
		public static const ROLE_FILLED:String = "Parser got role filled mssg";
		public static const ROLE_DENIED:String = "Parser got role denied mssg";
		public static const GOT_SCENARIO_LIST:String = "parser got scenario list";
		public static const GOT_SCENARIO_FILE:String = "parser got scenario file";
		public static const GOT_GAME_LIST:String = "parser got gamelist";
		public static const GOT_SCORE:String = "parser got score mssg";
		public static const GOT_GAMESTART:String = "Parser got game start";
		public static const GOT_UNKNOWN_XML:String = "got unknown xml";
		public static const ERROR:String = "Game error";
		
		public var _type:String;
		private var _bubbles:Boolean;
		private var _cancelable:Boolean;
		
		public var _username:String;
		public var _role:String;
		public var _scenarioList:Vector.<ScenarioListItem>;
		public var _gameList:Vector.<Game>;
		public var _scenarioFile:ScenarioFile;
		public var _score:Score;
		public var _xml:XML;
		
		/**
		 * Constructor.
		 * 
		 * @param	type       should be one of the consts defined in this class
		 * @param	bubbles    boolean; inherited from Event
		 * @param	cancelable boolean; inherited from Event
		 */
		public function GameEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false) 
		{
			this._type = type;
			this._bubbles = bubbles;
			this._cancelable = cancelable;
			
			super(GAME_EVENT, bubbles, cancelable);
		}
		
		/**
		 * Implement clone() so that this event can bubble properly
		 * 
		 * @return a copy of this object
		 */
		public override function clone():Event 
		{
			var ge:GameEvent = new GameEvent(GAME_EVENT, _bubbles, _cancelable)
			ge._type = _type;
			ge._username = _username;
			ge._gameList = _gameList;
			ge._scenarioFile = _scenarioFile;
			ge._scenarioList = _scenarioList;
			ge._score = _score;
			ge._role = _role;
			ge._xml = _xml;
			
			return ge;
		}
		
		public override function toString():String 
		{
			return formatToString("GameEvent", "_type", "_bubbles", "_cancelable");
		}
		
	}

}