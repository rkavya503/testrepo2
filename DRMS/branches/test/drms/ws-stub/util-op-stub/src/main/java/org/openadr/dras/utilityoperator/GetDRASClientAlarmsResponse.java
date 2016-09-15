/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.GetDRASClientAlarmsResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.logs.ListOfDRASClientAlarms;


/**
 * <p>Java class for GetDRASClientAlarmsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDRASClientAlarmsResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="alarms" type="{http://www.openadr.org/DRAS/Logs}ListOfDRASClientAlarms" minOccurs="0"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDRASClientAlarmsResponse", propOrder = {
    "alarms",
    "returnValue"
})
public class GetDRASClientAlarmsResponse
    implements Serializable
{

    /** The alarms. */
    @XmlElement(nillable = true)
    protected ListOfDRASClientAlarms alarms;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

    /**
     * Gets the value of the alarms property.
     * 
     * @return the alarms
     * 
     * possible object is
     * {@link ListOfDRASClientAlarms }
     */
    public ListOfDRASClientAlarms getAlarms() {
        return alarms;
    }

    /**
     * Sets the value of the alarms property.
     * 
     * @param value allowed object is
     * {@link ListOfDRASClientAlarms }
     */
    public void setAlarms(ListOfDRASClientAlarms value) {
        this.alarms = value;
    }

    /**
     * Gets the value of the returnValue property.
     * 
     * @return the return value
     * 
     * possible object is
     * {@link String }
     */
    public String getReturnValue() {
        return returnValue;
    }

    /**
     * Sets the value of the returnValue property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setReturnValue(String value) {
        this.returnValue = value;
    }

}
