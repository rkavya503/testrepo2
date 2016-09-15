/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuadrasclientdata.ListofSearchCriteria.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuadrasclientdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListofSearchCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListofSearchCriteria">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="searchCriteriaList" type="{http://www.openadr.org/DRAS/AkuaDRASClientData}AkuaDRASSearchCriteria" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListofSearchCriteria", propOrder = {
    "searchCriteriaList"
})
public class ListofSearchCriteria
    implements Serializable
{

    /** The search criteria list. */
    protected List<AkuaDRASSearchCriteria> searchCriteriaList;

    /**
     * Gets the value of the searchCriteriaList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the searchCriteriaList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getSearchCriteriaList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AkuaDRASSearchCriteria }
     * 
     * @return the search criteria list
     */
    public List<AkuaDRASSearchCriteria> getSearchCriteriaList() {
        if (searchCriteriaList == null) {
            searchCriteriaList = new ArrayList<AkuaDRASSearchCriteria>();
        }
        return this.searchCriteriaList;
    }

}
