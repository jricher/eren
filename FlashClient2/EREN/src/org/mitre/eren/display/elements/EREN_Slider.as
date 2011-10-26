package org.mitre.eren.display.elements 
{
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.ui.Mouse;
	
	/**
	 * ...
	 * @author Amanda Anganes
	 */
	public class EREN_Slider extends MovieClip 
	{
		//Contained mc's
		//private var thumb:MovieClip;
		//private var track:MovieClip;
		
		//Values
		private var min:Number;
		private var max:Number;
		private var val:Number;
		
		//Drag helpers
		private var dragging:Boolean = false;
		private var startX:Number;
		private var xMin:Number = 12;
		private var xMax:Number = 253;
		private var valOffset:Number = -12; //add this to slider.x 
		
		/**
		 * Constructor
		 */
		public function EREN_Slider() 
		{
			
			addEventListener(Event.ADDED_TO_STAGE, onAdded);
		}
		
		/**
		 * On addition to stage, set up MC's with event listeners
		 * @param	e
		 */
		private function onAdded(e:Event):void 
		{	
			//thumb = this.getChildByName("thumb") as MovieClip;
			//track = this.getChildByName("track") as MovieClip;		
			//Listen for clicks on the track - drag thumb to clicked spot
			track.addEventListener(MouseEvent.CLICK, onTrackClick);
			
			//Listen for all mouse events on thumb
			thumb.addEventListener(MouseEvent.MOUSE_OVER, onOver);
			thumb.addEventListener(MouseEvent.MOUSE_DOWN, onDown);
			thumb.addEventListener(MouseEvent.MOUSE_MOVE, onMove);
			stage.addEventListener(MouseEvent.MOUSE_MOVE, onMove);
			thumb.addEventListener(MouseEvent.MOUSE_OUT, onOut);
			thumb.addEventListener(MouseEvent.MOUSE_UP, onUp);
			stage.addEventListener(MouseEvent.MOUSE_UP, onUp);
		}
		
		/**
		 * Set the minimum allowed value.
		 * 
		 * @param	m the minimum value this slider may attain
		 */
		public function setMin(m:Number):void
		{
			this.min = m;
			//Set textfield
			min_txt.text = m.toString();
		}
		
		/**
		 * Set the maximum allowed value.
		 * 
		 * @param	m the maximum value this slider may attain
		 */
		public function setMax(m:Number):void
		{
			this.max = m;
			//set textfield
			max_txt.text = m.toString();
		}
		
		/**
		 * Get the current value of the slider
		 * 
		 * @return
		 */
		public function getValue():Number 
		{
			return this.val;
		}
		
		/**
		 * Initialize the slider by setting min, max, and the curent value
		 * the slider should display.
		 * 
		 * @param	min
		 * @param	max
		 * @param	currentVal
		 */
		public function initSlider(min:Number, max:Number, currentVal:Number) 
		{
			this.min = min;
			min_txt.text = min.toString();
			this.max = max;
			max_txt.text = max.toString();
			this.val = currentVal;
			val_txt.text = currentVal.toString();
			
			//Position thumb
			var xpos = Math.round( ((xMax - xMin) * (val - min)) / (max - min) );
			thumb.x = xpos;
			val_txt.x = xpos + valOffset;
			val_txt.text = val.toString();
		}
		
		/**
		 * TODO : implement
		 * Move the thumb to the spot on the track that was clicked.
		 * @param	e
		 */
		private function onTrackClick(e:MouseEvent):void 
		{
			
		}
		
		//TODO: create hover state
		private function onOver(e:MouseEvent):void 
		{
			
		}
		
		//TODO: create down state
		private function onDown(e:MouseEvent):void
		{
			//begin drag
			dragging = true;
			startX = mouseX - thumb.x;
		}
		
		private function onMove(e:MouseEvent):void 
		{
			if (dragging) 
			{
				//Move the thumb
				var newX:Number = mouseX - startX;
				if (newX < xMin) {
					newX = xMin;
				}
				if (newX > xMax) {
					newX = xMax;
				}
				thumb.x = newX;
				val = xposToVal(newX);
				val_txt.x = newX + valOffset;
				val_txt.text = val.toString();
			}
		}
		
		private function onOut(e:MouseEvent):void 
		{
			
		}
		
		private function onUp(e:MouseEvent):void 
		{
			dragging = false;
		}
		
		private function xposToVal(value:Number):int {
			return Math.round(map(value, xMin, xMax, min, max));
		}
		
		private function map(value:Number,low1:Number, high1:Number,low2:Number = 0,high2:Number = 1):Number 
		{
			//if the value and the 1st range low are equal to
			// the new value must be low2
			if (value == low1) {
				return low2;
			}
			
			//normalize both sets to a 0-? range
			var range1 = high1 - low1;
			var range2 = high2 - low2;
			
			//normalize the value to the new normalized range
			var result = value - low1;
			
			//define the range as a percentage (0.0 to 1.0)
			var ratio = result / range1;
			
			//find the value in the new normalized-range
			result = ratio * range2;
			
			//un-normalize the value in the new range
			result += low2;
			
			return result;
		}
		
	}

}