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
public class ActionInputs extends BaseWrapper implements ActionConstants {
    //private static Logger log = LoggingUtils.getLogger(ActionInputs.class);

    public ActionInputs(Element internal) {
        super(internal);
    }

    public ActionInputs(Factory factory, QName qname) {
        super(factory, qname);
    }


    public ActionInput addInput() {
        return addExtension(ACT_INPUT);
    }

    public List<ActionInput> getInputs() {
        return getExtensions(ACT_INPUT);
    }

}
