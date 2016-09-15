/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.Participant.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.annotations.DubiousColumnDefinition;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.participant.contact.ParticipantContactPerf;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.OperationModeValue;

/**
 * The Performance Entity Bean Participant.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "participant")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
        @NamedQuery(name = "ParticipantPerf.findAll",
                query = "select p from ParticipantPerf p",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ParticipantPerf.findClientNamesByParticipant",
                query = "select p.participantName from ParticipantPerf p where p.parent = :parentName",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ParticipantPerf.findClientsByParticipant",
                query = "select p from ParticipantPerf p where p.parent = :parentName",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ParticipantPerf.findByName",
                query = "select p from ParticipantPerf p where p.participantName = :name and p.client = :client",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ParticipantPerf.findAllByClient",
                query = "select p from ParticipantPerf p where p.client = :client",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ParticipantPerf.findClientBefore",
                query = "select p from ParticipantPerf p where p.commTime <= :time and p.client = 1",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ParticipantPerf.findByAccount",
                query = "select p from ParticipantPerf p where p.accountNumber = :account and p.client = :client",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        @NamedQuery(name = "ParticipantPerf.findByAccounts",
                query = "select p from ParticipantPerf p where p.accountNumber IN (:accounts)",
                hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class ParticipantPerf extends VersionedEntity {

	private static final long serialVersionUID = 3745732890651815311L;

    /** The participant name. */
    private String participantName;

    /** The type. */
    @Column(columnDefinition="varchar(12)")
    @DubiousColumnDefinition
    private byte type;

    /** The host address. */
    private String hostAddress;

    /** The manual control. */
    private boolean manualControl;
    
    private Date manualControlExpires;

    /** The account number. */
    @Column(name = "account")
    private String accountNumber;

    /** The first name. */
    private String firstName;

    /** The last name. */
    private String lastName;

    /** The meter name. */
    private String meterName;

    /** The contacts. */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "participantPerf")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<ParticipantContactPerf> contacts = new TreeSet<ParticipantContactPerf>();

    /** The feedback. */
    private boolean feedback;

    /** The meter id. */
    private String meterId;

    /** The notification level. */
    private int notificationLevel;

    /** The address. */
    private String address = "";

    /** The grid location. */
    private String gridLocation;

    /** The latitude. */
    private double latitude;

    /** The longitude. */
    private double longitude;

    /** The shed per hour kw. */
    private double shedPerHourKW;

    /** The last price. */
    private double lastPrice = -1.0;

    /** The client. */
    private boolean client;

    /** The parent. */
    private String parent;

    /** The comm time. */
    private Date commTime;

    /** The status. */
    @Column(columnDefinition="tinyint(3) unsigned DEFAULT '2'")
    @DubiousColumnDefinition
    private Integer status;

    /** The offline warning. */
    private Boolean offlineWarning;
    
    private Boolean testAccount = new Boolean(false);
    
    private Boolean activated = new Boolean(false);
    
    private Boolean dataEnabler = new Boolean(false);
    
    @Enumerated(value = EnumType.STRING)
    private EventStatus eventStatus = EventStatus.NONE;
    
    @Enumerated(value = EnumType.STRING)
    private OperationModeValue operationMode = OperationModeValue.UNKNOWN;
    
	/**
     * Gets the participant name.
     *
     * @return the participant name
     */
    public String getParticipantName() {
        return participantName;
    }

    /**
     * Sets the participant name.
     *
     * @param participantName the new participant name
     */
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public byte getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(byte type) {
        this.type = type;
    }

    /**
     * Gets the host address.
     *
     * @return the host address
     */
    public String getHostAddress() {
        return hostAddress;
    }

    /**
     * Sets the host address.
     *
     * @param hostAddress the new host address
     */
    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    /**
     * Gets the account number.
     *
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the account number.
     *
     * @param accountNumber the new account number
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Gets the first name.
     *
     * @return the firstName
     */
    public String getFirstName() {
        if (firstName == null)
            return "";
        else
            return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     *
     * @return the lastName
     */
    public String getLastName() {
        if (lastName == null)
            return "";
        else
            return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * Checks if is manual control.
     *
     * @return true, if is manual control
     */
    public boolean isManualControl() {
        return manualControl;
    }

    /**
     * Sets the manual control.
     *
     * @param manualControl the new manual control
     */
    public void setManualControl(boolean manualControl) {
        this.manualControl = manualControl;
    }

    public Date getManualControlExpires() {
		return manualControlExpires;
	}

	public void setManualControlExpires(Date manualControlExpires) {
		this.manualControlExpires = manualControlExpires;
	}

	/**
     * Checks if is feedback.
     *
     * @return true, if is feedback
     */
    public boolean isFeedback() {
        return feedback;
    }

    /**
     * Sets the feedback.
     *
     * @param feedback the new feedback
     */
    public void setFeedback(boolean feedback) {
        this.feedback = feedback;
    }

    /**
     * Gets the meter id.
     *
     * @return the meter id
     */
    public String getMeterId() {
        return meterId;
    }

    /**
     * Sets the meter id.
     *
     * @param meterId the new meter id
     */
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    /**
     * Gets the contacts - WARNING: may contain null entries in the list.
     *
     * @return the contacts
     */
    public Set<ParticipantContactPerf> getContacts() {
        return contacts;
    }

    /**
     * Sets the contacts.
     *
     * @param contacts the new contacts
     */
    public void setContacts(Set<ParticipantContactPerf> contacts) {
        this.contacts = contacts;
    }

    /**
     * Gets the notification level.
     *
     * @return the notification level
     */
    public int getNotificationLevel() {
        return notificationLevel;
    }

    /**
     * Sets the notification level.
     *
     * @param notificationLevel the new notification level
     */
    public void setNotificationLevel(int notificationLevel) {
        this.notificationLevel = notificationLevel;
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public String getAddress() {
        if (address == null){
        	return "";
        }
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
     * Gets the grid location.
     *
     * @return the grid location
     */
    public String getGridLocation() {
        return gridLocation;
    }

    /**
     * Sets the grid location.
     *
     * @param gridLocation the new grid location
     */
    public void setGridLocation(String gridLocation) {
        this.gridLocation = gridLocation;
    }

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude the new latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude the new longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the shed per hour kw.
     *
     * @return the shed per hour kw
     */
    public double getShedPerHourKW() {
        return shedPerHourKW;
    }

    /**
     * Sets the shed per hour kw.
     *
     * @param shedPerHourKW the new shed per hour kw
     */
    public void setShedPerHourKW(double shedPerHourKW) {
        this.shedPerHourKW = shedPerHourKW;
    }

    /**
     * Gets the last price.
     *
     * @return the last price
     */
    public double getLastPrice() {
        return lastPrice;
    }

    /**
     * Sets the last price.
     *
     * @param lastPrice the new last price
     */
    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    /**
     * Checks if is offline warning.
     *
     * @return true, if is offline warning
     */
    public boolean isOfflineWarning() {
        return offlineWarning;
    }

    /**
     * Sets the offline warning.
     *
     * @param offlineWarning the new offline warning
     */
    public void setOfflineWarning(boolean offlineWarning) {
        this.offlineWarning = offlineWarning;
    }
    
    /**
     * Checks if is client.
     *
     * @return true, if is client
     */
    public boolean isClient() {
        return client;
    }

    /**
     * Sets the client.
     *
     * @param client the new client
     */
    public void setClient(boolean client) {
        this.client = client;
    }

    /**
     * Gets the parent.
     *
     * @return the parent
     */
    public String getParent() {
        return parent;
    }

    /**
     * Sets the parent.
     *
     * @param parent the new parent
     */
    public void setParent(String parent) {
        this.parent = parent;
    }


    public Date getCommTime() {
		return commTime;
	}

	public void setCommTime(Date commTime) {
		this.commTime = commTime;
	}

	
    /**
     * Gets the client status.
     * 
     * @return the client status
     */
    public ClientStatus getClientStatus()
    {
    	return ClientStatus.values()[status];
    }
    
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean isTestAccount() {
		return testAccount;
	}

	public void setTestAccount(Boolean testAccount) {
		this.testAccount = testAccount;
	}

	public Boolean isActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public Boolean isDataEnabler() {
		if(dataEnabler == null){
			return false;
		}
		return dataEnabler;
	}

	public void setDataEnabler(Boolean activated) {
		this.dataEnabler = activated;
	}
	
	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	public OperationModeValue getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(OperationModeValue operationMode) {
		this.operationMode = operationMode;
	}

}