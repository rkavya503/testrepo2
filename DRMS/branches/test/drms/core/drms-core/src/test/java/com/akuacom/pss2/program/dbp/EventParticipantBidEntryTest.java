package com.akuacom.pss2.program.dbp;

import static com.akuacom.test.TestUtil.generateRandomBoolean;
import static com.akuacom.test.TestUtil.generateRandomDate;
import static com.akuacom.test.TestUtil.generateRandomDouble;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * Provides unit tests for the EventParticipantBidEntry entity.
 * 
 * @author Brian Chapman
 * 
 *         Created on 2010.11.11
 * 
 */
public class EventParticipantBidEntryTest extends
		BaseEntityFixture<EventParticipantBidEntry> {

	@Override
	public EventParticipantBidEntry generateRandomIncompleteEntity() {
		EventParticipantBidEntry bidEntry = new EventParticipantBidEntry();

		Date startTime = generateRandomDate();
		bidEntry.setStartTime(startTime);
		assertEquals(startTime, bidEntry.getStartTime());

		Date endTime = generateRandomDate();
		bidEntry.setEndTime(endTime);
		assertEquals(endTime, bidEntry.getEndTime());

		double kw = generateRandomDouble();
		bidEntry.setReductionKW(kw);
		assertEquals(kw, bidEntry.getReductionKW(), 0.01);

		double priceLevel = generateRandomDouble();
		bidEntry.setPriceLevel(priceLevel);
		assertEquals(priceLevel, bidEntry.getPriceLevel(), 0.01);

		boolean active = generateRandomBoolean();
		bidEntry.setActive(active);
		assertEquals(active, bidEntry.isActive());

		return bidEntry;
	}

}
