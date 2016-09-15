package com.akuacom.pss2.facdash;

import java.io.Serializable;
import java.util.Date;

import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.program.participant.DemandLimitingProgramParticipantState;
import com.akuacom.pss2.program.participant.ProgramParticipantRule;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.rule.Rule.Mode;
import com.akuacom.pss2.rule.Rule.Operator;
import com.akuacom.pss2.rule.Rule.Threshold;

public class DemandLimitingProgramParticipantSetting implements Serializable {

	private static final long serialVersionUID = 7013339919276890092L;
	/** The start. */
	private Date start;
	/** The end. */
	private Date end;

	/** The mode. */
	private Mode mode;

	/** The threshold. */
	private Threshold threshold;

	/** The variable. */
	private String variable;

	/** The operator. */
	private Operator operator;

	/** The value. */
	private Double value;

	/** The source. */
	private String source;

	/** The sort order. */
	private Integer sortOrder;

	/** The notifyAction flag. */
	private Boolean notifyAction = new Boolean(false);

	/** The signalAction flag. */
	private Boolean signalAction = new Boolean(false);
	
	private ProgramParticipantRule programParticipantRule;

	public DemandLimitingProgramParticipantSetting() {
		super();
	}
	
	public DemandLimitingProgramParticipantSetting(ProgramParticipantRule rule) {
		super();
		this.setProgramParticipantRule(rule);
	}
	
	public DemandLimitingProgramParticipantSetting(ProgramRule rule) {
		super();
		this.setProgramParticipantRule(rule);
	}

	public Date getStart() {
		return start;
	}
	
	public void setStart(Date start) {
		this.start = start;
	}
	
	public Date getEnd() {
		return end;
	}
	
	public void setEnd(Date end) {
		this.end = end;
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public Threshold getThreshold() {
		return threshold;
	}
	
	public void setThreshold(Threshold threshold) {
		this.threshold = threshold;
	}
	
	public String getVariable() {
		return variable;
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	public String getOperatorStr() {
		return operator.description();
	}
	
	public Operator getOperator() {
		return operator;
	}
	
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public Integer getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	public Boolean getNotifyAction() {
		return notifyAction;
	}
	
	public void setNotifyAction(Boolean notifyAction) {
		this.notifyAction = notifyAction;
	}
	
	public Boolean getSignalAction() {
		return signalAction;
	}
	
	public void setSignalAction(Boolean signalAction) {
		this.signalAction = signalAction;
	}
	
	public ProgramParticipantRule getProgramParticipantRule() {
		if (programParticipantRule != null) {
			programParticipantRule.setSortOrder(this.getSortOrder());
			programParticipantRule.setMode(this.getMode());
			programParticipantRule.setThreshold(this.getThreshold());
			programParticipantRule.setStart(this.getStart());
			programParticipantRule.setEnd(this.getEnd());
			programParticipantRule.setVariable(this.getVariable());
			programParticipantRule.setOperator(this.getOperator());
			programParticipantRule.setValue(this.getValue());
			programParticipantRule.setSource(this.getSource());
			programParticipantRule.setSignalAction(this.getSignalAction());
			programParticipantRule.setNotifyAction(this.getNotifyAction());			
		}			
		return programParticipantRule;
	}
	
	public void setProgramParticipantRule(ProgramParticipantRule programParticipantRule) {
		this.programParticipantRule = programParticipantRule;
		if (programParticipantRule != null) {
			this.setSortOrder(programParticipantRule.getSortOrder());
			this.setMode(programParticipantRule.getMode());
			this.setThreshold(programParticipantRule.getThreshold());
			this.setStart(programParticipantRule.getStart());
			this.setEnd(programParticipantRule.getEnd());
			this.setVariable(programParticipantRule.getVariable());
			this.setOperator(programParticipantRule.getOperator());
			this.setValue(programParticipantRule.getValue());
			this.setSource(programParticipantRule.getSource());
			this.setSignalAction(programParticipantRule.getSignalAction());
			this.setNotifyAction(programParticipantRule.getNotifyAction());			
		}
	}
	
	public void setProgramParticipantRule(ProgramRule programRule) {
		this.programParticipantRule = new ProgramParticipantRule();
		if (programParticipantRule != null) {
			this.setSortOrder(programRule.getSortOrder());
			this.setMode(programRule.getMode());
			this.setThreshold(programRule.getThreshold());
			this.setStart(programRule.getStart());
			this.setEnd(programRule.getEnd());
			this.setVariable(programRule.getVariable());
			this.setOperator(programRule.getOperator());
			this.setValue(programRule.getValue());
			this.setSource(programRule.getSource());
			this.setSignalAction(programRule.getSignalAction());
			this.setNotifyAction(programRule.getNotifyAction());			
		}
	}
	
	public void updateRuleAction() {
		this.setProgramParticipantRule(programParticipantRule);
	}

	public Boolean getEditable() {
		return (!this.getSource().equalsIgnoreCase(Rule.Source.TELEMETRY.getDescription()));
	}

	public Boolean getShowMode() {
		if (this.getMode() != null)
			return (this.getMode() != Rule.Mode.SPECIAL);
		else
			return true;
	}
	
	public String getVariableUom() {
		return DemandLimitingProgramParticipantState.getVariableUom(DemandLimitingProgramParticipantState.Variable.valueOf(this.getVariable()));
	}

}
