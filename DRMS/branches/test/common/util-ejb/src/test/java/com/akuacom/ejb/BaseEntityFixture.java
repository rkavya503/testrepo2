/**The Base Test class for all Entity Tests to inherit.

Add whatever you like.
 * 
 */
package com.akuacom.ejb;

import static org.junit.Assert.*;

import org.junit.Test;

import com.akuacom.test.TestUtil;

/**
 * The base test class for any test that is testing an implementation of
 * {@link BaseEntity}.
 * 
 * @author roller
 * 
 */
abstract public class BaseEntityFixture<EntityType extends BaseEntity> {

	protected static String genereateRandomUUID() {
		return TestUtil
				.generateRandomStringOfLength(BaseEntity.Details.UUID_LENGTH);
	}

	protected static <EntityType extends BaseEntity> void populateRandomProperties(
			EntityType entity) {
		entity.setUUID(genereateRandomUUID());

	}

	protected void assertBaseEntityProperties(EntityType expected,
			EntityType actual) {
		assertEquals("uuid", expected.getUUID(), actual.getUUID());
		// leaving the versioned properties alone for now...implement if you
		// think it is a good idea.
	}

	/**
	 * Calls {@link #generateRandomIncompleteEntity()} and then
	 * {@link #populateRandomProperties(BaseEntity)} to provide a complete
	 * populated entity.
	 * 
	 * @return
	 */
	public EntityType generateRandomEntity() {
		EntityType entity = generateRandomIncompleteEntity();
		populateRandomProperties(entity);
		return entity; 
	}

	/**
	 * IMplemented by the children, this will generate a valid Entity and do
	 * proper assertions making sure those values assigned are able to be
	 * loaded.
	 * 
	 * It is incomplete because the standard properties of {@link BaseEntity}
	 * will not be assigned. Call {@link #generateRandomEntity()} if you want
	 * all fields including UUID populated.
	 * 
	 * 
	 * 
	 * @return
	 */
	public abstract EntityType generateRandomIncompleteEntity();

	/**
	 * Simply calls the generator since the generator will provide the necessary
	 * asserts.
	 * 
	 */
	@Test
	public void testGettersSetters() {
		assertNotNull(generateRandomIncompleteEntity());

	}
}
