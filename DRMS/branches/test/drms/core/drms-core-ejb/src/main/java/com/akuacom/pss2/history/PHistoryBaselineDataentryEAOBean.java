package com.akuacom.pss2.history;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.akuacom.ejb.BaseEAOBean;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.data.DateRange;

@Stateless
public class PHistoryBaselineDataentryEAOBean extends BaseEAOBean<PHistoryBaselineDataentry> implements
PHistoryBaselineDataentryEAO.R, PHistoryBaselineDataentryEAO.L  {
	private static final Logger log = Logger.getLogger(PHistoryBaselineDataentryEAOBean.class);
	@EJB
	protected Pss2SQLExecutor.L sqlExecutor;

	public PHistoryBaselineDataentryEAOBean() {
		super(PHistoryBaselineDataentry.class);
	}

	@Override
	public void clear(String pdatasources,DateRange dateRange) {
        if(DateUtils.isSameDay(dateRange.getStartTime(), new Date())){
			clearBaseline(pdatasources);
	        clearHistoryBaseline(pdatasources, dateRange);// clear legacy data in history_baseline_dataentry
        }else{
            clearHistoryBaseline(pdatasources, dateRange);
        }
	}

	private void clearBaseline(String pdatasources) {
		Query query = em.createNativeQuery("delete from baseline_dataentry_temp where datasource_uuid=?" );
		query.setParameter(1, pdatasources);
		query.executeUpdate();
	}

	private void clearHistoryBaseline(String pdatasources, DateRange dateRange) {
		Query query = em.createNativeQuery("delete from history_baseline_dataentry where time >=? and time <=? and datasource_uuid=? " );
		query.setParameter(1, dateRange.getStartTime());
		query.setParameter(2, dateRange.getEndTime());
		query.setParameter(3, pdatasources);
		query.executeUpdate();
	}

	@Override
	public void generateShedForEventParticipant(Date date) {
		final Query query = em.createNativeQuery("{call calculateShedForEventParticipant(?,?,?)}" );
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		query.setParameter(1, cal.get(Calendar.YEAR));
		query.setParameter(2, cal.get(Calendar.MONTH)+1);
		query.setParameter(3, cal.get(Calendar.DATE));
		
		query.executeUpdate();
		
	}
	@Override
    public int insertOrUpdate(PHistoryBaselineDataentry entry) {
	    String tableName = "";
	    if(DateUtils.isSameDay(new Date(), entry.getTime())||new Date().before(entry.getTime())){
	            tableName = "baseline_dataentry_temp";
	    }else{
	            tableName = "history_baseline_dataentry";
	    }
	    String sqltemplate = null;
	    sqltemplate = " INSERT INTO " +tableName
	    +" (uuid, dataset_uuid,datasource_uuid, time,value,creationTime,valueType,actual) " +
	    " VALUES (REPLACE(UUID(), '-', ''), ${dataset_uuid}, ${datasource_uuid},  ${time}, ${value}, now(), ${valueType}, " +
	    " ${actual}) ON DUPLICATE KEY UPDATE VALUE= ${value}; ";
	    Map<String,Object> params = new HashMap<String,Object>();
	    params.put("dataset_uuid", entry.getDataSetUUID());
	    params.put("datasource_uuid", entry.getDataSourceUUID());
	    params.put("time", entry.getTime());
	    params.put("value", entry.getValue());
	    params.put("valueType", entry.getValueType());
	    params.put("actual", entry.isActual());
	    try{
	        String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
	        return sqlExecutor.execute(sql,params);
	    }catch (Exception e){
	        log.error(e.getMessage(),e);
	        throw new EJBException(e);
	    }
	}
	

}
