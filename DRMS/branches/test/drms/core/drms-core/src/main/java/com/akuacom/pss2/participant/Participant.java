/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.core.entities.Participant.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.annotations.DubiousColumnDefinition;
import com.akuacom.pss2.client.ClientManualSignal;
import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.pss2.util.MemorySequence;
import com.akuacom.pss2.util.ModeSlot;
import com.akuacom.pss2.util.OperationModeValue;
import com.akuacom.pss2.util.UserType;
import com.akuacom.utils.lang.DateUtil;

/**
 * The Entity Bean Participant.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "participant", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"participantName", "client" }) })
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
		@NamedQuery(name = "Participant.findAll", query = "select distinct(p) from Participant p left join fetch p.manualSignals left join fetch p.contacts", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findAllBare", query = "select distinct(p) from Participant p", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findByName.Single", query = "select distinct(p) from Participant p left join fetch p.manualSignals left join fetch p.contacts where p.participantName = :name and p.client = :client", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findClientNamesByParticipant", query = "select p.participantName from Participant p where p.parent = :parentName", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findClientsByParticipant", query = "select distinct(p) from Participant p left join fetch p.contacts left join fetch p.manualSignals left join fetch p.contacts where p.parent = :parentName", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.countByNameAndClient.Single", query = "select count(*) from Participant p where p.participantName = :name and p.client = :client", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.countByName", query = "SELECT count(DISTINCT p) FROM Participant p WHERE p.client=0 and p.participantName like :name", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.countByAccountNo", query = "SELECT count(DISTINCT p) FROM Participant p WHERE p.client=0 and p.accountNumber like :name", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.countByProgramName", query = "SELECT count(DISTINCT p) FROM Participant p "
				+ "LEFT JOIN p.programParticipants pp "
				+ "WHERE p.client=0 and pp.programName like :name", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.countByProgramNameAndClient", query = "SELECT count(DISTINCT p) FROM Participant p "
				+ "LEFT JOIN p.programParticipants pp "
				+ "WHERE p.client= :client and pp.programName like :name", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.countByProgramNameAndClientAndState", query = "SELECT count(DISTINCT p) FROM Participant p "
				+ "LEFT JOIN p.programParticipants pp "
				+ "WHERE p.client= :client and pp.programName like :name and pp.state= 1", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findByNameAndClient.Single", query = "select p from Participant p where p.participantName = :name and p.client = :client", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "false") }),
		@NamedQuery(name = "Participant.findByNameAndClientLJF.Single", query = "select distinct(p) from Participant p left join fetch p.manualSignals left join fetch p.contacts "
    			+" left join fetch p.programParticipants pp left join fetch pp.rtpStrateges left join fetch pp.ppRules left join fetch pp.constraint "
    			+" left join fetch pp.program prog left join fetch prog.bidConfig pb left join fetch pb.bidBlocks "
				+ "left join fetch p.eventParticipants ep  where p.participantName = :name and p.client = :client", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findByNameClientAndProgramLJF.Single", query = "select distinct(p) from Participant p "
		    			+" left join fetch p.programParticipants pp  left join fetch pp.program prog left join fetch prog.bidConfig pb left join fetch pb.bidBlocks "
						+ "left join fetch p.eventParticipants ep  where p.participantName = :name and p.client = :client and pp.programName = :programName", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findAllByClient", query = "select distinct(p) from Participant p left join fetch p.manualSignals left join fetch p.contacts where p.client = :client", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findClientsInfoByClient", query = "select distinct(p) from Participant p left join fetch p.contacts left join fetch p.programParticipants where p.client = :client", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findClientBefore", query = "select distinct(p) from Participant p left join fetch p.contacts where p.commTime <= :time and p.client = 1", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "false") }),
		@NamedQuery(name = "Participant.findByAccount", query = "select distinct(p) from Participant p left join fetch p.manualSignals left join fetch p.contacts where p.accountNumber = :account", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findParticipantByPageByFilterNameAndSort", query = "select p from Participant p where p.client = :client AND p.participantName like :filter ORDER BY :sort ASC", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findParticipantByPageByFilterAccountAndSort", query = "select p from Participant p where p.client = :client AND p.accountNumber like :filter ORDER BY :sort ASC", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findCount", query = "select count(p) from Participant p where p.client = :client", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findParticipantByPageByFilterParentAndSort", query = "select p from Participant p where p.client = :client AND p.parent like :filter ORDER BY :sort ASC", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findParticipantByPageByFilterTypeAndSort", query = "select p from Participant p where p.client = :client AND p.type = :filterType ORDER BY :sort ASC", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findInAccounts", 
			query = "select p from Participant p where p.accountNumber IN (:accounts) and p.testParticipant = '0' and p.client = '0' " +
					" and p.aggregator!='1' and (p.startDate =null OR p.startDate <=:time) and (p.endDate =null OR p.endDate >=:time) " +
					" ORDER BY p.accountNumber ASC", 
			hints = { @QueryHint(name = "org.hibernate.cacheable", value = "false") }),
		@NamedQuery(name = "Participant.findNotInAccounts", 
			query = "select p from Participant p where p.accountNumber Not IN (:accounts) and p.testParticipant = '0' and p.client = '0' " +
					" and p.aggregator!='1' and (p.startDate =null OR p.startDate <=:time) and (p.endDate =null OR p.endDate >=:time) " +
					" ORDER BY p.accountNumber ASC", 
			hints = { @QueryHint(name = "org.hibernate.cacheable", value = "false") }),
		@NamedQuery(name = "Participant.getInactiveAccounts", 
			query = "select p.accountNumber from Participant p where p.accountNumber IN (:accounts) and p.client = '0' " +
					" and (p.testParticipant = '1' or p.aggregator = '1' or p.startDate >:time or p.endDate <:time) " +
					" ORDER BY p.accountNumber ASC", 
			hints = { @QueryHint(name = "org.hibernate.cacheable", value = "false") }),
		@NamedQuery(name = "Participant.findByAccounts", query = "select p from Participant p where p.accountNumber IN (:accounts)", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "false") }),
		@NamedQuery(name = "Participant.getParticipantByClient", query = "select p from Participant p where p.participantName IN (:participantName) and p.client = '1'" , hints = { @QueryHint(name = "org.hibernate.cacheable", value = "false") }),
		@NamedQuery(name = "Participant.findBySecondaryAccounts", query = "select p from Participant p where p.secondaryAccountNumber IN (:accounts)", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "false") }),
		@NamedQuery(name = "Participant.findParticipantByPageByFilterProgramAndSort", query = "select p from Participant p left join p.programParticipants pp where pp.program.programName like :programName and p.client = :client ORDER BY :sort ASC", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findClientByPageByFilterProgramAndSort", query = "select p from Participant p left join p.programParticipants pp where pp.program.programName like :programName and p.client = :client and pp.state = 1 ORDER BY :sort ASC", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findParticipantByPageByFilterStatusAndSort", query = "select p from Participant p where p.client = :client AND p.status = :status ORDER BY :sort ASC", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		@NamedQuery(name = "Participant.findOfflineClientsForParticipant", query = "select p FROM Participant p where p.parent = :name and p.client = '1' and p.status = '2' and p.type <> '2' and p.manualControl = '0'", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "false") }),
		@NamedQuery(name = "Participant.getCoords", query = "select new scala.Tuple3(p.UUID, p.latitude,p.longitude) from Participant p", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
		
		//FINE GRAINED QUERY
		@NamedQuery(name = "Participant.findClientAllInfoByParticipant",
				   query = "Select distinct(p) from Participant p left join fetch p.contacts left join fetch p.manualSignals "
				  	     + "left join fetch p.programParticipants pp left join fetch pp.ppRules left join fetch pp.constraint left join fetch pp.bidEntries "
				  	     + "left join fetch pp.program prog left join fetch prog.bidConfig pb left join fetch pb.bidBlocks"
					     +" where p.parent = :parentName and client = true"),
					     
		@NamedQuery(name = "Participant.findClientAllInfoExceptContactsByParticipant",
					query = "Select distinct(p) from Participant p left join fetch p.manualSignals "
					+ "left join fetch p.programParticipants pp left join fetch pp.rtpStrateges left join fetch pp.ppRules left join fetch pp.constraint left join fetch pp.bidEntries "
					+ "left join fetch pp.program prog left join fetch prog.bidConfig pb left join fetch pb.bidBlocks"
					+" where p.parent = :parentName and client = true"),
					
					
	   @NamedQuery( name ="Participant.findParticipantOnlyByName.single", query="select distinct(p) from Participant p  where p.participantName = :name and p.client = :client"),
	   
	   @NamedQuery( name = "Participant.findParticipantWithProgramsParticipants.single",
		       query =" select distinct(p) from Participant p left join fetch p.programParticipants where p.participantName = :name and p.client = :client"),

	   @NamedQuery( name = "Participant.findParticipantWithContacts.single",
			       query =" select distinct(p) from Participant p left join fetch p.contacts where p.participantName = :name and p.client = :client"),
	
	   @NamedQuery( name = "Participant.findParticipantWithEvents.single",
					query =" select distinct(p) from Participant p left join fetch p.eventParticipants ep left join fetch ep.event" +
							"  where p.participantName = :name and p.client = :client"),	
							
		@NamedQuery( name = "Participant.findParticipantsWithEventParticipantsByAccounts",
					query =" select distinct(p) from Participant p left join fetch p.eventParticipants ep " +
												"  where p.accountNumber IN (:accounts)"),										
		@NamedQuery( name = "Participant.findClientsWithEvents",
					query =" select distinct(p) from Participant p left join fetch p.eventParticipants ep left join fetch ep.event" +
					"  where p.parent = :parent and p.client = true"),		
	   @NamedQuery( name ="Participant.findParticipantWithSheds.single",
				   query =" select distinct(p) from Participant p left join fetch p.shedEntries es" +
						  "  where p.participantName = :name and p.client = false"),		   
	   @NamedQuery (name = "Participant.findParticipantWithManualSignals.single",
			   query =" select distinct(p) from Participant p left join fetch p.manualSignals " +
					"  where p.participantName = :name and p.client = :client"),	
					
	   @NamedQuery(name = "Participant.findBySecondaryAccount", query = "select distinct(p) from Participant p where p.secondaryAccountNumber = :account", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
	   
	   @NamedQuery( name = "Participant.findClientsWithContacts",
		       query =" select distinct(p) from Participant p left join fetch p.contacts where p.participantName IN (:clientName) and p.client = 1"),

	   @NamedQuery( name = "Participant.findClientsWithContactsByParticipants",
				       query =" select distinct(p) from Participant p left join fetch p.contacts where p.parent IN (:parentName) and p.client = 1"),
	   @NamedQuery(name = "Participant.findByApplicationId", query = "select distinct(p) from Participant p left join fetch p.manualSignals left join fetch p.contacts where p.applicationId = :applicationId", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),

	   
	   @NamedQuery(name = "Participant.findEventParticipantWithSignalsByClientIds",
			   query = "select distinct(p) from Participant p left join fetch p.eventParticipants ep  left join fetch ep.event" +
			   			" left join fetch ep.signals eps left join fetch eps.eventParticipantSignalEntries " +
			   			" where p.UUID in(:pids)"),
	@NamedQuery(name  = "Participant.findClientsforEventParticipant",
		    	query = "select distinct(p) from Participant p left join fetch p.programParticipants pp " +
			   			    	    			" where p.client = 1 and pp.programName = :programName and pp.optStatus = 0 and p.parent IN (:participantNames)" ),
	
	@NamedQuery(name = "Participant.findByClientName.Single", query = "select distinct(p) from Participant p left join fetch p.manualSignals left join fetch p.contacts " 
				+ "left join fetch p.eventParticipants ep  where p.participantName = :name and p.client = :client", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") })
})
		
		
public class Participant extends VersionedEntity {

	private static final long serialVersionUID = 3745732890651815311L;

	/** The Constant TYPE_SOFTWARE. */
	public static final byte TYPE_AUTO = 0;

	/** The Constant TYPE_RELAY. */
	public static final byte TYPE_RELAY = 1;

	/** The Constant TYPE_CLIR. */
	public static final byte TYPE_CLIR = 1;

	/** The Constant TYPE_MANUAL. */
	public static final byte TYPE_MANUAL = 2;

	/** The Constant SORT_NAME. */
	public static final int SORT_NAME = 0;

	/** The Constant SORT_TYPE. */
	public static final int SORT_TYPE = 1;

	/** The Constant SORT_LAST_CONTACT. */
	public static final int SORT_LAST_CONTACT = 4;

	/** The Constant SORT_STATUS. */
	public static final int SORT_STATUS = 5;

	/** The Constant CONTROL_AUTPCPP. */
	public static final byte CONTROL_AUTPCPP = 0;

	/** The Constant CONTROL_NORMAL. */
	public static final byte CONTROL_NORMAL = 1;

	/** The Constant CONTROL_MODERATE. */
	public static final byte CONTROL_MODERATE = 2;

	/** The Constant CONTROL_HIGH. */
	public static final byte CONTROL_HIGH = 3;

	/** The participant name. */
	private String participantName;

	/** The type. */
	@Column(columnDefinition = "varchar(12)")
	@DubiousColumnDefinition
	private byte type;

	/** The host address. */
	private String hostAddress;

	/** The manual control. */
	private boolean manualControl;

	private Date manualControlExpires;

	private Integer push = 0;

	/** The manual signals. */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "participant")
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.REMOVE })
	@OrderBy("name, value")
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ClientManualSignal> manualSignals = new HashSet<ClientManualSignal>();

	/** The account number. */
	@Column(name = "account")
	private String accountNumber;
	
	/** The applicationId for DBP. */
	@Column(name = "applicationId")
	private String applicationId;
	
	/** The account number. */
	@Column(name = "secondary_account")
	private String secondaryAccountNumber;

	/** The first name. */
	private String firstName;

	/** The last name. */
	private String lastName;

	/** The meter name. */
	private String meterName;

	/** The contacts. */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "participant")
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.REMOVE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@OrderBy("UUID")
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ParticipantContact> contacts = new TreeSet<ParticipantContact>();
	
	

	/** The feedback. */
	private boolean feedback;

	/** The meter id. */
	private String meterId;

	/** The notification level. */
	private int notificationLevel;

	/** The address. */
	private String address = "";

	/** The grid location. */
	private String gridLocation;

	/** The latitude. */
	private double latitude;

	/** The longitude. */
	private double longitude;

	/** The shed per hour kw. */
	private double shedPerHourKW;
	
	/** The shed type : SIMPLE or ADVANCED */
	@Column(columnDefinition = "varchar(20)")
	private String shedType;
	
	/** The last price. */
	private double lastPrice = -1.0;

	/** The client. */
	private boolean client;

	/** The parent. */
	private String parent;

	/** The program participants. */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "participant")
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.REMOVE,
			org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ProgramParticipant> programParticipants = new HashSet<ProgramParticipant>();

	// @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy
	// = "participant")
	// @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
	// org.hibernate.annotations.CascadeType.REMOVE,
	// org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	// Remove cache, because it will cause a bug - the newly created event
	// is invisible in events tab in facility dash board
	// @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "participant")
	private Set<EventParticipant> eventParticipants = new HashSet<EventParticipant>();

	/** The shed 24 hours values. */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "participant")
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE,
			org.hibernate.annotations.CascadeType.REMOVE })
	@OrderBy("hourIndex, value")
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ParticipantShedEntry> shedEntries = new HashSet<ParticipantShedEntry>();
	
	
	
	/** The comm time. */
	private Date commTime;

	/** The status. */
	@Column(columnDefinition = "tinyint(3) unsigned DEFAULT '2'")
	@DubiousColumnDefinition
	private Integer status;

	/** The offline warning. */
	private Boolean offlineWarning;

	private Boolean testAccount = new Boolean(false);

	private Boolean activated = new Boolean(false);

	private Boolean dataEnabler = new Boolean(false);

	private String ip;

	private Boolean optOut = new Boolean(false);

	private Boolean installer = new Boolean(false);

	private Boolean aggregator = new Boolean(false);

	private Boolean clientAllowedToOptOut = new Boolean(false);

	private Boolean clientCanFailConfirmation = new Boolean(false);

	@Enumerated(value = EnumType.STRING)
	private EventStatus eventStatus = EventStatus.NONE;

	@Enumerated(value = EnumType.STRING)
	private OperationModeValue operationMode = OperationModeValue.UNKNOWN;

	@Enumerated(value = EnumType.STRING)
	private UserType userType = UserType.ADVANCED;

	@Column(name = "customerName")
	private String customerName;

	@Column(name = "serviceStreetAddress")
	private String serviceStreetAddress;

	@Column(name = "serviceCityName")
	private String serviceCityName;

	@Column(name = "zip")
	private String zip;

	@Column(name = "ABank")
	private String aBank;

	@Column(name = "SLAP")
	private String slap;

	@Column(name = "PNode")
	private String pNode;

	@Column(name = "servicePlan")
	private String servicePlan;
	@Column(name = "rateEffectiveDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date rateEffectiveDate;
	@Column(name = "directAccessParticipant")
	private Boolean directAccessParticipant = new Boolean(false);
	@Column(name = "uisActive")
	private Boolean uisActive = new Boolean(false);
	@Column(name = "testParticipant")
	private Boolean testParticipant = new Boolean(false);

	@Lob
	@Column(name = "notes")
	private String notes;
	@Column(name = "notesLastUpdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date notesLastUpdate;
	@Column(name = "notesAuthor")
	private String notesAuthor;

	// Demand Limiting
	@Column(name = "demandLimiting")
	private Boolean demandLimiting = new Boolean(false);

	/** the subaccount premiseNumber */
	private String premiseNumber;

	/** The "subaccount" start date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	/** The "subaccount" end date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date enrollmentDate;

	/** The comment. */
	@Column(columnDefinition = "text")
	private String comment;
	
	private Boolean defaultEventOptoutEnabled=new Boolean(true);
	private Boolean partEventOptoutEnabled=new Boolean(true);
	
	@Column(name = "customer")
	private Boolean customer = new Boolean(false);
	
	@Column(name = "non_auto_dr")
	private Boolean nonAutoDR = new Boolean(false);
	
	/** The device type. */
	@Column
	private String deviceType;

	@Column
	private Boolean leadAccount = new Boolean(false);
	
	@Column
	private Boolean retained = new Boolean(false);

	private String bcdRepName;
	private Date autoDrProfileStartDate;
	
	@Column
	private Boolean optOutClientOfflineNotification;
	@Column
	private Boolean clientOfflineNotificationEnable;
	@Column
	private Boolean clientOfflineNotificationAggEnable;
	@Column
	private int thresholdsSummer;
	@Column
	private int thresholdsUnSummer;
	@Column
	private int programOption;
	@Column
	private String substation;
	@Column
	private String blockNumber;
	@Column
	private String requestId;

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public OperationModeValue getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(OperationModeValue operationMode) {
		this.operationMode = operationMode;
	}

	/** The external contacts. */
	@Transient
	private List<ParticipantContact> externalContacts = new ArrayList<ParticipantContact>();

	/**
	 * Instantiates a new participant.
	 */
	public Participant() {
		// legacy initializations.
		setLastPrice(-1.0);
		setStatus(ClientStatus.OFFLINE.ordinal());
		setOfflineWarning(true);
	}

	public Boolean getInstaller() {
		return installer;
	}

	public void setInstaller(Boolean installer) {
		this.installer = installer;
	}

	public Boolean getAggregator() {
		if (aggregator == null) {
			return false;
		}
		return aggregator;
	}

	public void setAggregator(Boolean aggregator) {
		this.aggregator = aggregator;
	}

	public Boolean getClientAllowedToOptOut() {
		if (clientAllowedToOptOut == null) {
			return false;
		}
		return clientAllowedToOptOut;
	}

	public void setClientAllowedToOptOut(Boolean clientAllowedToOptOut) {
		this.clientAllowedToOptOut = clientAllowedToOptOut;
	}

	public Boolean getOptOut() {
		if (optOut == null) {
			return false;
		}
		return optOut;
	}

	public void setOptOut(Boolean optOut) {
		this.optOut = optOut;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPush() {
		return push;
	}

	public void setPush(Integer push) {
		this.push = push;
	}

	/**
	 * Gets the participant name.
	 * 
	 * @return the participant name
	 */
	public String getParticipantName() {
		return participantName;
	}

	/**
	 * Sets the participant name.
	 * 
	 * @param participantName
	 *            the new participant name
	 */
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public byte getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * Gets the host address.
	 * 
	 * @return the host address
	 */
	public String getHostAddress() {
		return hostAddress;
	}

	/**
	 * Sets the host address.
	 * 
	 * @param hostAddress
	 *            the new host address
	 */
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	/**
	 * Gets the account number.
	 * 
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Sets the account number.
	 * 
	 * @param accountNumber
	 *            the new account number
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Gets the first name.
	 * 
	 * @return the firstName
	 */
	public String getFirstName() {
		if (firstName == null)
			return "";
		else
			return firstName;
	}

	/**
	 * Sets the first name.
	 * 
	 * @param firstName
	 *            the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 * 
	 * @return the lastName
	 */
	public String getLastName() {
		if (lastName == null)
			return "";
		else
			return lastName;
	}

	/**
	 * Sets the last name.
	 * 
	 * @param lastName
	 *            the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the meter name.
	 * 
	 * @return the meter name
	 */
	public String getMeterName() {
		return meterName;
	}

	/**
	 * Sets the meter name.
	 * 
	 * @param meterName
	 *            the new meter name
	 */
	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	/**
	 * Checks if is manual control.
	 * 
	 * @return true, if is manual control
	 */
	public boolean isManualControl() {
		return manualControl;
	}

	/**
	 * Sets the manual control.
	 * 
	 * @param manualControl
	 *            the new manual control
	 */
	public void setManualControl(boolean manualControl) {
		this.manualControl = manualControl;
	}

	public Date getManualControlExpires() {
		return manualControlExpires;
	}

	public void setManualControlExpires(Date manualControlExpires) {
		this.manualControlExpires = manualControlExpires;
	}

	/**
	 * Checks if is feedback.
	 * 
	 * @return true, if is feedback
	 */
	public boolean isFeedback() {
		return feedback;
	}

	/**
	 * Sets the feedback.
	 * 
	 * @param feedback
	 *            the new feedback
	 */
	public void setFeedback(boolean feedback) {
		this.feedback = feedback;
	}

	/**
	 * Gets the meter id.
	 * 
	 * @return the meter id
	 */
	public String getMeterId() {
		return meterId;
	}

	/**
	 * Sets the meter id.
	 * 
	 * @param meterId
	 *            the new meter id
	 */
	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	/**
	 * @return the contacts
	 */
	public Set<ParticipantContact> getContacts() {
		return contacts;
	}

	/**
	 * Sets the contacts.
	 * 
	 * @param contacts
	 *            the new contacts
	 */
	public void setContacts(Set<ParticipantContact> contacts) {
		this.contacts = contacts;
	}

	/**
	 * Gets the notification level.
	 * 
	 * @return the notification level
	 */
	public int getNotificationLevel() {
		return notificationLevel;
	}

	/**
	 * Sets the notification level.
	 * 
	 * @param notificationLevel
	 *            the new notification level
	 */
	public void setNotificationLevel(int notificationLevel) {
		this.notificationLevel = notificationLevel;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress() {
		if (address == null) {
			return "";
		}
		return address;
	}

	/**
	 * Sets the address.
	 * 
	 * @param address
	 *            the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the grid location.
	 * 
	 * @return the grid location
	 */
	public String getGridLocation() {
		return gridLocation;
	}

	/**
	 * Sets the grid location.
	 * 
	 * @param gridLocation
	 *            the new grid location
	 */
	public void setGridLocation(String gridLocation) {
		this.gridLocation = gridLocation;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 * 
	 * @param latitude
	 *            the new latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 * 
	 * @param longitude
	 *            the new longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the shed per hour kw.
	 * 
	 * @return the shed per hour kw
	 */
	public double getShedPerHourKW() {
		return shedPerHourKW;
	}

	/**
	 * Sets the shed per hour kw.
	 * 
	 * @param shedPerHourKW
	 *            the new shed per hour kw
	 */
	public void setShedPerHourKW(double shedPerHourKW) {
		this.shedPerHourKW = shedPerHourKW;
	}

	/**
	 * Gets the last price.
	 * 
	 * @return the last price
	 */
	public double getLastPrice() {
		return lastPrice;
	}

	/**
	 * Sets the last price.
	 * 
	 * @param lastPrice
	 *            the new last price
	 */
	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}

	/**
	 * Checks if is offline warning.
	 * 
	 * @return true, if is offline warning
	 */
	public boolean isOfflineWarning() {
		return offlineWarning;
	}

	/**
	 * Sets the offline warning.
	 * 
	 * @param offlineWarning
	 *            the new offline warning
	 */
	public void setOfflineWarning(boolean offlineWarning) {
		this.offlineWarning = offlineWarning;
	}

	/**
	 * Gets the manual signals - WARNING: may have null entries in the list.
	 * 
	 * @return the manual signals
	 */
	public Set<ClientManualSignal> getManualSignals() {
		return manualSignals;
	}

	/**
	 * Sets the manual signals.
	 * 
	 * @param manualSignals
	 *            the new manual signals
	 */
	public void setManualSignals(Set<ClientManualSignal> manualSignals) {
		this.manualSignals = manualSignals;
	}

	/**
	 * Checks if is client.
	 * 
	 * @return true, if is client
	 */
	public boolean isClient() {
		return client;
	}

	/**
	 * Sets the client.
	 * 
	 * @param client
	 *            the new client
	 */
	public void setClient(boolean client) {
		this.client = client;
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param parent
	 *            the new parent
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	public Date getCommTime() {
		return commTime;
	}

	public void setCommTime(Date commTime) {
		this.commTime = commTime;
	}

	/**
	 * Gets the client status.
	 * 
	 * @return the client status
	 */
	public ClientStatus getClientStatus() {
		return ClientStatus.values()[status];
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getTestAccount() {
		return testAccount;
	}

	public void setTestAccount(Boolean testAccount) {
		this.testAccount = testAccount;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public Boolean getDataEnabler() {
		if (dataEnabler == null) {
			return false;
		}
		return dataEnabler;
	}

	public void setDataEnabler(Boolean activated) {
		this.dataEnabler = activated;
	}

	/**
	 * Gets the type string.
	 * 
	 * @return the type string
	 */
	public String getTypeString() {
		if (type == TYPE_AUTO) {
			return "AUTO";
		} else if (type == TYPE_MANUAL) {
			return "MANUAL";
		} else {
			return "unknown";
		}
	}

	/**
	 * @return the programParticipants
	 */
	public Set<ProgramParticipant> getProgramParticipants() {
		return programParticipants;
	}

	/**
	 * @param programParticipants
	 *            the programParticipants to set
	 */
	public void setProgramParticipants(
			Set<ProgramParticipant> programParticipants) {
		this.programParticipants = programParticipants;
	}

	public Set<EventParticipant> getEventParticipants() {
		return eventParticipants;
	}

	public void setEventParticipants(Set<EventParticipant> eventParticipants) {
		this.eventParticipants = eventParticipants;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	/**
	 * Gets the participantName.
	 * 
	 * @see #getParticipantName()
	 * @return the user
	 * @deprecated use {@link #getParticipantName()}
	 */
	public String getUser() {
		return getParticipantName();
	}

	/**
	 * Sets the user.
	 * 
	 * @deprecated use {@link #setParticipantName(String)}
	 * 
	 * @param user
	 *            the new user
	 */
	public void setUser(String user) {
		setParticipantName(user);
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getServiceStreetAddress() {
		return serviceStreetAddress;
	}

	public void setServiceStreetAddress(String serviceStreetAddress) {
		this.serviceStreetAddress = serviceStreetAddress;
	}

	public String getServiceCityName() {
		return serviceCityName;
	}

	public void setServiceCityName(String serviceCityName) {
		this.serviceCityName = serviceCityName;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getABank() {
		return aBank;
	}

	public void setABank(String aBank) {
		this.aBank = aBank;
	}

	public String getSlap() {
		return slap;
	}

	public void setSlap(String slap) {
		this.slap = slap;
	}

	public String getPNode() {
		return pNode;
	}

	public void setPNode(String pNode) {
		this.pNode = pNode;
	}

	public String getServicePlan() {
		return servicePlan;
	}

	public void setServicePlan(String servicePlan) {
		this.servicePlan = servicePlan;
	}

	public Date getRateEffectiveDate() {
		return rateEffectiveDate;
	}

	public void setRateEffectiveDate(Date rateEffectiveDate) {
		this.rateEffectiveDate = rateEffectiveDate;
	}

	public Boolean getDirectAccessParticipant() {
		return directAccessParticipant;
	}

	public void setDirectAccessParticipant(Boolean directAccessParticipant) {
		this.directAccessParticipant = directAccessParticipant;
	}

	/**
	 * Gets the external contacts.
	 * 
	 * @return the external contacts
	 */
	public List<ParticipantContact> getExternalContacts() {
		return externalContacts;
	}

	/**
	 * Sets the external contacts.
	 * 
	 * @param externalContacts
	 *            the new external contacts
	 */
	public void setExternalContacts(List<ParticipantContact> externalContacts) {
		this.externalContacts = externalContacts;
	}

	public List<Contact> getExternalContactsAsContactModels() {
		ArrayList<Contact> res = new ArrayList<Contact>();
		for (ParticipantContact pc : this.externalContacts) {
			if (pc != null)
				res.add(pc.getParticipantContactAsContact());
		}

		return res;
	}

	public List<Contact> getContactsAsContactModels1() {
		ArrayList<Contact> res = new ArrayList<Contact>();
		for (ParticipantContact pc : getContacts()) {
			if (pc != null)
				res.add(pc.getParticipantContactAsContact());
		}

		return res;
	}

	public List<EventState> getQuickEventStates(
			HashMap<String, Integer> programPriorities) {
		long nowMS = System.currentTimeMillis();
		List<EventState> eventStates = new ArrayList<EventState>();

		if (this.getEventParticipants() != null
				&& getEventParticipants().size() > 0) {
			for (EventParticipant eventPart : getEventParticipants()) {
				Event event = eventPart.getEvent();
				// only add to list of the event is issued
				if (event != null) {
					if (event.isIssued()) {
						EventState eventState = new EventState(); // programEJB.newEventState();
						eventState.setDrasClientID(getParticipantName());
						eventState.setEventIdentifier(event.getEventName());
						eventState.setEventModNumber(eventPart
								.getEventModNumber());
						eventState.setEventStateID(MemorySequence
								.getNextSequenceId());
						eventState.setProgramName(event.getProgramName());

						long startTimeMS = event.getStartTime().getTime();

						if (isManualControl()) {
							String pendingValue = getClientManualSignalValueAsString("pending");
							if (pendingValue == null) {
								// hardcoded default
								pendingValue = "off";
							}

							if (pendingValue.equals("on")) {
								eventState.setEventStatus(EventStatus.ACTIVE);
							} else {
								eventState.setEventStatus(EventStatus.NONE);
							}
						} else {
							String pendingValue = eventPart
									.getSignalValueForEventParticipantAsString("pending");
							if (pendingValue == null) {
								pendingValue = "off";
							}
							if (startTimeMS < nowMS) {
								eventState.setEventStatus(EventStatus.ACTIVE);
							} else if (pendingValue.equals("on")) {
								eventState.setEventStatus(EventStatus.NEAR);
							} else {
								eventState.setEventStatus(EventStatus.FAR);
							}
						}

						String modeSignalValue;
						if (isManualControl()) {
							modeSignalValue = getClientManualSignalValueAsString("mode");
							if (modeSignalValue == null) {
								// hardcoded default.
								modeSignalValue = "normal";
							}
						} else {
							modeSignalValue = eventPart
									.getSignalValueForEventParticipantAsString("mode");
							if (modeSignalValue == null) {
								modeSignalValue = "normal";
							}
						}

						eventState.setOperationModeValue(modeSignalValue);
						eventState.setManualControl(isManualControl());
						eventState
								.setCurrentTimeS((nowMS - startTimeMS) / 1000.0);

						List<ModeSlot> operationModeSchedule = new ArrayList<ModeSlot>();

						List<EventParticipantSignalEntry> signalEntries = eventPart
								.getSignalEntries();
						for (EventParticipantSignalEntry entry : signalEntries) {
							if (!entry.getParentSignal().getSignalDef()
									.getSignalName().equals("mode")) {
								continue;
							}

							ModeSlot modeSlot = new ModeSlot();
							modeSlot.setOperationModeValue(((EventParticipantSignalEntry) entry)
									.getLevelValue());
							modeSlot.setTimeSlotS((entry.getTime().getTime() - startTimeMS) / 1000.0);
							operationModeSchedule.add(modeSlot);
						}
						eventState
								.setOperationModeSchedule(operationModeSchedule);
						eventState.setNotificationTime(event.getIssuedTime());
						eventState.setStartTime(event.getStartTime());
						eventState.setEndTime(event.getEndTime());

						eventState.setEventInfoInstances(eventPart
								.getEventInfoInstance(eventState.getStartTime()
										.getTime()));

						eventStates.add(eventState);
					}
				}
			}
		}

		if (eventStates.size() == 0) {
			// create event state based on manual control or a empty/no event
			// NORMAL one
			EventState eventState = new EventState();
			eventState.setDrasClientID(getParticipantName());
			eventState.setEventIdentifier("");
			eventState.setEventModNumber(0);
			eventState.setEventStateID(MemorySequence.getNextSequenceId());
			eventState.setProgramName("");

			String pendingSignalValue = "";
			String modeSignalValue = "";
			if (isManualControl()) {
				pendingSignalValue = getClientManualSignalValueAsString("pending");
				modeSignalValue = getClientManualSignalValueAsString("mode");
			} else {
				pendingSignalValue = "off";
				modeSignalValue = "unknown";
			}

			if (pendingSignalValue.equals("on")) {
				eventState.setEventStatus(EventStatus.ACTIVE);
			} else {
				eventState.setEventStatus(EventStatus.NONE);
			}
			eventState.setOperationModeValue(modeSignalValue);
			eventState.setManualControl(isManualControl());
			eventStates.add(eventState);
		}

		if (isClient()) {
			Collections.sort(eventStates, new EventState.PriorityComparator(
					programPriorities));
		}

		return eventStates;
	}

	/**
	 * Utility method returns the string value for the signal or null if signal
	 * is not found
	 * 
	 * @param signalName
	 * @return
	 */
	public String getClientManualSignalValueAsString(String signalName) {
		// mode and pending would always be available in client manual signals
		// If not, manual control will not work.

		String res = null;
		if (getManualSignals() != null) {
			for (ClientManualSignal signalState : getManualSignals()) {
				if (signalState != null) {
					if (signalState.getName().equals(signalName)) {
						res = signalState.getValue();
						break;
					}
				}
			}
		}

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String user = this.getParticipantName();
		byte type = getType();
		String hostAddress = getHostAddress();
		String accountNumber = getAccountNumber();
		Boolean manualControl = isManualControl();
		String firstName = getFirstName();
		String lastName = getLastName();
		String meterName = getMeterName();
//		Set<ParticipantContact> contacts = getContacts();
		Boolean feedback = isFeedback();
		String meterId = getMeterId();
		String address = getAddress();
		String gridLocation = getGridLocation();
		Double latitude = getLatitude();
		Double longitude = getLongitude();
		double shedPerHourKW = getShedPerHourKW();
		String shedType = getShedType();
		String rv = "Participant: user = " + user;
		if (type == TYPE_AUTO) {
			rv += ", type = SOFTWARE";
		} else if (type == TYPE_CLIR) {
			rv += ", type = CLIR";
		} else if (type == TYPE_MANUAL) {
			rv += ", type = MANUAL";
		} else {
			rv += ", type unknown";
		}
		rv += ", hostAddress = " + hostAddress;
		rv += ", manualControl = " + manualControl;
		rv += ", AccountNum = " + accountNumber;
		rv += ", FirstName = " + firstName;
		rv += ", LastName = " + lastName;
		rv += ", meterName = " + meterName;
        // disabled as contacts are not auto loaded anymore.
        // and may throw hibernate exception when debugged.
/*
		if (contacts != null) {
			final Iterator<ParticipantContact> it = contacts.iterator();
			for (int i = 0; it.hasNext(); i++) {
				ParticipantContact c = it.next();
				rv += ", contact" + i + " = " + c.toString();
			}
		}
*/
		rv += ", feedback = " + feedback;
		rv += ", meterId = " + meterId;
		rv += ", address = " + address;
		rv += ", gridLocation = " + gridLocation;
		rv += ", latitude = " + latitude;
		rv += ", longitude = " + longitude;
		rv += ", shedPerHourKW = " + shedPerHourKW;
		rv += ", shedType = " + shedType;
		return rv;
	}

	public void setUisActive(Boolean uisActive) {
		this.uisActive = uisActive;
	}

	public Boolean getUisActive() {
		return uisActive;
	}

	public void setTestParticipant(Boolean testParticipant) {
		this.testParticipant = testParticipant;
	}

	public Boolean getTestParticipant() {
		return testParticipant;
	}

	public Boolean getDemandLimiting() {
		return demandLimiting;
	}

	public void setDemandLimiting(Boolean demandLimiting) {
		this.demandLimiting = demandLimiting;
	}

	public void setNotesLastUpdate(Date notesLastUpdate) {
		this.notesLastUpdate = notesLastUpdate;
	}

	public Date getNotesLastUpdate() {
		return notesLastUpdate;
	}

	public Boolean getClientCanFailConfirmation() {

		if (clientCanFailConfirmation == null) {
			return false;
		}
		return clientCanFailConfirmation;
	}

	public void setClientCanFailConfirmation(Boolean clientCanFailConfirmation) {
		this.clientCanFailConfirmation = clientCanFailConfirmation;
	}

	public void setNotesAuthor(String notesAuthor) {
		this.notesAuthor = notesAuthor;
	}

	public String getNotesAuthor() {
		return notesAuthor;
	}

	/*************** sub account fields ***************************************/
	public String getPremiseNumber() {
		return premiseNumber;
	}

	public void setPremiseNumber(String premiseNumber) {
		this.premiseNumber = premiseNumber;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getAccountActiveStatus() {
		String active;
		if (getStartDate() == null) {
			active = "";
		} else if (isActive(getStartDate(), getEndDate())) {
			active = "Active";
		} else {
			active = "Inactive";
		}

		return active;
	}

	private boolean isActive(Date startDate, Date endDate) {
		Date today = DateUtil.stripTime(new Date());
		if (endDate != null && today.after(DateUtil.stripTime(endDate))) {
			return false;
		} else if (today.before(DateUtil.stripTime(startDate))) {
			return false;
		} else {
			return true;
		}
	}
	/********** end subaccount ************************************************/

	/**
	 * @return the shedType
	 */
	public String getShedType() {
		return shedType;
	}

	/**
	 * @param shedType the shedType to set
	 */
	public void setShedType(String shedType) {
		this.shedType = shedType;
	}

	/**
	 * @return the shedEntries
	 */
	public Set<ParticipantShedEntry> getShedEntries() {
		return shedEntries;
	}

	/**
	 * @param shedEntries the shedEntries to set
	 */
	public void setShedEntries(Set<ParticipantShedEntry> shedEntries) {
		this.shedEntries = shedEntries;
	}

	public Boolean getDefaultEventOptoutEnabled() {
		return defaultEventOptoutEnabled;
	}

	public void setDefaultEventOptoutEnabled(Boolean defaultEventOptoutEnabled) {
		this.defaultEventOptoutEnabled = defaultEventOptoutEnabled;
	}

	public Boolean getPartEventOptoutEnabled() {
		return partEventOptoutEnabled;
	}

	public void setPartEventOptoutEnabled(Boolean partEventOptoutEnabled) {
		this.partEventOptoutEnabled = partEventOptoutEnabled;
	}

	/**
	 * @return the leadAccount
	 */
	public Boolean getLeadAccount() {
		return leadAccount;
	}

	/**
	 * @param leadAccount the leadAccount to set
	 */
	public void setLeadAccount(Boolean leadAccount) {
		this.leadAccount = leadAccount;
	}

	public Boolean getCustomer() {
		return customer;
	}

	public void setCustomer(Boolean customer) {
		this.customer = customer;
	}

	public Boolean getNonAutoDR() {
		return nonAutoDR;
	}

	public void setNonAutoDR(Boolean nonAutoDR) {
		this.nonAutoDR = nonAutoDR;
	}

	public String getBcdRepName() {
		return bcdRepName;
	}

	public void setBcdRepName(String bcdRepName) {
		this.bcdRepName = bcdRepName;
	}

	public Date getAutoDrProfileStartDate() {
		return autoDrProfileStartDate;
	}

	public void setAutoDrProfileStartDate(Date autoDrProfileStartDate) {
		this.autoDrProfileStartDate = autoDrProfileStartDate;
	}
	
	public Boolean getRetained() {
		return retained;
	}

	public void setRetained(Boolean retained) {
		this.retained = retained;
	}

	public String getSecondaryAccountNumber() {
		return secondaryAccountNumber;
	}

	public void setSecondaryAccountNumber(String secondaryAccountNumber) {
		this.secondaryAccountNumber = secondaryAccountNumber;
	}

	/**
	 * @return the optOutClientOfflineNotification
	 */
	public Boolean getOptOutClientOfflineNotification() {
		return optOutClientOfflineNotification;
	}

	/**
	 * @param optOutClientOfflineNotification the optOutClientOfflineNotification to set
	 */
	public void setOptOutClientOfflineNotification(
			Boolean optOutClientOfflineNotification) {
		this.optOutClientOfflineNotification = optOutClientOfflineNotification;
	}

	/**
	 * @return the clientOfflineNotificationEnable
	 */
	public Boolean getClientOfflineNotificationEnable() {
		return clientOfflineNotificationEnable;
	}

	/**
	 * @param clientOfflineNotificationEnable the clientOfflineNotificationEnable to set
	 */
	public void setClientOfflineNotificationEnable(
			Boolean clientOfflineNotificationEnable) {
		this.clientOfflineNotificationEnable = clientOfflineNotificationEnable;
	}

	/**
	 * @return the thresholdsSummer
	 */
	public int getThresholdsSummer() {
		return thresholdsSummer;
	}

	/**
	 * @param thresholdsSummer the thresholdsSummer to set
	 */
	public void setThresholdsSummer(int thresholdsSummer) {
		this.thresholdsSummer = thresholdsSummer;
	}

	/**
	 * @return the thresholdsUnSummer
	 */
	public int getThresholdsUnSummer() {
		return thresholdsUnSummer;
	}

	/**
	 * @param thresholdsUnSummer the thresholdsUnSummer to set
	 */
	public void setThresholdsUnSummer(int thresholdsUnSummer) {
		this.thresholdsUnSummer = thresholdsUnSummer;
	}

	/**
	 * @return the clientOfflineNotificationAggEnable
	 */
	public Boolean getClientOfflineNotificationAggEnable() {
		return clientOfflineNotificationAggEnable;
	}

	/**
	 * @param clientOfflineNotificationAggEnable the clientOfflineNotificationAggEnable to set
	 */
	public void setClientOfflineNotificationAggEnable(
			Boolean clientOfflineNotificationAggEnable) {
		this.clientOfflineNotificationAggEnable = clientOfflineNotificationAggEnable;
	}

	/**
	 * @return the programOption
	 */
	public int getProgramOption() {
		return programOption;
	}

	/**
	 * @param programOption the programOption to set
	 */
	public void setProgramOption(int programOption) {
		this.programOption = programOption;
	}

	/**
	 * @return the substation
	 */
	public String getSubstation() {
		return substation;
	}

	/**
	 * @param substation the substation to set
	 */
	public void setSubstation(String substation) {
		this.substation = substation;
	}

	/**
	 * @return the blockNumber
	 */
	public String getBlockNumber() {
		return blockNumber;
	}

	/**
	 * @param blockNumber the blockNumber to set
	 */
	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String toOperatorString()
		{
	        StringBuilder rv = new StringBuilder();
	        rv.append("Name: ").append(this.getParticipantName());
	        if(! this.isClient())
	        {
	        rv.append(", Account Number: ").append(this.getAccountNumber());
	        }
	        return rv.toString();
		}
}