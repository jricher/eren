package org.mitre.eren.protocol.edxl_rm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

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
 *         &lt;element name="InventoryRefreshDateTime" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}DateTimeType" minOccurs="0"/>
 *         &lt;element name="DeploymentStatus" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ValueListType" minOccurs="0"/>
 *         &lt;element name="Availability" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}AvailabilityType" minOccurs="0"/>
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
    "inventoryRefreshDateTime",
    "deploymentStatus",
    "availability"
})
public interface ResourceStatus extends ExtensibleElement, Element, EDXLRMConstants {

    public XMLGregorianCalendar getInventoryRefreshDateTime();
    public void setInventoryRefreshDateTime(XMLGregorianCalendar value);
    public ValueListType getDeploymentStatus();
    public ValueListType addDeploymentStatus();
    public String getAvailability();
    public void setAvailability(String value);
}