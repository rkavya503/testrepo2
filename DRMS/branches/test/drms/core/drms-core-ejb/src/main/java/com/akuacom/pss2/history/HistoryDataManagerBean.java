package com.akuacom.pss2.history;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.time.DateUtils;
import org.jboss.logging.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.jdbc.SQLBuilderException;
import com.akuacom.jdbc.SQLWord;
import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.asynch.AsynchCaller;
import com.akuacom.pss2.asynch.EJBAsynHoldingRunnable;
import com.akuacom.pss2.data.BaselineAdjustInfo;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSetEAO;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.PDataSourceEAO;
import com.akuacom.pss2.data.irr.DataCalculationHandler;
import com.akuacom.pss2.data.usage.BaselineConfig;
import com.akuacom.pss2.data.usage.BaselineConfigManager;
import com.akuacom.pss2.data.usage.BaselineModel;
import com.akuacom.pss2.data.usage.BaselineModelManager;
import com.akuacom.pss2.data.usage.DataSourceUsage;
import com.akuacom.pss2.data.usage.DataSourceUsageEAO;
import com.akuacom.pss2.data.usage.DataSourceUsageVo;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.pss2.data.usage.UsageSummary;
import com.akuacom.pss2.data.usage.UsageUtil;
import com.akuacom.pss2.data.usage.calcimpl.BaseLineMA;
import com.akuacom.pss2.data.usage.calcimpl.ImplFactory;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.utils.lang.DateUtil;

