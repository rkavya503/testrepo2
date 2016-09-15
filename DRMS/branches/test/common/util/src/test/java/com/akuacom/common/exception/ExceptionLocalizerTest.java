/**
 * 
 */
package com.akuacom.common.exception;

import static com.akuacom.test.TestUtil.generateRandomString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.akuacom.common.resource.DynamicResourceBundleMessage;
import com.akuacom.common.resource.ExampleMessage;
import com.akuacom.common.resource.ManagedResourceBundle;
import com.akuacom.common.resource.ManagedResourceBundleTest;

/**
 * Tests the {@link ExceptionLocalizer} framework including the
 * {@link ExceptionLocalizerFactory}, {@link ExceptionLocalizerProducer}, etc.
 * 
 * @author roller
 * 
 */
public class ExceptionLocalizerTest {

	protected ExceptionLocalizerFactory factory;

	@Before
	public void setUp() {
		// don't use the instance...we want a fresh one every test.
		factory = new ExceptionLocalizerFactory();
		factory.registerProducer(new DefaultExceptionLocalizer.Producer());

	}

	/**
	 * Tests that the default localizer will be produced when nothing else is
	 * generated. Everything should work even though resource bundles are
	 * unavailable.
	 * 
	 * @see DefaultExceptionLocalizer
	 */
	@Test
	public void testDefaultLocalizer() {

		String message = generateRandomString();
		ExceptionLocalizer localizer = factory.getExceptionLocalizer(message);
		assertEquals(message, localizer.getMessage());
		Exception exception = new Exception(message);
		localizer = factory.getExceptionLocalizer(message, exception);
		assertNotNull(localizer);
		assertEquals(message, localizer.getMessage());
		assertEquals(exception, localizer.getCause());
		localizer = factory.getExceptionLocalizer(exception);
		assertEquals(message, localizer.getMessage());
		assertEquals(exception, localizer.getCause());
	}

	/**
	 * Tests resource bundles directly (not using the managed resource bundle
	 * framework.
	 * 
	 * @see ResourceBundleExceptionLocalizer
	 */
	@Test
	public void testResourceBundleLocalizer() {

		factory.registerProducer(new ResourceBundleExceptionLocalizer.Producer(
				ManagedResourceBundleTest.EXAMPLE_PROPERTIES_BASE_NAME));

		// test the message is found int he file
		ExceptionLocalizer localizer = factory
				.getExceptionLocalizer(ExampleMessage.MESSAGE_IN_FILE
						.toString());
		assertNotNull(localizer);
		assertEquals(ManagedResourceBundleTest.MESSAGE_IN_FILE_VALUE, localizer
				.getMessage());
		assertNull(localizer.getCause());

		// make sure a key not in the file returns the default localizer
		String unknownKey = generateRandomString();
		localizer = factory.getExceptionLocalizer(unknownKey);
		assertNotNull(localizer);
		assertTrue(localizer.getClass().getName(),
				localizer instanceof DefaultExceptionLocalizer);
		assertEquals(unknownKey, localizer.getMessage());
		assertNull(localizer.getCause());

		// make sure key in file with params returns the proper message..this
		// only supports indexed params
		localizer = factory.getExceptionLocalizer(
				ExampleMessage.MESSAGE_IN_FILE_WITH_INDEXED_PARAMS.toString(),
				ManagedResourceBundleTest.MESSAGE_IN_FILE_WITH_PARAMS_PARAMS);
		assertNotNull(localizer);
		assertEquals(
				ManagedResourceBundleTest.MESSAGE_IN_FILE_WITH_PARAMS_VALUE_FORMATTED,
				localizer.getMessage());

	}

	/**
	 * Covers the {@link ManagedResourceBundleExceptionLocalizer} use of
	 * {@link ManagedResourceBundle} and friends.
	 * 
	 */
	@Test
	public void testManagedResourceBundleLocalizer() {
		factory
				.registerProducer(new ManagedResourceBundleExceptionLocalizer.Producer(
						ExampleMessage.class));

		Exception cause = new Exception();
		ExceptionLocalizer localizer = factory
				.getExceptionLocalizer(ExampleMessage.MESSAGE_IN_FILE);
		assertNotNull(localizer);
		assertEquals(ManagedResourceBundleTest.MESSAGE_IN_FILE_VALUE, localizer
				.getMessage());
		// now with exception
		localizer = factory.getExceptionLocalizer(
				ExampleMessage.MESSAGE_IN_FILE, cause);
		assertEquals(ManagedResourceBundleTest.MESSAGE_IN_FILE_VALUE, localizer
				.getMessage());

		// test the keys/values by index constructors
		localizer = factory.getExceptionLocalizer(
				ExampleMessage.MESSAGE_IN_FILE_WITH_PARAMS,
				ExampleMessage.MESSAGE_IN_FILE_WITH_PARAMS.getParameterKeys(),
				ManagedResourceBundleTest.MESSAGE_IN_FILE_WITH_PARAMS_PARAMS);
		assertNotNull(localizer);
		assertEquals(
				ManagedResourceBundleTest.MESSAGE_IN_FILE_WITH_PARAMS_VALUE_FORMATTED,
				localizer.getMessage());
		// now with exception
		localizer = factory.getExceptionLocalizer(
				ExampleMessage.MESSAGE_IN_FILE_WITH_PARAMS,
				ExampleMessage.MESSAGE_IN_FILE_WITH_PARAMS.getParameterKeys(),
				ManagedResourceBundleTest.MESSAGE_IN_FILE_WITH_PARAMS_PARAMS,
				cause);
		assertNotNull(localizer);
		assertEquals(
				ManagedResourceBundleTest.MESSAGE_IN_FILE_WITH_PARAMS_VALUE_FORMATTED,
				localizer.getMessage());

		// test the dynamic message created using the shortcut addParameter
		// methods
		DynamicResourceBundleMessage dynamicMessage = new DynamicResourceBundleMessage(
				ExampleMessage.MESSAGE_IN_FILE_WITH_PARAMS).addParameter(
				ExampleMessage.Param.FIRST_NAME,
				ManagedResourceBundleTest.FIRST_NAME_VALUE).addParameter(
				ExampleMessage.Param.USERNAME,
				ManagedResourceBundleTest.USERNAME_VALUE);
		localizer = factory.getExceptionLocalizer(dynamicMessage);
		assertNotNull(localizer);
		assertEquals(
				ManagedResourceBundleTest.MESSAGE_IN_FILE_WITH_PARAMS_VALUE_FORMATTED,
				localizer.getMessage());
		// now with exception
		localizer = factory.getExceptionLocalizer(dynamicMessage, cause);
		assertNotNull(localizer);
		assertEquals(
				ManagedResourceBundleTest.MESSAGE_IN_FILE_WITH_PARAMS_VALUE_FORMATTED,
				localizer.getMessage());

	}


}
