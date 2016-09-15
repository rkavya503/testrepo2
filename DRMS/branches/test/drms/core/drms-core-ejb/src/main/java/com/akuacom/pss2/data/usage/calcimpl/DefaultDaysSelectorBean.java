package com.akuacom.pss2.data.usage.calcimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.usage.BaselineConfig;
import com.akuacom.pss2.history.CustomerReportManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.PSS2Properties;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.DateUtil;

@Stateless
public class DefaultDaysSelectorBean implements DaysSelector.R, DaysSelector.L {

	@EJB
	SystemManager.L sysManager;
	@EJB
	CustomerReportManager.L reportManager;
	@EJB(beanName="DataManagerBean")
	DataManager.L dataManager;
	
	protected static final int NUMBEROFDAYS = 10;
    private static final Logger log =
    	Logger.getLogger(DefaultDaysSelectorBean.class);

    
    public DateRange getBaseDateRange(List<PDataSource> dataSourceList, Date date, BaselineConfig bc, Set<Date> holidays, String[] excludedPrograms){
        Calendar todate = new GregorianCalendar();
        todate.setTime(date);
        todate.add(Calendar.DATE, -1);
        
        int conditions = 0;
        if(bc.isExcludeEventDay()){
        	conditions = conditions|AkuaDaySelectorPredicate.ELIMINATE_EVENTDAY;
        }
        if(bc.isExcludeHoliday()){
        	conditions = conditions|AkuaDaySelectorPredicate.ELIMINATE_HOLIDAY;
        }
        if(bc.isWeekendExcluded()){
        	conditions = conditions|AkuaDaySelectorPredicate.ELIMINATE_WEEKEND;
        }
        
        List<Date> validDays = getValidDays(dataSourceList, NUMBEROFDAYS, todate.getTime(), 30, conditions, holidays, excludedPrograms);
        List<Date> excludeDates = getExcludionDays(todate.getTime(), validDays);
        
        DateRange dr = new DateRange();
        dr.setEndTime(todate.getTime());
        dr.setStartTime(validDays.get(validDays.size()-1));
        dr.setExcludedDays(excludeDates);
        
        StringBuffer longDescr = new StringBuffer();
        
        longDescr.append("DateRange: Start: "+validDays.get(validDays.size()-1)+" ");
        longDescr.append("End: "+todate.getTime()+" ");
        longDescr.append("Excludes: ");
        for(Date excludesDate: excludeDates){
        	 longDescr.append(excludesDate+",");
        }
        log.debug(LogUtils.createLogEntry(null, null, "DateRange", longDescr.toString()));

        return dr;
    	
    }
    
    public DateRange getBaseDateRange(List<String> datasourceUUIDs, Date date, BaselineConfig bc)
    {
        Calendar todate = new GregorianCalendar();
        todate.setTime(date);
        todate.add(Calendar.DATE, -1);
        
        int conditions = 0;
        if(bc.isExcludeEventDay()){
        	conditions = conditions|AkuaDaySelectorPredicate.ELIMINATE_EVENTDAY;
        }
        if(bc.isExcludeHoliday()){
        	conditions = conditions|AkuaDaySelectorPredicate.ELIMINATE_HOLIDAY;
        }
        if(bc.isWeekendExcluded()){
        	conditions = conditions|AkuaDaySelectorPredicate.ELIMINATE_WEEKEND;
        }
        
        List<Date> validDays = getValidDays(datasourceUUIDs, NUMBEROFDAYS, todate.getTime(), 30, conditions);
        List<Date> excludeDates = getExcludionDays(todate.getTime(), validDays);
        
        DateRange dr = new DateRange();
        dr.setEndTime(todate.getTime());
        dr.setStartTime(validDays.get(validDays.size()-1));
        dr.setExcludedDays(excludeDates);
        
        StringBuffer longDescr = new StringBuffer();
        
        longDescr.append("DateRange: Start: "+validDays.get(validDays.size()-1)+" ");
        longDescr.append("End: "+todate.getTime()+" ");
        longDescr.append("Excludes: ");
        for(Date excludesDate: excludeDates){
        	 longDescr.append(excludesDate+",");
        }
        log.debug(LogUtils.createLogEntry(null, null, "DateRange", longDescr.toString()));

        return dr;
    }

    
	private List<Date> getValidDays(List<PDataSource> dataSourceList, int size,
			Date date, int offset, int conditions, Set<Date> holidays, String[] excludedPrograms) {
		List<Date> dates = generateDates(date, offset);
		Set<Date> eventdays = null;
		Set<Date> holidaydays = null;
		if((conditions&AkuaDaySelectorPredicate.ELIMINATE_HOLIDAY)!=0){
			holidaydays = holidays;
		}
		
		if((conditions&AkuaDaySelectorPredicate.ELIMINATE_EVENTDAY)!=0){
			eventdays = getExcludeEventDaysByProgram(dataSourceList, date, offset, excludedPrograms);
		}
		
		AkuaDaySelectorPredicate dayPredicate = new AkuaDaySelectorPredicate(conditions,eventdays,holidaydays);

		@SuppressWarnings("unchecked")
		List<Date> validateDays = (List<Date>) AkuaCollectionUtils.select(dates,
				 dayPredicate, size);

		if (validateDays.size() < size) {
			Date toDate = DateUtil.add(dates.iterator().next(), Calendar.DATE, -1);
			validateDays.addAll(getValidDays(dataSourceList,
					size - validateDays.size(), toDate, offset,conditions, holidays, excludedPrograms));
		}

		return validateDays;
	}
    
    
    
