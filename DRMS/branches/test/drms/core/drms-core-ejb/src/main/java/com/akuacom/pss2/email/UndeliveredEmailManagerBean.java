package com.akuacom.pss2.email;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.jdbc.CellConverter;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.asynch.AsynchCaller;
import com.akuacom.pss2.asynch.EJBAsynchRunable;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.program.ProgramManagerBean;
import com.akuacom.pss2.query.NativeQueryManagerBean;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.task.RoutineStatus;
import com.akuacom.pss2.task.RoutineStatusEAO;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;
import com.kanaeki.firelog.util.FireLogEntry;

@Stateless
public class UndeliveredEmailManagerBean extends TimerManagerBean implements UndeliveredEmailManager.L,UndeliveredEmailManager.R {
	
	private static final Logger log = Logger.getLogger(NativeQueryManagerBean.class);
	private static final String GET_UNDELIVERED_EMAILS;
	private static final String GET_EMAILS_COUNTS;
	@EJB
	SystemManager.L sysManager;
	static {
		GET_UNDELIVERED_EMAILS = getSQLFromFile("getUndeliveredEmails.sql");
		GET_EMAILS_COUNTS = getSQLFromFile("getEmailCounts.sql");
		
	}

	@EJB
	Pss2SQLExecutor.L sqlExecutor;
	
