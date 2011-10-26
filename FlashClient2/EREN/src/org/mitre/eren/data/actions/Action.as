package org.mitre.eren.data.actions 
{
	/**
	 * Class representation of an individual action entry from the actions file
	 * 
	 * @author Amanda Anganes
	 */
	public class Action 
	{
		
		public var type:String;
		public var displayName:String;
		public var resourceId:String = "";
		public var quantity:int = 0;
		
		public var roles:Vector.<String>;
		public var stateTriggers:Vector.<String>;
		public var facilities:Vector.<String>;
		public var inputsVector:Vector.<Input>;
		
		public function Action() 
		{
			roles = new Vector.<String>();
			stateTriggers = new Vector.<String>();
			facilities = new Vector.<String>();
			inputsVector = new Vector.<Input>();
		}
		
		/**
		 * Create an action that just has a type and display name. This is used
		 * as a workaround by the ActionsMenu class to make a menu item for 
		 * displaying facility details.
		 * 
		 * @param	type
		 * @param	displayName
		 */
		public static function createDummyAction(type:String, displayName:String) {
			var act:Action = new Action();
			act.type = type;
			act.displayName = displayName;
			return act;
		}
		
		/**
		 * Create and return a new Action by parsing XML
		 * 
		 * @param	a xml "eren:action" document
		 * @return an Action object
		 */
		public static function parseAction(a:XML):Action {
			var action:Action = new Action();
			var act:Namespace = new Namespace("urn:mitre:eren:actions:1.0");
			var eren:Namespace = new Namespace("urn:mitre:eren:1.0");
			
			action.type = a.act::type.text();
			action.displayName = a.act::displayName.text();
			
			var rolesList:XMLList = a.act::roles.act::role;
			for each (var role:XML in rolesList) 
			{
				action.roles.push(role.text());
			}
			
			var triggers:XMLList = a.act::statetriggers.eren::status;
			for each (var t:XML in triggers)
			{
				action.stateTriggers.push(t.text());
			}	
			
			var facs:XMLList = a.act::facilityIds.act::facilityId;
			for each (var f:XML in facs) 
			{
				action.facilities.push(f.text());
			}
			
			var resourceQName:QName = new QName(act, "resource");
			if (a.hasOwnProperty(resourceQName))
			{
				action.resourceId = a.act::resource.text();
			}
			
			var inputQName:QName = new QName(act, "inputs");
			if (a.hasOwnProperty(inputQName)) 
			{
				var inputsList:XMLList = a.act::inputs.act::input;
				for each (var input:XML in inputsList) 
				{
					var type:String = input.@type;
					var name:String = input.@name;
					var from:String = input.@from;
					var qtext:String = input.act::questionText.text();
					
					trace("processing input: type = " + type + ", name = " + name + ", from = " + from + ", question text = " + qtext);
					
					var theInput:Input = new Input(type, name, from, qtext);
					
					var minQName:QName = new QName(eren, "min");
					if (input.hasOwnProperty(minQName)) 
					{
						var min:int = parseInt(input.eren::min.text());
						var max:int = parseInt(input.eren::max.text());
						theInput.max = max;
						theInput.min = min;
					}
					
					action.inputsVector.push(theInput);
				}
			}
			
			return action;
		}
		
		/**
		 * Get the next unfilled input from this Action
		 * 
		 * @return
		 */
		public function getNextUnfilledInput():Input 
		{
			
			for each (var input:Object in inputsVector) 
			{
				var iinput:Input = input as Input;
				if (iinput.filled == false) {
					return iinput;
				}
			}
			return null;
		}
		
		public function toString():String {
			var s:String = "Action: ";
			s += "type = " + (type == null ? "null" : type);
			s += ", display name = " + (displayName == null ? "null" : displayName);
			return s;
		}
	}
}