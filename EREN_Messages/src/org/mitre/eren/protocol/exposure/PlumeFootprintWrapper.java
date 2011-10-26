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
public class PlumeFootprintWrapper extends BaseWrapper implements PlumeFootprint, ExposureConstants {

	/**
	 * @param internal
	 */
	public PlumeFootprintWrapper(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param factory
	 * @param qname
	 */
	public PlumeFootprintWrapper(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getPlumeFootprint() {
		// TODO Auto-generated method stub
		String val = getSimpleExtension(EREN_PLUME);
		return val == null ? null : val;
	}

	@Override
	public void setPlumeFootprint(String plumeFootprint) {
		// TODO Auto-generated method stub
		if (plumeFootprint != null) {
			setElementText(EREN_PLUME, plumeFootprint);
		} else {
			removeElement(EREN_PLUME);
		}
	}	
	
	
	
}
