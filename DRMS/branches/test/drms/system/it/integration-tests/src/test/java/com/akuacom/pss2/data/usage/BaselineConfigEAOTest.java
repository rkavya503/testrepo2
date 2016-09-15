package com.akuacom.pss2.data.usage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.akuacom.ejb.AbstractVersionedEAOTest;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.test.TestUtil;

public class BaselineConfigEAOTest extends AbstractVersionedEAOTest<BaselineConfigEAO, BaselineConfig> {

	private List<BaselineModel> originalExisted = new ArrayList<BaselineModel>();
	BaselineModelEAO modelEAO = JBossFixture.lookupSessionRemote(BaselineModelEAO.class);
	BaselineConfigEAO configEAO = JBossFixture.lookupSessionRemote(BaselineConfigEAO.class);

	public BaselineConfigEAOTest() {
		super(BaselineConfigEAO.class);
	}

	@Before
	public void setup() throws EntityNotFoundException {
		List<BaselineModel> existed = modelEAO.getAll();
		for (BaselineModel model : existed) {

			BaselineModel destModel = BaselineModelEAOTest.generateBaselineModelClone(model);
			
			Set<BaselineConfig> configs = new HashSet<BaselineConfig>();
			for(BaselineConfig config:model.getBaselineConfigs()){
				
				BaselineConfig destConfig = BaselineModelEAOTest.generateBaselineConfigClone(config,destModel);
				configs.add(destConfig);
			}
			destModel.setBaselineConfigs(configs);
			
			originalExisted.add(destModel);
		}
		for (BaselineModel model : modelEAO.getAll()) {
			modelEAO.delete(model);
		}
	}
	
