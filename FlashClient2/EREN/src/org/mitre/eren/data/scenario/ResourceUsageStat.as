package org.mitre.eren.data.scenario 
{
	/**
	 * ...
	 * @author Amanda Anganes
	 */
	public class ResourceUsageStat 
	{
		public var type:String;
		public var available:int;
		public var total:int;
		
		public function ResourceUsageStat(type:String, available:int, total:int) 
		{
			this.type = type;
			this.available = available;
			this.total = total;
		}
		
	}

}