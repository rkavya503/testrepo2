package com.akuacom.pss2.data.usage.calcimpl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.usage.BaselineConfig;
import com.akuacom.pss2.data.usage.BaselineConfigManager;
import com.akuacom.pss2.data.usage.BaselineModel;
import com.akuacom.pss2.data.usage.BaselineModelEAO;
import com.akuacom.pss2.data.usage.BaselineModelManager;
import com.akuacom.pss2.data.usage.EntryDataUtil;
import com.akuacom.pss2.data.usage.UsageDataGeneratorManager;

@Ignore
public class BaselineThreeTenModelBeanTest {
	protected static DataManager dataManager;
	protected static UsageDataGeneratorManager generatorManager;
	protected static BaselineThreeTenModel threeTenModel;
	protected static BaselineModelManager baselineModelManager;
	protected static BaselineConfigManager baselineConfigManager;
	protected static BaselineModelEAO modelEAO;

	protected PDataSource source;
	Double[] result;

	@Before
	public void setup() {
		Double[] result = { 14.2421631304, 12.34821412, 14.15834616, 14.5510873, 12.44141643, 14.20374492, 14.56710875, 12.71529886, 14.4999276, 14.11708133, 12.47667508, 14.56386855, 12.61624047,
				23.89745551, 27.02913147, 27.91031949, 28.53784809, 31.71836208, 31.77556879, 16.10756683, 15.50688902, 19.15518714, 18.91334534, 20.68734205, 23.00468913, 28.55864112, 28.73388405,
				39.93103123, 40.27813954, 64.59740668, 66.36410219, 68.64284495, 71.10284309, 63.83097386, 64.81201683, 54.61979658, 49.6368573, 52.14566384, 57.55022763, 58.40877827, 62.89056588,
				59.79459229, 65.00748757, 61.69237477, 60.12330099, 62.63003993, 67.25567217, 65.83378687, 70.75675003, 63.27150408, 66.53499083, 63.4193824, 63.69664433, 60.24020051, 60.36750874,
				57.23623134, 56.72878562, 55.02221828, 62.10970229, 54.46403908, 60.40976299, 53.99118088, 59.19777336, 55.91330164, 62.09472092, 64.46522152, 65.72347312, 60.8548773, 65.49975972,
				40.32875488, 35.7988307, 38.29210902, 32.56803187, 31.91309595, 31.70505303, 35.05299147, 30.60454769, 14.16140687, 14.31063216, 18.82809575, 18.37961725, 18.90410031, 14.22763734,
				15.57163321, 14.01134093, 14.577006, 12.46060976, 15.7324965, 12.54561853, 14.08255319, 14.58853335, 31.35300292, 12.94184715, 14.31907723, 14.38069286, 13.87560304 };
		this.result = result;

		dataManager = JBossFixture.lookupSessionRemote(DataManager.class);
		generatorManager = JBossFixture.lookupSessionRemote(UsageDataGeneratorManager.class);
		threeTenModel = JBossFixture.lookupSessionRemote(BaselineThreeTenModel.class);
		baselineModelManager = JBossFixture.lookupSessionRemote(BaselineModelManager.class);
		baselineConfigManager = JBossFixture.lookupSessionRemote(BaselineConfigManager.class);
		modelEAO = JBossFixture.lookupSessionRemote(BaselineModelEAO.class);

		source = dumpTestingDataEntrys();

		dumpTestingBaselineModelAndBaselineConfig();

	}

	@Test
	public void testCalculate() {

		List<String> datasources = new ArrayList<String>();
		datasources.add(source.getUUID());
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, 2010);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 22);
		Date date = cal.getTime();

		List<PDataEntry> lists = threeTenModel.calculate(datasources, date);
		assertEquals(lists.size(), 96);

		for (int i = 0; i < lists.size(); i++) {
			assertEquals(result[i], lists.get(i).getValue(), 0.00001);
		}

	}

//	@After
//	public void tearDown() {
//		for (BaselineModel model : modelEAO.getAll()) {
//			try {
//				modelEAO.delete(model);
//			} catch (EntityNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	private PDataSource dumpTestingDataEntrys() {

		final PDataSet dataSet = dataManager.getDataSetByName("Usage");
		PDataSource source = dataManager.getDataSourceByNameAndOwner("meter1", "p1");

		if (source == null) {
			PDataSource ds = new PDataSource();
			ds.setName("meter1");
			ds.setOwnerID("p1");
			source = dataManager.createPDataSource(ds);
		}

		DateRange range = new DateRange();
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, 2010);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 22);
		Date endDate = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, -33);

		range.setEndTime(endDate);
		range.setStartTime(cal.getTime());
		List<PDataEntry> dataEntrys = null;
		try {
			dataEntrys = dataManager.getDataEntryList(dataSet.getUUID(), source.getUUID(), range, false);
		} catch (EntityNotFoundException e1) {
			e1.printStackTrace();
		}
		if (dataEntrys != null && dataEntrys.size() > 0) {
			dataManager.deleteDataEntryByDatasource(source.getUUID());
		}

		List<PDataEntry> dataEntryList = null;
		try {
			dataEntryList = generatorManager.generateDataForParticpant(EntryDataUtil.generateModel("dataentry_testData.sql", false, "\\d{4}\\-\\d{2}\\-\\d{2}\\s\\d{2}\\:\\d{2}\\:\\d{2},\\d+\\.?\\d*\\,"), source, dataSet);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final Set<PDataEntry> set = new HashSet<PDataEntry>(dataEntryList);

		dataManager.createDataEntries(set);

		return source;
	}

	private void dumpTestingBaselineModelAndBaselineConfig() {
		for (BaselineModel model : modelEAO.getAll()) {
			try {
				modelEAO.delete(model);
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
		}

		BaselineModel newMb = new BaselineModel();
		newMb.setName("ThreeTen");
		newMb.setImplClass("com.akuacom.pss2.data.usage.calcimpl.BaselineThreeTenModelBean");
		newMb.setDayPickerImplClass("com.akuacom.pss2.data.usage.calcimpl.DefaultDaysSelectorBean");
		newMb.setCalcImplClass("com.akuacom.pss2.data.usage.calcimpl.DefaultBaselineCalculatorBean");
		newMb.setDescription("3/10 baseline calculation model");

		BaselineConfig newBc = new BaselineConfig();
		newBc.setBaselineModel(newMb);
		newBc.setOwnerUuid(null);
		newBc.setName("Default 3/10 Config");
		newBc.setExcludeHolidayFromCalc(true);
		newBc.setExcludedDaysOfWeekFromCalc(1000001);
		newBc.setExcludeHoliday(false);
		newBc.setExcludedDaysOfWeek(0);
		newBc.setExcludeAbnormalDayImplClass("com.akuacom.pss2.data.usage.calcimpl.DefaultAbnormalDayHandlerBean");
		newBc.setDescription("Default 3/10 Config");

		Set<BaselineConfig> baselineConfigs = new HashSet<BaselineConfig>();
		baselineConfigs.add(newBc);
		newMb.setBaselineConfigs(baselineConfigs);

		baselineModelManager.createBaselineModel(newMb);
	}

}
