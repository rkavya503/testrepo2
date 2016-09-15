package com.akuacom.pss2.data.irr;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.log4j.Logger;
import org.jboss.wsf.spi.annotation.WebContext;

import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.pss2.data.usage.UsageUtil;
import com.akuacom.pss2.data.usage.calcimpl.DateEntrySelectPredicate;
import com.akuacom.pss2.data.usage.calcimpl.ImplFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.utils.lang.DateUtil;

	@Stateless
	@WebService(endpointInterface = "com.akuacom.pss2.data.irr.IRRUsageDataServicer", serviceName = "IRRUsageDataServicer")
	@SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.RPC)
	@WebContext(authMethod="BASIC")
	public class IRRUsageDataServicerBean implements IRRUsageDataServicer.R, IRRUsageDataServicer.L {
		
		private ThreadLocal<WebServiceContext> threadSafeContext = new ThreadLocal<WebServiceContext>();
	    @Resource
	    public void setContext(WebServiceContext context){
	    	threadSafeContext.set(context);
	    }
		
	    private static final Logger log = Logger.getLogger(IRRUsageDataServicerBean.class);
	    @EJB
	    protected DataManager.L dataManager;
	    @EJB
		protected SystemManager.L systemManager;
	    @EJB
		UsageDataManager.L usageDataManager;
		
        @Override
		public IRRUsageDataVo getDataEntriesForParticipant(String partName,
				String startTime, String endTime, boolean showRawData, boolean individual) {
        	
        	String loginUser = threadSafeContext.get().getUserPrincipal().getName();
        	boolean accessAble = accessCheck(partName, loginUser);
        	
        	if(!accessAble) {
        		log.error("Access denied error: "+loginUser +" tried to access "+partName+"'s usage data at "+new Date());
        		throw new EJBException("Access denied error: "+loginUser +" tried to access "+partName+"'s usage data at "+new Date());
        		
        	}
        	
			Date startDate = null;
			Date endDate = null;
            boolean isUiShowRawData = true;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            startDate = DateUtil.parseStringToDate(startTime, simpleDateFormat);
			endDate = DateUtil.parseStringToDate(endTime, simpleDateFormat);
			DateRange dateRange = new DateRange();
			dateRange.setStartTime(startDate);
			dateRange.setEndTime(endDate);
			
			//1. get TreeSet list
			List<TreeDataSet> dataSets = dataManager.getAllDataSet();
			//2.loop all sets, and get dataEntry for each dataSet
			StringBuffer messages = new StringBuffer();
			List<String> allPaticipantNames = null;
			List<String> contributedPaticipantNames = null;
			List<String> eventContributedPaticipantNames = null;
			if(dataSets!=null && !dataSets.isEmpty()){
				for(TreeDataSet set : dataSets){
					DataCalculationHandler handler = ImplFactory.instance().getDataCalculationHandler(set.getCalculationImplClass());
					PDataSet dataSet = new PDataSet();
					dataSet.setUUID(set.getUUID());
					dataSet.setName(set.getName());
					dataSet.setPeriod(set.getPeriod());
					
				    int maxiFractionDigits = set.getValuePrecision();
				    NumberFormat nf = getNumberFormatter(maxiFractionDigits);
					
					DataEntriesVo entriesVo = handler.getData(dataSet, partName, startDate, showRawData, individual, isUiShowRawData);
					if(entriesVo.getMessage()!=null&&!entriesVo.getMessage().isEmpty()){
						messages.append(entriesVo.getMessage());
						messages.append("\n");
					}
					if("Usage".equalsIgnoreCase(set.getName())&&entriesVo.getAllPaticipantNames()!=null&&!entriesVo.getAllPaticipantNames().isEmpty()){
						allPaticipantNames = entriesVo.getAllPaticipantNames();
						contributedPaticipantNames = entriesVo.getContributedPaticipantNames();
					}
					
					if("EventStartIndicator".equalsIgnoreCase(set.getName())&&entriesVo.getContributedPaticipantNames()!=null&&!entriesVo.getContributedPaticipantNames().isEmpty()){
						eventContributedPaticipantNames = entriesVo.getContributedPaticipantNames();
					}
					
					List<PDataEntry> entries = entriesVo.getEntries();
					List<TreeDataEntry> treeEntries = new ArrayList<TreeDataEntry>();
					for(PDataEntry entry: entries){
						TreeDataEntry te = new TreeDataEntry();
						te.setTime(entry.getTime());
						te.setValue(getDoubleValue(entry.getValue(),nf));
						te.setActual(entry.isActual());
						
						treeEntries.add(te);
					}
					TreeDataSource source = new TreeDataSource();
					source.setUUID(partName);
					source.setOwnerID(partName);
					source.setDataEntries(treeEntries);
					
					set.setDataSources(Arrays.asList(source));
				}
				
			}
			
			if(dataSets!=null && !dataSets.isEmpty()){
				List<TreeDataEntry> start = null;
				List<TreeDataEntry> end = null;
				for(TreeDataSet set : dataSets){
					// Baseline Usage EventStartIndicator EventEndIndicator
					if(set.getName().equalsIgnoreCase("EventStartIndicator")){
						start = extractData(start, set);
						if(start==null||start.isEmpty()){
							set.setDisable(true);
						}
					}else if(set.getName().equalsIgnoreCase("EventEndIndicator")){
						end = extractData(end, set);
						if(end==null||end.isEmpty()){
							set.setDisable(true);
						}
					}
					
				}// end of loop
			}
			
			if(systemManager.getPss2Features().isEnableEntireDayShedLine()){
				appendEntireDayShedLine(partName, dataSets);
			}
			
			if(systemManager.getPss2Features().isEnableEventPeriodShedLine()){
				appendEventPeriodShedLine(partName, dataSets, startDate, individual);
			}
			//null exception
			List<String> contributed = null;
			if(eventContributedPaticipantNames==null||contributedPaticipantNames==null){
				contributed = contributedPaticipantNames;
				
			}else{
				contributed = new ArrayList<String>();
				for(String value :contributedPaticipantNames){
					if(!eventContributedPaticipantNames.contains(value)){
						int i=allPaticipantNames.indexOf(value);
						value = value+" *";
						if( i!=-1){
							allPaticipantNames.set(i, value);
						}
						
					}
						
					contributed.add(value);
				}
				
			}
			
			
			IRRUsageDataVo vo = new IRRUsageDataVo();
			vo.setParticipantName(partName);
			vo.setDataSets(dataSets);
			vo.setMessage(messages.toString());
			vo.setAllPaticipantNames(allPaticipantNames);
			vo.setContributedPaticipantNames(contributed);
			
			return vo;
		}

		private boolean accessCheck(String partName,
				String loginUser) {
			if (threadSafeContext.get().isUserInRole("Admin") ||
					threadSafeContext.get().isUserInRole("Operator") || 
					threadSafeContext.get().isUserInRole("Readonly") ||
					threadSafeContext.get().isUserInRole("Dispatcher")) {
        		 return true;
             }
        	if(partName.equals(loginUser)) {
        		return true;
        	}else{
        		 List<String> children = usageDataManager.findAllParticipantNames(loginUser, new Date());
        		if(children.contains(partName)){
        			return true;
        		}
        	}
			return false;
		}

		private List<TreeDataEntry> extractData(List<TreeDataEntry> usage,
				TreeDataSet set) {
			List<TreeDataSource> datasources = set.getDataSources();
			if(datasources!=null&&!datasources.isEmpty()){
				TreeDataSource ds = datasources.get(0);
				if(ds.getDataEntries()!=null&&!ds.getDataEntries().isEmpty()){
					usage = ds.getDataEntries();
				}
			}
			return usage;
		}

		private NumberFormat getNumberFormatter(int maxiFractionDigits) {
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setGroupingUsed(false);
			nf.setMinimumFractionDigits(0);
			nf.setMaximumFractionDigits(maxiFractionDigits);
			return nf;
		}

        private void appendEventPeriodShedLine(String partName,
				List<TreeDataSet> dataSets, Date startDate, boolean individual) {
        	//1. check if there is an event on the particular day
        	Event event = null;
        	if(individual){
        		event = usageDataManager.getEventByDateAndParticipant(partName, startDate);
        	}else{
        		event = usageDataManager.getEventByDateAndAggreagator(partName, startDate);
        	}
        	
        	//2. load the configured properties. display name and color
        	String name = systemManager.getPss2Properties().getEventPeriodShedLineName();
			String color = systemManager.getPss2Properties().getEventPeriodShedLineColor();
        	if(event == null){
        		//3. return an empty Dataset when there isn't any event
        		TreeDataSet set = new TreeDataSet();
    			set.setUnit("kw");
    			set.setName(name);
    			set.setDescription(name);
    			set.setColorHint(color);
    			set.setDataType(0);
    			TreeDataSource source = new TreeDataSource();
    			source.setUUID(partName);
    			source.setOwnerID(partName);
    			source.setDataEntries(new ArrayList<TreeDataEntry>());
    			set.setDisable(true);
    			set.setDataSources(Arrays.asList(source));
    			dataSets.add(set);
    			
    			return;
        	}
        	//4. retrieve baseline data entries and usage data entries during the event period
			List<TreeDataEntry> usageEntriesTemp = null;
			List<TreeDataEntry> baseEntriesTemp = null;
			if(dataSets!=null && !dataSets.isEmpty()){
				for(TreeDataSet set : dataSets){
					if("Usage".equals(set.getName())){
						usageEntriesTemp = set.getDataSources().get(0).dataEntries;
					}
					if("Baseline".equals(set.getName())){
						baseEntriesTemp = set.getDataSources().get(0).dataEntries;
					}
				}
			}
			List<TreeDataEntry> usageEntries = new ArrayList<TreeDataEntry>();
			List<TreeDataEntry> baseEntries = new ArrayList<TreeDataEntry>();
			
			DateEntrySelectPredicate betweenPredicate = new DateEntrySelectPredicate(UsageUtil.getCurrentTime(event.getStartTime()), UsageUtil.getCurrentTime(event.getEndTime()), DateEntrySelectPredicate.BETWEEN_START_END, TreeDataEntry.class, "time");
			usageEntries = (List<TreeDataEntry>) CollectionUtils.select(usageEntriesTemp, betweenPredicate);
			baseEntries = (List<TreeDataEntry>) CollectionUtils.select(baseEntriesTemp, betweenPredicate);
			//5. generate the shed line, then put it into the result collection
			generateShedLine(partName, dataSets, usageEntries, baseEntries, name,
					color);
		}
		private void appendEntireDayShedLine(String partName,
				List<TreeDataSet> dataSets) {
			//1. retrieve baseline data entries and usage data entries 
			List<TreeDataEntry> usageEntriesTemp = null;
			List<TreeDataEntry> baseEntriesTemp = null;
			if(dataSets!=null && !dataSets.isEmpty()){
				for(TreeDataSet set : dataSets){
					if("Usage".equals(set.getName())){
						usageEntriesTemp = set.getDataSources().get(0).dataEntries;
					}
					if("Baseline".equals(set.getName())){
						baseEntriesTemp = set.getDataSources().get(0).dataEntries;
					}
				}
			}
			//2. load the configured properties. display name and color
			String name = systemManager.getPss2Properties().getEntireDayShedLineName();
			String color = systemManager.getPss2Properties().getEntireDayShedLineColor();
			//3. generate the shed line, then put it into the result collection
			generateShedLine(partName, dataSets, usageEntriesTemp, baseEntriesTemp, name,
					color);
		}

		private void generateShedLine(String partName, List<TreeDataSet> dataSets,
				List<TreeDataEntry> usageEntries,
				List<TreeDataEntry> baseEntries, String name, String color) {
			List<TreeDataEntry> shedEntries = new ArrayList<TreeDataEntry>();
			NumberFormat nf = getNumberFormatter(3);
			if(usageEntries!=null&&!usageEntries.isEmpty()&&baseEntries!=null&&!baseEntries.isEmpty()){
				//subtract
				Iterator<TreeDataEntry> dataEntrys = usageEntries.iterator();
				 while(dataEntrys.hasNext()){
					 TreeDataEntry cur = dataEntrys.next();
					 DateEntrySelectPredicate equalPredicate = new DateEntrySelectPredicate(UsageUtil.getCurrentTime(cur.getTime()), 0, DateEntrySelectPredicate.EQUAL_START, TreeDataEntry.class, "time");
					 
					 Iterator<TreeDataEntry> baseDataEntrys =
			             new FilterIterator( baseEntries.iterator(), equalPredicate );
					 if(baseDataEntrys.hasNext()){
						 Double value = baseDataEntrys.next().getValue() - cur.getValue();
						 TreeDataEntry entry = new TreeDataEntry();
						 entry.setActual(true);
						 entry.setTime(cur.getTime());
						 entry.setValue(getDoubleValue(value,nf));
						 
						 shedEntries.add(entry);
					 }
				 }
			}
			TreeDataSet set = new TreeDataSet();
			set.setUnit("kw");
			set.setName(name);
			set.setDescription(name);
			set.setColorHint(color);
			set.setDataType(0);
			TreeDataSource source = new TreeDataSource();
			source.setUUID(partName);
			source.setOwnerID(partName);
			source.setDataEntries(shedEntries);
			if(shedEntries==null||shedEntries.isEmpty()){
				set.setDisable(true);
			}
			set.setDataSources(Arrays.asList(source));
			dataSets.add(set);
		}

		@Override
		public IRRUsageDataVo getIRRDataEntriesForEvent(String eventName,
				String startTime, String endTime, boolean showRawData) {
			return null;
		}
		
		private static Double getDoubleValue(Double in, NumberFormat nf){
			   	
		   	if(Double.isNaN(in)) return 0.0;
		   	
		   	return Double.valueOf(nf.format(in));
		}

}
	
