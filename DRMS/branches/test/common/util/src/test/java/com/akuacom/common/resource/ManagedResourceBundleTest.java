/**
 * 
 */
package com.akuacom.common.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.junit.Test;

import com.akuacom.common.text.CustomFormatTest;
import com.akuacom.test.TestUtil;

/**
 * Tests the basic functionality of {@link ManagedResourceBundle}.
 * 
 * @author roller
 * 
 */
public class ManagedResourceBundleTest {

	public static final String EXAMPLE_PROPERTIES_BASE_NAME = ExampleMessage.class
			.getName();

	private static final String MISSING_KEY_BASE_NAME = EXAMPLE_PROPERTIES_BASE_NAME
			+ "MissingKey";

	private static final String EXTRA_KEY_BASE_NAME = EXAMPLE_PROPERTIES_BASE_NAME
			+ "ExtraKey";

	public static ManagedResourceBundle getValidBundle() {
		return new ManagedResourceBundle(ExampleMessage.class);
	}

	/**
	 * Corresponds to the {@link ExampleMessage#MESSAGE_IN_FILE} key in the
	 * example properties file.
	 * 
	 */
	public static final String MESSAGE_IN_FILE_VALUE = "Some irrelevant message.";

	public static final String FIRST_NAME_VALUE = "Dan";
	public static final String USERNAME_VALUE = "danno";

	/**
	 * Used to test the formatter of a username works.
	 * 
	 */
	public static final String[] MESSAGE_IN_FILE_WITH_PARAMS_PARAMS_UPPER = new String[] {
			FIRST_NAME_VALUE, USERNAME_VALUE.toUpperCase() };
	/** Used to test the indexed values. */
	public static final String[] MESSAGE_IN_FILE_WITH_PARAMS_PARAMS = {
			FIRST_NAME_VALUE, USERNAME_VALUE };
	public static final String MESSAGE_IN_FILE_WITH_PARAMS_VALUE_FORMATTED = "Dan created a new user danno.";

	/**
	 * Provides access to the ExampleResourceBundle used for testing.
	 * 
	 * @see ExampleMessage
	 * @return
	 */
	public static ResourceBundle getExampleResourceBundle() {
		return ResourceBundle.getBundle(EXAMPLE_PROPERTIES_BASE_NAME);
	}

	@Test
	public void testWithMissingFile() {
		try {
			new ManagedResourceBundle(ExampleMessage.class, TestUtil
					.generateRandomString());
			fail(ExampleMessage.class.getSimpleName()
					+ " requires a properties file");
		} catch (MissingResourceException e) {
			assertEquals(
					"The failure should have been caused by the enum providing the meesage in the file",
					ExampleMessage.MESSAGE_IN_FILE.toString(), e.getKey());
		}

	}

	@Test
	public void testValidExample() {
		validateManagedResourceBundle(ExampleMessage.class);
	}

	/**
	 * 
	 */
	@Test
	public void testAllConstantsExist() throws Exception {

		ManagedResourceBundle example = getValidBundle();
		assertNotNull(example);

		// throws missing resource exception if not o.k
		example.enumsMatchKeysInBundle();
		assertTrue(example.containsKey(ExampleMessage.MESSAGE_IN_FILE));
		assertTrue(example
				.containsKey(ExampleMessage.MESSAGE_IN_FILE_WITH_PARAMS));
		assertEquals(MESSAGE_IN_FILE_VALUE, example.getString(
				ExampleMessage.MESSAGE_IN_FILE, null));

	}

	public static void validateManagedResourceBundle(Class<? extends Enum<? extends ResourceBundleMessage>> keyClass) {
		assertNotNull(keyClass);
		assertTrue("Message class must be enum.  See "
				+ ExampleMessage.class.getName(), Enum.class
				.isAssignableFrom(keyClass));
		// autoamtically validates upon construction
		new ManagedResourceBundle(keyClass);

	}

