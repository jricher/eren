package org.mitre.eren.data.scenario 
{
	/**
	 * Resource Deployment Status
	 * @author Amanda Anganes
	 */
	public class RDStatus 
	{
		
		public var facilityID:String;
		public var status:String;
		
		//Eventually we may want to capture info about who is sending this message, and possibly 
		// display to the user as a UserMessage
		
		public function RDStatus(fid:String, stat:String) 
		{
			this.facilityID = fid;
			this.status = stat;
		}
		
	}

}