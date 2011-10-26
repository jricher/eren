package org.mitre.eren.protocol.exposure;


import org.mitre.eren.protocol.exposure.ExposureConstants;

import org.mitre.eren.protocol.exposure.PopulationExposure;
import org.mitre.eren.http.OutboundHttpEndpoint;
import org.opencare.lib.model.edxl.EDXLDistribution;

public class ExposureTest implements ExposureConstants {

	//Commented this out because it broke the build. 
	public static void main(String[] args) {
		
//		OutboundHttpEndpoint client = new OutboundHttpEndpoint("randomstring"); 
//		client.registerExtension(new ExposureExtensionFactory());		
//		
//		
//		EDXLDistribution edxl = client.makeEdxl();
//				
////		edxl.setSenderID(new String("populationexposure@eren.mitre.org"));
//		
//		PopulationExposure pe = (PopulationExposure) client.attachElement(edxl, EREN_POPULATION);
//		int population= 400001;
//		pe.setPopulationExposed(new Integer(population));
//
////		edxl.setDistributionStatus(EDXLDistribution.DistributionStatus.System); // TODO: make these make sense
////		edxl.setDistributionType(EDXLDistribution.DistributionType.Update);
//		
////		edxl.setSenderID("clock@erenbus.mitre.org"); // clock for this game TODO: make game ID
////		edxl.setCombinedConfidentiality("UNCLASSIFIED AND NOT SENSITIVE");
////		edxl.setLanguage("EN");
//		
//		
//		
//		System.out.println("edxl");
//        System.out.println(edxl.toString());
//        
//        
//        
//        Integer i = pe.getPopulationExposed();
//        
//        System.out.println("pop: " + i);
//        
    } 
	
}
