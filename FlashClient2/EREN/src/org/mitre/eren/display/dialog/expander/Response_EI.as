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
	import org.mitre.eren.events.ExpanderItemEvent;
	import org.mitre.eren.data.dialog.ResponseOption;
	import org.mitre.eren.display.dialog.expander.IExpanderItem;
	import org.mitre.eren.display.fonts.regular;
	import org.mitre.eren.display.elements.gradient_line;
	import org.mitre.eren.display.elements.ellipsis;
	import org.mitre.eren.events.ExpanderItemEvent;
	
	/**
	 * Implementation of IExpanderItem for displaying UserMessage ResponseOption data
	 * 
	 * @author Amanda Anganes
	 */
	public class Response_EI extends MovieClip implements IExpanderItem
	{
		//Interface properties
		private var _totalHeight:Number;	
		private var _index:int = 0;		
		private var _revealedHeight:Number;
		private var _isSelected:Boolean = false;
		private var _itemWidth:Number = 180;
		private var _padding:Number = 1;
		private var _startY:Number;
		
		//Response_EI-specific properties
		private var _responseOpt:ResponseOption;
		private var content:String;
		private var field:TLFTextField;
		private var format:TextLayoutFormat;
		private var isMultiline:Boolean = false;
		private var elips:ellipsis;
		
		//Graphics helpers
		private var boxTopLeft:Point;
		private var boxHeight:Number;
		private var lineHeight:Number;
		private var fieldWidth:Number;
		private var defaultColor = 0x014890;
		private var selectedColor = 0x3476B8;
		
		/**
		 * Constructor. 
		 * 
		 * @param	w the width of this EI
		 * @param	p padding
		 * @param	r the ResponseOption associated with this EI
		 */
		public function Response_EI(w:Number, p:Number, r:ResponseOption) 
		{		
			this._responseOpt = r;
			this.content = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\" lineBreak=\"explicit\"><span fontWeight=\"bold\">" 
						+ r.getShortResponse();
			if (r.getLongResponse() != null && r.getLongResponse().length > 1) 
			{
				this.content += ": </span><span>" + r.getLongResponse() + "</span></TextFlow>";	
			}
			else 
			{
				this.content += "</span></TextFlow>";
			}
			this.itemWidth = w;
			this.padding = p;
			
			this.field = new TLFTextField();
			field.x = padding;
			field.y = padding;
			field.width = itemWidth - (4 * padding);
			fieldWidth = field.width;
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
		
		private function init():void
		{
			//lineHeight is the height of a single line of text, without any 
			//padding or offsets
			lineHeight = (field.height / field.numLines);
			this._totalHeight = field.height + (2 * padding);
			
			var g:gradient_line = new gradient_line();
			
			if (index == 0) 
			{
				field.y = g.height + padding;
				this.revealedHeight = lineHeight + g.height + (2 * padding);
				boxTopLeft = new Point(0, 0);
				boxHeight = field.height + (2 * padding) + g.height;
				this._totalHeight += g.height;
			} 
			else 
			{				
				addChild(g);
				g.x = 0;
				g.y = 0;
				field.y = g.height + padding;
				this.revealedHeight = lineHeight + g.height + (2 * padding);
				this.totalHeight += g.height;
				boxTopLeft = new Point(0, g.height);
				boxHeight = field.height + (2 * padding);
			}
			
			drawBox(defaultColor);
			
			if (field.numLines > 1) 
			{
				elips = new ellipsis();
				elips.x = this.itemWidth;
				
				elips.y = g.height;
			
				elips.visible = true;
				this.addChild(elips);
			}
			
			field.width = itemWidth * field.numLines;
			
			fireRestackEvent();
			
		}
		
		private function drawBox(color):void 
		{
			this.graphics.clear();
			this.graphics.beginFill(color, 1);
			this.graphics.drawRect(boxTopLeft.x, boxTopLeft.y, itemWidth, boxHeight);
			this.graphics.endFill();
		}
		
		/**
		 * Select this EI
		 */
		public function select():void
		{
			this.isSelected = true;
			if (_responseOpt.getShortResponse() != null && _responseOpt.getShortResponse().length > 1) {
				this.content = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\" lineBreak=\"toFit\"><span fontWeight=\"bold\">" 
							+ _responseOpt.getShortResponse() + ": </span><span>" + _responseOpt.getLongResponse() + "</span></TextFlow>";
			} else {
				this.content = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\" lineBreak=\"toFit\"><span fontWeight=\"bold\">" 
							+ _responseOpt.getShortResponse() + "</span></TextFlow>";
			}
			field.tlfMarkup = content;
			field.width = fieldWidth;
			
			
			var myTextFlow:TextFlow = field.textFlow;
			myTextFlow.hostFormat = format;
			myTextFlow.flowComposer.updateAllControllers();
			
			drawBox(selectedColor);
			if (elips != null) {
				elips.visible = false;
			}
		}
		
		/**
		 * Deselect this EI
		 */
		public function deselect():void
		{
			this.isSelected = false;
			drawBox(defaultColor);
			if (elips != null) {
				elips.visible = true;
			}
			
			if (_responseOpt.getShortResponse() != null && _responseOpt.getShortResponse().length > 1) {
				this.content = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\" lineBreak=\"explicit\"><span fontWeight=\"bold\">" 
							+ _responseOpt.getShortResponse() + ": </span><span>" + _responseOpt.getLongResponse() + "</span></TextFlow>";
			} else {
				this.content = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\" lineBreak=\"explicit\"><span fontWeight=\"bold\">" 
							+ _responseOpt.getShortResponse() + "</span></TextFlow>";
			}
			field.tlfMarkup = content;
			
			field.width = fieldWidth * field.numLines;
			
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
		
		public function get responseOpt():ResponseOption
		{
			return this._responseOpt;
		}
		
		private function set responseOpt(r:ResponseOption):void 
		{
			this._responseOpt = r;
		}
	}

}