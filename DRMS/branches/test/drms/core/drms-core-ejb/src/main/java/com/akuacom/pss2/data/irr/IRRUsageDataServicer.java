package com.akuacom.pss2.data.irr;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.jws.WebService;

@WebService
public interface IRRUsageDataServicer {
	@Remote
    public interface R extends IRRUsageDataServicer {}
    @Local
    public interface L extends IRRUsageDataServicer {}

    IRRUsageDataVo getDataEntriesForParticipant(String partName, String startTime, String endTime,boolean showRawData, boolean individual);
    IRRUsageDataVo getIRRDataEntriesForEvent(String eventName, String startTime, String endTime,boolean showRawData);
	    
}
