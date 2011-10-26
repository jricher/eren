package org.mitre.eren.dialogue.custom;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;
import org.mitre.eren.dialogue.DialogueManager;
import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WaitTimer extends Action implements DialogueConstants {

  /**
   *  the amount of time to wait, in seconds of *game time* 
   */
  int time;
  /**
   * The name of the variable that is mapped to the owner of the timer in the execution context
   */
  String ownerVar;
  /**
   * The variable that specifies the event to fire when the timer is done
   */
  String event;
  
  /**
   * @return the event
   */
  public String getEvent() {
    return event;
  }

  /**
   * @param event the event to set
   */
  public void setEvent(String event) {
    this.event = event;
  }

  /**
   * @return the time
   */
  public int getTime() {
    return time;
  }

  /**
   * @param time the time to set
   */
  public void setTime(int time) {
    this.time = time;
  }

   
  /**
   * @return the ownerVar
   */
  public String getOwnerVar() {
    return ownerVar;
  }

  /**
   * @param ownerVar the ownerVar to set
   */
  public void setOwnerVar(String ownerVar) {
    this.ownerVar = ownerVar;
  }

  public WaitTimer() {
    super();
  }

  @Override
  public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
      SCInstance scInstance, Log appLog, Collection derivedEvents)
      throws ModelException, SCXMLExpressionException {
    
    Context ctx = scInstance.getContext(getParentTransitionTarget()); 
    final String source = (String) ctx.get(getOwnerVar());
    
    final DialogueManager dm = (DialogueManager) scInstance.getRootContext().get("dm");
    dm.queueDelayedEvent(event, source, time);
  }

}
