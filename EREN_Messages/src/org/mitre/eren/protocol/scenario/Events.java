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
public class Events extends BaseWrapper implements ScenarioConstants {

	/**
	 * @param internal
	 */
	public Events(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public Events(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Events.class);
	
	public Event addNewEvent() {
		return addExtension(EREN_EVENT);
	}
	
	public List<Event> getEventsList() {
		return getExtensions(EREN_EVENT);
	}
}
