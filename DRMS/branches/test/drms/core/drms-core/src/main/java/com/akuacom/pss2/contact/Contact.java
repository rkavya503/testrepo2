/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.ProgramContactEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.contact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.program.Program;
import com.akuacom.utils.lang.StringUtil;

/**
 * The Class ProgramContactDAO.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "contact", uniqueConstraints = {@UniqueConstraint(columnNames={"firstName", "lastName", "type", "address"})})
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
			@NamedQuery(
				name="Contact.findOwnerlessByOwnerTypeAndState",
				query="select distinct(c) from Contact c where c.owner is null and c.ownerType = :type and c.state = :state order by c.priority",
				hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
			@NamedQuery(
				name="Contact.getOperatorContacts",
				query="select c from Contact c where c.owner.programName =:programName")
		})
public class Contact extends VersionedEntity
{

	private static final long serialVersionUID = -2253509525278545380L;

	/** The Constant EMAIL_ADDRESS. */
    public static final String EMAIL_ADDRESS = "Email Address";
    
    /** The Constant PHONE_NUMBER. */
    public static final String PHONE_NUMBER = "Phone Number";
    
    /** The Constant FAX_NUMBER. */
    public static final String FAX_NUMBER = "Fax Number";
    
    /** The Constant PAGER. */
    public static final String PAGER = "Pager";

    public static final String SMS = "SMS";

    /** The contact deleted flag. */
    public static final int CONTACT_DELETED = 0;

    /** The contact active flag. */
    public static final int CONTACT_ACTIVE = 1;

        /** The Constant CONTACT_OWNER_TYPE_PROGRAM. */
    public static final String CONTACT_OWNER_TYPE_PROGRAM = "PROGRAM";

    /** The Constant CONTACT_OWNER_TYPE_PARTICIPANT. */
    public static final String CONTACT_OWNER_TYPE_PARTICIPANT = "PARTICIPANT";

    /** The Constant CONTACT_OWNER_TYPE_CLIENT. */
    public static final String CONTACT_OWNER_TYPE_CLIENT = "CLIENT";

    /** The Constant CONTACT_OWNER_TYPE_GLOBAL. */
    public static final String CONTACT_OWNER_TYPE_GLOBAL = "GLOBAL";

    /** The type. */
    private String type;
    
    /** The address. */
    private String address;
    
    /** The description. */
    private String description;
    
    /** The comm notification. */
    private boolean commNotification;
    
    /** The event notification. */
    @Enumerated(value = EnumType.STRING)
    private ContactEventNotificationType eventNotification;

    @Column(insertable=false, updatable=false )
    private String ownerID;
    
    /** The owner. */
    @ManyToOne
    @JoinColumn(name = "ownerID")
    private Program owner;

     /** The first name. */
    private String firstName;

    /** The last name. */
    private String lastName;

    /** The support type. */
    private String supportType;

    /** The owner type. */
    private String ownerType;

    /** The external. */
    private boolean external;
    
    /** The priority. */
    private int priority;

    /** The state. */
    private int state;

    /** The off season noti hours. */
    private double offSeasonNotiHours;

    /** The on season noti hours. */
    private double onSeasonNotiHours;
    
    //@Transient
    private long msgThreshold;
    private boolean defaultMsgThreshold;
    @Column
    private boolean optOutDigest;
    @Column
    private boolean optOutUndeliveredReport;
    public boolean isOptOutUndeliveredReport() {
		return optOutUndeliveredReport;
	}

	public void setOptOutUndeliveredReport(boolean optOutUndeliveredReport) {
		this.optOutUndeliveredReport = optOutUndeliveredReport;
	}

	@Column
    private boolean digestEnable;
    @Column
    private int digestInterval;
	public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSupportType() {
        return supportType;
    }

    public void setSupportType(String supportType) {
        this.supportType = supportType;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getOffSeasonNotiHours() {
        return offSeasonNotiHours;
    }

    public void setOffSeasonNotiHours(double offSeasonNotiHours) {
        this.offSeasonNotiHours = offSeasonNotiHours;
    }

    public double getOnSeasonNotiHours() {
        return onSeasonNotiHours;
    }

    public void setOnSeasonNotiHours(double onSeasonNotiHours) {
        this.onSeasonNotiHours = onSeasonNotiHours;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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
     * Checks if is comm notification.
     * 
     * @return true, if is comm notification
     */
    public boolean isCommNotification() {
        return commNotification;
    }

    /**
     * Sets the comm notification.
     * 
     * @param commNotification the new comm notification
     */
    public void setCommNotification(boolean commNotification) {
        this.commNotification = commNotification;
    }
    
    /**
     * Checks if is event notification.
     * 
     * @return true, if is event notification
     */
    public boolean eventNotificationOn() {
        return ContactEventNotificationType.FullNotification.equals(this.eventNotification)
        	|| ContactEventNotificationType.NotNormalNotification.equals(this.eventNotification);
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
     * Gets the owner.
     * 
     * @return the owner
     */
    public Program getOwner() {
        return owner;
    }

    /**
     * Sets the owner.
     * 
     * @param owner the new owner
     */
    public void setOwner(Program owner) {
        this.owner = owner;
    }
    
    public int getOfflineErrorThresholdMinutesForSeason(boolean onSeason) {
    	int res;
    	if (onSeason)
    		res = new Double(60.0 * this.getOnSeasonNotiHours()).intValue();
    	else
    		res = new Double(60.0 * this.getOffSeasonNotiHours()).intValue();
    	
    	return res;
    }
    
    public long getMsgThreshold() {
		return msgThreshold;
	}
    
	public void setMsgThreshold(long msgThreshold) {
		this.msgThreshold = msgThreshold;
	}
    
    public boolean getDefaultMsgThreshold() {
		return defaultMsgThreshold;
	}

	public void setDefaultMsgThreshold(boolean defaultMsgThreshold) {
		this.defaultMsgThreshold = defaultMsgThreshold;
	}

	/**
	 * @return the optOutDigest
	 */
	public boolean isOptOutDigest() {
		return optOutDigest;
	}

	/**
	 * @param optOutDigest the optOutDigest to set
	 */
	public void setOptOutDigest(boolean optOutDigest) {
		this.optOutDigest = optOutDigest;
	}

	/**
	 * @return the digestEnable
	 */
	public boolean isDigestEnable() {
		return digestEnable;
	}

	/**
	 * @param digestEnable the digestEnable to set
	 */
	public void setDigestEnable(boolean digestEnable) {
		this.digestEnable = digestEnable;
	}

	/**
	 * @return the digestInterval
	 */
	public int getDigestInterval() {
		return digestInterval;
	}

	/**
	 * @param digestInterval the digestInterval to set
	 */
	public void setDigestInterval(int digestInterval) {
		this.digestInterval = digestInterval;
	}

}
