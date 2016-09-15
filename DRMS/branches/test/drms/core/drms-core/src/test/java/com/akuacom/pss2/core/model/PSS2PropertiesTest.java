/**
 * 
 */
package com.akuacom.pss2.core.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Test;

import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyTest;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.test.TestUtil;

/**
 * Testing the pojo {@link PSS2Properties}
 * 
 * @author roller
 * 
 */
public class PSS2PropertiesTest {

	/**
	 * Use this to get a fully loaded random properties. THis will also run a
	 * bunch of asserts making sure the population works too.
	 * 
	 * @see #testProperties()
	 * @return
	 */
	public static PSS2Properties generateRandomProperties() {
		final double MIN_DOUBLE_THRESHOLD = 0.0000001;
		PSS2Properties testProperties = new PSS2Properties();

		String contactEMail = TestUtil.generateRandomString();
		testProperties.setContactEMail(contactEMail);
		assertEquals(contactEMail, testProperties.getContactEMail());

		String contactPhone = TestUtil.generateRandomString();
		testProperties.setContactPhone(contactPhone);
		assertEquals(contactPhone, testProperties.getContactPhone());

		String contactURLDisplayName = TestUtil.generateRandomString();
		testProperties.setContactURLDisplayName(contactURLDisplayName);
		assertEquals(contactURLDisplayName, testProperties
				.getContactURLDisplayName());

		String contactURLLink = TestUtil.generateRandomString();
		testProperties.setContactURLLink(contactURLLink);
		assertEquals(contactURLLink, testProperties.getContactURLLink());

		String firelog = TestUtil.generateRandomString();
		testProperties.setFirelog(firelog);
		assertEquals(firelog, testProperties.getFirelog());

		String[] logCategories = TestUtil.generateRandomStringArray(false);
		testProperties.setLogCategories(logCategories);
		assertArrayEquals(logCategories, testProperties.getLogCategories());

		String[] logoUrls = TestUtil.generateRandomStringArray(false);
		testProperties.setLogoUrls(logoUrls);
		assertArrayEquals(logoUrls, testProperties.getLogoUrls());

//		long offlineErrorThresholdM = TestUtil.generateRandomInt();
//		testProperties.setOfflineErrorThresholdM(offlineErrorThresholdM);
//		assertEquals(offlineErrorThresholdM, testProperties
//				.getOfflineErrorThresholdM());

		long offlineWarningThresholdM = TestUtil.generateRandomInt();
		testProperties.setOfflineWarningThresholdM(offlineWarningThresholdM);
		assertEquals(offlineWarningThresholdM, testProperties
				.getOfflineWarningThresholdM());

		String utilityDisplayName = TestUtil.generateRandomString();
		testProperties.setUtilityDisplayName(utilityDisplayName);
		assertEquals(utilityDisplayName, testProperties.getUtilityDisplayName());

		String utilityName = TestUtil.generateRandomString();
		testProperties.setUtilityName(utilityName);
		assertEquals(utilityName, testProperties.getUtilityName());

		String version = TestUtil.generateRandomString();
		testProperties.setVersion(version);
		assertEquals(version, testProperties.getVersion());

		return testProperties;
	}

	/**
	 * Just calls {@link #generateRandomProperties()} which does a bunch of
	 * asserts.
	 * 
	 */
	@Test
	public void testProperties() {
		// asserts are built into generator
		PSS2Properties properties = generateRandomProperties();
		assertNotNull(properties);

	}

	/**
	 * Makes sure the {@link PSS2Properties#equalValues(PSS2Properties)} method
	 * works.
	 * 
	 * 
	 */
	@Test
	public void testEqualValues() {
//		String utilityName = TestUtil.generateRandomString();
//		PSS2Properties properties1 = new PSS2Properties();
//		properties1.setUtilityName(utilityName);
//
//		PSS2Properties properties2 = new PSS2Properties();
//		properties2.setUtilityName(utilityName);
//
//		assertEqualProperties(properties1, properties2);
//
//		// now make sure if they are not the same the equals tells us so.
//		properties2.setUtilityName(TestUtil.generateRandomString());
//		assertFalse(properties1.equalValues(properties2));
	}

	/**
	 * Ensures the {@link PSS2Properties#PSS2Properties(java.util.Collection)}
	 * constructor receiving a collection will transfer all properties
	 * successfully.
	 * 
	 * @see PSS2Properties#PSS2Properties(java.util.Collection)
	 */
	@Test
	public void testCollectionConstructor() {
		PSS2Properties properties = generateRandomProperties();
		PSS2Properties constructed = new PSS2Properties(properties
				.getAllCoreProperties());
		assertEqualProperties(properties, constructed);
	}

	@Test
	public void testPropertyNameEnum() {
		PSS2Properties.PropertyName utilityName = PSS2Properties.PropertyName.UTILITY_NAME;
		// test the lookup of propertyName
		PSS2Properties.PropertyName found = PSS2Properties.PropertyName
				.valueFromPropertyName(utilityName.toString());
		assertEquals(utilityName, found);

		// test the lookup of the name (just to make sure we didn't mess it up,
		// which we can't).
		found = PSS2Properties.PropertyName.valueOf(utilityName.name());
		assertEquals(utilityName, found);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAll() {
		PSS2Properties properties = new PSS2Properties();

		properties.setContactPhone(TestUtil.generateRandomString());
		properties.setContactEMail(TestUtil.generateRandomString());

		Set<CoreProperty> all = properties.getAllCoreProperties();

		assertEquals(2, all.size());

		// can't mutate so this should be unsupported as expected exit
		all.add(new CorePropertyTest().generateRandomEntity());

	}

	@Test
	public void testDefaultValues() {
		PSS2Properties emptyProperties = new PSS2Properties();
		String defaultValue = TestUtil.generateRandomString();
		String result = emptyProperties.getStringValue(
				PropertyName.ABOUT_INFORMATION, defaultValue);
		assertEquals(defaultValue, result);
	}

	public static void assertEqualProperties(PSS2Properties testProperties,
			PSS2Properties storedProperties) {

	}

}
