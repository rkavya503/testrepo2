package com.akuacom.pss2.program.cpp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.akuacom.pss2.event.TimeBlock;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.rule.Rule.Mode;


public class CPPUtilsTest {
	
	private static final double VALUE_DBL = 999.00;
	private static final String SOURCE_VAR = "variable";
	private static final String MIN_START_TIME = "15:00";
	private static final Integer MIN_START_TIMEH = 15;
	private static final Integer MIN_START_TIMEM = 0;
	
	private static final String MAX_START_TIME = "16:00";
	private static final Integer MAX_START_TIMEH = 16;
	private static final Integer MAX_START_TIMEM = 0;
	
	private static final String MIN_END_TIME = "17:00";
	private static final Integer MIN_END_TIMEH = 17;
	private static final Integer MIN_END_TIMEM = 0;
	
	private static final String MAX_END_TIME = "18:00";
	private static final Integer MAX_END_TIMEH = 18;
	private static final Integer MAX_END_TIMEM = 0;
	
	private static final Integer CAL_YEAR = 2010;
	private static final Integer CAL_MONTH = 5;
	private static final Integer CAL_DAY = 26;
	private static final Integer CAL_START_TIMEH = 15;
	private static final Integer CAL_END_TIMEH = 17;
	private static final Integer CAL_TIMEM = 0;
	private static final Integer CAL_TIMES = 0;	
	
	private static final String TEST_PROG = "TestProg";
	private Program programVO=null; 
	ProgramParticipantRule programParticipantRuleEAO = null;
	ProgramParticipantRule programParticipantRuleEAOCPPNon = null;
	private List<ProgramParticipantRule> lstProgramParticipantRuleEAO = new ArrayList<ProgramParticipantRule>();
	List<CPPShedStrategy> lstStrategyCPP = new ArrayList<CPPShedStrategy>();
	List<CPPShedStrategy> lstStrategyCPPNot = new ArrayList<CPPShedStrategy>();
	
	ProgramRule programRuleEAO = null;
	
	@Before
	public void setUp(){		
		//Setup CPPProgram object
		programVO = new CPPProgram();
		programVO.setProgramName(TEST_PROG);
		
		programVO.setMinStartTime(MIN_START_TIME);
		programVO.setMinStartTimeH(MIN_START_TIMEH);
		programVO.setMinStartTimeM(MIN_START_TIMEM);
		
		programVO.setMaxStartTime(MAX_START_TIME);
		programVO.setMaxStartTimeH(MAX_START_TIMEH);
		programVO.setMaxStartTimeM(MAX_START_TIMEM);		
		
		programVO.setMinEndTime(MIN_END_TIME);
		programVO.setMinEndTimeH(MIN_END_TIMEH);
		programVO.setMinEndTimeM(MIN_END_TIMEM);
		
		programVO.setMaxEndTime(MAX_END_TIME);
		programVO.setMaxEndTimeH(MAX_END_TIMEH);
		programVO.setMaxEndTimeM(MAX_END_TIMEM);		
		
		programParticipantRuleEAO = new ProgramParticipantRule();
		programParticipantRuleEAO.setMode(Mode.NORMAL);		
		
		Calendar startCal = new GregorianCalendar(CAL_YEAR,CAL_MONTH,CAL_DAY,CAL_START_TIMEH,CAL_TIMEM,CAL_TIMES);
		Calendar endCal = new GregorianCalendar(CAL_YEAR,CAL_MONTH,CAL_DAY,CAL_END_TIMEH,CAL_TIMEM,CAL_TIMES);		
		
		programParticipantRuleEAO.setStart(startCal.getTime());
		programParticipantRuleEAO.setEnd(endCal.getTime());
		programParticipantRuleEAO.setSource(Rule.Source.CPP_SHED_STRATEGY.getDescription());		
		
		lstProgramParticipantRuleEAO.add(programParticipantRuleEAO);
		
		programParticipantRuleEAOCPPNon = new ProgramParticipantRule();
		programParticipantRuleEAOCPPNon.setSource(Rule.Source.SCERTP_SHED_STRATEGY.getDescription());		
		
		CPPShedStrategy cPPShedStragegy1 = new CPPShedStrategy();
		CPPShedStrategy cPPShedStragegy2 = new CPPShedStrategy();
		cPPShedStragegy1.setMode(CPPShedStrategy.ShedMode.DEFAULT);
		cPPShedStragegy2.setMode(CPPShedStrategy.ShedMode.NORMAL);
		TimeBlock timeBlock = new TimeBlock(startCal.getTime(),endCal.getTime());
		cPPShedStragegy2.setTimeBlock(timeBlock);
		lstStrategyCPPNot.add(cPPShedStragegy1);
		lstStrategyCPP.add(cPPShedStragegy2);		
	}	
	
	@Test
	public void testParseRules(){
		List<CPPShedStrategy> lstCppShedStategy = CPPUtils.parseRules((CPPProgram)programVO, lstProgramParticipantRuleEAO);
		assertNotNull(lstCppShedStategy);
		assertEquals(3,lstCppShedStategy.size());
		
//		For lstProgramParticipantRuleEAO only 1 rules with NORAML mode, then means the rules created, 
//		since (CAL_START_TIMEH - MIN_START_TIME =0), get(0) is NORAML, the rest is DEFAULT
		assertEquals(CPPShedStrategy.ShedMode.NORMAL,lstCppShedStategy.get(0).getMode());
		assertEquals(CPPShedStrategy.ShedMode.DEFAULT,lstCppShedStategy.get(1).getMode());
		assertEquals(CPPShedStrategy.ShedMode.DEFAULT,lstCppShedStategy.get(2).getMode());		
	}
	
	@Test
	public void testParseCPPShedStrategies(){
		List<ProgramParticipantRule> lstProgramParticipantRuleEAO= CPPUtils.parseCPPShedStrategies(lstStrategyCPP);
		assertNotNull(lstProgramParticipantRuleEAO);
		assertEquals(1,lstProgramParticipantRuleEAO.size());
	}
	
	@Test
	public void testParseNonCPPShedStrategies(){
		List<ProgramParticipantRule> lstProgramParticipantRuleEAO= CPPUtils.parseCPPShedStrategies(lstStrategyCPPNot);
		assertNotNull(lstProgramParticipantRuleEAO);
		assertEquals(0,lstProgramParticipantRuleEAO.size());
	}
	
	@Test
	public void testIsCPPRule(){
		assertTrue(CPPUtils.isCPPRule(programParticipantRuleEAO));
	}
	
	@Test
	public void testIsCPPRuleFalse(){
		assertFalse(CPPUtils.isCPPRule(programParticipantRuleEAOCPPNon));
	}	
	
	@Test
	public void testCreateDefaultClientRules(){
		programRuleEAO = new ProgramRule();
		programRuleEAO.setMode(Rule.Mode.NORMAL);
		programRuleEAO.setVariable(SOURCE_VAR);
		programRuleEAO.setOperator(Rule.Operator.LESS_THAN_OR_EQUAL);
		
		programRuleEAO.setValue(VALUE_DBL);
		CPPUtils.createDefaultClientRules(programVO, programRuleEAO, lstProgramParticipantRuleEAO);
		assertNotNull(lstProgramParticipantRuleEAO);
		assertEquals(4,lstProgramParticipantRuleEAO.size());
		for(ProgramParticipantRule var:lstProgramParticipantRuleEAO){
			assertEquals(Rule.Source.CPP_SHED_STRATEGY.getDescription(),var.getSource());
		}
    }	
}
