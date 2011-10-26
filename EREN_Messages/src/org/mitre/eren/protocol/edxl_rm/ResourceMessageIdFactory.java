package org.mitre.eren.protocol.edxl_rm;

import java.util.HashMap;
import java.util.Map;

public class ResourceMessageIdFactory {
  private static Map<String,Integer> idMap = new HashMap<String,Integer>();
  
  public static String getNextId (String messageSource) { 
    Integer last = idMap.get(messageSource);
    Integer next = last == null ? 0 : last+1;
    idMap.put(messageSource, next);
    return "urn:eren:"+messageSource+":"+next;
  }

}
