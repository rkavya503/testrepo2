package com.akuacom.jdbc;

public class SQLBuilderException extends Exception {

	private static final long serialVersionUID = -4791276436360039890L;

	public SQLBuilderException() {
		super();
	}

	public SQLBuilderException(String message) {
		super(message);
	}

	public SQLBuilderException(String message, Throwable cause) {
		super(message, cause);
	}
}
