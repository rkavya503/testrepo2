/**
 * 
 */
package com.akuacom.ejb;

import org.junit.Test;

import com.akuacom.common.resource.ManagedResourceBundleTest;
import com.akuacom.test.TestUtil;
import static org.junit.Assert.*;

/**
 * @see EJBExceptionMessage
 * @author roller
 * 
 */
public class EJBExceptionMessageTest {

	@Test
	public void testValidBundle() {

		ManagedResourceBundleTest
				.validateManagedResourceBundle(EJBExceptionMessage.class);
	}

	@Test(expected = DuplicateKeyException.class)
	public void testDuplicateKeyException() throws DuplicateKeyException {
		throw new DuplicateKeyException();
	}

	@Test(expected = EntityNotFoundException.class)
	public void testEntityNotFoundException() throws EntityNotFoundException {

		throw new EntityNotFoundException(TestUtil.generateRandomString());
	}

	@Test
	public void testDuplicateKeyExceptionMessage() {
		String uuid = "12345";
		String message = new DuplicateKeyException(uuid).getMessage();
		// TODO:Would be nice if the message replaced the parameter with the
		// uuid.
		assertEquals(EJBExceptionMessage.DUPLICATE_KEY.getValue(), message);
	}

}