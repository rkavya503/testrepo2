package com.akuacom.drms.test;

import javax.naming.NamingException;

/**
 * Tool to enable performance testing. You can pass in all the configuration parameters
 * and this tool will set up the database for proper client participants and generate 
 * JMeter test plans for performance testing.
 * @author Sunil
 *
 */
public class EventTimer {

	/**
	 * Default Threads
	 */
	private static final int DEFAULT_THREADS = 1;
	
	/**
	 * Default Ramp up Time
	 */
	private static final int DEFAULT_RAMP_UP_TIME = 60;

	/**
	 * Default Loop Count
	 */
	private static final int DEFAULT_LOOP_COUNT = 1;


	/**
	 * Default Host Name
	 */
	protected static final String DEFAULT_HOST_NAME = "localhost";


	/**
	 * Default DRAs port
	 */
	private static final int DEFAULT_DRAS_PORT = 8080;


	/**
	 * Default Client PAssword
	 */
	protected static final String DEFAULT_CLIENT_PASSWORD = "Test_1234";
	
	
	/**
	 * Default Confirmation flag
	 */
	private final boolean DEFAULT_CONFIRMATION = true;

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
	
	/**
	 * Default File Path
	 */
	private static final String DEFAULT_FILE_PATH = "/tmp/agg_results.xml";
	

	/**
	 * Default Test Plan File Path
	 */
	private static final String DEFAULT_TEST_PLAN_FILE_PATH = "target/akua_test_plan.jmx";

	/**
	 * Default User name
	 */
	protected static final String DEFAULT_USER_NAME = "admin";

	/**
	 * Default User password
	 */
	protected static final String DEFAULT_USER_PASSWORD = "Crank$215";
	
	public EventTimer(){
		
		
		
	}

	
	/**
	 * Main method
	 * @param args command line arguments
	 * @throws EventTimerException Failure  
	 * @throws NamingException 
	 * @throws InterruptedException 
	 * 
	 */
	public static void main(String[] args) throws EventTimerException, NamingException, InterruptedException {
		EventTimer tool = new EventTimer();

		int noThreads = 0;
		int rampTime = 0;
		int loopCount = 0;
		String host = null;
		int port = 0;
		String pwd = null;
		boolean confirmation = true;
		String partPrefix = null;
		String clientPrefix = null;
		int noParticipants = 0;
		String program = null;
		String filePath = null;
		String testPlanFilePath = null;
		String user = null;
		String upwd = null;
		boolean clean = false;
		
		if(args != null){

			if(args.length == 1 && args[0].equals("-h")){
				tool.usage();
				System.exit(1);
			}

			
			
			
			
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("-noThreads")){
					noThreads = Integer.valueOf(args[++i]);
				} else if(args[i].equals("-rampTime")){
					rampTime = Integer.valueOf(args[++i]);
				} else if(args[i].equals("-loopCount")){
					loopCount = Integer.valueOf(args[++i]);
				} else if(args[i].equals("-host")){
					host = args[++i];
				} else if(args[i].equals("-port")){
					port = Integer.valueOf(args[++i]);
				} else if(args[i].equals("-pwd")){
					pwd = args[++i];
				} else if(args[i].equals("-confirmation")){
					confirmation = Boolean.parseBoolean(args[++i]);
				} else if(args[i].equals("-partPrefix")){
					partPrefix = args[++i];
				} else if(args[i].equals("-clientPrefix")){
					clientPrefix = args[++i];
				} else if(args[i].equals("-noParticipants")){
					noParticipants = Integer.valueOf(args[++i]);
				} else if(args[i].equals("-program")){
					program = args[++i];
				} else if(args[i].equals("-filePath")){
					filePath = args[++i];
				} else if(args[i].equals("-testPlanFilePath")){
					testPlanFilePath = args[++i];
				} else if(args[i].equals("-user")){
					user = args[++i];
				} else if(args[i].equals("-upwd")){
					upwd = args[++i];
				} else if(args[i].equals("-clean")){
					clean = Boolean.parseBoolean(args[++i]);
				}
			}
			
			
		}
		
		

		String[] programName = null;
		if(program != null){
			programName = new String[]{program};
		}else{
			programName = new String[]{DEFAULT_PROGRAM_NAME};
		}


		long startTime = System.currentTimeMillis();
		
		ParticipantGenerator g = new ParticipantGenerator(host, user, upwd, programName, true);
		if(!clean){
			System.out.println("Creating Participants");
	    	g.generateParticipants(partPrefix,clientPrefix, pwd, noParticipants);
	    	long partTime = System.currentTimeMillis();
	    	System.out.println("Creating " + noParticipants + " Participants took - " + (partTime - startTime) + " msec");
	    	System.out.println("Average time to create a Participant  - " + ((partTime - startTime)/noParticipants) + " msec");
	    	String eventName = g.createEvent();
	    	long eventTime = System.currentTimeMillis();
	    	System.out.println("Event Creation with  " + noParticipants + " Participants subscribed to " + programName[0]  + " program took - " + (eventTime - partTime) + " msec");
//			eventTime = System.currentTimeMillis();
//	    	g.setSignalEntries(eventName, partPrefix, clientPrefix, noParticipants);
//	    	long signalTime = System.currentTimeMillis();
//	    	System.out.println("Creating Signal Entried for " + noParticipants + " Participants took - " + (signalTime - eventTime) + " msec");
//	    	System.out.println("Average time for creating signal entries for a Participant  - " + ((signalTime - eventTime)/noParticipants) + " msec");
	    	
		} else{
			System.out.println("Deleting Participants");
	    	g.clearEvents();
	    	g.clearParticipants(partPrefix,clientPrefix,noParticipants);
	    	g.cleanup();
		}
	}
	/**
	 * Usage 
	 */
	private void usage(){
		System.out.println("Usage: java com.akuacom.drms.test.EventTimer [-noThreads <Number of Threads - default " + DEFAULT_THREADS+">] [-rampTime <Ramp Up Time in Sec - default " + DEFAULT_RAMP_UP_TIME+">]  [-loopCount <Loop Count - default " + DEFAULT_LOOP_COUNT+ ">] [-host <DRAS Host Name - default " + DEFAULT_HOST_NAME +">] [-port <DRAS port - default " + DEFAULT_DRAS_PORT+">] [-pwd <Participant Password - default " + DEFAULT_CLIENT_PASSWORD+ ">] [-confirmation <Is confirmation needed - default " + DEFAULT_CONFIRMATION+">] [-partPrefix <Participant prefix - default "+DEFAULT_PARTICIPANT_PRIFIX+">] [-clientPrefix <Client prefix - default " +DEFAULT_CLIENT_PRIFIX+ ">] [-noParticipants <Number of Participants - default " + DEFAULT_NUMBER_OF_PARTICIPANTS +">] [-program <Program Name - default "+ DEFAULT_PROGRAM_NAME + ">] [-filePath <File to to store JMeter results - default " + DEFAULT_FILE_PATH +">] [-testPlanFilePath <File to to store JMeter Test Plan - default " + DEFAULT_TEST_PLAN_FILE_PATH +">] [-user <Website User name  - default " + DEFAULT_USER_NAME +">] [-upwd <Website User password  - default " + DEFAULT_USER_PASSWORD +">] [-clean <Flag to clean up the participants  - default " + "false" +">]");
	}
	

}
