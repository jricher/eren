package org.mitre.eren.events 
{
	import flash.events.Event;
	import org.mitre.eren.data.actions.Action;
	
	/**
	 * Custom event class for Action-related events.
	 * 
	 * @author Amanda Anganes
	 */
	public class ActionEvent extends Event 
	{
		
		public static const FIRE_ACTION:String = "eren fire action event";
		public static const GET_INPUT:String = "eren get input event";
		
		private var _type:String;
		private var _bubbles:Boolean;
		private var _cancelable:Boolean;
		
		public var action:Action;
		public var facilityId:String;
		
		/**
		 * Constructor.
		 * 
		 * @param	type       should be one of the consts defined in ActionEvent
		 * @param	bubbles    boolean; inherited from Event
		 * @param	cancelable boolean; inherited from Event
		 */
		public function ActionEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false)
		{
			this._type = type;
			this._bubbles = bubbles;
			this._cancelable = cancelable;
			
			super(type, bubbles, cancelable);
		}
		
		/**
		 * Implement clone() so that this event can be bubbled properly
		 * 
		 * @return a copy of this ActionEvent
		 */
		public override function clone():Event
		{
			var e:ActionEvent = new ActionEvent(_type, _bubbles, _cancelable);
			
			e.action = this.action;
			e.facilityId = this.facilityId;
			
			return e;
		}
		
		/**
		 * Override toString for easier debugging
		 * 
		 * @return a String representation of this object
		 */
		public override function toString():String
		{
			var s:String = "ActionEvent--type:";
			if (_type != null) s += _type;
			else s += "null";
			s += " action:";
			if (action != null && action.type != null) s += action.type;
			else s += "null";
			s += ",";
			if (action != null && action.type != null) s += action.displayName;
			else s += "null";
			return s;
		}
		
	}

}