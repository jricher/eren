package org.mitre.eren
{
	import flash.geom.Point;
	import org.mitre.eren.data.scenario.POD;

	/**
	 * This class defines static constants for use throughout the UI code.
	 */
	public class Constants
	{
		public static const PRIORITY_NORMAL:int = 0;
		public static const PRIORITY_WARNING:int = 1;
		public static const PRIORITY_DIRE:int = 2;
		
		public static const NOTIFICATION_START:Point = new Point(640, 558);
		public static const NOTIFICATION_STARTX:int = 640;
		public static const NOTIFICATION_STARTY:int = 558;
		public static const NOTIFICATION_BUFFER:int = 4;
		
		public static const DEFAULT_MAP_SIZE:Point = new Point(731.05, 549.90);
		public static const DEFAULT_ZOOM_LEVEL:Number = 11;
		
		public static const DIALOG_SCROLLBOX_BUFFER:Number = 2;
		public static const DIALOG_SCROLLBAR_WIDTH:Number = 12;
		public static const DIALOG_SCROLLBOX_POS:Point = new Point(9.4, 305);
		public static const DIALOG_SCROLLBOX_SIZE:Point = new Point(280, 246);
		public static const DIALOG_EXPANDER_WIDTH:Number = 280;
		
		public static const NUMERIC_STEPPER_POS:Point = new Point(133, 396);
		public static const SLIDER_POS:Point = new Point(30, 330);
		
		public static const QUESTION_TEXT_HEIGHT:Number = 127;
		public static const NOTIFICATION_TEXT_HEIGHT:Number = 375;
		public static const QUESTION_TEXT_POS:Point = new Point(8.4, 176.05);
		public static const QUESTION_TEXT_WIDTH:Number = 293;
		
		public static const EIG_PADDING:int = 5;
		
		public static const FACILITIES_SCROLLBOX_POS:Point = new Point( 11.5, 85);
		public static const FACILITIES_SCROLLBOX_SIZE:Point = new Point(280, 235);
		
		public static const SCENARIONAME_START:Point = new Point(12.05, 2.5);
		public static const SCENARIODURATION_START:Point = new Point(208, 2.5);
		public static const SCENARIOGAMETIME_START:Point = new Point(332, 2.5);
		public static const SCENARIO_BAR_POS:Point = new Point(495.55, 210);
		
		public static const GAMENAME_START:Point = new Point(21.15, 4);
		public static const GAMEDURATION_START:Point = new Point(329, 4);
		public static const GAMEGAMETIME_START:Point = new Point(472, 4);
		public static const GAMEPLAYERS_START:Point = new Point(630.5, 4);
		
		public static const GAME_BAR_POS:Point = new Point(348, 130);
		
		public static const ROLETITLE_START:Point = new Point(22.55, 4);
		public static const ROLE_BAR_POS:Point = new Point(436, 130);
		
		public static const VEE_LEFT_X:Number = 15;
		public static const BUBBLE_LEFT_X:Number = 0;
		public static const VEE_RIGHT_X:Number = 448;
		public static const BUBBLE_RIGHT_X:Number = 330;
		
		public static const RESOURCE_BAR_START_POS:Point = new Point(20, 362.10);
		public static const RESOURCE_BAR_OFFSET:Number = 47.6;
		
		public static const SIZE9_NUMBER_WIDTH:int = 5;
		//etc as needed
	}
}