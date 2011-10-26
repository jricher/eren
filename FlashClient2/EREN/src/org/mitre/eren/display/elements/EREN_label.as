package org.mitre.eren.display.elements 
{
	import flash.display.DisplayObject;
	import flash.display.MovieClip;
	import flash.events.MouseEvent;
	import flash.geom.Rectangle;
	import flash.text.Font;
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	import flash.text.TextFieldType;
	import flash.text.TextFormat;
	
	/**
	 * Custom label class for EREN
	 * !!
	 * @author Amanda Anganes
	 */
	public class EREN_label extends MovieClip
	{
		
		private var content_txt:TextField;
		private var delegate:DisplayObject;
		
		public function EREN_label(s:String) 
		{
			//create new font object
			
			//Initialize the content textfield
			content_txt = new TextField();
			content_txt.autoSize = TextFieldAutoSize.LEFT;
			content_txt.wordWrap = false;
			content_txt.selectable = false;
			content_txt.type = TextFieldType.DYNAMIC;
			
			//Set the text format so that we can use the embedded font
			var myFormat:TextFormat = new TextFormat();
			//myFormat.font = "Ebrima";
			myFormat.size = 16;
			myFormat.leftMargin = 4;
			myFormat.rightMargin = 4;
			content_txt.defaultTextFormat = myFormat;
			//content_txt.embedFonts = true;
			
			//Show the label's bounding box
			content_txt.background = true;
			content_txt.border = true;
			
			content_txt.x = 0;
			content_txt.y = 0;
			//content_txt.width = 100;
			//content_txt.height = 100;
			
			content_txt.text = s;
			//content_txt.htmlText = s;
			//content_txt.text = "Your Mom!";
			
			this.addChild(content_txt);
			
			addEventListener(MouseEvent.CLICK, clickDelegate);
		}
		
		//Change the content of the label
		public function setContent(s:String):void {
			this.content_txt.text = s;
		}
		
		public function setDelegate(d:DisplayObject):void {
			this.delegate = d;

		}
		
		public function clickDelegate(e:MouseEvent):void {
			if (this.delegate != null) {
				this.delegate.dispatchEvent(e);
			}
		}
	}


}