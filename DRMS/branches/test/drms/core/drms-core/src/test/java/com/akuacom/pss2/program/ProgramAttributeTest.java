/**
 * 
 */
package com.akuacom.pss2.program;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.pss2.program.bidding.BidConfig;
import com.akuacom.pss2.program.signal.ProgramSignal;
import com.akuacom.test.TestUtil;

/**
 * @author Linda
 *
 */
//8.30.2010 Linda: DRMS-1382 add test class for DRMS-866
public class ProgramAttributeTest extends BaseEntityFixture<Program> {

	
	@Override
	public Program generateRandomIncompleteEntity() {
		Program programAttribute = new Program();
		int priority = TestUtil.generateRandomInt();
		programAttribute.setPriority(priority);
		assertEquals(priority,programAttribute.getPriority());
		
		return programAttribute;
	}

	@Test
	@Ignore
	public void testCopy(){
		Program program1=generateRandomIncompleteEntity();
		program1.setUUID(genereateRandomUUID());
		program1.setEndEffectiveTime(TestUtil.generateRandomDate());
		program1.setBeginEffectiveTime(TestUtil.generateRandomDate());
		
		program1.setManualCreatable(true);
    	program1.setMaxDurationM(1440);

    	program1.setMaxEndTimeH(24);
    	program1.setMaxEndTimeM(1440);
    	program1.setMaxIssueTimeH(24);
    	program1.setMaxIssueTimeM(1440);
    	program1.setMaxStartTimeH(24);
    	program1.setMaxStartTimeM(1440);
    	program1.setMinDurationM(1);
    	program1.setMinEndTimeH(0);
    	program1.setMinEndTimeM(0);
    	program1.setMinStartTimeH(0);
    	program1.setMinStartTimeM(0);
    	program1.setMinIssueToStartM(1);
    	program1.setMustIssueBDBE(false);
    	program1.setNotificationParam1("test parameter");
    	program1.setPendingTimeDBEH(24);
    	program1.setPendingTimeDBEM(1440);
    	program1.setSignals(null);
    	program1.setBidConfig(null);
    	
		Program program2=new Program();
		program2.setUUID(genereateRandomUUID());
    	
    	assertNotNull(program2);
    	assertNull(program2.getUUID());
    	assertEquals(program1.getBeginEffectiveTime(), program2.getBeginEffectiveTime());
    	assertEquals(program1.getEndEffectiveTime(), program2.getEndEffectiveTime());
    	assertEquals(program1.isManualCreatable(), program2.isManualCreatable());
    	assertEquals(program1.getMaxDurationM(), program2.getMaxDurationM());
    	
    	assertEquals(program1.getMaxEndTimeH(), program2.getMaxEndTimeH());
    	assertEquals(program1.getMaxEndTimeM(), program2.getMaxEndTimeM());
    	assertEquals(program1.getMaxIssueTimeH(), program2.getMaxIssueTimeH());
    	assertEquals(program1.getMaxIssueTimeM(), program2.getMaxIssueTimeM());
    	assertEquals(program1.getMaxStartTimeH(), program2.getMaxStartTimeH());
    	assertEquals(program1.getMaxStartTimeM(), program2.getMaxStartTimeM());
    	assertEquals(program1.getMinDurationM(), program2.getMinDurationM());
    	assertEquals(program1.getMinEndTimeH(), program2.getMinEndTimeH());
    	assertEquals(program1.getMinEndTimeM(), program2.getMinEndTimeM());
    	assertEquals(program1.getMinStartTimeH(), program2.getMinStartTimeH());
    	assertEquals(program1.getMinStartTimeM(), program2.getMinStartTimeM());
    	assertEquals(program1.getMinIssueToStartM(), program2.getMinIssueToStartM());
    	assertEquals(program1.isMustIssueBDBE(), program2.isMustIssueBDBE());
    	assertEquals(program1.getNotificationParam1(), program2.getNotificationParam1());
    	assertEquals(program1.getPendingTimeDBEH(), program2.getPendingTimeDBEH());
    	assertEquals(program1.getPendingTimeDBEM(), program2.getPendingTimeDBEM());
    	assertNull(program2.getSignals());
    	assertNull(program2.getBidConfig());
    	
    	Set<ProgramSignal> signals = new HashSet<ProgramSignal>();
    	ProgramSignal signal=new ProgramSignal();
    	signal.setUUID("signal_uuid");
    	signals.add(signal);
    	program1.setSignals(signals);
    	
		Program program3=new Program();
		program3.setUUID(genereateRandomUUID());

    	program2=program1.copy(program1, program3.getProgramName());
    	
    	assertNotNull(program2);
    	assertNull(program2.getUUID());
    	assertEquals(program1.getSignals().size(), program2.getSignals().size());

    	Iterator<ProgramSignal> it = program2.getSignals().iterator();
    	assertEquals(null, it.next().getUUID());

    	BidConfig config=new BidConfig();
    	config.setUUID("bidconfig_uuid");
    	program1.setBidConfig(config);

		Program program4=new Program();
		program4.setUUID(genereateRandomUUID());
        
    	program2=program1.copy(program1, program4.getProgramName());
    	
    	assertNotNull(program2);
    	assertNull(program2.getUUID());
    	assertNotNull(program2.getBidConfig());
    	assertNull(program2.getBidConfig().getUUID());
    	
	}
	
	
}
