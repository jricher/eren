package org.mitre.eren.protocol.startup;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class Exposure extends BaseWrapper implements StartupConstants {

  public Exposure(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public Exposure(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }

}
