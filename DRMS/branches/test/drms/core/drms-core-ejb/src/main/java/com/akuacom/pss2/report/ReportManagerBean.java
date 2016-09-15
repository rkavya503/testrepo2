/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.report.ReportManagerBean.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.report;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.program.DRwebsite.PREvent;
import com.akuacom.pss2.program.dbp.DBPEventCreation;
import com.akuacom.pss2.program.dbp.DBPEventCreationGenEAO;
import com.akuacom.pss2.program.scertp.entities.Weather;
import com.akuacom.pss2.program.scertp.entities.WeatherEAO;
import com.akuacom.pss2.report.entities.Account;
import com.akuacom.pss2.report.entities.ClientInfo;
import com.akuacom.pss2.report.entities.ClientShedStrategy;
import com.akuacom.pss2.report.entities.OfflineRecord;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.util.CBPUtil;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;
/**
 * The Class ReportManagerBean.
 */
@Stateless
public class ReportManagerBean implements ReportManager.R, ReportManager.L {

    /** The Constant log. */
    private static final Logger log = Logger.getLogger(ReportManagerBean.class);

    @EJB private WeatherEAO.L weatherEAO;
    
    @EJB private DBPEventCreationGenEAO.L dbpEventCreationGenEAO;

    @EJB private CorePropertyEAO.L corePropEAO;
    
    @EJB
    private Pss2SQLExecutor.L sqlExecutor;
    
    
    private static final String SQL_CLIENT_INFO;

	static {
		SQL_CLIENT_INFO = getSQLFromFile("ClientInfo.sql");
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
        DataSource ds = (DataSource) context.lookup("java:mysql-pss2-ds");
        context.close();
        return ds;
    }

    /* (non-Javadoc)
     * @see com.akuacom.pss2.report.ReportManager#getAccounts()
     */
    public List<Account> getAccounts() {
        ArrayList<Account> list = new ArrayList<Account>();

        DataSource source = null;
        Connection conn = null;
        Statement s = null;
        try {
            source = getDataSource();
            conn = source.getConnection();
            s = conn.createStatement();
            
        	StringBuilder sb = new StringBuilder();
        	sb.append("SELECT p1.participantName, p1.account, IFNULL(p2.participantName, '') as parentName, ");
        	sb.append("p1.premiseNumber, p1.startDate, p1.endDate, pp1.programName ");
        	sb.append("FROM program_participant pp1 inner join participant p1 ");
        	sb.append("on p1.uuid = pp1.participant_uuid ");
        	sb.append("left outer join program_participant pp2 ");
        	sb.append("on pp2.uuid = pp1.parent_uuid ");
        	sb.append("left outer join participant p2 ");
        	sb.append("on p2.uuid = pp2.participant_uuid ");
            sb.append(" where p1.client = '0' OR p1.client = 'null' ");
        	sb.append("order by p1.participantName, pp1.programName ");

            ResultSet rs2 = s.executeQuery(sb.toString());
            
            while (rs2.next()) {
                Account a = new Account();
                a.setParticipantName(rs2.getString("participantName"));
                a.setAccountNumber(rs2.getString("account"));
                a.setSubAccount(rs2.getString("parentName"));
                a.setProgramNames(rs2.getString("programName"));
                a.setPremiseNumber(rs2.getString("premiseNumber"));
                a.setStartDate(rs2.getTimestamp("startDate"));
                a.setEndDate(rs2.getTimestamp("endDate"));
                String active;
                if (a.getStartDate() == null) {
                    active = "";
                } else if (isActive(a.getStartDate(), a.getEndDate())) {
                    active = "Active";
                } else {
                    active = "Inactive";
                }
                a.setActive(active);
                list.add(a);
            }
                    
        } catch (SQLException e) {
            //log.error(e.getMessage(), e);
            //09.27.2010 Frank: DRMS-1662 modify
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } catch (NamingException e) {
            //log.error(e.getMessage(), e);
            //09.27.2010 Frank: DRMS-1662 modify
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } finally {
            //A ResultSet object is automatically closed by the Statement object that generated it 
            //when that Statement object is closed, re-executed, or is used to retrieve the next result from a sequence of multiple results. 
        	try {if (s != null) s.close();} catch (SQLException sqlex) {}  
        	try {if (conn != null) conn.close();} catch (SQLException sqlex) {}  
        }

        return list;
    }

