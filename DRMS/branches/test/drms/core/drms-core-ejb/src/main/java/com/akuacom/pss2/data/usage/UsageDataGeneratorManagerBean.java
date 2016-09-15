package com.akuacom.pss2.data.usage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.DataGeneratorManager;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.utils.lang.DateUtil;
import com.akuacom.utils.lang.TimingUtil;

@Stateless
public class UsageDataGeneratorManagerBean implements UsageDataGeneratorManager.R, UsageDataGeneratorManager.L {

	private static final int NOISE_RATE = 10;
	private static long intervalInMillis = 15 * TimingUtil.MINUTE_MS;
	private static final Logger log = Logger.getLogger(DataGeneratorManager.class);
	private static final String USAGE_DEMO = "p1";
	

	/**
	 * 
	 * @throws Exception
	 */
	@Override
	public void generateData() throws Exception {
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, -30);
		List<String> partNames = new ArrayList<String>();
		partNames.add(USAGE_DEMO);
		generateData(partNames,null, cal.getTime(), new Date());
	}

	/**
	 * 
	 * @param participantName
	 * @param startTime
	 * @param endTime
	 * @throws Exception
	 */
	@Override
	public void generateData(List<String> partNames, List<String> dataSourceNames, Date startTime, Date endTime) throws Exception {
		for (int i = 0; i < partNames.size(); i++) {

			String participantName = partNames.get(i);
            if(null==dataSourceNames||dataSourceNames.size()==0){
            	dataSourceNames = new ArrayList<String>();
            	dataSourceNames.add("meter1");
            }
            for(int j=0; j<dataSourceNames.size(); j++){
            	String dataSourceName = dataSourceNames.get(j);
            	generateDataForPart(startTime, endTime, participantName, dataSourceName);
            }
		}
	}

	public List<PDataEntry> generateDataForPart(Date startTime, Date endTime,  String participantName, String dataSourceName) throws Exception {
		final DataManager bean = EJBFactory.getBean(DataManager.class);
		PDataSource source = bean.getDataSourceByNameAndOwner(dataSourceName, participantName);
		if (source == null) {
			PDataSource ds = new PDataSource();
			ds.setName("meter1");
			ds.setOwnerID(participantName);
			
			source = bean.createPDataSource(ds);
		} 

		final PDataSet dataSet = bean.getDataSetByName("Usage");

		final List<PDataEntry> dataEntryList = generateDataForParticpant(EntryDataUtil.generateModel(), source, dataSet, startTime, endTime);
		final Set<PDataEntry> set = new HashSet<PDataEntry>(dataEntryList);
		bean.createDataEntries(set);
		return dataEntryList;
	}

	public List<PDataEntry> generateDataForParticpant(Map<String, List<PDataEntry>> map, PDataSource datasource, PDataSet dataSet, Date startDate, Date endDate) {
		startDate = DateUtil.stripTime(startDate);
		endDate = DateUtil.stripTime(endDate);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		final Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		log.debug("Start: " + startDate + ", End: " + endDate);

		ArrayList<PDataEntry> results = new ArrayList<PDataEntry>();
		while (!cal.after(end)) {

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
			 
			cal.add(Calendar.DATE, 1);
		}
		return results;
	}

	private List<PDataEntry> generateDataFromModel(List<PDataEntry> inList, PDataSource datasource, PDataSet dataSet, Calendar cal) {

		final int slots = (int) (TimingUtil.DAY_MS / intervalInMillis);
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

	
	// for unit test data generation
	public List<PDataEntry> generateDataForParticpant(Map<String, List<PDataEntry>> map, PDataSource datasource, PDataSet dataSet) {

		final ArrayList<PDataEntry> results = new ArrayList<PDataEntry>();

		results.addAll(generateDataFromModel(map, datasource, dataSet));
		return results;
	}
	
	
	private static List<PDataEntry> generateDataFromModel(Map<String, List<PDataEntry>> map, PDataSource datasource, PDataSet dataSet) {
		final ArrayList<PDataEntry> results = new ArrayList<PDataEntry>();
		Set<String> keys = map.keySet();
		
		for(String key : keys){
			List<PDataEntry> list = map.get(key);
			for(PDataEntry entry : list){
				entry.setDatasource(datasource);
				entry.setDataSet(dataSet);
				entry.setActual(true);
			}
			results.addAll(list);
		}
		
		return results;
	}

}

class MainThread extends Thread {

	@Override
	public void run() {
		final DataManager bean = EJBFactory.getBean(DataManager.class);
		String prefix = "p";
		int index = 1;
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, -30);
		for (int i = 0; i < 1; i++) {
			String name = prefix + index++;

			String participantName = name;

			List<String> dataSourceNames = new ArrayList<String>();
			dataSourceNames.add("meter1");

			for (int j = 0; j < dataSourceNames.size(); j++) {
				String dataSourceName = dataSourceNames.get(j);

				PDataSource source = bean.getDataSourceByNameAndOwner(dataSourceName, participantName);
				if (source == null) {
					PDataSource ds = new PDataSource();
					ds.setName("meter1");
					ds.setOwnerID(participantName);
					source = bean.createPDataSource(ds);
				}

				final PDataSet dataSet = bean.getDataSetByName("Usage");

				UsageDataGeneratorManagerBean usageData = new UsageDataGeneratorManagerBean();
				List<PDataEntry> dataEntrys = null;
				try {
					dataEntrys = usageData.generateDataForParticpant(EntryDataUtil.generateModel(), source, dataSet, cal.getTime(), new Date());
					//System.out.println(dataEntrys.size());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				ParticipantThread partThread = new ParticipantThread(participantName,dataEntrys);
				partThread.start();

			}//end datasource loop

			try {
				Thread.sleep(1000 * 2); //start threads as 2 seconds interval
			} catch (InterruptedException e) {
			}

		}//end participant loop

	}

}

class ParticipantThread extends Thread{
	private String pName;
	private List<PDataEntry> dataEntrys;
	ParticipantThread (String pName,List<PDataEntry> dataEntrys){
		this.pName = pName;
		this.dataEntrys = dataEntrys;
	}
	  @Override  
	    public void run(){  
			final DataManager bean = EJBFactory.getBean(DataManager.class);
		  for(int i=0;i<dataEntrys.size();i++){
			  bean.createPDataEntry(dataEntrys.get(i));
			  try {  
	                Thread.sleep(1000*60*2);  //populate a dataEntry every 2 minutes
	            } catch (InterruptedException e) {  
	            }  
			  
		  }
		  
		  
	  }
	
}
