package org.mitre.eren.dialogue;

import javax.xml.namespace.QName;

import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.mitre.eren.protocol.startup.StartupConstants;

public enum MessageType implements DialogueConstants,StartupConstants {
  // message types
  USERINPUT_TYPE ("user.input",DLG_USERMESSAGE),
  SHUTDOWN_TYPE ("shutdown",EREN_LOGOUT), // we need a real shutdown message
  STARTUP_TYPE ("game.start",EREN_GAMESTART);
  
  String event;
  QName qname;
  private MessageType (String event,QName qname) { 
    this.event = event;
    this.qname = qname;
  }
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
   * @return the qname
   */
  public QName getQname() {
    return qname;
  }
  /**
   * @param qname the qname to set
   */
  public void setQname(QName qname) {
    this.qname = qname;
  }
  
  public String toString () { 
    return event;
  }
  
  

  
}
