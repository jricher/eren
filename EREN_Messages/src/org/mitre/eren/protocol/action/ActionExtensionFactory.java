/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
*****************************************************************************/

package org.mitre.eren.protocol.action;

import org.apache.abdera.util.AbstractExtensionFactory;

/**
 *
 * @author JWINSTON
 */
public class ActionExtensionFactory extends AbstractExtensionFactory implements ActionConstants  {

    public ActionExtensionFactory() {
        super(EREN_NS);
        addImpl(ACT_ACTION, Action.class);
        addImpl(ACT_ACTIONSFILE, ActionsFile.class);
        addImpl(ACT_COMMITACTION, ActionRequest.class);
        addImpl(ACT_FACILITIES, ActionFacilities.class);
        addImpl(ACT_FACILITY, ActionFacility.class);
        addImpl(ACT_RESOURCE, ActionResource.class);
        addImpl(ACT_ROLE, ActionRole.class);
        addImpl(ACT_ROLES, ActionRoles.class);
        addImpl(ACT_STATETRIGGERS, ActionStateTriggers.class);
        addImpl(ACT_INPUTS, ActionInputs.class);
        addImpl(ACT_INPUT, ActionInput.class);
        addImpl(ACT_PARAMETERS, ActionParameters.class);
        addImpl(ACT_PARAMETER, ActionParameter.class);
    }
}
