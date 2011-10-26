package org.mitre.eren.protocol.edxl_rm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;

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
public interface AssignmentInformation extends ExtensibleElement, Element, EDXLRMConstants {

    public QuantityType getQuantity();
    public QuantityType addQuantity();
    public String getRestrictions();
    public void setRestrictions(String value);
    public String getAnticipatedFunction();
    public void setAnticipatedFunction(String value);
    public PriceQuoteType getPriceQuote();
    public PriceQuoteType addPriceQuote();
    public String getOrderID();
    public void setOrderID(String value);
    public AssignmentInstructionsType getAssignmentInstructions();
    public AssignmentInstructionsType addAssignmentInstructions();
}
