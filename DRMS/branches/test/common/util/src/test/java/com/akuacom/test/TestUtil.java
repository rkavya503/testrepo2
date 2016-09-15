package com.akuacom.test;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.Assert.*;
import org.junit.Ignore;

import com.akuacom.utils.lang.DateUtil;

/**
 * Used for helping tests do the basic stuff.
 * 
 * @author roller
 * 
 * 
 */
@Ignore("This is just a utility to be used by tests and should not be tested itself.")
public class TestUtil {
    public final static int SELENIUM_RC_PORT = 1492;

	public static Double generateRandomDouble(double max) {
		return Math.random() * max;
	}

	public static Double generateRandomDouble() {
		return generateRandomDouble(Double.MAX_VALUE);
	}

	public static Integer generateRandomInt(int max) {
		return generateRandomDouble(max).intValue();
	}

	public static Integer generateRandomInt(int min, int max) {
		if (min >= max) {
			throw new RuntimeException(
					"Infinite loop since your min is greater than max");
		}
		Integer result;
		do {
			result = generateRandomInt(max);
		} while (result < min);
		return result;
	}

	public static String generateRandomString() {
		return generateRandomString(true);
	}

	public static String generateRandomString(boolean allowEmptyString) {
		int max = 50;
		String result = generateRandomStringOfLength(generateRandomInt(max));

		if (!allowEmptyString) {
			while (result.isEmpty()) {
				result = generateRandomStringOfLength(generateRandomInt(max));
			}
		}
		return result;
	}

	public static String generateRandomStringOfLength(int targetLength) {
		StringBuffer stringBuilder = new StringBuffer();
		while (stringBuilder.length() < targetLength) {
			stringBuilder.append(String.valueOf(generateRandomDouble()));
		}
		return stringBuilder.substring(0, targetLength);
	}

	public static Integer generateRandomInt() {
		return generateRandomInt(Integer.MAX_VALUE);
	}

	public static Date generateRandomDate() {

		return new Date(generateRandomInt());
	}

	public static String[] generateRandomStringArray() {
		return generateRandomStringArray(true);
	}

	public static String[] generateRandomStringArray(boolean allowEmtpyStrings) {
		// ensure at least 1 element in the random array
		int size = Math.max(1, generateRandomInt(20));
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = generateRandomString(allowEmtpyStrings);
		}

		return result;
	}

	public static Boolean generateRandomBoolean() {

		return generateRandomInt(10) % 2 == 0;
	}

	public static void assertNotEquals(Object expected, Object actual) {
		assertTrue(expected + " is expected, but found " + actual,
				expected.equals(actual));
	}

	public static void assertDateEqualsNoMillis(Date left, Date right) {
		left = DateUtil.stripMillis(left);
		right = DateUtil.stripMillis(right);

		assertEquals(left.getTime() + " != " + right.getTime(), left, right);

	}

	/**
	 * Given all of the items from an array this will return a random selection
	 * from that array.
	 */
	public static <ItemT> ItemT generateRandomFromArray(ItemT[] items) {
		int index = generateRandomInt(0, items.length);
		return items[index];
	}
	
	
	/**
	 * Set value to a member which dose not have a public setter 
	 * @param bean the target bean 
	 * @param memberName the member name
	 * @param value the value to set
	 */
	public static void setNonPublicMember(Object bean,String memberName,Object value) {
		try {
			Field field=findFieldByName(bean.getClass(),memberName);
			if(field!=null){
				field.setAccessible(true);
				field.set(bean, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	protected static Field findFieldByNameInThisClass(Class<?> cls,String memberName){
		try{
			return cls.getDeclaredField(memberName);
		}catch(Exception e){
			return null;
		}
	}
	
	protected static Field findFieldByName(Class<?> cls,String memberName){
		Field field= findFieldByNameInThisClass(cls,memberName);
		if(field!=null) return field;
		//check super class
		cls =cls.getSuperclass();
		if(cls!=null && !cls.isInterface())
			return findFieldByName(cls,memberName);
		
		return null;
	}
	
	/**
	 * get a value of member which dose not have a public getter 
	 * @param bean the target bean 
	 * @param memberName the member name
	 * @return the value of member
	 */
	public static Object getNonPublicMember(Object bean,String memberName){
		try {
			Field field=findFieldByName(bean.getClass(),memberName);
			if(field!=null){
				field.setAccessible(true);
				return field.get(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
