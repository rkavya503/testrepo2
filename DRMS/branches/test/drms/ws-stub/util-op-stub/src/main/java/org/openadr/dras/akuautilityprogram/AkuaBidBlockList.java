/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuautilityprogram.AkuaBidBlockList.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuautilityprogram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AkuaBidBlockList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AkuaBidBlockList">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="bidBlock" type="{http://www.openadr.org/DRAS/AkuaUtilityProgram}AkuaBidBlock" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AkuaBidBlockList", propOrder = {
    "bidBlock"
})
public class AkuaBidBlockList
    implements Serializable
{

    /** The bid block. */
    @XmlElement(nillable = true)
    protected List<AkuaBidBlock> bidBlock;

    /**
     * Gets the value of the bidBlock property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bidBlock property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getBidBlock().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AkuaBidBlock }
     * 
     * @return the bid block
     */
    public List<AkuaBidBlock> getBidBlock() {
        if (bidBlock == null) {
            bidBlock = new ArrayList<AkuaBidBlock>();
        }
        return this.bidBlock;
    }

}
