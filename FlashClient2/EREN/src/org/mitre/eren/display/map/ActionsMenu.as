package org.mitre.eren.display.map 
{
	import com.google.maps.services.DirectionsEvent;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import org.mitre.eren.data.actions.Action;
	import org.mitre.eren.events.ActionEvent;
	import org.mitre.eren.events.ErenEvent;
	
	/**
	 * An ActionsMenu contains a set of MenuOptions. Each menu is linked to a specific facility,
	 * so it has a reference to the facility's id. When the ActionsFile is parsed, the ResourceManager
	 * creates an ActionsMenu for each facility (if it has actions). 
	 * 
	 * @author Amanda Anganes
	 */
	public class ActionsMenu extends MovieClip 
	{
		private var menuOptions:Vector.<MenuOption>;
		private var maxWidth:Number = 0;
		public var facilityId:String = "";
		
		/**
		 * Constructor
		 * 
		 * @param	facilityID id of the facility this menu is linked to
		 */
		public function ActionsMenu(facilityID:String) 
		{
			menuOptions = new Vector.<MenuOption>();
			this.facilityId = facilityID;
		}
		
		/**
		 * Create and add a new MenuOption to the menu
		 * 
		 * @param	action  the action associated with this option
		 * @param	enabled whether this option is currently enabled or not
		 */
		public function addOption(action:Action, enabled:Boolean) 
		{
			var opt:MenuOption = new MenuOption(action, enabled, facilityId, menuOptions.length);
			opt.x = 0;
			this.addChild(opt);
			opt.addEventListener(MouseEvent.CLICK, fireAction);
			opt.addEventListener(ErenEvent.READY, optionReady);
			menuOptions.push(opt);
		}
		
		/**
		 * Handler for when a new MenuOption is ready. Place it within the menu and add it
		 * to the display list.
		 * 
		 * @param	e
		 */
		private function optionReady(e:Event):void 
		{
			trace("ActionsMenu - option is ready, placing");
			(e.target as MenuOption).removeEventListener(ErenEvent.READY, optionReady);
			var option:MenuOption = e.target as MenuOption;
			option.x = 0;
			option.y = 0 - (option.index * option.height); 
			if (option.width > maxWidth) 
			{
				//redraw
				maxWidth = option.width;
				for each (var opt:MenuOption in menuOptions)
				{
					opt.redraw(maxWidth);
				}
			}
		}
		
		/**
		 * Click listener for menu options. When an option is clicked on, if it is enabled,
		 * fire an ActionEvent. If the associated action requires input from the user, fire
		 * ActionEvent.GET_INPUT, otherwise just fire ActionEvent.FIRE_ACTION.
		 * 
		 * @param	e
		 */
		private function fireAction(e:MouseEvent):void
		{
			if ((e.target as MenuOption).isEnabled) 
			{
				var action:Action = (e.target as MenuOption).action;
				var evt:ActionEvent;
				if (action.inputsVector.length >= 1) 
				{
					evt = new ActionEvent(ActionEvent.GET_INPUT);
				}
				else 
				{
					evt = new ActionEvent(ActionEvent.FIRE_ACTION);
				}
				evt.action = action;
				evt.facilityId = facilityId;
				dispatchEvent(evt);
			}
		}
		
		/**
		 * Enable or disable the option with the given ID.
		 * 
		 * @param	enabled true = enable, false = disable
		 * @param	id      the ID of the option to enable/disable
		 */
		public function enableOption(enabled:Boolean, id:String) 
		{
			for each (var mo:MenuOption in menuOptions) 
			{
				if (mo.action.type == id) 
				{
					mo.isEnabled = enabled;
				}
			}
		}
		
	}

}