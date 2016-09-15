/*
 * www.akuacom.com Automating Demand Response
 *
 * com.akuacom.pss2.partdata.PartDataEntry.java - Copyright 1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data.usage;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.data.DataBinding;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;

/**
 * The Class PartDataEntry.
 */
@Entity
@Table(name = "dataentry_temp")
@NamedQueries({
    @NamedQuery(name = "CurrentUsageDataEntry.findDatasetNameByDatasourceOwner",
    	query = "select distinct de.dataSet.name from CurrentUsageDataEntry de where de.datasource.ownerID = :ownerID",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "CurrentUsageDataEntry.findLatestContactBySourceAndSet",
        query = "select max(de.time) from CurrentUsageDataEntry de where de.datasource.UUID = :sourceUUID and de.dataSet.UUID = :setUUID",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "CurrentUsageDataEntry.findByDataSourceUUID",
        query = "select de from CurrentUsageDataEntry de where de.datasource.UUID = :sourceUUID order by de.time desc",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "CurrentUsageDataEntry.findByDataSourceUUIDAndDate",
        query = "select de from CurrentUsageDataEntry de where de.datasource.UUID = :sourceUUID and date(de.time) = :day order by de.time",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "CurrentUsageDataEntry.findByDataSourceUUIDAndDates",
        query = "select de from CurrentUsageDataEntry de where de.datasource.UUID = :sourceUUID and date(de.time) >= :begin and date(de.time) <= :end order by de.time ",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "CurrentUsageDataEntry.findTenThree",
        query = "select de from CurrentUsageDataEntry de where de.datasource.UUID = :sourceUUID and date(de.time) <= :end order by de.time desc",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "CurrentUsageDataEntry.findLastActualTimeByDate",
        query = "select max(de.time) from CurrentUsageDataEntry de where de.datasource.UUID in (:sIds) and de.time >= :begin and de.time <= :end and de.actual=1",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
	@NamedQuery(name = "CurrentUsageDataEntry.getLastActualTimeByDatasourceOwner",
        query = "select max(time) from CurrentUsageDataEntry de where de.datasource.ownerID = :ownerID"),
    @NamedQuery(name = "CurrentUsageDataEntry.getMaxTime",
            query = "select max(de.time) from CurrentUsageDataEntry de",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}
    
    )
})
public class CurrentUsageDataEntry  extends BaseEntity implements DataBinding{
	private static final long serialVersionUID = 1L;

	/** The time. */
	@Column(name="`time`")
    private Date time;

    /** The value. */
    protected Double value;

    private String stringValue;

    private String valueType = Double.class.getName();

    private boolean actual;

    @ManyToOne
    @JoinColumn(name = "dataset_uuid")
    private PDataSet dataSet;

    @ManyToOne
    @JoinColumn(name = "datasource_uuid")
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
    
    public PDataEntry toDataEntry(){
    	PDataEntry entry = new PDataEntry();
    	entry.setActual(isActual());
    	entry.setTime(getTime());
    	entry.setDataSet(getDataSet());
    	entry.setDatasource(getDatasource());
    	entry.setValue(getValue());
    	entry.setValueType(getValueType());
    	entry.setStringValue(getStringValue());
    	return entry;
    }
    
}