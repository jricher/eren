package org.mitre.eren.protocol.edxl_rm;

import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;

public class TargetAreaWrapper extends BaseWrapper implements TargetArea {

	public TargetAreaWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }
  public TargetAreaWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }
  @Override
public String getResourceType() {
    return null;
  }
	@Override
	public void setResourceType(String rt) {}
	
}
