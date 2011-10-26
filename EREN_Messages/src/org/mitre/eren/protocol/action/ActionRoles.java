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
public class ActionRoles extends BaseWrapper implements ActionConstants {
    //private static Logger log = LoggingUtils.getLogger(ActionRoles.class);

    public ActionRoles(Element internal) {
        super(internal);
    }

    public ActionRoles(Factory factory, QName qname) {
        super(factory, qname);
    }


    public ActionRole addRole() {
        return addExtension(ACT_ROLE);
    }

    public List<ActionRole> getRoles() {
        return getExtensions(ACT_ROLE);
    }

}
