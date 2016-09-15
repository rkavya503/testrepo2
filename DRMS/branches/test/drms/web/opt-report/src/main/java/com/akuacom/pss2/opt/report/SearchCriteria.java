package com.akuacom.pss2.opt.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
	
	private static final String LABEL_EVENT      ="Event";
	
	private static final String LABEL_EVENT_OR_PARTICIPANT="Event or Participant";
	
	private static final String LABEL_PARTICIPANT ="Participant";
	
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
	
	private DateStructure startDate;
	
	private DateStructure endDate;
	
	private String program =PROGRAM_ALL;
	
	private List<ParticipantVO> participants;
	
	private ReportEvent event;
	
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

	public List<ParticipantVO> getParticipants() {
		return participants;
	}

	public List<String> getParticipantNames(){
		if(this.getParticipants()!=null){
			List<String> participantNames = new ArrayList<String>();
			for(ParticipantVO p: getParticipants()){
				participantNames.add(p.getParticipantName());
			}
			return participantNames;
		}
		return Collections.emptyList();
	}
	
	public void setParticipants(List<ParticipantVO> participants) {
		this.participants = participants;
	}
	
	public ReportEvent getEvent() {
		return event;
	}
	
	public Object get(FILTER filter){
		switch(filter){
		case participant:
			return getParticipants();
		case event:
			return getEvent();
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
	
	public String getSearchByLabel(){
		if(this.event==null && (this.participants == null || participants.size()==0))
			return LABEL_EVENT_OR_PARTICIPANT;
		else if(event!=null)
			return LABEL_EVENT;
		else 
			return LABEL_PARTICIPANT;
	}
	
	public Object getFilterByObject(){
		if(event!=null)
			return event;
		else 
			return participants;
	}
	
	public boolean isSearchByParticipant(){
		return this.participants!=null && !participants.isEmpty();
	}
	
	public boolean isSearchByEvent(){
		return this.event!=null;
	}
	
	public void setFilterByObject(Object object){
		event =null;
		participants = null;
		if(object instanceof ParticipantVO){
			participants = new ArrayList<ParticipantVO>();
			participants.add((ParticipantVO) object);
		}
		else if(object instanceof List){
			this.participants = (List<ParticipantVO>) object;
		}else if(object instanceof ReportEvent){
			this.event = (ReportEvent) object;
			//update other filter values 
			this.startDate= new DateStructure(event.getStartTime());
			this.endDate = new DateStructure(event.getEndTime());
			this.program = event.getProgramName();
		}
	}
	
	public String getFilterByObjectName(){
		if(event!=null)
			return "Event:"+ event.getEventName();
		else if(participants!=null){
			String name = "";
			for(ParticipantVO participant:participants){
				if(name.equals("")) name= participant.getParticipantName();
				else name+=","+participant.getParticipantName();
			}
			return LABEL_PARTICIPANT+":"+name;
		}
		return "";
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
	
	private static Date constructDateTime(Date dateOnly,String timeStr){
		DateStructure dateStructure = new DateStructure(dateOnly);
		dateStructure.setTimeStr(timeStr);
		return dateStructure.getTime();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getEndDateTime() == null) ? 0 : getEndDateTime().hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result
				+ ((participants == null) ? 0 : participants.hashCode());
		result = prime * result + ((program == null) ? 0 : program.hashCode());
		result = prime * result
				+ ((getStartDateTime() == null) ? 0 : getStartDateTime().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchCriteria other = (SearchCriteria) obj;
		if (!getEndDateTime().equals(other.getEndDateTime())) 
				return false;
		if (!getStartDateTime().equals(other.getEndDateTime())) 
				return false;
					
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (participants == null) {
			if (other.participants != null)
				return false;
		} else if (!participants.equals(other.participants))
			return false;
		if (program == null) {
			if (other.program != null)
				return false;
		} else if (!program.equals(other.program))
			return false;
		
		return true;
	}
	
}
