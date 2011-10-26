package org.mitre.eren.protocol.edxl_rm;

import java.util.List;

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
 *         &lt;element name="ResourceInfoElementID" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ResourceInfoElementIDType"/>
 *         &lt;element name="ResponseInformation" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ResponseInformationType" minOccurs="0"/>
 *         &lt;element name="Resource" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ResourceID" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ResourceIDType" minOccurs="0"/>
 *                   &lt;element name="Name" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ResourceNameType" minOccurs="0"/>
 *                   &lt;element name="TypeStructure" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ValueListType" minOccurs="0"/>
 *                   &lt;element name="TypeInfo" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}TypeInfoType" minOccurs="0"/>
 *                   &lt;element name="Keyword" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ValueListType" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="Description" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}DescriptionType" minOccurs="0"/>
 *                   &lt;element name="Credentials" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}CredentialsType" minOccurs="0"/>
 *                   &lt;element name="Certifications" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}CertificationsType" minOccurs="0"/>
 *                   &lt;element name="SpecialRequirements" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}SpecialRequirementsType" minOccurs="0"/>
 *                   &lt;element name="ResponsibleParty" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ContactInformationType" minOccurs="0"/>
 *                   &lt;element name="OwnershipInformation" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}OwnershipInformationType" minOccurs="0"/>
 *                   &lt;element name="ResourceStatus" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="InventoryRefreshDateTime" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}DateTimeType" minOccurs="0"/>
 *                             &lt;element name="DeploymentStatus" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ValueListType" minOccurs="0"/>
 *                             &lt;element name="Availability" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}AvailabilityType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AssignmentInformation" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Quantity" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}QuantityType" minOccurs="0"/>
 *                   &lt;element name="Restrictions" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}RestrictionsType" minOccurs="0"/>
 *                   &lt;element name="AnticipatedFunction" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}AnticipatedFunctionType" minOccurs="0"/>
 *                   &lt;element name="PriceQuote" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}PriceQuoteType" minOccurs="0"/>
 *                   &lt;element name="OrderID" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}OrderIDType" minOccurs="0"/>
 *                   &lt;element name="AssignmentInstructions" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}AssignmentInstructionsType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ScheduleInformation" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ScheduleType">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ScheduleTypeType">
 *                         &lt;enumeration value="RequestedArrival"/>
 *                         &lt;enumeration value="EstimatedArrival"/>
 *                         &lt;enumeration value="ActualArrival"/>
 *                         &lt;enumeration value="RequestedDeparture"/>
 *                         &lt;enumeration value="EstimatedDeparture"/>
 *                         &lt;enumeration value="ActualDeparture"/>
 *                         &lt;enumeration value="RequestedReturnDeparture"/>
 *                         &lt;enumeration value="EstimatedReturnDeparture"/>
 *                         &lt;enumeration value="ActualReturnDeparture"/>
 *                         &lt;enumeration value="RequestedReturnArrival"/>
 *                         &lt;enumeration value="EstimatedReturnArrival"/>
 *                         &lt;enumeration value="ActualReturnArrival"/>
 *                         &lt;enumeration value="BeginAvailable"/>
 *                         &lt;enumeration value="EndAvailable"/>
 *                         &lt;enumeration value="Committed"/>
 *                         &lt;enumeration value="Current"/>
 *                         &lt;enumeration value="ReportTo"/>
 *                         &lt;enumeration value="Route"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="DateTime" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}DateTimeType" minOccurs="0"/>
 *                   &lt;element name="Location" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}LocationType" minOccurs="0"/>
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
    "resourceInfoElementID",
    "responseInformation",
    "resource",
    "assignmentInformation",
    "scheduleInformation"
})
public class ResourceInformationWrapper extends BaseWrapper implements ResourceInformation {

     public ResourceInformationWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public ResourceInformationWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }

    /**
     * Gets the value of the resourceInfoElementID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Override
	public String getResourceInfoElementID() {
      return getSimpleExtension(RM_RESOURCEINFOELEMENTID);
    }

    /**
     * Sets the value of the resourceInfoElementID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Override
	public void setResourceInfoElementID(String value) {
        setElementText(RM_RESOURCEINFOELEMENTID,value);
    }

    /**
     * Gets the value of the responseInformation property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseInformationType }
     *     
     */
    @Override
	public ResponseInformationType getResponseInformation() {
        return getExtension(RM_RESPONSEINFORMATION);
    }

    /**
     * Sets the value of the responseInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseInformationType }
     *     
     */
    @Override
	public ResponseInformationType addResponseInformation() {
        return addExtension(RM_RESPONSEINFORMATION);
    }

    /**
     * Gets the value of the resource property.
     * 
     * @return
     *     possible object is
     *     {@link ReportResourceDeploymentStatusWrapper.ResourceInformation.Resource }
     *     
     */
    @Override
	public Resource getResource() {
        return getExtension(RM_RESOURCE);
    }

    /**
     * Sets the value of the resource property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportResourceDeploymentStatusWrapper.ResourceInformation.Resource }
     *     
     */
    @Override
	public Resource addResource() {
      return addExtension(RM_RESOURCE);
    }

    /**
     * Gets the value of the assignmentInformation property.
     * 
     * @return
     *     possible object is
     *     {@link ReportResourceDeploymentStatusWrapper.ResourceInformation.AssignmentInformation }
     *     
     */
    @Override
	public AssignmentInformation getAssignmentInformation() {
        return getExtension(RM_ASSIGNMENTINFORMATION);
    }

    /**
     * Sets the value of the assignmentInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportResourceDeploymentStatusWrapper.ResourceInformation.AssignmentInformation }
     *     
     */
    @Override
	public AssignmentInformation addAssignmentInformation() {
      return addExtension(RM_ASSIGNMENTINFORMATION);
    }

    /**
     * Gets the value of the scheduleInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scheduleInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScheduleInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReportResourceDeploymentStatusWrapper.ResourceInformation.ScheduleInformation }
     * 
     * 
     */
    @Override
	public List<ScheduleInformation> getScheduleInformation() {
      return getExtensions(RM_SCHEDULEINFORMATION);
    }
    
    @Override
	public ScheduleInformation addScheduleInformation() { 
      return addExtension(RM_SCHEDULEINFORMATION);
    }
}

