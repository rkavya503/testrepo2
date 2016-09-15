package com.akuacom.pss2.program.participant;

import static com.akuacom.test.TestUtil.*;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.akuacom.ejb.BaseEntityFixture;

/**
 * Tests the {@link InvalidDate} Entity
 * 
 * @author Brian Chapman
 * 
 */
public class InvalidDateTest extends
			BaseEntityFixture<InvalidDate> {

		@Override
		public InvalidDate generateRandomIncompleteEntity() {
			InvalidDate invalidDate = new InvalidDate();

			Date date = generateRandomDate();
			invalidDate.setInvalidDate(date);
			assertEquals(date, invalidDate.getInvalidDate());

			return invalidDate;
		}
	}
