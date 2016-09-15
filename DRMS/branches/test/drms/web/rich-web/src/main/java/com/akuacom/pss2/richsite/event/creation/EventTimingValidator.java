package com.akuacom.pss2.richsite.event.creation;

import java.util.List;

import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidator;
import com.akuacom.pss2.core.ValidatorFactory;
import com.akuacom.pss2.event.Event;

public class EventTimingValidator extends Validator {
	
	private static final long serialVersionUID = -5212942657386870028L;

	@Override
	public MSG validate(AbstractEventCreation model) {
		try {
			
			AbstractEventCreation creatonModel = (AbstractEventCreation) model;
			Event event = creatonModel.converToEvent(false);
			
			UploadFile uploadFile=creatonModel.getUploadFile();
			if (uploadFile.isAvailable()) {
				event = uploadFile.parseUploadFile(event);
				List<String> errors=uploadFile.getParser().getErrors();
				if (errors.size()>0) {
					StringBuilder builder=new StringBuilder();
					for (String error:errors) {
						if (builder.length()!=0 && !error.isEmpty())
							builder.append(", ");
						builder.append(error);
					}
					return new MSG(Validator.MSG_ERROR, builder.toString());
				}
			}
			
			ProgramValidator programValidator = ValidatorFactory.getProgramValidator(creatonModel.getProgram());            
			programValidator.validateEvent(event);
			if(event.getWarnings()!=null)
				return new MSG(Validator.MSG_WARN,"Warnings:"+ErrorUtil.getWarningMessage(event.getWarnings()));
		} catch (ProgramValidationException e) {
			final String s = ErrorUtil.getErrorMessage(e);
			return new MSG(Validator.MSG_ERROR,s);
		}
		return null;
	}
}
