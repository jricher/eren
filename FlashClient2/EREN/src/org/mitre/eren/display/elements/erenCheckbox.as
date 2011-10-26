package org.mitre.eren.display.elements 
{
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import fl.text.TLFTextField;
	import flash.text.TextFieldAutoSize;
	import flashx.textLayout.elements.TextFlow;
	import flashx.textLayout.formats.TextLayoutFormat;
	import org.mitre.eren.display.fonts.regular;
	import org.mitre.eren.events.ErenEvent;
	
	/**
	 * ...
	 * @author Amanda Anganes
	 */
	public class erenCheckbox extends MovieClip 
	{
		//State names <=> frame labels mappings
		private static const DEFAULT:String = "default";
		private static const OVER:String = "over";
		private static const DOWN:String = "down";
		private static const CHECKED:String = "checked";
		private static const OVER_CHECKED:String = "overChecked";
		private static const DOWN_CHECKED:String = "downChecked";
		
		//Current state
		public var isSelected:Boolean = false;
		
		//Label for this checkbox; optional
		public var label:String;
		
		//Label components
		private var field:TLFTextField;
		private var format:TextLayoutFormat;
		
		//Pixel buffer between text field and checkbox
		private var buffer:int = 14;
		
		public function erenCheckbox(s:String = "") 
		{
			this.label = s;
			this.gotoAndStop("default");
			this.addEventListener(MouseEvent.MOUSE_OVER, onOver);
			this.addEventListener(MouseEvent.MOUSE_DOWN, onDown);
			this.addEventListener(MouseEvent.MOUSE_UP, onUp);
			this.addEventListener(MouseEvent.MOUSE_OUT, onOut);
			
			//Create and position label
			field = new TLFTextField();
			field.text = s;
			field.embedFonts = true;
			field.autoSize = TextFieldAutoSize.LEFT;
			field.selectable = false;
			
			addChild(field);
			field.x = buffer;
			field.y = 0;
			
			var myFont:regular = new regular();			
			format = new TextLayoutFormat();
			format.color = 0xFFFFFF;
			format.fontFamily = myFont.fontName;
			format.fontSize = 14;
			format.fontLookup = "embeddedCFF";
			
			var myTextFlow:TextFlow = field.textFlow;
			myTextFlow.hostFormat = format;
			myTextFlow.flowComposer.updateAllControllers();
		}
		
		/**
		 * Set this checkbox to be selected/checked
		 */
		public function setChecked():void
		{
			isSelected = true;
			this.gotoAndStop(CHECKED);
		}
		
		private function onOver(e:MouseEvent):void 
		{
			if (isSelected) 
			{
				this.gotoAndStop(OVER_CHECKED);
			} 
			else 
			{
				this.gotoAndStop(OVER);
			}
		}
		
		private function onOut(e:MouseEvent):void 
		{
			if (isSelected)
			{
				this.gotoAndStop(CHECKED);
			}
			else
			{
				this.gotoAndStop(DEFAULT);
			}
		}
		
		private function onDown(e:MouseEvent):void 
		{
			if (isSelected) 
			{
				this.gotoAndStop(DOWN_CHECKED);
			} 
			else 
			{
				this.gotoAndStop(DOWN);
			}
		}
		
		private function onUp(e:MouseEvent):void 
		{
			if (isSelected) 
			{
				isSelected = false;
				this.gotoAndStop(DEFAULT);
			} 
			else 
			{
				isSelected = true;
				this.gotoAndStop(CHECKED);
			}
			
			var event:ErenEvent = new ErenEvent(ErenEvent.CHECK_SELECTION_EVENT);
			event._checkboxName = label;
			event._isSelected = isSelected;
			trace("Checkbox " + label + ", selected = " + isSelected);
			dispatchEvent(event);
		}
		
	}

}