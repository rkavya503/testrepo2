package com.akuacom.pss2.program.participant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.rule.Rule.Mode;
import com.akuacom.pss2.rule.Rule.Threshold;

public class DemandLimitingProgramParticipantState implements Serializable {

	private static final long serialVersionUID = 3801271615465100770L;

	public static double roundValue(double value) {
		double result = value * 100;
		result = Math.round(result);
		result = result / 100;
		return result;
	}

	public enum Variable {
		tariff_limit("tariff_limit"), rate_interval("rate_interval"), meter_interval(
				"meter_interval"), event_span("event_span"), interval_load(
				"interval_load"), meter_timestamp("meter_timestamp"), threshold_percentage(
				"threshold_percentage"), warning("warning"), threshold(
				"threshold");

		private final String description;

		Variable(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

	}

	public static String getVariableUom(Variable variable) {
		String variableUom = "";

		switch (variable) {
		case tariff_limit: {
			variableUom = "kW";
			break;
		}
		case rate_interval: {
			variableUom = "minutes";
			break;
		}
		case meter_interval: {
			variableUom = "minutes";
			break;
		}
		case event_span: {
			variableUom = "minutes";
			break;
		}
		case interval_load: {
			variableUom = "kW";
			break;
		}
		case meter_timestamp: {
			variableUom = "date and time";
			break;
		}
		case threshold_percentage: {
			variableUom = "%";
			break;
		}
		case warning: {
			variableUom = "(1=on)/(0=off)";
			break;
		}
		case threshold: {
			variableUom = "kW";
			break;
		}
		default:
			variableUom = "";
			break;
		}

		return variableUom;
	}

	private Boolean demandLimitingBlockade = new Boolean(false);
	private Double meterInterval = 0.0;
	private Double tariffLimit = 0.0;
	private Double rateInterval = 0.0;
	private Double rateIntervalLoad = 0.0;
	private Double normalThresholdPercentage = 0.0;
	private Double normalThreshold = 0.0;
	private Double warningThresholdPercentage = 0.0;
	private Double warningThreshold = 0.0;
	private Double moderateThresholdPercentage = 0.0;
	private Double moderateThreshold = 0.0;
	private Double highThresholdPercentage = 0.0;
	private Double highThreshold = 0.0;
	private Double exceededThresholdPercentage = 0.0;
	private Double exceededThreshold = 0.0;
	private Boolean demandLimitingOptOut = new Boolean(false);
	private Date demandLimitingBlockadeStart;
	private Date demandLimitingBlockadeEnd;

	private Boolean warning = new Boolean(false);
	private Double eventSpan = 0.0;
	private Date meterTimestamp;
	private Double intervalLoad = 0.0;
	private Boolean notify = new Boolean(false);
	private Mode currentMode = Mode.NORMAL;
	private Threshold currentThreshold = Threshold.normal;
	private Boolean signalize = new Boolean(false);
	private Boolean rulesDefined = new Boolean(false);
	private Double thresholdPercentage = 0.0;
	private ProgramParticipant programParticipant;

	private Boolean endSignal = false;
	private Boolean startSignal = false;
	private Boolean extendSignal = false;
	private Boolean modeModSignal = false;
	private Boolean startNotification = false;
	private Boolean endNotification = false;

	public DemandLimitingProgramParticipantState(
			ProgramParticipant demandLimitingProgramParticipant) {
		super();
		this.programParticipant = demandLimitingProgramParticipant;
		if ((demandLimitingProgramParticipant.getProgramParticipantRules() != null)
				&& (demandLimitingProgramParticipant
						.getProgramParticipantRules().size() > 0)) {
			setProgramParticipantState(demandLimitingProgramParticipant);
		}
	}

	private void setProgramParticipantState(
			ProgramParticipant demandLimitingProgramParticipant) {
		this.setRulesDefined(true);
		for (ProgramParticipantRule rule : demandLimitingProgramParticipant
				.getProgramParticipantRules()) {
			if ((rule.getSource() != null) && (rule.getVariable() != null)
					&& (rule.getValue() != null)) {
				setRuleVariables(rule);
			}
		}

		if (validRules()) {
			this.calcThresholdLevels();
			this.calcThresholdPercentage();
			this.evaluateState();
			this.setConstrainedState();
		}
	}

