package src
{
	/*
	 * @author Amanda Courtemanche
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
	import src.comm_lib;
	
	/*
	 * Main entry point for application.  Stage is created as an instance of main. 
	 */
	public class main extends MovieClip
	{
		
		//XMLSocket connection to erenbus proxy
		private var xmlSocket:XMLSocket;
		
		private var socketGo:Boolean = true;
		private var activeSocket:Boolean = false;
		private var commLib:comm_lib;
		//private var output_txt:TextArea;
		
		//Constructor
		public function main()
		{
			//Use a socket boolean for debugging purposes
			if (socketGo) {
				//Create the socket
				xmlSocket = new XMLSocket();
				trace("Created socket");
				//Connect to proxy and add data listener
				xmlSocket.connect("erenbus.mitre.org", 8090);
				xmlSocket.addEventListener(DataEvent.DATA, onXMLData);
				trace("Connected");
				activeSocket = true;
			}
			
			//Create an instance of the communications library, passing
			//this as a parent reference so the commLib can access public
			//display methods
			commLib = new comm_lib(this);
	
			//Add event listeners for buttons
			play_bttn.addEventListener(MouseEvent.CLICK, playHandler);
			stop_bttn.addEventListener(MouseEvent.CLICK, stopHandler);
			pause_bttn.addEventListener(MouseEvent.CLICK, pauseHandler);
			sync_check.addEventListener(MouseEvent.CLICK, syncHandler);
			ratio_check.addEventListener(MouseEvent.CLICK, ratioHandler);
			
			// Update screen every frame
			addEventListener(Event.ENTER_FRAME,enterFrameHandler);
			
			//Restrict input fields so that only numbers can be entered
			ratio_txt.restrict = "0-9";
			year_txt.restrict = "0-9";
			day_txt.restrict = "0-9";
			month_txt.restrict = "0-9";
			time_txt.restrict = "0-9 :";
		
		}
		
		public function output(s:String):void 
		{
			trace(s);
			output_txt.text += s + "\n";
			autoscroll();
			
		}

		private function autoscroll():void
		{
   			setTimeout(function():void{output_txt.verticalScrollPosition = output_txt.maxVerticalScrollPosition;},100);
		}
		
		/*
		 * Public display methods, called by comm_lib after parsing
		 */ 
		
		public function setGameTime(t:String):void
		{
			curr_gametime.text = t;
		}
		
		public function setSync(t:String):void
		{
			curr_sync.text = t;
		}
		
		public function setRatio(t:String):void
		{
			curr_ratio.text = t;
		}
		
		/*
		 * XML/EDXL methods
		 */
		 
		 //Send a message, adding the standard wrapper with namespace
		private function sendMssg(mssg:XML):void 
		{
			/*
			var newMssg:XML = 
			<mssg>
				<sendtobus>
					{mssg}
				</sendtobus>
			</mssg>;*/
			
			if (activeSocket) 
			{
				xmlSocket.send(mssg);
				output("Sent: " + mssg);
			}
			else 
				output(mssg);
		}
			
		//XMLSocket listener - simply pass the data to the comm library for parsing
		private function onXMLData(event:DataEvent):void
		{
			output("[" + event.type + "]" + "  Data: " + event.data);
			output("String version: " + event.toString);
			
			var data:XML = new XML(event.data);
			commLib.parse(data);
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
			var mssg:XML = commLib.createClockStart(); 
			sendMssg(mssg);
			
			/*trace("testing tick\n");
			var clocktick:XML = 
			<eren:clocktick xmlns:eren="urn:mitre:eren:1.0">
				<eren:time>49531</eren:time>
			</eren:clocktick>;
			
			commLib.parse(clocktick);*/
			
		}
		
		//Send a message to the clock to reset game time - requires restart
		private function stopHandler(event:MouseEvent):void
		{
			var mssg:XML = commLib.createClockReset();
			sendMssg(mssg);
			
			/*trace("testing sync\n");
			var clocktick:XML = 
			<eren:clocksync xmlns:eren="urn:mitre:eren:1.0">
				<eren:date>2010-04-19T15:00:00 UST</eren:date>
			</eren:clocksync>;
			
			commLib.parse(clocktick);*/
		}
		
		//Send a message to the clock to pause the game
		private function pauseHandler(event:MouseEvent):void
		{
			var mssg:XML = commLib.createClockPause();
			sendMssg(mssg);
			
		/*	trace("testing ratio\n");
			var clocktick:XML = 
			<eren:clockspeed xmlns:eren="urn:mitre:eren:1.0">
				<eren:ratio>330</eren:ratio>
			</eren:clockspeed>;
			
			commLib.parse(clocktick);*/
		}
		
		//Set a new sync time
		private function syncHandler(event:MouseEvent):void
		{	
			var mssg:XML = commLib.createClockSync(getDate());
			sendMssg(mssg);
		}
		
		//Set a new gametime/realtime ratio
		private function ratioHandler(event:MouseEvent):void
		{
			var mssg:XML = commLib.createClockSpeed(getRatio());
			sendMssg(mssg);
		}
		
		//This method may be unnecessary...
		protected function enterFrameHandler(event:Event):void
		{
			
		}
		
	}
	
}