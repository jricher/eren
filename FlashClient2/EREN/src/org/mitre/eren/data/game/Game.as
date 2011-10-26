package org.mitre.eren.data.game 
{
	import flash.utils.Dictionary;
	import org.mitre.eren.data.scenario.ScenarioListItem;
	import org.mitre.eren.game.utils.ErenUtilities;
	
	/**
	 * Game object representation. A game has an ID, a scenario (scenarioListItem),
	 * and a list of active roles. The active roles list tells how many of each roll
	 * are currently filled, and the minimum and maximum number of players who can 
	 * play in that roll.
	 * 
	 * @author Amanda Anganes
	 */
	public class Game 
	{
		public var gameID:String;
		public var gameName:String;
		public var scenario:ScenarioListItem;
		public var filledRoles:Dictionary;
		public var requiredMin:Dictionary;
		public var roleMax:Dictionary;
		
		public function Game(x:XML) 
		{
			filledRoles = new Dictionary();
			requiredMin = new Dictionary();
			roleMax = new Dictionary();
			
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			
			gameID = x.attribute("ID");
			trace("////////////////////////////////Got game with id: " + gameID);
			gameName = x.attribute("name");
			trace("Game name is " + gameName);
			var scn:XMLList = x.eren::scenario;
			for each (var s:XML in scn) {
				trace("got scenario item from x.eren::scenario list");
				scenario = new ScenarioListItem(s);
			}
			
			var activeRoles:XMLList = x.eren::activeroles.eren::activerole;
			
			for each (var ar:XML in activeRoles) 
			{
				var roleID:String = ar.eren::roleId.text();
				var numFilled:int = parseInt(ar.eren::numFilled.text());
				filledRoles[roleID] = numFilled;
				
				var numMin:int = parseInt(ar.eren::min.text());
				requiredMin[roleID] = numMin;
				
				var numMax:int = parseInt(ar.eren::max.text());
				roleMax[roleID] = numMax;
			}
			
		}
		
		/**
		 * Get the number of players already logged in to this game.
		 * 
		 * @return
		 */
		public function getActivePlayers():int
		{
			var count:int = 0;
			for each (var r:int in filledRoles) 
			{
				count += r;
			}
			return count;
		}
		
		/**
		 * Get the minimum number of players required to start this game.
		 * 
		 * @return
		 */
		public function getMinPlayers():int 
		{
			var count:int = 0;
			for each (var r:int in requiredMin) 
			{
				count += r;
			}
			return count;
		}
		
	}

}