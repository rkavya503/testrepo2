/**
 * 
 */
package com.akuacom.pss2.drw.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.akuacom.pss2.drw.CFEventManager;
import com.akuacom.pss2.drw.DREvent2013Manager;
import com.akuacom.pss2.drw.DREventManager;
import com.akuacom.pss2.drw.constant.DRWConstants;
import com.akuacom.pss2.drw.core.EventDetail;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventLegend;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.WeatherValue;
import com.akuacom.pss2.util.LogUtils;
import com.akuacom.utils.drw.CacheNotificationMessage;

public class StatusCache implements MessageListener {

	private static Logger log = Logger.getLogger(StatusCache.class);
	
	private Map<CacheCategory, Date> statusMap=new HashMap<CacheCategory, Date>();

	private static StatusCache instance = new StatusCache();
	
	@SuppressWarnings("unchecked")
	@Override
	public  void  onMessage(Message message) {
		 if (message instanceof ObjectMessage) {
			 try {
				Object obj=((ObjectMessage) message).getObject();
				if (obj instanceof String) {
					if (((String)obj).contains("CBP_EVENT")) {
						Thread.currentThread().setContextClassLoader(StatusCache.class.getClassLoader());
						handleEventMessage((String)obj);
					}else if(((String)obj).contains("AUTO_DISPATCH_EVENT")){
						Thread.currentThread().setContextClassLoader(StatusCache.class.getClassLoader());
						handleAutoDispatchEventMessage((String)obj);
					}
					
					return;
				}
				if (obj instanceof HashSet<?>) {
					HashSet<CacheNotificationMessage> msgs=(HashSet<CacheNotificationMessage>)obj;
					for (CacheNotificationMessage msg:msgs)
		        		updateCache(msg);
					return;
				}
				
			} catch (JMSException e) {
			}
			
			final CacheNotificationMessage msg = getNotification(message);
			if(msg!=null){
				Thread.currentThread().setContextClassLoader(StatusCache.class.getClassLoader());
				updateCache(msg);
			}
		 }
	}
	
