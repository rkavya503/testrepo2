package com.akuacom.pss2.data.irr;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.usage.BaselineConfigManager;
import com.akuacom.pss2.data.usage.BaselineModelManager;
import com.akuacom.pss2.data.usage.UsageDataManager;
import com.akuacom.pss2.data.usage.calcimpl.BaseLineMA;
import com.akuacom.pss2.system.SystemManager;
@Stateless
public class BaselineDataCalculationHandlerBean implements DataCalculationHandler.L, DataCalculationHandler.R {
	@EJB
	UsageDataManager.L dataManager;
    @EJB
	BaseLineMA.L baseLineMA;
    @EJB
    BaselineModelManager.L bmManager;
    @EJB
    BaselineConfigManager.L bcManager;
    @EJB
    protected SystemManager.L sysManager;

	@Override
	public DataEntriesVo getData(PDataSet dataSet, String participantName,
			Date date, Boolean showRawData, Boolean individual, Boolean isUiShowRawData) {
		showRawData = false;// this method will always return the adjusted baseline rather than raw baseline
		DataEntriesVo result = new DataEntriesVo();
		List<PDataEntry> temp = null;
		
		temp = dataManager.findBaselineEntryListForParticipant(participantName, date, dataSet.getUUID(),individual,showRawData);
		
		result.setEntries(temp);
		
		return result;
	}

}
