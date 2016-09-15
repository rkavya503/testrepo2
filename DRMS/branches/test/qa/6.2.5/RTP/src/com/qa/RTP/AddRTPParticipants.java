package com.qa.RTP;


import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
//import java.util.regex.Pattern;
import java.io.*;



public class AddRTPParticipants extends SeleneseTestCase {
//server.config has the information for these variables.
	String Server = "";
	String Username = "";
	String Password = "";
	String Contactemail = "";	
	
private void readConfigFile() throws Exception {
	try{
	    
	    FileInputStream fstream = new FileInputStream("server.config");
	   
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    
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
    selenium.setSpeed("50");
    selenium.start();
}
public void loginAb() throws Exception {
    selenium.open("/pss2.website/Login");
    selenium.type("fm-username", Username);
    selenium.type("fm-password", Password);
    selenium.click("Submit");
    selenium.waitForPageToLoad("30000");

}

private void createContact(String threshold) throws Exception {
    // selenium.click("//a//h4[text()='Contacts']");
    selenium.open("/facdash/jsp/client-edit.jsf");
    selenium.waitForPageToLoad("80000");
    selenium.click("//a//h4[text()='Contacts']");
    selenium.waitForPageToLoad("80000");
    selenium.click("navForm:newClientBtn");
    selenium.waitForPageToLoad("80000");
    selenium.type("navForm:address", Contactemail);
    selenium.type("navForm:name", "Z");
    if (threshold == "55" || threshold == "44")
        selenium.select("navForm:selectOneMenu_eventNotification", "label=Strategy Initiated Notifications");
    else
        selenium.select("navForm:selectOneMenu_eventNotification", "label=All Notifications");
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

    } else if (participantName == "Zachary") {
        createAdvancedStrategy("0.01", "0.06");
        saveStrategy();   
        modifyAdvancedStrategy("advanced", 13, "0.06", "0.09" );
        saveStrategy();   
        modifyAdvancedStrategy("advanced", 23, "0.06", "0.09" );
        saveStrategy();
        
    } else if (participantName == "Zeke") {
        createAdvancedStrategy("0.06", "0.09");
        saveStrategy();
        createSimpleStrategy ("0.01", "0.06");
        saveStrategy(participantName);

    } else if (participantName == "Zorro") {
        createSimpleStrategy ("0.01", "0.06");
        saveStrategy();
    } else if (participantName == "Zombie") {
        if (threshold == "55") {
            createAdvancedStrategy("0.06", "0.09");
            saveStrategy();
        }
    }else if (participantName == "Zuma") {
        createSimpleStrategy ("0.01", "0.06");
        saveStrategy();
        createAdvancedStrategy("0.06", "0.09");
        saveStrategy();   
        modifyAdvancedStrategy("advanced", 13, "0.01", "0.06" );
        saveStrategy();   
        modifyAdvancedStrategy("advanced", 23, "0.01", "0.06" );
        saveStrategy(participantName);
    }

}
// https://204.236.242.62:8443/facdash/jsp/scertp-shed-strategy-edit.jsf
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
    selenium.click("//input[@type='radio' and @value = 'ADVANCED']");
    selenium.type("navForm:name", "advanced");
    selenium.type("navForm:hours:0:moderate", moderate);
    selenium.type("navForm:hours:0:high", high);
    selenium.setSpeed("50");
    for ( int i = 1; i < 24; i++ ) {
        selenium.type("navForm:hours:" + Integer.toString(i) + ":moderate", moderate);
        selenium.type("navForm:hours:" + Integer.toString(i) + ":high", high);
    }
    selenium.setSpeed("5");
    selenium.click("//input[@type='submit' and @value='Create Strategy']");
    selenium.waitForPageToLoad("80000");


}
private void modifyAdvancedStrategy ( String strategyName, int row, String moderate, String high) {

    selenium.open("/facdash/jsp/scertp-shed-strategy-edit.jsf?strategyName=" + strategyName );
    selenium.waitForPageToLoad("80000");
    selenium.setSpeed("1000");
    selenium.type("navForm:hours:" + Integer.toString(row) + ":moderate", moderate);
    selenium.type("navForm:hours:" + Integer.toString(row) + ":high", high);
//when you access the page this way, the button is Create instead of Update
//    selenium.click("//input[@type='submit' and @value='Create Strategy']");
    selenium.click("//input[@type='submit']");
    selenium.setSpeed("1");
    selenium.waitForPageToLoad("80000");

}

