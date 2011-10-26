/**
 * 
 */
package org.mitre.eren.protocol.startup;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

/**
 * @author jricher
 *
 */
public class RoleRequest extends BaseWrapper implements StartupConstants {

	/**
	 * @param internal
	 */
	public RoleRequest(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public RoleRequest(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(RoleRequest.class);
    
    public String getScenarioId() {
		return getSimpleExtension(EREN_SCENARIOID);
	}

	public void setScenarioId(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_SCENARIOID, val);
		} else {
			removeElement(EREN_SCENARIOID);
		}
	}
    
    public String getRoleId() {
		return getSimpleExtension(EREN_ROLEID);
	}

	public void setRoleId(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_ROLEID, val);
		} else {
			removeElement(EREN_ROLEID);
		}
	}
}