	private void setRuleVariables(ProgramParticipantRule rule) {
		Rule.Source source = this.getSourceEnum(rule.getSource());
		Variable variable = Enum.valueOf(
				DemandLimitingProgramParticipantState.Variable.class,
				rule.getVariable());
		switch (source) {
		case DEMAND_LIMITING:
			setDemandLimitingVariables(rule, variable);
			break;
		case DEMAND_LIMITING_PARTICIPANT: {
			setDemandLimitingParticipantVariables(rule, variable);
			break;
		}
		case TELEMETRY: {
			setDemandLimitingTelemetryVariables(rule, variable);
			break;
		}
		case DEMAND_LIMITING_LOAD: {
			setDemandLimitingWattageVariables(rule, variable);
			break;
		}
		case DEMAND_LIMITING_PERCENTAGE: {
			setDemandLimitingPercentageVariables(rule, variable);
			break;
		}
		default:
			break;
		}
	}

	private void updateProgramParticipantRules() {
		if ((programParticipant.getProgramParticipantRules() != null)
				&& (programParticipant.getProgramParticipantRules().size() > 0)) {

			for (ProgramParticipantRule rule : programParticipant
					.getProgramParticipantRules()) {
				if ((rule.getSource() != null) && (rule.getVariable() != null)
						&& (rule.getValue() != null)) {
					getRuleVariableValues(rule);
				}
			}

		}

	}

	private void getRuleVariableValues(ProgramParticipantRule rule) {
		Rule.Source source = this.getSourceEnum(rule.getSource());
		Variable variable = Enum.valueOf(
				DemandLimitingProgramParticipantState.Variable.class,
				rule.getVariable());
		switch (source) {
		case DEMAND_LIMITING: {
			getDemandLimitingVariableValue(rule, variable);
			break;
		}

		case DEMAND_LIMITING_PARTICIPANT: {
			getDemandLimitingParticipantVariableValue(rule, variable);
			break;
		}
		case TELEMETRY: {
			getDemandLimitingTelemetryVariableValue(rule, variable);
			break;
		}
		case DEMAND_LIMITING_LOAD: {
			getDemandLimitingWattageVariableValue(rule, variable);
			break;
		}
		case DEMAND_LIMITING_PERCENTAGE: {
			getDemandLimitingPercentageVariableValue(rule, variable);
			break;
		}
		default:
			break;

		}
	}

	private void setDemandLimitingVariables(ProgramParticipantRule rule,
			Variable variable) {

		switch (variable) {
		case rate_interval:
			this.setRateInterval(rule.getValue());
			break;
		case meter_interval:
			this.setMeterInterval(rule.getValue());
			break;
		case event_span:
			this.setEventSpan(rule.getValue());
			break;
		default:
			break;
		}
	}

	private void setDemandLimitingParticipantVariables(
			ProgramParticipantRule rule, Variable variable) {
		switch (variable) {
		case tariff_limit:
			this.setTariffLimit(rule.getValue());
			break;
		default:
			break;

		}
	}

	private void setDemandLimitingTelemetryVariables(
			ProgramParticipantRule rule, Variable variable) {
		switch (variable) {
		case interval_load:
			this.setIntervalLoad(rule.getValue());
			break;

		case meter_timestamp:
			this.setMeterTimestamp(new Date(rule.getValue().longValue()));
			break;

		case threshold_percentage:
			this.setThresholdPercentage(rule.getValue());
			break;

		case warning:
			this.setWarning(rule.getValue() == 1.0);
			break;

		default:
			break;

		}

	}

	private void setDemandLimitingWattageVariables(ProgramParticipantRule rule,
			Variable variable) {

		switch (variable) {
		case interval_load:
			switch (rule.getThreshold()) {
			case normal:
				this.setNormalThreshold(rule.getValue());
				break;

			case warning:
				this.setWarningThreshold(rule.getValue());
				break;

			case moderate:
				this.setModerateThreshold(rule.getValue());
				break;

			case high:
				this.setHighThreshold(rule.getValue());
				break;

			case exceeded:
				this.setExceededThreshold(rule.getValue());
				break;

			case NA:
				break;

			default:
				break;
			}
			break;

		default:
			break;

		}
	}

	private void setDemandLimitingPercentageVariables(
			ProgramParticipantRule rule, Variable variable) {

		switch (variable) {
		case threshold_percentage:
			switch (rule.getThreshold()) {
			case normal:
				this.setNormalThresholdPercentage(rule.getValue());
				break;

			case warning:
				this.setWarningThresholdPercentage(rule.getValue());
				break;

			case moderate:
				this.setModerateThresholdPercentage(rule.getValue());
				break;

			case high:
				this.setHighThresholdPercentage(rule.getValue());
				break;

			case exceeded:
				this.setExceededThresholdPercentage(rule.getValue());
				break;

			case NA:
				break;

			default:
				break;
			}
			break;
		}
	}

	private void getDemandLimitingVariableValue(ProgramParticipantRule rule,
			Variable variable) {
		switch (variable) {
		case rate_interval:
			rule.setValue(this.getRateInterval());
			break;

		case meter_interval:
			rule.setValue(this.getMeterInterval());
			break;

		case event_span:
			rule.setValue(this.getEventSpan());
			break;

		default:
			break;
		}

	}

