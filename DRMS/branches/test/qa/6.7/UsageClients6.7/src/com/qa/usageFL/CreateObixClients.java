package com.qa.usageFL;

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

//import com.akuacom.test.TestUtil;

public class CreateObixClients extends SeleneseTestCase {
    // We create our Selenium test case
    Logger log = Logger.getLogger(CreateObixClients.class.getSimpleName());
    @Before
    public void setUp() {
        log.info("setting up");
//        selenium = new DefaultSelenium("localhost", TestUtil.SELENIUM_RC_PORT, "*chrome",
//                "https://204.236.242.62:8443/");
//        selenium = new DefaultSelenium("localhost", TestUtil.SELENIUM_RC_PORT, "*chrome",
//            "http://localhost:8080/");
        selenium = new DefaultSelenium("localhost", 4444, "*chrome",
            "http://184.72.88.58:8080/");
        selenium.start();
        log.info("selenium " + selenium);
        log.info("selenium.getLocation() " +selenium.getLocation());
    }

    public void loginAb() throws Exception {
        log.info("opening url");
        selenium.open("/pss2.website/Login");
        log.info("selenium.getLocation() " +selenium.getLocation());
//        selenium.type("fm-username", "admin");
//        selenium.type("fm-password", "Test_1234");
        selenium.type("fm-username", "a");
        selenium.type("fm-password", "b");
        selenium.click("Submit");
        selenium.waitForPageToLoad("30000");
    }
    
    private void createUDParticipant(String participantName, String password, String acctName) throws Exception{ 
        //selenium.open("/pss2.website/uoProgram.do");
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
        selenium.click("//input[@name='dataEnabler']");
        selenium.click("//input[@value='Update']");
        selenium.waitForPageToLoad("25000");
    }
    
    private void createUDClient(String participantName, String clientName, String password) throws Exception {
        selenium.open("/pss2.website/participantsMap.do");
        selenium.waitForPageToLoad("25000");
        selenium.open("/facdash/jsp/clients.jsf?user=" + participantName );
        selenium.waitForPageToLoad("30000");
        selenium.click("//input[@value='New Client']");
        selenium.waitForPageToLoad("30000");
        selenium.type("navForm:name", clientName);
        selenium.type("navForm:password", password);
        selenium.type("navForm:confirm", password);
        selenium.click("//input[@value='Create Client']");
        selenium.waitForPageToLoad("25000");
        //selenium.open("/pss2.website/uoProgram.do");
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
        String participantPrefix = "testP";
        String clientPrefix = "testC";
        String password = "Test_1234";
        String acctPrefix = "TestAcct";
        int clientCount = 200;
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
        String participantPrefix = "testG";
        String clientPrefix = "testG";
        String password = "Test_1234";
        String acctPrefix = "testG";
        int clientCount = 1;
        try {
            loginAb();
            log.info("logged in");
            String partName,clientName, acctName;
            clientName = clientPrefix;
            for(int i =0; i < clientCount; i++ ) {
                partName = participantPrefix + num(i,3);
                acctName = acctPrefix + num(i,3);
                //clientName = clientPrefix + num(i,3);
                createUDParticipant(partName, password, acctName);
                log.info("created participant " + partName);
                createUDClient(partName, clientName, password);
                log.info("created client " + clientName);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