	private List<Date> getValidDays(List<String> datasourceUUIDs, int size,
			Date date, int offset, int conditions) {
		List<Date> dates = generateDates(date, offset);
		Set<Date> eventdays = null;
		Set<Date> holidaydays = null;
		if((conditions&AkuaDaySelectorPredicate.ELIMINATE_HOLIDAY)!=0){
			holidaydays = getHolidays(sysManager);
		}
		
		if((conditions&AkuaDaySelectorPredicate.ELIMINATE_EVENTDAY)!=0){
			eventdays = getExcludeEventDaysByProgram(datasourceUUIDs, date, offset);
		}
		
		AkuaDaySelectorPredicate dayPredicate = new AkuaDaySelectorPredicate(conditions,eventdays,holidaydays);

		@SuppressWarnings("unchecked")
		List<Date> validateDays = (List<Date>) AkuaCollectionUtils.select(dates,
				 dayPredicate, size);

		if (validateDays.size() < size) {
			validateDays.addAll(getValidDays(datasourceUUIDs,
					size - validateDays.size(), date, offset,conditions));
		}

		return validateDays;
	}
	
	protected List<Date> generateDates(Date date, int offset) {
		List<Date> exDates = new ArrayList<Date>();
		Calendar fromDate = new GregorianCalendar();
		fromDate.setTime(date);
		offset = 0 - offset;
		fromDate.add(Calendar.DATE, offset);

		Calendar cur = Calendar.getInstance();
		cur.setTime(DateUtil.stripTime(fromDate.getTime()));

		while (!cur.getTime().after(DateUtil.stripTime(date))) {
			exDates.add(cur.getTime());
			cur.add(Calendar.DATE, 1);
		}

		Comparator<Date> comparator = new Comparator<Date>() {

			@Override
			public int compare(Date o1, Date o2) {
				return (0 - o1.compareTo(o2));
			}

		};
		// Order by event start time
		Collections.sort(exDates, comparator);

		return exDates;

	}

	private List<Date> getExcludionDays(Date endDate, List<Date> validDays) {
		List<Date> excludeDates = new ArrayList<Date>();
        Calendar cur = Calendar.getInstance();
	    cur.setTime(DateUtil.stripTime(validDays.get(validDays.size()-1)));
        while(!DateUtil.stripTime(cur.getTime()).after(DateUtil.stripTime(endDate)))
        {
           if(!validDays.contains(cur.getTime())){
        	   excludeDates.add(cur.getTime());
           }
           cur.add(Calendar.DATE, 1);
        }
		return excludeDates;
	}
	
    //todo add 3 functions to update DateRange by eliminate event days, weekend, holiday.
    // Call these three methods from getBaseDateRange based on BaselineConfig
    public DateRange excludeEventDays(DateRange dr)
    {
        return dr;
    }

