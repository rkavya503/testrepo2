/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.utilopws.BeanMappingUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.xml.datatype.XMLGregorianCalendar;

import com.kanaeki.firelog.util.FireLogEntry;
import org.openadr.dras.akuacontact.AkuaContact;
import org.openadr.dras.akuacontact.ListOfContacts;
import org.openadr.dras.akuadrasclientdata.AkuaDRASCursor;
import org.openadr.dras.akuadrasclientdata.AkuaDRASSearchCriteria;
import org.openadr.dras.akuadrasclientdata.AkuaDRASSearchHandler;
import org.openadr.dras.akuadrasclientdata.ClientData;
import org.openadr.dras.akuadrasclientdata.ListOfIDs;
import org.openadr.dras.akuadrasclientdata.ListofClientData;
import org.openadr.dras.akuadrasclientdata.ListofSearchCriteria;
import org.openadr.dras.akuaproperty.AkuaProperty;
import org.openadr.dras.akuaproperty.ListOfProperties;
import org.openadr.dras.akuartpconfig.AkuaRTPConfig;
import org.openadr.dras.akuartpconfig.ListOfRTPConfigs;
import org.openadr.dras.akuaseasonconfig.AkuaSeasonConfig;
import org.openadr.dras.akuaseasonconfig.ListOfSeasonConfigs;
import org.openadr.dras.akuasignal.AkuaSignal;
import org.openadr.dras.akuasignal.ListOfSignals;
import org.openadr.dras.akuasignal.ListOfValues;
import org.openadr.dras.akuautilityprogram.AkuaBidBlock;
import org.openadr.dras.akuautilityprogram.AkuaBidConfig;
import org.openadr.dras.akuautilityprogram.AkuaContactEmailList;
import org.openadr.dras.akuautilityprogram.AkuaSignalList;
import org.openadr.dras.akuautilityprogram.AkuaUtilityProgram;
import org.openadr.dras.bid.BidBlock;
import org.openadr.dras.drasclient.DRASClient;
import org.openadr.dras.drasclient.Location;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventstate.ListOfEventStates;
import org.openadr.dras.eventstate.OperationState;
import org.openadr.dras.eventstate.SimpleClientEventData;
import org.openadr.dras.logs.ListOfTransactionLogs;
import org.openadr.dras.logs.TransactionLog;
import org.openadr.dras.participantaccount.ContactInformation;
import org.openadr.dras.participantaccount.ListOfParticipantAccountIDs;
import org.openadr.dras.participantaccount.ListOfParticipantAccounts;
import org.openadr.dras.participantaccount.ParticipantAccount;
import org.openadr.dras.programconstraint.EventWindow;
import org.openadr.dras.programconstraint.ProgramConstraint;
import org.openadr.dras.utilitydrevent.ListOfUtilityDREvents;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilityprogram.ParticipantList;
import org.openadr.dras.utilityprogram.UtilityProgram;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.ejb.search.AkuaCursor;
import com.akuacom.ejb.search.SearchCriterion;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignal;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantExtInfo;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.bidding.BiddingProgramManager;
import com.akuacom.pss2.program.dbp.Bid;
import com.akuacom.pss2.program.dbp.BidEntry;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.dbp.DBPProgram;
import com.akuacom.pss2.program.dbp.DBPUtils;
import com.akuacom.pss2.program.eventtemplate.EventTemplate;
import com.akuacom.pss2.program.eventtemplate.EventTemplateSignalEntry;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.scertp.RTPConfig;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.pss2.program.signal.ProgramSignalEntry;
import com.akuacom.pss2.season.SeasonConfig;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;
import com.akuacom.pss2.signal.SignalLevelDef;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.util.DateTool;
import com.akuacom.pss2.util.ModeSlot;
import com.akuacom.utils.SortedArrayList;

/**
 * Created by IntelliJ IDEA. User: lin Date: Aug 4, 2008 Time: 3:44:17 PM
 */
public class BeanMappingUtil { 

	/** The Constant EMAIL_ADDRESS. */
	public static final String EMAIL_ADDRESS = "Email Address";

	/** The Constant PHONE_NUMBER. */
	public static final String PHONE_NUMBER = "Phone Number";

	/** The Constant FAX_NUMBER. */
	public static final String FAX_NUMBER = "Fax Number";

	/** The Constant PAGER. */
	public static final String PAGER = "Pager";

	/** The program manager. */
	private static EventManager programManager = EJBFactory
			.getBean(EventManager.class);

	/**
	 * Gets the contact information.
	 * 
	 * @param ci
	 *            the ci
	 * @param contactType
	 *            the contact type
	 * @param address
	 *            the address
	 * 
	 * @return the contact information
	 */
	static private ContactInformation getContactInformation(
			ContactInformation ci, String contactType, String address) {
		if (ci == null)
			ci = new ContactInformation();
		if (contactType.equals(EMAIL_ADDRESS)) {
			ContactInformation.EmailAddresses eas = new ContactInformation.EmailAddresses();
			eas.getAddress().add(address);
			ci.setEmailAddresses(eas);
		}
		if (contactType.equals(PHONE_NUMBER)) {
			ContactInformation.VoiceNumbers eas = new ContactInformation.VoiceNumbers();
			eas.getNumber().add(address);
			ci.setVoiceNumbers(eas);
		}
		if (contactType.equals(FAX_NUMBER)) {
			ContactInformation.FaxNumbers eas = new ContactInformation.FaxNumbers();
			eas.getNumber().add(address);
			ci.setFaxNumbers(eas);
		}
		if (contactType.equals(PAGER)) {
			ContactInformation.PagerNumbers eas = new ContactInformation.PagerNumbers();
			eas.getNumber().add(address);
			ci.setPagerNumbers(eas);
		}
		return ci;
	}

	/**
	 * Gets the dRAS client.
	 * 
	 * @param part
	 *            the part
	 * @param programList
	 *            the program list
	 * 
	 * @return the dRAS client
	 */
	static public DRASClient getDRASClient(Participant part,
			List<Program> programList) {
		DRASClient drasClient = new DRASClient();
		// TODO lin: not sure the following fields
		// drasClient.setCommsParams();
		// drasClient.setSimpleClientResponseSchedules();
		// drasClient.setOnLine();
		Location location = new Location();
		location.setAddress(part.getAddress());
		location.setGridLocation(part.getGridLocation());
		Location.Coordinate coordinate = new Location.Coordinate();
		coordinate.setLatitude(part.getLatitude());
		coordinate.setLongitude(part.getLongitude());
		location.setCoordinate(coordinate);
		drasClient.setLocationInformation(location);
		drasClient.setIdentifier(part.getAccountNumber());
		// drasClient.setClientType(part.getTypeString());
		drasClient.setClientType("SIMPLE"); // todo all we have map to simple
		// todo we are always online not supported others

		// commsParas:
		// connectionType: pull.
		// clinetURI not supported
		// clientAuthentication:
		// pollingPeriod: not supported. none of these supported in commsparas.

		// todo responseSchedule:
		// nearTransistoinTime maps pending on time
		// identifier: id for reponseSchedule
		// drasclient id:
		// todo map operationStateSpec
		// startTime map modeSignal signal entry. time
		// todo map Rule
		// value:
		// equation: we only support equal.

		drasClient.setParticipantID(part.getAccountNumber());

		DRASClient.Programs progs = new DRASClient.Programs();
		for (Program program : programList) {
			org.openadr.dras.utilityprogram.ProgramInfo pi = new org.openadr.dras.utilityprogram.ProgramInfo();
			pi.setProgramName(program.getProgramName());
			progs.getProgram().add(pi);
		}
		drasClient.setPrograms(progs);

		// todo 0313 add SimpleClientResponseSchedules mapping

		return drasClient;
	}

	/**
	 * Gets the participant account.
	 * 
	 * @param part
	 *            the part
	 * @param programList
	 *            the program list
	 * 
	 * @return the participant account
	 */
	static public ParticipantAccount getParticipantAccount(Participant part,
			List<Program> programList) {
		ParticipantAccount pa = new ParticipantAccount();
		pa.setAccountID(part.getAccountNumber());
		pa.setUserName(part.getUser());
		pa.setParticipantName(part.getUser());

		/*
		 * ContactInformation ci = new ContactInformation(); final List<Contact>
		 * contacts = part.getContacts(); for (Contact contact : contacts) { ci
		 * = getContactInformation(ci, contact.getType(), contact.getAddress());
		 * }
		 * 
		 * pa.setContactInformation(ci);
		 */
		ParticipantAccount.Programs progs = new ParticipantAccount.Programs();
		for (Program program : programList) {
			org.openadr.dras.utilityprogram.ProgramInfo pi = new org.openadr.dras.utilityprogram.ProgramInfo();
			pi.setProgramName(program.getProgramName());
			ProgramConstraint pc = new ProgramConstraint();
			progs.getProgram().add(pi);
			// TODO lin: we don't want to change program in participant
			// management operations.
			// will do it in program management.
			// pc.setMaxEventDuration(program.getm);
			// pc.setBlackOutDateTimeFilter(program.get);
			// pc.setConstraintID(program.get);
		}
		pa.setPrograms(progs);

		// todo locations address, geo, not supported in the server. client will
		// handle it.
		// todo group is supported.

		// for now, there is a one-to-one mapping of participants to dras
		// clients
		ParticipantAccount.DRASClients drasClients = new ParticipantAccount.DRASClients();
		drasClients.getClientID().add(pa.getParticipantName());
		pa.setDRASClients(drasClients);

		return pa;
	}

