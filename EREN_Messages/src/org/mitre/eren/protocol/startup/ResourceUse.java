package org.mitre.eren.protocol.startup;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.mitre.eren.protocol.edxl_rm.ResourceTypes;
import org.opencare.lib.model.BaseWrapper;

public class ResourceUse extends BaseWrapper implements StartupConstants {

    public ResourceUse(Element internal) {
        super(internal);
    }

    public ResourceUse(Factory factory, QName qname) {
        super(factory, qname);
    }
  
    public ResourceTypes getResourceType() {
        String resourceTypeString = getAttributeValue(EREN_RESOURCETYPE);
        ResourceTypes resourceType = ResourceTypes.valueOf(resourceTypeString);
        return resourceType;
    }

    public void setResourceType(ResourceTypes val) {
        if (val != null) {
            setAttributeValue(EREN_RESOURCETYPE, val.toString());
        } else {
            removeAttribute(EREN_RESOURCETYPE);
        }
    }

    public int getTotal() {
        String val = getAttributeValue(EREN_RESOURCETOTAL);
        if (val != null) {
            return Integer.getInteger(val);
        }
        return -1;
    }

    public void setTotal(int val) {
        if (val >= 0) {
            setAttributeValue(EREN_RESOURCETOTAL, String.valueOf(val));
        } else {
            removeAttribute(EREN_RESOURCETOTAL);
        }
    }

    public int getAvailable() {
        String val = getAttributeValue(EREN_RESOURCEAVAILABLE);
        if (val != null) {
            return Integer.getInteger(val);
        }
        return -1;
    }

    public void setAvailable(int val) {
        if (val >= 0) {
            setAttributeValue(EREN_RESOURCEAVAILABLE, String.valueOf(val));
        } else {
            removeAttribute(EREN_RESOURCEAVAILABLE);
        }
    }

}
