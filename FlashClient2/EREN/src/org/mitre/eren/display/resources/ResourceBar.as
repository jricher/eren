package org.mitre.eren.display.resources 
{
	import fl.text.TLFTextField;
	import flash.display.GradientType;
	import flash.display.InterpolationMethod;
	import flash.display.MovieClip;
	import flash.display.Shape;
	import flash.display.SpreadMethod;
	import flash.display.Sprite;
	import flash.events.MouseEvent;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import org.mitre.eren.Constants;
	import org.mitre.eren.display.fonts.bold;
	
	/**
	 * A ResourceBar is a visualization for a limited resource, which may be partially allocated
	 * in the game. The bar fills from left to right and displays a Balloon object above the center
	 * points of the allocated and free amounds.
	 * 
	 * @author Amanda Anganes
	 */
	public class ResourceBar extends MovieClip 
	{
		public static const EREN_BAR_SIZE:Point = new Point(280, 10);
		public static const EREN_BAR_CORNER_RADIUS:int = 3;
		public static const EREN_BAR_POS:Point = new Point(2.5, 17);
		public static const MIN_TXT_POS:Point = new Point(0, 30);
		
		public static const RESOURCE_TYPE:String = "Resource Type";
		public static const SLIDER_TYPE:String = "Slider Type";
		
		private var hasThumb:Boolean = false;
		private var min:int = 0;
		private var max:int = 100;
		private var label:String = "";
		private var barType:String;
		private var _value:int = 50;
		private var _type:String;
		
		private var barBackgroundColor1:uint = 0xA3BDD7;
		private var barBackgroundColor2:uint = 0xD6E4F3;
		private var barBackgroundColors:Array = [barBackgroundColor1, barBackgroundColor2];
		private var barBackgroundAlphas:Array = [100, 100];
		private var barBackgroundMatrix:Matrix = new Matrix();
		private var barBackground:Shape;
		
		private var fillBarColor1:uint = 0x43484D;
		private var fillBarColor2:uint = 0xAFBDCC;
		private var fillBarAlpha:int = 70;
		private var fillBarColors:Array = [fillBarColor2, fillBarColor1];
		private var fillBarAlphas:Array = [fillBarAlpha, fillBarAlpha];
		private var fillBarMatrix:Matrix = new Matrix();
		private var fillBar:Sprite;
		
		private var gradientType:String = GradientType.LINEAR;
		private var ratios:Array = [0, 255];
		private var spreadMethod:String = SpreadMethod.PAD; 
		private var interp:String = InterpolationMethod.LINEAR_RGB; 
		private var focalPtRatio:Number = 0; 
		
		private var textColor:uint = 0x193D62;
		private var labelFormat:TextFormat;
		private var minmaxFormat:TextFormat;
		private var minmaxoffset:int = 30;
		
		private var label_txt:TLFTextField;
		private var min_txt:TLFTextField;
		private var max_txt:TLFTextField;
		
		private var allocdBalloon:Balloon;
		private var freeBalloon:Balloon;
		
		/**
		 * Constructor.
		 * 
		 * @param	min           the minimum of the resource, usually 0
		 * @param	max           the total amount of the resource
		 * @param	label         the resource's name
		 * @param	resourceType  one of "CLINICAL_STAFF", "OPS_STAFF", etc; refers to the EREN_Messages resourceType enum
		 * @param	current       the current amount of the resource that is allocated
		 * @param	barType       ResourceBar.RESOURCE_TYPE or other types for extensions to the basic resource bar
		 */
		public function ResourceBar(min:int, max:int, label:String, resourceType:String, current:int = 50, barType:String = RESOURCE_TYPE) 
		{
			this.min = min;
			this.max = max;
			this._value = current;
			this.label = label;
			this._type = resourceType;
			this.barType = barType;
			
			//Set up gradient properties
			barBackgroundMatrix.createGradientBox(EREN_BAR_SIZE.x, EREN_BAR_SIZE.y, (Math.PI / 2));
			fillBarMatrix.createGradientBox(EREN_BAR_SIZE.x, EREN_BAR_SIZE.y, (Math.PI / 2));
			fillBar = new Sprite();
			fillBar.x = EREN_BAR_POS.x;
			fillBar.y = EREN_BAR_POS.y;
			barBackground = new Shape();
			barBackground.x = EREN_BAR_POS.x;
			barBackground.y = EREN_BAR_POS.y;
			addChild(barBackground);
			addChild(fillBar);
			
			//Set up text formats
			labelFormat = new TextFormat();
			labelFormat.color = textColor;
			labelFormat.font = (new bold()).fontName;
			labelFormat.bold = true;
			labelFormat.size = 12;
			minmaxFormat = new TextFormat();
			minmaxFormat.color = textColor;
			minmaxFormat.font = labelFormat.font;
			minmaxFormat.bold = true;
			minmaxFormat.size = 9;
			//minmaxFormat.align = TextFormatAlign.CENTER;
			
			//Create text fields
			label_txt = new TLFTextField();
			label_txt.selectable = false;
			label_txt.defaultTextFormat = labelFormat;
			label_txt.text = label;
			label_txt.height = 16;
			label_txt.autoSize = TextFieldAutoSize.LEFT;
			label_txt.multiline = false;
			label_txt.wordWrap = false;
			label_txt.x = 0;
			label_txt.y = 0;
			addChild(label_txt);
			
			min_txt = new TLFTextField();
			min_txt.defaultTextFormat = minmaxFormat;
			min_txt.selectable = false;
			min_txt.text = min.toString();
			min_txt.height = 13;
			min_txt.autoSize = TextFieldAutoSize.LEFT;
			min_txt.multiline = false;
			min_txt.wordWrap = false;
			min_txt.x = 0;
			min_txt.y = minmaxoffset; 
			addChild(min_txt);
			
			var maxstring:String = max.toString();
			var maxTxtWidth:int = Constants.SIZE9_NUMBER_WIDTH * maxstring.length;
			
			max_txt = new TLFTextField();
			max_txt.defaultTextFormat = minmaxFormat;
			max_txt.selectable = false;
			max_txt.text = max.toString();
			max_txt.autoSize = TextFieldAutoSize.LEFT;
			max_txt.multiline = false;
			max_txt.wordWrap = false;
			max_txt.x = EREN_BAR_SIZE.x - maxTxtWidth;
			max_txt.y = minmaxoffset;
			max_txt.height = 13;
			addChild(max_txt);
			
			//Create alloc'd and free balloons
			allocdBalloon = new Balloon("" + _value + " allocated");
			freeBalloon = new Balloon("" + (max - _value) + " free");
			this.addChild(allocdBalloon);
			this.addChild(freeBalloon);
			allocdBalloon.visible = false;
			freeBalloon.visible = false;
			
			//Fill in the background bar, since this only has to be done once
			barBackground.graphics.beginGradientFill(gradientType, barBackgroundColors, 
													barBackgroundAlphas, ratios, barBackgroundMatrix, 
													spreadMethod, interp, focalPtRatio);
			barBackground.graphics.lineStyle(2, 0x59738E, 0.3);
			barBackground.graphics.drawRoundRect(0, 0, EREN_BAR_SIZE.x, EREN_BAR_SIZE.y, EREN_BAR_CORNER_RADIUS, EREN_BAR_CORNER_RADIUS);
			barBackground.graphics.endFill();
			
			this.addEventListener(MouseEvent.MOUSE_OVER, onMouseOver, false, 0, true);
			this.addEventListener(MouseEvent.MOUSE_OUT, onMouseOut, false, 0, true);
			
			draw();
		}
		
		/**
		 * Setting the value of the bar triggers a redraw and repositioning
		 * of the allocated and free balloons.
		 */
		public function set value(val:int):void 
		{
			if (val > max) 
			{
				val = max;
			}
			if (val < min)
			{
				val = min;
			}
			this._value = val;
			allocdBalloon.update("" + _value + " allocated");
			freeBalloon.update("" + (max - _value) + " free"); 
			draw();
		}
		
		public function get value():int 
		{
			return _value;
		}
		
		public function get staffType():String
		{
			return _type;
		}
		
		private function draw():void
		{
			fillBar.graphics.clear();
			
			var fillWidth:Number = ((_value - min) / (max - min)) * EREN_BAR_SIZE.x;
			fillBar.graphics.beginGradientFill(gradientType, fillBarColors, 
													fillBarAlphas, ratios, fillBarMatrix, 
													spreadMethod, interp, focalPtRatio);
			fillBar.graphics.drawRoundRect(0, 0, fillWidth, EREN_BAR_SIZE.y, EREN_BAR_CORNER_RADIUS, EREN_BAR_CORNER_RADIUS);
			fillBar.graphics.endFill();
		}
		
		private function onMouseOver(e:MouseEvent):void
		{
			var endOfFill:Number = ((_value - min) / (max - min)) * EREN_BAR_SIZE.x;
			
			var midwayAllocd:Number = endOfFill / 2;
			var midwayFree:Number = endOfFill + ((EREN_BAR_SIZE.x - endOfFill) / 2);
			
			allocdBalloon.x = midwayAllocd - (allocdBalloon.width / 2);
			allocdBalloon.y = EREN_BAR_POS.y - allocdBalloon.height; 
			freeBalloon.x = midwayFree - (freeBalloon.width / 2);
			freeBalloon.y = EREN_BAR_POS.y - freeBalloon.height; 
			
			allocdBalloon.visible = true;
			freeBalloon.visible = true;
		}
		
		private function onMouseOut(e:MouseEvent):void
		{
			allocdBalloon.visible = false;
			freeBalloon.visible = false;
		}
		
	}

}