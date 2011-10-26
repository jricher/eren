/**
 * 
 */
package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

/**
 * 
 * TODO!!
 * 
 * @author gertner
 *
 */
public class KMLFile extends BaseWrapper implements ScenarioConstants {

	/**
	 * @param internal
	 */
	public KMLFile(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public KMLFile(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(NPC.class);

	public void setID(String val) {
		setAttributeValue("ID", val);
	}
	    	
	public String getID() {
		return getAttributeValue("ID");
	}
	
	public void setPath(String val) {
		if (val != null) {
			setElementText(EREN_PATH, val);
		} else {
			removeElement(EREN_PATH);
		}
	}

	public String getPath() {
		return getSimpleExtension(EREN_PATH);
	}

	    
}
