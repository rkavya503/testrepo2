package com.akuacom.pss2.data.usage;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.utils.DateUtil;

@Stateless
public class DataSourceUsageEAOBean extends DataSourceUsageGenEAOBean implements
		DataSourceUsageEAO.L, DataSourceUsageEAO.R {
	
	Logger log = Logger.getLogger(DataSourceUsageEAOBean.class);
	
	@EJB
	protected Pss2SQLExecutor.L sqlExecutor;
	
	@Override
	public void updateGapAndLastActual(String dataSourceId, Date date,Date lastpoint,
			double maxGap) {
		date = DateUtil.stripTime(date);
		//Date end = DateUtil.endOfDay(date);
		String sqltemplate =" INSERT datasource_usage(uuid,datasource_uuid,date,maxgap,lastactual,baseline_state,creationTime) "
			+" \n VALUES( REPLACE(UUID(), '-', ''),${datasource_uuid},${date},${maxgap}, ${lastpoint},0 ,NOW() )"
			+" \n ON DUPLICATE KEY UPDATE maxgap=${maxgap}, lastactual=${lastpoint} ";
		
		//Date today = DateUtil.stripTime(new Date());
	    Map<String,Object> params = new HashMap<String,Object>();
	    /*SQLWord table = new SQLWord("dataentry_temp");
	    if(today.after(date)){
	    	table = new SQLWord("dataentry");
	    }*/
	    //params.put("usagetable", table);
	    params.put("datasource_uuid", dataSourceId);
	    params.put("date", date);
	   //params.put("end", end);
	    params.put("maxgap", maxGap);
	    params.put("lastpoint", lastpoint);
	    try{
	    	String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
	    	sqlExecutor.execute(sql,params);
	    }catch (Exception e){
	    	log.error(e.getMessage(), e);
	    	throw new EJBException(e);
	    }
	}
	
	@Override
	public void updateBaselineState(String dataSourceId, Date date,
			boolean hasBaseline, Boolean adjusted, String eventName) {
		date = DateUtil.stripTime(date);
		String sqltemplate = null;
		if(adjusted !=null){
			sqltemplate =" INSERT INTO datasource_usage(uuid,datasource_uuid,date,maxgap,lastactual,baseline_state, creationTime, adjusted, eventName) " 
					+ " \nVALUES (REPLACE(UUID(), '-', ''), ${datasource_uuid}, ${date}, ${maxgap}, ${lastactual}, ${hasBaseline},NOW(),${adjusted},${eventName} )" 
					+ " \nON DUPLICATE KEY UPDATE baseline_state=${hasBaseline}, adjusted=${adjusted}, eventName =${eventName} ";
		}else{
			sqltemplate =" INSERT INTO datasource_usage(uuid,datasource_uuid,date,maxgap,lastactual,baseline_state, creationTime) " 
					+ " \nVALUES (REPLACE(UUID(), '-', ''), ${datasource_uuid}, ${date}, ${maxgap}, ${lastactual}, ${hasBaseline},NOW() )" 
					+ " \nON DUPLICATE KEY UPDATE baseline_state=${hasBaseline} ";
		}

		
	    Map<String,Object> params = new HashMap<String,Object>();
	    params.put("datasource_uuid", dataSourceId);
	    params.put("date", date);
	    params.put("maxgap", 1);
	    params.put("lastactual", null);
	    params.put("hasBaseline", hasBaseline?1:0);
	    if(adjusted !=null){
	    	params.put("adjusted", adjusted?1:0);
	    	params.put("eventName",eventName);
	    }
	    
	    try{
	    	String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
	    	sqlExecutor.execute(sql,params);
	    }catch (Exception e){
	    	log.error(e.getMessage(), e);
	    	throw new EJBException(e);
	    }
	}
	
	@Override
	public List<DataSourceUsageVo> findAllDatasourceByDate(Date date) {
		List<DataSourceUsageVo> result = null;
		date = DateUtil.stripTime(date);
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("  SELECT ds.ownerID, dus.adjusted, dus.eventName FROM datasource ds  ");
		sqlBuilder.append("  LEFT JOIN   ");
		sqlBuilder.append("  (  ");
		sqlBuilder.append("  SELECT datasource_uuid, adjusted, eventName FROM datasource_usage WHERE DATE = ${date}  ");
		sqlBuilder.append("  )dus  ");
		sqlBuilder.append("  ON ds.uuid = dus.datasource_uuid  ");
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("date", date);
		try{
			String parameterizedSQL = SQLBuilder.buildNamedParameterSQL(sqlBuilder.toString(), params);
			result = sqlExecutor.doNativeQuery(parameterizedSQL,params,
					new ListConverter<DataSourceUsageVo>(
							new ColumnAsFeatureFactory<DataSourceUsageVo>(
									DataSourceUsageVo.class)));
	    }catch (Exception e){
	    	log.error(e.getMessage(), e);
	    	throw new EJBException(e);
	    }
		
		return (result == null? Collections.EMPTY_LIST:result);
		
	}
	
}
