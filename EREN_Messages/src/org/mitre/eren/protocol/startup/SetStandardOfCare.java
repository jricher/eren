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
import java.security.InvalidParameterException;
import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.mitre.eren.protocol.dialogue.DialogueConstants;
import org.mitre.eren.protocol.edxl_rm.EDXLRMConstants;
import org.opencare.lib.model.BaseWrapper;

public class SetStandardOfCare extends BaseWrapper implements DialogueConstants, StartupConstants, EDXLRMConstants {

    public SetStandardOfCare(Element internal) {
        super(internal);
    }

    public SetStandardOfCare(Factory factory, QName qname) {
        super(factory, qname);
    }

    public void setPodId(String podId) {
        setAttributeValue(DLG_PODID, podId);
    }

    public String getPodId() {
        return getAttributeValue(DLG_PODID);
    }

    public void setStandardOfCare(int level) {
        if (level < 1 || level > 6) {
            throw new InvalidParameterException("Standard of Care leve must be between 1 and 6 inclusive");
        }
        setText(Integer.toString(level));
    }

    public int getStandardOfCare() {
        String level = getText().trim();
        return Integer.parseInt(level);
    }
}
