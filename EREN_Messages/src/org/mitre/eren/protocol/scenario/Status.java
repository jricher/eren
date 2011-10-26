/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
*****************************************************************************/

package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

/**
 *
 * @author JWINSTON
 */
public class Status extends BaseWrapper implements ScenarioConstants {

    //private static Logger log = LoggingUtils.getLogger(ActionRole.class);

    public Status(Element internal) {
        super(internal);
    }

    public Status(Factory factory, QName qname) {
        super(factory, qname);
    }

   public String getRole() {
        return getSimpleExtension(EREN_STATUS);
    }

    public void setRole(String val) {
        if (val != null) {
            setElementText(EREN_STATUS, val);
        } else {
            removeElement(EREN_STATUS);
        }
    }
}