@Stateless
public class HistoryDataManagerBean implements HistoryDataManager.R,
		HistoryDataManager.L {
    Logger log = Logger.getLogger(HistoryDataManagerBean.class);
	@EJB
	private PHistoryBaselineDataentryEAO.L dataEntryEAO;
	@EJB
	PDataSourceEAO.L dsEAO;
	@EJB
	PDataSetEAO.L datasetEao;
	@EJB
	SystemManager.L sysManager;
	@EJB
	BaselineModelManager.L bmManager;
	@EJB
	DataManager.L dataManager;
	@EJB
	UsageDataManager.L usageManager;
	@EJB
    BaselineConfigManager.L bcManager;
    @EJB
    DataSourceUsageEAO.L duEao;
    @EJB
    PHistoryBaselineDataentryGenEAO.L historyEntryEao;
    @EJB
    Pss2SQLExecutor.L sqlExecutor;
    @EJB
    BaseLineMA.L baseLineMA;
    @EJB
    protected AsynchCaller.L asynchCaller;
    @EJB
    HistoryEventParticipantGenEAO.L hisEventPartiEao;
    
    private static final int USAGE_TASK_HOLD = 1*1000; // 1 second
    private static final String USAGE_DATASOURCE_NAME = "meter1"; // Short-term limitation
    
    //TODO:performance review this block
	@Override
	public void createBaselineDataEntries(Set<PHistoryBaselineDataentry> dataEntryList) {
		if (dataEntryList == null || dataEntryList.size() == 0) {
			return;
		}
		try {
			for (PHistoryBaselineDataentry aDataEntryList : dataEntryList) {
				dataEntryEAO.insertOrUpdate(aDataEntryList);
			}
		} catch (EJBException e) {
			log.debug(e);
			throw e;
		} catch (Exception e) {
			log.debug(e);
			throw new EJBException();
		}
	}
	@Override
	public void clear(String pdatasources, DateRange dateRange) {
		dataEntryEAO.clear(pdatasources, dateRange);
		
	}
//	@Override
//	public void generateShedForEventParticipant(Date date) {
//		dataEntryEAO.generateShedForEventParticipant(date);
//	}
    
    /**
     * Calculates, stores, and returns a set of baseline data entries
     * for a given date.
     * 
     * for a past day, only 
     * for current day
     * 
     * @param datasourceId
     * @param baselineDay
     * @return List of PDataEntry
     */
	@Override
	public void createBaseline(String datasourceId, Date baselineDay) {
		//for past date and current day Date today = DateUtil.stripTime(new Date());,different handle
		PDataSource daSource = null;
		try {
			daSource = dsEAO.getById(datasourceId);
		} catch (EntityNotFoundException e) {
			log.error(e);
		}
		log.debug("Create or update baseline for  "+daSource.getOwnerID() +" and date ="+baselineDay +" when usage change");
		baselineDay = DateUtil.stripTime(baselineDay);
		
		DateRange dateRange = new DateRange();
		dateRange.setStartTime(baselineDay);
		dateRange.setEndTime(DateUtil.endOfDay(baselineDay));
		clear(datasourceId, dateRange);

		PDataSet dataSet = dataManager.getDataSetByName("Usage");
		List<PDataSource> dsUUIDs = new ArrayList<PDataSource>();
		try {
			dsUUIDs.add(dataManager.getPDataSource(datasourceId));
		} catch (EntityNotFoundException e) {
			log.debug(e);
		}
		// raw baseline without any adjust
		List<PDataEntry> baselineEntries = generateBaselineDataEntryList(dsUUIDs,
				baselineDay, dataSet);
		// adjust raw baseline, and store the adjusted result into db. Meanwhile it will send a message about baseline is changed, to trigger related calculation  
		forceBaselineAdjustHandler(baselineEntries, dsUUIDs.get(0).getOwnerID(), baselineDay);
				
		// store raw data for the current day, raw data only used to do adjust on the fly
		if(DateUtils.isSameDay(baselineDay, new Date())){
			Set<PHistoryBaselineDataentry> dataentrySet = new HashSet<PHistoryBaselineDataentry>();
			PDataSet baselineSet = dataManager.getDataSetByName("baseline");
			dataentrySet.clear();
			//today
			for (PDataEntry entry : baselineEntries) {
				PHistoryBaselineDataentry hisEntry = new PHistoryBaselineDataentry();
				try {
					BeanUtils.copyProperties(hisEntry, entry);
					hisEntry.setDataSet(baselineSet);
					if(hisEntry.isActual()){
						hisEntry.setActual(false);
					}
				} catch (Exception e) {
					log.debug(e);
				}
				dataentrySet.add(hisEntry);
			}
			
			createBaselineDataEntries(dataentrySet);
			
		}
	}
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void timerServiceHandler() {
		// Generate data for yesterday
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, -1);

		Date yesterday = DateUtil.stripTime(calendar);

		List<PDataSource> dataSources = dsEAO.getAll();
		for (PDataSource ds : dataSources) {
			createBaseline(ds.getUUID(), yesterday);
		}
		
		
//		List<EventParticipant> eps = findEventParticipant(yesterday);
//		
//		PDataSet usageSet = datasetEao.getDataSetByName("Usage");
//		PDataSet baselineSet = datasetEao.getDataSetByName("Baseline");
//		Date now = new Date();
//		System.out.println("###############################################################EventParticipant "+eps.size());
//		//3.for each participant, generate event shed(if both have usage and baseline)
//		for(EventParticipant ep: eps){
//			generateShed(ep, usageSet, baselineSet, yesterday, now);
//		}
		
		//Generate data for the coming day
		Date today = DateUtil.stripTime(new Date());

		for (PDataSource ds : dataSources) {
			createBaseline(ds.getUUID(), today);
		}
		
	}
	
	public void generateShed(EventParticipant ep, PDataSet usageSet, PDataSet baselineSet, Date date, Date now){
		
		Date start = ep.getStartTime();
		Date end = ep.getEndTime();
		String partname = ep.getParticipantName();
		List<PDataEntry> usage = usageManager.findRealTimeEntryListForParticipant(partname, usageSet.getUUID(), date, true, false,false);
		List<PDataEntry> baseline = usageManager.findBaselineEntryListForParticipant(partname, date, baselineSet.getUUID(),true,false);
		
    	if(usage==null||usage.isEmpty()||baseline==null||baseline.isEmpty()){
    		return;
    	}
    	
    	DateEntryEventPeriodPredicate predicate = new DateEntryEventPeriodPredicate(start.getTime(), end.getTime());
    	List<PDataEntry> usageDes = (List<PDataEntry>) CollectionUtils.select(usage, predicate);
    	if(usageDes==null||usageDes.isEmpty()) {
    		return;
    	}
    	
    	List<PDataEntry> baseDes = (List<PDataEntry>) CollectionUtils.select(baseline, predicate);
    	if(baseDes==null||baseDes.isEmpty()) {
    		return;
    	}
    	
    	double sum = 0;
    	for(PDataEntry entry : usageDes){
    		sum += entry.getValue();
    	}
    	
    	UsageSummary usageSummary = new UsageSummary();
    	usageSummary.setAverage(sum/usageDes.size());
    	usageSummary.setTotal(calculateEventDurationTotal(start, end, now, convertNumber(usageSummary.getAverage())));
    	
    	sum = 0;//reset
    	for(PDataEntry entry : baseDes){
    		sum += entry.getValue();
    	}
    	UsageSummary baseSummary = new UsageSummary();
    	baseSummary.setAverage(sum/baseDes.size());
    	baseSummary.setTotal(calculateEventDurationTotal(start, end, now, convertNumber(baseSummary.getAverage())));
    	
		double eventAvgShed = convertNumber(baseSummary.getAverage())-convertNumber(usageSummary.getAverage());
		double eventTotalShed = convertNumber(baseSummary.getTotal())-convertNumber(usageSummary.getTotal());
		
		updateEventParticipantShed(ep.getUuid(), eventAvgShed, eventTotalShed);
	}
	
	private static Double convertNumber(Double in){
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(2);
		
		return getDoubleValue(in, nf);
	}
	private static Double getDoubleValue(Double in, NumberFormat nf){
	   	
	   	if(Double.isNaN(in)) return 0.0;
	   	
	   	return Double.valueOf(nf.format(in));
	}
	private static double calculateEventDurationTotal(Date start, Date end, Date current, double avg) {
	    if(Double.isNaN(avg)){
			avg = 0;
		}
		if(!current.after(start)){
			//event have not started yet
			return 0;
		}
		//if event still active, event duration = current time - start time
		end = end.after(current)?current:end;
	    double total = 0;
	    double hours = (end.getTime()-start.getTime())/3600000.0;

	    total = avg*hours;

	    return total;
	}
	class DateEntryEventPeriodPredicate implements Predicate {
	    	
    	private long startTime;
    	private long endTime;
    	public DateEntryEventPeriodPredicate(long startTime, long endTime){
    		this.startTime = startTime;
    		this.endTime = endTime;
    	}

    	@Override
    	public boolean evaluate(Object object) {
    		PDataEntry entry = (PDataEntry) object;
    		long curTime = entry.getTime().getTime();
    		if (curTime > startTime&& curTime <= endTime) {
                return true;
            }
    		return false;
    		
    	}
    }
	
	/** move today's usage to historical tables **/
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void moveTodayUsageToHistory(){
		String insertSQL="INSERT INTO dataentry(UUID,dataset_uuid,datasource_uuid,time,value,creationTime,stringValue,valueType,actual,rawTime)"
				+" SELECT REPLACE(UUID(), '-', '') AS UUID, "
				+" dataset_uuid,dataSource_uuid,time,value, NOW(), stringValue,valueType,actual,rawTime"
				+" FROM dataentry_temp a WHERE time< ${time} on DUPLICATE KEY UPDATE value=a.value ";
		
		Map<String,Object> params = new HashMap<String,Object>(1);
		params.put("time", DateUtil.getStartOfDay(new Date()));
		
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(insertSQL, params);
			sqlExecutor.execute(sql,params);
		} catch (SQLBuilderException e) {
			log.error("Error to build SQL statement ", e);
		} catch (SQLException e) {
			log.error("Error to execute SQL statement ", e);
		}
		log.debug("Copy today's usage to dataentry table");
		
		String deleteSQL = " DELETE FROM dataentry_temp WHERE time<${time}";
		try {
			String sql = SQLBuilder.buildSQL(deleteSQL, params);
			sqlExecutor.execute(sql);
		} catch (SQLBuilderException e) {
			log.error("Error to build SQL statement ", e);
		} catch (SQLException e) {
			log.error("Error to execute SQL statement ", e);
		}
		log.debug("Delete usage data points from dataentry_temp table");
	}
	
	
			
	private void updateEventParticipantShed(String uuid,double average, double total){
		String updateSQL="UPDATE history_event_participant SET averageShed=${par_average},totalShed=${par_total} WHERE UUID=${par_uuid}";
		
		Map<String,Object> params = new HashMap<String,Object>(3);
		params.put("par_average", average);
		params.put("par_total", total);
		params.put("par_uuid", uuid);
		
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(updateSQL, params);
			sqlExecutor.execute(sql,params);
		} catch (SQLBuilderException e) {
			log.error("Error to build SQL statement ", e);
		} catch (SQLException e) {
			log.error("Error to execute SQL statement ", e);
		}	
	}
	
	/**
	 * return raw baseline without adjust
	 * @param datasourceUUID
	 * @param date
	 * @return
	 */
	private List<PDataEntry> generateBaselineDataEntryList(
			List<PDataSource> dsUUIDs, Date date, PDataSet dataSet) {
		String baselineModel = sysManager.getPss2Properties()
		.getBaselineModel();
		BaselineModel mb = bmManager.getBaselineModelByName(baselineModel);
	    Set<Date> holidays = getHolidays();
	    String[] excludedPrograms = getExcludedPrograms();
	    List<BaselineConfig> bcs = bcManager
	                .getBaselineConfigByBaselineModel(mb);
	    double missingDataThreshold = getMissingDataThreshold();
		
	
		if(mb != null) {
			List<PDataEntry> temp = ImplFactory.instance().getBaselineModel(mb.getImplClass()).
			calculate(dsUUIDs, date, holidays, excludedPrograms, mb, bcs, missingDataThreshold, dataSet);
			return temp;
		} else {
		    return Collections.<PDataEntry>emptyList();
		}
		

	}
	
 
	private Set<Date> getHolidays() {
		Set<Date> holidaydays = new HashSet<Date>();
		String holiday = null;
		try {
			holiday = sysManager.getPropertyByName(PSS2Properties.PropertyName.HOLIDAYS.toString()).getStringValue();
		} catch (EntityNotFoundException ignore) {}
		if(holiday!=null&&holiday.trim().length()>0){
			String[] holidayArray = holiday.split(",");
			
			for(String day: holidayArray){
				holidaydays.add(DateUtil.stripTime(com.akuacom.utils.DateUtil.parse(day,"MM/dd/yyyy")));
			}
		}
		
		return holidaydays;
	}   
	
	private String[] getExcludedPrograms(){
		String exludePrograms = null;
		try {
			exludePrograms = sysManager.getPropertyByName(PSS2Properties.PropertyName.EXCLUDED_PROGRAMS_FOR_BASELINE.toString()).getStringValue();
		} catch (EntityNotFoundException ignore) {}

		String[] programs = null;
		if (!(exludePrograms == null || exludePrograms.trim().equals(""))) {
			programs = exludePrograms.split(",");
		}
		
		return programs;
    }
	
    private double getMissingDataThreshold(){
    	double m = 0;
		try {
			m = sysManager.getPropertyByName(PSS2Properties.PropertyName.BASELINE_MISSINGDATA_THRESHOLD.toString()).getDoubleValue();
		} catch (EntityNotFoundException ignore) {}
    	
    	return m;
    }
    
	@Override
	public List<PDataEntry> generateBaseline(String participantName, Date date) {
		List<PDataEntry> baselineEntries = null;
		Date start = DateUtil.stripTime(date);
        PDataSet usageSet = dataManager.getDataSetByName("Usage");
        PDataSet baselineSet = dataManager.getDataSetByName("Baseline");
        List<PDataSource> dataSources =  dsEAO.getDataSourceByOwner(Arrays.asList(participantName));
        PDataSource ds = null;
        if(dataSources!=null&&dataSources.size()>0){
        	ds = dataSources.iterator().next();
        }
        if(ds!=null){
        	String partName = ds.getOwnerID();
            if (participantName.equals(partName)) {
                createBaseline(ds.getUUID(), start);
                updateInterpolationByDate(ds, participantName, start,usageSet,baselineSet, null );                
            }
        }
        
        return (baselineEntries==null?Collections.<PDataEntry>emptyList():baselineEntries);
	}
		
    /** 
     * 
     * @param pdatasource
     * @param participantName
     * @param current
     * @param usageSet
     * @param baselineSet
     * @param prefetchedBaseline  OPTIONAL, will be searched and queried at some expense if null
     */
	private void updateInterpolationByDate(PDataSource pdatasource,
			String participantName, Date current, PDataSet usageSet,
			PDataSet baselineSet, List<PDataEntry>prefetchedBaseline) {
		DataCalculationHandler usageHandler = ImplFactory.instance().getDataCalculationHandler(usageSet.getCalculationImplClass());
		DataCalculationHandler baselineHandler = ImplFactory.instance().getDataCalculationHandler(baselineSet.getCalculationImplClass());
			
		Date lastActual =  usageManager.getLastActualTimeByDate(participantName, current);//TODO:
		
		//one day's usage
		List<PDataEntry> usageEntries = usageHandler.getData(usageSet, participantName, current, true, true, false).getEntries();
        
        List<PDataEntry> baselineEntries;
        if (prefetchedBaseline == null) {
            baselineEntries = baselineHandler.getData(baselineSet, participantName, current, true, true, false).getEntries();
        } else {
            baselineEntries = prefetchedBaseline;
        }
		
        double maxGap = UsageUtil.getMaxGap(usageEntries,usageSet);
        //dsUsage.setMaxgap((float)maxGap);
		
        boolean gotBaseline = baselineEntries!=null && !baselineEntries.isEmpty();
    	//dsUsage.setBaseline_state(gotBaseline);
		
        if (maxGap > 0 && gotBaseline) {
            // For a day with missing pieces, interpolate to fill the holes with baseline
            try {
                interpolateMissingUsage(pdatasource, usageSet, usageEntries, baselineEntries);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
		}
        duEao.updateGapAndLastActual(pdatasource.getUUID(), current,lastActual, maxGap);
	}

    // Takes a list of usage data entries with gaps in it, and a complete list of baseline entries
    // and shifts baseline entries into the gaps in usage
    public void interpolateMissingUsage(PDataSource usageSource, PDataSet usageSet, 
            List<PDataEntry> usageEntries, List<PDataEntry> baselineEntries) {
        
        if (    usageSource  == null || 
                usageSet     == null || 
                usageEntries == null || 
                baselineEntries == null ||
                usageEntries.isEmpty()  ||
                baselineEntries.isEmpty()) {
            return;
    }

        Map<Date,PDataEntry> usageMap = new HashMap<Date,PDataEntry>();
        for (PDataEntry usageEntry : usageEntries) {
            if (usageEntry.isActual()) {
                usageMap.put(usageEntry.getTime(), usageEntry);
             }
        }

        Date earliestBaseline = baselineEntries.get(0).getTime();
        Date latestBaseline = baselineEntries.get(baselineEntries.size()-1).getTime();
        Set<PDataEntry> interpolatedList = new HashSet<PDataEntry>();
        boolean inGap = false;
        // will hold section of baseline to be shifted into usage
        List<PDataEntry> baselineForMissingUsage = new ArrayList<PDataEntry>();
        PDataEntry lastUsageBeforeGap = null;
        PDataEntry firstUsageAfterGap = null;
        PDataEntry lastBaselineBeforeGap = null;
        PDataEntry firstBaselineAfterGap = null;
        
        Date now = new Date();        
		//TODO: should be configurable both timeWindowSize and interpolateScope
		int windowSize = (int) sysManager.getPss2Properties().getInterpolateWindowsize();//mins        
        Date noInterpolationAfter = DateUtil.offSetBy(now, -windowSize);
        
        for (PDataEntry baselineEntry : baselineEntries) {
            Date baselineTime = baselineEntry.getTime();
            // Stop interpolating at present time minus a configurable "window"
            if (baselineTime.after(noInterpolationAfter)) { break; }
            
            boolean matchingUsage = usageMap.containsKey(baselineTime);            
            if (!matchingUsage && !inGap) {
                // Found the beginning of a gap
                inGap = true;
                baselineForMissingUsage.clear();
                baselineForMissingUsage.add(baselineEntry);
                if (lastBaselineBeforeGap == null) {
                    // special case, very beginning of day
                    lastBaselineBeforeGap = baselineEntry;
                    lastUsageBeforeGap = baselineEntry;
                }
            } else if (!matchingUsage && inGap) {
                // continuing a gap in usage
                // looking for the end of it all
                baselineForMissingUsage.add(baselineEntry);
            } else if (matchingUsage && inGap) {
                // Came to end of gap
                // Time to interpolate to backfill
                inGap = false;
                firstUsageAfterGap = usageMap.get(baselineTime);
                firstBaselineAfterGap = baselineEntry;
                double firstOffset = lastBaselineBeforeGap.getValue() - lastUsageBeforeGap.getValue();
                double lastOffset = firstBaselineAfterGap.getValue() - firstUsageAfterGap.getValue();
                int numSteps =  baselineForMissingUsage.size();
                double step = (lastOffset - firstOffset) / numSteps;
                int x = 0;
                for (PDataEntry interpBaseline: baselineForMissingUsage) {
                    ++x;
                    PDataEntry interp = new PDataEntry();
                    interp.setActual(false);
                    interp.setDataSet(usageSet);
                    interp.setDatasource(usageSource);
                    double interpShift = firstOffset + (step * x);
                    double baselineValue = interpBaseline.getValue() - interpShift;
                    interp.setValue(baselineValue);
                    interp.setValueType(interpBaseline.getValueType());  
                    interp.setTime(interpBaseline.getTime());
                    interpolatedList.add(interp);                                       
                }
                lastUsageBeforeGap = usageMap.get(baselineTime);
                lastBaselineBeforeGap = baselineEntry;
            } else if (matchingUsage && !inGap) {
                // no gap here         
                lastUsageBeforeGap = usageMap.get(baselineTime);
                lastBaselineBeforeGap = baselineEntry;
            }
    }
        // Save the interpolated data entries
		if(!interpolatedList.isEmpty()) {
            // There really should be a dataManager method that takes a list
            // and merges it in like this.  But there isn't
            String sourceUUID = usageSource.getUUID();
            String setUUID = usageSet.getUUID();
            List<PDataEntry> existings = dataManager.findByDataSourceDataSetAndDates(sourceUUID, setUUID, earliestBaseline, latestBaseline);
            for (PDataEntry interp : interpolatedList) {      
                boolean foundExisting = false;
                for (PDataEntry existing : existings) {
                    // look for an existing usage entry at this same time
                    if (existing.getTime().compareTo(interp.getTime()) == 0) {
                        foundExisting = true;
                        existing.setValue(interp.getValue());
                    }
                }
                Set<PDataEntry> dataEntryList = new HashSet<PDataEntry>();
                if (!foundExisting) {
                    // no existing usage at this time.  Make a new one
                	dataEntryList.clear();
                	dataEntryList.add(interp);
                    dataManager.createDataEntries(dataEntryList);
                }
            }
		}
    }
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void updateUsageInterpolation(PDataSource pdatasource, Date date) {
		String participantName = pdatasource.getOwnerID();
		PDataSet usageSet = dataManager.getDataSetByName("Usage");
		PDataSet baselineSet = dataManager.getDataSetByName("Baseline");
		
		updateInterpolationByDate(pdatasource, participantName, date, usageSet, baselineSet, null);
	}
	
    @Override
    public void onBaselineChange(String dataSourceId, Date date) {
    	PDataSource daSource = null;
		try {
			daSource = dsEAO.getById(dataSourceId);
		} catch (EntityNotFoundException e) {
			log.error(e);
		}
		log.debug("Process triggered when usage change - DataSource="+daSource.getOwnerID() +" &Date="+date);
		log.debug("(Re)Calculate Shed "+daSource.getOwnerID() +" and date ="+date +" when baseline change");
        refreshBaselineState(date, daSource, null, null);
        calculateShedForParticipant(daSource, date);
    }
    
	private void refreshBaselineState(Date date, PDataSource daSource, Boolean adjusted, String eventName) {
		String participantName = daSource.getOwnerID();
		PDataSet baselineSet = dataManager.getDataSetByName("Baseline");
		DataCalculationHandler baselineHandler = ImplFactory.instance().getDataCalculationHandler(baselineSet.getCalculationImplClass());
		
		List<PDataEntry> baselineEntries = baselineHandler.getData(baselineSet, participantName, date, false, true, false).getEntries();
		
		boolean gotBaseline = baselineEntries!=null && !baselineEntries.isEmpty();
		
		duEao.updateBaselineState(daSource.getUUID(), DateUtil.stripTime(date), gotBaseline, adjusted, eventName);
	}
   
   /**
    * Re-calculate event shed, when baseline is refreshed or a new usage data is received
    */
    @Override
	public void calculateShedForParticipant(PDataSource daSource, Date date) {
		PDataSet usageSet = dataManager.getDataSetByName("Usage");
		PDataSet baselineSet = dataManager.getDataSetByName("Baseline");
		List<HistoryEventParticipant> eventParts = hisEventPartiEao.findByParticipantDate(daSource.getOwnerID(), DateUtil.getStartOfDay(date), DateUtil.getEndOfDay(date));
		for(HistoryEventParticipant eventPart : eventParts){
		    List<PDataEntry> usageList = usageManager.findRealTimeEntryListForParticipant(daSource.getOwnerID(), usageSet.getUUID(), date, true, false,false);
		    List<PDataEntry> baseList = usageManager.findBaselineEntryListForParticipant(daSource.getOwnerID(), date, baselineSet.getUUID(), true, false);
		    
		    UsageSummary summary = usageManager.calculateShed(baseList, usageList, new Date(), eventPart.getStartTime(), eventPart.getEndTime());
		    eventPart.setAverageShed(summary.getAverage());
		    eventPart.setTotalShed(summary.getTotal());
		    
		    hisEventPartiEao.merge(eventPart);
		}
	}
    
	@Override
	public void onUsageChange(String dataSourceId, Date date,
			Boolean refreshBaseline) {
		PDataSource daSource = null;
		try {
			daSource = dsEAO.getById(dataSourceId);
		} catch (EntityNotFoundException e) {
			return;
		}
		if(refreshBaseline){
			createBaseline(dataSourceId, date);
		}else{
			log.debug("(Re)Calculate Shed "+daSource.getOwnerID() +" and date ="+date +" when usage change");
			calculateShedForParticipant(daSource, date);
		}
	}
	@Override
	public void baselineTimerServiceHandler() {
		
		Date baselineDay = new Date();
		
		String baselineModel = sysManager.getPss2Properties().getBaselineModel();
		BaselineModel mb = bmManager.getBaselineModelByName(baselineModel);
		List<BaselineConfig> bcs = bcManager.getBaselineConfigByBaselineModel(mb);
		BaselineConfig bc = (BaselineConfig) bcs.toArray()[0];// when
			
		BaselineAdjustInfo info = new BaselineAdjustInfo();
		info.setMinMARate(bc.getMinMARate());
		info.setMaxMARate(bc.getMaxMARate());
		float maxRate = bc.getMaxMARate();
		float minRate = bc.getMinMARate();
		if(maxRate==0 && minRate==0){
			info.setMaEnabled(false);
			log.debug("MA is disabled : (bc.getMaxMARate()==0)&&(bc.getMinMARate()==0)");
			return; // Adjust is disabled, return.
		}
		info.setMaEnabled(true);
		boolean maByevent = bc.isMaByEvent();
		if(!maByevent){
			info.setEventName(null);
			info.setStartTime(bc.getMaStartTime());
			info.setEndTime(bc.getMaEndTime());
			// Adjust by event is disabled, run default adjust(ma)
			long currentTime = UsageUtil.getCurrentTime(new Date());
			if(currentTime<info.getEndTime()) {
				log.debug("MA is disabled : before end time");
				return;
			}
			//get all by date
			List<DataSourceUsageVo> allDataSource =  duEao.findAllDatasourceByDate(baselineDay);
			log.debug("MA is running with "+allDataSource.size()+" datasources");
			// Traversing every datasource
			for(DataSourceUsageVo vo : allDataSource){
				if(vo.getAdjusted()!=null&&vo.getAdjusted()){//need to change to adjusted
					log.debug("MA already excetued :"+vo.getOwnerID()+" event name: "+vo.getEventName());
				continue;
				}
				String id = "baselineadjust_"+vo.getOwnerID()+"_"+DateUtil.formatDate(baselineDay);
				asynchCaller.call(new EJBAsynHoldingRunnable(id,USAGE_TASK_HOLD,5*60*1000,HistoryDataManager.class,
						"onBaselineAdjust", new Class[] {String.class,Date.class,Integer.class,Integer.class,String.class,List.class},
						 new Object[] {vo.getOwnerID(), baselineDay, info.getStartTime(), info.getEndTime(), info.getEventName(),null}));
			
			}
			return;
		}

		List<DataSourceUsageVo> allDataSource =  duEao.findAllDatasourceByDate(baselineDay);
		log.debug("MA is running** with "+allDataSource.size()+" datasources");
		// Traversing every datasource
		for(DataSourceUsageVo vo : allDataSource){
			com.akuacom.pss2.event.Event event  = dataManager.findFirstEventOfDay(vo.getOwnerID(), baselineDay);
			if(event==null){
				if(vo.getAdjusted()!=null&&vo.getAdjusted()&&vo.getEventName()==null){//MA already done.
					log.debug("MA already executed :"+vo.getOwnerID()+" event name: "+vo.getEventName());
					continue;
				}
				// no event, run the default MA.
				info.setEventName(null);
				info.setStartTime(bc.getMaStartTime());
				info.setEndTime(bc.getMaEndTime());
				
			}else{
				if(vo.getAdjusted()!=null&&vo.getAdjusted()&&event.getEventName().equalsIgnoreCase(vo.getEventName())){//Adjust already done.
					log.debug("MA already executed :"+vo.getOwnerID()+" event name: "+vo.getEventName());
					continue;
				}
				// Adjust baseline based on the first event
				Date midnight =  DateUtil.stripTime(event.getStartTime());
				long offset = event.getStartTime().getTime() - midnight.getTime();
				info.setStartTime((int) (offset-bc.getMaBackOffset()));
				info.setEndTime((int) (offset-bc.getMaBackOffset()+bc.getMaDuration()));
				info.setEventName(event.getEventName());
			}
			long currentTime = UsageUtil.getCurrentTime(new Date());
			if(currentTime<info.getEndTime()) {
				log.debug("MA is disabled : before end time:"+vo.getOwnerID());
				continue;
			}
			String id = "baselineadjust_"+vo.getOwnerID()+"_"+DateUtil.formatDate(baselineDay);
			asynchCaller.call(new EJBAsynHoldingRunnable(id,USAGE_TASK_HOLD,5*60*1000,HistoryDataManager.class,
					"onBaselineAdjust", new Class[] {String.class,Date.class,Integer.class,Integer.class,String.class,List.class},
					 new Object[] {vo.getOwnerID(), baselineDay, info.getStartTime(), info.getEndTime(), info.getEventName(),null}));
		
		}
		
	}
	
	/**
	 * midnight re-calculate baseline
	 * @param baselineEntries
	 * @param par
	 * @param baselineDay
	 */
	private void forceBaselineAdjustHandler(List<PDataEntry> baselineEntries, String participantName, Date baselineDay) {
		String baselineModel = sysManager.getPss2Properties().getBaselineModel();
		BaselineModel mb = bmManager.getBaselineModelByName(baselineModel);
		List<BaselineConfig> bcs = bcManager.getBaselineConfigByBaselineModel(mb);
		BaselineConfig bc = (BaselineConfig) bcs.toArray()[0];// when
			
		BaselineAdjustInfo info = new BaselineAdjustInfo();
		info.setMinMARate(bc.getMinMARate());
		info.setMaxMARate(bc.getMaxMARate());
		float maxRate = bc.getMaxMARate();
		float minRate = bc.getMinMARate();
		if(maxRate==0 && minRate==0){
			info.setMaEnabled(false);
			log.debug("MA disabled : (bc.getMaxMARate()==0)&&(bc.getMinMARate()==0)");
			String id = "baselineadjust_"+participantName+"_"+DateUtil.formatDate(baselineDay);
			asynchCaller.call(new EJBAsynHoldingRunnable(id,USAGE_TASK_HOLD,5*60*1000,HistoryDataManager.class,
					"onBaselineAdjust", new Class[] {String.class,Date.class,Integer.class,Integer.class,String.class,List.class},
					 new Object[] {participantName, baselineDay, 0, 0, null, baselineEntries})); // start and end set as 0, just store given baseline with out any adjusting
			return;
		}
		info.setMaEnabled(true);
		boolean maByevent = bc.isMaByEvent();
		if(!maByevent){
			info.setEventName(null);
			info.setStartTime(bc.getMaStartTime());
			info.setEndTime(bc.getMaEndTime());
			// Adjust by event is disabled, run default adjust(ma)
			long currentTime = UsageUtil.getCurrentTime(new Date());
			if(DateUtils.isSameDay(baselineDay, new Date())&&currentTime<info.getEndTime()) {
				log.debug("MA is disabled : before end time: ");
				info.setEndTime(info.getStartTime()); 
			}
			String id = "baselineadjust_"+participantName+"_"+DateUtil.formatDate(baselineDay);
			asynchCaller.call(new EJBAsynHoldingRunnable(id,USAGE_TASK_HOLD,5*60*1000,HistoryDataManager.class,
					"onBaselineAdjust", new Class[] {String.class,Date.class,Integer.class,Integer.class,String.class,List.class},
					 new Object[] {participantName, baselineDay, info.getStartTime(), info.getEndTime(), info.getEventName(), baselineEntries}));
			
			return;
			
		}
		
		com.akuacom.pss2.event.Event event  = dataManager.findFirstEventOfDay(participantName, baselineDay);
		if(event==null){
			info.setEventName(null);
			info.setStartTime(bc.getMaStartTime());
			info.setEndTime(bc.getMaEndTime());
			// no event, run the default MA.
		}else{
			Date midnight =  DateUtil.stripTime(event.getStartTime());
			long offset = event.getStartTime().getTime() - midnight.getTime();
			info.setStartTime((int) (offset-bc.getMaBackOffset()));
			info.setEndTime((int) (offset-bc.getMaBackOffset()+bc.getMaDuration()));
			info.setEventName(event.getEventName());
			// Adjust baseline based on the first event
		}
		long currentTime = UsageUtil.getCurrentTime(new Date());
		if(DateUtils.isSameDay(baselineDay, new Date())&&currentTime<info.getEndTime()) {
			log.debug("*MA is disabled : before end time:"+participantName);
			info.setEndTime(info.getStartTime()); 
		}
		String id = "baselineadjust_"+participantName+"_"+DateUtil.formatDate(baselineDay);
		log.debug("BaselLime Test *********forceBaselineAdjustHandler()333333333333");
		asynchCaller.call(new EJBAsynHoldingRunnable(id,USAGE_TASK_HOLD,5*60*1000,HistoryDataManager.class,
				 "onBaselineAdjust", new Class[] {String.class,Date.class,Integer.class,Integer.class,String.class,List.class},
				 new Object[] {participantName, baselineDay, info.getStartTime(), info.getEndTime(), info.getEventName(), baselineEntries}));
	
		
	}
	
    @Override
  /*
   * (non-Javadoc)
   * @see com.akuacom.pss2.history.HistoryDataManager#onBaselineAdjust(java.lang.String, java.util.Date, java.lang.Integer, java.lang.Integer, java.lang.String, java.util.List)
   */
    public void onBaselineAdjust(String participantName, Date date, Integer start, Integer end, String eventName, List<PDataEntry> baselineEntries) {
    	
    	log.debug("MA is running for "+participantName); 
    	log.debug("BaselLime Test *********onBaselineAdjust()**1111111111111");
    	//1. retrieve raw baseline
    	PDataSet baselineSet = dataManager.getDataSetByName("Baseline");
    	PDataSource daSource = null;
    	
		try {
			List<PDataSource> daSourceList = dsEAO.getDataSourceByOwner(Arrays.asList(participantName));
			for (PDataSource pDataSource : daSourceList) {
				log.debug("BaselLime Test *********onBaselineAdjust()** datasource name"+pDataSource.getName());
				 if(pDataSource.getName().equals("meter1")){
						log.debug("BaselLime Test *********onBaselineAdjust()**datasource set");
					 daSource =  pDataSource;
				 }
			}
		} catch (Exception e) {
			log.error(e);
		}
		if(baselineEntries==null){
			log.debug("BaselLime Test *********onBaselineAdjust()**2222222222222222");
			baselineEntries = usageManager.findBaselineEntryListForParticipant(participantName, date, baselineSet.getUUID(), true, true);//raw baseline without adjust
		}
    	
    	//2. adjust baseline
    	List<PDataEntry> adjusted = maAdjust(baselineEntries, participantName, date,
    			baselineSet.getUUID(), true, start, end);
    	Set<PHistoryBaselineDataentry> dataentrySet = new HashSet<PHistoryBaselineDataentry>();
    	boolean isDdjusted = true;
    	if(start == end){
    		isDdjusted = false;
    	}
    	for (PDataEntry entry : adjusted) {
    		log.debug("BaselLime Test *********onBaselineAdjust()**ownerId"+daSource.getOwnerID());
    		log.debug("BaselLime Test *********onBaselineAdjust()**DataSourceName"+daSource.getName());
    		log.debug("BaselLime Test *********onBaselineAdjust()**DataSetName"+baselineSet.getName());
			PHistoryBaselineDataentry hisEntry = new PHistoryBaselineDataentry();
			try {
				BeanUtils.copyProperties(hisEntry, entry);
				hisEntry.setDataSet(baselineSet);
				hisEntry.setDatasource(daSource);
				if(!hisEntry.isActual()){
					isDdjusted = false;
					hisEntry.setActual(true);
				}
			} catch (Exception e) {
				log.debug(e);
			}
			dataentrySet.add(hisEntry);
		}

		//3. store adjusted baseline
		createBaselineDataEntries(dataentrySet);
		log.debug("BaselLime Test *********onBaselineAdjust()**dataentrySet "+dataentrySet.size());
		//4. refresh datasource_usage
		if(!dataentrySet.isEmpty()){
			log.debug("BaselLime Test *********onBaselineAdjust()** true data Source uuid "+daSource.getUUID());
			duEao.updateBaselineState(daSource.getUUID(), date, true, isDdjusted, eventName);
		}else{
			log.debug("BaselLime Test *********onBaselineAdjust()** false data Source uuid "+daSource.getUUID());
			duEao.updateBaselineState(daSource.getUUID(), date, false, false, eventName);
		}
		
		String id = "baseline_"+daSource.getUUID()+"_"+DateUtil.formatDate(date);
		System.out.println("onBaselineAdjust () ");
		asynchCaller.call(new EJBAsynHoldingRunnable(id,USAGE_TASK_HOLD,5*60*1000,HistoryDataManager.class,
				 "onBaselineChange", new Class[] {String.class,Date.class},
				 new Object[] {daSource.getUUID(),date}));
    }
	   
	public List<PDataEntry> maAdjust(List<PDataEntry> temp, String participantName, Date date,
            String dataSetuuid, Boolean isIndividual, int start, int end) {
		log.debug("BaselLime Test *********maAdjust()**"+ dataSetuuid);
		if(start == end) return temp;
	    String baselineModel = sysManager.getPss2Properties()
	                    .getBaselineModel();
	    BaselineModel mb = bmManager.getBaselineModelByName(baselineModel);
	    List<BaselineConfig> bcs = bcManager.getBaselineConfigByBaselineModel(mb);
	    BaselineConfig bc = (BaselineConfig) bcs.toArray()[0];// when
	    
	    if((bc.getMaxMARate()==0)&&(bc.getMinMARate()==0)) return temp;//Disable ma feature when min and max rate's value is 0
	    
	    Date currentDate = new Date();
	    if(DateUtils.isSameDay(currentDate, date)){
	    	log.debug("BaselLime Test *********maAdjust()**0000000000011111111 Same day");
	            //calculate ma for the current day
	            long currentTime = UsageUtil.getCurrentTime(currentDate);
	            if(currentTime<bc.getMaStartTime()){
	                    // not in MA period yet.
	                    return temp;
	            }
	    }
	    // Morning adjustment                           
	    List<PDataEntry> usageList = null;
	    if(isIndividual){
	    	log.debug("BaselLime Test *********maAdjust()**1111111111111111"+ dataSetuuid);
	            usageList = usageManager.findRealTimeEntryListForParticipant
	            (participantName, dataSetuuid, date, true, true, false);// only return the actual point
	            log.debug("BaselLime Test *********maAdjust()**size"+ usageList.size());
	    }else{
	    	log.debug("BaselLime Test *********maAdjust()**2222222222222"+dataSetuuid);
	            usageList = usageManager.findRealTimeEntryListForParticipant
	                                (participantName, dataSetuuid, date, false, false, false);
	            log.debug("BaselLime Test *********maAdjust()**size"+ usageList.size());
	    }
	        
	    temp = baseLineMA.adjust(bc, temp, usageList, date, start, end);
	        
	    return temp;
	}
	private List<EventParticipant> findEventParticipant(Date date) {
		StringBuilder sqltemplate = new StringBuilder();
		sqltemplate.append(" SELECT ep.uuid uuid,e.eventName eventName,e.startTime startTime,e.endTime endTime,   ");
		sqltemplate.append("        ep.participantName participantName,ep.participant_uuid participant_uuid    ");
		sqltemplate.append(" FROM history_event e INNER JOIN history_event_participant ep ON ep.history_event_uuid=e.uuid   ");
		sqltemplate.append(" WHERE DATE(e.startTime)=${param_date}   ");
		sqltemplate.append(" AND ep.client=0 and ep.participation<>'40'  ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("param_date", DateUtils.truncate(date, Calendar.DATE));
		
		String sql = null;
		List<EventParticipant> results = null;
		try {
			sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
			results = sqlExecutor.doNativeQuery(sql,params,
					new ListConverter<EventParticipant>(new ColumnAsFeatureFactory<EventParticipant>(EventParticipant.class)));
		} catch (SQLBuilderException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	
}
