package com.akuacom.drms.performance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;


/**
 * Tool to enable performance testing. You can pass in all the configuration parameters
 * and this tool will set up the database for proper client participants and generate 
 * JMeter test plans for performance testing.
 * @author Sunil
 *
 */
public class PerformanceTool {

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
	 * Default Simple Dashboard Test Plan File Path
	 */
	private static final String DEFAULT_SIMPLE_TEST_PLAN_FILE_PATH = "target/simple_test_plan.jmx";

	/**
	 * Default data for simple dashboard File Path
	 */
	private static final String DEFAULT_SIMPLE_DATA_FILE_PATH = "target/user.dat";
	
	
	/**
	 * Default User name
	 */
	protected static final String DEFAULT_USER_NAME = "admin";

	/**
	 * Default User password
	 */
	protected static final String DEFAULT_USER_PASSWORD = "Crank$215";
	
	
	/**
	 * Default Number of Days
	 */
	protected static final int DEFAULT_NUMBER_OF_DAYS= 1;
	
	/**
	 * Default interval between Participant threads
	 */
	protected static final int DEFAULT_PART_INTERVAL = 0;
	/**
	 * Default interval for a participant populate points
	 */
	protected static final int DEFAULT_POINT_INTERVAL = 0;
	
	
	public PerformanceTool(){
		
		
		
	}

	
	/**
	 * Creates a JMeter test plan based on the configuration provided
	 * @param numberOfThreads Number of simultaneous threads - default is 1
	 * @param rampUpTime Ramp up time is seconds - default is 60 sec
	 * @param loopCount Loop count - default is 1
	 * @param drasHostName - Dras Host Name - default is localhost
	 * @param drasPort - dras port - default is 8443
	 * @param clientPassword - Participant client password - default is Test_1234
	 * @param isConfirmationNeeded - Do you need to send out confirmation - default is true
	 * @param participantPrefix - prefix to use for participant - default is test
	 * @param clientPrefix - prefix to use for client - default is test
	 * @param numberOfParticipants - - Number of participants - default is 1
	 * @param programName - Program Name - Default RTP Agricultural
	 * @param filePath - file path to store test results - default /tmp/agg_results.xml
	 * @return test plan as a string 
	 * @throws PerformanceToolException if any Failure encountered
	 */
	public String createSimplePlan(int numberOfThreads, int rampUpTime, int loopCount,  String drasHostName, int drasPort, String password, int numberOfParticipants, String filePath) {
		DashboardTestPlanGenerator g = new DashboardTestPlanGenerator();
		if (numberOfParticipants <= 0){
			numberOfParticipants = DEFAULT_NUMBER_OF_PARTICIPANTS;
		}
		if(drasHostName == null){
			drasHostName = DEFAULT_HOST_NAME;
		}
		if(drasPort == 0){
			drasPort = DEFAULT_DRAS_PORT;
		}
		if(password == null){
			password = DEFAULT_CLIENT_PASSWORD;
		}
		if(filePath == null){
			filePath = DEFAULT_SIMPLE_DATA_FILE_PATH;
			
		}


		return g.generateDashboardTestPlan((numberOfThreads == 0 ? DEFAULT_THREADS: numberOfThreads), (rampUpTime == 0 ? DEFAULT_RAMP_UP_TIME : rampUpTime), (loopCount == 0 ? numberOfParticipants : loopCount), drasHostName, drasPort, password, filePath);
		
	}
		
	public String createSimpleData(String participantPrefix, int numberOfParticipants){
		if(participantPrefix == null){
			participantPrefix = DEFAULT_PARTICIPANT_PRIFIX;
		}		if (numberOfParticipants <= 0){
			numberOfParticipants = DEFAULT_NUMBER_OF_PARTICIPANTS;
		}
		DashboardTestPlanGenerator g = new DashboardTestPlanGenerator();
		return g.generateDataFile(participantPrefix, numberOfParticipants);

	}
	
	

	
	
