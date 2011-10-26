/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
*****************************************************************************/

package org.mitre.eren.protocol.action;

/**
 *
 * @author JWINSTON
 */

import javax.xml.namespace.QName;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class ActionRequest extends BaseWrapper implements ActionConstants {
    //private static Logger log = LoggingUtils.getLogger(ActionRequest.class);

    public ActionRequest(Element internal) {
        super(internal);

    }

    public ActionRequest(Factory factory, QName qname) {
        super(factory, qname);

    }


    public String getAction() {
        String id = getSimpleExtension(ACT_TYPE);
        return id;
    }

    public void setAction(String val) {
        if (val != null) {
            setElementText(ACT_TYPE, val);
        } else {
            removeElement(ACT_TYPE);
        }
    }


    public String getRole() {
        String role = getSimpleExtension(ACT_ROLE);
        return role;
    }

    public void setRole(String val) {
        if (val != null) {
            setElementText(ACT_ROLE, val);
        } else {
            removeElement(ACT_ROLE);
        }
    }

    public String getFacility() {
        return getSimpleExtension(ACT_FACILITY);
    }

    public void setFacility(String val) {
        if (val != null) {
            setElementText(ACT_FACILITY, val);
        } else {
            removeElement(ACT_FACILITY);
        }
    }

    public String getResource() {
        String resource = getSimpleExtension(ACT_RESOURCE);
        return resource;
    }

    public void setResource(String val) {
        if (val != null) {
            setElementText(ACT_RESOURCE, val);
        } else {
            removeElement(ACT_RESOURCE);
        }
    }

    public ActionParameters addParameters() {
        ActionParameters params = getParameters();
        if (params == null) {
            params = addExtension(ACT_INPUTS);
        }
    	return params;
    }

    public ActionParameters getParameters() {
    	return getExtension(ACT_PARAMETERS);
    }

    public ActionParameter getParameter(String name) {
        ActionParameters params = getParameters();
        if (params == null)
            return null;
        for (ActionParameter p : params.getParameters()) {
            if (p == null)
                continue;
            if (name.equals(p.getName())) {
                return p;
            }
        }
        return null;
    }

    public void addParameter(String name, String value) {
        ActionParameters params = addParameters();
        ActionParameter param = params.addParameter();
        param.setName(name);
        param.setValue(value);
    }

    public int getQuantity() {
        int qty = 0;
        ActionParameter p = getParameter("quantity");
        if (p != null) {
            qty = Integer.parseInt(p.getValue());
        }
        return qty;
    }

    public void setQuantity(int qty){
        addParameter("quantity", String.valueOf(qty));
    }

    public int getValueParam() {
        int qty = 0;
        ActionParameter p = getParameter("value");
        if (p != null) {
            qty = Integer.parseInt(p.getValue());
        }
        return qty;
    }

    public void setValueParam(int qty){
        addParameter("value", String.valueOf(qty));
    }

}
