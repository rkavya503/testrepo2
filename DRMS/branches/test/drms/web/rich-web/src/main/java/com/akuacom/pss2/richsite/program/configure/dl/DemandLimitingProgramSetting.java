package com.akuacom.pss2.richsite.program.configure.dl;

import java.util.Date;
import com.akuacom.pss2.program.ProgramRule;
import com.akuacom.pss2.program.participant.DemandLimitingProgramParticipantState;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.rule.Rule.Mode;
import com.akuacom.pss2.rule.Rule.Operator;
import com.akuacom.pss2.rule.Rule.Threshold;

public class DemandLimitingProgramSetting {

	/** The start. */
	private Date start;
	/** The end. */
	private Date end;

	/** The mode. */
	private Mode mode = Mode.SPECIAL;

	/** The threshold. */
	private Threshold threshold = Threshold.NA;

	/** The variable. */
	private String variable;

	/** The operator. */
	private Operator operator;

	/** The value. */
	private Double value = 0.0;

	/** The source. */
	private String source;

	/** The sort order. */
	private Integer sortOrder;

	/** The notifyAction flag. */
	private Boolean notifyAction = new Boolean(false);

	/** The signalAction flag. */
	private Boolean signalAction = new Boolean(false);
	
	private ProgramRule programRule;

	public DemandLimitingProgramSetting() {
		super();
	}
	
	public DemandLimitingProgramSetting(ProgramRule rule) {
		super();
		this.setProgramRule(rule);
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
	
	public ProgramRule getProgramRule() {
		if (programRule != null) {
			programRule.setSortOrder(this.getSortOrder());
			programRule.setMode(this.getMode());
			programRule.setThreshold(this.getThreshold());
			programRule.setStart(this.getStart());
			programRule.setEnd(this.getEnd());
			programRule.setVariable(this.getVariable());
			programRule.setOperator(this.getOperator());
			programRule.setValue(this.getValue());
			programRule.setSource(this.getSource());
			programRule.setSignalAction(this.getSignalAction());
			programRule.setNotifyAction(this.getNotifyAction());			
		}			
		return programRule;
	}
	
	public void setProgramRule(ProgramRule programRule) {
		this.programRule = programRule;
		if (programRule != null) {
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
		this.setProgramRule(programRule);
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