	@Test(expected = MissingResourceException.class)
	public void testMissingKeysInFile() {
		ManagedResourceBundle example = new ManagedResourceBundle(
				ExampleMessage.class, MISSING_KEY_BASE_NAME);
		example.enumsMatchKeysInBundle();
	}

	@Test(expected = MissingResourceException.class)
	public void testExtraKeysInFile() {
		ManagedResourceBundle example = new ManagedResourceBundle(
				ExampleMessage.class, EXTRA_KEY_BASE_NAME);
		example.keysInBundleMatchEnums();
	}

	@Test(expected = MissingResourceException.class)
	public void testMissingFile() {
		new ManagedResourceBundle(ExampleMessage.class, TestUtil
				.generateRandomString());
	}

	@Test
	public void testFormatSimpleMessage() {
		DynamicResourceBundleMessage message = new DynamicResourceBundleMessage(
				ExampleMessage.MESSAGE_IN_FILE);
		ManagedResourceBundle example = getValidBundle();
		String formattedMessage = example.formatMessage(message);
		assertEquals(MESSAGE_IN_FILE_VALUE, formattedMessage);
	}

	/**
	 * @see ExampleMessage#MESSAGE_IN_FILE_WITH_PARAMS
	 */
	@Test
	public void testFormatWithParams() {
		DynamicResourceBundleMessage message = new DynamicResourceBundleMessage(
				ExampleMessage.MESSAGE_IN_FILE_WITH_PARAMS,
				ExampleMessage.MESSAGE_IN_FILE_WITH_PARAMS.getParameterKeys(),
				MESSAGE_IN_FILE_WITH_PARAMS_PARAMS_UPPER);
		ManagedResourceBundle example = getValidBundle();
		String formattedMessage = example.formatMessage(message);
		assertEquals(
				ManagedResourceBundleTest.MESSAGE_IN_FILE_WITH_PARAMS_VALUE_FORMATTED,
				formattedMessage);
	}

	/**
	 * Tests the message that uses many numbers to be formatted by the automatic
	 * formatting or provided formatters.
	 * 
	 * @see ExampleMessage#MESSAGE_TESTING_FORMATS
	 * 
	 */
	@Test
	public void testFormattedParameters() {
		String expected = CustomFormatTest.WHOLE_NUMBER_FORMATTED
				+ " is an int, " + CustomFormatTest.WHOLE_NUMBER_FORMATTED
				+ " is a long, " + CustomFormatTest.DECIMAL_FORMATTED
				+ " is a double, " + CustomFormatTest.DECIMAL_FORMATTED
				+ " is a float, " + CustomFormatTest.PERCENT_FORMATTED
				+ " is a pecent.";

		DynamicResourceBundleMessage message = new DynamicResourceBundleMessage(
				ExampleMessage.MESSAGE_TESTING_FORMATS).addParameter(
				ExampleMessage.Param.DOUBLE, CustomFormatTest.DECIMAL_NUMBER)
				.addParameter(ExampleMessage.Param.FLOAT,
						(float) CustomFormatTest.DECIMAL_NUMBER).addParameter(
						ExampleMessage.Param.INTEGER,
						CustomFormatTest.WHOLE_NUMBER).addParameter(
						ExampleMessage.Param.LONG,
						(long) CustomFormatTest.WHOLE_NUMBER).addParameter(
						ExampleMessage.Param.PERCENT,
						CustomFormatTest.DECIMAL_NUMBER);
		String formattedMessage = getValidBundle().formatMessage(message);
		assertEquals(expected, formattedMessage);
	}

	@Test
	public void testFactory() {
		ManagedResourceBundleFactory factory = ManagedResourceBundleFactory
				.getInstance();
		factory.register(getValidBundle());
		ManagedResourceBundle bundle = factory
				.getManagedResourceBundle(ExampleMessage.MESSAGE_IN_FILE);
		assertNotNull(bundle);
		bundle = factory
				.getManagedResourceBundle(ExampleMessage.MESSAGE_IN_FILE);
		assertNotNull(bundle);
		bundle = factory.getManagedResourceBundle(TestUtil
				.generateRandomString());
		assertNull(bundle);

	}

}
