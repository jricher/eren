package org.mitre.eren.protocol.edxl_rm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
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
public class ResourceStatusWrapper extends BaseWrapper implements ResourceStatus {

    public ResourceStatusWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public ResourceStatusWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }

    /**
     * Gets the value of the inventoryRefreshDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Override
	public XMLGregorianCalendar getInventoryRefreshDateTime() {
      String timestring = getSimpleExtension(RM_INVENTORYREFRESHDATETIME);
      return ERENDatatypeFactory.factory.newXMLGregorianCalendar(timestring);
    }

    /**
     * Sets the value of the inventoryRefreshDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Override
	public void setInventoryRefreshDateTime(XMLGregorianCalendar value) {
      setElementText(RM_INVENTORYREFRESHDATETIME, value.toXMLFormat());
    }

    /**
     * Gets the value of the deploymentStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ValueListType }
     *     
     */
    @Override
	public ValueListType getDeploymentStatus() {
        return getExtension(RM_DEPLOYMENTSTATUS);
    }

    /**
     * Sets the value of the deploymentStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueListType }
     *     
     */
    @Override
	public ValueListType addDeploymentStatus() {
        return addExtension(RM_DEPLOYMENTSTATUS);
    }

    /**
     * Gets the value of the availability property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getAvailability() {
        return getSimpleExtension(RM_AVAILABILITY);
    }

    /**
     * Sets the value of the availability property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setAvailability(String value) {
        setElementText(RM_AVAILABILITY,value);
    }

}
