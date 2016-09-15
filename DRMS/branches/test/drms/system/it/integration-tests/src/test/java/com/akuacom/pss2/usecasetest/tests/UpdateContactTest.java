/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.usecasetest.tests;

import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.usecasetest.cases.CreateClientUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateContactUsecase;
import com.akuacom.pss2.usecasetest.cases.CreateNewParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.DeleteParticipantUsecase;
import com.akuacom.pss2.usecasetest.cases.UpdateContactUsecase;

/**
 * Update Contact Integration Test
 * @author Jowey
 */
public class UpdateContactTest extends UsecaseTestBase{

    private static final String participantName = "testPart";
    private static final String accountNum = participantName;
    private static final String password = "Test_1234";
    private static final String clientName = "buster";
    private static final String clientPassword = "Test_12345";
    
    private static final String type = Contact.EMAIL_ADDRESS;
    private static final String address = "CreateContactTest@abc.com";
    private static final String name = "CreateContactTestClient";
    private static final boolean commNotification = true;
    private static final Double offSeasonNotiHours = 0.5;
    private static final Double onSeasonNotiHours = 0.5;
	private static final ContactEventNotificationType eventNotification = ContactEventNotificationType.FullNotification;
    private static final Integer msgThreshold = 10;

    @Test
    @Ignore
    public void runCase () throws Exception {

    	//1. Create Participant
        Participant part = (Participant)new CreateNewParticipantUsecase(participantName, accountNum, password).runCase();
        //2. Create Client        
        Participant client = (Participant)new CreateClientUsecase(part, clientName, clientPassword).runCase();
        //3. Create Contact
        ParticipantContact contact = (ParticipantContact)new CreateContactUsecase(client, type, address, name, commNotification, offSeasonNotiHours, onSeasonNotiHours, eventNotification, msgThreshold).runCase();
        
        //4. Update Contact
        runCase (new UpdateContactUsecase(contact));
        //5. Clean test data
        runCase (new DeleteParticipantUsecase(participantName));

    }
}
