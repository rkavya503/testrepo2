/**
 * 
 */
package com.akuacom.pss2.program;

import static com.akuacom.test.TestUtil.generateRandomBoolean;
import static com.akuacom.test.TestUtil.generateRandomDate;
import static com.akuacom.test.TestUtil.generateRandomInt;
import static com.akuacom.test.TestUtil.generateRandomString;
import static com.akuacom.test.TestUtil.generateRandomStringOfLength;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.pss2.program.signal.ProgramSignal;

/**
 * @author roller
 * 
 */
public class ProgramTest extends BaseEntityFixture<Program> {
	Logger log = Logger.getLogger(ProgramTest.class.getSimpleName());
	@Override
	public Program generateRandomIncompleteEntity() {
		Program program = new Program();
		program = generateRandomIncompleteEntity(program);

		return program;
	}
	
	protected <E extends Program> E generateRandomIncompleteEntity(E program) {
		
		int priority = generateRandomInt();
		program.setPriority(priority);
		assertEquals(priority, program.getPriority());

		String name = generateRandomString();
		program.setProgramName(name);
		assertEquals(name, program.getProgramName());

		java.util.Date beginEffectiveTime = generateRandomDate();
		program.setBeginEffectiveTime(beginEffectiveTime);
		assertEquals(beginEffectiveTime, program.getBeginEffectiveTime());

		java.util.Date endEffectiveTime = generateRandomDate();
		program.setEndEffectiveTime(endEffectiveTime);
		assertEquals(endEffectiveTime, program.getEndEffectiveTime());

		int minIssue = generateRandomInt(2048);
		program.setMinIssueToStartM(minIssue);
		assertEquals(minIssue, program.getMinIssueToStartM());

		boolean must = generateRandomBoolean();
		program.setMustIssueBDBE(must);
		assertEquals(must, program.isMustIssueBDBE());

		int timeH = generateRandomInt(2048);
		program.setMaxIssueTimeH(timeH);
		assertEquals(timeH, program.getMaxIssueTimeH());

		timeH = generateRandomInt(2048);
		program.setMinStartTimeH(timeH);
		assertEquals(timeH, program.getMinStartTimeH());

		timeH = generateRandomInt(2048);
		program.setMaxStartTimeH(timeH);
		assertEquals(timeH, program.getMaxStartTimeH());

		timeH = generateRandomInt(2048);
		program.setMinEndTimeH(timeH);
		assertEquals(timeH, program.getMinEndTimeH());

		timeH = generateRandomInt(2048);
		program.setMaxEndTimeH(timeH);
		assertEquals(timeH, program.getMaxEndTimeH());

		int timeM = generateRandomInt(2048);
		program.setMaxIssueTimeM(timeM);
		assertEquals(timeM, program.getMaxIssueTimeM());

		timeM = generateRandomInt(2048);
		program.setMaxStartTimeM(timeM);
		assertEquals(timeM, program.getMaxStartTimeM());

		timeH = generateRandomInt(2048);
		program.setMinStartTimeH(timeH);
		assertEquals(timeH, program.getMinStartTimeH());

		timeH = generateRandomInt(2048);
		program.setMinEndTimeH(timeH);
		assertEquals(timeH, program.getMinEndTimeH());

		timeH = generateRandomInt(2048);
		program.setMaxEndTimeH(timeH);
		assertEquals(timeH, program.getMaxEndTimeH());

		timeH = generateRandomInt(2048);
		program.setMinDurationM(timeH);
		assertEquals(timeH, program.getMinDurationM());

		timeH = generateRandomInt(2048);
		program.setMaxDurationM(timeH);
		assertEquals(timeH, program.getMaxDurationM());

		String notificationParam1 = generateRandomString();
		program.setNotificationParam1(notificationParam1);
		assertEquals(notificationParam1, program.getNotificationParam1());

		boolean manualCreatable = generateRandomBoolean();
		program.setManualCreatable(manualCreatable);
		assertEquals(manualCreatable, program.isManualCreatable());

		String utilityName = generateRandomString();
		program.setUtilityProgramName(utilityName);
		assertEquals(utilityName, program.getUtilityProgramName());

		String className = generateRandomString();
		program.setClassName(className);
		assertEquals(className, program.getClassName());

		Integer state = generateRandomInt(2048);
		program.setState(state);
		assertEquals(state, program.getState());

		String validatorClass = generateRandomString();
		program.setValidatorClass(validatorClass);
		assertEquals(validatorClass, program.getValidatorClass());

		String validatorConfigFile = generateRandomString();
		program.setValidatorConfigFile(validatorConfigFile);
		assertEquals(validatorConfigFile, program.getValidatorConfigFile());

		String uiConfigureProgramString = generateRandomString();
		program.setUiConfigureProgramString(uiConfigureProgramString);
		assertEquals(uiConfigureProgramString,
				program.getUiConfigureProgramString());

		String uiConfigureEventString = generateRandomString();
		program.setUiConfigureEventString(uiConfigureEventString);
		assertEquals(uiConfigureEventString,
				program.getUiConfigureEventString());

		String uiScheduleEventString = generateRandomString();
		program.setUiScheduleEventString(uiScheduleEventString);
		assertEquals(uiScheduleEventString, program.getUiScheduleEventString());

		String description = generateRandomString();
		program.setDescription(description);
		assertEquals(description, program.getDescription());

		java.util.Date autoRepeatTimeOfDay = generateRandomDate();
		program.setAutoRepeatTimeOfDay(autoRepeatTimeOfDay);
		assertEquals(autoRepeatTimeOfDay, program.getAutoRepeatTimeOfDay());

		java.util.Date lastErrorOptContract = generateRandomDate();
		program.setLastErrorOpContact(lastErrorOptContract);
		assertEquals(lastErrorOptContract, program.getLastErrorOpContact());

		boolean autoAccept = generateRandomBoolean();
		program.setAutoAccept(autoAccept);
		assertEquals(autoAccept, program.getAutoAccept());

		return program;
	}

