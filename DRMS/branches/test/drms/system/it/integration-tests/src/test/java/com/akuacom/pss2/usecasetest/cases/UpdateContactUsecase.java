package com.akuacom.pss2.usecasetest.cases;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.TreeSet;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;

public class UpdateContactUsecase extends AbstractUseCase {
	private static ParticipantManager participantManager = null;
	private Participant client = null;

	private ParticipantContact contact = null;

	protected ParticipantManager getParticipantMgr() {
		if (participantManager == null)
			participantManager = JBossFixture
					.lookupSessionRemote(ParticipantManagerBean.class);
		return participantManager;
	}
	
	public void setContact(ParticipantContact contact) {this.contact = contact;}
	public void setClient(Participant client) {this.client = client;}

	public UpdateContactUsecase(ParticipantContact contact) {
		setContact(contact);
		setClient(contact.getParticipant());
	}

	public Object runCase() throws Exception {
		
		contact.setType(Contact.FAX_NUMBER);
		contact.setDescription("CreateContactTestClient1");
		contact.setAddress("CreateContactTest1@abc.com");
		contact.setCommNotification(false);
		contact.setOffSeasonNotiHours(0.6);
		contact.setOnSeasonNotiHours(0.6);
		contact.setEventNotification(ContactEventNotificationType.NoNotification);
		contact.setMsgThreshold(11);
		
		Set<ParticipantContact> clientContacts = new TreeSet<ParticipantContact>();	
		clientContacts.add(contact);
		client.setContacts(clientContacts);
		
		getParticipantMgr().updateParticipant(client);
		
		client = getClientMgr().getClient(client.getAccountNumber());
		Set<ParticipantContact> contacts = client.getContacts();
		
		boolean contactExist = false;
		for (ParticipantContact contact : contacts) {
			if(contact.getDescription().equalsIgnoreCase("CreateContactTestClient1")){
				if(contact.getType().equalsIgnoreCase(Contact.FAX_NUMBER) 
						&& contact.getAddress().equalsIgnoreCase("CreateContactTest1@abc.com")
						&& contact.isCommNotification() == false
						&& contact.getOffSeasonNotiHours().doubleValue() == 0.6
						&& contact.getOnSeasonNotiHours().doubleValue() == 0.6
						&& contact.getEventNotification() == ContactEventNotificationType.NoNotification
						&& contact.getMsgThreshold() == 11){

					contactExist = true;
				}
			}
		}
		
		assertTrue(contactExist);
		
		return null;
	}
}
