/**
 * 
 */
package com.akuacom.pss2.richsite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.jdbc.SearchConstraint;
import com.akuacom.jdbc.SearchConstraint.ORDER;
import com.akuacom.jsf.model.AbstractTableContentProvider;
import com.akuacom.jsf.model.SortColumn;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dl.DemandLimitingProgram;
import com.akuacom.pss2.program.testProgram.TestProgram;
import com.akuacom.pss2.query.NativeQueryManager;
import com.akuacom.pss2.query.ProgramSummary;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.userrole.ViewBuilderManager;
import com.akuacom.pss2.userrole.viewlayout.ProgramViewLayout;
import com.akuacom.pss2.util.DrasRole;
import com.akuacom.pss2.util.ProgramTemplate;;

/**
 * the class ProgramDataProvider
 * 
 */
public class ProgramDataProvider extends
		AbstractTableContentProvider<JSFProgram> implements ProgramViewLayout {



	public ProgramDataProvider() {
		buildViewlayout();
	}

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger
			.getLogger(ProgramDataProvider.class.getName());

	private int totalCount = 0;
	private boolean operatorUser;

	private boolean isProgramEditEnabled;
	private boolean isProgramSelectEnabled;
	private boolean isProgramCloneEnabled;
	private boolean isProgramAddEnabled;
	private boolean isProgramRemEnabled;
	private boolean isProgramActnEnabled;
	private boolean isProgramPropsEnabled;
	private boolean isProgramSignalEnabled;
	private boolean isProgramSeasonEnabled;
	private boolean isProgramRuleEnabled;
	private boolean isProgramRtpEnabled;
	private boolean isProgramAddEventEnabled;
	private boolean isProgramExpEnabled;

	private List<JSFProgram> programs = Collections.emptyList();

	private SystemManager systemManager;

			
	@Override
	public int getTotalRowCount() {

		return totalCount;
	}

	@Override
	public List<? extends JSFProgram> getContents() {
		if (this.programs == null)
			return Collections.emptyList();
		else
			return programs;
	}

	@Override
	public void updateModel() {
		programs = new ArrayList<JSFProgram>();

		// load programs
		NativeQueryManager query = (NativeQueryManager) EJBFactory
				.getBean(NativeQueryManager.class);

		SystemManager systemManager = this.getSystemManager();

		List<ProgramSummary> programs = Collections.emptyList();
		try {
			SearchConstraint sc = getSearchConstraint();
			programs = query.getProgramSummary(sc);

			for (ProgramSummary program : programs) {
				if (program.getProgramName().equals(
						DemandLimitingProgram.PROGRAM_NAME)
						&& !systemManager.getPss2Features()
								.isDemandLimitingEnabled())
					continue;

				int clientCount = program.getClientCount();
				int participantCount = program.getParticipantCount();

				JSFProgram jsfprogram = new JSFProgram();
				jsfprogram.setProgramName(program.getProgramName());
				
				if(program.getClassName().contains(ProgramTemplate.BIPProgramEJB.toString())){
					jsfprogram.setClassName(ProgramTemplate.BIPProgramEJB.toString());	
				}else if(program.getClassName().contains(ProgramTemplate.CBPProgramEJB.toString())){
					jsfprogram.setClassName(ProgramTemplate.CBPProgramEJB.toString());	
				}else if(program.getClassName().contains(ProgramTemplate.CPPProgramEJB.toString())){
					jsfprogram.setClassName(ProgramTemplate.CPPProgramEJB.toString());	
				}else if(program.getClassName().contains(ProgramTemplate.DBPNoBidProgramEJB.toString())){
					jsfprogram.setClassName(ProgramTemplate.DBPNoBidProgramEJB.toString());	
				}else if(program.getClassName().contains(ProgramTemplate.DemandLimitingProgramEJB.toString())){
					jsfprogram.setClassName(ProgramTemplate.DemandLimitingProgramEJB.toString());	
				}else if(program.getClassName().contains(ProgramTemplate.DemoProgramEJB.toString())){
					jsfprogram.setClassName(ProgramTemplate.DemoProgramEJB.toString());	
				}else if(program.getClassName().contains(ProgramTemplate.SCERTPProgramEJB2013.toString())){
					jsfprogram.setClassName(ProgramTemplate.SCERTPProgramEJB2013.toString());	
				}else{
					jsfprogram.setClassName(ProgramTemplate.TestProgramEJB.toString());	
				}
				/** not needed for delete **/
				jsfprogram.setPriority(program.getPriority());

				/****** Num/ time remaining for event *********/
				jsfprogram.setParticipantsCount(participantCount);
				jsfprogram.setClientsCount(clientCount);
				jsfprogram.setDeleted(false);

				if (participantCount == 0)
					jsfprogram.setDisabled(true);

				jsfprogram.setClonable(true);
				jsfprogram.setEventCreatable(true);
				jsfprogram.setParticipantViewable(true);
				jsfprogram
						.setViewParticipantLabel(JSFProgram.VIEW_PARTICIPANTS_LABEL);
				if (program.getProgramName().equals(
						DemandLimitingProgram.PROGRAM_NAME)) {
					jsfprogram.setDisabled(false);
					jsfprogram.setClonable(false);
					jsfprogram.setEventCreatable(false);
				}
				if (program.getProgramName().equals(TestProgram.PROGRAM_NAME)) {
					jsfprogram.setDisabled(false);
					jsfprogram.setClonable(false);
					jsfprogram.setEventCreatable(false);
					jsfprogram.setParticipantViewable(false);
					jsfprogram
							.setViewParticipantLabel(JSFProgram.ALL_PARTICIPANTS_LABEL);

					ParticipantManager partManager = (ParticipantManager) EJBFactory
							.getBean(ParticipantManagerBean.class);
					participantCount = partManager.getParentParticipantCount();
					clientCount = partManager.getClientParticipantCount();

					jsfprogram.setParticipantsCount(participantCount);
					jsfprogram.setClientsCount(clientCount);
				}

				this.programs.add(jsfprogram);
			}
		} catch (Exception e) {
			FDUtils.addMsgError("Internal Error!");
		}
	}

	public void programDeleteAction() {
		List<JSFProgram> temp = new ArrayList<JSFProgram>();
		if (this.programs != null) {
			for (JSFProgram p : programs) {
				if (p.isDeleted())
					temp.add(p);
			}
		}
		if (!temp.isEmpty()) {
			ProgramManager pm = EJBFactory.getBean(ProgramManager.class);
			for (JSFProgram p : temp) {
				pm.removeProgram(p.getProgramName());
			}
		}
	}

	// exports
	public void exportHtmlTableToExcel() throws IOException {
		try {
			String filename = "programs.csv";
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) fc
					.getExternalContext().getResponse();
			response.reset();
			response.addHeader("cache-control", "must-revalidate");
			response.setContentType("application/octet_stream");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ filename + "\"");

			String table = getExportContents();
			response.getWriter().print(table);
			fc.responseComplete();
		} catch (Exception e) {
			FDUtils.addMsgInfo("Internal Error");
			log.error(e.getMessage(), e);
		}
	}

	public String getExportContents() {
		StringBuffer buffer = new StringBuffer("Program Name");
		SearchConstraint sc = new SearchConstraint(0, Integer.MAX_VALUE);
		sc.addSortColumn("programName", ORDER.ASC);
		List<ProgramSummary> summary = getExportProgramSummary(sc);

		for (ProgramSummary prog : summary) {
			if (!prog.getProgramName().equals(
					DemandLimitingProgram.PROGRAM_NAME)){
				buffer.append("\n");
				buffer.append(prog.getProgramName());
			}
		}
		return buffer.toString();
	}

	protected List<ProgramSummary> getExportProgramSummary(SearchConstraint sc) {
		List<ProgramSummary> summary = new ArrayList<ProgramSummary>();
		try {
			NativeQueryManager query = (NativeQueryManager) EJBFactory
					.getBean(NativeQueryManager.class);
			summary = query.getProgramSummary(sc);
		} catch (Exception e) {
			log.error(e);
			FDUtils.addMsgError("Failed to get program information");
		}
		return summary;
	}

	private SystemManager getSystemManager() {
		if (this.systemManager == null)
			this.systemManager = EJB3Factory.getLocalBean(SystemManager.class);
		return this.systemManager;
	}

	protected SearchConstraint getSearchConstraint() {
		SearchConstraint sc = new SearchConstraint(0, Integer.MAX_VALUE);
		SortColumn sortColumn = this.getSortColumn();
		if (sortColumn != null) {
			sc.addSortColumn(sortColumn.getName(),
					sortColumn.isAscendent() ? ORDER.ASC : ORDER.DESC);
		}
		if (sc.getOrderColumns().size() == 0)
			sc.addSortColumn("priority", ORDER.ASC);
		return sc;
	}

	public boolean isOperatorUser() {
		if (FacesContext.getCurrentInstance().getExternalContext()
				.isUserInRole(DrasRole.Operator.toString())) {
			operatorUser = true;
		} else {
			operatorUser = false;
		}
		return operatorUser;
	}
	
	private void buildViewlayout(){
		try{
		getViewBuilderManager().buildProgramViewLayout(this);
		}catch(NamingException NmE){
		 	
		}
	}
	
	private ViewBuilderManager getViewBuilderManager() throws NamingException{
		return (ViewBuilderManager) new InitialContext().lookup("pss2/ViewBuilderManagerBean/local");
	}

	public void setOperatorUser(boolean operatorUser) {
		this.operatorUser = operatorUser;
	}

	public boolean isProgramEditEnabled() {
		return isProgramEditEnabled;
	}

	public void setProgramEditEnabled(boolean isProgramEditEnabled) {
		this.isProgramEditEnabled = isProgramEditEnabled;
	}

	public boolean isProgramSelectEnabled() {
		return isProgramSelectEnabled;
	}

	public void setProgramSelectEnabled(boolean isProgramSelectEnabled) {
		this.isProgramSelectEnabled = isProgramSelectEnabled;
	}

	public boolean isProgramCloneEnabled() {
		return isProgramCloneEnabled;
	}

	public void setProgramCloneEnabled(boolean isProgramCloneEnabled) {
		this.isProgramCloneEnabled = isProgramCloneEnabled;
	}

	public boolean isProgramAddEnabled() {
		return isProgramAddEnabled;
	}

	public void setProgramAddEnabled(boolean isProgramAddEnabled) {
		this.isProgramAddEnabled = isProgramAddEnabled;
	}

	public boolean isProgramRemEnabled() {
		return isProgramRemEnabled;
	}

	public void setProgramRemEnabled(boolean isProgramRemEnabled) {
		this.isProgramRemEnabled = isProgramRemEnabled;
	}

	public boolean isProgramActnEnabled() {
		return isProgramActnEnabled;
	}

	public void setProgramActnEnabled(boolean isProgramActnEnabled) {
		this.isProgramActnEnabled = isProgramActnEnabled;
	}
	
	public boolean isProgramPropsEnabled() {
		return isProgramPropsEnabled;
	}
	
	public void setProgramPropsEnabled(boolean isProgramPropsEnabled) {
		this.isProgramPropsEnabled = isProgramPropsEnabled;
	}
	
	public boolean isProgramSignalEnabled() {
		return isProgramSignalEnabled;
	}
	
	public void setProgramSignalEnabled(boolean isProgramSignalEnabled) {
		this.isProgramSignalEnabled = isProgramSignalEnabled;
	}
	
	public boolean isProgramSeasonEnabled() {
		return isProgramSeasonEnabled;
	}
	
	public void setProgramSeasonEnabled(boolean isProgramSeasonEnabled) {
		this.isProgramSeasonEnabled = isProgramSeasonEnabled;
	}
	
	public boolean isProgramRuleEnabled() {
		return isProgramRuleEnabled;
	}
	
	public void setProgramRuleEnabled(boolean isProgramRuleEnabled) {
		this.isProgramRuleEnabled = isProgramRuleEnabled;
	}
	
	public boolean isProgramAddEventEnabled() {
		return isProgramAddEventEnabled;
	}
	
	public void setProgramAddEventEnabled(boolean isProgramAddEventEnabled) {
		this.isProgramAddEventEnabled = isProgramAddEventEnabled;
	}

	
	public boolean isProgramRtpEnabled() {
		return isProgramRtpEnabled;
	}
	
	public void setProgramRtpEnabled(boolean isProgramRtpEnabled) {
		this.isProgramRtpEnabled = isProgramRtpEnabled;
	}

	public boolean isProgramExpEnabled() {
		return isProgramExpEnabled;
	}
	
	public void setProgramExpEnabled(boolean isProgramExpEnabled) {
		this.isProgramExpEnabled = isProgramExpEnabled;
	}
	
}
