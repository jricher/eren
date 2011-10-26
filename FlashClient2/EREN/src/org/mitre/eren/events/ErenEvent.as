package org.mitre.eren.events 
{
	import flash.events.Event;
	
	/**
	 * Custom event class for EREN.
	 * 
	 * @author Amanda Anganes
	 */
	public class ErenEvent extends Event 
	{
		public static const CHECK_SELECTION_EVENT:String = "eren checkbox selected";
		public static const NOTIFICATION_CLICK:String = "eren notification clicked";
		public static const READY:String = "eren ready event";
		public static const SELECTION_EVENT:String = "eren generic selection event";
		
		private var _type:String;
		private var _bubbles:Boolean;
		private var _cancelable:Boolean;
		
		public  var _checkboxName:String;
		public  var _isSelected:Boolean;
		
		/**
		 * Constructor
		 * 
		 * @param	type       should be one of the consts defined in ErenEvent
		 * @param	bubbles    boolean; inherited from Event
		 * @param	cancelable boolean; inherited from Event
		 */
		public function ErenEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false) 
		{
			this._type = type;
			this._bubbles = bubbles;
			this._cancelable = cancelable;
			
			super(type, bubbles, cancelable);
		}
		
		/**
		 * Implement clone() so that this event can be bubbled correctly.
		 * 
		 * @return a copy of this object
		 */
		public override function clone():Event
		{
			var e:ErenEvent = new ErenEvent(_type, _bubbles, _cancelable);
			e._checkboxName = _checkboxName;
			e._isSelected = _isSelected;
			
			return e;
		}
		
		/**
		 * Return a string representation of this object
		 * 
		 * @return a string
		 */
		public override function toString():String
		{
			return formatToString("ErenEvent", "_type", "_bubbles", "_cancelable");
		}
		
	}

}