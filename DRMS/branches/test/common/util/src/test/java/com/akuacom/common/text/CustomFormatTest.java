/**
 * 
 */
package com.akuacom.common.text;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.test.TestUtil;

/**
 * Tests the {@link CustomFormat} classes and the {@link CustomFormatFactory}
 * who provides them.
 * 
 * @author roller
 * 
 */
public class CustomFormatTest {

	public static final String PERCENT_FORMATTED = "1,234,512.34%";
	public static final double DECIMAL_NUMBER = 12345.1234;
	public static final int WHOLE_NUMBER = 12345;
	public static final String WHOLE_NUMBER_FORMATTED = "12,345";
	public static final String DECIMAL_FORMATTED = "12,345.12";
	protected static CustomFormatFactory factory;
	private static Locale locale;

	@Before
	public void setUp() {
		factory = CustomFormatFactory.getInstance();
		locale = Locale.getDefault();
	}

	/** @see DoubleFormat */
	@Test
	public void testDouble() {
		Double number = DECIMAL_NUMBER;
		String expected = DECIMAL_FORMATTED;
		testFormat(number, expected, DoubleFormat.class);
	}

	/**
	 * @see IntegerFormat
	 */
	@Test
	public void testInteger() {
		testFormat(WHOLE_NUMBER, WHOLE_NUMBER_FORMATTED, IntegerFormat.class);
	}

	/**
	 * @see LongFormat
	 */
	@Test
	public void testLong() {
		testFormat((long) WHOLE_NUMBER, WHOLE_NUMBER_FORMATTED,
				LongFormat.class);
	}

	/** @see FloatFormat */
	@Test
	public void testFloat() {
		testFormat((float) DECIMAL_NUMBER, DECIMAL_FORMATTED, FloatFormat.class);
	}

	@Test
	@Ignore
	public void testInheritance() {
		// //unable to test inheritance because no custom formats provide a
		// handled type that inherits for another that can be registered
		// CustomFormatFactory factory = new CustomFormatFactory();
		// factory.register(parent custom format);
		// factory.getFormat(childObject);
		// provide assertions
	}

	/** @see PercentFormat */
	@Test
	public void testPercent() {
		// not available in factory
		PercentFormat percentFormat = new PercentFormat();
		assertEquals(PERCENT_FORMATTED, percentFormat.format(DECIMAL_NUMBER,
				locale));

	}

	/** @see DefaultFormat */
	@Test
	public void testString() {
		String junk = TestUtil.generateRandomString();
		testFormat(junk, junk, DefaultFormat.class);
	}

	public static void testFormat(Object value, String expected,
			Class<? extends CustomFormat> expectedFormatType) {

		CustomFormat format = factory.getFormat(value.getClass());
		assertEquals(expectedFormatType, format.getClass());
		assertEquals(expected, format.format(value, locale));
	}
}
