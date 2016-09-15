/**
 * 
 */
package com.akuacom.pss2.system.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerFixture;
import com.akuacom.test.TestUtil;

/**
 * Course-grained system testing of the {@link SystemManager} focusing solely on
 * the property capabilities.
 * 
 * @author roller
 * 
 */
public class SystemManagerPropertyTest extends SystemManagerFixture {

	/**
	 * A simple check to find a known value in the database.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	public void testUtilityName() throws EntityNotFoundException {
		String propertyName = PSS2Properties.PropertyName.UTILITY_NAME
				.getPropertyName();
		CoreProperty utilityNameProperty = systemManager
				.getPropertyByName(propertyName);
		assertNotNull(propertyName + " not found", utilityNameProperty);
		assertEquals(propertyName, utilityNameProperty.getPropertyName());
		assertNotNull(utilityNameProperty.getStringValue());

	}

	/**
	 * Specifically making sure LogCategories exists and is able to be parsed
	 * into an array with the pre-populated database.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	public void testLogCategories() throws EntityNotFoundException {
		String propertyName = PSS2Properties.PropertyName.LOG_CATEGORIES
				.getPropertyName();
		CoreProperty property = systemManager.getPropertyByName(propertyName);
		assertNotNull(property);
		String[] value = property.getStringArray();
		assertTrue(value.length > 1);
	}

	public static CoreProperty createCoreProperty() {
		return systemManager.setProperty(new CorePropertyTest().generateRandomEntity());
	}

	@Test
	public void testPropertyCreate() {
		String propertyName = TestUtil.generateRandomString();
		String propertyValue = TestUtil.generateRandomString();
		CoreProperty property = new CoreProperty(propertyName, propertyValue);
		CoreProperty createdProperty = systemManager.setProperty(property);
		assertNotNull(createdProperty);
		assertTrue("values aren't equal: " + property.toString() + " and "
				+ createdProperty.toString(), property
				.equalsValue(createdProperty));
		
		String mutatedPropertyValue = TestUtil.generateRandomString();
		CoreProperty mutated = new CoreProperty(propertyName,mutatedPropertyValue);
		mutated.setUUID(createdProperty.getUUID());
		CoreProperty mutationResult = systemManager.setProperty(mutated);
		assertEquals(createdProperty.getUUID(),mutationResult.getUUID());
		assertEquals(mutatedPropertyValue,mutationResult.getStringValue());

	}

	@Test(expected = EntityNotFoundException.class)
	public void testPropertyValueUpdate() throws EntityNotFoundException {
		CoreProperty property = createCoreProperty();
		String updatedValue = TestUtil.generateRandomString();
		CoreProperty updatedProperty = systemManager.updatePropertyValue(
				property.getUUID(), updatedValue);
		assertEquals(property, updatedProperty);
		assertEquals(updatedValue, updatedProperty.getStringValue());
		// now test to make sure entity not found is working
		systemManager.updatePropertyValue(TestUtil.generateRandomString(),
				updatedValue);
	}

	/**
	 * Gets all the properties, verifies there are the expected number and
	 * retrieves each on individually verifying the values are equal.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test
	public void testGetAllProperties() throws EntityNotFoundException {
		List<CoreProperty> coreProperties = systemManager.getAllProperties();

		assertTrue("Not even one core property?", !coreProperties.isEmpty());
		for (CoreProperty coreProperty : coreProperties) {
			CoreProperty corePropertyFound = systemManager
					.getPropertyByName(coreProperty.getPropertyName());
			assertNotNull(coreProperty.getPropertyName() + " not found",
					corePropertyFound);
			assertPropertyEquals(coreProperty, corePropertyFound);
		}

	}

	/**
	 * Retrieves all PSS2 Properties and tests that each one is provided in
	 * PSS2Properties as is individually retrieved by the manager.
	 * 
	 * These values are not modified to avoid modifying values that the app
	 * needs to run successfully.
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	public void testPSS2Properties() throws EntityNotFoundException {
		PSS2Properties originalProperties = systemManager.getPss2Properties();
		assertNotNull(originalProperties);
		Set<CoreProperty> allProperties = originalProperties
				.getAllCoreProperties();
		for (CoreProperty coreProperty : allProperties) {
			CoreProperty found = systemManager.getPropertyByName(coreProperty
					.getPropertyName());
			assertEquals(coreProperty, found);
		}

	}

	@Test
	public void testPSS2Features() {
		CoreProperty cpFeedback = new CoreProperty(
				PSS2Features.FeatureName.FEEDBACK.toString(), new Boolean(true));
		CoreProperty cpLocation = new CoreProperty(
				PSS2Features.FeatureName.LOCATION.toString(),
				new Boolean(false));
		CoreProperty cpShedInfo = new CoreProperty(
				PSS2Features.FeatureName.SHED_INFO.toString(),
				new Boolean(true));

		systemManager.setProperty(cpFeedback);
		systemManager.setProperty(cpLocation);
		systemManager.setProperty(cpShedInfo);

		PSS2Features feats = systemManager.getPss2Features();

		assertEquals(cpFeedback.isBooleanValue(), feats
				.isFeatureFeedbackEnabled());
		assertEquals(cpLocation.isBooleanValue(), feats
				.isFeatureLocationEnabled());
		assertEquals(cpShedInfo.isBooleanValue(), feats
				.isFeatureShedInfoEnabled());
	}

	public static void assertPropertyEquals(CoreProperty expected,
			CoreProperty actual) {
		assertEquals("property name not equal", expected.getPropertyName(),
				actual.getPropertyName());

		assertEquals("types are different", expected.getType(), actual
				.getType());
		assertEquals("values are different", expected.getStringValue(),
				expected.getStringValue());
	}

}
