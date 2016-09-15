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
public class BaselineFiveTenModelBeanTest {
	protected static DataManager dataManager;
	protected static UsageDataGeneratorManager generatorManager;
	protected static BaselineFiveTenModel fiveTenModel;
	protected static BaselineModelManager baselineModelManager;
	protected static BaselineConfigManager baselineConfigManager;
	protected static BaselineModelEAO modelEAO;
	protected PDataSource source;
	Double[] result;

	@Before
	public void setup() {
		Double[] result = { 14.17852907, 12.47344843, 14.17438922, 14.52839916, 12.63151207, 13.99921108, 14.57964084, 12.87157939, 14.19075097, 14.32340457, 12.5325433, 14.32805519, 12.71689588,
				23.80448015, 26.5790821, 28.09135976, 28.82283666, 31.55511573, 31.63924459, 16.04664679, 15.52333721, 19.14324991, 19.06287464, 20.75316357, 23.05454413, 28.59363114, 28.3056245,
				39.74303562, 40.95479017, 64.54142521, 66.94964084, 69.10539375, 70.10071215, 64.19334508, 63.57016151, 54.45576578, 49.04268563, 52.64126279, 57.39032246, 57.50993371, 62.8099335,
				59.62220597, 65.6545709, 61.6044237, 60.98481694, 62.08345416, 66.77071077, 66.97491303, 70.85498175, 63.04553864, 66.75953093, 63.37942376, 63.73922897, 60.11961521, 60.20283977,
				57.54944381, 57.15449521, 55.41099357, 61.01190508, 54.20104894, 59.72166463, 53.56912144, 59.24659866, 56.48771784, 62.30126219, 63.94488518, 65.0987113, 60.93314298, 64.80364016,
				40.33928328, 36.40686024, 38.30403718, 33.06194006, 31.65405416, 31.6438249, 34.91959909, 30.25454498, 14.22733139, 14.25591658, 18.76890434, 18.72165473, 18.82030637, 14.32858257,
				15.52478969, 14.20537373, 14.37888691, 12.63544169, 15.61589549, 12.43095473, 14.09880443, 14.4572944, 31.3107711, 12.74424416, 14.34530413, 14.1971406, 14.08530875

		};
		this.result = result;

		dataManager = JBossFixture.lookupSessionRemote(DataManager.class);
		generatorManager = JBossFixture.lookupSessionRemote(UsageDataGeneratorManager.class);
		fiveTenModel = JBossFixture.lookupSessionRemote(BaselineFiveTenModel.class);
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

		List<PDataEntry> lists = fiveTenModel.calculate(datasources, date);
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
		newMb.setName("FiveTen");
		newMb.setImplClass("com.akuacom.pss2.data.usage.calcimpl.BaselineFiveTenModelBean");
		newMb.setDayPickerImplClass("com.akuacom.pss2.data.usage.calcimpl.DefaultDaysSelectorBean");
		newMb.setCalcImplClass("com.akuacom.pss2.data.usage.calcimpl.DefaultBaselineCalculatorBean");
		newMb.setDescription("5/10 baseline calculation model");

		BaselineConfig newBc = new BaselineConfig();
		newBc.setBaselineModel(newMb);
		newBc.setOwnerUuid(null);
		newBc.setName("Default 5/10 Config");
		newBc.setExcludeHolidayFromCalc(true);
		newBc.setExcludedDaysOfWeekFromCalc(1000001);
		newBc.setExcludeHoliday(false);
		newBc.setExcludedDaysOfWeek(0);
		newBc.setExcludeAbnormalDayImplClass("com.akuacom.pss2.data.usage.calcimpl.DefaultAbnormalDayHandlerBean");
		newBc.setDescription("Default 5/10 Config");

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
