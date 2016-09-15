package com.akuacom.pss2.program.apx.common;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.event.Event;


public interface ApxManagerHelper {
	@Remote
    public interface R extends ApxManagerHelper {   }
    @Local
    public interface L extends ApxManagerHelper {   }
    
    public void sendEventCreationNotifications(Event event, List<ProgramValidationMessage> warnings);
    public void sendExceptionNotifications(String content, String programName) ;
    public void sendExceptionNotifications(Exception e, String programName);

}
