package org.mitre.eren.dialogue.custom;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.State;
import org.mitre.eren.dialogue.DMConstants;
import org.mitre.eren.dialogue.DialogueManager;

public class DuplicateSC extends Action implements DMConstants {
  
  private String id;
  

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }


  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }


  @Override
  public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
      SCInstance scInstance, Log appLog, Collection derivedEvents)
      throws ModelException, SCXMLExpressionException {
    
    DialogueManager dm = (DialogueManager) scInstance.getRootContext().get("dm");
    String scID = (String) scInstance.getRootContext().get("scID");
    Context ctx = scInstance.getContext(getParentTransitionTarget()); 
    
    dm.dialogueStarted(scID);
    dm.createStateMachine(id);
  }

}
