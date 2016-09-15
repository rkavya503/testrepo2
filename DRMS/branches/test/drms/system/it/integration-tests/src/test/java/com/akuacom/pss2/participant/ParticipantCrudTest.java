package com.akuacom.pss2.participant;


import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventInfo;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.usecasetest.cases.AbstractUseCase;


public class ParticipantCrudTest extends AbstractUseCase{

	
	private ParticipantManager pm = null;
	private ProgramParticipantManager ppm = null;
	private ClientManager cm = null;
	private ProgramManager progMgr = null;


	public Object runCase() throws Exception{
		return null;
	}
	
	@Before public void initialize(){
		pm = getPartMgr();
		ppm = getProgPartMgr();
		cm = getClientMgr();
		progMgr = getProgMgr();
	}
	
	
	/**
	 * Test get participants using managers
	 * get participants (all, for a given program, using filters/sorting)
	 * Create, validate the creation, then delete the participants
	 */
	@Test public void testGetParticipant() throws Exception{
		int numOfParticipants = 2;
		try{
			clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		}catch(Exception e){}
		
		generateParticipantsWithFullManager(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PASSWORD, numOfParticipants, ParticipantHelper.DEFAULT_PROGRAM_NAME);
		validateParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		List<String> partNames = pm.getParticipants();
		Assert.assertTrue(partNames.size() >= numOfParticipants);
		List<Participant> partList = pm.getAllParticipants();
		Assert.assertTrue(partList.size() >= numOfParticipants);
		boolean part1Flag = false;
		boolean part2Flag = false;
		for(Participant p : partList){
			if(p.getParticipantName().equals("test0")){
				part1Flag = true;
			}
			if(p.getParticipantName().equals("test1")){
				part2Flag = true;
			}
			
		}
		Assert.assertTrue(part1Flag);
		Assert.assertTrue(part2Flag);
		Assert.assertNotNull(pm.getParticipant("test0", false));
		Assert.assertNotNull(pm.getParticipant("test1", false));
		
		clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
	}
	
	
	
	/**
	 * Test Creation of participants using managers
	 * Create, validate the creation, then delete the participants
	 */
	@Test public void testCreateParticipant() throws Exception{
		int numOfParticipants = 2;
		try{
			clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		}catch(Exception e){}
		
		generateParticipantsWithFullManager(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PASSWORD, numOfParticipants, ParticipantHelper.DEFAULT_PROGRAM_NAME);
		validateParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
	}


