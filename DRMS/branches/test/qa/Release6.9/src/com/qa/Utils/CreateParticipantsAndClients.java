package com.qa.Utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.thoughtworks.selenium.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateParticipantsAndClients extends SeleneseTestCase {
	//possible customers: sce, cot, duke, sdg, pge
	
	String Server = "";
	String Username = "";
	String Password = "";
	String Contactemail = "";
	String Customer = "";
	
	int a;
	
	String RTPParticipants[] = {"RTP2K", "RTP2K-50K", "RTP50K", "RTPAg", "RTP2K-B", "RTP2K"};
	String RTPPrograms[] = {"RTP <2K", "RTP 2K-50K", "RTP >50K", "RTP Agricultural", "RTP <2k B", "RTP <2K"};
	String COTParticipants[] = {"aCOTDA1", "aCOTDA2", "aCOTDA3", "aCOTDO4", "aCOTDO5", "aCOTDO6"};
	String COTPrograms[] = {"COT DA", "COT DA", "COT DA", "COT DO", "COT DO", "COT DO"};
	String SDGParticipants[] = {"CBP-1-4DA", "CBP-2-6DA", "CBP-4-8DA", "CBP-1-4DO", "CBP-2-6DO", "CBP-4-8DO","CBP-cpp" };
	String SDGPrograms[] = {"CBP 1-4 DA", "CBP 2-6 DA", "CBP 4-8 DA", "CBP 1-4 DO", "CBP 2-6 DO", "CBP 4-8 DO", "CPP" };
	String PGEParticipants[] = {"rPdpU2-6", "rPdpU12-6", "rPdpEven12-6", "rPdp-Even2-6", "rPdp-Odd12-6", "rPdp-Odd2-6", "rDBP-DA-S", "rDBP-DA-Ag","rPeak-C24", "rPeak-BE24","rPeak-C1-7", "rPeak-BE1-7"};	
	String PGEPrograms[] = {"PDP - Auto PDP Unlimited 2-6", "PDP - Auto PDP Unlimited 12-6", "PDP - Auto PDP Even 12-6", "PDP - Auto PDP Even 2-6", "PDP - Auto PDP Odd 12-6", "PDP - Auto PDP Odd 2-6","DBP DA Single", "DBP DA Aggregate", "PeakChoice - Committed24", "PeakChoice - BestEffort24", "PeakChoice - Committed1-7", "PeakChoice - BestEffort1-7"};
	
	private void readConfigFile() throws Exception {
		try{
		    
		    FileInputStream fstream = new FileInputStream("serverParts.config");
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    
		    Server = br.readLine();
		    Username = br.readLine();
		    Password = br.readLine();
		    Contactemail = br.readLine();
		    Customer = br.readLine();
		    
		    in.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
	}
		
	
	public void Initialize() throws Exception {
		
		if (Customer.equals("sce")) a = RTPParticipants.length;	
		if (Customer.equals("cot")) a = COTParticipants.length;
		if (Customer.equals("sdg")) a = SDGParticipants.length;
		if (Customer.equals("pge")) a = PGEParticipants.length;		
	}
	
	@Before
	public void setUp() throws Exception {
		readConfigFile();
	    selenium = new DefaultSelenium("localhost", 4444, "*chrome", Server);
	    selenium.setSpeed("50");
	    selenium.start();
	}
	public void loginAb() throws Exception {
		
	    selenium.open("/pss2.website/Login");
	    selenium.type("fm-username",Username);
	    selenium.type("fm-password", Password);
	    selenium.click("Submit");
	    selenium.waitForPageToLoad("30000");

	}
	public void logOut() throws Exception {
		selenium.setSpeed("500");
		selenium.click("link=Logout");
	    selenium.waitForPageToLoad("80000");
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
	    selenium.type("navForm:threshold", threshold);
	    selenium.click("//input[@type='submit' and @value='Create Contact']");
	    selenium.waitForPageToLoad("30000");
	}
	
	
	
	private void createClient(String participantName, String threshold) throws Exception {
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
	    if (!selenium.isChecked("//input[@type='checkbox'] [@title='Participate Program']"))
	    	selenium.click("//input[@type='checkbox'] [@title='Participate Program']");
	    selenium.click("link=Done");
	    selenium.waitForPageToLoad("80000");
	}

	private void createParticipant(String participantName, String programName) throws Exception{
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
	    selenium.waitForPageToLoad("25000");
	}
	

	@Test
	public void testCreateParticipants() throws Exception {
		// gets the array size
		Initialize();
		//number of participants number of programs must match
		String[] participantName = new String[a];
		String[] programName = new String[a];
		if (Customer.equals("sce")) {
			
			 System.arraycopy(RTPParticipants, 0, participantName, 0, a);
			 System.arraycopy(RTPPrograms, 0, programName, 0, a);
		}
		if (Customer.equals("cot")) {
			
			 System.arraycopy(COTParticipants, 0, participantName, 0, a);
			 System.arraycopy(COTPrograms, 0, programName, 0, a);
		}
		if (Customer.equals("sdg")) {
			
			 System.arraycopy(SDGParticipants, 0, participantName, 0, a);
			 System.arraycopy(SDGPrograms, 0, programName, 0, a);
		}
		if (Customer.equals("pge")) {
			
			 System.arraycopy(PGEParticipants, 0, participantName, 0, a);
			 System.arraycopy(PGEPrograms, 0, programName, 0, a);
		}	
	    int clientCount = a;
	    
	    try {
	        loginAb();
	        
	        for(int i = 0; i < clientCount; i++ ) {
	            createParticipant(participantName[i], programName[i]);
	            createClient(participantName[i], Integer.toString(i + 50));
	        }
	        logOut();
	    }catch(Exception e) {
	        e.printStackTrace();
	    }
	}

@After
public void tearDown() throws Exception {
    selenium.stop();
}
}

