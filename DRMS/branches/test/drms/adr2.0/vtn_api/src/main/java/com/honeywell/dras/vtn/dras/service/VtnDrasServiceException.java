package com.honeywell.dras.vtn.dras.service;

public class VtnDrasServiceException extends Exception{
	
	/**
	 * Serial 
	 */
	private static final long serialVersionUID = 5598548836768910606L;

	public VtnDrasServiceException(String message){
		super(message);
	}
	
	public VtnDrasServiceException(String message, Exception ex){
		super(message, ex);
	}

}
