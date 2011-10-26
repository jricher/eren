package org.mitre.eren.display.map 
{
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Matrix;
	import flash.text.TextFormat;
	import flash.text.TextFieldAutoSize;
	import fl.text.TLFTextField;
	import flash.display.GradientType;
	import flashx.textLayout.elements.TextFlow;
	import flashx.textLayout.formats.TextLayoutFormat;
	import org.mitre.eren.display.fonts.regular;
	
	/**
	 * Custom Google Maps for Flash InfoWindow movie clip. Displays a string
	 * in a nicely filled and formatted box. Takes an ID string in its constructor
	 * corresponding to the id of the facility the RIW is attached to.
	 * 
	 * @author Amanda Anganes
	 */
	public class resourceInfoWindow extends MovieClip
	{
		private var field:TLFTextField;
		private var content:String;
		private var format:TextLayoutFormat;
		private var ID:String;
		
		/**
		 * Constructor.
		 * 
		 * @param	id ID of the facility this RIW is attached to
		 * @param	s  String containing the content to display
		 */
		public function resourceInfoWindow(id:String, s:String) 
		{
			this.ID = id;
			this.content = s;
			
			this.field = new TLFTextField();
			field.x = 0;
			field.y = 0;
			field.embedFonts = true;
			field.multiline = true;
			field.wordWrap = false;
			field.autoSize = TextFieldAutoSize.LEFT;
			field.selectable = false;
			field.text = content;
			
			this.addChild(field);
			
			var myFont:regular = new regular();			
			this.format = new TextLayoutFormat();
			format.color = 0x000000;
			format.fontFamily = myFont.fontName;
			format.fontSize = 16;
			format.fontLookup = "embeddedCFF";
			
			var myTextFlow:TextFlow = field.textFlow;
			myTextFlow.hostFormat = format;
			myTextFlow.flowComposer.updateAllControllers();
			
			addEventListener(Event.ADDED_TO_STAGE, onAdded);
		}
		
		private function onAdded(e:Event):void 
		{
			redraw();
		}
		
		/**
		 * Update the content displayed in the RIW.
		 * 
		 * @param	c new content String
		 */
		public function updateContent(c:String):void 
		{
			var oldNumLines:Number = field.numLines;
			this.content = c;
			field.text = content;
			if (oldNumLines != field.numLines) 
			{
				redraw();
			}
		}
		
		private function redraw():void 
		{
			this.graphics.clear();
			var ratioArray:Array = new Array(0, 255);
			var matrix:Matrix = new Matrix();
			matrix.createGradientBox(this.width, this.height, (Math.PI / 2), 0, 0);
			this.graphics.beginGradientFill(GradientType.LINEAR, new Array(0xFFFFFF, 0xE5E5E5), 
					new Array(.9, .9), new Array(0, 255), matrix);
			this.graphics.drawRoundRect(0, 0, this.width, this.height, 5, 5);
			this.graphics.endFill();
		}
		
		/**
		 * Get the ID associated with this RIW
		 * 
		 * @return the id
		 */
		public function getID():String 
		{
			return this.ID;
		}
		
	}

}