package com.akuacom.pss2.richsite.report;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.query.ProgramSummary;
import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.report.entities.ClientShedStrategy;
import com.akuacom.pss2.report.entities.ClientShedStrategyUtil;
import com.akuacom.pss2.report.entities.RTPShedStrategyReportSummary;
import com.akuacom.pss2.richsite.FDUtils;
import com.akuacom.pss2.util.CBPUtil;

public class ClientShedStrategyTableBackingBean {
	private static final String BIP_TYPE_CLASS = "com.akuacom.pss2.program.bip.BIPProgramEJB";
	private static final String CPP_TYPE_CLASS = "com.akuacom.pss2.program.cpp.CPPProgramEJB";
	private static final String CBP_TYPE_CLASS = "com.akuacom.pss2.program.cbp.CBPProgramEJB";
	private static final String DPP_TYPE_CLASS = "com.akuacom.pss2.program.dbp.DBPNoBidProgramEJB";
	private static final String RTP_TYPE_CLASS = "com.akuacom.pss2.program.scertp2013.SCERTPProgramEJB2013";

	/** The log */
	private static final Logger log = Logger.getLogger(ClientShedStrategyTableBackingBean.class);
	/** program items */
	private List<SelectItem> programItems;
	private String searchProgramName;
	private ReportManager report;
	private List<ClientShedStrategyBackingBean> shedStrategyList = new ArrayList<ClientShedStrategyBackingBean>();

	private NativeQueryManager queryManager;
	private List<RTPShedStrategyEntry> rtpShedStrategyList = new ArrayList<RTPShedStrategyEntry>();
	private boolean rtpProgram=false;
	
	private boolean resultEmptyFlag = true;
	/**
	 * Constructor
	 */
	public ClientShedStrategyTableBackingBean() {
		init();
	}

	private void init() {
		// initialize the program items
		// load programs
		NativeQueryManager query = (NativeQueryManager) EJBFactory.getBean(NativeQueryManager.class);
		List<ProgramSummary> programs = Collections.emptyList();
		try {
			programs = query.getProgramSummary(null);

		} catch (Exception e) {
			FDUtils.addMsgError("Internal Error!");
		}
		
		programItems = new ArrayList<SelectItem>();
		programItems.add(new SelectItem("Select", "Select"));
		
		List<String> programNameList = new ArrayList<String>();
		for(ProgramSummary program:programs){
			programNameList.add(program.getProgramName());
//			programItems.add(new SelectItem(program.getProgramName(),program.getProgramName()));
		}
		if(isEnableCBPConsolidation()){
			programNameList = CBPUtil.transferList(programNameList);
		}
		for(String program:programNameList){
			programItems.add(new SelectItem(program,program));
		}
	}
	public String searchFilterAction(){
		if(getSearchProgramName()==null||getSearchProgramName().equalsIgnoreCase("")||getSearchProgramName().equalsIgnoreCase("Select")){
			return "";
		}else{
			List<ClientShedStrategy> result = new ArrayList<ClientShedStrategy>();
			ProgramManager pm = (ProgramManager) EJBFactory.getBean(ProgramManagerBean.class);
			Program program = pm.getProgramOnly(getSearchProgramName());
			if (program != null) {
				String programClass = program.getClassName();
				if (CPP_TYPE_CLASS.equalsIgnoreCase(programClass) || CBP_TYPE_CLASS.equalsIgnoreCase(programClass) || BIP_TYPE_CLASS.equalsIgnoreCase(programClass)) {
					this.rtpProgram=false;
					result = getReport().getClientShedStrategysForCPP(getSearchProgramName());
					shedStrategyList = transfer(result);
				} else if (DPP_TYPE_CLASS.equalsIgnoreCase(programClass)) {
					this.rtpProgram=false;
					result = getReport().getClientShedStrategysForDBP(getSearchProgramName());
					shedStrategyList = transfer(result);
				} else if (RTP_TYPE_CLASS.equalsIgnoreCase(programClass)) {
					this.rtpProgram=true;
					this.rtpShedStrategyList=this.getRTPShedStrategyEntry(getSearchProgramName());
				} else {

				}
			}
		}
		
		return "";
	}

