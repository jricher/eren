package org.mitre.eren.dialogue;

import java.util.HashMap;
import java.util.Map;

import org.mitre.eren.http.OutboundHttpEndpoint;
import org.mitre.eren.protocol.clock.ClockConstants;
import org.mitre.eren.protocol.clock.Clocktick;
import org.mitre.eren.protocol.clock.SetClockspeed;
import org.opencare.lib.model.edxl.EDXLDistribution;

public class IdFactory {
  private static Map<String,Integer> messageids = new HashMap<String,Integer>();
  private static Map<String,Integer> stateChartIds = new HashMap<String,Integer>();
  
  public static String getNextMessageId (String sourceid) {
    Integer lastnum = messageids.get(sourceid);
    Integer nextnum = lastnum == null ? 0 : lastnum+1;
    messageids.put(sourceid, nextnum);
    return "urn:eren:dlg:"+sourceid+":"+nextnum.toString();
  }
  
  public static String getNextStateChartId (String npcId) { 
    Integer lastnum = stateChartIds.get(npcId);
    Integer nextnum = lastnum == null ? 0 : lastnum + 1;
    stateChartIds.put(npcId, nextnum);
    return npcId + ":" + nextnum.toString();
  }
  
}
