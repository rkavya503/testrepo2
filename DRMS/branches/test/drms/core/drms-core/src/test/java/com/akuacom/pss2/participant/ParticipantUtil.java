package com.akuacom.pss2.participant;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;

import com.akuacom.pss2.client.ClientStatus;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.participant.contact.ContactEventNotificationType;
import com.akuacom.pss2.participant.contact.ParticipantContact;

public class ParticipantUtil {
	public static final String DEFAULT_PASSWORD = "Test_1234";

	public static Participant getParicipantInstance(int i) {
		Participant p = new Participant();
		p.setUser("part" + i);
		p.setType(Participant.TYPE_AUTO);
		p.setHostAddress("0.0.0.0");
		p.setManualControl(false);
		p.setAccountNumber("accountNumber" + i);
		p.setFirstName("first name");
		p.setLastName("last name");
		p.setMeterName("meter name");
		p.setMeterId("meter id");
		p.setFeedback(false);
		p.setNotificationLevel(1);
		p.setAddress("address");
		p.setGridLocation("grid location");
		p.setLatitude(20.0);
		p.setLongitude(10.0);
		p.setShedPerHourKW(30.0);
		p.setLastPrice(1.0);
		p.setStatus(ClientStatus.OFFLINE.ordinal());
		p.setOfflineWarning(true);
		p.setClient(false);
		p.setParent("parent");
		
		Set<ParticipantContact> contacts = new TreeSet<ParticipantContact>();
		
		//For bi-direction association, the reference should set at both side to assure the 
		//the consistency of data 
		ParticipantContact contact=createEmailContact();
		contact.setParticipant(p);
		contacts.add(contact);
		
		contact=createPhoneContact();
		contact.setParticipant(p);
		contacts.add(contact);
		
		contact=createPhoneContact();
		contact.setParticipant(p);
		contacts.add(contact);
		
		contact=createPhoneContact();
		contact.setParticipant(p);
		contacts.add(contact);
		
		p.setContacts(contacts);
		return p;
	}

    public static Participant getParicipantInstance(String name) {
        final Participant vo = getParicipantInstance(0);
        vo.setParticipantName(name);
        vo.setAccountNumber(name);
        return vo;
    }


	/**
	 * @return
	 */
	public static ParticipantContact createFaxContact() {
		ParticipantContact contact;
		contact = new ParticipantContact();
		setContactFaxFields(contact);
		setCommonContactFields(contact);
		return contact;
	}

	/**
	 * @return
	 */
	public static ParticipantContact createPhoneContact() {
		ParticipantContact contact;
		contact = new ParticipantContact();
		setContactPhoneFields(contact);
		setCommonContactFields(contact);
		return contact;
	}

	public static ParticipantContact createEmailContact() {
		ParticipantContact contact = new ParticipantContact();
		setContactEmailFields(contact);
		setCommonContactFields(contact);
		return contact;
	}

	/**
	 * @param contact
	 */
	private static void setContactFaxFields(ParticipantContact contact) {
		contact.setAddress("4155551212");
		contact.setType(Contact.FAX_NUMBER);
	}

	/**
	 * @param contact
	 */
	private static void setContactPhoneFields(ParticipantContact contact) {
		contact.setAddress("4155551111");
		contact.setType(Contact.PHONE_NUMBER);
	}

	/**
	 * @param contact
	 */
	private static void setContactEmailFields(ParticipantContact contact) {
		contact.setAddress("part1@fake.com");
		contact.setType(Contact.EMAIL_ADDRESS);
	}

	/**
	 * @param contact
	 */
	private static void setCommonContactFields(ParticipantContact contact) {
		contact.setCommNotification(true);
		contact.setEventNotification(ContactEventNotificationType.NoNotification);
		contact.setOffSeasonNotiHours(0.05);
		contact.setOnSeasonNotiHours(0.05);
	}

	public static void compareParticipants(Participant p, Participant p1) {
		Assert.assertEquals(p.getAccountNumber(), p1.getAccountNumber());
		Assert.assertEquals(p.getAddress(), p1.getAddress());
		Assert.assertEquals(p.getClientStatus(), p1.getClientStatus());
		// Assert.assertEquals(p.getContacts(), p1.getContacts());
		// Assert.assertEquals(p.getExternalContacts(),
		// p1.getExternalContacts());
		Assert.assertEquals(p.getFirstName(), p1.getFirstName());
		Assert.assertEquals(p.getGridLocation(), p1.getGridLocation());
		Assert.assertEquals(p.getHostAddress(), p1.getHostAddress());
		Assert.assertEquals(p.getLastName(), p1.getLastName());
		Assert.assertEquals(p.getLastPrice(), p1.getLastPrice(), 0.005);
		Assert.assertEquals(p.getLatitude(), p1.getLatitude(), 0.005);
		Assert.assertEquals(p.getLongitude(), p1.getLongitude(), 0.005);
		// Assert.assertEquals(p.getManualSignalStates(),
		// p1.getManualSignalStates());
		Assert.assertEquals(p.getMeterId(), p1.getMeterId());
		Assert.assertEquals(p.getMeterName(), p1.getMeterName());
		Assert
				.assertEquals(p.getNotificationLevel(), p1
						.getNotificationLevel());
		Assert.assertEquals(p.getParent(), p1.getParent());
		Assert.assertEquals(p.getShedPerHourKW(), p1.getShedPerHourKW(), 0.005);
		Assert.assertEquals(p.getStatus(), p1.getStatus());
		Assert.assertEquals(p.getType(), p1.getType());
		Assert.assertEquals(p.getTypeString(), p1.getTypeString());

	}
}
