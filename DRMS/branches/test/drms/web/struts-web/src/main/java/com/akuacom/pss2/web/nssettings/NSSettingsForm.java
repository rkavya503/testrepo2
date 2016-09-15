/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.options.EmailsForm.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.nssettings;

import org.apache.struts.action.ActionForm;

/**
 * The struts form for notification system global settings.
 * 
 * @author Li Fei
 * 
 * Initial date 2010.07.01
 */
public class NSSettingsForm extends ActionForm {
    
	private String UUID;
	
	/** The message filter status. */
    private int filterStatus;

	/** The message expire time. */
    private int msgExpireTime;
    
    /** The threshold of similar message */
    private int msgThreshold;
    
    /** The check time of similar message. */
    private int frequency;
    
	/** The hour part of clear message time. */
    private int cleanMsgHour;

    /** The minute part of clear message time. */
    private int cleanMsgMinute;

    /** The block duration. */
    private int duration;

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String UUID) {
		this.UUID = UUID;
	}

	public int getFilterStatus() {
		return filterStatus;
	}

	public void setFilterStatus(int filterStatus) {
		this.filterStatus = filterStatus;
	}

	public int getMsgExpireTime() {
		return msgExpireTime;
	}

	public void setMsgExpireTime(int msgExpireTime) {
		this.msgExpireTime = msgExpireTime;
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
	
}
