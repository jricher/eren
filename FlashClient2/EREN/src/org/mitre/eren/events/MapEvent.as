package org.mitre.eren.events 
{
	import flash.events.Event;
	import org.mitre.eren.display.elements.MapDisplay;
	import com.google.maps.LatLng;
	import org.mitre.eren.data.dialog.KmlLayer;
	
	/**
	 * Custom event class for map related events. This is a super-typed event; all events 
	 * are fired as type MAP_EVENT, but once the event is recieved e._type will give the
	 * event type.
	 * 
	 * @author Amanda Anganes
	 */
	public class MapEvent extends Event 
	{
		//Super event type
		public static const MAP_EVENT:String = "MapEvent";
		
		//Individual event types
		public static const MAP_READY:String = "Map ready";
		public static const FLY_TO_LOCATION:String = "Fly to a location";
		public static const DISPLAY_KML:String = "Display KML";
		
		public  var _type:String;
		private var _bubbles:Boolean;
		private var _cancelable:Boolean;
		
		public var _kmlLayers:Vector.<KmlLayer>;
		public var _latlng:LatLng;
		public var _facID:String;
		
		/**
		 * Constructor.
		 * 
		 * @param	type       should be one of the consts defined in this class
		 * @param	bubbles    boolean; inherited from Event
		 * @param	cancelable boolean; inherited from Event
		 */
		public function MapEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false)
		{
			this._type = type;
			this._bubbles = bubbles;
			this._cancelable = cancelable;
			
			super(MAP_EVENT, _bubbles, _cancelable);
		}
		
		/**
		 * Implement clone() so that this event can bubble properly.
		 * 
		 * @return a copy of this object
		 */
		public override function clone():Event
		{
			var me:MapEvent = new MapEvent(MAP_EVENT, _bubbles, _cancelable);
			me._type = _type;
			me._kmlLayers = _kmlLayers;
			me._latlng = _latlng;
			me._facID = _facID;
			
			return me;
		}
		
		public override function toString():String 
		{
			return formatToString("MapEvnet", "_type", "_bubbles", "_cancelable");
		}
		
	}

}