	/**
	 *[From start Date  to end date ]
	 */
	@Override
	public List<EmailReportEntity> getUndeliveredEmails(Date start, Date end) throws SQLException{
		String sqltemplate =GET_UNDELIVERED_EMAILS;
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("startTime", start);
		params.put("endTIme", end);
		log.debug("get undelivered emails from "+start+"  to "+end);
		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			List<EmailReportEntity> results = sqlExecutor.doNativeQuery(sql,params, new ListConverter<EmailReportEntity>(new ColumnAsFeatureFactory<EmailReportEntity>(EmailReportEntity.class)));
			return results;
		}catch(Exception e){
			log.error(e);
			throw new SQLException(e);
		}
	}
	
	@Override
	public int getEmailsCounts(Date start, Date end) throws SQLException {
		String sqltemplate =GET_EMAILS_COUNTS;
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("startTime", start);
		params.put("endTIme", end);
		log.debug("getEmailsCounts emails from "+start+"  to "+end);
		try{
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			int count = sqlExecutor.doNativeQuery(sql,params,new CellConverter<Integer>(Integer.class));
			return count;
		}catch(Exception e){
			log.error(e);
			throw new SQLException(e);
		}
	}
	
   private static String getSQLFromFile(String sqlFileName){
		String sql = "";
		InputStream is = null;
		try{  
			is = ProgramManagerBean.class.getResourceAsStream("/com/akuacom/pss2/email/" + sqlFileName); 
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
   
   @Override
	public void scheduleTimer() {
		// Stop running timer before create new timer
		cancelTimers();
		Date invokeTime = getInvokeTime();
		
		timerService.createTimer(invokeTime,
				TIMER_DAILY_REFRESH_INTERVAL_MS, TIMER);
		
		FireLogEntry logEntry = LogUtils.createLogEntry();
		logEntry.setDescription("UndeliveredEmail_TIMER" + " [ " + invokeTime+ " ] is invoked.");
		log.debug(logEntry);
		
		
		if(DateUtils.isSameDay(invokeTime, new Date())){
			return;
		}
		
		// if the timer is executed successfully today, execute during server start-up
		RoutineStatus status = routineStatusEAO.getRoutineStatus(UndeliveredEmail_TIMER, new Date());
		if(status.getStatus()==false){
		     asynchCaller.call(new EJBAsynchRunable(
		    		 UndeliveredEmailManagerBean.class, 
    				 "invokeTimer",
    				 new Class[]{},
    				 new Object[]{}));
		}
	}
	private Date getInvokeTime() {
		//TODO: make this to be configurable
		Date nextDay = DateUtil.add(new Date(), Calendar.DATE, 1);
		Calendar today = Calendar.getInstance();
		
		DateFormat dt = new SimpleDateFormat("hh:mm:ss");
		Calendar invokeTime = Calendar.getInstance();
		
		String configuredTime = sysManager.getPss2Properties().getUndeliveredEmailSendTime();
		try {
			invokeTime.setTime(dt.parse(configuredTime));
			invokeTime.set(Calendar.YEAR, today.get(Calendar.YEAR));
			invokeTime.set(Calendar.MONTH, today.get(Calendar.MONTH));
			invokeTime.set(Calendar.DATE, today.get(Calendar.DATE));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(invokeTime.before(Calendar.getInstance())){
			//Today's schedule time passed, start at next day
			today.setTime(nextDay);
			today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE), invokeTime.get(Calendar.HOUR_OF_DAY), invokeTime.get(Calendar.MINUTE), invokeTime.get(Calendar.SECOND));
			nextDay = today.getTime();
			return nextDay;
		}else{
			return invokeTime.getTime();
		}
		
	}
	
	@Override
	public void invokeTimer() {
		FireLogEntry logEntry = LogUtils.createLogEntry();
		logEntry.setDescription(UndeliveredEmail_TIMER + " [ " + new Date()+ " ] is invoked.");
		log.debug(logEntry);
		
		sendReportEmail();
		
		RoutineStatus status = routineStatusEAO.getRoutineStatus(UndeliveredEmail_TIMER, new Date());
		status.setStatus(true);
		routineStatusEAO.insertOrUpdate(status);
	}
	
	public void sendReportEmail() {
		if(systemManager.getPss2Features().isEnableUndeliveredEmail()){
			log.info("UndeliveredEmailManagerBean Enable.");
			log.info("UndeliveredEmailManagerBean Started.");
			Date yesterday = DateUtil.getDate(new Date(), -1);
			try {
				sendReportEmail(yesterday);
			} catch (SQLException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
			log.info("UndeliveredEmailManagerBean Finished.");
    	}else{
    		log.info("UndeliveredEmailManagerBean Disable.");
    	}	
	}
	private void sendReportEmail(Date date) throws SQLException{
		MessageEntity consolidationEmail = constructEmails(date);
		List<Contact> contacts = getDigestContactList();
		notifier.sendNotification(contacts, 
				"operator", consolidationEmail.getSubject(), consolidationEmail.getContent(), consolidationEmail.getAttachFilename(), consolidationEmail.getAttachFileContent(), 
				NotificationMethod.getInstance(),
                new NotificationParametersVO(), Environment.isAkuacomEmailOnly(),
                "");
	}
	
	private static Double convertNumber(Double in){
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(false);
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(2);
		
		return getDoubleValue(in, nf);
	}
	
	private static Double getDoubleValue(Double in, NumberFormat nf){
	   	
	   	if(Double.isNaN(in)) return 0.0;
	   	
	   	return Double.valueOf(nf.format(in));
	}
	
	//Construct message contact
	private MessageEntity constructEmails(Date date) throws SQLException{
		Date start = DateUtil.getStartOfDay(date);
		Date end = DateUtil.getEndOfDay(date);
		List<EmailReportEntity> entities = getUndeliveredEmails(start, end);
		int counts = getEmailsCounts(start, end);
		Double rate = 0.0;
		if(counts!=0){
			rate = new Double(entities.size())*100.0/counts;
		}
		rate = convertNumber(rate);
		
//		String contentType =null;
//		try {
//			contentType = systemManager.getPropertyByName("emailContentType").getStringValue();
//			
//		} catch (EntityNotFoundException e) {
//			e.printStackTrace();
//		}
		
		String lineBreak = " \n";
		
		MessageEntity result = new MessageEntity();
		StringBuilder contentBuffer = new StringBuilder(); //dateTimeFormat + " z";
		contentBuffer.append("Undelivered Emails Daily Summary for :"+DateUtil.format(date,"MMM dd, yyyy z"));
		contentBuffer.append(", generated at :"+DateUtil.format(new Date()) +". ");
		contentBuffer.append(lineBreak);
		
		CoreProperty corp = null;
		try {
			corp = systemManager.getPropertyByName("utilityDisplayName");
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		
		if(entities==null||entities.isEmpty()||counts==0){// all emails send successfully
			contentBuffer.append("All emails sent out successfully.");   
			contentBuffer.append(lineBreak);
		}else{
			
			contentBuffer.append("Undelivered : "+entities.size());   
			contentBuffer.append(lineBreak);
			contentBuffer.append("Total       : "+counts);    
			contentBuffer.append(lineBreak);
			contentBuffer.append(rate+"% of total emails DRAS failed to send out.");   
			contentBuffer.append(lineBreak);
//			contentBuffer.append(entities.size()+" failed emails / "+counts+" total email, "+rate+"% of total emails DRAS failed to send out. \n");  
//			contentBuffer.append(rate+"%("+entities.size()+"/"+counts+") of total emails DRAS failed to send out.\n");    	
			if(entities.size()>300){
				String serverHost =null;
				try {
					serverHost = systemManager.getPropertyByName("serverHost").getStringValue();
				} catch (EntityNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				contentBuffer.append("The first 300 undelivered emails are listed in attached file,");    
				contentBuffer.append("for complete report please click "+serverHost);    
			
				entities = entities.subList(0, 300);
			}
			contentBuffer.append(lineBreak);
			
			result.setAttachFilename(corp.getStringValue()+"_DailyUndeliveredEmails_"+DateUtil.formatDate(date)+".csv");
			result.setAttachFileContent(generateContent(date,entities,counts,rate));
		}
    	
		result.setContent(contentBuffer.toString());
		result.setSubject(corp.getStringValue()+" Undelivered Email Daily Summary "+DateUtil.format(date,"MMM dd, yyyy"));
		return result;
	}

	/**
	 * Total emails in last day
Total failed emails in last day
% of failing
Suggest format: failed emails / total email, n% of total emails DRAS failed to send out
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	private String generateContent(Date date, List<EmailReportEntity> entities, int counts, Double rate) throws SQLException{
		
    	String result ="";
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append("Time of Email,Subject Line,Contact Name,Email Address,Participant Name,Client Name,\n");
    	for(EmailReportEntity entity : entities){
    		writeNext(sb, DateUtil.format(entity.getCreateTime()));
    		writeNext(sb,entity.getSubject());
    		writeNext(sb,entity.getContactName());
    		writeNext(sb,entity.getContactAddress());
    		writeNext(sb,entity.getParticipantName());
    		writeNext(sb,entity.getClientName());

    		sb.append("\n");
    	}
    	
    	result = sb.toString();
    	return result;
    }
	
	private List<Contact> getDigestContactList(){
		List<Contact> result = new ArrayList<Contact>();
		//retrieve data from database
		//List<Contact> operators = contactManager.getOperatorContacts();
		EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
		List<Contact> operators =  cache.getEscacheforoperatorcontacts();
        if(operators.isEmpty()){
        	operators = contactManager.getOperatorContacts();
        	cache.setEscacheforoperatorcontacts("OperatorContacts", operators);
        }
		for(Contact operator:operators){
			if(!operator.isOptOutUndeliveredReport()){
				result.add(operator);
			}
		}
		return result;
	}
	
	//**************************************
	
	@Override
	public void createTimers() {
		scheduleTimer();
	}

	@Override
	@Timeout
	public void timeoutHandler(Timer timer) {
		invokeTimer();
	}
	@Override
	public void cancelTimers() {
		Collection<?> timersList = timerService.getTimers();
	    super.cancelTimers(timersList);
	}
	


	@Override
	public String getTimersInfo() {
		Collection<?> timersList = timerService.getTimers();
		return super.getTimersInfo(timersList);
	}

	
	@Resource
	private TimerService timerService;
	@EJB 
	private SystemManager.L systemManager;
	
	@EJB
	private ContactManager.L contactManager;
	@EJB
	private MessageEAO.L messageEAO;
	@EJB
    protected Notifier.L notifier;
	@EJB
	RoutineStatusEAO.L routineStatusEAO;
	@EJB
	AsynchCaller.L asynchCaller;
	private static final String TIMER= ClientTestEmailManager.class.getName();
	private static final int TIMER_DAILY_REFRESH_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24
	private static final String UndeliveredEmail_TIMER = "UndeliveredEmail_TIMER";

	//************************************************
		private static final char QUOTECHAR ='"';
		private static final char ESCAPECHAR ='"';
		private static final char SEPARATOR =',';
		private static final String NULLCHAR ="null";
		private static final int INITIAL_STRING_SIZE = 128;
		
	    private StringBuffer writeNext(StringBuffer sb, String nextElement) {
	    	if(nextElement==null||NULLCHAR.equalsIgnoreCase(nextElement.trim())){
	    		sb.append(QUOTECHAR);
	    		sb.append(QUOTECHAR);
	    	    sb.append(SEPARATOR);
	    	    
	    	    return sb;
	    	}
	    	sb.append(QUOTECHAR);
	        sb.append(stringContainsSpecialCharacters(nextElement) ? processLine(nextElement) : nextElement);
	        sb.append(QUOTECHAR);
	        sb.append(SEPARATOR);
	        
	        return sb;
	    }

		private boolean stringContainsSpecialCharacters(String line) {
		    return line.indexOf(QUOTECHAR) != -1;
		}
		
		protected StringBuilder processLine(String nextElement)
		{
			StringBuilder sb = new StringBuilder(INITIAL_STRING_SIZE);
		    for (int j = 0; j < nextElement.length(); j++) {
		        char nextChar = nextElement.charAt(j);
		        if (nextChar == QUOTECHAR) {
		        	sb.append(ESCAPECHAR).append(nextChar);
		        } else {
		            sb.append(nextChar);
		        }
		    }
		    
		    return sb;
		}

}
