package org.mitre.eren.display.dialog.notifications 
{
	import org.mitre.eren.data.dialog.UserMessage;
	import fl.text.TLFTextField;	

	/**
	 * Extension of the Notification class, tied to the "warning" priority
	 * icon movie clip in EREN.fla.
	 * 
	 * @author Amanda Anganes
	 */
	public class WarningNotify extends Notification
	{
		
		public function WarningNotify(s:String, m:UserMessage) 
		{
			var t:TLFTextField = this.getChildByName("query_txt") as TLFTextField;
			super(s, m, t);
		}
		
	}

}