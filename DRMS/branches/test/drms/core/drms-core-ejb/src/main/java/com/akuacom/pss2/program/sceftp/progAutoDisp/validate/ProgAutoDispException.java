package com.akuacom.pss2.program.sceftp.progAutoDisp.validate;

public class ProgAutoDispException extends Exception {

	private static final long serialVersionUID = 1L;
	private String subject = "";
	private String message = "";
	public ProgAutoDispException(){
		super();
	}
	public ProgAutoDispException(String message){
		super();
		this.message=message;
	}
	public ProgAutoDispException(String subject,String message){
		super();
		this.subject=subject;
		this.message=message;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
