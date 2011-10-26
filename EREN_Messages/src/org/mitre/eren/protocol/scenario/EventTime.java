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
public class EventTime extends BaseWrapper implements ScenarioConstants {

	/**
	 * @param internal
	 */
	public EventTime(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public EventTime(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(EventTime.class);

    public String getStartTime() {
    	return getSimpleExtension(EREN_STARTTIME);
    }
    
    public void setStartTime(String val) {
	    if (val != null) {
			setElementText(EREN_STARTTIME, val);
		} else {
			removeElement(EREN_STARTTIME);
		}
    }    

}
