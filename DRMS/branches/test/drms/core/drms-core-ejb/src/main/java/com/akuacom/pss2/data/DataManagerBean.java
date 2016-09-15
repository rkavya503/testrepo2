package com.akuacom.pss2.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.persistence.EntityExistsException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import scala.Tuple2;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.jdbc.BeanFactory;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.HierarchyFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.jdbc.SQLBuilderException;
import com.akuacom.jdbc.SQLWord;
import com.akuacom.jdbc.SimpleListConverter;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.asynch.AsynchCaller;
import com.akuacom.pss2.client.ClientEAO;
import com.akuacom.pss2.client.ClientManager;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.irr.TreeDataEntry;
import com.akuacom.pss2.data.irr.TreeDataSet;
import com.akuacom.pss2.data.irr.TreeDataSource;
import com.akuacom.pss2.data.usage.BaselineConfig;
import com.akuacom.pss2.data.usage.BaselineConfigManager;
import com.akuacom.pss2.data.usage.BaselineModel;
import com.akuacom.pss2.data.usage.BaselineModelManager;
import com.akuacom.pss2.data.usage.CurrentUsageDataEntryEAO;
import com.akuacom.pss2.data.usage.CurrentUsageDataEntryGenEAO;
import com.akuacom.pss2.data.usage.DataSourceUsage;
import com.akuacom.pss2.data.usage.DataSourceUsageGenEAO;
import com.akuacom.pss2.data.usage.UsageUtil;
import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.history.HistoryDataManager;
import com.akuacom.pss2.history.HistoryEventParticipantGenEAO;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.dl.DemandLimitingProgram;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantAggregationManager;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.sql.SQLLoader;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.PSS2Features.FeatureName;
import com.akuacom.pss2.system.property.PSS2Properties.PropertyName;
import com.akuacom.utils.lang.DateUtil;
import com.akuacom.utils.lang.ExpiringHashMap;

@Stateless
public class DataManagerBean implements DataManager.R, DataManager.L {

	@EJB
	protected PDataEntryEAO.L dataEntryEAO;
	@EJB
	protected PDataSourceEAO.L dataSourceEAO;
	@EJB
	protected PDataSetEAO.L dataSetEAO;

	@Resource
	protected SessionContext context;

	@EJB
	protected ClientManager.L clientManager;
	@EJB
	protected ParticipantEAO.L partEAO;
	@EJB
	protected ClientEAO.L clientEAO;
	@EJB
	protected EventManager.L eventManager;
	@EJB
	protected ReportManager.L reportManager;
	@EJB
	protected CurrentUsageDataEntryEAO.L currentUsageEAO;
	@EJB
	protected ProgramParticipantAggregationManager.L aggManager;
	@EJB
	protected ProgramParticipantManager.L programParticipantManager;
	@EJB
	protected ParticipantManager.L participantManager;
	@EJB
	protected SystemManager.L systemManager;
	@EJB
	protected HistoryEventParticipantGenEAO.L heParticipantEAO;
	@EJB
	protected PDataEntryGenEAO.L dataEntryGenEAO;
	@EJB
	protected Pss2SQLExecutor.L sqlExecutor;
	@EJB
	protected DataSourceUsageGenEAO.L duEao;
	@EJB
	protected CurrentUsageDataEntryGenEAO.L currentUsageEao;

	@EJB
	protected BaselineModelManager.L bmManager;
	
	@EJB
	protected BaselineConfigManager.L bcManager;
	
	@EJB
	protected AsynchCaller.L asynchCaller;

	@Resource(mappedName = "queue/participantIntervalMeterDataUpdate")
	private Queue participantIntervalMeterDataUpdate;
	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory participantIntervalMeterDataUpdateFactory;

	private static final int USAGE_TASK_HOLD = 1*1000; // 1 second
	private static final String USAGE_TASK="usage";
	private static final int PERIOD_MIN_SECONDS = 30;
	private static final int LARGEST_UNFILTERED_DATAENTRY_LIST = 500;
	private static final Logger log = Logger.getLogger(DataManagerBean.class);
	private final static long PERIOD = TimeUnit.MILLISECONDS.convert(12l,
			TimeUnit.HOURS);
	final static protected Map<String, Map<String, List<Long>>> sidOidTimes = new ExpiringHashMap<String, Map<String, List<Long>>>(
			PERIOD);

	boolean useInterpolation;
	boolean useTempDataEntryTable;
	boolean demandLimitingEnabled = false;
	
