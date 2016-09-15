/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.CreateResponseSchedule.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.responseschedule.ResponseSchedule;


/**
 * <p>Java class for CreateResponseSchedule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateResponseSchedule">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="responseSchedule" type="{http://www.openadr.org/DRAS/ResponseSchedule}ResponseSchedule"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateResponseSchedule", propOrder = {
    "responseSchedule"
})
public class CreateResponseSchedule
    implements Serializable
{

    /** The response schedule. */
    @XmlElement(required = true)
    protected ResponseSchedule responseSchedule;

    /**
     * Gets the value of the responseSchedule property.
     * 
     * @return the response schedule
     * 
     * possible object is
     * {@link ResponseSchedule }
     */
    public ResponseSchedule getResponseSchedule() {
        return responseSchedule;
    }

    /**
     * Sets the value of the responseSchedule property.
     * 
     * @param value allowed object is
     * {@link ResponseSchedule }
     */
    public void setResponseSchedule(ResponseSchedule value) {
        this.responseSchedule = value;
    }

}
