/**
 * 
 */
package com.akuacom.pss2.data.gridpoint;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.axis2.context.MessageContext;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataEntryEAO;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSetEAO;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.PDataSourceEAO;
import com.akuacom.pss2.grippoint.wsclient.AuthClient;
import com.akuacom.pss2.grippoint.wsclient.AuthResponseBean;
import com.akuacom.pss2.grippoint.wsclient.RetrieveDataClient;
import com.akuacom.pss2.grippoint.wsclient.RetrieveResponseBean;
import com.akuacom.pss2.util.LogUtils;

/**
 * the class GridPointManagerBean
 * 
 */
@Stateless
public class GridPointManagerBean implements GridPointManager.L, GridPointManager.R {

    private static final Logger log = Logger.getLogger(GridPointTimerManagerBean.class);

	private static final String LOGIN_RESULT_OK="ok";
	private static final String DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss";
	private static final String DATASET_NAME="Usage";
	
	private static final String LOG_CATEGORY="Gridpoint Data Service";
	
	@EJB
	PDataSourceEAO.L dataSourceEAO;
	@EJB
	PDataSetEAO.L dataSetEAO;
	@EJB
	DataManager.L dataManager;
	@EJB
	GridPointConfigurationGenEAO.L configEAO;
	@EJB
	PDataEntryEAO.L dataEntryEAO;
	@EJB
	Pss2SQLExecutor.L sqlExecutor;
	
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.data.gridpoint.GridPointManager#process()
	 */
	@Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void process() {
		GridPointConfiguration config = getGridPointConfiguration();
		if (config==null) return;
    	
		try {
	    	AuthClient authenticationInvoker = new AuthClient("", config.getAuthenticationURL(), "");
			MessageContext authenticationResult = authenticationInvoker.authentication(config.getUsername(), config.getPassword());
			AuthResponseBean bean = new AuthResponseBean(authenticationResult);
			
			if(LOGIN_RESULT_OK.equals(bean.getLoginResult())&&bean.hasCookies()){
				RetrieveDataClient retrieveDataInvoker = new RetrieveDataClient("", config.getRetrieveDataURL(), "");
				
				List<PDataSource> dataSources=dataSourceEAO.getDataSourceBySiteID();
				PDataSet dataSet=dataSetEAO.getDataSetByName(DATASET_NAME);
				
				Date startDate=null;
				Date endDate=new Date();
				DateRange orgDateRange=new DateRange();
				Map<String, Date> startDateMap=new HashMap<String, Date>();
				if (config.getFixScopeEnabled()!=null && config.getFixScopeEnabled()){
					int dateScope=0-config.getFixScopeValue();
					startDate=DateUtils.addMinutes(endDate, dateScope);
				}else {
					startDate=DateUtils.addDays(endDate, 0-config.getDateBackScope());
					orgDateRange.setStartTime(startDate);
					orgDateRange.setEndTime(endDate);
					
					startDateMap=dataEntryEAO.getLastValidDate(dataSet.getUUID(), dataSources, orgDateRange);
				}
				
				SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT);
				for (PDataSource ds:dataSources) {
					if (config.getFixScopeEnabled()!=null && !config.getFixScopeEnabled()){
						if (startDateMap.get(ds.getUUID())!=null)
							startDate = startDateMap.get(ds.getUUID());
						else
							startDate=orgDateRange.getStartTime();
					}
					
					DateRange range=new DateRange();
					range.setStartTime(startDate);
					range.setEndTime(endDate);
					
					String siteID=ds.getSiteID();
					org.apache.axiom.soap.SOAPEnvelope resEnvelope = retrieveDataInvoker.retrieveData(bean.getCookie(), siteID, 
							format.format(startDate), format.format(endDate), "true");
					
					RetrieveResponseBean resBean = new RetrieveResponseBean(resEnvelope);
					if(resBean.isSuccessful()){
						Map<Date, Double> map = resBean.getMap();
						updateDataEntries(createDataEntries(map, dataSet, ds));
					}else{
						String description="Failed to retrieve data between "+format.format(startDate)+ " and " 
								+ format.format(endDate)+" from site "+siteID;
						log.warn(LogUtils.createLogEntry(null, LOG_CATEGORY, description, null));
					}
				}
			}
		} catch (Exception e) {
			log.error(LogUtils.createLogEntry(null, LOG_CATEGORY, e.getMessage(), null));
			log.debug(LogUtils.createExceptionLogEntry(null, LOG_CATEGORY, e));
		}
	}

	private void updateDataEntries(Set<PDataEntry> entries) {
		dataManager.createGridDataEntries(entries);
	}
	
	private Set<PDataEntry> createDataEntries(Map<Date, Double> map, PDataSet dataSet, PDataSource dataSource){
		Set<PDataEntry> dataEntries=new HashSet<PDataEntry>();
		for (Date time:map.keySet()) {
			PDataEntry entry=new PDataEntry();
			entry.setDataSet(dataSet);
			entry.setDatasource(dataSource);
			entry.setTime(time);
			entry.setValue(map.get(time));
			entry.setActual(true);
			dataEntries.add(entry);
		}
		
		return dataEntries;
	}
	
	protected GridPointConfiguration getGridPointConfiguration(){
		List<GridPointConfiguration> configList = configEAO.getAll();
		if (configList!=null && configList.size()>0)
			return configList.get(0);
		else
			return null;
	}
}
