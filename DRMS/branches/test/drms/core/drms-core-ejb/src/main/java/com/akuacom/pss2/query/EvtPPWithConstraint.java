package com.akuacom.pss2.query;

import com.akuacom.pss2.program.participant.Constraint;

public class EvtPPWithConstraint extends EvtParticipantCandidate {

	private static final long serialVersionUID = 6252989458299466415L;

	private Constraint constraint;

	public Constraint getConstraint() {
		return constraint;
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}
}
