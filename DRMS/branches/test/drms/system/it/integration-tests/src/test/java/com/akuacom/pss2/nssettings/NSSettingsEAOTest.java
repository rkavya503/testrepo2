package com.akuacom.pss2.nssettings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.akuacom.ejb.AbstractBaseEAOTest;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.test.TestUtil;

/**
 * NSSettingsTest is an intergration test for notification system global
 * settings function.
 * 
 * @author Li Fei
 * 
 *         Initial date 2010.07.01
 */

public class NSSettingsEAOTest extends
		AbstractBaseEAOTest<NSSettingsEAO, NSSettings> {
	public static final String DEFAULT_PASSWORD = "Test_1234";

	public NSSettingsEAOTest() {
		super(NSSettingsEAO.class);
	}

	@Override
	protected void assertEntityValuesNotEquals(NSSettings created,
			NSSettings found) {
		assertTrue(created.getFilterStatus() != found.getFilterStatus());
	}

	@Override
	protected void mutate(NSSettings found) {
		found.setFilterStatus(TestUtil.generateRandomInt(2048));
	}

	@Override
	protected void assertEntityValuesEquals(NSSettings created, NSSettings found) {
		assertEquals(created.getFilterStatus(), found.getFilterStatus());
	}

	@Test
	public void testGetNSSettings() {
		NSSettings defaultNss = eao.getNSSettings();
		assertNotNull(defaultNss);
	}

	@Test
	public void testGetNSSettingsDefault() throws EntityNotFoundException {
		List<NSSettings> nss = eao.getAll();
		for (NSSettings n : nss) {
			eao.delete(n);
		}

		NSSettings defaultNss = eao.getNSSettings();
		assertNotNull(defaultNss);
	}

	@Test
	public void testSaveNSSettings() {
		NSSettings setting = eao.getNSSettings();
		setting.setFrequency(1223);

		eao.saveNSSettings(setting);
	}

	@Override
	protected NSSettings generateRandomEntity() {
		NSSettings nSSettings = new NSSettingsTest()
				.generateRandomIncompleteEntity();

		assertNotNull(nSSettings);
		return nSSettings;
	}

}
