package com.akuacom.pss2.program.dbp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.EventInfoInstance.Participants;
import org.openadr.dras.eventinfo.EventInfoInstance.Values;
import org.openadr.dras.eventinfo.EventInfoValue;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.BiddingInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.ParticipantEAOBean;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.program.bidding.BidBlock;
import com.akuacom.pss2.program.bidding.BidConfig;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.test.TestUtil;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

@Ignore
public class DBPNoBidProgramEJBBeanTest {

	protected DBPNoBidProgramEJBBean dbpProgram;
	protected ProgramManager programManager;
	protected ParticipantEAO participantEAO;
	protected ParticipantManager participantManager;

	final String filename = "DBPEvent";
	final String programName = "DBP DA";

	private static final String DEFAULT_ACCOUTN_NUMBER = "001";
	private static final String DEFAULT_EVENT_START_TIME = "18:00";
	private static final String DEFAULT_EVENT_END_TIME = "20:00";
	private static final String DEFAULT_EVENT_DATE = "12/20/2010";

	@Before
	public void setup() {
		List<Program> programList = new ArrayList<Program>();

		DBPProgram program = getDBPProgram(programName);
		Participant participant = getParticipant(DEFAULT_ACCOUTN_NUMBER, false);
		ProgramParticipant programParticipant = generateProgramParcipant(
				program, participant);

		Set<ProgramParticipant> programParticipants = new HashSet<ProgramParticipant>();
		programParticipants.add(programParticipant);
		participant.setProgramParticipants(programParticipants);

		Participant client = getParticipant("p1.c1", true);
		client.setParent(participant.getParticipantName());

		ProgramParticipant programClientParticipant = generateProgramParcipant(
				program, client);

		Set<ProgramParticipant> programClientParticipants = new HashSet<ProgramParticipant>();
		programClientParticipants.add(programClientParticipant);
		client.setProgramParticipants(programClientParticipants);

		programList.add(program);
		programManager = new ProgramManagerBean(){
			List<Program> programList=new ArrayList<Program>();

			@Override
			public void setProgram(Program program) {
				this.programList.add(program);
			}
			
			@Override
			public Program getProgram(String programName) {
				for (Program program: programList){
					if (program.getProgramName().equals(programName))
						return program;
				}
				return null;
			}
		};
		
		for (Program prog:programList){
			programManager.setProgram(prog);
		}
//		programManager = new MockProgramManager();
//		programManager.setProgramList(programList);

		List<Participant> participantList = new ArrayList<Participant>();
		participantList.add(participant);
		List<ProgramParticipant> programParticipantList = new ArrayList<ProgramParticipant>();
		programParticipantList.add(programParticipant);
		programParticipantList.add(programClientParticipant);

		participantEAO = new ParticipantEAOBean() {
			
			List<Participant> participantList=new ArrayList<Participant>();
		    public List<Participant> getAllParticipants() {
		    	return participantList;
		    }

		    @Override
		    public List<Participant> findParticipantsByProgramName(String progName,
		            boolean isClient) throws AppServiceException {

		        List<Participant> parts = new ArrayList<Participant>();
		        for (Participant p: participantList) {
		        for (ProgramParticipant programParticipant : p.getProgramParticipants()) {
		            if (programParticipant.getProgramName().equals(progName)
		                    && programParticipant.getParticipant().isClient() == isClient) {
		                parts.add(programParticipant.getParticipant());
		            }
		        }
		        }

		        return parts;
		    }

		};
		
		participantEAO.getAllParticipants().addAll(participantList);

		participantManager = new ParticipantManagerBean() {
			List<Participant> participants=new ArrayList<Participant>();
			
			public List<Participant> getAllParticipants(){
				return participants;
			}

			public List<Participant> getParticipantsByAccounts(List<String> accountIDs){
				List<Participant> parts=new ArrayList<Participant>();
				for (Participant part: participants){
					if(accountIDs.contains(part.getAccountNumber()))
						parts.add(part);
				}
				return parts;
			}
		    public List<String> getClientNamesByParticipant(String partName){
				List<String> clientNames=new ArrayList<String>();
				for (Participant part: participants){
					if(part.isClient()&&part.getParent().equals(partName))
						clientNames.add(part.getParticipantName());
				}
				return clientNames;
		    }
			public List<String> getProgramsForParticipant(String participantName, boolean isClient){
				List<String> programNames=new ArrayList<String>();
				for(Participant part:participants){
					if (part.isClient() && part.getParticipantName().equals(participantName)){
						Set<ProgramParticipant> ppSet=part.getProgramParticipants();
						for (ProgramParticipant pp: ppSet){
							programNames.add(pp.getProgramName());
						}
					}
				}
		    	return programNames;
				
			}
		    public Participant getParticipantByAccount(String accountNumber) {
		    	for (Participant part: participants){
		    		if (part.getAccountNumber().equals(accountNumber))
		    			return part;
		    	}
		    	return null;
		    }

		};
		
		participantManager.getAllParticipants().add(participant);
		participantManager.getAllParticipants().add(client);

		dbpProgram = new DBPNoBidProgramEJBBean();
		TestUtil.setNonPublicMember(dbpProgram, "programManager",
				programManager);
		TestUtil.setNonPublicMember(dbpProgram, "participantEAO",
				participantEAO);
		TestUtil.setNonPublicMember(dbpProgram, "participantManager",
				participantManager);
	}

