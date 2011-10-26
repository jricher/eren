/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.protocol.startup;

/**
 *
 * @author JWINSTON
 */
import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.opencare.lib.model.BaseWrapper;

public class RequestPodStatus extends BaseWrapper implements DialogueConstants,EDXLRMConstants, StartupConstants {

    public RequestPodStatus(Element internal) {
        super(internal);
    }

    public RequestPodStatus(Factory factory, QName qname) {
        super(factory, qname);
    }

    public void setPodId(String podId) {
        setAttributeValue(DLG_PODID, podId);
    }

    public String getPodId() {
        return getAttributeValue(DLG_PODID);
    }

}
