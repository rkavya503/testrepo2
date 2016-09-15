/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.ProgramEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.event.EventTiming;
import com.akuacom.pss2.program.bidding.BidConfig;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.signal.ProgramSignal;

/**
 * The Class ProgramDAO.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "program")
@DiscriminatorValue("Program")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "programType", length = 64)
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries( {
		@NamedQuery(name = "Program.findAll", query = "select p from Program p", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Program.Priority.Max", query = "SELECT max(p.priority) FROM Program p"),
		@NamedQuery(name = "Program.Priority.Min", query = "SELECT min(p.priority) FROM Program p"),
		@NamedQuery(name = "Program.findByName", query = "select p from Program p where p.programName = :name", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "Program.findByProgramClass",query = "select p from Program p where p.programClass IN (:programClasses)", hints = {@QueryHint(name = "org.hibernate.cacheable", value = "false")}),
        @NamedQuery(name = "Program.findByLongProgramName",query = "select p from Program p where p.longProgramName = :longProgramName", hints = {@QueryHint(name = "org.hibernate.cacheable", value = "false")}),
        @NamedQuery(name = "Program.findByNameLoadBidConfig", query = "select distinct(p) from Program p left join fetch p.bidConfig where p.programName = :name"),
        @NamedQuery(name = "Program.getUiScheduleEventString", query = "select p.uiScheduleEventString from Program p where p.programName = :name", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Program.getProgramNameAndPriority", query = "select p.programName, p.priority from Program p", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Program.findByPrioritySorted", query = "SELECT p FROM Program p WHERE p.priority > :priority order by p.priority desc", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "Program.findByPriority", query = "SELECT p FROM Program p  order by p.priority asc", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "false") }),
        @NamedQuery(name = "Program.getProgramClassByName", query = "select p.programClass from Program p where p.programName = :name", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "Program.getProgramClassName", query = "select p.className from Program p where p.programName = :name", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        
        /*@NamedQuery(
                name = "Program.drasBiddingByProgramName",
                query = "SELECT p.bidConfig.drasBidding FROM Program p " +
                                        "LEFT JOIN p.bidConfig " +
                                        "where p.programName = :name",
                hints = { @QueryHint(name = "org.hibernate.cacheable",
                        value = "true") }),
        */
        
        
