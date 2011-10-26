package org.mitre.eren.dialogue;

import javax.xml.namespace.QName;

import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;

public enum ResourceMessageType implements EDXLRMConstants {
  COMMIT_RESOURCE ("commit.resource",RM_COMMITRESOURCE),
  REPORT_STATUS ("status.report",RM_REPORTRESOURCEDEPLOYMENTSTATUS),
  REQUISITION_RESOURCE ("requisition.resource",RM_REQUISITIONRESOURCE),
  REQUEST_RESOURCE ("request.resource",RM_REQUESTRESOURCE),
  RELEASE_RESOURCE ("release.resource", RM_RELEASERESOURCE),
  REQUEST_RETURN ("request.return", RM_REQUESTRETURN);
  
  String event;
  QName qname;
  private ResourceMessageType(String event,QName qname) { 
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
  
  public static ResourceMessageType get (String event) { 
    for (ResourceMessageType rmt : values()) { 
      if (rmt.toString().equals(event)) { 
        return rmt;
      }
    }
    return null;
  }
  
  public String toString () { 
    return event;
  }
  
  public static ResourceMessageType getMessageByQName (QName qname) { 
    for (ResourceMessageType type : values()) { 
      if (type.getQname().equals(qname)) { 
        return type;
      }
    }
    return null;
  }
  
  
}
