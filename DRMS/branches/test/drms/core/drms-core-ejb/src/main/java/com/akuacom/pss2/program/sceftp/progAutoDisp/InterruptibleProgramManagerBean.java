/**
 * 
 */
package com.akuacom.pss2.program.sceftp.progAutoDisp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import com.akuacom.pss2.program.sceftp.SCEFTPConfig;
import com.akuacom.pss2.topic.TopicPublisher;

@Stateless
public class InterruptibleProgramManagerBean implements InterruptibleProgramManager.L, InterruptibleProgramManager.R {
	@EJB
    protected TopicPublisher.L topicPublisher;
    private static final Logger log = Logger.getLogger(InterruptibleProgramManager.class);
//    private static final String API_PROGRAM="API";
//    private static final String BIP_PROGRAM="TOU-BIP";
//    private static final String SDP_PROGRAM="SDP";
//    private static final String SDP_PRODUCT_SDP="SDP";
//    private static final String SDP_PRODUCT_APS="APS";
//    private static final String SDP_PRODUCT_APSE="APS-E";
//    private static final String LOCATION_TYPE_ALL="ALL";
//    private static final String LOCATION_TYPE_SLAP="SLAP";
//    private static final String LOCATION_TYPE_ABANK="ABANK";
//    private static final String LOCATION_TYPE_SUBSTATION="SUBSTATION";
    private static final String ACTION_ACTIVATED="ACTIVATED";
    private static final String ACTION_CONTINUED="CONTINUED";
    private static final String ACTION_TERMINATED="TERMINATED";
    private static final String ACTION_SCHEDULED="SCHEDULED";
    public static final String ACTION_DELETE="DELETE";
    @Override
    public void dispatch(String fileName,String fileContext,SCEFTPConfig config){
    	log.info("Interruptible Program Auto Dispatch function invoked.");
    	//1 validate file name & file context
    		boolean validateFileNameFlag = validateFileName(fileName);
    		boolean validateFileContextFlag = validateFileContext(fileContext);
    	//2 validate passed, auto dispatch events
    	//	validate failed, handle validation error
    		if(validateFileNameFlag&&validateFileContextFlag){
    			processEvent(fileName,fileContext,config);
    		}else{
    			handleValidationError();
    			log.info("Interruptible Program Auto Dispatch function validation catch error and handle it.");
    		}
    		log.info("Interruptible Program Auto Dispatch function finished.");
    }
    