    public List<ClientInfo> getClientInfoResults(){
    	List<ClientInfo> list = new ArrayList<ClientInfo>();
    	
    	ProgramManager pm = EJB3Factory.getBean(ProgramManager.class);
        
    	boolean consolidationFlag=CBPUtil.isEnableCBPConsolidation(pm.getAllPrograms());
    	
        DateFormat dateFormat;
        try {
            CoreProperty property = corePropEAO.getByPropertyName(PSS2Features.FeatureName.DATE_FORMAT.getFeatureName());
            dateFormat = new SimpleDateFormat(property.getStringValue() + " HH:mm:ss");
        } catch (EntityNotFoundException e) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        DataSource source = null;
        Connection conn = null;
        Statement s = null;
        try {
            source = getDataSource();
            conn = source.getConnection();
            s = conn.createStatement();
            
            ResultSet rs = s.executeQuery(SQL_CLIENT_INFO);
            
            while (rs.next()) {
            	ClientInfo instance = new ClientInfo();
            	//autoControlValue
            	String autoControlValue =rs.getString("autoControlValue");
            	instance.setAutoControlValue(autoControlValue);
            	//munaulControlValue
            	String manualControlValue =rs.getString("manualControlValue");
            	instance.setManualControlValue(manualControlValue);
            	//manualControl
            	String manualControl =rs.getString("manualControl");
            	instance.setManualControl(manualControl);
            	//client
            	String client =rs.getString("client");
            	instance.setClient(client);
        		//participant
            	String participant =rs.getString("participant");
            	instance.setParticipant(participant);
        		//account number
            	String account =rs.getString("account");
            	instance.setAccountNumber(account);
        		//parent
            	String parent =rs.getString("parent");
            	instance.setParent(parent);
        		//premise number
            	String premiseNumber =rs.getString("premiseNumber");
            	instance.setPremiseNumber(premiseNumber);
        		//start date
            	Date startDate = rs.getTimestamp("startDate");
            	if(startDate!=null){
            		instance.setStartDate(dateFormat.format(startDate));
            	}
            	
        		//end date
                Date endDate = rs.getTimestamp("endDate");
                if(endDate!=null){
                	instance.setEndDate(dateFormat.format(endDate));
                }
                
                //profile
        		//Active if Start Date entered and no end date or future end date, 
        		//Inactive if end date < today and 
        		//Pending if no date entered.
        		Date today = new Date();
        		if(((startDate!=null&&endDate==null))||((startDate!=null&&endDate!=null)&&(!DateUtil.getStartOfDay(endDate).before(DateUtil.getStartOfDay(today))))){
        			instance.setProfile("Active");
        		}else if(endDate!=null){
        			if(DateUtil.getStartOfDay(endDate).before(DateUtil.getStartOfDay(today))){
        				instance.setProfile("Inactive");
        			}
        		}else if(startDate==null&&endDate==null){
        			instance.setProfile("Pending");
        		}
        		//event status
        		String eventStatus =rs.getString("eventStatus");
        		instance.setEventStatus(eventStatus);
        		//mode
        		String operationMode =rs.getString("operationMode");
       		 	instance.setMode(operationMode);
       		 	//common status
       		 	String status =rs.getString("status");
       		 	instance.setCommStatus(ClientInfo.loadClientStatus(status));
       		 	//last contact
       		 	Date commTime = rs.getTimestamp("commTime");
                if (commTime != null) {
                    instance.setLastContact(dateFormat.format(commTime));
                }
       		 	//client type
       		 	String type =rs.getString("type");
       		 	instance.setClientType(ClientInfo.loadClientType(type));
       		 	//device type
       		 	String deviceType =rs.getString("deviceType");
       		 	instance.setDeviceType(deviceType);
        		
        		String program =rs.getString("programName");
        		instance.setProgram(program);
        		String state =rs.getString("state");
        		instance.setState(state);
        		String address =rs.getString("address");
        		instance.setAddress(address);
        		//DRMS-7896
        		int priority =rs.getInt("priority");
        		instance.setPriority(priority);
        		int eventOptOut =rs.getInt("eventOptOut");
        		instance.setEventOptOut(eventOptOut);
        		boolean leadAccount =rs.getBoolean("leadAccount");
        		instance.setLeadAccount(leadAccount);
        		String ABank =rs.getString("ABank");
        		instance.setABank(ABank);
        		Date eventStartTime = rs.getTimestamp("eventStartTime");
        		instance.setEventStartTime(eventStartTime);
        		Date eventEndTime = rs.getTimestamp("eventEndTime");
        		instance.setEventEndTime(eventEndTime);
        		boolean testParticipant =rs.getBoolean("testParticipant");
        		instance.setTestParticipant(testParticipant);
        		boolean aggregator =rs.getBoolean("aggregator");
        		instance.setAggregator(aggregator);
        		boolean customer =rs.getBoolean("customer");
        		instance.setCustomer(customer);
        		boolean nonAutoDR =rs.getBoolean("nonAutoDR");
        		instance.setNonAutoDR(nonAutoDR);
        		list.add(instance);
            }
            list = ClientInfo.transfer(list,consolidationFlag);        
        } catch (SQLException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } catch (NamingException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } finally {
            try {if (s != null) s.close();} catch (SQLException sqlex) {}  
        	try {if (conn != null) conn.close();} catch (SQLException sqlex) {}  
        }
    	return list;
    }
    private boolean isActive(Date startDate, Date endDate) {
        Date today = DateUtil.stripTime(new Date());
        if (endDate != null &&
                today.after(DateUtil.stripTime(endDate))) {
            return false;
        } else if (today.before(DateUtil.stripTime(startDate))) {
            return false;
        } else {
            return true;
        }
    }


