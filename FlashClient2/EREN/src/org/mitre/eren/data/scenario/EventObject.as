package org.mitre.eren.data.scenario
{
	/**
	 * Event data structure
	 * !!
	 * @author Amanda Anganes
	 **/ 
	

	public class EventObject
	{
		public var ID:String;
		public var type:String;
		public var location;
		
		public function EventObject(i:String, t:String) {
			
			ID = i;
			type = t;
			
		}
	}
}