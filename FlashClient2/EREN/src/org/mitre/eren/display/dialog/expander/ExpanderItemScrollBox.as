package org.mitre.eren.display.dialog.expander
{	
	import fl.transitions.Tween;
	import fl.transitions.easing.Regular;
	import flash.display.MovieClip;
	import flash.display.Sprite;
	import flash.display.Stage;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.display.GradientType;
	import org.mitre.eren.events.ExpanderItemEvent;
	
	/**
	 * An ExpanderItemScrollBox (EIGSB) is a scrollable container for displaying
	 * EIGs. 
	 * 
	 * @author a_anganes
	 */
	public class ExpanderItemScrollBox extends MovieClip
	{
		
		private var sb:ScrollBar;
		private var _mask:MovieClip;
		private var _width:Number;
		private var _height:Number;
		private var bufferWidth:Number;
		private var t:Tween;
		
		public var eig:ExpanderItemGroup;
		private var boxColor = 0x05203C;
		
		/**
		 * Constructor. 
		 * 
		 * @param	size a Point describing the desired box size for the EIGSB
		 * @param	sbW scroll bar width
		 * @param	e the EIG to contain within this control
		 * @param	buffWidth buffer width; width of dead space to leave between the scrollbar and the content
		 * @param	s a reference to the Stage (requried by the ScrollBar constructor)
		 */
		public function ExpanderItemScrollBox(size:Point, sbW:Number, e:ExpanderItemGroup, buffWidth:Number, s:Stage) {
			
			_width = size.x;
			_height = size.y;
			bufferWidth = buffWidth;
			eig = e;
			
			eig.addEventListener(ExpanderItemEvent.HEIGHT_CHANGE, heightChangedListener);
			eig.addEventListener(ExpanderItemEvent.MOVE_TO_SHOW, moveToShowListener);
			
			var totalWidth:Number = _width + sbW + buffWidth;
			
			this.graphics.beginFill(boxColor, 1);
			this.graphics.drawRoundRect( -1.5, 3.5, totalWidth + 3, _height-2, 4, 4);
			this.graphics.endFill();
			
			addChild(eig);	
			
			//Draw the mask
			_mask = new MovieClip();
			addChild(_mask);
			_mask.x = 0;
			_mask.y = 0;
			
			_mask.graphics.clear();
			_mask.graphics.beginFill(0x000000, 1);
			_mask.graphics.drawRect(0, 5, _width - bufferWidth, _height - 5);
			_mask.graphics.endFill();
			
			//Apply the mask to the EIG
			//trace("adding mask");
			eig.mask = _mask;
			
			
			//Create and add the scrollbar
			sb = new ScrollBar(_height - 5, sbW, .2, s);
			sb.addEventListener(ScrollBarEvent.VALUE_CHANGED, sbchanged);
			sb.y = 5;
			sb.x = _width + buffWidth;
			addChild(sb);
			
			sb.resizeTrack(eig.height, _height);
			
		}
		
		//TODO: scrollbar does not behave as expected
		private function heightChangedListener(e:ExpanderItemEvent):void {
			
			//trace("height changed: new height = " + newHeight);
			sb.resizeTrack(e._newHeight, _height);
		}
		
		private function moveToShowListener(e:ExpanderItemEvent):void {
			//convert position to percentage of eig's height
			var percent:Number = e._position / eig.height;
			sb.scrollToPercentage(percent);
		}
		
		/**
		 * Convert an angle measured in degrees to one in radians.
		 * 
		 * @param	a the angle in degrees
		 * @return the angle in radians
		 */
		private function toRad(a:Number):Number {
			return a*Math.PI/180;
		}
		
		//react when the scrollbar moves - reposition the eig
		//TODO: listen to mousewheel events
		//TODO: click on track => move scrollbar
		private function sbchanged(e:ScrollBarEvent):void 
		{
			//trace("e.scrollpercent = " + e.scrollPercent + ", eig.revealedHeignt = " + eig.revealedHeight + ", this.height = " + this._height);
			var to:Number = -(eig.revealedHeight -this._height) * e.scrollPercent;// - this._height;
			//trace("Got scroll event: to = " + to);
			scrollToPos(to);
		}
		
		/**
		 * Scroll to a position.
		 * 
		 * @param	to
		 */
		public function scrollToPos(to:Number):void {
			/*if (t.isPlaying) 
			{
				t.stop();
			}*/
			
			if (to <= 0) 
			{
				//trace("scrolltopos: to = " + to + ", current y = " + eig.y);
				t = new Tween(eig, "y", Regular.easeIn, eig.y, to, 3);
			}
		}	
	}
}