    public List<OfflineRecord> getObixCommReport() {
        List<OfflineRecord> list = new ArrayList<OfflineRecord>();
        try {
            
            long timeOutTrigger = 15 * 60 * 1000; // 15 minutes
	        
	        SystemManager sm = EJB3Factory.getBean(SystemManager.class);
	        Double clientTimeout=null;
	        Double clientTimeoutIncrement=1.0;
	        
        	PSS2Features features=sm.getPss2Features();
        	clientTimeout=features.getClientTimeout();
        	if (features.getClientTimeoutIncrement() !=null)
        		clientTimeoutIncrement=clientTimeoutIncrement+features.getClientTimeoutIncrement();
        	if (clientTimeout!=null)
        		timeOutTrigger = new Double(clientTimeout.doubleValue() * clientTimeoutIncrement.doubleValue() * 60.0 * 1000.0).longValue();
            
            long now = System.currentTimeMillis();
        	
        	DataSource source = getDataSource();
            Connection conn = source.getConnection();
            try {
                PreparedStatement ps;
                
                String sql=" SELECT MAX(lastactual) AS lastComm, ds.ownerId,p.account"
                	+" FROM datasource ds, datasource_usage dsu,participant p "
                	+" \nWHERE ds.ownerId = p.participantName AND p.dataEnabler= 1 AND p.parent IS NULL"
                	+" AND ds.uuid = dsu.datasource_uuid AND lastactual IS NOT NULL "
                	+" GROUP BY ownerId order by ownerId";
				ps = conn.prepareStatement(sql.toString());
                try {
                    final ResultSet rs = ps.executeQuery();
                    try {
                        while (rs.next()) {
                            OfflineRecord e = new OfflineRecord();
                            e.setParticipantName(rs.getString("ownerId"));
                            e.setAccountId(rs.getString("account"));
                            e.setLastContact(rs.getTimestamp("lastComm"));
                            
                            boolean offline = e.getLastContact() == null || now - e.getLastContact().getTime() > timeOutTrigger;
                            if (offline) {
                            	e.setStatus("OFFLINE");
                    		} else {
                    			e.setStatus("ONLINE");
                    		}
                            
                            list.add(e);
                        }
                    } finally {
                        if (rs != null) rs.close();
                    }
                } finally {
                    if (ps != null) ps.close();
                }
            } finally {
                if( conn != null ) conn.close();
            }

        } catch (SQLException e) {
            //log.error(e.getMessage(), e);
            //09.27.2010 Frank: DRMS-1662 modify
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } catch (NamingException e) {
            //log.error(e.getMessage(), e);
            //09.27.2010 Frank: DRMS-1662 modify
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        }
        return list;
    }
    