//        @NamedQuery(name = "Program.findProgramAndContactsdByProgramName",
//        query = "select distinct(p) from Program p left join fetch p.operatorContacts where p.programName =:name"),
        
        @NamedQuery(name = "Program.getBySecondaryUtilityName",
        query = "select p from Program p where p.secondaryUtilityName =:secondaryUtilityName"),
        
        @NamedQuery(name = "Program.findProgramPerfByProgramName",
        query = "select distinct(p) from Program p where p.programName =:name"),
        
        @NamedQuery(name = "Program.findProgramAndRulesdByProgramName",
        query = "select distinct(p) from Program p left join fetch p.rules where p.programName =:name"),
        
        @NamedQuery(name = "Program.findByUtilityProgramName",
                query = "SELECT p FROM Program p where p.utilityProgramName IN (:utilityProgramNames)"),
        
		// TODO: this is not the fail safe query. based only on the assumption
		// that priority never changes
		@NamedQuery(name = "Program.findProgramNameOrderedByPriority", query = "select distinct (p.programName) from Program p where p.state =1 order by p.priority", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true")}),
		
		//named queries for FINE grained API
		@NamedQuery(name  = "Program.findAllProgramsWithParticipants",      
				query = "select distinct(p) from Program p left join fetch p.programParticipants pp left join fetch pp.participant"),
//		@NamedQuery(name  = "Program.findProgramWithContacts",      
//					query = "select distinct(p) from Program p left join fetch p.operatorContacts  where p.programName =:name"),
		@NamedQuery(name  = "Program.findProgramWithParticipants",  
					query = "select distinct(p) from Program p left join fetch p.programParticipants pp left join fetch pp.participant where p.programName =:name"),
		@NamedQuery(name  = "Program.findProgramsWithParticipants",  
					query = "select  p from Program p left join fetch p.programParticipants pp left join fetch pp.participant where p.programName IN (:programNames)"),		
		@NamedQuery(name  = "Program.findProgramWithSignals",       
					query = "select distinct(p) from Program p left join fetch p.signals ps left join fetch ps.programSignalEntries pe left join fetch ps.signal s left join fetch s.signalLevels where p.programName =:name"),
		@NamedQuery(name  = "Program.findProgramWithSignalsPerf",       
					query = "select distinct(p) from Program p left join fetch p.signals ps left join fetch ps.signal s where p.programName =:name"),
	
		@NamedQuery(name  = "Program.findProgramWithBidConfig",     
					query = "select distinct(p) from Program p left join fetch p.bidConfig  pb left join fetch pb.bidBlocks where p.programName =:name"),
		@NamedQuery(name  = "Program.findProgramWithRules",         
					query = "select distinct(p) from Program p left join fetch p.rules  where p.programName =:name"),
		@NamedQuery(name  = "Program.findProgramWithProgramParticipantsAndPRules",       
				    query = "select distinct(p) from Program p left join fetch p.programParticipants pp left join fetch pp.constraint left join fetch pp.ppRules left join fetch pp.participant where p.programName =:name"),
		@NamedQuery(name  = "Program.findOnlyProgramWithProgramParticipantsAndPRules",       
				    query = "select distinct(p) from Program p left join fetch p.programParticipants pp left join fetch pp.constraint left join fetch pp.ppRules left join fetch pp.participant part where p.programName =:name and (part.parent IN (:participantName) or part.participantName IN (:participantName))"),
		@NamedQuery(name  = "Program.findProgramWithAllConfig", /*bid,signal,rules*/ 
				    query = "select distinct(p) from Program p left join fetch p.signals ps left join fetch ps.programSignalEntries left join fetch ps.signal s left join fetch s.signalLevels left join fetch p.bidConfig bid left join fetch bid.bidBlocks left join fetch p.rules where p.programName =:name"),
//					@NamedQuery(name  = "Program.findProgramWithAllConfig", /*bid,signal,rules,contact*/ 
//				    query = "select distinct(p) from Program p left join fetch p.signals ps left join fetch ps.programSignalEntries left join fetch ps.signal s left join fetch s.signalLevels left join fetch p.bidConfig bid left join fetch bid.bidBlocks left join fetch p.rules left join fetch p.operatorContacts where p.programName =:name"),
	    @NamedQuery(name  = "Program.findProgramWithBidAndProgramParticipantsAndPRules",       
				   query = "select distinct(p) from Program p left join fetch p.bidConfig pb left join fetch pb.bidBlocks left join fetch p.programParticipants pp left join fetch pp.constraint left join fetch pp.ppRules left join fetch pp.participant where p.programName =:name")
})
		
public class Program extends VersionedEntity implements Serializable {

	private static final long serialVersionUID = 9101158520872981605L;

	/**
	 * The Constant MIN_PER_HOUR.
	 */
	protected static final long MIN_PER_HOUR = 60;

	/**
	 * The Constant MS_PER_MIN.
	 */
	protected static final int MS_PER_MIN = 60000;

	/**
	 * The Constant MINUTES_PER_DAY.
	 */
	protected static final long MINUTES_PER_DAY = 24 * MIN_PER_HOUR;

	// TODO: this should be a config param
	/**
	 * The Constant TEST_EVENT_SPACING_MS.
	 */
	public static final long TEST_EVENT_SPACING_MS = 1000 * 60 * 2;

	/**
	 * The PROGRA m_ deleted.
	 */
	public static int PROGRAM_DELETED = 0;

	/**
	 * The PROGRA m_ active.
	 */
	public static int PROGRAM_ACTIVE = 1;

	public static int DEFAULT_DURATION = 120;
	
	/**
	 * The program name.
	 */
	@Column(name = "name")
	private String programName;
	
	/**
	 * The long program name.
	 */
	private String longProgramName;

	/**
	 * The program class.
	 */
	private String programClass;
	
	/**
	 * The utility program name.
	 */
	private String utilityProgramName; // name used by utility

	/**
	 * The secondary utility program name.
	 */
	private String secondaryUtilityName; 
	
	/**
	 * The class name.
	 */
	private String className;

	/**
	 * The validator class.
	 */
	private String validatorClass;

	/**
	 * The validator config file.
	 */
	private String validatorConfigFile;

