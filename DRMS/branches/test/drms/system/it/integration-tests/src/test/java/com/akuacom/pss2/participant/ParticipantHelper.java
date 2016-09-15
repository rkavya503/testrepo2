package com.akuacom.pss2.participant;

import java.util.Date;
import java.util.LinkedHashSet;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.participant.ProgramParticipant;

public class ParticipantHelper {

	/**
	 * Default Client PAssword
	 */
	protected static final String DEFAULT_CLIENT_PASSWORD = "Test_1234";
	
	

	/**
	 * Default Client Prefix
	 */
	protected static final String DEFAULT_CLIENT_PRIFIX = "test";
	

	/**
	 * Default Participant Prefix
	 */
	protected static final String DEFAULT_PARTICIPANT_PRIFIX = "test";
	
	/**
	 * Default Number of participants
	 */
	protected static final int DEFAULT_NUMBER_OF_PARTICIPANTS = 1;

	/**
	 * Default Program Name
	 */
	protected static final String DEFAULT_PROGRAM_NAME = "RTP Agricultural";
	
	
	public Participant createParticipant(String name, boolean isClient, String parent) {
		Participant participant = new Participant();
    	participant.setAccountNumber(name);
    	participant.setActivated(true);
    	participant.setDataEnabler(true);
    	participant.setClient(isClient);
    	participant.setCreationTime(new Date());
    	participant.setCreator("perfTest");
    	participant.setFeedback(false);
    	participant.setLatitude(0.0);
    	participant.setLongitude(0.0);
    	participant.setShedPerHourKW(0.0);
//    	participant.setLastPrice(-1);
    	participant.setManualControl(false);
//    	participant.setOfflineWarning(true);
    	participant.setParticipantName(name);
//    	participant.setStatus(2);
//    	participant.setTestAccount(false);
    	participant.setType((byte) 0);
    	participant.setVersion(0);
    	participant.setProgramParticipants(new LinkedHashSet<ProgramParticipant>());
    	
    	if (isClient) {
    		participant.setParent(parent);
    	}
    	
    	return participant;
	}

}
