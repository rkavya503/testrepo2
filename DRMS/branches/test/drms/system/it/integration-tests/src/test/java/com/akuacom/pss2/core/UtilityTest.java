package com.akuacom.pss2.core;

import org.junit.Ignore;
import org.junit.Test;

/**
 * this class is intended to generate some basic accounts after db cleanups.
 *
 * @auther Dichen Mao
 */
import com.akuacom.pss2.participant.ParticipantUtil;
import com.akuacom.pss2.participant.Participant;

public class UtilityTest extends ParticipantManagerFixture {
    public static final String DEFAULT_PASSWORD = "Test_1234";

    @Ignore
    @Test
    public void sceAccountSetup() {
        createParticipant("CPP", 0);
        createParticipant("DBP DA", 1);
        createParticipant("DBP DA", 2);
        createParticipant("DBP DO", 3);
        createParticipant("DEMO", 4);
        createParticipant("DBP DA", 5);
    }

    @Ignore
    @Test
    public void pgeAccountSetup() {
        createParticipant("CPP", 0);
        createParticipant("DBP DO Single", 1);
        createParticipant("DBP DA Single", 2);
        createParticipant("PeakChoice", 3);
        createParticipant("PeakChoice", 4);
        createParticipant("PeakChoice", 5);
        createParticipant("CPP Test", 6);
        createParticipant("DBP Test Single", 7);
    }

    @Ignore
    @Test
    public void pgePcAccountSetup() {
        createParticipant("PeakChoice", "LARKSPUR", "9052139003");
        createParticipant("PeakChoice", "MENLO PARK", "1668421001");
        createParticipant("PeakChoice", "SAN JOSE", "9638068336");
        createParticipant("PeakChoice", "SAN FRANCISCO", "0679857279");
        createParticipant("PeakChoice", "BAKERSFIELD", "1752326273");
        createParticipant("PeakChoice", "ANTIOCH", "7585768873");
    }

    private void createParticipant(String programName, String participantName, String accountNumber) {
        final Participant p = ParticipantUtil.getParicipantInstance(participantName);
        p.setAccountNumber(accountNumber);
        pm.createParticipant(p, DEFAULT_PASSWORD.toCharArray(), null);
        programParticipantManager.addParticipantToProgram(programName, participantName, false);
        // create part0.c1
        createClient(programName, participantName, 1);
        createClient(programName, participantName, 2);
    }

    private void createParticipant(String programName, int index) {
        final Participant p = ParticipantUtil.getParicipantInstance(index);
        final String participantName = p.getUser();
        pm.createParticipant(p, DEFAULT_PASSWORD.toCharArray(), null);
        programParticipantManager.addParticipantToProgram(programName, participantName, false);
        // create part0.c1
        createClient(programName, participantName, 1);
        createClient(programName, participantName, 2);
    }

    private void createClient(String programName, String parentName, int index) {
        Participant c = ParticipantUtil.getParicipantInstance(0);
        c.setUser(parentName + ".c" + index);
        c.setAccountNumber(c.getUser());
        c.setParent(parentName);
        c.setClient(true);
        pm.createParticipant(c, DEFAULT_PASSWORD.toCharArray(), null);
        programParticipantManager.addParticipantToProgram(programName, c.getUser(), true);
    }
}
