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
public class ScenarioId extends BaseWrapper implements StartupConstants {

	/**
	 * @param internal
	 */
	public ScenarioId(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public ScenarioId(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(ScenarioId.class);
}
