package org.mitre.eren.data.scenario 
{
	import flash.utils.Dictionary;
	import org.mitre.eren.data.scenario.facility;
	import com.google.maps.LatLng;
	
	/**
	 * ...
	 * @author Amanda Anganes
	 */
	public class RSS extends facility 
	{
		public var staff:Vector.<Staff>;
		
		public function RSS(id:String, n:String, stat:String, lat:Number, lng:Number, s:Vector.<Staff>, sti:Dictionary) 
		{
			super(id, n, stat, new LatLng(lng, lat), sti);
			
		}
		
	}

}