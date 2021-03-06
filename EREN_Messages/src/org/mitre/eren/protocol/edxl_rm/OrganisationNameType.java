//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.26 at 12:27:28 PM EDT 
//


package org.mitre.eren.protocol.edxl_rm;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;


/**
 * Reusable complex type
 * 
 * <p>Java class for OrganisationNameType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrganisationNameType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NameElement" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;urn:oasis:names:tc:ciq:ct:3>String">
 *                 &lt;attGroup ref="{urn:oasis:names:tc:ciq:ct:3}grAbbreviation"/>
 *                 &lt;attribute name="ElementType" type="{urn:oasis:names:tc:ciq:xnl:3}OrganisationNameElementList" />
 *                 &lt;anyAttribute processContents='lax' namespace='##other'/>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SubDivisionName" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;urn:oasis:names:tc:ciq:ct:3>String">
 *                 &lt;attGroup ref="{urn:oasis:names:tc:ciq:ct:3}grAbbreviation"/>
 *                 &lt;attribute name="Type" type="{urn:oasis:names:tc:ciq:xnl:3}SubDivisionTypeList" />
 *                 &lt;anyAttribute processContents='lax' namespace='##other'/>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:ct:3}grLanguageCode"/>
 *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:ct:3}grValidityDate"/>
 *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:xnl:3}grNameKey"/>
 *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:ct:3}grDataQuality"/>
 *       &lt;attribute name="Type2" type="{urn:oasis:names:tc:ciq:xnl:3}OrganisationNameTypeList" />
 *       &lt;attribute name="OrganisationID" type="{urn:oasis:names:tc:ciq:ct:3}String" />
 *       &lt;attribute name="OrganisationIDType" type="{urn:oasis:names:tc:ciq:xnl:3}OrganisationIDTypeList" />
 *       &lt;attribute name="ID" type="{urn:oasis:names:tc:ciq:ct:3}String" />
 *       &lt;attribute name="Usage" type="{urn:oasis:names:tc:ciq:xnl:3}OrganisationNameUsageList" />
 *       &lt;attribute name="Status" type="{urn:oasis:names:tc:ciq:ct:3}StatusList" />
 *       &lt;attribute ref="{http://www.w3.org/1999/xlink1}type"/>
 *       &lt;attribute ref="{http://www.w3.org/1999/xlink1}label"/>
 *       &lt;attribute ref="{http://www.w3.org/1999/xlink1}href"/>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrganisationNameType", namespace = "urn:oasis:names:tc:ciq:xnl:3", propOrder = {
    "nameElement",
    "subDivisionName"
})
//@XmlSeeAlso({
//    org.mitre.eren.protocol.edxl_rm.Memberships.Membership.Organisation.class,
//    org.mitre.eren.protocol.edxl_rm.Qualifications.Qualification.Institution.class,
//    org.mitre.eren.protocol.edxl_rm.Accounts.Account.Organisation.class,
//    org.mitre.eren.protocol.edxl_rm.Documents.Document.IssuerName.class,
//    org.mitre.eren.protocol.edxl_rm.Occupations.Occupation.Employer.class,
//    org.mitre.eren.protocol.edxl_rm.PartyNameType.OrganisationName.class
//})
public interface OrganisationNameType extends Element, ExtensibleElement, EDXLRMConstants {

    public List<NameElement> getNameElement();
    public NameElement addNameElement();
//    public List<SubDivisionName> getSubDivisionName();
//    public OrganisationNameTypeList getType();
//    public void setType2(OrganisationNameTypeList value);
//    public String getOrganisationID();
//    public void setOrganisationID(String value);
//    public String getOrganisationIDType();
//    public void setOrganisationIDType(String value);
//    public String getID();
//    public void setID(String value);
//    public String getUsage();
//    public void setUsage(String value);
//    public String getStatus();
//    public void setStatus(String value);
//    public String getTypeAttr();
//    public void setTypeAttr(String value);
//    public String getLabel();
//    public void setLabel(String value);
//    public String getHref();
//    public void setHref(String value);
//    public String getLanguageCode();
//    public void setLanguageCode(String value);
//    public XMLGregorianCalendar getDateValidFrom();
//    public void setDateValidFrom(XMLGregorianCalendar value);
//    public XMLGregorianCalendar getDateValidTo();
//    public void setDateValidTo(XMLGregorianCalendar value);
//    public String getNameKey();
//    public void setNameKey(String value);
//    public String getNameKeyRef();
//    public void setNameKeyRef(String value);
//    public DataQualityTypeList getDataQualityType();
//    public void setDataQualityType(DataQualityTypeList value);
//    public XMLGregorianCalendar getValidFrom();
//    public void setValidFrom(XMLGregorianCalendar value);
//    public XMLGregorianCalendar getValidTo();
//    public void setValidTo(XMLGregorianCalendar value);
//    public Map<QName, String> getOtherAttributes();

}
