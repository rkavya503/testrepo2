package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.program.dl.DemandLimitingProgram;
import com.akuacom.pss2.program.participant.Constraint;
import com.akuacom.pss2.program.participant.DemandLimitingProgramParticipantState;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.pss2.program.participant.ProgramParticipantManager;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;

import com.akuacom.pss2.rule.Rule;

public class DemandLimitingProgramParticipantSettingsEditor implements
		Serializable {

	private static final long serialVersionUID = 660253592651530371L;
	private List<DemandLimitingProgramParticipantSetting> settings = new ArrayList<DemandLimitingProgramParticipantSetting>();
	private String programName;
	private Participant participant;
	private String participantName;

	private Program demandLimitingProgram;
	ProgramParticipant demandLimitingProgramParticipant = null;
	// DemandLimitingProgramParticipantState
	// demandLimitingProgramParticipantState = null;

	private Boolean optOut = new Boolean(false);

	private Date optOutStart;
	private Date optOutEnd;

	public DemandLimitingProgramParticipantSettingsEditor() {
		super();
		loadSettings();
	}

	private void loadSettings() {
		ParticipantManager participantManager = EJB3Factory
				.getBean(ParticipantManagerBean.class);
		ProgramParticipantManager programParticipantManager = EJBFactory
				.getBean(ProgramParticipantManager.class);
		ProgramManager programManager = EJB3Factory
				.getBean(ProgramManager.class);

		participantName = FDUtils.getParticipantName();
		programName = DemandLimitingProgram.PROGRAM_NAME;
		Program program = programManager.getProgramWithRules(programName);
		if ((program != null) && (program instanceof DemandLimitingProgram)) {
			demandLimitingProgram = (DemandLimitingProgram) program;
		} else {
			demandLimitingProgram = null;
		}
		participant = participantManager.getParticipantOnly(participantName,false);
		if (participant != null) {
			this.setParticipantName(participant.getParticipantName());
			demandLimitingProgramParticipant = programParticipantManager
					.getProgramParticipant(programName,
							this.getParticipantName(), false);
			if (demandLimitingProgramParticipant != null) {
				setOptOut(demandLimitingProgramParticipant.getOptStatus() == 1);
				if (demandLimitingProgramParticipant.getConstraint() == null) {
					demandLimitingProgramParticipant
							.setConstraint(new Constraint());
				}
				this.setOptOutStart(demandLimitingProgramParticipant
						.getConstraint().getMinActive());
				this.setOptOutEnd(demandLimitingProgramParticipant
						.getConstraint().getMaxActive());
				if ((demandLimitingProgramParticipant
						.getProgramParticipantRules() == null)
						|| ((demandLimitingProgramParticipant
								.getProgramParticipantRules() != null) && (demandLimitingProgramParticipant
								.getProgramParticipantRules().size() == 0))) {
					if (demandLimitingProgram != null) {
						demandLimitingProgramParticipant
								.setProgramParticipantRules(new HashSet<ProgramParticipantRule>());
						for (ProgramRule demandLimitingRule : this
								.getDemandLimitingRules()) {
							DemandLimitingProgramParticipantSetting demandLimitingProgramParticipantSetting = new DemandLimitingProgramParticipantSetting(
									demandLimitingRule);
							settings.add(demandLimitingProgramParticipantSetting);
							demandLimitingProgramParticipant
									.getProgramParticipantRules()
									.add(demandLimitingProgramParticipantSetting
											.getProgramParticipantRule());
						}
						programParticipantManager.updateProgramParticipant(
								demandLimitingProgramParticipant
										.getProgramName(),
								demandLimitingProgramParticipant
										.getParticipantName(), false,
								demandLimitingProgramParticipant);
					}
				}
				refreshSettings();

			}
		}
	}

	public void refreshSettings() {
		settings.clear();
		for (ProgramParticipantRule demandLimitingProgramParticipantRule : demandLimitingProgramParticipant
				.getProgramParticipantRules()) {
			if (!demandLimitingProgramParticipantRule.getSource()
					.equalsIgnoreCase(Rule.Source.TELEMETRY.getDescription()))
				settings.add(new DemandLimitingProgramParticipantSetting(
						demandLimitingProgramParticipantRule));
		}

	}

	public String saveSettings() {
		ProgramParticipantManager programParticipantManager = EJBFactory
				.getBean(ProgramParticipantManager.class);
		updateProgramParticipantSettings();
		programParticipantManager.updateProgramParticipant(
				demandLimitingProgramParticipant.getProgramName(),
				demandLimitingProgramParticipant.getParticipantName(), false,
				demandLimitingProgramParticipant);

		for (ProgramParticipant clientProgramParticipant : programParticipantManager
				.getProgramParticipantsByParent(
						demandLimitingProgramParticipant.getProgramName(),
						demandLimitingProgramParticipant.getParticipantName(),
						true)) {
			if (clientProgramParticipant.getProgram() instanceof DemandLimitingProgram) {
				updateProgramParticipantClientSettings(clientProgramParticipant);
				programParticipantManager.updateProgramParticipant(
						clientProgramParticipant.getProgramName(),
						clientProgramParticipant.getParticipantName(), true,
						clientProgramParticipant);
			}
		}
		return "success";
	}

	public void updateProgramParticipantSettings() {
		if (demandLimitingProgramParticipant != null) {
			for (DemandLimitingProgramParticipantSetting setting : settings) {
				for (ProgramParticipantRule demandLimitingRule : demandLimitingProgramParticipant
						.getProgramParticipantRules()) {
					if ((setting.getProgramParticipantRule().getUUID() != null)
							&& (demandLimitingRule.getUUID() != null)
							&& (setting.getProgramParticipantRule().getUUID()
									.equals(demandLimitingRule.getUUID()))) {
						demandLimitingRule.setSortOrder(setting.getSortOrder());
						demandLimitingRule.setMode(setting.getMode());
						demandLimitingRule.setThreshold(setting.getThreshold());
						demandLimitingRule.setStart(setting.getStart());
						demandLimitingRule.setEnd(setting.getEnd());
						demandLimitingRule.setVariable(setting.getVariable());
						demandLimitingRule.setOperator(setting.getOperator());
						demandLimitingRule.setValue(setting.getValue());
						demandLimitingRule.setSource(setting.getSource());
						demandLimitingRule.setSignalAction(setting
								.getSignalAction());
						demandLimitingRule.setNotifyAction(setting
								.getNotifyAction());
						break;
					} else if (setting.getSortOrder().equals(
							demandLimitingRule.getSortOrder())
							&& (setting.getMode().equals(demandLimitingRule
									.getMode()))
							&& (setting.getThreshold()
									.equals(demandLimitingRule.getThreshold()))
							&& (setting.getVariable().equals(demandLimitingRule
									.getVariable()))
							&& (setting.getOperator().equals(demandLimitingRule
									.getOperator()))
							&& (setting.getSource().equals(demandLimitingRule
									.getSource()))) {
						demandLimitingRule.setStart(setting.getStart());
						demandLimitingRule.setEnd(setting.getEnd());
						demandLimitingRule.setValue(setting.getValue());
						demandLimitingRule.setSignalAction(setting
								.getSignalAction());
						demandLimitingRule.setNotifyAction(setting
								.getNotifyAction());
						break;
					}

				}
			}
		}

		// if (optOut)
		setOptOutConstraint(demandLimitingProgramParticipant);

	}

	public void updateProgramParticipantClientSettings(
			ProgramParticipant programParticipant) {
		if (programParticipant != null) {
			for (DemandLimitingProgramParticipantSetting setting : settings) {
				for (ProgramParticipantRule demandLimitingRule : programParticipant
						.getProgramParticipantRules()) {
					if (setting.getSortOrder().equals(
							demandLimitingRule.getSortOrder())
							&& (setting.getMode().equals(demandLimitingRule
									.getMode()))
							&& (setting.getThreshold()
									.equals(demandLimitingRule.getThreshold()))
							&& (setting.getVariable().equals(demandLimitingRule
									.getVariable()))
							&& (setting.getOperator().equals(demandLimitingRule
									.getOperator()))
							&& (setting.getSource().equals(demandLimitingRule
									.getSource()))) {
						demandLimitingRule.setStart(setting.getStart());
						demandLimitingRule.setEnd(setting.getEnd());
						demandLimitingRule.setValue(setting.getValue());
						demandLimitingRule.setSignalAction(setting
								.getSignalAction());
						demandLimitingRule.setNotifyAction(setting
								.getNotifyAction());
						break;
					}
				}
			}

			// if (optOut)
			setOptOutConstraint(programParticipant);

		}
	}

	public String cancel() {
		return "cancel";
	}

	public void calculate() {
		updateProgramParticipantSettings();
		DemandLimitingProgramParticipantState demandLimitingProgramParticipantState = new DemandLimitingProgramParticipantState(
				demandLimitingProgramParticipant);

		if ((demandLimitingProgramParticipantState != null)
				&& (demandLimitingProgramParticipant != null)) {

			List<String> messages = demandLimitingProgramParticipantState
					.validateRules();
			FacesContext fc = FacesContext.getCurrentInstance();
			if (fc != null) {
				if ((messages != null) && (messages.size() > 0)) {
					for (String message : messages) {
						fc.addMessage(null, new FacesMessage(
								FacesMessage.SEVERITY_WARN, message, message));
					}
				} else {
					fc.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_INFO,
									"Demand limiting thresholds were calculated using tariff limit as the basis",
									"Demand limiting thresholds were calculated using tariff limit as the basis"));

				}
			}

			demandLimitingProgramParticipant = demandLimitingProgramParticipantState
					.getProgramParticipant();
			refreshSettings();
		}
	}

	public void validate() {
		updateProgramParticipantSettings();
		DemandLimitingProgramParticipantState demandLimitingProgramParticipantState = new DemandLimitingProgramParticipantState(
				demandLimitingProgramParticipant);

		if ((demandLimitingProgramParticipantState != null)
				&& (demandLimitingProgramParticipant != null)) {

			List<String> messages = demandLimitingProgramParticipantState
					.validateRules();
			FacesContext fc = FacesContext.getCurrentInstance();
			if (fc != null) {
				if ((messages != null) && (messages.size() > 0)) {
					for (String message : messages) {
						fc.addMessage(null, new FacesMessage(
								FacesMessage.SEVERITY_WARN, message, message));
					}
				} else {
					fc.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_INFO,
									"Current demand limiting settings passed default validation criteria",
									"Current demand limiting settings passed default validation criteria"));

				}
			}
		}
	}

	public void reset() {
		if (demandLimitingProgram != null) {
			settings.clear();
			for (ProgramRule demandLimitingRule : this.getDemandLimitingRules()) {
				if (!demandLimitingRule.getSource().equalsIgnoreCase(
						Rule.Source.TELEMETRY.getDescription())) {
					DemandLimitingProgramParticipantSetting demandLimitingProgramParticipantSetting = new DemandLimitingProgramParticipantSetting(
							demandLimitingRule);
					settings.add(demandLimitingProgramParticipantSetting);
				}
			}
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Settings were reset to demand limiting default values",
					"Settings were reset to demand limiting default values"));
		}
	}

	public void resetOptOutPeriod() {
		this.setOptOutStart(null);
		this.setOptOutEnd(null);

		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(
				null,
				new FacesMessage(
						FacesMessage.SEVERITY_INFO,
						"If opt out start and/or end dates were specified, they have been removed",
						"If opt out start and/or end dates were specified, they have been removed"));
	}

	public List<DemandLimitingProgramParticipantSetting> getSettings() {
		return settings;
	}

	public void setSettings(
			List<DemandLimitingProgramParticipantSetting> settings) {
		this.settings = settings;
	}

	public Set<ProgramRule> getDemandLimitingRules() {
		return demandLimitingProgram.getRules();
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

	public Boolean getOptOut() {
		return optOut;
	}

	public void setOptOut(Boolean optOut) {
		this.optOut = optOut;
	}

	public Date getOptOutStart() {
		return optOutStart;
	}

	public void setOptOutStart(Date optOutStart) {
		this.optOutStart = optOutStart;
	}

	public Date getOptOutEnd() {
		return optOutEnd;
	}

	public void setOptOutEnd(Date optOutEnd) {
		this.optOutEnd = optOutEnd;
	}

	public void setOptOutConstraint(ProgramParticipant programParticipant) {
		if (programParticipant.getConstraint() == null) {
			programParticipant.setConstraint(new Constraint());
		}
		programParticipant.getConstraint().setActiveAction(
				Constraint.Action.RESTRICT);
		programParticipant.getConstraint().setMinActive(this.getOptOutStart());
		programParticipant.getConstraint().setMaxActive(this.getOptOutEnd());
		programParticipant.getConstraint().setDurationAction(
				Constraint.Action.RESTRICT);
		programParticipant.getConstraint()
				.setMinDuration(this.getOptOutStart());
		programParticipant.getConstraint().setMaxDuration(this.getOptOutEnd());
		programParticipant.getConstraint().setNotifyAction(
				Constraint.Action.RESTRICT);
		programParticipant.getConstraint().setMinNotify(this.getOptOutStart());
		programParticipant.getConstraint().setMaxNotify(this.getOptOutEnd());
	}

}