	/**
	 * Creates a JMeter test plan based on the configuration provided
	 * @param numberOfThreads Number of simultaneous threads - default is 1
	 * @param rampUpTime Ramp up time is seconds - default is 60 sec
	 * @param loopCount Loop count - default is 1
	 * @param drasHostName - Dras Host Name - default is localhost
	 * @param drasPort - dras port - default is 8443
	 * @param clientPassword - Participant client password - default is Test_1234
	 * @param isConfirmationNeeded - Do you need to send out confirmation - default is true
	 * @param participantPrefix - prefix to use for participant - default is test
	 * @param clientPrefix - prefix to use for client - default is test
	 * @param numberOfParticipants - - Number of participants - default is 1
	 * @param programName - Program Name - Default RTP Agricultural
	 * @param filePath - file path to store test results - default /tmp/agg_results.xml
	 * @return test plan as a string 
	 * @throws PerformanceToolException if any Failure encountered
	 */
	public String createPlan(int numberOfThreads, int rampUpTime, int loopCount,  String drasHostName, int drasPort, String clientPassword, boolean isConfirmationNeeded, String participantPrefix, String clientPrefix, int numberOfParticipants, String programName, String filePath) throws PerformanceToolException{
		
		StringBuffer sb = new StringBuffer();
		TestPlanGenerator g = new TestPlanGenerator();
		sb.append(g.generateInitialNode());
		sb.append(g.generateThreadGroup((numberOfThreads == 0 ? DEFAULT_THREADS: numberOfThreads), (rampUpTime == 0 ? DEFAULT_RAMP_UP_TIME : rampUpTime) , (loopCount == 0 ? DEFAULT_LOOP_COUNT : loopCount)));
		if (numberOfParticipants <= 0){
			numberOfParticipants = DEFAULT_NUMBER_OF_PARTICIPANTS;
		}
		if(drasHostName == null){
			drasHostName = DEFAULT_HOST_NAME;
		}
		if(drasPort == 0){
			drasPort = DEFAULT_DRAS_PORT;
		}
		if(participantPrefix == null){
			participantPrefix = DEFAULT_PARTICIPANT_PRIFIX;
		}
		if(clientPrefix == null){
			clientPrefix = DEFAULT_CLIENT_PRIFIX;
		}
		if(clientPassword == null){
			clientPassword = DEFAULT_CLIENT_PASSWORD;
		}
		if(programName == null){
			programName = DEFAULT_PROGRAM_NAME;
		}

				
		for(int i = 0; i < numberOfParticipants; i++){
			String participantName = participantPrefix + i + "." + clientPrefix + i;
			sb.append(g.generateGetEventStatesController(drasHostName, drasPort, participantName, clientPassword));
			if(isConfirmationNeeded){
				sb.append(g.generateConfirmationController(drasHostName, drasPort, participantName, clientPassword, programName));
			}
		}
		
		sb.append(g.generateResultCollector(filePath == null ? DEFAULT_FILE_PATH : filePath));
		return sb.toString();
	}

