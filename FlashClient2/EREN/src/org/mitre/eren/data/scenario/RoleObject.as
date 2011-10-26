package org.mitre.eren.data.scenario
{
	/**
	 * Role Object data structure
	 * 
	 * !!
	 * @author Amanda Anganes
	 */ 
	
	public class RoleObject
	{
		public var min:Number;
		public var max:Number;
		public var title:String;
		public var description:String;
		public var briefing:String;
		public var ID:String;
		public var picURL:String;
		
		public function RoleObject(mn:Number, mx:Number, i:String, t:String, d:String, b:String, purl:String) 
		{
			min = mn;
			max = mx;
			ID = i;
			title = t;
			description = d;
			briefing = b;
			picURL = purl;
		}
		
	}
}