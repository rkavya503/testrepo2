package com.akuacom.drms.performance;

/**
 * Thrown when a failure encountered by Performance Tool
 * @author Sunil
 *
 */
public class PerformanceToolException extends Exception{

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = -3288752056930188496L;

	/**
	 * Creates a new PerformanceToolException
	 * @param message Reason for failure
	 */
	public PerformanceToolException(String message){
		super(message);
	}
	
	/**
	 * Creates a new PerformanceToolException
	 * @param message Reason for failure
	 * @param cause Throwable
	 */
	public PerformanceToolException(String message, Throwable cause){
		super(message, cause);
	}
	
}
