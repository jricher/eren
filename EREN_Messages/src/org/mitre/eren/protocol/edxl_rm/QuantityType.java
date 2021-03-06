//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.26 at 12:27:28 PM EDT 
//


package org.mitre.eren.protocol.edxl_rm;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;


/**
 * <p>Java class for QuantityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuantityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="QuantityText" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="MeasuredQuantity">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Amount" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                   &lt;element name="UnitOfMeasure" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ValueListType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuantityType", namespace = "urn:oasis:names:tc:emergency:EDXL:RM:1.0", propOrder = {
    "quantityText",
    "measuredQuantity"
})
@XmlSeeAlso({
    PriceQuoteType.class
})
public interface QuantityType extends Element, ExtensibleElement, EDXLRMConstants {

    public List<String> getQuantityText();
    public void addQuantityText(String value);
    public MeasuredQuantity getMeasuredQuantity();
    public MeasuredQuantity addMeasuredQuantity();


}
