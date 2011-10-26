package org.mitre.eren.display.resources 
{
	import fl.text.TLFTextField;
	import flash.display.Sprite;
	import flash.filters.DropShadowFilter;
	import flash.text.TextFormat;
	import flash.text.TextFormatAlign;
	import org.mitre.eren.Constants;
	import org.mitre.eren.display.fonts.bold;
	
	/**
	 * Code to draw the balloon objects used by ResourceBar to display
	 * allocated and free resource amounts. The content within the balloon
	 * may be updated at any time, and the balloon will redraw itself to
	 * fit the new content.
	 * 
	 * @author Amanda Anganes
	 */
	public class Balloon extends Sprite
	{
		
		private var content:String = "";
		private var balloon_txt:TLFTextField;
		private var padding:int = 5;
		private var textColor:uint = 0x193D62;
		
		/**
		 * Constructor. Initializes the balloon's visuals.
		 * 
		 * @param	content the content to display within this bubble
		 */
		public function Balloon(content:String) 
		{	
			this.content = content;
			
			var dropShadow:DropShadowFilter = new DropShadowFilter();
			dropShadow.strength = .25;
			dropShadow.blurX = 3;
			dropShadow.blurY = 3;
			dropShadow.distance = 3;
			
			this.filters = new Array(dropShadow);
			
			var balloonFormat:TextFormat = new TextFormat();
			balloonFormat.color = textColor;
			balloonFormat.font = (new bold()).fontName;;
			balloonFormat.bold = true;
			balloonFormat.size = 9;
			balloonFormat.align = TextFormatAlign.CENTER;
			
			var contentTxtWidth:Number = content.length * Constants.SIZE9_NUMBER_WIDTH;
			
			balloon_txt = new TLFTextField();
			balloon_txt.width = contentTxtWidth;
			balloon_txt.height = 13;
			balloon_txt.selectable = false;
			balloon_txt.defaultTextFormat = balloonFormat;
			balloon_txt.x = padding;
			balloon_txt.y = padding;
			balloon_txt.text = content;
			
			this.addChild(balloon_txt);
			draw();
		}
		
		/**
		 * Update the content displayed in this bubble
		 * 
		 * @param	newContent the new content
		 */
		public function update(newContent:String):void
		{
			this.content = newContent;
			balloon_txt.text = newContent;
			draw();
		}
		
		/**
		 * Draw the visuals
		 */
		private function draw():void 
		{
			var contentTxtWidth:Number = content.length * Constants.SIZE9_NUMBER_WIDTH;
			var balloonHeight:int = 8 + 2 * padding;
			var balloonWidth:int = contentTxtWidth + 2 * padding;
			
			this.graphics.clear();
			
			balloon_txt.width = contentTxtWidth;
			this.graphics.beginFill(0xffffff, .9);
			this.graphics.lineStyle(2, 0x89939f);
			this.graphics.drawRoundRect(0, 0, balloonWidth, balloonHeight, 15, 15);
			this.graphics.moveTo((balloonWidth / 2) - 3, balloonHeight);
			this.graphics.lineStyle(4, 0x89939f);
			this.graphics.lineTo((balloonWidth / 2), balloonHeight + 5);
			this.graphics.lineTo((balloonWidth / 2) + 3, balloonHeight);
		}
		
	}
	
}