package org.mitre.eren.display.dialog.notifications 
{
	import fl.text.TLFTextField;
	import fl.transitions.easing.*;
	import fl.transitions.Tween;
	import flash.display.MovieClip;
	import flash.text.TextFormat;
	import org.mitre.eren.data.dialog.UserMessage;
	

	/**
	 * Notification icon base class
	 * 
	 * @author Amanda Anganes, Justin Richer
	 */
	public class Notification extends MovieClip 
	{
		
		/** Members **/
		private var fade_tween:Tween;
		private var content:String;
		public var message:UserMessage
		private var italic:TextFormat;
		private var reg:TextFormat;		
		private var query_txt:TLFTextField;
		public var isSelected:Boolean = false;
		
		/** Constructor **/
		public function Notification(s:String, m:UserMessage, t:*) 
		{
			content = s;
			message = m;
			query_txt = t as TLFTextField;
			
			italic = new TextFormat();
			reg = new TextFormat();
			reg.italic = false;
			italic.italic = true;
			
			//Start out invisible
			this.alpha = 0;
			
			appear();		
		}
		
		/**
		 * Highlight this notification (tying it to the message center window)
		 */
		public function highlight():void 
		{
			this.gotoAndStop(2);
			this.isSelected = true;
			updateContent();
		}
		
		/**
		 * Revert this notification message to be de-selected
		 */
		public function revert():void 
		{
			this.gotoAndStop(1);
			this.isSelected = false;
			updateContent();
		}
		
		/**
		 * Update the content of the icon
		 */
		//TODO: change content to property setter which forces update of content
		public function updateContent():void 
		{
			var c:TLFTextField = this.getChildByName("query_txt") as TLFTextField;
			c.text = content;
		}
		
		/**
		 * Set the content of the icon to a new String
		 * 
		 * @param	s the new content string
		 */
		public function setContent(s:String):void 
		{
			content = s;
			updateContent();
		}
		
		/**
		 * Fade in
		 */
		public function appear():void 
		{
			fade_tween = new Tween(this, "alpha", Strong.easeOut, 0, 1, .5 , true);
		}
	}
}