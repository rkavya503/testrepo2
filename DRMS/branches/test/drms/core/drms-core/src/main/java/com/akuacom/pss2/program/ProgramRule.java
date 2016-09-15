package com.akuacom.pss2.program;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.pss2.rule.Rule;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "program_rule")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ProgramRule extends Rule {

	private static final long serialVersionUID = 8008727317475044425L;

	/** The program */
	@ManyToOne
	@JoinColumn(name = "program_uuid")
	private Program program;

	/**
	 * Gets the program participant.
	 * 
	 * @return the program participant
	 */
	public Program getProgram() {
		return program;
	}

	/**
	 * Sets the program participant.
	 * 
	 * @param Program
	 *            the new program participant
	 */
	public void setProgram(Program program) {
		this.program = program;
	}

	public ProgramRule copy(ProgramRule existing, Program prog) {
		ProgramRule cloned = new ProgramRule();
		cloned.setEnd(existing.getEnd());
		cloned.setMode(existing.getMode());
		cloned.setOperator(existing.getOperator());
		cloned.setSortOrder(existing.getSortOrder());
		cloned.setSource(existing.getSource());
		cloned.setStart(existing.getStart());
		cloned.setProgram(prog);
		cloned.setThreshold(existing.getThreshold());
		cloned.setNotifyAction(existing.getNotifyAction());
		cloned.setSignalAction(existing.getSignalAction());

		return cloned;
	}
}
