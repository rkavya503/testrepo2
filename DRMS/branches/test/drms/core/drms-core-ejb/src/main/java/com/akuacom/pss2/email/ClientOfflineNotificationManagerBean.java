package com.akuacom.pss2.email;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.asynch.AsynchCaller;
import com.akuacom.pss2.asynch.EJBAsynchRunable;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantEAO;
import com.akuacom.pss2.participant.contact.ParticipantContact;
import com.akuacom.pss2.report.entities.ClientOfflineReportEntity;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.task.RoutineStatus;
import com.akuacom.pss2.task.RoutineStatusEAO;
import com.akuacom.pss2.task.TimerConfig;
import com.akuacom.pss2.task.TimerConfigEAO;
import com.akuacom.pss2.task.TimerConfigManager;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.pss2.util.Environment;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;
import com.kanaeki.firelog.util.FireLogEntry;

@Stateless
public class ClientOfflineNotificationManagerBean extends TimerManagerBean 
	implements ClientOfflineNotificationManager.L,ClientOfflineNotificationManager.R {
	@EJB 
	private SystemManager.L systemManager;
	@Resource
	private TimerService timerService;
	@EJB
	RoutineStatusEAO.L routineStatusEAO;
	@EJB
	TimerConfigEAO.L timerConfigEAO;
	@EJB
	AsynchCaller.L asynchCaller;
	@EJB
    protected Notifier.L notifier;
	@EJB
    ParticipantEAO.L participantEAO;
	@EJB
	Pss2SQLExecutor.L pss2SqlExecutor;
	private static final String TIMER= ClientOfflineNotificationManagerBean.class.getName();
	private static final Logger log = Logger.getLogger(ClientOfflineNotificationManager.class);
	private static final int TIMER_DAILY_REFRESH_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24
	private static final int TIMER_WEEKLY_REFRESH_INTERVAL_MS = 7 * 24 * 60 * 60 * 1000; // 24
	private static final String CLIENT_OFFLINE_NOTIFICATION_EMAIL_DAILY_TIMER = "ClientOffDailyMailTimer";
	private static final String CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER = "ClientOffWeeklyMailTimer";
	private long interval_summer=0;
	private long interval_unsummer=0;
	private TimerConfig summerConfig;
	private TimerConfig unSummerConfig;
	private Calendar timerBeginCalendar;
	private Calendar timerEndCalendar;
	
	private void startSummerTimerSimple(){
		//0 retrieve config time
		//1 construct the next invoke time
		//2 schedule timer
		TimerConfig config = getSummerConfig();
		int hour = config.getInvokeHour();
		int min = config.getInvokeMin();
		Calendar systemTime = Calendar.getInstance();
		Calendar configTime = Calendar.getInstance();
		configTime.set(Calendar.HOUR_OF_DAY, hour);
		configTime.set(Calendar.MINUTE, min);
		configTime.set(Calendar.SECOND, 0);
		Date invokeTime;
		if(systemTime.before(configTime)){
			invokeTime = configTime.getTime();
		}else{
			configTime.add(Calendar.DAY_OF_YEAR, 1);
			invokeTime = configTime.getTime();
		}
		timerService.createTimer(invokeTime,TIMER_DAILY_REFRESH_INTERVAL_MS, TIMER);
		log.info("Client Offline Notification timer will be invoked at :"+invokeTime);
	}
	private void startWinterTimerSimple(){
		TimerConfig config = getUnSummerConfig();
		int hour = config.getInvokeHour();
		int min = config.getInvokeMin();
		int day = config.getDay();//Su-0,Mo-1,Tu-2,We-3,Th-4,Fr-5,Sa-6
		Calendar systemTime = Calendar.getInstance();
		Calendar configTime = Calendar.getInstance();
		configTime.set(Calendar.HOUR_OF_DAY, hour);
		configTime.set(Calendar.MINUTE, min);
		configTime.set(Calendar.SECOND, 0);
		configTime.set(Calendar.DAY_OF_WEEK, day+1);
		Date invokeTime;
		if(systemTime.before(configTime)){
			invokeTime = configTime.getTime();
		}else{
			configTime.add(Calendar.WEEK_OF_YEAR, 1);
			invokeTime = configTime.getTime();
		}
		timerService.createTimer(invokeTime,TIMER_WEEKLY_REFRESH_INTERVAL_MS, TIMER);
		log.info("Client Offline Notification function will be invoked at :"+invokeTime);
	}
	private void startSummerTimer(){
		//0 retrieve summer config time and system time
		//1 whether the daily timer has been exceed today?
		//1.1 not exceed today
		//1.1.1 the summer config time has not been reached today yet, set the timer invoke at summer config time today and the timer interval as 1 day.
		//1.1.2 the summer config time has been reached today, set the timer invoke at summer config time next day and the timer interval as 1 day.
		//		invoke the client offline notification function today immediately.
		//1.2 exceed today,set the timer invoke at summer config time next day and the timer interval as 1 day.
		try {
			TimerConfig config = getSummerConfig();
			int hour = config.getInvokeHour();
			int min = config.getInvokeMin();
			Calendar systemTime = Calendar.getInstance();
			Calendar configTime = Calendar.getInstance();
			configTime.set(Calendar.HOUR_OF_DAY, hour);
			configTime.set(Calendar.MINUTE, min);
			configTime.set(Calendar.SECOND, 0);
			RoutineStatus status = routineStatusEAO.getRoutineStatus(CLIENT_OFFLINE_NOTIFICATION_EMAIL_DAILY_TIMER, new Date());
			if(status.getStatus()==false){
				if(systemTime.before(configTime)){
					Date invokeTime = configTime.getTime();
					timerService.createTimer(invokeTime,TIMER_DAILY_REFRESH_INTERVAL_MS, TIMER);
					log.info("Client Offline Notification timer will be invoked at :"+invokeTime);
				}else{
					configTime.add(Calendar.DAY_OF_YEAR, 1);
					Date invokeTime = configTime.getTime();
					timerService.createTimer(invokeTime,TIMER_DAILY_REFRESH_INTERVAL_MS, TIMER);
					log.info("Client Offline Notification timer will be invoked at :"+invokeTime);
					asynchCaller.call(new EJBAsynchRunable(ClientOfflineNotificationManager.class,"invokeTimer",new Class[]{},new Object[]{}));
					log.info("Client Offline Notification function will be invoked at now for asynch method");
				}
			}else{
				configTime.add(Calendar.DAY_OF_YEAR, 1);
				Date invokeTime = configTime.getTime();
				timerService.createTimer(invokeTime,TIMER_DAILY_REFRESH_INTERVAL_MS, TIMER);
				log.info("Client Offline Notification timer will be invoked at :"+invokeTime);
			}
		}catch(Exception e) {
			log.error("Client Offline notification function catch exception and the message is :"+e.getMessage());
		}
	}
	

	
	private void startWinterTimer(){
		//0 retrieve winter config time&day and system time&day(Su,Mo,Tu,We,Th,Fr,Sa)
		//1 whether the timer has been exceed this week?
		//1.1 not exceed this week
		//1.1.1 the winter config time has not been reached yet, set the timer invoke at winter config time this week and the timer interval as 1 week.
		//1.1.2 the winter config time has been reached this week, set the timer invoke at winter config time next week and the timer interval as 1 week.
		//		Do not invoke the client offline notification function this week anymore.
		//1.2 exceed this week, set the timer invoke at winter config time next week and the timer interval as 1 week.
		try {
			TimerConfig config = getUnSummerConfig();
			int hour = config.getInvokeHour();
			int min = config.getInvokeMin();
			int day = config.getDay();//Su-0,Mo-1,Tu-2,We-3,Th-4,Fr-5,Sa-6
			Calendar systemTime = Calendar.getInstance();
			Calendar configTime = Calendar.getInstance();
			configTime.set(Calendar.HOUR_OF_DAY, hour);
			configTime.set(Calendar.MINUTE, min);
			configTime.set(Calendar.SECOND, 0);
			configTime.set(Calendar.DAY_OF_WEEK, day+1);
			if(!isRecordInCurrentWeek()){
				log.info("Client Offline Notification using winter config setting and current week has not been invoked.");
				if(systemTime.before(configTime)){
					Date invokeTime = configTime.getTime();
					timerService.createTimer(invokeTime,TIMER_WEEKLY_REFRESH_INTERVAL_MS, TIMER);
					log.info("Client Offline Notification function will be invoked at :"+invokeTime);
				}else{
					log.info("Client Offline Notification winter config setting invoke time has been passed this week.");
					configTime.add(Calendar.WEEK_OF_YEAR, 1);
					Date invokeTime = configTime.getTime();
					timerService.createTimer(invokeTime,TIMER_WEEKLY_REFRESH_INTERVAL_MS, TIMER);
					log.info("Client Offline Notification function will be invoked at next week :"+invokeTime);
				}
			}else{
				log.info("Client Offline Notification using winter config setting and current week has been invoked, set the next invoke time as winter config time for the next.");
				configTime.add(Calendar.WEEK_OF_YEAR, 1);
				Date invokeTime = configTime.getTime();
				timerService.createTimer(invokeTime,TIMER_WEEKLY_REFRESH_INTERVAL_MS, TIMER);
				log.info("Client Offline Notification function will be invoked at next week :"+invokeTime);
			}
			
		}catch(Exception e) {
			log.error("Client Offline notification function catch exception and the message is :"+e.getMessage());
		}
	}
	private boolean isRecordInCurrentWeek(){
		Calendar sunday = Calendar.getInstance();
		Calendar monday = Calendar.getInstance();
		Calendar tuesday = Calendar.getInstance();
		Calendar wednesday = Calendar.getInstance();
		Calendar thursday = Calendar.getInstance();
		Calendar friday = Calendar.getInstance();
		Calendar saturday = Calendar.getInstance();
		sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		tuesday.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		wednesday.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		thursday.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		friday.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		saturday.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		List<RoutineStatus> result = new ArrayList<RoutineStatus>();
		result.add(routineStatusEAO.getRoutineStatus(CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER, sunday.getTime()));
		result.add(routineStatusEAO.getRoutineStatus(CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER, sunday.getTime()));
		result.add(routineStatusEAO.getRoutineStatus(CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER, sunday.getTime()));
		result.add(routineStatusEAO.getRoutineStatus(CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER, sunday.getTime()));
		result.add(routineStatusEAO.getRoutineStatus(CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER, sunday.getTime()));
		result.add(routineStatusEAO.getRoutineStatus(CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER, sunday.getTime()));
		result.add(routineStatusEAO.getRoutineStatus(CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER, sunday.getTime()));
		for(RoutineStatus status :result){
			if(status.getStatus()==true){
				return true;
			}
		}
		return false;
	}	
	@Override
	public void scheduleTimer() {
		// Stop running timer before create new timer
		cancelTimers();
		
//		if(isSummer(new Date())){
//			startSummerTimer();
//		}else{
//			startWinterTimer();
//		}
		//0 retrieve config time
		//1 construct the next invoke time
		//2 schedule timer
		if(isSummer(new Date())){
			startSummerTimerSimple();
		}else{
			startWinterTimerSimple();
		}
	}
	
	@Override
	public void invokeTimer() {
		FireLogEntry logEntry = LogUtils.createLogEntry();
		String message="";
		if(isSummer(new Date())){
			message = CLIENT_OFFLINE_NOTIFICATION_EMAIL_DAILY_TIMER;
		}else{
			message = CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER;
		}
		logEntry.setDescription(message + " [ " + new Date()+ " ] is invoked.");
		log.info(logEntry);
		if(isSummer(new Date())){
			businessLogic();
		}else{
			TimerConfig config = getUnSummerConfig();
			int day = config.getDay();
			Date date = new Date();
			int compareDay = date.getDay();
			if(compareDay==day){
				businessLogic();
			}
		}
		
	}
	
	@Override
	public void businessLogic(){
		//1 retrieve core properties to check this function is enabled
		if(systemManager.getPss2Features().isClientOfflineNotificationEnabled()){
			double summerThreshold =0;
			
			double unSummerThreshold = 0;
			TimerConfig config = getSummerConfig();
			if(config==null){
				summerThreshold = 24;
			}else{
				summerThreshold =  config.getThreshold();
			}
			TimerConfig configwinter = getUnSummerConfig();
			if(configwinter==null){
				unSummerThreshold = 168;
			}else{
				unSummerThreshold =  configwinter.getThreshold();
			}
			boolean isSummer = true;
			log.info("Client Offline Notification Function Enable.");
			
			log.info("Client Offline Notification Function Started.");
			Date end = new Date();
			long interval=0;
			if(isSummer(end)){
//				interval = getInterval_summer();
				interval = TIMER_DAILY_REFRESH_INTERVAL_MS;
				isSummer = true;
			}else{
//				interval = getInterval_unsummer();
				interval = TIMER_WEEKLY_REFRESH_INTERVAL_MS;
				isSummer = false;
			}
			
			Date start = new Date(end.getTime() - interval);
			
			List<ClientOfflineReportEntity> aggregatorList = getAggregationParticipant();
			
			
			
			List<ClientOfflineReportEntity> list = getEntityList(start,end);
			list = filterList(list,summerThreshold,unSummerThreshold,isSummer);
			list = generateEmailContent(list);
			Map<String,List<ClientOfflineReportEntity>> map = combineSameClient(list);
			list = filterCombine(map);
			Set<String> clientName = new HashSet<String>();
			for(ClientOfflineReportEntity instance : list){
				if(instance.getClientName()!=null&&(!instance.getClientName().equalsIgnoreCase(""))){
					clientName.add(instance.getClientName());
				}
			}
			allEnabledAggregator = this.getAllAggregator(aggregatorList);
			allNotificationNoNeedChild = this.getAllAggregatedChildNotificationNoNeed(allEnabledAggregator,list);
			allNotificationNeedChild = getAllNotificationNeedChild(clientName,allNotificationNoNeedChild,allEnabledAggregator);
			//send Agg
			if(allEnabledAggregator!=null){
				Set<String> parentSet = allEnabledAggregator.keySet();
				if(parentSet.size()>0){
					List<Participant> clientsWithContacts = participantEAO.findClientsWithContactsByParticipants(parentSet);
					sendAggregatorNotification(allEnabledAggregator,clientsWithContacts,list);
				}
			}
			//send Normal
			if(clientName.size()>0){
				List<Participant> clientsWithContacts = participantEAO.findClientsWithContacts(clientName);
				sendNormalNotification(allNotificationNeedChild,clientsWithContacts,list);
			}
			log.info("Client Offline Notification Function Finished.");
			String message="";
			if(isSummer(new Date())){
				message = CLIENT_OFFLINE_NOTIFICATION_EMAIL_DAILY_TIMER;
			}else{
				message = CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER;
			}
			RoutineStatus status = routineStatusEAO.getRoutineStatus(message, new Date());
			status.setStatus(true);
			routineStatusEAO.insertOrUpdate(status);
			log.info("Client Offline Notification Routine Status Records Update.");
			
    	}else{
    		log.info("Client Offline Notification Function Disable.");
    	}
	}
	
	private Map<String,List<ClientOfflineReportEntity>> combineSameClient(List<ClientOfflineReportEntity> list){
		Map<String,List<ClientOfflineReportEntity>> map = new TreeMap<String,List<ClientOfflineReportEntity>>();
		for(ClientOfflineReportEntity instance:list){
			String clientName = instance.getClientName();
			if(map.containsKey(clientName)){
				map.get(clientName).add(instance);
			}else{
				List<ClientOfflineReportEntity> l = new ArrayList<ClientOfflineReportEntity>();
				l.add(instance);
				map.put(clientName, l);
			}
		}
		return map;
	}
	private List<ClientOfflineReportEntity> filterCombine(Map<String,List<ClientOfflineReportEntity>> map){
		List<ClientOfflineReportEntity> result = new ArrayList<ClientOfflineReportEntity>();
		Set<String> keySet = map.keySet();
		for(String client:keySet){
			List<ClientOfflineReportEntity> l = map.get(client);
			ClientOfflineReportEntity first = null;
			String content = "";
			for(ClientOfflineReportEntity entity:l){
				if(first==null){
					first = entity;
				}
				content += entity.getContent();
			}
			if(first!=null){
//				content += first.getGenerateTimeContent();
				first.setContent(content);
				result.add(first);
			}
		}
		return result;
	}
	
	private void sendAggregatorNotification(Map<String,Set<String>> allEnabledAggregator,List<Participant> clientsWithContacts,List<ClientOfflineReportEntity> list){
		Set<String> keySet = allEnabledAggregator.keySet();
		for(String key:keySet){
			Set<String> allClients = new TreeSet<String>();
			Set<String> childrenParticipant = allEnabledAggregator.get(key);
			for(String participant:childrenParticipant){
				allClients.addAll(getChildClients(list,participant));
			}
			
			//generate subject
			String subject = "Your DRAS aggregator " + key + " offline: client(s) - Information for restoring connectivity";
			String content ="";
			//generate aggregator content
			for(ClientOfflineReportEntity instance:list){
				String participantName = instance.getParticipantName();
				if(participantName.equalsIgnoreCase(key)){
					content+=instance.getContent();
				}
			}
			//generate children content
			for(ClientOfflineReportEntity instance:list){
				String clientName = instance.getClientName();
				if(allClients.contains(clientName)){
					content+=instance.getContent();
				}
			}
			if(content.trim().equalsIgnoreCase("")){
				content +="No client offline exceed the threshold. \n";
			}
			content += "The report generated at " + DateUtil.format(new Date())+ ". \n";
			content +="\n";
			content += getMailSuffix();
			//get contacts
			for(Participant client:clientsWithContacts){
				String name = client.getParent();
				if(name.equalsIgnoreCase(key)){
					//send email
					Set<ParticipantContact> contacts = client.getContacts();
					for(ParticipantContact contact:contacts){
						notifier.sendNotification(contact,
								name, subject,
                                content,
                                NotificationMethod.getInstance(), null,
                                Environment.isAkuacomEmailOnly(),
                                false, true, "");
					}
				}
			}	
		}
	}
	private void sendNormalNotification(Set<String> allNotificationNeedChild,List<Participant> clientsWithContacts,List<ClientOfflineReportEntity> list){
		for(String key:allNotificationNeedChild){
			for(ClientOfflineReportEntity instance:list){
				String clientName = instance.getClientName();
				if(clientName.equalsIgnoreCase(key)){
					//get contacts
					for(Participant client:clientsWithContacts){
						String name = client.getParticipantName();
						if(name.equalsIgnoreCase(key)){
							//send email
							Set<ParticipantContact> contacts = client.getContacts();
							for(ParticipantContact contact:contacts){
								String content = instance.getContent();
								content += "The report generated at " + DateUtil.format(new Date())+ ". \n";
								content +="\n";
								content += getMailSuffix();
								notifier.sendNotification(contact,
										name, instance.getSubject(),
										content,
		                                NotificationMethod.getInstance(), null,
		                                Environment.isAkuacomEmailOnly(),
		                                false, true, "");
							}
						}
					}	
				}
			}
			
		}
	}
	private List<ClientOfflineReportEntity> generateEmailContent(List<ClientOfflineReportEntity> list){
		
		for(ClientOfflineReportEntity instance:list){
			String participantName = instance.getParticipantName();
			String clientName = instance.getClientName();
			Date startDate = instance.getStartTime();
			Date endDate = instance.getEndTime();
			String end = DateUtil.format(endDate);
			if(end==null||end.equalsIgnoreCase("")){
				end = " until now";
			}else{
				end = " to "+end;
			}
			Date lastContact = instance.getLastContact();
			String offline = instance.getOffline();
			
//			String subject = "DRAS client " + clientName + " offline duration exceed the thresholds in the system";
			String subject = "Your DRAS client " + clientName + " is offline - Information for restoring connectivity";
			String generateDate  = DateUtil.format(new Date());
			String content = "DRAS client " +clientName+ " of participant " +participantName +" has been offline " + offline
			+ " from " + DateUtil.format(startDate) + end 
			+". \n ";
			instance.setSubject(subject);
			instance.setContent(content);
			instance.setGenerateTimeContent("The report generated at " + generateDate+". \n");
		}
		return list;
	}
	public List<ClientOfflineReportEntity> filterList(List<ClientOfflineReportEntity> list,double summerThreshold,double unSummerThreshold,boolean isSummer) {
		List<ClientOfflineReportEntity> result = new ArrayList<ClientOfflineReportEntity>();
		
		for(ClientOfflineReportEntity entity:list){
			int duration = entity.getDuration()/60;// Duration for hour
			int threshold = 24;
			if(entity.isClientOfflineNotificationEnable()){
				if(isSummer){
					threshold = entity.getThresholdsSummer();
				}else{
					threshold = entity.getThresholdsUnSummer();
				}	
			}else{
				if(isSummer){
					threshold = (int) summerThreshold;
				}else{
					threshold = (int) unSummerThreshold;
				}
			}
			
			
			//DRMS 8396: If e.getEndTime () is null means client has never come online; so do not send email to the client.
			if(entity.getEndTime() != null)
			{	
				if(duration>=threshold){
					result.add(entity);
				}
			}
		}
		return result;
	}
	public List<ClientOfflineReportEntity> getEntityList(Date start,Date end) {
		List<ClientOfflineReportEntity> result = new ArrayList<ClientOfflineReportEntity>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select distinct child.uuid as clientUUID, c.duration, parent.optOutClientOfflineNotification,parent.clientOfflineNotificationEnable,parent.thresholdsSummer,parent.thresholdsUnSummer,child.participantName as clientName,child.parent as participantName, parent.account as accountNumber,child.commTime as lastContact, c.startTime as startTime, c.endTime  ");
    	sb.append(" from client_status c , participant child,participant parent ");
    	sb.append(" where c.client_uuid = child.uuid and child.type = 0 and c.status = 0 and child.parent = parent.participantName and parent.optOutClientOfflineNotification = 0  ");
    	sb.append(" and  (endTime is null or (endTime >= ${startTime} and endTime <= ${endTime}) ) order by clientName asc, startTime desc");
        
    	Map<String,Object> params = new HashMap<String,Object>();
		params.put("startTime", DateUtil.format(start,"yyyy-MM-dd HH:mm:ss"));
		params.put("endTime", DateUtil.format(end,"yyyy-MM-dd HH:mm:ss"));
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sb.toString(), params);
			result = pss2SqlExecutor.doNativeQuery(sql, params, new ListConverter<ClientOfflineReportEntity>(new ColumnAsFeatureFactory<ClientOfflineReportEntity>(ClientOfflineReportEntity.class)));
			Date current = new Date();
			for(ClientOfflineReportEntity entity:result){
				buildModel(entity,current);
			}
		}catch(Exception e){
			log.info(e);
			throw new EJBException(e);
		}
		return result;
	}
	private void buildModel(ClientOfflineReportEntity e,Date current){
		//if date > current date
		//offline is current - start time
		//else offlien is date - start time
		
		if(e.getStartTime()==null||current==null){
			return;
		}
		Date endDate;
		Date endTime = e.getEndTime();
		if(endTime==null){
			endDate = current;
		}else{
			endDate = endTime;
		}
		long diff = endDate.getTime()-e.getStartTime().getTime();
		long dayDiff = 1000*60*60*24;
		long hourDiff = 1000*60*60;
		long minutesDiff = 1000*60;
		long duration = diff/minutesDiff;
		long dayInterval = diff/dayDiff;
		long hourInterval = (diff%dayDiff)/hourDiff;
		long minInterval = ((diff%dayDiff)%hourDiff)/minutesDiff;
		String offline="";
		long totalMins = dayInterval*24*60+hourInterval*60+minInterval;
		offline = dayInterval+" Day(s),"+hourInterval+" Hour(s),"+minInterval+" Minute(s),"+totalMins+" Total Minute(s)";
		e.setDuration((int) duration);
		e.setOffline(offline);
		e.setGenerateTime(current);
		
		
		
		e.setOfflineDays(String.valueOf(dayInterval));
		e.setOfflineHours(String.valueOf(hourInterval));
		e.setOfflineMins(String.valueOf(minInterval));
		e.setOfflineTotalMins(String.valueOf(totalMins));
		
	}
	
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
	

	@EJB
	TimerConfigManager.L timerConfigManager;
	@Override
	public String getTimersInfo() {
		Collection<?> timersList = timerService.getTimers();
		return super.getTimersInfo(timersList);
	}
	private static final String summer="CLIENT_OFFLINE_NOTIFICATION_EMAIL_DAILY_TIMER";
	private static final String winter="CLIENT_OFFLINE_NOTIFICATION_EMAIL_WEEKLY_TIMER";
	/**
	 * @return the config
	 */
	public TimerConfig getSummerConfig() {
			
			List<TimerConfig> list = timerConfigManager.getTimerConfigList();
			for(TimerConfig instance:list){
				if(summer.equalsIgnoreCase(instance.getName())){
					summerConfig = instance;
				}
			}
			
		return summerConfig;
	}
	/**
	 * @param config the config to set
	 */
	public void setSummerConfig(TimerConfig summerConfig) {
		this.summerConfig = summerConfig;
	}
	/**
	 * @return the timerBeginCalendar
	 */
	public Calendar getSummerTimerBeginCalendar() {
		TimerConfig config = getSummerConfig();
		timerBeginCalendar = Calendar.getInstance();
		if(config==null){
			timerBeginCalendar.set(Calendar.MONTH, 5);
			timerBeginCalendar.set(Calendar.DATE, 1);
			timerBeginCalendar.set(Calendar.HOUR_OF_DAY, 0);
			timerBeginCalendar.set(Calendar.MINUTE, 30);
			timerBeginCalendar.set(Calendar.SECOND, 0);
		}else{
			timerBeginCalendar.set(Calendar.MONTH, config.getStartMonth());
			timerBeginCalendar.set(Calendar.DATE, config.getStartDay());
			timerBeginCalendar.set(Calendar.HOUR_OF_DAY, config.getInvokeHour());
			timerBeginCalendar.set(Calendar.MINUTE, config.getInvokeMin());
			timerBeginCalendar.set(Calendar.SECOND, 0);
		}
		return timerBeginCalendar;
	}
	/**
	 * @param timerBeginCalendar the timerBeginCalendar to set
	 */
	public void setTimerBeginCalendar(Calendar timerBeginCalendar) {
		this.timerBeginCalendar = timerBeginCalendar;
	}
	/**
	 * @return the timerEndCalendar
	 */
	public Calendar getSummerTimerEndCalendar() {
		TimerConfig config = getSummerConfig();
		timerEndCalendar = Calendar.getInstance();
		if(config==null){
			timerEndCalendar.set(Calendar.MONTH, 8);
			timerEndCalendar.set(Calendar.DATE, 30);
			timerEndCalendar.set(Calendar.HOUR_OF_DAY, 0);
			timerEndCalendar.set(Calendar.MINUTE, 30);
			timerEndCalendar.set(Calendar.SECOND, 0);
		}else{
			timerEndCalendar.set(Calendar.MONTH, config.getEndMonth());
			timerEndCalendar.set(Calendar.DATE, config.getEndDay());
			timerEndCalendar.set(Calendar.HOUR_OF_DAY, config.getInvokeHour());
			timerEndCalendar.set(Calendar.MINUTE, config.getInvokeMin());
			timerEndCalendar.set(Calendar.SECOND, 0);
		}
		return timerEndCalendar;
	}
	/**
	 * @param timerEndCalendar the timerEndCalendar to set
	 */
	public void setTimerEndCalendar(Calendar timerEndCalendar) {
		this.timerEndCalendar = timerEndCalendar;
	}
	
	public boolean isSummer(Date date){
		 Date begin = getSummerTimerBeginCalendar().getTime();
		 Date end = getSummerTimerEndCalendar().getTime();
		 if(date.after(begin)&&date.before(end)){
			 return true;
		 }else{
			 return false;
		 }
	}
	/**
	 * @return the interval_summer
	 */
	public long getInterval_summer() {
		TimerConfig config = getSummerConfig();
		if(config==null){
			interval_summer = TIMER_DAILY_REFRESH_INTERVAL_MS;
		}else{
			interval_summer = (long) config.getInterval();
		}
		return interval_summer;
	}
	/**
	 * @param interval_summer the interval_summer to set
	 */
	public void setInterval_summer(int interval_summer) {
		this.interval_summer = interval_summer;
	}
	/**
	 * @return the interval_unsummer
	 */
	public long getInterval_unsummer() {
		
		Date date = new Date();
		Calendar summerBeginCalendar = getSummerTimerBeginCalendar();
		long diff = summerBeginCalendar.getTime().getTime()-date.getTime();
		TimerConfig config = getUnSummerConfig();
		if(config==null){
			interval_unsummer = TIMER_WEEKLY_REFRESH_INTERVAL_MS;
		}else{
			interval_unsummer = (long) config.getInterval();
		}
		if(TIMER_WEEKLY_REFRESH_INTERVAL_MS>diff){
			interval_unsummer = TIMER_DAILY_REFRESH_INTERVAL_MS*(1+diff/TIMER_DAILY_REFRESH_INTERVAL_MS);
		}
		return interval_unsummer;
		
		
		
	}
	/**
	 * @param interval_unsummer the interval_unsummer to set
	 */
	public void setInterval_unsummer(int interval_unsummer) {
		this.interval_unsummer = interval_unsummer;
	}
	/**
	 * @return the unSummerConfig
	 */
	public TimerConfig getUnSummerConfig() {
			List<TimerConfig> list = timerConfigManager.getTimerConfigList();
			for(TimerConfig instance:list){
				if(winter.equalsIgnoreCase(instance.getName())){
					unSummerConfig = instance;
				}
			}
		return unSummerConfig;
	}
	/**
	 * @param unSummerConfig the unSummerConfig to set
	 */
	public void setUnSummerConfig(TimerConfig unSummerConfig) {
		this.unSummerConfig = unSummerConfig;
	}

	private String SQL_FIND_AGGREGATION_PARTICIPANT= 
		" select distinct parent.uuid, parent.name as participantName, child.participantName as childParticipant,parent.clientOfflineNotificationAggEnable as aggEnable " +
		" from( " +
		" select distinct p.uuid, p.participantName as name, p.clientOfflineNotificationAggEnable " +
		" from participant p " +
		" left join program_participant pp on p.uuid=pp.participant_uuid " +
		" where p.client = 0) parent " +
		" left outer join  program_participant pparent on parent.uuid =  pparent.participant_uuid " +
		" left outer join  program_participant pchild  on pchild.parent_uuid = pparent.uuid and pchild.programName=pparent.programName " +
		" left outer join  participant child on child.uuid = pchild.participant_uuid  " +
		" where child.participantName is not null " ;

	
	private List<ClientOfflineReportEntity> getAggregationParticipant() {
		List<ClientOfflineReportEntity> result = new ArrayList<ClientOfflineReportEntity>();
        StringBuilder sb = new StringBuilder();
    	sb.append(SQL_FIND_AGGREGATION_PARTICIPANT);
    	Map<String,Object> params = new HashMap<String,Object>();
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sb.toString(), params);
			result = pss2SqlExecutor.doNativeQuery(sql, params, new ListConverter<ClientOfflineReportEntity>(new ColumnAsFeatureFactory<ClientOfflineReportEntity>(ClientOfflineReportEntity.class)));
			
		}catch(Exception e){
			log.info(e);
			throw new EJBException(e);
		}
		return result;
	}

	
	private Map<String,Set<String>> allEnabledAggregator;
	private Set<String> allNotificationNoNeedChild;
	private Set<String> allNotificationNeedChild;
	
	private Set<String> getAllNotificationNeedChild(Set<String> allChild,Set<String> allNotificationNoNeedChild,Map<String,Set<String>> allEnabledAggregator){
		Set<String> result = new HashSet<String>();
		for(String instance:allChild){
			result.add(instance);
		}
		result.removeAll(allNotificationNoNeedChild);
		return result;
	}
	
	private Map<String,Set<String>> getAllAggregator(List<ClientOfflineReportEntity> list){
		Map<String,Set<String>> map = new HashMap<String,Set<String>>();
		for(ClientOfflineReportEntity instance : list){
			if(instance.isAggEnable()){
				map.put(instance.getParticipantName(), getChild(list,instance.getParticipantName()));
			}
		}
		return map;
	}
	
	
	
	private Set<String> getChild(List<ClientOfflineReportEntity> list,String rootName){
		Set<String> result = new HashSet<String>();
		for(ClientOfflineReportEntity instance : list){
			String name = instance.getParticipantName();
			if(name.equalsIgnoreCase(rootName)){
				String child = instance.getChildParticipant();
				result.add(child);
				Set<String> tmpSet = getChild(list,child);
				result.addAll(tmpSet);
			}
		}
		return result;
	}
	
	private Set<String> getChildClients(List<ClientOfflineReportEntity> list,String rootName){
		Set<String> result = new HashSet<String>();
		for(ClientOfflineReportEntity instance : list){
			String name = instance.getParticipantName();
			if(name.equalsIgnoreCase(rootName)){
				String child = instance.getClientName();
				result.add(child);
				Set<String> tmpSet = getChildClients(list,child);
				result.addAll(tmpSet);
			}
		}
		return result;
	}

	private Set<String> getAllAggregatedChildNotificationNoNeed(Map<String,Set<String>> allEnabledAggregator,List<ClientOfflineReportEntity> list){
		Set<String> result = new HashSet<String>();
		
		Set<String> keySet = allEnabledAggregator.keySet();
		
		for(String key : keySet){
			Set<String> participantSet = allEnabledAggregator.get(key);
			for(String participant:participantSet){
				for(ClientOfflineReportEntity entity:list){
					String c =entity.getClientName();
					String p = entity.getParticipantName();
					if(p.equalsIgnoreCase(participant)){
						result.add(c);
					}
				}
			}
			//remove aggregator client
			for(ClientOfflineReportEntity entity:list){
				String c =entity.getClientName();
				String p = entity.getParticipantName();
				if(p.equalsIgnoreCase(key)){
					result.add(c);
				}
			}
		}
		return result;
	}
	
	private String getMailSuffix(){
		StringBuffer sb = new StringBuffer();
		sb.append("Common restoration methods:\n");
		sb.append("         - Resetting the hardware device by powering down and up \n");
		sb.append("         - Confirming that all wiring is properly connected \n");
		sb.append("         - Checking the network to ensure the firewall is not blocking communications \n");
		sb.append("         - Confirming that the client password and the DRAS client password match \n");
		sb.append("         - Confirming the device is not configured to an old IP address \n");
		sb.append("         - Ensuring that the internet connection is working properly \n");
		sb.append("\n");
		sb.append("For further technical assistance, please contact ops@akuacom.com. For all other questions, please contact the Auto-DR Help Desk at ");
		sb.append(systemManager.getPss2Properties().getHelpDeskEmailAddress());
		sb.append(".");
		
		return sb.toString();
	}
}