private void saveStrategy (){
    selenium.open("/facdash/jsp/scertp-shed-strategies.jsf");
    selenium.click("//input[@type='submit' and @value='Save Strategies']");
    selenium.waitForPageToLoad("80000");
}

private void saveStrategy (String participantName){
    selenium.open("/facdash/jsp/scertp-shed-strategies.jsf");
    selenium.setSpeed("1000");
    //when checked, checked="checked"  title="Weekend","Winter", "Summer"    
      //row 1, summer, winter, weekend
    //      selenium.click("//tbody[@id='navForm:strategies:tb']/tr[1]/td[4]/input");
    //      selenium.click("//tbody[@id='navForm:strategies:tb']/tr[1]/td[5]/input");
    //      selenium.click("//tbody[@id='navForm:strategies:tb']/tr[1]/td[6]/input");
        //row 2, summer, winter, weekend
    //      selenium.click("//tbody[@id='navForm:strategies:tb']/tr[2]/td[4]/input");
    //      selenium.click("//tbody[@id='navForm:strategies:tb']/tr[2]/td[5]/input");
    if (participantName == "Zelda" )
        selenium.click("//tbody[@id='navForm:strategies:tb']/tr[2]/td[6]/input[@type='checkbox' and @title='Weekend']");
    else if (participantName == "Zeke" ) {
        selenium.click("//tbody[@id='navForm:strategies:tb']/tr[2]/td[4]/input[@type='checkbox' and @title='Summer']");
        selenium.click("//tbody[@id='navForm:strategies:tb']/tr[2]/td[5]/input[@type='checkbox' and @title='Winter']");
        selenium.click("//tbody[@id='navForm:strategies:tb']/tr[2]/td[6]/input[@type='checkbox' and @title='Weekend']");
    }
    selenium.click("//input[@type='submit' and @value='Save Strategies']");
    selenium.setSpeed("1");
    selenium.waitForPageToLoad("80000");

}

private void createRTPClient(String participantName, String threshold) throws Exception {
    String password = "Test_1234";
    selenium.open("/pss2.website/participantsMap.do");
    selenium.waitForPageToLoad("25000");
    selenium.open("/facdash/jsp/clients.jsf?user=" + participantName );
    selenium.waitForPageToLoad("30000");
    selenium.click("//input[@value='New Client']");
    selenium.waitForPageToLoad("30000");
    if (threshold == "44")
        selenium.type("navForm:name", "2");
    else
        selenium.type("navForm:name", "1");
    selenium.type("navForm:password", password);
    selenium.type("navForm:confirm", password);
    selenium.click("//input[@value='Create Client']");
    selenium.waitForPageToLoad("25000");
    selenium.click("//a[@id='navForm:client_programs_href']/h4");
    selenium.waitForPageToLoad("80000");
    createContact(threshold);
    selenium.setSpeed("1000");
    selenium.click("//a[@id='navForm:client_programs_href']/h4");
    selenium.waitForPageToLoad("80000");
    selenium.setSpeed("50");
    selenium.click("//input[@type='checkbox'] [@title='Participate Program']");
    createShedStrategy (participantName, threshold);
    selenium.click("link=Done");
    selenium.waitForPageToLoad("80000");
}

private void createRTPParticipant(String participantName, String programName) throws Exception{
    String password = "Test_1234";
    selenium.waitForPageToLoad("30000");
    selenium.click("link=Add |");
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
    selenium.waitForPageToLoad("25000");
}


@Test
public void testCreateZParticipants() throws Exception {
    String[] participantName = {"Zelda", "Zachary", "Zeke", "Zorro", "Zombie", "Zuma"};
    String [] programName = {"RTP <2K", "RTP 2K-50K", "RTP >50K", "RTP Agricultural", "RTP <2k B", "RTP <2K"};
    String[] threshold = {"99", "88", "77", "66", "55", "44","33"};

    int clientCount = 6;
    try {
        loginAb();
        for(int i = 0; i < clientCount; i++ ) {
            createRTPParticipant(participantName[i], programName[i]);
            createRTPClient(participantName[i], threshold[i]);
            if (threshold[i].contains("55"))
                createRTPClient(participantName[i], threshold[i+1]);

        }
    }catch(Exception e) {
        e.printStackTrace();
    }
}


@After
public void tearDown() throws Exception {
    selenium.stop();
}
}
