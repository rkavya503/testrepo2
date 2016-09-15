package com.akuacom.pss2.data.usage;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

public interface DataSourceUsageEAO extends DataSourceUsageGenEAO {

	@Remote
	public interface R extends DataSourceUsageEAO {
	}

	@Local
	public interface L extends DataSourceUsageEAO {
	}

	void updateGapAndLastActual(String dataSourceId, Date date, Date lastPoint,double maxGap);
	
	void updateBaselineState(String dataSourceId, Date date,
			boolean hasBaseline, Boolean adjusted, String eventName);
	
	List<DataSourceUsageVo> findAllDatasourceByDate(Date date);
}
