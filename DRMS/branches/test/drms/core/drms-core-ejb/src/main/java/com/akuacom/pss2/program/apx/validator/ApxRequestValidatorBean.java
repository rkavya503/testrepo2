package com.akuacom.pss2.program.apx.validator;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventEAO;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.program.apx.parser.APXXmlParser;
import com.akuacom.pss2.program.sceftp.CreationFailureException;

@Stateless
public class ApxRequestValidatorBean implements ApxRequestValidator.L,ApxRequestValidator.R{
	@EJB
	EventEAO.L eventEAO;
	@EJB
	ProgramManager.L programManager;
	
	public void validateApxRequest(APXXmlParser xmlParser)throws CreationFailureException, ProgramValidationException{
		checkForRequestXmlParsingError(xmlParser);
		checkForDuplicateEvent(xmlParser);
		validateProgram(xmlParser);
	}
	private void checkForRequestXmlParsingError(APXXmlParser xmlParser) throws CreationFailureException{
		if(0>=xmlParser.getMessages().size()){
			return;
		}
		StringBuilder content=new StringBuilder();
		for (ProgramValidationMessage msg:xmlParser.getMessages()) {
			content.append(msg.getParameterName());
			content.append(": ");
			content.append(msg.getDescription());
			content.append("\n");
		}
    	throw new CreationFailureException(content.toString());
	}
	private void checkForDuplicateEvent(APXXmlParser xmlParser) throws CreationFailureException{
		String eventName = xmlParser.getEventName();
		if(!doesEventAlreadyExist(eventName)){
			return;
		}
		StringBuilder content=new StringBuilder();
		content.append("EventID");
		content.append(": ");
		content.append("Duplicate event "+xmlParser.getEventName()+" exists");
    	throw new CreationFailureException(content.toString());
	}
	private void validateProgram(APXXmlParser xmlParser) throws CreationFailureException, ProgramValidationException{
		String utilityProgramName = xmlParser.getProgramName();
		Program program=programManager.getProgramFromUtilityProgramName(utilityProgramName);
		 if(null == program){
			 StringBuilder content=new StringBuilder();
			 content.append("Program ");
			 content.append(": ");
			 content.append("Program does not exist with utility program name :");
			 content.append(utilityProgramName);
			 throw new CreationFailureException(content.toString());
		 }
		 validateProgramWithEvent(xmlParser,program);
	}
	private void validateProgramWithEvent(APXXmlParser xmlParser,Program program) throws ProgramValidationException{
		Event event=new Event();
		event.setProgramName(program.getProgramName());
		event.setEventName(xmlParser.getEventName());
		event.setStartTime(xmlParser.getEventStartTime());
		event.setEndTime(xmlParser.getEventEndTime());
		event.setIssuedTime(new Date());
		event.setReceivedTime(new Date());
		event.setLocations(xmlParser.getLocations());

		ProgramValidator programValidator = ValidatorFactory.getProgramValidator(program);            
		programValidator.validateEvent(event);
		
	}
	private boolean doesEventAlreadyExist(String eventName){
	    	boolean exist=false;
			Event event = eventEAO.findEventOnlyByEventName(eventName);
			exist=(event!=null);
			return exist;
	}

}
