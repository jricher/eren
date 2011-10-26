/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
 * 
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

public class Action extends BaseWrapper implements ActionConstants {
    //private static Logger log = LoggingUtils.getLogger(Action.class);

    public Action(Element internal) {
        super(internal);

    }

    public Action(Factory factory, QName qname) {
        super(factory, qname);

    }

    public void setType(String val) {
        if (val != null) {
            setElementText(ACT_TYPE, val);
        } else {
            removeElement(ACT_TYPE);
        }
    }

    public String getType() {
        return getSimpleExtension(ACT_TYPE);
    }

    public void setDisplayName(String val) {
        if (val != null) {
            setElementText(ACT_DISPLAYNAME, val);
        } else {
            removeElement(ACT_DISPLAYNAME);
        }
    }

    public String getDisplayName() {
        return getSimpleExtension(ACT_DISPLAYNAME);
    }

    public ActionRoles addRoles() {
    	return addExtension(ACT_ROLES);
    }

    public ActionRoles getRoles() {
    	return getExtension(ACT_ROLES);
    }

    public ActionFacilities addFacilities() {
    	return addExtension(ACT_FACILITIES);
    }

    public ActionFacilities getFacilities() {
    	return getExtension(ACT_FACILITIES);
    }

    public void setResource(String resource) {
        if (resource != null) {
            setElementText(ACT_RESOURCE, resource);
        } else {
            removeElement(ACT_RESOURCE);
        }
    }

    public String getResource() {
    	return getSimpleExtension(ACT_RESOURCE);
    }

    public ActionStateTriggers addStateTriggers() {
    	return addExtension(ACT_STATETRIGGERS);
    }

    public ActionStateTriggers getStateTriggers() {
    	return getExtension(ACT_STATETRIGGERS);
    }

    public ActionInputs addInputs() {
    	return addExtension(ACT_INPUTS);
    }

    public ActionInputs getInputs() {
    	return getExtension(ACT_INPUTS);
    }

    public void setActionMessage(String resource) {
        if (resource != null) {
            setElementText(ACT_MESSAGE, resource);
        } else {
            removeElement(ACT_MESSAGE);
        }
    }

    public String getMessage() {
    	return getSimpleExtension(ACT_MESSAGE);
    }

}
