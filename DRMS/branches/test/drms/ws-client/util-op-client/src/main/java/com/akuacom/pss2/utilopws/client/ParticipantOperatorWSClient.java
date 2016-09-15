/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.utilopws.client.ParticipantOperatorWSClient.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.utilopws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;
import org.openadr.dras.bid.Bid;
import org.openadr.dras.drasclient.DRASClient;
import org.openadr.dras.drasclient.ListOfCommsStatus;
import org.openadr.dras.drasclient.ListOfDRASClients;
import org.openadr.dras.eventinfo.ListOfEventInfoType;
import org.openadr.dras.eventstate.ListOfEventStates;
import org.openadr.dras.feedback.ListOfFeedback;
import org.openadr.dras.logs.ListOfDRASClientAlarms;
import org.openadr.dras.logs.ListOfTransactionLogs;
import org.openadr.dras.optoutstate.ListOfOptOutStates;
import org.openadr.dras.optoutstate.OptOutState;
import org.openadr.dras.participantaccount.ContactInformation;
import org.openadr.dras.participantaccount.ListOfParticipantAccountIDs;
import org.openadr.dras.participantaccount.ListOfParticipantAccounts;
import org.openadr.dras.participantaccount.ParticipantAccount;
import org.openadr.dras.participantoperator.ParticipantOperator;
import org.openadr.dras.participantoperator.ParticipantOperator_Service;
import org.openadr.dras.programconstraint.DateTimeWindow;
import org.openadr.dras.programconstraint.ListOfProgramConstraints;
import org.openadr.dras.programconstraint.ProgramConstraint;
import org.openadr.dras.responseschedule.OperationStateSpec;
import org.openadr.dras.responseschedule.ResponseSchedule;
import org.openadr.dras.utilityprogram.ListOfIDs;
import org.openadr.dras.utilityprogram.ListOfProgramNames;
import org.openadr.dras.utilityprogram.ParticipantList;
import org.openadr.dras.utilityprogram.ProgramInfo;

/**
 * The Class ParticipantOperatorWSClient.
 */
public class ParticipantOperatorWSClient {
    // singleton
    /** The Constant INSTANCE. */
    public static final ParticipantOperatorWSClient INSTANCE = new ParticipantOperatorWSClient();

    // version number
    /** The Constant VERSION_NUMBER. */
    private static final String VERSION_NUMBER = "0.1";

    // default configuration file name
    /** The Constant DEFAULT_CONFIG_FILE_NAME. */
    private static final String DEFAULT_CONFIG_FILE_NAME = "ParticipantOperatorWSClient.conf";

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
    private static final String DEFAULT_END_POINT_PATH = "ParticipantOperatorWS/ParticipantOperatorWS";

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
    private ParticipantOperator service = null;

    // configuration properties
    /** The config. */
    private Properties config = null;

    /** The program name. */
    private static String programName = "Test";

