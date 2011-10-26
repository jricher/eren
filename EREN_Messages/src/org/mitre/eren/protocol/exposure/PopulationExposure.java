/**
 * 
 */
package org.mitre.eren.protocol.exposure;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;
import org.mitre.eren.protocol.exposure.ExposureConstants;

/**
 * @author DZYWICKI
 *
 */
public interface PopulationExposure extends ExtensibleElement, Element, ExposureConstants {
	public Integer getPopulationExposed();
	public void setPopulationExposed(Integer populationExposed);
	
}