	/**
	 * Gets the participant ext info.
	 * 
	 * @param partAccount
	 *            the part account
	 * @param part
	 *            the part
	 * 
	 * @return the participant ext info
	 */
	static public ParticipantExtInfo getParticipantExtInfo(
			ParticipantAccount partAccount, Participant part) {
		ParticipantExtInfo paInfo;
		if (part == null) {
			paInfo = new ParticipantExtInfo();
		} else {
			paInfo = (ParticipantExtInfo) part;
		}
		paInfo.setAccountNumber(partAccount.getAccountID());
		paInfo.setUser(partAccount.getUserName());
		paInfo.setPassword("Test_1234");

		final Set<ParticipantContact> contacts = new TreeSet<ParticipantContact>();
		if (partAccount.getContactInformation() != null
				&& partAccount.getContactInformation().getEmailAddresses() != null) {
			List<String> listEmails = partAccount.getContactInformation()
					.getEmailAddresses().getAddress();
			if (listEmails != null) {
				for (String email : listEmails) {
					ParticipantContact contact = new ParticipantContact();
					contact.setAddress(email);
					contact.setType(EMAIL_ADDRESS);
					contacts.add(contact);
				}
			}
		}

		if (partAccount.getContactInformation() != null
				&& partAccount.getContactInformation().getPagerNumbers() != null) {
			List<String> listPages = partAccount.getContactInformation()
					.getPagerNumbers().getNumber();
			if (listPages != null) {
				for (String page : listPages) {
					ParticipantContact contact = new ParticipantContact();
					contact.setAddress(page);
					contact.setType(PAGER);
					contacts.add(contact);
				}
			}
		}

		if (partAccount.getContactInformation() != null
				&& partAccount.getContactInformation().getFaxNumbers() != null) {
			List<String> listFax = partAccount.getContactInformation()
					.getFaxNumbers().getNumber();
			if (listFax != null) {
				for (String fax : listFax) {
					ParticipantContact contact = new ParticipantContact();
					contact.setAddress(fax);
					contact.setType(FAX_NUMBER);
					contacts.add(contact);
				}
			}
		}

		if (partAccount.getContactInformation() != null
				&& partAccount.getContactInformation().getVoiceNumbers() != null) {
			List<String> listNumber = partAccount.getContactInformation()
					.getVoiceNumbers().getNumber();
			if (listNumber != null) {
				for (String page : listNumber) {
					ParticipantContact contact = new ParticipantContact();
					contact.setAddress(page);
					contact.setType(PHONE_NUMBER);
					contacts.add(contact);
				}
			}

		}
		paInfo.setContacts(contacts);

		ParticipantAccount.Programs programs = partAccount.getPrograms();

		List<Program> progs = new ArrayList();
		if (programs != null) {
			for (int i = 0; i < programs.getProgram().size(); i++) {
				org.openadr.dras.utilityprogram.ProgramInfo pi = programs
						.getProgram().get(i);

				Program program = new Program();

				program.setProgramName(pi.getProgramName());
				// TODO lin: we don't want to change program in participant
				// management operations.
				// will do it in program management.
				// TODO lin: However, participant can have its own customized
				// constraints etc. This is not supported yet.
				progs.add(program);
			}
		}
		paInfo.setPrograms(progs);

		return paInfo;
	}

	/**
	 * Gets the participant ext info list.
	 * 
	 * @param ParticipantAccounts
	 *            the participant accounts
	 * 
	 * @return the participant ext info list
	 */
	static public List<ParticipantExtInfo> getParticipantExtInfoList(
			ListOfParticipantAccounts ParticipantAccounts) {
		List<ParticipantExtInfo> paiList = new ArrayList();
		List<ParticipantAccount> list = ParticipantAccounts
				.getParticipantAccount();

		for (ParticipantAccount pa : list) {
			ParticipantExtInfo pai = getParticipantExtInfo(pa, null);
			paiList.add(pai);
		}
		return paiList;
	}

	/**
	 * Gets the program ext info.
	 * 
	 * @param program program
     * @param utilProg the util prog
	 * @throws Exception the exception
	 */
	static public void populateProgram(Program program, UtilityProgram utilProg)
			throws Exception {

		if (utilProg instanceof AkuaUtilityProgram) {
			AkuaUtilityProgram aUtilProg = (AkuaUtilityProgram) utilProg;

			if ("com.akuacom.pss2.program.dbp.DBPProgramEJB".equals(aUtilProg
					.getClassName())
					|| "com.akuacom.pss2.program.dbp.DBPBidProgramEJB"
							.equals(aUtilProg.getClassName())
					|| "com.akuacom.pss2.program.dbp.DBPNoBidProgramEJB"
							.equals(aUtilProg.getClassName())) {
				AkuaBidConfig bc = aUtilProg.getBidConfig();
				program.getBidConfig().setAcceptTimeoutPeriodM(bc
						.getAcceptTimeoutPeriodM());
				program.getBidConfig().setDefaultBidKW(bc.getDefaultBidKW());
				program.getBidConfig().setDrasBidding(bc.isDrasBidding());
				program.getBidConfig().setDrasRespondByPeriodM(bc
						.getDrasRespondByPeriodM());
				program.getBidConfig().setMinBidKW(bc.getMinBidKW());
				program.getBidConfig().setMinConsectutiveBlocks(bc
						.getMinConsectutiveBlocks());
				program.getBidConfig().setRespondByTimeH(bc.getRespondByTime().getHour());
				program.getBidConfig().setRespondByTimeM(bc.getRespondByTime().getMinute());

				List<AkuaBidBlock> blocks = bc.getBidBlocks().getBidBlock();
				if (blocks != null && blocks.size() > 0) {
					Set<com.akuacom.pss2.program.bidding.BidBlock> bidBlocks = new HashSet<com.akuacom.pss2.program.bidding.BidBlock>();
					int index = 0;
					for (AkuaBidBlock block : blocks) {
						com.akuacom.pss2.program.bidding.BidBlock tb = new com.akuacom.pss2.program.bidding.BidBlock();
						tb.setEndTimeH((DateTool
								.converXMLGregorianCalendarToDate(block
										.getEndTime())).getHours());
						tb.setEndTimeM(DateTool
								.converXMLGregorianCalendarToDate(
										block.getEndTime()).getMinutes());
						tb.setStartTimeH(DateTool
								.converXMLGregorianCalendarToDate(
										block.getStartTime()).getHours());
						tb.setStartTimeM(DateTool
								.converXMLGregorianCalendarToDate(
										block.getStartTime()).getMinutes());

                        bidBlocks.add(tb);
						index++;
					}
					program.getBidConfig().setBidBlocks(bidBlocks);
				}
			}

			program.setProgramName(aUtilProg.getName());
			program.setClassName(aUtilProg.getClassName());
			program.setUtilityProgramName(aUtilProg.getUtiltyProgramName());
			program.setPriority(aUtilProg.getPriority().intValue());
			program.setValidatorClass(aUtilProg.getValidatorClass());
			program.setValidatorConfigFile("");
			program.setUiScheduleEventString(aUtilProg.getUiScheduleEventString());
			program.setUiConfigureProgramString("");
			String emailsStr = "";
			if (aUtilProg.getContactEmails() != null) {
				List<String> emails = aUtilProg.getContactEmails()
						.getContactEmail();
				for (String email : emails) {
					if (!emailsStr.isEmpty()) {
						emailsStr = emailsStr + ",";
					}
					emailsStr = emailsStr + email;
				}

			}
			program.setMinIssueToStartM(aUtilProg.getMinIssueToStartM());
			program.setMustIssueBDBE(aUtilProg.isMustIssueBDBE());
			Date maxIssueTime = DateTool
					.converXMLGregorianCalendarToDate(aUtilProg
							.getMaxIssueTime());
			program.setMaxIssueTimeH(maxIssueTime.getHours());
			program.setMaxIssueTimeM(maxIssueTime.getMinutes());
			Date minStartTime = DateTool
					.converXMLGregorianCalendarToDate(aUtilProg
							.getMinStartTime());
			program.setMinStartTimeH(minStartTime.getHours());
			program.setMinStartTimeM(minStartTime.getMinutes());
			Date maxStartTime = DateTool
					.converXMLGregorianCalendarToDate(aUtilProg
							.getMaxStartTime());
			program.setMaxStartTimeH(maxStartTime.getHours());
			program.setMaxStartTimeM(maxStartTime.getMinutes());
			Date minEndTime = DateTool
					.converXMLGregorianCalendarToDate(aUtilProg.getMinEndTime());
			program.setMinEndTimeH(minEndTime.getHours());
			program.setMinEndTimeM(minEndTime.getMinutes());
			Date maxEndTime = DateTool
					.converXMLGregorianCalendarToDate(aUtilProg.getMaxEndTime());
			program.setMaxEndTimeH(maxEndTime.getHours());
			program.setMaxEndTimeM(maxEndTime.getMinutes());
			Date pendingTimeDBE = DateTool
					.converXMLGregorianCalendarToDate(aUtilProg
							.getPendingTimeDBE());
			program.setPendingTimeDBEH(pendingTimeDBE.getHours());
			program.setPendingTimeDBEM(pendingTimeDBE.getMinutes());
			program.setMinDurationM(aUtilProg.getMinDurationM());
			program.setMaxDurationM(aUtilProg.getMaxDurationM());
			program.setNotificationParam1(aUtilProg.getNotificationParam1());

			program.setManualCreatable(aUtilProg.isManualCreatable());

			List<String> signalStrings = aUtilProg.getSignals().getSignal();
			if (signalStrings == null || signalStrings.size() <= 0) {
				throw new Exception("Signals are required in program fields.");
			}
            Set<ProgramSignal> set = updateSignals(program, signalStrings);
            program.setSignals(set);
            
		} else {
			program.setProgramName(utilProg.getName());
			// TODO lin: UtilityProgram doesn't have utilityname. use name now,
			// need to verify.
			program.setUtilityProgramName(utilProg.getName());
			program.setPriority(utilProg.getPriority().intValue());
			String className = "com.akuacom.pss2.program.demo.DemoProgramEJB";
			program.setClassName(className);
			program.setValidatorClass("com.akuacom.pss2.program.cpp.CPPValidator");
			program.setValidatorConfigFile("cpp.validator.config");

			SignalManager signalManager = EJB3Factory
					.getLocalBean(SignalManager.class);
			ProgramSignal signal1 = new ProgramSignal();
			signal1.setSignalDef(signalManager.getSignal("pending"));
			ProgramSignal signal2 = new ProgramSignal();
			signal1.setSignalDef(signalManager.getSignal("mode"));
			Set<ProgramSignal> signals = new HashSet<ProgramSignal>();
			signals.add(signal1);
			signals.add(signal2);

			program.setSignals(signals);

			program.setUiScheduleEventString("DemoSchedulePage");
			program.setUiConfigureProgramString("");
			program.setUiConfigureEventString("");
			program.setMinIssueToStartM(0);
			program.setMustIssueBDBE(false);
			program.setMaxIssueTime("23:59");
			program.setMinStartTime("0:00");
			program.setMaxStartTime("23:59");
			program.setMinEndTime("0:00");
			program.setMaxEndTime("23:59");
			program.setMinDurationM(1);
			program.setMaxDurationM(1440);
			program.setPendingTimeDBE("21:00");
			program.setNotificationParam1("");
			program.setManualCreatable(false);

			ParticipantList partList = utilProg.getParticipants();
			if (partList != null && partList.getAccounts() != null) {
				List<String> parts = partList.getAccounts().getParticipantID();
				program.setParticipants(parts);
			}
		}
	}

