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
public class ActionParameter extends BaseWrapper implements ActionConstants {

    //private static Logger log = LoggingUtils.getLogger(ActionParameter.class);

    public ActionParameter(Element internal) {
        super(internal);
    }

    public ActionParameter(Factory factory, QName qname) {
        super(factory, qname);
    }


    public void setName(String value) {
        if (value != null) {
            setAttributeValue(ACT_NAME, value);
        }
    }

    public String getName() {
        return getAttributeValue(ACT_NAME);
    }


    public void setValue(String val) {
        if (val != null) {
            setText(val);
        } 
    }

    public String getValue() {
        return getText();
    }
}
