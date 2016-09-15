/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.eventstate.CustomData.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.eventstate;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * This represents  information that may be sent to the DRAS Client in order to perform fucntions or send information that is not normally within the scope of the EventState of a DR event.  Definition of what may be sent in these fields is outside the scope of the
 * 
 * <p>Java class for CustomData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomData">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomData")
public class CustomData
    implements Serializable
{


}
