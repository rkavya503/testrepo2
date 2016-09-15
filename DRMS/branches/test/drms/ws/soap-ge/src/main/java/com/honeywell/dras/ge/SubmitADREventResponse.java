
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
 *         &lt;element name="AdrEventResponseStc" type="{ENMAC/ADR}AdrEventResponseStc"/&gt;
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
    "adrEventResponseStc"
})
@XmlRootElement(name = "SubmitADREventResponse")
public class SubmitADREventResponse {

    @XmlElement(name = "AdrEventResponseStc", required = true)
    protected AdrEventResponseStc adrEventResponseStc;

    /**
     * Gets the value of the adrEventResponseStc property.
     * 
     * @return
     *     possible object is
     *     {@link AdrEventResponseStc }
     *     
     */
    public AdrEventResponseStc getAdrEventResponseStc() {
        return adrEventResponseStc;
    }

    /**
     * Sets the value of the adrEventResponseStc property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdrEventResponseStc }
     *     
     */
    public void setAdrEventResponseStc(AdrEventResponseStc value) {
        this.adrEventResponseStc = value;
    }

}
