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

import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;


/**
 * <p>Java class for MessageRecallType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageRecallType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RecallMessageID" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}MessageIDType"/>
 *         &lt;element name="RecallType" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}RecallTypeType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageRecallType", namespace = "urn:oasis:names:tc:emergency:EDXL:RM:1.0", propOrder = {
    "recallMessageID",
    "recallType"
})
public interface MessageRecallType extends Element, ExtensibleElement, EDXLRMConstants {

    public String getRecallMessageID();
    public void setRecallMessageID(String value);
    public RecallTypeType getRecallType();
    public void setRecallType(RecallTypeType value);
}