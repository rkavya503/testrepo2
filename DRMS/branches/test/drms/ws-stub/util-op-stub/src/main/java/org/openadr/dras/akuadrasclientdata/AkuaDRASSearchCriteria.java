/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.akuadrasclientdata.AkuaDRASSearchCriteria.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.akuadrasclientdata;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AkuaDRASSearchCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AkuaDRASSearchCriteria">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="fieldName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="fieldValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;element name="joinOperator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AkuaDRASSearchCriteria", propOrder = {
    "fieldName",
    "operator",
    "fieldValue",
    "joinOperator"
})
public class AkuaDRASSearchCriteria
    implements Serializable
{

    /** The field name. */
    @XmlElement(required = true)
    protected String fieldName;
    
    /** The operator. */
    @XmlElement(required = true)
    protected String operator;
    
    /** The field value. */
    @XmlElement(required = true)
    protected String fieldValue;
    
    /** The join operator. */
    @XmlElement(required = true)
    protected String joinOperator;

    /**
     * Gets the value of the fieldName property.
     * 
     * @return the field name
     * 
     * possible object is
     * {@link String }
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the value of the fieldName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setFieldName(String value) {
        this.fieldName = value;
    }

    /**
     * Gets the value of the operator property.
     * 
     * @return the operator
     * 
     * possible object is
     * {@link String }
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setOperator(String value) {
        this.operator = value;
    }

    /**
     * Gets the value of the fieldValue property.
     * 
     * @return the field value
     * 
     * possible object is
     * {@link String }
     */
    public String getFieldValue() {
        return fieldValue;
    }

    /**
     * Sets the value of the fieldValue property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setFieldValue(String value) {
        this.fieldValue = value;
    }

    /**
     * Gets the value of the joinOperator property.
     * 
     * @return the join operator
     * 
     * possible object is
     * {@link String }
     */
    public String getJoinOperator() {
        return joinOperator;
    }

    /**
     * Sets the value of the joinOperator property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setJoinOperator(String value) {
        this.joinOperator = value;
    }

}
