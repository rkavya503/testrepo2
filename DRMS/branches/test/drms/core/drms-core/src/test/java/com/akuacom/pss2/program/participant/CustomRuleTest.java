package com.akuacom.pss2.program.participant;

import com.akuacom.pss2.rule.RuleTest;

/**
 * The unit test for the CustomRule entity.
 * 
 * @author Brian Chapman
 * 
 */
public class CustomRuleTest extends RuleTest<CustomRule> {

	@Override
	public CustomRule generateRandomIncompleteEntity() {
		CustomRule rule = new CustomRule();
		generateRandomIncompleteEntity(rule);

		/*
		 * Add CustomRule getters and setters here Except programParticipant,
		 * which needs to be done with a persisted ProgramParticipant in the
		 * EAOTest. Thus the work "Incomplete" in the method name.
		 */
		
		return rule;
	}

}
