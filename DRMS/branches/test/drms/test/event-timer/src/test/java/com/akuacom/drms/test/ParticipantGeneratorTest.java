package com.akuacom.drms.test;

import org.junit.Ignore;
import org.junit.Test;



/**
 * Tests methods in ParticipantGenerator
 * @author Sunil
 *
 */
public class ParticipantGeneratorTest {

	private String host = "localhost";
	
	private String user = "a";
	
	private String pwd = "b";
	
	private String programName = "RTP Agricultural";
	
	private String participantPrefix = "test";
	
	private String clientPrefix = "test";
	
	private String clientPwd = "Test_1234";
	
	private int noOfParticipants = 20;
	
    /**
     * tests generateParticipants
     * @throws Exception 
     */
    @Test
    @Ignore("Remove ignore if you want to run this test - make sure your DRAS is running locally")
	public void testGenerateParticipants() throws Exception
    {
    	ParticipantGenerator g = new ParticipantGenerator(host, user, pwd, new String[]{programName}, true);
    	g.generateParticipants(participantPrefix,clientPrefix, clientPwd, noOfParticipants);
    	g.clearParticipants(participantPrefix,clientPrefix,noOfParticipants);
    }


    /**
     * tests setSignalEntries
     * @throws Exception 
     */
    @Test
    @Ignore("Remove ignore if you want to run this test - make sure your DRAS is running locally")
	public void testSetSignalEntries() throws Exception
    {
    	ParticipantGenerator g = new ParticipantGenerator(host, user, pwd, new String[]{programName}, true);
    	try{
        	g.generateParticipants(participantPrefix,clientPrefix, clientPwd, noOfParticipants);
        	String eventName = g.createEvent();
        	g.setSignalEntries(eventName, participantPrefix, clientPrefix, noOfParticipants);
    		
    	}finally{
        	g.clearEvents();
        	g.clearParticipants(participantPrefix,clientPrefix,noOfParticipants);
    	}
    }
    
    
    
    /**
     * tests generateParticipants
     * @throws Exception 
     */
    @Test
    @Ignore("Remove ignore if you want to run this test - make sure your DRAS is running locally")
    public void testClearEvents() throws Exception
    {
    	ParticipantGenerator g = new ParticipantGenerator(host, user, pwd, new String[]{programName}, true);
    	g.clearEvents();
    }
    
    
    /**
     * tests createEvent
     * @throws Exception 
     */
    @Test
    @Ignore("Remove ignore if you want to run this test - make sure your DRAS is running locally")
    public void testCreateEvent() throws Exception
    {
    	ParticipantGenerator g = new ParticipantGenerator(host, user, pwd, new String[]{programName}, true);
    	g.createEvent();
    	g.clearEvents();
    	
    }
    
    /**
     * tests flow
     * @throws Exception 
     */
    @Test
    @Ignore("Remove ignore if you want to run this test - make sure your DRAS is running locally")
    public void testFlow() throws Exception{
    	ParticipantGenerator g = new ParticipantGenerator(host, user, pwd, new String[]{programName}, true);
    	g.generateParticipants(participantPrefix,clientPrefix, clientPwd, noOfParticipants);
    	g.createEvent();
    	g.clearEvents();
    	g.clearParticipants(participantPrefix,clientPrefix,noOfParticipants);
    	g.cleanup();

    	
    }

}

