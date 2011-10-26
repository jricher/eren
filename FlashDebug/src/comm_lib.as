package src 
{	
	/**
	 * Communications library for EREN Flash front-end.
	 * 
	 * @author Amanda Courtemanche
	 */
	internal class comm_lib
	{
		
		private var parent_ref:Object;
		
		//Constructor.  Pass in an reference to the parent
		//object (main) so we can call main's public dipslay methods.
		public function comm_lib(ref:*) 
		{
			parent_ref = ref;
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
			parent_ref.setGameTime(tmString);
			
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
			parent_ref.setSync(niceDate);
		}
		
		internal function updateRatio(rate:String):void
		{
			//Update the ratio display
			parent_ref.setRatio(rate);
		}
		
		/*
		 * Parser
		 */ 
		
		internal function parse(x_xml:XML):void 
		{
			var cmd:String = x_xml.name().localName as String;
			var value:String = x_xml.children()[0].text();
			parent_ref.output("Parsing: cmd = " + cmd + "\nvalue = " + value);
			
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
				updateRatio(value);
			}
			else {
				parent_ref.output("comm_lib parser: unknown xml");
			}
		}
		
	}

}