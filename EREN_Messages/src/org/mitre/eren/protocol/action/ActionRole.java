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
public class ActionRole extends BaseWrapper implements ActionConstants {

    //private static Logger log = LoggingUtils.getLogger(ActionRole.class);

    public ActionRole(Element internal) {
        super(internal);
    }

    public ActionRole(Factory factory, QName qname) {
        super(factory, qname);
    }

   public String getRole() {
        return getSimpleExtension(ACT_ROLE);
    }

    public void setRole(String val) {
        if (val != null) {
            setElementText(ACT_ROLE, val);
        } else {
            removeElement(ACT_ROLE);
        }
    }
}
