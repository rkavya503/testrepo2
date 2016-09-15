/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.jdbc.CellConverter;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.data.usage.UsageSummary;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class PDataEntryEAOBean extends PDataEntryGenEAOBean implements
        PDataEntryEAO.R, PDataEntryEAO.L {
	
	private static final Logger log = Logger.getLogger(PDataEntryEAOBean.class);
	 
	@EJB
	protected Pss2SQLExecutor.L sqlExecutor;
	
	  
    public PDataEntryEAOBean() {
        super(PDataEntry.class);
    }

    public List<String> getDatasetNamesByOwnerID(String ownerID)
            throws EntityNotFoundException {
        final Query query = em.createNamedQuery(
                "PDataEntry.findDatasetNameByDatasourceOwner").setParameter(
                "ownerID", ownerID);
        return query.getResultList();
    }

    public List<java.util.Date> getDataDays(List<String> datasetUUIDs,
            String datasourceUUID, DateRange range)
            throws EntityNotFoundException {
        List<Object> values = new ArrayList<Object>();
        String strQuery = "select distinct(date(de.time)) as dates  from PDataEntry de "
                + "where  de.datasource.UUID = ?1 " + "and (";
        values.add(datasourceUUID);
        int i = 1;
        for (String datasetUUID : datasetUUIDs) {
            i++;
            strQuery = strQuery
                    + (i > 2 ? (" or de.dataSet.UUID = ?" + i)
                            : (" de.dataSet.UUID = ?" + i));
            values.add(datasetUUID);
        }

        strQuery = strQuery + ") ";
        if (range != null) {
            if (range.getStartTime() != null) {
                i++;
                strQuery = strQuery + " and de.time >= ?" + i;
                values.add(range.getStartTime().getTime());
            }
            if (range.getEndTime() != null) {
                i++;
                strQuery = strQuery + " and de.time < ?" + i;
                values.add(range.getEndTime().getTime());
            }
        }

        strQuery = strQuery + " order by date(de.time) ";

        Query query = em.createQuery(strQuery);
        if (values != null || values.size() > 0) {
            for (int j = 0; j < values.size(); j++)
                query.setParameter(j + 1, values.get(j));
        }

        return query.getResultList();
    }
    
    public Date getLastestContact(String datasourceUUID, String datasetUUID)
            throws EntityNotFoundException {
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("SELECT MAX(lastComm) AS lastTime FROM(    ");
    	sb.append("SELECT MAX(TIME) lastComm FROM dataentry_temp WHERE dataset_uuid='"+datasetUUID+"' AND datasource_uuid='"+datasourceUUID+"' ");
    	sb.append("    UNION    ");
    	sb.append("SELECT MAX(TIME) lastComm FROM dataentry WHERE dataset_uuid='"+datasetUUID+"' AND datasource_uuid='"+datasourceUUID+"' ");
    	sb.append(") table1");
    	Date last = null;		
    	try {
    		last = sqlExecutor.doNativeQuery(sb.toString(), new CellConverter<Date>(Date.class));
		} catch (SQLException e) {
			throw new EntityNotFoundException(e);
		}
    	
        return last;
    }

    public List<PDataEntry> getLatestDataEntry(String datasourceUUID,
            List<String> dataSetUUIDs) throws EntityNotFoundException {
        if (dataSetUUIDs == null || dataSetUUIDs.size() <= 0) {
            return null;
        }

        String sql = "select de from PDataEntry de where de.datasource.UUID = '"
                + datasourceUUID + "' and (";
        for (int i = 0; i < dataSetUUIDs.size(); i++) {
            if (i > 0)
                sql = sql + " or ";
            String pdUUID = dataSetUUIDs.get(i);
            sql = sql
                    + " de.time = (select max(de1.time) from PDataEntry de1 where de1.dataSet.UUID= "
                    + pdUUID + " and de1.datasource.UUID = '" + datasourceUUID
                    + "')";
        }
        sql = sql + ") group by de.dataSet.UUID";
        Query query = em.createQuery(sql);
        return query.getResultList();
    }

    public PDataEntry getLatestDataEntry(String datasourceUUID,
            String dataSetUUID, Date time) throws EntityNotFoundException {
        if (datasourceUUID == null || dataSetUUID == null) {
            return null;
        }
        Date last = this.getPreviousTimePoint(datasourceUUID, dataSetUUID, time);
        if(last==null) return null;
        
        StringBuffer sb = new StringBuffer();
    	sb.append("SELECT uuid,time,value,actual FROM dataentry_temp WHERE dataset_uuid='"+dataSetUUID+"' AND datasource_uuid='"+datasourceUUID+"' AND time=${lastTime} ");
    	sb.append("    UNION    ");
    	sb.append("SELECT uuid,time,value,actual FROM dataentry WHERE dataset_uuid='"+dataSetUUID+"' AND datasource_uuid='"+datasourceUUID+"' AND time=${lastTime} ");
    	List<PDataEntry> result = null;
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("lastTime", last);
    	try {
    		result = sqlExecutor.doNativeQuery(sb.toString(), params, new ListConverter<PDataEntry>(
					new ColumnAsFeatureFactory<PDataEntry>(
					PDataEntry.class)));
		} catch (SQLException e) {
			throw new EntityNotFoundException(e);
		}
    	return (result==null||result.isEmpty())?null:result.iterator().next();
    }

    public List<PDataEntry> getDataEntryForThreeTen(String datasourceUUID,
            int numberOfDays, Date endDate) {
        if (datasourceUUID == null) {
            return null;
        }

        final Query query = em.createNamedQuery("PDataEntry.findTenThree")
                .setParameter("sourceUUID", datasourceUUID)
                .setParameter("end", endDate);
        query.setMaxResults(numberOfDays);
        List<PDataEntry> ret = query.getResultList();
        ret.size();
        return ret;
    }

    public List<PDataEntry> getDataEntryForByDataSourceUUIDAndDate(
            String datasourceUUID, Date date) {
        if (datasourceUUID == null) {
            return null;
        }

        final Query query = em
                .createNamedQuery("PDataEntry.findByDataSourceUUIDAndDate")
                .setParameter("sourceUUID", datasourceUUID)
                .setParameter("day", date);
        List<PDataEntry> ret = query.getResultList();
        ret.size();
        return ret;
    }

    public List<PDataEntry> getDataEntryForByDataSourceUUIDsAndDates(
            List<String> datasourceUUIDs, Date start, Date end) {
        if (datasourceUUIDs == null || datasourceUUIDs.size() == 0) {
            return null;
        }

        if (datasourceUUIDs.size() == 1) {
            return getDataEntryForByDataSourceUUIDAndDates(
                    datasourceUUIDs.get(0), start, end);
        }

        List<PDataEntry> ret = new ArrayList<PDataEntry>();

        List<Object> values = new ArrayList<Object>();

        String sql = "select count(de.time) as numPoint, sum(de.value), de.time from PDataEntry de where de.datasource.UUID in (";
        for (int i = 0; i < datasourceUUIDs.size(); i++) {
            if (i > 0)
                sql = sql + ", ";
            String datasourceUUID = datasourceUUIDs.get(i);
            sql = sql + "'" + datasourceUUID + "'";
        }
        sql = sql
                + ") and de.time >= ? and de.time <= ? group by de.time order by de.time";
        values.add(start);
        values.add(end);

        try {
            Query query = em.createQuery(sql);
            for (int i = 0; i < values.size(); i++)
                query.setParameter(i + 1, values.get(i));

            List list = query.getResultList();
            for (int i = 0; i < list.size(); i++) {
                Object[] arr = (Object[]) list.get(i);
                long count = (Long) arr[0];
                Double value = (Double) arr[1];
                Date time = (Date) arr[2];
                // if(count == datasourceUUIDs.size())
                if (true) {
                    PDataEntry pde = new PDataEntry();
                    pde.setValue(value);
                    pde.setTime(time);
                    ret.add(pde);
                }
            }
            return ret;
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<PDataEntry> getDataEntryForByDataSourceUUIDAndDates(
            String datasourceUUID, Date start, Date end) {
        if (datasourceUUID == null) {
            return null;
        }

        final Query query = em
                .createNamedQuery("PDataEntry.findByDataSourceUUIDAndDates")
                .setParameter("sourceUUID", datasourceUUID)
                .setParameter("begin", start).setParameter("end", end);
        List<PDataEntry> ret = query.getResultList();
        ret.size();
        return ret;
    }

    public List<PDataEntry> getDataEntryForByDataSourceUUID(
            String datasourceUUID) {
        if (datasourceUUID == null) {
            return null;
        }

        final Query query = em.createNamedQuery(
                "PDataEntry.findByDataSourceUUIDAndDates").setParameter(
                "sourceUUID", datasourceUUID);
        List<PDataEntry> ret = query.getResultList();
        ret.size();
        return ret;
    }

    public List<PDataEntry> getDataEntryForByDataSourceUUIDs(
            List<String> datasourceUUIDs) {
        return null;
    }

    public List<UsageSummary> getUsageReport(Map<String, String> dsUUIDs,
            Date start, Date end) {
        double hours = (end.getTime() - start.getTime()) / 3600000.0;
        String sql = "select avg(de.value), de.datasource.UUID from PDataEntry de where de.datasource.UUID in (";
        int i = 0;
        for (String datasourceUUID : dsUUIDs.keySet()) {
            if (i > 0)
                sql = sql + ", ";
            sql = sql + "'" + datasourceUUID + "'";
            i++;
        }
        sql = sql
                + ") and de.time >= ? and de.time <= ? group by de.datasource.UUID";
        List<Object> values = new ArrayList<Object>();
        values.add(start);
        values.add(end);

        List<UsageSummary> ret = new ArrayList<UsageSummary>();

        try {
            Query query = em.createQuery(sql);
            for (int j = 0; j < values.size(); j++)
                query.setParameter(j + 1, values.get(j));
            List list = query.getResultList();
            if (list == null) {
                return ret;
            }
            for (Object obj : list) {
                Object[] arr = (Object[]) obj;
                UsageSummary us = null;

                if (arr != null) {
                    us = new UsageSummary();
                    String uuid = (String) arr[1];
                    String partName = dsUUIDs.get(uuid);
                    us.setName(partName);

                    if (arr != null && arr[0] != null) {
                        us.setAverage((Double) arr[0]);
                        us.setTotal(us.getAverage() * hours);
                    }
                }
                ret.add(us);
            }

            return ret;
        } catch (NoResultException e) {
            return null;
        }
    }

    public UsageSummary getUsageSummary(List<String> dsUUIDs, Date start,
            Date end) {
        double hours = (end.getTime() - start.getTime()) / 3600000.0;
        String sql = "select avg(de.value) from PDataEntry de where de.datasource.UUID in (";
        for (int i = 0; i < dsUUIDs.size(); i++) {
            if (i > 0)
                sql = sql + ", ";
            String datasourceUUID = dsUUIDs.get(i);
            sql = sql + "'" + datasourceUUID + "'";
        }
        sql = sql + ") and de.time >= ? and de.time <= ? ";
        List<Object> values = new ArrayList<Object>();
        values.add(start);
        values.add(end);

        try {
            Query query = em.createQuery(sql);
            for (int i = 0; i < values.size(); i++)
                query.setParameter(i + 1, values.get(i));

            Object obj = query.getSingleResult();
            UsageSummary us = null;
            if (obj != null) {
                us = new UsageSummary();
                us.setAverage((Double) obj);
                us.setTotal(us.getAverage() * hours);
            }

            return us;
        } catch (NoResultException e) {
            return null;
        }
    }

	@Override
	public void deleteDataEntryByDatasource(String datasourceUUID) {
		
		final Query query = em.createNamedQuery(
        "PDataEntry.deleteByDatasource").setParameter(
        "sourceUUID", datasourceUUID);
		
		query.executeUpdate();
	}
	
	/**
	 * Return UsageSummary including sum and avg
	 * @param dsUUIDs
	 * @param start
	 * @param end
	 * @return
	 */
    @Override
     public List<UsageSummary> getUsageSumReport(Map<String, String> dsUUIDs,
                Date start, Date end) {
            String sql = "select sum(de.value), de.datasource.UUID, avg(de.value) from PDataEntry de where de.datasource.UUID in (";
        int i = 0;
        for (String datasourceUUID : dsUUIDs.keySet()) {
            if (i > 0)
                sql = sql + ", ";
            sql = sql + "'" + datasourceUUID + "'";
            i++;
        }
        sql = sql
                + ") and de.time >= ? and de.time <= ? group by de.datasource.UUID";
        List<Object> values = new ArrayList<Object>();
        values.add(start);
        values.add(end);
    
        List<UsageSummary> ret = new ArrayList<UsageSummary>();
    
        try {
            Query query = em.createQuery(sql);
            for (int j = 0; j < values.size(); j++)
                query.setParameter(j + 1, values.get(j));
            List list = query.getResultList();
            if (list == null) {
                return ret;
            }
            for (Object obj : list) {
                Object[] arr = (Object[]) obj;
                UsageSummary us = null;
    
                if (arr != null) {
                    us = new UsageSummary();
                    String uuid = (String) arr[1];
                    String partName = dsUUIDs.get(uuid);
                    us.setName(partName);
    
                    if (arr != null && arr[0] != null) {
                    	us.setAverage((Double) arr[2]);
                        us.setTotal((Double) arr[0]);
                    }
                }
                ret.add(us);
            }
    
            return ret;
        } catch (NoResultException e) {
            return null;
        }
    }
	
    public long deleteSyntheticDups() {
        List<String> ids = em.createNamedQuery("PDataEntry.getSyntheticDups").getResultList();
        return ids.isEmpty() ? 0 : deleteSyntheticDups(ids);
    }
    


	@Override
	public List<PDataEntry> getDataEntryList(String datasetUUID,
	        String datasourceUUID, DateRange range)
	        throws EntityNotFoundException {
	    String strQuery = "select de from PDataEntry de where 0=0 ";
	    int i = 0;
	
	    List<Object> values = new ArrayList<Object>();
	    if (datasetUUID != null) {
	        i++;
	        strQuery = strQuery + " and de.dataSet.UUID = ?" + i;
	        values.add(datasetUUID);
	    }
	    if (datasourceUUID != null) {
	        i++;
	        strQuery = strQuery + " and de.datasource.UUID = ?" + i;
	        values.add(datasourceUUID);
	    }
	    if (range != null && range.getStartTime() != null) {
	        final Date startTime = new Date(range.getStartTime().getTime());
	        // final long startTime = range.getStartTime().getTime();
	        i++;
	        strQuery = strQuery + " and de.time >= ?" + i;
	        values.add(startTime);
	    }
	    if (range != null && range.getEndTime() != null) {
	        final Date endTime = new Date(range.getEndTime().getTime());
	        // final long endTime = range.getEndTime().getTime();
	        i++;
	        strQuery = strQuery + " and de.time < ?" + i;
	        values.add(endTime);
	    }
	    strQuery = strQuery + " order by de.time";
	    
	    return getByFilters(strQuery, values);
	
	}

    @Override
    public Date getLastActualTime(List<String> dsUUIDs, DateRange range) 
    {
        List<Date> list= em.createNamedQuery("PDataEntry.getLastActualTime")
	    .setParameter("sIds", dsUUIDs).getResultList();
	
        if(null==list||list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }
    
    @Override
    public Date getLastActualTimeByDatasourceOwner(String ownerId) {
        Date res = (Date)em.createNamedQuery("PDataEntry.getLastActualTimeByDatasourceOwner")
        		.setParameter("ownerID", ownerId).getSingleResult();
	
        return res;
    }

	@Override
	public PDataEntry getLatestDataEntry(List<String> datasourceUUID,
			Date start, Date end, Date currentTime){
		Query query = em.createNamedQuery("PDataEntry.getLatestDataEntry");
		query.setParameter("begin", start);
		query.setParameter("end", end);
		query.setParameter("currentTime", currentTime);
		query.setParameter("sIds", datasourceUUID);
		
        List<Object[]> list= query.getResultList();
	
        if(null==list||list.isEmpty()){
            return null;
        }else{
        	Object[] item = list.get(0);
        	PDataEntry result = new PDataEntry();
        	result.setTime((Date)item[1]);
        	result.setValue((Double)item[0]);
            return result;
        }		
	}
	@Override
    public PDataEntry getNextDataEntry(String datasourceUUID,
            String dataSetUUID, Date time) throws EntityNotFoundException {
        if (datasourceUUID == null || dataSetUUID == null) {
            return null;
        }

        Date last = this.getNextTimePoint(datasourceUUID, dataSetUUID, time);
        if(last==null) return null;
        
        StringBuffer sb = new StringBuffer();
    	sb.append("SELECT uuid,time,value,actual FROM dataentry_temp WHERE dataset_uuid='"+dataSetUUID+"' AND datasource_uuid='"+datasourceUUID+"' AND time=${lastTime} ");
    	sb.append("    UNION    ");
    	sb.append("SELECT uuid,time,value,actual FROM dataentry WHERE dataset_uuid='"+dataSetUUID+"' AND datasource_uuid='"+datasourceUUID+"' AND time=${lastTime} ");
    	List<PDataEntry> result = null;
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("lastTime", last);
    	try {
    		result = sqlExecutor.doNativeQuery(sb.toString(), params, new ListConverter<PDataEntry>(
					new ColumnAsFeatureFactory<PDataEntry>(
					PDataEntry.class)));
		} catch (SQLException e) {
			throw new EntityNotFoundException(e);
		}
    	return (result==null||result.isEmpty())?null:result.iterator().next();
    }

	@Override
	public Date getLastActualByDate(List<String> dsUUIDs, DateRange range) {

        List<Date> list= em.createNamedQuery("PDataEntry.getLastActualTimeByDate")
	    .setParameter("sIds", dsUUIDs).setParameter("begin", range.getStartTime()).setParameter("end", range.getEndTime()).getResultList();
	
        if(null==list||list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
	}
	
	@Override
	public Map<String, Date> getLastValidDate(String dataset_uuid, List<PDataSource> dataSourceList, DateRange range) {
		Map<String, Date> map=new HashMap<String, Date>();
		
		if (dataSourceList!=null && dataSourceList.size()>0) {
			List<String> dsUUIDs=new ArrayList<String>();
			for (PDataSource ds:dataSourceList) {
				dsUUIDs.add(ds.getUUID());
			}
			
			Query query=em.createNamedQuery("PDataEntry.getLastValidDate");
			query.setParameter("dataset_uuid", dataset_uuid);
			query.setParameter("uuids", dsUUIDs);
			query.setParameter("begin", range.getStartTime());
			query.setParameter("end", range.getEndTime());
			
			List list=query.getResultList();
			for (Object obj:list) {
				if (obj instanceof Object[] && ((Object[])obj).length==2) {
					Object[] row=(Object[])obj;
					map.put((String)row[0], (Date)row[1]);
				}
			}
		}
	
        return map;
	}
	
	@Override
	public List<Date> getDataTime(String dataset_uuid, String datasource_uuid, DateRange range) {
		Query query=em.createNamedQuery("PDataEntry.getDataTime");
		query.setParameter("dataset_uuid", dataset_uuid);
		query.setParameter("datasource_uuid", datasource_uuid);
		query.setParameter("begin", range.getStartTime());
		query.setParameter("end", range.getEndTime());
		
		return query.getResultList();
	}

	@Override
	public List<PDataEntry> getVirtualData(String dataset_uuid, String datasource_uuid, DateRange range) {
		Query query=em.createNamedQuery("PDataEntry.getVirtualData");
		query.setParameter("dataset_uuid", dataset_uuid);
		query.setParameter("datasource_uuid", datasource_uuid);
		query.setParameter("begin", range.getStartTime());
		query.setParameter("end", range.getEndTime());
		
		return query.getResultList();
	}
	
	@Override
	public void removeByParticipantNameAndRange(List<String> participantNames, Date start, Date end){
		String sqltemplate =" DELETE de.* FROM dataentry de, datasource ds " +
		        " WHERE de.datasource_uuid=ds.uuid AND ds.ownerID in ${ownerIDs} AND de.time >= ${startTime} AND de.time <= ${endTime}; ";
		String sqltemplate_temp =" DELETE de.* FROM dataentry_temp de, datasource ds " +
		        " WHERE de.datasource_uuid=ds.uuid AND ds.ownerID in ${ownerIDs} AND de.time >= ${startTime} AND de.time <= ${endTime}; ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ownerIDs", participantNames);
		params.put("startTime", start);
		params.put("endTime", end);
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
			String sql_temp = SQLBuilder.buildNamedParameterSQL(sqltemplate_temp.toString(), params);
			sqlExecutor.execute(sql,params);
			sqlExecutor.execute(sql_temp,params);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new EJBException(e);
		}
	}
	
	@Override
	public int insertOrUpdate(PDataEntry entry) {
		String sqltemplate =" INSERT INTO dataentry(uuid, dataset_uuid,datasource_uuid, time,value,creationTime,valueType,actual, rawTime) " +
        " VALUES (REPLACE(UUID(), '-', ''), ${dataset_uuid}, ${datasource_uuid},  ${time}, ${value}, now(), ${valueType}, " +
        " ${actual},  ${rawTime}) ON DUPLICATE KEY UPDATE VALUE= ${value}; ";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("dataset_uuid", entry.getDataSetUUID());
        params.put("datasource_uuid", entry.getDataSourceUUID());
        params.put("time", entry.getTime());
        params.put("value", entry.getValue());
        params.put("valueType", entry.getValueType());
        params.put("actual", entry.isActual());
        params.put("rawTime", entry.getRawTime());
        try{
        	String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate.toString(), params);
        	return sqlExecutor.execute(sql,params);
        }catch (Exception e){
        	log.error(e.getMessage(),e);
        	throw new EJBException(e);
        }
	}

	@Override
	public List<PDataEntry> getLatestDataEntry(String datasourceUUID,
			String dataSetUUID) throws EntityNotFoundException {
		
			Date last = getLastestContact(datasourceUUID, dataSetUUID);
			if(last==null) return new ArrayList<PDataEntry>();
			
			StringBuffer sb = new StringBuffer();
	    	sb.append("SELECT uuid,time,value,actual FROM dataentry_temp WHERE dataset_uuid='"+dataSetUUID+"' AND datasource_uuid='"+datasourceUUID+"' AND time=${lastTime} ");
	    	sb.append("    UNION    ");
	    	sb.append("SELECT uuid,time,value,actual FROM dataentry WHERE dataset_uuid='"+dataSetUUID+"' AND datasource_uuid='"+datasourceUUID+"' AND time=${lastTime} ");
	    	List<PDataEntry> result = null;
	    	Map<String, Object> params = new HashMap<String, Object>();
			params.put("lastTime", last);
	    	try {
	    		result = sqlExecutor.doNativeQuery(sb.toString(), params, new ListConverter<PDataEntry>(
						new ColumnAsFeatureFactory<PDataEntry>(
						PDataEntry.class)));
			} catch (SQLException e) {
				throw new EntityNotFoundException(e);
			}
	    	
	    	return result==null?new ArrayList<PDataEntry>(): result;
	}

	@Override
	public Date getPreviousTimePoint(String datasourceUUID, String datasetUUID, Date current)
			throws EntityNotFoundException {
		StringBuffer sb = new StringBuffer();
	 	sb.append("SELECT MAX(lastComm) AS lastTime FROM(    ");
    	sb.append("SELECT MAX(TIME) lastComm FROM dataentry_temp WHERE dataset_uuid='"+datasetUUID+"' AND datasource_uuid='"+datasourceUUID+"' AND time<${current} ");
    	sb.append("    UNION    ");
    	sb.append("SELECT MAX(TIME) lastComm FROM dataentry WHERE dataset_uuid='"+datasetUUID+"' AND datasource_uuid='"+datasourceUUID+"' AND time<${current} ");
    	sb.append(") table1");
    	
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("current", current);
    	Date result = null;
    	try {
    		result = sqlExecutor.doNativeQuery(sb.toString(), params, new CellConverter<Date>(Date.class));
		} catch (SQLException e) {
			throw new EntityNotFoundException(e);
		}
    	
		return result;
	}

	@Override
	public Date getNextTimePoint(String datasourceUUID, String datasetUUID, Date current)
			throws EntityNotFoundException {
		StringBuffer sb = new StringBuffer();
	 	sb.append("SELECT MIN(lastComm) AS lastTime FROM(    ");
    	sb.append("SELECT MIN(TIME) lastComm FROM dataentry_temp WHERE dataset_uuid='"+datasetUUID+"' AND datasource_uuid='"+datasourceUUID+"' AND time>${current} ");
    	sb.append("    UNION    ");
    	sb.append("SELECT MIN(TIME) lastComm FROM dataentry WHERE dataset_uuid='"+datasetUUID+"' AND datasource_uuid='"+datasourceUUID+"' AND time>${current} ");
    	sb.append(") table1");
    	
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("current", current);
    	Date result = null;
    	try {
    		result = sqlExecutor.doNativeQuery(sb.toString(), params, new CellConverter<Date>(Date.class));
		} catch (SQLException e) {
			throw new EJBException(e);
		}
    	
		return result;
	}

}