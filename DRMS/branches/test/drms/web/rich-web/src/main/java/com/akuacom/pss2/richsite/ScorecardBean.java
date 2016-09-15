package com.akuacom.pss2.richsite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramEAO;
import com.akuacom.pss2.program.DRwebsite.PREvent;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.system.SystemManager;

public class ScorecardBean implements Serializable{

	/** The serialVersionUID */
	private static final long serialVersionUID = 3831750127717274433L;
	
	/** The log */
	private static final Logger log = Logger.getLogger(ScorecardBean.class);

	private String programClass;
	
	private Date startDate;
	
	private Date endDate;
	
	private List<PREvent> eventList;
	
	private ArrayList<SelectItem> programClassList;
	
	private String dateFormat;
	
	private static String PROGRAM_DEMAND_LIMITING="Demand Limiting Program";
	/**
	 * Constructor
	 */
	public ScorecardBean() {
		try {
			initialize();
		} 
		catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * Function for initialize the ScorecardBean
	 */
	private void initialize() {
		eventList = new ArrayList<PREvent>();
		
		programClassList = new ArrayList<SelectItem>();
		ProgramEAO pEao = EJBFactory.getBean(ProgramEAO.class);
		List<Program> programs = pEao.getAll();
		
		ArrayList<String> tmp = new ArrayList<String>();
		
		
		
        SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
        dateFormat=systemManager.getPss2Features().getDateFormat();
        
        for (Program p : programs) {
			if (p.getProgramClass() != null && !p.getProgramName().equals(TestProgram.PROGRAM_NAME)
					&& !tmp.contains(p.getProgramClass())) {
				if(!p.getProgramName().equalsIgnoreCase(PROGRAM_DEMAND_LIMITING)){
					tmp.add(p.getProgramClass());
					programClassList.add(new SelectItem(p.getProgramClass(), p.getProgramClass()));
				}
			}
		}
	}
	
	private boolean isDataValid() {
		boolean res = true;
		
		//check for null and start before end
		if (startDate == null || endDate == null) {
			return false;
		}
		
		if (endDate.before(startDate)) {
			return false;
		}
		return res;
	}
	
	public String searchHistoryAction() {
		eventList = new ArrayList<PREvent>();
	    if (!isDataValid()) {
			FDUtils.addMsgError("Report dates are not valid.");
			return "searchError";
	    }
		
	    try {
	    	ReportManager rm = EJBFactory.getBean(ReportManager.class);
	    	eventList = rm.getEventListByProgramClassWithHistoryEvent(programClass, startDate, endDate);
	    } catch (AppServiceException asex) {
	    	FDUtils.addMsgError("Failed to get event list: " + asex.getMessage());
	    }
        
		return "searchHistory";
	}

	public List<SelectItem> getAvailableProgramClasses() {
		return programClassList;
	}

	public String getProgramClass() {
		return programClass;
	}

	public void setProgramClass(String programClass) {
		this.programClass = programClass;
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public long getStartDateAsTime() {
		if (startDate == null) 
			return -1;
	   
		return startDate.getTime();
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
	public long getEndDateAsTime() {
		if (endDate == null) 
			return -1;
		return endDate.getTime();
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<PREvent> getEventList() {
		return eventList;
	}

	public void setEventList(List<PREvent> eventList) {
		this.eventList = eventList;
	}

	public String getDateFormat() {
		return dateFormat;
	}
	
}
