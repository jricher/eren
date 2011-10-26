package org.mitre.eren.display.elements 
{
	import fl.text.TLFTextField;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.ui.Mouse;
	import flash.utils.Timer;
	
	/**
	 * @author Amanda Anganes
	 */
	public class erenNumericStepper extends MovieClip 
	{
		
		public var upArrowBttn:MovieClip;
		public var downArrowBttn:MovieClip;
		public var field:TLFTextField;
		
		//Properties
		private var _value:int = 0;
		private var _step:int = 1;
		private var _max:int;
		private var _min:int;
		
		//State
		private var initialTimer:Timer;
		private var intervalTimer:Timer;
		
		public function erenNumericStepper() 
		{
			this.addEventListener(Event.ADDED_TO_STAGE, onAdded);
		}
		
		private function onAdded(e:Event):void
		{
			
			field = this.getChildByName("field") as TLFTextField;
			
			upArrowBttn = this.getChildByName("upArrow") as MovieClip;
			downArrowBttn = this.getChildByName("downArrow") as MovieClip;
			
			upArrowBttn.arrow.mouseEnabled = false;
			upArrowBttn.gotoAndStop(4);
			downArrowBttn.arrow.mouseEnabled = false;
			
			upArrowBttn.addEventListener(MouseEvent.MOUSE_OVER, onOverUp);
			upArrowBttn.addEventListener(MouseEvent.MOUSE_OUT, onOutUp);
			upArrowBttn.addEventListener(MouseEvent.MOUSE_DOWN, onDownInc);
			upArrowBttn.addEventListener(MouseEvent.MOUSE_UP, onOutUp);
			
			downArrowBttn.addEventListener(MouseEvent.MOUSE_OVER, onOverDown);
			downArrowBttn.addEventListener(MouseEvent.MOUSE_OUT, onOutDown);
			downArrowBttn.addEventListener(MouseEvent.MOUSE_DOWN, onDownDec);
			downArrowBttn.addEventListener(MouseEvent.MOUSE_UP, onOutDown);
			
		}
		
		public function get value():int 
		{
			this._value = parseInt(field.text);
			return _value;
		}
		
		public function setValue(i:int):void 
		{
			this._value = i;
			field.text = _value.toString();
		}
		
		public function set step(i:int):void 
		{
			this._step = i;
		}
		
		public function get step():int 
		{
			return this._step;
		}
		
		public function set min(i:int):void
		{
			this._min = i;
		}
		
		public function get min():int 
		{
			return this._min;
		}
		
		public function set max(i:int):void
		{
			this._max = i;
		}
		
		public function get max():int 
		{
			return this._max;
		}
		
		//4, 5, 6
		private function onOverUp(e:MouseEvent):void
		{
			trace("Up arrow: over");
			(e.target as MovieClip).gotoAndStop(5);
			trace("movie clip is now at frame " + (e.target as MovieClip).currentFrame);
		}
		
		private function onOutUp(e:MouseEvent):void 
		{
			trace("Up arrow: out");
			(e.target as MovieClip).gotoAndStop(4);
			trace("movie clip is now at frame " + (e.target as MovieClip).currentFrame);
			if (intervalTimer && intervalTimer.running == true) {
				intervalTimer.stop();
			}
			if (initialTimer && initialTimer.running == true) {
				initialTimer.stop();
			}
		}
		
		private function onDownInc(e:MouseEvent):void 
		{
			trace("------------------------------------");
			trace("Up arrow: down");
			(e.target as MovieClip).gotoAndStop(6);
			
			_value = parseInt(field.text);
			_value += step;
			field.text = _value.toString();
			trace("movie clip is now at frame " + (e.target as MovieClip).currentFrame);
			
			if (initialTimer && initialTimer.running == true) {
				initialTimer.stop();
			}
			if (intervalTimer && intervalTimer.running == true) {
				intervalTimer.stop();
			} else {
				initialTimer = new Timer(600);
				initialTimer.start();
				initialTimer.addEventListener(TimerEvent.TIMER, startIncrease, false, 0, true);
			}
			
		}
		
		//1, 2, 3
		private function onOverDown(e:MouseEvent):void
		{
			trace("Down arrow: over");
			(e.target as MovieClip).gotoAndStop(2);
			trace("movie clip is now at frame " + (e.target as MovieClip).currentFrame);
		}
		
		private function onOutDown(e:MouseEvent):void 
		{
			trace("Down arrow: out");
			(e.target as MovieClip).gotoAndStop(1);
			trace("movie clip is now at frame " + (e.target as MovieClip).currentFrame);
			if (intervalTimer && intervalTimer.running == true) {
				intervalTimer.stop();
			}
			if (initialTimer && initialTimer.running == true) {
				initialTimer.stop();
			}
		}
		
		private function onDownDec(e:MouseEvent):void
		{
			trace("------------------------------------");
			trace("Down arrow: down");
			(e.target as MovieClip).gotoAndStop(3);
			
			_value = parseInt(field.text);
			_value -= step;
			field.text = _value.toString();
			trace("movie clip is now at frame " + (e.target as MovieClip).currentFrame);
			if (initialTimer && initialTimer.running == true) {
				initialTimer.stop();
			}
			if (intervalTimer && intervalTimer.running == true) {
				intervalTimer.stop();
			} else {
				initialTimer = new Timer(600);
				initialTimer.start();
				initialTimer.addEventListener(TimerEvent.TIMER, startDecrease, false, 0, true);
			}
		}
		
		private function startDecrease(e:TimerEvent):void
		{
			trace("starting decrease ticker");
			initialTimer.stop();
			if (!intervalTimer || (intervalTimer && intervalTimer.running != true)) {
				intervalTimer = new Timer(20);
				intervalTimer.start();
				intervalTimer.addEventListener(TimerEvent.TIMER, decreaseValue, false, 0, true);
			}
		}
		
		private function startIncrease(e:TimerEvent):void
		{
			trace("starting increase ticker");
			initialTimer.stop();
			if (!intervalTimer || (intervalTimer && intervalTimer.running != true)) {
				intervalTimer = new Timer(20);
				intervalTimer.start();
				intervalTimer.addEventListener(TimerEvent.TIMER, increaseValue, false, 0, true);
			}
		}
		
		private function increaseValue(e:TimerEvent):void {
			trace("timer tick - increase, delay = " + intervalTimer.delay + ", " + e.target);
			
			_value = parseInt(field.text);
			_value += step;
			field.text = _value.toString();
		}
		
		private function decreaseValue(e:TimerEvent):void {
			trace("timer tick - decrease, delay = " + intervalTimer.delay + ", " + e.target);
			_value = parseInt(field.text);
			_value -= step;
			field.text = _value.toString();
		}
		
	}

}