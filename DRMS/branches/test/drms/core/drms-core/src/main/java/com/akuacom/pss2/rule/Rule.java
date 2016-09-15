/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.RuleEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.rule;

import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.annotations.DubiousColumnDefinition;

/**
 * The Base Class Rule.
 */
@SuppressWarnings("serial") //define serial in subclasses.
@MappedSuperclass
public class Rule extends VersionedEntity {

    private static final long serialVersionUID = -7997155710053876283L;
	/**
	 * The Enum Mode.
	 */
	public enum Mode {
		NORMAL, MODERATE, HIGH, SPECIAL
	}

	/**
	 * The Enum Operator.
	 */
	public enum Operator {
		ALWAYS("ALWAYS"), LESS_THAN("<"), LESS_THAN_OR_EQUAL("<="), EQUAL("="), GREATER_THAN(
				">"), GREATER_THAN_OR_EQUAL(">="), NOT_EQUAL("NOT EQUAL");

		private final String description;

		Operator(String desc) {
			this.description = desc;
		}

		public String description() {
			return this.description;
		}
	}

	public enum Source {
		CUSTOM("Custom", 0),
        BID_MAPPING("Bid Mapping", 10000),
        SCERTP_SHED_STRATEGY("SCE RTP Strategy", 20000),
        PROGRAM("Program", 30000),
        CPP_SHED_STRATEGY("CPP Strategy", 40000),
        TELEMETRY("Telemetry", 50000),
        DEMAND_LIMITING("Demand Limiting", 60000),
        DEMAND_LIMITING_PERCENTAGE("Demand Limiting Percentage", 70000),
        DEMAND_LIMITING_LOAD("Demand Limiting Load", 80000),
        DEMAND_LIMITING_PARTICIPANT("Demand Limiting Participant", 80000);

		private final String description;
		private final int offset;

		Source(String description, int offset) {
			this.description = description;
            this.offset = offset;
		}

		public String getDescription() {
			return this.description;
		}

        public int getOffset() {
			return this.offset;
		}
	}

	/**
	 * Threshold Enum.
	 */
	public enum Threshold {
		normal("normal"), warning("warning"), moderate("moderate"), high("high"), exceeded("exceeded"), NA("not applicable");

		private final String description;

		Threshold(String description) {
			this.description = description;
		}

		public String getDescription() {
			return this.description;
		}
	}

	/** The start. */
	private Date start;

	/** The end. */
	private Date end;

	/** The mode. */
	@Enumerated(EnumType.STRING)
	private Mode mode;

	/** The threshold. */
	@Enumerated(EnumType.STRING)
	private Threshold threshold = Threshold.NA;

	/** The variable. */
	private String variable;

	/** The operator. */
	@Enumerated(EnumType.STRING)
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

	/**
	 * Gets the mode.
	 * 
	 * @return the mode
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * Sets the mode.
	 * 
	 * @param mode
	 *            the new mode
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	/**
	 * Gets the threshold.
	 * 
	 * @return the threshold
	 */
	public Threshold getThreshold() {
		return threshold;
	}

	/**
	 * Sets the threshold.
	 * 
	 * @param mode
	 *            the new threshold
	 */
	public void setThreshold(Threshold threshold) {
		this.threshold = threshold;
	}

	/**
	 * Gets the variable.
	 * 
	 * @return the variable
	 */
	public String getVariable() {
		return variable;
	}

	/**
	 * Sets the variable.
	 * 
	 * @param variable
	 *            the new variable
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}

	/**
	 * Gets the operator.
	 * 
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 * 
	 * @param operator
	 *            the new operator
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * Gets the start.
	 * 
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * Sets the start.
	 * 
	 * @param start
	 *            the new start
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * Gets the end.
	 * 
	 * @return the end
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * Sets the end.
	 * 
	 * @param end
	 *            the new end
	 */
	public void setEnd(Date end) {
		this.end = end;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * Gets the source.
	 * 
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 * 
	 * @param source
	 *            the new source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Gets the sort order.
	 * 
	 * @return the sort order
	 */
	public Integer getSortOrder() {
		return sortOrder;
	}

	/**
	 * Sets the sort order.
	 * 
	 * @param sortOrder
	 *            the new sort order
	 */
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
		result = prime * result
				+ ((sortOrder == null) ? 0 : sortOrder.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result
				+ ((variable == null) ? 0 : variable.hashCode());
		result = prime * result
				+ ((threshold == null) ? 0 : threshold.hashCode());
		result = prime * result
				+ ((notifyAction == null) ? 0 : notifyAction.hashCode());
		result = prime * result
				+ ((signalAction == null) ? 0 : signalAction.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Rule other = (Rule) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (mode == null) {
			if (other.mode != null)
				return false;
		} else if (!mode.equals(other.mode))
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (variable == null) {
			if (other.variable != null)
				return false;
		} else if (!variable.equals(other.variable))
			return false;
		if (threshold == null) {
			if (other.threshold != null)
				return false;
		} else if (!threshold.equals(other.threshold))
			return false;
		if (notifyAction == null) {
			if (other.notifyAction != null)
				return false;
		} else if (!notifyAction.equals(other.notifyAction))
			return false;
		if (signalAction == null) {
			if (other.signalAction != null)
				return false;
		} else if (!signalAction.equals(other.signalAction))
			return false;
		return true;
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

	public static class SortOrderComparator implements Comparator<Rule> {
		public int compare(Rule o1, Rule o2) {
			return o1.getSortOrder().compareTo(o2.getSortOrder());
		}
	}
}
