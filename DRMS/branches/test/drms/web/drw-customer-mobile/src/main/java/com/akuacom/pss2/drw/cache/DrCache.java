package com.akuacom.pss2.drw.cache;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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

import org.apache.log4j.Logger;

import com.akuacom.pss2.drw.RTPWeather;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.WeatherValue;
import com.akuacom.utils.drw.CacheNotificationMessage;


public class DrCache implements MessageListener {
	
	private static Logger log = Logger.getLogger(DrCache.class);
	private List<RTPWeather> forcastRTPEvents = new ArrayList<RTPWeather>();
	
	private RTPWeather currentRTP = new RTPWeather();
	
	/**
	 * @return the forcastRTPEvents
	 */
	public List<RTPWeather> getForcastRTPEvents() {
		return forcastRTPEvents;
	}

	/**
	 * @param forcastRTPEvents the forcastRTPEvents to set
	 */
	public void setForcastRTPEvents(List<RTPWeather> forcastRTPEvents) {
		this.forcastRTPEvents = forcastRTPEvents;
	}

	/**
	 * @return the currentRTP
	 */
	public RTPWeather getCurrentRTP() {
		if (forcastRTPEvents!=null && forcastRTPEvents.size()>0)
			currentRTP=forcastRTPEvents.get(0);
		return currentRTP;
	}

	/**
	 * @param currentRTP the currentRTP to set
	 */
	public void setCurrentRTP(RTPWeather currentRTP) {
		this.currentRTP = currentRTP;
	}
	
	private List<RTPWeather> getRTPForcast() {
		List<RTPWeather> result=new ArrayList<RTPWeather>();
		try {
			result = toRTPDataMode(DRWUtil.getCFEventManager().getForcast(new Date()));
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		return result;
	}
	
	private List<RTPWeather> toRTPDataMode(List<WeatherValue> weathers) {
		List<RTPWeather> models=new ArrayList<RTPWeather>();
		
		if(weathers.size() > 0)
		{
			Date weatherModifiedDate = new Date();
			for (WeatherValue weather:weathers) {
				RTPWeather model=new RTPWeather();
				SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
				model.setDayOfWeeK(dateFormat.format(weather.getDate()));
				dateFormat = new SimpleDateFormat("MMMM dd, yyyy");	
				model.setDate(dateFormat.format(weather.getDate()));
				model.setPricingCategory(weather.getPricingCategory());
				model.setModifiedTime(weatherModifiedDate.toString());
				models.add(model);
			}
			
		}
		return models;
	}
	
	
	private Date updateTime;
	private static DrCache instance = new DrCache();
	public static DrCache getInstance(){
		return instance;
	}
	

	private DrCache(){
		initSubscriber();
		initLibraryCache();
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	
	private Topic pss2Topic;
	private TopicConnection connection = null;
	private TopicSession session = null;
	private TopicConnectionFactory topicConnectionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message message) {
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
					Thread.currentThread().setContextClassLoader(DrCache.class.getClassLoader());
					HashSet<CacheNotificationMessage> msgs=(HashSet<CacheNotificationMessage>)obj;
					for (Object object:msgs){
						CacheNotificationMessage msg = DrEventCache.unmarshalMesssage(object);
						updateCache(msg);
					}
					return;
				}else if (obj instanceof String){
					Thread.currentThread().setContextClassLoader(DrCache.class.getClassLoader());
					return;
				}
				
			} catch (JMSException e) {
			}catch (RuntimeException rte) {
				log.error(" Redeliver Message in DrCache " +rte.getMessage());
				try{
					session.commit();
				}catch (JMSException e) {
				}
			}
			
			final CacheNotificationMessage msg = getNotification(message);
			if(msg!=null){
				Thread.currentThread().setContextClassLoader(DrCache.class.getClassLoader());
				updateCache(msg);
			}
		 }
	}
	
	private synchronized void updateCache(CacheNotificationMessage message){
		if(!"RTP".equalsIgnoreCase(message.getProgramName())){
			return;
		}
		log.debug(message);
		Date now = new Date();	
		setForcastRTPEvents(getRTPForcast());
		setCurrentRTP(getForcastRTPEvents().isEmpty()?new RTPWeather():getForcastRTPEvents().get(0));//need to consider empty list case
		setUpdateTime(now);
	}
	
	public synchronized void updateRTPCache(CacheNotificationMessage msg){
		updateCache(msg);
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
	

	private void initSubscriber() {
		try {
//			Properties env = new Properties();
//			env.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
//			env.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
//			
//			env.setProperty("java.naming.provider.url", "jnp://localhost:1099");
//			
//			InitialContext iniCtx = new InitialContext(env);
//			//TODO ?
//		    Object tmp = iniCtx.lookup("ConnectionFactory");
//			
//		    topicConnectionFactory = (TopicConnectionFactory) tmp; 
//		    connection =topicConnectionFactory.createTopicConnection();
//			session = connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
//				
//			pss2Topic = (Topic) iniCtx.lookup("topic/pss2");
//			
//			MessageConsumer subscriber = session.createSubscriber(pss2Topic);
//			subscriber.setMessageListener(this);
//			connection.start();
		} catch (Exception e) {
			log.error("Fail to subscribe topic toipc/pss2- "+ e.getMessage(),e);
		}
	}
	
	private void initLibraryCache(){
		
		setForcastRTPEvents(getRTPForcast());
		setCurrentRTP(getForcastRTPEvents().isEmpty()?new RTPWeather():getForcastRTPEvents().get(0));//need to consider empty list case
		
		setUpdateTime(new Date());
	}
	

}
