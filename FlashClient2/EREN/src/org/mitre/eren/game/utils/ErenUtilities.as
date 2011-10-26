package org.mitre.eren.game.utils 
{
	import flash.utils.Dictionary;
	
	/**
	 * This class contains some common utilities that didn't fit anywhere else.
	 * 
	 * @author Amanda Anganes
	 */
	public class ErenUtilities 
	{
		
		/**
		 * Constructor
		 */
		public function ErenUtilities() 
		{
			
		}
		
		/**
		 * Get the length of a dictionary.
		 * 
		 * @param	d the dictionary to find the length of
		 */
		public static function getLength(d:Dictionary) {
			var i:int = 0;
			for each (var n:* in d) {
				i++;
			}
			return i;
		}
		
		/**
		 * Format a time given in seconds to days, hours, minutes, seconds
		 * 
		 * @param	time seconds
		 * @return formatted string representation
		 */
		public static function formatTime ( time:Number ):String
		{
			var remainder:Number;
			
			var days:Number = time / (60 * 60 * 24);
			
			remainder = days - (Math.floor ( days ) );
			
			days = Math.floor (days);
			
			var hours:Number = remainder * 24;
			
			remainder = hours - (Math.floor ( hours ));
			
			hours = Math.floor ( hours );
			
			var minutes = remainder * 60;
			
			remainder = minutes - (Math.floor ( minutes ));
			
			minutes = Math.floor ( minutes );
			
			var seconds = remainder * 60;
			
			remainder = seconds - (Math.floor ( seconds ));
			
			seconds = Math.floor ( seconds );
			
			var timeString:String = "";
			
			if (days > 0) {
				if (days == 1) {
					timeString += days + " day";
				} else {
					timeString += days + " days";
				}
			}
			if (hours > 0) {
				if (days == 0) {
					timeString += hours + " hrs";
				} else {
					timeString += ", " + hours + " hrs";
				}
			}
			if (minutes > 0) {
				if (hours == 0 && days == 0) {
					timeString += minutes + " min";
				} else {
					timeString += ", " + minutes + " min";
				}
			}
			if (seconds > 0) {
				timeString += ", " + seconds + " sec";
			}
			
			return timeString;
		}
	}
}