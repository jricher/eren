package org.mitre.eren.data.scenario 
{
	/**
	 * ...
	 * @author a_anganes
	 */
	public class Staff
	{
		
		public var staffFunction:String;
		public var min:Number;
		public var target:Number;
		public var current:Number;
		
		public function Staff(f:String, m:Number, t:Number, c:Number) {
			
			staffFunction = f;
			min = m;
			target = t;
			current = c;
			
		}
		
	}

}