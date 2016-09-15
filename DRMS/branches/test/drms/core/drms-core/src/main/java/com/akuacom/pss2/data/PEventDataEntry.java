/*
 * www.akuacom.com Automating Demand Response
 *
 * com.akuacom.pss2.partdata.PartDataEntry.java - Copyright 1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.utils.lang.DateUtil;

/**
 * The Class PartDataEntry.
 */
@Entity
@Table(name = "event_dataentry")
public class PEventDataEntry extends BaseEntity {

	
	private static final long serialVersionUID = 231182875912275321L;

	/** The time. */
	@Column(name="`time`")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date time = new Date();

	@Transient
	private String strTime;
	
	private String eventName;
	
    /** The value. */
    protected Double value;

    private String stringValue;

    private String valueType = Double.class.getName();

    private boolean actual; 
    
    @ManyToOne
    @JoinColumn(name = "dataset_uuid", nullable=false)
    private PDataSet dataSet;

    
    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    public PDataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(PDataSet dataSet) {
        this.dataSet = dataSet;
    }
    
    /**
     * Gets the time.
     *
     * @return the time
     */
    public Date getTime() {
        return time;
    }
    
    public long getMillis() {
        return time.getTime();
    }

    /**
     * Sets the time.
     *
     * @param time the new time
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(Double value) {
        this.value = value;
    }
    

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
    
    public String getStrTime() {//time
        return DateUtil.format(time, "yyyy-MM-dd-HH-mm-ss");
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }
    
    public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
    
}