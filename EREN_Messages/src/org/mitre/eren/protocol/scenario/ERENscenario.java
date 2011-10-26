package org.mitre.eren.protocol.scenario;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class ERENscenario extends BaseWrapper implements ScenarioConstants {

	/**
     * @param internal
     */
    public ERENscenario(Element internal) {
	    super(internal);
	    // TODO Auto-generated constructor stub
    }

	/**
     * @param factory
     * @param qname
     */
    public ERENscenario(Factory factory, QName qname) {
	    super(factory, qname);
	    // TODO Auto-generated constructor stub
    }

    public void setID(String val) {
    	setAttributeValue("ID", val);
    }
    
    public String getID() {
    	return getAttributeValue("ID");
    }
    
    public String getDescription() {
    	return getSimpleExtension(EREN_DESCRIPTION);
    }
    
    public void setDescription(String val) {
	    if (val != null) {
			setElementText(EREN_DESCRIPTION, val);
		} else {
			removeElement(EREN_DESCRIPTION);
		}
    }    
    
    public String getImage() {
    	return getSimpleExtension(EREN_IMAGE);
    }
    
    public void setImage(String val) {
	    if (val != null) {
			setElementText(EREN_IMAGE, val);
		} else {
			removeElement(EREN_IMAGE);
		}
    }    
    
    public String getBaseUrl() {
    	return getSimpleExtension(EREN_BASEURL);
    }
    
    public void setBaseUrl(String val) {
	    if (val != null) {
			setElementText(EREN_BASEURL, val);
		} else {
			removeElement(EREN_BASEURL);
		}
    }    
    
    public String getActionsFile() {
    	return getSimpleExtension(EREN_ACTIONSFILE);
    }

    public void setActionsFile(String val) {
	    if (val != null) {
			setElementText(EREN_ACTIONSFILE, val);
		} else {
			removeElement(EREN_ACTIONSFILE);
		}
    }


   public String getName() {
    	return getSimpleExtension(EREN_NAME);
    }
    
    public void setName(String val) {
	    if (val != null) {
			setElementText(EREN_NAME, val);
		} else {
			removeElement(EREN_NAME);
		}
    }    
    
    public Events addEvents() {
    	return addExtension(EREN_EVENTS);
    }
    
    public Events getEvents() {
    	return getExtension(EREN_EVENTS);
    }
    
    public ScenarioLocation addScenarioLocation() {
    	return addExtension(EREN_SCENARIOLOCATION);
    }
    
    public ScenarioLocation getScenarioLocation() {
    	return getExtension(EREN_SCENARIOLOCATION);
    }

    public Facilities addFacilities() {
    	return addExtension(EREN_FACILITIES);
    }
    
    public Facilities getFacilities() {
    	return getExtension(EREN_FACILITIES);
    }
    
    public People addPeople() {
    	return addExtension(EREN_PEOPLE);
    }
    
    public People getPeople() {
    	return getExtension(EREN_PEOPLE);
    }

    public KMLFiles addKMLFiles () { 
      return addExtension(EREN_KMLFILES);
    }
    
    public KMLFiles getKMLFiles () { 
      return getExtension(EREN_KMLFILES);
    }
    
    public Equipment addEquipment() {
    	return addExtension(EREN_EQUIPMENT);
    }
    
    public Equipment getEquipment() {
    	return getExtension(EREN_EQUIPMENT);
    }
    
    public Roles addRoles() {
    	return addExtension(EREN_ROLES);
    }
    
    public Roles getRoles() {
    	return getExtension(EREN_ROLES);
    }

    public Timing addTiming() {
    	return addExtension(EREN_TIMING);
    }
    
    public Timing getTiming() {
    	return getExtension(EREN_TIMING);
    }

}

