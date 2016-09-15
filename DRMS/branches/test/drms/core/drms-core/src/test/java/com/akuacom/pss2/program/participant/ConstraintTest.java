package com.akuacom.pss2.program.participant;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.test.TestUtil;

/**
 * Tests the Constraint entity
 * 
 * @author Brian Chapman
 * 
 *         Created on 2010.11.09
 */
public class ConstraintTest extends BaseEntityFixture<Constraint> {

	public Constraint generateRandomIncompleteEntity() {
		Constraint constraint = new Constraint();

		Date maxActive = TestUtil.generateRandomDate();
		constraint.setMaxActive(maxActive);
		assertEquals(maxActive, constraint.getMaxActive());

		return constraint;
	}
}
