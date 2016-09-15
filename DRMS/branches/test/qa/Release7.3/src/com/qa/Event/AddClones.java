package com.qa.Event;

//7.3

import com.qa.Report.*;
import com.thoughtworks.selenium.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.logging.Logger;


public class AddClones extends SeleneseTestCase {
//server.config has the information for these variables.
	
	String Build = ""; //We are getting the build from the About page now.
	String Server = "";
	String Username = "";
	String Password = "";
	String Programs = "";
	String Clones = "";
	String Details = "";
	//reports results in a *.csv file
	boolean reportResults = true;
	
	// SPEED SETTINGS FOR SELENIUM
	public static final String FAST = "100";
	public static final String VERYFAST = "50";
	public static final String MODERATE = "1000";
	public static final String SLOW = "3000";
	public static final String VERYSLOW = "5000";
	//SPEED SETTINGS FOR THREAD
	public static final int TSLOW = 3000;
	

	Logger log = Logger.getLogger(AddParticipants.class.getSimpleName());
	
	private void readConfigFile() throws Exception {
		try{
		    
		    FileInputStream fstream = new FileInputStream("serverClones.csv");
		   
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
		    Programs = br.readLine();
		    Clones = br.readLine();
		   
		   
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
	
	private boolean addOneClone(String program, String clone) throws Exception{
			
	    String pFromProgramsPage = "";
	   	boolean result = false;    
	    selenium.click("link=Programs");
	    selenium.waitForPageToLoad("30000");
	    int count = (selenium.getXpathCount("//tbody/tr/td[2]")).intValue();
	    
	    int i = 1;
	    while ( i < count ){
	    	selenium.setSpeed(VERYFAST);
	    	if (selenium.isElementPresent("//tbody/tr[" + i + "]/td[2]/span/span")){
	    		pFromProgramsPage = selenium.getText("//tbody/tr[" + i + "]/td[2]/span/span");
	    		if (pFromProgramsPage.contentEquals(program)) {
	    			log.info("Add Clone for:  " + program );
	    			selenium.setSpeed(FAST);
	    			selenium.click("xpath=(//a[contains(text(),'Clone')])[" + i + "]");
	    			selenium.waitForPageToLoad("30000");
	    			selenium.type("name=programNameClone", clone);
	    			selenium.click("//input[@type='submit']");
	    			selenium.waitForPageToLoad("30000");
	    			log.info("Added Clone: " + clone);
	    			Details = "Added " + clone;
	    			result = true;
	    			Thread.sleep(TSLOW);
	    			count = i;
	    		} //if
	    	} //if
	    	i++;
	    } //while
	    return result;
	}
		
	@Test
	public void testAddClones() throws Exception {
		
		    String cloneName[] = Clones.split(",");
		    String programName[] = Programs.split(",");
		    String component = "SCE:Operator Utility";
		    String test = "Verify Add Clones: ";
		    //We are getting the build from the About page now.
		    Build = getBuild();
		   
		    TestReport res = new TestReport("TestReport"+ Build + ".csv",Build,Server);
		    if (reportResults)res.openReport();
		    
		    
		    selenium.setSpeed(MODERATE);
		  	
		    int count  = programName.length;
		    try {
		    	loginAb();
				for(int i = 0; i < count; i++ ) {
					Details = "";
					if (addOneClone(programName[i], cloneName[i])) {
						if (reportResults)res.report(component, test + programName[i], "Pass", Details);
					} else if (reportResults) res.report(component, test + programName[i], "Fail", Details);
					// one clone for each program
					Thread.sleep(TSLOW);
					
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