	private void getDemandLimitingParticipantVariableValue(
			ProgramParticipantRule rule, Variable variable) {
		switch (variable) {
		case tariff_limit:
			rule.setValue(this.getTariffLimit());
			break;

		default:
			break;

		}
	}

	private void getDemandLimitingTelemetryVariableValue(
			ProgramParticipantRule rule, Variable variable) {
		switch (variable) {
		case interval_load:
			rule.setValue(this.getIntervalLoad());
			break;

		case meter_timestamp:
			rule.setValue(new Double(this.getMeterTimestamp().getTime()));
			break;

		case threshold_percentage:
			rule.setValue(this.getThresholdPercentage());
			break;

		case warning:
			rule.setValue(this.getWarning() ? 1.0 : 0.0);
			break;

		default:
			break;

		}

	}

	private void getDemandLimitingWattageVariableValue(
			ProgramParticipantRule rule, Variable variable) {
		switch (variable) {
		case interval_load:
			switch (rule.getThreshold()) {
			case normal:
				rule.setValue(this.getNormalThreshold());
				break;

			case warning:
				rule.setValue(this.getWarningThreshold());
				break;

			case moderate:
				rule.setValue(this.getModerateThreshold());
				break;

			case high:
				rule.setValue(this.getHighThreshold());
				break;

			case exceeded:
				rule.setValue(this.getExceededThreshold());
				break;

			case NA:
				break;

			default:
				break;
			}
			break;

		default:
			break;

		}

	}

	private void getDemandLimitingPercentageVariableValue(
			ProgramParticipantRule rule, Variable variable) {
		switch (variable) {
		case threshold_percentage:
			switch (rule.getThreshold()) {
			case normal:
				rule.setValue(this.getNormalThresholdPercentage());
				break;

			case warning:
				rule.setValue(this.getWarningThresholdPercentage());
				break;

			case moderate:
				rule.setValue(this.getModerateThresholdPercentage());
				break;

			case high:
				rule.setValue(this.getHighThresholdPercentage());
				break;

			case exceeded:
				rule.setValue(this.exceededThresholdPercentage);
				break;

			case NA:
				break;

			default:
				break;
			}
			break;

		default:
			break;

		}

	}

	public ProgramParticipant getProgramParticipant() {
		this.updateProgramParticipantRules();
		return programParticipant;
	}

	public Set<ProgramParticipantRule> getProgramParticipantRules() {
		this.updateProgramParticipantRules();
		if (programParticipant != null)
			return programParticipant.getProgramParticipantRules();
		else
			return null;
	}

	public Boolean validRules() {
		return (validateRules().size() == 0);
	}

