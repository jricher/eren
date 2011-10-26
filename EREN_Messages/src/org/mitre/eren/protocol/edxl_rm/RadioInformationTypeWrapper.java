//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.26 at 12:27:28 PM EDT 
//


package org.mitre.eren.protocol.edxl_rm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;


/**
 * <p>Java class for RadioInformationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RadioInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RadioType" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ValueListType"/>
 *         &lt;element name="RadioChannel" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}RadioChannelType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RadioInformationType", namespace = "urn:oasis:names:tc:emergency:EDXL:RM:1.0", propOrder = {
    "radioType",
    "radioChannel"
})
public class RadioInformationTypeWrapper extends BaseWrapper implements RadioInformationType {



    public RadioInformationTypeWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public RadioInformationTypeWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }

    /**
     * Gets the value of the radioType property.
     * 
     * @return
     *     possible object is
     *     {@link ValueListType }
     *     
     */
    @Override
	public ValueListType getRadioType() {
        return getExtension(RM_RADIOTYPE);
    }

    /**
     * Sets the value of the radioType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueListType }
     *     
     */
    @Override
	public ValueListType addRadioType() {
        return addExtension(RM_RADIOTYPE);
    }

    /**
     * Gets the value of the radioChannel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getRadioChannel() {
        return getSimpleExtension(RM_RADIOCHANNEL);
    }

    /**
     * Sets the value of the radioChannel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setRadioChannel(String value) {
        setElementText(RM_RADIOCHANNEL,value);
    }

}