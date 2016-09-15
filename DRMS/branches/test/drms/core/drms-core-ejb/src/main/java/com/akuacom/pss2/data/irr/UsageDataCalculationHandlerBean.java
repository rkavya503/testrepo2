package com.akuacom.pss2.data.irr;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.usage.UsageDataManager;

@Stateless
public class UsageDataCalculationHandlerBean implements DataCalculationHandler.L, DataCalculationHandler.R {
	@EJB
	UsageDataManager.L dataManager;
	@Override
	public DataEntriesVo getData(PDataSet dataSet, String participantName,
			Date date, Boolean showRawData, Boolean individual, Boolean isUiShowRawData) {
		DataEntriesVo result = new DataEntriesVo();
		result.setEntries(dataManager.findRealTimeEntryListForParticipant(participantName, dataSet.getUUID(), date, individual, showRawData, isUiShowRawData));
		if(!individual){
			result.setAllPaticipantNames(dataManager.findAllParticipantNames(participantName, date));
			result.setContributedPaticipantNames(dataManager.findContributedParticipantNames(participantName, date));
		}
		
		if(result.getEntries()==null||result.getEntries().isEmpty()) result.setMessage("No usage data for this day for this participant.");
		
		return result;
	}

}
