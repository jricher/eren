package org.mitre.eren.display.dialog.expander 
{
	
	/**
	 * Interface for Expander Items.
	 * 
	 * @author a_anganes
	 */
	public interface IExpanderItem { 
		
		//Properties
		function get totalHeight():Number;
		function set totalHeight(n:Number):void;
		
		function get index():int;
		function set index(i:int):void;
		
		function get revealedHeight():Number;
		function set revealedHeight(n:Number):void;
		
		function get isSelected():Boolean;
		function set isSelected(b:Boolean):void;
		
		function get itemWidth():Number;
		function set itemWidth(n:Number):void;
		
		function get padding():Number;
		function set padding(n:Number):void;
		
		function get startY():Number;
		function set startY(n:Number):void;
		
		//Methods
		function select():void;
		
		function deselect():void;
		
		//Properties and methods needed so that others can treat this as a MovieClip
		function get y():Number;
		function set y(n:Number):void;
		
		function get x():Number;
		function set x(n:Number):void;
		
		function addEventListener(type:String, listener:Function, useCapture:Boolean = false, priority:int = 0, useWeakReference:Boolean = false):void;
		function removeEventListener(type:String, listener:Function, useCapture:Boolean = false):void;
		
	}
	
}