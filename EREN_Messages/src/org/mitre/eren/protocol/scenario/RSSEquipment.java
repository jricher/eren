/**
 * 
 */
package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

/**
 * @author jricher
 *
 */
public class RSSEquipment extends BaseWrapper implements ScenarioConstants {

	/**
	 * @param internal
	 */
	public RSSEquipment(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public RSSEquipment(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(RSSEquipment.class);
	   public void setID(String val) {
	    	setAttributeValue("ID", val);
	    }
	    
	    public String getID() {
	    	return getAttributeValue("ID");
	    }
	    
	    public Location getLocation() {
	    	return getExtension(EREN_LOCATION);
	    }
	    
	    public Location addLocation() {
	    	return addExtension(EREN_LOCATION);
	    }

	    public void setQuantity(String val) {
		    if (val != null) {
				setElementText(EREN_QUANTITY, val);
			} else {
				removeElement(EREN_QUANTITY);
			}
	    }    
	    
	    public String getQuantity() {
	    	return getSimpleExtension(EREN_QUANTITY);
	    }
}
