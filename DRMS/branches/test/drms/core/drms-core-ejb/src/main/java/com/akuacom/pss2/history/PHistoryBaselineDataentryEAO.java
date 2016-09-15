package com.akuacom.pss2.history;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.pss2.data.DateRange;

public interface PHistoryBaselineDataentryEAO extends BaseEAO<PHistoryBaselineDataentry>{
	@Remote
	public interface R extends PHistoryBaselineDataentryEAO {
	}

	@Local
	public interface L extends PHistoryBaselineDataentryEAO {
	}
	
	void clear(String pdatasources,DateRange dateRange);
	void generateShedForEventParticipant(Date date);
	int insertOrUpdate(PHistoryBaselineDataentry entry);
}
