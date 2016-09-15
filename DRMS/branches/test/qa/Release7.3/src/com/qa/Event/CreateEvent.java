package com.qa.Event;

//7.3

import com.qa.Report.TestReport;
import com.thoughtworks.selenium.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.logging.Logger;

public class CreateEvent extends SeleneseTestCase {
//server.config has the information for these variables.
	String Build = "";
	String Server = "";
	String Username = "";
	String Password = "";
	String Programs = "";
	String DayOfDate = "";
	String DayAheadDate = "";
	String StartTime =  "";
	String EndTime = "";
	String Offset = "";
	String Participants = "";
	String Details = "";
	boolean reportResults = true;
	// since you can only pass by value in java,
	// this seems to be the easiest way
	String date = "";
	String start = "";
	String end = "";
	String offset = "";
	// SPEED SETTINGS FOR SELENIUM
	public static final String FAST = "100";
	public static final String VERYFAST = "50";
	public static final String MODERATE = "1000";
	public static final String SLOW = "3000";
	public static final String VERYSLOW = "5000";
	// for qa3
//	public static final String MODERATE = "3000";
//	public static final String SLOW = "5000";
//	public static final String VERYSLOW = "8000";
	//SPEED SETTINGS FOR THREAD
	public static final int TSLOW = 3000;
	
	
	

