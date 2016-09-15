/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.logs.ListOfTransactionLogs.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.logs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This represents a list of TransactionLogs.
 * 
 * 
 * <p>Java class for ListOfTransactionLogs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfTransactionLogs">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="transactionLog" type="{http://www.openadr.org/DRAS/Logs}TransactionLog" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfTransactionLogs", propOrder = {
    "transactionLog"
})
public class ListOfTransactionLogs
    implements Serializable
{

    /** The transaction log. */
    @XmlElement(nillable = true)
    protected List<TransactionLog> transactionLog;

    /**
     * Gets the value of the transactionLog property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transactionLog property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getTransactionLog().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransactionLog }
     * 
     * @return the transaction log
     */
    public List<TransactionLog> getTransactionLog() {
        if (transactionLog == null) {
            transactionLog = new ArrayList<TransactionLog>();
        }
        return this.transactionLog;
    }

}
