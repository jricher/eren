package org.mitre.eren.events 
{
	import flash.events.Event;
	
	/**
	 * Custom event class for message sending events. This is a super-typed event; all events 
	 * are fired as type SEND, but once the event is recieved e._type will give the
	 * event type.
	 * 
	 * @author Amanda Anganes
	 */
	public class SendMessageEvent extends Event 
	{
		//Super event type
		public static const SEND:String = "Send Message Event";
		
		//Individual event types
		public static const SINGLE_RESPONSE:String = "Send single response";
		public static const MULTI_RESPONSE:String = "Send multli response";
		public static const VALUE_RESPONSE:String = "Send value response";
		
		public var _type:String;
		private var _bubbles:Boolean = false;
		private var _cancelable:Boolean = false;
		
		public var _messageID:String;
		public var _singleResponseID:String;
		public var _responseIDs:Vector.<String>;
		public var _value:Number;
		
		/**
		 * Constructor.
		 * 
		 * @param	type       should be one of the consts defined in this class
		 * @param	bubbles    boolean; inherited from Event
		 * @param	cancelable boolean; inherited from Event
		 */
		public function SendMessageEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false)
		{
			this._type = type;
			this._bubbles = bubbles;
			this._cancelable = cancelable;
			
			super(SEND, _bubbles, _cancelable);
		}
		
		/**
		 * Implement clone() so that this event can bubble correctly
		 * 
		 * @return a copy of this object
		 */
		public override function clone():Event
		{
			var sme:SendMessageEvent = new SendMessageEvent(SEND, _bubbles, _cancelable);
			sme._type = _type;
			sme._messageID = _messageID;
			sme._responseIDs = _responseIDs;
			sme._singleResponseID = _singleResponseID;
			sme._value = _value;
			
			return sme;
		}
		
		public override function toString():String 
		{
			var s:String = "[SendMessageEvent type=" + this._type + " bubbles=" + this._bubbles + " cancelable=" + _cancelable + "]";
			return s;
		}
		
	}

}