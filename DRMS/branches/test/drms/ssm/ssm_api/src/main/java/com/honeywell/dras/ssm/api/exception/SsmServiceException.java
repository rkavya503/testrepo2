package com.honeywell.dras.ssm.api.exception;

/**
 * 
 * @author E395886(Ram Pandey)
 *
 */

public class SsmServiceException extends Exception{

	
	private static final long serialVersionUID = 1L;
	
	public SsmServiceException(String message){
		super(message);
	}
	
	public SsmServiceException(String message, Exception ex){
		super(message, ex);
	}

}
