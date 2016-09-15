package com.akuacom.pss2.richsite.program.configure.signal;

import java.util.List;
import com.akuacom.pss2.core.ValidationException;
/**
 * 
 * Filename:    SeasonConfigureValidator.java 
 * Description: 
 * Copyright:   Copyright (c)2010
 * Company:     
 * @version:    
 * Create at:   Feb 17, 2011 4:59:19 PM 
 * 
 */
public class SignalConfigureValidator {
	
	/** signal empty validation error message*/
	private static final String VALIDATION_ERROR_SIGNAL_EMPTY="Signals are required in program fields.";

	/**
	 * function for validate signal
	 * @param model SignalConfigureDataModel instance
	 * @throws ValidationException
	 */
	public static void signalValidation(SignalConfigureDataModel model) throws ValidationException{
		if(model!=null){
//			List<SelectItem> programSignalSelectItems = model.getProgramSignalSelectItems();
			List<SignalDefDataModel> signalDefDataModelList = model.getSignalDefDataModelList();
			
			if(signalDefDataModelList==null||signalDefDataModelList.size()<=0){
				throw new ValidationException(VALIDATION_ERROR_SIGNAL_EMPTY);
			}
			int var = 0;
			for(int i=0;i<signalDefDataModelList.size();i++){
				if(signalDefDataModelList.get(i).isSelect()){
					var ++;
				}
			}
			if(var==0){
				throw new ValidationException(VALIDATION_ERROR_SIGNAL_EMPTY);	
			}
		}
	}
}