    /* (non-Javadoc)
     * @see com.akuacom.pss2.report.ReportManager#getWeatherRecords(java.util.Date, java.util.Date)
     */
    public List<Weather> getWeatherRecords(Date start, Date end)
    {
    	List<Weather> weatherList=new ArrayList<Weather>();
    	
    	weatherList=weatherEAO.getWeatherByDateRange(start, end);
    	return weatherList;
    }
    
	@Override
	public List<DBPEventCreation> getDBPEventCreation(Date start, Date end) {
		return dbpEventCreationGenEAO.findByDateRange(start, end);
	}

	// this method is for score card
	@Override
	public List<PREvent> getEventListByProgramClassWithHistoryEvent(String programClass, Date start, Date end) throws AppServiceException {
		List<PREvent> list = new ArrayList<PREvent>();

		try {
			DataSource source = getDataSource();
			final Connection conn = source.getConnection();
			try {
				final Statement s = conn.createStatement();
				try {
					Date startDate=DateUtil.getStartOfDay(start);
					Date endDate=DateUtil.getStartOfDay(new Date(end.getTime() + DateUtil.MSEC_IN_DAY));

					
                	StringBuilder builder=new StringBuilder();
                	builder.append("select DISTINCT p.programClass, p.longProgramName, he.programName, he.eventName, he.startTime, he.endTime ");
                	builder.append("from program p, history_event he ");
                	builder.append("where p.name = he.programName ");
                	builder.append("and p.programClass = ? and he.startTime >= ? ");
                	builder.append("and he.endTime < ? and he.cancelled = ? ");
                	builder.append("order by he.startTime desc, he.programName");
                	
	                PreparedStatement ps =
                        conn.prepareStatement(builder.toString());

                	ps.clearParameters();
                	ps.setString(1, programClass);
                	ps.setDate(2, new java.sql.Date(startDate.getTime()));
                	ps.setDate(3, new java.sql.Date(endDate.getTime()));
                	ps.setBoolean(4, false);
                	
					final ResultSet rs = ps.executeQuery();

					try {
						while (rs.next()) {
							PREvent e = new PREvent();
							e.setProgramName(rs.getString("programName"));
							e.setLongProgramName(rs.getString("longProgramName"));
							e.setProgramClass(rs.getString("programClass"));
							
							com.akuacom.pss2.event.Event event=new com.akuacom.pss2.event.Event();
							event.setProgramName(rs.getString("programName"));
							event.setEventName(rs.getString("eventName"));
							event.setStartTime(rs.getTimestamp("startTime"));
							event.setEndTime(rs.getTimestamp("endTime"));
							e.setEvent(event);
							list.add(e);
						}
					} finally {
						if (rs != null)
							rs.close();
					}
				} finally {
					if (s != null)
						s.close();
				}
			} finally {
				if (conn != null)
					conn.close();
			}
		} catch (Exception e) {
			StringBuilder builder=new StringBuilder();
			builder.append("failed to get event history. program name: ");
			builder.append(programClass);
			builder.append(" start date: ");
			builder.append(start);
			builder.append(" end date: ");
			builder.append(end);			
			throw new AppServiceException(builder.toString(), e);
		}
		return list;
	}

	
	
	
	public List<ClientShedStrategy> getClientShedStrategysForCPP(){
    	List<ClientShedStrategy> list = new ArrayList<ClientShedStrategy>();

       
        DataSource source = null;
        Connection conn = null;
        Statement s = null;
        try {
            source = getDataSource();
            conn = source.getConnection();
            s = conn.createStatement();
            
        	StringBuilder sb = new StringBuilder();
			sb.append(" SELECT DISTINCT  ");
        	sb.append(" pClient.uuid,pClient.participantName AS CLIENT,pClient.parent AS participant,pParticipant.account,IFNULL(p2.participantName, '') AS parent,pp0.programName,pp0.uuid,");
        	sb.append(" ppr.mode,ppr.source,ppr.sortOrder,ppr.start,ppr.end,ppr.value ");
        	sb.append(" FROM  ");
        	sb.append(" (  ");
        	sb.append(" SELECT p.*   ");
        	sb.append(" FROM participant p   ");
        	sb.append(" WHERE p.client = '1'  ");
        	sb.append(" ) pClient  ");
        	sb.append(" INNER JOIN participant pParticipant ON pParticipant.participantName = pClient.parent  ");
        	sb.append(" LEFT JOIN program_participant pp0   ");
        	sb.append(" ON pClient.uuid = pp0.participant_uuid   ");
        	sb.append(" LEFT JOIN program_participant pp1   ");
        	sb.append(" ON pParticipant.uuid = pp1.participant_uuid   ");
        	sb.append(" LEFT JOIN program_participant pp2   ");
        	sb.append(" ON pp2.uuid = pp1.parent_uuid    ");
        	sb.append(" LEFT JOIN participant p2   ");
        	sb.append(" ON p2.uuid = pp2.participant_uuid  ");
        	sb.append(" LEFT JOIN program_participant_rule ppr ");
        	sb.append(" ON pp0.uuid=ppr.program_participant_uuid ");
        	sb.append(" WHERE ppr.uuid IS NOT NULL AND ppr.source = 'CPP Strategy' AND pp0.state='1' ");
        	sb.append(" ORDER BY pClient.participantName,pp0.programName,ppr.sortOrder ");
            
            ResultSet rs = s.executeQuery(sb.toString());
            
            while (rs.next()) {
            	ClientShedStrategy instance = new ClientShedStrategy();

            	//client
            	String client =rs.getString("client");
            	instance.setClient(client);
        		//participant
            	String participant =rs.getString("participant");
            	instance.setParticipant(participant);
        		//account number
            	String account =rs.getString("account");
            	instance.setAccountNumber(account);
        		//parent
            	String parent =rs.getString("parent");
            	instance.setParent(parent);
            	//program
        		String program =rs.getString("programName");
        		instance.setProgram(program);
        		
        		//mode
        		String mode =rs.getString("mode");
        		instance.setMode(mode);
        		//source
        		String sourceValue =rs.getString("source");
        		instance.setSource(sourceValue);
        		//sortOrder
        		String sortOrder =rs.getString("sortOrder");
        		instance.setSortOrder(sortOrder);
        		//start
        		String start =rs.getString("start");
        		instance.setStart(start);
        		//end
        		String end =rs.getString("end");
        		instance.setEnd(end);
        		//value
        		String value =rs.getString("value");
        		instance.setRtpValue(value);
        		
        		instance = ClientShedStrategy.build(instance);
        		
        		list.add(instance);
            }
            list = ClientShedStrategy.transfer(list);        
        } catch (SQLException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } catch (NamingException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } finally {
            try {if (s != null) s.close();} catch (SQLException sqlex) {}  
        	try {if (conn != null) conn.close();} catch (SQLException sqlex) {}  
        }
    	return list;
    }
	