    /**
     * The strategy here is to drop the unattended item, update existing, and add new.
     *
     * @param program program
     * @param signalStrings signal strings
     * @return new program signal set
     */
    private static Set<ProgramSignal> updateSignals(Program program, List<String> signalStrings) {
        SignalManager signalManager = EJB3Factory.getLocalBean(SignalManager.class);
        Set<ProgramSignal> signals = program.getSignals();
        Set<ProgramSignal> set = new HashSet<ProgramSignal>();
        for (String signalString : signalStrings) {
            final SignalDef signalDef = signalManager.getSignal(signalString);
            ProgramSignal s = null;
            if(signals != null) {
            	for (ProgramSignal signal : signals) {
	                if (signal.getSignalDef().equals(signalDef)) {
	                    s = signal;
	                    break;
	                }
	            }
	        }
            if (s == null) {
                s = new ProgramSignal();
                s.setSignalDef(signalDef);
                s.setProgram(program);
            }
            set.add(s);
        }
        return set;
    }

    /**
	 * Gets the akua utility program.
	 * 
	 * @param prog
	 *            the prog
	 * 
	 * @return the akua utility program
	 */
	static public AkuaUtilityProgram getAkuaUtilityProgram(Program prog) {
		AkuaUtilityProgram aProg = new AkuaUtilityProgram();
		aProg.setClassName(prog.getClassName());
		aProg.setManualCreatable(prog.isManualCreatable());
		aProg.setMaxDurationM(Integer.valueOf(String.valueOf(prog
				.getMaxDurationM())));
		aProg.setMinDurationM(Integer.valueOf(String.valueOf(prog
				.getMinDurationM())));
		aProg.setMinIssueToStartM(Integer.valueOf(String.valueOf(prog
				.getMinIssueToStartM())));
		Date maxEndTime = new Date();
		maxEndTime.setHours(prog.getMaxEndTimeH());
		maxEndTime.setMinutes(prog.getMaxEndTimeM());
		aProg.setMaxEndTime(DateTool
				.converDateToXMLGregorianCalendar(maxEndTime));
		Date minEndTime = new Date();
		minEndTime.setHours(prog.getMinEndTimeH());
		minEndTime.setMinutes(prog.getMinEndTimeM());
		aProg.setMinEndTime(DateTool
				.converDateToXMLGregorianCalendar(minEndTime));
		Date minStartTime = new Date();
		minStartTime.setHours(prog.getMinStartTimeH());
		minStartTime.setMinutes(prog.getMinStartTimeM());
		aProg.setMinStartTime(DateTool
				.converDateToXMLGregorianCalendar(minStartTime));
		Date maxStartTime = new Date();
		maxStartTime.setHours(prog.getMaxStartTimeH());
		maxStartTime.setMinutes(prog.getMaxStartTimeM());
		aProg.setMaxStartTime(DateTool
				.converDateToXMLGregorianCalendar(maxStartTime));
		Date pendingTimeDBE = new Date();
		pendingTimeDBE.setHours(prog.getPendingTimeDBEH());
		pendingTimeDBE.setMinutes(prog.getPendingTimeDBEM());
		aProg.setPendingTimeDBE(DateTool
				.converDateToXMLGregorianCalendar(pendingTimeDBE));
		Date maxIssueTime = new Date();
		maxIssueTime.setHours(prog.getMaxIssueTimeH());
		maxIssueTime.setMinutes(prog.getMaxIssueTimeM());
		aProg.setMaxIssueTime(DateTool
				.converDateToXMLGregorianCalendar(maxIssueTime));
		aProg.setMustIssueBDBE(prog.isMustIssueBDBE());
		aProg.setRemoteProgram(prog.isRemoteProgram());
		aProg.setUiScheduleEventString(prog.getUiScheduleEventString());
		aProg.setUtiltyProgramName(prog.getUtilityProgramName());
		aProg.setValidatorClass(prog.getValidatorClass());
		aProg.setName(prog.getProgramName());
		aProg.setNotificationParam1(prog.getNotificationParam1());
		aProg.setPriority(new BigInteger(String.valueOf(prog.getPriority())));
		aProg.setUiScheduleEventString(prog.getUiScheduleEventString());

		AkuaContactEmailList aContactList = new AkuaContactEmailList();
		aProg.setContactEmails(aContactList);

		ProgramConstraint pc = new ProgramConstraint();
		pc.setConstraintID(prog.getProgramName());
		pc.setProgramName(prog.getProgramName());
		pc.setMaxConsecutiveDays(BigInteger.valueOf(0));
		pc.setMaxEventDuration(Double.valueOf(0));
		aProg.setProgramConstraints(pc);

		// setup signal level
		List<String> signals = new ArrayList<String>();
		for (com.akuacom.pss2.program.signal.ProgramSignal sig : prog.getSignals()) {
			signals.add(sig.getSignalDef().getSignalName());
		}
		AkuaSignalList sigList = new AkuaSignalList();
		aProg.setSignals(sigList);
		aProg.getSignals().getSignal().addAll(signals);

		/*
		if ("com.akuacom.pss2.program.dbp.DBPProgramEJB".equals(prog
				.getClassName())
				|| "com.akuacom.pss2.program.dbp.DBPBidProgramEJB".equals(prog
						.getClassName())
				|| "com.akuacom.pss2.program.dbp.DBPNoBidProgramEJB"
						.equals(prog.getClassName())) {
			AkuaBidConfig abc = new AkuaBidConfig();

			abc.setAcceptTimeoutPeriodM(Integer.valueOf(String
					.valueOf(((DBPProgram) prog).getAcceptTimeoutPeriodM())));
			abc.setDefaultBidKW(((DBPProgram) prog).getDefaultBidKW());
			abc.setDrasBidding(((DBPProgram) prog).isDrasBidding());
			abc.setDrasRespondByPeriodM(Integer.valueOf(String
					.valueOf(((DBPProgram) prog).getDrasRespondByPeriodM())));
			abc.setMinBidKW(((DBPProgram) prog).getMinBidKW());
			abc.setMinConsectutiveBlocks(((DBPProgram) prog)
					.getMinConsectutiveBlocks());
			Date responseTime = new Date();
			responseTime.setHours(((DBPProgram) prog).getRespondByTimeH());
			responseTime.setMinutes(((DBPProgram) prog).getRespondByTimeM());
			abc.setRespondByTime(DateTool
					.converDateToXMLGregorianCalendar(responseTime));
			if (((DBPProgram) prog).getBidBlocks() != null
					&& ((DBPProgram) prog).getBidBlocks().length > 0) {
				AkuaBidBlockList abbl = new AkuaBidBlockList();
				abc.setBidBlocks(abbl);
				for (int i = 0; i < ((DBPProgram) prog).getBidBlocks().length; i++) {
					TimeBlock tb = ((DBPProgram) prog).getBidBlocks()[i];
					AkuaBidBlock abb = new AkuaBidBlock();
					Date startTime = new Date();
					startTime.setHours(tb.getStartHour());
					startTime.setMinutes(tb.getEndMinute());
					abb.setStartTime(DateTool
							.converDateToXMLGregorianCalendar(startTime));
					Date endTime = new Date();
					endTime.setHours(tb.getEndHour());
					endTime.setMinutes(tb.getEndMinute());
					abb.setEndTime(DateTool
							.converDateToXMLGregorianCalendar(endTime));
					abc.getBidBlocks().getBidBlock().add(abb);
				}
			}
			aProg.setBidConfig(abc);
		}
		*/
		return aProg;
	}

	/**
	 * Gets the program util.
	 * 
	 * @param program the program
	 * @return the program util
	 */
	static public UtilityProgram getProgramUtil(Program program) {
		UtilityProgram utilProg = new UtilityProgram();
		utilProg.setName(program.getProgramName());
		// TODO lin: UtilityProgram doesn't have utilityname. use name now, need
		// to verify.

		// Todo map to programConstraints
		// pss2 currently doesn't have this
		ProgramConstraint pc = new ProgramConstraint();
		pc.setConstraintID(program.getProgramName());
		pc.setProgramName(program.getProgramName());
		pc.setMaxConsecutiveDays(BigInteger.valueOf(0));
		pc.setMaxEventDuration(Double.valueOf(0));
		utilProg.setProgramConstraints(pc);

		// Todo map to biddingConfiguration:
		// submitTime: map to drasRespoinseByPeripodM others not supported
		if (program instanceof DBPProgram) {
			UtilityProgram.BiddingConfiguration bidConf = new UtilityProgram.BiddingConfiguration();

			bidConf.setSubmitTime(program.getBidConfig()
					.getDrasRespondByPeriodM() * 60);
			utilProg.setBiddingConfiguration(bidConf);
		}

		// Todo map to EventInfoTypes:
		// scheduleType: sc, cpp, dbp dynamic. we don't restrict in
		// implementation. we don't have flag
		// none means static with one time slot.
		// static means time slots were fixed at program level.
		// dynamic schedule were dynamic
		// enumerations: 6 of them mpas to numberSignal. one maps to levelSignal
		// based on typeID. it's loadLevel. the value is none egative integer
		// name: map to Signal signalName
		// typeId: map to NumberSignal numberType
		// minValue: not supported
		// maxValue: not supported
		// utilProg.setEventInfoTypes();

		utilProg.setPriority(new BigInteger(String.valueOf(program.getPriority())));

		List<String> partList = program.getParticipants();
		ParticipantList parts = new ParticipantList();
		parts.setAccounts(new ParticipantList.Accounts());
		parts.getAccounts().getParticipantID().addAll(partList);
		utilProg.setParticipants(parts);

		// n number of biding slots each one has certain amont of time (1 hour)
		// price is fixed

		return utilProg;
	}

	/**
	 * Gets the list of transaction logs.
	 * 
	 * @param logs
	 *            the logs
	 * 
	 * @return the list of transaction logs
	 */
	static public ListOfTransactionLogs getListOfTransactionLogs(
			List<FireLogEntry> logs) {
		ListOfTransactionLogs tranlogs = new ListOfTransactionLogs();
		for (FireLogEntry log : logs) {
			TransactionLog tranlog = getTransactionLog(log);
			tranlogs.getTransactionLog().add(tranlog);
		}
		return tranlogs;
	}

