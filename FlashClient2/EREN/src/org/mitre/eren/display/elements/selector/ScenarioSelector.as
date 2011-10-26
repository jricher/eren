package org.mitre.eren.display.elements.selector 
{
	import flash.geom.Point;
	import org.mitre.eren.data.scenario.ScenarioListItem;
	import org.mitre.eren.Constants;
	import fl.text.TLFTextField;
	import flash.text.TextFieldAutoSize;
	
	/**
	 * Scenario selection extension of SelectorBar. Contains
	 * a ScenarioListItem.
	 * 
	 * @author Amanda Anganes
	 */
	public class ScenarioSelector extends SelectorBar 
	{
		
		public var scenario:ScenarioListItem;
		
		/**
		 * Constructor. Set size but do not draw yet.
		 */
		public function ScenarioSelector() 
		{
			super();
			this.size = new Point(408, 20);;
			super.selectedFormat.size = 12;
			super.deselectedFormat.size = 12;
		}
		
		/**
		 * Set the ScenarioListItem and index associated with this bar, and 
		 * construct visuals.
		 * 
		 * @param	s the ScenarioListItem associated with this selector bar
		 * @param	index the index of this selector bar
		 */
		public function build(s:ScenarioListItem, index:int) 
		{
			this.index = index;
			this.scenario = s;
			
			var nameBox:TLFTextField = new TLFTextField();
			nameBox.x = Constants.SCENARIONAME_START.x;
			nameBox.y = Constants.SCENARIONAME_START.y;
			nameBox.text = s.name;
			nameBox.autoSize = TextFieldAutoSize.LEFT;
			nameBox.selectable = false;
			this.fields.push(nameBox);
			this.addChild(nameBox);
			
			var durationBox:TLFTextField = new TLFTextField();
			durationBox.x = Constants.SCENARIODURATION_START.x;
			durationBox.y = Constants.SCENARIODURATION_START.y;
			durationBox.text = s.duration;
			durationBox.autoSize = TextFieldAutoSize.LEFT;
			durationBox.selectable = false;
			this.fields.push(durationBox);
			this.addChild(durationBox);
			
			var gametimeBox:TLFTextField = new TLFTextField();
			gametimeBox.x = Constants.SCENARIOGAMETIME_START.x;
			gametimeBox.y = Constants.SCENARIOGAMETIME_START.y;
			gametimeBox.text = s.gametime;
			gametimeBox.autoSize = TextFieldAutoSize.LEFT;
			gametimeBox.selectable = false;
			this.fields.push(gametimeBox);
			this.addChild(gametimeBox);
		}
		
	}

}