package org.mitre.eren.display.map.timeline
{
	import org.mitre.eren.display.map.timeline.bubble;
	import org.mitre.eren.display.map.timeline.vee;
	import org.mitre.eren.Constants;
	import flash.display.MovieClip;
	import flash.events.Event;
	import fl.text.TLFTextField;
	
	/**
	 * Timeline class to control timeline widget.  Replaces ClockPanel.
	 * 
	 * @author Amanda Anganes
	 **/
	public class Timeline extends MovieClip
	{
		
		private var v:vee;
		private var bub:bubble;
		private var time_txt:TLFTextField;
		private var ratio:Number;
		private var syncDate:Date;
		private var numGameSeconds:Number;
		private var startTime:Date;
		private var endTime:Date;
		
		/**
		 * Constructor.
		 */
		public function Timeline()
		{
			super();
			syncDate = new Date();
		}
		
		/**
		 * Once the Timeline has been built on the stage, the creater or parent should call
		 * Timeline.init() to set up our references to the components created on the stage.
		 */
		public function init():void 
		{
			this.bub = this.getChildByName("bubble_mc") as bubble;
			this.v = this.getChildByName("vee_mc") as vee;
		}
		
		/**
		 * Set the syncronization date for this Timeline
		 * 
		 * @param	d the syncronization date
		 */
		public function setSync(d:Date):void 
		{
			syncDate = d;
		}
		
		/**
		 * Set the gametime / walltime ratio
		 * 
		 * @param	n the new ratio
		 */
		public function setRatio(n:Number):void 
		{
			ratio = n;
		}
		
		/**
		 * Set the start time for this Timeline
		 * 
		 * @param	d a Date representing the start time
		 */
		public function setStartTime(d:Date):void 
		{
			this.startTime = d;
		}
		
		/**
		 * Set the duration of the game, in seconds. After this many seconds
		 * has ellapsed, the bubble should be at the end of the timeline and 
		 * stop advancing.
		 * 
		 * @param	n the game duration, in seconds
		 */
		public function setNumGameSeconds(n:int):void 
		{
			this.numGameSeconds = n;
		}
		
		/**
		 * Advance the current time. The number passed in by the clocktick is the number of seconds since
		 * game start, so this is added to the sync date and then displayed.
		 * 
		 * @param	t the number of seconds ellapsed since game start
		 */
		public function tick(t:Number):void 
		{
			var d:Date = new Date(syncDate.fullYear, syncDate.month, syncDate.date, 
									syncDate.hours, syncDate.minutes, syncDate.seconds + t, syncDate.milliseconds);
			bub.setTime(d);
			
			var percent:Number = t / numGameSeconds;
			if (percent >= 1) 
			{
				percent = 1;
			}
			
			var veeX:Number = (percent * (Constants.VEE_RIGHT_X - Constants.VEE_LEFT_X)) + Constants.VEE_LEFT_X;
			var bubbleX:Number = (percent * (Constants.BUBBLE_RIGHT_X - Constants.BUBBLE_LEFT_X)) + Constants.BUBBLE_LEFT_X;
			
			v.x = veeX;
			bub.x = bubbleX;
		}
		
		/**
		 * Display a specific Date
		 * 
		 * @param	d the Date to display
		 */
		public function display(d:Date):void 
		{
			bub.setTime(d);
		}
		
	}
}