    public DateRange excludeWeekendDays(DateRange dr)
    {
        return dr;
    }

    public DateRange excludeHoliday(DateRange dr)
    {
        return dr;
    }
    
    private Set<Date> getHolidays(SystemManager sysManager) {
		Set<Date> holidaydays = new HashSet<Date>();
		String holiday = null;
		try {
			holiday = sysManager.getPropertyByName(PSS2Properties.PropertyName.HOLIDAYS.toString()).getStringValue();
		} catch (EntityNotFoundException ignore) {}
		if(holiday!=null&&holiday.trim().length()>0){
			String[] holidayArray = holiday.split(",");
			
			for(String day: holidayArray){
				holidaydays.add(DateUtil.stripTime(DateUtil.parse(day,"MM/dd/yyyy")));
			}
		}
		
		return holidaydays;
	}
    
	private Set<Date> getExcludeEventDaysByProgram(
			List<PDataSource> dataSourceList, Date date, int offset, String[] excludedPrograms) {
		
		Calendar fromDate = new GregorianCalendar();
		fromDate.setTime(date);
		offset = 0 - offset;
		fromDate.add(Calendar.DATE, offset);
		Date startdate = DateUtil.getStartOfDay(fromDate.getTime());
		Date endDate = DateUtil.endOfDay(date);

		Set<Date> exDates = new HashSet<Date>();

		List<String> participantIds = new ArrayList<String>();
		for(PDataSource dSource: dataSourceList){
			participantIds.add(dSource.getOwnerID());
		}
		
		List<com.akuacom.pss2.event.Event> events = reportManager.getEventListByParticipantAndProgram(
				participantIds, excludedPrograms, startdate, endDate);
		for (com.akuacom.pss2.event.Event ev : events) {

			if (DateUtils.isSameDay(ev.getStartTime(), ev.getEndTime())) {
				exDates.add(DateUtil.stripTime(ev.getStartTime()));
			} else {
				Calendar cur = Calendar.getInstance();
				cur.setTime(DateUtil.stripTime(ev.getStartTime()));
				
		        // compareTo(when) > 0		         
		        // if and only if when is a Calendar instance. Otherwise, the method returns false.
				while (!cur.after(DateUtil.stripCalendarTime(ev.getEndTime()))) {
					exDates.add(cur.getTime());
					cur.add(Calendar.DATE, 1);
				}
			}

		}

		return exDates;
		
	}

    
	private Set<Date> getExcludeEventDaysByProgram(
			List<String> datasourceUUIDs, Date date, int offset) {
		Calendar fromDate = new GregorianCalendar();
		fromDate.setTime(date);
		offset = 0 - offset;
		fromDate.add(Calendar.DATE, offset);
		Date startdate = fromDate.getTime();

		Set<Date> exDates = new HashSet<Date>();

		String exludePrograms = null;
		try {
			exludePrograms = sysManager.getPropertyByName(PSS2Properties.PropertyName.EXCLUDED_PROGRAMS_FOR_BASELINE.toString()).getStringValue();
		} catch (EntityNotFoundException ignore) {}
		
		List<String> participantIds = dataManager
				.getDataSourcesById(datasourceUUIDs);
		// Do nothing when there isn't any program configured as exception 
		String[] programs = null;
		if (!(exludePrograms == null || exludePrograms.trim().equals(""))) {
			programs = exludePrograms.split(",");
		}

		List<com.akuacom.pss2.event.Event> events = reportManager.getEventListByParticipantAndProgram(
				participantIds, programs, startdate, date);
		for (com.akuacom.pss2.event.Event ev : events) {

			if (DateUtils.isSameDay(ev.getStartTime(), ev.getEndTime())) {
				exDates.add(DateUtil.stripTime(ev.getStartTime()));
			} else {
				Calendar cur = Calendar.getInstance();
				cur.setTime(DateUtil.stripTime(ev.getStartTime()));

				while (!cur.after(DateUtil.stripTime(ev.getEndTime()))) {
					exDates.add(cur.getTime());
					cur.add(Calendar.DATE, 1);
				}
			}

		}

		return exDates;

	}
}
