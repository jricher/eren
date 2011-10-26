/**
 * 
 */
package org.mitre.eren.protocol.scenario;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

/**
 * @author jricher
 *
 */
public class Roles extends BaseWrapper implements ScenarioConstants {

	/**
	 * @param internal
	 */
	public Roles(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public Roles(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Roles.class);

	public Role addRole() {
		return addExtension(EREN_ROLE);
	}
	
	public List<Role> getRoles() {
		return getExtensions(EREN_ROLE);
	}
	
}
