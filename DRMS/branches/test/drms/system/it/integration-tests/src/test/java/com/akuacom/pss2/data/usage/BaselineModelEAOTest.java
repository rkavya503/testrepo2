package com.akuacom.pss2.data.usage;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.akuacom.ejb.AbstractVersionedEAOTest;
import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.test.TestUtil;
//mvn install -Pjboss,integrationTest -Dtest=BaselineModelEAOTest  
public class BaselineModelEAOTest extends AbstractVersionedEAOTest<BaselineModelEAO, BaselineModel> {

	BaselineModelEAO modelEAO = JBossFixture.lookupSessionRemote(BaselineModelEAO.class);
	
	private List<BaselineModel> originalExisted = new ArrayList<BaselineModel>();

	public BaselineModelEAOTest() {
		super(BaselineModelEAO.class);
	}


	@Before
	public void setup() throws EntityNotFoundException {
		List<BaselineModel> existed = modelEAO.getAll();
		for (BaselineModel model : existed) {

			BaselineModel destModel = generateBaselineModelClone(model);
			
			Set<BaselineConfig> configs = new HashSet<BaselineConfig>();
			for(BaselineConfig config:model.getBaselineConfigs()){
				
				BaselineConfig destConfig = generateBaselineConfigClone(config,destModel);
				configs.add(destConfig);
			}
			destModel.setBaselineConfigs(configs);
			
			originalExisted.add(destModel);
		}
		for (BaselineModel model : modelEAO.getAll()) {
			modelEAO.delete(model);
		}
	}

	public static BaselineModel generateBaselineModelClone(BaselineModel model) {
		BaselineModel destModel = new BaselineModel();
		
		destModel.setCalcImplClass(model.getCalcImplClass());
		destModel.setDayPickerImplClass(model.getDayPickerImplClass());
		destModel.setDescription(model.getDescription());
		destModel.setImplClass(model.getImplClass());
		destModel.setName(model.getName());
		destModel.setVersion(model.getVersion());
		destModel.setUUID(null);
		
		return destModel;
	}

	public static  BaselineConfig generateBaselineConfigClone(BaselineConfig config,BaselineModel destModel) {
		
		BaselineConfig destConfig = new BaselineConfig();
		destConfig.setBaselineModel(destModel);
		destConfig.setDescription(config.getDescription());
		destConfig.setEventEndTime(config.getEventEndTime());
		destConfig.setEventStartTime(config.getEventStartTime());
		destConfig.setExcludeAbnormalDayImplClass(config.getExcludeAbnormalDayImplClass());
		destConfig.setExcludedDaysOfWeek(config.getExcludedDaysOfWeek());
		destConfig.setExcludedDaysOfWeekFromCalc(config.getExcludedDaysOfWeekFromCalc());
		destConfig.setExcludeEventDay(config.isExcludeEventDay());
		destConfig.setExcludeHoliday(config.isExcludeHoliday());
		destConfig.setExcludeHolidayFromCalc(config.isExcludeHolidayFromCalc());
		destConfig.setMaEndTime(config.getMaEndTime());
		destConfig.setMaStartTime(config.getMaStartTime());
		destConfig.setMaxMARate(config.getMaxMARate());
		destConfig.setMinMARate(config.getMinMARate());
		destConfig.setName(config.getName());
		destConfig.setOwnerUuid(config.getOwnerUuid());
		destConfig.setVersion(config.getVersion());
		destConfig.setUUID(null);
		
		return destConfig;
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
	protected void assertEntityValuesEquals(BaselineModel created,
			BaselineModel found) {

	}

	@Override
	protected void assertEntityValuesNotEquals(BaselineModel created,
			BaselineModel found) {

	}

	@Override
	protected BaselineModel generateRandomEntity() {
		final BaselineModel entity = new BaselineModel();

		entity.setUUID(TestUtil.generateRandomStringOfLength(32));
		entity.setName(TestUtil.generateRandomStringOfLength(256));
		entity.setImplClass(TestUtil.generateRandomStringOfLength(256));
		entity.setDayPickerImplClass(TestUtil.generateRandomStringOfLength(256));
		entity.setCalcImplClass(TestUtil.generateRandomStringOfLength(256));
		entity.setDescription(TestUtil.generateRandomStringOfLength(256));

		Assert.assertNotNull(entity);
		return entity;
	}

	@Override
	protected void mutate(BaselineModel entity) {
		entity.setName(TestUtil.generateRandomString());
	}

	@Test
	public void testGetBaselineModelByName() throws DuplicateKeyException {
		BaselineModelEAOTest test = new BaselineModelEAOTest();
		BaselineModel created = test.generateRandomEntity();
		modelEAO.create(created);
		BaselineModel found = modelEAO.getBaselineModelByName(created.getName());

		assertEquals(created.getName(), found.getName());
	}
}
