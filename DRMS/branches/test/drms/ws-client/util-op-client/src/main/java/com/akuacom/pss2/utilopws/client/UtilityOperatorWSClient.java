/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.client.UtilityOperatorWSClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;
import org.openadr.dras.bid.ListOfBids;
import org.openadr.dras.drasclient.ListOfCommsStatus;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.EventInfoInstance.Values;
import org.openadr.dras.eventinfo.EventInfoValue;
import org.openadr.dras.eventstate.ListOfEventStates;
import org.openadr.dras.feedback.ListOfFeedback;
import org.openadr.dras.logs.ListOfDRASClientAlarms;
import org.openadr.dras.logs.ListOfTransactionLogs;
import org.openadr.dras.participantaccount.ContactInformation;
import org.openadr.dras.participantaccount.ListOfParticipantAccountIDs;
import org.openadr.dras.participantaccount.ListOfParticipantAccounts;
import org.openadr.dras.participantaccount.ParticipantAccount;
import org.openadr.dras.programconstraint.ConstraintFilter;
import org.openadr.dras.programconstraint.DateTimeWindow;
import org.openadr.dras.programconstraint.ProgramConstraint;
import org.openadr.dras.utilitydrevent.ListOfEventIDs;
import org.openadr.dras.utilitydrevent.ListOfUtilityDREvents;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventInformation;
import org.openadr.dras.utilitydrevent.UtilityDREvent.EventTiming;
import org.openadr.dras.utilityoperator.UtilityOperator;
import org.openadr.dras.utilityoperator.UtilityOperator_Service;
import org.openadr.dras.utilityprogram.ListOfIDs;
import org.openadr.dras.utilityprogram.ListOfProgramNames;
import org.openadr.dras.utilityprogram.ListOfPrograms;
import org.openadr.dras.utilityprogram.ParticipantList;
import org.openadr.dras.utilityprogram.ProgramInfo;
import org.openadr.dras.utilityprogram.UtilityProgram;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

/**
 * The Class UtilityOperatorWSClient.
 */
public class UtilityOperatorWSClient {
    // singleton
    /** The Constant INSTANCE. */
    public static final UtilityOperatorWSClient INSTANCE = new UtilityOperatorWSClient();

    // version number
    /** The Constant VERSION_NUMBER. */
    private static final String VERSION_NUMBER = "0.1";

    // default configuration file name
    /** The Constant DEFAULT_CONFIG_FILE_NAME. */
    private static final String DEFAULT_CONFIG_FILE_NAME = "UtilityOperatorWSClient.conf";

    // web service endpoint host
    /** The Constant PROP_END_POINT_HOST. */
    private static final String PROP_END_POINT_HOST = "endPointHost";

    /** The Constant DEFAULT_END_POINT_HOST. */
    private static final String DEFAULT_END_POINT_HOST = "pge.openadr.com";
    // web service endpoint port
    /** The Constant PROP_END_POINT_PORT. */
    private static final String PROP_END_POINT_PORT = "endPointPort";

    /** The Constant DEFAULT_END_POINT_PORT. */
    private static final String DEFAULT_END_POINT_PORT = "443";
    // web service endpoint path
    /** The Constant PROP_END_POINT_PATH. */
    private static final String PROP_END_POINT_PATH = "endPointPath";

    /** The Constant DEFAULT_END_POINT_PATH. */
    private static final String DEFAULT_END_POINT_PATH = "UtilityOperatorWS/UtilityOperatorWS";

    /** The Constant defaultProps. */
    private static final String[][] defaultProps = {
            { PROP_END_POINT_HOST, DEFAULT_END_POINT_HOST },
            { PROP_END_POINT_PORT, DEFAULT_END_POINT_PORT },
            { PROP_END_POINT_PATH, DEFAULT_END_POINT_PATH }, };

    // web service endpoint
    /** The end point. */
    private String endPoint = null;

    // application logger
    /** The logger. */
    private static Logger logger = Logger
            .getLogger("com.akuacom.pss2.utilopws.client");

    // web service stub
    /** The service. */
    private UtilityOperator service = null;

    // configuration properties
    /** The config. */
    private Properties config = null;

    /**
     * Instantiates a new utility operator ws client.
     */
    private UtilityOperatorWSClient() {
    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        INSTANCE.initialize(args);
        INSTANCE.testProgram();
        INSTANCE.testGroup();
    }

    /**
     * Gets the end point.
     * 
     * @return the end point
     */
    public String getEndPoint() {
        return endPoint;
    }

