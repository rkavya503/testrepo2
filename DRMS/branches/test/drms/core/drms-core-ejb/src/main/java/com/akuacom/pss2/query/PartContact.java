package com.akuacom.pss2.query;

import java.io.Serializable;

import com.akuacom.pss2.participant.contact.ContactEventNotificationType;

public class PartContact implements Serializable{

	private static final long serialVersionUID = 2657944251515351421L;
	//participant name
	private String name;
	private String address;
	private ContactEventNotificationType eventNotification; 
	private Boolean commNotification;
	private Boolean demandLimitingNotifications;
	private String description;//contact name
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public ContactEventNotificationType getEventNotification() {
		return eventNotification;
	}
	public void setEventNotification(ContactEventNotificationType eventNotification) {
		this.eventNotification = eventNotification;
	}
	public Boolean getCommNotification() {
		return commNotification;
	}
	public void setCommNotification(Boolean commNotification) {
		this.commNotification = commNotification;
	}
	public Boolean getDemandLimitingNotifications() {
		return demandLimitingNotifications;
	}
	public void setDemandLimitingNotifications(Boolean demandLimitingNotifications) {
		this.demandLimitingNotifications = demandLimitingNotifications;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
