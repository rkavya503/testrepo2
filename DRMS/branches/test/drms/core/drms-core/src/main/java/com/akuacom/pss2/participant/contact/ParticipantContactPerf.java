/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.ContactEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant.contact;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.participant.ParticipantPerf;

/**
 * Performance entity bean for particpant_contacts
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "participant_contact")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class ParticipantContactPerf extends BaseEntity {

	private static final long serialVersionUID = -66043684834211279L;

	/** The type. */
    private String type;    // enums?;
    
    /** The address. */
    private String address;
    
    /** The description. */
    private String description;
    
    /** The comm notification. */
    private Boolean commNotification;
    
    /** The event notification. */
    @Enumerated(value = EnumType.STRING)
    private ContactEventNotificationType eventNotification;
    
    /** The off season noti hours. */
    private Double offSeasonNotiHours;

    /** The on season noti hours. */
    private Double onSeasonNotiHours;
    
    /** The external. */
    private Boolean external = new Boolean(false);

    /** The offline error. */
    private Boolean offlineError = new Boolean(false);
    
    /** The participant. */
    @ManyToOne
    @JoinColumn(name = "participant_uuid")
    private ParticipantPerf participantPerf;

    
    /** message threshold to this contact per day **/
    private int msgThreshold;   
    private boolean defaultMsgThreshold;
    
    public boolean getDefaultMsgThreshold() {
		return defaultMsgThreshold;
	}

	public void setDefaultMsgThreshold(boolean defaultMsgThreshold) {
		this.defaultMsgThreshold = defaultMsgThreshold;
	}
    
    /**
     * Gets the address.
     * 
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address.
     * 
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     * 
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type the new type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Checks if is comm notification.
     * 
     * @return true, if is comm notification
     */
    public Boolean isCommNotification() {
        return commNotification;
    }

    /**
     * Sets the comm notification.
     * 
     * @param commNotification the new comm notification
     */
    public void setCommNotification(Boolean commNotification) {
        this.commNotification = commNotification;
    }

    public ContactEventNotificationType getEventNotification() {
    	return this.eventNotification;
    }

    /**
     * Sets the event notification.
     * 
     * @param eventNotification the new event notification
     */
    public void setEventNotification(ContactEventNotificationType eventNotification) {
        this.eventNotification = eventNotification;
    }

    /**
     * Checks if is external.
     * 
     * @return true, if is external
     */
    public Boolean isExternal() {
        return external;
    }

    /**
     * Sets the external.
     * 
     * @param external the new external
     */
    public void setExternal(Boolean external) {
        this.external = external;
    }
    
    public Double getOffSeasonNotiHours() {
		return offSeasonNotiHours;
	}

	public void setOffSeasonNotiHours(Double offSeasonNotiHours) {
		this.offSeasonNotiHours = offSeasonNotiHours;
	}

	public Double getOnSeasonNotiHours() {
		return onSeasonNotiHours;
	}

	public void setOnSeasonNotiHours(Double onSeasonNotiHours) {
		this.onSeasonNotiHours = onSeasonNotiHours;
	}

    /**
     * Checks if is offline error.
     *
     * @return true, if is offline error
     */
    public boolean isOfflineError() {
        return offlineError;
    }

    /**
     * Sets the offline error.
     *
     * @param offlineError the new offline error
     */
    public void setOfflineError(boolean offlineError) {
        this.offlineError = offlineError;
    }

    /**
     * Gets the participant.
     * 
     * @return the participant
     */
    public ParticipantPerf getParticipantPerf() {
        return participantPerf;
    }

    /**
     * Sets the participant.
     * 
     * @param participant the new participant
     */
    public void setParticipantPerf(ParticipantPerf participant) {
        this.participantPerf = participant;
    }
    
    public int getMsgThreshold() {
		return msgThreshold;
	}

	public void setMsgThreshold(int msgThreshold) {
		this.msgThreshold = msgThreshold;
	}

    public int getOfflineErrorThresholdMinutesForSeason(boolean onSeason) {
    	int res;
    	if (onSeason)
    		res = new Double(60.0 * this.getOnSeasonNotiHours()).intValue();
    	else
    		res = new Double(60.0 * this.getOffSeasonNotiHours()).intValue();
    	
    	return res;
    }
    
    public Contact getParticipantContactAsContact() {
    	Contact c = new Contact();
    	c.setAddress(this.getAddress());
    	c.setCommNotification(this.isCommNotification());
    	c.setDescription(this.getDescription());
    	c.setEventNotification(this.getEventNotification());
    	
    	//transfer id is required for contact level msg filtering
    	c.setUUID(this.getUUID());
    	c.setType(this.getType());
    	
    	c.setMsgThreshold(this.msgThreshold);
    	
    	return c;
    }
    
    
}