	Logger log = Logger.getLogger(CreateEvent.class.getSimpleName());
		
	
	private void readConfigFile() throws Exception {
		try{
		    
		    FileInputStream fstream = new FileInputStream("serverEvent.config");
		   
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    
		    Build = br.readLine();
		    Server = br.readLine();
		    Username = br.readLine();
		    Password = br.readLine();
		    Programs = br.readLine();
		    DayOfDate = br.readLine();
		    DayAheadDate = 	br.readLine();
		    StartTime =  br.readLine();
		    EndTime = br.readLine();
		    Offset = br.readLine();
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
	
	
public void createEvent(String programName, String date, String start, String end, String offset) throws Exception {
	
	start = "label=" + start;
	end = "label=" + end;
	offset = "label=" + offset;
	selenium.setSpeed(SLOW);
	selenium.click("link=Programs");
	selenium.waitForPageToLoad("30000");
	log.info("Beginning createEvent with " + programName );
	
	 int count = (selenium.getXpathCount("//tbody/tr")).intValue() - 2;
     String pFromProgramsPage = "";
   
    int i = 1;
     while ( i < count ){
    	 pFromProgramsPage = "";
         selenium.setSpeed(VERYFAST);
         if (selenium.isElementPresent("//tbody/tr[" + i + "]/td[2]/span/span")){
            
        	 pFromProgramsPage = selenium.getText("//tbody/tr[" + i + "]/td[2]/span/span");
             if (pFromProgramsPage.contentEquals(programName)) {
            	 log.info("Add Event " + programName );
                 selenium.setSpeed(FAST);
                 selenium.click("xpath=(//a[contains(text(),'Add event')])[" + i + "]");
                 selenium.waitForPageToLoad("30000");
                 selenium.click("id=timing-form:eventDateCalendarPopupButton");
                 selenium.type("id=timing-form:eventDateCalendarInputDate", date);
                 selenium.select("//tbody/tr/td/form/div[2]/div/table/tbody/tr[4]/td[2]/select[contains(@name,'24pc3')]", start);
                 selenium.select("//tbody/tr/td/form/div[2]/div/table/tbody/tr[4]/td[2]/select[contains(@name,'26pc3')]", offset);
                 selenium.select("//tbody/tr/td/form/div[2]/div/table/tbody/tr[5]/td[2]/select[contains(@name,'33pc3')]", end);
                 selenium.setSpeed(SLOW);
                   
                 selenium.click("//input[@type='button' and @title='Next Step']"); 
                 Thread.sleep(TSLOW);
                 if (selenium.isTextPresent("Enroll")) 
                	 selenium.click("//input[@type='button' and @value='Issue Event']");
                 Thread.sleep(TSLOW);
                 if (selenium.isTextPresent("Pending")) 
                	 selenium.click("//input[@type='button' and @value='Confirm']");
             	 selenium.waitForPageToLoad("30000");
             	 log.info("Complete Add Event " + programName );
             	 Thread.sleep(TSLOW);
             	 selenium.setSpeed(FAST);
                 i = count;
             }
             i++;
         }
        
         else i++;
     }
     log.info("Ending createEvent with " + programName );
}
public void createEventWithOneParticipant(String participant, String programName, String date, String start, String end, String offset) throws Exception {
	
	start = "label=" + start;
	end = "label=" + end;
	offset = "label=" + offset;
	selenium.setSpeed(SLOW);
	selenium.click("link=Programs");
	selenium.waitForPageToLoad("30000");
	log.info("Beginning createEvent with " + programName + " and " + participant  );
	
	 int count = (selenium.getXpathCount("//tbody/tr")).intValue() - 2;
     String pFromProgramsPage = "";
   
    int i = 1;
     while ( i < count ){
    	 pFromProgramsPage = "";
         selenium.setSpeed(VERYFAST);
        
         if (selenium.isElementPresent("//tbody/tr[" + i + "]/td[2]/span/span")){
            
        	 pFromProgramsPage = selenium.getText("//tbody/tr[" + i + "]/td[2]/span/span");
             if (pFromProgramsPage.contentEquals(programName)) {
            	 log.info("Add Event " + programName );
                 selenium.setSpeed(FAST);
                 selenium.click("xpath=(//a[contains(text(),'Add event')])[" + i + "]");
                 selenium.waitForPageToLoad("30000");
                 selenium.click("id=timing-form:eventDateCalendarPopupButton");
                 selenium.type("id=timing-form:eventDateCalendarInputDate", date);
                 selenium.select("//tbody/tr/td/form/div[2]/div/table/tbody/tr[4]/td[2]/select[contains(@name,'24pc3')]", start);
                 selenium.select("//tbody/tr/td/form/div[2]/div/table/tbody/tr[4]/td[2]/select[contains(@name,'26pc3')]", offset);
                 selenium.select("//tbody/tr/td/form/div[2]/div/table/tbody/tr[5]/td[2]/select[contains(@name,'33pc3')]", end);
                 selenium.setSpeed(SLOW);
                   
                 selenium.click("//input[@type='button' and @title='Next Step']"); 
                 Thread.sleep(TSLOW);
                 Thread.sleep(TSLOW);
                 selenium.click("//input[@type='checkbox'][@class='check-select-all'] ");
                 Thread.sleep(TSLOW);
         		selenium.click("id=selected-part-form:remove-selected-btn");
         		selenium.click("id=selected-part-form:enroll-more-button");
         //		selenium.waitForPageToLoad("30000");
         		selenium.type("//table[@id='participant-form:reject-filter-block']/tbody/tr/td[5]/input", participant); 
         		selenium.click("id=participant-form:apply-reject-filter"); //hit filter button
         		selenium.click("name=participant-form:rejectedParticipantList_selected");  // checked top participant in pop-up
         		selenium.click("id=participant-form:enroll-selected-btn");
         		selenium.click("id=participant-form:apply-reject-filter");
         //		selenium.waitForPageToLoad("30000");
                selenium.click("//input[@type='button' and @value='Issue Event']");
                 Thread.sleep(TSLOW);
                 if (selenium.isTextPresent("Pending")) 
                	 selenium.click("//input[@type='button' and @value='Confirm']");
             	 selenium.waitForPageToLoad("30000");
             	 log.info("Complete Add Event " + programName );
             	 Thread.sleep(TSLOW);
             	 selenium.setSpeed(FAST);
                 i = count;
             }
             i++;
         }
        
         else i++;
     }
     log.info("Ending createEvent with " + programName );
}
public void createRTPEvent(String programName) throws Exception {
	
	selenium.click("link=Programs");
	selenium.waitForPageToLoad("30000");
	int count = (selenium.getXpathCount("//tbody/tr")).intValue() - 2;
	String pFromProgramsPage = "";
	log.info("Beginning createRTPEvent with " + programName );
	
	for (int i = 1; i < count; i++) {
		pFromProgramsPage = "";
		selenium.setSpeed(VERYFAST);
		if (selenium.isElementPresent("//tbody/tr[" + i + "]/td[2]/span/span")){

			pFromProgramsPage = selenium.getText("//tbody/tr[" + i + "]/td[2]/span/span");
			if (pFromProgramsPage.contentEquals(programName)) {
				log.info("Add Event " + programName );
				selenium.setSpeed(FAST);
				selenium.click("xpath=(//a[contains(text(),'Add event')])[" + i + "]");
				selenium.waitForPageToLoad("30000");
				Thread.sleep(TSLOW);
				selenium.click("//input[@type='submit' and @value='Confirm']");
				Thread.sleep(TSLOW);
				i = count;
                
			} //if
		} //if
	} //for
	log.info("Ending createRTPEvent with " + programName );
}

public void createDemoEvent (String programName) throws Exception {
	
	selenium.click("link=Programs");
	selenium.waitForPageToLoad("30000");
	int count = (selenium.getXpathCount("//tbody/tr")).intValue() - 2;
	String pFromProgramsPage = "";
	log.info("Beginning createDemoEvent with " + programName );
	
	for (int i = 1; i < count; i++) {
		pFromProgramsPage = "";
		selenium.setSpeed(VERYFAST);
		if (selenium.isElementPresent("//tbody/tr[" + i + "]/td[2]/span/span")){

			pFromProgramsPage = selenium.getText("//tbody/tr[" + i + "]/td[2]/span/span");
			if (pFromProgramsPage.contains(programName)) {
				log.info("Add Event " + programName );
				selenium.setSpeed(FAST);
				selenium.click("xpath=(//a[contains(text(),'Add event')])[" + i + "]");
				selenium.waitForPageToLoad("30000");
				Thread.sleep(TSLOW);
				selenium.click("//input[@type='button' and @value='Issue Event']");
				Thread.sleep(TSLOW);
				selenium.click("//input[@type='button' and @value='Confirm']");
				Thread.sleep(TSLOW);
				i = count;
                
			} //if
		} //if
	} //for	
	log.info("Ending createDemoEvent with " + programName );
}


public void checkEvent(String programName, String date, String start, String end, String offset) throws Exception {
	
	// should already be logged in, probably on the events page
	//selenium.isTextPresent(programName)
    // should verify that we are on Events page
   
	boolean eventFound = false;
	String pFromEventsPage = "";
	String sFromEventsPage = "";
	String sDateTime = date + " " + start + ":" + offset ;
	log.info("Check Event " + programName );
	
	int count =   (selenium.getXpathCount("//tr/td[3]/div/span")).intValue();
	for ( int i = 1; i <= count; i++){
		selenium.setSpeed(FAST);
		pFromEventsPage = selenium.getText("//tr[" + i + "]/td[3]/div/span");
		sFromEventsPage = selenium.getText("//tr[" + i + "]/td[5]/div/span");

		if ( pFromEventsPage.contentEquals(programName)){
			// RTP events pick up the time 
			if (programName.contains("RTP")) {
				eventFound = true;
				log.info("Event Found " + programName );
				i = count +1;
			} else if (programName.toUpperCase().contains("DEMO") ) {
				eventFound = true;
				log.info("Event Found " + programName );
				i = count +1;
			} else if (sFromEventsPage.contentEquals(sDateTime)) {
				eventFound = true;
				log.info("Event Found " + programName );
				log.info("Start Time " + sDateTime );
				i = count +1;
			} //if
		} //if

	} // for loop
	log.info("Check Event " + programName + " Event Found: " + eventFound );
	assertTrue(eventFound);	
	
}



public boolean setParameters (String programName) throws Exception {
	boolean programFound = false;
	if (programName.contains("CBP")){
		start = StartTime;
		offset = Offset;
		programFound = true;
		if (programName.contains("4-8"))end = Integer.toString(Integer.parseInt(StartTime)+ 4);
		if (programName.contains("DO")) date = DayOfDate;
		else if (programName.contains("DA")) date = DayAheadDate;
		else programFound = false;
	}
	else if (programName.contains("DO")){
		date = DayOfDate;
		start = StartTime;
		end = EndTime;
		offset = Offset;
		programFound = true;
		
	}else if ((programName.contains("DA"))){
		date = DayAheadDate;
		start = StartTime;
		end = EndTime;
		offset = Offset;
		programFound = true;
	}
	else if (programName.contains("CPP")){
		date = DayAheadDate;
		start = "14";
		end = "18";
		offset = "00";
		programFound = true;
	} 
	return programFound;
		
}


	@Test
public void testCreateEvents() throws Exception {
	    String[] programName = Programs.split(",");
	    String[] participantName =Participants.split(",");
	   
	    date = DayOfDate;
	    offset = Offset;
	    selenium.setSpeed(VERYFAST);
	    
	    String component = "SCE:Operator Utility";
	    String test = "Verify Add Events: ";
	    
	    //We are getting the build from the About Page. 
	    Build = getBuild();
	   
	    TestReport res = new TestReport("TestReport"+ Build + ".csv",Build,Server);
	    if (reportResults) res.openReport();
	   
	    
	   
	    int count  = programName.length;
	    try {
	    	loginAb();
			for(int i = 0; i < count; i++ ) {
				
				start = StartTime;
			    end = EndTime;
				if (setParameters(programName[i])) 
					createEventWithOneParticipant(participantName[i], programName[i], date, start, end, offset);
				/*else if (program.contains("RTP")) createRTPEvent(programName[i]);
				else if (program.toUpperCase().contains("DEMO") ) createDemoEvent(program);
				else {
					log.info("Program, " + program + ", not found.");
					continue;				
				}	*/			
				checkEvent(programName[i], date, start, end, offset);
				Details = "Created event with one participant: " + participantName[i];
				if (reportResults) res.report(component, test + programName[i], "Pass", Details);
			}
			if (reportResults)res.closeReport();
			selenium.click("link=Logout");
			log.info("Logging Out");
		}catch(Exception e) {
			e.printStackTrace();
		}
	   
	}

	@After
	public void tearDown() throws Exception {
	    selenium.stop();
	}
	
	
} // close CreateEvent class


	