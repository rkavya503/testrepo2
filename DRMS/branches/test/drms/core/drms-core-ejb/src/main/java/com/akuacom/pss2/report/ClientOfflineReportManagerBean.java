package com.akuacom.pss2.report;

import java.text.DateFormat;
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
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.apache.log4j.Logger;

import com.akuacom.ejb.DuplicateKeyException;
import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.asynch.AsynchCaller;
import com.akuacom.pss2.asynch.EJBAsynchRunable;
import com.akuacom.pss2.email.ClientOfflineNotificationManager;
import com.akuacom.pss2.report.entities.ClientOfflineReport;
import com.akuacom.pss2.report.entities.ClientOfflineReportEntity;
import com.akuacom.pss2.system.SystemManager;
import com.akuacom.pss2.task.RoutineStatus;
import com.akuacom.pss2.task.RoutineStatusEAO;
import com.akuacom.pss2.task.TimerConfig;
import com.akuacom.pss2.task.TimerConfigEAO;
import com.akuacom.pss2.task.TimerConfigManager;
import com.akuacom.pss2.timer.TimerManagerBean;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.lang.DateUtil;
import com.kanaeki.firelog.util.FireLogEntry;

@Stateless
public class ClientOfflineReportManagerBean extends TimerManagerBean 
	implements ClientOfflineReportManager.L,ClientOfflineReportManager.R {
	@EJB 
	private SystemManager.L systemManager;
	@Resource
	private TimerService timerService;
	@EJB
	RoutineStatusEAO.L routineStatusEAO;
	@EJB
	TimerConfigEAO.L timerConfigEAO;
	@EJB
	ClientOfflineReportEAO.L clientOfflineReportEAO;
	@EJB
	AsynchCaller.L asynchCaller;
	@EJB
	TimerConfigManager.L timerConfigManager;
	@EJB
	Pss2SQLExecutor.L pss2SqlExecutor;
	private static final String TIMER= ClientOfflineReportManagerBean.class.getName();
	private static final Logger log = Logger.getLogger(ClientOfflineReportManagerBean.class);
	private static final int TIMER_DAILY_REFRESH_INTERVAL_MS = 24 * 60 * 60 * 1000; // 24
	private static final String Client_OFFLINE_REPORT_TIMER = "ClientOfflineReportTIMER";

	@Override
	public void scheduleTimer() {
		// Stop running timer before create new timer
		cancelTimers();
		
		//0 retrieve config time and system time
		//1 whether the daily timer has been exceed today?
		//1.1 not exceed today
		//1.1.1 the config time has not been reached today yet, set the timer invoke at config time today and the timer interval as 1 day.
		//1.1.2 the config time has been reached today, set the timer invoke at config time next day and the timer interval as 1 day.
		//		invoke the client offline report function today immediately.
		//1.2 exceed today,set the timer invoke at config time next day and the timer interval as 1 day.
		try {
			TimerConfig config = getConfig();
			int hour = config.getInvokeHour();
			int min = config.getInvokeMin();
			Calendar systemTime = Calendar.getInstance();
			Calendar configTime = Calendar.getInstance();
			configTime.set(Calendar.HOUR_OF_DAY, hour);
			configTime.set(Calendar.MINUTE, min);
			configTime.set(Calendar.SECOND, 0);
			RoutineStatus status = routineStatusEAO.getRoutineStatus(Client_OFFLINE_REPORT_TIMER, new Date());
			if(status.getStatus()==false){
				if(systemTime.before(configTime)){
					Date invokeTime = configTime.getTime();
					timerService.createTimer(invokeTime,TIMER_DAILY_REFRESH_INTERVAL_MS, TIMER);
					log.info("Client Offline report timer will be invoked at :"+invokeTime);
				}else{
					configTime.add(Calendar.DAY_OF_YEAR, 1);
					Date invokeTime = configTime.getTime();
					timerService.createTimer(invokeTime,TIMER_DAILY_REFRESH_INTERVAL_MS, TIMER);
					log.info("Client Offline report timer will be invoked at :"+invokeTime);
					asynchCaller.call(new EJBAsynchRunable(ClientOfflineReportManager.class,"invokeTimer",new Class[]{},new Object[]{}));
					log.info("Client Offline report function will be invoked at now for asynch method");
				}
			}else{
				configTime.add(Calendar.DAY_OF_YEAR, 1);
				Date invokeTime = configTime.getTime();
				timerService.createTimer(invokeTime,TIMER_DAILY_REFRESH_INTERVAL_MS, TIMER);
				log.info("Client Offline report timer will be invoked at :"+invokeTime);
			}
		}catch(Exception e) {
			log.error("Client Offline report function catch exception and the message is :"+e.getMessage());
		}
		
	}
	private static final String report="CLIENT_OFFLINE_REPORT_TIMER";
	private TimerConfig getConfig(){
		TimerConfig config = null;
		List<TimerConfig> list = timerConfigManager.getTimerConfigList();
		for(TimerConfig instance:list){
			if(report.equalsIgnoreCase(instance.getName())){
				config = instance;
			}
		}
		return config;
	}
	
	@Override
	public void invokeTimer() {
		FireLogEntry logEntry = LogUtils.createLogEntry();
		logEntry.setDescription(Client_OFFLINE_REPORT_TIMER + " [ " + new Date()+ " ] is invoked.");
		log.info(logEntry);
		generateReport();
		
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
	


	@Override
	public String getTimersInfo() {
		Collection<?> timersList = timerService.getTimers();
		return super.getTimersInfo(timersList);
	}
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.report.ClientOfflineReportManager#generateReport()
	 */
	@Override
	public synchronized void generateReport() {
		
		// 1, find all offline clients and fields: client name, participant name, account number, last contact, creation time
		// 2, compute the report detail
		// 3, compute the report 
		// 4, save & generate report
		if(systemManager.getPss2Features().isClientOfflineReportEnabled()){
			log.info("Client Offline Report function enabled.");
			if(isGeneratedToday()){
				log.info("Client Offline Report already generate today, not generate anymore.");
				return;
			}
			Date invokeDate = new Date();
			Date startDate = DateUtil.getDate(invokeDate, -1);
			
			List<ClientOfflineReportEntity> reportDetails = getReportDetail(startDate,invokeDate,invokeDate);
			log.info("Client Offline Report generate report retrieve "+reportDetails.size()+" report details.");
			ClientOfflineReport report = ClientOfflineReport.generateReport(reportDetails, invokeDate);
			try {
				clientOfflineReportEAO.create(report);
			} catch (DuplicateKeyException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
			RoutineStatus status = routineStatusEAO.getRoutineStatus(Client_OFFLINE_REPORT_TIMER, new Date());
			status.setStatus(true);
			routineStatusEAO.insertOrUpdate(status);
			log.info("Client Offline Report function enabled and finished.");
			log.info("Client Offline Report function update Routine Status.");
		}else{
			log.info("Client Offline Report function disabled.");
		}
			
	}
	
	private boolean isGeneratedToday() {
		List<ClientOfflineReport> result = new ArrayList<ClientOfflineReport>();
		Date date = new Date();
		Date start = DateUtil.getStartOfDay(date);
		Date end = DateUtil.endOfDay(date);
		String sqltemplate= " select * from report_client_offline where generateTime >= ${startTime} and generateTime <= ${endTime} ";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startTime", DateUtil.format(start,"yyyy-MM-dd HH:mm:ss"));
		params.put("endTime", DateUtil.format(end,"yyyy-MM-dd HH:mm:ss"));

		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			result = pss2SqlExecutor.doNativeQuery(sql, params, new ListConverter<ClientOfflineReport>(new ColumnAsFeatureFactory<ClientOfflineReport>(ClientOfflineReport.class)));
		}catch(Exception e){
			log.info(e);
			throw new EJBException(e);
		}
		if(result!=null&&result.size()>0){
			return true;
		}else{
			return false;
		}
	}

	
	@Override
	public void generateReport(Date date) {
		
		// 1, find all offline clients and fields: client name, participant name, account number, last contact, creation time
		// 2, compute the report detail
		// 3, compute the report 
		// 4, save & generate report
		if(date!=null){
			
		}else{
			date = new Date();
		}
		Date startDate = DateUtil.getDate(date, -1);
		List<ClientOfflineReportEntity> reportDetails = getReportDetail(startDate,date,date);
		ClientOfflineReport report = ClientOfflineReport.generateReport(reportDetails, date);
		try {
			clientOfflineReportEAO.create(report);
		} catch (DuplicateKeyException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}		
	}
	/* (non-Javadoc)
	 * @see com.akuacom.pss2.report.ClientOfflineReportManager#getReportSummary()
	 */
	@Override
	public List<ClientOfflineReport> getReportSummary() {
		
		List<ClientOfflineReport> result = new ArrayList<ClientOfflineReport>();
		Date date = new Date();
//		String sqltemplate= " select distinct UUID,DATE_FORMAT(s.generateTime,'%Y-%m-%d') as generateTime from report_client_offline s order by generateTime desc; ";
		String sqltemplate= " select distinct UUID,s.generateTime from report_client_offline s order by generateTime desc; ";
		Map<String,Object> params = new HashMap<String,Object>();

		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sqltemplate, params);
			result = pss2SqlExecutor.doNativeQuery(sql, params, new ListConverter<ClientOfflineReport>(new ColumnAsFeatureFactory<ClientOfflineReport>(ClientOfflineReport.class)));
		}catch(Exception e){
			log.info(e);
			throw new EJBException(e);
		}
		return result;
	}
	@Override
	public ClientOfflineReport getReport(String uuid){
		ClientOfflineReport e = new ClientOfflineReport();
		e.setUUID(uuid);
		try {
			e = clientOfflineReportEAO.get(e);
			e.getDetails().size();
		} catch (EntityNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			log.error(e1.getMessage());
		}
		return e;
	}
	public List<ClientOfflineReportEntity> getReportDetail(Date start,Date end,Date generateDate) {
		
		List<ClientOfflineReportEntity> result = new ArrayList<ClientOfflineReportEntity>();

//		String sqltemplate= " select distinct UUID,DATE_FORMAT(s.generateTime,'%Y-%m-%d') as generateTime from report_client_offline s order by generateTime desc; ";
        StringBuilder sb = new StringBuilder();
    	sb.append(" select distinct p.participantName as clientName,p.parent as participantName, parent.account as accountNumber,p.commTime as lastContact, c.startTime as startTime, c.endTime ");
    	sb.append(" from client_status c , participant p,participant parent ");
    	sb.append(" where c.client_uuid = p.uuid and p.type = 0 and c.status = 0 and p.parent = parent.participantName");
    	sb.append(" and ((c.startTime >= ${startTime} and c.startTime <= ${endTime} ) or ( c.endTime >= ${startTime} and c.endTime <= ${endTime}) or endTime is null) order by clientName asc, startTime desc");		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startTime", DateUtil.format(start,"yyyy-MM-dd HH:mm:ss"));
		params.put("endTime", DateUtil.format(end,"yyyy-MM-dd HH:mm:ss"));
		try {
			String sql = SQLBuilder.buildNamedParameterSQL(sb.toString(), params);
			result = pss2SqlExecutor.doNativeQuery(sql, params, new ListConverter<ClientOfflineReportEntity>(new ColumnAsFeatureFactory<ClientOfflineReportEntity>(ClientOfflineReportEntity.class)));
			Date current = new Date();
			for(ClientOfflineReportEntity entity:result){
				buildModel(entity,generateDate,current);
			}
		}catch(Exception e){
			log.info(e);
			throw new EJBException(e);
		}
		return result;
	}

	
	private void buildModel(ClientOfflineReportEntity e,Date date,Date current){
		//if date > current date
		//offline is current - start time
		//else offlien is date - start time
		
		if(e.getStartTime()==null||date==null||current==null){
			log.info("Client Offline Report Entity construct error: start time, generate time or current date is null");
			return;
		}
		long compare = current.getTime()-date.getTime();
		Date endDate;
		Date endTime = e.getEndTime();
		if(endTime==null){
			if(compare>0){
				endDate = current;
			}else{
				endDate = date;
			}
		}else{
			endDate = endTime;
		}
		long diff = endDate.getTime()-e.getStartTime().getTime();
		long dayDiff = 1000*60*60*24;
		long hourDiff = 1000*60*60;
		long minutesDiff = 1000*60;
		long dayInterval = diff/dayDiff;
		long hourInterval = (diff%dayDiff)/hourDiff;
		long minInterval = ((diff%dayDiff)%hourDiff)/minutesDiff;
		
		String offline = dayInterval+" Day(s),"+hourInterval+" Hour(s),"+minInterval+" Minute(s)";
		e.setOffline(offline);
		e.setGenerateTime(date);
	}
}
