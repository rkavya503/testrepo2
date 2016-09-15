package com.akuacom.pss2.richsite.program.configure.dl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.program.dl.DemandLimitingProgram;

import com.akuacom.pss2.rule.Rule;

public class DemandLimitingProgramSettingsEditor {

	private List<DemandLimitingProgramSetting> settings = new ArrayList<DemandLimitingProgramSetting>();

	private DemandLimitingProgram demandLimitingProgram;
	private ProgramManager programManager = EJB3Factory
			.getBean(ProgramManagerBean.class);

	public DemandLimitingProgramSettingsEditor() {
		super();
		loadSettings();
	}

	private void loadSettings() {
		Program program = programManager.getProgramWithRules(DemandLimitingProgram.PROGRAM_NAME);
		if ((program != null) && (program instanceof DemandLimitingProgram)) {
			demandLimitingProgram = (DemandLimitingProgram) program;
			for (ProgramRule demandLimitingRule : this.getDemandLimitingRules()) {
				if ((demandLimitingRule.getSource().equalsIgnoreCase(Rule.Source.DEMAND_LIMITING.getDescription())) ||
						(demandLimitingRule.getSource().equalsIgnoreCase(Rule.Source.DEMAND_LIMITING_PERCENTAGE.getDescription())))
				settings.add(new DemandLimitingProgramSetting(
					demandLimitingRule));
			}
		}
	}

	public String saveSettings() {
		Program program = programManager.getProgramWithRules(DemandLimitingProgram.PROGRAM_NAME);
		if ((program != null) && (program instanceof DemandLimitingProgram)) {
			demandLimitingProgram = (DemandLimitingProgram) program;
			for (ProgramRule demandLimitingRule : this.getDemandLimitingRules()) {
				for (DemandLimitingProgramSetting setting : settings) {
					if (setting.getProgramRule().getUUID()
							.equals(demandLimitingRule.getUUID())) {
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
					}
				}
			}
		}
		programManager.updateProgram(demandLimitingProgram);
		return "success";
	}

	public String cancel() {
		return "cancel";
	}

	public List<DemandLimitingProgramSetting> getSettings() {
		return settings;
	}

	public void setSettings(List<DemandLimitingProgramSetting> settings) {
		this.settings = settings;
	}

	public Set<ProgramRule> getDemandLimitingRules() {
		return demandLimitingProgram.getRules();
	}
}
