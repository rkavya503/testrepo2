/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantoperator.SetDREventFeedback.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantoperator;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openadr.dras.feedback.FeedBack;


/**
 * <p>Java class for SetDREventFeedback complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SetDREventFeedback">
 * &lt;complexContent>
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 * &lt;sequence>
 * &lt;element name="feedBack" type="{http://www.openadr.org/DRAS/Feedback}FeedBack"/>
 * &lt;/sequence>
 * &lt;/restriction>
 * &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetDREventFeedback", propOrder = {
    "feedBack"
})
public class SetDREventFeedback
    implements Serializable
{

    /** The feed back. */
    @XmlElement(required = true)
    protected FeedBack feedBack;

    /**
     * Gets the value of the feedBack property.
     * 
     * @return the feed back
     * 
     * possible object is
     * {@link FeedBack }
     */
    public FeedBack getFeedBack() {
        return feedBack;
    }

    /**
     * Sets the value of the feedBack property.
     * 
     * @param value allowed object is
     * {@link FeedBack }
     */
    public void setFeedBack(FeedBack value) {
        this.feedBack = value;
    }

}
