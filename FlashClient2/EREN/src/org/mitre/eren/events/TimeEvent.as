package org.mitre.eren.events 
{
	import flash.events.Event;
	
	/**
	 * Custom event class for time-related events.
	 * 
	 * @author Amanda Anganes
	 */
	public class TimeEvent extends Event 
	{
		public static const TIME_EVENT:String = "Time event";
		public static const GOT_DATE:String = "Parser got date mssg";
		public static const GOT_RATIO:String = "Parser got ration mssg";
		public static const GOT_TICK:String = "Parser got tick mssg";
		
		public var _type:String;
		public var _date:Date;
		public var _ratio:int;
		public var _seconds:int;
		
		private var _bubbles:Boolean;
		private var _cancelable:Boolean;
		
		/**
		 * Constructor.
		 * 
		 * @param	type       should be one of the consts defined in this class
		 * @param	bubbles    boolean; inherited from Event
		 * @param	cancelable boolean; inherited from Event
		 */
		public function TimeEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false) 
		{
			this._type = type;
			this._bubbles = bubbles;
			this._cancelable = cancelable;
			
			super(TIME_EVENT, bubbles, cancelable);
		}
		
		/**
		 * Implement clone() so that this event can bubble properly
		 * 
		 * @return a copy of this object
		 */
		public override function clone():Event 
		{
			var te:TimeEvent = new TimeEvent(TIME_EVENT, _bubbles, _cancelable);
			te._type = _type;
			te._date = _date;
			te._ratio = _ratio;
			te._seconds = _seconds;
			
			return te;
		}
		
		public override function toString():String 
		{
			if (_type == GOT_DATE) 
			{
				return formatToString("TimeEvent", "_type", "_date", "_bubbles", "_cancelable");
			}
			else if (_type == GOT_RATIO)
			{
				return formatToString("TimeEvent", "_type", "_ratio", "_bubbles", "_cancelable");
			}
			else if (_type == GOT_TICK) 
			{
				return formatToString("TimeEvent", "_type", "_seconds", "_bubbles", "_cancelable");
			}
			else 
			{
				return formatToString("TimeEvent", "_type", "_bubbles", "_cancelable");
			}
		}
		
	}

}