    /**
     * Initialize.
     */
    public void initialize() {
        String args[] = new String[1];
        args[0] = DEFAULT_CONFIG_FILE_NAME;
        initialize(args);
    }

    /**
     * Initialize.
     * 
     * @param args
     *            the args
     */
    public void initialize(String[] args) {
        config = configureProperties(args);

        int i = 0;

        endPoint = "http://";

        endPoint += config.getProperty(PROP_END_POINT_HOST) + ":"
                + config.getProperty(PROP_END_POINT_PORT) + "/"
                + config.getProperty(PROP_END_POINT_PATH);

        logger.debug("endpoint = " + endPoint);

        Authenticator.setDefault(new SimpleAuthenticator());

        // create stub
        try {
            service = new UtilityOperator_Service(new URL(endPoint + "?wsdl"),
                    new QName("http://www.openadr.org/DRAS/UtilityOperator/",
                            "UtilityOperatorWSService"))
                    .getUtilityOperatorPort();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Test init event.
     * 
     * @param programName
     *            the program name
     * @param participantID
     *            the participant id
     */
    private void testInitEvent(String programName, String participantID) {
        System.out
                .println("######## Start initiateDREvent ######################################");

        UtilityDREvent event = new UtilityDREvent();
        // event timing
        EventTiming eventTiming = new EventTiming();

        // set the notification time to 12/31/2008 16:00
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 2009);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        eventTiming.setNotificationTime(new XMLGregorianCalendarImpl(cal));

        // set the start time to 12/31/2008 18:00
        cal.set(Calendar.HOUR_OF_DAY, 18);
        eventTiming.setStartTime(new XMLGregorianCalendarImpl(cal));

        // set the end time to 12/31/2008 20:00
        cal.set(Calendar.HOUR_OF_DAY, 20);
        eventTiming.setEndTime(new XMLGregorianCalendarImpl(cal));

        event.setEventTiming(eventTiming);

        EventInformation eventInfo = new EventInformation();

        EventInfoInstance eventInfoInstance = new EventInfoInstance();
        // eventInfoInstance.setEventInfoTypeID("LOAD_LEVEL")
        eventInfoInstance.setEventInfoTypeName("OperationModeValue");
        Values values = new Values();
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

        eventInfoInstance = new EventInfoInstance();
        // eventInfoInstance.setEventInfoTypeID("PRICE_ABSOLUTE")
        eventInfoInstance
                .setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
                        .name());
        eventInfoValue = new EventInfoValue();
        eventInfoValue.setStartTime(0.0);
        eventInfoValue.setValue(1.0);
        eventInfoValue.setStartTime(60.0);
        eventInfoValue.setValue(2.0);
        eventInfoValue.setStartTime(120.0);
        eventInfoValue.setValue(3.0);
        values = new Values();
        eventInfoInstance.setValues(values);
        eventInfoValues = values.getValue();
        eventInfoValues.add(eventInfoValue);
        eventInfo.getEventInfoInstance().add(eventInfoInstance);

        eventInfoInstance = new EventInfoInstance();
        // eventInfoInstance.setEventInfoTypeID("PRICE_RELATIVE")
        eventInfoInstance
                .setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.PRICE_ABSOLUTE
                        .name());
        eventInfoValue = new EventInfoValue();
        eventInfoValue.setStartTime(0.0);
        eventInfoValue.setValue(1.0);
        eventInfoValue.setStartTime(60.0);
        eventInfoValue.setValue(2.0);
        eventInfoValue.setStartTime(120.0);
        eventInfoValue.setValue(3.0);
        values = new Values();
        eventInfoInstance.setValues(values);
        eventInfoValues = values.getValue();
        eventInfoValues.add(eventInfoValue);
        eventInfo.getEventInfoInstance().add(eventInfoInstance);

        eventInfoInstance = new EventInfoInstance();
        // eventInfoInstance.setEventInfoTypeID("PRICE_MULTIPLE")
        eventInfoInstance.setEventInfoTypeName("PRICE_MULTIPLE");
        eventInfoValue = new EventInfoValue();
        eventInfoValue.setStartTime(0.0);
        eventInfoValue.setValue(1.0);
        eventInfoValue.setStartTime(60.0);
        eventInfoValue.setValue(2.0);
        eventInfoValue.setStartTime(120.0);
        eventInfoValue.setValue(3.0);
        values = new Values();
        eventInfoInstance.setValues(values);
        eventInfoValues = values.getValue();
        eventInfoValues.add(eventInfoValue);
        eventInfo.getEventInfoInstance().add(eventInfoInstance);

