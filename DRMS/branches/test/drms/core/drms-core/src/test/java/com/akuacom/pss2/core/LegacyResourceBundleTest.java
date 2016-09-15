package com.akuacom.pss2.core;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.akuacom.common.exception.ExceptionLocalizerFactory;
import com.akuacom.test.TestUtil;

public class LegacyResourceBundleTest {

	
	/**Legacy ResourceUtilBundle uses ${?} params and is supported in Exception Localizer.
	 * 
	 */
	@Test
	public void testLegacyResourceBundleUtil() {
		String KEY = "ERROR_EVENT_CREATE_BID_FILE_BID_BLOCKS_DONT_FIT";
		String EXPECTED_MESSAGE = "Bid blocks don't fit exactly between ${?} and ${?}";
		String PARAM1 = TestUtil.generateRandomString();
		String PARAM2 = TestUtil.generateRandomString();
		String EXPECTED_MESSAGE_FORMATTED = "Bid blocks don't fit exactly between " + PARAM1 + " and " + PARAM2;
		
		String[] PARAMS = { PARAM1, PARAM2 };
		String actualMessageNoParams = ExceptionLocalizerFactory.getInstance()
				.getExceptionLocalizer(KEY).getMessage();
		assertEquals(EXPECTED_MESSAGE, actualMessageNoParams);

		String acutalFormattedMessage = ExceptionLocalizerFactory.getInstance()
				.getExceptionLocalizer(KEY, PARAMS).getMessage();
		assertEquals(EXPECTED_MESSAGE_FORMATTED,acutalFormattedMessage);
	

		acutalFormattedMessage = ResourceBundleUtil.getLocalizedString(KEY, Arrays.asList(PARAMS));
		assertEquals(EXPECTED_MESSAGE_FORMATTED,acutalFormattedMessage);
	}
}