	public List<ClientShedStrategy> getClientShedStrategysForDBP(){
    	List<ClientShedStrategy> list = new ArrayList<ClientShedStrategy>();

       
        DataSource source = null;
        Connection conn = null;
        Statement s = null;
        try {
            source = getDataSource();
            conn = source.getConnection();
            s = conn.createStatement();
            
        	StringBuilder sb = new StringBuilder();
        	sb.append(" SELECT DISTINCT tmp.client, tmp.programName,tmp.programType,tmp.participant,tmp.account,IFNULL(parent.participantName, '') AS parent,tmp.uuid , blm.*  ");
        	sb.append(" FROM  ");
        	sb.append(" (        	  ");
        	sb.append(" SELECT client.uuid,client.participantName AS CLIENT, pp1.programName, program.programType,client.parent AS participant,participant.account ,pp2.parent_uuid   ");
        	sb.append(" FROM 	participant CLIENT ,  ");
        	sb.append(" 	program program,  ");
        	sb.append(" 	program_participant pp1,  ");
        	sb.append(" 	participant participant,  ");
        	sb.append(" 	program_participant pp2  ");
        	sb.append(" WHERE program.programType='DBPProgram' AND program.uuid= pp1.program_uuid AND client.client = '1' AND pp1.state='1'  ");
        	sb.append(" AND pp1.participant_uuid = client.uuid  ");
        	sb.append(" AND client.parent=participant.participantName  ");
        	sb.append(" AND pp2.participant_uuid=participant.uuid  ");
        	sb.append(" ) tmp  ");
        	sb.append(" LEFT JOIN program_participant pp3     ");
        	sb.append(" ON pp3.uuid =tmp.parent_uuid      ");
        	sb.append(" LEFT JOIN participant parent     ");
        	sb.append(" ON parent.uuid = pp3.participant_uuid   ");
        	sb.append(" LEFT JOIN bid_level_mapping blm  ");
        	sb.append(" ON blm.participant_uuid = tmp.uuid  ");
        	sb.append(" WHERE blm.uuid IS NOT NULL  ");
        	sb.append(" ORDER BY tmp.client,tmp.programName  ");
            
            ResultSet rs = s.executeQuery(sb.toString());
            
            while (rs.next()) {
            	ClientShedStrategy instance = new ClientShedStrategy();

            	//client
            	String client =rs.getString("client");
            	instance.setClient(client);
        		//participant
            	String participant =rs.getString("participant");
            	instance.setParticipant(participant);
        		//account number
            	String account =rs.getString("account");
            	instance.setAccountNumber(account);
        		//parent
            	String parent =rs.getString("parent");
            	instance.setParent(parent);
            	//program
        		String program =rs.getString("programName");
        		instance.setProgram(program);
        		//programType
        		String programType =rs.getString("programType");
        		instance.setProgramType(programType);
        		//timeBlock
        		String timeBlock =rs.getString("timeBlock");
        		instance.setTimeBlock(timeBlock);
        		//normal
        		String normal =rs.getString("normal");
        		instance.setNormal(normal);
        		//moderate
        		String moderate =rs.getString("moderate");
        		instance.setModerate(moderate);
        		//high
        		String high =rs.getString("high");
        		instance.setHigh(high);
        		
        		instance = ClientShedStrategy.build(instance);
        		
        		list.add(instance);
            }
            list = ClientShedStrategy.transfer(list);        
        } catch (SQLException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } catch (NamingException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } finally {
            try {if (s != null) s.close();} catch (SQLException sqlex) {}  
        	try {if (conn != null) conn.close();} catch (SQLException sqlex) {}  
        }
    	return list;
    }
	/**
	 * @param programName
	 * @return
	 */
	public List<ClientShedStrategy> getClientShedStrategysForCPP(String programName){
		List<ClientShedStrategy> list = new ArrayList<ClientShedStrategy>();
        DataSource source = null;
        Connection conn = null;
        Statement s = null;
        try {
            source = getDataSource();
            conn = source.getConnection();
        	StringBuilder sb = new StringBuilder();
			sb.append(" SELECT DISTINCT  ");
        	sb.append(" pClient.uuid,pClient.participantName AS client,pClient.parent AS participant,pParticipant.account,IFNULL(p2.participantName, '') AS parent,pp0.programName,pp0.state,pp0.uuid,");
        	sb.append(" ppr.mode,ppr.source,ppr.sortOrder,ppr.start,ppr.end,ppr.value ");
        	sb.append(" FROM  ");
        	sb.append(" (  ");
        	sb.append(" SELECT p.*   ");
        	sb.append(" FROM participant p   ");
        	sb.append(" WHERE p.client = '1' ");
        	sb.append(" ) pClient  ");
        	sb.append(" INNER JOIN participant pParticipant ON pParticipant.participantName = pClient.parent  ");
        	sb.append(" LEFT JOIN program_participant pp0   ");
        	sb.append(" ON pClient.uuid = pp0.participant_uuid   ");
        	sb.append(" LEFT JOIN program_participant pp1   ");
        	sb.append(" ON pParticipant.uuid = pp1.participant_uuid   ");
        	sb.append(" LEFT JOIN program_participant pp2   ");
        	sb.append(" ON pp2.uuid = pp1.parent_uuid    ");
        	sb.append(" LEFT JOIN participant p2   ");
        	sb.append(" ON p2.uuid = pp2.participant_uuid  ");
        	sb.append(" LEFT JOIN program_participant_rule ppr ");
        	sb.append(" ON pp0.uuid=ppr.program_participant_uuid ");
        	sb.append(" WHERE ppr.uuid IS NOT NULL AND ppr.source = 'CPP Strategy' AND pParticipant.testParticipant = '0' AND pp0.programName= ? ");
        	sb.append(" ORDER BY pClient.participantName,pp0.programName,ppr.sortOrder ");  
        	
            PreparedStatement ps =
                    conn.prepareStatement(sb.toString());
            ps.setString(1, programName);
        	
            ResultSet rs = ps.executeQuery();            
            while (rs.next()) {
            	ClientShedStrategy instance = new ClientShedStrategy();
            	//client
            	String client =rs.getString("client");
            	instance.setClient(client);
        		//participant
            	String participant =rs.getString("participant");
            	instance.setParticipant(participant);
        		//account number
            	String account =rs.getString("account");
            	instance.setAccountNumber(account);
        		//parent
            	String parent =rs.getString("parent");
            	instance.setParent(parent);
            	//program
        		String program =rs.getString("programName");
        		instance.setProgram(program);       		
        		//mode
        		String mode =rs.getString("mode");
        		instance.setMode(mode);
        		//source
        		String sourceValue =rs.getString("source");
        		instance.setSource(sourceValue);
        		//sortOrder
        		String sortOrder =rs.getString("sortOrder");
        		instance.setSortOrder(sortOrder);
        		//start
        		String start =rs.getString("start");
        		instance.setStart(start);
        		//end
        		String end =rs.getString("end");
        		instance.setEnd(end);
        		//value
        		String value =rs.getString("value");
        		instance.setRtpValue(value);
        		//state
        		String state =rs.getString("state");
        		instance.setState(state);
        		
        		instance = ClientShedStrategy.build(instance);
        		
        		list.add(instance);
            }
            list = ClientShedStrategy.transfer(list);        
        } catch (SQLException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } catch (NamingException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } finally {
            try {if (s != null) s.close();} catch (SQLException sqlex) {}  
        	try {if (conn != null) conn.close();} catch (SQLException sqlex) {}  
        }
    	return list;
	}

