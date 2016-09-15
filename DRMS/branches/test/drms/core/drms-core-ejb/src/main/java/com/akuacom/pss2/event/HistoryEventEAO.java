package com.akuacom.pss2.event;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.history.HistoryEvent;
import com.akuacom.pss2.history.HistoryEventGenEAO;

public interface HistoryEventEAO extends HistoryEventGenEAO {
	@Remote
	public interface R extends HistoryEventEAO {
	}

	@Local
	public interface L extends HistoryEventEAO {
    }

	List<HistoryEvent> findByAggregatorProgramAndDate(Date startTime, Date endTime, List<String> participantNames, List<String> programNames);
}