	private DBPProgram getDBPProgram(String programName) {
		DBPProgram program = new DBPProgram();
		program.setProgramName(programName);
		program.setMaxStartTime("12:0");
		program.setMaxEndTime("20:0");
		program.setMinStartTime("18:0");
		program.setMinEndTime("14:0");

		BidConfig bidConfig = new BidConfig();
		bidConfig.setUUID(TestUtil.generateRandomString());
		bidConfig.setMinBidKW(0);
		bidConfig.setDefaultBidKW(50);
		bidConfig.setDrasRespondByPeriodM(30);
		bidConfig.setRespondByTimeH(15);
		bidConfig.setRespondByTimeM(0);
		bidConfig.setMinConsectutiveBlocks(2);
		bidConfig.setAcceptTimeoutPeriodM(30);

		Set<BidBlock> bidBlocks = new TreeSet<BidBlock>(
				new Comparator<BidBlock>() {
					@Override
					public int compare(BidBlock o1, BidBlock o2) {
						return TimeBlock.compareStartTimes(DBPUtils
								.getTimeBlock(o1), DBPUtils.getTimeBlock(o2));
					}
				});
		for (int i = 12; i < 20; i++) {
			BidBlock bidBlock = new BidBlock();
			bidBlock.setUUID("bidBlock" + i);
			bidBlock.setStartTimeH(i);
			bidBlock.setStartTimeM(0);
			bidBlock.setEndTimeH(i + 1);
			bidBlock.setEndTimeM(0);
			bidBlock.setVersion(1);
			bidBlock.setBidConfig(bidConfig);
			bidBlocks.add(bidBlock);
		}
		bidConfig.setBidBlocks(bidBlocks);
		program.setBidConfig(bidConfig);

		return program;
	}

	private ProgramParticipant generateProgramParcipant(Program program,
			Participant participant) {
		ProgramParticipant programParticipant = new ProgramParticipant();
		programParticipant.setProgram(program);
		programParticipant.setProgramName(program.getProgramName());
		programParticipant.setParticipant(participant);
		return programParticipant;
	}

	private ProgramParticipant generateProgramParcipant(String programName,
			String participantName, boolean isClient) {
		return generateProgramParcipant(getDBPProgram(programName),
				getParticipant(participantName, isClient));
	}

	private Participant getParticipant(String accountNumaber, boolean isClient) {
		Participant participant = new Participant();
		participant.setAccountNumber(accountNumaber);
		participant.setParticipantName(accountNumaber);
		participant.setClient(isClient);
		return participant;
	}

	@Test
	public void testProcessAutoBidFile() throws ParseException, IOException,
			ProgramValidationException {
		EventFile eventFile = new EventFile();

		Calendar now = Calendar.getInstance();
		now.setTimeZone(TimeZone.getDefault());
		now.setTime(new Date());

		// event date is the next weekday.
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getDefault());
		if (now.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
				|| now.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			return;
		if (now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
			cal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 3);
		else
			cal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1);

		SimpleDateFormat format = new SimpleDateFormat("yyMMdd_hhmmss");
		// event name
		eventFile.setProgramName(programName);
		// event date
		format.applyPattern("MM/dd/yyyy");
		eventFile.setEventDate(format.format(cal.getTime()));

