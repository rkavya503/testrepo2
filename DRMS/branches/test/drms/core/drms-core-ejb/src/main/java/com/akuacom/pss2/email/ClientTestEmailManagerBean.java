package com.akuacom.pss2.email;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.akuacom.pss2.asynch.AsynchCaller;
import com.akuacom.pss2.asynch.EJBAsynchRunable;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactManager;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.task.RoutineStatus;
import com.akuacom.pss2.task.RoutineStatusEAO;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;
import com.kanaeki.firelog.util.FireLogEntry;

@Stateless
public class ClientTestEmailManagerBean extends TimerManagerBean 
	implements ClientTestEmailManager.L,ClientTestEmailManager.R {

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
	private static final Logger log = Logger.getLogger(ClientTestEmailManagerBean.class);
	private static final int TIMER_DAILY_REFRESH_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24
	private static final String Client_Test_Email_Consolidation_TIMER = "ClientTestEmailDigestTIMER";
	
	@Override
	public void scheduleTimer() {
		// Stop running timer before create new timer
		cancelTimers();
		Date nextDay = getInvokeTime();
		
		timerService.createTimer(nextDay,
				TIMER_DAILY_REFRESH_INTERVAL_MS, TIMER);
		
		FireLogEntry logEntry = LogUtils.createLogEntry();
		logEntry.setDescription(Client_Test_Email_Consolidation_TIMER + " [ " + nextDay+ " ] is invoked.");
		log.debug(logEntry);
		
		// if the timer is executed successfully today, execute during server start-up
		RoutineStatus status = routineStatusEAO.getRoutineStatus(Client_Test_Email_Consolidation_TIMER, new Date());
		if(status.getStatus()==false){
		     asynchCaller.call(new EJBAsynchRunable(
		    		 ClientTestEmailManager.class, 
     				 "invokeTimer",
     				 new Class[]{},
     				 new Object[]{}));
		}
	}
	private Date getInvokeTime() {
		Date nextDay = DateUtil.add(new Date(), Calendar.DATE, 1);
		
		DateFormat dt = new SimpleDateFormat("hh:mm:ss");
		Calendar cal2 = Calendar.getInstance();
		try {
			cal2.setTime(dt.parse("01:00:00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(nextDay);
		
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), cal2.get(Calendar.HOUR_OF_DAY), cal2.get(Calendar.MINUTE), cal2.get(Calendar.SECOND));
		nextDay = cal.getTime();
		return nextDay;
	}
	
	@Override
	public void invokeTimer() {
		FireLogEntry logEntry = LogUtils.createLogEntry();
		logEntry.setDescription(Client_Test_Email_Consolidation_TIMER + " [ " + new Date()+ " ] is invoked.");
		log.debug(logEntry);
		emailConsolidationAction();
		RoutineStatus status = routineStatusEAO.getRoutineStatus(Client_Test_Email_Consolidation_TIMER, new Date());
		status.setStatus(true);
		routineStatusEAO.insertOrUpdate(status);
	}
	
	@Override
	public void createTimers() {
		scheduleTimer();
//		cancelTimers();
//		timerService.createTimer(getStartDate(), 
//					TIMER_DAILY_REFRESH_INTERVAL_MS
//					,TIMER);
////		timerService.createTimer(new Date(), 
////				60*1000
////				,TIMER);
//		log.debug(TIMER +" created successfully");
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


	private Date getStartDate(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
//        cal.set(Calendar.AM_PM, Calendar.AM);
        return cal.getTime();
	}
	
	private void emailConsolidationAction(){
		if(systemManager.getPss2Features().isClientTestEmailConsolidationEnabled()){
			log.info("Client Test Email Consolidation Function Enable.");
			log.info("Client Test Email Consolidation Function Started.");
			int interval = (int) systemManager.getPss2Properties().getClientTestEmailConsolidationInterval();
			if(interval>90||interval<1){
				interval = 1;
			}
			Date from = DateUtil.getDate(DateUtil.getStartOfDay(new Date()), -interval);
			Date to = DateUtil.getDate(DateUtil.getEndOfDay(new Date()), -1);
			digestClientTestEmail(from,to);
			log.info("Client Test Email Consolidation Function Finished.");
    	}else{
    		log.info("Client Test Email Consolidation Function Disable.");
    	}	
	}
	@Override
	public void digestClientTestEmail(Date start,Date end){
		List<Contact> result = getDigestContactList();
		for(int i=0;i<result.size();i++){
			Contact contact = result.get(i);
			if(contact.isDigestEnable()){
				int interval = contact.getDigestInterval();
				if(interval>90||interval<1){
					interval = 1;
				}
				start = DateUtil.getDate(DateUtil.getStartOfDay(new Date()), -interval);
				end = DateUtil.getDate(DateUtil.getEndOfDay(new Date()), -1);
			}
			digestClientTestEmail(contact, start , end);
		}
	}

	
	private List<Contact> getDigestContactList(){
		List<Contact> result = new ArrayList<Contact>();
		//retrieve data from database
		List<Contact> operators = contactManager.getOperatorContacts();
		for(Contact operator:operators){
			if(!operator.isOptOutDigest()){
				result.add(operator);
			}
		}
		return result;
	}
	
	private void digestClientTestEmail(Contact contact,Date start,Date end){
		//todo
		String to = contact.getAddress();
		String userName="operator";
		List<MessageEntity> result = messageEAO.findByDigest(start, end, to, userName);
		MessageEntity consolidationEmail = consolidationEmails(result,to);
		sendConsolidationEmail(consolidationEmail,contact);
	}
	
	private MessageEntity consolidationEmails(List<MessageEntity> emails,String operator){
		MessageEntity result = new MessageEntity();
		StringBuilder contentBuffer = new StringBuilder();
		contentBuffer.append("Client Test Email Digest Time: " + new Date());
		contentBuffer.append("\n");
		contentBuffer.append("Operator : " + operator);
		contentBuffer.append("\n");
//		for(int i =0 ;i<emails.size();i++){
//			MessageEntity entity = emails.get(i);
//			Date createDate = entity.getCreationTime();
//			String subject = entity.getSubject();
//			contentBuffer.append(createDate+ " : " + subject);
//			contentBuffer.append("\n");
//		}
		
		Map<String,List<MessageEntity>> emailsByEvent = buildMessageByEvents(emails);
		Set<String> eventKeySet = emailsByEvent.keySet();
		Iterator<String> iterator = eventKeySet.iterator();
		while(iterator.hasNext()){
			String eventKey = iterator.next();
			List<MessageEntity> messages = emailsByEvent.get(eventKey);
			String eventContent = generateEventContent(messages,eventKey);
			contentBuffer.append(eventContent);
			contentBuffer.append("\n");
		}
		
		result.setContent(contentBuffer.toString());
		result.setSubject("Client Test Email Consolidation for "+operator);
		return result;
	}
	
	private Map<String,List<MessageEntity>> buildMessageByEvents(List<MessageEntity> emails){
		Map<String,List<MessageEntity>> result = new HashMap<String,List<MessageEntity>>();
		for(int i =0 ;i<emails.size();i++){
			MessageEntity entity = emails.get(i);
			String subject = entity.getSubject();
			String[] r = subject.split("Test_");
			if(r[1]!=null){
				String[] r1 = r[1].split(" ");
				if(r1[0]!=null){
					String key = "Test_"+r1[0];
					//System.out.println(r1[0]);
					if(result.containsKey(key)){
						List<MessageEntity> messages = result.get(key);
						messages.add(entity);
					}else{
						List<MessageEntity> messages = new ArrayList<MessageEntity>();
						messages.add(entity);
						result.put(key, messages);
					}
					
				}
			}
		}	
		return result;
	}
	private String generateEventContent(List<MessageEntity> messages,String eventName){
		String result = "";
		String content="";
		List<String> subject = new ArrayList<String>(); 
		for(int i =0;i<messages.size();i++){
			MessageEntity message = messages.get(i);
			subject.add(message.getSubject()+":"+ DateUtil.format(message.getCreationTime())+"\t\n");
			if(i==0){
				content = message.getContent();
			}
		}
		StringBuilder contentBuffer = new StringBuilder();
		
		contentBuffer.append("\n");
		contentBuffer.append("Event["+eventName+"] details:");
		contentBuffer.append("\n");
		contentBuffer.append(content);
		contentBuffer.append("Event["+eventName+"] operator mails details:");
		contentBuffer.append("\n");
		for(int i =0;i<subject.size();i++){
			String contentSubject = subject.get(i);
			contentBuffer.append(contentSubject);
		}
		
		result= contentBuffer.toString();
		//result=result.replaceAll("Participants have opted out of", "\nParticipants have opted out of");
		return result;
	}
	
	private void sendConsolidationEmail(MessageEntity consolidationEmail,Contact contact){
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(contact);
		notifier.sendNotification(contacts, "digester", consolidationEmail.getSubject(), consolidationEmail.getContent(),
				NotificationMethod.getInstance(),
                new NotificationParametersVO(), Environment.isAkuacomEmailOnly(),
                "");
//		try {
//			notifier.sendNotification(contacts, "digester", consolidationEmail.getSubject(), consolidationEmail.getContent(), NotificationMethod.getInstance(),
//					new NotificationParametersVO(), Environment.isAkuacomEmailOnly(),false,false, "", MessageEntity.PRIORITY_URGENT);
//	    	}catch(JMSException e){
//	    		throw new EJBException("Exceptions in JMS Channel");
//	    }
		
	}
	@Override
	public List<MessageEntity> findDigestMessage(Date start, Date end,List<Contact> operators){
		List<MessageEntity> result = new ArrayList<MessageEntity>();
		if(start!=null&&end!=null){
			if(operators==null){
				operators = contactManager.getOperatorContacts();
			}
			List<String> address = new ArrayList<String>();
			for(Contact operator:operators){
				address.add(operator.getAddress());
			}
			
			result = messageEAO.findDigestMessage(start, end, address);
		}
		return result;
	}
	
	@Override
	public List<JSFClientTestEmailReportEntity> getReport(Date startDate,Date endDate) {
		List<JSFClientTestEmailReportEntity> entities = new ArrayList<JSFClientTestEmailReportEntity>();
		ResultSet rs = null;
        DataSource source = null;
        Connection conn = null;
        Statement s = null;
        try {
            source = getDataSource();
            conn = source.getConnection();
            s = conn.createStatement();
            
        	StringBuilder sb = new StringBuilder();
        	sb.append(" select distinct pClient.parent as participant, pClient.participantName as client, 'Client Test' as programName, ");
        	sb.append(" pc.description as contactName, pc.address as contactAddress, m.creationTime as createTime, m.subject ");
        	sb.append(" from ( select distinct p.* from participant p left join ");
        	sb.append(" (select pm.value,pm.participant_uuid from participant_manualsignal pm where pm.name ='mode') table2 ");
        	sb.append(" on p.uuid = table2.participant_uuid where p.client = '1' ) pClient  ");
        	sb.append(" inner join participant pParticipant on pParticipant.participantName = pClient.parent ");
        	sb.append(" inner join participant_contact pc on pc.participant_uuid = pClient.uuid ");
        	sb.append(" inner join message m on pc.address = m.to and m.subject like '%Client Test %' and m.userName!='operator' and m.userName !='digester' and m.userName  = pClient.participantName ");
        	sb.append(" and m.creationTime >= ? and m.creationTime < ? ");
            sb.append(" order by m.creationTime ");
            
            PreparedStatement ps =conn.prepareStatement(sb.toString());

        	ps.clearParameters();
        	endDate = DateUtil.getNextDay(endDate);
        	ps.setDate(1, new java.sql.Date(startDate.getTime()));
        	ps.setDate(2, new java.sql.Date(endDate.getTime()));
        	
			rs = ps.executeQuery();
			while (rs.next()) {
				JSFClientTestEmailReportEntity e = new JSFClientTestEmailReportEntity();
				e.setParticipantName(rs.getString("participant"));
				e.setClientName(rs.getString("client"));
				e.setProgramName(rs.getString("programName"));
				e.setContactName(rs.getString("contactName"));
				e.setContactAddress(rs.getString("contactAddress"));
				e.setCreateTime(rs.getDate("createTime").toString());
				e.setSubjectList(rs.getString("subject"));
				entities.add(e);
			}
        } catch (SQLException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } catch (NamingException e) {
            log.debug(LogUtils.createExceptionLogEntry("", this.getClass().getName(), e));
        } finally {
            //A ResultSet object is automatically closed by the Statement object that generated it 
            //when that Statement object is closed, re-executed, or is used to retrieve the next result from a sequence of multiple results. 
        	try {if (s != null) s.close();} catch (SQLException sqlex) {}  
        	try {if (conn != null) conn.close();} catch (SQLException sqlex) {}  
        }
        return entities; 
    }
	
	private DataSource getDataSource() throws NamingException {
        Context context = new InitialContext();
        DataSource ds = (DataSource) context.lookup("java:mysql-pss2-ds");
        context.close();
        return ds;
    }

}
