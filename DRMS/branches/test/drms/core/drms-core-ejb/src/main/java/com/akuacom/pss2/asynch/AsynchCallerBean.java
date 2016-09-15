package com.akuacom.pss2.asynch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.akuacom.jdbc.CellConverter;
import com.akuacom.jdbc.ColumnAsFeatureFactory;
import com.akuacom.jdbc.ListConverter;
import com.akuacom.jdbc.SQLBuilder;
import com.akuacom.jdbc.SQLWord;
import com.akuacom.pss2.Pss2SQLExecutor;
import com.akuacom.pss2.timer.TimerManagerBean;

@Stateless
public class AsynchCallerBean extends TimerManagerBean implements 
				AsynchCaller.L, AsynchCaller.R {

	private static Logger log = Logger.getLogger(AsynchCallerBean.class);
	
	@EJB
	private Pss2SQLExecutor.L sqlExecutor;
	
	private static final int TIMER_INTERVAL = 10000; // 5 sec
	
	@Resource(mappedName = "queue/asynchCallQueue")
	private Queue asynchCallQueue;
	
	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory queueConnectionFactory;
	
	@Resource
	private TimerService timerService;
	
	private static final int  TASK_CONCURRENT = 2;
	
	private static final String TIMER= AsynchCaller.class.getName();
	
	@Override
	public void call(AsynchRunable runnable)  {
		if(runnable instanceof HoldingRunnable){
			//put it in the task queue and hold it for a while to see if
			//it can be merged with tasks coming soon, and therefore to reduce the total 
			//amount of tasks to be executed to avoid big impact on performance
			//A timer will check all the task in the queue and then re-schedule the tasks
			//this operation is very quick and will not block other operation
			merge(runnable);
		}else{
			runImmediately(runnable);
		}
	}
	
	public void merge(AsynchRunable runnable){
		if(runnable instanceof HoldingRunnable){
			
			BasetHoldingRunnable asynTask = (BasetHoldingRunnable)runnable;
			String sqlTempalte=asynTask.getMergeSQLTempleate();
			Map<String,Object> params =asynTask.getMergeSQLParam();
			try {
				String sql = SQLBuilder.buildNamedParameterSQL(sqlTempalte, params);
				sqlExecutor.execute(sql,params);
			} catch (Exception e) {
				log.error(e);
			}
		}
	}
	
	protected void runImmediately(AsynchRunable runnable){
		//run it immediately but not block other operation
		//the task will be triggered by jms channel 
		Connection connection = null;
		Session session = null;
		try {
			connection = queueConnectionFactory.createConnection();
			session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(asynchCallQueue);
			messageProducer.send(session.createObjectMessage(runnable));
		} catch (JMSException e) {
			log.error(e.getMessage(),e);
			throw new EJBException(e);
		}finally{
			if (session != null) {
				try {session.close();} catch (JMSException e) {
					log.warn("Cannot close session", e);
				}
			}
			if (connection != null) {
				try {	connection.close();		} catch (JMSException e) {
					log.warn("Cannot close connection", e);
				}
			}
		}
	}
	
	@Timeout
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void timeoutHandler(Timer timer) {
		try{
			processQueue();
		}catch(Exception e){
			log.error(e);
		}
	}
	
	protected void processQueue() throws Exception{
		String sqlTempalte=GET_TO_DO_TASK_COUNT;
		Map<String,Object> params = Collections.emptyMap();
		
		String sql = SQLBuilder.buildNamedParameterSQL(sqlTempalte, params);
		int count =sqlExecutor.doNativeQuery(sql,params,CellConverter.make(Integer.class));
		if(count<=0)
			return ;
		
		int taskToSchedule = TASK_CONCURRENT;
		if(count>TASK_CONCURRENT*5){
			taskToSchedule *= 2;
		}
		if(count>TASK_CONCURRENT*10){
			taskToSchedule *= 2;
		}
		if(count>TASK_CONCURRENT*100){
			taskToSchedule *= 5;
		}
		if(count>TASK_CONCURRENT*1000){
			taskToSchedule *=5;
		}
		if(count> TASK_CONCURRENT *2000){
			log.warn("WARNING!!!, TOO MANY PENDDING TASKS" +count);
		}
		
		sqlTempalte=GET_TO_DO_TASKS;
		params = getPendingTasksSQLParam(taskToSchedule);
		sql = SQLBuilder.buildNamedParameterSQL(sqlTempalte, params);
		
		Map<String,String> mappings = new HashMap<String,String>();
		mappings.put("runnable", "serializedRunnable");
		List<BasetHoldingRunnable> tasks = sqlExecutor.doNativeQuery(sql,params,
			ListConverter.make(ColumnAsFeatureFactory.make(BasetHoldingRunnable.class,mappings,"id")));
		
		List<String> ids = new ArrayList<String>(taskToSchedule);
		
		for(BasetHoldingRunnable task: tasks){
			if(task.getRunnable()!=null)
				runImmediately(task.getRunnable());
			ids.add(task.getId());
		}
		if(tasks.size()>0){
			log.debug("Aycn Task pendding "+(count-tasks.size())+", running " +tasks.size());
		}
		removeRunningTasks(ids);
	}

	protected void updateStatus(List<String> ids,String status) throws Exception{
		String sqlTempalte=UPDATE_STATUS;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status",status);
		params.put("ids", ids);
		String sql = SQLBuilder.buildNamedParameterSQL(sqlTempalte, params);
		sqlExecutor.execute(sql,params);
	}
	
	protected void removeRunningTasks(List<String> ids) throws Exception{
		String sqlTempalte=REMOVE_TASKS;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ids", ids);
		String sql = SQLBuilder.buildNamedParameterSQL(sqlTempalte, params);
		sqlExecutor.execute(sql,params);
	}
	
	@Override
	public String getTimersInfo() {
		Collection<?> timersList = timerService.getTimers();
		return super.getTimersInfo(timersList);
	}

	@Override
	public void createTimers() {
		cancelTimers();
		timerService.createTimer(new Date(), 
					TIMER_INTERVAL
					,TIMER);
		log.debug(TIMER +" created successfully");
	}
	
	@Override
	public void cancelTimers() {
		 Collection<?> timersList = timerService.getTimers();
	     super.cancelTimers(timersList);
	}

	
	protected Map<String,Object> getPendingTasksSQLParam(int taskTodo){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("range", new SQLWord("limit 0,"+taskTodo));
		return params;
	}
	
	public static final String GET_TO_DO_TASK_COUNT= "SELECT COUNT(*) FROM asyn_task "
		+ "WHERE  (TIMESTAMPDIFF(MICROSECOND,lastUpdate,NOW())/1000>minHold OR  TIMESTAMPDIFF(MICROSECOND,creationTime,NOW())/1000>maxHold) AND status='PENDDING' ";

		
	public static final String GET_TO_DO_TASKS= "SELECT * FROM asyn_task "
		+ " WHERE  (TIMESTAMPDIFF(MICROSECOND,lastUpdate,NOW())/1000>minHold OR TIMESTAMPDIFF(MICROSECOND,creationTime,NOW())/1000>maxHold) AND STATUS='PENDDING'"
		+ " ORDER BY priority DESC, lastUpdate ASC ${range}";
	
	
	public static final String UPDATE_STATUS= "update asyn_task set status =${status}"
		+ " WHERE  id in ${ids}";
	
	public static final String REMOVE_TASKS= "delete from asyn_task "
		+ " WHERE  id in ${ids}";
}
