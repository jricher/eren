package org.mitre.eren.display.elements.selector 
{
	import fl.text.TLFTextField;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.text.TextFormat;
	import org.mitre.eren.display.fonts.regular;
	import org.mitre.eren.events.ErenEvent;
	
	/**
	 * Movie clip class to represent a selector bar object, which is 
	 * used for game, role, and scenario selection.
	 * 
	 * @author Amanda Anganes
	 */
	public class SelectorBar extends MovieClip 
	{
		protected var size:Point = new Point(704, 24);
		protected var fields:Vector.<TLFTextField>;
		protected var selectedFormat:TextFormat;
		protected var deselectedFormat:TextFormat;
		
		private var _index:int;
		private var _selected:Boolean = false;
		
		/**
		 * Constructor
		 */
		public function SelectorBar() 
		{
			fields = new Vector.<TLFTextField>();
			var regFont:regular = new regular();
			
			selectedFormat = new TextFormat();
			selectedFormat.font = regFont.fontName;
			selectedFormat.size = 14;
			selectedFormat.color = 0x355D87;
			
			deselectedFormat = new TextFormat();
			deselectedFormat.font = regFont.fontName;
			deselectedFormat.size = 14;
			deselectedFormat.color = 0xffffff;
			
			this.addEventListener(MouseEvent.CLICK, onClick, false, 0, true);
			this.addEventListener(Event.ADDED_TO_STAGE, draw, false, 0, true);
		}
		
		/**
		 * Added_to_stage listener. Trigger a re-draw of this bar by setting this.selected (property setter 
		 * triggers drawing if needed).
		 * 
		 * @param	e
		 */
		private function draw(e:Event):void
		{
			selected = this._selected;
		}
		
		/**
		 * Click listener. Fire an ErenEvent.SELECTION_EVENT.
		 * 
		 * @param	e
		 */
		private function onClick(e:MouseEvent) 
		{
			var ee:ErenEvent = new ErenEvent(ErenEvent.SELECTION_EVENT);
			ee._isSelected = true;
			dispatchEvent(ee);
		}
		
		/**
		 * Draw the bar in white, and the text in blue
		 */
		private function drawSelected():void 
		{
			for each (var f:TLFTextField in fields) 
			{
				f.setTextFormat(selectedFormat);
			}
			this.graphics.clear();
			this.graphics.beginFill(0xffffff);
			this.graphics.drawRoundRect(0, 0, size.x, size.y, 2, 2);
			this.graphics.endFill();
		}
		
		/**
		 * Draw the bar in blue, and the text in white
		 */
		private function drawDefault():void 
		{
			for each (var f2:TLFTextField in fields) 
			{
				f2.setTextFormat(deselectedFormat);
			}
			this.graphics.clear();
			this.graphics.beginFill(0xffffff, 0);
			this.graphics.drawRoundRect(0, 0, size.x, size.y, 2, 2);
			this.graphics.endFill();
		}
		
		/**
		 * Setting the "selected" property triggers a redraw
		 */
		public function set selected(selected:Boolean) 
		{
			if (selected) 
			{
				drawSelected();
				_selected = true;
			}
			else 
			{
				drawDefault();
				_selected = false;
			}
		}
		
		public function get selected():Boolean
		{
			return _selected;
		}
		
		public function get index():int
		{
			return this._index;
		}
		
		public function set index(i:int):void 
		{
			this._index = i;
		}
		
		public override function get height():Number
		{
			return this.size.y;
		}
	}

}