package com.akuacom.pss2.email;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.timer.TimerManager;

public interface UndeliveredEmailManager extends TimerManager{
	
	@Remote
    public interface R extends UndeliveredEmailManager {}
    @Local
    public interface L extends UndeliveredEmailManager {}
    
    public List<EmailReportEntity> getUndeliveredEmails(Date start,Date end) throws SQLException;
    
    public int getEmailsCounts(Date start,Date end) throws SQLException;
    
    public void scheduleTimer();
    public void invokeTimer();
    public void sendReportEmail();
    
}