	private List<RTPShedStrategyEntry> getRTPShedStrategyEntry(String programName) {
		List<RTPShedStrategyEntry> result=new ArrayList<RTPShedStrategyEntry>();
		
		try {
			SimpleDateFormat format=new SimpleDateFormat("kkmm");
			Calendar cal=Calendar.getInstance();
			cal.set(Calendar.MINUTE, 0);
			
			List<RTPShedStrategyReportSummary> summary=this.getQueryManager().getRTPShedStrategyReport(programName);
			for (RTPShedStrategyReportSummary client:summary) {
				for (int i=0;i<24;i++) {
					RTPShedStrategyEntry entry=new RTPShedStrategyEntry();
					entry.setAccount(client.getAccount());
					entry.setClient(client.getClient());
					entry.setEnroll(client.getEnroll());
					entry.setExtremelyHotSummerWeekday(client.getExtremelyHotSummerWeekdayList().get(i));
					entry.setHighCostWeekend(client.getHighCostWeekendList().get(i));
					entry.setHighCostWinterWeekday(client.getHighCostWinterWeekdayList().get(i));
					entry.setHotSummerWeekday(client.getHotSummerWeekdayList().get(i));
					entry.setLowCostWeekend(client.getLowCostWeekendList().get(i));
					entry.setLowCostWinterWeekday(client.getLowCostWinterWeekdayList().get(i));
					entry.setMildSummerWeekday(client.getMildSummerWeekdayList().get(i));
					entry.setModerateSummerWeekday(client.getModerateSummerWeekdayList().get(i));
					entry.setParent(client.getParent());
					entry.setParticipant(client.getParticipant());
					entry.setProgram(client.getProgram());
					entry.setVeryHostSummerWeekday(client.getVeryHostSummerWeekdayList().get(i));
					
					cal.set(Calendar.HOUR_OF_DAY, i+1);
					entry.setStartTime(format.format(cal.getTime()));
					
					result.add(entry);
				}
			}
		} catch (SQLException e) {
			log.info("Failed to get RTP Shed Strategy report: " + e.getMessage());
		}
		
		return result;
	}

