/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.responseschedule.OperationStateSpec.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.responseschedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * This is used to define an Operation State. Each
 * Operation State is a single time slot within a Response
 * Schedule. A series of these states define a Response
 * Schedule as described in the ResponseSchedule entity .
 * It defines a time slot and a set of rules that are used
 * to create one of the operation mode states that a Simple
 * DRAS Client may be in during the specified time slot.
 * 
 * 
 * <p>Java class for OperationStateSpec complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OperationStateSpec">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}double"/>
 * &lt;element name="rules" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="rule" type="{http://www.openadr.org/DRAS/ResponseSchedule}Rule" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperationStateSpec", propOrder = {
    "startTime",
    "rules"
})
public class OperationStateSpec
    implements Serializable
{

    /** The start time. */
    protected double startTime;
    
    /** The rules. */
    protected OperationStateSpec.Rules rules;

    /**
     * Gets the value of the startTime property.
     * 
     * @return the start time
     */
    public double getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     * 
     * @param value the value
     */
    public void setStartTime(double value) {
        this.startTime = value;
    }

    /**
     * Gets the value of the rules property.
     * 
     * @return the rules
     * 
     * possible object is
     * {@link OperationStateSpec.Rules }
     */
    public OperationStateSpec.Rules getRules() {
        return rules;
    }

    /**
     * Sets the value of the rules property.
     * 
     * @param value allowed object is
     * {@link OperationStateSpec.Rules }
     */
    public void setRules(OperationStateSpec.Rules value) {
        this.rules = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     * &lt;complexContent>
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     * &lt;sequence>
     * &lt;element name="rule" type="{http://www.openadr.org/DRAS/ResponseSchedule}Rule" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "rule"
    })
    public static class Rules
        implements Serializable
    {

        /** The rule. */
        protected List<Rule> rule;

        /**
         * Gets the value of the rule property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the rule property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getRule().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Rule }
         * 
         * @return the rule
         */
        public List<Rule> getRule() {
            if (rule == null) {
                rule = new ArrayList<Rule>();
            }
            return this.rule;
        }

    }

}
