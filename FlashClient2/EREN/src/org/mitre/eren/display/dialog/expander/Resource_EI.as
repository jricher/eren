package org.mitre.eren.display.dialog.expander 
{
	import fl.text.TLFTextField;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.geom.Point;
	import flash.text.TextFormat;
	import flash.text.TextFieldAutoSize;
	import flashx.textLayout.elements.TextFlow;
	import flashx.textLayout.formats.TextLayoutFormat;
	import org.mitre.eren.data.dialog.ResponseOption;
	import org.mitre.eren.display.dialog.expander.IExpanderItem;
	import org.mitre.eren.display.elements.triangleSelector;
	import org.mitre.eren.display.fonts.regular;
	import org.mitre.eren.events.ErenEvent;
	import org.mitre.eren.events.ExpanderItemEvent;
	
	/**
	 * Implementation of IExpanderItem for holding EREN resource information.
	 * 
	 * @author Amanda Anganes
	 */
	public class Resource_EI extends MovieClip implements IExpanderItem {
		
		//Interface properties
		private var _totalHeight:Number;	
		private var _index:int = 0;		
		private var _revealedHeight:Number;
		private var _isSelected:Boolean = false;
		private var _itemWidth:Number = 180;
		private var _padding:Number = 5;
		private var _startY:Number;
		
		//Resource_EI specific properties
		private var _facilityID:String;
		private var field:TLFTextField;
		private var triSel:triangleSelector;
		private var content:String;
		
		//Graphics helpers
		private var boxTopLeft:Point;
		private var boxHeight:Number;
		private var defaultColor = 0x014890;
		private var selectedColor = 0x3476B8;
		private var format:TextLayoutFormat;
		
		/**
		 * Constructor. 
		 * 
		 * @param	fID facility ID; id of the facility that this EI is tied to
		 * @param	c content string; should already be in TLFMarkup form
		 * @param	w width of the EI
		 * @param	p padding
		 */
		public function Resource_EI(fID:String, c:String, w:Number, p:Number) 
		{
			this._facilityID = fID;
			this.content = "       " + c; //Prepend with spaces to make room for the arrow
			this.itemWidth = w;
			this.padding = p;
			
			this.field = new TLFTextField();
			field.x = 2 * padding;
			field.y = padding;
			field.width = itemWidth - (2 * padding);
			field.embedFonts = true;
			field.multiline = true;
			field.wordWrap = true;
			field.autoSize = TextFieldAutoSize.LEFT;
			field.selectable = false;
			
			//This sets the content of the text field and allows TLF formatting tags
			//to be included in the string.
			field.tlfMarkup = content;
			
			this.addChild(field);
			
			var myFont:regular = new regular();			
			this.format = new TextLayoutFormat();
			format.color = 0xFFFFFF;
			format.fontFamily = myFont.fontName;
			format.fontSize = 16;
			format.fontLookup = "embeddedCFF";
			
			var myTextFlow:TextFlow = field.textFlow;
			myTextFlow.hostFormat = format;
			myTextFlow.flowComposer.updateAllControllers();
			
			//Flow composer does not update and apply text format until
			//the text field is on the stage.
			this.addEventListener(Event.ADDED_TO_STAGE, added);
			
		}
		
		private function added(e:Event):void 
		{
			this.init();
		}
		
		private function fireRestackEvent():void 
		{
			dispatchEvent(new ExpanderItemEvent(ExpanderItemEvent.RESTACK));
		}
		
		private function fireSelectionEvent():void 
		{
			var e:ExpanderItemEvent = new ExpanderItemEvent(ExpanderItemEvent.RESOURCE_SELECTION);
			e._selected = isSelected;
			e._facilityID = facilityID;
			dispatchEvent(e);
		}
		
		private function init():void
		{
			//lineHeight is the height of a single line of text, without any 
			//padding or offsets
			var lineHeight:Number = (field.height / field.numLines);
			this.totalHeight = field.height + (2 * padding);			
			this.revealedHeight = lineHeight + (2 * padding);
			
			if (field.numLines > 1) {
				//TODO: capitalize triangleselector
				triSel = new triangleSelector();
				triSel.gotoAndStop("collapsed");
				triSel.x = padding;
				triSel.y = padding;
				addChild(triSel);
			}
			
			drawBox(defaultColor);
			
			field.width = itemWidth * field.numLines;
			
			fireRestackEvent();
			
		}
		
		private function drawBox(color):void 
		{
			this.graphics.clear();
			this.graphics.beginFill(color);
			this.graphics.drawRect(0, 0, itemWidth, totalHeight);
			this.graphics.endFill();
		}
		
		/**
		 * Update the content of the EI with a new content string.
		 * 
		 * @param	c the new content string (Must already be in valid TLFMarkup form)
		 */
		public function updateText(c:String):void {
			var oldNumLines:Number = field.numLines;
			this.content = "       " + c;
			
			field.tlfMarkup = content;
			var myTextFlow:TextFlow = field.textFlow;
			myTextFlow.hostFormat = format;
			myTextFlow.flowComposer.updateAllControllers();
			
			//Check if new height is the same as previous
			if (oldNumLines == field.numLines) {
				//Same, nothing to do
				return;
			} else {
				//Different, need to redraw
				var lineHeight:Number = (field.height / field.numLines);
				this.totalHeight = field.height + (2 * padding);			
				this.revealedHeight = lineHeight + (2 * padding);
				
				drawBox(defaultColor);
				fireRestackEvent();
				
				if (this.isSelected) {
					select();
				}
			}			
		}
		
		/**
		 * Select this EI.
		 */
		public function select():void
		{
			this.isSelected = true;
			drawBox(selectedColor);
			triSel.gotoAndStop("expanded");
			fireSelectionEvent();
			
			field.width = itemWidth;
			//field.height = boxHeight - (padding * 2)
			
			var myTextFlow:TextFlow = field.textFlow;
			myTextFlow.hostFormat = format;
			myTextFlow.flowComposer.updateAllControllers();
			
		}
		
		/**
		 * Deselect this EI
		 */
		public function deselect():void
		{
			this.isSelected = false;
			drawBox(defaultColor);
			triSel.gotoAndStop("collapsed");
			fireSelectionEvent();
			
			field.width = itemWidth * field.numLines;
			//field.height = _revealedHeight;
			
			var myTextFlow:TextFlow = field.textFlow;
			myTextFlow.hostFormat = format;
			myTextFlow.flowComposer.updateAllControllers();
			
		}
		
		/** Getters and setters for private fields **/
		
		public function get totalHeight():Number
		{
			return this._totalHeight;
		}
		
		public function set totalHeight(n:Number):void
		{
			this._totalHeight = n;
		}
		
		public function get index():int
		{
			return this._index;
		}
		
		public function set index(i:int):void
		{
			this._index = i;
		}
		
		public function get revealedHeight():Number
		{
			return this._revealedHeight;
		}
		
		public function set revealedHeight(n:Number):void
		{
			this._revealedHeight = n;
		}
		
		public function get isSelected():Boolean
		{
			return this._isSelected;
		}
		
		public function set isSelected(b:Boolean):void
		{
			this._isSelected = b;
		}
		
		public function get itemWidth():Number
		{
			return this._itemWidth;
		}
		
		public function set itemWidth(n:Number):void
		{
			this._itemWidth = n;
		}
		
		public function get padding():Number
		{
			return this._padding;
		}
		
		public function set padding(n:Number):void
		{
			this._padding = n;
		}
		
		public function get startY():Number
		{
			return this._startY;
		}
		
		public function set startY(n:Number):void
		{
			this._startY = n;
		}
		
		public function get facilityID():String
		{
			return this._facilityID;
		}
	}
}