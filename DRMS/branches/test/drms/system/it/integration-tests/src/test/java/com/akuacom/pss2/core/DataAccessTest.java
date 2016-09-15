/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.DataAccessTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import java.util.List;
import java.util.ListIterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.ParticipantUtil;
import com.akuacom.test.TestUtil;

/**
 * The Class DataAccessTest.
 */
public class DataAccessTest extends JBossFixture {

	/** The data access. */
	private ParticipantEAO participantEAO;

	/** The log. */
	//private static Logger log = Logger.getLogger(DataAccessTest.class.getName());

	@Before
	public void setUp() throws Exception {

		participantEAO = JBossFixture.lookupSessionRemote(ParticipantEAO.class);

	}

	/**
	 * Prints the participants.
	 * 
	 * @param dataAccess
	 *            the data access
	 * @param sort
	 *            the sort
	 */
	private static void printParticipants(ParticipantEAO dataAccess, int sort) {
		try {
			List participants = dataAccess.getParticipants(sort);
			for (Object participant1 : participants) {
				Participant participant = (Participant) participant1;
				System.out.println(participant.toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Test create participant.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Ignore("should not be remotely tested")
	@Test
	public void testCreateParticipant() throws Exception {
		//log.info("testCreateParticipant");

		List parts = participantEAO.getParticipants();
		int before_create = parts.size();

		Participant p0 = ParticipantUtil.getParicipantInstance(TestUtil.generateRandomInt());

		participantEAO.createParticipant(p0);

		Participant p1 = ParticipantUtil.getParicipantInstance(TestUtil.generateRandomInt()); 
		participantEAO.createParticipant(p1);

		Participant p2 = ParticipantUtil.getParicipantInstance(TestUtil.generateRandomInt()); 

		participantEAO.createParticipant(p2);

		parts = participantEAO.getParticipants();
		int after_create = parts.size();

		Assert.assertEquals(before_create + 3, after_create);
		Assert.assertEquals(after_create - 3, before_create);

		Participant participant = participantEAO.getParticipant(p0.getUser());
		ParticipantUtil.compareParticipants(p0, participant);

		participant = participantEAO.getParticipant(p1.getUser());
		ParticipantUtil.compareParticipants(p1, participant);

		participant = participantEAO.getParticipantByAccount(p2.getAccountNumber());
		ParticipantUtil.compareParticipants(p2, participant);
	}

	// Test Case to create and update a Participant

	/**
	 * Test update participant.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testUpdateParticipant() throws Exception {
		//log.info("testUpdateParticipant");

		Participant participantStart = ParticipantUtil.getParicipantInstance(TestUtil.generateRandomInt());
		participantEAO.createParticipant(participantStart);
		String participantName = participantStart.getUser();
		Participant participant = participantEAO
				.getParticipant(participantName);
		ParticipantUtil.compareParticipants(participant, participantStart);

		participant.setAccountNumber(TestUtil.generateRandomStringOfLength(15));
		
		participantEAO.updateParticipant(participantName, participant);
		Participant updatedParticipant = participantEAO.getParticipant(participantName);
		ParticipantUtil.compareParticipants(participant, updatedParticipant);
		

		participantEAO.removeParticipant(participantName);
	}

	// test case that sorts the participants by NAME and asserts they are sorted
	// lexicographically.

	/**
	 * Test get participant sort name.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Ignore
	@Test
	public void testGetParticipantSortName() throws Exception {
		//log.info("testGetParticipantSortName");

		List participants = participantEAO.getParticipants(Participant.SORT_NAME);
		//log.info("List size sort type is " + participants.size());

		ListIterator i = participants.listIterator();
		int num;
		String partName1;
		String partName2;
		for (num = 0; num < participants.size() - 1; num++) {

			partName1 = (String) i.next();
			partName2 = (String) i.next();
			String str1 = partName1.toUpperCase();
			String str2 = partName2.toUpperCase();

			Assert.assertTrue(str2.compareTo(str1) > 0
					|| str2.compareTo(str1) == 0);

			partName1 = (String) i.previous();
		}
	}

	/**
	 * Test get participant sort type.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Ignore
	@Test
	public void testGetParticipantSortType() throws Exception {
		//log.info("testGetParticipantSortType");

		List participants = participantEAO.getParticipants(Participant.SORT_TYPE);
		//log.info("List size sort type is " + participants.size());
		ListIterator i = participants.listIterator();
		int num;
		String partName1;
		String partName2;
		Participant part1;
		Participant part2;
		for (num = 0; num < participants.size() - 1; num++) {
			partName1 = (String) i.next();
			partName2 = (String) i.next();
			part1 = participantEAO.getParticipant(partName1);
			part2 = participantEAO.getParticipant(partName2);

			byte str1 = part1.getType();
			byte str2 = part2.getType();
			// System.out.println("comparing "+str1+" "+str2);

			Assert.assertTrue(part2.getType() > part1.getType()
					|| part2.getType() == part1.getType());

			partName1 = (String) i.previous();

		}

	}

	/**
	 * Test remove participant.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Ignore
	@Test
	public void testRemoveParticipant() throws Exception {
		//log.info("testRemoveParticipant");
		try {
			int numParticipants = participantEAO.getParticipants().size();

			participantEAO.removeParticipant("participant0");
			Assert.assertEquals(numParticipants - 1, participantEAO
					.getParticipants().size());

			participantEAO.removeParticipant("participant1");
			Assert.assertEquals(numParticipants - 2, participantEAO
					.getParticipants().size());

			participantEAO.removeParticipant("participant2");
			Assert.assertEquals(numParticipants - 3, participantEAO
					.getParticipants().size());
		} catch (Exception e) {
			//log.info("testRemoveParticipant failed: ", e);
		}
	}
}