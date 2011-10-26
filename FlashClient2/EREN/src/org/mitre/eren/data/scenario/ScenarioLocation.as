package org.mitre.eren.data.scenario 
{
	import com.google.maps.LatLng;
	
	/**
	 * Scenario location data structure
	 * !!
	 * @author Amanda Anganes
	 */
	public class ScenarioLocation
	{
		
		public var name:String;
		public var state:String;
		public var loc:LatLng;
		public var population:Number;
		
		public function ScenarioLocation(n:String, s:String, lat:Number, lng:Number, pop:Number) 
		{
			name = n;
			state = s;
			loc = new LatLng(lng, lat);
			population = pop;
		}
		
	}

}