	/**
	 * Function for export clients info to the excel.
	 * 
	 * @throws IOException
	 */
	public void exportToExcel() throws IOException {
		log.info("ClientShedStrategyTableBackingBean generating the shed strategy report...");
		//String program = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("program");
		//List<ClientShedStrategy> list = retrieveData(program);
		String filename = "clientShedStrategy.csv";
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) fc
				.getExternalContext().getResponse();
		response.reset();
		response.addHeader("cache-control", "must-revalidate");
		response.setContentType("application/octet_stream");
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ filename + "\"");
		String result = generateContent();
		response.getWriter().print(result);
		fc.responseComplete();
		log.info("The shed strategy report has been export.");
	}

	private String generateRTPContent() {
		StringBuffer sb = new StringBuffer(
				"Client,Program,Enabled,Participant,Account Number,Parent,End Time," +
				"EXTREMELY HOT SUMMER WEEKDAY,VERY HOT SUMMER WEEKDAY," +
				"HOT SUMMER WEEKDAY,MODERATE SUMMER WEEKDAY,MILD SUMMER WEEKDAY,HIGH COST WINTER WEEKDAY," +
				"LOW COST WINTER WEEKDAY,HIGH COST WEEKEND,LOW COST WEEKEND,\n");

		for (RTPShedStrategyEntry client : rtpShedStrategyList) {
			sb.append(client.getClient() + ",");
			sb.append(client.getProgram() + ",");
			sb.append(client.getEnroll() + ",");
			sb.append(client.getParticipant() + ",");
			sb.append(client.getAccount() + ",");
			sb.append(client.getParent() + ",");
			sb.append(client.getStartTime()+",");
			sb.append(client.getExtremelyHotSummerWeekday()+",");
			sb.append(client.getVeryHostSummerWeekday()+",");
			sb.append(client.getHotSummerWeekday()+",");
			sb.append(client.getModerateSummerWeekday()+",");
			sb.append(client.getMildSummerWeekday()+",");
			sb.append(client.getHighCostWinterWeekday()+",");
			sb.append(client.getLowCostWinterWeekday()+",");
			sb.append(client.getHighCostWeekend()+",");
			sb.append(client.getLowCostWeekend()+",");
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Private function for generate the excel file content.
	 * 
	 * @return
	 */
	private String generateContent() {
		if (this.rtpProgram) {
			return generateRTPContent();
		}
		
		String result = "";
		StringBuffer sb = new StringBuffer(
				"Client,Program,Enabled,Participant,Account Number,Parent,Season,End Time,Moderate,High,\n");

		for (ClientShedStrategyBackingBean client : shedStrategyList) {
			
					sb.append(client.getClient() + ",");
					String program = client.getProgram();
					if(isEnableCBPConsolidation()&&CBPUtil.isCBPConsolidationProgram(program)){
						program = "CBP";
					}
					sb.append(program + ",");
					sb.append(client.getEnroll() + ",");
					sb.append(client.getParticipant() + ",");
					sb.append(client.getAccount() + ",");
					sb.append(client.getParent() + ",");
					sb.append(client.getSeason() + ",");
					sb.append(client.getStartTime()+",");
					sb.append(client.getModerateValue()+",");
					sb.append(client.getHighValue()+",");
					sb.append("\n");
				
		}
		result = sb.toString();
		return result;
	}
	
	private List<ClientShedStrategyBackingBean> transfer(List<ClientShedStrategy> list){
		List<ClientShedStrategyBackingBean> result = new ArrayList<ClientShedStrategyBackingBean>();
		for (ClientShedStrategy client : list) {
			if (client.isRtpFlag()) {
				if (client.getRtpType().equalsIgnoreCase("SIMPLE")) {
					
					String moderateValue = ClientShedStrategyUtil.getRTPModerateValue(client.getAllValue());
					String highValue = ClientShedStrategyUtil.getRTPHighValue(client.getAllValue());
					if(!(moderateValue.trim().equalsIgnoreCase("")&&highValue.trim().equalsIgnoreCase(""))){
						ClientShedStrategyBackingBean bean = new ClientShedStrategyBackingBean();
						bean.setClient(client.getClient());
						String program = client.getProgram();
//						if(isEnableCBPConsolidation()&&CBPUtil.isCBPConsolidationProgram(program)){
//							program = "CBP";
//						}
						bean.setProgram(program);
						bean.setEnroll(client.getState());
						bean.setAccount(client.getAccountNumber());
						bean.setParent(client.getParent());
						bean.setSeason(client.getSeason());
						bean.setParticipant(client.getParticipant());
						bean.setStartTime("");
						bean.setModerateValue(moderateValue);
						bean.setHighValue(highValue);
						result.add(bean);	
					}
				} else {
					boolean validationFlag = false;
					//Validation
					for (int i = 0; i < 24; i++) {
						String value = client.getIntervalValueList().get(i);
						String m = ClientShedStrategyUtil.getRTPModerateValue(value);
						String v = ClientShedStrategyUtil.getRTPHighValue(value);
						
						if((m!=null&&(!m.trim().equalsIgnoreCase("")))||(v!=null&&(!v.trim().equalsIgnoreCase("")))){
							validationFlag = true;
							break;
						}
					}
					if(validationFlag){
						for (int i = 0; i < 24; i++) {
							ClientShedStrategyBackingBean bean = new ClientShedStrategyBackingBean();
							bean.setClient(client.getClient());
							String program = client.getProgram();
							if(isEnableCBPConsolidation()&&CBPUtil.isCBPConsolidationProgram(program)){
								program = "CBP";
							}
							bean.setProgram(program);
							bean.setEnroll(client.getState());
							bean.setAccount(client.getAccountNumber());
							bean.setParent(client.getParent());
							bean.setSeason(client.getSeason());
							bean.setParticipant(client.getParticipant());
							int j = i + 1;
							if (j < 10) {
								bean.setStartTime("0" + j + "00");
							} else {
								bean.setStartTime(j + "00");
							}
							String value = client.getIntervalValueList().get(i);
							bean.setModerateValue(ClientShedStrategyUtil.getRTPModerateValue(value));
							bean.setHighValue(ClientShedStrategyUtil.getRTPHighValue(value));
							result.add(bean);
						}
					}
					
				}

			} else {
				// CPP & DBP
				
				boolean validationFlag = false;
				//Validation
				for (int i = 0; i < 24; i++) {
					if (client.isCppFlag()) {
						String value = client.getIntervalValueList().get(i);
						if((value!=null&&(!value.trim().equalsIgnoreCase("")))){
							validationFlag = true;
							break;
						}
					} else if (client.isDbpFlag()) {
						String value = client.getIntervalValueList().get(i);
						String m = null;
						String v = null;
						if(value!=null){
							m=ClientShedStrategyUtil.getDBPModerateValue(value);
							v=ClientShedStrategyUtil.getDBPHighValue(value);
						}
						if((m!=null&&(!m.trim().equalsIgnoreCase("")))||(v!=null&&(!v.trim().equalsIgnoreCase("")))){
							validationFlag = true;
							break;
						}
					}
				}
				if(validationFlag){
					for (int i = 0; i < 24; i++) {
						ClientShedStrategyBackingBean bean = new ClientShedStrategyBackingBean();
						bean.setClient(client.getClient());
						String program = client.getProgram();
						if(isEnableCBPConsolidation()&&CBPUtil.isCBPConsolidationProgram(program)){
							program = "CBP";
						}
						bean.setProgram(program);
						bean.setEnroll(client.getState());
						bean.setAccount(client.getAccountNumber());
						bean.setParent(client.getParent());
						bean.setSeason(client.getSeason());
						bean.setParticipant(client.getParticipant());
						int j = i + 1;
						if (j < 10) {
							bean.setStartTime("0" + j + "00");
						} else {
							bean.setStartTime(j + "00");
						}
						String value = client.getIntervalValueList().get(i);
						if (client.isCppFlag()) {
							if (value != null && value.equalsIgnoreCase("Moderate")) {
								bean.setModerateValue(value);
							} else if (value != null&& value.equalsIgnoreCase("High")) {
								bean.setHighValue(value);
							}
							
						} else if (client.isDbpFlag()) {
							bean.setModerateValue(ClientShedStrategyUtil.getDBPModerateValue(value));
							bean.setHighValue(ClientShedStrategyUtil.getDBPHighValue(value));
						}

						result.add(bean);
					}
				}
				
			}

		}
		return result;
	}
	
	
	// ------------------------------------------------Setters and Getters---------------------------------------------------

	public ReportManager getReport() {
		if (report == null) {
			report = EJBFactory.getBean(ReportManager.class);
		}
		return report;
	}

	public NativeQueryManager getQueryManager() {
		if (queryManager == null) {
			queryManager = EJBFactory.getBean(NativeQueryManager.class);
		}
		return queryManager;
	}

	public void setReport(ReportManager report) {
		this.report = report;
	}

	/**
	 * @param programItems
	 *            the programItems to set
	 */
	public void setProgramItems(List<SelectItem> programItems) {
		this.programItems = programItems;
	}

	/**
	 * @return the programItems
	 */
	public List<SelectItem> getProgramItems() {
		return programItems;
	}

	/**
	 * @param searchProgramName the searchProgramName to set
	 */
	public void setSearchProgramName(String searchProgramName) {
		this.searchProgramName = searchProgramName;
	}

	/**
	 * @return the searchProgramName
	 */
	public String getSearchProgramName() {
		if(isEnableCBPConsolidation()&&"CBP".equalsIgnoreCase(searchProgramName)){
			return CBPUtil.CBP14DO;
		}else{
			return searchProgramName;	
		}
		
	}

	/**
	 * @return the shedStrategyList
	 */
	public List<ClientShedStrategyBackingBean> getShedStrategyList() {
		return shedStrategyList;
	}

	/**
	 * @param shedStrategyList the shedStrategyList to set
	 */
	public void setShedStrategyList(List<ClientShedStrategyBackingBean> shedStrategyList) {
		this.shedStrategyList = shedStrategyList;
	}

	/**
	 * @return the resultEmptyFlag
	 */
	public boolean isResultEmptyFlag() {
		if (this.rtpProgram)
			return this.rtpShedStrategyList!=null && this.rtpShedStrategyList.size()>0?false:true;
			
		int size = shedStrategyList.size();
		if(size>0){
			resultEmptyFlag = false;
		}else{
			resultEmptyFlag = true;
		}
		return resultEmptyFlag;
	}

	/**
	 * @param resultEmptyFlag the resultEmptyFlag to set
	 */
	public void setResultEmptyFlag(boolean resultEmptyFlag) {
		this.resultEmptyFlag = resultEmptyFlag;
	}
	
	public List<RTPShedStrategyEntry> getRtpShedStrategyList() {
		return rtpShedStrategyList;
	}

	public void setRtpShedStrategyList(
			List<RTPShedStrategyEntry> rtpShedStrategyList) {
		this.rtpShedStrategyList = rtpShedStrategyList;
	}

	public boolean isRtpProgram() {
		return rtpProgram;
	}

	public void setRtpProgram(boolean rtpProgram) {
		this.rtpProgram = rtpProgram;
	}
	private static ProgramManager pm;

	public static ProgramManager getPm() {
		if(pm==null){
			pm = EJB3Factory.getBean(ProgramManager.class);
		}
		return pm;
	}
	public boolean isEnableCBPConsolidation(){
		Boolean result = CBPUtil.isEnableCBPConsolidation();
		if(result==null){
			result = CBPUtil.isEnableCBPConsolidation(getPm().getAllPrograms());
		}
		return result;
	}
}
