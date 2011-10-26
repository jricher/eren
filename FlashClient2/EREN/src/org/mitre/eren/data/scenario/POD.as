package org.mitre.eren.data.scenario 
{
	import com.google.maps.LatLng;
	import flash.utils.Dictionary;
	
	/**
	 * POD data structure
	 * 
	 * @author Amanda Anganes
	 */
	public class POD extends facility
	{
		public var staff:Vector.<Staff>;
		public var queueSize:int;
		public var throughput:int;
		public var hasMeds:Boolean;
		public var hasEquipment:Boolean;
		public var standardOfCare:int;
		
		public function POD(id:String, n:String, stat:String, lat:Number, lng:Number, s:Vector.<Staff>, sti:Dictionary) 
		{
			super(id, n, stat, new LatLng(lng, lat), sti);
			staff = s;
			queueSize = 0;
			throughput = 0;
			hasEquipment = false;
			hasMeds = false;
			//Standard of care is an integer from 1 - 6
			standardOfCare = 1;
		}
		
	}

}