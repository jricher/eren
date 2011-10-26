package org.mitre.eren.protocol.exposure;

import org.apache.abdera.util.AbstractExtensionFactory;

public class ExposureExtensionFactory extends AbstractExtensionFactory implements ExposureConstants {

	public ExposureExtensionFactory() {
		super(EREN_NS);
		
		// clock controls
		addImpl(EREN_POPULATION, PopulationExposureWrapper.class);
		addImpl(EREN_PLUME, PlumeFootprintWrapper.class);
	}


}
