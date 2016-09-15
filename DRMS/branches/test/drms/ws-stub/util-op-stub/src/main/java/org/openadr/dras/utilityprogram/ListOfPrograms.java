/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityprogram.ListOfPrograms.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
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
 * List of UtilityProgram objects.
 * 
 * 
 * <p>Java class for ListOfPrograms complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfPrograms">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="program" type="{http://www.openadr.org/DRAS/UtilityProgram}UtilityProgram" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfPrograms", propOrder = {
    "program"
})
public class ListOfPrograms
    implements Serializable
{

    /** The program. */
    @XmlElement(nillable = true)
    protected List<UtilityProgram> program;

    /**
     * Gets the value of the program property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the program property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getProgram().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UtilityProgram }
     * 
     * @return the program
     */
    public List<UtilityProgram> getProgram() {
        if (program == null) {
            program = new ArrayList<UtilityProgram>();
        }
        return this.program;
    }

}
