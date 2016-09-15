package com.akuacom.pss2.drw.cache;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.akuacom.pss2.drw.DREvent2013Manager;
import com.akuacom.pss2.drw.Event;
import com.akuacom.pss2.drw.entry.Category;
import com.akuacom.pss2.drw.entry.CategotyList;
import com.akuacom.pss2.drw.entry.LoadFileUtil;
import com.akuacom.pss2.drw.util.ActiveEventPredicateFilter;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.util.EventPredicateFilter;
import com.akuacom.pss2.drw.util.PredicateFilter;
import com.akuacom.pss2.drw.util.ScheduleIREventPredicateFilter;
import com.akuacom.pss2.drw.value.EventVo;
import com.akuacom.utils.drw.CacheNotificationMessage;


public class DrEventCache implements MessageListener {
	
	private static Logger log = Logger.getLogger(DrEventCache.class);
	private EventStatusCache status = null;
	private List<Category> categoryList;
	private Date updateTime;
	private static DrEventCache instance = new DrEventCache();
	public static DrEventCache getInstance(){
		return instance;
	}
	
	public EventStatusCache getStatus() {
		return status;
	}

	private DrEventCache(){
		loadConfigurationData();
		initSubscriber();
		initLibraryCache();
		this.setUpdateTime(new Date());
	}
	
	private List<Event> list= new ArrayList<Event>();

	public List<Event> getList() {
		return list;
	}

