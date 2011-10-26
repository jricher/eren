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
public class Message extends BaseWrapper implements StartupConstants {

	/**
	 * @param internal
	 */
	public Message(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public Message(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Message.class);

	public SendToBus getSendToBus() {
		return getExtension(EREN_SENDTOBUS);
	}

	public SendToBus addSendToBus() {
		return addExtension(EREN_SENDTOBUS);
	}
}
