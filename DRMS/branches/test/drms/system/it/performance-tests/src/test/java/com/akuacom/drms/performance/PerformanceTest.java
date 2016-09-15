package com.akuacom.drms.performance;

import junit.framework.Assert;

import org.junit.Test;

public class PerformanceTest {

    private static final String HOST_NAME = "localhost";

    private static final String USER_NAME = "a";

    private static final String USER_PWD = "b";

    private static final String PROGRAM_NAME = "CPP";

    private static final String PARTICIPANT_PREFIX = "perf";

    private static final String CLIENT_PWD = "Test_1234";

    private static final int NUMBER_OF_PARTICIPANTS = 500;

    private ParticipantGenerator g = null;

    @Test
    public void testPerformance() throws Exception {
        g = new ParticipantGenerator(HOST_NAME, USER_NAME, USER_PWD,
                new String[] { PROGRAM_NAME }, true);
        Assert.assertNotNull(g);

        testParticipantGeneration();
        testEventCreation();
    }

    private void testEventCreation() throws Exception {
        System.out.println("Creating Event");
        long startTime = System.currentTimeMillis();
        g.createEvent(PROGRAM_NAME);
        long eventTime = System.currentTimeMillis();
        long result = eventTime - startTime;
        System.out.println(PROGRAM_NAME + "Event Creation with  "
                + NUMBER_OF_PARTICIPANTS + " Participants subscribed to "
                + PROGRAM_NAME + " program took - " + result + " msec");
        Assert.assertTrue(result < 130000);
    }

    private void testParticipantGeneration() throws Exception {

        System.out.println("Creating Participants");
        long startTime = System.currentTimeMillis();
        g.generateParticipants(PARTICIPANT_PREFIX, PARTICIPANT_PREFIX,
                CLIENT_PWD, NUMBER_OF_PARTICIPANTS);
        long partTime = System.currentTimeMillis();
        long result = partTime - startTime;
        long singleResult = result / NUMBER_OF_PARTICIPANTS;
        System.out.println("Creating " + NUMBER_OF_PARTICIPANTS
                + " Participants took - " + result + " msec");
        System.out.println("Average time to create a Participant  - "
                + singleResult + " msec");
        Assert.assertTrue(singleResult < 10000);
    }
}
