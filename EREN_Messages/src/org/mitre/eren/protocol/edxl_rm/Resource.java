package org.mitre.eren.protocol.edxl_rm;

import java.util.List;

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
public interface Resource extends ExtensibleElement, Element, EDXLRMConstants {

    public String getResourceID();
    public void setResourceID(String value);
    public String getName();
    public void setName(String value);
    public ValueListType getTypeStructure();
    public ValueListType addTypeStructure();
    public ExtensibleElement getTypeInfo();
    public ExtensibleElement addTypeInfo();
    public List<ValueListType> getKeyword();
    public ValueListType addKeyword();
    public String getDescription();
    public void setDescription(String value);
    public String getCredentials();
    public void setCredentials(String value);
    public String getCertifications();
    public void setCertifications(String value);
    public String getSpecialRequirements();
    public void setSpecialRequirements(String value);
    public ContactInformationType getResponsibleParty();
    public ContactInformationType addResponsibleParty();
    public OwnershipInformationType getOwnershipInformation();
    public OwnershipInformationType addOwnershipInformation();
    public ResourceStatus getResourceStatus();
    public ResourceStatus addResourceStatus();
    

}

