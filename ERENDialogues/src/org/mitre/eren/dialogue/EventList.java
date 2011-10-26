package org.mitre.eren.dialogue;

import java.util.ArrayList;
import java.util.LinkedList;

public class EventList extends LinkedList<DialogueEvent> {
  
  /**
   * 
   */
  private static final long serialVersionUID = -6151079478158120262L;

  public boolean addEvent (String event) { 
    return addEvent(event,null);
  }
  
  public boolean addEvent (String event, Object payload) { 
    DialogueEvent e = new DialogueEvent(event,payload);
    return this.add(e);
  }
  
  public boolean hasEvent (String event) { 
    for (DialogueEvent ev : this) { 
      if (ev.getEvent().equals(event)) return true;
    }
    return false;
  }
  
  public DialogueEvent getEvent (String event) { 
    for (DialogueEvent ev : this) { 
      if (ev.getEvent().equals(event)) return ev;
    }
    return null;
  }
  
  public boolean removeEvent (String event) { 
    for (DialogueEvent ev : this) { 
      if (ev.getEvent().equals(event)) return this.remove(ev);
    }
    return false;
  }
 
  public boolean removeEvent (DialogueEvent event) { 
    return this.remove(event);
}  
  

}
