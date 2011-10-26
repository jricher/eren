package org.mitre.eren.display.map 
{
	import flash.display.MovieClip;
	import flash.events.EventDispatcher;
	import flash.events.Event;
	import flash.geom.Point;
	import flash.utils.Dictionary;
	import org.mitre.eren.data.dialog.KmlLayer;
	import org.mitre.eren.display.elements.erenCheckbox;
	import org.mitre.eren.events.ErenEvent;
	
	
	/**
	 * Class to dynamically build and display/manage a layer control for viewing layers of KML
	 * on the EREN map. Clicking on the checkboxes fires ErenEvent.CHECKSELECTED and ErenEvent.CHECKDESELECTED.
	 * 
	 * @author Amanda Anganes
	 */
	public class LayerControl extends MovieClip
	{
		//Drawing constants
		private var padding:int = 5;
		private var checktextbuffer:int = 12;
		private var fillColor:uint = 0x004D9C;
		private var outlineColor:uint = 0xFFFFFF;
		private var fillAlpha:Number = 0.85;
		private var outlineAlpha:Number = 1.0;
		private var lineThickness:Number = 1.5;
		private var roundeRectRadius:int = 3;
		private var topRight:Point = new Point(738, 33);
		private var checkboxHeight:int = 15;
		private var checkboxBuffer:int = 8;
		
		//Components
		private var layers:Dictionary;
		private var checkboxes:Vector.<erenCheckbox>;
		
		/**
		 * Constructor
		 */
		public function LayerControl() 
		{
			layers = new Dictionary();
			checkboxes = new Vector.<erenCheckbox>();
		}
		
		/**
		 * Add the given KmlLayer to the collection.
		 * 
		 * @param	k   the KmlLayer to add
		 */
		public function addLayer(k:KmlLayer):void 
		{
			if (layers[k.name] == null) 
			{
				layers[k.name] = k;
				
				var c:erenCheckbox = new erenCheckbox(k.name);
				checkboxes.push(c);
				addChild(c);
				c.x = padding;
				c.y = padding + (checkboxHeight + checkboxBuffer) * (checkboxes.length - 1);
				c.addEventListener(ErenEvent.CHECK_SELECTION_EVENT, checkEvent);
				c.setChecked();
				
				redraw();
			}
		}
		
		private function redraw():void 
		{
			if (this.stage != null) 
			{
				this.graphics.clear();
				this.graphics.beginFill(fillColor, fillAlpha);
				this.graphics.lineStyle(lineThickness, outlineColor, outlineAlpha);
				this.graphics.drawRoundRect(0, 0, this.width + padding, this.height + padding, roundeRectRadius, roundeRectRadius);
				this.graphics.endFill();
			} 
			else 
			{
				this.addEventListener(Event.ADDED_TO_STAGE, onAdded);				
			}
		}
		
		private function onAdded(e:Event):void
		{
			redraw();
		}
		
		private function checkEvent(e:ErenEvent):void
		{
			if (e._isSelected) 
			{
				(layers[e._checkboxName] as KmlLayer).showLayer();
			}
			else 
			{
				(layers[e._checkboxName] as KmlLayer).hideLayer();
			}
		}
	}
}