	@Test
	public void testCopy() {
		Program existing = generateRandomIncompleteEntity();
		existing.setUUID(generateRandomStringOfLength(32));
		existing.setRules(null);

		Set<ProgramSignal> signals = new HashSet<ProgramSignal>();
		// DRMS-2139 Add several signals for the test after Program Signals is
		// converted to pattern.
		// signals.add(ProgramSignals.getRandomEntity());
		existing.setSignals(signals);

		String newProgramName = "newProgram";
		Program copy = existing.copy(existing, newProgramName);
		assertNotNull(copy);
		log.info("copy " + copy + ", id " + copy.getUUID());
		assertEquals(newProgramName, copy.getProgramName());
		assertEquals(existing.getAutoAccept(), copy.getAutoAccept());
		assertEquals(existing.getClassName(), copy.getClassName());
		assertEquals(existing.getDescription(), copy.getDescription());
		assertEquals(existing.getAutoRepeatTimeOfDay(),	copy.getAutoRepeatTimeOfDay());
		assertEquals(existing.getLastErrorOpContact(), copy.getLastErrorOpContact());
		assertNull(copy.getModifier());
		assertEquals(existing.getState(), copy.getState());
		assertEquals(existing.getUiConfigureEventString(), copy.getUiConfigureEventString());
		assertEquals(existing.getUiConfigureProgramString(), copy.getUiConfigureProgramString());
		assertEquals(existing.getUiScheduleEventString(), copy.getUiScheduleEventString());
		assertEquals(existing.getUtilityProgramName(), copy.getUtilityProgramName());
		assertEquals(existing.getValidatorClass(), copy.getValidatorClass());
		assertEquals(existing.getValidatorConfigFile(), copy.getValidatorConfigFile());
		assertEquals(existing.getSignals().size(), copy.getSignals().size());
		assertNull(copy.getRules());

		Set<ProgramRule> rules = new HashSet<ProgramRule>();
		ProgramRule rule = new ProgramRule();
		rule.setUUID("program_rule_uuid");
		rules.add(rule);

		existing.setRules(rules);
		Program copy2 = existing.copy(existing, newProgramName);
		assertEquals(existing.getRules().size(), copy2.getRules().size());
	}
}
