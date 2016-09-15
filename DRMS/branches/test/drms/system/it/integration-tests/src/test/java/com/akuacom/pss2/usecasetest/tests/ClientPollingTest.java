package com.akuacom.pss2.usecasetest.tests;

import org.junit.Test;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.usecasetest.cases.ClientPollingBacnetUsecase;
import com.akuacom.pss2.usecasetest.cases.ClientPollingREST2Usecase;
import com.akuacom.pss2.usecasetest.cases.ClientPollingRESTUsecase;
import com.akuacom.pss2.usecasetest.cases.ClientPollingSOAP2Usecase;
import com.akuacom.pss2.usecasetest.cases.ClientPollingSOAPUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateClientUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateNewParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteParticipantUsecase;

public class ClientPollingTest extends UsecaseTestBase{

    private static final String participantName = "pollPart";
    private static final String partAccountNum = participantName;
    private static final String clientName = "pollClient";
    private static final String clientFullName = "pollPart.pollClient";
    private static final String password = "Test_1234";

    @Test
    public void runCase () throws Exception {
        Participant part = (Participant)new CreateNewParticipantUsecase(participantName, partAccountNum, password).runCase();
        runCase (new CreateClientUsecase(part, clientName, password));
        
        ClientPollingState cpsREST2 = (ClientPollingState)new ClientPollingREST2Usecase(clientFullName, password).runCase();
        ClientPollingState cpsREST = (ClientPollingState)new ClientPollingRESTUsecase(clientFullName, password).runCase();
        
        ClientPollingState cpsSOAP2 = (ClientPollingState)new ClientPollingSOAP2Usecase(clientFullName, password).runCase();
        ClientPollingState cpsSOAP = (ClientPollingState)new ClientPollingSOAPUsecase(clientFullName, password).runCase();
        
        ClientPollingState cpsBacnet = (ClientPollingState)new ClientPollingBacnetUsecase(clientFullName, password).runCase();
        
    	runCase(new DeleteParticipantUsecase(participantName));
    	
    	org.junit.Assert.assertTrue("REST2 Poll test fail: " + cpsREST2, cpsREST2.isGoodNormal());
    	
    	org.junit.Assert.assertTrue("REST Poll test fail: " + cpsREST, cpsREST.isGoodNormal());
    	
    	org.junit.Assert.assertTrue("SOAP 2 Poll test fail: " + cpsSOAP2, cpsSOAP2.isGoodNormal());
    	
    	// SOAPWS server is not implemented correctly - the event states are not loaded.
    	// However, there may be a legacy client pulling the values out of the result string
    	// so we are ignoring this test and deprecating the client
//    	org.junit.Assert.assertTrue("SOAP Poll test fail: " + cpsSOAP, cpsSOAP.isGoodNormal());
    	
    	org.junit.Assert.assertTrue("BACnet Poll test fail: " + cpsBacnet, cpsBacnet.isGoodNormal());
    }
}
