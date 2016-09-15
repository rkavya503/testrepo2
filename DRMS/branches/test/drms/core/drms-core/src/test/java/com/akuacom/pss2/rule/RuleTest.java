package com.akuacom.pss2.rule;

import static com.akuacom.test.TestUtil.generateRandomStringOfLength;
import static org.junit.Assert.assertEquals;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * An abstract test method for entities that extend Rule.
 * 
 * @author Brian Chapman
 *
 * @param <E>
 */
public abstract class RuleTest<E extends Rule> extends BaseEntityFixture<E> {
	
	public E generateRandomIncompleteEntity(E entity) {
		
		String variable = generateRandomStringOfLength(60);
		entity.setVariable(variable);
		assertEquals(variable, entity.getVariable());
		
		return entity;
	}

}
