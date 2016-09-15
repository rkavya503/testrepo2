/**
 * 
 */
package com.akuacom.pss2.program.bidding;

import static com.akuacom.test.TestUtil.generateRandomInt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * @author Linda
 * 
 * Created on 2010.08.30
 */
public class BidBlockTest extends BaseEntityFixture<BidBlock> {
	// setup constants from BidBlock
	private static final int REFERENCE_YEAR = 2000;
	private static final int REFERENCE_MONTH = 1;
	private static final int REFERENCE_DAY = 1;

	@Override
	public BidBlock generateRandomIncompleteEntity() {
		BidBlock bidBlock = new BidBlock();

		int startTimeM = generateRandomInt(1024);
		bidBlock.setStartTimeM(startTimeM);
		assertEquals(startTimeM, bidBlock.getStartTimeM());

		return bidBlock;
	}

	@Test
	public void testRefenceTime() {
		BidBlock config = new BidBlock();
		int startH = generateRandomInt(12);
		int endH = generateRandomInt(12);
		int startM = generateRandomInt(59);
		int endM = generateRandomInt(59);

		config.setStartTimeH(startH);
		config.setStartTimeM(startM);
		config.setEndTimeH(endH);
		config.setEndTimeM(endM);

		Calendar startDate = new GregorianCalendar();
		startDate.setTimeInMillis(config.getStartReferenceTime().getTime());
		assertEquals(REFERENCE_YEAR, startDate.get(Calendar.YEAR));
		assertEquals(REFERENCE_MONTH, startDate.get(Calendar.MONTH));
		assertEquals(REFERENCE_DAY, startDate.get(Calendar.DATE));
		assertEquals(startH, startDate.get(Calendar.HOUR));
		assertEquals(startM, startDate.get(Calendar.MINUTE));
		assertEquals(0, startDate.get(Calendar.SECOND));

		Calendar endDate = new GregorianCalendar();
		endDate.setTimeInMillis(config.getEndReferenceTime().getTime());
		assertEquals(REFERENCE_YEAR, endDate.get(Calendar.YEAR));
		assertEquals(REFERENCE_MONTH, endDate.get(Calendar.MONTH));
		assertEquals(REFERENCE_DAY, endDate.get(Calendar.DATE));
		assertEquals(endH, endDate.get(Calendar.HOUR));
		assertEquals(endM, endDate.get(Calendar.MINUTE));
		assertEquals(0, endDate.get(Calendar.SECOND));
	}

	@Test
	public void testCopy() {

		BidConfig config = new BidConfig();
		config.setUUID("bidconfig_uuid");

		BidBlock exitingBlock = new BidBlock();
		exitingBlock.setUUID("bidblock_uuid");
		exitingBlock.setBidConfig(config);

		exitingBlock.setEndTimeH((int) System.currentTimeMillis());
		exitingBlock.setEndTimeM((int) System.currentTimeMillis());
		exitingBlock.setStartTimeH((int) System.currentTimeMillis());
		exitingBlock.setStartTimeM((int) System.currentTimeMillis());
		exitingBlock.setModifier("modifier");

		BidConfig parent = new BidConfig();
		parent.setUUID("parent_bidconfig_uuid");
		BidBlock cloned = exitingBlock.copy(exitingBlock, parent);

		assertNotNull(cloned);
		assertEquals(parent, cloned.getBidConfig());
		assertEquals(exitingBlock.getEndTimeH(), cloned.getEndTimeH());
		assertEquals(exitingBlock.getEndTimeM(), cloned.getEndTimeM());
		assertEquals(exitingBlock.getStartTimeH(), cloned.getStartTimeH());
		assertEquals(exitingBlock.getStartTimeM(), cloned.getStartTimeM());

		assertNull(cloned.getModifier());
	}

}
