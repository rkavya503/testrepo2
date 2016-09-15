/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.optoutstate.ListOfOptOutStates.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.optoutstate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This represents a list of OptOutStates.
 * 
 * 
 * <p>Java class for ListOfOptOutStates complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfOptOutStates">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="optOutState" type="{http://www.openadr.org/DRAS/OptOutState}OptOutState" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfOptOutStates", propOrder = {
    "optOutState"
})
public class ListOfOptOutStates
    implements Serializable
{

    /** The opt out state. */
    @XmlElement(nillable = true)
    protected List<OptOutState> optOutState;

    /**
     * Gets the value of the optOutState property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the optOutState property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getOptOutState().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OptOutState }
     * 
     * @return the opt out state
     */
    public List<OptOutState> getOptOutState() {
        if (optOutState == null) {
            optOutState = new ArrayList<OptOutState>();
        }
        return this.optOutState;
    }

}
