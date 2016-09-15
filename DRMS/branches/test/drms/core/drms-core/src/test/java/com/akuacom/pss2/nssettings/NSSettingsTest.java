package com.akuacom.pss2.nssettings;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.test.TestUtil;

/**
 * NSSettingsTest is a test for notification system global settings function.
 * 
 * @author Brian Chapman
 * 
 * Initial date 2010.10.25
 */
public class NSSettingsTest extends BaseEntityFixture<NSSettings> {
	
	@Override
	public NSSettings generateRandomIncompleteEntity() {
		NSSettings nss = new NSSettings();
		
		int filterStatus = TestUtil.generateRandomInt(2048);
		nss.setFilterStatus(filterStatus);
		assertEquals(filterStatus, nss.getFilterStatus());

		int msgThreshold = TestUtil.generateRandomInt(2048);
		nss.setMsgThreshold(msgThreshold);
		assertEquals(msgThreshold, nss.getMsgThreshold());
		
		int frequency = TestUtil.generateRandomInt(2048);
		nss.setFrequency(frequency);
		assertEquals(frequency, nss.getFrequency());

		int duration = TestUtil.generateRandomInt(2048);
		nss.setDuration(duration);
		assertEquals(duration, nss.getDuration());

		int msgExpireTime = TestUtil.generateRandomInt(2048);
		nss.setMsgExpireTime(msgExpireTime);
		assertEquals(msgExpireTime, nss.getMsgExpireTime());

		int cleanMsgHour = TestUtil.generateRandomInt(2048);
		nss.setCleanMsgHour(cleanMsgHour);
		assertEquals(cleanMsgHour, nss.getCleanMsgHour());

		int cleanMsgMinute = TestUtil.generateRandomInt(2048);
		nss.setCleanMsgMinute(cleanMsgMinute);
		assertEquals(cleanMsgMinute, nss.getCleanMsgMinute());
		
		String exceptionContacts = TestUtil.generateRandomString();
		nss.setExceptionContacts(exceptionContacts);
		assertEquals(exceptionContacts, nss.getExceptionContacts());
		
		return nss;
	}

    @Test
    public void testToString() {
    	NSSettings ns = this.generateRandomEntity();
		String value = ns.toString();
		assertEquals("status = " + ns.getFilterStatus() +
				     ", threshold = " + ns.getMsgThreshold() +
				     ", frequency = " + ns.getFrequency() +
				     ", duration = " + ns.getDuration() +
				     ", msgExpireTime = " + ns.getMsgExpireTime() +
				     ", cleanMsgHour = " + ns.getCleanMsgHour() +
				     ", cleanMsgMinute = " + ns.getCleanMsgMinute(), value);
    }
    
	public void testGetExceptionContactsAsListEmptyExceptionContacts() {
    	NSSettings ns = this.generateRandomEntity();
		assertEquals(Collections.emptyList(), ns.getExceptionContactsAsList());    
	}
	
	public void testGetExceptionContactsAsList() {
		NSSettings ns = this.generateRandomEntity();
		String string = TestUtil.generateRandomString() + "," + TestUtil.generateRandomString();
		ns.setExceptionContacts(string);
		assertEquals(2, ns.getExceptionContactsAsList().size());
	}
	
}
