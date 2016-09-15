/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.FireLogReaderBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import com.akuacom.pss2.util.DateTool;
import com.akuacom.pss2.util.LogUtils;
import com.honeywell.drms.log.reader.LogManager;
import com.honeywell.drms.log.reader.LogSearchCriteria;
import com.kanaeki.firelog.util.FireLogEntry;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * FireLog access class.
 */
public class FireLogReaderBean
{
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(FireLogReaderBean.class);
    
    final String logDir = System.getProperty("jboss.server.log.dir");
    
    /** The ds. */
    private DataSource ds;

    /**
     * Instantiates a new fire log reader bean.
     */
    public FireLogReaderBean() {
        try {
            ds = getDataSource();
        } catch (NamingException e) {
        	log.error("can not connect to fire log.");
        }
    }

    /**
     * Gets the data source.
     * 
     * @return the data source
     * 
     * @throws NamingException the naming exception
     */
    private DataSource getDataSource() throws NamingException {
        Context context = new InitialContext();
        DataSource ds = (DataSource) context.lookup("java:mysql-firelog-ds");
        context.close();
        return ds;
    }

    /*
    +-------------+--------------+------+-----+---------+----------------+
    | Field       | Type         | Null | Key | Default | Extra          |
    +-------------+--------------+------+-----+---------+----------------+
    | id          | bigint(20)   |      | PRI | NULL    | auto_increment |
    | logdate     | datetime     | YES  |     | NULL    |                |
    | loglevel    | int(11)      | YES  |     | NULL    |                |
    | sessionID   | varchar(64)  | YES  |     | NULL    |                |
    | userrole    | varchar(24)  | YES  |     | NULL    |                |
    | username    | varchar(24)  | YES  |     | NULL    |                |
    | category    | varchar(128) | YES  |     | NULL    |                |
    | userparam1  | varchar(24)  | YES  |     | NULL    |                |
    | userparam2  | varchar(24)  | YES  |     | NULL    |                |
    | userparam3  | varchar(24)  | YES  |     | NULL    |                |
    | starttime   | datetime     | YES  |     | NULL    |                |
    | methodname  | varchar(64)  | YES  |     | NULL    |                |
    | classname   | varchar(128) | YES  |     | NULL    |                |
    | filename    | varchar(128) | YES  |     | NULL    |                |
    | linenum     | int(11)      | YES  |     | NULL    |                |
    | description | varchar(128) | YES  |     | NULL    |                |
    | longdesc    | text         | YES  |     | NULL    |                |
    +-------------+--------------+------+-----+---------+----------------+
    */
    /** The Constant GET_BY_ID_SQL. */
    private static final String GET_BY_ID_SQL = "select * from firelogentry where id = ?";

    /** The Constant SEARCH_LOG_ENTRIES_SQL. */
    private static final String SEARCH_LOG_ENTRIES_SQL = "select * from firelogentry where 0=0 ";

    /**
     * Fetch an entry from db by id.
     * 
     * @param id entry id
     * 
     * @return VO if found, null otherwise
     */
    public FireLogEntry getLog(int id) {

        FireLogEntry result = null;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ds.getConnection();
            // get record by id
            ps = conn.prepareStatement(GET_BY_ID_SQL);
            ps.clearParameters();
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            // populate the bean
            if (rs.next()) {
                result = getFireLogEntry(rs);
            }

            // connection clean up
            ps.close();
            ps = null;
            
            conn.close();
            conn = null;
        } catch (SQLException e) {
            // TODO 2992
        	log.error(LogUtils.createLogEntry("", this.getClass().getSimpleName(), 
        			"Failed to get log: log id = " + id, e.getMessage()));
        } finally {

        	if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        	
        	if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }

