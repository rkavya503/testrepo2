/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.CreateDRASClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.drasclient.DRASClient;


/**
 * <p>Java class for CreateDRASClient complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateDRASClient">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="DRASClient" type="{http://www.openadr.org/DRAS/DRASClient}DRASClient"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateDRASClient", propOrder = {
    "drasClient"
})
public class CreateDRASClient
    implements Serializable
{

    /** The dras client. */
    @XmlElement(name = "DRASClient", required = true)
    protected DRASClient drasClient;

    /**
     * Gets the value of the drasClient property.
     * 
     * @return the DRAS client
     * 
     * possible object is
     * {@link DRASClient }
     */
    public DRASClient getDRASClient() {
        return drasClient;
    }

    /**
     * Sets the value of the drasClient property.
     * 
     * @param value allowed object is
     * {@link DRASClient }
     */
    public void setDRASClient(DRASClient value) {
        this.drasClient = value;
    }

}
