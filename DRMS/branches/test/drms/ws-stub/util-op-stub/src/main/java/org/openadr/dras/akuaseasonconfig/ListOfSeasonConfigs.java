/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuaseasonconfig.ListOfSeasonConfigs.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuaseasonconfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListOfSeasonConfigs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfSeasonConfigs">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="seasonTable" type="{http://www.openadr.org/DRAS/AkuaSeasonConfig}AkuaSeasonConfig" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfSeasonConfigs", propOrder = {
    "seasonTable"
})
public class ListOfSeasonConfigs
    implements Serializable
{

    /** The season table. */
    protected List<AkuaSeasonConfig> seasonTable;

    /**
     * Gets the value of the seasonTable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the seasonTable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getSeasonTable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AkuaSeasonConfig }
     * 
     * @return the season table
     */
    public List<AkuaSeasonConfig> getSeasonTable() {
        if (seasonTable == null) {
            seasonTable = new ArrayList<AkuaSeasonConfig>();
        }
        return this.seasonTable;
    }

}
