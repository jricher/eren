package org.mitre.eren.protocol.startup;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class GameOver extends BaseWrapper implements StartupConstants {

	public GameOver(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param factory
	 * @param qname
	 */
	public GameOver(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
    public String getGameId() {
    	return getSimpleExtension(EREN_GAMEID);
    }
    
    public void setGameId(String val) {
    	if (val != null) {
    		setElementText(EREN_GAMEID, val);
    	}
    	else {
    		removeElement(EREN_GAMEID);
    	}
    }
}