		// current time should be less than 17:55
		if (now.get(Calendar.HOUR_OF_DAY) >= 18)
			return;
		if (now.get(Calendar.HOUR_OF_DAY) == 17
				&& now.get(Calendar.MINUTE) > 50)
			return;

		eventFile.setEventStartTime(DEFAULT_EVENT_START_TIME);
		eventFile.setEventEndTime(DEFAULT_EVENT_END_TIME);
		eventFile.setHeader("Account,18:00-19:00,19:00-20:00");
		String[] bidEntries = new String[2];
		bidEntries[0] = "001,100,80";
		bidEntries[1] = "002,100,100";
		eventFile.setBidEntries(bidEntries);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write(generateFileString(eventFile).getBytes());

		SCEDBPEventFileParser parser=new SCEDBPEventFileParser(os.toString(), filename);
		
		UtilityDREvent drEvent = dbpProgram.createUtilityDREvent(parser);

		// one bid entry is available, and the other generates warning message
		// utilitydrevent is generated from bid entry: 001, 100, 100
		assertEquals(programName, drEvent.getProgramName()); // program name
		assertNotNull(drEvent.getEventIdentifier()); // event
		// name

		UtilityDREvent.EventTiming eventTiming = drEvent.getEventTiming();

		SimpleDateFormat inputDateParser = new SimpleDateFormat(
				"MM/dd/yyyy HH:mm");

		// start time
		Date time = eventTiming.getStartTime().toGregorianCalendar().getTime();
		assertEquals(inputDateParser.format(getTime(eventFile.getEventDate(),
				eventFile.getEventStartTime())), inputDateParser.format(time));
		// end time
		time = eventTiming.getEndTime().toGregorianCalendar().getTime();
		assertEquals(inputDateParser.format(getTime(eventFile.getEventDate(),
				eventFile.getEventEndTime())), inputDateParser.format(time));

		// notification time
		assertNotNull(eventTiming.getNotificationTime());

		// bidding information
		time = drEvent.getBiddingInformation().getOpeningTime()
				.toGregorianCalendar().getTime();
		assertEquals(inputDateParser.format(getTime(eventFile.getEventDate(),
				eventFile.getEventStartTime())), inputDateParser.format(time));
		time = drEvent.getBiddingInformation().getClosingTime()
				.toGregorianCalendar().getTime();
		assertEquals(inputDateParser.format(getTime(eventFile.getEventDate(),
				eventFile.getEventStartTime())), inputDateParser.format(time));

