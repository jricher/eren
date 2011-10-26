package org.mitre.eren.protocol.scenario;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class KMLFiles extends BaseWrapper implements ScenarioConstants {

	public KMLFiles(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public KMLFiles(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(People.class);
	
	public KMLFile addKMLFile() {
		return addExtension(EREN_KMLFILE);
	}
	
	public List<KMLFile> getKMLFile() {
		return getExtensions(EREN_KMLFILE);
	}

}
