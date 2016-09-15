package com.akuacom.pss2.report;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.report.entities.ClientOfflineReport;
import com.akuacom.pss2.report.entities.ClientOfflineReportEntity;
import com.akuacom.pss2.timer.TimerManager;

public interface ClientOfflineReportManager extends TimerManager {
	
	@Remote
    public interface R extends ClientOfflineReportManager {}
    @Local
    public interface L extends ClientOfflineReportManager {}
	void invokeTimer();
	void scheduleTimer();
    void generateReport();
    List<ClientOfflineReport> getReportSummary();
//	List<ClientOfflineReportEntity> getReportDetail(Date date);
	ClientOfflineReport getReport(String uuid);
	void generateReport(Date date);
}
