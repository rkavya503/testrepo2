/**
 * 
 */
package com.akuacom.pss2.system.property;

import static com.akuacom.test.TestUtil.generateRandomBoolean;
import static com.akuacom.test.TestUtil.generateRandomDouble;
import static com.akuacom.test.TestUtil.generateRandomString;
import static com.akuacom.test.TestUtil.generateRandomStringArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.pss2.system.property.CoreProperty.PropertyType;

/**
 * Tests the POJO part of the Entity {@link CoreProperty}
 * 
 * @author roller
 * 
 */
// we want to test deprecated methods until they are gone.
@SuppressWarnings("deprecation")
public class CorePropertyTest extends BaseEntityFixture<CoreProperty> {

	@Override
	public CoreProperty generateRandomIncompleteEntity() {
		String propertyName = generateRandomString();
		String stringValue = generateRandomString();
		CoreProperty coreProperty = new CoreProperty(propertyName, stringValue);

		coreProperty.setPropertyName(propertyName);
		assertEquals(propertyName, coreProperty.getPropertyName());

		PropertyType type = PropertyType.STRING;
		coreProperty.setPropertyType(type);
		assertEquals(type, coreProperty.getPropertyType());

		return coreProperty;
	}

	@Test
	public void testLooseConstructor() {
		String id = generateRandomString();
		String propertyName = generateRandomString();
		String propertyValue = generateRandomString();
		String type = CoreProperty.TEXT_TYPE;
		CoreProperty property = new CoreProperty(id, propertyName,
				propertyValue, type);
		assertEquals(id, property.getUUID());
		assertEquals(propertyName, property.getPropertyName());
		assertEquals(propertyValue, property.getValueAsString());
		assertEquals(type, property.getType());
		assertEquals(CoreProperty.PropertyType.TEXT, property.getPropertyType());

	}

	/**
	 * checks everything that is considered equal in CoreProperty.
	 * 
	 * @param left
	 * @param right
	 */
	public static void testValueEquals() {
		CoreProperty cp1 = new CoreProperty(generateRandomString(), true);
		CoreProperty cp2 = new CoreProperty(generateRandomString(), true);
		assertTrue(cp1.equalsValue(cp2));
	}

	/**
	 * Tests the boolean properties for {@link CoreProperty}
	 * 
	 * @see CoreProperty#CorePropertyEAO(String, Boolean)
	 * @see CoreProperty#isBooleanValue()
	 */

	@Test
	public void testBooleanValue() {
		String propertyName = generateRandomString();
		Boolean booleanValue = generateRandomBoolean();
		CoreProperty property = new CoreProperty(propertyName, booleanValue);
		assertEquals(propertyName, property.getPropertyName());
		assertEquals(booleanValue, property.isBooleanValue());
		Boolean booleanValueFromString = Boolean.valueOf(property
				.getValueAsString());
		assertEquals("getValueAsString() is not producing correct output",
				booleanValue, booleanValueFromString);
		assertEquals(CoreProperty.PropertyType.BOOLEAN,
				property.getPropertyType());
		assertEquals("legacy type is failing", CoreProperty.BOOLEAN_TYPE,
				property.getType());

	}

	/**
	 * Tests the String properties of {@link CoreProperty}.
	 * 
	 */
	@Test
	public void testStringValue() {
		String propertyName = generateRandomString();
		String stringValue = generateRandomString();
		CoreProperty property = new CoreProperty(propertyName, stringValue);
		assertEquals("legacy type is failing", CoreProperty.STRING_TYPE,
				property.getType());
		assertEquals(propertyName, property.getPropertyName());
		assertEquals(stringValue, property.getStringValue());
		assertEquals(stringValue, property.getValueAsString());
		assertEquals(CoreProperty.PropertyType.STRING,
				property.getPropertyType());

	}

