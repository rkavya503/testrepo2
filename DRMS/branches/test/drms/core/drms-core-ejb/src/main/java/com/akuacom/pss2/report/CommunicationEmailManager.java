package com.akuacom.pss2.report;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.report.entities.CommunicationEmail;

public interface CommunicationEmailManager {
	
	@Remote
    public interface R extends CommunicationEmailManager {}
    @Local
    public interface L extends CommunicationEmailManager {}
	
    List<CommunicationEmail> find(Date from,Date to);
}
