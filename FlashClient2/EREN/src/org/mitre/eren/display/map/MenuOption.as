package org.mitre.eren.display.map 
{
	import fl.text.TLFTextField;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import org.mitre.eren.data.actions.Action;
	import org.mitre.eren.display.fonts.regular;
	import org.mitre.eren.events.ErenEvent;
	
	/**
	 * A MenuOption represents an action that a player may take on a particular facility.
	 * MenuOptions are contained inside an ActionsMenu, and each MenuOption contains a reference
	 * to the Action object it represents. When a MenuOption is enabled, it is displayed in blue
	 * with white text. If the option is disabled, it is displayed greyed out and is not clickable.
	 * 
	 * @author Amanda Anganes
	 */
	public class MenuOption extends MovieClip 
	{
		////// Data \\\\\\
		public  var _enabled:Boolean = false;
		public  var action:Action;
		public  var facilityId:String;
		public  var index:int = 0;
		
		////// Visuals \\\\\\
		private var enabledFormat:TextFormat;
		private var disabledFormat:TextFormat;
		private var textField:TLFTextField;
		private var font:regular;
		private var disabledColor:uint = 0xA5B3C2;
		private var enabledColor:uint = 0x033970;
		private var hoverColor:uint = 0x1f67b1;
		private var pressedColor:uint = 0x125ca9;
		private var fontSize:int = 14;
		private var customWidth:Number = 0;
		
		/**
		 * Constructor. 
		 * 
		 * @param	action     the Action associated with this menu option
		 * @param	enabled    true = enabled, false = disabled.
		 * @param	facilityId the id of the facility this action is tied to
		 * @param	i          index of this menu item (used by the ActionsMenu for placement)
		 */
		public function MenuOption(action:Action, enabled:Boolean, facilityId:String, i:int) 
		{
			//Set data
			this.action = action;
			this._enabled = enabled;
			this.facilityId = facilityId;
			this.index = i;
			
			//Set up visuals
			textField = new TLFTextField();
			textField.text = action.displayName;
			textField.autoSize = TextFieldAutoSize.LEFT;
			textField.embedFonts = true;
			textField.selectable = false;
			textField.x = 0;
			textField.y = 0;
			addChild(textField);
			
			font = new regular();
			enabledFormat = new TextFormat();
			enabledFormat.font = font.fontName;
			enabledFormat.size = fontSize;
			enabledFormat.color = 0xFFFFFF;
			
			disabledFormat = new TextFormat();
			disabledFormat.font = font.fontName;
			disabledFormat.size = fontSize;
			disabledFormat.color = 0x999999;
			textField.setTextFormat((enabled ? enabledFormat : disabledFormat));
			
			this.addEventListener(Event.ADDED_TO_STAGE, onAdded);
			this.addEventListener(MouseEvent.ROLL_OVER, onOver);
			this.addEventListener(MouseEvent.ROLL_OUT, onOut);
			this.mouseChildren = false;
			this.buttonMode = true;
		}
		
		private function onOver(e:MouseEvent):void
		{
			if (_enabled) 
			{ 
				draw(hoverColor);
			}
		}
		
		private function onOut(e:MouseEvent):void
		{
			draw((_enabled == true) ? enabledColor : disabledColor);
		}
		
		/**
		 * Set the "isEnabled" property ("enabled" is a reserved keyword).
		 * This causes a redraw.
		 */
		public function set isEnabled(val:Boolean):void 
		{
			if (val == true) 
			{
				//Set enabled
				this._enabled = true;
				draw(enabledColor);
			}
			else 
			{
				//Set disabled
				this._enabled = false;
				draw(disabledColor);
			}
		}
		
		public function get isEnabled():Boolean 
		{
			return this._enabled;
		}
		
		/**
		 * Force a redraw of the menu option with a new width. This is used so
		 * that as options are added to the menu, all options can be constrained 
		 * to be as wide as the widest option.
		 * 
		 * @param	newWidth the new width this option should use
		 */
		public function redraw(newWidth:Number):void
		{
			this.customWidth = newWidth;
			draw((_enabled == true) ? enabledColor : disabledColor);
		}
		
		private function draw(color:uint):void
		{
			this.graphics.clear();
			this.graphics.beginFill(color, 1);
			this.graphics.lineStyle(1, 0xFFFFFF);
			this.graphics.drawRect(0, 0, ((customWidth == 0) ? this.width : customWidth), this.height);
			this.graphics.endFill();
			textField.setTextFormat((_enabled ? enabledFormat : disabledFormat));
		}
		
		private function onAdded(e:Event):void
		{
			draw((_enabled == true) ? enabledColor : disabledColor);
			dispatchEvent(new ErenEvent(ErenEvent.READY));
		}
	}
}