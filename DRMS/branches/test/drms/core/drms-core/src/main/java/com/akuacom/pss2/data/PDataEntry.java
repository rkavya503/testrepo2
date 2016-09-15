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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.pss2.data.usage.CurrentUsageDataEntry;
import com.akuacom.utils.lang.DateUtil;

/**
 * The Class PartDataEntry.
 */
@Entity
@Table(name = "dataentry")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
    @NamedQuery(name = "PDataEntry.findDatasetNameByDatasourceOwner",
    	query = "select distinct de.dataSet.name from PDataEntry de where de.datasource.ownerID = :ownerID",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "PDataEntry.findLatestContactBySourceAndSet",
        query = "select max(de.time) from PDataEntry de where de.datasource.UUID = :sourceUUID and de.dataSet.UUID = :setUUID",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "PDataEntry.findByDataSourceUUID",
        query = "select de from PDataEntry de where de.datasource.UUID = :sourceUUID order by de.time desc",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "PDataEntry.findByDataSourceUUIDAndDate",
        query = "select de from PDataEntry de where de.datasource.UUID = :sourceUUID and date(de.time) = :day order by de.time",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "PDataEntry.findByDataSourceUUIDAndDates",
        query = "select de from PDataEntry de where de.datasource.UUID = :sourceUUID and date(de.time) >= :begin and date(de.time) <= :end order by de.time ",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "PDataEntry.findTenThree",
        query = "select de from PDataEntry de where de.datasource.UUID = :sourceUUID and date(de.time) <= :end order by de.time desc",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "PDataEntry.getDataEntriesByOwnersAndDates",
                query = "select de from PDataEntry de where de.datasource.ownerID in (:ownerID) and de.time >= :start and de.time <= :end order by de.dataSet.UUID, de.datasource.UUID, de.time  "),
    @NamedQuery(name = "PDataEntry.getLastActualTime",
            query = "select max(time) from PDataEntry de where de.datasource.UUID in (:sIds)"),
            
    @NamedQuery(name = "PDataEntry.getLastActualTimeByDatasourceOwner",
            query = "select max(time) from PDataEntry de where de.datasource.ownerID = :ownerID"),
            
    @NamedQuery(name = "PDataEntry.getLatestDataEntry",
            query = "select sum(de.value),de.time from PDataEntry de where de.time=" +
            		"(select max(sub_de.time) from PDataEntry sub_de where sub_de.time>= :begin and sub_de.time<= :end " +
            		"and sub_de.time<= :currentTime and sub_de.datasource.UUID in (:sIds)) and de.datasource.UUID in (:sIds)"),
    @NamedQuery(name = "PDataEntry.deleteByDatasource",
            query = "delete from PDataEntry de where de.datasource.UUID = :sourceUUID "),
    @NamedQuery(name = "PDataEntry.findByIdsBefore",
            query = "select de from PDataEntry de where de.datasource.UUID = :oId and de.dataSet.UUID = :sId and de.time < :time order by de.time desc",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
            
    @NamedQuery(name = "PDataEntry.getDataDays",
            query = "select distinct(date(de.time)) from PDataEntry de where de.datasource.UUID = :oId and de.dataSet.UUID in (:sIds) "
               + " and de.time <= :end and de.time >= :start order by de.time",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "PDataEntry.getDataDaysBySourceAndSet",
            query = "select distinct(date(de.time)) from PDataEntry de where de.datasource.UUID = :oId and de.dataSet.UUID in (:sIds) "
               + " order by de.time",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
            
    @NamedQuery(name = "PDataEntry.countBySourceSetAndTime",
            query = "select count(*) from PDataEntry de where de.datasource = :o and de.dataSet = :s and de.actual = 1" +
             "and de.time = :time"),
    @NamedQuery(name = "PDataEntry.getTupleByIdsAndTimes",
            query = "select distinct new scala.Tuple2(de.UUID, de.time) from PDataEntry de where de.datasource.UUID = :oId and de.dataSet.UUID = :sId " +
             "and de.actual = 1 and de.time in (:times)",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "PDataEntry.getSyntheticDups",
            query = "select distinct deDup.UUID from PDataEntry deDup, PDataEntry de where deDup.actual <> 1 and " +
             "de.datasource.UUID = deDup.datasource.UUID "
            + "and de.dataSet.UUID = deDup.dataSet.UUID and de.time = deDup.time and de.actual=1"),
    @NamedQuery(name = "PDataEntry.deleteSyntheticDups",
            query = "delete from PDataEntry deDup where deDup.actual <> 1 and deDup.UUID in (:ids)"),
    @NamedQuery(name = "PDataEntry.findByDataSourceDataSetAndDates",
            query = "select de from PDataEntry de where de.datasource.UUID = :sourceUUID and de.dataSet.UUID = :setUUID and de.time >= :begin and de.time <= :end order by de.time ",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "PDataEntry.getLastActualTimeByDate",
            query = "select max(time) from PDataEntry de where de.datasource.UUID in (:sIds) and de.time >= :begin and de.time <= :end and de.actual=1"),
    @NamedQuery(name = "PDataEntry.getLastValidDate",
            query = "select de.datasource.UUID, max(de.time) from PDataEntry de where de.dataSet.UUID=:dataset_uuid and "
            		+ " de.datasource.UUID in (:uuids) and de.time >= :begin and de.time <= :end and de.actual=1"
            		+ " group by de.datasource.UUID"),
    @NamedQuery(name = "PDataEntry.getDataTime",
            query = "select de.time from PDataEntry de where de.dataSet.UUID=:dataset_uuid and "
            		+ " de.datasource.UUID =:datasource_uuid and de.time >= :begin and de.time <= :end and de.actual=1"),
    @NamedQuery(name = "PDataEntry.getVirtualData",
                    query = "select de from PDataEntry de where de.dataSet.UUID=:dataset_uuid and "
                    		+ " de.datasource.UUID =:datasource_uuid and de.time >= :begin and de.time <= :end and de.actual=0"),
    @NamedQuery(name = "PDataEntry.findByDataSourceDataSetAndTime",
            query = "select de from PDataEntry de where de.datasource.UUID = :sourceUUID and de.dataSet.UUID = :setUUID and de.time = :entryTime ",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class PDataEntry extends BaseEntity implements DataBinding {

	private static final long serialVersionUID = 231182875912275321L;

	/** The time. */
	@Column(name="`time`")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date time = new Date();
	
	@Column(name="`rawTime`")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date rawTime = new Date();
	
	@Transient
	private String strTime;
	
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
    
    public String getStrTime() {//time
        return DateUtil.format(time, "yyyy-MM-dd-HH-mm-ss");
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }
    public Date getRawTime() {
		return rawTime;
	}

	public void setRawTime(Date rawTime) {
		this.rawTime = rawTime;
	}
    @Override
    public String getDataSetUUID() {
        if (dataSet != null)  {
            return dataSet.getUUID();
}
        return null;
    }

    @Override
    public String getDataSourceUUID() {
        if (datasource != null)  {
            return datasource.getUUID();
        }
        return null;
    }
    
    public CurrentUsageDataEntry toCurrentDataEntry(){
    	CurrentUsageDataEntry entry = new CurrentUsageDataEntry();
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