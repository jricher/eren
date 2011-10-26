package org.mitre.eren.dialogue;

public class DialogueEvent {

  private String event;
  private Object payload;
  
  public DialogueEvent(String event, Object payload) {
    super();
    this.event = event;
    this.payload = payload;
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
   * @return the payload
   */
  public Object getPayload() {
    return payload;
  }
  /**
   * @param payload the payload to set
   */
  public void setPayload(Object payload) {
    this.payload = payload;
  }
  
  
}
