package com.akuacom.pss2.customer.baseline;

import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;

import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.history.HistoryDataManager;
import com.akuacom.utils.lang.DateUtil;

public class BaseLineGenerator implements Runnable{
	Logger log = Logger.getLogger(BaseLineGenerator.class);
	private Date date;
	private Date endDate;
	private String participantName;
	private HistoryDataManager dataManager;
	private ProgressUpdater progressUpdater;
	
	public BaseLineGenerator(ProgressUpdater progressUpdater,Date date, String participantName,
			HistoryDataManager dataManager) {
		this.date = date;
		this.endDate =DateUtil.endOfDay(date);
		this.participantName = participantName;
		this.dataManager = dataManager;
		this.progressUpdater = progressUpdater;
	}
	
	@Override
	public void run() {
		try {
			log.debug("###Baseline generation debug### : Prepare to generate baseline for participant: "+participantName+" for :"+date);
			List<PDataEntry> baseline = dataManager.generateBaseline(participantName, date);
			if(baseline.isEmpty()){
				log.debug("###Baseline generation debug### : No baseline for participant: "+participantName+" for :"+date);
			}else{
				log.debug("###Baseline generation debug### : Baseline generated for participant: "+participantName+" for :"+date);
			}
//			dataManager.generateShedForEventParticipant(date);
		}catch(Exception e){
			log.debug("###Baseline generation error### : error occured"+e+" when generate baseline for participant: "+participantName+" for :"+date);
		}
		progressUpdater.progress(1);
	}
}
