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
import org.mitre.eren.protocol.scenario.ScenarioConstants;
import org.opencare.lib.model.BaseWrapper;

/**
 *
 * @author JWINSTON
 */
public class ActionInput extends BaseWrapper implements ActionConstants, ScenarioConstants {

    //private static Logger log = LoggingUtils.getLogger(ActionInput.class);

    public ActionInput(Element internal) {
        super(internal);
    }

    public ActionInput(Factory factory, QName qname) {
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

    public void setType(String val) {
        if (val != null) {
            setAttributeValue(ACT_TYPE, val);
        }
    }

    public String getType() {
        return getAttributeValue(ACT_TYPE);
    }

    public void setFrom(String val) {
        if (val != null) {
            setAttributeValue(ACT_FROM, val);
        }
    }

    public String getFrom() {
        return getAttributeValue(ACT_FROM);
    }

    public void setQuestion(String question) {
        if (question != null) {
            setElementText(ACT_QUESTION, question);
        } else {
            removeElement(ACT_QUESTION);
        }
    }

    public String getQuestion() {
        return getSimpleExtension(ACT_QUESTION);
    }

    public void setMin(int min) {
        setElementText(EREN_MIN, String.valueOf(min));
    }

    public void unsetMin() {
        removeElement(EREN_MIN);
    }

    public int getMin() {
        String sval = getSimpleExtension(EREN_MIN);
        return Integer.parseInt(sval);
    }

    public void setMax(int max) {
        setElementText(EREN_MAX, String.valueOf(max));
    }

    public void unsetMax() {
        removeElement(EREN_MAX);
    }

    public int getMax() {
        String sval = getSimpleExtension(EREN_MAX);
        return Integer.parseInt(sval);
    }

    public void setMessage(String xmlMessage) {
        if (xmlMessage != null) {
            setElementText(ACT_MESSAGE, xmlMessage);
        } else {
            removeElement(ACT_MESSAGE);
        }
    }

    public String getMessage() {
        return getSimpleExtension(ACT_MESSAGE);
    }
}
