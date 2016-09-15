package com.akuacom.pss2.richsite.event;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.pss2.data.usage.UsageSummary;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.utils.lang.DateUtil;

public class EventUsageDataModel implements Serializable {
	private static final long serialVersionUID = -6073862197029218612L;
	
	public EventUsageDataModel() {
		loadValue();
	}
	
	private Double baseValue = 0.0;
	private Double usageValue = 0.0;
	private Double shedValue = 0.0;
	private String titleLabel;
	
	private void loadValue() {
		Map<String, Object> requestMap = 
			FacesContext.getCurrentInstance().getExternalContext().getRequestMap();

		EventDetailDataModel eventDetailDataModel = (EventDetailDataModel) requestMap.get("EventDetailDataModel");
		Event event = eventDetailDataModel.getEvent();
		boolean isIndividual = eventDetailDataModel.isIndividualparticipant();
		
		List<String> participantNames = new ArrayList<String>();
		for (EventParticipant part : event.getEventParticipants()) {
			if (part != null && part.getParticipant() != null 
					&& !part.getParticipant().isClient()) {
				participantNames.add(part.getParticipant().getParticipantName());
			}
		}
		
		DataManager dataManager = (DataManager)  EJBFactory.getBean(DataManager.class);
		PDataSet baselineset = dataManager.getDataSetByName("Baseline");
		
		if (participantNames.size() > 0) {
			UsageDataManager usageDataManager = (UsageDataManager)  EJBFactory.getBean(UsageDataManager.class);
			// currently only support active event, need support historical event in the future
			List<PDataEntry> usage = usageDataManager.findRealTimeEntryListForEvent(event.getEventName(), null, isIndividual, false);
			List<PDataEntry> base = usageDataManager.findBaselineEntryListForEvent(event.getEventName(),baselineset.getUUID(), null,isIndividual, false);
			//findBaselineEntryListForEvent(event.getEventName(), null, false)
			
			Date eventStart =  null;
			Date eventEnd =   null;
			Date today = new Date();
	        eventStart =  event.getStartTime();
	        eventEnd =   event.getEndTime();
	        if(eventEnd==null) eventEnd = DateUtil.getEndOfDay(eventStart);
	       if(!today.after(eventEnd)){
	       	// when an event is in active status, calculate the usage report from event start time to current time
	       	eventEnd = today;
	       }
	       UsageSummary baseEvent = getUsageSummaryFromList(base,eventStart,eventEnd);
	  
	       UsageSummary  actualEvent = getUsageSummaryFromList(usage,eventStart,eventEnd);
	       
	       UsageSummary shedEvent = getShedSummaryForEvent(baseEvent,actualEvent);
	       
		    
	       NumberFormat nf = getNumberFormatter();
		   if(actualEvent!=null){
				this.setUsageValue(getDoubleValue(actualEvent.getAverage(),nf));
		   }
		   
		   if(baseEvent!=null){
		       	this.setBaseValue(getDoubleValue(baseEvent.getAverage(),nf));
		   }
		   
			if(shedEvent!=null){
				this.setShedValue(getDoubleValue(shedEvent.getAverage(),nf));
			}
		}
		Date eventEnd =   event.getEndTime();
		if(eventEnd==null) eventEnd = DateUtil.getEndOfDay(event.getStartTime());
	    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
	    this.setTitleLabel("During Event (through "+format.format(eventEnd)+")");
	}
	
	public void getMatrix(ActionEvent event){
		loadValue();
	}

	
	public Double getBaseValue() {
		if(Double.isNaN(baseValue)){
			return 0.0;
		}
		return baseValue;
	}
	public Double getUsageValue() {
		if(Double.isNaN(usageValue)){
			return 0.0;
		}
		return usageValue;
	}
	public Double getShedValue() {
		if(Double.isNaN(shedValue)){
			return 0.0;
		}
		return shedValue;
	}
	public void setBaseValue(Double baseValue) {
		this.baseValue = baseValue;
	}
	public void setUsageValue(Double usageValue) {
		this.usageValue = usageValue;
	}
	public void setShedValue(Double shedValue) {
		this.shedValue = shedValue;
	}
	public String getTitleLabel() {
		return titleLabel;
	}
	public void setTitleLabel(String titleLabel) {
		this.titleLabel = titleLabel;
	}

	//duplicated with UsageDataServiceBean.java
    public static class DateEntrySelectPredicate implements Predicate {
    	
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
    		if (curTime > startTime&& curTime <= endTime) {
                return true;
            }
    		return false;
    		
    	}
    }
  //duplicated with UsageDataServiceBean.java
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
  //duplicated with UsageDataServiceBean.java
    private UsageSummary getShedSummaryForEvent(UsageSummary baseEvent, UsageSummary actualEvent){
    	UsageSummary summary = new UsageSummary();
    	if(baseEvent==null||actualEvent==null||baseEvent.getAverage()==0||actualEvent.getAverage()==0) return summary;
    	
    	summary.setAverage(baseEvent.getAverage()-actualEvent.getAverage());
    	
    	return summary;
    }
  //duplicated with UsageDataServiceBean.java
	private NumberFormat getNumberFormatter() {
		NumberFormat nf = NumberFormat.getNumberInstance();
	    int maxiFractionDigits = 3;
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(maxiFractionDigits);
		return nf;
	}
	  //duplicated with UsageDataServiceBean.java
   private static Double getDoubleValue(Double in, NumberFormat nf){
	   	
	   	if(Double.isNaN(in)) return 0.0;
	   	
	   	return Double.valueOf(nf.format(in));
	   }
}
