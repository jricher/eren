package org.mitre.eren.events 
{
	import flash.events.Event;
	import org.mitre.eren.data.dialog.UserMessage;
	import org.mitre.eren.data.scenario.PODupdate;
	import org.mitre.eren.data.scenario.RDStatus;
	import org.mitre.eren.data.scenario.ResourceUsageStats;

	/**
	 * Custom event class for message center related events. This is a super-typed event; all events 
	 * are fired as type MESSAGE_CENTER_EVENT, but once the event is recieved e._type will give the
	 * event type.
	 * 
	 * @author Amanda Anganes
	 */
	public class MessageCenterEvent extends Event
	{
		//Super type
		public static const MESSAGE_CENTER_EVENT:String = "Message Center Event";
		
		//Individual types
		public static const GOT_USER_MSSG:String = "Parser got user message";
		public static const GOT_PODSTATUS:String = "Parser got pod status";
		public static const GOT_RDSTATUS:String = "Parser got rd status";
		public static const DESELECT_NOTIFICATION:String = "Deselect notification object";
		public static const DISPLAY_USER_MSSG:String = "Display user message";
		public static const COMMIT_ACTION:String = "Commit a user action";
		public static const GOT_RESOURCE_USAGE_STATS:String = "parser got resource usage stats";
		
		public  var _type:String;
		private var _bubbles:Boolean;
		private var _cancelable:Boolean;
		
		public  var _rdStatus:RDStatus;
		public  var _podUpdate:PODupdate;
		public  var _userMssg:UserMessage;
		public  var _actionId:String;
		public  var _facilityId:String;
		public  var _resourceId:String = "";
		public  var _quantity:int = 0;
		public  var _resourceStats:ResourceUsageStats;
		
		/**
		 * Constructor.
		 * 
		 * @param	type       should be one of the consts defined in this class
		 * @param	bubbles    boolean; inherited from Event
		 * @param	cancelable boolean; inherited from Event
		 */
		public function MessageCenterEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false)
		{
			this._type = type;
			this._bubbles = bubbles;
			this._cancelable = cancelable;
			
			super(MESSAGE_CENTER_EVENT, bubbles, cancelable);
		}
		
		/**
		 * Override clone() so that this event can bubble properly
		 * 
		 * @return a copy of this object
		 */
		public override function clone():Event 
		{
			var mce:MessageCenterEvent = new MessageCenterEvent(MESSAGE_CENTER_EVENT, _bubbles, _cancelable);
			mce._type = _type;
			mce._rdStatus = _rdStatus;
			mce._podUpdate = _podUpdate;
			mce._userMssg = _userMssg;
			mce._actionId = _actionId;
			mce._facilityId = _facilityId;
			mce._resourceId = _resourceId;
			mce._quantity = _quantity;
			mce._resourceStats = _resourceStats;
			
			return mce;
		}
		
		public override function toString():String 
		{
			return formatToString("MessageCenterEvent", "_type", "bubbles", "cancelable");
		}
		
	}

}