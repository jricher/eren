package org.mitre.eren.display.elements.selector 
{
	import org.mitre.eren.data.game.Game;
	import org.mitre.eren.display.elements.selector.SelectorBar;
	import org.mitre.eren.Constants;
	import fl.text.TLFTextField;
	import flash.text.TextFieldAutoSize;
	
	/**
	 * Game selection extension of SelectorBar. Holds a game object
	 * and specific text fields for displaying game information.
	 * 
	 * @author Amanda Anganes
	 */
	public class GameSelector extends SelectorBar 
	{
		
		public var game:Game;
		private var playersBox:TLFTextField
		
		/**
		 * Constructor. Wait to do any drawing.
		 */
		public function GameSelector() 
		{
			super();
		}
		
		/**
		 * Set the Game and the index for this selector bar, and build the
		 * visuals.
		 * 
		 * @param	g Game object associated with this bar
		 * @param	index index of the bar
		 */
		public function build(g:Game, index:int) 
		{
			///trace("Building game selector with index " + index);
			this.game = g;
			this.index = index;
			
			var nameBox:TLFTextField = new TLFTextField();
			nameBox.x = Constants.GAMENAME_START.x;
			nameBox.y = Constants.GAMENAME_START.y;
			nameBox.text = g.gameName;
			nameBox.autoSize = TextFieldAutoSize.LEFT;
			nameBox.selectable = false;
			fields.push(nameBox);
			this.addChild(nameBox);
			
			var durationBox:TLFTextField = new TLFTextField();
			durationBox.x = Constants.GAMEDURATION_START.x;
			durationBox.y = Constants.GAMEDURATION_START.y;
			durationBox.text = g.scenario.duration;
			durationBox.autoSize = TextFieldAutoSize.LEFT;
			durationBox.selectable = false;
			fields.push(durationBox);
			this.addChild(durationBox);
			
			var gametimeBox:TLFTextField = new TLFTextField();
			gametimeBox.x = Constants.GAMEGAMETIME_START.x;
			gametimeBox.y = Constants.GAMEGAMETIME_START.y;
			gametimeBox.text = g.scenario.gametime;
			gametimeBox.autoSize = TextFieldAutoSize.LEFT;
			gametimeBox.selectable = false;
			fields.push(gametimeBox);
			this.addChild(gametimeBox);
			
			playersBox = new TLFTextField();
			playersBox.x = Constants.GAMEPLAYERS_START.x;
			playersBox.y = Constants.GAMEPLAYERS_START.y;
			playersBox.text = g.getActivePlayers() + " / " + game.getMinPlayers();
			playersBox.autoSize = TextFieldAutoSize.LEFT;
			playersBox.selectable = false;
			fields.push(playersBox);
			this.addChild(playersBox);
		}
		
		/**
		 * Display updated current/min/max player numbers
		 * 
		 * @param	newgame Game object containing new info
		 */
		public function updatePlayers(newgame:Game) 
		{
			playersBox.text = newgame.getActivePlayers() + " / " + newgame.getMinPlayers();
			this.game = newgame;
		}
		
	}

}