package com.akuacom.test;

import static com.akuacom.test.TestUtil.generateRandomStringArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Generated;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import com.akuacom.utils.DateUtilTest;

/**
 * Verifies the {@link TestUtil} is generating random values as expected.
 * 
 * @author roller
 * 
 */
public class TestUtilTest {

	@Test
	public void testRandomInt() {
		int max = 50;
		int result = TestUtil.generateRandomInt(max);
		assertTrue(result >= 0);
		assertTrue(result <= max);
	}

	@Test
	public void testRandomString() {
		int length = 33;
		String result = TestUtil.generateRandomStringOfLength(length);
		assertEquals(length, result.length());

	}

	@Test
	public void testRandomStringArray() {
		String[] result = TestUtil.generateRandomStringArray();
		assertTrue(result.length > 0);
		for (String string : result) {
			assertNotNull(string);

		}
	}

	/**
	 * Basically tests {@link TestUtil#generateRandomBoolean()} with enough
	 * attempts to ensure a true.
	 * 
	 */
	@Test
	public void testRandomBoolean() {
		
		//one flaw in this...doesn't ensure there are many falses, only trues.
		int howManyTries = 20;
		boolean result = false;
		for (int i = 0; i < howManyTries; i++) {
			result |= TestUtil.generateRandomBoolean();
		}
		assertTrue("Seriously, out of " + howManyTries
				+ " not a single true?  That's not random enough", result);
	}
	
	public void testRandomIntRange(){
		int reasonableSize = Integer.MAX_VALUE/5;
		int min = TestUtil.generateRandomInt(reasonableSize);
		int range = TestUtil.generateRandomInt(reasonableSize);
		int max = min + range;
		Integer result = TestUtil.generateRandomInt(min, max);
		assertTrue("too small",min < result);
		assertTrue("too big ",max > result);
	}
	@Test
	public void testDateNoMillis(){
		TestUtil.assertDateEqualsNoMillis(DateUtilTest.NO_MILLIS_DATE, DateUtilTest.WITH_MILLIS_DATE);
	}
	
	/**@see TestUtil#generateRandomFromArray(Object[])
	 * 
	 */
	@Test
	public void testRandomFromArray(){
		String[] array = generateRandomStringArray();
		
		String result = TestUtil.generateRandomFromArray(array);
		assertNotNull(result);
		assertTrue(ArrayUtils.contains(array, result));
		
	}
	
	@Test 
	public void testNonPublicAccessor(){
		GrandChildClass gc= new GrandChildClass();
		TestUtil.setNonPublicMember(gc, "name", "David W.");
		TestUtil.setNonPublicMember(gc, "address", "Address");
		TestUtil.setNonPublicMember(gc, "telphone", "111");
		assertEquals("David W.",TestUtil.getNonPublicMember(gc, "name"));
		assertEquals("Address",TestUtil.getNonPublicMember(gc, "address"));
		assertEquals("111",TestUtil.getNonPublicMember(gc, "telphone"));
	}
}
