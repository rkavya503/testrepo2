/**
 * 
 */
package com.akuacom.pss2.program.cbp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.cpp.CPPProgramEJBBean;
import com.akuacom.pss2.util.EventStatus;
import com.akuacom.utils.drw.CacheNotificationMessage;


/**
 * the class CBPProgramEJBBeam
 *
 */
@Stateless
public class CBPProgramEJBBean extends CPPProgramEJBBean implements CBPProgramEJB.L, CBPProgramEJB.R {

	  
    @Override
    protected void publicToDRwebsite(Program program, Event event) {    	
    	if(event.getLocations()!=null && event.getLocations().size()>0) {
	        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        StringBuilder eventMsg=new StringBuilder();
	        eventMsg.append("CBP_EVENT;");
	        eventMsg.append("CREATE;");
	        eventMsg.append(program.getProgramClass()+";");
	        eventMsg.append(program.getProgramName()+";");
	        eventMsg.append(event.getEventName()+";");
	        eventMsg.append(format.format(event.getStartTime())+";");
	        eventMsg.append(format.format(event.getEndTime())+";");
	        eventMsg.append(format.format(event.getIssuedTime())+";");	        
	        eventMsg.append(convertToString(event.getLocations()));
	        
	        topicPublisher.publish(eventMsg.toString());
    	}
    }
    
    private String convertToString(List<String> input) {
    	StringBuilder builder=new StringBuilder();
    	for (String s:input) {
    		builder.append(s);
    		builder.append(",");
    	}
    	return builder.substring(0, builder.length()-1);
    }
    
    @Override
    protected void publicCancelToDRwebsite(Program program, Event event) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder eventMsg=new StringBuilder();
        eventMsg.append("CBP_EVENT;");
        eventMsg.append("CANCEL;");
        eventMsg.append(program.getProgramClass()+";");
        eventMsg.append(program.getProgramName()+";");
        eventMsg.append(event.getEventName()+";");
        eventMsg.append(event.getEventStatus().equals(EventStatus.ACTIVE)+";");
        eventMsg.append(format.format(new Date()));
        
        topicPublisher.publish(eventMsg.toString());
    }
}
