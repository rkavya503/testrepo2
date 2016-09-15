package com.akuacom.common.exception;

/**
 * Provides the methods to be used by {@link ExceptionLocalizerFactory} for each
 * {@link ExceptionLocalizer} to determine if they understand the criteria
 * given. This is used to produce the actual ExceptionLocalizer instances and is
 * typically an inner class bundled with the ExceptionLoclizers it produces.
 * 
 * @author roller
 * 
 */
public interface ExceptionLocalizerProducer {

	/**
	 * Each ExceptionLocalizer has the opportunity to indicate their
	 * understanding of the given criteria. If the registered
	 * ParticipatingExceptionLocalizer recognizes the criteria it will produce
	 * an instance of the ExceptionLocalizer with the criteria provided.
	 * 
	 * @param criteria
	 *            any individual piece of information that will indicate a
	 *            particular localizer is capable of handling
	 * @param cause
	 *            An optional exception that is unlikely to be used to determine
	 *            the Localizer, but needed for construction of an instance.
	 * @return the localizer containing the criteria and cause that will provide
	 *         the message.
	 */
	public ExceptionLocalizer understands(Object criteria, Exception cause);

	/**
	 * Similar to understands, this will only return a
	 * {@link DefaultExceptionLocalizer} if it understands the given object.
	 * 
	 * The default is the last ditch effort to produce something of value. Use
	 * of this essentially means the system has not been initialized completely
	 * so resource bundles and other resources are not
	 * {@link ExceptionLocalizerFactory#registerProducer(ExceptionLocalizerProducer)
	 * registered} properly.
	 * 
	 * @param criteria
	 * @param cause
	 * @return the default localizer
	 */
	public ExceptionLocalizer getDefault(Object criteria, Exception cause);

}
