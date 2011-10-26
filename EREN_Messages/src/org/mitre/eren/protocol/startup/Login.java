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
public class Login extends BaseWrapper implements StartupConstants {

	/**
	 * @param internal
	 */
	public Login(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public Login(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Login.class);
    
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
    
    public String getPassword() {
		return getSimpleExtension(EREN_PASSWORD);
	}

	public void setPassword(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_PASSWORD, val);
		} else {
			removeElement(EREN_PASSWORD);
		}
	}
    
    public String getClient() {
		return getSimpleExtension(EREN_CLIENT);
	}

	public void setClient(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_CLIENT, val);
		} else {
			removeElement(EREN_CLIENT);
		}
	}
}
