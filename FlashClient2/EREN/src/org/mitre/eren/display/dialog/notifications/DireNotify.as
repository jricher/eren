package org.mitre.eren.display.dialog.notifications 
{
	import org.mitre.eren.data.dialog.UserMessage;
	import fl.text.TLFTextField;	
	
	/**
	 * Extension of the Notification class, tied to the "dire" priority
	 * icon movie clip in EREN.fla.
	 * 
	 * @author Amanda Anganes
	 */
	public class DireNotify extends Notification
	{
		
		public function DireNotify(s:String, m:UserMessage) 
		{
			var t:TLFTextField = this.getChildByName("query_txt") as TLFTextField;
			super(s, m, t);
		}
		
	}

}