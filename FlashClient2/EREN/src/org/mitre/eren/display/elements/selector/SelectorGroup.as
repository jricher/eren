package org.mitre.eren.display.elements.selector 
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import org.mitre.eren.display.elements.selector.SelectorBar;
	import org.mitre.eren.events.ErenEvent;
	import org.mitre.eren.Constants;
	
	/**
	 * A SelectorGroup functions like a RadioButtonGroup, but for SelectorBar objects.
	 * Manages a list so that they are displayed in a stack, and only 1 item may be
	 * selected at a time. When a new item is selected the old one is deselected. One
	 * item must be selected at all times.
	 * 
	 * @author Amanda Anganes
	 */
	public class SelectorGroup
	{
		private var items:Vector.<SelectorBar>;
		
		/**
		 * Constructor. Initialize the items vector.
		 */
		public function SelectorGroup() 
		{
			items = new Vector.<SelectorBar>();
		}
		
		/**
		 * Add a SelectorBar item to the SelectorGroup.
		 * 
		 * @param	item the item to add
		 */
		public function add(item:SelectorBar):void 
		{
			item.addEventListener(ErenEvent.SELECTION_EVENT, onSelection, false, 0, true);
			items.push(item);
		}
		
		/**
		 * Remove all items from this group
		 */
		public function clearAllItems():void 
		{
			items = new Vector.<SelectorBar>();
		}
		
		/**
		 * Return the items managed by this group
		 * 
		 * @return the group's items
		 */
		public function getItems():Vector.<SelectorBar> 
		{
			return items;
		}
		
		/**
		 * Return the number of items managed by this group
		 * 
		 * @return the number of items
		 */
		public function getNumItems():int 
		{
			return items.length;
		}
		
		/**
		 * Remove the given SelectorBar from the group - this means moving all items
		 * below the given one up by 1.
		 * 
		 * @param	s the object to remove
		 */
		public function removeItem(s:SelectorBar):void
		{
			var indexRemoved:int = s.index;
			items.splice(indexRemoved, 1);
			
			for each (var sb:SelectorBar in items) 
			{
				if (sb.index < indexRemoved) 
				{
					continue;
				}
				else 
				{
					//Move this item
					sb.index -= 1;
					sb.x -= sb.height;				
				}
			}				
		}
		
		/**
		 * Get the currently selected item
		 * 
		 * @return the currently selected item
		 */
		public function getSelected():SelectorBar
		{
			for each (var s:SelectorBar in items) 
			{
				if (s.selected) {
					return s;
				}
			}
			return null;
		}
		
		/**
		 * Handle the EREN selection event
		 * 
		 * @param	e
		 */
		private function onSelection(e:ErenEvent):void 
		{
			var index:int = (e.target as SelectorBar).index;
			
			for each (var s:SelectorBar in items) 
			{
				if (s.index != index) {
					s.selected = false;
				}
				else {
					s.selected = true;
				}
			}
		}
	}
}