        eventInfoInstance = new EventInfoInstance();
        // eventInfoInstance.setEventInfoTypeID("LOAD_LEVEL")
        eventInfoInstance
                .setEventInfoTypeName(com.akuacom.pss2.util.EventInfoInstance.SignalType.LOAD_LEVEL
                        .name());
        eventInfoValue = new EventInfoValue();
        eventInfoValue.setStartTime(0.0);
        eventInfoValue.setValue(1.0);
        eventInfoValue.setStartTime(60.0);
        eventInfoValue.setValue(2.0);
        eventInfoValue.setStartTime(120.0);
        eventInfoValue.setValue(3.0);
        values = new Values();
        eventInfoInstance.setValues(values);
        eventInfoValues = values.getValue();
        eventInfoValues.add(eventInfoValue);
        eventInfo.getEventInfoInstance().add(eventInfoInstance);

        eventInfoInstance = new EventInfoInstance();
        // eventInfoInstance.setEventInfoTypeID("LOAD_AMOUNT")
        eventInfoInstance.setEventInfoTypeName("LOAD_AMOUNT");
        eventInfoValue = new EventInfoValue();
        eventInfoValue.setStartTime(0.0);
        eventInfoValue.setValue(1.0);
        eventInfoValue.setStartTime(60.0);
        eventInfoValue.setValue(2.0);
        eventInfoValue.setStartTime(120.0);
        eventInfoValue.setValue(3.0);
        values = new Values();
        eventInfoInstance.setValues(values);
        eventInfoValues = values.getValue();
        eventInfoValues.add(eventInfoValue);
        eventInfo.getEventInfoInstance().add(eventInfoInstance);

        eventInfoInstance = new EventInfoInstance();
        // eventInfoInstance.setEventInfoTypeID("LOAD_PERCENTAGE")
        eventInfoInstance.setEventInfoTypeName("LOAD_PERCENTAGE");
        eventInfoValue = new EventInfoValue();
        eventInfoValue.setStartTime(0.0);
        eventInfoValue.setValue(1.0);
        eventInfoValue.setStartTime(60.0);
        eventInfoValue.setValue(2.0);
        eventInfoValue.setStartTime(120.0);
        eventInfoValue.setValue(3.0);
        values = new Values();
        eventInfoInstance.setValues(values);
        eventInfoValues = values.getValue();
        eventInfoValues.add(eventInfoValue);
        eventInfo.getEventInfoInstance().add(eventInfoInstance);

        eventInfoInstance = new EventInfoInstance();
        // eventInfoInstance.setEventInfoTypeID("GRID_RELIABILITY")
        eventInfoInstance.setEventInfoTypeName("GRID_RELIABILITY");
        eventInfoValue = new EventInfoValue();
        eventInfoValue.setStartTime(0.0);
        eventInfoValue.setValue(1.0);
        eventInfoValue.setStartTime(60.0);
        eventInfoValue.setValue(2.0);
        eventInfoValue.setStartTime(120.0);
        eventInfoValue.setValue(3.0);
        values = new Values();
        eventInfoInstance.setValues(values);
        eventInfoValues = values.getValue();
        eventInfoValues.add(eventInfoValue);
        eventInfo.getEventInfoInstance().add(eventInfoInstance);

        event.setEventInformation(eventInfo);

