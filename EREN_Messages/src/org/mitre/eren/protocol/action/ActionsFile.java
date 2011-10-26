/*****************************************************************************
 *  License Agreement
 * 
 *  Copyright (C) 2011. The MITRE Corporation (http://www.mitre.org/).
 *  All Rights Reserved.
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
public class ActionsFile extends BaseWrapper implements ActionConstants {

    public ActionsFile(Element internal) {
        super(internal);

    }

    public ActionsFile(Factory factory, QName qname) {
        super(factory, qname);

    }

   public Action addAction() {

    	return addExtension(ACT_ACTION);
    }

    public List<Action> getActions() {
    	return getExtensions(ACT_ACTION);
    }

}
