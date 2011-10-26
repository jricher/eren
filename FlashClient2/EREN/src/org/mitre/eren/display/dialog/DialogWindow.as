package org.mitre.eren.display.dialog 
{
	import fl.transitions.easing.*;
	import fl.transitions.Tween;
	import fl.transitions.TweenEvent;
	import flash.display.MovieClip;
	import flash.events.MouseEvent;
	import flash.utils.Dictionary;
	import org.as3commons.collections.framework.core.LinkedMapIterator;
	import org.as3commons.collections.LinkedMap;
	import org.mitre.eren.Constants;
	import org.mitre.eren.data.actions.Action;
	import org.mitre.eren.data.actions.Input;
	import org.mitre.eren.data.dialog.KmlLayer;
	import org.mitre.eren.data.dialog.ResponseOption;
	import org.mitre.eren.data.dialog.UserMessage;
	import org.mitre.eren.data.scenario.Person;
	import org.mitre.eren.display.dialog.expander.ExpanderItemGroup;
	import org.mitre.eren.display.dialog.expander.ExpanderItemScrollBox;
	import org.mitre.eren.display.dialog.expander.IExpanderItem;
	import org.mitre.eren.display.dialog.expander.Response_EI;
	import org.mitre.eren.display.elements.EREN_Button;
	import org.mitre.eren.display.elements.EREN_Slider;
	import org.mitre.eren.display.elements.TLFtextfieldScrollbox;
	import org.mitre.eren.events.ActionEvent;
	import org.mitre.eren.events.ExpanderItemEvent;
	import org.mitre.eren.events.MapEvent;
	import org.mitre.eren.events.MessageCenterEvent;
	import org.mitre.eren.events.SendMessageEvent;
	import org.mitre.eren.game.utils.ResourceManager;
	
	/**
	 * Code-behind for "Messages" tab. Handles displaying userMessagees, 
	 * getting responses back from the User, and passing on those 
	 * responses. This movie clip is also used to gather input from the 
	 * user when the commit an action using a facility's menu.
	 * 
	 * @see org.mitre.eren.game.utils.MessageCenterManager.as
	 * @see org.mitre.eren.game.utils.MssgQueue.as
	 * 
	 * @author Amanda Anganes
	 */
	public class DialogWindow extends MovieClip	{
		
		private var messageID:String;
		private var location:String;
		private var people:Dictionary;
		private var baseURL:String;
		private var resourceManager:ResourceManager;
		private var question_txt:TLFtextfieldScrollbox;
		
		//Group of expandable text items
		private var eig:ExpanderItemGroup;
		//EIG Scrollbox
		private var eigSB:ExpanderItemScrollBox;
		private var hasScrollBox:Boolean = false;
		
		//Slider for numeric response types
		private var slider:EREN_Slider;
		private var hasSlider:Boolean = false;
		
		//State helpers.
		private var open:Boolean = false;
		private var hasDialog:Boolean = false;
		private var doClearOut:Boolean = false;
		private var currentMessage:UserMessage;
		
		//Buttons
		private var okBttn:EREN_Button;
		private var respondBttn:EREN_Button;
		private var postponeBttn:EREN_Button;
		private var inputBttn:EREN_Button;
		private var inputBttnListener:Function;
		
		//Animation helpers
		private var showTween:Tween;
		private var timeoutTween:Tween;		
		private var timeoutVar:Number; //The value to tween when fading out/in text
		private var fadingOut:Boolean = true; //Are we fading out "message was sent" (true), or fading in "no message to display" (false)?
		
		//State variable that the MCM can check
		public var stayOpen:Boolean = false;
		
		/**
		 * Constructor. 
		 */
		public function DialogWindow() 
		{
			question_txt = new TLFtextfieldScrollbox(Constants.QUESTION_TEXT_WIDTH, Constants.QUESTION_TEXT_HEIGHT);
			addChild(question_txt);
			question_txt.x = Constants.QUESTION_TEXT_POS.x;
			question_txt.y = Constants.QUESTION_TEXT_POS.y;
			
			okBttn = getChildByName("ok_bt") as EREN_Button;
			okBttn.setContent("ok");
			okBttn.setEnabled();
			okBttn.addEventListener(MouseEvent.CLICK, oclick);
			
			postponeBttn = getChildByName("postpone_bt") as EREN_Button;
			postponeBttn.setContent("postpone");
			
			respondBttn = getChildByName("respond_bt") as EREN_Button;
			respondBttn.setContent("respond");
			respondBttn.setEnabled();
			respondBttn.addEventListener(MouseEvent.CLICK, click);
			
			inputBttn = getChildByName("input_bttn") as EREN_Button;
			inputBttn.setContent("commit");
			inputBttn.setEnabled();
			inputBttn.visible = false;
			
			slider = new EREN_Slider();
			addChild(slider);
			slider.initSlider(0, 100, 50);
			slider.x = Constants.SLIDER_POS.x;
			slider.y = Constants.SLIDER_POS.y;
			slider.visible = false;
		}
		
		/**
		 * Set the base URL of the game. The DialogWindow needs this information
		 * because incoming userMessages contain partial URLs.
		 * 
		 * @param	url the base URL of the running game
		 */
		public function setBaseURL(url:String):void 
		{
			this.baseURL = url;
		}
		
		/**
		 * Set the people dictionary. The people dictionary is a Dictionary object 
		 * containing Person objects representing all of the NPCs registered in
		 * the Scenario File. Person references are used to populate sender information
		 * from userMessages, which only contain the ID of the sender NPC. 
		 * 
		 * @see org.mitre.eren.data.scenario.Person.as
		 * @param	p an npc id-indexed dictionary of Person objects
		 */
		public function setPeopleDictionary(p:Dictionary):void 
		{
			this.people = p;
		}
		
		/**
		 * Give the DW a reference to the resource manager for the game. This
		 * is currently unused, but is intended to allow the UI to show "smart"
		 * slider bars for resource allocation that reflect the amount of
		 * a resource that is currently available.
		 * 
		 * @param	rm the ResourceManager of this game
		 */
		public function setResourceManager(rm:ResourceManager):void
		{
			this.resourceManager = rm;
		}
		
		/**
		 * Listener function for ActionEvent.GET_INPUT.
		 * Public so that the MessageCenterManager can wire this up.
		 * 
		 * @param	e 
		 */
		public function getInput(e:ActionEvent) 
		{
			trace("GetInput called; action is " + e.action.displayName);
			
			var action:Action = e.action;
			var inputs:Vector.<Input> = action.inputsVector;
			var currentInput:Input;
			
			for each (var input:Object in inputs) 
			{
				var iinput:Input = input as Input;
				if (iinput.filled == false)
				{
					currentInput = iinput;
					break;
				}
			}
			
			if (iinput.type == "integer")
			{
				displayNPC(iinput.fromId);
				
				//put up slider
				question_txt.visible = true;
				status_txt.visible = false;
				
				question_txt.changeHeight(Constants.QUESTION_TEXT_HEIGHT);
				
				var text:String = "<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\"><span>" 
						+ iinput.questionText + "</span></TextFlow>";
				question_txt.setContent(text);
				okBttn.visible = false;
				respondBttn.visible = false;
				postponeBttn.visible = false;
				
				slider.initSlider(iinput.min, iinput.max, (Math.round(iinput.min + (iinput.max - iinput.min) / 2)));
				hasSlider = true;
				slider.visible = true;
				
				inputBttn.visible = true;
				inputBttnListener = makeClosure(action, iinput, e.facilityId);
				//TODO: for the whole closure thing to really work, we need to remove
				//the old closure listener from the button before adding the new one.
				inputBttn.addEventListener(MouseEvent.CLICK, inputBttnListener, false, 0, true);
			}
		}
		
		/**
		 * Produce a listener function for a given action and input object.
		 * 
		 * @param	action
		 * @param	input
		 * @param	facId
		 * @return
		 */
		private function makeClosure(action:Action, input:Input, facId:String):Function 
		{
			var f:Function = function(e:MouseEvent):void {
				commitListener(action, input, facId);
			};
			
			return f;
		}
		
		/**
		 * This function is not used as a listener directly, but made into a closure
		 * represnting a request from the given action for a particular input. When
		 * the user makes their selection, this listener is called (with action, input,
		 * and facId filled in). The input value is set on the action object, which is
		 * then checked to see if there are more actions to process. If so, getInput
		 * is called again and the cycle repeats.
		 * 
		 * @param	action
		 * @param	input
		 * @param	facId
		 */
		private function commitListener(action:Action, input:Input, facId:String):void 
		{
			inputBttn.removeEventListener(MouseEvent.CLICK, inputBttnListener);
			input.filled = true;
			input.integerValue = slider.getValue() as int;
			
			var nextInput:Input = action.getNextUnfilledInput();
			
			if (nextInput != null) 
			{
				//There are more inputs to process
				var actionEvent:ActionEvent = new ActionEvent(ActionEvent.GET_INPUT);
				actionEvent.action = action;
				getInput(actionEvent);
			}
			else
			{
				//There are no more inputs to process, so fire commit action event
				var commitEvent:ActionEvent = new ActionEvent(ActionEvent.FIRE_ACTION);
				commitEvent.action = action;
				commitEvent.facilityId = facId;
				dispatchEvent(commitEvent);
				clearOld();
				fadeOut();
			}
		}
		
		/** Respond button listeners **/
		
		//TODO: change to have multiple kinds of UserMessage subclasses
		private function click(e:MouseEvent):void 
		{
			status_txt.text = "";
			
			if (respondBttn.isEnabled) 
			{	
				if (slider.visible == true) 
				{
					stayOpen = currentMessage.hasFollowup;
					slider.visible = false;
					hasSlider = false;
					fireSendValue(messageID, slider.getValue());
				} 
				else 
				{
					var d:LinkedMap = currentMessage.getResponseOptions();
					var r:ResponseOption = d.itemFor(getSelectedSingleItemID());
					if (r.hasFollowup()) 
					{
						stayOpen = true;
					} 
					else 
					{
						stayOpen = false;
					}
					if (currentMessage.getType() == "multi") 
					{
						fireSendMultipleResponses(messageID, getSelectedItemsIDs());
					} 
					else 
					{
						fireSendResponse(messageID, getSelectedSingleItemID());	
					}
				}
				
				status_txt.visible = true;
				// clean things out
				
				if (stayOpen == false) {				
					// go away
					trace("This message has NO FOLLOWUP message...");
					fadeOut();
				} else {
					//stay open
					//TODO: add some kind of indication
					status_txt.text = "";
					trace("This message has a FOLLOWUP message; staying open");
				}
				currentMessage = null;				
				clearOld();
				
			}
		}
		
		private function timeoutTweenFinish(e:TweenEvent):void 
		{
			if (fadingOut) 
			{
				fadingOut = false;
				status_txt.text = "No message to display";
				timeoutTween = new Tween(status_txt, "alpha", Strong.easeIn, 0, 1, .5, true);
			} 
			else 
			{
				fadingOut = true;
				timeoutTween.removeEventListener(TweenEvent.MOTION_FINISH, timeoutTweenFinish);
			}
		}
		
		/** ok button listeners **/
		
		private function oclick(e:MouseEvent):void 
		{
			status_txt.visible = true;
			status_txt.text = "";
			
			// clean things out
			if (currentMessage.hasFollowup) 
			{
				//stay open
				stayOpen = true;
				status_txt.text = "";
				//trace("This notificatoin message, id = " + currentMessage.getID() + ", HAS a FOLLOWUP message; staying open");
				okBttn.visible = true;
				respondBttn.visible = false;
				postponeBttn.visible = false;
			} 
			else 
			{	
				stayOpen = false;
				//trace("This notification message, id = " + currentMessage.getID() + ",  has NO FOLLOWUP message...");
				fadeOut();
			}
			
			//Send empty response
			fireSendResponse(currentMessage.getID(), "");
			currentMessage = null;
			clearOld();
		}
		
		/**
		 * Display a given UserMessage. The MessageCenterManager calls this method
		 * when it recieves a click notification from any Notification icon.
		 * 
		 * @param	m the UserMessage to display
		 */	
		public function displayMessage(m:UserMessage):void 
		{
			trace("--------------------Displaying message with id: " + m.getID() + "----------------------------------");
			/** Clean things up before we deal with the new message **/
			
			status_txt.text = "";
			status_txt.visible = false;
			question_txt.visible = true;
			hasDialog = true;
			
			if (this.currentMessage != null) 
			{
				// if we've got a dialog that we haven't answered yet, then we need to dehighlight it
				fireDeselected(currentMessage);
			}
			
			this.currentMessage = m;
			clearOld();
			
			/** Now we are ready to deal with the new message to display **/
			
			messageID = m.getID();
			location = m.getLocation();
			
			var responses:LinkedMap = m.getResponseOptions();
			var i:int;
			
			if (m.hasKML) 
			{
				fireDisplayKML(m.getKmlLayers());
			}
			
			if (m.getLocation() != null) 
			{
				fireFlyTo(m.getLocation());
			}
			
			if (m.getType() == "integer") 
			{
				trace("Got numeric response message");
				//display slider instead
				question_txt.changeHeight(Constants.QUESTION_TEXT_HEIGHT);
				question_txt.setContent(m.getText());
				okBttn.visible = false;
				respondBttn.visible = true;
				respondBttn.setEnabled();
				postponeBttn.visible = false;
				
				slider.initSlider(m.minValue, m.maxValue, (Math.round(m.minValue + (m.maxValue - m.minValue) / 2)));
				hasSlider = true;
				slider.visible = true;
				
			} 
			else if (m.getType() == "single" || m.getType() == "multi")
			{
				
				trace("Got standard user message!");
				question_txt.changeHeight(Constants.QUESTION_TEXT_HEIGHT);
				question_txt.setContent(m.getText());
				okBttn.visible = false;
				respondBttn.visible = true;
				respondBttn.setDisabled();
				postponeBttn.visible = false;
				
				var iterator:LinkedMapIterator = responses.iterator() as LinkedMapIterator;
				
				//Initialize ExpanderItemGroup
				eig = new ExpanderItemGroup(Constants.DIALOG_EXPANDER_WIDTH, ExpanderItemGroup.SINGLE);
				eig.addEventListener(ExpanderItemEvent.SELECTION_EVENT, onSelectionEvent);
				
				//Create TextExpanderItems for each ResponseOption, and add it to the EIG
				while (iterator.hasNext()) 
				{
					var r:ResponseOption = iterator.next() as ResponseOption;
					var item:Response_EI = new Response_EI(Constants.DIALOG_EXPANDER_WIDTH, 5, r);
					eig.addItem(item);	
				}
				
				//When done adding TEIs, create the ExpanderItemScrollBox and add it to the stage
				eigSB = new ExpanderItemScrollBox(Constants.DIALOG_SCROLLBOX_SIZE, 
									Constants.DIALOG_SCROLLBAR_WIDTH, eig, Constants.DIALOG_SCROLLBOX_BUFFER, stage); 
				eigSB.x = Constants.DIALOG_SCROLLBOX_POS.x;
				eigSB.y = Constants.DIALOG_SCROLLBOX_POS.y;
				
				if (m.getType() == "multi") 
				{
					eigSB.eig.type = ExpanderItemGroup.MULTI;
				} 
				else
				{
					eigSB.eig.type = ExpanderItemGroup.SINGLE;
				}
				
				addChild(eigSB);
				hasScrollBox = true;
				
			}
			else if (m.getType() == "notify") 
			{
				//If there are no responses, this is just a notification and we should display an OK button
				//rather than a list of options and a respond.
				trace("Got notification message!");
				question_txt.changeHeight(Constants.NOTIFICATION_TEXT_HEIGHT);
				question_txt.setContent(m.getText());
				okBttn.visible = true;
				respondBttn.visible = false;
				postponeBttn.visible = false;
			} 
			else
			{
				trace("ERROR --------------------- incompatible message type, type is: " + m.getType());
			}
			
			displayNPC(m.getNpcId());
			
		}
		
		private function onSelectionEvent(e:ExpanderItemEvent):void 
		{
			if (e._selected) 
			{
				respondBttn.setEnabled();
			} 
			else 
			{
				respondBttn.setDisabled();
			}
		}
		
		/** Events **/
		
		private function fireFlyTo(facID:String):void 
		{
			var me:MapEvent = new MapEvent(MapEvent.FLY_TO_LOCATION);
			me._facID = facID;
			dispatchEvent(me);
		}
		
		private function fireDisplayKML(k:Vector.<KmlLayer>):void 
		{
			var me:MapEvent = new MapEvent(MapEvent.DISPLAY_KML);
			me._kmlLayers = k;
			dispatchEvent(me);
		}
		
		private function fireSendResponse(mID:String, rID:String):void 
		{
			var sme:SendMessageEvent = new SendMessageEvent(SendMessageEvent.SINGLE_RESPONSE);
			sme._messageID = mID;
			sme._singleResponseID = rID;
			dispatchEvent(sme);
		}
		
		private function fireSendMultipleResponses(mID:String, responseIDs:Vector.<String>)
		{
			var sme:SendMessageEvent = new SendMessageEvent(SendMessageEvent.MULTI_RESPONSE);
			sme._messageID = mID;
			sme._responseIDs = responseIDs;
			dispatchEvent(sme);
		}
		
		private function fireSendValue(mID:String, val:Number):void 
		{
			var sme:SendMessageEvent = new SendMessageEvent(SendMessageEvent.VALUE_RESPONSE);
			sme._messageID = mID;
			sme._value = val;
			dispatchEvent(sme);
			dispatchEvent(sme);
		}
		
		private function fireDeselected(u:UserMessage):void 
		{
			var mce:MessageCenterEvent = new MessageCenterEvent(MessageCenterEvent.DESELECT_NOTIFICATION);
			mce._userMssg = u;
			dispatchEvent(mce);
		}
		
		/** Utilities **/
		
		/** Clear old display items **/
		private function clearOld():void 
		{
			hasDialog = false;
			if (hasScrollBox) 
			{
				removeChild(eigSB); //Hanging question - do we need to do more to ensure garbage collection of TEIs?
				hasScrollBox = false;
			}
			if (slider.visible = true) 
			{
				slider.visible = false;
				hasSlider = false;
			}
			avatar_mc.displayImage("resources/images/Untitled.png");
			name_txt.text = "";
			occupation_txt.text = "";
			question_txt.setContent("");
		}
		
		/** Fade out the dialog window **/
		private function fadeOut() 
		{
			question_txt.visible = false;
			status_txt.visible = true;
			status_txt.text = "Message was sent";
			trace("Message was sent; fading out");
			timeoutTween = new Tween(status_txt, "alpha", Strong.easeIn, 1, 0, 1.5, true);
			fadingOut = true;
			timeoutTween.addEventListener(TweenEvent.MOTION_FINISH, timeoutTweenFinish);
			okBttn.visible = false;
			respondBttn.visible = false;
			postponeBttn.visible = false;
			inputBttn.visible = false;
		}		
		
		/** Display all of the "from" fields for an NPC: image, name, job info, etc **/
		private function displayNPC(personId:String) 
		{
			var person:Person = people[personId];
			var url:String = baseURL + person.imageURL;
			
			avatar_mc.displayImage(url);
			name_txt.text = person.firstName + person.lastName;
			occupation_txt.text = person.npcRole;
		}
		
		/** For single selection, return the single selected item. **/
		private function getSelectedSingleItemID():String 
		{
			var r:Response_EI = eigSB.eig.getSelected()[0] as Response_EI;
			return r.responseOpt.getID();
		}
		
		/** For multi selection, return a vector of selected items. **/
		private function getSelectedItemsIDs():Vector.<String>
		{
			var REIs:Vector.<IExpanderItem> = eigSB.eig.getSelected() as Vector.<IExpanderItem>;
			var retVec:Vector.<String> = new Vector.<String>();
			
			for each (var i:IExpanderItem in REIs) 
			{
				var r:Response_EI = i as Response_EI;
				retVec.push(r.responseOpt.getID());
			}
			
			return retVec;
		}
	}

}