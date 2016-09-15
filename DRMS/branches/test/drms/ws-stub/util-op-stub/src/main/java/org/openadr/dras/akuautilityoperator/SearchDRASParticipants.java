/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityoperator.SearchDRASParticipants.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.akuadrasclientdata.AkuaDRASSearchHandler;
import org.openadr.dras.utilityprogram.ListOfProgramNames;


/**
 * <p>Java class for SearchDRASParticipants complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchDRASParticipants">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="searchHandler" type="{http://www.openadr.org/DRAS/AkuaDRASClientData}AkuaDRASSearchHandler" minOccurs="0"/>
 * &lt;element name="programNames" type="{http://www.openadr.org/DRAS/UtilityProgram}ListOfProgramNames" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchDRASParticipants", propOrder = {
    "searchHandler",
    "programNames"
})
public class SearchDRASParticipants
    implements Serializable
{

    /** The search handler. */
    @XmlElement(nillable = true)
    protected AkuaDRASSearchHandler searchHandler;
    
    /** The program names. */
    @XmlElement(nillable = true)
    protected ListOfProgramNames programNames;

    /**
     * Gets the value of the searchHandler property.
     * 
     * @return the search handler
     * 
     * possible object is
     * {@link AkuaDRASSearchHandler }
     */
    public AkuaDRASSearchHandler getSearchHandler() {
        return searchHandler;
    }

    /**
     * Sets the value of the searchHandler property.
     * 
     * @param value allowed object is
     * {@link AkuaDRASSearchHandler }
     */
    public void setSearchHandler(AkuaDRASSearchHandler value) {
        this.searchHandler = value;
    }

    /**
     * Gets the value of the programNames property.
     * 
     * @return the program names
     * 
     * possible object is
     * {@link ListOfProgramNames }
     */
    public ListOfProgramNames getProgramNames() {
        return programNames;
    }

    /**
     * Sets the value of the programNames property.
     * 
     * @param value allowed object is
     * {@link ListOfProgramNames }
     */
    public void setProgramNames(ListOfProgramNames value) {
        this.programNames = value;
    }

}
