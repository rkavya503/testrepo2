package com.akuacom.drms.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.usage.EntryDataUtil;
import com.akuacom.utils.lang.DateUtil;

public class UsageDataGenerator {
	
	/**
	 * Constructor
	 * 
	 * @param host
	 *            Host name of the machine
	 * @param loginUserName
	 *            Login user name
	 * @param loginPwd
	 *            Login Password
	 */
	public UsageDataGenerator(String host, String loginUserName,
			String loginPwd) {
		this.host = (host != null ? host : PartDataTimer.DEFAULT_HOST_NAME);
		this.loginUserName = (loginUserName != null ? loginUserName
				: PartDataTimer.DEFAULT_USER_NAME);
		this.loginPwd = (loginPwd != null ? loginPwd
				: PartDataTimer.DEFAULT_USER_PASSWORD);

	}

	private static final int NOISE_RATE = 10;
	private static long intervalInMillis = 15 * 60 * 1000;
	private static final int MILLI_SECONDS_OF_HOUR = 60 * 60 * 1000;
	private static final int MILLI_SECONDS_OF_DAY = 24 * MILLI_SECONDS_OF_HOUR;
	

	/**
	 * InitialContext
	 */
	private InitialContext initialContext = null;
	/**
	 * ClientManager
	 */
	private DataManager dataManager;
	
	/**
	 * Login User Name
	 */
	private String loginUserName = null;

	/**
	 * Login Password
	 */
	private String loginPwd = null;

	/**
	 * Host
	 */
	private String host = null;
	/**
	 * Generates participants
	 * 
	 * @param participantPrefix
	 * @param clientPrefix
	 * @param clientPassword
	 * @param numberOfParticipants
	 * @throws NamingException
	 */
	public List<Long> generateUsageData(String participantPrefix, int numberOfParticipants, int numberOfDays, int partInterval , int pointInterval)
			throws NamingException {
		List<Long> threadIds = new ArrayList<Long>();
		if (participantPrefix == null) {
			participantPrefix = PartDataTimer.DEFAULT_PARTICIPANT_PRIFIX;
		}
		if (numberOfParticipants == 0) {
			numberOfParticipants = PartDataTimer.DEFAULT_NUMBER_OF_PARTICIPANTS;
		}
		if (numberOfDays == 0) {
			numberOfDays = PartDataTimer.DEFAULT_NUMBER_OF_DAYS;
		}
		if (partInterval == 0) {
			partInterval = PartDataTimer.DEFAULT_PART_INTERVAL;
		}
		if (pointInterval == 0) {
			pointInterval = PartDataTimer.DEFAULT_POINT_INTERVAL;
		}
		final DataManager bean = getDataManager();
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, -(numberOfDays-1));
		for (int i = 0; i < numberOfParticipants; i++) {
			String participantName = participantPrefix + i;

			List<String> dataSourceNames = new ArrayList<String>();
			dataSourceNames.add("meter1");

			for (int j = 0; j < dataSourceNames.size(); j++) {
				String dataSourceName = dataSourceNames.get(j);

				PDataSource source = getDataSourceByName(bean, participantName, dataSourceName);

				final PDataSet dataSet = bean.getDataSetByName("Usage");

				List<PDataEntry> dataEntrys = null;
				try {
					dataEntrys = UsageDataGenerator.generateDataForParticpant(EntryDataUtil.generateModel(), source, dataSet, cal.getTime(), new Date());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				ParticipantUsageGenerateThread partThread = new ParticipantUsageGenerateThread(participantName,dataEntrys,pointInterval);
				threadIds.add(partThread.getId());
				partThread.start();

			}//end datasource loop
			
			if(partInterval!=0){
				try {
					Thread.sleep(1000 * partInterval); //start threads as 2 seconds interval
				} catch (InterruptedException e) {
				}
			}



		}//end participant loop
		return threadIds;
	}
	
	private DataManager getDataManager() throws NamingException {
		if (dataManager == null) {
			dataManager = (DataManager) getInitialContext().lookup(
					"pss2/DataManagerBean/remote");
		}
		return dataManager;
	}
	/**
	 * Fetches the initial context
	 * 
	 * @return InitialContext
	 * @throws NamingException
	 *             Failure
	 */
	private InitialContext getInitialContext() throws NamingException {
		if (initialContext == null) {

			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.SECURITY_PRINCIPAL, loginUserName);
			env.put(Context.SECURITY_CREDENTIALS, loginPwd);
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jboss.security.jndi.JndiLoginInitialContextFactory");
			env.put("java.naming.provider.url", host + ":1099");
			initialContext = new InitialContext(env);
		}

