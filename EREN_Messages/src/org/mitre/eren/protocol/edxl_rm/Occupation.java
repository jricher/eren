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
 *         &lt;element name="OccupationElement" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;urn:oasis:names:tc:ciq:ct:3>String">
 *                 &lt;attribute name="Type" type="{urn:oasis:names:tc:ciq:xpil:3}OccupationElementList" />
 *                 &lt;anyAttribute processContents='lax' namespace='##other'/>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Employer" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{urn:oasis:names:tc:ciq:xnl:3}OrganisationNameType">
 *                 &lt;anyAttribute processContents='lax' namespace='##other'/>
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:ct:3}grDataQuality"/>
 *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:ct:3}grValidityDate"/>
 *       &lt;attribute name="isSelfEmployed" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="Status" type="{urn:oasis:names:tc:ciq:ct:3}StatusList" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "occupationElement",
    "employer"
})

public interface Occupation extends Element, ExtensibleElement, EDXLRMConstants {
 

  public List<OccupationElement> getOccupationElement();
  public OccupationElement addOccupationElement();
  public Employer getEmployer();
  public Employer addEmployer();
  
//  public void setEmployer(Employer value);
//  public Boolean isIsSelfEmployed();
//  public void setIsSelfEmployed(Boolean value);
//  public String getStatus();
//  public void setStatus(String value);
//  public DataQualityTypeList getDataQualityType();
//  public void setDataQualityType(DataQualityTypeList value);
//  public XMLGregorianCalendar getValidFrom();
//  public void setValidFrom(XMLGregorianCalendar value);
//  public XMLGregorianCalendar getValidTo();
//  public void setValidTo(XMLGregorianCalendar value);
//  public XMLGregorianCalendar getDateValidFrom();
//  public void setDateValidFrom(XMLGregorianCalendar value);
//  public XMLGregorianCalendar getDateValidTo();
//  public void setDateValidTo(XMLGregorianCalendar value);
//  public Map<QName, String> getOtherAttributes();


}


