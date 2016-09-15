package com.akuacom.jdbc;

public class BeanCreationException extends RuntimeException {

	private static final long serialVersionUID = -9056942360985953046L;

	public BeanCreationException(String msg, Throwable cause){
		super(msg,cause);
	}
	
	public BeanCreationException(String msg){
		super(msg);
	}
	
	public BeanCreationException(Throwable cause){
		super(cause);
	}
}
