/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
*****************************************************************************/

package org.mitre.eren.protocol.edxl_rm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;

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
public class RequestReturnWrapper extends ResourceMessageWrapper implements RequestReturn {

   public RequestReturnWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }



    public RequestReturnWrapper(Factory factory, QName qname) {
      super(factory, qname);
      // TODO Auto-generated constructor stub
    }

}
