package com.qa.Event;

//7.3


import com.qa.Report.TestReport;
import com.thoughtworks.selenium.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.logging.Logger;


public class AddParticipants extends SeleneseTestCase {
//server.config has the information for these variables.
	String Build = "";
	String Server = "";
	String Username = "";
	String Password = "";
	String EmailAddress = "";
	String Participants = "";
	String Programs = "";
	String Clients = "";
	String ClientPrograms = "";
	String Details = "";
	boolean reportResults = true;
	
	// SPEED SETTINGS FOR SELENIUM
	public static final String FAST = "100";
	public static final String VERYFAST = "50";
//	public static final String MODERATE = "1000";
//	public static final String SLOW = "3000";
//	public static final String VERYSLOW = "5000";
	// for qa3
	public static final String MODERATE = "3000";
	public static final String SLOW = "5000";
	public static final String VERYSLOW = "8000";
	//SPEED SETTINGS FOR THREAD
	public static final int TSLOW = 3000;
	

	Logger log = Logger.getLogger(AddParticipants.class.getSimpleName());
	
	private void readConfigFile() throws Exception {
		try{
		    
		    FileInputStream fstream = new FileInputStream("serverParticipantsWithClones.csv");
		   
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    
		    Build = br.readLine();
		    Build = Build.substring(0, Build.indexOf(","));
		    Server = br.readLine();
		    Server = Server.substring(0, Server.indexOf(","));
		    Username = br.readLine();
		    Username = Username.substring(0, Username.indexOf(","));
		    Password = br.readLine();
		    Password = Password.substring(0, Password.indexOf(","));
		    EmailAddress = br.readLine();
		    EmailAddress = EmailAddress.substring(0, EmailAddress.indexOf(","));
		    Participants = br.readLine();
		    Programs = br.readLine();
		    Clients = br.readLine();
		    ClientPrograms = br.readLine();
		   
		    in.close();
		    
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
	}
	
	@Before
	public void setUp() throws Exception {
		//get server, username, password, contact email from server.config
		readConfigFile();		
	    selenium = new DefaultSelenium("localhost", 4444, "*chrome", Server);
	    selenium.setSpeed(SLOW);
	    selenium.start();
	    // for qa3 60 seconds instead of 30
	    selenium.setTimeout("60000");
	}
	public void loginAb() throws Exception {
	    selenium.open("/pss2.website/Login");
	    selenium.type("fm-username", Username);
	    selenium.type("fm-password", Password);
	    selenium.click("Submit");
	    selenium.waitForPageToLoad("90000");
	}
	public String getBuild() throws Exception {
		
		loginAb();
		selenium.click("link=About"); 
	    selenium.waitForPageToLoad("30000");
	    String BuildFromAboutPage = selenium.getText("//form/h4[2]");
	    selenium.click("link=Logout");
	    
		return BuildFromAboutPage;
	}
	
	
	private boolean addOneParticipant(String participantName, String programs) throws Exception{
		
		String[] programName = programs.split(":");		
		String password = "Test_1234";
		
		
		int count = programName.length;		
		
	    selenium.waitForPageToLoad("30000");
	    selenium.click("link=Participants");
	 
	    selenium.waitForPageToLoad("30000");
	    selenium.click("//a[@href='/pss2.website/commDevDetail.do?dispatch=create']");
	    selenium.waitForPageToLoad("30000");
	    selenium.type("//input[@name='userName']", participantName);
	    selenium.type("//input[@name='password']", password);
	    selenium.type("//input[@name='password2']", password);
	    selenium.type("//input[@name='accountNumber']", participantName);
	    selenium.click("//input[@value='Save']");
	    selenium.waitForPageToLoad("25000");
	    // enroll in programs
	    Details = participantName + " in: ";
	    if (!programName[0].contains("None")) {	
		    for ( int i = 0; i < count; i++) {
		    	selenium.setSpeed(FAST);
		    	selenium.click("//input[@name='programs'][@value=" + "'" + programName[i] + "']");
			    selenium.click("//input[@name='clientConfig'][@value=" + "'" + programName[i] + "']");
			    Details = Details + programName[i] + " ";
		    } 
	    }
	    // all participants are enrolled in usage
	    selenium.click("//input[@name='dataEnabler']");
	    // participants with aggregat in their name become aggregators
	    if (participantName.toUpperCase().contains("AGGREGAT"))selenium.click("//input[@name='aggregator']");
	    selenium.setSpeed(SLOW);
	    selenium.click("//input[@value='Update']");
	    selenium.waitForPageToLoad("50000");
	    log.info("Added Participant: " + participantName);
	    return true;
	}
	
	public void fromParticipantsTabToAddClient(String participant) throws Exception {
		
		// this code moves us over to the correct participant's facdash
		selenium.waitForPageToLoad("30000");
	    String pFromParticipantsPage = "";
		if (!selenium.isTextPresent("Participants"))selenium.open("/pss2.website/participantsMap.do");

        selenium.type("//input [@type='text']", participant);
        selenium.click("//input[@title='Search Participant']");
        int count = (selenium.getXpathCount("//form/table[4]/tbody/tr")).intValue();
       
        for (int i = 1; i <= count; i++) {
        	pFromParticipantsPage = "";
    		selenium.setSpeed(MODERATE);
    		if (selenium.isElementPresent("//tbody/tr[" + i + "]/td[2]/span")){
    			pFromParticipantsPage = selenium.getText("//tbody/tr[" + i + "]/td[2]/span");
    			if (pFromParticipantsPage.contentEquals(participant)) {
    				selenium.click("xpath=(//a[contains(text(),'Add Client')])[" + i + "]");
    				selenium.waitForPageToLoad("30000");
    				Thread.sleep(TSLOW);
    				
    				i = count;
                    
    			} //if
    		} //if
    	} //for
			
	}
	
	public boolean addOneClient(String participant, String programs) throws Exception {
		// move to facdash
		fromParticipantsTabToAddClient(participant);
		// create client
		String password = "Test_1234";
		selenium.type("navForm:name", "c1");
		selenium.type("navForm:password", password);
		selenium.type("navForm:confirm", password);
		selenium.click("//input[@value='Create Client']");
		Thread.sleep(6000);	
		selenium.waitForPageToLoad("75000");
		log.info("Added Client: " + participant + ".c1");
		
		// enroll in programs
		selenium.click("id=navForm:client_programs_href");
		selenium.waitForPageToLoad("80000");
		int countPrograms = (selenium.getXpathCount("//tr/td/h5")).intValue();
		for (int i = 1; i <= countPrograms; i++){	   
			if (!selenium.isChecked("//tr[" + i + "]/td[2]/input[@type ='checkbox']")){
				selenium.click("//tr[" + i + "]/td[2]/input[@type ='checkbox']");
				if (i == 1){
					log.info(participant  + ".c1" + " participating in all programs.");	
					Details = Details + "Client c1 in all programs, ";
				}			
			}
		}
		Thread.sleep(3000);	
		//add contact
		selenium.setSpeed(MODERATE);
        // return to utility operator		
		selenium.click("link=Done");
		return true;
	}

	public void addMoreClients(String participant, String numberClients, String programs)throws Exception{
		// 2nd and 3rd clients can have no more than one program with this code
		// and this configuration file
		fromParticipantsTabToAddClient(participant);
		
		String[] programName = programs.split(":");
		int number = Integer.parseInt(numberClients) - 1;
		String password = "Test_1234";
	    selenium.waitForPageToLoad("30000");
        // 
        for (int i = 0; i < number; i++ ) {
    		//  create client with "c" + the first extra client is 2
        	if (i > 0) {
        		selenium.click("navForm:navClients");
        		selenium.waitForPageToLoad("30000");
				selenium.click("//input[@value='New Client']");
				selenium.waitForPageToLoad("30000");		
        	}
        	selenium.type("navForm:name", "c" + Integer.toString(i+2));
    		selenium.type("navForm:password", password);
    		selenium.type("navForm:confirm", password);
    		selenium.click("//input[@value='Create Client']");
    		selenium.waitForPageToLoad("25000");
    		log.info("Added Client: " + participant + ".c" + (i+2));	
    			if (!programName[i].contentEquals("None")){
    				// for now only one program per 2nd and 3rd client
    				selenium.click("id=navForm:client_programs_href");
    				selenium.waitForPageToLoad("80000");
    				int countPrograms = (selenium.getXpathCount("//tr/td/h5")).intValue();
    				for (int j = 1; j <= countPrograms; j++){
    					if (programName[i].contentEquals(selenium.getText("//tr[" + j + "]/td[1]/h5"))) {
    						if (!selenium.isChecked("//tr[" + j + "]/td[2]/input[@type ='checkbox']")){
    							selenium.click("//tr[" + j + "]/td[2]/input[@type ='checkbox']");
    							log.info(participant  + ".c" + (i+2)+ " participating in : " + programName[i]);
    							Details = Details  + ".c" + (i+2)+ " participating in : " + programName[i];
    							//change Shed Strategy to Moderate for 2nd and 3rd clients in programs
    							if (programName[i].contains("DO") || programName[i].contains("DA")){
	    							selenium.click("//tr[" + j + "]/td[3]/input[@value='Shed Strategies']");
	    							for (int k = 1; k <= 8; k++){
	    								selenium.select("//tr[" + k + "]/td[2]/select","label=MODERATE");
	    							}
	    							selenium.click("//input[@value='Save']");
	    							selenium.waitForPageToLoad("50000");
	    							log.info(participant  + ".c" + (i+2)+ " Shed Strategy Moderate");
	    							Details = Details + ".c" + (i+2)+ " Shed Strategy Moderate";
    							}	   						
    						} //if  						
    					}//if
    				} //for    				
    				// add contact
    			} 
    			
    	}        
		Thread.sleep(3000);	
		selenium.setSpeed(MODERATE);
		selenium.click("link=Done");
	}
	
	@Test
	public void testAddParticipants() throws Exception {
		
		    String participantName[] = Participants.split(",");
		    String programName[] = Programs.split(",");
		    String clientsNumber[] = Clients.split(",");
		    String clientPrograms[] = ClientPrograms.split(",");
		    
		    String component = "Operator Utility";
		    String test = "Verify Add Participants: ";
		    
		    //We are getting the build from the About Page. 
		    Build = getBuild();
		   
		    TestReport res = new TestReport("TestReport"+ Build + ".csv",Build,Server);
		    if (reportResults) res.openReport();
		   
		    
		    
		    selenium.setSpeed(MODERATE);
		  	
		    int count  = participantName.length;
		    try {
		    	loginAb();
				for(int i = 0; i < count; i++ ) {
					Details = "";
					if (addOneParticipant(participantName[i], programName[i])) {
					// first client is in all programs
						if(addOneClient(participantName[i], programName[i])) {
					// second and third clients are in one or no programs
							if (!clientsNumber[i].contentEquals("1")) 
								addMoreClients(participantName[i],clientsNumber[i],clientPrograms[i]);
							if (reportResults) res.report(component, test + participantName[i], "Pass", Details);
						}
					} else if (reportResults) res.report(component, test + participantName[i], "Fail", Details);
							
						//} else res.report(component, test + programName[i], "Fail", Details);
							
				}
				if (reportResults)res.closeReport();
				selenium.click("link=Logout");
				log.info("Successfully exited the loop.");
				log.info("Logging Out");
				
			}catch(Exception e) {
				e.printStackTrace();
			}		    		   
		}
}
