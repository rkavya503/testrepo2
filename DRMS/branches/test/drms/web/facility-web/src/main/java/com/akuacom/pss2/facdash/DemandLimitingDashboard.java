package com.akuacom.pss2.facdash;

import java.io.Serializable;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.dl.DemandLimitingProgram;
import com.akuacom.pss2.program.participant.DemandLimitingProgramParticipantState;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;

public class DemandLimitingDashboard implements
		Serializable {
	private static final long serialVersionUID = -8049671022643169975L;
	private Participant participant;
	private String participantName;

	private Program demandLimitingProgram;
	private ProgramParticipant demandLimitingProgramParticipant = null;
	private DemandLimitingProgramParticipantState demandLimitingProgramParticipantState = null;
	
	public DemandLimitingDashboard() {
		super();
		loadState();
	}

	private void loadState() {
		ParticipantManager participantManager = EJB3Factory
				.getBean(ParticipantManagerBean.class);
		ProgramParticipantManager programParticipantManager = EJBFactory
				.getBean(ProgramParticipantManager.class);
		ProgramManager programManager = EJB3Factory
				.getBean(ProgramManager.class);

		participantName = FDUtils.getParticipantName();
		Program program = programManager.getProgramOnly(DemandLimitingProgram.PROGRAM_NAME);
		if ((program != null) && (program instanceof DemandLimitingProgram)) {
			demandLimitingProgram = (DemandLimitingProgram) program;
		} else {
			demandLimitingProgram = null;
		}
		participant = participantManager.getParticipant(participantName);
		if (participant != null) {
			this.setParticipantName(participant.getParticipantName());
			demandLimitingProgramParticipant = programParticipantManager
					.getProgramParticipant(DemandLimitingProgram.PROGRAM_NAME,
							this.getParticipantName(), false);
			if (demandLimitingProgramParticipant != null) {
				demandLimitingProgramParticipantState = new DemandLimitingProgramParticipantState(demandLimitingProgramParticipant);
			}
		}
	}

	public void refreshState() {
		ProgramParticipantManager programParticipantManager = EJBFactory
				.getBean(ProgramParticipantManager.class);
		if (participant != null) {
			this.setParticipantName(participant.getParticipantName());
			demandLimitingProgramParticipant = programParticipantManager
					.getProgramParticipant(DemandLimitingProgram.PROGRAM_NAME,
							this.getParticipantName(), false);
			if (demandLimitingProgramParticipant != null) {
				demandLimitingProgramParticipantState = new DemandLimitingProgramParticipantState(demandLimitingProgramParticipant);
			}
		}

	}
	
	public Integer getPollInterval() {
		if (demandLimitingProgramParticipantState != null) {
			return (demandLimitingProgramParticipantState.getMeterInterval().intValue()*60*1000);
		} else {
			return (5*60*1000);
		}
	}


	public Program getDemandLimitingProgram() {
		return demandLimitingProgram;
	}

	public void setDemandLimitingProgram(Program demandLimitingProgram) {
		this.demandLimitingProgram = demandLimitingProgram;
	}

	public ProgramParticipant getDemandLimitingProgramParticipant() {
		return demandLimitingProgramParticipant;
	}

	public void setDemandLimitingProgramParticipant(
			ProgramParticipant demandLimitingProgramParticipant) {
		this.demandLimitingProgramParticipant = demandLimitingProgramParticipant;
	}

	public DemandLimitingProgramParticipantState getDemandLimitingProgramParticipantState() {
		return demandLimitingProgramParticipantState;
	}

	public void setDemandLimitingProgramParticipantState(
			DemandLimitingProgramParticipantState demandLimitingProgramParticipantState) {
		this.demandLimitingProgramParticipantState = demandLimitingProgramParticipantState;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public String getIntervalLoadStr() {
		return (this.getDemandLimitingProgramParticipantState() != null ? getDemandLimitingProgramParticipantState().getIntervalLoad().toString()+ " kW" : "0 kW");
	}

	public String getCurrentThresholdStr() {
		return (this.getDemandLimitingProgramParticipantState() != null ? getDemandLimitingProgramParticipantState().getCurrentThreshold().getDescription() : "Not available");
	}

	public String getIntervalLoadColor() {
		if (this.getDemandLimitingProgramParticipantState() == null)
			//return Integer.toHexString(Integer.parseInt("FFFFFF",16));
			return "#FFFFFF";
		else {
			switch(this.getDemandLimitingProgramParticipantState().getCurrentThreshold()) {
			case normal: 				
				//return Integer.toHexString(Integer.parseInt("00FF00",16));
				return "#00FF00";
			case warning:
				//return Integer.toHexString(Integer.parseInt("FFFF0A",16));
				return "#FFFF0A";
			case moderate:
				//return Integer.toHexString(Integer.parseInt("FF7E00",16));
				return "#FF7E00";
			case high:
				//return Integer.toHexString(Integer.parseInt("FC1C14",16));  
				return "#FC1C14";
			case exceeded:
				//return Integer.toHexString(Integer.parseInt("FF0000",16));
				return "#FF0000";
			case NA:
				//return Integer.toHexString(Integer.parseInt("FFFFFF",16));
				return "#FFFFFF";
			default:
				//return Integer.toHexString(Integer.parseInt("FFFFFF",16));
				return "#FFFFFF";
			}
		}
			
	}

	public Double getMeterMax() {
		if (this.getDemandLimitingProgramParticipantState() == null)
			return 0.0;
		else if (this.getDemandLimitingProgramParticipantState().getIntervalLoad() <= this.getDemandLimitingProgramParticipantState().getExceededThreshold())
			return this.getDemandLimitingProgramParticipantState().getExceededThreshold();
		else 
			return this.getDemandLimitingProgramParticipantState().getIntervalLoad();
	}

	public Double getMeterMaxPercentage() {
		if (this.getDemandLimitingProgramParticipantState() == null)
			return 0.0;
		else if (this.getDemandLimitingProgramParticipantState().getIntervalLoad() <= this.getDemandLimitingProgramParticipantState().getExceededThreshold())
			return 100.00;
		else 
			return (this.getDemandLimitingProgramParticipantState().getIntervalLoad()/this.getDemandLimitingProgramParticipantState().getExceededThreshold()) * 100.00;
	}

}
