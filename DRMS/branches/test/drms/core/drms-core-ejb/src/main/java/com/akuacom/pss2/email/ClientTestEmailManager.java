package com.akuacom.pss2.email;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.timer.TimerManager;

public interface ClientTestEmailManager extends TimerManager {
	
	@Remote
    public interface R extends ClientTestEmailManager {}
    @Local
    public interface L extends ClientTestEmailManager {}
    
    public void digestClientTestEmail(Date start,Date end);
    
    public List<MessageEntity> findDigestMessage(Date start, Date end,List<Contact> operators);

	List<JSFClientTestEmailReportEntity> getReport(Date startDate, Date endDate);
	
	void invokeTimer();
	void scheduleTimer();
}
