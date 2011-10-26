package org.mitre.eren.data.scenario 
{
	import com.google.maps.LatLng;
	import flash.utils.Dictionary;
	
	/**
	 * Hospital data structure
	 * 
	 * @author Amanda Anganes
	 */
	public class Hospital extends facility
	{
		
		public var staff:Vector.<Staff>;
		public var Manager:Person;
		public var capacity:int;
		public var filled:int;
		
		public function Hospital(id:String, n:String, stat:String, 
			lat:Number, lng:Number, s:Vector.<Staff>, mngr:Person, cap:int, f:int, sti:Dictionary) 
		{	
			super(id, n, stat, new LatLng(lng, lat), sti);
			this.staff = s;
			this.Manager = mngr;
			this.capacity = cap;
			this.filled = f;
		}
		
		
	}

}