        return result;
    }

    /**
     * Gets the fire log entry.
     * 
     * @param rs the rs
     * 
     * @return the fire log entry
     * 
     * @throws SQLException the SQL exception
     */
    private FireLogEntry getFireLogEntry(ResultSet rs) throws SQLException {
        FireLogEntry entry = new FireLogEntry();

        entry.setId(rs.getInt("id"));
        entry.setLogDate(new Date(rs.getTimestamp("logdate").getTime() + rs.getInt("logmillisec")));
        entry.setLogLevel(rs.getInt("loglevel"));
        entry.setSessionID(rs.getString("sessionID"));
        entry.setUserRole(rs.getString("userrole"));
        entry.setUserName(rs.getString("username"));
        entry.setCategory(rs.getString("category"));
        entry.setUserParam1(rs.getString("userparam1"));
        entry.setUserParam2(rs.getString("userparam2"));
        entry.setUserParam3(rs.getString("userparam3"));
        entry.setStartTime(rs.getDate("starttime"));
        entry.setMethodName(rs.getString("methodname"));
        entry.setClassName(rs.getString("classname"));
        entry.setFileName(rs.getString("filename"));
        entry.setLineNum(rs.getInt("linenum"));
        entry.setDescription(rs.getString("description"));
        entry.setLongDescr(rs.getString("longdesc"));

        return entry;
    }

    /**
     * Gets the log count.
     * 
     * @param query the query
     * 
     * @return the log count
     */
    public int getLogCount(String query) {
        Connection conn = null;
        Statement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                count = rs.getInt(1);
            }
            stmt.close();
            stmt = null;
            conn.close();
            conn = null;
        } catch (SQLException e) {
        	// TODO 2992
        	log.error(LogUtils.createLogEntry("", this.getClass().getSimpleName(), 
        			"Failed to get log count", e.getMessage()));
        } finally {
            if (stmt != null) {
                try {
                	stmt.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        	
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
        return count;
    }

    /**
     * Gets the logs based on query.
     * 
     * @param query the query
     * 
     * @return the logs
     */
    public List<FireLogEntry> getLogs(String query) {
        Connection conn = null;
        Statement stmt = null;

        List<FireLogEntry> list = new ArrayList<FireLogEntry>();
        try {
            conn = ds.getConnection();
            // get record by id
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // populate the bean
            while (rs.next()) {
                FireLogEntry entry = getFireLogEntry(rs);
                list.add(entry);
            }
            stmt.close();
            stmt = null;
            conn.close();
            conn = null;
        } catch (SQLException e) {
            // TODO 2992
        	log.error(LogUtils.createLogEntry("", this.getClass().getSimpleName(), 
        			"Failed to get logs", e.getMessage()));
        } finally {
            if (stmt != null) {
                try {
                	stmt.close();
                } catch (SQLException e) {
                    // ignore
                }
            }

        	if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
        return list;
    }
    
    public List<FireLogEntry> searchLog(List<String> usernames, String methodName, Date startTime, Date endTime)
    					throws Exception{
    	
    	LogManager logManager=new LogManager(this.logDir);
    	if(startTime==null || endTime==null)
    		return Collections.emptyList();
    	
    	String str=null;
    	if(usernames!=null){
    		for(String s:usernames){
    			if(str==null) str= s;
    			else str+= (";"+s);
    		}
    	}
    	LogSearchCriteria sc = new LogSearchCriteria(startTime,	endTime, 10000,str,"","","");
    	return logManager.doQuery(sc);
    }
    
    /**
     * Search log.
     * 
     * @param usernames the usernames
     * @param methodName the method name
     * @param startTime the start time
     * @param endTime the end time
     * 
     * @return the list< fire log entry>
     * 
     * @throws Exception the exception
     */
    //TODO remove
    public List<FireLogEntry> searchLog_old(List<String> usernames, String methodName, Date startTime, Date endTime)
        throws Exception
    {
        Connection conn = null;
        PreparedStatement stmt = null;

        List<FireLogEntry> list = new ArrayList<FireLogEntry>();
        String query = SEARCH_LOG_ENTRIES_SQL;
        if(usernames != null && usernames.size() > 0)
        {
            query = query + " and username in(";
            for(int i=0; i<usernames.size(); i++)
            {
                String username = usernames.get(i);
                if(i<usernames.size()-1)
                {
                    query = query + "'" + username + "', ";
                }
                else
                {
                    query = query + "'" + username + "') ";
                }
            }
        }

        if(methodName != null)
        {
            query = query + " and methodname = '" + methodName + "' ";
        }

        java.sql.Timestamp startSqlDate = null;
        if(startTime != null)
        {
            Date startDateRound = DateTool.roundTime(startTime, Calendar.MILLISECOND, true);
            startSqlDate = new Timestamp(startDateRound.getTime());
            query = query + " and logdate >= '" + startSqlDate.toString() + "' ";
            //startSqlDate = new java.sql.Date(startDateRound.getTime());
        }
        java.sql.Timestamp endSqlDate = null;
        if(endTime != null)
        {
            Date endDateRound = DateTool.roundTime(endTime, Calendar.MILLISECOND, false);
            endSqlDate = new Timestamp(endDateRound.getTime());
            query = query + " and logdate <= '" + endSqlDate.toString() + "' ";

        }
        try
        {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.clearParameters();

            ResultSet rs = stmt.executeQuery(query);

            // populate the bean
            while (rs.next())
            {
                FireLogEntry entry = getFireLogEntry(rs);
                Date logDate = entry.getLogDate();
                if( (startTime != null && logDate.after(startTime) )
                        || ( endTime != null && logDate.before(endTime))
                        || (startTime == null && endTime == null))
                {
                    list.add(entry);
                }
            }
            stmt.close();
            stmt = null;
            conn.close();
            conn = null;
        } catch (SQLException e) {
            // TODO 2992
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
            throw e;
        } finally {
            if (stmt != null) {
                try {
                	stmt.close();
                } catch (SQLException e) {
                    // ignore
                }
            }

        	if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
        return list;
    }
}