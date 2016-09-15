
package com.honeywell.dras.ge;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AdrEventStc" type="{ENMAC/ADR}AdrEventStc"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "adrEventStc"
})
@XmlRootElement(name = "SubmitADREvent")
public class SubmitADREvent {

    @XmlElement(name = "AdrEventStc", required = true)
    protected AdrEventStc adrEventStc;

    /**
     * Gets the value of the adrEventStc property.
     * 
     * @return
     *     possible object is
     *     {@link AdrEventStc }
     *     
     */
    public AdrEventStc getAdrEventStc() {
        return adrEventStc;
    }

    /**
     * Sets the value of the adrEventStc property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdrEventStc }
     *     
     */
    public void setAdrEventStc(AdrEventStc value) {
        this.adrEventStc = value;
    }

}
