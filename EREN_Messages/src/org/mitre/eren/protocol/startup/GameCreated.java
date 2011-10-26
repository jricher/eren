/**
 * 
 */
package org.mitre.eren.protocol.startup;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

/**
 * @author aanganes
 *
 */
public class GameCreated extends BaseWrapper implements StartupConstants {

	/**
	 * @param internal
	 */
	public GameCreated(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public GameCreated(Factory factory, QName qname) {
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
    
    public String getGameName() {
		return getSimpleExtension(EREN_GAMENAME);
	}
	
	public void setGameName(String val){
		if (val != null) {
			setElementText(EREN_GAMENAME, val);
		} else {
			removeElement(EREN_GAMENAME);
		}
	}
}