	@PostConstruct
	private void init() {
		try {
			useInterpolation = !systemManager.getProperty(
					PropertyName.DISABLE_USAGE_DATA_INTERPOLATION)
					.isBooleanValue();
			demandLimitingEnabled = systemManager.getPss2Features()
					.isDemandLimitingEnabled();
		} catch (EntityNotFoundException nfe) {
			CoreProperty cp = new CoreProperty(
					PropertyName.DISABLE_USAGE_DATA_INTERPOLATION
							.getPropertyName(),
					true);
			systemManager.setProperty(cp);
			useInterpolation = false;
		}
		try {
			CoreProperty useTempDataEntryEnabled = systemManager
					.getPropertyByName(FeatureName.USETEMPDATAENTRYTBL
							.toString());
			useTempDataEntryTable = useTempDataEntryEnabled.isBooleanValue();
		} catch (EntityNotFoundException eaten) {
		}
		
		String baselineModel = systemManager.getPss2Properties().getBaselineModel();
		BaselineModel mb = bmManager.getBaselineModelByName(baselineModel);
		List<BaselineConfig> bcs = bcManager.getBaselineConfigByBaselineModel(mb);
		BaselineConfig bc = (BaselineConfig) bcs.toArray()[0];// when
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void createDataEntries(Set<PDataEntry> dataEntryList) {
		Set<PDataEntry> pastDataEnties = new HashSet<PDataEntry>();
		Set<PDataEntry> todayDataEntries = new HashSet<PDataEntry>();
		//NOT make sure all data entries are belonging to same data source
		//since data point interpolation will be triggered 
		//after insertion of this batch of data entries for that data source 
		//We don't want to trigger interpolation on insertion of every point
		
		
		String dataSourceId = null;
		String partName=null;
		if (dataEntryList != null) {
			for (PDataEntry dataentry : dataEntryList) {
				dataSourceId = dataentry.getDataSourceUUID();
				partName=dataentry.getDatasource().getOwnerID();
				Date startOfToday = DateUtil.getStartOfDay(new Date());
				if (startOfToday.after(dataentry.getTime())) {
					pastDataEnties.add(dataentry);
				} else {
					todayDataEntries.add(dataentry);
				}
			}
			
			Participant part=participantManager.getParticipant(partName);
			if (!pastDataEnties.isEmpty()) {
				updateHistoryDataEntries(dataSourceId,pastDataEnties, part);
			}

			if (!todayDataEntries.isEmpty()) {
				doCreateTodayDataEntries(dataSourceId,todayDataEntries, part);
			}
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	// transaction not supported so that failure of insertion of one point
	// dosn't affect insertion of other points
	public void createGridDataEntries(Set<PDataEntry> dataEntryList) {
		createDataEntries(dataEntryList);
	}

	
	/**
	 * push usage point to dataentry table if this point is in the past (not for
	 * today)
	 * return whether usage data is changed or not
	 */
	protected Set<PDataEntry> updateHistoryDataEntries(String dataSourceId,Set<PDataEntry> dataEntryList, Participant part) {
		Set<Date> dates = new HashSet<Date>();
		Set<PDataEntry> updatedEntries = new HashSet<PDataEntry>();
		
		String baselineModel = systemManager.getPss2Properties().getBaselineModel();
		BaselineModel mb = bmManager.getBaselineModelByName(baselineModel);
		List<BaselineConfig> bcs = bcManager.getBaselineConfigByBaselineModel(mb);
		BaselineConfig bc = (BaselineConfig) bcs.toArray()[0];// when
		
		boolean maEnabled =false;
		long maStart = 0, maEnd = 0;
		//if Morning adjustment is enabled
//		if(bc.getMinMARate()!=0 || bc.getMaxMARate()!=0){
//			maEnabled = true;
//			maStart = bc.getMaStartTime();
//			maEnd = bc.getMaEndTime();
//		}
		//don't need to re-create baseline when historical data is coming
		
		boolean updateBaseline = false;
		
		boolean validateFlag = systemManager.getPss2Features().isEnableValidateUsageData();
		
		for (PDataEntry entry : dataEntryList) {
			try {
				if(validateFlag){
					entry=validateDataEntry(entry, part);	
				}
				if (entry==null)
					continue;
				
				entry=adjustDataEntry(entry);
				
				int i =dataEntryEAO.insertOrUpdate(entry);
				//usage point  changed
				if(i>0) {
					updatedEntries.add(entry);
					if(entry.isActual()){
						Date date =  DateUtil.stripTime(entry.getTime());
						dates.add(date);
						//only usage changes during MA period will lead to baseline re-calculation
						if(maEnabled ){
							long offset = entry.getTime().getTime() - date.getTime();
							if(offset>=maStart && offset<=maEnd){
								updateBaseline = true;
							}
						}
					}
				}
//				if ((demandLimitingEnabled) && (entry.isActual())) {
//					demandLimitingDispatch(entry);
//				}
			} catch (Exception e) {
				// do nothing
				// transaction not required so that failure insertion of one point
				// dosen't affect insertion of other points
				log.debug("Failed to insert ");
			}
		}
		for(Date date:dates){
			String id = USAGE_TASK+"_"+dataSourceId+"_"+DateUtil.formatDate(date);
			asynchCaller.call(new UsageAsynHoldingRunnable(
						id,USAGE_TASK_HOLD,5*60*1000,dataSourceId,date,updateBaseline));
		}
		return updatedEntries;
	}
	
	private PDataEntry adjustDataEntry(PDataEntry entry) {
		PDataSet ds=entry.getDataSet();
		if (!ds.getName().equals("Baseline") || !ds.getName().equals("EventStartIndicator") || !ds.getName().equals("EventEndIndicator")) {
			long period=ds.getPeriod();
			if (isParticipantDemandLimitingEnabled(entry))
				period=60;
			
			Date time=DateUtil.stripMillis(entry.getTime());
			Date startOfDay=DateUtil.getStartOfDay(time);
			
			long mod=((time.getTime()-startOfDay.getTime())/1000)%period;
			if (mod*2>=period){
				entry.setTime(DateUtil.add(time, Calendar.SECOND, (int)(period-mod)));
			} else if (mod>0) {
				entry.setTime(DateUtil.add(time, Calendar.SECOND, (int)(0-mod)));
			}
		}

		return entry;
	}
	
	private PDataEntry validateDataEntry(PDataEntry entry, Participant part){
		
		if (entry.getValue().isNaN()) {
			log.info("Data entry value is null");
			return null;
		}
		if (entry.getValue().doubleValue()<0) {
			log.info("Data entry value is less than 0");
			log.info("Data entry value is: "+entry.getValue().doubleValue());
			return null;
		}
		
		double shed=part.getShedPerHourKW();
		
		if(shed>0){
			if(entry.getValue().doubleValue()>100*shed){
				log.info("Data entry value is more than 100 times the registered usage value.");
				log.info("Data entry value is: "+entry.getValue().doubleValue());
				return null;
			}
		}else{
	 		Double maxUsageData = systemManager.getPss2Properties().getMaxUsageData();
	 	 	if(entry.getValue().doubleValue()>maxUsageData){
	 	 		log.info("Data entry value is more than max usage data in the system defined.");
	 	 		log.info("Data entry value is: "+entry.getValue().doubleValue());
	 	 	    return null;
	 	 	}
	 	}
		
		return entry;
	}
	
	/**
	 * push usage point to dataentry_temp if the point is for today. Today's
	 * usage will be moved to dataentry table by a timer
	 */
	protected Set<PDataEntry> doCreateTodayDataEntries(String dataSourceId,Set<PDataEntry> dataEntryList, Participant part) {
		Set<PDataEntry> updatedEntries = new HashSet<PDataEntry>();
		Date date = null;
		boolean updateBaseline = false;
		
		String baselineModel = systemManager.getPss2Properties().getBaselineModel();
		BaselineModel mb = bmManager.getBaselineModelByName(baselineModel);
		List<BaselineConfig> bcs = bcManager.getBaselineConfigByBaselineModel(mb);
		BaselineConfig bc = (BaselineConfig) bcs.toArray()[0];// when
		
		boolean maEnabled =false;
		long maStart = 0, maEnd = 0;
		//if Morning adjustment is enabled
		if(bc.getMinMARate()!=0 || bc.getMaxMARate()!=0){
			DataSourceUsage du = null;
			List<DataSourceUsage> dus = duEao.findBysourceIdAndDate(dataSourceId, new Date());
			if(dus!=null&&!dus.isEmpty()) du = dus.iterator().next();
			if(du!=null&&du.getAdjusted()!=null&&du.getAdjusted()){
				//don't need to re-calculate baseline unless it is already adjusted
				maEnabled = true;
				BaselineAdjustInfo info = getAdjustInfo(new Date(), part.getParticipantName());
				maStart = info.getStartTime();
				maEnd = info.getEndTime();
			}else{
				maEnabled = false;
			}
			
		}
		boolean validateFlag = systemManager.getPss2Features().isEnableValidateUsageData();
		for (PDataEntry entry : dataEntryList) {
			if(validateFlag){
				entry=validateDataEntry(entry, part);	
			}
			
			if (entry==null)
				continue;
			
			entry=adjustDataEntry(entry);

			int i =currentUsageEAO.insertOrUpdate(entry);
			//usage point  changed
			if(i>0) {
				if(entry.isActual()){
					date = DateUtil.stripTime(entry.getTime());
					//only usage changes during MA period will lead to baseline re-calculation
					if(maEnabled ){
						long offset = entry.getTime().getTime() - date.getTime();
						if(offset >=maStart && offset<=maEnd){
							updateBaseline = true;
						}
					}
				}
				updatedEntries.add(entry);
			}
			if ((demandLimitingEnabled) && (entry.isActual())) {
				demandLimitingDispatch(entry);
			}
		}
		if(!updatedEntries.isEmpty() && date!=null){
			String id = USAGE_TASK+"_"+dataSourceId+"_"+DateUtil.formatDate(date);
			asynchCaller.call(new UsageAsynHoldingRunnable(
						id,USAGE_TASK_HOLD,-1,dataSourceId,date,updateBaseline));
		}
		return updatedEntries;
	}
	
	private boolean isParticipantDemandLimitingEnabled(PDataEntry dataEntry) {
		boolean demandLimitingEnabledForParticipant = false;
		PDataSource dataSource = null;
		try {
			dataSource = dataEntry.getDatasource();
			if (dataSource != null) {
				ProgramParticipant demandLimitingProgramParticipant = programParticipantManager
						.getProgramParticipant(
								DemandLimitingProgram.PROGRAM_NAME,
								dataSource.getOwnerID(), false);
				demandLimitingEnabledForParticipant = ((demandLimitingProgramParticipant != null)
						&& (demandLimitingProgramParticipant.getParticipant() != null)
						&& demandLimitingProgramParticipant.getParticipant()
								.getDemandLimiting()
						&& (demandLimitingProgramParticipant
								.getProgramParticipantRules() != null) && (demandLimitingProgramParticipant
						.getProgramParticipantRules().size() > 0));
			}
		} catch (Exception e) {
		}
		return demandLimitingEnabledForParticipant;

	}

	private void demandLimitingDispatch(PDataEntry dataEntry) {
		if (isParticipantDemandLimitingEnabled(dataEntry)) {
			Connection connection = null;
			Session session = null;
			try {
				connection = participantIntervalMeterDataUpdateFactory
						.createConnection();
				session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				MessageProducer messageProducer = session
						.createProducer(participantIntervalMeterDataUpdate);
				messageProducer.send(session.createObjectMessage(dataEntry));
				// log.debug("Sent a data entry");
			} catch (JMSException e) {
				log.warn(
						"Exception occurred while sending dataentry to demand limiting dispatcher",
						e);
			} finally {
				if (session != null) {
					try {
						session.close();
					} catch (JMSException e) {
						log.warn("Cannot close session", e);
					}
				}
				if (connection != null) {
					try {
						connection.close();
					} catch (JMSException e) {
						log.warn("Cannot close connection", e);
					}
				}
			}
		}
	}

	public PDataSource getDataSourceByNameAndOwner(String name, String owner) {
		return dataSourceEAO.getDataSourceByNameAndOwner(name, owner);
	}

	public List<PDataSource> getDataSourceByNameAndProgramName(String name,
			String programName) {
		List<PDataSource> ret = new ArrayList<PDataSource>();
		try {
			List<Participant> parts = partEAO
					.findParticipantsByProgramName(programName);
			for (Participant part : parts) {
				PDataSource pds = dataSourceEAO.getDataSourceByNameAndOwner(
						name, part.getParticipantName());
				if (pds == null) {
					continue;
				}
				ret.add(pds);
			}
		} catch (AppServiceException ase) {
			return null;
		}
		return ret;
	}

	/*public List<PDataSource> getDataSourceByNameAndEventName(String name,
			String eventName) {
		List<PDataSource> ret = new ArrayList<PDataSource>();
		List<String> participantNames = new ArrayList<String>();
		try {
			List<EventParticipant> eventParts = eventManager
					.getEventParticipantsForEvent(eventName);
			for (EventParticipant part : eventParts) {
				if (!part.getParticipant().isClient()) {
					participantNames.add(part.getParticipant()
							.getParticipantName());
				}
			}
			ret = dataSourceEAO.getDataSourceByOwner(participantNames);
		} catch (Exception ase) {
			return null;
		}
		return ret;
	}*/

	public PDataSet getDataSetByName(String name) {
		return dataSetEAO.getDataSetByName(name);
	}

	public PDataSet createPDataSet(PDataSet dataset) {
		try {
			return dataSetEAO.create(dataset);
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException();
		}
	}

	public void deletePDataSet(String uuid) {
		try {
			dataSetEAO.delete(uuid);
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException();
		}
	}

	public PDataSet getPDataSet(String uuid) throws EntityNotFoundException {
		return dataSetEAO.getById(uuid);
	}

	public List<PDataSet> getDataSets() {
		return dataSetEAO.getAll();
	}

	public PDataSet updatePDataSet(PDataSet dataset) {
		try {
			return dataSetEAO.update(dataset);
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException();
		}
	}

	public PDataSource createPDataSource(PDataSource dataSource) {
		try {
			return dataSourceEAO.create(dataSource);
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException();
		}
	}

	public void deletePDataSource(String uuid) {
		try {
			dataSourceEAO.delete(uuid);
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException();
		}
	}

	public PDataSource getPDataSource(String uuid)
			throws EntityNotFoundException {
		return dataSourceEAO.getById(uuid);
	}

	public PDataSource getPDataSourceByOwnerIdAndName(String ownerId,
			String name) {
		return dataSourceEAO.getDataSourceByNameAndOwner(name, ownerId);
	}

	public PDataSource updatePDataSource(PDataSource dataSource) {
		try {
			return dataSourceEAO.update(dataSource);
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException();
		}
	}

	public PDataEntry createPDataEntry(PDataEntry dataEntry) {
		try {
			String baselineModel = systemManager.getPss2Properties().getBaselineModel();
			BaselineModel mb = bmManager.getBaselineModelByName(baselineModel);
			List<BaselineConfig> bcs = bcManager.getBaselineConfigByBaselineModel(mb);
			BaselineConfig bc = (BaselineConfig) bcs.toArray()[0];// when
			
			boolean maEnabled =false;
			long maStart = 0, maEnd = 0;
			//if Morning adjustment is enabled
			if(DateUtils.isSameDay(dataEntry.getTime(),new Date())){
				if(bc.getMinMARate()!=0 || bc.getMaxMARate()!=0){
					DataSourceUsage du = null;
					List<DataSourceUsage> dus = duEao.findBysourceIdAndDate(dataEntry.getDatasource().getUUID(), new Date());
					if(dus!=null&&!dus.isEmpty()) du = dus.iterator().next();
					if(du!=null&&du.getAdjusted()!=null&&du.getAdjusted()){
						//don't need to re-calculate baseline unless it is already adjusted
						maEnabled = true;
						BaselineAdjustInfo info = getAdjustInfo(new Date(), dataEntry.getDatasource().getOwnerID());
						maStart = info.getStartTime();
						maEnd = info.getEndTime();
					}else{
						maEnabled = false;
					}
				}
			}
			
			
			Date time =dataEntry.getTime();
			Date today = DateUtil.stripTime(new Date());
			PDataEntry result = null;
			if (today.after(time)) {
				//past day's usage
				result= dataEntryEAO.create(dataEntry);
			} else {
				//today's usage
				result= currentUsageEAO.create(dataEntry.toCurrentDataEntry()).toDataEntry();
			}
			if(dataEntry.isActual()){
				String dataSourceId = dataEntry.getDataSourceUUID();
				Date date =  DateUtil.stripTime(time);
				String id = dataSourceId+"_"+DateUtil.formatDate(date);
				boolean updateBaseline =false;
				if(maEnabled ){
					long offset = dataEntry.getTime().getTime() - date.getTime();
					if(offset>=maStart && offset<=maEnd){
						updateBaseline = true;
					}
				}
				asynchCaller.call(new UsageAsynHoldingRunnable(
								id,USAGE_TASK_HOLD,-1,dataSourceId,date,updateBaseline));
			}
			return result;
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException();
		}
	}

	public void deletePDataEntry(String uuid) {
		try {
			dataEntryEAO.delete(uuid);
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException();
		}
	}

	public PDataEntry getPDataEntry(String uuid) throws EntityNotFoundException {
		return dataEntryEAO.getById(uuid);
	}

	public PDataEntry updatePDataEntry(PDataEntry dataEntry) {
		try {
			return dataEntryEAO.update(dataEntry);
		} catch (EJBException e) {
			throw e;
		} catch (Exception e) {
			throw new EJBException();
		}
	}

	/**
	 * Gets all the descendant data sources
	 * 
	 * @param pDataSource
	 *            List of descendant data sources
	 * @return List of descendant data sources
	 */
	public List<PDataSource> getExpandedSources(PDataSource pDataSource) {
		List<PDataSource> result = new ArrayList<PDataSource>();
		if (pDataSource != null) {
			String participantName = pDataSource.getOwnerID();
			Participant participant = participantManager
					.getParticipant(participantName);
			Set<ProgramParticipant> ppList = participant
					.getProgramParticipants();
			// Using the first one
			if (ppList != null && ppList.size() > 0) {
				ProgramParticipant pp = ppList.iterator().next();
				Set<ProgramParticipant> tree = aggManager
						.getFlatDescendants(pp);
				if (tree != null) {
					Iterator<ProgramParticipant> it = tree.iterator();
					while (it.hasNext()) {
						String pName = it.next().getParticipantName();
						PDataSource source = getDataSourceByNameAndOwner(
								pDataSource.getName(), pName);
						if (source != null) {
							result.add(source);
						}
					}
				}
			}
		}
		return result;
	}

	public void removeDuplicates(Collection<PDataEntry> dataEntries,
			PDataSet pDataSet, PDataSource pDataSource) {
		Set<Date> times = new HashSet<Date>(dataEntries.size());
		for (PDataEntry pde : dataEntries) {
			times.add(pde.getTime());
		}
		java.util.List<Tuple2<String, Date>> existingIdTimes = getExisting(
				pDataSet.getUUID(), pDataSource.getUUID(), times);
		Iterator<PDataEntry> i = dataEntries.iterator();
		PDataEntry pde = null;
		while (i.hasNext()) {
			pde = i.next();
			Double v = pde.getValue();
			if (v != null && v < 0) {
				i.remove();
			} else {
				for (Tuple2<String, Date> t : existingIdTimes) {
					if (pde.getTime().equals(t._2)) {
						i.remove();
						break;
					}
				}
			}
		}
	}
	
	public List<Tuple2<String, Date>> getExisting(String datasetUUID,
			String datasourceUUID, Set<Date> times) {
		//commented out, since now we have insertOrUpdate API in EAO Layer for dataentry
		//don't need to check duplicate usage points 
		//further more, there is a bug - if the point not a real usage point, it will never get updated even though a real 
		//value comes in 
		/*return dataEntryEAO.getEm()
				.createNamedQuery("PDataEntry.getTupleByIdsAndTimes")
				.setParameter("oId", datasourceUUID)
				.setParameter("sId", datasetUUID).setParameter("times", times)
				.getResultList();*/
		return Collections.emptyList();
	}


	/**
	 * Get Value Time Map from the data entries
	 * 
	 * @param dataEntries
	 *            List of data entries
	 * @return Value time map
	 */
	protected Map<Date, Double> getValueTimeMap(List<PDataEntry> dataEntries) {
		Map<Date, Double> valueTimeMap = new HashMap<Date, Double>();
		for (PDataEntry entry : dataEntries) {
			valueTimeMap.put(entry.getTime(), entry.getValue());
		}
		return valueTimeMap;
	}
	
	@Override
	@Deprecated
	public List<PDataEntry> getDataEntryList(String datasetUUID,
			String datasourceUUID, DateRange range, boolean showRawData)
			throws EntityNotFoundException {
		List<PDataEntry> usageDes = dataEntryEAO.getDataEntryList(datasetUUID,
				datasourceUUID, range);
		PDataSet dataSet = dataSetEAO.getById(datasetUUID);
		if (usageDes.isEmpty()) {
			return new ArrayList<PDataEntry>();
		} else if (showRawData) {
			return usageDes;
		} else if (UsageUtil.isExceedMaxGap(systemManager.getPss2Properties()
				.getMissingDataThreshold(), usageDes, dataSet)) {
			return new ArrayList<PDataEntry>();
		}
		return null;
		// else{
		// return makeUpMissingData(usageDes);
		// }
	}

	public List<String> getDatasetNamesByOwnerID(String ownerID)
			throws EntityNotFoundException {
		return dataEntryEAO.getDatasetNamesByOwnerID(ownerID);
	}

	public List<java.util.Date> getDataDays(List<String> datasetUUIDs,
			String datasourceUUID, DateRange range)
			throws EntityNotFoundException {
		return dataEntryEAO.getDataDays(datasetUUIDs, datasourceUUID, range);
	}

	public List<PDataEntry> getLatestDataEntry(String datasourceUUID)
			throws EntityNotFoundException {
		PDataSet dataset = dataSetEAO.getDataSetByName("Usage");
		if(dataset==null) return new ArrayList<PDataEntry>();
		
		return dataEntryEAO.getLatestDataEntry(datasourceUUID, dataset.getUUID());
	}

	public PDataEntry getLatestDataEntry(String datasourceUUID,
			String datasetUUID, Date time)

	{
		try {
			return dataEntryEAO.getLatestDataEntry(datasourceUUID, datasetUUID,
					time);
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	public java.util.Date getLastestContact(String datasourceUUID,
			String datasetUUID) throws EntityNotFoundException {
		return dataEntryEAO.getLastestContact(datasourceUUID, datasetUUID);
	}

	@Override
	public void deleteDataEntryByDatasource(String datasourceUUID) {
		dataEntryEAO.deleteDataEntryByDatasource(datasourceUUID);

	}

	@Override
	public List<String> getDataSourcesById(List<String> ids) {
		return dataSourceEAO.getDataSourceOwnerById(ids);
	}

	
	private PDataEntry generateMockValue(PDataEntry mockEntry, PDataSet dset,
			List<PDataEntry> usage, List<PDataEntry> forcast) {
		// Don't make up until baseline and usage line are exist
		if (usage == null || usage.isEmpty() || forcast == null
				|| forcast.isEmpty()) {
			return null;
		}
		PDataEntry pre = getPreviousValidEntryFromList(mockEntry.getTime(),
				usage, dset);
		PDataEntry next = getNextValidEntryFromList(mockEntry.getTime(), usage,
				dset);

		try {
			if (pre != null && next != null) {
				double startOffset = pre.getValue()
						- getEntryFromList(pre.getTime(), forcast).getValue();
				double endOffset = next.getValue()
						- getEntryFromList(next.getTime(), forcast).getValue();

				long count = UsageUtil.getPoints(pre.getTime(), next.getTime(),
						dset) - 1;
				double step = (endOffset - startOffset) / count;

				long stepCounts = UsageUtil.getPoints(pre.getTime(),
						mockEntry.getTime(), dset);
				double value = getEntryFromList(mockEntry.getTime(), forcast)
						.getValue() + (startOffset + step * stepCounts);

				mockEntry.setValue(value);
				mockEntry.setDataSet(pre.getDataSet());
				mockEntry.setDatasource(pre.getDatasource());

				return mockEntry;
			}
		} catch (Exception ex) {
			return null;
		}

		return null;
	}

	private PDataEntry getNextValidEntryFromList(Date startTime,
			List<PDataEntry> list, PDataSet dset) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		Date nextDay = DateUtil.stripTime(DateUtil.add(startTime,
				Calendar.DATE, 1));
		while (startTime.before(nextDay)) {
			PDataEntry next = getEntryFromList(startTime, list);
			if (next != null && next.isActual() == true) {
				return next;
			}
			startTime = DateUtil.add(startTime, Calendar.SECOND,
					(int) dset.getPeriod());
		}
		return null;
	}

	private PDataEntry getPreviousValidEntryFromList(Date startTime,
			List<PDataEntry> list, PDataSet dset) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		Date lastDay = DateUtil.getEndOfDay(DateUtil.add(startTime,
				Calendar.DATE, -1));
		while (!startTime.before(lastDay)) {
			PDataEntry last = getEntryFromList(startTime, list);
			if (last != null && last.isActual() == true) {
				return last;
			}
			startTime = DateUtil.add(startTime, Calendar.SECOND,
					-(int) dset.getPeriod());
		}
		return null;
	}

	private void storeToDB(List<PDataEntry> missingList) {
		for (PDataEntry entry : missingList) {
			try {
				createPDataEntry(entry);
			} catch (Exception ex) {
				Throwable cause = ex.getCause().getCause();
				if (cause instanceof EntityExistsException) {
					// Ignore EntityExistsException.since another thread insert
					// this entry into DB already.Continue insert the others
				} else {
					throw new EJBException(ex);
				}
			}
		}
	}

	public PDataEntry getEntryFromList(Date time, List<PDataEntry> list) {
		if (list == null || list.isEmpty() || time == null) {
			return null;
		}

		for (PDataEntry de : list) {
			if (de == null)
				continue;

			if ((time.getTime()) == (de.getTime().getTime())) {
				return de;
			}
		}

		return null;

	}

	@Override
	public List<String> getDataSourceIdByNameAndOwner(String name,
			List<String> owners) {
		return dataSourceEAO.getDataSourceIdByNameAndOwners(name, owners);
	}

	@Override
	public List<TreeDataSet> getHierarchyDataSet(String participantName,
			DateRange dateRange, boolean includeProgramDataSources,
			boolean includeEventDataSources,
			boolean includeParticipantDataSources) throws AppServiceException {
		List<TreeDataSet> dataSet = new ArrayList<TreeDataSet>();
		Map<String,Object> params = new HashMap<String,Object>();
		try {
			String sql = getDataEntrySQL(params,
					getDataSourceOwners(participantName,
							includeProgramDataSources, includeEventDataSources,
							includeParticipantDataSources), dateRange,
					PERIOD_MIN_SECONDS);

			Map<String, String> datasetColToFeatureMap = new HashMap<String, String>();
			datasetColToFeatureMap.put("datasetName", "name");
			datasetColToFeatureMap.put("dataset_uuid", "UUID");
			Map<String, String> datasourceColToFeatureMap = new HashMap<String, String>();
			datasourceColToFeatureMap.put("datasourceName", "name");
			datasourceColToFeatureMap.put("datasource_uuid", "UUID");

			Map<String, BeanFactory<?>> factories = new HashMap<String, BeanFactory<?>>();
			factories.put("/", new ColumnAsFeatureFactory<TreeDataSet>(
					TreeDataSet.class, datasetColToFeatureMap, "dataset_uuid"));
			factories.put("/dataSource",
					new ColumnAsFeatureFactory<TreeDataSource>(
							TreeDataSource.class, datasourceColToFeatureMap,
							"datasource_uuid", "dataset_uuid"));
			factories.put("/dataSource/dataEntry",
					new ColumnAsFeatureFactory<TreeDataEntry>(
							TreeDataEntry.class));

			HierarchyFactory<TreeDataSet> factory = new HierarchyFactory<TreeDataSet>(
					factories) {
				private static final long serialVersionUID = 2468387495125385719L;

				@Override
				public void buildUp(Object parent, Object child, String path) {
					if (path.equals("/dataSource")) {
						TreeDataSet set = (TreeDataSet) parent;
						TreeDataSource source = (TreeDataSource) child;
						set.getDataSources().add(source);
					} else if (path.equals("/dataSource/dataEntry")) {
						TreeDataSource source = (TreeDataSource) parent;
						TreeDataEntry entry = (TreeDataEntry) child;
						source.getDataEntries().add(entry);
					}
				}
			};
			ListConverter<TreeDataSet> converter = new ListConverter<TreeDataSet>(
					factory);
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sql, params);
			dataSet = sqlExecutor.doNativeQuery(parameterizedSQL,params, converter);

			dataSet = reduceDataVolume(dataSet);

		} catch (Exception e) {
			throw new AppServiceException(e);
		}

		return dataSet;
	}

	private List<String> getDataSourceOwners(String participantName,
			boolean includeProgramDataSources, boolean includeEventDataSources,
			boolean includeParticipantDataSources) throws SQLBuilderException,
			SQLException {
		List<String> owners = new ArrayList<String>();
		if (includeParticipantDataSources) {
			owners.add(participantName);

			// get any clients under this participant
			List<Participant> clients = clientManager
					.getClients(participantName);
			if (clients != null) {
				for (Participant client : clients) {
					owners.add(client.getParticipantName());
				}
			}
		}

		if (includeProgramDataSources) {
			StringBuilder builder = new StringBuilder();
			builder.append("select pp.programName from participant p, program_participant pp");
			builder.append(" where pp.participant_uuid=p.uuid [and p.participantName=${participantName}]");

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("participantName", participantName);

			String sql = SQLBuilder.buildNamedParameterSQL(builder.toString(), params);
			List<String> programs = new ArrayList<String>();
			programs = (List<String>) sqlExecutor.doNativeQuery(sql,params,
					new SimpleListConverter<String>(String.class));
			owners.addAll(programs);
		}

		if (includeEventDataSources) {
			// TODO: not supported yet
		}

		return owners;
	}

	private static String getDataEntrySQL(Map<String, Object> params,List<String> dataSourceOwners,
			DateRange dateRange, int minPeriodSec) throws SQLBuilderException {

		String sqltemplate = "SELECT dset.name AS datasetName, dset.unit, dset.period, dset.description, dset.sync,"
				+ "      dset.graphType, dset.valuePrecision, dset.colorHint, dset.interpolated, dset.dataType,dset.enumTitles,"
				+ "      ds.ownerID, ds.name AS datasourceName, ds.enabled, ds.ownerType, "
				+ "      de.dataset_uuid, de.datasource_uuid, de.time, de.value, de.stringValue, de.valueType, de.actual  "
				+ "FROM dataSource ds, dataset dset, "
				+ "( SELECT de.* FROM dataEntry de WHERE de.time >= ${startTime} AND  de.time <= ${endTime} )de "
				+ "[WHERE ds.ownerID in ${owners}] AND ds.uuid = de.dataSource_uuid AND dset.uuid = de.dataSet_uuid "
				+ "ORDER BY de.dataset_uuid, de.dataSource_uuid,de.time ";

		params.put("owners", dataSourceOwners);
		params.put("startTime", dateRange.getStartTime());
		params.put("endTime", dateRange.getEndTime());
		return sqltemplate;
	}

	/**
	 * Takes the result of a full-resolution dataset query and filters data
	 * entries closer than a minimum threshold. When several entries are bunched
	 * together in too small a space, they get replaced by a single entry
	 * representative of the average time and value
	 * 
	 * @param dataSet
	 * @return
	 */
	private List<TreeDataSet> reduceDataVolume(List<TreeDataSet> dataSet) {
		for (TreeDataSet dset : dataSet) {
			for (TreeDataSource dsource : dset.getDataSources()) {
				int numEntries = dsource.getDataEntries().size();
				if (numEntries > LARGEST_UNFILTERED_DATAENTRY_LIST) {
					reduceOneDataSource(dsource);
				}
			}
		}
		return dataSet;
	}

	private void reduceOneDataSource(TreeDataSource dsource) {
		int numEntries = dsource.getDataEntries().size();
		List<TreeDataEntry> filteredEntries = new ArrayList<TreeDataEntry>();
		List<TreeDataEntry> averager = new ArrayList<TreeDataEntry>();
		long refTime = 0;
		int count = 0;
		for (TreeDataEntry dentry : dsource.getDataEntries()) {
			++count;
			if (count == 1 || count == numEntries) {
				// Alays include the first or last entry
				filteredEntries.add(dentry);
				refTime = dentry.getTime().getTime();
			} else {
				// slice entries into time periods and find one entry for each
				// period
				averager.add(dentry);
				long thisTime = dentry.getTime().getTime();
				if (thisTime - refTime >= PERIOD_MIN_SECONDS * 1000) {
					if (averager.size() == 1) {
						// single step went long enough
						filteredEntries.add(dentry);
					} else {
						// multiple steps before going long enough
						TreeDataEntry prev = averager.get(averager.size() - 2); // previous
																				// point
						long prevTime = prev.getTime().getTime();
						if (thisTime - prevTime >= PERIOD_MIN_SECONDS * 1000) {
							// We went through some tightly-bunched points,
							// but now we've gone more than the threshold in
							// just
							// one jump. There was a significant gap, so keep
							// the boundaries of the gap instead of averaging
							TreeDataEntry average = getAveragePoint(averager);
							filteredEntries.add(average);
							if (!average.equals(prev)) {
								filteredEntries.add(prev);
							}
							filteredEntries.add(dentry);
						} else {
							// We went through some tightly-bunched points,
							// eventually wandering more than the threshold from
							// the previous reference point. Keep the average.
							filteredEntries.add(getAveragePoint(averager));
						}
					}
					refTime = dentry.getTime().getTime();
					averager.clear();
				}
			}
		}
		dsource.setDataEntries(filteredEntries);
	}

	// Search the list of points being averaged
	// and find the real actual sample closest to
	// the average time and value
	private TreeDataEntry getAveragePoint(List<TreeDataEntry> averager) {
		if (averager.isEmpty()) {
			return null;
		}

		TreeDataEntry dentry = averager.get(averager.size() - 1);
		if (averager.size() == 1) {
			return dentry;
		}

		// find the average time and value and keep that
		double avrgVal = 0;
		long avrgTime = 0;
		for (TreeDataEntry sampl : averager) {
			// figure the average time and value of the sample
			avrgVal += sampl.getValue();
			avrgTime += sampl.getTime().getTime();
		}

		TreeDataEntry closest = null;
		double minDist = Double.MAX_VALUE;
		for (TreeDataEntry sampl : averager) {
			// simple rise plus run distance calculation
			// good enough for sorting
			double sampleDist = Math.abs(avrgVal - sampl.getValue())
					+ Math.abs(avrgTime - sampl.getTime().getTime());

			if (sampleDist < minDist) {
				closest = sampl;
			}
		}
		return closest;
	}

	@Override
	public List<TreeDataSet> getAllDataSet() {
		String sql = "SELECT UUID, name, calculationImplClass, unit, period, description, "
				+ "graphType, valuePrecision, colorHint, dataType, enumTitles, missingDataType  FROM dataset where name in ('Baseline','EventStartIndicator','EventEndIndicator','Usage')";
		
		List<TreeDataSet> report = null;
		try {
			report = sqlExecutor.doNativeQuery(sql,
					new ListConverter<TreeDataSet>(
							new ColumnAsFeatureFactory<TreeDataSet>(
									TreeDataSet.class)));
		} catch (SQLException e1) {
			throw new EJBException(e1);
		}

		return (report == null ? new ArrayList<TreeDataSet>() : report);
	}
	
	@Override
	public PDataEntry getNextDataEntry(String datasourceUUID,
			String datasetUUID, Date time) {
		try {
			return dataEntryEAO.getNextDataEntry(datasourceUUID, datasetUUID,
					time);
		} catch (EntityNotFoundException e) {
			return null;
		}
	}

	@Override
	public List<PDataEntry> findByDataSourceDataSetAndDates(String sourceUUID,
			String setUUID, Date begin, Date end) {
		StringBuffer sb = new StringBuffer();
    	
    	sb.append("SELECT uuid,time,value,actual FROM dataentry WHERE dataset_uuid='"+setUUID+"' AND datasource_uuid='"+sourceUUID+"' AND time>=${startTime} AND time<=${endTime} ");
    	sb.append("    UNION     ");
    	sb.append("SELECT uuid,time,value,actual FROM dataentry_temp WHERE dataset_uuid='"+setUUID+"' AND datasource_uuid='"+sourceUUID+"' AND time>=${startTime} AND time<=${endTime} ");
    	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", begin);
		params.put("endTime", end);
    	List<PDataEntry> result = null;
    	try {
    		result = sqlExecutor.doNativeQuery(sb.toString(), params, new ListConverter<PDataEntry>(
					new ColumnAsFeatureFactory<PDataEntry>(
					PDataEntry.class)));
		} catch (SQLException e) {
			log.error(e);
			throw new EJBException(e);
		}
    	
    	return result==null?new ArrayList<PDataEntry>(): result;
	}

	@Override
	public List<PDataEntry> findByDataSourceDataSetAndTime(String sourceUUID,
			String setUUID, Date entryTime) {
		return dataEntryGenEAO.findByDataSourceDataSetAndTime(sourceUUID,
				setUUID, entryTime);
	}

	@Override
	public void createOrUpdateEventDataEntries(String eventName,
			String dataSetId) {
		// first check whether the event is created in db or not
		PDataSet set = null;
		try {
			set = dataSetEAO.getById(dataSetId);
		} catch (EntityNotFoundException e1) {
			log.error(e1.getMessage(), e1);
			return;
		}
		String sqltemplate = SQLLoader.INSERT_EVENT_BASELINE_SQL;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("datasetId", set.getUUID());
		params.put("eventName", eventName);
		// TODO
		String tableName = "dataentry";
		if (set.getName().equalsIgnoreCase("Baseline")) {
			tableName = "history_baseline_dataentry";
		}
		params.put("entryTable", new SQLWord(tableName));
		try {
			// first delete old if exists
			// String
			// sql="DELETE FROM event_dataentry where eventName='"+eventName+"'";
			// sqlExecutor.execute(sql);
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			sqlExecutor.execute(sql,params);
			log.debug("Insert data entry for event " + eventName
					+ "successfully");
		} catch (Exception e) {
			log.error("Failed to create data entries for event " + eventName, e);
			throw new EJBException(e);
		}
	}

	@Override
	public void onUsageChange(String dataSourceId,Date date, boolean updateBaseline) {
		try {
			PDataSource pdatasource = dataSourceEAO.getById(dataSourceId);
			log.debug("Process triggered when usage change - DataSource="+pdatasource.getOwnerID() +" &Date="+date+",updateBaseline="+updateBaseline);
			//update max_gap, last actual & do missing data interpolation
			HistoryDataManager  historyManager = EJBFactory.getBean(HistoryDataManager.class);
			historyManager.updateUsageInterpolation(pdatasource,date);
			
			//update baseline if necessary
			historyManager.onUsageChange(dataSourceId,date,updateBaseline);
			
		} catch (EntityNotFoundException e) {
			log.error(e.getMessage(),e);
		}
	}
	
	@Override
	public void onEventCreated(String eventName) {

	}

	@Override
	public void onEventCompleted(String eventName) {

	}
	
	@Override
	public void clearUsageData(List<String> participantNames, Date start, Date end){
		dataEntryEAO.removeByParticipantNameAndRange(participantNames, start, end);
	}

	@Override
	public com.akuacom.pss2.event.Event findFirstEventOfDay(String participantName, Date date) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT allevent.eventName,  allevent.startTime from (   									");
    	sb.append(" SELECT he.eventName,he.startTime 															");
    	sb.append(" FROM history_event he, history_event_participant hp         								");
    	sb.append(" WHERE he.uuid=hp.history_event_uuid         												");
    	sb.append(" AND DATE(he.startTime)=DATE(${date})         												");
    	sb.append(" AND hp.participantName=${participantName}         											");
    	sb.append("    UNION ALL     																			");
    	sb.append(" SELECT he.eventName,he.startTime 															");
    	sb.append(" FROM event he, event_participant hp, participant part         								");
    	sb.append(" WHERE he.uuid=hp.event_uuid         														");
    	sb.append(" AND hp.participant_uuid=part.uuid    														");
    	sb.append(" AND DATE(he.startTime)=DATE(${date})         												");
    	sb.append(" AND part.participantName=${participantName}         										");
    	sb.append(" ) allevent order by allevent.startTime   													");
    	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("date", date);
		params.put("participantName", participantName);
    	List<com.akuacom.pss2.event.Event> result = null;
    	try {
    		result = sqlExecutor.doNativeQuery(sb.toString(), params, new ListConverter<com.akuacom.pss2.event.Event>(
					new ColumnAsFeatureFactory<com.akuacom.pss2.event.Event>(
							com.akuacom.pss2.event.Event.class)));
		} catch (SQLException e) {
			log.error(e);
			throw new EJBException(e);
		}
    	
    	if(result==null||result.isEmpty()) return null;
    	
    	return result.iterator().next();
	}
	
	private BaselineAdjustInfo getAdjustInfo(Date baselineDay, String participantName){
		String baselineModel = systemManager.getPss2Properties().getBaselineModel();
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
			return info; // Adjust is disabled, return.
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
				info.setMaEnabled(false);
			}
			
			return info;
		}

		com.akuacom.pss2.event.Event event  = findFirstEventOfDay(participantName, baselineDay);
		if(event==null){
			info.setEventName(null);
			info.setStartTime(bc.getMaStartTime());
			info.setEndTime(bc.getMaEndTime());
			
			return info;
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
			info.setMaEnabled(false);
		}
		return info;
	}

}