package org.mitre.eren.dialogue;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.scxml.EventDispatcher;


public class DialogueDispatcher implements EventDispatcher {

  Logger log = Logger.getLogger(getClass().getName());
  DialogueManager dm;

  public DialogueDispatcher(DialogueManager dm) {
    super();
    this.dm = dm;
  }

  /* (non-Javadoc)
   * @see org.apache.commons.scxml.EventDispatcher#cancel(java.lang.String)
   */
  @Override
  public void cancel(String sendId) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.apache.commons.scxml.EventDispatcher#send(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Map, java.lang.Object, long, java.util.List)
   */
  @Override
  public void send(String sendId, String target, String targetType,
      String event, Map params, Object hints, long delay, List externalNodes) {
    // TODO Auto-generated method stub
    log.info("Sending event " + event + " to target " + target + " with payload " + params);
	  if (delay == 0) { 
		  dm.queueEvent(event,params,target);	  
	  } else { 
		  dm.queueDelayedEvent(event, params, target, delay);
	  }
    
  }

}
