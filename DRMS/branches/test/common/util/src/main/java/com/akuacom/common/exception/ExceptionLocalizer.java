/**
 * 
 */
package com.akuacom.common.exception;

import java.io.Serializable;

/**
 * Used to provide Localized messages transported by exceptions through the
 * business tier into the presentation tier.
 * 
 * This is intented to be used by an Exception to delegate it's methods to the
 * similar methods in here for common handling of messages and exceptions.
 * 
 * All exception localizers should be registered with
 * {@link ExceptionLocalizerFactory} which is also used to serve up the correct
 * localizer when given the proper information (such as resource bundle keys,
 * etc).
 * 
 * @author roller
 * 
 */
public interface ExceptionLocalizer extends Serializable {

	/**
	 * The localized message if possible, or the default locale message if
	 * locale is not yet available.
	 * 
	 * @return
	 */
	public String getMessage();

	/**
	 * The other exception that caused this exception.
	 * 
	 * @return
	 */
	public Exception getCause();
}