	/**
	 * Gets the transaction log.
	 * 
	 * @param log
	 *            the log
	 * 
	 * @return the transaction log
	 */
	static public TransactionLog getTransactionLog(FireLogEntry log) {
		TransactionLog tranlog = new TransactionLog();
		tranlog.setDescription(log.getDescription());

		// TODO LIN no result in internal log
		// tranlog.setResult(?);

		// tranlog.setRole(log.getUserRole());
		tranlog.setTimeStamp(DateTool.converDateToXMLGregorianCalendar(log
				.getLogDate()));
		tranlog.setUserName(log.getUserName());

		// TODO lin: what's transactionName?
		// tranlog.setTransactionName(log.get);
		return tranlog;
	}

	/**
	 * Gets the list of utility dr events.
	 * 
	 * @param progEventList
	 *            the prog event list
	 * 
	 * @return the list of utility dr events
	 * 
	 * @throws Exception
	 *             the exception
	 */
	static public ListOfUtilityDREvents getListOfUtilityDREvents(
			List<Event> progEventList) throws Exception {
		ListOfUtilityDREvents drEvents = new ListOfUtilityDREvents();
		for (Event progEvent : progEventList) {
			UtilityDREvent drEvent = getUtilityDREvent(progEvent);
			drEvents.getDREvent().add(drEvent);
		}
		return drEvents;
	}

	/**
	 * Gets the utility dr event.
	 * 
	 * @param progEvent
	 *            the prog event
	 * 
	 * @return the utility dr event
	 * 
	 * @throws Exception
	 *             the exception
	 */
	static public UtilityDREvent getUtilityDREvent(Event progEvent)
			throws Exception {
		UtilityDREvent event = new UtilityDREvent();
		event.setProgramName(progEvent.getProgramName());
		event.setEventIdentifier(progEvent.getEventName());
		UtilityDREvent.Destinations destinations = new UtilityDREvent.Destinations();
		event.setDestinations(destinations);
		UtilityDREvent.Destinations.Participants parts = new UtilityDREvent.Destinations.Participants();
		destinations.setParticipants(parts);

		List<EventParticipant> partList = progEvent.getParticipants();
		for (EventParticipant part : partList) {
			parts.getParticipantID().add(part.getParticipant().getAccountNumber());
		}

		UtilityDREvent.EventTiming eventTiming = new UtilityDREvent.EventTiming();
		event.setEventTiming(eventTiming);
		eventTiming.setEndTime(DateTool
				.converDateToXMLGregorianCalendar(progEvent.getEndTime()));
		eventTiming.setStartTime(DateTool
				.converDateToXMLGregorianCalendar(progEvent.getStartTime()));
		eventTiming.setNotificationTime(DateTool
				.converDateToXMLGregorianCalendar(progEvent.getIssuedTime()));

		if (progEvent instanceof DBPEvent) {
			DBPEvent dbpEvent = (DBPEvent) progEvent;
			UtilityDREvent.BiddingInformation biddingInfo = new UtilityDREvent.BiddingInformation();

			event.setBiddingInformation(biddingInfo);
			biddingInfo.setClosingTime(DateTool
					.converDateToXMLGregorianCalendar(dbpEvent.getRespondBy()));
			// responseByTime in dbp only
			// TODO lin: bid's opening time not supported it takes notification
			// time.
			biddingInfo.setOpeningTime(DateTool
					.converDateToXMLGregorianCalendar(dbpEvent.getStartTime()));

			// TODO lin: not sure how to get time block for cpp program. will
			// only get it for dbp

			// todo eventModNumber not supported
			// todo utilityITName ITRON for multiple utils. No need to map

			BiddingProgramManager dbpProgEJB = EJBFactory
					.getBean(BiddingProgramManager.class);

			UtilityDREvent.EventInformation eventInformation = new UtilityDREvent.EventInformation();
			event.setEventInformation(eventInformation);
			List<EventInfoInstance> eventInfos = eventInformation
					.getEventInfoInstance();

			for (EventParticipant part : partList) {
				// TODO lin: for now create an eventInfo for each Participant.
				// can potentially group participants for one group of event
				// values.
				EventInfoInstance eventInfo = new EventInfoInstance();
				eventInfos.add(eventInfo);
				String partID = part.getParticipant().getAccountNumber();
				EventInfoInstance.Participants InfoParts = new EventInfoInstance.Participants();
				eventInfo.setParticipants(InfoParts);
				InfoParts.getAccountID().add(partID);
				List<BidEntry> bids;
				try {
					bids = dbpProgEJB.getBid(progEvent.getProgramName(),
							progEvent.getEventName(),
							part.getParticipant().getParticipantName(), false);

				} catch (Exception e) {
					throw e;
				} // eventInfo.setValues();
				// TODO lin: need to understand the endTime of
				// EventInfoInstance. not sure why it's double.

				org.openadr.dras.bid.Bid bid = getBid(progEvent
						.getProgramName(), progEvent.getEventName(), partID,
						bids);
			}

		}

		return event;
	}

	/**
	 * Gets the program event.
	 * 
	 * @param utilEvent
	 *            the util event
	 * @param isDBP
	 *            the is dbp
	 * 
	 * @return the program event
	 */
	static public Event getProgramEvent(UtilityDREvent utilEvent, boolean isDBP) {

		// todo 0313 wait for the internal event object definition
		if (isDBP) {
			DBPEvent event = new DBPEvent();
			event.setProgramName(utilEvent.getProgramName());
			event.setEventName(utilEvent.getEventIdentifier());
			event.setReceivedTime(new Date());
			event.setIssuedTime(utilEvent.getEventTiming()
					.getNotificationTime().toGregorianCalendar().getTime());
			event.setStartTime(utilEvent.getEventTiming().getStartTime()
					.toGregorianCalendar().getTime());
			event.setEndTime(utilEvent.getEventTiming().getEndTime()
					.toGregorianCalendar().getTime());
			event.setRespondBy(utilEvent.getBiddingInformation()
					.getClosingTime().toGregorianCalendar().getTime());
			return event;
		} else {
			Event event = new Event();
			event.setProgramName(utilEvent.getProgramName());
			event.setEventName(utilEvent.getEventIdentifier());
			event.setReceivedTime(new Date()); // todo this is ok or not setting
			// it
			event.setIssuedTime(utilEvent.getEventTiming()
					.getNotificationTime().toGregorianCalendar().getTime());
			event.setStartTime(utilEvent.getEventTiming().getStartTime()
					.toGregorianCalendar().getTime());
			event.setEndTime(utilEvent.getEventTiming().getEndTime()
					.toGregorianCalendar().getTime());

			// todo 0313
			//

			return event;
		}
	}

	/**
	 * Gets the bid.
	 * 
	 * @param progName
	 *            the prog name
	 * @param eventName
	 *            the event name
	 * @param participantID
	 *            the participant id
	 * @param bidEntries
	 *            the bid entries
	 * 
	 * @return the bid
	 */
	static public org.openadr.dras.bid.Bid getBid(String progName,
			String eventName, String participantID, List<BidEntry> bidEntries) {
		org.openadr.dras.bid.Bid bid = new org.openadr.dras.bid.Bid();
		bid.setProgramName(progName);
		bid.setEventID(eventName);
		bid.setParticipantAccount(participantID);
		// TODO lin what's options, signature

		org.openadr.dras.bid.Bid.BidBlocks bidBlocks = new org.openadr.dras.bid.Bid.BidBlocks();
		for (BidEntry bidEntry : bidEntries) {
			BidBlock block = new BidBlock();
			block.setLoad(bidEntry.getPriceLevel());
			// todo
			// block.setPrice(bidEntry.getPriceLevel());
			// block.setDuration for noon to 4 then duration of 1 hour from 1 to
			// 2 optional not supported
			org.openadr.dras.bid.BidBlock.TimePeriod timePeriod = new org.openadr.dras.bid.BidBlock.TimePeriod();
			XMLGregorianCalendar startCal = DateTool
					.converDateToXMLGregorianCalendar(bidEntry.getBlockStart());
			XMLGregorianCalendar endCal = DateTool
					.converDateToXMLGregorianCalendar(bidEntry.getBlockEnd());
			timePeriod.setStartTime(startCal);
			timePeriod.setEndTime(endCal);
			block.setTimePeriod(timePeriod);

			// bid.setSignature() not supported

			// TODO how to get duration? caculated from timePeriod?

			bidBlocks.getBlock().add(block);
		}
		// TODO for all objects how to get SchemaVersion
		// bid.setSchemaVersion();
		bid.setBidBlocks(bidBlocks);
		return bid;
	}

	/**
	 * Gets the bid list.
	 * 
	 * @param partIDs
	 *            the part i ds
	 * @param eventID
	 *            the event id
	 * @param progName
	 *            the prog name
	 * 
	 * @return the bid list
	 * 
	 * @throws ProgramValidationException
	 *             the program validatation exception
	 */
	public static List<Bid> getBidList(ListOfParticipantAccountIDs partIDs,
			String eventID, String progName) throws ProgramValidationException {
		List<String> partIDList = partIDs.getParticipantAccountID();
		List<Bid> eventBidList = new ArrayList<Bid>();
		for (String partID : partIDList) {
			Bid bid = new Bid();
			bid.setEventName(eventID);
			bid.setAccountName(partID);
			bid.setProgramName(progName);
			eventBidList.add(bid);
		}
		return eventBidList;
	}

	/**
	 * Gets the bid list for event.
	 * 
	 * @param drEvents
	 *            the dr events
	 * @param eventID
	 *            the event id
	 * 
	 * @return the bid list for event
	 * 
	 * @throws ProgramValidationException
	 *             the program validatation exception
	 */
	public static List<Bid> getBidListForEvent(ListOfUtilityDREvents drEvents,
			String eventID) throws ProgramValidationException {
		List<UtilityDREvent> drEventList = drEvents.getDREvent();
		List<ProgramValidationMessage> errors = new ArrayList();
		List<Bid> bidList = null;
		for (UtilityDREvent drEvent : drEventList) {
			if (!drEvent.getEventIdentifier().equals(eventID)) {
				ProgramValidationMessage error = new ProgramValidationMessage(
						drEvent.getEventIdentifier(), "Not Valid Event ID.");
				errors.add(error);
			} else {
				// TODO lin: need to figure out how much can be changed in event
				// for each participant
				// DBPEvent dbpEvent = (DBPEvent) getProgramEvent(drEvent,
				// true);

				bidList = getBidEntry(drEvent, bidList);
			}
		}

		return bidList;
	}

