package org.mitre.eren.display.elements 
{
	import flash.display.MovieClip;
	import flash.events.MouseEvent;
	import flash.ui.Mouse;
	import fl.text.TLFTextField;
	
	/**
	 * Copy of EREN_Button.as, since you can't attach the same class to more than one kind of movie clip.
	 * EREN_Button2 is used to display blue-on-white buttons, as opposed to white-on-blue ones.
	 * 
	 * @author Amanda Anganes
	 */
	public class EREN_Button2 extends MovieClip {
		
		public var isEnabled:Boolean = false;
		
			/**
		 * Constructor
		 */
		public function EREN_Button2() 
		{
			gotoAndStop("disabled");
		}
		
		/**
		 * Set this button to be enabled
		 */
		public function setEnabled():void 
		{
			this.gotoAndStop(1);
			addListeners();
			isEnabled = true;
		}
		
		/**
		 * Set this button to be disabled
		 */
		public function setDisabled():void 
		{
			this.gotoAndStop("disabled");
			if (isEnabled) 
			{
				removeListeners();
			}
			isEnabled = false;
		}
		
		/**
		 * Set the content (label) for this button
		 * 
		 * @param	s a string to display on the face of the button
		 */
		public function setContent(s:String):void 
		{
			content_txt.text = s;
		}
		
		/**
		 * Add the necessary mouse listeners to the button
		 */
		private function addListeners():void 
		{
			this.addEventListener(MouseEvent.MOUSE_OVER, mouseover, false, 0, true);
			this.addEventListener(MouseEvent.MOUSE_UP, mouseclick, false, 0, true);
			this.addEventListener(MouseEvent.MOUSE_DOWN, mousedown, false, 0, true);
			this.addEventListener(MouseEvent.MOUSE_OUT, mouseout, false, 0, true);
		}
		
		/**
		 * Remove all moust listeners from this button
		 */
		public function removeListeners():void 
		{
			this.removeEventListener(MouseEvent.MOUSE_OVER, mouseover);
			this.removeEventListener(MouseEvent.MOUSE_UP, mouseclick);
			this.removeEventListener(MouseEvent.MOUSE_DOWN, mousedown);
			this.removeEventListener(MouseEvent.MOUSE_OUT, mouseout);
		}
		
		private function mouseover(e:MouseEvent):void 
		{
			this.gotoAndStop("highlight");
		}
		
		private function mouseout(e:MouseEvent):void 
		{
			this.gotoAndStop(1);
		}
		
		private function mouseclick(e:MouseEvent):void 
		{
			this.gotoAndStop(1);
		}
		
		private function mousedown(e:MouseEvent):void 
		{
			this.gotoAndStop("depressed");
		}
		
	}

}