	public List<String> validateRules() {
		List<String> validationErrors = new ArrayList<String>();
		if ((tariffLimit == null) || (tariffLimit == 0.0))
			validationErrors
					.add(new String(
							"tariff_limit cannot be null or 0.0; It has to be above 0.0"));
		else
			this.calcThresholdLevels();

		if ((rateInterval == null) || (rateInterval == 0.0))
			validationErrors
					.add(new String(
							"rate_interval cannot be null or 0.0; It has to be above 0.0"));
		if ((meterInterval == null) || (meterInterval == 0.0))
			validationErrors
					.add(new String(
							"meter_interval cannot be null or 0.0; It has to be above 0.0"));
		if ((eventSpan == null) || (eventSpan == 0.0))
			validationErrors
					.add(new String(
							"event_span cannot be null or 0.0; It has to be above 0.0"));

		if ((normalThreshold == null) && (normalThresholdPercentage == null))
			validationErrors
					.add(new String(
							"Both normal threshold and normal threshold percentage cannot be null; Either one should be defined with a value > 0.0"));
		else if ((normalThreshold == 0.0) && (normalThresholdPercentage == 0.0))
			validationErrors
					.add(new String(
							"Both normal threshold and normal threshold percentage cannot be 0.0; Either one should be defined with a value > 0.0"));

		if ((warningThreshold == null) && (warningThresholdPercentage == null))
			validationErrors
					.add(new String(
							"Both warning threshold and warning threshold percentage cannot be null; Either one should be defined with a value > 0.0"));
		else if ((warningThreshold == 0.0)
				&& (warningThresholdPercentage == 0.0))
			validationErrors
					.add(new String(
							"Both warning threshold and warning threshold percentage cannot be 0.0; Either one should be defined with a value > 0.0"));

		if ((moderateThreshold == null)
				&& (moderateThresholdPercentage == null))
			validationErrors
					.add(new String(
							"Both moderate threshold and moderate threshold percentage cannot be null; Either one should be defined with a value > 0.0"));
		else if ((moderateThreshold == 0.0)
				&& (moderateThresholdPercentage == 0.0))
			validationErrors
					.add(new String(
							"Both moderate threshold and moderate threshold percentage cannot be 0.0; Either one should be defined with a value > 0.0"));

		if ((highThreshold == null) && (highThresholdPercentage == null))
			validationErrors
					.add(new String(
							"Both high threshold and high threshold percentage cannot be null; Either one should be defined with a value > 0.0"));
		else if ((highThreshold == 0.0) && (highThresholdPercentage == 0.0))
			validationErrors
					.add(new String(
							"Both high threshold and high threshold percentage cannot be 0.0; Either one should be defined with a value > 0.0"));

		if ((exceededThreshold == null)
				&& (exceededThresholdPercentage == null))
			validationErrors
					.add(new String(
							"Both exceeded threshold and exceeded threshold percentage cannot be null; Either one should be defined with a value > 0.0"));
		else if ((exceededThreshold == 0.0)
				&& (exceededThresholdPercentage == 0.0))
			validationErrors
					.add(new String(
							"Both exceeded threshold and exceeded threshold percentage cannot be 0.0; Either one should be defined with a value > 0.0"));

		if (warningThreshold < normalThreshold)
			validationErrors.add(new String(
					"Warning threshold should be >= normal threshold"));
		else if (warningThresholdPercentage < normalThresholdPercentage)
			validationErrors
					.add(new String(
							"Warning threshold percentage should be >= normal threshold percentage"));

		if (moderateThreshold < warningThreshold)
			validationErrors.add(new String(
					"Moderate threshold should be >= warning threshold"));
		else if (moderateThresholdPercentage < warningThresholdPercentage)
			validationErrors
					.add(new String(
							"Moderate threshold percentage should be >= warning threshold percentage"));

		if (highThreshold < moderateThreshold)
			validationErrors.add(new String(
					"High threshold should be >= moderate threshold"));
		else if (highThresholdPercentage < moderateThresholdPercentage)
			validationErrors
					.add(new String(
							"High threshold percentage should be >= moderate threshold percentage"));

		if (exceededThreshold < highThreshold)
			validationErrors.add(new String(
					"Exceeded threshold should be >= high threshold"));
		else if (exceededThresholdPercentage < highThresholdPercentage)
			validationErrors
					.add(new String(
							"Exceeded threshold percentage should be >= high threshold percentage"));

		return validationErrors;
	}

	public void setTelemetryData(Double intervalLoad, Date meterTimestamp) {
		if ((intervalLoad != null) && (meterTimestamp != null)) {
			if ((this.getMeterTimestamp() == null)
					|| ((this.getMeterTimestamp() != null) && (this
							.getMeterTimestamp().before(meterTimestamp)))) {
				this.setIntervalLoad(intervalLoad);
				this.setMeterTimestamp(meterTimestamp);
				if (validRules()) {
					this.calcThresholdLevels();
					this.calcThresholdPercentage();
					this.evaluateState();
				}
			}
		}
	}