	/**
	 * Gets the bid entry.
	 * 
	 * @param drEvent
	 *            the dr event
	 * @param bidList
	 *            the bid list
	 * 
	 * @return the bid entry
	 */
	static public List<Bid> getBidEntry(UtilityDREvent drEvent,
			List<Bid> bidList) {
		if (bidList == null) {
			bidList = new ArrayList<Bid>();
		}

		List<String> partList = drEvent.getDestinations().getParticipants()
				.getParticipantID();
		for (String part : partList) {
			Bid outBid = null;
			boolean newBid = true;
			for (Bid inBid : bidList) {
				if (inBid.getAccountName().equals(part)) {
					newBid = false;
					outBid = inBid;
					break;
				}
			}
			if (newBid) {
				outBid = new Bid();
				outBid.setProgramName(drEvent.getProgramName());
				outBid.setEventName(drEvent.getEventIdentifier());
				outBid.setAccountName(part);
				bidList.add(outBid);
			}

			Date startDate = DateTool.converXMLGregorianCalendarToDate(drEvent
					.getEventTiming().getStartTime());
			Date endDate = DateTool.converXMLGregorianCalendarToDate(drEvent
					.getEventTiming().getEndTime());
			BidEntry bidEntry = new BidEntry();
			bidEntry.setBlockEnd(endDate);
			bidEntry.setBlockStart(startDate);

			// TODO lin: we can also change event info here. but skip for now

			if (outBid != null && outBid.getBidEntries() != null) {
				outBid.getBidEntries().add(bidEntry);
			} else {
				List<BidEntry> entryList = new ArrayList<BidEntry>();
				entryList.add(bidEntry);
			}
		}

		return bidList;
	}

	/**
	 * Gets the program constraint.
	 * 
	 * @param program
	 *            the program
	 * @param event
	 *            the event
	 * 
	 * @return the program constraint
	 */
	static public ProgramConstraint getProgramConstraint(Program program,
			Event event) {
		if (program == null) {
			return null;
		}
		ProgramConstraint progConstraint = new ProgramConstraint();
		progConstraint.setProgramName(program.getProgramName());
		progConstraint.setConstraintID(program.getProgramName());

		BigInteger integer = new BigInteger(String.valueOf(program
				.getMaxDurationMS()));
		BigDecimal decimal = new BigDecimal(integer, 3);
		Double maxDuration = new Double(decimal.doubleValue());

		progConstraint.setMaxEventDuration(maxDuration);

		// TODO lin: need to confirm if this is from event or program.
		if (event != null) {
			Date startTime = event.getStartTime();
			Date endTime = event.getEndTime();

			EventWindow eventWindow = new EventWindow();
			eventWindow.setStartTime(DateTool
					.converDateToXMLGregorianCalendar(startTime));
			// TODO lin: need to confirm why endtime is biginteger
			eventWindow.setEndTime(DateTool
					.converDateToXMLGregorianCalendar(endTime));

			progConstraint.setEventWindow(eventWindow);

		}

		// TODO lin need to match to internal program
		/*
		 * EventWindow eventWindow = new EventWindow(); program.
		 * eventWindow.setStartTime(); progConstraint.setEventWindow();
		 * 
		 * progConstraint.set
		 */
		return progConstraint;
	}

	/**
	 * Gets the program constraint.
	 * 
	 * @param programConstraint
	 *            the program constraint
	 * 
	 * @return the program constraint
	 */
	static public Program getProgramConstraint(
			ProgramConstraint programConstraint) {
		Program prog = new Program();
		prog.setProgramName(programConstraint.getProgramName());
		// long maxDuration = programConstraint.getMaxEventDuration() * 1000;
		// Double maxDuration = new
		// Double(String.valueOf(program.getMaxDurationMS()/1000)) ;

		// progConstraint.setMaxEventDuration(maxDuration);

		// TODO lin need to match to internal program
		/*
		 * EventWindow eventWindow = new EventWindow(); program.
		 * eventWindow.setStartTime(); progConstraint.setEventWindow();
		 * 
		 * progConstraint.set
		 */
		return prog;
	}

	/**
	 * Gets the list of event states.
	 * 
	 * @param states
	 *            the states
	 * 
	 * @return the list of event states
	 */
	static public ListOfEventStates getListOfEventStates(
			List<com.akuacom.pss2.util.EventState> states) {
		ListOfEventStates eStates = new ListOfEventStates();
		for (com.akuacom.pss2.util.EventState log : states) {
			org.openadr.dras.eventstate.EventState state = getEventState(log);
			eStates.getEventStates().add(state);
		}
		return eStates;
	}

	/**
	 * Gets the event state.
	 * 
	 * @param state
	 *            the state
	 * 
	 * @return the event state
	 */
	static public org.openadr.dras.eventstate.EventState getEventState(
			com.akuacom.pss2.util.EventState state) {
		org.openadr.dras.eventstate.EventState eState = new org.openadr.dras.eventstate.EventState();
		eState.setDrasClientID(state.getDrasClientID());
		eState.setDrasName("Akuacom 4.3");
		eState.setEventIdentifier(state.getEventIdentifier());
		eState.setOffLine(false);
		eState.setProgramName(state.getProgramName());
		// eState.setSchemaVersion("0.9");
		eState.setEventModNumber(state.getEventModNumber());
		eState.setEventStateID(state.getEventStateID());

		SimpleClientEventData simpleDate = new SimpleClientEventData();

		simpleDate.setOperationModeValue(state.getOperationModeValue()
				.toString());
		simpleDate.setEventStatus(state.getEventStatus().toString());

		SimpleClientEventData.OperationModeSchedule modeSch = new SimpleClientEventData.OperationModeSchedule();

		List<ModeSlot> slots = state.getOperationModeSchedule();
		if (slots != null) {
			for (ModeSlot slot : slots) {
				OperationState oState = new OperationState();
				long longSlot = Double.doubleToLongBits(slot.getTimeSlotS());

				oState.setModeTimeSlot(BigInteger.valueOf(longSlot));
				oState.setOperationModeValue(slot.getOperationModeValue()
						.toString());
				modeSch.getModeSlot().add(oState);

			}
		}
		simpleDate.setOperationModeSchedule(modeSch);

		long longCurTime = Double.doubleToLongBits(state.getCurrentTimeS());

		simpleDate.setCurrentTime(BigDecimal.valueOf(longCurTime));

		eState.setSimpleDRModeData(simpleDate);

		// TODO lin add more for event based infor

		return eState;
	}

	/**
	 * Gets the participant account list.
	 * 
	 * @param parts
	 *            the parts
	 * 
	 * @return the participant account list
	 * 
	 * @throws ProgramValidationException
	 *             the program validatation exception
	 */
	public static ListOfParticipantAccounts getParticipantAccountList(
			List<Participant> parts) throws ProgramValidationException {
		ListOfParticipantAccounts pas = new ListOfParticipantAccounts();
		for (Participant part : parts) {
			pas.getParticipantAccount().add(getParticipantAccount(part));
		}
		return pas;
	}

	/**
	 * Gets the participant account.
	 * 
	 * @param part
	 *            the part
	 * 
	 * @return the participant account
	 */
	static public ParticipantAccount getParticipantAccount(Participant part) {
		ParticipantAccount pa = new ParticipantAccount();
		pa.setAccountID(part.getAccountNumber());
		pa.setUserName(part.getParticipantName());
		pa.setParticipantName(part.getParticipantName());

		Set<ProgramParticipant> pps = part.getProgramParticipants();

		ParticipantAccount.Programs progs = new ParticipantAccount.Programs();
		for (ProgramParticipant program : pps) {
			org.openadr.dras.utilityprogram.ProgramInfo pi = new org.openadr.dras.utilityprogram.ProgramInfo();
			pi.setProgramName(program.getProgramName());
			progs.getProgram().add(pi);
		}
		pa.setPrograms(progs);

		// for now, there is a one-to-one mapping of participants to dras
		// clients
		ParticipantAccount.DRASClients drasClients = new ParticipantAccount.DRASClients();
		drasClients.getClientID().add(pa.getParticipantName());
		pa.setDRASClients(drasClients);

		return pa;
	}

	/**
	 * Gets the client data list.
	 * 
	 * @param parts
	 *            the parts
	 * 
	 * @return the client data list
	 * 
	 * @throws ProgramValidationException
	 *             the program validatation exception
	 */
	public static ListofClientData getClientDataList(List<Participant> parts)
			throws ProgramValidationException {
		ListofClientData pas = new ListofClientData();
		for (Participant part : parts) {
			pas.getData().add(getClientData(part));
		}
		return pas;
	}

	/**
	 * Gets the client data.
	 * 
	 * @param part
	 *            the part
	 * 
	 * @return the client data
	 */
	static public ClientData getClientData(Participant part) {
		ClientData cd = new ClientData();
		cd.setClientID(part.getAccountNumber());
		cd.setParticipantID(part.getParticipantName());
		ListOfIDs programIDs = new ListOfIDs();

		Set<ProgramParticipant> pps = part.getProgramParticipants();

		for (ProgramParticipant program : pps) {
			if (program.getState() == ProgramParticipant.PROGRAM_PART_ACTIVE) {
				programIDs.getId().add(program.getProgramName());
			}
		}
		cd.setProgramIDs(programIDs);

		cd.setMySiteURL("../pss2.website/mysite.do?user="
				+ part.getParticipantName());
		cd.setGraphURL("../pss2.website/usages.do?userName="
				+ part.getParticipantName());

		cd.setParam3(part.getTypeString());

		return cd;
	}

