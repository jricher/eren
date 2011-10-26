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
public class RoleFilled extends BaseWrapper implements StartupConstants {

	/**
	 * @param internal
	 */
	public RoleFilled(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public RoleFilled(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(RoleFilled.class);
	
    
    public String getUsername() {
		return getSimpleExtension(EREN_USERNAME);
	}

	public void setUsername(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_USERNAME, val);
		} else {
			removeElement(EREN_USERNAME);
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
	public String getGameID() {
		return getSimpleExtension(EREN_GAMEID);
	}
	
	public void setGameId(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_GAMEID, val);
		} else {
			removeElement(EREN_GAMEID);
		}
	}
	
}