    /**
     * Instantiates a new participant operator ws client.
     */
    private ParticipantOperatorWSClient() {
    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        INSTANCE.initialize(args);
        INSTANCE.test();
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

        endPoint = "http://";

        endPoint += config.getProperty(PROP_END_POINT_HOST) + ":"
                + config.getProperty(PROP_END_POINT_PORT) + "/"
                + config.getProperty(PROP_END_POINT_PATH);

        logger.debug("endpoint = " + endPoint);

        Authenticator.setDefault(new SimpleAuthenticator());

        // create stub
        try {
            service = new ParticipantOperator_Service(new URL(endPoint
                    + "?wsdl"), new QName(
                    "http://www.openadr.org/DRAS/ParticipantOperator/",
                    "ParticipantOperatorWSService"))
                    .getParticipantOperatorPort();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
     * Test.
     */
    private void test() {
        System.out
                .println("######## Start getProgramInformation ######################################");

        programName = config.getProperty("programName");
        Holder<ListOfEventInfoType> holderForList = new Holder<ListOfEventInfoType>();
        Holder<ProgramConstraint> holderForProgConstraint = new Holder<ProgramConstraint>();
        ListOfEventInfoType list = new ListOfEventInfoType();
        ProgramConstraint constraint = new ProgramConstraint();
        holderForList.value = list;
        holderForProgConstraint.value = constraint;
        Holder<String> holderForRetValue = new Holder<String>();
        service.getProgramInformation("DEMO", "", holderForProgConstraint,
                holderForList, holderForRetValue);
        service.getProgramInformation("DEMO", null, holderForProgConstraint,
                holderForList, holderForRetValue);
        service.getProgramInformation(programName, "", holderForProgConstraint,
                holderForList, holderForRetValue);

        list = holderForList.value;
        constraint = holderForProgConstraint.value;

        PrintTool.printProgramConstraint(constraint);
        PrintTool.printListOfEventInfoType(list);

        System.out
                .println("######## End getProgramInformation ######################################");

        testDRASClient(constraint);
    }

    /**
     * Test dras client.
     * 
     * @param constraint
     *            the constraint
     */
    private void testDRASClient(ProgramConstraint constraint) {
        System.out
                .println("######## Start createDRASClient ######################################");

        DRASClient drasClient = new DRASClient();
        drasClient.setClientType("SIMPLE");
        drasClient.setIdentifier("test_client_1");
        drasClient.setOnLine(true);
        drasClient.setParticipantID("test_part_1");
        DRASClient.Programs programs = new DRASClient.Programs();
        ProgramInfo progInfo = new ProgramInfo();
        progInfo.setProgramName(programName);
        progInfo.setProgramConstraints(constraint);
        programs.getProgram().add(progInfo);
        drasClient.setPrograms(programs);

        String ret = service.createDRASClient(drasClient);
        System.out.println(ret);

        DRASClient drasClient2 = new DRASClient();
        drasClient2.setClientType("SMART");
        drasClient2.setIdentifier("test_client_2");
        drasClient2.setOnLine(true);
        drasClient2.setParticipantID("test_part_2");
        drasClient2.setPrograms(programs);

        ret = service.createDRASClient(drasClient2);
        System.out.println(ret);

        System.out
                .println("######## End createDRASClient ######################################");

        System.out
                .println("######## Start modifyDRASClient ######################################");

        drasClient.setClientType("SMART");
        ret = service.modifyDRASClient(drasClient);
        System.out.println(ret);

        drasClient2.setClientType("SIMPLE");
        ret = service.modifyDRASClient(drasClient2);
        System.out.println(ret);

        System.out
                .println("######## End modifyDRASClient ######################################");

        System.out
                .println("######## Start getDRASClientInfo ######################################");
        ListOfIDs drasClientIDs = new ListOfIDs();
        drasClientIDs.getIdentifier().add("test_client_1");
        drasClientIDs.getIdentifier().add("test_client_2");
        ListOfParticipantAccountIDs partIDs = new ListOfParticipantAccountIDs();
        partIDs.getParticipantAccountID().add("test_client_1");
        partIDs.getParticipantAccountID().add("test_client_2");
        Holder<ListOfDRASClients> holderForListOfDRASClients = new Holder<ListOfDRASClients>();
        Holder<String> holderForRetValue = new Holder<String>();
        service.getDRASClientInfo(drasClientIDs, partIDs, "",
                holderForListOfDRASClients, holderForRetValue);
        ListOfDRASClients dRASClientInfo = holderForListOfDRASClients.value;
        PrintTool.printListOfDRASClients(dRASClientInfo);
        service.getDRASClientInfo(null, null, null, holderForListOfDRASClients,
                holderForRetValue);
        dRASClientInfo = holderForListOfDRASClients.value;
        PrintTool.printListOfDRASClients(dRASClientInfo);
        System.out
                .println("######## End getDRASClientInfo ######################################");

        System.out
                .println("######## Start getDRASClientProgramConstraints ######################################");
        Holder<ListOfProgramConstraints> holderForListOfProgramConstraints = new Holder<ListOfProgramConstraints>();
        holderForRetValue = new Holder<String>();
        service.getDRASClientProgramConstraints(drasClientIDs, partIDs, "",
                holderForListOfProgramConstraints, holderForRetValue);
        ListOfProgramConstraints constraints = holderForListOfProgramConstraints.value;
        PrintTool.printListOfProgramConstraints(constraints);

        System.out
                .println("######## End getDRASClientProgramConstraints ######################################");

        System.out
                .println("######## Start setDRASClientProgramConstraints ######################################");
        // get one constraint;
        if (constraints != null
                && constraints.getProgramConstraint().size() > 0) {
            ProgramConstraint constraintForMod = constraints
                    .getProgramConstraint().get(0);
            constraintForMod.setMaxEventDuration(10000.99);
            String drasClientID = constraintForMod.getDRASClientID();
            ListOfIDs inputIDs = new ListOfIDs();
            inputIDs.getIdentifier().add(drasClientID);
            String partID = constraintForMod.getParticipantID();
            ListOfParticipantAccountIDs partIDs2 = new ListOfParticipantAccountIDs();
            partIDs2.getParticipantAccountID().add(partID);

            ret = service.setDRASClientProgramConstraints(inputIDs, partIDs,
                    "", constraintForMod);
            System.out.println(ret);
        }
        System.out
                .println("######## End setDRASClientProgramConstraints ######################################");

        System.out
                .println("######## Start createResponseSchedule ######################################");
        ResponseSchedule resSchedule = new ResponseSchedule();
        resSchedule.setDRASClientID("test_client_1");
        resSchedule.setIdentifier("test_client_1");
        resSchedule.setNearTransitionTime(100.9);
        resSchedule.setProgramName(programName);

        ResponseSchedule.OperationStates states = new ResponseSchedule.OperationStates();
        resSchedule.setOperationStates(states);
        OperationStateSpec spec = new OperationStateSpec();
        double startTime = (new Date()).getTime() / 1000;
        spec.setStartTime(startTime);
        // OperationStateSpec.Rules rules = new OperationStateSpec.Rules();
        // rules.getRule().add(new Rule());

        // spec.setRules(rules);
        states.getStateSpec().add(spec);
        ret = service.createResponseSchedule(resSchedule);
        System.out.println(ret);

        System.out
                .println("######## End createResponseSchedule ######################################");

        System.out
                .println("######## Start getResponseSchedule ######################################");
        Holder<ResponseSchedule> holderForResponseSchedule = new Holder<ResponseSchedule>();
        holderForRetValue = new Holder<String>();
        service.getResponseSchedule(programName, "test_client_1",
                holderForResponseSchedule, holderForRetValue);
        ResponseSchedule retResSche = holderForResponseSchedule.value;
        PrintTool.printResponseSchedule(retResSche);

        System.out
                .println("######## End getResponseSchedule ######################################");

        System.out
                .println("######## Start getDREventFeedback ######################################");
        ListOfIDs ids1 = new ListOfIDs();
        ListOfIDs ids2 = new ListOfIDs();
        ListOfIDs ids3 = new ListOfIDs();
        ids2.getIdentifier().add("test_client_1");
        ids1.getIdentifier().add("test_client_1");
        ids3.getIdentifier().add(programName);
        DateTimeWindow window = new DateTimeWindow();
        Date startTime1 = new Date((new Date()).getTime() - 600000);
        window.setStartDateTime(PrintTool
                .converDateToXMLGregorianCalendar(startTime1));
        window.setEndDateTime(PrintTool
                .converDateToXMLGregorianCalendar(new Date()));
        Holder<ListOfFeedback> holderForListOfFeedback = new Holder<ListOfFeedback>();
        holderForRetValue = new Holder<String>();
        service.getDREventFeedback(ids1, ids2, ids3, window, "",
                holderForListOfFeedback, holderForRetValue);
        ListOfFeedback feedbacks = holderForListOfFeedback.value;
        PrintTool.printListOfFeedback(feedbacks);
        System.out
                .println("######## End getDREventFeedback ######################################");

        System.out
                .println("######## Start setDREventFeedback ######################################");
        if (feedbacks != null && feedbacks.getFeedback().size() > 0) {
            feedbacks.getFeedback().get(0).getFeedback()
                    .setValue("Test Feedback");
            ret = service.setDREventFeedback(feedbacks.getFeedback().get(0));
            System.out.println(ret);
        }
        System.out
                .println("######## End setDREventFeedback ######################################");

        System.out
                .println("######## Start setTestMode ######################################");
        ret = service.setTestMode("", "TRUE");
        System.out.println(ret);
        System.out
                .println("######## End setTestMode ######################################");

        System.out
                .println("######## Start getTestModeState ######################################");
        Holder<String> holderForState = new Holder<String>();
        holderForRetValue = new Holder<String>();
        service.getTestModeState("test_client_1", holderForState,
                holderForRetValue);
        ret = holderForState.value;
        System.out.println(ret);
        System.out
                .println("######## End getTestModeState ######################################");

        System.out
                .println("######## Start setTestModeState ######################################");
        ret = service.setTestModeState("test_client_1", "MEDIUM", "FAR");
        System.out.println(ret);
        System.out
                .println("######## End setTestModeState ######################################");

        testParticipant();
        testTransactionLog(dRASClientInfo);

        System.out
                .println("######## Start deleteDRASClientProgramConstraints ######################################");
        drasClientIDs = new ListOfIDs();
        drasClientIDs.getIdentifier().add("test_client_1");
        drasClientIDs.getIdentifier().add("test_client_2");
        partIDs = new ListOfParticipantAccountIDs();
        partIDs.getParticipantAccountID().add("test_part_1");
        partIDs.getParticipantAccountID().add("test_part_2");
        ret = service.deleteDRASClientProgramConstraints(drasClientIDs,
                partIDs, "", programName);
        System.out.println(ret);
        System.out
                .println("######## End deleteDRASClientProgramConstraints ######################################");

        System.out
                .println("######## Start deleteResponseSchedule ######################################");
        ret = service.deleteResponseSchedule(programName, "test_client_1");
        System.out
                .println("######## End deleteResponseSchedule ######################################");

        System.out
                .println("######## Start deleteDRASClient ######################################");

        ret = service.deleteDRASClient(drasClient.getIdentifier());
        System.out.println(ret);

        ret = service.deleteDRASClient(drasClient2.getIdentifier());
        System.out.println(ret);

        System.out
                .println("######## End deleteDRASClient ######################################");
    }

    /**
     * Test participant.
     */
    private void testParticipant() {
        System.out
                .println("######## Start getParticipantAccounts ######################################");
        ListOfParticipantAccountIDs ids = new ListOfParticipantAccountIDs();
        ids.getParticipantAccountID().add("test_part_1");
        ids.getParticipantAccountID().add("test_part_2");
        Holder<ListOfParticipantAccounts> holderForListOfParticipantAccounts = new Holder<ListOfParticipantAccounts>();
        Holder<String> holderForRetValue = new Holder<String>();
        service.getParticipantAccounts(ids, "",
                holderForListOfParticipantAccounts, holderForRetValue);
        ListOfParticipantAccounts accounts = holderForListOfParticipantAccounts.value;
        PrintTool.printParticipantAccounts(accounts);
        System.out
                .println("######## End getParticipantAccounts ######################################");

        System.out
                .println("######## Start modifyParticipantAccounts ######################################");

        ParticipantAccount pa3 = new ParticipantAccount();
        pa3.setUserName("test_part_2");
        pa3.setAccountID("test_part_2");
        ContactInformation ci2 = new ContactInformation();
        ContactInformation.VoiceNumbers eas2 = new ContactInformation.VoiceNumbers();
        eas2.getNumber().add("555-555-5555");
        ci2.setVoiceNumbers(eas2);
        pa3.setContactInformation(ci2);
        pa3.setParticipantName(programName);

        String ret = service.modifyParticipantAccounts(ids, "", pa3);
        System.out.println(ret);
        System.out
                .println("######## End modifyParticipantAccounts ######################################");

        System.out
                .println("######## Start getParticipantProgramConstraints ######################################");
        Holder<ListOfProgramConstraints> holderForListOfProgramConstraints = new Holder<ListOfProgramConstraints>();
        holderForRetValue = new Holder<String>();
        service.getParticipantProgramConstraints(ids, programName, "",
                holderForListOfProgramConstraints, holderForRetValue);
        ListOfProgramConstraints constraints = holderForListOfProgramConstraints.value;
        PrintTool.printListOfProgramConstraints(constraints);
        System.out
                .println("######## End getParticipantProgramConstraints ######################################");

        System.out
                .println("######## Start setParticipantProgramConstraints ######################################");
        if (constraints != null
                && constraints.getProgramConstraint().size() > 0) {
            ProgramConstraint constraint = constraints.getProgramConstraint()
                    .get(0);
            constraint.setMaxEventDuration(10000.99);
            ret = service.setParticipantProgramConstraints(ids, "", constraint);
            System.out.println(ret);
        }
        System.out
                .println("######## End setParticipantProgramConstraints ######################################");

        System.out
                .println("######## Start getDRASClientCommsStatus ######################################");
        ParticipantList plist = new ParticipantList();
        ParticipantList.Accounts accts = new ParticipantList.Accounts();
        accts.getParticipantID().add("test_part_1");
        plist.setAccounts(accts);

        ListOfProgramNames progs = new ListOfProgramNames();
        progs.getProgramID().add(programName);
        Holder<ListOfCommsStatus> holderForListOfCommsStatus = new Holder<ListOfCommsStatus>();
        holderForRetValue = new Holder<String>();
        service.getDRASClientCommsStatus(plist, progs,
                holderForListOfCommsStatus, holderForRetValue);
        ListOfCommsStatus status = holderForListOfCommsStatus.value;
        PrintTool.printListOfCommsStatus(status);
        System.out
                .println("######## End getDRASClientCommsStatus ######################################");

        testBid();

        System.out
                .println("######## Start deleteParticipantProgramConstraints ######################################");
        ret = service.deleteParticipantProgramConstraints(ids, "", programName);
        System.out.println(ret);
        System.out
                .println("######## End deleteParticipantProgramConstraints ######################################");
    }

    /**
     * Test bid.
     */
    private void testBid() {
        System.out
                .println("######## Start getStandingBid ######################################");
        Holder<Bid> holderForBid = new Holder<Bid>();
        Holder<String> holderForRetValue = new Holder<String>();
        service.getStandingBid("test_part_1", programName, holderForBid,
                holderForRetValue);
        Bid bid = holderForBid.value;
        PrintTool.printBid(bid);
        System.out
                .println("######## End getStandingBid ######################################");

        System.out
                .println("######## Start submitStandingBid ######################################");
        if (bid != null) {
            String ret = service.submitStandingBid(bid);
            System.out.println(ret);
        }
        System.out
                .println("######## End submitStandingBid ######################################");

        System.out
                .println("######## Start deleteStandingBid ######################################");
        String ret = service.deleteStandingBid("test_part_1", programName);
        System.out.println(ret);
        System.out
                .println("######## End deleteStandingBid ######################################");

        System.out
                .println("######## Start getBid ######################################");
        holderForBid = new Holder<Bid>();
        holderForRetValue = new Holder<String>();
        service.getBid("test_part_1", programName, holderForBid,
                holderForRetValue);
        bid = holderForBid.value;
        PrintTool.printBid(bid);
        System.out
                .println("######## End getBid ######################################");

        System.out
                .println("######## Start submitBid ######################################");
        if (bid != null) {
            ret = service.submitBid(bid);
            System.out.println(ret);
        }
        System.out
                .println("######## End submitBid ######################################");

        System.out
                .println("######## Start createOptOutState ######################################");
        OptOutState optOutState = new OptOutState();
        OptOutState.DRASClients clients = new OptOutState.DRASClients();
        clients.getDRASClientID().add("test_client_1");

        optOutState.setDRASClients(clients);

        String eventID = "";
        if (bid != null)
            bid.getEventID();

        optOutState.setEventID(eventID);
        optOutState.setIdentifier("test_part_1");
        optOutState.setParticipantID("test_part_1");
        optOutState.setProgramName(programName);
        OptOutState.Schedule schedule = new OptOutState.Schedule();
        OptOutState.Schedule.TimeElement ele = new OptOutState.Schedule.TimeElement();
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 3600000);
        ele.setStart(PrintTool.converDateToXMLGregorianCalendar(startDate));
        ele.setEnd(PrintTool.converDateToXMLGregorianCalendar(endDate));
        schedule.getTimeElement().add(ele);
        optOutState.setSchedule(schedule);
        ret = service.createOptOutState(optOutState);
        System.out
                .println("######## End createOptOutState ######################################");

        System.out
                .println("######## Start getOptOutStates ######################################");
        if (eventID == null)
            eventID = "";
        Holder<ListOfOptOutStates> holderForListOfOptOutStates = new Holder<ListOfOptOutStates>();
        holderForRetValue = new Holder<String>();
        service.getOptOutStates("test_part_1", programName, "test_client_1",
                "", eventID, holderForListOfOptOutStates, holderForRetValue);
        ListOfOptOutStates ops = holderForListOfOptOutStates.value;
        PrintTool.printListOfOptOutStates(ops);
        System.out
                .println("######## End getOptOutStates ######################################");

        System.out
                .println("######## Start deleteOptOutState ######################################");
        if (ops != null && ops.getOptOutState() != null
                && ops.getOptOutState().size() > 0) {
            ret = service.deleteOptOutState(ops.getOptOutState().get(0)
                    .getIdentifier());
            System.out.println(ret);
        }
        System.out
                .println("######## End deleteOptOutState ######################################");

    }

    /**
     * Test transaction log.
     * 
     * @param list
     *            the list
     */
    private void testTransactionLog(ListOfDRASClients list) {
        System.out
                .println("######## Start getDRASTransactions ######################################");
        ListOfIDs drasClientIDs = new ListOfIDs();
        drasClientIDs.getIdentifier().add("test_client_1");
        drasClientIDs.getIdentifier().add("test_client_2");
        ListOfIDs partIDs = new ListOfIDs();
        partIDs.getIdentifier().add("test_part_1");
        ListOfIDs userNames = new ListOfIDs();
        userNames.getIdentifier().add("test_part_1");
        DateTimeWindow window = new DateTimeWindow();
        Date startTime1 = new Date((new Date()).getTime() - 600000);
        window.setStartDateTime(PrintTool
                .converDateToXMLGregorianCalendar(startTime1));
        window.setEndDateTime(PrintTool
                .converDateToXMLGregorianCalendar(new Date()));
        Holder<ListOfTransactionLogs> holderForListOfTransactionLogs = new Holder<ListOfTransactionLogs>();
        Holder<String> holderForRetValue = new Holder<String>();
        service.getDRASTransactions(userNames, drasClientIDs, partIDs, "",
                window, "SUCCESSFUL", holderForListOfTransactionLogs,
                holderForRetValue);
        ListOfTransactionLogs logs = holderForListOfTransactionLogs.value;
        PrintTool.printTransactionLogs(logs);
        System.out
                .println("######## End getDRASTransactions ######################################");

        String progName = programName;
        ListOfProgramNames prognames = new ListOfProgramNames();
        prognames.getProgramID().add(progName);
        Holder<ListOfEventStates> holderForListOfEventStates = new Holder<ListOfEventStates>();
        holderForRetValue = new Holder<String>();

        service.getDRASEventStates(null, prognames, null, null,
                holderForListOfEventStates, holderForRetValue);

        System.out
                .println("######## Start getDRASClientAlarms ######################################");
        Holder<ListOfDRASClientAlarms> holderForListOfDRASClientAlarms = new Holder<ListOfDRASClientAlarms>();
        holderForRetValue = new Holder<String>();
        service.getDRASClientAlarms(drasClientIDs, partIDs, window,
                holderForListOfDRASClientAlarms, holderForRetValue);
        ListOfDRASClientAlarms alarms = holderForListOfDRASClientAlarms.value;
        PrintTool.printListOfDRASClientAlarms(alarms);
        System.out
                .println("######## End getDRASClientAlarms ######################################");
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
            return new PasswordAuthentication("p", "q".toCharArray());
        }
    }
}