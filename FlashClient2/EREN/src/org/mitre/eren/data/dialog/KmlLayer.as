package org.mitre.eren.data.dialog 
{
	
	import com.distriqt.gmaps.kml.utils.KmlDisplayObject;
	import com.distriqt.gmaps.kml.utils.KmlLoader;
	import com.google.maps.interfaces.IPane;
	import com.google.maps.Map;
	import com.google.maps.MapMouseEvent;
	import com.google.maps.InfoWindowOptions;
	import flash.display.DisplayObject;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import org.mitre.eren.display.elements.MapDisplay;
	import org.mitre.eren.display.map.resourceInfoWindow;
	
	/**
	 * A KmlLayer uses the KmlLoader from the distriqt library to load kml files
	 * from a given set of urls, convert those files into display objects, and insert 
	 * those display objects into a Google Maps IPane. This IPane can then be 
	 * directly added to the map instance.
	 * 
	 * @author Amanda Anganes
	 */
	public class KmlLayer extends EventDispatcher
	{
		
		private var _name:String;
		private var _urls:Array;
		private var _pane:IPane;
		private var objects:Vector.<KmlDisplayObject>;
		private var _loader:KmlLoader;
		
		private var loadedSoFar:int = 0;
		private var loadingIndex:int = 0;
		
		private var _map:Map;
		
		public function KmlLayer(s:String) 
		{
			this._name = s;
			_urls = new Array();
			_loader = new KmlLoader();
			objects = new Vector.<KmlDisplayObject>();
		}
		
		public function setMap(m:Map):void
		{
			this._map = m;
		}
		
		public function addURL(s:String):void 
		{
			_urls.push(s);
		}
		
		public function get urls():Array 
		{
			return this._urls;
		}
		
		override public function toString():String
		{
			var s:String = "KmlLayer: " + _name + ", " + _urls.length + " urls\n";
			for each (var u:String in _urls) {
				s += "\t" + u + "\n";
			}
			return s;
		}
		
		public function get pane():IPane 
		{
			return this._pane;
		}
		
		public function set pane(p:IPane):void 
		{
			this._pane = p;
		}
		
		public function get name(): String
		{
			return this._name;
		}
		
		public function loadKML():void 
		{			
			//trace("*****************loading kml for layer " + name);
			_loader.load(_urls.pop());
			_loader.addEventListener(Event.COMPLETE, loader_completeHandler);			
		}
		
		public function hideLayer():void 
		{
			_pane.visible = false;
		}
		
		public function showLayer():void 
		{
			_pane.visible = true;
		}
		
		private function loader_completeHandler(e:Event):void 
		{
			for each (var o:KmlDisplayObject in _loader.objects) 
			{
				//trace("adding object " + o + " to layer");
				this.objects.push(o);
			}
			_loader = new KmlLoader();
			
			if (_urls.length >= 1)
			{
				//trace("loading next url");
				_loader.load(_urls.pop());
				_loader.addEventListener(Event.COMPLETE, loader_completeHandler);
			} 
			else
			{
				//trace("complete");
				complete();
			}
		}
		
		private function complete():void 
		{
			//trace("KMLLayer " + name + " complete! Adding objects to map pane");
			for each (var ob:KmlDisplayObject in objects) 
				{
					addObject(ob);
					addInteraction(ob);
				}
				
				dispatchEvent( new Event( Event.COMPLETE ));
		}
		
		private function addInteraction(_object:KmlDisplayObject):void
		{
			//trace("Adding interaction to " + _object.name);
			_object.interactive = true;
			_object.addEventListener(MapMouseEvent.ROLL_OVER, rolloverhandler, false, 0, true);
			
			for each (var _child:KmlDisplayObject in _object.children)
			{
				addInteraction( _child );
			}
			
		}
		
		private function rolloverhandler(e:MapMouseEvent):void
		{
			var _object:KmlDisplayObject = KmlDisplayObject(e.currentTarget);
			var riw:resourceInfoWindow = new resourceInfoWindow(_object.name, _object.name);
			riw.addEventListener(MapMouseEvent.CLICK, clickHandler, false, 0, true);
			var iwo:InfoWindowOptions = new InfoWindowOptions();
			iwo.customContent = riw;
			iwo.customOffset = new Point(20, riw.height);
			_map.openInfoWindow( _object.latLng, iwo);
			trace("rollover overlay");
		}
		
		
		private function clickHandler(e:MapMouseEvent):void
		{
			_map.closeInfoWindow();
		}
		
		private function addObject( _object:KmlDisplayObject ):void
		{
			// It may just be a container with child elements
			//    so check if there is an overlay
			if (_object.overlay != null)
			{
				// Here you can add the kml object to your map
				_pane.addOverlay( _object.overlay );
			}
			// Add the children
			for each (var _child:KmlDisplayObject in _object.children)
			{
				addObject( _child );
			}
		}
		
	}

}