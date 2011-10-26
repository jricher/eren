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
public class ScheduleInformationWrapper extends BaseWrapper implements ScheduleInformation {

    public ScheduleInformationWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public ScheduleInformationWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }

    /**
     * Gets the value of the scheduleType property.
     * 
     * @return
     *     possible object is
     *     {@link ScheduleTypeType }
     *     
     */
    @Override
	public ScheduleTypeType getScheduleType() {
        String st = getSimpleExtension(RM_SCHEDULETYPE);
        return ScheduleTypeType.fromValue(st);
    }

    /**
     * Sets the value of the scheduleType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScheduleTypeType }
     *     
     */
    @Override
	public void setScheduleType(ScheduleTypeType value) {
        setElementText(RM_SCHEDULETYPE,value.value());
    }

    /**
     * Gets the value of the dateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Override
	public XMLGregorianCalendar getDateTime() {
        String dt = getSimpleExtension(RM_DATETIME);
        return ERENDatatypeFactory.factory.newXMLGregorianCalendar(dt);
    }

    /**
     * Sets the value of the dateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Override
	public void setDateTime(XMLGregorianCalendar value) {
      setElementText(RM_DATETIME,value.toXMLFormat());
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link LocationType }
     *     
     */
    @Override
	public LocationType getLocation() {
      return getExtension(RM_LOCATION);
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocationType }
     *     
     */
    @Override
	public LocationType addLocation() {
        return addExtension(RM_LOCATION);
    }

}