	/**
	 * The ui schedule event string.
	 */
	private String uiScheduleEventString;

	/**
	 * The ui configure program string.
	 */
	private String uiConfigureProgramString;

	/**
	 * The ui configure event string.
	 */
	private String uiConfigureEventString;

	/**
	 * The state.
	 */
	private Integer state;

	/**
	 * The description.
	 */
	private String description;

	/**
	 * The priority.
	 */
	@Column(unique = true)
	private int priority;

	/**
	 * The signals.
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "program")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.REMOVE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ProgramSignal> signals;

	/**
	 * The min issue to start m.
	 */
	private int minIssueToStartM;

	/**
	 * The must issue bdbe.
	 */
	private boolean mustIssueBDBE;

	/**
	 * The max issue time h.
	 */
	private int maxIssueTimeH;

	/**
	 * The max issue time m.
	 */
	private int maxIssueTimeM;

	/**
	 * The min start time h.
	 */
	private int minStartTimeH;

	/**
	 * The min start time m.
	 */
	private int minStartTimeM;

	/**
	 * The max start time h.
	 */
	private int maxStartTimeH;

	/**
	 * The max start time m.
	 */
	private int maxStartTimeM;

	/**
	 * The min end time h.
	 */
	private int minEndTimeH;

	/**
	 * The min end time m.
	 */
	private int minEndTimeM;

	/**
	 * The max end time h.
	 */
	private int maxEndTimeH;

	/**
	 * The max end time m.
	 */
	private int maxEndTimeM;

	/**
	 * The min duration m.
	 */
	private int minDurationM;

	/**
	 * The max duration m.
	 */
	private int maxDurationM;

	/**
	 * The pending time dbeh.
	 */
	private int pendingTimeDBEH;

	/**
	 * The pending time dbem.
	 */
	private int pendingTimeDBEM;

	/**testProgram
	 * The notification param1.
	 */
	private String notificationParam1;

	/**
	 * The operator e mails.
	 */
	@Transient
	private String operatorEMails;

	/**
	 * The Number of participants in program.
	 */
	@Transient
	private int inParticipants;

	/**
	 * The Number of clients in program.
	 */
	@Transient
	private int inClients;

	/**
	 * The manual creatable.
	 */
	private boolean manualCreatable = true;

    /**
	 * The test program.
	 */
	private Boolean testProgram;
	
	private String programGroup;
	
	private Integer defaultDuration; 

