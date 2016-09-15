/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.MailUtilTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.contact.ParticipantContact;

/**
 * The Class MailUtilTest.
 */
public class MailUtilTest extends TestCase {
    
    /**
     * Test send envoy notification.
     */
    public void testSendEnvoyNotification() {
        //try {
            final Participant p = new Participant();
            p.setFirstName("test");
            p.setLastName("akua");
            final Set<ParticipantContact> contacts = new TreeSet<ParticipantContact>();
            final ParticipantContact contact = new ParticipantContact();
            contact.setType(Contact.EMAIL_ADDRESS);
            contact.setAddress("test@akuacom.com");
            contacts.add(contact);
            p.setContacts(contacts);
            final NotificationMethod method = new NotificationMethod();
            method.setMedia(NotificationMethod.ENVOY_MESSAGE);
            method.setEmail(true);
            final NotificationParametersVO np = new NotificationParametersVO();
            np.setEventId("1234");
            np.setEventStartDate(new Date());
            np.setEventEndDate(new Date());
            np.setMeterName("My Meter");
            np.setProgramName("Auto-DBP - Day-ahead WG2 Aggregate");
            np.setRespondBy(new Date());
            np.setTimeZone("PST");
//            final TimeBlock[] timeBlocks = new TimeBlock[3];
//            for (int i = 0; i < timeBlocks.length; i++) {
//                final TimeBlock timeBlock = new TimeBlock();
//                timeBlock.setStartHour(12 + i);
//                timeBlock.setEndHour(13 + i);
//                timeBlocks[i] = timeBlock;
//            }
//            np.setEntries(timeBlocks);
            np.setUnitPrice("0.8");
            // MailUtil.sendEnvoyNotification(p.getExternalContacts(), "test", "test", method, np, true);
        //} catch (NamingException e) {
         //   e.printStackTrace();
         //   Assert.fail("exception: " + e.getMessage());
        //} catch (JMSException e) {
        //    e.printStackTrace();
        //    Assert.fail("exception: " + e.getMessage());
        //}
    }
}