	/**
	 * @param programName
	 * @return
	 */
	public List<ClientShedStrategy> getClientShedStrategysForDBP(String programName){
		List<ClientShedStrategy> list = new ArrayList<ClientShedStrategy>();	       
        DataSource source = null;
        Connection conn = null;
        Statement s = null;
        try {
            source = getDataSource();
            conn = source.getConnection();
        	StringBuilder sb = new StringBuilder();
        	sb.append(" SELECT DISTINCT tmp.client, tmp.programName,tmp.state,tmp.programType,tmp.participant,tmp.account,IFNULL(parent.participantName, '') AS parent,tmp.uuid , blm.*  ");
        	sb.append(" FROM  ");
        	sb.append(" (        	  ");
        	sb.append(" SELECT client.uuid,client.participantName AS client, pp1.programName,pp1.state, program.programType,client.parent AS participant,participant.account ,pp2.parent_uuid   ");
        	sb.append(" FROM 	participant client ,  ");
        	sb.append(" 	program program,  ");
        	sb.append(" 	program_participant pp1,  ");
        	sb.append(" 	participant participant,  ");
        	sb.append(" 	program_participant pp2  ");
        	sb.append(" WHERE program.programType='DBPProgram' AND program.uuid= pp1.program_uuid AND client.client = '1' AND participant.testParticipant = '0' AND pp1.programName= ? ");
        	sb.append(" AND pp1.participant_uuid = client.uuid  ");
        	sb.append(" AND client.parent=participant.participantName  ");
        	sb.append(" AND pp2.participant_uuid=participant.uuid  ");
        	sb.append(" ) tmp  ");
        	sb.append(" LEFT JOIN program_participant pp3     ");
        	sb.append(" ON pp3.uuid =tmp.parent_uuid      ");
        	sb.append(" LEFT JOIN participant parent     ");
        	sb.append(" ON parent.uuid = pp3.participant_uuid   ");
        	sb.append(" LEFT JOIN bid_level_mapping blm  ");
        	sb.append(" ON blm.participant_uuid = tmp.uuid  ");
        	sb.append(" WHERE blm.uuid IS NOT NULL  ");
        	sb.append(" ORDER BY tmp.client,tmp.programName  ");
            
            PreparedStatement ps =
                    conn.prepareStatement(sb.toString());
            ps.setString(1, programName);
        	
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
            	ClientShedStrategy instance = new ClientShedStrategy();
            	//client
            	String client =rs.getString("client");
            	instance.setClient(client);
        		//participant
            	String participant =rs.getString("participant");
            	instance.setParticipant(participant);
        		//account number
            	String account =rs.getString("account");
            	instance.setAccountNumber(account);
        		//parent
            	String parent =rs.getString("parent");
            	instance.setParent(parent);
            	//program
        		String program =rs.getString("programName");
        		instance.setProgram(program);
        		//programType
        		String programType =rs.getString("programType");
        		instance.setProgramType(programType);
        		//timeBlock
        		String timeBlock =rs.getString("timeBlock");
        		instance.setTimeBlock(timeBlock);
        		//normal
        		String normal =rs.getString("normal");
        		instance.setNormal(normal);
        		//moderate
        		String moderate =rs.getString("moderate");
        		instance.setModerate(moderate);
        		//high
        		String high =rs.getString("high");
        		instance.setHigh(high);        	
        		//state
        		String state =rs.getString("state");
        		instance.setState(state);
        		
        		
        		instance = ClientShedStrategy.build(instance);
        		list.add(instance);
            }
            list = ClientShedStrategy.transfer(list);        
        } catch (SQLException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } catch (NamingException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } finally {
            try {if (s != null) s.close();} catch (SQLException sqlex) {}  
        	try {if (conn != null) conn.close();} catch (SQLException sqlex) {}  
        }
    	return list;
	}

	private static String getSQLFromFile(String sqlFileName){
		String sql = "";
		InputStream is = null;
		try{  
			is = ProgramManagerBean.class.getResourceAsStream("/com/akuacom/pss2/query/" + sqlFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String s = "";
			while ((s = br.readLine()) != null) {
				sql = sql + s + " \n ";
			}
		}
		catch (Exception e) {
			log.error("Unable to load SQL file. " +sqlFileName);
			log.debug(e.getStackTrace());
		}
		finally{
			if(is != null){ try {is.close();}catch(Exception e){};}
		}
		return sql;
	}
}
