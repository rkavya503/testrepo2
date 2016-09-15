package com.qa.RTP;
//7.3

import com.qa.Report.*;
import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;



public class AddRTPParticipants extends SeleneseTestCase {
//server.config has the information for these variables.
	String Build = "";  //We are getting the build from the About page now.
	String Server = "";
	String Username = "";
	String Password = "";
	String Contactemail = "";
	String Details = "";
	String Details_Notifications = "";
	String Details_ShedStrategies = "";
	
	boolean reportResults = true;
	
private void readConfigFile() throws Exception {
	try{
	    
	    FileInputStream fstream = new FileInputStream("serverRTP7.config");
	   
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    
	    Build = br.readLine();
	    Server = br.readLine();
	    Username = br.readLine();
	    Password = br.readLine();
	    Contactemail = br.readLine();
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
    selenium.setSpeed("1000");
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

private void createContact(String threshold) throws Exception {
	
	selenium.click("id=navForm:contactTab_lbl");
    selenium.waitForPageToLoad("80000");
    selenium.click("navForm:newClientBtn");
    selenium.waitForPageToLoad("80000");
    selenium.type("navForm:address", Contactemail);
    selenium.type("navForm:name", "Z");
    if (threshold == "55" || threshold == "44"){
        selenium.select("navForm:selectOneMenu_eventNotification", "label=Strategy Initiated Notifications");
        Details_Notifications = Details_Notifications + ": Strategy Initiated Notifications ";
    }
    else if (threshold == "33"){   	
        selenium.select("navForm:selectOneMenu_eventNotification", "label=No Notifications");
        Details_Notifications = Details_Notifications + ": No Notifications ";
    }
    else {
        selenium.select("navForm:selectOneMenu_eventNotification", "label=All Notifications");
        Details_Notifications = Details_Notifications + ": All Notifications ";
    }
    selenium.type("navForm:threshold", threshold);
    selenium.click("//input[@type='submit' and @value='Create Contact']");
    selenium.waitForPageToLoad("30000");
}
private void createShedStrategy (String participantName, String threshold){
	
    if (participantName == "Zelda") {
        createSimpleStrategy ("0.01", "0.06");
        saveStrategy();
        createAdvancedStrategy("0.06", "0.09");
        saveStrategy(participantName);
        Details_ShedStrategies = Details_ShedStrategies + " SS Simple 0.01-0.06:WE checked Adv 0.06-0.09";

    } else if (participantName == "Zachary") {
        createAdvancedStrategy("0.01", "0.06");
        saveStrategy();   
        modifyAdvancedStrategy("advanced", 13, "0.06", "0.09" );
        saveStrategy();   
        modifyAdvancedStrategy("advanced", 23, "0.06", "0.09" );
        saveStrategy();
        Details_ShedStrategies = Details_ShedStrategies + " SS Adv 0.01-0.06:13:00 23:00 0.06-0.09";
        
    } else if (participantName == "Zeke") {
        createAdvancedStrategy("0.06", "0.09");
        saveStrategy();
        createSimpleStrategy ("0.01", "0.06");
        saveStrategy(participantName);
        Details_ShedStrategies = Details_ShedStrategies + " SS Adv 0.06-0.09:Simple checked 0.06-0.09";
        
    } else if (participantName == "Zorro") {
        createSimpleStrategy ("0.01", "0.06");
        saveStrategy();
        Details_ShedStrategies = Details_ShedStrategies + " SS Simple 0.01-0.06";
        
    } else if (participantName == "Zombie") {
        if (threshold == "55") {
            createSimpleStrategy("0.01", "0.09");
            saveStrategy();
            Details_ShedStrategies = Details_ShedStrategies + " C1: SS Simple 0.01-0.09";
        } else if (threshold == "33") {
        	createSimpleStrategy ("0.01", "0.06");
            saveStrategy();
            Details_ShedStrategies = Details_ShedStrategies + " C3: SS Simple 0.01-0.06";
        }
    }
    
}


private void createSimpleStrategy (String moderate, String high){

    selenium.click("//input[@type='submit' and @value='Shed Strategies']");
    selenium.waitForPageToLoad("80000");
    selenium.click("//input[@type='submit' and @value='New Strategy']");
    selenium.waitForPageToLoad("80000");
    selenium.click("//input[@type='radio' and @value = 'SIMPLE']");
    selenium.type("navForm:name", "simple");
    selenium.type("navForm:hours:0:moderate", moderate);
    selenium.type("navForm:hours:0:high", high);
    selenium.click("//input[@type='submit' and @value='Create Strategy']");
    selenium.waitForPageToLoad("80000");

}
private void createAdvancedStrategy ( String moderate, String high) {
   
    selenium.click("//input[@type='submit' and @value='Shed Strategies']");
    selenium.waitForPageToLoad("80000");
    selenium.click("//input[@type='submit' and @value='New Strategy']");
    selenium.waitForPageToLoad("80000");
    selenium.setSpeed("1000");
    selenium.click("navForm:radioButtonStrategy:1");
    selenium.type("navForm:name", "advanced");
    selenium.type("navForm:hours:0:moderate", moderate);
    selenium.type("navForm:hours:0:high", high);
    selenium.setSpeed("50");
    for ( int i = 1; i < 24; i++ ) {
        selenium.type("navForm:hours:" + Integer.toString(i) + ":moderate", moderate);
        selenium.type("navForm:hours:" + Integer.toString(i) + ":high", high);
    }
    selenium.setSpeed("100");
    selenium.click("//input[@type='submit' and @value='Create Strategy']");
    selenium.waitForPageToLoad("80000");


}
private void modifyAdvancedStrategy ( String strategyName, int row, String moderate, String high) {

	selenium.click("//input[@type='submit' and @value='Shed Strategies']");
	selenium.waitForPageToLoad("80000");
	selenium.click("//a[contains(text(), 'advanced')] ");
    selenium.waitForPageToLoad("80000");
    selenium.setSpeed("1000");
    selenium.type("navForm:hours:" + Integer.toString(row) + ":moderate", moderate);
    selenium.type("navForm:hours:" + Integer.toString(row) + ":high", high);
    selenium.click("//input[@type='submit']");
    selenium.setSpeed("100");
    selenium.waitForPageToLoad("80000");

}

private void saveStrategy (){

    selenium.click("//input[@type='submit' and @value='Save Strategies']");
    selenium.waitForPageToLoad("80000");
}

private void saveStrategy (String participantName){

    selenium.setSpeed("1000");
    
    if (participantName == "Zelda" )
    	 selenium.click("//tbody/tr[2]/td[6]/input[@type='checkbox' and @title='Weekend']");
    else if (participantName == "Zeke" ) {
        selenium.click("//tbody[@id='navForm:strategies:tb']/tr[2]/td[4]/input[@type='checkbox' and @title='Summer']");
        selenium.click("//tbody[@id='navForm:strategies:tb']/tr[2]/td[5]/input[@type='checkbox' and @title='Winter']");
        selenium.click("//tbody[@id='navForm:strategies:tb']/tr[2]/td[6]/input[@type='checkbox' and @title='Weekend']");
    }
    selenium.click("//input[@type='submit' and @value='Save Strategies']");
    selenium.setSpeed("100");
    selenium.waitForPageToLoad("80000");

}

private void createRTPClient(String participantName, String threshold) throws Exception {
    String password = "Test_1234";
    selenium.setSpeed("3000");
  
    selenium.waitForPageToLoad("50000");
    selenium.click("//input[@value='New Client']");
    selenium.waitForPageToLoad("50000");
    if (threshold == "44"){
        selenium.type("navForm:name", "2");
        Details = Details + " C2: ";
        Details_Notifications = Details_Notifications + " C2: ";
    }
    else if (threshold == "33" ){
    	selenium.type("navForm:name", "3");
    	Details = Details + " C3: ";
    	Details_Notifications = Details_Notifications + " C3: ";
    }
    else {	
        selenium.type("navForm:name", "1");
        Details = Details + " C1: ";
        Details_Notifications = Details_Notifications + " C1: ";
    }
    selenium.type("navForm:password", password);
    selenium.type("navForm:confirm", password);
    selenium.click("//input[@value='Create Client']");
    selenium.waitForPageToLoad("25000");
 
    selenium.click("id=navForm:client_programs_href");

    selenium.waitForPageToLoad("80000");
    
    createContact(threshold);
    selenium.setSpeed("1000");

    selenium.click("id=navForm:client_programs_href");

    selenium.waitForPageToLoad("80000");
   
    if (!selenium.isChecked("//input[@type='checkbox'] [@title='Participate Program']"))
    	selenium.click("//input[@type='checkbox'] [@title='Participate Program']");
    
    createShedStrategy (participantName, threshold);
    selenium.setSpeed("100");
    Details = Details + "Client added";
 
}

private void createRTPParticipant(String participantName, String programName) throws Exception{
    String password = "Test_1234";
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
    selenium.click("//input[@name='programs'][@value=" + "'" + programName + "']");
    selenium.click("//input[@name='clientConfig'][@value=" + "'" + programName + "']");
    selenium.click("//input[@name='dataEnabler']");
    selenium.click("//input[@value='Update']");
    selenium.waitForPageToLoad("50000");
    Details = "Part: " + participantName;
}


@Test
public void testCreateZParticipants() throws Exception {
    String[] participantName = {"Zelda", "Zachary", "Zeke", "Zorro", "Zombie"};
    String [] programName = {"RTP <2KV", "RTP 2KV-50KV", "RTP >50KV", "RTP Agricultural", "RTP <2KV", "RTP <2KV"};
    String[] threshold = {"99", "88", "77", "66", "55", "44","33","22"};

    
    String component = "SCE:RTP";
    String test = "Verify Add RTP Participants and Clients: ";
    String test_SS = "Verify Add Shed Strategies: ";
    String test_Notifications = "Verify Add Notifications: ";
  //We are getting the build from the About page now.
    Build = getBuild();
    TestReport res = new TestReport("TestReport"+ Build + ".csv",Build,Server);
    if (reportResults)res.openReport();
    
    
    int clientCount = 5;
    try {
  
        for(int i = 0; i < clientCount; i++ ) {
        	Username = "admin";
            loginAb();
            
            createRTPParticipant(participantName[i], programName[i]);
            Details_ShedStrategies = "Part: " + participantName[i];
            Details_Notifications = "Part: " + participantName[i];
            selenium.click("link=Logout");
		//	selenium.waitForPageToLoad("5000");
            Username = participantName[i];
            loginAb();
            createRTPClient(participantName[i], threshold[i]);
            
            if (threshold[i].contains("55")){
            	selenium.click("id=navForm:navClients");
            	selenium.waitForPageToLoad("5000");
            	createRTPClient(participantName[i], threshold[i+1]); 
            	selenium.click("id=navForm:navClients");
            	selenium.waitForPageToLoad("5000");
                createRTPClient(participantName[i], threshold[i+2]);
            }
            if (reportResults)res.report(component, test + programName[i], "Pass", Details);
            if (reportResults)res.report(component, test_SS + programName[i], "Pass", Details_ShedStrategies);
            if (reportResults)res.report(component, test_Notifications + programName[i], "Pass", Details_Notifications);
            selenium.click("link=Logout");
			selenium.waitForPageToLoad("5000");

        }
        if (reportResults)res.closeReport();
    }catch(Exception e) {
        e.printStackTrace();
    }
}


@After
public void tearDown() throws Exception {
    selenium.stop();
}
}
