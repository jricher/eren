/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
*****************************************************************************/

package org.mitre.eren.protocol.startup;

import java.util.List;
import javax.xml.namespace.QName;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;


/**
 *
 * @author JWINSTON
 */
public class ResourceUsage extends BaseWrapper implements StartupConstants {

    public ResourceUsage(Element internal) {
        super(internal);

    }

    public ResourceUsage(Factory factory, QName qname) {
        super(factory, qname);

    }

   public ResourceUse addResourceUse() {
    	return addExtension(EREN_RESOURCEUSE);
    }

    public List<ResourceUse> getResourceUses() {
    	return getExtensions(EREN_RESOURCEUSE);
    }

}
