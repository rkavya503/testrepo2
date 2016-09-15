/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityprogram.ListOfProgramNames.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityprogram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This is a list of program identifiers.
 * 
 * 
 * <p>Java class for ListOfProgramNames complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfProgramNames">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="programID" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfProgramNames", propOrder = {
    "programID"
})
public class ListOfProgramNames
    implements Serializable
{

    /** The program id. */
    @XmlElement(nillable = true)
    protected List<String> programID;

    /**
     * Gets the value of the programID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the programID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getProgramID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * @return the program id
     */
    public List<String> getProgramID() {
        if (programID == null) {
            programID = new ArrayList<String>();
        }
        return this.programID;
    }

}
