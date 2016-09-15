/**
 * 
 */
package com.akuacom.pss2.signal;

import static com.akuacom.test.TestUtil.generateRandomBoolean;
import static com.akuacom.test.TestUtil.generateRandomDouble;
import static com.akuacom.test.TestUtil.*;
import static com.akuacom.test.TestUtil.generateRandomString;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.akuacom.ejb.BaseEntityFixture;
import com.akuacom.ejb.BaseEntityUtil;
import com.akuacom.test.TestUtil;

/**
 * Unit test for {@link SignalDef}.
 * 
 * A good example of how to handle composite relationships Signal is composed of
 * (or owns) SignalLevels.
 * 
 * @author roller
 * 
 */
public class SignalTest extends BaseEntityFixture<SignalDef> {

	private static final double DOUBLE_VALUE_PRECISION = 0.00001;
	private static SignalTest instance;

	@Test
	public void testGettersSetters() {

	}

	public static SignalTest getInstance() {
		if (instance == null) {
			instance = new SignalTest();
		}
		return instance;
	}

	@Override
	public SignalDef generateRandomIncompleteEntity() {
		SignalDef signal = new SignalDef();
		String type = TestUtil.generateRandomString();
		String signalName = type;
		signal.setSignalName(signalName);
		assertEquals(signalName, signal.getSignalName());
		signal.setType(type);
		signal.setSignalLevels(generateRandomSignalLevels(signal, false));
		return signal;
	}

	public static Set<SignalLevelDef> generateRandomSignalLevels(SignalDef signal,
			boolean populateBaseEntityFields) {
		int count = generateRandomInt(10);
		HashSet<SignalLevelDef> signalLevels = new HashSet<SignalLevelDef>();
		for (int i = 0; i < count; i++) {
			signalLevels.add(generateRandomSignalLevel(signal,
					populateBaseEntityFields));
		}
		return signalLevels;
	}

	public static SignalLevelDef generateRandomSignalLevel(SignalDef signal,
			boolean populateBaseEntityFields) {

		SignalLevelDef signalLevel = new SignalLevelDef();
		if (populateBaseEntityFields) {
			populateRandomProperties(signalLevel);
		}
		Boolean defaultValue = generateRandomBoolean();
		signalLevel.setDefaultValue(defaultValue);
		assertEquals("defaultValue", defaultValue, signalLevel.isDefaultValue());

		/*  needs signal refactor
		double doubleValue = generateRandomDouble();
		signalLevel.setDoubleValue(doubleValue);
		assertEquals(doubleValue, signalLevel.getDoubleValue(),
				DOUBLE_VALUE_PRECISION);
				*/

		signalLevel.setSignal(signal);
		assertSignalEquals(signal, signalLevel.getSignal());

		String stringValue = generateRandomString();
		signalLevel.setStringValue(stringValue);
		assertEquals(stringValue, signalLevel.getStringValue());
		return signalLevel;
	}

	public static void assertSignalEquals(SignalDef expected, SignalDef actual) {
		assertEquals(expected.getSignalName(), actual.getSignalName());
		assertEquals(expected.getType(), actual.getType());
		assertSignalLevelEquals(expected.getSignalLevels(), actual
				.getSignalLevels());
	}

	private static void assertSignalLevelEquals(Set<SignalLevelDef> expecteds,
			Set<SignalLevelDef> actuals) {
		if (expecteds != null && actuals != null) {
			assertEquals("set lengths should be the same", expecteds.size(),
					actuals.size());

			for (SignalLevelDef actual : actuals) {
				boolean matchFound = false;
				for (SignalLevelDef expected : expecteds) {
					String expectedValue = expected.getStringValue();
					if(expectedValue != null && expectedValue.equals(actual.getStringValue())){
						assertSignalLevelEquals(expected, actual);
						matchFound = true;
					}
				}
				if(!matchFound){
					fail("no match found for " + actual.getSignal().getSignalName());
				}
				
			}
		}

	}

	private static void assertSignalLevelEquals(SignalLevelDef expected,
			SignalLevelDef actual) {
		/*  needs signal refactor
		assertEquals(actual.getSignal().getSignalName() + " has invalid level " + actual.getUUID(), expected.getDoubleValue(), actual.getDoubleValue(),
				DOUBLE_VALUE_PRECISION);
		assertEquals(expected.getSignal().getSignalName(), actual.getSignal().getSignalName());
		assertEquals(expected.getStringValue(), actual.getStringValue());
*/
	}

}
