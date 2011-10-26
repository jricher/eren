package org.mitre.eren.protocol.edxl_rm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;urn:oasis:names:tc:ciq:ct:3>String">
 *       &lt;attribute name="Type" type="{urn:oasis:names:tc:ciq:xpil:3}OccupationElementList" />
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

public class OccupationElementWrapper extends BaseWrapper implements OccupationElement {

    public OccupationElementWrapper(Element internal) {
    super(internal);
    // TODO Auto-generated constructor stub
  }

  public OccupationElementWrapper(Factory factory, QName qname) {
    super(factory, qname);
    // TODO Auto-generated constructor stub
  }


    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link OccupationElementList }
     *     
     */
    @Override
	public OccupationElementList getType() {
      String type = getAttributeValue(XPIL_TYPE);
      return OccupationElementList.fromValue(type);
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link OccupationElementList }
     *     
     */
    @Override
	public void setType(OccupationElementList value) {
      setAttributeValue(XPIL_TYPE,value.value());
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
    @Override
	public Map<QName, String> getOtherAttributes() {
      List<QName> attr = getExtensionAttributes();
      Map<QName,String> values = new HashMap<QName,String>();
      for (QName a : attr) { 
        values.put(a,getAttributeValue(a));
      }
      return values;
    }
}
