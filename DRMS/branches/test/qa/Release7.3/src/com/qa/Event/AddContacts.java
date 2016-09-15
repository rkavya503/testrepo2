package com.qa.Event;


import com.qa.Report.TestReport;
import com.thoughtworks.selenium.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.logging.Logger;


public class AddContacts extends SeleneseTestCase {
//server.config has the information for these variables.
	
	String Server = "";
	String Username = "";
	String Password = "";
	String EmailAddress = "";
	String Participants = "";
	String Clients = "";
	String NotificationType = "";
	String Threshold = "";
	
	String Details = "";
	boolean reportResults = true;
	
	// SPEED SETTINGS FOR SELENIUM
	public static final String FAST = "100";
	public static final String VERYFAST = "50";
/*	public static final String MODERATE = "1000";
	public static final String SLOW = "3000";
	public static final String VERYSLOW = "5000";*/
	// for qa3
	public static final String MODERATE = "3000";
	public static final String SLOW = "5000";
	public static final String VERYSLOW = "8000";
	//SPEED SETTINGS FOR THREAD
	public static final int TSLOW = 3000;
	

	Logger log = Logger.getLogger(AddParticipants.class.getSimpleName());
	
	private void readConfigFile() throws Exception {
		try{
		    
		    FileInputStream fstream = new FileInputStream("serverContacts.config");
		   
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    
		    Server = br.readLine();
		    Server = Server.substring(0, Server.indexOf(","));
		    Username = br.readLine();
		    Username = Username.substring(0, Username.indexOf(","));
		    Password = br.readLine();
		    Password = Password.substring(0, Password.indexOf(","));
		    EmailAddress = br.readLine();
		    Participants = br.readLine();
		    Clients = br.readLine();
		    NotificationType = br.readLine();
		    Threshold = br.readLine();
		   
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
	public void loginFacDash(String user, String pw ) throws Exception {
	    selenium.open("/pss2.website/Login");
	    selenium.type("fm-username", user);
	    selenium.type("fm-password", pw);
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
	public boolean addOneContact(String participant, String client, String email, String typeName, String threshold) throws Exception {
		//table[2]/tbody/tr[1]/td[2]
		//should be on the Clients Tab
		//find the right client
		int count = (selenium.getXpathCount("//table[2]/tbody/tr/td[2]")).intValue();
	       
        for (int i = 1; i <= count; i++) {
    		selenium.setSpeed(MODERATE);
    		if (selenium.isElementPresent("//table[2]/tbody/tr[" + i + "]/td[2]")){
    			if (client.contentEquals(selenium.getText("//tbody/tr[" + i + "]/td[2]/h3/a"))){
    				selenium.click("//tbody/tr[" + i + "]/td[2]/h3/a");
    				selenium.waitForPageToLoad("30000");
    				Thread.sleep(TSLOW);  				
    				i = count;
                    
    			} //if
    		} //if
    	} //for	
		
		//selenium.click("//h3/a[contains(text(),client)]");	
		selenium.click("id=navForm:contactTab_lbl");
	    selenium.waitForPageToLoad("80000");
	    selenium.click("navForm:newClientBtn");
	    selenium.waitForPageToLoad("80000");
	    selenium.type("navForm:address", email);
	    selenium.type("navForm:name", "Test");
	    if (typeName.contentEquals("Strategy")){
	        selenium.select("navForm:selectOneMenu_eventNotification", "label=Strategy Initiated Notifications");
	        
	    }
	    else if (typeName.contentEquals("No")){   	
	        selenium.select("navForm:selectOneMenu_eventNotification", "label=No Notifications");
	        
	    }
	    else {
	        selenium.select("navForm:selectOneMenu_eventNotification", "label=All Notifications");
	       
	    }
	    if (Integer.parseInt(threshold) != 10) {
	    	if (selenium.isChecked("//input[@name='navForm:defaultMsgThreshold']")){ 					
	    		selenium.click("//input[@name='navForm:defaultMsgThreshold']");
	    	    selenium.type("navForm:threshold", threshold);
	    	}
	    }
	    selenium.click("//input[@type='submit' and @value='Create Contact']");
	    log.info("Added one contact for client: " + client + " email address: " + email);
	    selenium.waitForPageToLoad("30000");
		
		return true;
	}
	
	@Test
	public void testAddContacts() throws Exception {
		
		    String participantName[] = Participants.split(",");
		    String clientName[] = Clients.split(",");
		    String typeName[] = NotificationType.split(",");
		    String threshold[] = Threshold.split(",");
		    String email[] = EmailAddress.split(",");
		    String pw = "Test_1234";
		    
		    String component = "SCE:Operator Utility";
		    String test = "Verify Add Contacts: ";
		    
		    //We are getting the build from the About Page. 
		    String Build = getBuild();
		   
		    TestReport res = new TestReport("TestReport"+ Build + ".csv",Build,Server);
		    if (reportResults) res.openReport();
		   
		    
		    
		    selenium.setSpeed(MODERATE);
		  	
		    int count  = participantName.length;
		    try {
		    	
				for(int i = 0; i < count; i++ ) {
					loginFacDash (participantName[i], pw);
					if (addOneContact(participantName[i], clientName[i], email[i], typeName[i], threshold[i])) {
						Details = "Part: "	+ participantName[i] + " Client: " + clientName[i] + " Notification Type: "	+  typeName[i] + " Threshold: " +threshold[i] + " Email Address: " + email[i];
						if (reportResults) res.report(component, test + participantName[i], "Pass", Details);					
					}
					selenium.click("link=Logout");
				}
				if (reportResults)res.closeReport();
				
				log.info("Successfully exited the loop.");
				log.info("Logging Out");
				
			}catch(Exception e) {
				e.printStackTrace();
			}		    		   
		}
}
	