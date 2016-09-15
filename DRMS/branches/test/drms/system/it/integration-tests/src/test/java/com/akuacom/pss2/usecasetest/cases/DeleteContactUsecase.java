package com.akuacom.pss2.usecasetest.cases;

import static org.junit.Assert.assertFalse;

import java.util.Set;
import java.util.TreeSet;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.pss2.participant.contact.ParticipantContact;

public class DeleteContactUsecase extends AbstractUseCase {
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

	public DeleteContactUsecase(ParticipantContact contact) {
		setContact(contact);
		setClient(contact.getParticipant());
	}

	public Object runCase() throws Exception {
		
		Set<ParticipantContact> clientContacts = client.getContacts();
		clientContacts = new TreeSet<ParticipantContact>();
		client.setContacts(clientContacts);
		
		getParticipantMgr().updateParticipant(client);
		
		client = getClientMgr().getClient(client.getAccountNumber());
		Set<ParticipantContact> contacts = client.getContacts();
		
		boolean contactExist = false;
		for (ParticipantContact contact : contacts) {
			if(contact.getDescription().equalsIgnoreCase("CreateContactTestClient")){
				contactExist = true;
			}
		}
		assertFalse(contactExist);
		
		return null;
	}
}
