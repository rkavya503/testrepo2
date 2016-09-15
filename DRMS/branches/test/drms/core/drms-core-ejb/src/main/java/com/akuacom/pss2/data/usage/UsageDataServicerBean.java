package com.akuacom.pss2.data.usage;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.jboss.wsf.spi.annotation.WebContext;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.PDataSourceEAO;
import com.akuacom.pss2.data.usage.calcimpl.DateEntrySelectPredicate;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.history.HistoryEvent;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;
import com.kanaeki.firelog.util.FireLogEntry;

@Stateless
@WebService(endpointInterface = "com.akuacom.pss2.data.usage.UsageDataServicer", serviceName = "UsageDataServicer")
@SOAPBinding(style = javax.jws.soap.SOAPBinding.Style.RPC)
@WebContext(authMethod="BASIC")
public class UsageDataServicerBean extends UsageDataManagerBean implements
        UsageDataServicer.R, UsageDataServicer.L {
	
	private ThreadLocal<WebServiceContext> threadSafeContext = new ThreadLocal<WebServiceContext>();
    @Resource
    public void setContext(WebServiceContext context){
    	threadSafeContext.set(context);
    }
    @EJB
    ParticipantEAO.L eao;
    @EJB
    protected PDataSourceEAO.L dataSourceEAO;
    @EJB
    SystemManager.L sysManager;

    private static final Logger log = Logger.getLogger(UsageDataServicerBean.class);

    @javax.jws.WebMethod()
    public List<String> getDateRanges(String participantName,
            List<String> partDataSetNames, String strDate) {
    	DateRange dateRange = null;
    	if((strDate!=null&&strDate.trim().length()!=0)){
    		 dateRange = UsageUtil.generateDateRange(strDate);
    	}
        String retCode = "FAILURE: ";
        FireLogEntry logEntry = LogUtils.createLogEntry();
        logEntry.setCategory(LogUtils.CATAGORY_WEBSERVICE);
        List<String> ret = new ArrayList<String>();
        try {
            List<String> datasetUUIDs = new ArrayList<String>();
            for (PDataSet pdataset : getDataSets()) {
                datasetUUIDs.add(pdataset.getUUID());
            }
            PDataSource pdatasource = getDataSourceByNameAndOwner("meter1",
                    participantName);
            //Aggregation
            List<PDataSource> expandedDataSource = this.getExpandedSources(pdatasource);
            List<Date> dates = getDataDays(datasetUUIDs, pdatasource.getUUID(),
                    dateRange);
            for(PDataSource source: expandedDataSource){
            	List<Date> d = getDataDays(datasetUUIDs, source.getUUID(),
                        dateRange);
            	if(d != null && d.size() > 0){
            		if(dates != null){
            			dates.addAll(d);
            		}else{
            			dates = d;
            		}
            	}
            }
            ret.addAll(UsageUtil.getDRASDateRanges(dates));

        } catch (Exception e) {
            logEntry.setDescription(e.getMessage());
            // TODO 2992 entry not logged?
            retCode = retCode + "TransactionID: "
                    + LogUtils.getTransactionIDFromLogEntry(logEntry)
                    + " ErrorMessage: " + e.getMessage();
            log.debug(logEntry);
        }
        return ret;
    }
    
    private boolean accessCheck(String partName,
			String loginUser) {
		if (threadSafeContext.get().isUserInRole("Admin")||
				threadSafeContext.get().isUserInRole("Operator") ||
				threadSafeContext.get().isUserInRole("Readonly") ||
				threadSafeContext.get().isUserInRole("Dispatcher")) {
    		 return true;
         }
    	if(partName.equals(loginUser)) {
    		return true;
    	}else{
    		 List<String> children = findAllParticipantNames(loginUser, new Date());
    		if(children.contains(partName)){
    			return true;
    		}
    	}
		return false;
	}

   @javax.jws.WebMethod()
   public UsageDataTransferVo getDataEntries(List<String> partNames, String programName, String eventName, List<String> partDataSetNames, String strDate, boolean showRawData, boolean isReport, boolean individual){
	   String loginUser = threadSafeContext.get().getUserPrincipal().getName();
	   boolean accessAble = (partNames==null||partNames.isEmpty())?true:false;
	   String name = null;
	   for(String partName : partNames){
		   accessAble = accessCheck(partName, loginUser);
		   if(!accessAble){
			   name = partName;
			   break;
		   }
	   }
	   	
	   if(!accessAble) {
		   log.error("Access denied error: "+loginUser +" tried to access "+name+"'s usage data at "+new Date());
   		
		   throw new EJBException("Access denied error: "+loginUser +" tried to access "+name+"'s usage data at "+new Date());
	   }
	   
	   long startTime = System.currentTimeMillis(); 
	    UsageDataTransferVo vo =new UsageDataTransferVo();
	    List<DateRange> dateRanges = new ArrayList<DateRange>();
    	DateRange dateRange = UsageUtil.generateDateRange(strDate);
    	dateRanges.add(dateRange);
    	
        DateRange dr = null;
		if (dateRanges != null && dateRanges.size() > 0) {
			dr = dateRanges.get(0);
		}
        
        Event eve = null;
        PDataSet usageset = dataSetEAO.getDataSetByName("usage");
		PDataSet baselineset = dataSetEAO.getDataSetByName("Baseline");
	   	if(showRawData){
			//currently only concern with individual participant
	   		if(partNames != null && partNames.size() > 0) {
	        	//isIndividual = crManager.isIndividualparticipant(partNames.iterator().next());

	        	List<PDataEntry> usage = findRealTimeEntryListForParticipant
	        			(partNames.iterator().next(), usageset.getUUID(), DateUtil.parseStringToDate(strDate,DateUtil.dateFormatter()),true, true, false);//findRawRealTimeEntryListForParticipant(partNames.iterator().next(), DateUtil.parseStringToDate(strDate,DateUtil.dateFormatter()));
	        	List<PDataEntry> base = findBaselineEntryListForParticipant(partNames.iterator().next(), DateUtil.parseStringToDate(strDate,DateUtil.dateFormatter()), baselineset.getUUID(), individual,false);// this method will always return the adjusted baseline rather than raw baseline
	        			//findBaselineEntryListForParticipant(partNames.iterator().next(), DateUtil.parseStringToDate(strDate,DateUtil.dateFormatter()));
	        	vo.setUsagelineList(usage);
	    	    vo.setBaselineList(base);
	        	
				eve = getEventByDateAndParticipant(partNames, dr);
				
			} else if (eventName != null && !eventName.isEmpty()) {
				eve = getEventByName(eventName);//TODO: show raw data
				List<PDataEntry> usage = null;
				List<PDataEntry> base = null;
				if(eve.isActive()){
					usage = findRealTimeEntryListForEvent(eventName, !eve.isActive(), true, true);
					base = findBaselineEntryListForEvent(eventName,baselineset.getUUID(),!eve.isActive(),individual,true);
				}else{
					usage = findRealTimeEntryListForEvent(eventName, !eve.isActive(), true, true);
					base = findBaselineEntryListForEvent(eventName,baselineset.getUUID(),!eve.isActive(),individual,true);
				}
				
				vo.setUsagelineList(usage);
	    	    vo.setBaselineList(base);
			}
		}else{
			// if individual 
			
	        if (partNames != null && partNames.size() > 0) {
	        	long startTimeUsage = System.currentTimeMillis(); 
	        	List<PDataEntry> usage =findRealTimeEntryListForParticipant(
	        			partNames.iterator().next(), usageset.getUUID(), DateUtil.parseStringToDate(strDate,DateUtil.dateFormatter()),
	        			individual, false, false);
	        			
	        	long duration_usage = System.currentTimeMillis() - startTimeUsage;
	    		log.info("UsageDataServicerBean findRealTimeEntryListForParticipant took "+duration_usage+ "(ms)");
	    		long startTimeBase = System.currentTimeMillis(); 
	        	List<PDataEntry> base = findBaselineEntryListForParticipant(partNames.iterator().next(), DateUtil.parseStringToDate(strDate,DateUtil.dateFormatter()), baselineset.getUUID(),individual, showRawData);
	        	long duration_base = System.currentTimeMillis() - startTimeBase;
	    		log.info("UsageDataServicerBean findBaselineEntryListForParticipant took "+duration_base+ "(ms)");
	        	vo.setUsagelineList(usage);
	    	    vo.setBaselineList(base);
	        	
	    	    long startTimeEvent = System.currentTimeMillis(); 
				eve = getEventByDateAndParticipant(partNames, dr);
				long duration_Event = System.currentTimeMillis() - startTimeEvent;
	    		log.info("UsageDataServicerBean getEventByDateAndParticipant took "+duration_Event+ "(ms)");
				
			} else if (eventName != null && !eventName.isEmpty()) {
				
				eve = getEventByName(eventName);
				List<PDataEntry> usage = null;
				List<PDataEntry> base = null;
				if(eve.isActive()){
					usage = findRealTimeEntryListForEvent(eventName, !eve.isActive(), individual, false);
					base = findBaselineEntryListForEvent(eventName, baselineset.getUUID(),!eve.isActive(),individual,false);
				}else{
					usage = findRealTimeEntryListForEvent(eventName, !eve.isActive(), individual, false);
					base = findBaselineEntryListForEvent(eventName, baselineset.getUUID(),!eve.isActive(),individual,false);
				}
				
				vo.setUsagelineList(usage);
	    	    vo.setBaselineList(base);
			}
		}
        List<PDataEntry> eventLines = new ArrayList<PDataEntry>();
        
		if (eve != null
		        && eve.getStartTime().getTime() < (DateUtil
		                .endOfDay(new Date())).getTime()) {
			PDataEntry startDot = new PDataEntry();
	        startDot.setTime(eve.getStartTime());
	        startDot.setValue(0.0);
	        
	        PDataEntry endDot = new PDataEntry();
	        endDot.setTime(eve.getEndTime());
	        endDot.setValue(0.0);
	        
	        eventLines.add(startDot);
	        eventLines.add(endDot);
		}
		
        vo.setUsagelineEventList(eventLines);
        long duration = System.currentTimeMillis() - startTime;
		log.info("UsageDataServicerBean getDataEntries took "+duration+ "(ms)");
        if(!isReport) return vo;
        
        return getReportTable(vo, partNames, dateRange, showRawData);
    }
   
	// On the current day only, instead of "Entire Day" it should say:
	//	Today(until current time), such as, Today (until 10:15 AM)
	private UsageDataTransferVo getReportTable(UsageDataTransferVo vo, List<String> partNames,
			DateRange dateRange,boolean showRawData) {
		Date date = dateRange.getStartTime();
	   Calendar calEnd = new GregorianCalendar();
	
	   calEnd.setTime(date);
	
	   Date today = new Date();
	   if(DateUtils.isSameDay(today, date)){// last time is now
		   calEnd.setTime(today);
//		   Date lastTime = getLastActualTime(partNames.get(0), DateUtil.stripTime(date));
//		   if((null!=lastTime)&&today.after(lastTime)&&DateUtils.isSameDay(today, lastTime)){
//			 calEnd.setTime(lastTime);
//		   }
		   SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
		   vo.setEntireTitle("Today (until "+format.format(calEnd.getTime())+")");
	    }else{
		   calEnd = DateUtil.endOfDay(calEnd);
		   vo.setEntireTitle("Entire Day");
	    }
	   if(showRawData) return vo;
	   
	   final PDataSet dataSet = getDataSetByName("Usage");
	   
	   Date start = DateUtil.stripTime(date);
	   Date end = calEnd.getTime();
	   UsageSummary actual = this.getUsageSummaryFromList(vo.getUsagelineList(), start, end);
	   NumberFormat nf = getNumberFormatter();
	   if(actual!=null)
	   {
		   	vo.setDayAvgActual(getDoubleValue(actual.getAverage(),nf));
			vo.setDayTotalActual(getDoubleValue(calculateTotal(date, calEnd, today, actual.getAverage()),nf));
	   }
	   UsageSummary base = this.getUsageSummaryFromList(vo.getBaselineList(), start, end);
	   if(base!=null){
	   		vo.setDayAvgBase(getDoubleValue(base.getAverage(),nf));
			vo.setDayTotalBase(getDoubleValue(calculateTotal(date, calEnd, today, base.getAverage()),nf));
	   }
	  // UsageSummary shed = this.getShedSummaryFromList(vo.getUsagelineList(), vo.getBaselineList(), start, end);
	   if(actual!=null&&base!=null&&base.getAverage()>0&&actual.getAverage()>0){
	   		vo.setDayAvgShed(getDoubleValue(base.getAverage()-actual.getAverage(),nf));
			vo.setDayTotalShed(getDoubleValue(calculateTotal(date, calEnd, today, vo.getDayAvgShed()),nf));
	   }else{
		   vo.setDayAvgShed(0);
		   vo.setDayTotalShed(0);
	   }
	   
	   Event eve = getEventByDateAndParticipant(partNames, dateRange); // TODO: the event we obtained already
		
	   UsageSummary baseEvent = null;
	   UsageSummary shedEvent = null;
	   UsageSummary actualEvent = null;
	
	   Date eventStart =  null;
	   Date eventEnd =   null;
	   //todo we only assume one event a day
	   if(eve != null)
	   {
	       eventStart =  eve.getStartTime();
	       eventEnd =   eve.getEndTime();
	       if(!today.after(eventEnd)){
	       	// when an event is in active status, calculate the usage report from event start time to current time
	       	eventEnd = today;
	       }
	       baseEvent = getUsageSummaryFromList(vo.getBaselineList(),eventStart,eventEnd);
	  
	       actualEvent = getUsageSummaryFromList(vo.getUsagelineList(),eventStart,eventEnd);
	       
	       shedEvent = getShedSummaryForEvent(baseEvent,actualEvent);
	    }
	
	   if(actualEvent!=null){
	   		vo.setEventAvgActual(getDoubleValue(actualEvent.getAverage(),nf));
			vo.setEventTotalActual(getDoubleValue(calculateEventDurationTotal(eventStart, eventEnd, today, actualEvent.getAverage()),nf));
	   }
	   
	   if(baseEvent!=null){
	       	vo.setEventAvgBase(getDoubleValue(baseEvent.getAverage(),nf));
			vo.setEventTotalBase(getDoubleValue(calculateEventDurationTotal(eventStart, eventEnd, today, baseEvent.getAverage()),nf));
	   }
	   
		if(shedEvent!=null){
			vo.setEventAvgShed(getDoubleValue(shedEvent.getAverage(),nf));
			vo.setEventTotalShed(getDoubleValue(calculateEventDurationTotal(eventStart, eventEnd, today, shedEvent.getAverage()),nf));
		}
		
	   return vo;
	}

	private NumberFormat getNumberFormatter() {
		NumberFormat nf = NumberFormat.getNumberInstance();
	    int maxiFractionDigits = 3;
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(maxiFractionDigits);
		return nf;
	}
    
	/**
	 * Return all event whether active or historical
	 * @param eventName
	 * @param partName
	 * @return
	 */
	 private Event getEventByDateAndParticipant(List<String> partNames, DateRange dr) {
		Event eve = null;
		List<Event> events = null;
		List<String> excludePrograms = new ArrayList<String>();
		String excludes = sysManager.getPss2Properties().getExcludedProgramsForEventLine();
		if(excludes!=null){
			excludePrograms = Arrays.asList(excludes.split(","));
		}
		
		events = eventManager.findByParticipantProgramAndDate(dr.getStartTime(), DateUtil.getNextDay(dr.getStartTime()), partNames,excludePrograms);
		
		if(events!=null&&events.size()>0){
			eve = events.iterator().next();
		}
		
		if(eve!=null) return eve;
			
		//if active or schedule event not exists ,get historical data	
		List<Object> participants = new ArrayList<Object>();
		for(String name : partNames){
			participants.add(name);
		}
		List<Object> programs = new ArrayList<Object>();
		programs.addAll(excludePrograms);
		List<HistoryEvent> hisList = eventManager.findHisEventByParticipantProgramAndDate(dr.getStartTime(), DateUtil.getNextDay(dr.getStartTime()),participants, programs);
			
		if(hisList!=null&&hisList.size()>0){
			HistoryEvent he = hisList.iterator().next();
			eve = new Event();
            eve.setEventName(he.getEventName());
            eve.setStartTime(he.getStartTime());
            eve.setEndTime(he.getEndTime());
		}
		
		return eve;
	}
	 
	 @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	 private Event getEventByName(String eventName) {
		 
		 	Event event = eventManager.getEventPerf(eventName);
		 	if(event!=null){
		 		event.setActive(true);
			    return event;
		 	}
		 	
	 		HistoryEvent hisEvent = null;
	 		try {
	 			hisEvent = eventManager.getByEventName(eventName);
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
			}
			if(hisEvent!=null){
				event = new Event();
				event.setEventName(hisEvent.getEventName());
				event.setStartTime(hisEvent.getStartTime());
				event.setEndTime(hisEvent.getEndTime());
				event.setActive(false);
		        return event;
			}
			
			return null;
	}

	 

    @javax.jws.WebMethod()
    public static HashMap<PDataSet, List<PDataEntry>> getBaselineDataSetShed(
            List<PDataEntry> dataEntryList, PDataSet dataset, Date start,
            Date end, HashMap<PDataSet, List<PDataEntry>> in) {
        if (dataset == null)
            return in;
        PDataSet pds = new PDataSet();
        pds.setName("BaseLineShed");
        pds.setPeriod(dataset.getPeriod());
        pds.setSync(dataset.isSync());
        pds.setUnit(dataset.getUnit());
        List<PDataEntry> pdes = new ArrayList<PDataEntry>();
        if (dataEntryList != null) {
            boolean endAdded = false;
            boolean startAdded = false;
            for (PDataEntry input : dataEntryList) {
                if (startAdded && endAdded) {
                    break;
                }
                if (input.getTime().getTime() >= end.getTime() && !endAdded) {
                    pdes.add(clone(input));
                    endAdded = true;
                }
                if (input.getTime().getTime() >= start.getTime() && !startAdded) {
                    pdes.add(clone(input));
                    startAdded = true;
                }

            }
        }

        if (pdes != null && pdes.size() > 0) {
            in.put(pds, pdes);
        }

        return in;
    }

    public static HashMap<PDataSet, List<PDataEntry>> getPartDataSetShed(
            List<PDataEntry> dataEntryList, PDataSet dataset, Date start,
            Date end, HashMap<PDataSet, List<PDataEntry>> in) {
        if (dataset == null)
            return in;
        PDataSet pds = new PDataSet();
        // pds.setId(dataset.getUUID());
        // pds.setName(dataset.getName());
        pds.setName("CurrentUsageShed");
        pds.setPeriod(dataset.getPeriod());
        pds.setSync(dataset.isSync());
        pds.setUnit(dataset.getUnit());
        List<PDataEntry> pdes = new ArrayList<PDataEntry>();
        if (dataEntryList != null) {
            boolean endAdded = false;
            boolean startAdded = false;
            for (PDataEntry input : dataEntryList) {
                if (startAdded && endAdded) {
                    break;
                }
                if (input.getTime().getTime() >= end.getTime() && !endAdded) {
                    pdes.add(clone(input));
                    endAdded = true;
                }
                if (input.getTime().getTime() >= start.getTime() && !startAdded) {
                    pdes.add(clone(input));
                    startAdded = true;
                }

            }

        }

        if (pdes != null && pdes.size() > 0) {
            in.put(pds, pdes);
        }

        return in;
    }

    public static PDataEntry clone(PDataEntry in) {
        PDataEntry out = new PDataEntry();

        out.setTime(in.getTime());
        out.setValue(in.getValue());
        out.setValueType(in.getValueType());
        out.setStringValue(in.getStringValue());
        out.setDatasource(in.getDatasource());
        out.setDataSet(in.getDataSet());
        out.setCreationTime(in.getCreationTime());
        out.setActual(in.isActual());

        return out;
    }
    
    private UsageSummary getUsageSummaryFromList(List<PDataEntry> usageList, Date start, Date end){
    	UsageSummary summary = new UsageSummary();
    	if(usageList==null||usageList.isEmpty()) return summary;
    	
    	DateEntrySelectPredicate predicate = new DateEntrySelectPredicate(start.getTime(), end.getTime(), DateEntrySelectPredicate.BETWEEN_START_END, PDataEntry.class,"time");
    	List<PDataEntry> usageDes = (List<PDataEntry>) CollectionUtils.select(usageList, predicate);
    	
    	if(usageDes==null||usageDes.isEmpty()) return summary;
    	
    	double sum = 0;
    	for(PDataEntry entry : usageDes){
    		sum += entry.getValue();
    	}
    	summary.setAverage(sum/usageDes.size());
    	
    	return summary;
    }
    
/*    private UsageSummary getShedSummaryFromList(List<PDataEntry> usageList, List<PDataEntry> baselineList, Date start, Date end){
    	UsageSummary summary = new UsageSummary();
    	if(usageList==null||usageList.isEmpty()||baselineList==null||baselineList.isEmpty()) return summary;
    	
    	DateEntrySelectPredicate predicate = new DateEntrySelectPredicate(start.getTime(), end.getTime());
    	List<PDataEntry> usageDes = (List<PDataEntry>) CollectionUtils.select(usageList, predicate);
    	List<PDataEntry> baseDes = (List<PDataEntry>) CollectionUtils.select(baselineList, predicate);
    	
    	if(usageDes==null||usageDes.isEmpty()||baseDes==null||baseDes.isEmpty()) return summary;
    	
    	double sumUsage = 0;
    	for(PDataEntry entry : usageDes){
    		sumUsage += entry.getValue();
    	}
    	
    	double sumBase = 0;
    	for(PDataEntry entry : baseDes){
    		sumBase += entry.getValue();
    	}
		summary.setAverage(sumBase/baseDes.size()-sumUsage/usageDes.size());
    	
    	return summary;
    }*/
    
    private UsageSummary getShedSummaryForEvent(UsageSummary baseEvent, UsageSummary actualEvent){
    	UsageSummary summary = new UsageSummary();
    	if(baseEvent==null||actualEvent==null||baseEvent.getAverage()==0||actualEvent.getAverage()==0) return summary;
    	
    	summary.setAverage(baseEvent.getAverage()-actualEvent.getAverage());
    	
    	return summary;
    }
    
    public static HashMap<PDataSet, List<PDataEntry>> getEventDataSetShed(List<PDataEntry> dataEntryList, PDataSet dataset, Date start, Date end, HashMap<PDataSet, List<PDataEntry>> in)
    {
        if(dataset == null) return in;
        PDataSet pds = new PDataSet();
        pds.setName("CurrentUsageShed");
        pds.setPeriod(dataset.getPeriod());
        pds.setSync(dataset.isSync());
        pds.setUnit(dataset.getUnit());
        List<PDataEntry> pdes = new ArrayList<PDataEntry>();
     
        PDataEntry startDot = new PDataEntry();
        startDot.setTime(start);
        startDot.setValue(0.0);
        
        PDataEntry endDot = new PDataEntry();
        endDot.setTime(end);
        endDot.setValue(0.0);
        
        pdes.add(startDot);
        pdes.add(endDot);
    	
        in.put(pds, pdes);

        return in;
    }
  

   private static Double getDoubleValue(Double in, NumberFormat nf){
   	
   	if(Double.isNaN(in)) return 0.0;
   	
   	return Double.valueOf(nf.format(in));
   }
   
	private static double calculateTotal(Date date, Calendar calEnd, Date today, double avg) {
		if(Double.isNaN(avg)){
			avg = 0;
		}
	   double total = 0;
	   double hours = (calEnd.getTime().getTime()-DateUtil.stripTime(date).getTime())/3600000.0;

	   if(DateUtils.isSameDay(today, date)){
		   total = avg*hours;
	   }else{
		   total = avg*24;
	   }
	   return total;
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
	
	/**
     * Gets all the descendant data sources
     * @param pDataSource List of descendant data sources
     * @return List of descendant data sources
     */
	private List<String> getExpandedParticipants(String participantName){
    	List<String> result = new ArrayList<String>();
    	result.add(participantName);
    	
    	Participant participant = participantManager.getParticipant(participantName);
    	Set<ProgramParticipant> ppList =  participant.getProgramParticipants();
    	// Using the first one
    	if(ppList != null  && ppList.size() > 0){
    		ProgramParticipant pp = ppList.iterator().next();
    		Set<ProgramParticipant> tree = aggManager.getFlatDescendants(pp);
    		if(tree != null){
    			Iterator<ProgramParticipant> it = tree.iterator();
    			while(it.hasNext()){
    				String pName = it.next().getParticipantName();
    				result.add(pName);	
    			}
    		}
    	}
    
    	return result;
    }
}