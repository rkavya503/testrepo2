/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.commdev.CommDevVO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.commdev;

import java.util.Date;
import java.util.List;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.utils.ColUtil;

/**
 * This class holds the fields to be displayed on the CommDev list page.
 * All fields are for read only.
 * 
 * @author Dichen Mao
 * @since 4.1
 */
public class CommDevVO extends Participant {
    
    /** The programs. */
    private String programs;
    
    /** The signal levels. */
    private String signalLevels;
    
    /** The last contact. */
    private Date lastContact;
    
    /** The active. */
    private boolean active;
    
    /** The last contact in minute. */
    private long lastContactInMinute;
    
    /** The status string. */
    private String statusString;
    
    /** The status color. */
    private String statusColor;
    
    private boolean dataEnabler;
    
    private String clientsConfigForm2;

    /**
     * Gets the contact1.
     * 
     * @return the contact1
     */
    public String getContact1()
    {
        List<ParticipantContact> contacts = ColUtil.getList(getContacts());
        if(contacts.size() > 0)
        {
            return contacts.get(0).getAddress();
        }
        return "";
    }

    /**
     * Gets the contact2.
     * 
     * @return the contact2
     */
    public String getContact2()
    {
        List<ParticipantContact> contacts = ColUtil.getList(getContacts());
        if(contacts.size() > 1)
        {
            return contacts.get(1).getAddress();
        }
        return "";
    }

    /**
     * Gets the contact3.
     * 
     * @return the contact3
     */
    public String getContact3()
    {
        List<ParticipantContact> contacts = ColUtil.getList(getContacts());
        if(contacts.size() > 2)
        {
            return contacts.get(2).getAddress();
        }
        return "";
    }

    /**
     * Gets the contact4.
     * 
     * @return the contact4
     */
    public String getContact4()
    {
        List<ParticipantContact> contacts = ColUtil.getList(getContacts());
        if(contacts.size() > 3)
        {
            return contacts.get(3).getAddress();
        }
        return "";
    }

    
    /**
     * Gets the last contact.
     * 
     * @return the last contact
     */
    public Date getLastContact() {
        return lastContact;
    }

    /**
     * Sets the last contact.
     * 
     * @param lastContact the new last contact
     */
    public void setLastContact(Date lastContact) {
        this.lastContact = lastContact;
    }

    /**
     * Gets the programs.
     * 
     * @return the programs
     */
    public String getPrograms() {
        return programs;
    }

    /**
     * Sets the programs.
     * 
     * @param programs the new programs
     */
    public void setPrograms(String programs) {
        this.programs = programs;
    }

    /**
     * Gets the signal levels.
     * 
     * @return the signal levels
     */
    public String getSignalLevels() {
        return signalLevels;
    }

    /**
     * Sets the signal levels.
     * 
     * @param signalLevels the new signal levels
     */
    public void setSignalLevels(String signalLevels) {
        this.signalLevels = signalLevels;
    }

    /**
     * Gets the status string.
     * 
     * @return the status string
     */
    public String getStatusString() {
        return statusString;
    }

    /**
     * Sets the status string.
     * 
     * @param statusString the new status string
     */
    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    /**
     * Gets the status color.
     * 
     * @return the status color
     */
    public String getStatusColor() {
        return statusColor;
    }

    /**
     * Sets the status color.
     * 
     * @param statusColor the new status color
     */
    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    /**
     * Checks if is active.
     * 
     * @return true, if is active
     */
    public boolean isActive() {
        return active;
    }
  
    /**
     * Sets the active.
     * 
     * @param active the new active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Checks if is dataEnabler
     * 
     * @return true, if is dataEnabler
     */
    public boolean isdataEnabler() {
   	 return dataEnabler;
    }


    /**
     * Sets the dataEnabler.
     * 
     * @param dataEnabler the new dataEnabler
     */
    public void setDataEnabler(boolean dataEnabler) {
        this.dataEnabler = dataEnabler;
    }
    
    public String getClientsConfig2() {
        return clientsConfigForm2;
    }

    public void setClientsConfig2(String clientsConfigForm2) {	
        this.clientsConfigForm2 = clientsConfigForm2;
    }

    /**
     * Gets the last contact in minute.
     * 
     * @return the last contact in minute
     */
    public long getLastContactInMinute() {
        return lastContactInMinute;
    }

    /**
     * Sets the last contact in minute.
     * 
     * @param lastContactInMinute the new last contact in minute
     */
    public void setLastContactInMinute(long lastContactInMinute) {
        this.lastContactInMinute = lastContactInMinute;
    }
}
