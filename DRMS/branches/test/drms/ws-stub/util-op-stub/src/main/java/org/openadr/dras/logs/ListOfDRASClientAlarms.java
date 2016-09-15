/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.logs.ListOfDRASClientAlarms.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.logs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Represents a list of DRASClientAlarms.
 * 
 * 
 * <p>Java class for ListOfDRASClientAlarms complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfDRASClientAlarms">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="drasClientAlarm" type="{http://www.openadr.org/DRAS/Logs}DRASClientAlarm" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfDRASClientAlarms", propOrder = {
    "drasClientAlarm"
})
public class ListOfDRASClientAlarms
    implements Serializable
{

    /** The dras client alarm. */
    @XmlElement(nillable = true)
    protected List<DRASClientAlarm> drasClientAlarm;

    /**
     * Gets the value of the drasClientAlarm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the drasClientAlarm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getDrasClientAlarm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DRASClientAlarm }
     * 
     * @return the dras client alarm
     */
    public List<DRASClientAlarm> getDrasClientAlarm() {
        if (drasClientAlarm == null) {
            drasClientAlarm = new ArrayList<DRASClientAlarm>();
        }
        return this.drasClientAlarm;
    }

}
