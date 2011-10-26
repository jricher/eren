package org.mitre.eren.display.dialog.notifications 
{
	import org.mitre.eren.data.dialog.UserMessage;
	import fl.text.TLFTextField;
	
	/**
	 * Extension of the Notification class, tied to the "normal" priority
	 * icon movie clip in EREN.fla.
	 * 
	 * @author Amanda Anganes
	 */
	public class NormalNotify extends Notification
	{
		
		public function NormalNotify(s:String, m:UserMessage) 
		{
			
			var t:TLFTextField = this.getChildByName("query_txt") as TLFTextField;
			
			super(s, m, t);
		}
		
	}

}