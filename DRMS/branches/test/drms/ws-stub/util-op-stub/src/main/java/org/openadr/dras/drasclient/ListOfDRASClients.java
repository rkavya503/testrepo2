/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.drasclient.ListOfDRASClients.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.drasclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * This represents a list of DRASClient objects.
 * 
 * 
 * <p>Java class for ListOfDRASClients complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfDRASClients">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="drasClient" type="{http://www.openadr.org/DRAS/DRASClient}DRASClient" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfDRASClients", propOrder = {
    "drasClient"
})
public class ListOfDRASClients
    implements Serializable
{

    /** The dras client. */
    protected List<DRASClient> drasClient;

    /**
     * Gets the value of the drasClient property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the drasClient property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getDrasClient().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DRASClient }
     * 
     * @return the dras client
     */
    public List<DRASClient> getDrasClient() {
        if (drasClient == null) {
            drasClient = new ArrayList<DRASClient>();
        }
        return this.drasClient;
    }

}
