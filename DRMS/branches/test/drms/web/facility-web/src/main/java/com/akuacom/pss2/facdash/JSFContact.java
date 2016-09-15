/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.facdash.JSFContact.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.validator.EmailValidator;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.nssettings.NSSettings;
import com.akuacom.pss2.nssettings.NSSettingsManager;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;

/**
 * The Class JSFContact.
 */
public class JSFContact implements Serializable
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The Enum Type.
	 */
	public enum Type {
		EMAIL("Email Address"), 
		VOICE("Phone Number"), 
		FAX("Fax Number"), 
		PAGE("Pager Number"),
        SMS("SMS");
		
		private String addressText;
		
		private Type(String addressText) 
		{
			this.addressText = addressText;
		}
		
		public String getAddressText()
		{
			return addressText;
		}
	}
	
	/** The name. */
	private String name;
	
	/** The address. */
	private String address;
	
	/** The type. */
	private Type type = Type.EMAIL;
	
	/** The comm notification. */
	private boolean commNotification;
	
	/** The event notification. */
	private String eventNotification;
	
	/** The off season notification. */
	private double offSeasonNotification = 0.5;//DRMS-1537 JerryM
	
	/** The on season notification. */
	private double onSeasonNotification = 0.5;//DRMS-1537 JerryM
	
	/** The notes. */
	private String notes;
	
	/** The delete. */
	private boolean delete;
	
	/** The edit. */
	private boolean edit;

	
    /** message threshold to this contact per day **/
    private int msgThreshold = 0;
    private boolean defaultMsgThreshold = true;
    
	private boolean external;
	
    //Demand Limiting
    private Boolean demandLimitingNotifications   = new Boolean(true);
    
    private Boolean participantDemandLimitingEnabled = new Boolean(false);
	
    private String UUID;
	/**
	 * Instantiates a new jSF contact.
	 */
	public JSFContact()
	{
		this.commNotification = true;
		this.setDefaultMsgThreshold(true);
		this.setMsgThreshold(0);	
	}
	/**
	 * Instantiates a new jSF contact.
	 * 
	 * @param contact the contact
	 */
	public JSFContact(ParticipantContact contact)
	{
		this.UUID=contact.getUUID();
		this.name = contact.getDescription();
		this.address = contact.getAddress();
		if(contact.getType().equals(Contact.EMAIL_ADDRESS))
		{
			this.type = Type.EMAIL;
		}
		else if(contact.getType().equals(Contact.FAX_NUMBER))
		{
			this.type = Type.FAX;			
		}
		else if(contact.getType().equals(Contact.PAGER))
		{
			this.type = Type.PAGE;			
		}
		else if(contact.getType().equals(Contact.PHONE_NUMBER))
		{
			this.type = Type.VOICE;			
		} else if (contact.getType().equals(Contact.SMS)) {
			this.type = Type.SMS;
		}
		this.commNotification = contact.isCommNotification();
		this.eventNotification = contact.getEventNotification().toString();		
		this.onSeasonNotification = contact.getOnSeasonNotiHours();
		this.offSeasonNotification = contact.getOffSeasonNotiHours();
		this.defaultMsgThreshold = contact.getDefaultMsgThreshold();
		this.setMsgThreshold(contact.getMsgThreshold().intValue());			
		
		this.setDemandLimitingNotifications(contact.getDemandLimitingNotifications());
			
		this.external = contact.getExternal();
	}

	/**
	 * Parses the contact.
	 * 
	 * @return the contact
	 */
	public ParticipantContact parseContact()
	{
		ParticipantContact contact = new ParticipantContact();
		contact.setDescription(name);
		contact.setAddress(address);
		switch(type)
		{
		case EMAIL:
			contact.setType(Contact.EMAIL_ADDRESS);
			break;
		case FAX:
			contact.setType(Contact.FAX_NUMBER);
			break;
		case PAGE:
			contact.setType(Contact.PAGER);
			break;
		case VOICE:
			contact.setType(Contact.PHONE_NUMBER);
			break;
		case SMS:
			contact.setType(Contact.SMS);
			break;
		}
		
		Double onSeasonNoti = 0.5;//DRMS-1537 JerryM
		Double offSeasonNoti = 0.5;//DRMS-1537 JerryM
		
		try {
			onSeasonNoti = Double.valueOf(onSeasonNotification);
			offSeasonNoti = Double.valueOf(offSeasonNotification);
		} catch (NumberFormatException nfex) {
			// dan will validate input on client
		}
		
		contact.setOnSeasonNotiHours(onSeasonNoti);
		contact.setOffSeasonNotiHours(offSeasonNoti);
		contact.setCommNotification(commNotification);
		contact.setEventNotification(ContactEventNotificationType.valueOf(eventNotification));
		contact.setDefaultMsgThreshold(defaultMsgThreshold);
		contact.setMsgThreshold(msgThreshold);
		contact.setExternal(external);
		
	    contact.setDemandLimitingNotifications(demandLimitingNotifications);
		contact.setUUID(this.UUID);
		return contact;
	}
	
	/**
	 * Gets the available modes.
	 * 
	 * @return the available modes
	 */
	public List<SelectItem> getAvailableTypes()
	{
		List<SelectItem> availableTypes = new ArrayList<SelectItem>();
        Header1 header1 = FDUtils.getHeader1();
        if(!header1.isExtendedNotification()){
			availableTypes.add(new SelectItem(Type.EMAIL));

		} else if (header1.getUtilityName().equals("heco")) {
            availableTypes.add(new SelectItem(Type.EMAIL));
            availableTypes.add(new SelectItem(Type.VOICE));
            availableTypes.add(new SelectItem(Type.SMS));

        } else {
            availableTypes.add(new SelectItem(Type.EMAIL));
            availableTypes.add(new SelectItem(Type.VOICE, Type.VOICE + " (DBP Only)"));
            availableTypes.add(new SelectItem(Type.FAX, Type.FAX + " (DBP Only)"));
            availableTypes.add(new SelectItem(Type.PAGE, Type.PAGE + " (DBP Only)"));
            availableTypes.add(new SelectItem(Type.SMS, Type.SMS + " (DBP Only)"));
        }
		return availableTypes;
	}
	
	public boolean isDemandLimitingEnabled() {
		return (FDUtils.getHeader1().isDemandLimitingEnabled());
	}
	
	/**
	 * Gets the available event notification types.
	 * 
	 * @return the available operators
	 */
	public List<SelectItem> getAvailableEventNotificationTypes()
	{
		List<SelectItem> availableEventTypes = new ArrayList<SelectItem>();
		for (ContactEventNotificationType eventType : ContactEventNotificationType.values())
		{
			availableEventTypes.add(new SelectItem(eventType.toString(), eventType.description()));
		}
		return availableEventTypes;
	}
	
	public String getEventNotificationDisplay() {
		return ContactEventNotificationType.valueOf(eventNotification).description();
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * Sets the address.
	 * 
	 * @param address the new address
	 */
	public void setAddress(String address)
	{
		this.address = address;
	}
    
    public String getAddressText() {
        return type.getAddressText();
    }

    /**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Type getType()
	{
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	public void setType(Type type)
	{
		this.type = type;
	}

	/**
	 * Checks if is comm notification.
	 * 
	 * @return true, if is comm notification
	 */
	public boolean isCommNotification()
	{
		return commNotification;
	}

	/**
	 * Sets the comm notification.
	 * 
	 * @param commNotification the new comm notification
	 */
	public void setCommNotification(boolean commNotification)
	{
		this.commNotification = commNotification;
	}

	/**
	 * Checks if is event notification.
	 * 
	 * @return true, if is event notification
	 */
	public String getEventNotification()
	{
		return eventNotification;
	}

	/**
	 * Sets the event notification.
	 * 
	 * @param eventNotification the new event notification
	 */
	public void setEventNotification(String eventNotification)
	{
		this.eventNotification = eventNotification;
	}

	/**
	 * Gets the off season notification.
	 * 
	 * @return the off season notification
	 */
	public double getOffSeasonNotification()
	{
		return offSeasonNotification;
	}

	/**
	 * Sets the off season notification.
	 * 
	 * @param offSeasonNotification the new off season notification
	 */
	public void setOffSeasonNotification(double offSeasonNotification)
	{
		this.offSeasonNotification = offSeasonNotification;
	}

	/**
	 * Gets the on season notification.
	 * 
	 * @return the on season notification
	 */
	public double getOnSeasonNotification()
	{
		return onSeasonNotification;
	}

	/**
	 * Sets the on season notification.
	 * 
	 * @param onSeasonNotification the new on season notification
	 */
	public void setOnSeasonNotification(double onSeasonNotification)
	{
		this.onSeasonNotification = onSeasonNotification;
	}

	/**
	 * Gets the notes.
	 * 
	 * @return the notes
	 */
	public String getNotes()
	{
		return notes;
	}

	/**
	 * Sets the notes.
	 * 
	 * @param notes the new notes
	 */
	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	/**
	 * Checks if is delete.
	 * 
	 * @return true, if is delete
	 */
	public boolean isDelete()
	{
		return delete;
	}

	/**
	 * Sets the delete.
	 * 
	 * @param delete the new delete
	 */
	public void setDelete(boolean delete)
	{
		this.delete = delete;
	}

	/**
	 * Checks if is edits the.
	 * 
	 * @return true, if is edits the
	 */
	public boolean isEdit()
	{
		return edit;
	}
	
	/**
	 * get the message threshold 
	 * @return
	 */
	public int getMsgThreshold() {
		return msgThreshold;
	}
	
	/**
	 * set message threshold
	 * @param msgThreshold
	 */
	public void setMsgThreshold(int msgThreshold) {
		NSSettingsManager NSSettingsManager = EJBFactory.getBean(NSSettingsManager.class);
		NSSettings settings = NSSettingsManager.getNSSettings();
		if ((this.defaultMsgThreshold) && (settings!=null)) {
			this.msgThreshold = settings.getMsgThreshold();
		} else {
			this.msgThreshold = msgThreshold;
		}
	}
    
    public boolean getDefaultMsgThreshold() {
		return defaultMsgThreshold;
	}

	public void setDefaultMsgThreshold(boolean defaultMsgThreshold) {
		this.defaultMsgThreshold = defaultMsgThreshold;
		//this.setMsgThreshold(this.getMsgThreshold());
	}
	
	/**
	 * Sets the edits the.
	 * 
	 * @param edit the new edits the
	 */
	public void setEdit(boolean edit)
	{
		this.edit = edit;
	}
	
	public String getKey()
	{
		return name + "." + type.toString() + "." + address;
	}
	
    public boolean isExternal()
	{
		return external;
	}
    
	public void setExternal(boolean external)
	{
		this.external = external;
	}
	
    public void validateEmail(FacesContext context, UIComponent toValidate, Object value) {
        if (type == Type.EMAIL) {
            String email = (String) value;
            if (!EmailValidator.getInstance().isValid(email)) {
                ((UIInput) toValidate).setValid(false);
                FacesMessage message = new FacesMessage("Invalid email");
                context.addMessage(toValidate.getClientId(context), message);
            }
        }
    }

	public void setDemandLimitingNotifications(
			Boolean demandLimitingNotifications) {
		this.demandLimitingNotifications = demandLimitingNotifications;
	}
	public Boolean getDemandLimitingNotifications() {
		return demandLimitingNotifications;
	}
	public Boolean getParticipantDemandLimitingEnabled() {
		return participantDemandLimitingEnabled;
	}
	public void setParticipantDemandLimitingEnabled(
			Boolean participantDemandLimitingEnabled) {
		this.participantDemandLimitingEnabled = participantDemandLimitingEnabled;
	}
	/**
	 * @return the uUID
	 */
	public String getUUID() {
		return UUID;
	}
	/**
	 * @param uUID the uUID to set
	 */
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	
	
}