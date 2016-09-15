/**
 * 
 */
package com.akuacom.pss2.program.bidding;

import static com.akuacom.test.TestUtil.generateRandomDouble;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.pss2.program.Program;

/**
 * @author Linda
 * 
 */
public class BidConfigTest extends BaseEntityFixture<BidConfig> {

	@Override
    public BidConfig generateRandomIncompleteEntity() {
    	BidConfig bidConfig = new BidConfig();
    	
    	double minBidKW = generateRandomDouble();
    	bidConfig.setMinBidKW(minBidKW);
    	assertEquals(minBidKW, bidConfig.getMinBidKW(), 0.01);
    	
    	return bidConfig;
    }

	@Test
	public void testCopy() {
		Program program = new Program();
		program.setUUID("program_uuid");

		BidConfig existing = new BidConfig();
		existing.setUUID("bidconfig_uuid");
		existing.setAcceptTimeoutPeriodM((int) System.currentTimeMillis());
		existing.setDefaultBidKW(0.998);
		existing.setDrasBidding(true);
		existing.setDrasRespondByPeriodM(5);
		existing.setMinBidKW(0.765);
		existing.setMinConsectutiveBlocks(5);
		existing.setProgram(program);
		existing.setRespondByTimeH((int) System.currentTimeMillis());
		existing.setRespondByTimeM((int) System.currentTimeMillis());
		existing.setModifier("modifier");
		existing.setBidBlocks(null);

		Program parent = new Program();
		parent.setUUID("parent_program_uuid");
		BidConfig cloned = existing.copy(existing, parent);

		assertNotNull(cloned);
		assertEquals(existing.getAcceptTimeoutPeriodM(), cloned.getAcceptTimeoutPeriodM());
		assertEquals(existing.getDefaultBidKW(), cloned.getDefaultBidKW(), 0);
		assertEquals(true, cloned.isDrasBidding());
		assertEquals(existing.getDrasRespondByPeriodM(), cloned.getDrasRespondByPeriodM());
		assertEquals(existing.getMinBidKW(), cloned.getMinBidKW(), 0);
		assertEquals(existing.getMinConsectutiveBlocks(), cloned.getMinConsectutiveBlocks());
		assertEquals(parent, cloned.getProgram());
		assertEquals(existing.getRespondByTimeH(), cloned.getRespondByTimeH());
		assertEquals(existing.getRespondByTimeM(), cloned.getRespondByTimeM());
		assertNull(cloned.getModifier());
		assertNull(cloned.getBidBlocks());

		Set<BidBlock> bidBlockList = new HashSet<BidBlock>();
		BidBlock block1 = new BidBlock();
		block1.setUUID("bidblock_uuid1");
		bidBlockList.add(block1);

		BidBlock block2 = new BidBlock();
		block2.setUUID("bidblock_uuid2");
		bidBlockList.add(block2);

		existing.setBidBlocks(bidBlockList);
		
		cloned=null;
		cloned=existing.copy(existing, parent);
		
		assertNotNull(cloned);
		assertEquals(2, cloned.getBidBlocks().size());
		
	}
}
