package com.akuacom.pss2.data.irr;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.data.PDataSet;

public interface DataCalculationHandler {
	 @Remote
     public interface R extends DataCalculationHandler {}
     @Local
     public interface L extends DataCalculationHandler {}
	/**
	 * Find data for given data set,participant and date
	 * @param dataSet
	 * @param participantName
	 * @param date
	 * @return
	 */
     DataEntriesVo getData(PDataSet dataSet, String participantName, Date date, Boolean showRawData, Boolean individual, Boolean isUiShowRawData);

}