	public boolean calcThresholdLevels() {
		boolean levelsUpdated = false;
		if (tariffLimit > 0) {
			if ((normalThresholdPercentage != null)
					&& (normalThresholdPercentage == 0.0)
					&& (normalThreshold != null) && (normalThreshold >= 0.0)) {
				/*
				 * normalThresholdPercentage = (normalThreshold / tariffLimit)
				 * (rateInterval / meterInterval) * 100;
				 */
				normalThresholdPercentage = (normalThreshold / tariffLimit) * 100;

				levelsUpdated = true;
			} else if ((normalThresholdPercentage != null)
					&& (normalThresholdPercentage >= 0.0)
					&& (normalThreshold != null) && (normalThreshold == 0.0)) {
				/*
				 * normalThreshold = (normalThresholdPercentage / 100)
				 * tariffLimit * (meterInterval / rateInterval);
				 */normalThreshold = (normalThresholdPercentage / 100)
						* tariffLimit;
				levelsUpdated = true;
			}

			if ((warningThresholdPercentage != null)
					&& (warningThresholdPercentage == 0.0)
					&& (warningThreshold != null) && (warningThreshold >= 0.0)) {
				/*
				 * warningThresholdPercentage = (warningThreshold / tariffLimit)
				 * (rateInterval / meterInterval) * 100;
				 */warningThresholdPercentage = (warningThreshold / tariffLimit) * 100;
				levelsUpdated = true;
			} else if ((warningThresholdPercentage != null)
					&& (warningThresholdPercentage >= 0.0)
					&& (warningThreshold != null) && (warningThreshold == 0.0)) {
				/*
				 * warningThreshold = (warningThresholdPercentage / 100)
				 * tariffLimit * (meterInterval / rateInterval);
				 */warningThreshold = (warningThresholdPercentage / 100)
						* tariffLimit;
				levelsUpdated = true;
			}

			if ((moderateThresholdPercentage != null)
					&& (moderateThresholdPercentage == 0.0)
					&& (moderateThreshold != null)
					&& (moderateThreshold >= 0.0)) {
				/*
				 * moderateThresholdPercentage = (moderateThreshold /
				 * tariffLimit) (rateInterval / meterInterval) * 100;
				 */moderateThresholdPercentage = (moderateThreshold / tariffLimit) * 100;
				levelsUpdated = true;
			} else if ((moderateThresholdPercentage != null)
					&& (moderateThresholdPercentage >= 0.0)
					&& (moderateThreshold != null)
					&& (moderateThreshold == 0.0)) {
				/*
				 * moderateThreshold = (moderateThresholdPercentage / 100)
				 * tariffLimit * (meterInterval / rateInterval);
				 */moderateThreshold = (moderateThresholdPercentage / 100)
						* tariffLimit;
				levelsUpdated = true;
			}

			if ((highThresholdPercentage != null)
					&& (highThresholdPercentage == 0.0)
					&& (highThreshold != null) && (highThreshold >= 0.0)) {
				/*
				 * highThresholdPercentage = (highThreshold / tariffLimit)
				 * (rateInterval / meterInterval) * 100;
				 */highThresholdPercentage = (highThreshold / tariffLimit) * 100;
				levelsUpdated = true;
			} else if ((highThresholdPercentage != null)
					&& (highThresholdPercentage >= 0.0)
					&& (highThreshold != null) && (highThreshold == 0.0)) {
				/*
				 * highThreshold = (highThresholdPercentage / 100) * tariffLimit
				 * (meterInterval / rateInterval);
				 */highThreshold = (highThresholdPercentage / 100)
						* tariffLimit;
				levelsUpdated = true;
			}

			if ((exceededThresholdPercentage != null)
					&& (exceededThresholdPercentage == 0.0)
					&& (exceededThreshold != null)
					&& (exceededThreshold >= 0.0)) {
				/*
				 * exceededThresholdPercentage = (exceededThreshold /
				 * tariffLimit) 100 * (rateInterval / meterInterval);
				 */exceededThresholdPercentage = (exceededThreshold / tariffLimit) * 100;
				levelsUpdated = true;
			} else if ((exceededThresholdPercentage != null)
					&& (exceededThresholdPercentage >= 0.0)
					&& (exceededThreshold != null)
					&& (exceededThreshold == 0.0)) {
				/*
				 * exceededThreshold = (exceededThresholdPercentage / 100)
				 * tariffLimit * (meterInterval / rateInterval);
				 */exceededThreshold = (exceededThresholdPercentage / 100)
						* tariffLimit;
				levelsUpdated = true;
			}

		}

		if (normalThreshold > 0)
			normalThresholdPercentage = (normalThreshold / tariffLimit) * 100;

		if (warningThreshold > 0)
			warningThresholdPercentage = (warningThreshold / tariffLimit) * 100;

		if (moderateThreshold > 0)
			moderateThresholdPercentage = (moderateThreshold / tariffLimit) * 100;

		if (highThreshold > 0)
			highThresholdPercentage = (highThreshold / tariffLimit) * 100;

		if (exceededThreshold > 0)
			exceededThresholdPercentage = (exceededThreshold / tariffLimit) * 100;
		return levelsUpdated;

	}

	private Double calcThresholdPercentage() {
		if ((intervalLoad != null) && (rateInterval != null)
				&& (meterInterval != null) && (tariffLimit != null)
				&& (intervalLoad != 0.0) && (rateInterval != 0.0)
				&& (meterInterval != 0.0) && (tariffLimit != 0.0))
			thresholdPercentage = (calcRateIntervalLoad() / tariffLimit) * 100;
		else
			thresholdPercentage = 0.0;
		return thresholdPercentage;
	}

	private Double calcRateIntervalLoad() {
		if ((intervalLoad != null) && (rateInterval != null)
				&& (meterInterval != null) && (intervalLoad != 0.0)
				&& (rateInterval != 0.0) && (meterInterval != 0.0))
			// rateIntervalLoad = intervalLoad * (rateInterval / meterInterval);
			rateIntervalLoad = intervalLoad;
		else
			rateIntervalLoad = 0.0;
		return rateIntervalLoad;
	}

