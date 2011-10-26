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

public class OccupationWrapper extends BaseWrapper implements Occupation {
  
  public OccupationWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public OccupationWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }
  
  @Override
public Employer getEmployer() {
    return getExtension(XPIL_EMPLOYER);
  }

  @Override
public Employer addEmployer() {
    return addExtension(XPIL_EMPLOYER);
  }
  



  /**
   * Gets the value of the occupationElement property.
   * 
   * 
   */
  @Override
public List<OccupationElement> getOccupationElement() {
    return getExtensions(XPIL_OCCUPATIONELEMENT);
  }
  
  @Override
public OccupationElement addOccupationElement() { 
    return addExtension(XPIL_OCCUPATIONELEMENT);
  }


}


