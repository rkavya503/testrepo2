package com.akuacom.jdbc;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class JavaTypesTest {
	
	static enum WEEK_DAY{
		MON, TUE, WED, THU, FRI, SAT,SUN
	}
	
	@Test
	public void forceCastTest(){
		Long longValue = 12L;
		Short shortValue = 100;
		int   intValue = 101;
		double doubleValue = 1.11;
		float floatValue = 1.01F;
		BigDecimal big = new BigDecimal("11111.11");
		
		int longToInt = JavaTypes.forceCast(longValue, Integer.class);
		assertEquals(12, longToInt);
		
		long intToLong = JavaTypes.forceCast(intValue, Long.class);
		assertEquals(101,intToLong);
		
		int shortToInt = JavaTypes.forceCast(shortValue, Integer.class);
		assertEquals(100,shortToInt);
		
		double f2d= JavaTypes.forceCast(floatValue, Double.class);
		assertEquals(f2d,1.01,5);
		
		double big2d = JavaTypes.forceCast(big, Double.class);
		assertEquals(big2d,11111.11,5);
		
		BigDecimal d2big = JavaTypes.forceCast(doubleValue, BigDecimal.class);
		assertEquals(d2big.doubleValue(), 1.11,3);
		
		assertEquals(JavaTypes.forceCast(WEEK_DAY.MON, Integer.class).intValue(), 0);
		assertEquals(JavaTypes.forceCast(WEEK_DAY.MON, String.class), "MON");
		
		assertEquals(JavaTypes.forceCast(1, WEEK_DAY.class), WEEK_DAY.TUE);
		assertEquals(JavaTypes.forceCast("FRI", WEEK_DAY.class), WEEK_DAY.FRI);
	}
	
	@Test
	public void testTypeJudge(){
		int intValue = 101;
		assertTrue(JavaTypes.isNumber(intValue));
		Integer intValue1= 101;
		assertTrue(JavaTypes.isNumber(intValue1));
	}
	
}