	/**
	 * Gets the search handler.
	 * 
	 * @param drasSearchHandler
	 *            the dras search handler
	 * 
	 * @return the search handler
	 */
	static public SearchHandler getSearchHandler(
			AkuaDRASSearchHandler drasSearchHandler) {
		SearchHandler searchHandler;
		if (drasSearchHandler == null) {
			searchHandler = null;
		} else {
			searchHandler = new SearchHandler();
			if (drasSearchHandler.getDrasCursor() != null) {
				AkuaCursor cursor = new AkuaCursor();
				if (drasSearchHandler.getDrasCursor().getMaxPageSize() > 0) {
					cursor.setMaxPageSize(drasSearchHandler.getDrasCursor()
							.getMaxPageSize());
				}
				if (drasSearchHandler.getDrasCursor().getStartIndex() >= 0) {
					cursor.setStartIndex(drasSearchHandler.getDrasCursor()
							.getStartIndex());
				}
				if (drasSearchHandler.getDrasCursor().getSelectedPage() > 0) {
					cursor.setSelectedPage(drasSearchHandler.getDrasCursor()
							.getSelectedPage());
				}

				searchHandler.setCursor(cursor);
			}

			if (drasSearchHandler.getSearchCriteriaList() != null
					&& drasSearchHandler.getSearchCriteriaList()
							.getSearchCriteriaList().size() > 0) {
				List criList = new ArrayList();
				for (AkuaDRASSearchCriteria cri : drasSearchHandler
						.getSearchCriteriaList().getSearchCriteriaList()) {
					SearchCriterion sCri = new SearchCriterion();
					sCri.setFieldName(cri.getFieldName());
					// todo, hack for now, need to add object type for the
					// non-java client
					if (cri.getFieldValue().toLowerCase().equals("true")) {
						sCri.setFieldValue(new Boolean(true));
					} else if (cri.getFieldValue().toLowerCase()
							.equals("false")) {
						sCri.setFieldValue(new Boolean(false));
					} else {
						sCri.setFieldValue(cri.getFieldValue());
					}
					sCri.setJoinOperator(cri.getJoinOperator());
					sCri.setOperator(cri.getOperator());
					criList.add(sCri);

				}
				searchHandler.setCriteria(criList);
			}
		}

		return searchHandler;
	}

	/**
	 * Gets the akua dras search handler.
	 * 
	 * @param searchHandler
	 *            the search handler
	 * 
	 * @return the akua dras search handler
	 */
	static public AkuaDRASSearchHandler getAkuaDRASSearchHandler(
			SearchHandler searchHandler) {
		AkuaDRASSearchHandler drasSearchHandler = new AkuaDRASSearchHandler();

		if (searchHandler != null) {
			if (searchHandler.getCursor() != null) {
				AkuaDRASCursor cursor = new AkuaDRASCursor();
				cursor.setMaxPageSize(searchHandler.getCursor()
						.getMaxPageSize());
				cursor.setStartIndex(searchHandler.getCursor().getStartIndex());
				cursor.setSelectedPage(searchHandler.getCursor()
						.getSelectedPage());
				cursor.setPageSize(searchHandler.getCursor().getPageSize());
				cursor.setTotal(searchHandler.getCursor().getTotal());
				drasSearchHandler.setDrasCursor(cursor);
			}

			if (searchHandler.getCriteria() != null
					&& searchHandler.getCriteria().size() > 0) {
				ListofSearchCriteria criList = new ListofSearchCriteria();

				Iterator it = searchHandler.getCriteria().iterator();
				while (it.hasNext()) {
					SearchCriterion cri = (SearchCriterion) it.next();

					AkuaDRASSearchCriteria sCri = new AkuaDRASSearchCriteria();
					sCri.setFieldName(cri.getFieldName());
					sCri.setFieldValue(cri.getFieldValue().toString());

					sCri.setJoinOperator(cri.getJoinOperator());
					sCri.setOperator(cri.getOperator());
					criList.getSearchCriteriaList().add(sCri);

				}
				drasSearchHandler.setSearchCriteriaList(criList);
			}
		}

		return drasSearchHandler;
	}

	/**
	 * Gets the latest contact.
	 * 
	 * @param lastContact
	 *            the last contact
	 * @param withSec
	 *            the with sec
	 * 
	 * @return the latest contact
	 */
	static public String getLatestContact(Date lastContact, boolean withSec) {
		String ret;
		if (lastContact != null) {
			final long lastMin = (System.currentTimeMillis() - lastContact
					.getTime())
					/ (60 * 1000);
			if (lastMin < 12 * 60) // less than 12 hours
			{
				if (withSec) {
					ret = Long.toString(lastMin)
							+ " mins "
							+ new SimpleDateFormat("HH:mm:ss")
									.format(lastContact);
				} else {
					ret = Long.toString(lastMin) + " mins "
							+ new SimpleDateFormat("HH:mm").format(lastContact);
				}
			} else {
				if (withSec) {
					ret = new SimpleDateFormat("MM/dd/yy HH:mm:ss")
							.format(lastContact);
				} else {
					ret = new SimpleDateFormat("MM/dd/yy HH:mm")
							.format(lastContact);
				}

			}
		} else {
			ret = "";
		}
		return ret;
	}

