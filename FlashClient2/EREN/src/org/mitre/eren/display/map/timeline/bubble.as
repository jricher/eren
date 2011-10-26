package org.mitre.eren.display.map.timeline 
{
	import flash.display.MovieClip;
	import flash.events.Event;
	
	/**
	 * Code-behind for the timeline bubble component. The bubble displays the current time
	 * in a "nice" format, as "Tuesday 2:35 PM".
	 * 
	 * @author Amanda Anganes
	 */
	public class bubble extends MovieClip
	{
		private var days:Array;
		
		public function bubble() 
		{
			days = new Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
			
		}
		
		public function setTime(d:Date):void
		{
			var time:String;			
			var day:String = days[d.getDay()];
			var hours:Number = d.getHours();
			var minutes:String = String(d.getMinutes());			
			var meridian:String;
			
			if (hours > 12) {
				hours -= 12;
				meridian = "PM";
			} 
			else if (hours == 12)
				meridian = "PM";
			else 
				meridian = "AM";
				
			if (hours == 0) hours = 12;
			
			if (d.getMinutes() < 10) {
				//if this is a 1-digit number, add a leading 0 to it
				minutes = "0" + d.getMinutes();
			} else minutes = String(d.getMinutes());
			
			//Create a human-readable 'nice' display of the time	
			time = day + " " + hours + ":" + minutes + " " + meridian;
			
			time_txt.text = time;
		}
		
	}

}