        // create OPENADR event
        event.setProgramName(programName);
        event.setEventIdentifier(Long.toString(System.currentTimeMillis()));
        Holder<ConstraintFilter> holderForConstraintFilter = new Holder<ConstraintFilter>();
        Holder<String> holderForRetValue = new Holder<String>();
        service.initiateDREvent(event, holderForRetValue,
                holderForConstraintFilter);
        System.out.println("DEMO initiateDREvent rv:\n"
                + holderForRetValue.value);
        System.out
                .println("######## End initiateDREvent ######################################");
    }

    // configure the program properties
    /**
     * Configure properties.
     * 
     * @param args
     *            the args
     * 
     * @return the properties
     */
    private Properties configureProperties(String[] args) {
        // default the config file name
        String configFileName = DEFAULT_CONFIG_FILE_NAME;

        // if there are any command line arguments, assume the first one is the
        // configuration file name and ignore all the others
        if (args.length >= 1) {
            configFileName = args[0];
        }
        if (args.length > 1) {
            System.out.println("ignoring extra command line arguments");
        }

        // setup the default configuration
        Properties defaultConfig = new Properties();
        defaultConfig.put(PROP_END_POINT_HOST, DEFAULT_END_POINT_HOST);
        defaultConfig.put(PROP_END_POINT_PORT, DEFAULT_END_POINT_PORT);
        defaultConfig.put(PROP_END_POINT_PATH, DEFAULT_END_POINT_PATH);

        // create the poperties object using the deafults and then read in any
        // custimazations from the configu file
        Properties config = new Properties(defaultConfig);

        // read the config file
        File configFile = new File(configFileName);
        System.out.println("configuration file = "
                + configFile.getAbsolutePath());
        if (!configFile.isFile()) {
            System.out.println("configuration file not found - using defaults");
        } else {
            try {
                FileInputStream in = new FileInputStream(configFile);
                config.load(in);
            } catch (IOException e) {
                System.out
                        .println("error reading configuration file - using defaults");
            }
        }

        // dump properties
        config.list(System.out);
        System.out.println("------------------------");

        return config;
    }

    /**
     * Test participant account.
     * 
     * @param programName1
     *            the program name1
     * @param programName2
     *            the program name2
     */
    private void testParticipantAccount(String programName1, String programName2) {
        System.out.println("Start testParticipantAccount");

        System.out.println("Start getParticipantFeedback");

        ListOfIDs ids1 = new ListOfIDs();
        ListOfIDs ids2 = new ListOfIDs();
        ListOfIDs ids3 = new ListOfIDs();
        ids3.getIdentifier().add(programName1);
        DateTimeWindow window = new DateTimeWindow();
        Date startTime = new Date((new Date()).getTime() - 600000);
        window.setStartDateTime(PrintTool
                .converDateToXMLGregorianCalendar(startTime));
        window.setEndDateTime(PrintTool
                .converDateToXMLGregorianCalendar(new Date()));
        Holder<ListOfFeedback> holderForListOfFeedback = new Holder<ListOfFeedback>();
        Holder<String> holderForRetValue = new Holder<String>();
        service.getParticipantFeedback(ids1, ids2, ids3, window, "test",
                holderForListOfFeedback, holderForRetValue);
        ListOfFeedback feedback = holderForListOfFeedback.value;
        PrintTool.printListOfFeedback(feedback);

        ListOfParticipantAccounts paList = new ListOfParticipantAccounts();
        ParticipantAccount pa1 = new ParticipantAccount();
        pa1.setUserName("test_part_name1");
        pa1.setAccountID("test_part_name1");
        // pa1.setPassword("password");
        pa1.setParticipantName("test_part_name1");

        ContactInformation ci = new ContactInformation();
        ContactInformation.EmailAddresses eas = new ContactInformation.EmailAddresses();
        eas.getAddress().add("xxx@akuacom.com");
        ci.setEmailAddresses(eas);

        pa1.setContactInformation(ci);

        ParticipantAccount pa2 = new ParticipantAccount();
        pa2.setUserName("test_part_name2");
        pa2.setAccountID("test_part_name2");
        // pa2.setPassword("password");
        pa2.setParticipantName("test_part_name2");

        ProgramInfo pi1 = new ProgramInfo();
        pi1.setProgramName(programName1);
        ProgramInfo pi2 = new ProgramInfo();
        pi2.setProgramName(programName2);

        ParticipantAccount.Programs progs = new ParticipantAccount.Programs();
        progs.getProgram().add(pi1);
        progs.getProgram().add(pi2);

        pa1.setPrograms(progs);
        pa2.setPrograms(progs);

        paList.getParticipantAccount().add(pa1);
        paList.getParticipantAccount().add(pa2);

        ListOfParticipantAccountIDs ids = new ListOfParticipantAccountIDs();
        ids.getParticipantAccountID().add("test_part_name1");
        ids.getParticipantAccountID().add("test_part_name2");

        System.out.println("Start deleteParticipantAccounts");
        String ret = service.deleteParticipantAccounts(ids);
        System.out.println(ret);

        System.out.println("Start createParticipantAccounts");
        ret = service.createParticipantAccounts(paList);
        System.out.println(ret);

        paList = new ListOfParticipantAccounts();
        pa1 = new ParticipantAccount();
        pa1.setUserName("test_part_name1");
        pa1.setAccountID("test_part_name1");
        // pa1.setPassword("password");
        pa1.setParticipantName("test_part_name1");

        ret = service.createParticipantAccounts(paList);
        System.out.println(ret);

        paList.getParticipantAccount().add(pa1);
        ret = service.createParticipantAccounts(paList);
        System.out.println(ret);

        System.out.println("Start modifyParticipantAccounts");
        ListOfParticipantAccounts paList2 = new ListOfParticipantAccounts();
        ParticipantAccount pa3 = new ParticipantAccount();
        pa3.setUserName("test_part_name2");
        pa3.setAccountID("test_part_name2");
        pa3.setParticipantName("test_part_name2");
        ContactInformation ci2 = new ContactInformation();
        ContactInformation.VoiceNumbers eas2 = new ContactInformation.VoiceNumbers();
        eas2.getNumber().add("555-555-5555");
        ci2.setVoiceNumbers(eas2);
        pa3.setContactInformation(ci2);

        ProgramInfo pi3 = new ProgramInfo();
        pi3.setProgramName(programName1);

        ParticipantAccount.Programs progs3 = new ParticipantAccount.Programs();
        progs3.getProgram().add(pi3);

        pa3.setPrograms(progs3);

        paList2.getParticipantAccount().add(pa3);

        ret = service.modifyParticipantAccounts(paList2);
        System.out.println(ret);

        System.out.println("Start getParticipantAccounts: " + programName1);
        ParticipantList pl = new ParticipantList();
        Holder<ListOfParticipantAccounts> holderForListOfParticipantAccounts = new Holder<ListOfParticipantAccounts>();
        holderForRetValue = new Holder<String>();
        service.getParticipantAccounts(pl, programName2,
                holderForListOfParticipantAccounts, holderForRetValue);
        ListOfParticipantAccounts pas = holderForListOfParticipantAccounts.value;
        PrintTool.printParticipantAccounts(pas);
        ParticipantList.Accounts accounts = new ParticipantList.Accounts();
        pl.setAccounts(accounts);
        pl.getAccounts().getParticipantID().add("test_part_name2");
        holderForListOfParticipantAccounts = new Holder<ListOfParticipantAccounts>();
        holderForRetValue = new Holder<String>();
        service.getParticipantAccounts(pl, programName1,
                holderForListOfParticipantAccounts, holderForRetValue);
        pas = holderForListOfParticipantAccounts.value;
        System.out.println("Start getParticipantAccounts: " + programName1);
        PrintTool.printParticipantAccounts(pas);

        testEvent(programName1, "test_part_name2");
        /*
         * System.out.println("Start deleteParticipantAccounts"); ret =
         * service.deleteParticipantAccounts(ids); System.out.println(ret);
         */
    }

    /**
     * Test program.
     */
    private void testProgram() {
        System.out.println("Start testProgram");

        System.out
                .println("######## Start deleteProgram ######################################");
        ListOfParticipantAccountIDs ids = new ListOfParticipantAccountIDs();
        ids.getParticipantAccountID().add("test_part_name1");
        String ret;

        // ret = service.deleteProgram("DBP Program Test") ;
        // ret = service.deleteProgram("CPP Program Test") ;
        ret = service.deleteProgram("Demo Program Test1");
        ret = service.deleteProgram("Demo Program Test2");
        System.out.println(ret);
        System.out
                .println("######## End deleteProgram ######################################");

        System.out
                .println("######## Start createProgram ######################################");
        UtilityProgram prog = new UtilityProgram();
        // prog.setName("DBP Program Test");
        prog.setName("Demo Program Test1");
        prog.setPriority(new BigInteger("10"));

        ProgramConstraint constraint = new ProgramConstraint();

        Holder<String> holderForRetValue = new Holder<String>();
        prog.setProgramConstraints(constraint);
        constraint.setProgramName("Demo Program Test1");
        constraint.setConstraintID("Demo Program Test1");

        ret = service.createProgram(prog);
        System.out.println(ret);

        prog = new UtilityProgram();
        prog.setName("Demo Program Test2");
        prog.setPriority(new BigInteger("10"));

        ret = service.createProgram(prog);
        System.out.println(ret);

        System.out
                .println("######## End createProgram ######################################");

        testParticipantAccount("Demo Program Test1", "Demo Program Test2");

        System.out
                .println("######## Start modifyProgram ######################################");
        prog.setPriority(new BigInteger("9"));
        ParticipantList pl = new ParticipantList();
        ParticipantList.Accounts accounts = new ParticipantList.Accounts();
        accounts = new ParticipantList.Accounts();
        pl.setAccounts(accounts);
        pl = new ParticipantList();
        accounts = new ParticipantList.Accounts();
        pl.setAccounts(accounts);
        pl.getAccounts().getParticipantID().add("test_part_name1");
        prog.setParticipants(pl);
        service.modifyProgram(prog);

        System.out
                .println("######## End modifyProgram ######################################");

        System.out
                .println("######## Start adjustProgramParticipants ######################################");
        prog.setPriority(new BigInteger("9"));
        pl = new ParticipantList();
        accounts = new ParticipantList.Accounts();
        pl.setAccounts(accounts);
        pl.getAccounts().getParticipantID().add("test_part_name1");
        prog.setParticipants(pl);
        service.adjustProgramParticipants("Demo Program Test1", true, pl);
        System.out
                .println("######## End adjustProgramParticipants ######################################");

        System.out
                .println("######## Start getPrograms ######################################");
        Holder<ListOfPrograms> holderForListOfPrograms = new Holder<ListOfPrograms>();
        holderForRetValue = new Holder<String>();
        service.getPrograms(new ListOfProgramNames(), new ParticipantList(),
                holderForListOfPrograms, holderForRetValue);
        ListOfPrograms progs = holderForListOfPrograms.value;
        holderForListOfPrograms = new Holder<ListOfPrograms>();
        holderForRetValue = new Holder<String>();
        service.getPrograms(null, null, holderForListOfPrograms,
                holderForRetValue);
        progs = holderForListOfPrograms.value;

        PrintTool.printPrograms(progs);

        ListOfProgramNames names = new ListOfProgramNames();
        names.getProgramID().add("Demo Program Test2");

        System.out.println("Start getPrograms 2");
        holderForListOfPrograms = new Holder<ListOfPrograms>();
        holderForRetValue = new Holder<String>();
        service.getPrograms(names, new ParticipantList(),
                holderForListOfPrograms, holderForRetValue);
        progs = holderForListOfPrograms.value;

        PrintTool.printPrograms(progs);

        ParticipantList partList = new ParticipantList();
        partList.setAccounts(new ParticipantList.Accounts());
        partList.getAccounts().getParticipantID().add("test_part_name1");

        holderForListOfPrograms = new Holder<ListOfPrograms>();
        holderForRetValue = new Holder<String>();
        service.getPrograms(names, partList, holderForListOfPrograms,
                holderForRetValue);
        progs = holderForListOfPrograms.value;

        PrintTool.printPrograms(progs);

        System.out
                .println("######## End getPrograms ######################################");

        System.out
                .println("######## Start adjustProgramParticipants ######################################");
        prog.setPriority(new BigInteger("9"));
        pl = new ParticipantList();
        accounts = new ParticipantList.Accounts();
        pl.setAccounts(accounts);
        pl.getAccounts().getParticipantID().add("test_part_name1");
        prog.setParticipants(pl);

        String retV = service.adjustProgramParticipants("Demo Program Test1",
                false, pl);
        System.out
                .println("retV\n######## End adjustProgramParticipants ######################################");

        testTransactionLog("Demo Program Test1", "Demo Program Test2");

        System.out
                .println("######## Start deleteProgram ######################################");
        System.out.println("Start deleteParticipantAccounts");
        ids = new ListOfParticipantAccountIDs();
        ids.getParticipantAccountID().add("test_part_name1");
        ids.getParticipantAccountID().add("test_part_name2");
        ret = service.deleteParticipantAccounts(ids);
        System.out.println(ret);
        ret = service.deleteProgram("Demo Program Test1");
        System.out.println("delete Demo Program Test1 " + ret);
        ret = service.deleteProgram("Demo Program Test2");
        System.out.println("delete Demo Program Test2 " + ret);
        System.out
                .println("######## End deleteProgram ######################################");
    }

    /**
     * Test transaction log.
     * 
     * @param programName1
     *            the program name1
     * @param programName2
     *            the program name2
     */
    private void testTransactionLog(String programName1, String programName2) {
        System.out
                .println("######## Start getDRASTransactions ######################################");
        ListOfIDs ids = new ListOfIDs();
        String methodName = "error";
        DateTimeWindow interval = new DateTimeWindow();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss S");
        try {
            interval.setStartDateTime(PrintTool
                    .converDateToXMLGregorianCalendar(dateFormat
                            .parse("2008-08-04 14:37:20 560")));
            interval.setStartDateTime(PrintTool
                    .converDateToXMLGregorianCalendar(dateFormat
                            .parse("2008-08-04 18:28:01 345")));
        } catch (Exception e) {
        }

        String resultCode = new String();
        Holder<ListOfTransactionLogs> holderForListOfTransactionLogs = new Holder<ListOfTransactionLogs>();
        Holder<String> holderForRetValue = new Holder<String>();
        service.getDRASTransactions(ids, ids, ids, methodName, interval,
                resultCode, holderForListOfTransactionLogs, holderForRetValue);
        ListOfTransactionLogs logs = holderForListOfTransactionLogs.value;
        PrintTool.printTransactionLogs(logs);
        System.out
                .println("######## End getDRASTransactions ######################################");

        System.out
                .println("######## Start getDRASClientCommsStatus ######################################");
        Holder<ListOfCommsStatus> holderForListOfCommsStatus = new Holder<ListOfCommsStatus>();
        holderForRetValue = new Holder<String>();
        service.getDRASClientCommsStatus(new ParticipantList(),
                new ListOfProgramNames(), holderForListOfCommsStatus,
                holderForRetValue);
        ListOfCommsStatus listOfStuts = holderForListOfCommsStatus.value;
        PrintTool.printListOfCommsStatus(listOfStuts);

        ListOfProgramNames progs = new ListOfProgramNames();
        progs.getProgramID().add(programName1);
        Holder<ListOfCommsStatus> holderForlistOfStuts = new Holder<ListOfCommsStatus>();
        holderForRetValue = new Holder<String>();
        service.getDRASClientCommsStatus(new ParticipantList(), progs,
                holderForlistOfStuts, holderForRetValue);
        listOfStuts = holderForlistOfStuts.value;
        PrintTool.printListOfCommsStatus(listOfStuts);
        System.out
                .println("######## End getDRASClientCommsStatus ######################################");

        System.out
                .println("######## Start getDRASClientAlarms ######################################");
        ListOfIDs ids1 = new ListOfIDs();
        ListOfIDs ids3 = new ListOfIDs();
        ids3.getIdentifier().add(programName2);
        DateTimeWindow window = new DateTimeWindow();
        Date startTime = new Date((new Date()).getTime() - 600000);
        window.setStartDateTime(PrintTool
                .converDateToXMLGregorianCalendar(startTime));
        window.setEndDateTime(PrintTool
                .converDateToXMLGregorianCalendar(new Date()));
        Holder<ListOfDRASClientAlarms> holderForListOfDRASClientAlarms = new Holder<ListOfDRASClientAlarms>();
        holderForRetValue = new Holder<String>();
        service.getDRASClientAlarms(ids1, ids3, window,
                holderForListOfDRASClientAlarms, holderForRetValue);
        ListOfDRASClientAlarms alarms = holderForListOfDRASClientAlarms.value;
        PrintTool.printListOfDRASClientAlarms(alarms);
        System.out
                .println("######## End getDRASClientAlarms ######################################");

    }

    /**
     * Test event.
     * 
     * @param programName
     *            the program name
     * @param participantID
     *            the participant id
     */
    private void testEvent(String programName, String participantID) {
        testInitEvent(programName, participantID);

        System.out
                .println("######## Start getDREventInformation ######################################");
        String progName = programName;
        ListOfProgramNames prognames = new ListOfProgramNames();
        prognames.getProgramID().add(progName);
        Holder<ListOfUtilityDREvents> holderForListOfUtilityDREvents = new Holder<ListOfUtilityDREvents>();
        Holder<String> holderForRetValue = new Holder<String>();
        service.getDREventInformation(new ListOfEventIDs(), prognames,
                new ParticipantList(), holderForListOfUtilityDREvents,
                holderForRetValue);
        PrintTool
                .printListOfUtilityDREvents(holderForListOfUtilityDREvents.value);
        ListOfUtilityDREvents drEvents = holderForListOfUtilityDREvents.value;
        System.out
                .println("######## End getDREventInformation ######################################");

        if (drEvents != null && drEvents.getDREvent() != null
                && drEvents.getDREvent().size() > 0) {
            System.out
                    .println("######## Start getEventConstraint ######################################");

            String eventID = drEvents.getDREvent().get(0).getEventIdentifier();

            Holder<ProgramConstraint> holderForProgramConstraint = new Holder<ProgramConstraint>();
            holderForRetValue = new Holder<String>();
            service.getEventConstraint(eventID, holderForProgramConstraint,
                    holderForRetValue);
            ProgramConstraint constraint = holderForProgramConstraint.value;
            PrintTool.printProgramConstraint(constraint);
            System.out
                    .println("######## End getEventConstraint ######################################");

            System.out
                    .println("######## Start setEventConstraint ######################################");
            constraint.setMaxEventDuration(10000.99);

            String ret = service.setEventConstraint(eventID, constraint);
            System.out.println(ret);
            System.out
                    .println("######## End setEventConstraint ######################################");

            testBid(progName, eventID);

            System.out
                    .println("######## Start modifyDREvent ######################################");
            String eventType = "CANCEL";
            Holder<ConstraintFilter> holderForConstraintFilter = new Holder<ConstraintFilter>();
            holderForRetValue = new Holder<String>();
            service.modifyDREvent(eventID, eventType, drEvents.getDREvent()
                    .get(0), holderForRetValue, holderForConstraintFilter);
            System.out.println(holderForConstraintFilter.value.value());
            System.out
                    .println("######## End modifyDREvent ######################################");
        }

        Holder<ListOfEventStates> holderForListOfEventStates = new Holder<ListOfEventStates>();
        holderForRetValue = new Holder<String>();

        service.getDRASEventStates(null, prognames, null, null,
                holderForListOfEventStates, holderForRetValue);

        System.out
                .println("######## Start getDREventInformation ######################################");
        Holder<ListOfUtilityDREvents> drEventList = new Holder<ListOfUtilityDREvents>();
        holderForRetValue = new Holder<String>();
        service.getDREventInformation(new ListOfEventIDs(),
                new ListOfProgramNames(), new ParticipantList(), drEventList,
                holderForRetValue);
        drEvents = drEventList.value;
        PrintTool.printListOfUtilityDREvents(drEvents);
        System.out
                .println("######## End getDREventInformation ######################################");
    }

    /**
     * Test bid.
     * 
     * @param progName
     *            the prog name
     * @param eventName
     *            the event name
     */
    private void testBid(String progName, String eventName) {
        System.out.println("Start testBid");

        System.out
                .println("######## Start getCurrentBids ######################################");

        Holder<ListOfBids> holderForBids = new Holder<ListOfBids>();
        Holder<Boolean> holderForStatus = new Holder<Boolean>();
        ListOfBids bidList = new ListOfBids();
        Boolean bol = new Boolean(false);
        holderForBids.value = bidList;
        holderForStatus.value = bol;

        Holder<String> holderForRetValue = new Holder<String>();
        service.getCurrentBids(eventName, progName, holderForBids,
                holderForStatus, holderForRetValue);

        ListOfBids bids = holderForBids.value;
        Boolean status = holderForStatus.value;

        String statusStr = status.booleanValue() ? "open" : "closed";
        System.out.println("Bid as following for " + progName + " and event "
                + eventName + " is " + statusStr);
        PrintTool.printListOfBids(bids);
        System.out
                .println("######## End getCurrentBids ######################################");

        System.out
                .println("######## Start setBidStatus ######################################");
        ListOfParticipantAccountIDs rejectIDs = new ListOfParticipantAccountIDs();
        if (bids.getBids().size() > 0) {
            rejectIDs.getParticipantAccountID().add(
                    bids.getBids().get(0).getParticipantAccount());

            String ret = service.setBidStatus(eventName,
                    new ListOfUtilityDREvents(), rejectIDs);
            System.out.println(ret);
        }
        System.out
                .println("######## End setBidStatus ######################################");
    }

    /**
     * Test group.
     */
    private void testGroup() {
        System.out
                .println("######## Start getGroups ######################################");
        // ListOfIDs ids = service.getGroups();
        // PrintTool.printListOfIDs(ids);
        System.out
                .println("######## End getGroups ######################################");
    }

    /**
     * Gets the event name.
     * 
     * @return the event name
     */
    public static String getEventName() {
        final Date now = new Date();
        final SimpleDateFormat format = new SimpleDateFormat("yyMMdd-HHmmss");
        return format.format(now);
    }

    /**
     * The Class SimpleAuthenticator.
     */
    private class SimpleAuthenticator extends Authenticator {

        /*
         * (non-Javadoc)
         * 
         * @see java.net.Authenticator#getPasswordAuthentication()
         */
        public PasswordAuthentication getPasswordAuthentication() {
            System.out.println("Authenticating");
            return new PasswordAuthentication("u", "v".toCharArray());
        }
    }
}
