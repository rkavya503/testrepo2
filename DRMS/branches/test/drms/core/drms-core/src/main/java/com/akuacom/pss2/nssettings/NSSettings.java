/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.nssettings.NSSettings.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.nssettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;

/**
 * 
 * The Class NSSettingsVo. Mapping to message_filter_settings table
 * 
 * @author Li Fei
 * 
 * Initial date 2010.07.01
 */

@Entity
@Table(name="message_filter_setting")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class NSSettings extends BaseEntity {
    
	private static final long serialVersionUID = 9206679204448630263L;
	
	private int filterStatus;
    
    private int msgThreshold;

    private int frequency;

    private int duration;

    private int msgExpireTime;

    @Transient
    private String exceptionContacts;
    
	private int cleanMsgHour;
    
    private int cleanMsgMinute;

	public int getFilterStatus() {
		return filterStatus;
	}

	public void setFilterStatus(int filterStatus) {
		this.filterStatus = filterStatus;
	}

	public int getMsgThreshold() {
		return msgThreshold;
	}

	public void setMsgThreshold(int msgThreshold) {
		this.msgThreshold = msgThreshold;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getMsgExpireTime() {
		return msgExpireTime;
	}

	public void setMsgExpireTime(int msgExpireTime) {
		this.msgExpireTime = msgExpireTime;
	}

	public List<String> getExceptionContactsAsList() {
		if(this.exceptionContacts==null)
			return Collections.emptyList();
		String strs[]=exceptionContacts.split(",");
		for(int i=0;i<strs.length;i++){
			strs[i]=strs[i].trim();
		}
		return Arrays.asList(strs);
	}

	
	public String getExceptionContacts() {
		return exceptionContacts;
	}
	
	public void setExceptionContacts(String exceptionContacts) {
		this.exceptionContacts = exceptionContacts;
	}
	
	public int getCleanMsgHour() {
		return cleanMsgHour;
	}

	public void setCleanMsgHour(int cleanMsgHour) {
		this.cleanMsgHour = cleanMsgHour;
	}

	public int getCleanMsgMinute() {
		return cleanMsgMinute;
	}

	public void setCleanMsgMinute(int cleanMsgMinute) {
		this.cleanMsgMinute = cleanMsgMinute;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
	    sb.append("status = ").append(filterStatus);
	    sb.append(", threshold = ").append( msgThreshold);
	    sb.append(", frequency = ").append(frequency);
	    sb.append(", duration = ").append(duration);
	    sb.append(", msgExpireTime = ").append(msgExpireTime);
	    sb.append(", cleanMsgHour = ").append(cleanMsgHour);
	    sb.append(", cleanMsgMinute = ").append(cleanMsgMinute);
	    return sb.toString();
	}
}
