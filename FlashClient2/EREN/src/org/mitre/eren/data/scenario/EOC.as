package org.mitre.eren.data.scenario
{
	import com.google.maps.LatLng;
	import flash.utils.Dictionary;
	
	/**
	 * EOC data structure
	 * 
	 * @author Amanda Anganes
	 **/ 
	
	public class EOC extends facility
	{
		
		public function EOC(id:String, n:String, stat:String, lat:Number, lng:Number, sti:Dictionary) 
		{
			
			super(id, n, stat, new LatLng(lng, lat), sti);
			
		}
		
	}
}