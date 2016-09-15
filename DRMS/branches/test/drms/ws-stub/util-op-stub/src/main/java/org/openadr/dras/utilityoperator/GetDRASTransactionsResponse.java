/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityoperator.GetDRASTransactionsResponse.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.logs.ListOfTransactionLogs;


/**
 * <p>Java class for GetDRASTransactionsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetDRASTransactionsResponse">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="transactions" type="{http://www.openadr.org/DRAS/Logs}ListOfTransactionLogs" minOccurs="0"/>
 * &lt;element name="returnValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetDRASTransactionsResponse", propOrder = {
    "transactions",
    "returnValue"
})
public class GetDRASTransactionsResponse
    implements Serializable
{

    /** The transactions. */
    @XmlElement(nillable = true)
    protected ListOfTransactionLogs transactions;
    
    /** The return value. */
    @XmlElement(required = true)
    protected String returnValue;

    /**
     * Gets the value of the transactions property.
     * 
     * @return the transactions
     * 
     * possible object is
     * {@link ListOfTransactionLogs }
     */
    public ListOfTransactionLogs getTransactions() {
        return transactions;
    }

    /**
     * Sets the value of the transactions property.
     * 
     * @param value allowed object is
     * {@link ListOfTransactionLogs }
     */
    public void setTransactions(ListOfTransactionLogs value) {
        this.transactions = value;
    }

    /**
     * Gets the value of the returnValue property.
     * 
     * @return the return value
     * 
     * possible object is
     * {@link String }
     */
    public String getReturnValue() {
        return returnValue;
    }

    /**
     * Sets the value of the returnValue property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setReturnValue(String value) {
        this.returnValue = value;
    }

}
