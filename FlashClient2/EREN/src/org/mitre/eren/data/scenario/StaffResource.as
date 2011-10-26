package org.mitre.eren.data.scenario 
{
	/**
	 * ...
	 * @author Amanda Anganes
	 */
	public class StaffResource
	{
		
		public var staffFunction:String;
		public var quantity:int;
		public var status:String;
		public var displayName:String;
		public var available:int = 0;
		
		public function StaffResource(status:String, quantity:int, func:String, dispName:String) 
		{
			this.status = status;
			this.quantity = quantity;
			this.staffFunction = func;
			this.displayName = dispName;
		}
	}
}