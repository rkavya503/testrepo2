/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuadrasclientdata.AkuaDRASSearchHandler.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuadrasclientdata;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AkuaDRASSearchHandler complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AkuaDRASSearchHandler">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="sortField" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="sortByDirection" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="searchCriteriaList" type="{http://www.openadr.org/DRAS/AkuaDRASClientData}ListofSearchCriteria" minOccurs="0"/>
 * &lt;element name="drasCursor" type="{http://www.openadr.org/DRAS/AkuaDRASClientData}AkuaDRASCursor" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AkuaDRASSearchHandler", propOrder = {
    "sortField",
    "sortByDirection",
    "searchCriteriaList",
    "drasCursor"
})
public class AkuaDRASSearchHandler
    implements Serializable
{

    /** The sort field. */
    @XmlElement(required = true, nillable = true)
    protected String sortField;
    
    /** The sort by direction. */
    @XmlElement(required = true, nillable = true)
    protected String sortByDirection;
    
    /** The search criteria list. */
    @XmlElement(nillable = true)
    protected ListofSearchCriteria searchCriteriaList;
    
    /** The dras cursor. */
    @XmlElement(nillable = true)
    protected AkuaDRASCursor drasCursor;

    /**
     * Gets the value of the sortField property.
     * 
     * @return the sort field
     * 
     * possible object is
     * {@link String }
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * Sets the value of the sortField property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setSortField(String value) {
        this.sortField = value;
    }

    /**
     * Gets the value of the sortByDirection property.
     * 
     * @return the sort by direction
     * 
     * possible object is
     * {@link String }
     */
    public String getSortByDirection() {
        return sortByDirection;
    }

    /**
     * Sets the value of the sortByDirection property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setSortByDirection(String value) {
        this.sortByDirection = value;
    }

    /**
     * Gets the value of the searchCriteriaList property.
     * 
     * @return the search criteria list
     * 
     * possible object is
     * {@link ListofSearchCriteria }
     */
    public ListofSearchCriteria getSearchCriteriaList() {
        return searchCriteriaList;
    }

    /**
     * Sets the value of the searchCriteriaList property.
     * 
     * @param value allowed object is
     * {@link ListofSearchCriteria }
     */
    public void setSearchCriteriaList(ListofSearchCriteria value) {
        this.searchCriteriaList = value;
    }

    /**
     * Gets the value of the drasCursor property.
     * 
     * @return the dras cursor
     * 
     * possible object is
     * {@link AkuaDRASCursor }
     */
    public AkuaDRASCursor getDrasCursor() {
        return drasCursor;
    }

    /**
     * Sets the value of the drasCursor property.
     * 
     * @param value allowed object is
     * {@link AkuaDRASCursor }
     */
    public void setDrasCursor(AkuaDRASCursor value) {
        this.drasCursor = value;
    }

}
