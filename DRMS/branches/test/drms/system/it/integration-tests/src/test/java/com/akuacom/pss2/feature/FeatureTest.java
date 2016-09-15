package com.akuacom.pss2.feature;



import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.ws.Holder;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.EventInfoValue;
import org.openadr.dras.participantaccount.ListOfParticipantAccountIDs;
import org.openadr.dras.participantaccount.ListOfParticipantAccounts;
import org.openadr.dras.participantaccount.ParticipantAccount;
import org.openadr.dras.programconstraint.ConstraintFilter;
import org.openadr.dras.utilitydrevent.ListOfEventIDs;
import org.openadr.dras.utilitydrevent.ListOfUtilityDREvents;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilityprogram.ListOfProgramNames;
import org.openadr.dras.utilityprogram.ParticipantList;
import org.openadr.dras.utilityprogram.ProgramInfo;

import com.akuacom.ejb.search.AkuaCursor;
import com.akuacom.ejb.search.SearchCriterion;
import com.akuacom.ejb.search.SearchHandler;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * This class test for program related stuff only.
 */
@Ignore
public class FeatureTest extends FeatureFixture {

    @Test
    public void featureSanityCheck(){
        
        //testAddParticipants();
        //testAddClients();
        //testParticipantList();
        //testClientList();
        //testIssueEvent();
        //testWSClientPolling();
        //checkTick5Second();
        //testDeleteParticipants();
    }

    public void testAddParticipants(){
        ListOfParticipantAccounts paList = new ListOfParticipantAccounts();

        int j=0;
        for(int i = 0; i <= FeatureFixture.NUMBER_OF_PARTICIPANTS; i++)
        {
            ParticipantAccount pa2 = new ParticipantAccount();
            pa2.setUserName("part_" + i);
            pa2.setAccountID("part_" + i);
            pa2.setParticipantName("part_" + i);

            ProgramInfo pi1 = new ProgramInfo();

            if(j >= programs.size())
            {
                j = 0;
            }
            if(programs.size() == 0)
            {
                // this should not happen. System should always have programs.   
            }
            else if(programs.get(j).getClassName().equals("com.akuacom.pss2.program.scertp.SCERTPProgramEJB"))
            {
                pi1.setProgramName("DEMO");
            }
            else
            {
                pi1.setProgramName(programs.get(j).getProgramName());
            }
            j++;

            ParticipantAccount.Programs progs = new ParticipantAccount.Programs();
            progs.getProgram().add(pi1);

            pa2.setPrograms(progs);

            paList.getParticipantAccount().add(pa2);
        }
        String ret = utilOp.createParticipantAccounts(paList);
	}

    public void testAddClients(){


        List<Participant> parts = pm.getAllParticipants();

        for(Participant part : parts)
        {
            for(int j=0; j<FeatureFixture.NUMBER_OF_CLIENTS_IN_PARTICIPANT; j++)
            {
                Participant client1 = new Participant();

                client1.setUser(part.getParticipantName() + ".client" + j);
                client1.setParent(part.getParticipantName());
                client1.setAccountNumber(part.getParticipantName() + ".client" + j);
                List<String> programNameList = pm.getProgramsForParticipant(part.getParticipantName(), false);

                int n=0;
                String[] programNames = new String[programNameList.size()];
                for(String pp : programNameList)
                {
                    programNames[n] = pp;
                    n++;
                }

                pm.createParticipant(client1, "Test_1234".toCharArray(), programNames);

            }
        }
        
    }

    public void testParticipantList(){
        SearchHandler sh = new SearchHandler();
        SearchCriterion sCri = new SearchCriterion();
        AkuaCursor cursor = new AkuaCursor();
        sh.setCursor(cursor);
        sCri.setFieldName("p.client");
        sCri.setOperator("=");
        sCri.setFieldValue(false);
        List scris = new ArrayList<SearchCriterion>();
        scris.add(sCri);
        sh.setCriteria(scris);

        sh = pm.searchParticipants(new ArrayList<String>(), new ArrayList<String>(), sh);
        sh.getResults();
        parts = sh.getResults();
    }

    public void testClientList(){
        SearchHandler sh = new SearchHandler();
        SearchCriterion sCri = new SearchCriterion();
        sCri.setFieldName("p.client");
        sCri.setOperator("=");
        sCri.setFieldValue(true);
        List scris = new ArrayList();
        scris.add(sCri);
        sh.setCriteria(scris);

        sh = pm.searchParticipants(new ArrayList<String>(), new ArrayList<String>(), sh);
        clients = sh.getResults();
    }

    public void testIssueEvent(){

        for(Program prog : programs)
        {
            if(prog.getClassName().equals("com.akuacom.pss2.program.demo.DemoProgramEJB"))
            {
                String eventName = this.testDemoIssueEvent(prog.getProgramName());
                ListOfUtilityDREvents events = this.getEvents(prog.getProgramName());
                this.testDeleteEvent(events.getDREvent().get(0));
                //eventName = this.testDemoIssueEvent(prog.getProgramName());
            }
        }
    }

