/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.feedback.ListOfFeedback.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.feedback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Represents a list of Feedback objects.
 * 
 * 
 * <p>Java class for ListOfFeedback complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfFeedback">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="feedback" type="{http://www.openadr.org/DRAS/Feedback}FeedBack" maxOccurs="unbounded" minOccurs="0"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfFeedback", propOrder = {
    "feedback"
})
public class ListOfFeedback
    implements Serializable
{

    /** The feedback. */
    @XmlElement(nillable = true)
    protected List<FeedBack> feedback;

    /**
     * Gets the value of the feedback property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the feedback property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getFeedback().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FeedBack }
     * 
     * @return the feedback
     */
    public List<FeedBack> getFeedback() {
        if (feedback == null) {
            feedback = new ArrayList<FeedBack>();
        }
        return this.feedback;
    }

}
