/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.responseschedule.ResponseSchedule.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.responseschedule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Response schedules are associated with both a DR program
 * and a DRAS Client. They are used to define how a Simple
 * DRAS Client will respond to DR Events that issued for a
 * specific DR program. The Response Schedule is used by
 * the DRAS to map the information associated with a DR
 * Event to the simple levels required by a Simple DRAS
 * Client. Response Schedules are typically set up and
 * configured by Participants and associated with specific
 * DRAS Clients although they may be set up by the
 * Utility/ISO if there is a common set of strategies that
 * should be used with all DRAS Clients.
 * 
 * 
 * <p>Java class for ResponseSchedule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseSchedule">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="nearTransitionTime" type="{http://www.w3.org/2001/XMLSchema}double"/>
 * &lt;element name="operationStates">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="stateSpec" type="{http://www.openadr.org/DRAS/ResponseSchedule}OperationStateSpec" maxOccurs="unbounded"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;/sequence>
 * &lt;attribute name="schemaVersion" type="{http://www.w3.org/2001/XMLSchema}string" default="1.0" />
 * &lt;attribute name="identifier" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="programName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;attribute name="DRASClientID" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseSchedule", propOrder = {
    "nearTransitionTime",
    "operationStates"
})
public class ResponseSchedule
    implements Serializable
{

    /** The near transition time. */
    protected double nearTransitionTime;
    
    /** The operation states. */
    @XmlElement(required = true)
    protected ResponseSchedule.OperationStates operationStates;
    
    /** The schema version. */
    @XmlAttribute
    protected String schemaVersion;
    
    /** The identifier. */
    @XmlAttribute(required = true)
    protected String identifier;
    
    /** The program name. */
    @XmlAttribute(required = true)
    protected String programName;
    
    /** The dras client id. */
    @XmlAttribute(name = "DRASClientID")
    @XmlSchemaType(name = "anySimpleType")
    protected String drasClientID;

    /**
     * Gets the value of the nearTransitionTime property.
     * 
     * @return the near transition time
     */
    public double getNearTransitionTime() {
        return nearTransitionTime;
    }

    /**
     * Sets the value of the nearTransitionTime property.
     * 
     * @param value the value
     */
    public void setNearTransitionTime(double value) {
        this.nearTransitionTime = value;
    }

    /**
     * Gets the value of the operationStates property.
     * 
     * @return the operation states
     * 
     * possible object is
     * {@link ResponseSchedule.OperationStates }
     */
    public ResponseSchedule.OperationStates getOperationStates() {
        return operationStates;
    }

    /**
     * Sets the value of the operationStates property.
     * 
     * @param value allowed object is
     * {@link ResponseSchedule.OperationStates }
     */
    public void setOperationStates(ResponseSchedule.OperationStates value) {
        this.operationStates = value;
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
        if (schemaVersion == null) {
            return "1.0";
        } else {
            return schemaVersion;
        }
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

    /**
     * Gets the value of the identifier property.
     * 
     * @return the identifier
     * 
     * possible object is
     * {@link String }
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the programName property.
     * 
     * @return the program name
     * 
     * possible object is
     * {@link String }
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the value of the programName property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setProgramName(String value) {
        this.programName = value;
    }

    /**
     * Gets the value of the drasClientID property.
     * 
     * @return the DRAS client id
     * 
     * possible object is
     * {@link String }
     */
    public String getDRASClientID() {
        return drasClientID;
    }

    /**
     * Sets the value of the drasClientID property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    public void setDRASClientID(String value) {
        this.drasClientID = value;
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
     * &lt;element name="stateSpec" type="{http://www.openadr.org/DRAS/ResponseSchedule}OperationStateSpec" maxOccurs="unbounded"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "stateSpec"
    })
    public static class OperationStates
        implements Serializable
    {

        /** The state spec. */
        @XmlElement(required = true)
        protected List<OperationStateSpec> stateSpec;

        /**
         * Gets the value of the stateSpec property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the stateSpec property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getStateSpec().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link OperationStateSpec }
         * 
         * @return the state spec
         */
        public List<OperationStateSpec> getStateSpec() {
            if (stateSpec == null) {
                stateSpec = new ArrayList<OperationStateSpec>();
            }
            return this.stateSpec;
        }

    }

}
