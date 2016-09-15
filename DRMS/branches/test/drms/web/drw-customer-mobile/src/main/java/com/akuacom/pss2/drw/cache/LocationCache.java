package com.akuacom.pss2.drw.cache;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;
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

import com.akuacom.pss2.drw.DREvent2013Manager;
import com.akuacom.pss2.drw.entry.Category;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.SlapBlockRange;
import com.akuacom.utils.drw.CacheNotificationMessage;


public class LocationCache implements MessageListener {
	
	private static Logger log = Logger.getLogger(LocationCache.class);
	
	private Map<String,SlapBlockRange> slapBlockRange = new HashMap<String,SlapBlockRange>();
	private Map<String,List<String>> slapBlocks = new HashMap<String,List<String>>();
	
	public Map<String, SlapBlockRange> getSlapBlockRange() {
		return slapBlockRange;
	}


	public void setSlapBlockRange(Map<String, SlapBlockRange> slapBlockRange) {
		this.slapBlockRange = slapBlockRange;
	}

	private Date updateTime;
	private static LocationCache instance = new LocationCache();
	public static LocationCache getInstance(){
		return instance;
	}

	private LocationCache(){
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
					Thread.currentThread().setContextClassLoader(LocationCache.class.getClassLoader());
					HashSet<CacheNotificationMessage> msgs=(HashSet<CacheNotificationMessage>)obj;
					for (Object object:msgs){
						CacheNotificationMessage msg = DrEventCache.unmarshalMesssage(object);
						updateCache(msg);
					}
					return;
				}else if (obj instanceof String){
					Thread.currentThread().setContextClassLoader(LocationCache.class.getClassLoader());
					return;
				}
				
			} catch (JMSException e) {
			}catch (RuntimeException rte) {
				log.error(" Redeliver Message in LocationCache " +rte.getMessage());
				try{
					session.commit();
				}catch (JMSException e) {
				}
			}
			
			final CacheNotificationMessage msg = getNotification(message);
			if(msg!=null){
				Thread.currentThread().setContextClassLoader(LocationCache.class.getClassLoader());
				updateCache(msg);
			}
		 }
	}
	
	private synchronized void updateCache(CacheNotificationMessage message){
		if(!"Location".equalsIgnoreCase(message.getProgramName())){
			return;
		}
		log.debug(message);
		Date now = new Date();	
		DREvent2013Manager manager=DRWUtil.getDREvent2013Manager();
		List<String> slaps = manager.getAllSlaps();
		slapBlockRange = new HashMap<String,SlapBlockRange>();
		for(String number : slaps){
			slapBlockRange.put(number, manager.getSlapBlockRange(number));
			slapBlocks.put(number, manager.getBlocksForSlap(number));
		}
		
		setUpdateTime(now);
	}
	
	public synchronized void updateLocCache(CacheNotificationMessage msg){
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
//			env.setProperty("java.naming.provider.url", "jnp://localhost:1099");
//			InitialContext iniCtx = new InitialContext(env);
			//TODO ?
//		    Object tmp = iniCtx.lookup("ConnectionFactory");
//		    topicConnectionFactory = (TopicConnectionFactory) tmp; 
//		    connection =topicConnectionFactory.createTopicConnection();
//			session = connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
//			pss2Topic = (Topic) iniCtx.lookup("topic/pss2");
//			MessageConsumer subscriber = session.createSubscriber(pss2Topic);
//			subscriber.setMessageListener(this);
//			connection.start();
		} catch (Exception e) {
			log.error("Fail to subscribe topic toipc/pss2- "+ e.getMessage(),e);
		}
	}
	
	private void initLibraryCache(){
		DREvent2013Manager manager=DRWUtil.getDREvent2013Manager();
		List<String> slaps = manager.getAllSlaps();
		slapBlockRange = new HashMap<String,SlapBlockRange>();
		for(String number : slaps){
			slapBlockRange.put(number, manager.getSlapBlockRange(number));
			slapBlocks.put(number, manager.getBlocksForSlap(number));
		}
		
		setUpdateTime(new Date());
	}

	public Map<String, List<String>> getSlapBlocks() {
		return slapBlocks;
	}

	public void setSlapBlocks(Map<String, List<String>> slapBlocks) {
		this.slapBlocks = slapBlocks;
	}
	
}
