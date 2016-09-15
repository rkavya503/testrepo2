/**
 * 
 */
package com.akuacom.common.exception;

/**
 * Not really a localizer at all, this will simply pass the basic message and
 * exception along assuming they are already localized if needed.
 * 
 * @author roller
 * 
 */
public class DefaultExceptionLocalizer implements ExceptionLocalizer {

	private static final long serialVersionUID = 5167774523914101554L;

	private Exception exception;

	private String message;

	public DefaultExceptionLocalizer(Exception exception) {
		this(null, exception);
	}

	/**
	 * 
	 */
	public DefaultExceptionLocalizer(String message) {
		this(message, null);
	}

	public DefaultExceptionLocalizer(String message, Exception exception) {
		this.message = message;
		this.exception = exception;
	}

	@Override
	public Exception getCause() {
		return exception;
	}

	@Override
	public String getMessage() {

		return this.message;
	}

	static class Producer implements ExceptionLocalizerProducer {

		 Producer(){}
		
		@Override
		public ExceptionLocalizer getDefault(Object criteria, Exception cause) {
			if (criteria instanceof String) {
				return new DefaultExceptionLocalizer((String) criteria,
						cause);
			}
			return null;
		}

		@Override
		public ExceptionLocalizer understands(Object criteria, Exception cause) {
			//never understands anything...only used for last ditch efforts to create a default.
			return null;
		}

	}

}