		return initialContext;
	}
	/**
	 * Clears the participants
	 * 
	 * @param participantPrefix
	 *            participant Prefix
	 * @param clientPrefix
	 *            client Prefix
	 * @param numberOfParticipants
	 *            number Of Participants
	 * @throws NamingException
	 *             Failure
	 */
	public void clearUsageData(String participantPrefix,int numberOfParticipants){
		try{
		if (participantPrefix == null) {
			participantPrefix = PartDataTimer.DEFAULT_PARTICIPANT_PRIFIX;
		}
		if (numberOfParticipants == 0) {
			numberOfParticipants = PartDataTimer.DEFAULT_NUMBER_OF_PARTICIPANTS;
		}
		
		for (int i = 0; i < numberOfParticipants; i++) {
			String participantName = participantPrefix + i;
			
			PDataSource source = getDataManager().getDataSourceByNameAndOwner("meter1", participantName);
			getDataManager().deletePDataSource(source.getUUID());
	
		}
		
		}catch(Exception ex){
			System.out.println("meet error in clearUsageData");
		}
	}
	
	/**
	 * Clean up the context and connection
	 */
	public void cleanup() {
		if (initialContext != null) {
			try {
				initialContext.close();
				initialContext = null;
				dataManager = null;
			} catch (NamingException e) {
			}

		}
	}


	private PDataSource getDataSourceByName(final DataManager bean, String participantName, String dataSourceName) {
		PDataSource source = bean.getDataSourceByNameAndOwner(dataSourceName, participantName);
		if (source == null) {
			PDataSource ds = new PDataSource();
			ds.setName("meter1");
			ds.setOwnerID(participantName);
			source = bean.createPDataSource(ds);
		}
		return source;
	}

	private static List<PDataEntry> generateDataForParticpant(Map<String, List<PDataEntry>> map, PDataSource datasource, PDataSet dataSet, Date startDate, Date endDate) {
		startDate = DateUtil.stripTime(startDate);
		endDate = DateUtil.stripTime(endDate);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		final Calendar end = Calendar.getInstance();
		end.setTime(endDate);

		final ArrayList<PDataEntry> results = new ArrayList<PDataEntry>();
		for (; !cal.after(end); cal.add(Calendar.DATE, 1)) {

			List<PDataEntry> inList = null;

			switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				inList =  map.get("sunday");
				break;
			case Calendar.MONDAY:
				inList =  map.get("monday");
				break;
			case Calendar.TUESDAY:
				inList =  map.get("tuesday");
				break;
			case Calendar.WEDNESDAY:
				inList =  map.get("wednesday");
				break;
			case Calendar.THURSDAY:
				inList =  map.get("thursday");
				break;
			case Calendar.FRIDAY:
				inList =  map.get("friday");
				break;
			case Calendar.SATURDAY:
				inList =  map.get("saturday");
				break;
			default:
				break;
			}

			results.addAll(generateDataFromModel(inList, datasource, dataSet, cal));
		}
		return results;
	}

	private static List<PDataEntry> generateDataFromModel(List<PDataEntry> inList, PDataSource datasource, PDataSet dataSet, Calendar cal) {

		final int slots = (int) (MILLI_SECONDS_OF_DAY / intervalInMillis);
		final List<PDataEntry> set = new ArrayList<PDataEntry>(slots);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		for (int i = 0; i < slots; i++) {
			final PDataEntry entry = new PDataEntry();

			PDataEntry in = (PDataEntry) inList.get(i);

			Date tem = in.getTime();
			Calendar temC = new GregorianCalendar();
			temC.setTime(tem);
			temC.set(Calendar.YEAR, year);
			temC.set(Calendar.MONTH, month);
			temC.set(Calendar.DATE, day);
			entry.setDatasource(datasource);
			entry.setDataSet(dataSet);
			entry.setTime(temC.getTime());
			entry.setValue(random(in.getValue()));
			set.add(entry);
		}
		return set;
	}

	private static Double random(Double in) {
		return in * (100 + Math.random() * NOISE_RATE) / 100;
	}
	
	class ParticipantUsageGenerateThread extends Thread{
		private String pName;
		private List<PDataEntry> dataEntrys;
		private int pointInterval;
		ParticipantUsageGenerateThread (String pName,List<PDataEntry> dataEntrys,int pointInterval){
			this.pName = pName;
			this.dataEntrys = dataEntrys;
			this.pointInterval = pointInterval;
		}
		  @Override  
		    public void run(){  
			  DataManager bean=null;
			try {
				bean = getDataManager();
			} catch (NamingException e) {
				e.printStackTrace();
			}
			  for(int i=0;i<dataEntrys.size();i++){
				  bean.createPDataEntry(dataEntrys.get(i));
				  if(pointInterval!=0){
					  try {  
			                Thread.sleep(1000*pointInterval);  //populate a dataEntry every 2 minutes
			            } catch (InterruptedException e) {  
			            }  
				  }
				  
			  }
			  
			  
		  }
	}

}

