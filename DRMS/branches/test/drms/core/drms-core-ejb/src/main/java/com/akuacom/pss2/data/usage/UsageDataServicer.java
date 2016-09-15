package com.akuacom.pss2.data.usage;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.jws.WebService;

@WebService
public interface UsageDataServicer {
    
    @Remote
    public interface R extends UsageDataServicer {}
    @Local
    public interface L extends UsageDataServicer {}

    UsageDataTransferVo getDataEntries(List<String> partNames, String programName, String eventName, List<String> partDataSetNames, String strDate,boolean showRawData, boolean isReport, boolean individual);

    List<String> getDateRanges(String participantName,List<String> partDataSetNames,String strDate);
    
}