/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
*****************************************************************************/

package org.mitre.eren.protocol.action;

import javax.xml.namespace.QName;
import org.mitre.eren.protocol.ERENConstants;

/**
 *
 * @author JWINSTON
 */
public interface ActionConstants extends ERENConstants{
    public static final String ACTION_PFX = "act:";
    public static final String ACTION_NS = "urn:mitre:eren:actions:1.0";
    public static final QName ACT_ACTIONSFILE = new QName(EREN_NS, "actionsfile", EREN_PFX);
    public static final QName ACT_COMMITACTION = new QName(EREN_NS, "commitAction", EREN_PFX);
    public static final QName ACT_ACTION = new QName(EREN_NS, "action", EREN_PFX);
    public static final QName ACT_TYPE = new QName(ACTION_NS, "type", ACTION_PFX);
    public static final QName ACT_DISPLAYNAME = new QName(ACTION_NS, "displayName", ACTION_PFX);
    public static final QName ACT_COMMAND = new QName(ACTION_NS, "command", ACTION_PFX);
    public static final QName ACT_ROLES = new QName(ACTION_NS, "roles", ACTION_PFX);
    public static final QName ACT_ROLE = new QName(ACTION_NS, "role", ACTION_PFX);
    public static final QName ACT_FACILITIES = new QName(ACTION_NS, "facilityIds", ACTION_PFX);
    public static final QName ACT_FACILITY = new QName(ACTION_NS, "facilityId", ACTION_PFX);
    public static final QName ACT_RESOURCE = new QName(ACTION_NS, "resource", ACTION_PFX);
    public static final QName ACT_STATETRIGGERS = new QName(ACTION_NS, "statetriggers", ACTION_PFX);
    public static final QName ACT_INPUTS = new QName(ACTION_NS, "inputs", ACTION_PFX);
    public static final QName ACT_INPUT = new QName(ACTION_NS, "input", ACTION_PFX);
    public static final QName ACT_NAME = new QName(ACTION_NS, "name", ACTION_PFX);
    public static final QName ACT_FROM = new QName(ACTION_NS, "from", ACTION_PFX);
    public static final QName ACT_QUESTION = new QName(ACTION_NS, "questionText", ACTION_PFX);
    public static final QName ACT_MESSAGE = new QName(ACTION_NS, "message", ACTION_PFX);
    public static final QName ACT_PARAMETERS = new QName(ACTION_NS, "parameters", ACTION_PFX);
    public static final QName ACT_PARAMETER = new QName(ACTION_NS, "parameter", ACTION_PFX);

    public enum ActionsEnum {
        OPEN, CLOSE, COMMIT, RELEASE, SETSTANDARDOFCARE;
    }
}
