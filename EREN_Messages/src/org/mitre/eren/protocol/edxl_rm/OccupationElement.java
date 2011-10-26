package org.mitre.eren.protocol.edxl_rm;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.ExtensibleElement;


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

public interface OccupationElement extends ExtensibleElement, Element,
    EDXLRMConstants {


    public OccupationElementList getType();
    public void setType(OccupationElementList value);
    public Map<QName, String> getOtherAttributes();
}
