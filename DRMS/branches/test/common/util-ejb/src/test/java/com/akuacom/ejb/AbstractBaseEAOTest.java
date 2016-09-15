/**
 * 
 */
package com.akuacom.ejb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.test.TestUtil;

/**
 * The generic test class that will test the basic methods made available by
 * {@link BaseEAO}.
 * 
 * Children must implement worker methods to take care of the details for a few
 * items, but overall this will test the CRUD and whatever is exposed by
 * {@link BaseEAO}. The rest is up to you.
 * 
 * @author roller
 * 
 */
public abstract class AbstractBaseEAOTest<EntityEAOType extends BaseEAO<EntityType>, EntityType extends BaseEntity>
		extends JBossFixture {

	public static String DEFAULT_CREATOR = "a";
	protected EntityEAOType eao;

	// provides a static cache of the eao so lookups only happen once.
	private  BaseEAO<BaseEntity> staticEAO;

	/** Used only for remote testing. */
	protected AbstractBaseEAOTest(Class<? extends EntityEAOType> eaoBeanClass) {
		this(eaoBeanClass, new Class[] {});
	}

	/**
	 * Provide the eaoBeanClass that matches the EntityType generic.
	 * 
	 * The classes are the classes to deploy into the embedded container when
	 * running as embedded.
	 * 
	 * @param eaoBeanClass
	 * @param embeddedClassesToDeploy
	 * @throws DeploymentException
	 */
	public AbstractBaseEAOTest(Class<? extends EntityEAOType> eaoBeanClass,
			Class... embeddedClassesToDeploy) {

		super(embeddedClassesToDeploy);
		initialize(eaoBeanClass);

	}

    private void initialize(Class<? extends EntityEAOType> eaoBeanClassForLookup) {

		if (staticEAO == null) {
			if (isEmbedded()) {
				// embedded jboss uses a different JNDI Naming standard
				eao = JBossFixture.lookupSession(eaoBeanClassForLookup, false);
			} else {
				// this uses the standard ear-based lookup
				eao = lookupSessionRemote(eaoBeanClassForLookup);
			}
			staticEAO = (BaseEAO<BaseEntity>) eao;
		} else {
			eao = (EntityEAOType) staticEAO;
		}
	}

	/**
	 * Generically tests the create/read/update/delete methods for any CommonEAO
	 * implementation
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	public void testCrud() throws DuplicateKeyException,
			EntityNotFoundException {
		EntityType generated = generateRandomEntity();

		String randomUUID = TestUtil.generateRandomString();
		EntityType found;
		EntityType afterMute;
		try {
			found = eao.getById(randomUUID);
			assertNull(
					"hey, why is this already found...shouldn't we throw exceptions? "
							+ randomUUID, found);
			fail("nothing should be found and the exception should have been thrown");
		} catch (EntityNotFoundException e) {
			// this is the correct behavior
		}

		// (C) test create
		EntityType created = eao.create(generated);
		assertEntityValuesEquals(generated, created);
		// may not be considered equal because ID is different

		String uuid = created.getUUID();
		assertNotNull(uuid);

		assertBasePropertiesAfterCreation(created);

		// (R) test get
		found = eao.getById(uuid);
		assertEquals(created, found);

		// (U) now, let's mutate and test the set method
		mutate(found);
		afterMute = eao.update(found);
		// ids should still be the same
		assertEquals(created.getUUID(), afterMute.getUUID());
		assertEntityValuesNotEquals(created, afterMute);
		
		assertBasePropertiesAfterModification(found,afterMute);
		
		// (D) test removal
		delete(uuid);
		try {
			found = eao.getById(uuid);
			fail("it should have been deleted");
		} catch (EntityNotFoundException e) {
			// cool..it should have been deleted
		}

	}

	/**
	 * Deletes entity by calling deleteRelationship.
	 * @param entity
	 * @throws EntityNotFoundException
	 */
	public void delete(EntityType entity) throws EntityNotFoundException {
		eao.delete(entity);
	}
	
	public void delete(String uuid) throws EntityNotFoundException {
		EntityType entity = eao.getById(uuid);
		delete(entity);
	}

	/**
	 * Intended to be called right after creation to ensure the base properties
	 * are set as desired.
	 * 
	 * @param created
	 */
	protected void assertBasePropertiesAfterCreation(EntityType created) {
		// test base properties.
		Date createdDate = created.getCreationTime();
		assertNotNull(createdDate);
	}

	/**
	 * Intended to be called right after updation to ensure base properties are
	 * set as desired.
	 * 
	 * @param created
	 * @param mutated
	 */
	protected void assertBasePropertiesAfterModification(EntityType created,
			EntityType mutated) {
		// make sure creation time is the same as previously because ...we
		// modified, not created.
		Date foundCreationTime = mutated.getCreationTime();
		assertNotNull(foundCreationTime);
		Date createdDate = created.getCreationTime();
		TestUtil.assertDateEqualsNoMillis(createdDate, foundCreationTime);

	}

	/**
	 * Creates a couple and finds them in the list returned.
	 * 
	 * @throws DuplicateKeyException
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	public void testFindAll() throws DuplicateKeyException,
			EntityNotFoundException {
		EntityType entity1 = generateRandomPersistedEntity();
		EntityType entity2 = generateRandomPersistedEntity();

		List<EntityType> all = eao.getAll();
		assertTrue(all.contains(entity1));
		assertTrue(all.contains(entity2));

		// clean it up
		delete(entity1);
		delete(entity2);

	}

	/**
	 * Creates an entity and tests that it can be set, then mutates a 
	 * copy and tests that it is set as a seperate object.
	 * @throws DuplicateKeyException
	 * @throws EntityNotFoundException
	 */
	@Test
	@Ignore
	public void testSet() throws DuplicateKeyException, EntityNotFoundException {
		EntityType generated = generateRandomEntity();
		
		EntityType created = eao.merge(generated);
		assertEntityValuesEquals(generated, created);
		assertBasePropertiesAfterCreation(created);
		EntityType mutated = eao.get(created);
		mutate(mutated);

		assertEntityValuesNotEquals(created, mutated);

		EntityType resultOfMutatedSet = eao.merge(mutated);
		// ensure entity ids are the same
		assertEquals("ids are not the same", created, resultOfMutatedSet);
		assertEntityValuesNotEquals(created, resultOfMutatedSet);
		assertEntityValuesEquals(mutated, resultOfMutatedSet);
		assertBasePropertiesAfterModification(mutated, resultOfMutatedSet);

		delete(resultOfMutatedSet);

	}

	/**
	 * Null ID is o.k. as long as there are other ways to find. This will not be
	 * able to find since business keys should be random and not in database.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test(expected = EntityNotFoundException.class)
	public void testUpdateNullId() throws EntityNotFoundException {
		EntityType entity = generateRandomEntity();
		entity.setUUID(null);
		eao.update(entity);
	}

	/**
	 * Just to make sure it will not update something that gives a bogus id.
	 * 
	 * @throws EntityNotFoundException
	 */
	@Test(expected = EntityNotFoundException.class)
	public void testUpdateBadId() throws EntityNotFoundException {
		EntityType nonExistent = generateRandomEntity();
		eao.update(nonExistent);
	}

	/**
	 * Tests delete  method 
	 * @throws EntityNotFoundException
	 * @throws DuplicateKeyException
	 */
	@Test
	public void testDelete() throws EntityNotFoundException,
			DuplicateKeyException {
		EntityType generated = generateRandomEntityWithUUID();
		try {
			delete(generated);
			fail("can't delete what doesn't exist");
		} catch (EntityNotFoundException e) {
			generated.deleteUUID();
			EntityType created = eao.create(generated);
			delete(created);
			try {
				eao.get(created);
				fail("what the heck, this should have been deleted " + created);
			} catch (EntityNotFoundException e1) {
				// happy exit
			}
		}

	}

	/**
	 * Get's the generated entity from the child implementation and creates it
	 * so the recipient of this Entity will know it is in the database. Please
	 * consider removing this entity when you are done with it.
	 * 
	 * @return
	 * @throws DuplicateKeyException
	 */
	public EntityType generateRandomPersistedEntity()
			throws DuplicateKeyException {
		return eao.create(generateRandomEntity());
	}

	/**
	 * Delegated to implementor to determine that the two entity values are not
	 * equal
	 * 
	 * @param created
	 * @param found
	 */
	abstract protected void assertEntityValuesNotEquals(EntityType created,
			EntityType found);

	/**
	 * Delegated to implementor intending to modify values of significance of
	 * the given entity.
	 * 
	 * @param found
	 */
	abstract protected void mutate(EntityType found);

	abstract protected void assertEntityValuesEquals(EntityType created,
			EntityType found);

	/**
	 * Must generated a popuplated entitye (without a UUID) so that it can test
	 * the properties needed. Consider calling your POJO test to have a static
	 * method generate this fake entity for you.
	 * 
	 * @return
	 */
	abstract protected EntityType generateRandomEntity();
	
	protected  EntityType generateRandomEntityWithUUID(){
		EntityType entity = generateRandomEntity();
		BaseEntityFixture.populateRandomProperties(entity);
		return entity;
	}

}
