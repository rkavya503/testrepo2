package com.akuacom.pss2.query;

import java.io.Serializable;
import java.util.List;

public interface EventEnrollingItem extends Serializable {
	
	List<EvtParticipantCandidate> getLegibleEventParticipants();
	
	double getRegisterShed();
	
	double getAvailableShed();
	
}
