package org.mitre.eren.protocol.edxl_rm;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;



    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;urn:oasis:names:tc:ciq:ct:3>String">
     *       &lt;attGroup ref="{urn:oasis:names:tc:ciq:ct:3}grAbbreviation"/>
     *       &lt;attribute name="Type" type="{urn:oasis:names:tc:ciq:xnl:3}SubDivisionTypeList" />
     *       &lt;anyAttribute processContents='lax' namespace='##other'/>
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public class SubDivisionName {

        @XmlValue
        @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
        protected String value;
        @XmlAttribute(name = "Type", namespace = "urn:oasis:names:tc:ciq:xnl:3")
        protected SubDivisionTypeList type;
        @XmlAttribute(name = "Abbreviation", namespace = "urn:oasis:names:tc:ciq:ct:3")
        protected Boolean abbreviation;
        @XmlAnyAttribute
        private Map<QName, String> otherAttributes = new HashMap<QName, String>();

        /**
         * Normalized and Collapsed String
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link SubDivisionTypeList }
         *     
         */
        public SubDivisionTypeList getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link SubDivisionTypeList }
         *     
         */
        public void setType(SubDivisionTypeList value) {
            this.type = value;
        }

        /**
         * Gets the value of the abbreviation property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isAbbreviation() {
            return abbreviation;
        }

        /**
         * Sets the value of the abbreviation property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setAbbreviation(Boolean value) {
            this.abbreviation = value;
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