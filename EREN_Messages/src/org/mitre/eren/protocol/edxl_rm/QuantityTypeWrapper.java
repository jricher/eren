//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.26 at 12:27:28 PM EDT 
//


package org.mitre.eren.protocol.edxl_rm;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;


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
public class QuantityTypeWrapper extends BaseWrapper implements QuantityType {

    public QuantityTypeWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public QuantityTypeWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }



    @Override
	public List<String> getQuantityText() {
        List<Element> values = getExtensions(RM_QUANTITYTEXT);
        List<String> result = new ArrayList<String>();
        for (Element e : values) { 
          result.add(e.getText());
        }
        return result;
    }
    
    @Override
	public void addQuantityText(String value) { 
      Element e = addExtension(RM_QUANTITYTEXT);
      e.setText(value);
    }

    @Override
	public MeasuredQuantity getMeasuredQuantity() {
        return getExtension(RM_MEASUREDQUANTITY);
    }

    /**
     * Sets the value of the measuredQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuantityTypeWrapper.MeasuredQuantity }
     *     
     */
    @Override
	public MeasuredQuantity addMeasuredQuantity() {
        return addExtension(RM_MEASUREDQUANTITY);
    }


 

}