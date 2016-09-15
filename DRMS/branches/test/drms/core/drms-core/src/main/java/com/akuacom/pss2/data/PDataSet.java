/*
 * www.akuacom.com Automating Demand Response
 *
 * com.akuacom.pss2.partdata.PartData.java - Copyright 1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

/**
 * The Class PartData.
 */
@Entity
@Table(name = "dataset")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
	@NamedQuery(name = "PDataSet.getDatasetUUIDByDatasourceOwner",
	    	query = "select distinct de.dataSet.UUID from PDataEntry de where de.datasource.UUID = :sourceUUID",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
	@NamedQuery(name = "PDataSet.findDatasets",
	    	query = "select ds from PDataSet ds",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
	@NamedQuery(name = "PDataSet.findByName",
            query = "select ds from PDataSet ds where ds.name = :name",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
        })
public class PDataSet extends VersionedEntity implements Cloneable {

	private static final long serialVersionUID = 5780873407572093227L;

	/** The Constant CURRENT_USAGE. */
    public static final String CURRENT_USAGE = "Current Usage";

    /** The Constant BID_LEVEL. */
    public static final String BID_LEVEL = "Bid Level";

    /** The Constant PROJECTED_SHED_USAGE. */
    public static final String PROJECTED_SHED_USAGE = "Projected Shed Usage";

    /** The Constant PROJECTED_NORMAL_USAGE. */
    public static final String PROJECTED_NORMAL_USAGE = "Projected Normal Usage";

    /** The Constant INSTRUCTION_LEVEL. */
    public static final String INSTRUCTION_LEVEL = "Instruction Level";

    /** The name. */
    private String name;

    /** The name. */
    private String enumTitles;

    /** The unit. */
    private String unit;

    /** The sync. */
    private boolean sync;

    /** The period. */
    @Column(columnDefinition="bigint(20) unsigned NOT NULL")
    private long period;

//    private String calcImplClass;
    
    private String calculationImplClass;

    private String description;
    
    public static final int DATA_TYPE_NUMBER = 0;
    public static final int DATA_TYPE_ENUM   = 1;
    public static final int DATA_TYPE_EVENT  = 2; 
    
    private int dataType = DATA_TYPE_NUMBER;
    
    public static final int GRAPH_LINE  = 0;
    public static final int GRAPH_CURVE = 1;
    public static final int GRAPH_POINT = 2;
    public static final int GRAPH_NONE  = 3;
    public static final int GRAPH_EVENT = 4;
    public static final int GRAPH_STEP  = 5;
    public static final int GRAPH_BAR   = 6;
    private int graphType = GRAPH_LINE;

    private int valuePrecision = 0;  // multiplier for decimal places  
    private String colorHint = null;
    private boolean interpolated = false;
    
//    public String getCalcImplClass() {
//        return calcImplClass;
//    }
//
//    public void setCalcImplClass(String calcImplClass) {
//        this.calcImplClass = calcImplClass;
//    }

    /**
     * Gets the identifying (short) name of this data set.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the identifying (short) name of this data set.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unit of measurement of this data set (example: KwH or MPH)
     *
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit of measurement of this data set (example: KwH or MPH)
     *
     * @param unit the new unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Indicates whether this data set is recorded at fixed (synchronous) 
     * intervals.  As in: exactly every 60 seconds, etc.
     * (The length of the synchronous period is set with the period property)
     *
     * @return true, if is synchronous
     */
    public boolean isSync() {
        return sync;
    }

    /**
     * Indicates whether this data set is recorded at fixed (synchronous) 
     * intervals.  As in: exactly every 60 seconds, etc.
     * (The length of the synchronous period is set with the period property)
     *
     * @param sync the new sync
     */
    public void setSync(boolean sync) {
        this.sync = sync;
    }

    /**
     * Gets the period (fixed time interval) of a synchronous data set
     *
     * @return the period
     */
    public long getPeriod() {
        return period;
    }

    /**
     * Sets the period (fixed time interval) of a synchronous data set
     *
     * @param period the new period
     */
    public void setPeriod(long period) {
        this.period = period;
    }

    /**
     * @return the descriptive name (display string) of this data set
     */
    public String getDescription() {
        if (description == null) return name;
        return description;
}

    /**
     * @param description the descriptive name (display string) of this data set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the dataType
     */
    public int getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the graphType
     */
    public int getGraphType() {
        return graphType;
    }

    /**
     * @param graphType the graphType to set
     */
    public void setGraphType(int graphType) {
        this.graphType = graphType;
    }

    @Override
    public PDataSet clone() {       
        PDataSet newDS = new PDataSet();
        newDS.setCalculationImplClass(getCalculationImplClass());
        newDS.setDataType(getDataType());
        newDS.setDescription(getDescription());
        newDS.setGraphType(getGraphType());
        newDS.setName(getName());
        newDS.setPeriod(getPeriod());
        newDS.setSync(isSync());
        newDS.setUnit(getUnit());
        return newDS;
    }

    /**
     * @return the valuePrecision
     */
    public int getValuePrecision() {
        return valuePrecision;
    }

    /**
     * @param valuePrecision the valuePrecision to set
     */
    public void setValuePrecision(int valuePrecision) {
        this.valuePrecision = valuePrecision;
    }

    /**
     * @return the colorHint
     */
    public String getColorHint() {
        return colorHint;
    }

    /**
     * @param colorHint the colorHint to set
     */
    public void setColorHint(String colorHint) {
        this.colorHint = colorHint;
    }

    /**
     * @return the interpolated
     */
    public boolean isInterpolated() {
        return interpolated;
    }

    /**
     * @param interpolated the interpolated to set
     */
    public void setInterpolated(boolean interpolated) {
        this.interpolated = interpolated;
    }

	public String getEnumTitles() {
		return enumTitles;
	}

	public void setEnumTitles(String enumTitles) {
		this.enumTitles = enumTitles;
	}

	public String getCalculationImplClass() {
		return calculationImplClass;
	}

	public void setCalculationImplClass(String calculationImplClass) {
		this.calculationImplClass = calculationImplClass;
	}
    
}