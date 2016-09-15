package com.akuacom.pss2.customer.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.akuacom.common.DateStructure;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.history.vo.ParticipantVO;
import com.akuacom.pss2.history.vo.ReportEvent;
import com.akuacom.utils.DateUtil;

public class SearchCriteria implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 8081280233232757751L;
	
	public static final String PROGRAM_ALL="ALL";
	public static final String PROGRAM_ALL_LABEL="---------- ALL ----------";
	
	public static enum FILTER {
		participant,
		event,
		startDate,
		startTime,
		endDate,
		endTime,
		program
	}
	
	public SearchCriteria(){
		Date date = new Date(System.currentTimeMillis() - DateUtil.MSEC_IN_DAY);
		startDate = new DateStructure(date);
		startDate.setTimeStr("00:00");
		
		endDate = new DateStructure(date);
		endDate.setTimeStr("23:59");
	}
	
	private String aggParticipantName;
	
	private List<ParticipantVO> participants;
	
	private DateStructure startDate;
	
	private DateStructure endDate;
	
	private String program =PROGRAM_ALL;
	
	
	public Date getStartDate(){
		return startDate.getDateOnly();
	}
	
	public Date getEndDate(){
		return endDate.getDateOnly();
	}
	
	public Date getStartOfStartDate(){
		return DateUtil.getStartOfDay(getStartDate());
	}
	
	public Date getEndOfEndDate(){
		return DateUtil.getEndOfDay(getEndDate());
	}
	
	public void setStartDate(Date date){
		startDate.setDateOnly(date);
	}
	
	public void setEndDate(Date date){
		endDate.setDateOnly(date);
	}
	
	public String getStartTime(){
		return startDate.getTimeStr();
	}
	
	public void setStartTime(String timeStr){
		startDate.setTimeStr(timeStr);
	}
	
	public String getEndTime(){
		return endDate.getTimeStr();
	}
	
	public void setEndTime(String timeStr){
		endDate.setTimeStr(timeStr);
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}
	
	public Object get(FILTER filter){
		switch(filter){
		case participant:
			return getParticipantName();
		case startDate:
			return getStartDate();
		case endDate:
			return getEndDate();
		case startTime:
			return getStartTime();
		case endTime:
			return getEndTime();
		case program:
			return getProgram();
		}
		return null;
	}
	
	public DateRange getDateTimeRange(){
		DateRange range = new DateRange();
		range.setStartTime(getStartDateTime());
		range.setEndTime(getEndDateTime());
		return range;
	}
	
	public DateRange getDateRange(){
		DateRange range = new DateRange();
		range.setStartTime(getStartDate());
		range.setEndTime(getEndDate());
		return range;
	}
	
	public static boolean isSame(SearchCriteria sc1,SearchCriteria sc2,FILTER[] filters){
		if(sc1==sc2) return true;
		for(FILTER filter:filters){
			Object obj1 = sc1.get(filter);
			Object obj2 = sc2.get(filter);
			if (obj1 == null) {
				if (obj2 != null)
					return false;
			} else if (!obj1.equals(obj2))
				return false;
		}
		return true;
	}
	
	
	public SearchCriteria copy(){
		  try {
			return (SearchCriteria) BeanUtils.cloneBean(this);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Date getStartDateTime(){
		return constructDateTime(getStartDate(),getStartTime());
	}
	
	public Date getEndDateTime(){
		return constructDateTime(getEndDate(),getEndTime());
	}
	
	
	public String getParticipantName() {
		return aggParticipantName;
	}

	public void setParticipantName(String aggParticipantName) {
		this.aggParticipantName = aggParticipantName;
	}

	private static Date constructDateTime(Date dateOnly,String timeStr){
		DateStructure dateStructure = new DateStructure(dateOnly);
		dateStructure.setTimeStr(timeStr);
		return dateStructure.getTime();
	}
	
	public List<String> getParticipantNames(){
		if(this.participants!=null){
			List<String> parts = new ArrayList<String>();
			for(ParticipantVO p:this.participants){
				parts.add(p.getParticipantName());
			}
			return parts;
		}else
			return Arrays.asList(aggParticipantName);
	}
	
	public List<ParticipantVO> getParticipants(){
		return participants;
	}
	
	public void setParticipants(List<ParticipantVO> participants) {
		this.participants = participants;
	}
	
	public void setFilterByObject(List<ParticipantVO> parts){
		this.participants =parts;
	}
	
	public String getFilterByObjectName(){
		 if(participants!=null){
			String name = "";
			for(ParticipantVO participant:participants){
				if(name.equals("")) name= participant.getParticipantName();
				else name+=","+participant.getParticipantName();
			}
			return "Participants"+":"+name;
		}
		 return "";
	}
}