	@After
	public void tearDown() throws EntityNotFoundException {
		
		for (BaselineModel model : modelEAO.getAll()) {
			modelEAO.delete(model);
		}
		
		if(originalExisted!=null){
			for (BaselineModel model : originalExisted) {
				try {
					modelEAO.create(model);
				} catch (DuplicateKeyException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void assertEntityValuesEquals(BaselineConfig created,
			BaselineConfig found) {

	}

	@Override
	protected void assertEntityValuesNotEquals(BaselineConfig created,
			BaselineConfig found) {

	}

	@Override
	protected BaselineConfig generateRandomEntity() {
		BaselineConfig entity = new BaselineConfig();
		entity.setUUID(TestUtil.generateRandomStringOfLength(32));
		entity.setOwnerUuid(TestUtil.generateRandomStringOfLength(32));
		entity.setName(TestUtil.generateRandomStringOfLength(256));
		entity.setExcludeHolidayFromCalc(TestUtil.generateRandomBoolean());
		entity.setExcludedDaysOfWeekFromCalc(generateRandomNumber(7));
		entity.setExcludeHoliday(TestUtil.generateRandomBoolean());
		entity.setExcludedDaysOfWeek(generateRandomNumber(7));
		entity.setExcludeAbnormalDayImplClass(TestUtil.generateRandomStringOfLength(256));
		entity.setDescription(TestUtil.generateRandomStringOfLength(256));

		BaselineModelEAOTest modelTest = new BaselineModelEAOTest();
		BaselineModel model = modelTest.generateRandomEntity();
		try {
			model=modelEAO.create(model);
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
		}
		entity.setBaselineModel(model);

		return entity;
	}


	@Override
	protected void mutate(BaselineConfig entity) {
		entity.setName(TestUtil.generateRandomString());

	}

	@Test
	public void testGetBaselineConfigByOwnerUUID() throws DuplicateKeyException {

		BaselineConfigEAOTest test = new BaselineConfigEAOTest();
		BaselineConfig created = test.generateRandomEntity();

		configEAO.create(created);
		BaselineConfig found = configEAO.getBaselineConfigByOwnerUUID(created.getOwnerUuid());

		assertEquals(created.getName(), found.getName());
	}

	@Test
	public void testGetBaselineConfigByName() throws DuplicateKeyException {
		BaselineConfigEAOTest test = new BaselineConfigEAOTest();
		BaselineConfig created = test.generateRandomEntity();
		configEAO.create(created);

		BaselineConfig found = configEAO.getBaselineConfigByName(created.getName());

		assertEquals(created.getName(), found.getName());
	}

	@Test
	public void testGetBaselineConfigByBaselineModel() throws DuplicateKeyException {
		BaselineConfigEAOTest test = new BaselineConfigEAOTest();
		BaselineConfig created = test.generateRandomEntity();
		configEAO.create(created);

		List<BaselineConfig> configs = configEAO.getBaselineConfigByBaselineModel(created.getBaselineModel());
		for (BaselineConfig found : configs) {
			assertEquals(created.getBaselineModel(), found.getBaselineModel());
		}

	}

	private static int generateRandomNumber(int n){
		StringBuffer strBuffer = new StringBuffer();
		for(int i=0; i<n; i++){
			strBuffer.append(TestUtil.generateRandomInt(0, 1));
		}

		return Integer.parseInt(strBuffer.toString());
	}
	
	/**
	 * Deletes entity by calling deleteRelationship.
	 * @param entity
	 * @throws EntityNotFoundException
	 */
	@Override
	public void delete(BaselineConfig entity) throws EntityNotFoundException {
		BaselineModel model = modelEAO.getById(entity.getBaselineModel().getUUID());
		if (model != null) {
			model.getBaselineConfigs().remove(entity);
			configEAO.delete(entity);
			modelEAO.update(model);
		}
		//configEAO.delete(entity);
	}
	
	@Override
	public void delete(String uuid) throws EntityNotFoundException {
		BaselineConfig entity = eao.getById(uuid);
		if (entity != null)
			delete(entity);
	}
	
	/**
	 * Generically tests the create/read/update/delete methods for any CommonEAO
	 * implementation
	 * 
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	@Override
	public void testCrud() throws DuplicateKeyException,
			EntityNotFoundException {
		BaselineConfig generated = generateRandomEntity();

		String randomUUID = TestUtil.generateRandomString();
		BaselineConfig found;
		BaselineConfig afterMute;
		try {
			found = configEAO.getById(randomUUID);
			assertNull(
					"hey, why is this already found...shouldn't we throw exceptions? "
							+ randomUUID, found);
			fail("nothing should be found and the exception should have been thrown");
		} catch (EntityNotFoundException e) {
			// this is the correct behavior
		}

		// (C) test create
		BaselineConfig created = configEAO.create(generated);
		assertEntityValuesEquals(generated, created);
		// may not be considered equal because ID is different

		String uuid = created.getUUID();
		assertNotNull(uuid);

		assertBasePropertiesAfterCreation(created);

		// (R) test get
		found = configEAO.getById(uuid);
		assertEquals(created, found);

		// (U) now, let's mutate and test the set method
		mutate(found);
		afterMute = configEAO.update(found);
		// ids should still be the same
		assertEquals(created.getUUID(), afterMute.getUUID());
		assertEntityValuesNotEquals(created, afterMute);
		
		assertBasePropertiesAfterModification(found,afterMute);
		
		// (D) test removal
		BaselineModel model1 = modelEAO.getById(created.getBaselineModel().getUUID());
		
		modelEAO.delete(model1);
		
		try {
			found = configEAO.getById(uuid);
			fail("it should have been deleted");
		} catch (EntityNotFoundException e) {
			// cool..it should have been deleted
		}

	}
	
	/**
	 * Creates a couple and finds them in the list returned.
	 * 
	 * @throws DuplicateKeyException
	 * @throws EntityNotFoundException
	 * 
	 */
	@Test
	@Override
	
	public void testFindAll() throws DuplicateKeyException,
			EntityNotFoundException {
		BaselineConfig entity1 = generateRandomPersistedEntity();
		BaselineConfig entity2 = generateRandomPersistedEntity();

		List<BaselineConfig> all = configEAO.getAll();
		assertTrue(all.contains(entity1));
		assertTrue(all.contains(entity2));

		BaselineModel model1 = modelEAO.getById(entity1.getBaselineModel().getUUID());
		BaselineModel model2 = modelEAO.getById(entity2.getBaselineModel().getUUID());
		
		modelEAO.delete(model1);
		modelEAO.delete(model2);


	}
	
	/**
	 * Creates an entity and tests that it can be set, then mutates a 
	 * copy and tests that it is set as a seperate object.
	 * @throws DuplicateKeyException
	 * @throws EntityNotFoundException
	 */
	@Test
	@Override
	public void testSet() throws DuplicateKeyException, EntityNotFoundException {
		BaselineConfig generated = generateRandomEntity();
		
		BaselineConfig created = configEAO.merge(generated);
		assertEntityValuesEquals(generated, created);
		assertBasePropertiesAfterCreation(created);
		BaselineConfig mutated = configEAO.get(created);
		mutate(mutated);

		assertEntityValuesNotEquals(created, mutated);

		BaselineConfig resultOfMutatedSet = configEAO.merge(mutated);
		// ensure entity ids are the same
		assertEquals("ids are not the same", created, resultOfMutatedSet);
		assertEntityValuesNotEquals(created, resultOfMutatedSet);
		assertEntityValuesEquals(mutated, resultOfMutatedSet);
		assertBasePropertiesAfterModification(mutated, resultOfMutatedSet);

		BaselineModel model = modelEAO.getById(created.getBaselineModel().getUUID());

		modelEAO.delete(model);
		

	}
	
	/**
	 * Tests delete  method 
	 * @throws EntityNotFoundException
	 * @throws DuplicateKeyException
	 */
	@Test
	@Override
	public void testDelete() throws EntityNotFoundException,
			DuplicateKeyException {
		BaselineConfig generated = generateRandomEntityWithUUID();
		try {
			delete(generated);
			fail("can't delete what doesn't exist");
		} catch (EntityNotFoundException e) {
			generated.setUUID(null);
			BaselineConfig created = eao.create(generated);
			delete(created);
			try {
				configEAO.get(created);
				fail("what the heck, this should have been deleted " + created);
			} catch (EntityNotFoundException e1) {
				// happy exit
			}
		}

	}


}