	/**
	 * The bid config.
	 */
	//@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "program")
	//@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	//OneToMany to achieve lazy load for one-to-one mapping
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "program")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.REMOVE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<BidConfig> bidConfig;

	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "program")
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.REMOVE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ProgramParticipant> programParticipants;

	/**
	 * The begin effective time.
	 */
	private Date beginEffectiveTime;

	/**
	 * The end effective time.
	 */
	private Date endEffectiveTime;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "program")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.REMOVE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ProgramRule> rules;

	@Temporal(TemporalType.TIME)
	private Date autoRepeatTimeOfDay;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastErrorOpContact;

	private Boolean autoAccept;

	/**
	 * The remote program.
	 */
	@Transient
	private boolean remoteProgram;
	
	/**
	 * The _participants.
	 */
	@Transient
	private List<String> participants;
	
	public Program() {}

	/**
	 * Gets the participants.
	 * 
	 * @return the participants
	 */
	public List<String> getParticipants() {
		return participants;
	}

	/**
	 * Sets the participants.
	 * 
	 * @param participants
	 *            the new participants
	 */
	public void setParticipants(List<String> participants) {
		this.participants = participants;
	}

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
	 * @param programName
	 *            the new program name
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getLongProgramName() {
		return longProgramName;
	}

	public void setLongProgramName(String longProgramName) {
		this.longProgramName = longProgramName;
	}

	public String getProgramClass() {
		return programClass;
	}

	public void setProgramClass(String programClass) {
		this.programClass = programClass;
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
	 * @param utilityProgramName
	 *            the new utility program name
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
	 * @param className
	 *            the new class name
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
	 * @param validatorClass
	 *            the new validator class
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
	 * @param validatorConfigFile
	 *            the new validator config file
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
	 * @param uiScheduleEventString
	 *            the new ui schedule event string
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
	 * @param uiConfigureProgramString
	 *            the new ui configure program string
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
	 * @param uiConfigureEventString
	 *            the new ui configure event string
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
	 * @param state
	 *            the new state
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
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}	

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

	public Set<ProgramRule> getRules() {
		return rules;
	}

	/**
	 * Sets the rules.
	 * 
	 * @param rules
	 *            the new rules
	 */
	public void setRules(Set<ProgramRule> rules) {
		this.rules = rules;
	}

	public Boolean getAutoAccept() {
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
	 * @param beginEffectiveTime
	 *            the new begin effective time
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
	 * @param endEffectiveTime
	 *            the new end effective time
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
	 * @param priority
	 *            the new priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Gets the signals.
	 * 
	 * @return the signals
	 */
	public Set<ProgramSignal> getSignals() {
		return signals;
	}

	/**
	 * Checks if is remote program.
	 * 
	 * @return true, if is remote program
	 */
	public boolean isRemoteProgram() {
		return remoteProgram;
	}

	/**
	 * Sets the remote program.
	 * 
	 * @param remoteProgram
	 *            the new remote program
	 */
	public void setRemoteProgram(boolean remoteProgram) {
		this.remoteProgram = remoteProgram;
	}

	/**
	 * Sets the signals.
	 * 
	 * @param signals
	 *            the new signals
	 */
	public void setSignals(Set<ProgramSignal> signals) {
		this.signals = signals;
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
	 * @param minIssueToStartM
	 *            the new min issue to start m
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
	 * @param mustIssueBDBE
	 *            the new must issue bdbe
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
	 * @param maxIssueTimeH
	 *            the new max issue time h
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
	 * @param maxIssueTimeM
	 *            the new max issue time m
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
	 * @param minStartTimeH
	 *            the new min start time h
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
	 * @param minStartTimeM
	 *            the new min start time m
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
	 * @param maxStartTimeH
	 *            the new max start time h
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
	 * @param maxStartTimeM
	 *            the new max start time m
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
	 * @param minEndTimeH
	 *            the new min end time h
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
	 * @param minEndTimeM
	 *            the new min end time m
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
	 * @param maxEndTimeH
	 *            the new max end time h
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
	 * @param maxEndTimeM
	 *            the new max end time m
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
	 * @param minDurationM
	 *            the new min duration m
	 */
	public void setMinDurationM(int minDurationM) {
		this.minDurationM = minDurationM;
	}

	/**
	 * Gets the max duration ms.
	 * 
	 * @return the max duration ms
	 */
	public int getMaxDurationMS() {
		return maxDurationM * MS_PER_MIN;
	}

	public void setMaxDurationMS(int maxDurationMS) {
		this.maxDurationM = maxDurationMS / MS_PER_MIN;
	}

	/**
	 * Gets the min duration ms.
	 * 
	 * @return the min duration ms
	 */
	public long getMinDurationMS() {
		return minDurationM * MS_PER_MIN;
	}

	public void setMinDurationMS(long minDurationMS) {
		this.minDurationM = Math.round(minDurationMS / MS_PER_MIN);
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
	 * @param maxDurationM
	 *            the new max duration m
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
	 * @param pendingTimeDBEH
	 *            the new pending time dbeh
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
	 * @param pendingTimeDBEM
	 *            the new pending time dbem
	 */
	public void setPendingTimeDBEM(int pendingTimeDBEM) {
		this.pendingTimeDBEM = pendingTimeDBEM;
	}

	/**
	 * Gets the pending lead ms.
	 * 
	 * @param event
	 *            the event
	 * @return the pending lead ms
	 */
	public long getPendingLeadMS(EventTiming eventTiming) {
		// TODO: this should be cached since it never changes
		GregorianCalendar startCal = new GregorianCalendar();
		startCal.setTime(eventTiming.getStartTime());
		GregorianCalendar midnightDOE = new GregorianCalendar(startCal
				.get(Calendar.YEAR), startCal.get(Calendar.MONTH), startCal
				.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		long timeBeforeStartDOEMS = eventTiming.getStartTime().getTime()
				- midnightDOE.getTimeInMillis();

		if (mustIssueBDBE) {
			long midnightDBEMS = MINUTES_PER_DAY * MS_PER_MIN;
			long pendingTimeBDEMS = (pendingTimeDBEH * MIN_PER_HOUR + pendingTimeDBEM)
					* MS_PER_MIN;
			return timeBeforeStartDOEMS + (midnightDBEMS - pendingTimeBDEMS);
		}
		// this will most likely be overridden by the subclasses;
		return timeBeforeStartDOEMS;
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
	 * @param notificationParam1
	 *            the new notification param1
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
	 * @param manualCreatable
	 *            the new manual creatable
	 */
	public void setManualCreatable(boolean manualCreatable) {
		this.manualCreatable = manualCreatable;
	}

	/**
	 * Gets the bid config.
	 * 
	 * @return the bid config
	 */
	public BidConfig getBidConfig() {
		if(bidConfig!=null && !bidConfig.isEmpty()) return bidConfig.iterator().next();
		else return null;
	}
	
	/**
	 * Sets the bid config.
	 * 
	 * @param bidConfig
	 *            the new bid config
	 */
	public void setBidConfig(BidConfig bidConfig) {
		if(this.bidConfig==null){
			this.bidConfig = new HashSet<BidConfig>();
		}
		if(!this.bidConfig.isEmpty()) this.bidConfig.remove(0);
		this.bidConfig.add(bidConfig);
	}
	
	/**
	 * Gets the program participants.
	 * 
	 * @return the program participants
	 */
	public Set<ProgramParticipant> getProgramParticipants() {
		return programParticipants;
	}

	/**
	 * Gets num. program clients.
	 * 
	 * @return num. program clients.
	 */
	public int getInClients() {
		return inClients;
	}

	/**
	 * Sets num. program clients.
	 * 
	 * @param num
	 *            . program clients.
	 */
	public void setInClients(int inClients) {
		this.inClients = inClients;
	}

	/**
	 * Gets num. program participants.
	 * 
	 * @return num. program participants.
	 */
	public int getInParticipants() {
		return inParticipants;
	}

	/**
	 * Sets num. program participants.
	 * 
	 * @param num
	 *            . program participants.
	 */
	public void setInParticipants(int inParticipants) {
		this.inParticipants = inParticipants;
	}

    public String getSecondaryUtilityName() {
		return secondaryUtilityName;
	}

	public void setSecondaryUtilityName(String secondaryUtilityName) {
		this.secondaryUtilityName = secondaryUtilityName;
	}

	public Integer getDefaultDuration() {
		return defaultDuration;
	}

	public void setDefaultDuration(Integer defaultDuration) {
		this.defaultDuration = defaultDuration;
	}

	/**
	 * Checks if is test program.
	 *
	 * @return true, if is test program
	 */
	public Boolean getTestProgram() {
		return testProgram;
	}

	/**
	 * Sets the test program.
	 *
	 * @param testProgram
	 *            the new test program
	 */
	public void setTestProgram(Boolean testProgram) {
		this.testProgram = testProgram;
	}

	/**
	 * Sets the program participants.
	 * 
	 * @param programParticipants
	 *            the new program participants
	 */
	public void setProgramParticipants(
			Set<ProgramParticipant> programParticipants) {
		this.programParticipants = programParticipants;
	}

	public void setPendingTimeDBE(String time) {
		String[] timeParts = time.split(":");
		setPendingTimeDBEH(new Integer(timeParts[0]));
		setPendingTimeDBEM(new Integer(timeParts[1]));
	}

	public void setMinStartTime(String time) {
		String[] timeParts = time.split(":");
		setMinStartTimeH(new Integer(timeParts[0]));
		setMinStartTimeM(new Integer(timeParts[1]));
	}

	public void setMinEndTime(String time) {
		String[] timeParts = time.split(":");
		setMinEndTimeH(new Integer(timeParts[0]));
		setMinEndTimeM(new Integer(timeParts[1]));
	}

	public void setMaxStartTime(String time) {
		String[] timeParts = time.split(":");
		setMaxStartTimeH(new Integer(timeParts[0]));
		setMaxStartTimeM(new Integer(timeParts[1]));
	}

	public void setMaxIssueTime(String time) {
		String[] timeParts = time.split(":");
		setMaxIssueTimeH(new Integer(timeParts[0]));
		setMaxIssueTimeM(new Integer(timeParts[1]));
	}

	public void setMaxEndTime(String time) {
		String[] timeParts = time.split(":");
		setMaxEndTimeH(new Integer(timeParts[0]));
		setMaxEndTimeM(new Integer(timeParts[1]));
	}

	public Program copy(Program existing, String newProgramName) {
		Program prog = existing.getNewInstance();
		prog.setAutoAccept(existing.getAutoAccept());
		prog.setAutoRepeatTimeOfDay(existing.getAutoRepeatTimeOfDay());
		prog.setClassName(existing.getClassName());
		prog.setDescription(existing.getDescription());
		prog.setLastErrorOpContact(existing.getLastErrorOpContact());
		// prog.setModifiedTime(null);
		prog.setModifier(null);
		prog.setProgramName(newProgramName);
		prog.setState(existing.getState());
		prog.setNotificationParam1(existing.getNotificationParam1());
		prog.setUiConfigureEventString(existing.getUiConfigureEventString());
		prog
				.setUiConfigureProgramString(existing
						.getUiConfigureProgramString());
		prog.setUiScheduleEventString(existing.getUiScheduleEventString());
		prog.setUtilityProgramName(existing.getUtilityProgramName());
		prog.setValidatorClass(existing.getValidatorClass());
		prog.setValidatorConfigFile(existing.getValidatorConfigFile());

		if (existing.getRules() != null) {
			Set<ProgramRule> rules = new HashSet<ProgramRule>();
			for (ProgramRule programRule : existing.getRules()) {
				ProgramRule cloned = programRule.copy(programRule, prog);
				rules.add(cloned);
			}
			prog.setRules(rules);
		}

		prog.setBeginEffectiveTime(existing.getBeginEffectiveTime());
		prog.setEndEffectiveTime(existing.getEndEffectiveTime());
		prog.setManualCreatable(existing.isManualCreatable());
		prog.setMaxDurationM(existing.getMaxDurationM());

		prog.setMaxEndTimeH(existing.getMaxEndTimeH());
		prog.setMaxEndTimeM(existing.getMaxEndTimeM());
		prog.setMaxIssueTimeH(existing.getMaxIssueTimeH());
		prog.setMaxIssueTimeM(existing.getMaxIssueTimeM());
		prog.setMaxStartTimeH(existing.getMaxStartTimeH());
		prog.setMaxStartTimeM(existing.getMaxStartTimeM());
		prog.setMinDurationM(existing.getMinDurationM());
		prog.setMinEndTimeH(existing.getMinEndTimeH());
		prog.setMinEndTimeM(existing.getMinEndTimeM());
		prog.setMinStartTimeH(existing.getMinStartTimeH());
		prog.setMinStartTimeM(existing.getMinStartTimeM());
		prog.setMinIssueToStartM(existing.getMinIssueToStartM());
		prog.setMustIssueBDBE(existing.isMustIssueBDBE());
		prog.setNotificationParam1(existing.getNotificationParam1());
		prog.setPendingTimeDBEH(existing.getPendingTimeDBEH());
		prog.setPendingTimeDBEM(existing.getPendingTimeDBEM());
		
        if (prog.getClassName() !=null && (prog.getClassName().equals("com.akuacom.pss2.program.demo.DemoProgramEJB") 
        		|| prog.getClassName().equals("com.akuacom.pss2.program.testProgram.TestProgramEJB"))) {
        	prog.setDefaultDuration(DEFAULT_DURATION);
        }
		
		//also copy long program name and program class
		prog.setLongProgramName(prog.getProgramName());
		prog.setProgramClass(existing.getProgramClass());

		Set<ProgramSignal> signals = new HashSet<ProgramSignal>();

		for (ProgramSignal programSignal : existing.getSignals()) {
			ProgramSignal cloned = programSignal.copy(programSignal, prog);
			signals.add(cloned);
		}
		prog.setSignals(signals);

		final BidConfig config = existing.getBidConfig();
		if (config != null) {
			prog.setBidConfig(config.copy(config, prog));
		}
		
		return prog;
	}

	public Program getNewInstance() {
		return new Program();
	}

	/**
	 * @return the programGroup
	 */
	public String getProgramGroup() {
		return programGroup;
}

	/**
	 * @param programGroup the programGroup to set
	 */
	public void setProgramGroup(String programGroup) {
		this.programGroup = programGroup;
	}
	
	
}
