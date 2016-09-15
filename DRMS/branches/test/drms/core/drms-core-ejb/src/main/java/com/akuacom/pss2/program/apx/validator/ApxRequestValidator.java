package com.akuacom.pss2.program.apx.validator;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.program.apx.parser.APXXmlParser;
import com.akuacom.pss2.program.sceftp.CreationFailureException;


public interface ApxRequestValidator {
	@Remote
    public interface R extends ApxRequestValidator {   }
    @Local
    public interface L extends ApxRequestValidator {   }
    
    public void validateApxRequest(APXXmlParser xmlParser) throws CreationFailureException, ProgramValidationException;

}
