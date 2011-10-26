/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.protocol.edxl_rm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author JWINSTON
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "messageID",
    "sentDateTime",
    "messageContentType",
    "messageDescription",
    "originatingMessageID",
    "precedingMessageID",
    "incidentInformation",
    "messageRecall",
    "funding",
    "contactInformation",
    "resourceInformation"
})
@XmlRootElement(name = "RequestReturn", namespace = "urn:oasis:names:tc:emergency:EDXL:RM:1.0:msg")
public interface RequestReturn extends ResourceMessage {

}
