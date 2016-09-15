/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.programconstraint.ListOfProgramConstraints.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.programconstraint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This represetns a list of ProgramConstraint objects.
 * 
 * 
 * <p>Java class for ListOfProgramConstraints complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfProgramConstraints">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="programConstraint" type="{http://www.openadr.org/DRAS/ProgramConstraint}ProgramConstraint" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfProgramConstraints", propOrder = {
    "programConstraint"
})
public class ListOfProgramConstraints
    implements Serializable
{

    /** The program constraint. */
    @XmlElement(nillable = true)
    protected List<ProgramConstraint> programConstraint;

    /**
     * Gets the value of the programConstraint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the programConstraint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getProgramConstraint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProgramConstraint }
     * 
     * @return the program constraint
     */
    public List<ProgramConstraint> getProgramConstraint() {
        if (programConstraint == null) {
            programConstraint = new ArrayList<ProgramConstraint>();
        }
        return this.programConstraint;
    }

}
