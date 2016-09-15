package com.akuacom.pss2.program;

import com.akuacom.pss2.rule.RuleTest;

/**
 * Unit tests for the {@link ProgramRule} entity
 * 
 * @author Brian Chapman
 * 
 */
public class ProgramRuleTest extends RuleTest<ProgramRule> {

	@Override
	public ProgramRule generateRandomIncompleteEntity() {
		ProgramRule rule = new ProgramRule();
		rule = generateRandomIncompleteEntity(rule);

		/*
		 * Add tests for getters and setters here as they are added to the
		 * entity
		 */

		return rule;
	}

}
