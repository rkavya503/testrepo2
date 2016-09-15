package com.akuacom.drms.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.akuacom.ejb.EntityNotFoundException;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.DateRange;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.utils.lang.DateUtil;

public class UsageDataQuery {

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
	public UsageDataQuery(String host, String loginUserName, String loginPwd) {
		this.host = (host != null ? host : UsageDataQueryTimer.DEFAULT_HOST_NAME);
		this.loginUserName = (loginUserName != null ? loginUserName : UsageDataQueryTimer.DEFAULT_USER_NAME);
		this.loginPwd = (loginPwd != null ? loginPwd : UsageDataQueryTimer.DEFAULT_USER_PASSWORD);

	}

	/**
	 * InitialContext
	 */
	private InitialContext initialContext = null;
	/**
	 * ClientManager
	 */
	private DataManager dataManager;

	private UsageDataManager dataServicer;

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
	public List<Long> queryUsageData(String participantPrefix, int numberOfParticipants, int numberOfDays, int partInterval, int pointInterval, int exeTimes) throws NamingException {
		List<Long> threadIds = new ArrayList<Long>();
		if (participantPrefix == null) {
			participantPrefix = UsageDataQueryTimer.DEFAULT_PARTICIPANT_PRIFIX;
		}
		if (numberOfParticipants == 0) {
			numberOfParticipants = UsageDataQueryTimer.DEFAULT_NUMBER_OF_PARTICIPANTS;
		}
		if (numberOfDays == 0) {
			numberOfDays = UsageDataQueryTimer.DEFAULT_NUMBER_OF_DAYS;
		}
		if (partInterval == 0) {
			partInterval = UsageDataQueryTimer.DEFAULT_PART_INTERVAL;
		}
		if (pointInterval == 0) {
			pointInterval = UsageDataQueryTimer.DEFAULT_POINT_INTERVAL;
		}
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, -(numberOfDays - 1));
		for (int i = 0; i < numberOfParticipants; i++) {
			String participantName = participantPrefix + (i + 1);

			List<String> dataSourceNames = new ArrayList<String>();
			dataSourceNames.add("meter1");

			for (int j = 0; j < dataSourceNames.size(); j++) {
				List<DateRange> dateRanges = new ArrayList<DateRange>();
				setupDateRange(dateRanges);

				UsageQueryThread partThread = new UsageQueryThread(participantName, dateRanges, pointInterval, exeTimes);

				Thread newthread = new Thread(partThread);
				newthread.start();

			}

			if (partInterval != 0) {
				try {
					Thread.sleep(1000 * partInterval); // start threads as 2
														// seconds interval0 *
														// 60*5
				} catch (InterruptedException e) {
				}
			}

		}
		return threadIds;
	}

	/**
	 * Generates participants
	 * 
	 * @param participantPrefix
	 * @param clientPrefix
	 * @param clientPassword
	 * @param numberOfParticipants
	 * @throws NamingException
	 */
	public List<Long> queryBaseLineData(String participantPrefix, int numberOfParticipants, int numberOfDays, int partInterval, int pointInterval, int exeTimes) throws NamingException {
		List<Long> threadIds = new ArrayList<Long>();
		if (participantPrefix == null) {
			participantPrefix = UsageDataQueryTimer.DEFAULT_PARTICIPANT_PRIFIX;
		}
		if (numberOfParticipants == 0) {
			numberOfParticipants = UsageDataQueryTimer.DEFAULT_NUMBER_OF_PARTICIPANTS;
		}
		if (numberOfDays == 0) {
			numberOfDays = UsageDataQueryTimer.DEFAULT_NUMBER_OF_DAYS;
		}
		if (partInterval == 0) {
			partInterval = UsageDataQueryTimer.DEFAULT_PART_INTERVAL;
		}
		if (pointInterval == 0) {
			pointInterval = UsageDataQueryTimer.DEFAULT_POINT_INTERVAL;
		}
		if (exeTimes == 0) {
			exeTimes = UsageDataQueryTimer.DEFAULT_EXECUTE_TIMES;
		}
		
		
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, -(numberOfDays - 1));
		for (int i = 0; i < numberOfParticipants; i++) {
			String participantName = participantPrefix + (i + 1);

			List<String> dataSourceNames = new ArrayList<String>();
			dataSourceNames.add("meter1");

			for (int j = 0; j < dataSourceNames.size(); j++) {
				BaseLineQueryThread partThread = new BaseLineQueryThread(participantName, pointInterval,exeTimes);
				Thread newthread = new Thread(partThread);
				newthread.start();

			}

			if (partInterval != 0) {
				try {
					Thread.sleep(1000 * partInterval); // start threads as 2
														// seconds interval0 *
														// 60*5
				} catch (InterruptedException e) {
				}
			}

		}// end participant loop
		return threadIds;
	}

	class UsageQueryThread implements Runnable {
		private String pName;
		private List<DateRange> dateRanges;
		private int pointInterval;
		private int exeTimes;

		UsageQueryThread(String pName, List<DateRange> dateRanges, int pointInterval, int exeTimes) {
			this.pName = pName;
			this.dateRanges = dateRanges;
			this.pointInterval = pointInterval;
			this.exeTimes = exeTimes;
		}

		@Override
		public void run() {
			for (int i = 0; i < exeTimes; i++) {
				DataManager bean = null;
				try {
					bean = getDataManager();
				} catch (NamingException e) {
					e.printStackTrace();
				}
				
				UsageDataManager dataService = null;
				try {
					dataService = getUsageDataServicer();
				} catch (NamingException e) {
					e.printStackTrace();
				}

				List<PDataSet> pdatasets = bean.getDataSets();
				List<String> pdatasources = new ArrayList<String>();

				PDataSource pdatasource = bean.getDataSourceByNameAndOwner("meter1", pName);
				if (pdatasource == null) {
					PDataSource ds = new PDataSource();
					ds.setName("meter1");
					ds.setOwnerID(pName);
					pdatasource = bean.createPDataSource(ds);
				}
				pdatasources.add(pdatasource.getUUID());

				for (PDataSet pdataset : pdatasets) {
					for (DateRange dr : dateRanges) {
						long beforeGenerateUsageTime = System.currentTimeMillis();
						dataService.findRealTimeEntryListForParticipant(pName, pdataset.getUUID(), dr.getStartTime(), null, false, false);//(pName, dr.getStartTime(), pdataset.getUUID());
						//bean.getDataEntryList(pdataset.getUUID(), pdatasources, dr,false);Modified by Frank
						long afterGenerateUsageTime = System.currentTimeMillis();
						System.out.println((afterGenerateUsageTime - beforeGenerateUsageTime));

					}
				}

				if (pointInterval != 0) {
					try {
						Thread.sleep(1000 * pointInterval);
					} catch (InterruptedException e) {
					}
				}

			}

		}
	}

	class BaseLineQueryThread implements Runnable {

		private String pName;
		private int pointInterval;
		private int exeTimes;

		BaseLineQueryThread(String pName, int pointInterval, int exeTimes) {
			this.pName = pName;
			this.pointInterval = pointInterval;
			this.exeTimes = exeTimes;
		}

		@Override
		public void run() {

			for (int i = 0; i < exeTimes; i++) {

				DataManager bean = null;
				try {
					bean = getDataManager();
				} catch (NamingException e) {
					e.printStackTrace();
				}

				UsageDataManager dataService = null;
				try {
					dataService = getUsageDataServicer();
				} catch (NamingException e) {
					e.printStackTrace();
				}

				List<String> pdatasources = new ArrayList<String>();

				PDataSource pdatasource = bean.getDataSourceByNameAndOwner("meter1", pName);
				if (pdatasource == null) {
					PDataSource ds = new PDataSource();
					ds.setName("meter1");
					ds.setOwnerID(pName);
					pdatasource = bean.createPDataSource(ds);
				}
				pdatasources.add(pdatasource.getUUID());
				
				PDataSet dataset = bean.getDataSetByName("baseline");

				long beforeQueryBaseLIneTime = System.currentTimeMillis();
				Date today = DateUtil.stripTime(new Date());
				//Modified by Frank
				dataService.findBaselineEntryListForParticipant(pName, today, dataset.getUUID(),null,false);//.getBaselineDataEntryList(pdatasources, today);

				long afterGenerateUsageTime = System.currentTimeMillis();
				System.out.println((afterGenerateUsageTime - beforeQueryBaseLIneTime));

				if (pointInterval != 0) {
					try {
						Thread.sleep(1000 * pointInterval);
					} catch (InterruptedException e) {
					}
				}

			}

		}
	}

	private void setEndTime(Date endTime) {
		endTime.setHours(23);
		endTime.setMinutes(59);
		endTime.setSeconds(59);
	}

	private void setStartTime(Date startTime) {
		startTime.setHours(0);
		startTime.setMinutes(0);
		startTime.setSeconds(0);
	}

	private DataManager getDataManager() throws NamingException {
		if (dataManager == null) {
			dataManager = (DataManager) getInitialContext().lookup("pss2/DataManagerBean/remote");
		}
		return dataManager;
	}

	private UsageDataManager getUsageDataServicer() throws NamingException {
		if (dataServicer == null) {
			dataServicer = (UsageDataManager) getInitialContext().lookup("pss2/UsageDataServicerBean/remote");
		}
		return dataServicer;
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
			env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.security.jndi.JndiLoginInitialContextFactory");
			env.put("java.naming.provider.url", host + ":1099");
			initialContext = new InitialContext(env);
		}

		return initialContext;
	}

	private void setupDateRange(List<DateRange> dateRanges) {
		DateRange today = new DateRange();
		Date endTime = new Date();
		Date startTime = new Date();
		setStartTime(startTime);
		setEndTime(endTime);
		today.setStartTime(startTime);
		today.setEndTime(endTime);
		dateRanges.add(today);
	}

}
