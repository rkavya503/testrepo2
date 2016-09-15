package com.akuacom.pss2.data.irr;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.usage.UsageDataManager;

@Stateless
public class DefaultDataCalculationHandlerBean implements
		DataCalculationHandler.L, DataCalculationHandler.R {
	@EJB
	UsageDataManager.L dataManager;

	@Override
	public DataEntriesVo getData(PDataSet dataSet, String participantName,
			Date date, Boolean showRawData, Boolean individual, Boolean isUiShowRawData) {
		DataEntriesVo result = new DataEntriesVo();
		result.setEntries(dataManager.findDefaultEntryListForParticipant(participantName, dataSet.getUUID(), date));
		
		return result;
	}

}
