package org.mitre.eren.display.elements 
{
	import fl.text.TLFTextField;
	import fl.transitions.easing.Regular;
	import fl.transitions.Tween;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.text.TextFieldAutoSize;
	import flashx.textLayout.elements.TextFlow;
	import flashx.textLayout.formats.TextLayoutFormat;
	import org.mitre.eren.display.dialog.expander.ScrollBar;
	import org.mitre.eren.display.dialog.expander.ScrollBarEvent;
	import org.mitre.eren.display.fonts.regular;
	
	/**
	 * TLF text field scrollbox class. Wraps a TLF text field in a movie clip,
	 * which adds a scrollbar and masks if the text field is taller than the
	 * bounding box specified.
	 * 
	 * @author Amanda Anganes
	 */
	public class TLFtextfieldScrollbox extends MovieClip 
	{
		
		private var w:Number;
		private var h:Number;
		private var scrollbar:ScrollBar;
		private var field:TLFTextField;
		private var format:TextLayoutFormat;
		private var scrollbarwidth:Number = 10;
		private var _mask:MovieClip;
		
		/**
		 * Constructor.
		 * 
		 * @param	width  the desired width for this box
		 * @param	height the desired height for this box
		 */
		public function TLFtextfieldScrollbox(width:Number, height:Number) 
		{
			
			this.w = width;
			this.h = height;
			
			_mask = new MovieClip();
			_mask.graphics.beginFill(0x000000, 1);
			_mask.graphics.drawRect(0, 0, w, h);
			_mask.graphics.endFill();
			this.addChild(_mask);
			_mask.x = 0;
			_mask.y = 0;
			this.mask = _mask;
			
			this.field = new TLFTextField();
			field.x = 0;
			field.y = 0;
			field.width = w - scrollbarwidth;
			field.embedFonts = true;
			field.multiline = true;
			field.wordWrap = true;
			field.autoSize = TextFieldAutoSize.CENTER;
			field.selectable = false;
			
			field.tlfMarkup = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\"><span></span></TextFlow>";
			
			this.addChild(field);
			
			var myFont:regular = new regular();			
			format = new TextLayoutFormat();
			format.color = 0xFFFFFF;
			format.fontFamily = myFont.fontName;
			format.fontSize = 18;
			format.fontLookup = "embeddedCFF";
			
			var myTextFlow:TextFlow = field.textFlow;
			myTextFlow.hostFormat = format;
			myTextFlow.flowComposer.updateAllControllers();
			
			this.addEventListener(Event.ADDED_TO_STAGE, onadded);
		}
		
		/**
		 * Listener for the added_to_stage event.
		 * 
		 * @param	e
		 */
		private function onadded(e:Event):void
		{
			scrollbar = new ScrollBar(h, scrollbarwidth, 1, this.stage);
			scrollbar.x = this.w - this.scrollbarwidth;
			scrollbar.y = 0;
			this.addChild(scrollbar);
			scrollbar.visible = false;
			scrollbar.addEventListener(ScrollBarEvent.VALUE_CHANGED, doscroll);
			
		}
		
		/**
		 * Set or update the content of the scrollbox.
		 * 
		 * @param	s a String containing the text to place in the scrollbox
		 */
		public function setContent(s:String):void
		{
			field.tlfMarkup = s;
			trace("Setting content of the tlftextfieldscrollbox to be" + s);
			var myTextFlow:TextFlow = field.textFlow;
			myTextFlow.hostFormat = format;
			myTextFlow.flowComposer.updateAllControllers();
			
			//calculate height
			var textHeight:Number = field.height;
			//determine if scrollbar needs to be visible or not
			if (textHeight > this.h) {
				scrollbar.resizeTrack(textHeight, this.h);
				scrollbar.visible = true;
			} else {
				scrollbar.visible = false;
			}
			
		}
		
		/**
		 * Change the height of the scrollbox.
		 * 
		 * @param	newHeight the new height
		 */
		public function changeHeight(newHeight:Number):void
		{
			this.h = newHeight;
			_mask.graphics.clear();
			_mask.graphics.beginFill(0x000000, 1);
			_mask.graphics.drawRect(0, 0, w, h);
			_mask.graphics.endFill();
			this.mask = _mask;
			
			//determine if scrollbar needs to be visible or not 
			if (field.height > this.h) {
				//change scrollbar height
				removeChild(scrollbar);
				var percent:Number = field.height / this.h;
				scrollbar = new ScrollBar(h, scrollbarwidth, percent, this.stage);
				scrollbar.visible = true;
				addChild(scrollbar);
				scrollbar.x = this.w - this.scrollbarwidth;
				scrollbar.y = 0;
			} else {
				scrollbar.visible = false;
			}
			
		}
		
		/**
		 * Listener for scroll bar events
		 * 
		 * @param	e
		 */
		private function doscroll(e:ScrollBarEvent):void
		{
			if (scrollbar.visible) {
				//only perform scrolling if the component is visible
				var to:Number = -(field.height - this.h) * e.scrollPercent;
				if (to <= 0) {
					var t:Tween = new Tween(field, "y", Regular.easeIn, field.y, to, 3);
				}
			}
		}
		
	}

}