package org.mitre.eren.display.elements.selector 
{
	import flash.geom.Point;
	import org.mitre.eren.data.scenario.RoleObject;
	import org.mitre.eren.display.elements.selector.SelectorBar;
	import fl.text.TLFTextField;
	import org.mitre.eren.Constants;
	import flash.text.TextFieldAutoSize;
	
	/**
	 * Role selection extension of SelectorBar. Contains a 
	 * RoleObject.
	 * 
	 * @author Amanda Anganes
	 */
	public class RoleSelector extends SelectorBar 
	{
		
		public var role:RoleObject;
		
		/**
		 * Constructor. Initialize size but do not draw yet.
		 */
		public function RoleSelector() 
		{
			super();
			this.size = new Point(616, 24);
		}
		
		/**
		 * Set the RoleObject and index, and construct the visuals.
		 * 
		 * @param	r the RoleObject represented by this selector bar
		 * @param	index the index of this selector bar
		 */
		public function build(r:RoleObject, index:int) 
		{
			var titleBox:TLFTextField = new TLFTextField();
			titleBox.x = Constants.ROLETITLE_START.x;
			titleBox.y = Constants.ROLETITLE_START.y;
			titleBox.text = r.title;
			titleBox.autoSize = TextFieldAutoSize.LEFT;
			titleBox.selectable = false;
			
			this.addChild(titleBox);
			
			fields.push(titleBox);
			
			this.index = index;
			this.role = r;
		}	
	}

}