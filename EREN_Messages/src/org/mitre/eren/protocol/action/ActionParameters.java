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
public class ActionParameters extends BaseWrapper implements ActionConstants {
    //private static Logger log = LoggingUtils.getLogger(ActionParameter.class);

    public ActionParameters(Element internal) {
        super(internal);
    }

    public ActionParameters(Factory factory, QName qname) {
        super(factory, qname);
    }


    public ActionParameter addParameter() {
        return addExtension(ACT_PARAMETER);
    }

    public List<ActionParameter> getParameters() {
        return getExtensions(ACT_PARAMETER);
    }

}
