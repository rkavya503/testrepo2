package com.qa.RTP;
//6.9/6.10

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
//import java.util.regex.Pattern;
import java.io.*;
//works for participants with one client and more than one contact
//Zuma,Zuma
//1,1
//works for participants with a second client named "2" and one contact.
//Zombie,Zombie
//1,2


public class DeleteContacts extends SeleneseTestCase {
//serverDeleteContacts.config has the information for these variables.
	String Server = "";
	String Password = "";
	//array of participants(Zachary, Zeke, Zeke) , array of number of client (1, 1, 2)
	String NumberContacts = "";
	String Username = "";
	String Client = "";	
	
private void readConfigFile() throws Exception {
	try{
	    
	    FileInputStream fstream = new FileInputStream("serverDeleteContacts.config");
	   
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    
	    Server = br.readLine();
	    Password = br.readLine();
	    Username = br.readLine();	    
	    Client = br.readLine();
	    
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
public void loginClient(String part) throws Exception {
  selenium.open("/pss2.website/Login");
  selenium.type("fm-username", part);
  selenium.type("fm-password", Password);
  selenium.click("Submit");
  selenium.waitForPageToLoad("30000");

}

private void deleteContact(String participant, String client) throws Exception {
	//since we don't know what tab will be selected
	//we will choose the least likely - About - to click to first
	selenium.setSpeed("1000");	
	selenium.click("navForm:navAbout");
	selenium.waitForPageToLoad("30000");
	selenium.click("navForm:navClients");
	selenium.waitForPageToLoad("30000");
	

	 if (Integer.parseInt(client)== 2 ) {
		 selenium.click("//html/body/div/form/div[2]/div/table/tbody/tr[2]/td[2]/a/h3");	 
	 } else if (Integer.parseInt(client)== 3 ) { 
	     selenium.click("//html/body/div/form/div[2]/div/table/tbody/tr[3]/td[2]/a/h3");
	 } else {
		 selenium.click("//html/body/div/form/div[2]/div/table/tbody/tr/td[2]/a/h3");		 
	 }
	  selenium.waitForPageToLoad("80000");
	  selenium.click("//a//h4[text()='Contacts']");
	  selenium.waitForPageToLoad("80000");
	  selenium.click("//input[@type='checkbox'] [@title='Select Client for Delete']");
	  selenium.click("//input[@type='submit'] [@title='Delete Contacts']");
	  selenium.waitForPageToLoad("80000");
	  selenium.setSpeed("50");
}


@Test
public void testDeleteContacts() throws Exception {

		String[] participantName = Username.split(",");
		String[] clientName = Client.split(",");
		int contactCount = participantName.length;
		try {
	        
	        for(int i = 0; i < contactCount; i++ ) {
	        	loginClient(participantName[i]);
	            deleteContact(participantName[i], clientName[i]);
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
