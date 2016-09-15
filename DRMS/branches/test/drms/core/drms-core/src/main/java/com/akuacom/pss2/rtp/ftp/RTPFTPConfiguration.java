/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.MessageVO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.rtp.ftp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
@Table(name="rtp_ftp_configuration")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
@NamedQueries( {
    @NamedQuery(name = "RTPFTPConfiguration.findAll", query = "select distinct o from RTPFTPConfiguration o")
})
public class RTPFTPConfiguration extends BaseEntity {
    
	private static final long serialVersionUID = -2018690057710812798L;
	
	/** The FTP URL */
	private String url;

	/** The FTP port */
	private String port;

	/** The FTP path*/
	private String path;

	/** The FTP file name*/
	private String fileName;
	
	/** The FTP user name*/
	private String userName;
	
	/** The FTP user password*/
	private String password;
	
	/** The FTP scan start time*/
    @Column(name = "scanStartTime")
	private String startTime;
	
	/** The FTP scan end time*/
    @Column(name = "scanEndTime")
	private String endTime;
	
	/** The FTP scan interval*/
    @Column(name = "scanInterval")
	private String interval;
	
	/** Required*/
	private boolean required;
	
	/** The MIN temperature, default value is -80*/
	private double minTemperature;
	
	/** The MAX temperature, default value is 130*/
	private double maxTemperature;
	
	/** The last process temperature file time*/
	private Date lastProcessTime;
	
	/** The notification send tag*/
	private boolean sendNotification;

	/** The FTP connection error sent flag */
	private boolean sentConnError;

	public String getUrl() {
		return url;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPort() {
		return port;
	}
    
	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public double getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(double minTemperature) {
		this.minTemperature = minTemperature;
	}

	public double getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(double maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public Date getLastProcessTime() {
		return lastProcessTime;
	}

	public void setLastProcessTime(Date lastProcessTime) {
		this.lastProcessTime = lastProcessTime;
	}

	public void setSendNotification(boolean sendNotification) {
		this.sendNotification = sendNotification;
	}

	public boolean isSendNotification() {
		return sendNotification;
	}

	public boolean isSentConnError() {
		return sentConnError;
	}

	public void setSentConnError(boolean sentConnError) {
		this.sentConnError = sentConnError;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
	    sb.append("url = ").append(url);
	    sb.append(", path = ").append( path);
	    sb.append(", fileName = ").append(fileName);
	    sb.append(", userName = ").append(userName);
	    sb.append(", password = ").append(password);
	    sb.append(", startTime = ").append(startTime);
	    sb.append(", endTime = ").append(endTime);
	    sb.append(", interval = ").append(interval);
	    sb.append(", minTemperature = ").append(minTemperature);
	    sb.append(", maxTemperature = ").append(maxTemperature);
	    sb.append(", lastProcessTime = ").append(lastProcessTime);
	    sb.append(", sendNotification = ").append(sendNotification);
	    return sb.toString();
	}

}