	public void evaluateState() {
		resetStateDispatches();
		if (thresholdPercentage >= 0.0) {
			if ((intervalLoad >= exceededThreshold)
					|| (thresholdPercentage >= exceededThresholdPercentage)) {
				setExceededThresholdState();
			} else if ((intervalLoad >= highThreshold)
					|| (thresholdPercentage >= highThresholdPercentage)) {
				setHighThresholdState();
			} else if ((intervalLoad >= moderateThreshold)
					|| (thresholdPercentage >= moderateThresholdPercentage)) {
				setModerateThresholdState();
			} else if ((intervalLoad >= warningThreshold)
					|| (thresholdPercentage >= warningThresholdPercentage)) {
				setWarningThresholdState();
			} else if ((intervalLoad < normalThreshold)
					|| (thresholdPercentage < normalThresholdPercentage)) {
				setNormalThresholdState();

			}
		}
	}

	private void resetThresholdStates() {
		this.currentThreshold = Threshold.NA;
	}

	private void resetStateDispatches() {
		this.setNotify(false);
		this.setStartNotification(false);
		this.setEndNotification(false);

		this.setStartSignal(false);
		this.setSignalize(false);
		this.setExtendSignal(false);
		this.setModeModSignal(false);
		this.setEndSignal(false);

	}

	private void setExceededThresholdState() {
		if (!this.getDemandLimitingBlockade())
			setExceededThresholdStateDispatch();
		else
			resetStateDispatches();
		this.setCurrentMode(Mode.HIGH);
		this.setCurrentThreshold(Threshold.exceeded);
	}

	private void setExceededThresholdStateDispatch() {
		if (this.getCurrentThreshold() != Threshold.exceeded) {
			this.setNotify(true);
			this.setStartNotification(true);
		}

		if (this.getCurrentMode() == Mode.HIGH) {
			this.setSignalize(true);
			this.setExtendSignal(true);
		} else if (this.getCurrentMode() == Mode.NORMAL) {
			this.setSignalize(true);
			this.setStartSignal(true);
			this.setModeModSignal(true);
		} else if (this.getCurrentMode() == Mode.MODERATE) {
			this.setSignalize(true);
			this.setModeModSignal(true);
			this.setExtendSignal(true);
		}
	}

	private void setHighThresholdState() {
		if (!this.getDemandLimitingBlockade())
			setHighThresholdStateDispatch();
		else
			resetStateDispatches();

		this.setCurrentMode(Mode.HIGH);
		this.setCurrentThreshold(Threshold.high);
	}

	private void setHighThresholdStateDispatch() {
		if (this.getCurrentThreshold() != Threshold.high) {
			this.setNotify(true);
			this.setStartNotification(true);
		}

		if (this.getCurrentMode() == Mode.HIGH) {
			this.setSignalize(true);
			this.setExtendSignal(true);
		} else if (this.getCurrentMode() == Mode.NORMAL) {
			this.setSignalize(true);
			this.setStartSignal(true);
			this.setModeModSignal(true);
		} else if (this.getCurrentMode() == Mode.MODERATE) {
			this.setSignalize(true);
			this.setModeModSignal(true);
			this.setExtendSignal(true);
		}
	}

	private void setModerateThresholdState() {
		if (!this.getDemandLimitingBlockade())
			setModerateThresholdStateDispatch();
		else
			resetStateDispatches();
		this.setCurrentMode(Mode.MODERATE);
		this.setCurrentThreshold(Threshold.moderate);
	}

	private void setModerateThresholdStateDispatch() {
		if (this.getCurrentThreshold() != Threshold.moderate) {
			this.setNotify(true);
			this.setStartNotification(true);
		}

		if (this.getCurrentMode() == Mode.MODERATE) {
			this.setSignalize(true);
			this.setExtendSignal(true);
		} else if (this.getCurrentMode() == Mode.NORMAL) {
			this.setSignalize(true);
			this.setStartSignal(true);
			this.setModeModSignal(true);
		} else if (this.getCurrentMode() == Mode.HIGH) {
			this.setSignalize(true);
			this.setModeModSignal(true);
			this.setExtendSignal(true);
		}
	}

	private void setWarningThresholdState() {
		if (!this.getDemandLimitingBlockade())
			setWarningThresholdStateDispatch();
		else
			resetStateDispatches();
		this.setCurrentMode(Mode.NORMAL);
		this.setCurrentThreshold(Threshold.warning);
	}

	private void setWarningThresholdStateDispatch() {
		if (this.getCurrentThreshold() != Threshold.warning) {
			this.setNotify(true);
			this.setStartNotification(true);
		}

		if ((this.getCurrentMode() == Mode.MODERATE)
				|| (this.getCurrentMode() == Mode.HIGH)) {
			this.setSignalize(true);
			this.setEndSignal(true);
		}
	}

	private void setNormalThresholdState() {
		if (!this.getDemandLimitingBlockade())
			setNormalThresholdStateDispatch();
		else
			resetStateDispatches();
		this.setCurrentMode(Mode.NORMAL);
		this.setCurrentThreshold(Threshold.normal);
	}

