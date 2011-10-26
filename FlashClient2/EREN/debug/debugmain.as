package debug
{
	/*
	 * @author Amanda Anganes
	 */ 
	
	import fl.controls.TextArea;
	import fl.controls.ScrollPolicy;
	import flash.net.NetConnection;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.NetStatusEvent;
	import flash.events.DataEvent;
	import flash.text.TextField;
	import flash.display.MovieClip;
	import flash.display.SimpleButton;
	import flash.net.XMLSocket;
	import flash.system.Security;
	import flash.utils.setTimeout;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	/*
	 * Main entry point for application.  Stage is created as an instance of main. 
	 */
	public class debugmain extends MovieClip
	{
		
		//XMLSocket connection to erenbus proxy
		private var xmlSocket:XMLSocket;
		
		private var socketGo:Boolean = true;
		private var activeSocket:Boolean = false;
		
		//Constructor
		public function debugmain()
		{
			readConfig();
			
			//Add event listeners for buttons
			play_bttn.addEventListener(MouseEvent.CLICK, playHandler);
			stop_bttn.addEventListener(MouseEvent.CLICK, stopHandler);
			pause_bttn.addEventListener(MouseEvent.CLICK, pauseHandler);
			sync_check.addEventListener(MouseEvent.CLICK, syncHandler);
			ratio_check.addEventListener(MouseEvent.CLICK, ratioHandler);
			
			//Restrict input fields so that only numbers can be entered
			ratio_txt.restrict = "0-9";
			year_txt.restrict = "0-9";
			day_txt.restrict = "0-9";
			month_txt.restrict = "0-9";
			time_txt.restrict = "0-9 :";
		
		}
		
		
		private function readConfig():void {
			trace("opening config file");
			var myLoader:URLLoader = new URLLoader();
			myLoader.load(new URLRequest("config.xml"));
			myLoader.addEventListener(Event.COMPLETE, processXML);
		}
		
		private function processXML(e:Event):void {
			trace("processing xml");
			var configxml:XML = new XML(e.target.data);
			trace("xml is " + configxml);
			var fl:Namespace = new Namespace("flash:mitre:org:1.0"); 
			var busbool:String = configxml.fl::bus.text();
			
			var host:String = configxml.fl::host.text();
			var port:int = parseInt(configxml.fl::port.text());
			
			output("read config file: ");
			output("usebus:" + busbool);
			output("host:" + host);
			output("port:" + port);
			
			//Use a socket boolean for debugging purposes
			if (busbool) {
				//Create the socket
				xmlSocket = new XMLSocket();
				trace("Created socket");
				//Connect to proxy and add data listener
				xmlSocket.connect(host, port);
				xmlSocket.addEventListener(DataEvent.DATA, onXMLData);
				trace("Connected");
				activeSocket = true;
			}
		
		}	
		
		public function output(s:String):void 
		{
			//trace(s);
			output_txt.text += s + "\n";
			autoscroll();
			
		}

		private function autoscroll():void
		{
   			setTimeout(function():void{output_txt.verticalScrollPosition = output_txt.maxVerticalScrollPosition;},100);
		}
		
		/*
		 * XML/EDXL methods
		 */
		 
		 //Send a message, adding the standard wrapper with namespace
		private function sendMssg(mssg:XML):void 
		{
			if (activeSocket) 
			{
				xmlSocket.send(mssg);
				output("Sent: " + mssg);
			}
			else 
				output(mssg);
		}
			
		//XMLSocket listener - simply pass the data to the parser
		private function onXMLData(event:DataEvent):void
		{
			output("[" + event.type + "]" + "  Data: " + event.data);
			output("String version: " + event.toString);
			
			var data:XML = new XML(event.data);
			parse(data);
		}
		
		//Return a date string properly formatted for sending with EDXL
		private function getDate():String
		{
			var time:String = time_txt.text;
			
			var month:Number = Number(month_txt.text) - 1;
			
			var t:String = time.substring(0,2) + ":" 
							+ time.substring(3, 5) + ":" + time.substring(6, 8);
							
			if (month_txt.text.length == 1) month_txt.text = "0" + month_txt.text;
			if (day_txt.text.length == 1) day_txt.text = "0" + day_txt.text;
			
			var s:String = "" + year_txt.text + "-" + month_txt.text 
							+ "-" + day_txt.text + "T" + t 
							+ " UST";
		
			return s;	
		}
		
		//For consistancy only - nothing complicated going on here
		private function getRatio():String
		{
			return ratio_txt.text;
		}
		
		/*  
		 * Button handlers for main window.
		 */
		 
		 //Send a message to the clock to start game time
		private function playHandler(event:MouseEvent):void
		{
			var mssg:XML = createClockStart(); 
			sendMssg(mssg);	
		}
		
		//Send a message to the clock to reset game time - requires restart
		private function stopHandler(event:MouseEvent):void
		{
			var mssg:XML = createClockReset();
			sendMssg(mssg);
		}
		
		//Send a message to the clock to pause the game
		private function pauseHandler(event:MouseEvent):void
		{
			var mssg:XML = createClockPause();
			sendMssg(mssg);
		}
		
		//Set a new sync time
		private function syncHandler(event:MouseEvent):void
		{	
			var mssg:XML = createClockSync(getDate());
			sendMssg(mssg);
		}
		
		//Set a new gametime/realtime ratio
		private function ratioHandler(event:MouseEvent):void
		{
			var mssg:XML = createClockSpeed(getRatio());
			sendMssg(mssg);
		}
		
				/*
		 * XML creation methods
		 */ 
		
		//Create a set clock sync message
		internal function createClockSync(date:String):XML
		{
			var sync:XML =
			<eren:mssg xmlns:eren="urn:mitre:eren:1.0">
				<eren:sendtobus>
					<eren:setclocksync>
						<eren:date>{date}</eren:date>
					</eren:setclocksync>
				</eren:sendtobus>
			</eren:mssg>;
			
			return sync;
		}
		
		//Create a set ratio message
		internal function createClockSpeed(ratio:String):XML
		{
			var mssg:XML =
			<eren:mssg xmlns:eren="urn:mitre:eren:1.0">
				<eren:sendtobus>
					<eren:setclockspeed>
						<eren:ratio>{ratio}</eren:ratio>
					</eren:setclockspeed>
				</eren:sendtobus>
			</eren:mssg>;
			
			return mssg;
		}
		
		//Create a clock start message
		internal function createClockStart():XML
		{
			var mssg:XML = 
			<eren:mssg xmlns:eren="urn:mitre:eren:1.0">
				<eren:sendtobus>
					<eren:clockstart/>
				</eren:sendtobus>
			</eren:mssg>;
			return mssg;
		}
		
		//Create a clock pause message
		internal function createClockPause():XML
		{
			var mssg:XML = 
			<eren:mssg xmlns:eren="urn:mitre:eren:1.0">
				<eren:sendtobus>
					<eren:clockpause/>
				</eren:sendtobus>
			</eren:mssg>;
			return mssg;
		}
	
		//Create a clock reset message
		internal function createClockReset():XML
		{
			var mssg:XML = 
			<eren:mssg xmlns:eren="urn:mitre:eren:1.0">
				<eren:sendtobus>
					<eren:clockreset/>
				</eren:sendtobus>
			</eren:mssg>;
			return mssg;
		}

		/*
		 * Update methods called by the parser
		 */ 
		
		internal function updateTime(time:Number):void
		{
			//Convert time to hours, minutes, seconds
			var hours:Number = time / 3600;
			hours = Math.floor(hours);
			var minutes:Number = time / 60 - (hours * 60);
			minutes = Math.floor(minutes);
			var seconds:Number = time % 60;
			
			//Compose the value into a time string
			var tmString:String = "" + hours + ":" + minutes + ":" + seconds;
			
			//Change the display
			curr_gametime.text = tmString;
			
		}
		
		internal function updateSync(sync:String):void
		{
			//Parse sync string into year, month, day, time
			var year:String = sync.substring(0,4);
			var month:String = sync.substring(5,7);
			var day:String = sync.substring(8,10);
			var time:String = sync.substring(11,19);
			
			//Compose a 'nice' version of the time string
			var niceDate:String = "" + month + "/" + day + " " + year + " " + time;
			
			//Change the display
			curr_sync.text = niceDate;
		}
		
		/*
		 * Parser
		 */ 
		
		internal function parse(x_xml:XML):void 
		{
			var cmd:String = x_xml.name().localName as String;
			var value:String = x_xml.children()[0].text();
			output("Parsing: cmd = " + cmd + "\nvalue = " + value);
			
			//For now, only parse clock messages.  
			if (cmd == "clocktick")
			{
				//update clock with new time
				var time:Number = Number(value);
				updateTime(time);
			}
			else if (cmd == "clocksync")
			{
				updateSync(value);
			}
			else if (cmd == "clockspeed")
			{
				curr_ratio.text = value;
			}
			else {
				output("clock parser: unknown xml");
			}
		}
		
	}
	
}