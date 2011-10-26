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
 *         &lt;element name="ScheduleType">
 *           &lt;simpleType>
 *             &lt;restriction base="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}ScheduleTypeType">
 *               &lt;enumeration value="RequestedArrival"/>
 *               &lt;enumeration value="EstimatedArrival"/>
 *               &lt;enumeration value="ActualArrival"/>
 *               &lt;enumeration value="RequestedDeparture"/>
 *               &lt;enumeration value="EstimatedDeparture"/>
 *               &lt;enumeration value="ActualDeparture"/>
 *               &lt;enumeration value="RequestedReturnDeparture"/>
 *               &lt;enumeration value="EstimatedReturnDeparture"/>
 *               &lt;enumeration value="ActualReturnDeparture"/>
 *               &lt;enumeration value="RequestedReturnArrival"/>
 *               &lt;enumeration value="EstimatedReturnArrival"/>
 *               &lt;enumeration value="ActualReturnArrival"/>
 *               &lt;enumeration value="BeginAvailable"/>
 *               &lt;enumeration value="EndAvailable"/>
 *               &lt;enumeration value="Committed"/>
 *               &lt;enumeration value="Current"/>
 *               &lt;enumeration value="ReportTo"/>
 *               &lt;enumeration value="Route"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DateTime" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}DateTimeType" minOccurs="0"/>
 *         &lt;element name="Location" type="{urn:oasis:names:tc:emergency:EDXL:RM:1.0}LocationType" minOccurs="0"/>
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
    "scheduleType",
    "dateTime",
    "location"
})
public interface ScheduleInformation extends ExtensibleElement, Element, EDXLRMConstants {

    public ScheduleTypeType getScheduleType();
    public void setScheduleType(ScheduleTypeType value);
    public XMLGregorianCalendar getDateTime();
    public void setDateTime(XMLGregorianCalendar value);
    public LocationType getLocation();
    public LocationType addLocation();

}
