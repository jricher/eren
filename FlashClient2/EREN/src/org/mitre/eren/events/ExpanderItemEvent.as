package org.mitre.eren.events 
{
	import flash.events.Event;
	
	/**
	 * Custom event class for ExpanderItem events.
	 * 
	 * @author Amanda Anganes
	 */
	public class ExpanderItemEvent extends Event
	{
		
		public static const RESTACK:String = "expander item restack event";
		public static const RESOURCE_SELECTION:String = "resource expander item selection event";
		public static const SELECTION_EVENT:String = "expander item selection event";
		public static const HEIGHT_CHANGE:String = "expander item height change";
		public static const MOVE_TO_SHOW:String = "expander item move to show event";
		
		private var _bubbles:Boolean;
		private var _cancelable:Boolean;
		public  var _type:String;
		
		public  var _selected:Boolean = false;
		public  var _facilityID:String;
		public  var _newHeight:Number;
		public  var _position:Number;
		
		/**
		 * Constructor 
		 * 
		 * @param	type       should be one of the consts defined by this class
		 * @param	bubbles    boolean; inherited from Event
		 * @param	cancelable boolean; inherited from Event
		 */
		public function ExpanderItemEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false)  
		{
			this._type = type;
			this._bubbles = bubbles;
			this._cancelable = cancelable;
			
			super(_type, _bubbles, _cancelable);
		}
		
		/**
		 * Implement clone() so that this event can bubble properly.
		 * 
		 * @return a copy of this object
		 */
		public override function clone():Event 
		{
			var e:ExpanderItemEvent = new ExpanderItemEvent(_type, _bubbles, _cancelable);
			e._selected = _selected;
			e._facilityID = _facilityID;
			e._position = _position;
			e._newHeight = _newHeight;
			
			return e;
		}
		
		/**
		 * Return a string representation of this event
		 * 
		 * @return a string
		 */
		public override function toString():String
		{
			return formatToString("ExpanderItemEvent", "_type", "_bubbles", "_cancelable");
		}
		
	}

}