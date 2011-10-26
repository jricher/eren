/**
 * 
 */
package org.mitre.eren.protocol.startup;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.mitre.eren.protocol.ERENConstants;
import org.opencare.lib.model.BaseWrapper;

/**
 * @author AANGANES
 *
 */
public class ActiveRoles extends BaseWrapper implements StartupConstants,
		ERENConstants {

	/**
	 * @param internal
	 */
	public ActiveRoles(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public ActiveRoles(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}

	public List<ActiveRole> getActiveRoles() {
		return getExtensions(EREN_ACTIVEROLE);
	}
	
	public ActiveRole addRole() {
		return addExtension(EREN_ACTIVEROLE);
	}
	
}
