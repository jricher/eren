/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.protocol.action;

import java.util.List;
import javax.xml.namespace.QName;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

/**
 *
 * @author JWINSTON
 */
public class ActionFacilities extends BaseWrapper implements ActionConstants {
    //private static Logger log = LoggingUtils.getLogger(ActionFacilities.class);

    public ActionFacilities(Element internal) {
        super(internal);
    }

    public ActionFacilities(Factory factory, QName qname) {
        super(factory, qname);
    }


    public ActionFacility addFacility() {
        return addExtension(ACT_FACILITY);
    }

    public List<ActionFacility> getFacilities() {
        return getExtensions(ACT_FACILITY);
    }

}
