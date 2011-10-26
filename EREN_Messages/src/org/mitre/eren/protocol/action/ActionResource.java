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
public class ActionResource extends BaseWrapper implements ActionConstants {

    //private static Logger log = LoggingUtils.getLogger(ActionRole.class);

    public ActionResource(Element internal) {
        super(internal);
    }

    public ActionResource(Factory factory, QName qname) {
        super(factory, qname);
    }

   public String getResources() {
        return getSimpleExtension(ACT_ROLE);
    }

    public void setResources(String val) {
        if (val != null) {
            setElementText(ACT_RESOURCE, val);
        } else {
            removeElement(ACT_RESOURCE);
        }
    }
}
