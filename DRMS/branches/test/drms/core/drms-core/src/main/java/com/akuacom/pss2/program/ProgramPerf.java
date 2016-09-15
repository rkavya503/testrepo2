/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.ProgramEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

/**
 * Read only, view object entities for performance.  
 * 
 * Should be able to delete after refactor
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "program")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
    @NamedQuery(name = "ProgramPerf.findAll", 
    		query = "select p from ProgramPerf p",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "ProgramPerf.findByName",
    		query = "select p from ProgramPerf p where p.programName = :name",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})

public class ProgramPerf extends VersionedEntity {
    
	/** The program name. */
    @Column(name = "name")
	private String programName;
	
	/** The utility program name. */
	private String utilityProgramName; // name used by utility
	
	/** The class name. */
	private String className;
    
    /** The validator class. */
    private String validatorClass;
    
    /** The validator config file. */
    private String validatorConfigFile;
	
	/** The ui schedule event string. */
	private String uiScheduleEventString;
	
	/** The ui configure program string. */
	private String uiConfigureProgramString;
	
	/** The ui configure event string. */
	private String uiConfigureEventString;
    
    /** The state. */
    private Integer state;
    
    /** The description. */
    private String description;
    
    @Temporal(TemporalType.TIME)
    private Date autoRepeatTimeOfDay;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastErrorOpContact;

    private Boolean autoAccept;
    
    /** The priority. */
    private int priority;
    
    /** The min issue to start m. */
    private int minIssueToStartM;
    
    /** The must issue bdbe. */
    private boolean mustIssueBDBE;
    
    /** The max issue time h. */
    private	int maxIssueTimeH;
    
    /** The max issue time m. */
    private	int maxIssueTimeM;
    
    /** The min start time h. */
    private int minStartTimeH;
    
    /** The min start time m. */
    private int minStartTimeM;
    
    /** The max start time h. */
    private int maxStartTimeH;
    
    /** The max start time m. */
    private int maxStartTimeM;
    
    /** The min end time h. */
    private int minEndTimeH;
    
    /** The min end time m. */
    private int minEndTimeM;
    
    /** The max end time h. */
    private int maxEndTimeH;
    
    /** The max end time m. */
    private int maxEndTimeM;
    
    /** The min duration m. */
    private int minDurationM;
    
    /** The max duration m. */
    private int maxDurationM;
    
    /** The pending time dbeh. */
    private	int pendingTimeDBEH;
    
    /** The pending time dbem. */
    private	int pendingTimeDBEM;
    
    /** The notification param1. */
    private String notificationParam1;
    
    /** The manual creatable. */
    private boolean manualCreatable = true;
    
    /** The begin effective time. */
    private Date beginEffectiveTime;
    
    /** The end effective time. */
    private Date endEffectiveTime;
    
    /**
     * Gets the program name.
     * 
     * @return the program name
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the program name.
     * 
     * @param programName the new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * Gets the utility program name.
     * 
     * @return the utility program name
     */
    public String getUtilityProgramName() {
        return utilityProgramName;
    }

    /**
     * Sets the utility program name.
     * 
     * @param utilityProgramName the new utility program name
     */
    public void setUtilityProgramName(String utilityProgramName) {
        this.utilityProgramName = utilityProgramName;
    }

    /**
     * Gets the class name.
     * 
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the class name.
     * 
     * @param className the new class name
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Gets the validator class.
     * 
     * @return the validator class
     */
    public String getValidatorClass() {
        return validatorClass;
    }

    /**
     * Sets the validator class.
     * 
     * @param validatorClass the new validator class
     */
    public void setValidatorClass(String validatorClass) {
        this.validatorClass = validatorClass;
    }

    /**
     * Gets the validator config file.
     * 
     * @return the validator config file
     */
    public String getValidatorConfigFile() {
        return validatorConfigFile;
    }

    /**
     * Sets the validator config file.
     * 
     * @param validatorConfigFile the new validator config file
     */
    public void setValidatorConfigFile(String validatorConfigFile) {
        this.validatorConfigFile = validatorConfigFile;
    }

    /**
     * Gets the ui schedule event string.
     * 
     * @return the ui schedule event string
     */
    public String getUiScheduleEventString() {
        return uiScheduleEventString;
    }

    /**
     * Sets the ui schedule event string.
     * 
     * @param uiScheduleEventString the new ui schedule event string
     */
    public void setUiScheduleEventString(String uiScheduleEventString) {
        this.uiScheduleEventString = uiScheduleEventString;
    }

    /**
     * Gets the ui configure program string.
     * 
     * @return the ui configure program string
     */
    public String getUiConfigureProgramString() {
        return uiConfigureProgramString;
    }

    /**
     * Sets the ui configure program string.
     * 
     * @param uiConfigureProgramString the new ui configure program string
     */
    public void setUiConfigureProgramString(String uiConfigureProgramString) {
        this.uiConfigureProgramString = uiConfigureProgramString;
    }

    /**
     * Gets the ui configure event string.
     * 
     * @return the ui configure event string
     */
    public String getUiConfigureEventString() {
        return uiConfigureEventString;
    }

    /**
     * Sets the ui configure event string.
     * 
     * @param uiConfigureEventString the new ui configure event string
     */
    public void setUiConfigureEventString(String uiConfigureEventString) {
        this.uiConfigureEventString = uiConfigureEventString;
    }

    /**
     * Gets the state.
     * 
     * @return the state
     */
    public Integer getState() {
        return state;
    }

    /**
     * Sets the state.
     * 
     * @param state the new state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     * 
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Checks if is repeating events.
     * 
     * @return true, if is repeating events
     */
	public Date getAutoRepeatTimeOfDay() {
		return autoRepeatTimeOfDay;
	}

	public void setAutoRepeatTimeOfDay(Date autoRepeatTimeOfDay) {
		this.autoRepeatTimeOfDay = autoRepeatTimeOfDay;
	}

	public Date getLastErrorOpContact() {
		return lastErrorOpContact;
	}

	public void setLastErrorOpContact(Date lastErrorOpContact) {
		this.lastErrorOpContact = lastErrorOpContact;
	}
	
    public Boolean isAutoAccept() {
        return autoAccept;
    }

    public void setAutoAccept(Boolean autoAccept) {
        this.autoAccept = autoAccept;
    }
    
    /**
     * Gets the begin effective time.
     * 
     * @return the begin effective time
     */
    public Date getBeginEffectiveTime() {
        return beginEffectiveTime;
    }

    /**
     * Sets the begin effective time.
     * 
     * @param beginEffectiveTime the new begin effective time
     */
    public void setBeginEffectiveTime(Date beginEffectiveTime) {
        this.beginEffectiveTime = beginEffectiveTime;
    }

    /**
     * Gets the end effective time.
     * 
     * @return the end effective time
     */
    public Date getEndEffectiveTime() {
        return endEffectiveTime;
    }

    /**
     * Sets the end effective time.
     * 
     * @param endEffectiveTime the new end effective time
     */
    public void setEndEffectiveTime(Date endEffectiveTime) {
        this.endEffectiveTime = endEffectiveTime;
    }

    /**
     * Gets the priority.
     * 
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the priority.
     * 
     * @param priority the new priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Gets the min issue to start m.
     * 
     * @return the min issue to start m
     */
    public int getMinIssueToStartM() {
        return minIssueToStartM;
    }

    /**
     * Sets the min issue to start m.
     * 
     * @param minIssueToStartM the new min issue to start m
     */
    public void setMinIssueToStartM(int minIssueToStartM) {
        this.minIssueToStartM = minIssueToStartM;
    }

    /**
     * Checks if is must issue bdbe.
     * 
     * @return true, if is must issue bdbe
     */
    public boolean isMustIssueBDBE() {
        return mustIssueBDBE;
    }

    /**
     * Sets the must issue bdbe.
     * 
     * @param mustIssueBDBE the new must issue bdbe
     */
    public void setMustIssueBDBE(boolean mustIssueBDBE) {
        this.mustIssueBDBE = mustIssueBDBE;
    }

    /**
     * Gets the max issue time h.
     * 
     * @return the max issue time h
     */
    public int getMaxIssueTimeH() {
        return maxIssueTimeH;
    }

    /**
     * Sets the max issue time h.
     * 
     * @param maxIssueTimeH the new max issue time h
     */
    public void setMaxIssueTimeH(int maxIssueTimeH) {
        this.maxIssueTimeH = maxIssueTimeH;
    }

    /**
     * Gets the max issue time m.
     * 
     * @return the max issue time m
     */
    public int getMaxIssueTimeM() {
        return maxIssueTimeM;
    }

    /**
     * Sets the max issue time m.
     * 
     * @param maxIssueTimeM the new max issue time m
     */
    public void setMaxIssueTimeM(int maxIssueTimeM) {
        this.maxIssueTimeM = maxIssueTimeM;
    }

    /**
     * Gets the min start time h.
     * 
     * @return the min start time h
     */
    public int getMinStartTimeH() {
        return minStartTimeH;
    }

    /**
     * Sets the min start time h.
     * 
     * @param minStartTimeH the new min start time h
     */
    public void setMinStartTimeH(int minStartTimeH) {
        this.minStartTimeH = minStartTimeH;
    }

    /**
     * Gets the min start time m.
     * 
     * @return the min start time m
     */
    public int getMinStartTimeM() {
        return minStartTimeM;
    }

    /**
     * Sets the min start time m.
     * 
     * @param minStartTimeM the new min start time m
     */
    public void setMinStartTimeM(int minStartTimeM) {
        this.minStartTimeM = minStartTimeM;
    }

    /**
     * Gets the max start time h.
     * 
     * @return the max start time h
     */
    public int getMaxStartTimeH() {
        return maxStartTimeH;
    }

    /**
     * Sets the max start time h.
     * 
     * @param maxStartTimeH the new max start time h
     */
    public void setMaxStartTimeH(int maxStartTimeH) {
        this.maxStartTimeH = maxStartTimeH;
    }

    /**
     * Gets the max start time m.
     * 
     * @return the max start time m
     */
    public int getMaxStartTimeM() {
        return maxStartTimeM;
    }

    /**
     * Sets the max start time m.
     * 
     * @param maxStartTimeM the new max start time m
     */
    public void setMaxStartTimeM(int maxStartTimeM) {
        this.maxStartTimeM = maxStartTimeM;
    }

    /**
     * Gets the min end time h.
     * 
     * @return the min end time h
     */
    public int getMinEndTimeH() {
        return minEndTimeH;
    }

    /**
     * Sets the min end time h.
     * 
     * @param minEndTimeH the new min end time h
     */
    public void setMinEndTimeH(int minEndTimeH) {
        this.minEndTimeH = minEndTimeH;
    }

    /**
     * Gets the min end time m.
     * 
     * @return the min end time m
     */
    public int getMinEndTimeM() {
        return minEndTimeM;
    }

    /**
     * Sets the min end time m.
     * 
     * @param minEndTimeM the new min end time m
     */
    public void setMinEndTimeM(int minEndTimeM) {
        this.minEndTimeM = minEndTimeM;
    }

    /**
     * Gets the max end time h.
     * 
     * @return the max end time h
     */
    public int getMaxEndTimeH() {
        return maxEndTimeH;
    }

    /**
     * Sets the max end time h.
     * 
     * @param maxEndTimeH the new max end time h
     */
    public void setMaxEndTimeH(int maxEndTimeH) {
        this.maxEndTimeH = maxEndTimeH;
    }

    /**
     * Gets the max end time m.
     * 
     * @return the max end time m
     */
    public int getMaxEndTimeM() {
        return maxEndTimeM;
    }

    /**
     * Sets the max end time m.
     * 
     * @param maxEndTimeM the new max end time m
     */
    public void setMaxEndTimeM(int maxEndTimeM) {
        this.maxEndTimeM = maxEndTimeM;
    }

    /**
     * Gets the min duration m.
     * 
     * @return the min duration m
     */
    public int getMinDurationM() {
        return minDurationM;
    }

    /**
     * Sets the min duration m.
     * 
     * @param minDurationM the new min duration m
     */
    public void setMinDurationM(int minDurationM) {
        this.minDurationM = minDurationM;
    }

    /**
     * Gets the max duration m.
     * 
     * @return the max duration m
     */
    public int getMaxDurationM() {
        return maxDurationM;
    }

    /**
     * Sets the max duration m.
     * 
     * @param maxDurationM the new max duration m
     */
    public void setMaxDurationM(int maxDurationM) {
        this.maxDurationM = maxDurationM;
    }

    /**
     * Gets the pending time dbeh.
     * 
     * @return the pending time dbeh
     */
    public int getPendingTimeDBEH() {
        return pendingTimeDBEH;
    }

    /**
     * Sets the pending time dbeh.
     * 
     * @param pendingTimeDBEH the new pending time dbeh
     */
    public void setPendingTimeDBEH(int pendingTimeDBEH) {
        this.pendingTimeDBEH = pendingTimeDBEH;
    }

    /**
     * Gets the pending time dbem.
     * 
     * @return the pending time dbem
     */
    public int getPendingTimeDBEM() {
        return pendingTimeDBEM;
    }

    /**
     * Sets the pending time dbem.
     * 
     * @param pendingTimeDBEM the new pending time dbem
     */
    public void setPendingTimeDBEM(int pendingTimeDBEM) {
        this.pendingTimeDBEM = pendingTimeDBEM;
    }

    /**
     * Gets the notification param1.
     * 
     * @return the notification param1
     */
    public String getNotificationParam1() {
        return notificationParam1;
    }

    /**
     * Sets the notification param1.
     * 
     * @param notificationParam1 the new notification param1
     */
    public void setNotificationParam1(String notificationParam1) {
        this.notificationParam1 = notificationParam1;
    }

    /**
     * Checks if is manual creatable.
     * 
     * @return true, if is manual creatable
     */
    public boolean isManualCreatable() {
        return manualCreatable;
    }

    /**
     * Sets the manual creatable.
     * 
     * @param manualCreatable the new manual creatable
     */
    public void setManualCreatable(boolean manualCreatable) {
        this.manualCreatable = manualCreatable;
    }
}
