/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.NotificationParametersVO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.program.dbp.EventBidBlock;

/**
 * The Class NotificationParametersVO.
 */
public class NotificationParametersVO implements Serializable {

    /**
     * The Enum Transport.
     */
    public static enum Transport {
        /** The email. */
        email, 
        /** The fax. */
        fax,
        /** The phone. */
        phone,
        /** The pager. */
        pager,
        /** The sms. */
        sms
    }

 // Changes made for SGSDI-203 - Varolii Template - Starts 

/*
    private final String sender = "Curtailment-Manager@pge.com";
    private String email = "Curtailment-Manager@pge.com";
    */
    
    /** The sender. */
    private final String sender = "PG&amp;E Demand Response";
    
    /** The email. */
    private String email = "inter-act@pge.com";
    
 // Changes made for SGSDI-203 - Varolii Template - ends 
    
    /** The theme. */
    private String theme = "PGE_AUTODBP:PGE_AUTODBP;PGE:;VOICETALENT:LESLIE;";
    
    /** The event id. */
    private String eventId;
    
    /** The is test event. */
    private final boolean isTestEvent = false;
    
    /** The event condition. */
    private String eventCondition = "EventIssued";
    
    /** The meter name. */
    private String meterName;
    
    /** The program name. */
    private String programName;
    
    /** The program type. */
    private final String programType = "Voluntary participation";
    
    /** The settlement type. */
    private final String settlementType = "Baseline - Actual";
    
    /** The event start date. */
    private Date eventStartDate;
    
    /** The event end date. */
    private Date eventEndDate;
    
    /** The time zone. */
    private String timeZone;
    
    /** The respond by. */
    private Date respondBy;
    
    /** The url. */
    private final String url = "https://pge.openadr.com/pss2.website";
    
    /** The energy unit. */
    private final String energyUnit = "KWH";
    
    /** The energy price unit. */
    private final String energyPriceUnit = "$/KWH";
    
    /** The entries. */
    private List<EventBidBlock> entries;
    
    /** The unit price. */
    private String unitPrice;
    
    /**
     * Gets the sender.
     * 
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the email.
     * 
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the theme.
     * 
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * Checks if is test event.
     * 
     * @return true, if is test event
     */
    public boolean isTestEvent() {
        return isTestEvent;
    }

    /**
     * Gets the event condition.
     * 
     * @return the event condition
     */
    public String getEventCondition() {
        return eventCondition;
    }

    public void setEventCondition(String eventCondition) {
		this.eventCondition = eventCondition;
	}

	/**
     * Gets the program type.
     * 
     * @return the program type
     */
    public String getProgramType() {
        return programType;
    }

    /**
     * Gets the settlement type.
     * 
     * @return the settlement type
     */
    public String getSettlementType() {
        return settlementType;
    }

    /**
     * Gets the url.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets the energy unit.
     * 
     * @return the energy unit
     */
    public String getEnergyUnit() {
        return energyUnit;
    }

    /**
     * Gets the energy price unit.
     * 
     * @return the energy price unit
     */
    public String getEnergyPriceUnit() {
        return energyPriceUnit;
    }

    /**
     * Gets the event id.
     * 
     * @return the event id
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets the event id.
     * 
     * @param eventId the new event id
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the meter name.
     * 
     * @return the meter name
     */
    public String getMeterName() {
        return meterName;
    }

    /**
     * Sets the meter name.
     * 
     * @param meterName the new meter name
     */
    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

    /**
     * Gets the program name.
     * 
     * @return the program name
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the program name.
     * 
     * @param programName the new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * Gets the event start date.
     * 
     * @return the event start date
     */
    public Date getEventStartDate() {
        return eventStartDate;
    }

    /**
     * Sets the event start date.
     * 
     * @param eventStartDate the new event start date
     */
    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    /**
     * Gets the event end date.
     * 
     * @return the event end date
     */
    public Date getEventEndDate() {
        return eventEndDate;
    }

    /**
     * Sets the event end date.
     * 
     * @param eventEndDate the new event end date
     */
    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    /**
     * Gets the time zone.
     * 
     * @return the time zone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the time zone.
     * 
     * @param timeZone the new time zone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Gets the respond by.
     * 
     * @return the respond by
     */
    public Date getRespondBy() {
        return respondBy;
    }

    /**
     * Sets the respond by.
     * 
     * @param respondBy the new respond by
     */
    public void setRespondBy(Date respondBy) {
        this.respondBy = respondBy;
    }

    /**
     * Gets the entries.
     * 
     * @return the entries
     */
    public List<EventBidBlock> getEntries() {
        return entries;
    }

    /**
     * Sets the entries.
     * 
     * @param entries the new entries
     */
    public void setEntries(List<EventBidBlock> entries) {
        this.entries = entries;
    }

    /**
     * Gets the unit price.
     * 
     * @return the unit price
     */
    public String getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price.
     * 
     * @param unitPrice the new unit price
     */
    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }
    
}
