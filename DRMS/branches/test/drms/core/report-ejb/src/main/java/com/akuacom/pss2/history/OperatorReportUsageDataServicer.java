package com.akuacom.pss2.history;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.jws.WebService;

import com.akuacom.pss2.history.vo.UsageDataTransferVo;

@WebService
public interface OperatorReportUsageDataServicer {

	@Remote
    public interface R extends OperatorReportUsageDataServicer {}
    @Local
    public interface L extends OperatorReportUsageDataServicer {}

    UsageDataTransferVo getReportDataEntries(List<String> partNames, String strDate, String eventName);
}
