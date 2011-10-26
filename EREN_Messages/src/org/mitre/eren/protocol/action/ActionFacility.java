/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
*****************************************************************************/

package org.mitre.eren.protocol.action;

import javax.xml.namespace.QName;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

/**
 *
 * @author JWINSTON
 */
public class ActionFacility extends BaseWrapper implements ActionConstants {

    //private static Logger log = LoggingUtils.getLogger(ActionFacility.class);

    public ActionFacility(Element internal) {
        super(internal);
    }

    public ActionFacility(Factory factory, QName qname) {
        super(factory, qname);
    }

   public String getFacility() {
        return getSimpleExtension(ACT_FACILITY);
    }

    public void setFacility(String val) {
        if (val != null) {
            setElementText(ACT_FACILITY, val);
        } else {
            removeElement(ACT_FACILITY);
        }
    }
}
