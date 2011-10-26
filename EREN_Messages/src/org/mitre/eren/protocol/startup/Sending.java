package org.mitre.eren.protocol.startup;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class Sending extends BaseWrapper implements StartupConstants {

	public Sending(Element internal) {
		super(internal);
		// TODO Auto-generated constructor stub
	}

	public Sending(Factory factory, QName qname) {
		super(factory, qname);
		// TODO Auto-generated constructor stub
	}
	//private static Logger log = LoggingUtils.getLogger(Sending.class);
	
    public String getFileName() {
		return getSimpleExtension(EREN_FILENAME);
	}

	public void setFileName(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_FILENAME, val);
		} else {
			removeElement(EREN_FILENAME);
		}
	}

    public String getFileSize() {
		return getSimpleExtension(EREN_FILESIZE);
	}

	public void setFileSize(String val) {
		// TODO Auto-generated method stub
		if (val != null) {
			setElementText(EREN_FILESIZE, val);
		} else {
			removeElement(EREN_FILESIZE);
		}
	}

}
