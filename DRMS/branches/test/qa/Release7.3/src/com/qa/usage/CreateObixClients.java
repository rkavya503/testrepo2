package com.qa.usage;
//7.3

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

//import com.akuacom.test.TestUtil;

public class CreateObixClients extends SeleneseTestCase {
		
	String Server = "";
	String Username = "";
	String Password = "";
	String Contactemail = "";	
	String PartPrefix = "";
	String ClientPrefix = "";
	String AccountPrefix = "";
	int Count = 0;
	String Testtype = "";
	
	
	Logger log = Logger.getLogger(CreateObixClients.class.getSimpleName());
	
	private void readConfigFile() throws Exception {
		try{
		    
		    FileInputStream fstream = new FileInputStream("serverUsage7.config");
		   
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    
		    Server = br.readLine();
		    Username = br.readLine();
		    Password = br.readLine();
		    Contactemail = br.readLine();
		    PartPrefix = br.readLine();
		    ClientPrefix = br.readLine();
		    AccountPrefix = br.readLine();
		    Count = Integer.parseInt( br.readLine());
		    Testtype = br.readLine();

		    //Close the input stream
		    in.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
	}
			
    @Before
    public void setUp() {
        log.info("setting up");
        
        try {
        	readConfigFile();
        } catch (Exception e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
        selenium = new DefaultSelenium("localhost", 4444, "*chrome", Server);
        selenium.setSpeed("50");
        selenium.start();
        log.info("selenium " + selenium);
        log.info("selenium.getLocation() " +selenium.getLocation());
    }

    public void loginAb() throws Exception {
        log.info("opening url");
        selenium.open("/pss2.website/Login");
        log.info("selenium.getLocation() " +selenium.getLocation());
        selenium.type("fm-username", Username);
        selenium.type("fm-password", Password);
        selenium.click("Submit");
        selenium.waitForPageToLoad("30000");
    }
    
    private void createUDParticipant(String participantName, String password, String acctName) throws Exception{ 
     ///   selenium.open("/pss2.website/uoProgram.do");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Participants");
        selenium.waitForPageToLoad("30000");
        log.info("selenium.getTitle() " + selenium.getTitle());
        selenium.click("//a[@href='/pss2.website/commDevDetail.do?dispatch=create']");
        selenium.waitForPageToLoad("30000");
        selenium.type("//input[@name='userName']", participantName);
        selenium.type("//input[@name='password']", password);
        selenium.type("//input[@name='password2']", password);
        selenium.type("//input[@name='accountNumber']", acctName);
        selenium.click("//input[@value='Save']");
        selenium.waitForPageToLoad("25000");
        selenium.click("//input[@name='programs'][@value='CPP']");
        selenium.click("//input[@name='clientConfig'][@value='CPP']");
        //this is for usage, it can be removed
        selenium.click("//input[@name='dataEnabler']");
        
        selenium.click("//input[@value='Update']");
        selenium.waitForPageToLoad("25000");
    }
    
    private void createUDClient(String participantName, String clientName, String password) throws Exception {
        selenium.open("/pss2.website/participantsMap.do");
        selenium.waitForPageToLoad("50000");
        selenium.open("/facdash/jsp/clients.jsf?user=" + participantName );
        selenium.waitForPageToLoad("50000");
        selenium.click("//input[@value='New Client']");
        selenium.waitForPageToLoad("30000");
        selenium.type("navForm:name", clientName);
        selenium.type("navForm:password", password);
        selenium.type("navForm:confirm", password);
        selenium.click("//input[@value='Create Client']");
        selenium.waitForPageToLoad("25000");
        
        //to put clients in program
        selenium.setSpeed("1000");
        //selenium.click("//a[@id='navForm:client_programs_href']/h4");
        selenium.click("//a[@id='navForm:client_programs_href']");
        selenium.waitForPageToLoad("80000");
        if (!selenium.isChecked("//input[@type='checkbox'] [@title='Participate Program']"))
        	selenium.click("//input[@type='checkbox'] [@title='Participate Program']");
        selenium.setSpeed("100");
        // end of putting clients in program       
        selenium.click("link=Done");
     }
    
    
    String num(int n, int width) {
        String s = ""+n;
        while(s.length() < width) {
            s = "0" + s;
        }
        return s;
    }
    
    public void createClientsAndOneParticipant() {
        String participantPrefix = "testG";
        String clientPrefix = "testG";
        String password = "Test_1234";
        String acctPrefix = "TestAcct";
        int clientCount = 100;
        try {
            loginAb();
            log.info("logged in");
            createUDParticipant(participantPrefix + "0", password, acctPrefix + "0");
            log.info("created participant " + participantPrefix + "0");
            for(int i = 2; i < clientCount; i++ ) {
                createUDClient(participantPrefix + "0", clientPrefix + num(i,3), password);
                log.info("created client " + clientPrefix + i);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCreateClientAndParticipants() {
    	// this is where it all happens
        String participantPrefix = PartPrefix;
        String clientPrefix = ClientPrefix;
        String password = "Test_1234";
        String acctPrefix = AccountPrefix;
        int clientCount = Count;
        try {
            loginAb();
            log.info("logged in");
            String partName,clientName, acctName;
            clientName = clientPrefix;
            //performance clients
            if (Testtype.contains("P"))
            {
	            for(int i =0; i < clientCount; i++ ) {
	            	partName = participantPrefix + i;
	                acctName = acctPrefix + i;
	                clientName = clientPrefix + i;
	                createUDParticipant(partName, password, acctName);
	                log.info("created participant " + partName);
	                createUDClient(partName, clientName, password);
	                log.info("created client " + clientName);
	            }
            } else {
            	//obix clients
            	for(int i = 0; i < clientCount; i++ ) {
                       partName = participantPrefix + num(i,3);
                       acctName = acctPrefix + num(i,3);
                       createUDParticipant(partName, password, acctName);
                       log.info("created participant " + partName);
                       createUDClient(partName, clientName, password);
                       log.info("created client " + clientName);
                   }	
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
