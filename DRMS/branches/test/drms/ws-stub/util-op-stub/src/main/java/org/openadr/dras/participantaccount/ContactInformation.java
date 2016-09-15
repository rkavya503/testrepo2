/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantaccount.ContactInformation.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantaccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContactInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContactInformation">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="emailAddresses" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="voiceNumbers" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="pagerNumbers" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * &lt;/element>
 * &lt;element name="faxNumbers" minOccurs="0">
 * &lt;complexType>
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "ContactInformation", propOrder = {
    "emailAddresses",
    "voiceNumbers",
    "pagerNumbers",
    "faxNumbers"
})
public class ContactInformation
    implements Serializable
{

    /** The email addresses. */
    @XmlElement(nillable = true)
    protected ContactInformation.EmailAddresses emailAddresses;
    
    /** The voice numbers. */
    @XmlElement(nillable = true)
    protected ContactInformation.VoiceNumbers voiceNumbers;
    
    /** The pager numbers. */
    @XmlElement(nillable = true)
    protected ContactInformation.PagerNumbers pagerNumbers;
    
    /** The fax numbers. */
    @XmlElement(nillable = true)
    protected ContactInformation.FaxNumbers faxNumbers;

    /**
     * Gets the value of the emailAddresses property.
     * 
     * @return the email addresses
     * 
     * possible object is
     * {@link ContactInformation.EmailAddresses }
     */
    public ContactInformation.EmailAddresses getEmailAddresses() {
        return emailAddresses;
    }

    /**
     * Sets the value of the emailAddresses property.
     * 
     * @param value allowed object is
     * {@link ContactInformation.EmailAddresses }
     */
    public void setEmailAddresses(ContactInformation.EmailAddresses value) {
        this.emailAddresses = value;
    }

    /**
     * Gets the value of the voiceNumbers property.
     * 
     * @return the voice numbers
     * 
     * possible object is
     * {@link ContactInformation.VoiceNumbers }
     */
    public ContactInformation.VoiceNumbers getVoiceNumbers() {
        return voiceNumbers;
    }

    /**
     * Sets the value of the voiceNumbers property.
     * 
     * @param value allowed object is
     * {@link ContactInformation.VoiceNumbers }
     */
    public void setVoiceNumbers(ContactInformation.VoiceNumbers value) {
        this.voiceNumbers = value;
    }

    /**
     * Gets the value of the pagerNumbers property.
     * 
     * @return the pager numbers
     * 
     * possible object is
     * {@link ContactInformation.PagerNumbers }
     */
    public ContactInformation.PagerNumbers getPagerNumbers() {
        return pagerNumbers;
    }

    /**
     * Sets the value of the pagerNumbers property.
     * 
     * @param value allowed object is
     * {@link ContactInformation.PagerNumbers }
     */
    public void setPagerNumbers(ContactInformation.PagerNumbers value) {
        this.pagerNumbers = value;
    }

    /**
     * Gets the value of the faxNumbers property.
     * 
     * @return the fax numbers
     * 
     * possible object is
     * {@link ContactInformation.FaxNumbers }
     */
    public ContactInformation.FaxNumbers getFaxNumbers() {
        return faxNumbers;
    }

    /**
     * Sets the value of the faxNumbers property.
     * 
     * @param value allowed object is
     * {@link ContactInformation.FaxNumbers }
     */
    public void setFaxNumbers(ContactInformation.FaxNumbers value) {
        this.faxNumbers = value;
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
     * &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "address"
    })
    public static class EmailAddresses
        implements Serializable
    {

        /** The address. */
        @XmlElement(nillable = true)
        protected List<String> address;

        /**
         * Gets the value of the address property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the address property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getAddress().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * @return the address
         */
        public List<String> getAddress() {
            if (address == null) {
                address = new ArrayList<String>();
            }
            return this.address;
        }

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
     * &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "number"
    })
    public static class FaxNumbers
        implements Serializable
    {

        /** The number. */
        @XmlElement(nillable = true)
        protected List<String> number;

        /**
         * Gets the value of the number property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the number property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getNumber().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * @return the number
         */
        public List<String> getNumber() {
            if (number == null) {
                number = new ArrayList<String>();
            }
            return this.number;
        }

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
     * &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "number"
    })
    public static class PagerNumbers
        implements Serializable
    {

        /** The number. */
        @XmlElement(nillable = true)
        protected List<String> number;

        /**
         * Gets the value of the number property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the number property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getNumber().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * @return the number
         */
        public List<String> getNumber() {
            if (number == null) {
                number = new ArrayList<String>();
            }
            return this.number;
        }

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
     * &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     * &lt;/sequence>
     * &lt;/restriction>
     * &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "number"
    })
    public static class VoiceNumbers
        implements Serializable
    {

        /** The number. */
        @XmlElement(nillable = true)
        protected List<String> number;

        /**
         * Gets the value of the number property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the number property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         * getNumber().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * @return the number
         */
        public List<String> getNumber() {
            if (number == null) {
                number = new ArrayList<String>();
            }
            return this.number;
        }

    }

}
