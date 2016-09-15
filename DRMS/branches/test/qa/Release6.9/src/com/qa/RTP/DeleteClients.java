package com.qa.RTP;
//6.9/6.10

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
//import java.util.regex.Pattern;
import java.io.*;
//will log in as client, opt out of any events, delete the client

public class DeleteClients extends SeleneseTestCase {
	//serverDeleteContacts.config has the information for these variables.
	String Server = "";
	String Password = "";
	//array of participants(Zachary, Zeke, Zeke)
	String Participant = "";	

	private void readConfigFile() throws Exception {
		try{

			FileInputStream fstream = new FileInputStream("serverDeleteClients.config");

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			Server = br.readLine();
			Password = br.readLine();   
			Participant  = br.readLine();

			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}


	@Before
	public void setUp() throws Exception {
		//get server, participant password, participant name from serverDeleteClients.config
		readConfigFile();
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", Server);

		selenium.setSpeed("1500");
		selenium.start();
	}
	public void loginClient(String part) throws Exception {
		selenium.open("/pss2.website/Login");
		selenium.type("fm-username", part);
		selenium.type("fm-password", Password);
		selenium.click("Submit");
		selenium.waitForPageToLoad("30000");

	}

	private void deleteClient () throws Exception {

		selenium.click("navForm:navAbout");
		selenium.waitForPageToLoad("30000");	
		selenium.click("navForm:navClients");
		selenium.waitForPageToLoad("30000");	
		while (selenium.isElementPresent("//input[@type='checkbox']")) {
			optOut();
			selenium.click("navForm:navClients");
			selenium.waitForPageToLoad("30000");

			selenium.click("//input[@type='checkbox']");
			selenium.click("//input[@type='button'] [@value='Delete Clients']");
			selenium.click("navForm:yes");

		}
	}

	private void optOut() throws Exception {
		//since we don't know what tab will be selected
		//we will choose the least likely - About - to click to first

		selenium.click("navForm:navEvents");
		selenium.waitForPageToLoad("30000");
		//Check for events and opt-out
		while (selenium.isElementPresent("//input[@type='button'] [@value='Opt-out']")) {
			selenium.click("//input[@type='button'] [@value='Opt-out']");
			selenium.click("deleteForm:yes");
		}
	}


	@Test
	public void testDeleteClients() throws Exception {

		String[] participantName = Participant.split(",");
		int count = participantName.length;
		try {

			for(int i = 0; i < count; i++ ) {
				loginClient(participantName[i]);
				deleteClient();
				selenium.click("link=Logout");
				selenium.waitForPageToLoad("80000");
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
