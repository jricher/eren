package org.mitre.eren.protocol.dialogue;

import javax.xml.namespace.QName;

import org.mitre.eren.protocol.ERENConstants;

public interface DialogueConstants extends ERENConstants {

  public static final String DLG_NS = "urn:mitre:eren:dlg:1.0";
  public static final String SCXML_NS = "http://www.w3.org/2005/07/scxml";
  
  public static final String DLG_PFX = "dlg";
  public static final String SCXML_PFX = "scxml";
  
  public static final QName DLG_ERENMESSAGE = new QName(DLG_NS,"erenMessage",DLG_PFX);
  
  
  public static final QName DLG_MESSAGETEXT = new QName(DLG_NS,"messageText",DLG_PFX); 
  public static final QName DLG_MESSAGEID = new QName(DLG_NS,"messageID",DLG_PFX);
  public static final QName DLG_PRECEDINGMESSAGEID = new QName(DLG_NS,"precedingMessageID",DLG_PFX);
  public static final QName DLG_USERMESSAGE = new QName(DLG_NS,"userMessage",DLG_PFX);
  public static final QName DLG_RESPONSEOPTION = new QName(DLG_NS,"responseOption",DLG_PFX);
  public static final QName DLG_RESPONSEID = new QName(DLG_NS,"responseID",DLG_PFX);
  public static final QName DLG_RESPONSEVALUE = new QName(DLG_NS,"responseValue",DLG_PFX);
  public static final QName DLG_RESPONSERANGE = new QName(DLG_NS,"responseRange",DLG_PFX);
  public static final QName DLG_RESPONSEMIN = new QName(DLG_NS,"responseMin",DLG_PFX);
  public static final QName DLG_RESPONSEMAX = new QName(DLG_NS,"responseMax",DLG_PFX);
  public static final QName DLG_SHORTRESPONSE = new QName(DLG_NS,"shortResponse",DLG_PFX);
  public static final QName DLG_LONGRESPONSE = new QName(DLG_NS,"longResponse",DLG_PFX);
  public static final QName DLG_SUMMARY = new QName(DLG_NS,"summary",DLG_PFX);
  public static final QName DLG_TIMEOUT = new QName(DLG_NS,"timeout",DLG_PFX);
  public static final QName DLG_PRIORITY = new QName(DLG_NS,"priority",DLG_PFX);
  public static final QName DLG_MESSAGETYPE = new QName(DLG_NS,"messageType",DLG_PFX);
  public static final QName DLG_LOCATION = new QName(DLG_NS,"location",DLG_PFX);
  public static final QName DLG_SENDER = new QName(DLG_NS,"sender",DLG_PFX);
  public static final QName DLG_FOLLOWUP = new QName(DLG_NS,"followup",DLG_PFX);
  public static final QName DLG_PHOTOURL = new QName(DLG_NS,"photoURL",DLG_PFX);
  
  public static final QName DLG_OPENPOD = new QName(DLG_NS,"openPod",DLG_PFX);
  public static final QName DLG_PODID = new QName(DLG_NS,"podId",DLG_PFX);
  public static final QName DLG_TIMESTAMP = new QName(DLG_NS,"timestamp",DLG_PFX);
  
  public static final QName DLG_USERRESPONSE = new QName(DLG_NS,"userResponse",DLG_PFX);
  public static final QName DLG_ADDRESSEE = new QName(DLG_NS,"addressee",DLG_PFX);

  public static final QName DLG_KMLLAYER = new QName(DLG_NS, "kmlLayer",DLG_PFX);
  public static final QName DLG_KMLURL = new QName(DLG_NS, "kmlurl",DLG_PFX);
  public static final QName DLG_LAYERID = new QName(DLG_NS, "layerID",DLG_PFX);
  

  
  public static final QName SCXML_OUTGOING = new QName(SCXML_NS,"outgoing",SCXML_PFX);

} 
