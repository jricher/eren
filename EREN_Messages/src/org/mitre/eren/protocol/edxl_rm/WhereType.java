//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.26 at 12:27:28 PM EDT 
//


package org.mitre.eren.protocol.edxl_rm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;
import org.opencare.lib.model.BaseWrapper;


/**
 * Container for optional OASIS GML attributes
 * 
 * <p>Java class for WhereType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WhereType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://www.opengis.net/gml}Point"/>
 *         &lt;element ref="{http://www.opengis.net/gml}LineString"/>
 *         &lt;element ref="{http://www.opengis.net/gml}CircleByCenterPoint"/>
 *         &lt;element ref="{http://www.opengis.net/gml}Polygon"/>
 *         &lt;element ref="{http://www.opengis.net/gml}Envelope"/>
 *       &lt;/choice>
 *       &lt;attGroup ref="{urn:oasis:names:tc:emergency:EDXL:HAVE:1.0:geo-oasis}whereAttrGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WhereType", namespace = "urn:oasis:names:tc:emergency:EDXL:HAVE:1.0:geo-oasis", propOrder = {
    "point",
    "lineString",
    "circleByCenterPoint",
    "polygon",
    "envelope"
})
public class WhereType extends BaseWrapper implements Element, ExtensibleElement, EDXLRMConstants {

    public WhereType(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public WhereType(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }



    /**
     * Gets the value of the point property.
     * 
     * @return
     *     possible object is
     *     {@link PointType }
     *     
     */
    public PointType getPoint() {
       return getExtension(GML_POINT);
    }

    /**
     * Sets the value of the point property.
     * 
     * @param value
     *     allowed object is
     *     {@link PointType }
     *     
     */
    public PointType addPoint() {
        return addExtension(GML_POINT);
    }

//    /**
//     * Gets the value of the lineString property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link LineStringType }
//     *     
//     */
//    public LineStringType getLineString() {
//        return getExtension(GML_POINT);
//    }
//
//    /**
//     * Sets the value of the lineString property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link LineStringType }
//     *     
//     */
//    public void setLineString(LineStringType value) {
//        this.lineString = value;
//    }

//    /**
//     * Gets the value of the circleByCenterPoint property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link CircleByCenterPointType }
//     *     
//     */
//    public CircleByCenterPointType getCircleByCenterPoint() {
//        return circleByCenterPoint;
//    }
//
//    /**
//     * Sets the value of the circleByCenterPoint property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link CircleByCenterPointType }
//     *     
//     */
//    public void setCircleByCenterPoint(CircleByCenterPointType value) {
//        this.circleByCenterPoint = value;
//    }
//
//    /**
//     * Gets the value of the polygon property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link PolygonType }
//     *     
//     */
//    public PolygonType getPolygon() {
//        return polygon;
//    }
//
//    /**
//     * Sets the value of the polygon property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link PolygonType }
//     *     
//     */
//    public void setPolygon(PolygonType value) {
//        this.polygon = value;
//    }
//
//    /**
//     * Gets the value of the envelope property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link EnvelopeType }
//     *     
//     */
//    public EnvelopeType getEnvelope() {
//        return envelope;
//    }
//
//    /**
//     * Sets the value of the envelope property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link EnvelopeType }
//     *     
//     */
//    public void setEnvelope(EnvelopeType value) {
//        this.envelope = value;
//    }
//
//    /**
//     * Gets the value of the featuretypetag property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getFeaturetypetag() {
//        return featuretypetag;
//    }
//
//    /**
//     * Sets the value of the featuretypetag property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setFeaturetypetag(String value) {
//        this.featuretypetag = value;
//    }
//
//    /**
//     * Gets the value of the relationshiptag property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link String }
//     *     
//     */
//    public String getRelationshiptag() {
//        return relationshiptag;
//    }
//
//    /**
//     * Sets the value of the relationshiptag property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link String }
//     *     
//     */
//    public void setRelationshiptag(String value) {
//        this.relationshiptag = value;
//    }
//
//    /**
//     * Gets the value of the elev property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link Double }
//     *     
//     */
//    public Double getElev() {
//        return elev;
//    }
//
//    /**
//     * Sets the value of the elev property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link Double }
//     *     
//     */
//    public void setElev(Double value) {
//        this.elev = value;
//    }
//
//    /**
//     * Gets the value of the floor property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link Double }
//     *     
//     */
//    public Double getFloor() {
//        return floor;
//    }
//
//    /**
//     * Sets the value of the floor property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link Double }
//     *     
//     */
//    public void setFloor(Double value) {
//        this.floor = value;
//    }
//
//    /**
//     * Gets the value of the radius property.
//     * 
//     * @return
//     *     possible object is
//     *     {@link Double }
//     *     
//     */
//    public Double getRadius() {
//        return radius;
//    }
//
//    /**
//     * Sets the value of the radius property.
//     * 
//     * @param value
//     *     allowed object is
//     *     {@link Double }
//     *     
//     */
//    public void setRadius(Double value) {
//        this.radius = value;
//    }

}