	/**
	 * Test Creation of participants using Base entities and top level objects
	 * Create, validate the creation, then delete the participants
	 */
	@Ignore("This test needs SCE DB - once that feature is implemented we can turn this test on to run automatically")
	@Test public void testCreateParticipantEntities() throws Exception{
		int numOfParticipants = 2;
		try{
			clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		}catch(Exception e){}
		
		generateParticipantsWithTopLevelPart(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PASSWORD, numOfParticipants, ParticipantHelper.DEFAULT_PROGRAM_NAME);
		validateParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
	}
	

	
	/**
	 * Test Deletion of participants using managers
	 * Create, validate the creation, then delete the participants
	 */
	@Test public void testDeleteParticipant() throws Exception{
		int numOfParticipants = 2;
		try{
			clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		}catch(Exception e){}
		
		generateParticipantsWithFullManager(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PASSWORD, numOfParticipants, ParticipantHelper.DEFAULT_PROGRAM_NAME);
		validateParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		validateParticipantsNotPresent(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
	}
	
	

	/**
	 * Test Update of participant Account Number
	 * Create, validate the creation, update, then delete the participants
	 */
	@Test public void testUpdateParticipantAccountNumber() throws Exception{
		int numOfParticipants = 2;
		try{
			clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		}catch(Exception e){}
		
		generateParticipantsWithFullManager(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PASSWORD, numOfParticipants, ParticipantHelper.DEFAULT_PROGRAM_NAME);
		validateParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		String participantName = ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX + 0;
		Participant p = pm.getParticipant(participantName);
		String newAccountNumber = "blah";
		p.setAccountNumber(newAccountNumber);
		pm.updateParticipant(p);
		p = pm.getParticipant(participantName);
		Assert.assertNotNull(p);
		Assert.assertArrayEquals(new String[]{newAccountNumber}, new String[]{p.getAccountNumber()});
		clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
	}
	

	/**
	 * Test Update of participant password
	 * Create, validate the creation, update, then delete the participants
	 */
	@Test public void testUpdateParticipantPassword() throws Exception{
		int numOfParticipants = 2;
		try{
			clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		}catch(Exception e){}
		
		generateParticipantsWithFullManager(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PASSWORD, numOfParticipants, ParticipantHelper.DEFAULT_PROGRAM_NAME);
		validateParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
		String participantName = ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX + 0;
		Participant p = pm.getParticipant(participantName);
		String newPassword = "Test_2222";
		pm.setParticipantPassword(p.getUUID(), newPassword);
		pm.updateParticipant(p);
		p = pm.getParticipant(participantName);
		Assert.assertNotNull(p);
		clearParticipants(ParticipantHelper.DEFAULT_PARTICIPANT_PRIFIX, ParticipantHelper.DEFAULT_CLIENT_PRIFIX, numOfParticipants);
	}
	
	
	
	/**
	 * Generate participants using base entities and top level objects
	 * @param participantPrefix participant prefix
	 * @param clientPrefix client prefix
	 * @param clientPassword client password
	 * @param numberOfParticipants number of participants
	 * @param programName program name
	 * @throws NamingException failure
	 */
	private void generateParticipantsWithTopLevelPart(String participantPrefix,
			String clientPrefix, String clientPassword, int numberOfParticipants, String programName)
			throws NamingException {
		
		Program program = progMgr.getProgram(programName);
		ParticipantHelper ph = new ParticipantHelper();
		
		for (int i = 0; i < numberOfParticipants; i++) {
			// create participant
			String participantName = participantPrefix + i;
			Participant participant = ph.createParticipant(participantName, false, null);

			// create participant's program participant
			ProgramParticipant pp = new ProgramParticipant();
        	pp.setCreationTime(new Date());
        	pp.setCreator("perfTest");
        	pp.setProgram(program);
        	pp.setProgramName(program.getProgramName());
        	pp.setParticipant(participant);
        	pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
        	pp.setClientConfig(1);
        	participant.getProgramParticipants().add(pp);
			
        	// save participant
        	pm.createParticipant(participant, clientPassword.toCharArray(), null);
			
			// create client
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			Participant clientParticipant = ph.createParticipant(clientName, true, participantName);

			// create client's program participant
			pp = new ProgramParticipant();
        	pp.setCreationTime(new Date());
        	pp.setCreator("perfTest");
        	pp.setProgram(program);
        	pp.setProgramName(program.getProgramName());
        	pp.setParticipant(clientParticipant);
        	pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
        	pp.setClientConfig(1);
        	clientParticipant.getProgramParticipants().add(pp);
			
        	// save client
        	cm.createClient(clientParticipant, clientPassword.toCharArray());
		}
		
		progMgr.refreshProgram(program.getProgramName());
	}
	
	
	
	
	/**
	 * Validates the participants 
	 * @param participantPrefix participant Prefix
	 * @param clientPrefix client Prefix
	 * @param numberOfParticipants number Of Participants
	 * @throws Exception Failure
	 */

	private void validateParticipants(String participantPrefix, String clientPrefix, int numberOfParticipants) throws Exception{
		List<String> partNames = pm.getParticipants();
		for(int i = 0; i < numberOfParticipants; i++){
			String participantName = participantPrefix + i;
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			Assert.assertTrue(partNames.contains(participantName));
			Assert.assertNotNull(pm.getParticipant(participantName));
			List<Participant> clientList = cm.getClients(participantName);
			Assert.assertNotNull(clientList);
			Assert.assertTrue(clientList.size() == 1);
		}

		
	}

	
	/**
	 * Validates the participants are not present
	 * @param participantPrefix participant Prefix
	 * @param clientPrefix client Prefix
	 * @param numberOfParticipants number Of Participants
	 * @throws Exception Failure
	 */

	private void validateParticipantsNotPresent(String participantPrefix, String clientPrefix, int numberOfParticipants) throws Exception{
		List<String> partNames = pm.getParticipants();
		for(int i = 0; i < numberOfParticipants; i++){
			String participantName = participantPrefix + i;
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			Assert.assertFalse(partNames.contains(participantName));
			Assert.assertNull(pm.getParticipant(participantName));
			List<Participant> clientList = cm.getClients(participantName);
			Assert.assertNotNull(clientList);
			Assert.assertTrue(clientList.size() == 0);
		}

		
	}
	
	
	/**
	 * Clears the participants 
	 * @param participantPrefix participant Prefix
	 * @param clientPrefix client Prefix
	 * @param numberOfParticipants number Of Participants
	 * @throws NamingException Failure
	 */
	private void clearParticipants(String participantPrefix, String clientPrefix, int numberOfParticipants) throws NamingException{
		
		for(int i = 0; i < numberOfParticipants; i++){
			String participantName = participantPrefix + i;
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			cm.removeClient(clientName);
			pm.removeParticipant(participantName);
		}
	}
	
	
	
	private void generateParticipantsWithFullManager(String participantPrefix,
			String clientPrefix, String clientPassword, int numberOfParticipants, String programName)
			throws NamingException {

		for(int i = 0; i < numberOfParticipants; i++){
			String participantName = participantPrefix + i;
			String clientName = participantPrefix + i + "." + clientPrefix + i;
			Participant participant = new Participant();
	        participant.setParticipantName(participantName);
	        participant.setAccountNumber(participantName);
	        participant.setDataEnabler(true);
	        pm.createParticipant(participant, clientPassword.toCharArray(), null);


      		for (ProgramParticipant ppc : pm.getProgramParticpantsForClientConfig(participantName , false)) {
      			ppc.setClientConfig(1);
      			ppm.updateProgramParticipant(ppc.getProgramName(), participantName, false , ppc);
    		}	        
	        
	        Participant clientParticipant = new Participant();
			clientParticipant.setClient(true);
			clientParticipant.setUser(clientName);
			clientParticipant.setType((byte)0);
			clientParticipant.setAccountNumber(clientName);
			clientParticipant.setParent(participantName);
			clientParticipant.setActivated(true);
			cm.createClient(clientParticipant, clientPassword.toCharArray());


//        	ProgramParticipant pp = ppm.getClientProgramParticipants(programName, clientName, true);
//			pp.setState(ProgramParticipant.PROGRAM_PART_ACTIVE);
//			ppm.updateProgramParticipant(programName, clientName, true, pp);
		}
		
		
	}	
}