	/**
	 * Tests the Double properties of {@link CoreProperty}
	 * 
	 */
	@Test
	public void testDoubleValue() {
		String propertyName = generateRandomString();
		Double doubleValue = generateRandomDouble();
		CoreProperty property = new CoreProperty(propertyName, doubleValue);

		assertEquals(propertyName, property.getPropertyName());
		assertEquals(doubleValue, property.getDoubleValue());
		Double asString = Double.valueOf(property.getValueAsString());
		assertEquals(doubleValue, asString);
		assertEquals(CoreProperty.PropertyType.NUMBER,
				property.getPropertyType());
		assertEquals(doubleValue.intValue(), property.getIntegerValue()
				.intValue());
		assertEquals("legacy type is failing", CoreProperty.NUMBER_TYPE,
				property.getType());

	}

	@Test
	public void testStringArray() {
		String propertyName = generateRandomString();
		final boolean ALLOW_EMPTIES = true;
		String[] array = generateRandomStringArray(ALLOW_EMPTIES);
		CoreProperty property = new CoreProperty(propertyName, array);
		assertEquals("legacy type is failing",
				CoreProperty.PropertyType.STRING_ARRAY.name(),
				property.getType());
		assertEquals(propertyName, property.getPropertyName());
		assertEquals(CoreProperty.PropertyType.STRING_ARRAY,
				property.getPropertyType());
		String[] propertyGeneratedStringArray = property.getStringArray();
		assertArrayEquals(Arrays.toString(array) + " expected, but was "
				+ Arrays.toString(propertyGeneratedStringArray), array,
				propertyGeneratedStringArray);

		String asString = property.getValueAsString();
		CoreProperty propertyByString = new CoreProperty(propertyName,
				asString, PropertyType.STRING_ARRAY);
		assertTrue("property values are not equal",
				property.equalsValue(propertyByString));

		List<String> asList = Arrays.asList(array);
		CoreProperty propertyAsList = new CoreProperty(propertyName, asList);
		assertEquals(asList, propertyAsList.getStringList());
		assertArrayEquals(array, propertyAsList.getStringArray());
		assertArrayEquals(array, propertyByString.getStringArray());
		assertEquals(CoreProperty.PropertyType.STRING_ARRAY,
				property.getPropertyType());

	}

	/**
	 * @see CoreProperty#CorePropertyEAO(String, String, PropertyType)
	 */
	@Test
	public void testBooleanAsString() {
		testValueAsString(generateRandomBoolean().toString(),
				CoreProperty.PropertyType.BOOLEAN);
		// now let's get negative
		try {
			// this should throw an exception
			String randomDouble = generateRandomDouble().toString();
			testValueAsString(randomDouble, CoreProperty.PropertyType.BOOLEAN);
			fail(randomDouble
					+ " shouldn't have been allowed to be converted to boolean");
		} catch (Exception e) {
			// good. we wanted something bad.
		}

	}

	/**
	 * @see CoreProperty#CorePropertyEAO(String, String, PropertyType)
	 */
	@Test
	public void testDoubleAsString() {
		PropertyType propertyType = CoreProperty.PropertyType.NUMBER;
		testValueAsString(generateRandomDouble().toString(), propertyType);
		// now let's get negative
		try {
			// this should throw an exception
			String randomString = generateRandomBoolean().toString();
			testValueAsString(randomString, propertyType);
			fail(randomString
					+ " shouldn't have been allowed to be converted to double");
		} catch (Exception e) {
			// good. we wanted something bad.
		}

	}

	/**
	 * @see CoreProperty#CorePropertyEAO(String, String, PropertyType)
	 */
	@Test
	public void testStringAsString() {
		PropertyType propertyType = CoreProperty.PropertyType.STRING;
		testValueAsString(generateRandomString(), propertyType);
		// there is no negative situation here.
	}

	/**
	 * Given a value as a string and a matching type this will construct and
	 * test the value conversion.
	 * 
	 * @param value
	 * @param propertyType
	 */
	public void testValueAsString(String value,
			CoreProperty.PropertyType propertyType) {
		String propertyName = generateRandomString();
		CoreProperty property = new CoreProperty(propertyName, value,
				propertyType);
		assertEquals(propertyType, property.getPropertyType());

	}

}