	private void setNormalThresholdStateDispatch() {
		if ((this.getCurrentThreshold() == Threshold.warning)
				|| (this.getCurrentThreshold() == Threshold.moderate)
				|| (this.getCurrentThreshold() == Threshold.high)
				|| (this.getCurrentThreshold() == Threshold.exceeded)) {
			this.setNotify(true);
			this.setEndNotification(true);
		}

		if ((this.getCurrentMode() == Mode.MODERATE)
				|| (this.getCurrentMode() == Mode.HIGH)) {
			this.setSignalize(true);
			this.setEndSignal(true);
		}
	}

	public Boolean getDemandLimitingBlockade() {
		return demandLimitingBlockade;
	}

	public void setDemandLimitingBlockade(Boolean demandLimitingBlockade) {
		this.demandLimitingBlockade = demandLimitingBlockade;
	}

	public Double getMeterInterval() {
		return meterInterval;
	}

	public void setMeterInterval(Double meterInterval) {
		this.meterInterval = meterInterval;
	}

	public Double getTariffLimit() {
		return tariffLimit;
	}

	public void setTariffLimit(Double tariffLimit) {
		this.tariffLimit = tariffLimit;
	}

	public Double getRateInterval() {
		return rateInterval;
	}

	public void setRateInterval(Double rateInterval) {
		this.rateInterval = rateInterval;
	}

	public Double getNormalThresholdPercentage() {
		return normalThresholdPercentage;
	}

	public void setNormalThresholdPercentage(Double normalThresholdPercentage) {
		this.normalThresholdPercentage = normalThresholdPercentage;
	}

	public Double getNormalThreshold() {
		return normalThreshold;
	}

	public void setNormalThreshold(Double normalThreshold) {
		this.normalThreshold = normalThreshold;
	}

	public Double getWarningThresholdPercentage() {
		return warningThresholdPercentage;
	}

	public void setWarningThresholdPercentage(Double warningThresholdPercentage) {
		this.warningThresholdPercentage = warningThresholdPercentage;
	}

	public Double getWarningThreshold() {
		return warningThreshold;
	}

	public void setWarningThreshold(Double warningThreshold) {
		this.warningThreshold = warningThreshold;
	}

	public Double getModerateThresholdPercentage() {
		return moderateThresholdPercentage;
	}

	public void setModerateThresholdPercentage(
			Double moderateThresholdPercentage) {
		this.moderateThresholdPercentage = moderateThresholdPercentage;
	}

	public Double getModerateThreshold() {
		return moderateThreshold;
	}

	public void setModerateThreshold(Double moderateThreshold) {
		this.moderateThreshold = moderateThreshold;
	}

	public Double getHighThresholdPercentage() {
		return highThresholdPercentage;
	}

	public void setHighThresholdPercentage(Double highThresholdPercentage) {
		this.highThresholdPercentage = highThresholdPercentage;
	}

	public Double getHighThreshold() {
		return highThreshold;
	}

	public void setHighThreshold(Double highThreshold) {
		this.highThreshold = highThreshold;
	}

	public Double getExceededThresholdPercentage() {
		return exceededThresholdPercentage;
	}

	public void setExceededThresholdPercentage(
			Double exceededThresholdPercentage) {
		this.exceededThresholdPercentage = exceededThresholdPercentage;
	}

	public Double getExceededThreshold() {
		return exceededThreshold;
	}

	public void setExceededThreshold(Double exceededThreshold) {
		this.exceededThreshold = exceededThreshold;
	}

	public Boolean getDemandLimitingOptOut() {
		return demandLimitingOptOut;
	}

	public void setDemandLimitingOptOut(Boolean demandLimitingOptOut) {
		this.demandLimitingOptOut = demandLimitingOptOut;
	}

	public Date getDemandLimitingBlockadeStart() {
		return demandLimitingBlockadeStart;
	}

	public void setDemandLimitingBlockadeStart(Date demandLimitingBlockadeStart) {
		this.demandLimitingBlockadeStart = demandLimitingBlockadeStart;
	}

	public Date getDemandLimitingBlockadeEnd() {
		return demandLimitingBlockadeEnd;
	}

	public void setDemandLimitingBlockadeEnd(Date demandLimitingBlockadeEnd) {
		this.demandLimitingBlockadeEnd = demandLimitingBlockadeEnd;
	}

	public Mode getCurrentMode() {
		return currentMode;
	}

	public void setCurrentMode(Mode currentMode) {
		this.currentMode = currentMode;
	}

	public Boolean getWarning() {
		return warning;
	}

	public void setWarning(Boolean warning) {
		this.warning = warning;
	}

	public Double getEventSpan() {
		return eventSpan;
	}