	/**
	 * Persist Simple dashboard test plan
	 * @param filePath path to persist the test plan too
	 * @param testPlan test plan as a string
	 * @throws IOException Failure
	 */
	public void presistSimpleTestPlan(String filePath, String testPlan) throws PerformanceToolException{
		if(filePath == null){
			filePath = DEFAULT_SIMPLE_TEST_PLAN_FILE_PATH;
			
		}
		File file = new File(filePath);
		
		BufferedWriter out = null;
		try{
			out = new BufferedWriter(new FileWriter(file));
			out.write(testPlan); 
			System.out.println("Simple Dashboard Jmeter Test Plan created at " + file.getAbsolutePath());
		} catch (IOException e) {
			throw new PerformanceToolException(e.getLocalizedMessage(), e);
		}finally{
			try {
				out.close();
			} catch (IOException e) {
			}  		
			
		}
	}
	
	
	/**
	 * Persist Simple dashboard test plan
	 * @param filePath path to persist the test plan too
	 * @param testPlan test plan as a string
	 * @throws IOException Failure
	 */
	public void presistSimpleData(String filePath, String testPlan) throws PerformanceToolException{
		if(filePath == null){
			filePath = DEFAULT_SIMPLE_DATA_FILE_PATH;
			
		}
		File file = new File(filePath);
		
		BufferedWriter out = null;
		try{
			out = new BufferedWriter(new FileWriter(file));
			out.write(testPlan); 
			System.out.println("Simple Dashboard Participant Data created at " + file.getAbsolutePath());
		} catch (IOException e) {
			throw new PerformanceToolException(e.getLocalizedMessage(), e);
		}finally{
			try {
				out.close();
			} catch (IOException e) {
			}  		
			
		}
	}
	
	
	/**
	 * Persist test plan
	 * @param filePath path to persist the test plan too
	 * @param testPlan test plan as a string
	 * @throws IOException Failure
	 */
	public void presistTestPlan(String filePath, String testPlan) throws PerformanceToolException{
		if(filePath == null){
			filePath = DEFAULT_TEST_PLAN_FILE_PATH;
			
		}
		File file = new File(filePath);
		
		BufferedWriter out = null;
		try{
			out = new BufferedWriter(new FileWriter(file));
			out.write(testPlan); 
			System.out.println("Jmeter Test Plan created at " + file.getAbsolutePath());
		} catch (IOException e) {
			throw new PerformanceToolException(e.getLocalizedMessage(), e);
		}finally{
			try {
				out.close();
			} catch (IOException e) {
			}  		
			
		}
	}
	