	/**
	 * Gets the utility dr event.
	 * 
	 * @param template
	 *            the template
	 * 
	 * @return the utility dr event
	 * 
	 * @throws Exception
	 *             the exception
	 */
	static public org.openadr.dras.akuautilitydrevent.UtilityDREvent getUtilityDREvent(
			EventTemplate template) throws Exception {
		if (template == null)
			return null;
		org.openadr.dras.akuautilitydrevent.UtilityDREvent event = new org.openadr.dras.akuautilitydrevent.UtilityDREvent();
		event.setProgramName(template.getProgramName());
		event.setEventIdentifier(template.getName());
		org.openadr.dras.akuautilitydrevent.UtilityDREvent.Destinations destinations = new org.openadr.dras.akuautilitydrevent.UtilityDREvent.Destinations();
		event.setDestinations(destinations);
		org.openadr.dras.akuautilitydrevent.UtilityDREvent.Destinations.Participants parts = new org.openadr.dras.akuautilitydrevent.UtilityDREvent.Destinations.Participants();
		destinations.setParticipants(parts);

		org.openadr.dras.akuautilitydrevent.EventTiming eventTiming = new org.openadr.dras.akuautilitydrevent.EventTiming();
		event.setEventTiming(eventTiming);

		Date issueTime = null;
		Date startTime = null;
		Date endTime = null;
		long relativeStartTime = 0;

		Set<EventTemplateSignalEntry> entries = template.getSingalEntries();
		Iterator it = entries.iterator();
		org.openadr.dras.akuaeventinfo.EventInfoInstance eventInfoInstance = null;
		org.openadr.dras.akuaeventinfo.EventInfoInstance eventInfoInstancePrice1 = null;
		org.openadr.dras.akuaeventinfo.EventInfoInstance eventInfoInstancePrice2 = null;
		org.openadr.dras.akuaeventinfo.Values values = null;
		org.openadr.dras.akuaeventinfo.Values valuesPrice1 = null;
		org.openadr.dras.akuaeventinfo.Values valuesPrice2 = null;
		Date stTime = new Date((new Date()).getTime() + 10 * 60 * 1000);
		stTime = DateTool.roundTime(stTime, Calendar.MILLISECOND, true);
		stTime = DateTool.roundTime(stTime, Calendar.SECOND, true);

		while (it.hasNext()) {
			EventTemplateSignalEntry entry = (EventTemplateSignalEntry) it
					.next();
			String signalType = entry.getSignalType();
			String value = entry.getValue();

			if (signalType.equalsIgnoreCase("pending")) {
				issueTime = new Date(stTime.getTime()
						+ entry.getRelativeStartTime());
			}
			if (signalType.equalsIgnoreCase("mode")) {
				if (eventInfoInstance == null) {
					eventInfoInstance = new org.openadr.dras.akuaeventinfo.EventInfoInstance();
					if (startTime == null) {
						startTime = new Date(stTime.getTime()
								+ entry.getRelativeStartTime());
						relativeStartTime = entry.getRelativeStartTime();
					}
					eventInfoInstance
							.setEventInfoTypeName("OperationModeValue");

				}

				org.openadr.dras.akuaeventinfo.EventInfoValue inforValue = new org.openadr.dras.akuaeventinfo.EventInfoValue();
				Double relativeTime = Double.valueOf(((double) (entry
						.getRelativeStartTime() - relativeStartTime)) / 1000);
				inforValue.setStartTime(relativeTime);
				if (value.equalsIgnoreCase("normal")) {
					inforValue.setValue(1);
				} else if (value.equalsIgnoreCase("moderate")) {
					inforValue.setValue(2);
				} else if (value.equalsIgnoreCase("high")) {
					inforValue.setValue(3);
				}
				if (values == null) {
					values = new org.openadr.dras.akuaeventinfo.Values();
					eventInfoInstance.setValues(values);
				}

				eventInfoInstance.getValues().getValue().add(inforValue);
			}
			if (signalType
					.equalsIgnoreCase(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
							.name())) {
				if (eventInfoInstancePrice1 == null) {
					eventInfoInstancePrice1 = new org.openadr.dras.akuaeventinfo.EventInfoInstance();
					if (startTime == null) {
						startTime = new Date(stTime.getTime()
								+ entry.getRelativeStartTime());
						relativeStartTime = entry.getRelativeStartTime();
					}
					eventInfoInstancePrice1
							.setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
									.name());
				}

				org.openadr.dras.akuaeventinfo.EventInfoValue inforValue = new org.openadr.dras.akuaeventinfo.EventInfoValue();
				Double relativeTime = Double.valueOf(((double) (entry
						.getRelativeStartTime() - relativeStartTime)) / 1000);
				inforValue.setStartTime(relativeTime);
				inforValue.setValue(Double.valueOf(value).doubleValue());
				if (valuesPrice1 == null) {
					valuesPrice1 = new org.openadr.dras.akuaeventinfo.Values();
					eventInfoInstancePrice1.setValues(valuesPrice1);
				}
				eventInfoInstancePrice1.getValues().getValue().add(inforValue);
			}
			if (signalType
					.equalsIgnoreCase(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
							.name())) {
				if (eventInfoInstancePrice2 == null) {
					eventInfoInstancePrice2 = new org.openadr.dras.akuaeventinfo.EventInfoInstance();
					if (startTime == null) {
						startTime = new Date(stTime.getTime()
								+ entry.getRelativeStartTime());
						relativeStartTime = entry.getRelativeStartTime();
					}
					eventInfoInstancePrice2
							.setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
									.name());
				}

				org.openadr.dras.akuaeventinfo.EventInfoValue inforValue = new org.openadr.dras.akuaeventinfo.EventInfoValue();
				Double relativeTime = Double.valueOf(((double) (entry
						.getRelativeStartTime() - relativeStartTime)) / 1000);
				inforValue.setStartTime(relativeTime);
				inforValue.setValue(Double.valueOf(value).doubleValue());
				if (valuesPrice2 == null) {
					valuesPrice2 = new org.openadr.dras.akuaeventinfo.Values();
					eventInfoInstancePrice2.setValues(valuesPrice2);
				}
				eventInfoInstancePrice2.getValues().getValue().add(inforValue);
			}
			if (signalType.equalsIgnoreCase("end")) {
				endTime = new Date(stTime.getTime()
						+ entry.getRelativeStartTime());
			}
		}

		eventTiming.setEndTime(DateTool
				.converDateToXMLGregorianCalendar(endTime));
		eventTiming.setStartTime(DateTool
				.converDateToXMLGregorianCalendar(startTime));
		eventTiming.setNotificationTime(DateTool
				.converDateToXMLGregorianCalendar(issueTime));

		// setup event information here
		org.openadr.dras.akuautilitydrevent.EventInformation info = new org.openadr.dras.akuautilitydrevent.EventInformation();
		info.getEventInfoInstance().add(eventInfoInstance);
		if (eventInfoInstancePrice2 != null)
			info.getEventInfoInstance().add(eventInfoInstancePrice2);
		if (eventInfoInstancePrice1 != null)
			info.getEventInfoInstance().add(eventInfoInstancePrice1);
		event.setEventInformation(info);

		return event;
	}

	/**
	 * Gets the akua utility dr event.
	 * 
	 * @param progEvent
	 *            the prog event
	 * 
	 * @return the akua utility dr event
	 * 
	 * @throws Exception
	 *             the exception
	 */
	static public org.openadr.dras.akuautilitydrevent.UtilityDREvent getAkuaUtilityDREvent(
			Event progEvent) throws Exception {
		org.openadr.dras.akuautilitydrevent.UtilityDREvent event = new org.openadr.dras.akuautilitydrevent.UtilityDREvent();
		event.setProgramName(progEvent.getProgramName());
		event.setEventIdentifier(progEvent.getEventName());
		org.openadr.dras.akuautilitydrevent.UtilityDREvent.Destinations destinations = new org.openadr.dras.akuautilitydrevent.UtilityDREvent.Destinations();
		event.setDestinations(destinations);
		org.openadr.dras.akuautilitydrevent.UtilityDREvent.Destinations.Participants parts = new org.openadr.dras.akuautilitydrevent.UtilityDREvent.Destinations.Participants();
		destinations.setParticipants(parts);

		org.openadr.dras.akuautilitydrevent.EventInformation info = new org.openadr.dras.akuautilitydrevent.EventInformation();

		List<EventParticipant> partList = progEvent.getParticipants();
		for (EventParticipant part : partList) {
			String partName = part.getParticipant().getParticipantName();
			Set<EventParticipantSignal> signals = part.getSignals();
			for (EventParticipantSignal signal : signals) {
				String sigName = signal.getSignalDef().getSignalName();
				if (sigName.equalsIgnoreCase("mode")) {
					org.openadr.dras.akuaeventinfo.EventInfoInstance eventInfoInstance = new org.openadr.dras.akuaeventinfo.EventInfoInstance();
					eventInfoInstance
							.setEventInfoTypeName("OperationModeValue");
					SortedArrayList<SignalEntry> entries = new SortedArrayList<SignalEntry>();
					entries.addAll(signal.getSignalEntries());
					org.openadr.dras.akuaeventinfo.Values values = new org.openadr.dras.akuaeventinfo.Values();
					for (SignalEntry entry : entries) {
						EventParticipantSignalEntry levelEntry = (EventParticipantSignalEntry) entry;
						org.openadr.dras.akuaeventinfo.EventInfoValue inforValue = new org.openadr.dras.akuaeventinfo.EventInfoValue();
						Double relativeTime = Double.valueOf((levelEntry
								.getTime().getTime() - progEvent.getStartTime()
								.getTime()) / 1000);
						inforValue.setStartTime(relativeTime);
						if (levelEntry.getLevelValue().equalsIgnoreCase("normal")) {
							inforValue.setValue(1);
						} else if (levelEntry.getLevelValue().equalsIgnoreCase(
								"moderate")) {
							inforValue.setValue(2);
						} else if (levelEntry.getLevelValue().equalsIgnoreCase(
								"high")) {
							inforValue.setValue(3);
						}
						values.getValue().add(inforValue);
					}
					eventInfoInstance.setValues(values);
					info.getEventInfoInstance().add(eventInfoInstance);

				} else if (sigName
						.equalsIgnoreCase(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
								.name())) {

					org.openadr.dras.akuaeventinfo.EventInfoInstance eventInfoInstance = new org.openadr.dras.akuaeventinfo.EventInfoInstance();
					eventInfoInstance
							.setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
									.name());
					Set<? extends SignalEntry> entries = signal.getSignalEntries();
					org.openadr.dras.akuaeventinfo.Values values = new org.openadr.dras.akuaeventinfo.Values();
					for (SignalEntry entry : entries) {
						org.openadr.dras.akuaeventinfo.EventInfoValue inforValue = new org.openadr.dras.akuaeventinfo.EventInfoValue();

						Double relativeTime = Double.valueOf((entry
								.getTime().getTime() - progEvent.getStartTime()
								.getTime()) / 1000);
						inforValue.setStartTime(relativeTime);
						inforValue.setValue(entry.getNumberValue());
						values.getValue().add(inforValue);
					}
					eventInfoInstance.setValues(values);
					info.getEventInfoInstance().add(eventInfoInstance);
				} else if (sigName
						.equalsIgnoreCase(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
								.name())) {
					org.openadr.dras.akuaeventinfo.EventInfoInstance eventInfoInstance = new org.openadr.dras.akuaeventinfo.EventInfoInstance();
					eventInfoInstance
							.setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
									.name());
					Set<? extends SignalEntry> entries = signal.getSignalEntries();
					org.openadr.dras.akuaeventinfo.Values values = new org.openadr.dras.akuaeventinfo.Values();
					for (SignalEntry entry : entries) {
						org.openadr.dras.akuaeventinfo.EventInfoValue inforValue = new org.openadr.dras.akuaeventinfo.EventInfoValue();
						Double relativeTime = Double.valueOf((entry
								.getTime().getTime() - progEvent.getStartTime()
								.getTime()) / 1000);
						inforValue.setStartTime(relativeTime);
						inforValue.setValue(entry.getNumberValue());
						values.getValue().add(inforValue);
					}
					eventInfoInstance.setValues(values);
					info.getEventInfoInstance().add(eventInfoInstance);
				}
			}
		}
		event.setEventInformation(info);

		org.openadr.dras.akuautilitydrevent.EventTiming eventTiming = new org.openadr.dras.akuautilitydrevent.EventTiming();
		event.setEventTiming(eventTiming);
		eventTiming.setEndTime(DateTool
				.converDateToXMLGregorianCalendar(progEvent.getEndTime()));
		eventTiming.setStartTime(DateTool
				.converDateToXMLGregorianCalendar(progEvent.getStartTime()));
		eventTiming.setNotificationTime(DateTool
				.converDateToXMLGregorianCalendar(progEvent.getIssuedTime()));

		return event;
	}

	/**
	 * Gets the signal list.
	 * 
	 * @param signals
	 *            the signals
	 * 
	 * @return the signal list
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static List<com.akuacom.pss2.signal.SignalDef> getSignalList(
			ListOfSignals signals) throws Exception {
		List<com.akuacom.pss2.signal.SignalDef> pas = new ArrayList<com.akuacom.pss2.signal.SignalDef>();
		for (AkuaSignal signal : signals.getSignal()) {
			pas.add(getSignal(signal));
		}
		return pas;
	}

	/**
	 * Gets the signal.
	 * 
	 * @param sig
	 *            the sig
	 * 
	 * @return the signal
	 */
	static public com.akuacom.pss2.signal.SignalDef getSignal(AkuaSignal sig) {
		com.akuacom.pss2.signal.SignalDef asig = new com.akuacom.pss2.signal.SignalDef();

		asig.setSignalName(sig.getSignalName());
		asig.setType(sig.getType());
		asig.setUUID(sig.getId());

		ListOfValues levels = sig.getValue();
		Set<SignalLevelDef> values = new HashSet();

		for (String level : levels.getValue()) {
			SignalLevelDef sl = new SignalLevelDef();
			if (sig
					.getType()
					.equalsIgnoreCase(
							com.akuacom.pss2.util.EventInfoInstance.SignalType.LOAD_LEVEL
									.name())) {
				sl.setStringValue(level);
				values.add(sl);
			} else {
				// TODO: this doesn't make sense to me = levels are numbers?
				// sl.setDoubleValue(Double.valueOf(level));
				sl.setDefaultValue(true);
				values.add(sl);
			}
			sl.setSignal(asig);
		}
		asig.setSignalLevels(values);

		return asig;
	}

	/**
	 * Gets the signal list.
	 * 
	 * @param signals
	 *            the signals
	 * 
	 * @return the signal list
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static ListOfSignals getSignalList(
			List<com.akuacom.pss2.signal.SignalDef> signals) throws Exception {
		ListOfSignals pas = new ListOfSignals();
		for (com.akuacom.pss2.signal.SignalDef signal : signals) {
			pas.getSignal().add(getSignal(signal));
		}
		return pas;
	}

	/**
	 * Gets the signal.
	 * 
	 * @param sig
	 *            the sig
	 * 
	 * @return the signal
	 */
	static public AkuaSignal getSignal(com.akuacom.pss2.signal.SignalDef sig) {
		AkuaSignal asig = new AkuaSignal();

		asig.setSignalName(sig.getSignalName());
		asig.setType(sig.getType());
		asig.setId(sig.getUUID());

		ListOfValues values = new ListOfValues();
		Set<SignalLevelDef> levels = sig.getSignalLevels();

		for (SignalLevelDef level : levels) {
			if (sig
					.getType()
					.equalsIgnoreCase(
							com.akuacom.pss2.util.EventInfoInstance.SignalType.LOAD_LEVEL
									.name())) {
				values.getValue().add(level.getStringValue());
			} else {
				// TODO: this doesn't make sense to me = levels are numbers?
				// values.getValue().add(String.valueOf(level.getDoubleValue()));
			}
		}
		asig.setValue(values);

		return asig;
	}

	/**
	 * Gets the signal string.
	 * 
	 * @param signal
	 *            the signal
	 * 
	 * @return the signal string
	 */
	static public String getSignalString(com.akuacom.pss2.signal.SignalDef signal) {
		if (signal == null)
			return null;

		String type = signal.getType();
		if (com.akuacom.pss2.util.EventInfoInstance.SignalType.LOAD_LEVEL
				.name().equals(type)) {
			Set<SignalLevelDef> sis = signal.getSignalLevels();

			for (SignalLevelDef level : sis) {
				if (level.isDefaultValue()) {
					return level.getStringValue();
				}
			}
		} else {
			Set<SignalLevelDef> sis = signal.getSignalLevels();
			for (SignalLevelDef level : sis) {
				if (level.isDefaultValue()) {
					// TODO: this doesn't make sense to me = levels are numbers?
					// return String.valueOf(level.getDoubleValue());
				}
			}

		}
		return null;
	}

	/**
	 * Gets the contact list.
	 * 
	 * @param contacts
	 *            the contacts
	 * 
	 * @return the contact list
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static ListOfContacts getContactList(
			List<com.akuacom.pss2.contact.Contact> contacts) throws Exception {
		ListOfContacts pas = new ListOfContacts();
		for (com.akuacom.pss2.contact.Contact contact : contacts) {
			pas.getContact().add(getContact(contact));
		}
		return pas;
	}

	/**
	 * Gets the contact.
	 * 
	 * @param contact
	 *            the contact
	 * 
	 * @return the contact
	 */
	static public AkuaContact getContact(
			com.akuacom.pss2.contact.Contact contact) {
		AkuaContact aContact = new AkuaContact();
		aContact.setAddress(contact.getAddress());
		aContact.setType(contact.getType());
		aContact.setCommNotification(contact.isCommNotification());
		aContact.setEventNotification(getEventNotification(contact
				.getEventNotification()));
		aContact.setFirstName(contact.getFirstName());
		aContact.setLastName(contact.getLastName());
		aContact.setId(contact.getUUID());
		aContact.setOffSeasonNotiHours(contact.getOffSeasonNotiHours());
		aContact.setOnSeasonNotiHours(contact.getOnSeasonNotiHours());
		aContact.setId(contact.getUUID());
		return aContact;
	}

	/**
	 * Maps ENUM to boolean
	 * 
	 * @param eventType
	 *            ContactEventNotificationType
	 * @return true of ContactEventNotificationType.FullNotification false
	 *         otherwise
	 */
	private static boolean getEventNotification(
			ContactEventNotificationType eventType) {
		if (eventType != null
				&& eventType
						.equals(ContactEventNotificationType.FullNotification)) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the contact list.
	 * 
	 * @param contacts
	 *            the contacts
	 * 
	 * @return the contact list
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static List<com.akuacom.pss2.contact.Contact> getContactList(
			ListOfContacts contacts) throws Exception {
		List<com.akuacom.pss2.contact.Contact> pas = new ArrayList<com.akuacom.pss2.contact.Contact>();
		int priority = 0;
		for (AkuaContact contact : contacts.getContact()) {
			com.akuacom.pss2.contact.Contact c = getContact(contact);
			c.setPriority(priority);
			pas.add(c);
			priority++;
		}
		return pas;
	}

	/**
	 * Gets the property list.
	 * 
	 * @param propList
	 *            the prop list
	 * 
	 * @return the property list
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static ListOfProperties getPropertyList(List<CoreProperty> propList)
			throws Exception {
		ListOfProperties list = new ListOfProperties();
		int priority = 0;
		for (CoreProperty prop : propList) {
			AkuaProperty c = getProperty(prop);
			if (c != null)
				list.getProperty().add(c);
		}
		/*
		 * if(CorePropertyImpl.aboutInfo != null && !
		 * CorePropertyImpl.aboutInfo.isEmpty()) { AkuaProperty about = new
		 * AkuaProperty(); about.setValue(CorePropertyImpl.aboutInfo);
		 * about.setType("String"); about.setName("aboutInformation");
		 * about.setId("dummy"); list.getProperty().add(about); }
		 */
		return list;
	}

	/**
	 * Gets the property.
	 * 
	 * @param prop
	 *            the prop
	 * 
	 * @return the property
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static AkuaProperty getProperty(CoreProperty prop) throws Exception {
		AkuaProperty aprop = null;
		aprop = new AkuaProperty();
		aprop.setId(prop.getUUID());
		aprop.setName(prop.getPropertyName());
		aprop.setType(prop.getType());
		aprop.setValue(prop.getValueAsString());
		return aprop;
	}

	/**
	 * Gets the property list.
	 * 
	 * @param propList
	 *            the prop list
	 * 
	 * @return the property list
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static Set<CoreProperty> getPropertyList(ListOfProperties propList)
			throws Exception {
		Set<CoreProperty> list = new HashSet<CoreProperty>();
		for (AkuaProperty prop : propList.getProperty()) {
			CoreProperty c = getProperty(prop);
			list.add(c);
		}
		return list;
	}

	/**
	 * Gets the property.
	 * 
	 * @param prop
	 *            the prop
	 * 
	 * @return the property
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static CoreProperty getProperty(AkuaProperty prop) throws Exception {
		CoreProperty coreProperty = new CoreProperty(prop.getId(), prop
				.getName(), prop.getValue(), prop.getType());
		return coreProperty;
	}

	/**
	 * Gets the contact.
	 * 
	 * @param contact
	 *            the contact
	 * 
	 * @return the contact
	 */
	static public com.akuacom.pss2.contact.Contact getContact(
			AkuaContact contact) {
		com.akuacom.pss2.contact.Contact aContact = new com.akuacom.pss2.contact.Contact();
		aContact.setAddress(contact.getAddress());
		aContact.setType(contact.getType());
		aContact.setCommNotification(contact.isCommNotification());
		aContact
				.setEventNotification(contact.isEventNotification() ? ContactEventNotificationType.FullNotification
						: ContactEventNotificationType.NoNotification);
		aContact.setFirstName(contact.getFirstName());
		aContact.setLastName(contact.getLastName());
		aContact.setUUID(contact.getId());
		aContact.setOffSeasonNotiHours(contact.getOffSeasonNotiHours());
		aContact.setOnSeasonNotiHours(contact.getOnSeasonNotiHours());
		return aContact;
	}

	/**
	 * Gets the season config list.
	 * 
	 * @param propList
	 *            the prop list
	 * 
	 * @return the season config list
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static ListOfSeasonConfigs getSeasonConfigList(
			List<SeasonConfig> propList) throws Exception {
		ListOfSeasonConfigs list = new ListOfSeasonConfigs();

		for (SeasonConfig prop : propList) {
			AkuaSeasonConfig c = getSeasonConfig(prop);
			if (c != null)
				list.getSeasonTable().add(c);
		}

		return list;
	}

	/**
	 * Gets the season config.
	 * 
	 * @param prop
	 *            the prop
	 * 
	 * @return the season config
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static AkuaSeasonConfig getSeasonConfig(SeasonConfig prop)
			throws Exception {
		AkuaSeasonConfig aprop = new AkuaSeasonConfig();
		aprop.setId(prop.getUUID());
		aprop.setName(prop.getName());
		aprop.setStartDate(DateTool.converDateToXMLGregorianCalendar(prop
				.getStartDate()));
		aprop.setEndDate(DateTool.converDateToXMLGregorianCalendar(prop
				.getEndDate()));
		return aprop;
	}

	/**
	 * Gets the season config list.
	 * 
	 * @param propList
	 *            the prop list
	 * 
	 * @return the season config list
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static Set<SeasonConfig> getSeasonConfigList(
			ListOfSeasonConfigs propList) throws Exception {
		Set<SeasonConfig> list = new HashSet<SeasonConfig>();
		for (AkuaSeasonConfig prop : propList.getSeasonTable()) {
			SeasonConfig c = getSeasonConfig(prop);
			list.add(c);
		}
		return list;
	}

	/**
	 * Gets the season config.
	 * 
	 * @param prop
	 *            the prop
	 * 
	 * @return the season config
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static SeasonConfig getSeasonConfig(AkuaSeasonConfig prop)
			throws Exception {
		SeasonConfig aprop = new SeasonConfig();
		aprop.setUUID(prop.getId());
		aprop.setName(prop.getName());
		aprop.setStartDate(DateTool.converXMLGregorianCalendarToDate(prop
				.getStartDate()));
		aprop.setEndDate(DateTool.converXMLGregorianCalendarToDate(prop
				.getEndDate()));
		return aprop;
	}

	/**
	 * Gets the rTP config list.
	 * 
	 * @param propList
	 *            the prop list
	 * 
	 * @return the rTP config list
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static ListOfRTPConfigs getRTPConfigList(List<RTPConfig> propList)
			throws Exception {
		ListOfRTPConfigs list = new ListOfRTPConfigs();

		for (RTPConfig prop : propList) {
			AkuaRTPConfig c = getRTPConfig(prop);
			if (c != null)
				list.getPriceTable().add(c);
		}

		return list;
	}

	/**
	 * Gets the rTP config.
	 * 
	 * @param prop
	 *            the prop
	 * 
	 * @return the rTP config
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static AkuaRTPConfig getRTPConfig(RTPConfig prop) throws Exception {
		AkuaRTPConfig aprop = new AkuaRTPConfig();
		aprop.setId(prop.getUUID());
		aprop.setName(prop.getName());
		aprop.setStartTime(DateTool.converDateToXMLGregorianCalendar(prop
				.getStartTime()));
		aprop.setEndTime(DateTool.converDateToXMLGregorianCalendar(prop
				.getEndTime()));
		aprop.setStartTemperature(prop.getStartTemperature());
		aprop.setEndTemperature(prop.getEndTemperature());
		aprop.setRate(prop.getRate());
		aprop.setSeasonName(prop.getSeasonName());
		return aprop;
	}

	/**
	 * Gets the rTP config list.
	 * 
	 * @param propList
	 *            the prop list
	 * 
	 * @return the rTP config list
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static List<RTPConfig> getRTPConfigList(ListOfRTPConfigs propList)
			throws Exception {
		List<RTPConfig> list = new ArrayList<RTPConfig>();
		for (AkuaRTPConfig prop : propList.getPriceTable()) {
			RTPConfig c = getRTPConfig(prop);
			list.add(c);
		}
		return list;
	}

	/**
	 * Gets the rTP config.
	 * 
	 * @param prop
	 *            the prop
	 * 
	 * @return the rTP config
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static RTPConfig getRTPConfig(AkuaRTPConfig prop) throws Exception {
		RTPConfig aprop = new RTPConfig();
		aprop.setUUID(prop.getId());
		aprop.setName(prop.getName());
		aprop.setStartTime(DateTool.converXMLGregorianCalendarToDate(prop
				.getStartTime()));
		aprop.setEndTime(DateTool.converXMLGregorianCalendarToDate(prop
				.getEndTime()));
		aprop.setStartTemperature(prop.getStartTemperature());
		aprop.setEndTemperature(prop.getEndTemperature());
		aprop.setRate(prop.getRate());
		return aprop;
	}
}
