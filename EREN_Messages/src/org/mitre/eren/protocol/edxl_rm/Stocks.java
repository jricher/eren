//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.26 at 12:27:28 PM EDT 
//


package org.mitre.eren.protocol.edxl_rm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


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
 *         &lt;element name="Stock" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attGroup ref="{urn:oasis:names:tc:ciq:ct:3}grDataQuality"/>
 *                 &lt;attribute name="ListedCode" type="{urn:oasis:names:tc:ciq:ct:3}String" />
 *                 &lt;attribute name="MarketName" type="{urn:oasis:names:tc:ciq:ct:3}String" />
 *                 &lt;attribute name="CountryName" type="{urn:oasis:names:tc:ciq:ct:3}String" />
 *                 &lt;attribute name="InvestedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                 &lt;attribute name="ShareQuantity" type="{urn:oasis:names:tc:ciq:ct:3}String" />
 *                 &lt;attribute name="ListedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                 &lt;anyAttribute processContents='lax' namespace='##other'/>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
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
    "stock"
})
@XmlRootElement(name = "Stocks")
public class Stocks {

    @XmlElement(name = "Stock", required = true)
    protected List<Stocks.Stock> stock;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the stock property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stock property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStock().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Stocks.Stock }
     * 
     * 
     */
    public List<Stocks.Stock> getStock() {
        if (stock == null) {
            stock = new ArrayList<Stocks.Stock>();
        }
        return this.stock;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:ct:3}grDataQuality"/>
     *       &lt;attribute name="ListedCode" type="{urn:oasis:names:tc:ciq:ct:3}String" />
     *       &lt;attribute name="MarketName" type="{urn:oasis:names:tc:ciq:ct:3}String" />
     *       &lt;attribute name="CountryName" type="{urn:oasis:names:tc:ciq:ct:3}String" />
     *       &lt;attribute name="InvestedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *       &lt;attribute name="ShareQuantity" type="{urn:oasis:names:tc:ciq:ct:3}String" />
     *       &lt;attribute name="ListedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *       &lt;anyAttribute processContents='lax' namespace='##other'/>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Stock {

        @XmlAttribute(name = "ListedCode", namespace = "urn:oasis:names:tc:ciq:xpil:3")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String listedCode;
        @XmlAttribute(name = "MarketName", namespace = "urn:oasis:names:tc:ciq:xpil:3")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String marketName;
        @XmlAttribute(name = "CountryName", namespace = "urn:oasis:names:tc:ciq:xpil:3")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String countryName;
        @XmlAttribute(name = "InvestedDate", namespace = "urn:oasis:names:tc:ciq:xpil:3")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar investedDate;
        @XmlAttribute(name = "ShareQuantity", namespace = "urn:oasis:names:tc:ciq:xpil:3")
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String shareQuantity;
        @XmlAttribute(name = "ListedDate", namespace = "urn:oasis:names:tc:ciq:xpil:3")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar listedDate;
        @XmlAttribute(name = "DataQualityType", namespace = "urn:oasis:names:tc:ciq:ct:3")
        protected DataQualityTypeList dataQualityType;
        @XmlAttribute(name = "ValidFrom", namespace = "urn:oasis:names:tc:ciq:ct:3")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar validFrom;
        @XmlAttribute(name = "ValidTo", namespace = "urn:oasis:names:tc:ciq:ct:3")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar validTo;
        @XmlAnyAttribute
        private Map<QName, String> otherAttributes = new HashMap<QName, String>();

        /**
         * Gets the value of the listedCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getListedCode() {
            return listedCode;
        }

        /**
         * Sets the value of the listedCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setListedCode(String value) {
            this.listedCode = value;
        }

        /**
         * Gets the value of the marketName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMarketName() {
            return marketName;
        }

        /**
         * Sets the value of the marketName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMarketName(String value) {
            this.marketName = value;
        }

        /**
         * Gets the value of the countryName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCountryName() {
            return countryName;
        }

        /**
         * Sets the value of the countryName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCountryName(String value) {
            this.countryName = value;
        }

        /**
         * Gets the value of the investedDate property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getInvestedDate() {
            return investedDate;
        }

        /**
         * Sets the value of the investedDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setInvestedDate(XMLGregorianCalendar value) {
            this.investedDate = value;
        }

        /**
         * Gets the value of the shareQuantity property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getShareQuantity() {
            return shareQuantity;
        }

        /**
         * Sets the value of the shareQuantity property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setShareQuantity(String value) {
            this.shareQuantity = value;
        }

        /**
         * Gets the value of the listedDate property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getListedDate() {
            return listedDate;
        }

        /**
         * Sets the value of the listedDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setListedDate(XMLGregorianCalendar value) {
            this.listedDate = value;
        }

        /**
         * Gets the value of the dataQualityType property.
         * 
         * @return
         *     possible object is
         *     {@link DataQualityTypeList }
         *     
         */
        public DataQualityTypeList getDataQualityType() {
            return dataQualityType;
        }

        /**
         * Sets the value of the dataQualityType property.
         * 
         * @param value
         *     allowed object is
         *     {@link DataQualityTypeList }
         *     
         */
        public void setDataQualityType(DataQualityTypeList value) {
            this.dataQualityType = value;
        }

        /**
         * Gets the value of the validFrom property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getValidFrom() {
            return validFrom;
        }

        /**
         * Sets the value of the validFrom property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setValidFrom(XMLGregorianCalendar value) {
            this.validFrom = value;
        }

        /**
         * Gets the value of the validTo property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getValidTo() {
            return validTo;
        }

        /**
         * Sets the value of the validTo property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setValidTo(XMLGregorianCalendar value) {
            this.validTo = value;
        }

        /**
         * Gets a map that contains attributes that aren't bound to any typed property on this class.
         * 
         * <p>
         * the map is keyed by the name of the attribute and 
         * the value is the string value of the attribute.
         * 
         * the map returned by this method is live, and you can add new attribute
         * by updating the map directly. Because of this design, there's no setter.
         * 
         * 
         * @return
         *     always non-null
         */
        public Map<QName, String> getOtherAttributes() {
            return otherAttributes;
        }

    }

}
