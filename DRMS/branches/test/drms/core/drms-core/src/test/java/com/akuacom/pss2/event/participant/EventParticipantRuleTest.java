package com.akuacom.pss2.event.participant;

import com.akuacom.pss2.rule.RuleTest;

/**
 * Provides unit tests for the EventParticipantRule entity.
 * 
 * @author Brian Chapman
 * 
 *         Created on 2010.11.11
 * 
 */
public class EventParticipantRuleTest extends RuleTest<EventParticipantRule> {

	@Override
	public EventParticipantRule generateRandomIncompleteEntity() {
		EventParticipantRule rule = new EventParticipantRule();

		rule = generateRandomIncompleteEntity(rule);

		/*
		 * Add getters and setters tests here as they are added
		 * to this entity.
		 * 
		 */
		
		return rule;
	}

}
