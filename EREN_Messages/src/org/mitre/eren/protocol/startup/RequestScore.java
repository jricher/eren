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

public class RequestScore extends BaseWrapper implements DialogueConstants,EDXLRMConstants {

    public RequestScore(Element internal) {
        super(internal);
    }

    public RequestScore(Factory factory, QName qname) {
        super(factory, qname);
    }

}
