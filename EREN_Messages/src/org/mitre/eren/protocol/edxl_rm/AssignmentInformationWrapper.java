package org.mitre.eren.protocol.edxl_rm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.opencare.lib.model.BaseWrapper;



/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Quantity" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}QuantityType" minOccurs="0"/>
 *         &lt;element name="Restrictions" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}RestrictionsType" minOccurs="0"/>
 *         &lt;element name="AnticipatedFunction" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}AnticipatedFunctionType" minOccurs="0"/>
 *         &lt;element name="PriceQuote" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}PriceQuoteType" minOccurs="0"/>
 *         &lt;element name="OrderID" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}OrderIDType" minOccurs="0"/>
 *         &lt;element name="AssignmentInstructions" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}AssignmentInstructionsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "quantity",
    "restrictions",
    "anticipatedFunction",
    "priceQuote",
    "orderID",
    "assignmentInstructions"
})
public class AssignmentInformationWrapper extends BaseWrapper implements AssignmentInformation {

    public AssignmentInformationWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public AssignmentInformationWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link QuantityType }
     *     
     */
    @Override
	public QuantityType getQuantity() {
        return getExtension(RM_QUANTITY);
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *     
     */
    @Override
	public QuantityType addQuantity () {
        return addExtension(RM_QUANTITY);
    }

    /**
     * Gets the value of the restrictions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getRestrictions() {
        return getSimpleExtension(RM_RESTRICTIONS);
    }

    /**
     * Sets the value of the restrictions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setRestrictions(String value) {
        setElementText(RM_RESTRICTIONS,value);
    }

    /**
     * Gets the value of the anticipatedFunction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getAnticipatedFunction() {
        return getSimpleExtension(RM_ANTICIPATEDFUNCTION);
    }

    /**
     * Sets the value of the anticipatedFunction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setAnticipatedFunction(String value) {
        setElementText(RM_ANTICIPATEDFUNCTION,value);
    }

    /**
     * Gets the value of the priceQuote property.
     * 
     * @return
     *     possible object is
     *     {@link PriceQuoteType }
     *     
     */
    @Override
	public PriceQuoteType getPriceQuote() {
        return getExtension(RM_PRICEQUOTE);
    }

    /**
     * Sets the value of the priceQuote property.
     * 
     * @param value
     *     allowed object is
     *     {@link PriceQuoteType }
     *     
     */
    @Override
	public PriceQuoteType addPriceQuote() {
        return addExtension(RM_PRICEQUOTE);
    }

    /**
     * Gets the value of the orderID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getOrderID() {
        return getSimpleExtension(RM_ORDERID);
    }

    /**
     * Sets the value of the orderID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setOrderID(String value) {
      setElementText(RM_ORDERID,value);
    }

    /**
     * Gets the value of the assignmentInstructions property.
     * 
     * @return
     *     possible object is
     *     {@link AssignmentInstructionsType }
     *     
     */
    @Override
	public AssignmentInstructionsType getAssignmentInstructions() {
      return getExtension(RM_ASSIGNMENTINSTRUCTIONS);
    }

    /**
     * Sets the value of the assignmentInstructions property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssignmentInstructionsType }
     *     
     */
    @Override
	public AssignmentInstructionsType addAssignmentInstructions() {
      return addExtension(RM_ASSIGNMENTINSTRUCTIONS);
    }

}

