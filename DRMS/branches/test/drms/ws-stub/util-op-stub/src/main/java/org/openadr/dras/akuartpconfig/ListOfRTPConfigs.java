/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuartpconfig.ListOfRTPConfigs.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuartpconfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListOfRTPConfigs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfRTPConfigs">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="priceTable" type="{http://www.openadr.org/DRAS/AkuaRTPConfig}AkuaRTPConfig" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfRTPConfigs", propOrder = {
    "priceTable"
})
public class ListOfRTPConfigs
    implements Serializable
{

    /** The price table. */
    protected List<AkuaRTPConfig> priceTable;

    /**
     * Gets the value of the priceTable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the priceTable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getPriceTable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AkuaRTPConfig }
     * 
     * @return the price table
     */
    public List<AkuaRTPConfig> getPriceTable() {
        if (priceTable == null) {
            priceTable = new ArrayList<AkuaRTPConfig>();
        }
        return this.priceTable;
    }

}
