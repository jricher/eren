package org.mitre.eren.protocol.edxl_rm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;
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
 *         &lt;element name="ResourceID" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ResourceIDType" minOccurs="0"/>
 *         &lt;element name="Name" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ResourceNameType" minOccurs="0"/>
 *         &lt;element name="TypeStructure" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ValueListType" minOccurs="0"/>
 *         &lt;element name="TypeInfo" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}TypeInfoType" minOccurs="0"/>
 *         &lt;element name="Keyword" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ValueListType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Description" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}DescriptionType" minOccurs="0"/>
 *         &lt;element name="Credentials" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}CredentialsType" minOccurs="0"/>
 *         &lt;element name="Certifications" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}CertificationsType" minOccurs="0"/>
 *         &lt;element name="SpecialRequirements" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}SpecialRequirementsType" minOccurs="0"/>
 *         &lt;element name="ResponsibleParty" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ContactInformationType" minOccurs="0"/>
 *         &lt;element name="OwnershipInformation" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}OwnershipInformationType" minOccurs="0"/>
 *         &lt;element name="ResourceStatus" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="InventoryRefreshDateTime" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}DateTimeType" minOccurs="0"/>
 *                   &lt;element name="DeploymentStatus" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ValueListType" minOccurs="0"/>
 *                   &lt;element name="Availability" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}AvailabilityType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "resourceID",
    "name",
    "typeStructure",
    "typeInfo",
    "keyword",
    "description",
    "credentials",
    "certifications",
    "specialRequirements",
    "responsibleParty",
    "ownershipInformation",
    "resourceStatus"
})
public class ResourceWrapper extends BaseWrapper implements Resource {


    public ResourceWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public ResourceWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }

    /**
     * Gets the value of the resourceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getResourceID() {
        return getSimpleExtension(RM_RESOURCEID);
    }

    /**
     * Sets the value of the resourceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setResourceID(String value) {
        setElementText(RM_RESOURCEID, value);
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getName() {
        return getSimpleExtension(RM_RESOURCENAME);
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setName(String value) {
        setElementText(RM_RESOURCENAME,value);
    }

    /**
     * Gets the value of the typeStructure property.
     * 
     * @return
     *     possible object is
     *     {@link ValueListType }
     *     
     */
    @Override
	public ValueListType getTypeStructure() {
        return getExtension(RM_TYPESTRUCTURE);
    }

    /**
     * Sets the value of the typeStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueListType }
     *     
     */
    @Override
	public ValueListType addTypeStructure() {
        return addExtension(RM_TYPESTRUCTURE);
    }

    /**
     * Gets the value of the typeInfo property.
     * 
     * @return
     *     possible object is
     *     {@link TypeInfoType }
     *     
     */
    @Override
	public ExtensibleElement getTypeInfo() {
        return getExtension(RM_TYPEINFO);
    }

    /**
     * Sets the value of the typeInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeInfoType }
     *     
     */
    @Override
	public ExtensibleElement addTypeInfo() {
        return addExtension(RM_TYPEINFO);
    }

    /**
     * Gets the value of the keyword property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keyword property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeyword().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueListType }
     * 
     * 
     */
    @Override
	public List<ValueListType> getKeyword() {
        return getExtensions(RM_KEYWORD);
    }
    
    @Override
	public ValueListType addKeyword() { 
      return addExtension(RM_KEYWORD);
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getDescription() {
        return getSimpleExtension(RM_DESCRIPTION);
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setDescription(String value) {
        setElementText(RM_DESCRIPTION,value);
    }

    /**
     * Gets the value of the credentials property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getCredentials() {
        return getSimpleExtension(RM_CREDENTIALS);
    }

    /**
     * Sets the value of the credentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setCredentials(String value) {
        setElementText(RM_CREDENTIALS,value);
    }

    /**
     * Gets the value of the certifications property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getCertifications() {
      return getSimpleExtension(RM_CERTIFICATIONS);
    }

    /**
     * Sets the value of the certifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setCertifications(String value) {
        setElementText(RM_CERTIFICATIONS,value);
    }

    /**
     * Gets the value of the specialRequirements property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getSpecialRequirements() {
        return getSimpleExtension(RM_SPECIALREQUIREMENTS);
    }

    /**
     * Sets the value of the specialRequirements property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setSpecialRequirements(String value) {
      setElementText(RM_SPECIALREQUIREMENTS,value);
    }

    /**
     * Gets the value of the responsibleParty property.
     * 
     * @return
     *     possible object is
     *     {@link ContactInformationType }
     *     
     */
    @Override
	public ContactInformationType getResponsibleParty() {
        return getExtension(RM_RESPONSIBLEPARTY);
    }

    /**
     * Sets the value of the responsibleParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactInformationType }
     *     
     */
    @Override
	public ContactInformationType addResponsibleParty() {
        return addExtension(RM_RESPONSIBLEPARTY);
    }

    /**
     * Gets the value of the ownershipInformation property.
     * 
     * @return
     *     possible object is
     *     {@link OwnershipInformationType }
     *     
     */
    @Override
	public OwnershipInformationType getOwnershipInformation() {
        return getExtension(RM_OWNERSHIPINFORMATION);
    }

    /**
     * Sets the value of the ownershipInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link OwnershipInformationType }
     *     
     */
    @Override
	public OwnershipInformationType addOwnershipInformation() {
        return addExtension(RM_OWNERSHIPINFORMATION);
    }

    /**
     * Gets the value of the resourceStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ReportResourceDeploymentStatusWrapper.ResourceInformation.Resource.ResourceStatus }
     *     
     */
    @Override
	public ResourceStatus getResourceStatus() {
        return getExtension(RM_RESOURCESTATUS);
    }

    /**
     * Sets the value of the resourceStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportResourceDeploymentStatusWrapper.ResourceInformation.Resource.ResourceStatus }
     *     
     */
    @Override
	public ResourceStatus addResourceStatus() {
        return addExtension(RM_RESOURCESTATUS);
    }
}
