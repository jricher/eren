package org.mitre.eren.data.actions 
{
	import org.as3commons.collections.framework.core.LinkedMapIterator;
	import org.as3commons.collections.framework.core.MapIterator;
	import org.as3commons.collections.framework.IIterator;
	import org.as3commons.collections.LinkedMap;
	import org.mitre.eren.data.actions.Action;
	import flash.utils.Dictionary;
	
	/**
	 * Class representation of the Actions File, which contains a list of actions.
	 * This class parses that actions list directly into the actionsVector, and also 
	 * populates a map, keyed by facility id, of all the actions available to a 
	 * particular facility.
	 * 
	 * @author Amanda Anganes
	 */
	public class ActionsFile 
	{
		
		public var facilityToActionsMap:LinkedMap;
		public var actionsVector:Vector.<Action>;
		
		/**
		 * Constructor. Takes an xml document (the actions file) and parses
		 * it into accessable fields.
		 * @param	x
		 */
		public function ActionsFile(x:XML) 
		{
			var act:Namespace = new Namespace("urn:mitre:eren:actions:1.0");
			
			facilityToActionsMap = new LinkedMap();
			actionsVector = new Vector.<Action>();
			
			var actionsList:XMLList = x.act::action;			
			for each (var xaction:XML in actionsList) 
			{
				var a:Action = Action.parseAction(xaction);
				trace("Got action of type " + a.type + " with display name " + a.displayName);
				actionsVector.push(a);
			}
			
			for each (var action:Action in actionsVector) 
			{
				for each (var fID:String in action.facilities) 
				{
					if (!facilityToActionsMap.hasKey(fID))
					{
						facilityToActionsMap.add(fID, new LinkedMap());
					}
					(facilityToActionsMap.itemFor(fID) as LinkedMap).add(action.displayName, action);
				}
			}
		}
		
		public function toString():String 
		{
			var string:String = "ActionsFile:\n";
			var i:int = 1;
			var iterator:LinkedMapIterator = facilityToActionsMap.iterator() as LinkedMapIterator;
			while (iterator.hasNext()) 
			{
				iterator.next();
				string += "   Actions for facility " + iterator.key + ":\n";
				var innerIterator:LinkedMapIterator = (iterator.current as LinkedMap).iterator() as LinkedMapIterator;
				while (innerIterator.hasNext()) 
				{
					innerIterator.next();
					var action:Action = innerIterator.current as Action;
					string += "      Action " + action.type + "\n";
				}
			}
			return string;
		}
		
	}

}