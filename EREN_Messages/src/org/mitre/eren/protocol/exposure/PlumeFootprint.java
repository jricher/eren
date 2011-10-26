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
public interface PlumeFootprint extends ExtensibleElement, Element, ExposureConstants {
	public String getPlumeFootprint();
	public void setPlumeFootprint(String plumeFootprint);

}
