package org.mitre.eren.protocol.dialogue;

import org.apache.abdera.util.AbstractExtensionFactory;
import org.mitre.eren.protocol.scenario.Location;


public class DialogueExtensionFactory extends AbstractExtensionFactory implements DialogueConstants {

  
  public DialogueExtensionFactory() {
    super(DLG_NS);
    addImpl(DLG_USERMESSAGE, UserMessage.class);
    addImpl(DLG_LOCATION, Location.class);
    addImpl(DLG_RESPONSEOPTION, ResponseOption.class);
    addImpl(DLG_USERRESPONSE, UserResponse.class);
    addImpl(DLG_SENDER, Sender.class);
    addImpl(DLG_OPENPOD, OpenPod.class);
    addImpl(DLG_KMLLAYER, KMLLayer.class);
    addImpl(DLG_KMLURL, KMLURL.class);
    addImpl(DLG_RESPONSERANGE, ResponseRange.class);
  }


}
