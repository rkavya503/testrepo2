package com.qa.RTP;
//6.9/6.10

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
//import java.util.regex.Pattern;
import java.io.*;
//will log in as client, opt out of any events

public class DeleteRTPParticipants extends SeleneseTestCase {
	//serverDeleteRTPParticipants.config has the information for these variables.
	String Server = "";
	String Username = "";
	String Password = "";
	//array of participants(Zachary,Zeke,Zeke)
	String Participant = "";


	private void readConfigFile() throws Exception {
		try{

			FileInputStream fstream = new FileInputStream("serverDeleteRTPParticipants.config");

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			Server = br.readLine();
			Username = br.readLine();
			Password = br.readLine();   
			Participant  = br.readLine();

			in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	@Before
	public void setUp() throws Exception {
		//get server, participant password, participant name from serverDeleteRTPParticipants.config
		readConfigFile();
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", Server);

		selenium.setSpeed("1000");
		selenium.start();
	}
	public void loginClient(String part) throws Exception {
		selenium.open("/pss2.website/Login");
		selenium.type("fm-username", part);
		selenium.type("fm-password", Password);
		selenium.click("Submit");
		selenium.waitForPageToLoad("30000");

	}
	public void loginAdmin() throws Exception {
		selenium.open("/pss2.website/Login");
		selenium.type("fm-username", Username);
		selenium.type("fm-password", Password);
		selenium.click("Submit");
		selenium.waitForPageToLoad("30000");
	}
	String num(int n, int width) {
		String s = ""+n;
		while(s.length() < width) {
			s = "0" + s;
		}
		return s;
	}
	private void deleteParticipant (String participant) throws Exception {
		loginAdmin();
		selenium.click("link=Participants");
		selenium.waitForPageToLoad("30000");
		selenium.type("//input[contains(@name, 'FilterCondition')]", participant);
		selenium.click("//input[contains(@name, 'FilterButton')]");
		selenium.click("//input [contains(@name, 'participants:0')]");
		selenium.click("//img[@src='/pss2.utility/images/layout/delete_device.gif']");
		selenium.click("deleteForm:yes");
	}

	private void optOut(String participant) throws Exception {
		//since we don't know what tab will be selected
		//we will choose the least likely - About - to click to first

		loginClient(participant);
		selenium.click("navForm:navAbout");
		selenium.waitForPageToLoad("30000");		
		selenium.click("navForm:navEvents");
		selenium.waitForPageToLoad("30000");
		//Check for events and opt-out
		//need to slow it down
		selenium.setSpeed("3000");
		while (selenium.isElementPresent("//input[@type='button'] [@value='Opt-out']")) {
			selenium.click("//input[@type='button'] [@value='Opt-out']");
			selenium.click("deleteForm:yes");
		}
		selenium.setSpeed("500");
		selenium.click("link=Logout");
		selenium.waitForPageToLoad("80000");
	}


	@Test
	public void testDeleteParticipants() throws Exception {

		String[] participantName = Participant.split(",");
		int count = participantName.length;

		try {

			for(int i = 0; i < count; i++ ) { 	
				optOut(participantName[i]); 
				deleteParticipant(participantName[i]);
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
