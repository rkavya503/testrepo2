package com.akuacom.pss2.usecasetest.cases;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.TreeSet;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.client.ClientManagerBean;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;

public class CreateContactUsecase extends AbstractUseCase {

	private static ParticipantManager participantManager = null;
	private static ClientManager clientManager = null;
	
	private Participant client = null;
	private ParticipantContact contact = null;
	Set<ParticipantContact> clientContacts = new TreeSet<ParticipantContact>();	

	private String type;
    private String address;
    private String name;
    private boolean commNotification;
    private Double offSeasonNotiHours;
    private Double onSeasonNotiHours;
	private ContactEventNotificationType eventNotification;
    private Integer msgThreshold;
	
	public ParticipantContact getContact() {
		return contact;
	}
	public void setContact(ParticipantContact contact) {
		this.contact = contact;
	}

	protected ParticipantManager getParticipantMgr() {
		if (participantManager == null)
			participantManager = JBossFixture
					.lookupSessionRemote(ParticipantManagerBean.class);
		return participantManager;
	}

	protected ClientManager getContactMgr() {
		if (clientManager == null)
			clientManager = JBossFixture
					.lookupSessionRemote(ClientManagerBean.class);
		return clientManager;
	}
	
	public void setClient(Participant client) {
		this.client = client;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCommNotification(boolean commNotification) {
		this.commNotification = commNotification;
	}
	public void setOffSeasonNotiHours(Double offSeasonNotiHours) {
		this.offSeasonNotiHours = offSeasonNotiHours;
	}
	public void setOnSeasonNotiHours(Double onSeasonNotiHours) {
		this.onSeasonNotiHours = onSeasonNotiHours;
	}
	public void setEventNotification(ContactEventNotificationType eventNotification) {
		this.eventNotification = eventNotification;
	}
	public void setMsgThreshold(Integer msgThreshold) {
		this.msgThreshold = msgThreshold;
	}
	
	
	public CreateContactUsecase() {	}
	
	public CreateContactUsecase(Participant client, String type,
			String address, String name, boolean commNotification,
			Double offSeasonNotiHours, Double onSeasonNotiHours,
			ContactEventNotificationType eventNotification, Integer msgThreshold) {
		
		setClient(client);
		setType(type);
		setAddress(address);
		setName(name);
		setOffSeasonNotiHours(offSeasonNotiHours);
		setOnSeasonNotiHours(onSeasonNotiHours);
		setCommNotification(commNotification);
		setEventNotification(eventNotification);
		setMsgThreshold(msgThreshold);		
	}


	public Object runCase() throws Exception {

		contact = new ParticipantContact();
		contact.setType(type);
		contact.setAddress(address);
		contact.setDescription(name);
		contact.setOffSeasonNotiHours(offSeasonNotiHours);
		contact.setOnSeasonNotiHours(onSeasonNotiHours);
		contact.setCommNotification(commNotification);
		contact.setEventNotification(eventNotification);
		contact.setMsgThreshold(msgThreshold);
		
		contact.setParticipant(client);
		
		clientContacts.add(contact);
		client.setContacts(clientContacts);
		
		getParticipantMgr().updateParticipant(client);
		
		client = getClientMgr().getClient(client.getAccountNumber());
		Set<ParticipantContact> contacts = client.getContacts();
		
		boolean contactExist = false;
		ParticipantContact foundContact = null;
		for (ParticipantContact contact : contacts) {
			if(contact.getDescription().equalsIgnoreCase("CreateContactTestClient")){
				if(contact.getType().equalsIgnoreCase(Contact.EMAIL_ADDRESS) 
						&& contact.getAddress().equalsIgnoreCase("CreateContactTest@abc.com")
						&& contact.isCommNotification() == true
						&& contact.getOffSeasonNotiHours().doubleValue() == 0.5
						&& contact.getOnSeasonNotiHours().doubleValue() == 0.5
						&& contact.getEventNotification() == ContactEventNotificationType.FullNotification
						&& contact.getMsgThreshold() == 10){
					
					foundContact = contact;  
					contactExist = true;
				}
			}
		}
		
		assertTrue(contactExist);

		return foundContact;
	}
}
