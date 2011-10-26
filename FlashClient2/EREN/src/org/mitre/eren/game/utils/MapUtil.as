package org.mitre.eren.game.utils 
{
	import com.google.maps.controls.PositionControl;
	import com.google.maps.controls.ZoomControl;
	import com.google.maps.interfaces.IOverlay;
	import com.google.maps.LatLng;
	import com.google.maps.Map;
	import com.google.maps.MapEvent;
	import com.google.maps.MapOptions;
	import com.google.maps.MapType;
	import com.google.maps.overlays.Marker;
	import com.google.maps.overlays.MarkerOptions;
	import flash.display.MovieClip;
	import flash.geom.Point;
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import org.mitre.eren.Constants;
	import org.mitre.eren.events.GameEvent;
	import org.mitre.eren.events.MapEvent;

	/**
	 * Map utility to interface with Google Maps for Flash library. 
	 * 
	 * @author Amanda Anganes
	 */
	public class MapUtil extends MovieClip
	{
		/** The Google Maps for Flash API key for this map **/
		private var mapkey:String;
		
		/** LatLng coordinate at which to center the map when it is created **/
		private var startcenter:LatLng;
		
		/** The dimensions of the map, defaulted to Constants.DEFAULT_MAP_SIZE **/
		private var size:Point = Constants.DEFAULT_MAP_SIZE;
		
		/** Reference to the actual Google map **/
		private var theMap:Map;
		
		/**
		 * Constructor. This object is instantiated on the stage, so the 
		 * constructor is empty.
		 */
		public function MapUtil() 
		{
			
		}
		
		/**
		 * Set the Google Maps for Flash API key. This MUST be set before trying to initialize the map, or else
		 * an error is thrown. Unfortunately there is no error thrown by the Google Map object if the API key
		 * is bad or there is no network access, so that must be troubleshooted by hand.
		 * 
		 * @param	s the String key
		 */
		public function setKey(s:String):void 
		{
			mapkey = s;
		}
		
		/**
		 * Set the size of the map
		 * 
		 * @param	p a point representing the desired map size
		 */
		public function setSize(p:Point):void 
		{
			size = p;
		}
		
		/**
		 * Initialize the map. This should be called after setKey() and setSize(),
		 * and a center position must be provided. Once this method is called, 
		 * the callee should listen for the (EREN) MapEvent.MAP_READY from this class. Once
		 * that event is fired the map is completely ready to use.
		 * 
		 * @param	center a LatLng object specifying the center coordinates for the map
		 */
		public function initializeMap(center:LatLng):void 
		{
			if (mapkey == null) 
			{
				throwError("Map did not have a key set, aborting");
			}
			else 
			{
				trace("Creating map");
				theMap = new Map();
				
				theMap.key = mapkey;
				theMap.sensor = "false";
				theMap.setSize(size);
				trace("set map key : " + mapkey + ".");
				var newcenter:LatLng = new LatLng(center.lat() + 15, center.lng());
				startcenter = newcenter;
				theMap.addEventListener(com.google.maps.MapEvent.MAP_PREINITIALIZE, onMapPreinit);
				trace("Waiting for map to be ready");
				trace("map key = " + theMap.key);
				this.addChild(theMap);
			}
		}
		
		/**
		 * Handle the GoogleMaps MAP_PREINITIALIZE event. Set initialization 
		 * parameters and wait for all the map tiles to load.
		 * 
		 * @param	event
		 */
		private function onMapPreinit(event:com.google.maps.MapEvent):void 
		{
			trace("map pre init'd");
			var initOptions:MapOptions = new MapOptions({
				continuousZoom: false,
				mapType: MapType.NORMAL_MAP_TYPE,
				center: startcenter,
				zoom: Constants.DEFAULT_ZOOM_LEVEL
			});
			
			theMap.setInitOptions(initOptions);
			theMap.addEventListener(com.google.maps.MapEvent.TILES_LOADED, onTilesLoaded);
			theMap.removeEventListener(com.google.maps.MapEvent.MAP_PREINITIALIZE, onMapPreinit);
		}
		
		/**
		 * Pan the map to a given location.
		 * 
		 * @param	l LatLng object specifying new center location
		 */
		public function flytocenter(l:LatLng):void 
		{
			theMap.panTo(l);
		}
		
		/**
		 * Set the zoom level of the map
		 * 
		 * @param	zoomlevel the new zoom level
		 */
		public function setZoom(zoomlevel:int):void 
		{
			theMap.setZoom(zoomlevel);
		}
		
		/**
		 * Add an overlay to the map
		 * 
		 * @param	o the IOverlay object to add
		 */
		public function addOverlay(o:IOverlay):void 
		{
			theMap.addOverlay(o);
		}
		
		/**
		 * Handle the Google Maps TILES_LOADED event. Add the ZoomControl and PositionControl,
		 * and fire the EREN MAP_READY event.
		 * 
		 * @param	e
		 */
		private function onTilesLoaded(e:com.google.maps.MapEvent):void 
		{
			//remove the event listener so this only happens once
			theMap.removeEventListener(com.google.maps.MapEvent.TILES_LOADED, onTilesLoaded);
			trace("tiles are loaded");
			theMap.addControl(new ZoomControl());
			theMap.addControl(new PositionControl());
			trace("firing mapready back to engine");
			fireMapIsReady();
		}
		
		/**
		 * Create and place a label anywhere on the map.
		 * 
		 * @param	l the coordinate at which to place this label
		 * @param	s the String content of this label
		 */
		public function label(l:LatLng, s:String):void 
		{	
			if (s.length < 1) s = "Facility";
			
			var lbl:TextField = new TextField();
			lbl.text = s;
			lbl.background = true;
			lbl.border = true;
			var tf:TextFormat = new TextFormat()
			//tf.font = eb.fontName;
			tf.size = 8;
			
			lbl.defaultTextFormat = tf;
			//lbl.embedFonts = true;
			lbl.autoSize = TextFieldAutoSize.LEFT;
			lbl.selectable = false;
		
			var mo:MarkerOptions = new MarkerOptions();			
			mo.icon = lbl;
			
			var m:Marker = new Marker(l, mo);
			theMap.addOverlay(m);
		}
		
		/**
		 * Fire the eren.events.MapEvent.MAP_READY event.
		 */
		private function fireMapIsReady():void 
		{	
			var me:org.mitre.eren.events.MapEvent = new org.mitre.eren.events.MapEvent(org.mitre.eren.events.MapEvent.MAP_READY);
			dispatchEvent(me);
		}
		
		/**
		 * Get the Google Maps Map instance from this MapUtil
		 * 
		 * @return the Map
		 */
		public function getMap():Map 
		{
			return this.theMap;
		}
		
		/**
		 * Throw an error. 
		 * 
		 * @param	e error text
		 */
		private function throwError(e:String):void 
		{
			dispatchEvent(new GameEvent(GameEvent.ERROR));
		}
	}

}