    private boolean validateFileName(String fileName){
    	boolean result = true;
    	return result;
    }
    private boolean validateFileContext(String fileContext){
    	boolean result = true;
    	return result;
    }
    private void handleValidationError(){
    	
    }
    /**
     * Function for process Event
     * 1	determine which program: API/BIP/SDP
     * 2	determine which action: A/C/T/S
     * 3	
     * @param fileName
     * @param fileContext
     */
    private void processEvent(String fileName,String fileContext,SCEFTPConfig config){
    	//get action name 
    	String actionName = ProgAutoDispUtil.getActionNameByFileName(fileName);
    	//get program name
    	String program = ProgAutoDispUtil.getProgramByContext(fileContext);
    	if(ACTION_ACTIVATED.equalsIgnoreCase(actionName)){
    		processACTIVATED(fileContext,program,config);
    		log.info("Interruptible Program Auto Dispatch ACTIVATED event.");
    	}else if(ACTION_CONTINUED.equalsIgnoreCase(actionName)){
    		processCONTINUED(fileContext,program,config);
    		log.info("Interruptible Program Auto Dispatch CONTINUED event.");
    	}else if(ACTION_TERMINATED.equalsIgnoreCase(actionName)){
    		processTERMINATED(fileContext,program,config);
    		log.info("Interruptible Program Auto Dispatch TERMINATED event.");
    	}else if(ACTION_SCHEDULED.equalsIgnoreCase(actionName)){
    		processSCHEDULED(fileContext,program,config);
    		log.info("Interruptible Program Auto Dispatch SCHEDULED event.");
    	}
    }
    /**
     * Build message
     * 0	AUTO_DISPATCH_EVENT
     * 1	actionType:"ACTIVATED";"CONTINUED";"TERMINATED";"SCHEDULED";
     * 2	program:API;TOU-BIP;SDP;
     * 3	dispatch type:ALL;ABANK;SLAP;SUBSTATION;
     * 4	location
     * 5	product:only for SDP:SDP;APS;APS-E;
     * 6	issue time
     * 7	start time
     * 8	end time
     * @param fileContext
     * @param actionType
     * @return
     */
    private String buildMessage(String fileContext,String actionType,SCEFTPConfig config){
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder eventMsg=new StringBuilder();
        eventMsg.append("AUTO_DISPATCH_EVENT;");
        eventMsg.append(actionType+";");
        eventMsg.append(ProgAutoDispUtil.getProgramByContext(fileContext)+";");
        eventMsg.append(ProgAutoDispUtil.getDispatchTypeByContext(fileContext)+";");
//        eventMsg.append(ProgAutoDispUtil.getLocationByContext(fileContext)+";");
        eventMsg.append(ProgAutoDispUtil.buildLocationName(fileContext)+";");
        eventMsg.append(ProgAutoDispUtil.getProductByContext(fileContext)+";");
        eventMsg.append(String.valueOf(config.getDelayInterval())+";");
        Date issueDate = ProgAutoDispUtil.getIssueTimeByContext(fileContext);
        if(issueDate!=null){
        	eventMsg.append(format.format(issueDate)+";");
        }else{
        	eventMsg.append(";");
        }
        Date startDate = ProgAutoDispUtil.getStartDateByContext(fileContext);
        if(startDate!=null){
        	eventMsg.append(format.format(startDate)+";");
        }else{
        	eventMsg.append(";");
        }
        Date endDate = ProgAutoDispUtil.getEndDateByContext(fileContext);
        if(endDate!=null){
        	eventMsg.append(format.format(endDate)+";");
        }else{
        	eventMsg.append(";");
        }
        
        return eventMsg.toString();
    }
    /**
     * This function will create an event in DR Admin application with the Event Issue Date/Time, 
     * Event Start Date/Time and Event Estimated End Date/Time with data from the record: 
     * Event Issue Date/Time, Event Start Date/Time and Event End Date/Time.
	 * It is expected that the Event End Date / Time from the file will be empty and the event will be created without an end time.
	 *  If an Event End Date / Time is available, then it will be utilized as an Event Estimated End Date / Time.
     * @param fileContext
     * @param program
     */
    private void processACTIVATED(String fileContext,String program,SCEFTPConfig config){        
        topicPublisher.publish(buildMessage(fileContext,ACTION_ACTIVATED,config));
    }
    private void processCONTINUED(String fileContext,String program,SCEFTPConfig config){
    	topicPublisher.publish(buildMessage(fileContext,ACTION_CONTINUED,config));
    }
    /**
     * When a TERMINATED record is received, the first thing that the system does is to match any active events. 
     * The match algorithm is as follows:
	 * Match all events that have the same Event Start Date / Time and the same location type and location number. 
	 * For those events, set the Event Actual End Date / Time to the Event End Date / Time in the TERMINATED record.
     * @param fileContext
     * @param program
     */
    private void processTERMINATED(String fileContext,String program,SCEFTPConfig config){
    	topicPublisher.publish(buildMessage(fileContext,ACTION_TERMINATED,config));
    }
    private void processSCHEDULED(String fileContext,String program,SCEFTPConfig config){
    	topicPublisher.publish(buildMessage(fileContext,ACTION_SCHEDULED,config));
    }
    private String buildMessage(String actionType,String programName, String product,String dispatchType, List<String> locationNumbers, Date issueTime,Date startTime, Date endTime){
    	if(programName.contains("BIP")){
    		programName ="BIP";
    	}
    	if(product.contains("BIP")){
    		product ="BIP2013";
    	}
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	StringBuilder eventMsg=new StringBuilder();
        eventMsg.append("AUTO_DISPATCH_EVENT;");
        eventMsg.append(actionType+";");
        eventMsg.append(programName+";");
        eventMsg.append(dispatchType+";");
        String locationNumber="";
        for(String s:locationNumbers){
        	locationNumber=locationNumber+"-"+s;
        }
        if(locationNumber.startsWith("-")){
        	locationNumber = locationNumber.substring(1);
        }
        eventMsg.append(locationNumber+";");
        eventMsg.append(product+";");
        eventMsg.append("0;");
        if(issueTime!=null){
        	eventMsg.append(format.format(issueTime)+";");
        }else{
        	eventMsg.append(";");
        }
        if(startTime!=null){
        	eventMsg.append(format.format(startTime)+";");
        }else{
        	eventMsg.append(";");
        }
        if(endTime!=null){
        	eventMsg.append(format.format(endTime)+";");
        }else{
        	eventMsg.append(";");
        }
        
        return eventMsg.toString();
    }
	@Override
	public void createEvent(String programName, String product,String dispatchType, List<String> locationNumbers, Date issueTime,Date startTime, Date endTime) {

        String result = buildMessage(ACTION_ACTIVATED,programName,product,dispatchType,locationNumbers,issueTime,startTime,endTime);
		topicPublisher.publish(result);
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.program.sceftp.progAutoDisp.InterruptibleProgramManager#deleteEvent(java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.Date)
	 */
	@Override
	public void deleteEvent(String programName, String product,String dispatchType, List<String> locationNumbers, Date startTime) {
        String result = buildMessage(ACTION_DELETE,programName,product,dispatchType,locationNumbers,null,startTime,null);
		topicPublisher.publish(result);
		
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.program.sceftp.progAutoDisp.InterruptibleProgramManager#endEvent(java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.Date)
	 */
	@Override
	public void endEvent(String programName, String product,String dispatchType, List<String> locationNumbers, Date startTime,Date endTime) {
		String result = buildMessage(ACTION_TERMINATED,programName,product,dispatchType,locationNumbers,null,startTime,endTime);
		topicPublisher.publish(result);
	}

	@Override
	public void dispatchEvent(String dispatchType,String programName, String product,String locationdispatchType, List<String> locationNumbers, Date issueTime,Date startTime, Date endTime) {
		if(ACTION_ACTIVATED.equalsIgnoreCase(dispatchType)){
			createEvent(programName,product,dispatchType,locationNumbers,issueTime,startTime,endTime);
    		log.info("Interruptible Program Auto Dispatch ACTIVATED event.");
    	}else if(ACTION_DELETE.equalsIgnoreCase(dispatchType)){
    		deleteEvent(programName,product,dispatchType,locationNumbers,startTime);
    		log.info("Interruptible Program Auto Dispatch DELETE event.");
    	}else if(ACTION_TERMINATED.equalsIgnoreCase(dispatchType)){
    		endEvent(programName,product,dispatchType,locationNumbers,startTime,endTime);
    		log.info("Interruptible Program Auto Dispatch TERMINATED event.");
    	}else if(ACTION_SCHEDULED.equalsIgnoreCase(dispatchType)){
    		createEvent(programName,product,dispatchType,locationNumbers,issueTime,startTime,endTime);
    		log.info("Interruptible Program Auto Dispatch SCHEDULED event.");
    	}
	}


}
