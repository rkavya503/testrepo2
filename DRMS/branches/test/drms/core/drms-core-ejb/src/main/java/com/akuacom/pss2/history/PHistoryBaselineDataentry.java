/*
 * www.akuacom.com Automating Demand Response
 *
 * com.akuacom.pss2.partdata.PartDataEntry.java - Copyright 1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.history;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.data.DataBinding;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;

/**
 * The Class PartDataEntry.
 */
@Entity
@Table(name = "history_baseline_dataentry")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
	 @NamedQuery(name = "PHistoryBaselineDataentry.getByTime",
	            query = "select p from PHistoryBaselineDataentry p where p.time = :entrytime ")
})
public class PHistoryBaselineDataentry extends BaseEntity implements DataBinding{

	private static final long serialVersionUID = -9113676950341660563L;

	/** The time. */
	@Column(name="`time`")
    private Date time = new Date();

    /** The value. */
    protected Double value;

    private String stringValue;

    private String valueType = Double.class.getName();

    private boolean actual; 

    @ManyToOne
    @JoinColumn(name = "dataset_uuid", nullable=false)
    private PDataSet dataSet;

    @ManyToOne
    @JoinColumn(name = "datasource_uuid", nullable=false)
    private PDataSource datasource;

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

    public PDataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(PDataSource datasource) {
        this.datasource = datasource;
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
    
    public String getDataSetUUID() {
        if (dataSet != null)  {
            return dataSet.getUUID();
        }
        return null;
    }

    public String getDataSourceUUID() {
        if (datasource != null)  {
            return datasource.getUUID();
        }
        return null;
    }
}