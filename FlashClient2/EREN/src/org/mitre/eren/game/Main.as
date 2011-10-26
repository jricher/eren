package org.mitre.eren.game 
{
	import flash.display.MovieClip;
	import flash.display.Stage;
	import flash.events.Event;
	import flash.events.UncaughtErrorEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	/**
	 * Main entry point of application; Stage is created as an instance of Main.
	 * 
	 * Read config file for startup options, pass to relevant modules.
	 * @author a_anganes
	 */
	public class Main extends MovieClip
	{
		
		private var myEngine:Engine;
		private var mapKey:String;
		private var useBus:Boolean = true;
		private var port:Number;
		private var host:String;
		
		public static var STAGE:Stage;
		
		public function Main() 
		{
			STAGE = this.stage;
			readConfig();
			gotoAndStop(1);
			//TODO: add overlay to display error message; fullscreen no clickthroughs
			loaderInfo.uncaughtErrorEvents.addEventListener(UncaughtErrorEvent.UNCAUGHT_ERROR, uncaughtErrorHandler);
		}
		
		public function getEngine() 
		{
			return myEngine;
		}
		
		private function uncaughtErrorHandler(e:UncaughtErrorEvent):void 
		{
			trace("Got uncaught error " + e.toString());
		}
		
		private function readConfig():void 
		{
			var myLoader:URLLoader = new URLLoader();
			myLoader.load(new URLRequest("config.xml"));
			myLoader.addEventListener(Event.COMPLETE, processXML);
		}
		
		private function processXML(e:Event):void 
		{
			
			var configxml:XML = new XML(e.target.data);
			var fl:Namespace = new Namespace("flash:mitre:org:1.0"); 
			mapKey = configxml.fl::key.text();
			var busbool:String = configxml.fl::bus.text();
			
			if ( busbool == "true" ) {
				//use bus!
				useBus = true;
			}
			else if (busbool == "false" ){
				useBus = false;
			}
			
			host = configxml.fl::host.text();
			port = parseInt(configxml.fl::port.text());
			
			trace("read config file: ");
			trace("key: ." + mapKey + ".");
			trace(useBus);
			trace(host);
			trace(port);
			
			trace("constructing engine");
			myEngine = new Engine(this, mapKey, useBus, host, port);
		
		}			
		
	}

}