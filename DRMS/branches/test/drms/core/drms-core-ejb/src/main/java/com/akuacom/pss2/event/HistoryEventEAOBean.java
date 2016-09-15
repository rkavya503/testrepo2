package com.akuacom.pss2.event;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.akuacom.pss2.history.HistoryEvent;
import com.akuacom.pss2.history.HistoryEventGenEAOBean;

@Stateless
public class HistoryEventEAOBean extends HistoryEventGenEAOBean implements HistoryEventEAO.R, HistoryEventEAO.L{
	
	public HistoryEventEAOBean() {
		super(HistoryEvent.class);
	}

	@Override
	public List<HistoryEvent> findByAggregatorProgramAndDate(Date startTime,
			Date endTime, List<String> participantNames,
			List<String> programNames) {
		Query q = em.createNamedQuery( "HistoryEvent.findByAggregatorProgramAndDate" );
		q.setParameter("startTime", startTime);
		q.setParameter("endTime", endTime);
		q.setParameter("participantNames", participantNames);
		q.setParameter("programNames", programNames);
		return q.getResultList();
	}

}
