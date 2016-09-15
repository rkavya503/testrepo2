/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.ContactEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant.contact;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.participant.Participant;

/**
 * The Class ParticipantContact.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "participant_contact", uniqueConstraints = {@UniqueConstraint(columnNames={"description", "type", "address", "participant_uuid"})})
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class ParticipantContact extends BaseEntity implements Comparable<ParticipantContact> {

	private static final long serialVersionUID = -7895697553021306693L;

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
    private Participant participant;

    /** message threshold to this contact per day **/
    private Integer msgThreshold;   
    private boolean defaultMsgThreshold;
 
    //Demand Limiting
    @Column(name = "demandLimitingNotifications")
    private Boolean demandLimitingNotifications   = new Boolean(true);
    @Column(name = "rateLimitExceededNotifications")
    private Boolean rateLimitExceededNotifications   = new Boolean(true);
    @Column(name = "rateLimitExceededNotificationInterval")
    private Integer rateLimitExceededNotificationInterval;
    @Column(name = "rateLimitExceededNotificationLast")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rateLimitExceededNotificationLast;
    @Column(name = "demandModerateNotifications")
    private Boolean demandModerateNotifications   = new Boolean(true);
    @Column(name = "demandModerateNotificationInterval")
    private Integer demandModerateNotificationInterval;
    @Column(name = "demandModerateNotificationLast")
    @Temporal(TemporalType.TIMESTAMP)
    private Date demandModerateNotificationLast;
    @Column(name = "demandHighNotifications")
    private Boolean demandHighNotifications   = new Boolean(true);
    @Column(name = "demandHighNotificationInterval")
    private Integer demandHighNotificationInterval;
    @Column(name = "demandHighNotificationLast")
    @Temporal(TemporalType.TIMESTAMP)
    private Date demandHighNotificationLast;
    @Column(name = "demandNormalNotifications")
    private Boolean demandNormalNotifications   = new Boolean(true);
    @Column(name = "demandNormalNotificationInterval")
    private Integer demandNormalNotificationInterval;
    @Column(name = "demandNormalNotificationLast")
    @Temporal(TemporalType.TIMESTAMP)
    private Date demandNormalNotificationLast;
    @Column(name = "demandWarningNotifications")
    private Boolean demandWarningNotifications  = new Boolean(true);
    @Column(name = "demandWarningNotificationInterval")
    private Integer demandWarningNotificationInterval;
    @Column(name = "demandWarningNotificationLast")
    @Temporal(TemporalType.TIMESTAMP)
    private Date demandWarningNotificationLast;
    
    
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
    public Boolean getExternal() {
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
    public Participant getParticipant() {
        return participant;
    }

    /**
     * Sets the participant.
     * 
     * @param participant the new participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
	
    /**
	 * get the message threshold 
	 * @return
	 */
	public Integer getMsgThreshold() {
		return msgThreshold;
	}

    public Boolean getDemandLimitingNotifications() {
        return demandLimitingNotifications;
    }

    public void setDemandLimitingNotifications(Boolean demandLimitingNotifications) {
        this.demandLimitingNotifications = demandLimitingNotifications;
    }

    public Boolean getRateLimitExceededNotifications() {
        return rateLimitExceededNotifications;
    }

    public void setRateLimitExceededNotifications(Boolean rateLimitExceededNotifications) {
        this.rateLimitExceededNotifications = rateLimitExceededNotifications;
    }

    public Integer getRateLimitExceededNotificationInterval() {
        return rateLimitExceededNotificationInterval;
    }

    public void setRateLimitExceededNotificationInterval(Integer rateLimitExceededNotificationInterval) {
        this.rateLimitExceededNotificationInterval = rateLimitExceededNotificationInterval;
    }

    public Date getRateLimitExceededNotificationLast() {
        return rateLimitExceededNotificationLast;
    }

    public void setRateLimitExceededNotificationLast(Date rateLimitExceededNotificationLast) {
        this.rateLimitExceededNotificationLast = rateLimitExceededNotificationLast;
    }

    public Boolean getDemandModerateNotifications() {
        return demandModerateNotifications;
    }

    public void setDemandModerateNotifications(Boolean demandModerateNotifications) {
        this.demandModerateNotifications = demandModerateNotifications;
    }

    public Integer getDemandModerateNotificationInterval() {
        return demandModerateNotificationInterval;
    }

    public void setDemandModerateNotificationInterval(Integer demandModerateNotificationInterval) {
        this.demandModerateNotificationInterval = demandModerateNotificationInterval;
    }

    public Date getDemandModerateNotificationLast() {
        return demandModerateNotificationLast;
    }

    public void setDemandModerateNotificationLast(Date demandModerateNotificationLast) {
        this.demandModerateNotificationLast = demandModerateNotificationLast;
    }

    public Boolean getDemandHighNotifications() {
        return demandHighNotifications;
    }

    public void setDemandHighNotifications(Boolean demandHighNotifications) {
        this.demandHighNotifications = demandHighNotifications;
    }

    public Integer getDemandHighNotificationInterval() {
        return demandHighNotificationInterval;
    }

    public void setDemandHighNotificationInterval(Integer demandHighNotificationInterval) {
        this.demandHighNotificationInterval = demandHighNotificationInterval;
    }

    public Date getDemandHighNotificationLast() {
        return demandHighNotificationLast;
    }

    public void setDemandHighNotificationLast(Date demandHighNotificationLast) {
        this.demandHighNotificationLast = demandHighNotificationLast;
    }

    public Boolean getDemandNormalNotifications() {
        return demandNormalNotifications;
    }

    public void setDemandNormalNotifications(Boolean demandNormalNotifications) {
        this.demandNormalNotifications = demandNormalNotifications;
    }

    public Integer getDemandNormalNotificationInterval() {
        return demandNormalNotificationInterval;
    }

    public void setDemandNormalNotificationInterval(Integer demandNormalNotificationInterval) {
        this.demandNormalNotificationInterval = demandNormalNotificationInterval;
    }

    public Date getDemandNormalNotificationLast() {
        return demandNormalNotificationLast;
    }

    public void setDemandNormalNotificationLast(Date demandNormalNotificationLast) {
        this.demandNormalNotificationLast = demandNormalNotificationLast;
    }

    public Boolean getDemandWarningNotifications() {
        return demandWarningNotifications;
    }

    public void setDemandWarningNotifications(Boolean demandWarningNotifications) {
        this.demandWarningNotifications = demandWarningNotifications;
    }

    public Integer getDemandWarningNotificationInterval() {
        return demandWarningNotificationInterval;
    }

    public void setDemandWarningNotificationInterval(Integer demandWarningNotificationInterval) {
        this.demandWarningNotificationInterval = demandWarningNotificationInterval;
    }

    public Date getDemandWarningNotificationLast() {
        return demandWarningNotificationLast;
    }

    public void setDemandWarningNotificationLast(Date demandWarningNotificationLast) {
        this.demandWarningNotificationLast = demandWarningNotificationLast;
    }

	
	/**
	 * set message threshold
	 * @param msgThreshold
	 */
	public void setMsgThreshold(Integer msgThreshold) {
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
    	//c.setId(this.getId());
    	
    	c.setUUID(getUUID());
    	c.setType(this.getType());
    	
    	return c;
    }

    @Override
    public int compareTo(ParticipantContact o) {
        if (o.getUUID() == null) {
            return 1;
        } else if (this.getUUID() == null) {
            return -1;
        } else {
            return this.getUUID().compareTo(o.getUUID());
        }
    }
}
