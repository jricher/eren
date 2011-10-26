package org.mitre.eren.game.utils
{
	
	import fl.transitions.easing.Strong;
	import fl.transitions.Tween;
	import flash.display.MovieClip;
	import flash.events.MouseEvent;
	import org.mitre.eren.Constants;
	import org.mitre.eren.data.dialog.UserMessage;
	import org.mitre.eren.display.dialog.notifications.DireNotify;
	import org.mitre.eren.display.dialog.notifications.NormalNotify;
	import org.mitre.eren.display.dialog.notifications.Notification;
	import org.mitre.eren.display.dialog.notifications.WarningNotify;
	import org.mitre.eren.events.MessageCenterEvent;
	import org.mitre.eren.game.Main;
	
	/**
	 * MssgQueue manages the queue of pending UserMessages. As messages are
	 * added to the queue, they are given notification icons corresponding 
	 * to the priority of the message. Messages stack as they come in, and 
	 * when a message is finished, other messages in the queue will slide
	 * down visually.
	 * 
	 * @see org.mitre.eren.game.utils.MessageCenterManager.as
	 * @see org.mitre.eren.display.dialog.notification.Notification.as
	 * 
	 * @author Amanda Anganes
	 **/ 
	public class MssgQueue extends MovieClip
	{
		//The queue is an array of notification icons, which contain their
		//own usermessages
		private var queue:Array; 
		
		private var myMain:Main;
		
		/** Constructor **/
		public function MssgQueue()
		{
			
			queue = new Array();
		}
		
		/**
		 * Set the reference to Main. This is needed because notification 
		 * icons are added as children of Main. TODO: this probably is not necessary;
		 * add items as children of this directly.
		 * 
		 * @param	m
		 */
		public function setMain(m:Main): void 
		{
			this.myMain = m;
		}
		
		/**
		 * Add an item to the queue.
		 * 
		 * @param	mssg             the message to display
		 * @param	checkForFollowup true if this message claims to be a followup message
		 */
		public function add(mssg:UserMessage, checkForFollowup:Boolean = true):void 
		{	
			//if this is a followup message, do not display a notification icon.  Just
			//tell the dialog window to display
			if (checkForFollowup && mssg.hasPreceding) 
			{
				//TOOD: Should we somehow check here whether the previously displayed message id
				//matches the preceding mssg id on this new mssg?
				fireDisplayMssg(mssg);
			}
			else 
			{				
				//Add the new notification icon
				var notify:Notification;
				
				if (mssg.getPriority() == Constants.PRIORITY_NORMAL) 
				{	
					notify = new NormalNotify(mssg.getSummary(), mssg);
				}
				else if (mssg.getPriority() == Constants.PRIORITY_WARNING) 
				{
					notify = new WarningNotify(mssg.getSummary(), mssg);
				} 
				else if (mssg.getPriority() == Constants.PRIORITY_DIRE) 
				{
						notify = new DireNotify(mssg.getSummary(), mssg);
				}
				else 
				{
					trace("notification does not equal any of my priorities! =" + mssg.getPriority());
				}
				
				notify.addEventListener(MouseEvent.CLICK, this.onClick);
				notify.x = Constants.NOTIFICATION_STARTX;
				notify.y = Constants.NOTIFICATION_STARTY - 
							((notify.height + Constants.NOTIFICATION_BUFFER) * queue.length);
				
				if (myMain == null) 
				{
					trace("main is null!");
				}
				//TODO: change to add children directly to mssgqueue
				myMain.addChild(notify);
				notify.updateContent();
				queue.push(notify);
			}
		}
		
		/**
		 * Remove an item from the queue
		 * 
		 * @param	n the Notification item to remove
		 */
		public function remove(n:Notification):void 
		{
			//handle removal
			var found:Boolean = false;
			var i:int = -1;
			for each (var nd in queue) 
			{
				if (n == nd) 
				{
					i++;
					found = true;
					myMain.removeChild(n);
				} 
				else if (found) 
				{
					//Tween other messages downwards
					var t:Tween = new Tween(nd, "y", Strong.easeOut, nd.y, 
									nd.y + (n.height + Constants.NOTIFICATION_BUFFER), 0.5, true);
					t.start();
				} 
				else 
				{
					i++;
				}
			}
			if (found) 
			{
				queue.splice(i, 1);
			}
		}
		
		/**
		 * Find a notification icon by its unique ID
		 * 
		 * @param	id the ID of the icon to find
		 * @return the found icon, or null if none was found
		 */
		public function findByID(id:String):Notification 
		{
			//Remove a notification icon from the queue by looking up its id
			trace("finding notification by id: " + id);
			for each (var nd in queue) 
			{
				var nID:String = (nd as Notification).message.getID();
				if (nID == id) 
				{
					//found
					return (nd as Notification);
				}
			}
			return null;
		}
		
		/**
		 * Deselect the notification.
		 * 
		 * @param	n the Notification to deselect
		 */
		public function deselectNotification(n:Notification):void 
		{
			n.revert();
		}
		
		/**
		 * Get the length of the queue
		 * 
		 * @return the length of the queue
		 */
		public function getNumMssgs():Number 
		{
			return queue.length;
		}
		
		private function onClick(e:MouseEvent):void 
		{
			var sender:Notification = e.currentTarget as Notification;
			sender.highlight();
			fireDisplayMssg(sender.message);
		}
		
		private function fireDisplayMssg(mssg:UserMessage):void 
		{
			var mce:MessageCenterEvent = new MessageCenterEvent(MessageCenterEvent.DISPLAY_USER_MSSG);
			mce._userMssg = mssg;
			dispatchEvent(mce);
		}
	}
}