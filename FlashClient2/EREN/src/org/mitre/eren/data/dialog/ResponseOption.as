package org.mitre.eren.data.dialog 
{
	/**
	 * Response option data structure. Each response option has an ID number, a short 
	 * response, and an optional long response.  It is up to the caller to check whether 
	 * the long response is empty before attempting to display it.
	 * 
	 * @author a_anganes
	 */
	public class ResponseOption
	{
		
		private var ID:String;
		private var shortResponse:String;
		private var longResponse:String;
		private var followup:Boolean;
		
		/**
		 * Constructor 
		 * 
		 * @param	i ID of the response option
		 * @param	sr short response
		 * @param	lr long response
		 * @param	f followup? Boolean
		 */
		public function ResponseOption(i:String, sr:String, lr:String, f:Boolean) 
		{
			ID = i;
			shortResponse = sr;
			longResponse = lr;
			followup = f;
		}
		
		//*************************************
		// Getter methods for private members
		//*************************************
		
		public function getID():String {
			return ID;
		}
		
		public function getShortResponse():String {
			return shortResponse;
		}
		
		public function getLongResponse():String {
			return longResponse;
		}
		
		public function hasFollowup():Boolean {
			return followup;
		}
	}
}