	/**
	 * Main method
	 * @param args command line arguments
	 * @throws PerformanceToolException Failure  
	 * @throws NamingException 
	 * 
	 */
	public static void main(String[] args) throws PerformanceToolException, NamingException {
		PerformanceTool tool = new PerformanceTool();

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
		String simpleTestPlanFilePath = null;
		String simpleTestDataFilePath = null;
		String user = null;
		String upwd = null;
		boolean clean = false;
		int numberOfDays = 0;
		
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
				}else if(args[i].equals("-noDays")){
					numberOfDays = Integer.valueOf(args[++i]);
				}else if(args[i].equals("-simpleTestPlanFilePath")){
					simpleTestPlanFilePath = args[++i];
				}else if(args[i].equals("-simpleTestDataFilePath")){
					simpleTestDataFilePath = args[++i];
				}
			}
			
			
		}
		
		
		
		
		
		String testPlan = tool.createPlan(noThreads, rampTime, loopCount, host, port, pwd, confirmation, partPrefix, clientPrefix, noParticipants, program, filePath);
		tool.presistTestPlan(testPlanFilePath, testPlan);
		
		
		String simpleTestPlan = tool.createSimplePlan(noThreads, rampTime, loopCount, host, port, pwd, noParticipants, simpleTestDataFilePath);
		tool.presistSimpleTestPlan(simpleTestPlanFilePath, simpleTestPlan);
		

		String simpleData = tool.createSimpleData(partPrefix, noParticipants);
		tool.presistSimpleData(simpleTestDataFilePath, simpleData);
		
		

		String[] programName = null;
		if(program != null){
			programName = new String[]{program};
		}else{
			programName = new String[]{DEFAULT_PROGRAM_NAME};
		}
    	if(noParticipants==0){
    		noParticipants = DEFAULT_NUMBER_OF_PARTICIPANTS;
    	}
    	if(numberOfDays==0){
    		numberOfDays = DEFAULT_NUMBER_OF_PARTICIPANTS;
    	}

		
		long startTime = System.currentTimeMillis();		
		
		ParticipantGenerator g = new ParticipantGenerator(host, user, upwd, programName, true);
		UsageDataGenerator usageGenerator = new UsageDataGenerator(host, user, upwd);
		if(!clean){
			System.out.println("Creating Participants");
	    	g.generateParticipants(partPrefix,clientPrefix, pwd, noParticipants);
	    	long partTime = System.currentTimeMillis();
	    	System.out.println("Creating " + noParticipants + " Participants took - " + (partTime - startTime) + " msec");
	    	System.out.println("Average time to create a Participant  - " + ((partTime - startTime)/noParticipants) + " msec");
	    	g.createEvent();
	    	long eventTime = System.currentTimeMillis();
	    	System.out.println("Event Creation with  " + noParticipants + " Participants subscribed to " + programName[0]  + " program took - " + (eventTime - partTime) + " msec");

/*	    	
	    	
	    	ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
	    	
	    	long beforeGenerateUsageTime = System.currentTimeMillis();
	    	List<Long> ids = usageGenerator.generateUsageData(partPrefix, noParticipants, numberOfDays,DEFAULT_PART_INTERVAL ,DEFAULT_POINT_INTERVAL);
	    	while(true){
	    		Iterator<Long> item = ids.iterator();
	    		
	    		while(item.hasNext()){
	    			long id = item.next();
	    			ThreadInfo info = mxBean.getThreadInfo(id);
	    			if(info==null||Thread.State.TERMINATED.equals(info.getThreadState())){
	    				item.remove();
	    			}
	    		}
	    		if(ids.size()==0){
	    			break;
	    		}
	    	}
	    	long afterGenerateUsageTime = System.currentTimeMillis();
	    	System.out.println("UsageData Creation with  " + noParticipants + " Participants and " + numberOfDays  + " days took - " + (afterGenerateUsageTime - beforeGenerateUsageTime) + " msec");
	    	
*/	    	
	    	
	    	
		} else{
			System.out.println("Deleting Participants, Events, Usage Data");
			usageGenerator.clearUsageData(partPrefix, noParticipants);
			usageGenerator.cleanup();
	    	g.clearEvents();
	    	g.clearParticipants(partPrefix,clientPrefix,noParticipants);
	    	g.cleanup();
		}
	}
	/**
	 * Usage 
	 */
	private void usage(){
		System.out.println("Usage: java com.akuacom.drms.performance.PerformanceTool [-noThreads <Number of Threads - default " + DEFAULT_THREADS+">] [-rampTime <Ramp Up Time in Sec - default " + DEFAULT_RAMP_UP_TIME+">]  [-loopCount <Loop Count - default " + DEFAULT_LOOP_COUNT+ ">] [-host <DRAS Host Name - default " + DEFAULT_HOST_NAME +">] [-port <DRAS port - default " + DEFAULT_DRAS_PORT+">] [-pwd <Participant Password - default " + DEFAULT_CLIENT_PASSWORD+ ">] [-confirmation <Is confirmation needed - default " + DEFAULT_CONFIRMATION+">] [-partPrefix <Participant prefix - default "+DEFAULT_PARTICIPANT_PRIFIX+">] [-clientPrefix <Client prefix - default " +DEFAULT_CLIENT_PRIFIX+ ">] [-noParticipants <Number of Participants - default " + DEFAULT_NUMBER_OF_PARTICIPANTS +">] [-program <Program Name - default "+ DEFAULT_PROGRAM_NAME + ">] [-filePath <File to to store JMeter results - default " + DEFAULT_FILE_PATH +">] [-testPlanFilePath <File to to store JMeter Test Plan - default " + DEFAULT_TEST_PLAN_FILE_PATH +">] [-simpleTestPlanFilePath <File to to store JMeter Test Plan for simplified dashboard - default " + DEFAULT_SIMPLE_TEST_PLAN_FILE_PATH +">] [-user <Website User name  - default " + DEFAULT_USER_NAME +">] [-upwd <Website User password  - default " + DEFAULT_USER_PASSWORD +">] [-clean <Flag to clean up the participants  - default " + "false" +">]");
	}
	

}