	protected CacheNotificationMessage getNotification(Message message) {
		 try {
			Object obj  = ((ObjectMessage) message).getObject();
			if(obj instanceof CacheNotificationMessage)
				return (CacheNotificationMessage) obj;
			else {
				 //it's may be loaded by different class loader
				 //serialize it
				 ByteArrayOutputStream bos = null;
				 ObjectOutput out =null;
				 byte[] bytes = null;
				 try{
					 bos = new ByteArrayOutputStream();
					 out = new ObjectOutputStream(bos);   
					 out.writeObject(obj);
					 bytes = bos.toByteArray();
				 }finally{
					 if(out!=null) try{out.close();}catch(Exception e){};
					 if(bos!=null) try{bos.close();}catch(Exception e){};
				 }
				 
				 //de-serialize it using current class loader 
				 if(bytes!=null){
					 ByteArrayInputStream bis = null;
					 ObjectInput in =null;
					 try{
					 bis= new ByteArrayInputStream(bytes);
					 in  = new ObjectInputStream(bis);
					 Object msg = in.readObject();
					 //then it's should be expect type
					 if(msg instanceof CacheNotificationMessage){
						 return (CacheNotificationMessage) msg;
					 }
					 }finally{
						 if(bis!=null) try{bis.close();}catch(Exception e){};
						 if(in!=null)  try{in.close();}catch(Exception e){};
					 }
				 }
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	protected void handleEventMessage(String eMsg){
		String[] msgs=eMsg.split(";");
		
		DREventManager manager=DRWUtil.getEvtManager();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			if (msgs[1].equalsIgnoreCase("CREATE")) {
				manager.createEvent(msgs[2], msgs[3], msgs[4], format.parse(msgs[5]), format.parse(msgs[6]), format.parse(msgs[7]), msgs[8].split(","));
				
				Date now=new Date();
				setCacheStatus(CacheCategory.SCHE_CBP_EVENT, now);
				setCacheStatus(CacheCategory.KIOSK_SCHEDULED,  now);

			} else if (msgs[1].equalsIgnoreCase("CANCEL")) {
				manager.cancelEvent(msgs[4], format.parse(msgs[6]),	Boolean.parseBoolean(msgs[5]));
				
				Date now=new Date();
				if (Boolean.parseBoolean(msgs[5])) {
					setCacheStatus(CacheCategory.ACT_CBP_EVENT, now);
					setCacheStatus(CacheCategory.KIOSK_ACTIVE, now);
				} else {
					setCacheStatus(CacheCategory.SCHE_CBP_EVENT, now);
					setCacheStatus(CacheCategory.KIOSK_SCHEDULED, now);
				}
			}
		} catch (Exception e) {
			log.error(LogUtils.createExceptionLogEntry(null, "failed to create CBP event in drwebsite: "+ e.getMessage(), e));
		}
	}
	
	
	private synchronized void  updateCache(CacheNotificationMessage message){
		Date now =new Date();
		if (message.isActive()) {
			setCacheStatus(CacheCategory.KIOSK_ACTIVE, now);
		}else{
			setCacheStatus(CacheCategory.KIOSK_SCHEDULED, now);
		}
		if (message.getProgramName().equals("SDP")){
			if (message.getProduct().equals("SDP")) {
				if (message.isActive()) {
					setCacheStatus(CacheCategory.ACT_SDPRESI_EVENT, now);
				}else{
					setCacheStatus(CacheCategory.SCHE_SDPRESI_EVENT, now);
				}
			} else {
				if (message.isActive()) {
					setCacheStatus(CacheCategory.ACT_SDPCOME_EVENT, now);
				}else{
					setCacheStatus(CacheCategory.SCHE_SDPCOME_EVENT, now);
				}
			}
		}
		
		if (message.getProgramName().equals("CPP")) {
				setCacheStatus(CacheCategory.SAI_RESIDENTIAL_ACTIVE, now);
				setCacheStatus(CacheCategory.SAI_COMMERCIAL_ACTIVE, now);
			if (!message.isActive()) {
				setCacheStatus(CacheCategory.SAI_COMMERCIAL_SCHEDULED, now);
				setCacheStatus(CacheCategory.SAI_RESIDENTIAL_SCHEDULED, now);
			}
		}
		
		if (message.getProgramName().equals("API")) {
			if (message.isActive()) {
				setCacheStatus(CacheCategory.ACT_API_EVENT, now);
			}else{
				setCacheStatus(CacheCategory.SCHE_API_EVENT, now);
			}
		}
		
		if (message.getProgramName().equals("BIP")||message.getProgramName().equals("BIP2013")) {
			if (message.isActive()) {
				setCacheStatus(CacheCategory.ACT_BIP_EVENT, now);
			}else{
				setCacheStatus(CacheCategory.SCHE_BIP_EVENT, now);
			}
		}
		
		if (message.getProgramName().equals("CBP")) {
			if (message.isActive()) {
				setCacheStatus(CacheCategory.ACT_CBP_EVENT, now);
			}else{
				setCacheStatus(CacheCategory.SCHE_CBP_EVENT, now);
			}
		}
		
		if (message.getProgramName().equals("DBP")) {
			setCacheStatus(CacheCategory.DBP_COMMERCIAL_ACTIVE, now);
			if (!message.isActive()) {
				setCacheStatus(CacheCategory.DBP_COMMERCIAL_SCHEDULED, now);
			}
		}

		if (message.getProgramName().equals("DRC")) {
			setCacheStatus(CacheCategory.DRC_COMMERCIAL_ACTIVE, now);
			if (!message.isActive()) {
				setCacheStatus(CacheCategory.DRC_COMMERCIAL_SCHEDULED, now);
			}
		}
		
		if (message.getProgramName().equals("SPD")) {
			if (message.isActive()) {
				setCacheStatus(CacheCategory.SPD_RESIDENTIAL_ACTIVE, now);
			} else {
				setCacheStatus(CacheCategory.SPD_RESIDENTIAL_SCHEDULED, now);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void updateEventCache(CacheCategory category){
		String programName=null;
		List<String> products = null;
		switch (category) {
			case ACT_SDPRESI_EVENT:
				programName="SDP"; products = new ArrayList<String>(); products.add("SDP");
				EventCache.getInstance().getActSDPResiEventLegends().setEventLegends(getActiveEventLegend(programName,products));
				break;
			case SCHE_SDPRESI_EVENT:
				programName="SDP"; products = new ArrayList<String>(); products.add("SDP");
				EventCache.getInstance().getScheSDPResiEventLegends().setEventLegends(getScheEventLegend(programName,products));
				break;
			case ACT_SDPCOME_EVENT:
				programName="SDP"; products = new ArrayList<String>(); products.add("APS"); products.add("APS-E");
				EventCache.getInstance().getActSDPComeEventLegends().setEventLegends(getActiveEventLegend(programName,products));
				break;
			case SCHE_SDPCOME_EVENT:
				programName="SDP"; products = new ArrayList<String>(); products.add("APS"); products.add("APS-E");
				EventCache.getInstance().getScheSDPComeEventLegends().setEventLegends(getScheEventLegend(programName,products));
				break;
			case ACT_API_EVENT:
				programName="API"; products = new ArrayList<String>(); products.add("API");
				EventCache.getInstance().getActAPIEventLegends().setEventLegends(getActiveEventLegend(programName,products));
				break;
			case SCHE_API_EVENT:
				programName="API"; products = new ArrayList<String>(); products.add("API");
				EventCache.getInstance().getScheAPIEventLegends().setEventLegends(getScheEventLegend(programName,products));
				break;
			case ACT_BIP_EVENT:
				programName="BIP"; products = new ArrayList<String>(); products.add("BIP");products.add("BIP2013");
				EventCache.getInstance().getActBIPEventLegends().setEventLegends(getActiveEventLegend(programName,products));
				break;
			case SCHE_BIP_EVENT:
				programName="BIP"; products = new ArrayList<String>(); products.add("BIP");products.add("BIP2013");
				EventCache.getInstance().getScheBIPEventLegends().setEventLegends(getScheEventLegend(programName,products));
				break;
			case ACT_CBP_EVENT:
				programName="CBP"; products = new ArrayList<String>(); 
				products.add(DRWConstants.PRODUCT_CBP_DO_14);
				products.add(DRWConstants.PRODUCT_CBP_DO_26);
				products.add(DRWConstants.PRODUCT_CBP_DO_48);
				products.add(DRWConstants.PRODUCT_CBP_DA_14);
				products.add(DRWConstants.PRODUCT_CBP_DA_26);
				products.add(DRWConstants.PRODUCT_CBP_DA_48);
				EventCache.getInstance().getActCBPEventLegends().setEventLegends(getActiveEventLegend(programName,products));
				break;
			case SCHE_CBP_EVENT:
				programName="CBP"; products = new ArrayList<String>(); 
				products.add(DRWConstants.PRODUCT_CBP_DO_14);
				products.add(DRWConstants.PRODUCT_CBP_DO_26);
				products.add(DRWConstants.PRODUCT_CBP_DO_48);
				products.add(DRWConstants.PRODUCT_CBP_DA_14);
				products.add(DRWConstants.PRODUCT_CBP_DA_26);
				products.add(DRWConstants.PRODUCT_CBP_DA_48);
				EventCache.getInstance().getScheCBPEventLegends().setEventLegends(getScheEventLegend(programName,products));
				break;
		//--------------------------------------------
			case SAI_RESIDENTIAL_ACTIVE:
				EventCache.getInstance().setActiveSAIResiEvents(getEventValue("CPP", true, false));
				break;
			case SAI_RESIDENTIAL_SCHEDULED:
				EventCache.getInstance().setScheduleSAIResiEvents(getEventValue("CPP", false, false));
				break;
			case SPD_RESIDENTIAL_ACTIVE:
				EventCache.getInstance().setActiveSPDResiEvents(getEventValue("SPD", true, false));
				break;
			case SPD_RESIDENTIAL_SCHEDULED:
				EventCache.getInstance().setScheduleSPDResiEvents(getEventValue("SPD", false, false));
				break;	
			case SAI_COMMERCIAL_ACTIVE:
				EventCache.getInstance().setActiveSAICommEvents(getEventValue("CPP", true, true));
				break;
			case SAI_COMMERCIAL_SCHEDULED:
				EventCache.getInstance().setScheduleSAICommEvents(getEventValue("CPP", false, true));
				break;
			case DBP_COMMERCIAL_ACTIVE:
				EventCache.getInstance().setActiveDBPCommEvents(getEventValue("DBP", true, true));
				break;
			case DBP_COMMERCIAL_SCHEDULED:
				EventCache.getInstance().setScheduleDBPCommEvents(getEventValue("DBP", false, true));
				break;
			case DRC_COMMERCIAL_ACTIVE:
				EventCache.getInstance().setActiveDRCCommEvents(getEventValue("DRC", true, true));
				break;
			case DRC_COMMERCIAL_SCHEDULED:
				EventCache.getInstance().setScheduleDRCCommEvents(getEventValue("DRC", false, true));
				break;
			case RTP_FORCAST:
				EventCache.getInstance().setForcastRTPEvents(getRTPForcast());
				break;
			case KIOSK_ACTIVE:
				EventCache.getInstance().setKioskActiveEvents(toKioskDataMode(DRWUtil.getCFEventManager().getKioskEvent(true)));
				break;
			case KIOSK_SCHEDULED:
				EventCache.getInstance().setKioskScheduledEvents(toKioskDataMode(DRWUtil.getCFEventManager().getKioskEvent(false)));
				break;
			case LOCATION:
				EventCache.getInstance().setAllBlocks(getAllBlocks());
				EventCache.getInstance().setLocationCache(getLocationCache());
				EventCache.getInstance().setSlapBlockMap(EventCache.getInstance().retrieveSlapBlockMap());
				EventCache.getInstance().setBlockKMLs(EventCache.getInstance().retrieveBlockKMLMap());
				break;	
		}
	}
	
	private List<RTPEventDataModel> getRTPForcast() {
		List<RTPEventDataModel> result=new ArrayList<RTPEventDataModel>();
		try {
			result = toRTPDataMode(DRWUtil.getCFEventManager().getForcast(new Date()));
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		return result;
	}

	private List<BaseEventDataModel> getEventValue(String programName, boolean active, boolean commercial) {
		List<String> programs=new ArrayList<String>();
		programs.add(programName);
		DREvent2013Manager manager=DRWUtil.getDREvent2013Manager();
		
		List<BaseEventDataModel> models=new ArrayList<BaseEventDataModel>();
		if (active){
			models=toDataMode(manager.getEvents(programs, commercial,true));
		}
		else{
			models=toDataMode(manager.getEvents(programs, commercial,false));
		}
			
		return models;
	}
	
	private List<BaseEventDataModel> toDataMode(List<EventValue> events) {
		List<BaseEventDataModel> models=new ArrayList<BaseEventDataModel>();
		for (EventValue event:events) {
			BaseEventDataModel model=new BaseEventDataModel(event);
			models.add(model);
		}
		return models;
	}
	
	private List<RTPEventDataModel> toRTPDataMode(List<WeatherValue> weathers) {
		List<RTPEventDataModel> models=new ArrayList<RTPEventDataModel>();
		for (WeatherValue weather:weathers) {
			RTPEventDataModel model=new RTPEventDataModel(weather);
			models.add(model);
		}
		return models;
	}
	
	private List<KioskEventDataModel> toKioskDataMode(List<EventValue> events) {
		List<KioskEventDataModel> models=new ArrayList<KioskEventDataModel>();
		for (EventValue event:events) {
			KioskEventDataModel model=new KioskEventDataModel(event);
			models.add(model);
		}
		return models;
	}
	
	private Topic pss2Topic;
	
	private TopicConnection connection = null;
	private TopicSession session = null;
	
	private TopicConnectionFactory  topicConnectionFactory;
	
	private StatusCache() {
		try {
			Properties env = new Properties();
			env.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
			env.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			
			env.setProperty("java.naming.provider.url", "jnp://localhost:1099");
			
			InitialContext iniCtx = new InitialContext(env);
			//TODO ?
		    Object tmp = iniCtx.lookup("ConnectionFactory");
			
		    topicConnectionFactory = (TopicConnectionFactory) tmp; 
		    connection =topicConnectionFactory.createTopicConnection();
			session = connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
				
			pss2Topic = (Topic) iniCtx.lookup("topic/pss2");
			
			MessageConsumer subscriber = session.createSubscriber(pss2Topic);
			subscriber.setMessageListener(this);
			connection.start();
		
			//initialize status cache and event data cache
			initCacheValue();
		} catch (Exception e) {
			log.error("Fail to subscribe topic toipc/pss2- "+ e.getMessage(),e);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if(session!=null) try{ session.close();}catch(Exception e){};
		if(connection!=null) try{ connection.close();}catch(Exception e){};
	}
	private List<EventLegend> getActiveEventLegend(String programName, List<String> products) {
		DREvent2013Manager manager=DRWUtil.getDREvent2013Manager();
		
		List<EventLegend> models=new ArrayList<EventLegend>();
		
		models=manager.getActiveEvents(programName, products);
	
		return models;
	}
	
	private List<EventLegend> getScheEventLegend(String programName, List<String> products) {
		DREvent2013Manager manager=DRWUtil.getDREvent2013Manager();
		
		List<EventLegend> models=new ArrayList<EventLegend>();
		
		models=manager.getScheduleEvents(programName, products);
	
		return models;
	}

	private void initCacheValue(){
		Date now=new Date();
		setCacheStatus(CacheCategory.ACT_SDPRESI_EVENT, now);
		setCacheStatus(CacheCategory.ACT_SDPCOME_EVENT, now);
		setCacheStatus(CacheCategory.ACT_API_EVENT, now);
		setCacheStatus(CacheCategory.ACT_BIP_EVENT, now);
		setCacheStatus(CacheCategory.ACT_CBP_EVENT, now);
		
		setCacheStatus(CacheCategory.SCHE_SDPRESI_EVENT, now);
		setCacheStatus(CacheCategory.SCHE_SDPCOME_EVENT, now);
		setCacheStatus(CacheCategory.SCHE_API_EVENT, now);
		setCacheStatus(CacheCategory.SCHE_BIP_EVENT, now);
		setCacheStatus(CacheCategory.SCHE_CBP_EVENT, now);
		//---------------------------------
		setCacheStatus(CacheCategory.DBP_COMMERCIAL_ACTIVE, now);
		setCacheStatus(CacheCategory.DBP_COMMERCIAL_SCHEDULED, now);
		setCacheStatus(CacheCategory.DRC_COMMERCIAL_ACTIVE, now);
		setCacheStatus(CacheCategory.DRC_COMMERCIAL_SCHEDULED, now);
		setCacheStatus(CacheCategory.KIOSK_ACTIVE, now);
		setCacheStatus(CacheCategory.KIOSK_SCHEDULED, now);
		setCacheStatus(CacheCategory.RTP_FORCAST, now);
		setCacheStatus(CacheCategory.SAI_COMMERCIAL_ACTIVE, now);
		setCacheStatus(CacheCategory.SAI_COMMERCIAL_SCHEDULED, now);
		setCacheStatus(CacheCategory.SAI_RESIDENTIAL_ACTIVE, now);
		setCacheStatus(CacheCategory.SAI_RESIDENTIAL_SCHEDULED, now);
		setCacheStatus(CacheCategory.SPD_RESIDENTIAL_ACTIVE, now);
		setCacheStatus(CacheCategory.SPD_RESIDENTIAL_SCHEDULED, now);
	}
	
	public static StatusCache getInstance(){
		return instance;
	}

	public void setCacheStatus(List<CacheCategory> catogaryList, Date date) {
		for (CacheCategory catogary:catogaryList) {
			updateEventCache(catogary);
			statusMap.put(catogary, date);
		}
	}
	
	public void setCacheStatus(CacheCategory catogary, Date date) {
		updateEventCache(catogary);
		statusMap.put(catogary, date);
	}
	
	public Date getCacheStatus(CacheCategory catogary) {
		return statusMap.get(catogary);
	}

	public Map<CacheCategory, Date> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<CacheCategory, Date> statusMap) {
		this.statusMap = statusMap;
	}
	public LocationCache getLocationCache() {
		CFEventManager manager=DRWUtil.getCFEventManager();
		List<Location> locations = manager.getLocationEntity();
		LocationCache locationCache = EventCache.getInstance().getLocationCache();
		if(locationCache==null){
			locationCache = new LocationCache();
		}
		locationCache.buildLocationCache(locations);
		return locationCache;
	}
	public List<String> getAllBlocks(){
		CFEventManager manager=DRWUtil.getCFEventManager();
		return manager.getAllBlock();
	}
	
	
	protected void handleAutoDispatchEventMessage(String eMsg){
		String[] msgs=eMsg.split(";");
		
		DREventManager manager=DRWUtil.getEvtManager();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			String actionType = msgs[1];
			String program= msgs[2];
			String dispatchType= msgs[3];
			String location= msgs[4];
			String product= msgs[5];
			String delayInterval = msgs[6];
			int delay = Integer.valueOf(delayInterval);
			String issueTime= msgs[7];
			String startTime= msgs[8];
			
			Date issue=null;
			Date start=null;
			Date end=null;
			if(issueTime!=null&&(!"".equalsIgnoreCase(issueTime))){
				issue = format.parse(issueTime);
			}
			if(startTime!=null&&(!"".equalsIgnoreCase(startTime))){
				start = format.parse(startTime);
			}
			if(msgs.length>9){
				String endTime= msgs[9];
				if(endTime!=null&&(!"".equalsIgnoreCase(endTime))){
					end = format.parse(endTime);
				}
			}
			if(DRWConstants.PROGRAM_API.equalsIgnoreCase(program)){
				program = "API";
				product = "API";
			}else if(DRWConstants.PROGRAM_BIP_TOU.equalsIgnoreCase(program)||DRWConstants.PROGRAM_BIP.equalsIgnoreCase(program)){
				program = "BIP";
				product = "BIP2013";
			}else if(DRWConstants.PROGRAM_SDP.equalsIgnoreCase(program)){
				
			}
			if(DRWConstants.ACTION_ACTIVATED.equalsIgnoreCase(actionType)){
				if("BIP".equalsIgnoreCase(program)){
					manager.createEvent(program,product,dispatchType,location,null,issue,start,end,false);
				}else{
					manager.createEvent(program,product,dispatchType,location,null,issue,start,end);
				}
			}else if(DRWConstants.ACTION_CONTINUED.equalsIgnoreCase(actionType)){
				List<EventDetail> list = manager.getAutoDispatchEvents(program, product, start, dispatchType, location, false);
				if(list.size()>0){
					List<String> result = new ArrayList<String>();
					for(EventDetail instance:list){
						Date actualEndTime = instance.getActualEndTime();
						if(actualEndTime==null){
							result.add(instance.getUUID());
						}else{
							StringBuffer sb = new StringBuffer();
							sb.append("Event Auto Dispatch CONTINUED action can not update the event which has actual end time:["+actualEndTime+"] in program["+program+"],");
							sb.append("Start time["+startTime+"].");
							log.info(sb.toString());
						}
					}
					manager.updateEndTime(result, end, false);
				}else{
					
					manager.createEvent(program,product,dispatchType,location,null,issue,start,end);
					
					log.info("Event Auto Dispatch CONTINUED action didn't match any events in the system, it will create a new event.");
				}
			}else if(DRWConstants.ACTION_TERMINATED.equalsIgnoreCase(actionType)){
				
				List<EventDetail> list = manager.getAutoDispatchEvents(program, product, start, dispatchType, location, false);
				if(list.size()>0 && (end!=null)){
					List<String> result = new ArrayList<String>();
					for(EventDetail instance:list){
						result.add(instance.getUUID());
					}
					if(DRWConstants.PROGRAM_API.equalsIgnoreCase(program)||DRWConstants.PROGRAM_SDP.equalsIgnoreCase(program)){
						Calendar instance = Calendar.getInstance();
						instance.setTime(end);
						//instance.add(Calendar.MINUTE, 7);
						instance.add(Calendar.MINUTE,delay);
						end = instance.getTime();
					}
					manager.updateEndTime(result, end, true);
				}else{
					log.info("Event Auto Dispatch TERMINATED action didn't match any events in the system.");
				}
			}else if(DRWConstants.ACTION_SCHEDULED.equalsIgnoreCase(actionType)){
				//as same as activated action
				
				manager.createEvent(program,product,dispatchType,location,null,issue,start,end);
				
			
			}else if(DRWConstants.ACTION_DELETE.equalsIgnoreCase(actionType)){
				List<EventDetail> list = manager.getAutoDispatchEvents(program, product, start, dispatchType, location, false);
				if(list.size()>0){
					List<String> eventDetailUuid = new ArrayList<String>();
					for(EventDetail ed:list){
						eventDetailUuid.add(ed.getUUID());
					}
					manager.removeEvent(eventDetailUuid);
				}
			}
			updateAutoDispatchCache(actionType,program);
		} catch (Exception e) {
			log.error(LogUtils.createExceptionLogEntry(null, "failed to create CBP event in drwebsite: "+ e.getMessage(), e));
		}
	}
	
	private void updateAutoDispatchCache(String actionType,String program){
		if(DRWConstants.ACTION_ACTIVATED.equalsIgnoreCase(actionType)){
			//update cache
			if(DRWConstants.PROGRAM_API.equalsIgnoreCase(program)){
				
			}else if(DRWConstants.PROGRAM_BIP_TOU.equalsIgnoreCase(program)){
				
			}else if(DRWConstants.PROGRAM_SDP.equalsIgnoreCase(program)){
				
			}
		}else if(DRWConstants.ACTION_CONTINUED.equalsIgnoreCase(actionType)){
			//update cache	
			if(DRWConstants.PROGRAM_API.equalsIgnoreCase(program)){
				
			}else if(DRWConstants.PROGRAM_BIP_TOU.equalsIgnoreCase(program)){
				
			}else if(DRWConstants.PROGRAM_SDP.equalsIgnoreCase(program)){
				
			}
		}else if(DRWConstants.ACTION_TERMINATED.equalsIgnoreCase(actionType)){
			//update cache
			if(DRWConstants.PROGRAM_API.equalsIgnoreCase(program)){
				
			}else if(DRWConstants.PROGRAM_BIP_TOU.equalsIgnoreCase(program)){
				
			}else if(DRWConstants.PROGRAM_SDP.equalsIgnoreCase(program)){
				
			}
		}else if(DRWConstants.ACTION_SCHEDULED.equalsIgnoreCase(actionType)){
			//update cache
			if(DRWConstants.PROGRAM_API.equalsIgnoreCase(program)){
				
			}else if(DRWConstants.PROGRAM_BIP_TOU.equalsIgnoreCase(program)){
				
			}else if(DRWConstants.PROGRAM_SDP.equalsIgnoreCase(program)){
				
			}
		}
	}
}
