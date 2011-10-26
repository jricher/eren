package org.mitre.eren.data.scenario 
{
	/**
	 * ...
	 * @author ...
	 */
	public class PODupdate 
	{
		public var id:String;
		public var medStaff:int;
		public var supStaff:int;
		public var secStaff:int;
		public var qSize:int;
		public var tp:Number;
		public var hasMds:Boolean;
		public var hasEq:Boolean;
		public var soc:int;
		
		public function PODupdate(idee:String, medical:int, support:int, security:int, q:int, 
									through:int, meds:Boolean, equip:Boolean, standard:int) 
		{
			this.id = idee;
			this.medStaff = medical;
			this.supStaff = support;
			this.secStaff = security;
			this.qSize = q;
			this.tp = through;
			this.hasMds = meds;
			this.hasEq = equip;
			this.soc = standard;
		}
		
	}

}