package com.qa.Event;
//7.4

import com.qa.Report.*;

import com.thoughtworks.selenium.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.logging.Logger;

public class EnableUsage extends SeleneseTestCase {
//server.config has the information for these variables.
	String Build = "x";
	String Server = "";
	String Username = "";
	String Password = "";
	String Participants = "";
	String Details = "";
	
	// SPEED SETTINGS FOR SELENIUM
	public static final String FAST = "100";
	public static final String VERYFAST = "50";
	public static final String MODERATE = "1000";
	public static final String SLOW = "3000";
	public static final String VERYSLOW = "5000";
	//SPEED SETTINGS FOR THREAD
	public static final int TSLOW = 3000;
	

	Logger log = Logger.getLogger(CreateEvent.class.getSimpleName());
	
	
	
	
	private void readConfigFile() throws Exception {
		try{
		    
		    FileInputStream fstream = new FileInputStream("serverEnableUsage.config");
		   
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    
		    Build = br.readLine();
		    Server = br.readLine();
		    Username = br.readLine();
		    Password = br.readLine();
		    Participants = br.readLine();
		    
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
	    selenium.setSpeed(MODERATE);
	    selenium.start();
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
	
	private boolean enableClientData (String participant) throws Exception {
		log.info("Looking to enable data for: " + participant ); 
        String pFromParticipantsPage = "";
        boolean success = false;
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
    				log.info("We found the participant: " + participant );
    				selenium.click("xpath=(//a[contains(text(),'Edit')])[" + i + "]");
    				selenium.waitForPageToLoad("30000");
    				Thread.sleep(TSLOW);
    				if (!selenium.isChecked("//input[@name='dataEnabler']")){ 					
    					selenium.click("//input[@name='dataEnabler']");
    					Thread.sleep(TSLOW);
    					selenium.click("//input[@value='Update']");
    					selenium.waitForPageToLoad("50000");
    					log.info("We enabled usage for the participant: " + participant );
    					Details = "We enabled usage for the participant: " + participant;
    					Thread.sleep(TSLOW);
    					success = true;
    				} else {
    					Thread.sleep(TSLOW);
    					selenium.click("//input[@value='Cancel']");
    					selenium.waitForPageToLoad("50000");
    					log.info("Usage was already enabled for: " + participant );
    					Details = "Usage was already enabled for: " + participant;
    					success = false;
    				}//if			
    				i = count;
                    
    			} //if
    		} //if
    	} //for
        return success;
	}
	
	@Test
	public void testEnableUsage() throws Exception {
		    String[] participantName = Participants.split(",");
		    String participant = "";
		    boolean success = false;
		    selenium.setSpeed(VERYFAST);
		    String component = "Core";
		    String test = "Enable Usage: "; 
		    String result = ""; 
		   //We are getting the build from the About Page. 
		    Build = getBuild();
		    TestReport res = new TestReport("TestReport"+ Build + ".csv",Build,Server);
		    res.openReport();			
			
			
		    int count  = participantName.length;
		    try {
		    	loginAb();
				for(int i = 0; i < count; i++ ) {
					participant = participantName[i];
					success = enableClientData(participant);
				//	if (success) log.info("Data enabled for:" + participant);
					if (success) result = "Pass"; else result = "Fail";
					 res.report(component, test + participant, result, Details);
				}
				selenium.click("link=Logout");
				res.closeReport();
				log.info("Logging Out");
			}catch(Exception e) {
				e.printStackTrace();
			}
		   
		}

		@After
		public void tearDown() throws Exception {
		    selenium.stop();
		}
		
		
	} // close EnableUsage class