		// one bid entry
		assertEquals(1, drEvent.getEventInformation().getEventInfoInstance()
				.size());
		EventInformation eventInformation = drEvent.getEventInformation();
		//
		assertEquals(1, eventInformation.getEventInfoInstance().size());
		assertEquals("001", eventInformation.getEventInfoInstance().get(0)
				.getParticipants().getAccountID().get(0));
		assertEquals(2, eventInformation.getEventInfoInstance().get(0)
				.getValues().getValue().size());
		List<EventInfoValue> values = eventInformation.getEventInfoInstance()
				.get(0).getValues().getValue();
		assertEquals(100, values.get(0).getValue(), 0);
		assertEquals(80, values.get(1).getValue(), 0);

	}

	@Test
	public void testCreateNewDBPEvent() throws ParseException {
		UtilityDREvent utilityDREvent = new UtilityDREvent();
		utilityDREvent.setProgramName(programName);

		GregorianCalendar now = new GregorianCalendar();
		now.setTimeZone(TimeZone.getDefault());
		now.setTime(new Date());

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getDefault());
		cal.setTime(new Date());
		cal.setTime(getTime(DEFAULT_EVENT_DATE, DEFAULT_EVENT_START_TIME));

		SimpleDateFormat format = new SimpleDateFormat("yyMMdd_hhmmss");
		utilityDREvent.setEventIdentifier(format.format(new Date()));
		EventTiming eventTiming = new EventTiming();
		eventTiming.setStartTime(new XMLGregorianCalendarImpl(cal));

		cal.setTime(getTime(DEFAULT_EVENT_DATE, DEFAULT_EVENT_END_TIME));
		eventTiming.setEndTime(new XMLGregorianCalendarImpl(cal));

		eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(now));
		utilityDREvent.setEventTiming(eventTiming);

		BiddingInformation biddingInformation = new BiddingInformation();
		biddingInformation.setOpeningTime(eventTiming.getStartTime());
		biddingInformation.setClosingTime(eventTiming.getStartTime());
		utilityDREvent.setBiddingInformation(biddingInformation);

		EventInformation eventInformation = new EventInformation();
		EventInfoInstance eventInfoInstance = new EventInfoInstance();
		Participants participants = new Participants();
		eventInfoInstance.setParticipants(participants);
		Values values = new Values();
		eventInfoInstance.setValues(values);
		eventInfoInstance.getParticipants().getAccountID().add(
				DEFAULT_ACCOUTN_NUMBER);
		double eventStartTimeS = 0;
		double eventEndTimeS = 2 * 3600;
		for (int i = 0; i < 2; i++) {
			EventInfoValue value = new EventInfoValue();
			value.setValue(100);

			double startTimeS = i * 3600;
			value.setStartTime(startTimeS);
			eventInfoInstance.getValues().getValue().add(value);
		}
		eventInfoInstance.setEndTime(eventEndTimeS - eventStartTimeS);
		eventInformation.getEventInfoInstance().add(eventInfoInstance);
		utilityDREvent.setEventInformation(eventInformation);

		Event event = dbpProgram.createNewEvent(utilityDREvent);
		assertNotNull(event);
		assertTrue(event instanceof DBPEvent);
		DBPEvent dbpEvent = (DBPEvent) event;
		assertEquals(utilityDREvent.getProgramName(), dbpEvent.getProgramName());
		assertEquals(utilityDREvent.getEventIdentifier(), dbpEvent
				.getEventName());
		assertEquals(utilityDREvent.getEventTiming().getStartTime()
				.toGregorianCalendar().getTime(), dbpEvent.getStartTime());
		assertEquals(utilityDREvent.getEventTiming().getEndTime()
				.toGregorianCalendar().getTime(), dbpEvent.getEndTime());
		assertEquals(utilityDREvent.getEventTiming().getNotificationTime()
				.toGregorianCalendar().getTime(), dbpEvent.getIssuedTime());
		assertEquals(utilityDREvent.getEventTiming().getStartTime()
				.toGregorianCalendar().getTime(), dbpEvent.getRespondBy());
		assertEquals(utilityDREvent.getEventTiming().getStartTime()
				.toGregorianCalendar().getTime(), dbpEvent.getDrasRespondBy());

		assertEquals(BidState.PROCESSING_COMPLETE, dbpEvent
				.getCurrentBidState());

		assertNotNull(dbpEvent.getParticipants());
		assertEquals(1, dbpEvent.getParticipants().size());
	}

	private Date getTime(String date, String time) throws ParseException {
		SimpleDateFormat inputDateParser = new SimpleDateFormat(
				"MM/dd/yyyy HH:mm");

		return inputDateParser.parse(date + " " + time);
	}

	private String generateFileString(EventFile eventFile) {
		StringBuffer buf = new StringBuffer();
		buf.append(eventFile.getProgramName() + "\n");
		buf.append(eventFile.getEventDate() + "\n");
		buf.append(eventFile.getEventStartTime() + "\n");
		buf.append(eventFile.getEventEndTime() + "\n");
		buf.append(eventFile.getHeader() + "\n");
		buf.append(eventFile.getBidEntries()[0] + "\n");
		buf.append(eventFile.getBidEntries()[1] + "\n");

		return buf.toString();
	}

	@Ignore
	@Test(expected = ProgramValidationException.class)
	public void processAutoBidFileWithException() {

	}

	public class EventFile {
		String programName;
		String eventDate;
		String eventStartTime;
		String eventEndTime;
		String header;
		String[] bidEntries;

		public String getProgramName() {
			return programName;
		}

		public void setProgramName(String programName) {
			this.programName = programName;
		}

		public String getEventDate() {
			return eventDate;
		}

		public void setEventDate(String eventDate) {
			this.eventDate = eventDate;
		}

		public String getEventStartTime() {
			return eventStartTime;
		}

		public void setEventStartTime(String eventStartTime) {
			this.eventStartTime = eventStartTime;
		}

		public String getEventEndTime() {
			return eventEndTime;
		}

		public void setEventEndTime(String eventEndTime) {
			this.eventEndTime = eventEndTime;
		}

		public String getHeader() {
			return header;
		}

		public void setHeader(String header) {
			this.header = header;
		}

		public String[] getBidEntries() {
			return bidEntries;
		}

		public void setBidEntries(String[] bidEntries) {
			this.bidEntries = bidEntries;
		}
	}

}
