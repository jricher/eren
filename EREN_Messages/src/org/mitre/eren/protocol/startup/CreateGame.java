package org.mitre.eren.protocol.startup;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class CreateGame extends BaseWrapper implements StartupConstants {

	public CreateGame(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public CreateGame(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	
	public String getRoleId() {
		return getSimpleExtension(EREN_ROLEID);
	}

	public void setRoleId(String val) {
		if (val != null) {
			setElementText(EREN_ROLEID, val);
		} else {
			removeElement(EREN_ROLEID);
		}
	}
	
	public String getScenarioId(){
		return getSimpleExtension(EREN_SCENARIOID);
	}
	
	public void setScenarioId(String val){
		if (val != null) {
			setElementText(EREN_SCENARIOID, val);
		} else {
			removeElement(EREN_SCENARIOID);
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
