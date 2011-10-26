/**
 * 
 */
package org.mitre.eren.protocol.exposure;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;
import org.mitre.eren.protocol.exposure.ExposureConstants;

/**
 * @author DZYWICKI
 *
 */
public class PopulationExposureWrapper extends BaseWrapper implements ExposureConstants, PopulationExposure {

	/**
	 * @param internal
	 */
	public PopulationExposureWrapper(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public PopulationExposureWrapper(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Integer getPopulationExposed() {
		// TODO Auto-generated method stub
		String val = getSimpleExtension(EREN_POPULATION);
		return val == null ? null : Integer.parseInt(val);
	}

	@Override
	public void setPopulationExposed(Integer populationExposure) {
		// TODO Auto-generated method stub
		if (populationExposure != null) {
			setElementText(EREN_POPULATION, populationExposure.toString());
		} else {
			removeElement(EREN_POPULATION);
		}
	}	
	
	
}