    public ListOfUtilityDREvents getEvents(String progName)
    {
        ListOfProgramNames prognames = new ListOfProgramNames();
        prognames.getProgramID().add(progName);
        Holder<ListOfUtilityDREvents> holderForListOfUtilityDREvents = new Holder<ListOfUtilityDREvents>();
        Holder<String> holderForRetValue = new Holder<String>();
        utilOp.getDREventInformation(new ListOfEventIDs(), prognames, new ParticipantList(), holderForListOfUtilityDREvents, holderForRetValue);
        return holderForListOfUtilityDREvents.value;
    }

    public void testDeleteEvent(UtilityDREvent event)
    {

        String eventType = "CANCEL";
        Holder<String> holderForRetValue = new Holder<String>();
        Holder<ConstraintFilter> holderForConstraintFilter = new Holder<ConstraintFilter>();

        this.utilOp.modifyDREvent(event.getEventIdentifier(), eventType, event, holderForRetValue, holderForConstraintFilter );
    }

    public String testDemoIssueEvent(String programName){

        UtilityDREvent event = new UtilityDREvent();

	    UtilityDREvent.EventTiming eventTiming = new UtilityDREvent.EventTiming();

		Date now = new Date();
        //set notification time 5 mins later
        Date notificationTime = new Date(now.getTime() + 5*60*1000);
        GregorianCalendar notiCal = new GregorianCalendar();
        notiCal.setTime(notificationTime);
		eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(notiCal));

        //set start time 2 mins after the notification time
        Date startTime = new Date(notificationTime.getTime() + 2*60*1000);
        GregorianCalendar startCal = new GregorianCalendar();
        startCal.setTime(startTime);
        eventTiming.setStartTime(new XMLGregorianCalendarImpl(startCal));

        //set end time 3 mins after start time
        Date endTime = new Date(startTime.getTime() + 3*60*1000);
        GregorianCalendar endCal = new GregorianCalendar();
        endCal.setTime(endTime);
        eventTiming.setEndTime(new XMLGregorianCalendarImpl(endCal));

        event.setEventTiming(eventTiming);

		UtilityDREvent.EventInformation eventInfo = new UtilityDREvent.EventInformation();

		EventInfoInstance eventInfoInstance = new EventInfoInstance();
		eventInfoInstance.setEventInfoTypeName("OperationModeValue");
		EventInfoInstance.Values values = new EventInfoInstance.Values();
		eventInfoInstance.setValues(values);
		List<EventInfoValue> eventInfoValues = values.getValue();
		EventInfoValue eventInfoValue = new EventInfoValue();
		eventInfoValue.setStartTime(0.0);
		eventInfoValue.setValue(1.0); // normal
		eventInfoValues.add(eventInfoValue);
		eventInfoValue = new EventInfoValue();
		eventInfoValue.setStartTime(60.0);
		eventInfoValue.setValue(2.0); // moderate
		eventInfoValues.add(eventInfoValue);
		eventInfoValue = new EventInfoValue();
		eventInfoValue.setStartTime(120.0);
		eventInfoValue.setValue(3.0); // high
		eventInfoValues.add(eventInfoValue);
		eventInfo.getEventInfoInstance().add(eventInfoInstance);
        /*
		eventInfoInstance = new EventInfoInstance();
		eventInfoInstance.setEventInfoTypeName("PRICE_ABSOLUTE");
		eventInfoValue = new EventInfoValue();
		eventInfoValue.setStartTime(0.0);
		eventInfoValue.setValue(1.0);
		eventInfoValue.setStartTime(60.0);
		eventInfoValue.setValue(2.0);
		eventInfoValue.setStartTime(120.0);
		eventInfoValue.setValue(3.0);
		values = new EventInfoInstance.Values();
		eventInfoInstance.setValues(values);
		eventInfoValues = values.getValue();
		eventInfoValues.add(eventInfoValue);
		eventInfo.getEventInfoInstance().add(eventInfoInstance);
        */
		event.setEventInformation(eventInfo);

	    event.setProgramName(programName);
	    event.setEventIdentifier(Long.toString(System.currentTimeMillis()));
        Holder<ConstraintFilter> holderForConstraintFilter = new Holder<ConstraintFilter>();
        Holder<String> holderForRetValue = new Holder<String>();
        this.utilOp.initiateDREvent(event, holderForRetValue, holderForConstraintFilter );
		
        return event.getEventIdentifier();
    }

    public void testWSClientPolling(){
        FeatureFixture.POLLING = true;
        Date startPolling = new Date();
        try
        {
            for(Participant client : clients)
            {
                Thread.currentThread().sleep(1000);
            }
            Thread.currentThread().sleep(1200000);
        }
        catch (Exception e)
        {
            Assert.fail();
        }

        FeatureFixture.POLLING = false;
    }

    public void checkTick5Second(){
        // only retrieve slow method on tick5seconds
	}

    public void testDeleteParticipants(){

        ListOfParticipantAccountIDs ids = new ListOfParticipantAccountIDs();
        for(Participant client : clients)
        {
            ids.getParticipantAccountID().add(client.getAccountNumber()) ;
        }
        for(Participant part : parts)
        {
            ids.getParticipantAccountID().add(part.getAccountNumber()) ;   
        }
        utilOp.deleteParticipantAccounts(ids);

    }
}