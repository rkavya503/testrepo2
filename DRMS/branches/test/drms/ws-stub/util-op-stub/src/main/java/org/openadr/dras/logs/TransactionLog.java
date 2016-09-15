/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.logs.TransactionLog.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.logs;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * All transactions with the DRAS are logged and may be
 * queried by various operators. A transaction is defined
 * as an invocation of a web service method. Each entry in
 * the transaction log is represented by this entity.
 * 
 * 
 * <p>Java class for TransactionLog complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransactionLog">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="role" minOccurs="0">
 * &lt;simpleType>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="DRAS_OPERATOR"/>
 * &lt;enumeration value="UTILITY_OPERATOR"/>
 * &lt;enumeration value="PARTICIPANT_OPERATOR"/>
 * &lt;enumeration value="DRAS_CLIENT"/>
 * &lt;enumeration value="INSTALLER"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * &lt;/element>
 * &lt;element name="timeStamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 * &lt;element name="transactionName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 * &lt;element name="result" minOccurs="0">
 * &lt;simpleType>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="SUCCESS"/>
 * &lt;enumeration value="FAILURE"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionLog", propOrder = {
    "userName",
    "role",
    "timeStamp",
    "transactionName",
    "description",
    "result"
})
public class TransactionLog
    implements Serializable
{

    /** The user name. */
    protected String userName;
    
    /** The role. */
    protected String role;
    
    /** The time stamp. */
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeStamp;
    
    /** The transaction name. */
    protected String transactionName;
    
    /** The description. */
    @XmlElement(nillable = true)
    protected String description;
    
    /** The result. */
    protected String result;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;

    /**
     * Gets the value of the userName property.
     * 
     * @return the user name
     * 
     * possible object is
     * {@link String }
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the role property.
     * 
     * @return the role
     * 
     * possible object is
     * {@link String }
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return the time stamp
     * 
     * possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value allowed object is
     * {@link XMLGregorianCalendar }
     */
    public void setTimeStamp(XMLGregorianCalendar value) {
        this.timeStamp = value;
    }

    /**
     * Gets the value of the transactionName property.
     * 
     * @return the transaction name
     * 
     * possible object is
     * {@link String }
     */
    public String getTransactionName() {
        return transactionName;
    }

    /**
     * Sets the value of the transactionName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setTransactionName(String value) {
        this.transactionName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return the description
     * 
     * possible object is
     * {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the result property.
     * 
     * @return the result
     * 
     * possible object is
     * {@link String }
     */
    public String getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setResult(String value) {
        this.result = value;
    }

    /**
     * Gets the value of the schemaVersion property.
     * 
     * @return the schema version
     * 
     * possible object is
     * {@link String }
     */
    public String getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * Sets the value of the schemaVersion property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setSchemaVersion(String value) {
        this.schemaVersion = value;
    }

}
