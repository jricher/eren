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
public interface ResourceInformation extends ExtensibleElement, Element, EDXLRMConstants {

    public String getResourceInfoElementID();
    public void setResourceInfoElementID(String value);
    public ResponseInformationType getResponseInformation();
    public ResponseInformationType addResponseInformation();
    public Resource getResource();
    public Resource addResource();
    public AssignmentInformation getAssignmentInformation();
    public AssignmentInformation addAssignmentInformation();
    public List<ScheduleInformation> getScheduleInformation();
    public ScheduleInformation addScheduleInformation();

}