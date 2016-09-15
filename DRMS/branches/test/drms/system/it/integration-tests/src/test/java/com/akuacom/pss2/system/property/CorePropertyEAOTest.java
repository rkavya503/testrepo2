/**
 * 
 */
package com.akuacom.pss2.system.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.ejb.EJBTransactionRolledbackException;

import org.junit.Assert;
import org.junit.Test;

import com.akuacom.ejb.AbstractVersionedEAOTest;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.test.TestUtil;

/**
 * Testing the CRUD methods on the {@link CorePropertyEAO} session bean.
 * 
 * @author roller
 * 
 */
public class CorePropertyEAOTest extends
		AbstractVersionedEAOTest<CorePropertyEAO, CoreProperty> {

	/**
	 * The default Embedded container test.
	 * 
	 * @see JBossEmbeddedFixture
	 * */
	public CorePropertyEAOTest() {
		super(CorePropertyEAOBean.class, CorePropertyEAO.class,
				CorePropertyEAOBean.class, CoreProperty.class);
	}

	/**
	 * Property name is a standard finder. This will ensure that specific method
	 * is working.
	 * 
	 * @throws EntityNotFoundException
	 *             entity not found
	 * 
	 */
	@Test(expected = EntityNotFoundException.class)
	public void testFindByPropertyName() throws EntityNotFoundException {
		CoreProperty property = generateRandomEntity();
		String propertyName = property.getPropertyName();
		CoreProperty found = eao.getByPropertyName(propertyName);
		assertEntityValuesEquals(property, found);
		eao.delete(found);
		eao.getByPropertyName(propertyName);
		fail("This should have thrown EntityNotFoundException");
	}

	@Test
	public void testSetByPropertyName() throws DuplicateKeyException,
			EntityNotFoundException {
		CoreProperty original = generateRandomPersistedEntity();
		CoreProperty found = eao.get(original);
		mutate(found);
		CoreProperty mutated = eao.merge(found);
		assertEquals(original.getUUID(), mutated.getUUID());
		assertEquals(original.getPropertyName(), mutated.getPropertyName());
		assertEquals(found.getValueAsString(), mutated.getValueAsString());
	}

	@Test(expected = EJBTransactionRolledbackException.class)
	public void testDuplicatePropertyName() throws DuplicateKeyException,
			EntityNotFoundException {
		CoreProperty original = generateRandomPersistedEntity();
		CoreProperty copy = new CoreProperty(original.getPropertyName(),
				original.getValueAsString(), original.getPropertyType());
		assertEntityValuesEquals(original, copy);
		try {
			copy = eao.create(copy);
			eao.delete(copy);
			fail("should have thrown an EJBTransactionRolledbackException due to uniqueness constraint violation of propertyName: "
					+ copy.getPropertyName() + " and "
					+ original.getPropertyName());
		} finally {
			eao.delete(original);
		}

	}
	
	protected void assertEntityValuesNotEquals(CoreProperty created,
			CoreProperty found) {
		assertTrue(!created.equalsValue(found));
	}

	@Override
	protected void mutate(CoreProperty entity) {
		entity.setStringValue(TestUtil.generateRandomString());
	}

	@Override
	protected void assertEntityValuesEquals(CoreProperty created,
			CoreProperty found) {
		assertTrue(created.equalsValue(found));

	}

	@Override
	protected CoreProperty generateRandomEntity() {
		CoreProperty entity = new CorePropertyTest().generateRandomIncompleteEntity();
		Assert.assertNotNull(entity);
		return entity;
	}

}