	public void setEventSpan(Double eventSpan) {
		this.eventSpan = eventSpan;
	}

	public Date getMeterTimestamp() {
		return meterTimestamp;
	}

	public void setMeterTimestamp(Date meterTimestamp) {
		this.meterTimestamp = meterTimestamp;
	}

	public Double getIntervalLoad() {
		return (intervalLoad != null) ? DemandLimitingProgramParticipantState
				.roundValue(intervalLoad) : 0.0;
	}

	public void setIntervalLoad(Double intervalLoad) {
		this.intervalLoad = intervalLoad;
	}

	public Boolean getNotify() {
		return notify;
	}

	public void setNotify(Boolean notify) {
		this.notify = notify;
	}

	public Boolean getRulesDefined() {
		return rulesDefined;
	}

	public void setRulesDefined(Boolean rulesDefined) {
		this.rulesDefined = rulesDefined;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Threshold getCurrentThreshold() {
		return currentThreshold;
	}

	public void setCurrentThreshold(Threshold currentThreshold) {
		this.currentThreshold = currentThreshold;
	}

	public Double getThresholdPercentage() {
		return (thresholdPercentage != null) ? DemandLimitingProgramParticipantState
				.roundValue(thresholdPercentage) : 0.0;
	}

	public void setThresholdPercentage(Double thresholdPercentage) {
		this.thresholdPercentage = thresholdPercentage;
	}

	public void setSignalize(Boolean signalize) {
		this.signalize = signalize;
	}

	public Boolean getSignalize() {
		return signalize;
	}

	public Double getRateIntervalLoad() {
		return rateIntervalLoad;
	}

	public void setRateIntervalLoad(Double rateIntervalLoad) {
		this.rateIntervalLoad = rateIntervalLoad;
	}

	private Rule.Source getSourceEnum(String source) {
		Rule.Source sourceEnum = Rule.Source.DEMAND_LIMITING;
		if ((source != null) && (source.length() > 0)) {
			source = source.trim();
			source = source.replace(' ', '_');
			source = source.toUpperCase();
			try {
				sourceEnum = Enum.valueOf(Rule.Source.class, source);
			} catch (Exception e) {

			}
		}
		return sourceEnum;
	}

	public Boolean getEndSignal() {
		return endSignal;
	}

	public void setEndSignal(Boolean endSignal) {
		this.endSignal = endSignal;
	}

	public Boolean getStartSignal() {
		return startSignal;
	}

	public void setStartSignal(Boolean startSignal) {
		this.startSignal = startSignal;
	}

	public Boolean getExtendSignal() {
		return extendSignal;
	}

	public void setExtendSignal(Boolean extendSignal) {
		this.extendSignal = extendSignal;
	}

	public Boolean getStartNotification() {
		return startNotification;
	}

	public void setStartNotification(Boolean startNotification) {
		this.startNotification = startNotification;
	}

	public Boolean getEndNotification() {
		return endNotification;
	}

	public void setEndNotification(Boolean endNotification) {
		this.endNotification = endNotification;
	}

	public Boolean getModeModSignal() {
		return modeModSignal;
	}

	public void setModeModSignal(Boolean modeModSignal) {
		this.modeModSignal = modeModSignal;
	}

	public boolean isProgramParticipantConstrained() {
		return setConstrainedState();
	}

	private boolean setConstrainedState() {

		boolean constrained = (programParticipant == null)
				&& (programParticipant.getParticipant() == null);

		if (!constrained) {
			// if (programParticipant.getParticipant().getOptOut())
			this.setDemandLimitingOptOut(programParticipant.getOptStatus() == 1);
			// || programParticipant.getParticipant().getOptOut());
			constrained = this.getDemandLimitingOptOut();
			if ((programParticipant.getConstraint() != null)
					&& (programParticipant.getConstraint().getActiveAction() != null)
					&& (programParticipant.getConstraint().getMinActive() != null)
					&& (programParticipant.getConstraint().getMaxActive() != null)) {
				if (programParticipant.getConstraint().getActiveAction() == Constraint.Action.RESTRICT) {
					this.setDemandLimitingBlockadeStart(programParticipant
							.getConstraint().getMinActive());
					this.setDemandLimitingBlockadeEnd(programParticipant
							.getConstraint().getMaxActive());
					Calendar now = new GregorianCalendar();
					constrained = constrained
							|| (this.getDemandLimitingBlockadeEnd().after(
									now.getTime()) && this
									.getDemandLimitingBlockadeStart().before(
											now.getTime()));
				} else {
					this.setDemandLimitingBlockadeStart(null);
					this.setDemandLimitingBlockadeEnd(null);
				}
			}
		}

		this.setDemandLimitingBlockade(constrained);
		return constrained;
	}

}
