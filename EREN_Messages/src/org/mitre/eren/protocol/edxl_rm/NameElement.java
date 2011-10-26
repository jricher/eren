package org.mitre.eren.protocol.edxl_rm;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;urn:oasis:names:tc:ciq:ct:3>String">
 *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:ct:3}grAbbreviation"/>
 *       &lt;attribute name="ElementType" type="{urn:oasis:names:tc:ciq:xnl:3}PersonNameElementList" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "value"
})

public interface NameElement extends Element, EDXLRMConstants, ExtensibleElement {

  public PersonNameElementList getElementType();
  public void setElementType(PersonNameElementList value);
  public Boolean isAbbreviation();
  public void setAbbreviation(Boolean value);
  public Map<QName, String> getOtherAttributes();

}

