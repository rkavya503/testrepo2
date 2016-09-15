package com.honeywell.dras.vtn.push.service;

public class VtnPushServiceException extends Exception {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -5793699277557480837L;

	public VtnPushServiceException(String message){
		super(message);
	}
	
	public VtnPushServiceException(String message, Exception ex){
		super(message, ex);
	}

}
