package com.akuacom.drms.test;

import java.net.URL;

/**
 * Thrown when we fail to Unmarshal
 * @author Sunil
 *
 */
public class UnmarshalException extends Exception{

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = -2768915115866601076L;
	/**
	 * URL
	 */
	private URL fileUrl = null;

	
	/**
	 * Creates a new UnmarshalException
	 * @param message Reason for failure
	 * @param  URL of the file being unmarshalled
	 */
	public UnmarshalException(String message, URL url){
		super(message);
		fileUrl = url;
		
	}
	
	/**
	 * Creates a new UnmarshalException
	 * @param message Reason for failure
	 * @param cause Throwable
	 * @param  URL of the file being unmarshalled
	 */
	public UnmarshalException(String message, Throwable cause, URL url){
		super(message, cause);
		fileUrl = url;
	}
	
	
	/**
	 * Returns the URL
	 * @return URL
	 */
	public URL getURL(){
		return fileUrl;
	}
}
