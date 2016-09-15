package com.akuacom.pss2.data.usage.calcimpl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
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
public class BaselineTenTenModelBeanTest {
	protected static DataManager dataManager;
	protected static UsageDataGeneratorManager generatorManager;
	protected static BaselineTenTenModel tebTenModel;
	protected static BaselineModelManager baselineModelManager;
	protected static BaselineConfigManager baselineConfigManager;
	protected static BaselineModelEAO modelEAO;
	protected PDataSource source;
	Double[] result;

	@Before
	public void setup() {
		Double[] result = { 14.17249246, 13.06434988, 14.0960962, 13.74911459, 13.96514489, 13.47788763, 14.54244582, 12.68639789, 14.9395923, 13.63041722, 13.69824852, 14.86646535, 14.53465453,
				26.66414367, 29.48956125, 31.59239001, 27.67724754, 30.02807675, 32.84249078, 24.72521998, 23.09724706, 25.67661774, 25.77878804, 26.38429493, 30.30965056, 31.56526713, 32.99682482,
				38.73331192, 40.21109271, 54.52883071, 54.53461519, 53.54656109, 51.61080902, 48.05850718, 47.81064586, 41.6513363, 38.72642042, 41.03010318, 44.02109561, 43.15148669, 45.69449627,
				43.73141981, 48.67821038, 46.00125299, 45.7457538, 45.29600952, 48.76527277, 48.65963759, 49.93982252, 45.49392663, 46.71813452, 44.99791213, 44.62537324, 42.88327148, 43.54663875,
				40.89247563, 42.33254438, 41.3090784, 44.30510321, 40.88754882, 43.13760403, 39.89866576, 43.63101399, 42.39655603, 45.27162193, 46.70089522, 46.30372979, 43.43550736, 46.83988988,
				30.76613678, 29.16956969, 29.66734335, 27.73762527, 25.25041158, 25.84624365, 27.64471531, 25.28691802, 14.79691561, 14.92095967, 17.45634882, 18.2033607, 17.43875279, 14.85822328,
				14.93768389, 14.78399847, 15.00108337, 13.81973176, 15.59767923, 13.83988972, 14.20452443, 15.02118618, 24.55467854, 13.93124192, 14.15465866, 15.35895583, 13.4608393

		};
		this.result = result;

		dataManager = JBossFixture.lookupSessionRemote(DataManager.class);
		generatorManager = JBossFixture.lookupSessionRemote(UsageDataGeneratorManager.class);
		tebTenModel = JBossFixture.lookupSessionRemote(BaselineTenTenModel.class);
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

		List<PDataEntry> lists = tebTenModel.calculate(datasources, date);
		assertEquals(lists.size(), 96);

		for (int i = 0; i < lists.size(); i++) {
			assertEquals(result[i], lists.get(i).getValue(), 0.00001);
		}

	}

	@After
	public void tearDown() {
		for (BaselineModel model : modelEAO.getAll()) {
			try {
				modelEAO.delete(model);
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
		}
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
		newMb.setName("TenTen");
		newMb.setImplClass("com.akuacom.pss2.data.usage.calcimpl.BaselineTenTenModelBean");
		newMb.setDayPickerImplClass("com.akuacom.pss2.data.usage.calcimpl.DefaultDaysSelectorBean");
		newMb.setCalcImplClass("com.akuacom.pss2.data.usage.calcimpl.DefaultBaselineCalculatorBean");
		newMb.setDescription("10/10 baseline calculation model");

		BaselineConfig newBc = new BaselineConfig();
		newBc.setBaselineModel(newMb);
		newBc.setOwnerUuid(null);
		newBc.setName("Default 10/10 Config");
		newBc.setExcludeHolidayFromCalc(true);
		newBc.setExcludedDaysOfWeekFromCalc(1000001);
		newBc.setExcludeHoliday(false);
		newBc.setExcludedDaysOfWeek(0);
		newBc.setExcludeAbnormalDayImplClass("com.akuacom.pss2.data.usage.calcimpl.DefaultAbnormalDayHandlerBean");
		newBc.setDescription("Default 10/10 Config");

		Set<BaselineConfig> baselineConfigs = new HashSet<BaselineConfig>();
		baselineConfigs.add(newBc);
		newMb.setBaselineConfigs(baselineConfigs);

		baselineModelManager.createBaselineModel(newMb);
	}

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

}
