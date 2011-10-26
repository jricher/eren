/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
*****************************************************************************/

package org.mitre.eren.protocol.action;

import org.mitre.eren.protocol.scenario.Status;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.mitre.eren.protocol.scenario.ScenarioConstants;
import org.opencare.lib.model.BaseWrapper;

/**
 *
 * @author JWINSTON
 */
public class ActionStateTriggers extends BaseWrapper implements ActionConstants, ScenarioConstants {
    //private static Logger log = LoggingUtils.getLogger(ActionStateTriggers.class);

    public ActionStateTriggers(Element internal) {
        super(internal);
    }

    public ActionStateTriggers(Factory factory, QName qname) {
        super(factory, qname);
    }


    public void addStateTrigger(String status) {
        addSimpleExtension(EREN_STATUS, status);
    }

    public List<Status> getStateTriggers() {
        return getExtensions(EREN_STATUS);
    }

}
