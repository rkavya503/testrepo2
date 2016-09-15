/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuaeventinfo.Values.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuaeventinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * These are the actual values. Note that if there
 * are a schedule of values then there will be more
 * than one value defined in the list. In addition
 * if the schdule associated with the values is
 * dynamic then the schedule slots associated with
 * the values is also specified.
 * 
 * 
 * <p>Java class for Values complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Values">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="value" type="{http://www.openadr.org/DRAS/AkuaEventInfo}EventInfoValue" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Values", propOrder = {
    "value"
})
public class Values
    implements Serializable
{

    /** The value. */
    @XmlElement(nillable = true)
    protected List<EventInfoValue> value;

    /**
     * Gets the value of the value property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the value property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventInfoValue }
     * 
     * @return the value
     */
    public List<EventInfoValue> getValue() {
        if (value == null) {
            value = new ArrayList<EventInfoValue>();
        }
        return this.value;
    }

}
