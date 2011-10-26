package org.mitre.eren.display.dialog.expander 
{
	import fl.motion.Tweenables;
	import fl.transitions.Tween;
	import fl.transitions.TweenEvent;
	import flash.display.DisplayObject;
	import flash.display.MovieClip;
	import flash.display.Sprite;
	import flash.events.MouseEvent;
	import fl.transitions.easing.Strong;
	import flash.ui.Mouse;
	import flash.utils.IExternalizable;
	import org.mitre.eren.events.ErenEvent;
	import org.mitre.eren.events.ExpanderItemEvent;
	
	/**
	 * An ExpanderItemGroup (EIG) functions similarly to a radio button group.
	 * It displays a stacked, collapsable/expandable accordian group of 
	 * IExpanderItems. An EIG may be placed inside an ExpanderItemScrollBox (EIGSB), or
	 * used by itself. Note however that if an EIG is used without the EIGSB, as items
	 * are expanded or collapsed the height of the control will change.
	 * 
	 * An EIG has two modes: SINGLE or MULTI. In SINGLE mode, only 1 ExpanderItem 
	 * can be selected at one time. If an item is selected and a user tries to 
	 * select another item, the first item will be automatically collapsed/deselected
	 * while the new item is expanded/selected.
	 * 
	 * In MULTI mode, users can select as many items in the group as they desire
	 * at one time. Users must explicitly collapse/deselect an item if they want
	 * it to close.
	 * 
	 * @author Amanda Anganes
	 */
	public class ExpanderItemGroup extends MovieClip
	{
		//Enum: types of ExpanderItemGroups
		public static const SINGLE:int = 0;
		public static const MULTI:int = 1;	
		
		//Accessable properties.  
		//Once created, only type can be changed/set.
		private var _type:int = SINGLE;
		private var _revealedHeight:Number;
		private var _itemWidth:Number;
		private var _expanderitems:Vector.<IExpanderItem>;
		
		//Private properties
		private var paramObj:Object;
		private var theTween:Tween;
		private var theMask:Sprite;
		private var maskColor = 0x05203C;
		
		//Animation helpers
		private var animatingEI:IExpanderItem;
		private var selectingEI:IExpanderItem;
		private var deselectingEI:IExpanderItem;
		private var selecting:Boolean = false;
		private var isOneOrMoreSelected:Boolean = false;
		private var finishPositions:Array;
		private var startPositions:Array;
		private var maskStartY:Number;
		private var maskFinishY:Number;
		private var firstRestack:Boolean = true;
		
		/**
		 * Constructor. 
		 * 
		 * @param	width the desired width of the EIG. Height is variable.
		 * @param	type the type of EIG, must be one of {SINGLE, MULTI}
		 */
		public function ExpanderItemGroup(width:Number, type:int) 
		{
			this._itemWidth = width;
			this._type = type;
			
			paramObj = { t:0 };
			_expanderitems = new Vector.<IExpanderItem>;
			finishPositions = new Array();
			startPositions = new Array();
			
			theMask = new Sprite();
			addChild(theMask);
			_revealedHeight = 0;
			maskStartY = 0;
			
		}
		
		/**
		 * Add an item to the EIG.
		 * 
		 * @param	item the item to add to the EIG
		 */
		public function addItem(item:IExpanderItem):void 
		{
			expanderItems.push(item);	
			item.addEventListener(ExpanderItemEvent.RESTACK, this.restackItems);
			item.addEventListener(MouseEvent.CLICK, onclick);
			addChild(item as DisplayObject);
			
			item.x = 0;
			item.y = (item.revealedHeight) * (numItems - 1);
			item.startY = item.y;
			item.index = numItems - 1;
			
			//Remove and re-add the mask after adding this new item - 
			//this way z-order is preserved without having to touch it			
			removeChild(theMask);
			this._revealedHeight += item.revealedHeight;
			theMask.y = 0;
			theMask.y = item.y + item.revealedHeight;
			addChild(theMask);
		}
		
		/**
		 * Remove all items from this EIG.
		 */
		public function clearItems():void 
		{
			for (var i:int = 0; i < numItems; i++) {
				var tmp:IExpanderItem = expanderItems.pop();
				tmp.removeEventListener(MouseEvent.CLICK, onclick);
				this.removeChild(tmp as DisplayObject);
			}
			expanderItems = new Vector.<IExpanderItem>;
			theMask.graphics.clear();
		}
		
		/**
		 * Get the currently selected item(s) in this EIG.
		 * 
		 * @return a vector of IExpanderItems from this EIG that are currently selected
		 */
		public function getSelected():Vector.<IExpanderItem> 
		{
			var retVec:Vector.<IExpanderItem> = new Vector.<IExpanderItem>;
			//trace("Inside EIG.getSelected");
			for each (var ei:IExpanderItem in expanderItems)
			{
				if (ei.isSelected) 
				{
					//trace("Selected item: " + ei);
					retVec.push(ei);
				}
			}
			
			
			
			return retVec;
		}
		
		/**
		 * Click handler for the items in this EIG
		 * 
		 * @param	e the mouse event
		 */
		private function onclick(e:MouseEvent):void 
		{
			var item:IExpanderItem = e.currentTarget as IExpanderItem;
			
			if (isOneOrMoreSelected && this._type == SINGLE) 
			{
				//There should only be one item in the getSelected vector
				var s:IExpanderItem = getSelected()[0];
				if (s == item) 
				{
					//Clicked item is already selected; so deselect it
					deselect(s);
				}
				else 
				{
					//An item is selected but another item was clicked:
					//deselect previous selection and select clicked item
					deselctAndSelect(item, s);
					selectingEI = item;
					deselectingEI = s;
				}
			}
			else 
			{
				//Either nothing is selected now, or this EIG is of type multi, 
				//so just select or deselect the clicked item
				if (item.isSelected) 
				{					
					deselect(item);
				} 
				else 
				{	
					select(item);
				}
			}
		}
		
		/**
		 * Select an item. Calculate start and finish positions
		 * for all expander items, as well as start and finish for the mask.
		 * 
		 * @param	item the item to be selected
		 */
		private function select(item:IExpanderItem):void 
		{
			isOneOrMoreSelected = true;
			item.select();
			
			var heightOffset:Number = item.totalHeight - item.revealedHeight;
			
			startPositions = new Array();
			finishPositions = new Array();
			
			//Calculate start and finish positions for all items
			for (var i:int = 0; i < numItems; i++) 
			{
				startPositions[i] = expanderItems[i].y;
				if (i > item.index) 
				{
					//This item is below the expanding one, so it must move down (y++) 
					finishPositions[i] = expanderItems[i].y + heightOffset;
				}
				else 
				{
					//This item is above the expanding one, so it does not need to move at all
					finishPositions[i] = expanderItems[i].y;
				}
			}
			
			//Handle the mask
			maskStartY = theMask.y;
			if (item.index == (numItems - 1)) 
			{
				//This is the last item, only the mask will move
				maskFinishY = finishPositions[numItems - 1] + item.totalHeight;
			}
			else 
			{
				var lastIndex:int = numItems - 1;
				var lastItem:IExpanderItem = expanderItems[lastIndex];
				maskFinishY = finishPositions[lastIndex] + (lastItem.isSelected ? lastItem.totalHeight : lastItem.revealedHeight);
			}
			
			startTween();
			var e:ExpanderItemEvent = new ExpanderItemEvent(ExpanderItemEvent.SELECTION_EVENT);
			e._selected = true;
			dispatchEvent(e);
			var e2:ExpanderItemEvent = new ExpanderItemEvent(ExpanderItemEvent.HEIGHT_CHANGE);
			e2._newHeight = this.height;
			dispatchEvent(e2);
			var e3:ExpanderItemEvent = new ExpanderItemEvent(ExpanderItemEvent.MOVE_TO_SHOW);
			e3._position = finishPositions[item.index];
			dispatchEvent(e3);
		}
		
		/**
		 * Deselect an item. Calculate start and finish positions
		 * for all expander items, as well as start and finish for the mask.
		 * 
		 * @param	item the item to be deselected
		 */
		private function deselect(item:IExpanderItem):void 
		{
			item.deselect();
			
			startPositions = new Array();
			finishPositions = new Array();
			
			var vec:Vector.<IExpanderItem> = getSelected();
			if (vec.length == 0) {
				isOneOrMoreSelected = false;
			}
			
			var heightOffset:Number = item.totalHeight - item.revealedHeight;
			
			//Calculate start and finish positions for all items
			for (var i:int = 0; i < numItems; i++) 
			{
				startPositions[i] = expanderItems[i].y;
				if (i > item.index) 
				{
					//This item is below the collapsing one, so it must move up (y--) 
					finishPositions[i] = expanderItems[i].y - heightOffset;
				}
				else 
				{
					//This item is above the collapsing one, so it does not need to move at all
					finishPositions[i] = expanderItems[i].y;
				}
			}
			
			//Handle the mask
			maskStartY = theMask.y;
			var lastIndex:int = numItems - 1;
			var lastItem:IExpanderItem = expanderItems[lastIndex];
			maskFinishY = finishPositions[lastIndex] + (lastItem.isSelected ? lastItem.totalHeight : lastItem.revealedHeight);
			
			startTween();
			var e:ExpanderItemEvent = new ExpanderItemEvent(ExpanderItemEvent.SELECTION_EVENT);
			e._selected = isOneOrMoreSelected;
			dispatchEvent(e);
			var e2:ExpanderItemEvent = new ExpanderItemEvent(ExpanderItemEvent.HEIGHT_CHANGE);
			e2._newHeight = this.height;
			dispatchEvent(e2);
			var e3:ExpanderItemEvent = new ExpanderItemEvent(ExpanderItemEvent.MOVE_TO_SHOW);
			e3._position = finishPositions[item.index];
			dispatchEvent(e3);
		}
		
		/**
		 * Select item s and deselect item d. Calculate start and finish positions
		 * for all expander items, as well as start and finish for the mask.
		 * 
		 * @param	s the item to be selected
		 * @param	d the item to be deselected
		 */
		private function deselctAndSelect(s:IExpanderItem, d:IExpanderItem):void 
		{			
			d.deselect();
			s.select();
			
			var heightOffset:Number = s.totalHeight - s.revealedHeight;
			
			startPositions = new Array();
			finishPositions = new Array();
			
			//Calculate start and finish positions for all items
			for (var i:int = 0; i < numItems; i++) 
			{
				startPositions[i] = expanderItems[i].y;
				if (i > s.index) 
				{
					//This item will move
					finishPositions[i] = expanderItems[i].startY + heightOffset;
				}
				else 
				{
					//Finish is the initial start position 
					finishPositions[i] = expanderItems[i].startY;
				}
			}
			
			//Handle the mask
			maskStartY = theMask.y;
			if (s.index == (numItems - 1)) 
			{
				//This is the last item, only the mask will move
				maskFinishY = finishPositions[numItems - 1] + s.totalHeight;
			}
			else 
			{
				maskFinishY = finishPositions[numItems - 1] + expanderItems[numItems - 1].revealedHeight;
			}
			var e3:ExpanderItemEvent = new ExpanderItemEvent(ExpanderItemEvent.MOVE_TO_SHOW);
			e3._position = finishPositions[s.index];
			dispatchEvent(e3);
			startTween();
		}
		
		/** Animation methods **/
		
		/**
		 * Start theTween and set up its motion change and motion finish handlers.
		 */
		private function startTween():void 
		{
			theTween = new Tween(paramObj, "t", Strong.easeOut, 0, 1, .3, true);
			theTween.addEventListener(TweenEvent.MOTION_CHANGE, motionChange);
			theTween.addEventListener(TweenEvent.MOTION_FINISH, motionFinish);
		}
		
		/**
		 * Apply paramObj.t % of the difference between start and finish for
		 * all items.  When done, fire the height change event so the container
		 * (usually a scrollbox) knows to adjust.
		 * @param	e
		 */
		private function motionChange(e:TweenEvent):void 
		{
			for (var i:int = 0; i < numItems; i++) 
			{
				var diff:Number = finishPositions[i] - startPositions[i];
				expanderItems[i].y = startPositions[i] + (paramObj.t * diff);
			}
			
			theMask.y = maskStartY + (paramObj.t * (maskFinishY - maskStartY));
			this._revealedHeight = theMask.y;
			var e2:ExpanderItemEvent = new ExpanderItemEvent(ExpanderItemEvent.HEIGHT_CHANGE);
			e2._newHeight = this.height;
			dispatchEvent(e2);
		}
		
		/**
		 * Stop tweens and clean up.
		 * @param	e
		 */
		private function motionFinish(e:TweenEvent):void 
		{
			theTween.removeEventListener(TweenEvent.MOTION_CHANGE, motionChange);
			theTween.removeEventListener(TweenEvent.MOTION_FINISH, motionFinish);
		}
		
		/** Helper methods **/
		
		private function redrawMask():void 
		{
			var h:Number = expanderItems[numItems - 1].totalHeight;
			if (h > 0) 
			{
				theMask.graphics.clear();
				theMask.graphics.beginFill(maskColor);
				theMask.graphics.drawRect(0, 0, itemWidth, expanderItems[numItems-1].totalHeight);
				theMask.graphics.endFill();
			}
		}
		
		private function restackItems(e:ExpanderItemEvent):void 
		{
			var itemHeight:Number = expanderItems[0].revealedHeight;
			
			for (var i:int = 1; i < numItems; i++) 
			{
				var item:IExpanderItem = expanderItems[i];
				item.y = (itemHeight * i);
				item.startY = item.y;
			}
			
			redrawMask();
			
			theMask.y = expanderItems[numItems - 1].startY + itemHeight;
			maskStartY = theMask.y;
		}
		
		
		/** Accessor methods **/
		
		function get numItems():int {
			return _expanderitems.length;
		}
		
		 function get type():int {
			return this._type;
		}
		
		public function set type(t:int):void {
			this._type = t;
		}
		
		public function get revealedHeight():Number {
			return this._revealedHeight;
		}
		
		public function set revealedHeight(n:Number):void {
			this._revealedHeight = n;
		}
		
		public function get itemWidth():Number {
			return this._itemWidth;
		}
		
		public function set itemWidth(n:Number):void {
			this._itemWidth = n;
		}
		
		public function get expanderItems():Vector.<IExpanderItem> {
			return this._expanderitems;
		}
		
		public function set expanderItems(e:Vector.<IExpanderItem>): void{
			this._expanderitems = e;
		}
		
	}

}