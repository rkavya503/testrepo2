/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.program.services.ProgramServicerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data.usage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.util.LogUtils;

/**
 * This class provides a stateless session bean BO facade for all the functions
 * related to the program management.
 */
@Stateless
public class CurrentUsageDataEntryEAOBean extends
        com.akuacom.ejb.BaseEAOBean<CurrentUsageDataEntry> implements
        CurrentUsageDataEntryEAO.R, CurrentUsageDataEntryEAO.L {
	
	@EJB 
	Pss2SQLExecutor.L sqlExecutor; 
	
	
    private static final Logger log = Logger
            .getLogger(CurrentUsageDataEntryEAOBean.class);

    public CurrentUsageDataEntryEAOBean(Class<CurrentUsageDataEntry> entityClass) {
        super(entityClass);
    }

    public CurrentUsageDataEntryEAOBean() {
        super(CurrentUsageDataEntry.class);
    }

    @Override
    public Date getLastActualTimeByDatasourceOwner(String ownerId) {
        Date res = (Date)em.createNamedQuery("CurrentUsageDataEntry.getLastActualTimeByDatasourceOwner")
        		.setParameter("ownerID", ownerId).getSingleResult();
	
        return res;
    }

    public List<String> getDatasetNamesByOwnerID(String ownerID)
            throws EntityNotFoundException {
        return em.createNamedQuery(
                "CurrentUsageDataEntry.findDatasetNameByDatasourceOwner")
                .setParameter("ownerID", ownerID).getResultList();
    }

    // TODO 3118
    public List<Date> getDataDays(List<String> datasetUUIDs,
            String datasourceUUID, DateRange range)
            throws EntityNotFoundException {
        List<Object> values = new ArrayList<Object>();
        String strQuery = "select distinct(date(de.time)) as dates  from CurrentUsageDataEntry de "
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
        return (Date)em
                .createNamedQuery(
                        "CurrentUsageDataEntry.findLatestContactBySourceAndSet")
                .setParameter("sourceUUID", datasourceUUID)
                .setParameter("setUUID", datasetUUID)
                .getSingleResult();
    }

    // TODO 3118
    public List<CurrentUsageDataEntry> getLatestDataEntry(
            String datasourceUUID, List<String> dataSetUUIDs)
            throws EntityNotFoundException {
        if (dataSetUUIDs.isEmpty()) {
            return Collections.<CurrentUsageDataEntry>emptyList();
        }

        String sql = "select de from CurrentUsageDataEntry de where de.datasource.UUID = '"
                + datasourceUUID + "' and (";
        for (int i = 0; i < dataSetUUIDs.size(); i++) {
            if (i > 0)
                sql = sql + " or ";
            String pdUUID = dataSetUUIDs.get(i);
            sql = sql
                    + " de.time = (select max(de1.time) from CurrentUsageDataEntry de1 where de1.dataSet.UUID= "
                    + pdUUID + " and de1.datasource.UUID = '" + datasourceUUID
                    + "')";
        }
        sql = sql + ") group by de.dataSet.UUID";
        return em.createQuery(sql).getResultList();
    }

    public List<CurrentUsageDataEntry> getDataEntryForThreeTen(
            String datasourceUUID, int numberOfDays, Date endDate) {
        if (datasourceUUID == null) {
            return Collections.<CurrentUsageDataEntry>emptyList();
        }

        return em
                .createNamedQuery("CurrentUsageDataEntry.findTenThree")
                .setParameter("sourceUUID", datasourceUUID)
                .setParameter("end", endDate)
                .setMaxResults(numberOfDays)
                .getResultList();
    }

    public List<CurrentUsageDataEntry> getDataEntryForByDataSourceUUIDAndDate(
            String datasourceUUID, Date date) {
        if (datasourceUUID == null) {
            return Collections.<CurrentUsageDataEntry>emptyList();
        }

        return em
                .createNamedQuery(
                        "CurrentUsageDataEntry.findByDataSourceUUIDAndDate")
                .setParameter("sourceUUID", datasourceUUID)
                .setParameter("day", date).getResultList();
    }

    // TODO 3118
    public List<CurrentUsageDataEntry> getDataEntryForByDataSourceUUIDsAndDates(
            List<String> datasourceUUIDs, Date start, Date end) {
        if (datasourceUUIDs == null || datasourceUUIDs.size() == 0) {
            return Collections.<CurrentUsageDataEntry>emptyList();
        }

        if (datasourceUUIDs.size() == 1) {
            return getDataEntryForByDataSourceUUIDAndDates(
                    datasourceUUIDs.get(0), start, end);
        }

        List<CurrentUsageDataEntry> ret = new ArrayList<CurrentUsageDataEntry>();

        List<Object> values = new ArrayList<Object>();

        String sql = "select count(de.time) as numPoint, sum(de.value), de.time from CurrentUsageDataEntry de where de.datasource.UUID in (";
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
                    CurrentUsageDataEntry pde = new CurrentUsageDataEntry();
                    pde.setValue(value);
                    pde.setTime(time);
                    ret.add(pde);
                }
            }
            return ret;
        } catch (NoResultException e) {
            return ret;
        }

    }

    public List<CurrentUsageDataEntry> getDataEntryForByDataSourceUUIDAndDates(
            String datasourceUUID, Date start, Date end) {
        if (datasourceUUID == null) {
            return Collections.<CurrentUsageDataEntry>emptyList();
        }

        return em
                .createNamedQuery(
                        "CurrentUsageDataEntry.findByDataSourceUUIDAndDates")
                .setParameter("sourceUUID", datasourceUUID)
                .setParameter("begin", start).setParameter("end", end).getResultList();
    }

    public List<CurrentUsageDataEntry> getDataEntryForByDataSourceUUID(
            String datasourceUUID) {
        if (datasourceUUID == null) {
            return Collections.<CurrentUsageDataEntry>emptyList();
        }

        return em.createNamedQuery(
                "CurrentUsageDataEntry.findByDataSourceUUIDAndDates")
                .setParameter("sourceUUID", datasourceUUID).getResultList();
    }

    public List<CurrentUsageDataEntry> getDataEntryForByDataSourceUUIDs(
            List<String> datasourceUUIDs) {
        return Collections.<CurrentUsageDataEntry>emptyList();
    }

    // TODO 3118
    public List<UsageSummary> getUsageReport(Map<String, String> dsUUIDs,
            Date start, Date end) {
        double hours = (end.getTime() - start.getTime()) / 3600000;
        String sql = "select avg(de.value), de.datasource.UUID from CurrentUsageDataEntry de where de.datasource.UUID in (";
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

    // TODO 3118
    public UsageSummary getUsageSummary(List<String> dsUUIDs, Date start,
            Date end) {
        double hours = (end.getTime() - start.getTime()) / 3600000;
        String sql = "select avg(de.value) from CurrentUsageDataEntry de where de.datasource.UUID in (";
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

    public Date getMaxTime() {
        return (Date) em.createNamedQuery("CurrentUsageDataEntry.getMaxTime").getSingleResult();
    }

    // TODO 3118
    public void pushFromCurrentToHistory() {
        Connection conn = null;
        try {
            DataSource source = getDataSource();
            try {
                conn = source.getConnection();

                PreparedStatement ps1 = conn
                        .prepareStatement("insert into dataentry "
                                + "select * from dataentry; ");
                try {
                    ps1.executeUpdate();
                } finally {
                    if (ps1 != null)
                        ps1.close();
                }

                PreparedStatement ps2 = conn
                        .prepareStatement("delete from dataentry_temp ");
                try {
                    ps2.executeUpdate();
                } finally {
                    if (ps2 != null)
                        ps2.close();
                }

            } finally {
                if (conn != null)
                    conn.close();
            }
        } catch (Exception e) {
            log.error(LogUtils.createExceptionLogEntry("", this.getClass()
                    .getName(), e));
            throw new EJBException(e);
        }
        
        
    }

    private DataSource getDataSource() throws NamingException {
        Context context = new InitialContext();
        DataSource ds = (DataSource) context.lookup("java:mysql-pss2-ds");
        context.close();
        return ds;
    }
    
	@Override
	public int insertOrUpdate(PDataEntry entry) {
		//use mysql syntax - insert into ... on duplicate key update ... 
		String sqltemplate =" INSERT INTO dataentry_temp(uuid, dataset_uuid,datasource_uuid, time,value,creationTime,valueType,actual, rawTime) " +
        " VALUES (REPLACE(UUID(), '-', ''), ${dataset_uuid}, ${datasource_uuid},  ${time}, ${value}, now(), ${valueType}, " +
        " ${actual}, ${rawTime}) ON DUPLICATE KEY UPDATE VALUE= ${value}; ";
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
        	return sqlExecutor.execute(sql, params);
        }catch (Exception e){
        	log.error(e.getMessage(),e);
        	throw new EJBException(e);
        }
	}	
}