	public void setList(List<Event> list) {
		this.list = list;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public List<Event> getEvent(PredicateFilter filter){
		List<Event> result = filter.select(list);
		return result;
	}
	
	private Topic pss2Topic;
	private TopicConnection connection = null;
	private TopicSession session = null;
	private TopicConnectionFactory topicConnectionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public  void  onMessage(Message message) {
		try {
			//Delay the message delivery:
			//The messaging system will hold the message for a specified interval before making it available to consumers.
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		 if (message instanceof ObjectMessage) {
			 
			 try {
				Object obj=((ObjectMessage) message).getObject();
				if (obj instanceof HashSet<?>) {
					String uUid = null;
					Thread.currentThread().setContextClassLoader(DrEventCache.class.getClassLoader());
					HashSet<CacheNotificationMessage> msgs=(HashSet<CacheNotificationMessage>)obj;
					LinkedHashSet<CacheNotificationMessage> msgsL =new LinkedHashSet<CacheNotificationMessage>(msgs);
					//(new HashSet<Event>(DrEventCache.getInstance().getList()))
					for (Object object:msgsL){
						CacheNotificationMessage msg = unmarshalMesssage(object);
						if(uUid == null || !uUid.equals(msg.getuUID())){
							uUid = msg.getuUID();
							updateStatusCache(msg);
							LocationCache.getInstance().updateLocCache(msg);
							DrCache.getInstance().updateRTPCache(msg);
						}
					}
					return;
				}else if (obj instanceof String){
					Thread.currentThread().setContextClassLoader(DrEventCache.class.getClassLoader());
					handleAutoDispatchEventMessage((String)obj);
					return;
				}
				
			} catch (JMSException e) {
			}catch (RuntimeException rte) {
				log.error(" Redeliver Message in DrEventCache " +rte.getMessage());
				try{
					session.commit();
				}catch (JMSException e) {
				}
			}
			
			final CacheNotificationMessage msgs = getNotification(message);
			if(msgs!=null){
				Thread.currentThread().setContextClassLoader(DrEventCache.class.getClassLoader());
				updateCache(msgs);
				LocationCache.getInstance().updateLocCache(msgs);
				DrCache.getInstance().updateRTPCache(msgs);
			}
		 }
	}
	
	
	public static CacheNotificationMessage unmarshalMesssage(Object obj) {
		 try {
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
	
	public synchronized void updateStatusCache(CacheNotificationMessage message){
		Date now =new Date();	
		List<Category> categories = getCategory(message.getProgramName(), Arrays.asList(message.getProduct()), message.isActive());
		for(Category category : categories){
			getStatus().setCacheStatus(EventCategory.valueOf(category.getName()), now);
			updateEventCache(EventCategory.valueOf(category.getName()));//comment opened by me
		}
		
	}
	
	
	public synchronized void updateCache(CacheNotificationMessage message){
		Date now =new Date();	
		List<Category> categories = getCategory(message.getProgramName(), Arrays.asList(message.getProduct()), message.isActive());
		for(Category category : categories){
			getStatus().setCacheStatus(EventCategory.valueOf(category.getName()), now);
			updateEventCache(EventCategory.valueOf(category.getName()));
		}
		
	}


	public synchronized void updateRemCache(CacheNotificationMessage message){
		Date now =new Date();	
		List<Category> categories = getCategory(message.getProgramName(), Arrays.asList(message.getProduct()), message.isActive());
		for(Category category : categories){
			getStatus().setCacheStatus(EventCategory.valueOf(category.getName()), now);
			//updateRemEventCache(EventCategory.valueOf(category.getName())); // Commented as the the fetching the events details will happen on message from JMS  
		}
		
	}

	public synchronized void updateActCache(CacheNotificationMessage message){
		Date now =new Date();	
		List<Category> categories = getCategory(message.getProgramName(), Arrays.asList(message.getProduct()), message.isActive());
		for(Category category : categories){
			getStatus().setCacheStatus(EventCategory.valueOf(category.getName()), now);
			//updateActEventCache(EventCategory.valueOf(category.getName())); // Commented as the the fetching the events details will happen on message from JMS
		}
		
	}

	public synchronized void updateSCHDCache(CacheNotificationMessage message){
		Date now =new Date();	
		List<Category> categories = getCategory(message.getProgramName(), Arrays.asList(message.getProduct()), message.isActive());
		for(Category category : categories){
			getStatus().setCacheStatus(EventCategory.valueOf(category.getName()), now);
			//updateSCHDEventCache(EventCategory.valueOf(category.getName())); // Commented as the the fetching the events details will happen on message from JMS
		}
		
	}
	
	public synchronized void updateDelCache(CacheNotificationMessage message){
		Date now =new Date();	
		List<Category> categories = getCategory(message.getProgramName(), Arrays.asList(message.getProduct()), message.isActive());
		for(Category category : categories){
			getStatus().setCacheStatus(EventCategory.valueOf(category.getName()), now);
			//updateDelEventCache(EventCategory.valueOf(category.getName()));
		}
		
	}
	
	private void updateEventCache(EventCategory category){
		Category cate = this.getCategory(category.toString());
		String programName=cate.getProgramName(); //program name display in IR
		String program=cate.getProgram(); // program name used in DR
		List<String> products = cate.getProducts().getProductsList();
		
		List<Event> eventsAll = this.getList();
		List<Event> result = null;
		PredicateFilter predicate = null; 
		
		predicate = new EventPredicateFilter(Arrays.asList(programName), products);
		if(cate.isActive()){
			predicate.add(new ActiveEventPredicateFilter());
		}else{
			predicate.add(new ScheduleIREventPredicateFilter());//FIXME: should be in-active events  
		}
		
		result = predicate.select(eventsAll);
		this.getList().removeAll(result);
	
		result = null;
		if("DR".equalsIgnoreCase(cate.getEventType())){
			result = getDrEvent(program, cate.isActive());
		}else{
			if(cate.isActive()){
				result = getActiveIrEvent(programName, products);
			}else{
				result = getScheIrEvent(programName, products);
			}
			
		}
		this.getList().addAll(result);
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if(session!=null) try{ session.close();}catch(Exception e){};
		if(connection!=null) try{ connection.close();}catch(Exception e){};
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
	
	private List<Event> getDrEvent(String programName, boolean active) {
		List<String> programs=new ArrayList<String>();
		programs.add(programName);
		DREvent2013Manager manager=DRWUtil.getDREvent2013Manager();
		
		List<EventVo> events=new ArrayList<EventVo>();
		events = manager.getDREvents4Mobile(programs, active);
		
		List<Event> models=new ArrayList<Event>();
		for (EventVo event:events) {
			Event model=new Event();
			model.setUuid(event.getUuid());
			model.setProgram(event.getProgram());
			model.setProduct(event.getProduct());
			model.setStartDateTime(event.getStartTime());
			model.setEndDateTime(event.getEndTime());
			model.setIssueTime(event.getIssueTime());
			model.setActive(event.isActive());
			model.setIr(false);
			models.add(model);
		}
		return models;
	}
	
	private List<Event> getActiveIrEvent(String programName, List<String> products) {
		DREvent2013Manager manager=DRWUtil.getDREvent2013Manager();
		List<EventVo> events=new ArrayList<EventVo>();
		events=manager.getActiveEventDetails(programName, products);
	
		List<Event> models=new ArrayList<Event>();
		for (EventVo event:events) {
			Event model=new Event();
			model.setUuid(event.getUuid());
			model.setParentUuid(event.getParentUuid());
			model.setProgram(event.getProgram());
			model.setProduct(event.getProduct());
			model.setStartDateTime(event.getStartTime());
			model.setEndDateTime(event.getEndTime());
			model.setIssueTime(event.getIssueTime());
			model.setActive(true);
			model.setIr(true);
			model.setEstimated(event.isEstimated());
			model.setLocations(event.getLocations());
			if(event.getLocationId()==null||"SLAP".equalsIgnoreCase(event.getLocationId())){
				model.setLocationBlock("All Blocks");
				model.setMinBlock(0);
				model.setMaxBlock(Integer.MAX_VALUE);
			}else if(event.getLocationType().equalsIgnoreCase("SLAP")){
				model.setMinBlock(LocationCache.getInstance().getSlapBlockRange().get(event.getLocationNumber()).getMinValue());
				model.setMaxBlock(LocationCache.getInstance().getSlapBlockRange().get(event.getLocationNumber()).getMaxValue());
			}else if(event.getLocationType().equalsIgnoreCase("ABank")){
				model.setLocationBlock(event.getLocationBlock());
				model.setMinBlock(Integer.valueOf(event.getLocationBlock()));
				model.setMaxBlock(Integer.valueOf(event.getLocationBlock()));
			}else{
				model.setLocationBlock("-1");
				model.setMinBlock(-1);
				model.setMaxBlock(-1);
			}
			
			models.add(model);
		}
		return models;
	}
	
	private List<Event> getScheIrEvent(String programName, List<String> products) {
		DREvent2013Manager manager=DRWUtil.getDREvent2013Manager();
		List<EventVo> events=new ArrayList<EventVo>();
		events=manager.getScheduleEventDetails(programName, products);
	
		List<Event> models=new ArrayList<Event>();
		for (EventVo event:events) {
			Event model=new Event();
			model.setParentUuid(event.getParentUuid());
			model.setUuid(event.getUuid());
			model.setProgram(event.getProgram());
			model.setProduct(event.getProduct());
			model.setStartDateTime(event.getStartTime());
			model.setEndDateTime(event.getEndTime());
			model.setActive(false);
			model.setIr(true);
			model.setIssueTime(event.getIssueTime());
			model.setEstimated(event.isEstimated());
			model.setLocations(event.getLocations());
			if(event.getLocationId()==null||"SLAP".equalsIgnoreCase(event.getLocationId())){//FIXME:Frank
				model.setLocationBlock("All Blocks");
				model.setMinBlock(0);
				model.setMaxBlock(Integer.MAX_VALUE);
			}else if(event.getLocationType().equalsIgnoreCase("SLAP")){
				model.setMinBlock(LocationCache.getInstance().getSlapBlockRange().get(event.getLocationNumber()).getMinValue());
				model.setMaxBlock(LocationCache.getInstance().getSlapBlockRange().get(event.getLocationNumber()).getMaxValue());
			}else if(event.getLocationType().equalsIgnoreCase("ABank")){
				model.setLocationBlock(event.getLocationBlock());
				model.setMinBlock(Integer.valueOf(event.getLocationBlock()));
				model.setMaxBlock(Integer.valueOf(event.getLocationBlock()));
			}else{
				model.setLocationBlock("-1");
				model.setMinBlock(-1);
				model.setMaxBlock(-1);
			}
			
			models.add(model);
		}
		
		return models;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}
	
	public Category getCategory(String name){
		if(categoryList == null || categoryList.isEmpty()) return null;
		for(Category category : categoryList){
			if(name.equalsIgnoreCase(category.getName())){
				return category;
			}
		}
		return null;
	}
	
	public List<Category> getCategory(String program, List<String> products, boolean active){
		if(categoryList == null || categoryList.isEmpty()) return null;
		List<Category> temp = new ArrayList<Category>();
		for(Category category : categoryList){
			if(!program.equalsIgnoreCase(category.getProgramName())&&!program.equalsIgnoreCase(category.getProgram())){
				continue;
			}
			if(active!=category.isActive()){
				continue;
			}
			if(category.getProducts().getProductsList()==null||category.getProducts().getProductsList().containsAll(products)){
				temp.add(category);
			}
		}
		return temp;
	}

	
	
	protected void handleAutoDispatchEventMessage(String eMsg){
		String[] msgs=eMsg.split(";");
		try {
			String program= msgs[2];
			String product= msgs[5];

			if("API".equalsIgnoreCase(program)){
				program = "API";
				product = "API";
			}else if("TOU-BIP".equalsIgnoreCase(program)||"BIP".equalsIgnoreCase(program)){
				program = "BIP";
				product = "BIP2013";
			}else if("SDP".equalsIgnoreCase(program)){
				
			}

			Date now =new Date();	
			List<Category> categories = getCategory(program, Arrays.asList(product), false);
			for(Category category : categories){
				getStatus().setCacheStatus(EventCategory.valueOf(category.getName()), now);
			}
			
		} catch (Exception e) {
			log.error("failed to create event in mobile dr website: "+ e.getMessage(), e);
		}
	}
	
	
	
	private void initSubscriber() {
		try {
			Properties env = new Properties();
			env.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
			env.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			env.setProperty("java.naming.provider.url", "jnp://localhost:1099");
			
			InitialContext iniCtx = new InitialContext(env);
		    Object tmp = iniCtx.lookup("ConnectionFactory");
			
		    topicConnectionFactory = (TopicConnectionFactory) tmp; 
		    connection =topicConnectionFactory.createTopicConnection();
			session = connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
				
			pss2Topic = (Topic) iniCtx.lookup("topic/pss2");
			
			MessageConsumer subscriber = session.createSubscriber(pss2Topic);
			subscriber.setMessageListener(this);
			
			connection.start();
		} catch (Exception e) {
			log.error("Fail to subscribe topic toipc/pss2- "+ e.getMessage(),e);
		}
	}
	
	private void initLibraryCache(){
		
		for (EventCategory category : EventCategory.values()) {
			updateEventCache(category);
		}
		
		status = EventStatusCache.getInstance().initCacheValue();
	}
	
	private void loadConfigurationData() {
		JAXBContext context;
		Reader reader = null;
		try {
			context = JAXBContext.newInstance(CategotyList.class);
			Unmarshaller um = context.createUnmarshaller();
			reader = LoadFileUtil.loadSQLFromFile(LoadFileUtil.class, "categorys-jaxb.xml");
			CategotyList categoryStore = (CategotyList) um.unmarshal(reader);
			categoryList = categoryStore.getCategorysList();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(reader!=null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
