package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.faces.event.ActionEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.time.DateUtils;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSetEAO;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.pss2.data.usage.UsageSummary;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.SystemManagerBean;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.utils.BuildProperties;
import com.akuacom.utils.lang.DateUtil;
import javax.faces.event.ActionEvent;

public class IRRUsage implements Serializable {
	private static final long serialVersionUID = 7809025948134780131L;

	private String participantName;// = FDUtils.getParticipantName();
	
	private Date date;

	private String pdWsURL;

	private String selectDate;
	
	private boolean showRawData;
	
	private boolean disableAggregatedData;
	
	private String disclaimer;
	
	private List<ReportSummary> reports;
	
	private boolean individualparticipant;
	
	private DateRange dateRange;

	public String getPdWsURL() {
		return "../../pss2-drms-core-ejb-" + new BuildProperties().getVersion()
				+ "/IRRUsageDataServicerBean";
	}

	public void setPdWsURL(String pdWsURL) {
		this.pdWsURL = pdWsURL;
	}

	public String getSelectDate() {
		return DateUtil.format(getDate(), "yyyy-MM-dd");
	}

	public void setSelectDate(String selectDate) {
		this.selectDate = selectDate;
	}

	public Date getDate() {
		if(null==date){
			return new Date();
		}
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getParticipantName() {
		return FDUtils.getParticipantName();
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public void refresh(ActionEvent event) {
		System.out.print("1");
	}
	public void refresh() {
		System.out.print("2");
	}
	
	public List<ReportSummary> getReports(ActionEvent event){
		return getReports();
	}
	
	public List<ReportSummary> getReports() {
		//TODO: reset first
		List<ReportSummary> result = new ArrayList<ReportSummary>();
		UsageDataManager dataManager = (UsageDataManager)  EJBFactory.getBean(UsageDataManager.class);
		PDataSetEAO dataSetEAO =(PDataSetEAO)  EJBFactory.getBean(PDataSetEAO.class);
		
		PDataSet usageSet = dataSetEAO.getDataSetByName("Usage");
		PDataSet baselineSet = dataSetEAO.getDataSetByName("Baseline");
		
		List<PDataEntry> usage = null;
		boolean individual = false;
		if(disableAggregatedData){
			individual = true;
		}else{
			individual = isIndividualparticipant();
		}
		usage = dataManager.findRealTimeEntryListForParticipant(getParticipantName(), usageSet.getUUID(), getDate(),individual, false ,false);
			
         Date firstUsageTime = null;
         Date lastUsageTime = null;
         if (usage != null && usage.size() > 0) {
             firstUsageTime = usage.get(0).getTime();
             lastUsageTime = usage.get(usage.size()-1).getTime();
         }
        if (firstUsageTime == null) {
        	firstUsageTime = DateUtil.getStartOfDay(getDate());                   
        }    
        if (lastUsageTime == null) {
           lastUsageTime = DateUtil.getStartOfDay(getDate());                   
        }         
         
		List<PDataEntry> base = dataManager.findBaselineEntryListForParticipant(getParticipantName(), getDate(), baselineSet.getUUID(),individual,false);
		
        ReportSummary eveSum = new ReportSummary();
        ReportSummary daySum = new ReportSummary();

        Date now = new Date();
        if (DateUtils.isSameDay(now, getDate())) {// last time is now
            //SimpleDateFormat format = new SimpleDateFormat("hh:mm a");Put the time in 24hour format hh:mm.
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            daySum.setName("Today (through " + format.format(lastUsageTime) + ")");
            eveSum.setName("During Event (through " + format.format(lastUsageTime) + ")");
        } else {
            daySum.setName("Entire Day");
            eveSum.setName("During Event");
        }
                      
		   //show the same calculated data based on actual usage data received and fabricated data if missing data exists 
		   // regardless [Show Raw Data]is checked or not.
		   
		   UsageSummary actual = this.getUsageSummaryFromList(usage, firstUsageTime, lastUsageTime);		   
		   if(actual!=null)
		   {
			   daySum.setActualAvg(convertNumber(actual.getAverage()));
			   daySum.setActualTotal(calculateTotal(firstUsageTime, lastUsageTime, convertNumber(actual.getAverage())));
		   }
		   UsageSummary baseSummary = this.getUsageSummaryFromList(base, firstUsageTime, lastUsageTime);
		   if(base!=null){
			   daySum.setBaseAvg(convertNumber(baseSummary.getAverage()));
			   daySum.setBaseTotal(calculateTotal(firstUsageTime, lastUsageTime, convertNumber(baseSummary.getAverage())));
		   }
		  // UsageSummary shed = this.getShedSummaryFromList(vo.getUsagelineList(), vo.getBaselineList(), start, end);
		   if(actual!=null&&baseSummary!=null&&baseSummary.getAverage()>0&&actual.getAverage()>0){
			   	daySum.setShedAvg(convertNumber(daySum.getBaseAvg()-daySum.getActualAvg()));
				daySum.setShedTotal(calculateTotal(firstUsageTime, lastUsageTime, daySum.getShedAvg()));
		   }
		   
		   Event eve = null;
		   if(individual){
			   eve = dataManager.getEventByDateAndParticipant(getParticipantName(), getDate());
		   }else{
			   eve = dataManager.getEventByDateAndAggreagator(getParticipantName(), getDate());
		   }
		   
		   UsageSummary baseEvent = null;
		   UsageSummary shedEvent = null;
		   UsageSummary actualEvent = null;
		
		   Date eventStart =  null;
		   Date eventEnd =   null;
		   if(eve != null)
		   {
		       eventStart =  eve.getStartTime();
		       eventEnd =   eve.getEndTime();
		       if(!now.after(eventEnd)){
		       	// when an event is in active status, calculate the usage report from event start time to current time
		       	eventEnd = now;
		       }
		       if(!individual){
		    	   List<String> eventContributed = new ArrayList<String>();
					Set<EventParticipant> eps =eve.getEventParticipants();
					for(EventParticipant ep : eps){
						if((ep.getOptOutTime()==null||ep.getOptOutTime().after(eventStart))&&(!ep.getParticipant().isClient())){
							eventContributed.add(ep.getParticipant().getParticipantName());
						}
					}
					
					List<String> aggContributed = dataManager.findContributedParticipantNames(getParticipantName(), getDate());
					aggContributed.retainAll(eventContributed);//exclude those participants who doesn't attend this activity
					
					usage = dataManager.findRealTimeUsageDataEntryList(usageSet.getUUID(),aggContributed,getDate());
					base = dataManager.findForecastUsageDataEntryList(baselineSet.getUUID(),aggContributed,getDate());
		    	   
		       }
		       
		       baseEvent = getUsageSummaryFromList4Event(base,eventStart,eventEnd);
		       actualEvent = getUsageSummaryFromList4Event(usage,eventStart,eventEnd);
		       shedEvent = getShedSummaryForEvent(baseEvent,actualEvent);
		    }
		
		   if(actualEvent!=null){
			   eveSum.setActualAvg(convertNumber(actualEvent.getAverage()));
			   eveSum.setActualTotal(calculateEventDurationTotal(eventStart, eventEnd, now, convertNumber(actualEvent.getAverage())));
		   }
		   
		   if(baseEvent!=null){
			   eveSum.setBaseAvg(convertNumber(baseEvent.getAverage()));
			   eveSum.setBaseTotal(calculateEventDurationTotal(eventStart, eventEnd, now, convertNumber(baseEvent.getAverage())));
		   }
		   
			if(shedEvent!=null){
				eveSum.setShedAvg(convertNumber(shedEvent.getAverage()));
				eveSum.setShedTotal(calculateEventDurationTotal(eventStart, eventEnd, now, convertNumber(shedEvent.getAverage())));
			}
		
		//*********************************************************************************************************
		
		result.add(daySum);
		result.add(eveSum);
		reports = result;
		
		return reports;
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

	public void setReports(List<ReportSummary> reports) {
		this.reports = reports;
	}
	//duplicate with usageDataServicebean
    private UsageSummary getUsageSummaryFromList(List<PDataEntry> usageList, Date start, Date end){
    	UsageSummary summary = new UsageSummary();
    	if(usageList==null||usageList.isEmpty()) return summary;
    	
    	DateEntrySelectPredicate predicate = new DateEntrySelectPredicate(start.getTime(), end.getTime());
    	List<PDataEntry> usageDes = (List<PDataEntry>) CollectionUtils.select(usageList, predicate);
    	
    	if(usageDes==null||usageDes.isEmpty()) return summary;
    	
    	double sum = 0;
    	for(PDataEntry entry : usageDes){
    		sum += entry.getValue();
    	}
    	summary.setAverage(sum/usageDes.size());
    	
    	return summary;
    }
    
    private UsageSummary getUsageSummaryFromList4Event(List<PDataEntry> usageList, Date start, Date end){
    	UsageSummary summary = new UsageSummary();
    	if(usageList==null||usageList.isEmpty()) return summary;
    	
    	DateEntrySelectPredicate4Event predicate = new DateEntrySelectPredicate4Event(start.getTime(), end.getTime());
    	List<PDataEntry> usageDes = (List<PDataEntry>) CollectionUtils.select(usageList, predicate);
    	
    	if(usageDes==null||usageDes.isEmpty()) return summary;
    	
    	double sum = 0;
    	for(PDataEntry entry : usageDes){
    		sum += entry.getValue();
    	}
    	summary.setAverage(sum/usageDes.size());
    	
    	return summary;
    }
    //duplicate with usageDataServicebean
    class DateEntrySelectPredicate implements Predicate {
    	
    	private long startTime;
    	private long endTime;
    	public DateEntrySelectPredicate(long startTime, long endTime){
    		this.startTime = startTime;
    		this.endTime = endTime;
    	}

    	@Override
    	public boolean evaluate(Object object) {
    		PDataEntry entry = (PDataEntry) object;
    		long curTime = entry.getTime().getTime();
    		if (curTime >= startTime&& curTime <= endTime) {
                return true;
            }
    		return false;
    		
    	}
    }
    
 class DateEntrySelectPredicate4Event implements Predicate {
    	
    	private long startTime;
    	private long endTime;
    	public DateEntrySelectPredicate4Event(long startTime, long endTime){
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
    
    // Calculates the total for an hourly average across a timespan.  
	private static double calculateTotal(Date start, Date end, double avg) {
		if(Double.isNaN(avg)){
			avg = 0;
		}
	   double total = 0;
	   double hours = 0.0;
	   if(DateUtils.isSameDay(start, new Date())){
		   hours = (end.getTime()-start.getTime())/3600000.0;
	   }else{
		   hours = 24;
	   }
       total = avg*hours;

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
	
    private UsageSummary getShedSummaryForEvent(UsageSummary baseEvent, UsageSummary actualEvent){
    	UsageSummary summary = new UsageSummary();
    	if(baseEvent==null||actualEvent==null||baseEvent.getAverage()==0||actualEvent.getAverage()==0) return summary;
    	
    	summary.setAverage(convertNumber(baseEvent.getAverage())-convertNumber(actualEvent.getAverage()));
    	
    	return summary;
    }

	public boolean isIndividualparticipant() {
		UsageDataManager um = (UsageDataManager)  EJBFactory.getBean(UsageDataManager.class);
        individualparticipant = um.isIndividualparticipant(getParticipantName());
        
		return individualparticipant;
	}

	public void setIndividualparticipant(boolean individualparticipant) {
		this.individualparticipant = individualparticipant;
	}

	public String getDisclaimer() {
		if (disclaimer==null || disclaimer.isEmpty()) {
	        SystemManager systemManager = EJBFactory.getBean(SystemManagerBean.class);
	        disclaimer=systemManager.getPss2Properties().getTelemetryDisclaimer();
		}
        
		return disclaimer;
	}

	public boolean isShowRawData() {
		return showRawData;
	}

	public void setShowRawData(boolean showRawData) {
		this.showRawData = showRawData;
	}

	public DateRange getDateRange() {
		UsageDataManager um = (UsageDataManager)  EJBFactory.getBean(UsageDataManager.class);
		return um.getPickableDateRange(getParticipantName(),isIndividualparticipant());
	}

	public void setDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
	}

	public boolean isDisableAggregatedData() {
		return disableAggregatedData;
	}

	public void setDisableAggregatedData(boolean disableAggregatedData) {
		this.disableAggregatedData = disableAggregatedData;
	}

}
