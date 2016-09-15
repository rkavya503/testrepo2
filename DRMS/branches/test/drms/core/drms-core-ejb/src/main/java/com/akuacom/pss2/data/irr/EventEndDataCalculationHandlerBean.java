package com.akuacom.pss2.data.irr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSetEAO;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.pss2.event.Event;
@Stateless
public class EventEndDataCalculationHandlerBean implements DataCalculationHandler.L, DataCalculationHandler.R {

	@EJB
	UsageDataManager.L dataManager;
	@EJB
	PDataSetEAO.L dataSetEAO;

	@Override
	public DataEntriesVo getData(PDataSet dataSet, String participantName,
			Date date, Boolean showRawData, Boolean individual, Boolean isUiShowRawData) {
		DataEntriesVo result = new DataEntriesVo();
		Event eve = null;
		if(individual){
			eve = dataManager.getEventByDateAndParticipant(participantName, date);
		}else{
			eve = dataManager.getEventByDateAndAggreagator(participantName, date);
		}
		if(eve == null){
			result.setEntries(new ArrayList<PDataEntry>());
			return result;
		}else{
			Date time = eve.getEndTime();
			
			List<PDataEntry> eventEndLine = new ArrayList<PDataEntry>();
			PDataEntry start = new PDataEntry();
			PDataEntry end = new PDataEntry();
			start.setTime(time);
			start.setValue(0.0);
			start.setActual(true);
			end.setTime(time);
			end.setValue(1.0);
			end.setActual(true);
			
			eventEndLine.add(start);
			eventEndLine.add(end);
			
			result.setEntries(eventEndLine);
			return result;
		}
		
	}
	
	public double getMax(List<PDataEntry> entries){
		double max = 0;
		for(PDataEntry entry : entries){
			if(entry.getValue()>max){
				max = entry.getValue();
			}
		}
		
		return max;
	}

}
