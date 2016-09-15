/**
 * 
 */
package com.akuacom.common.resource;

/**
 * Base class used to reference keys found in resources bundles (instead of
 * using strings).
 * 
 * It is encouraged to declare an Enum that implements this interface. Notice
 * only the toString() method is used to represent the key which goes inline
 * with the eunum capbilities.
 * 
 * public enum MyKeys implements ResourceBundleMessageKey{
 * ERROR_DOING_SOMETHING, INFO_ABOUT_SOMETHING_ELSE, ETC}
 * 
 * @author roller
 * 
 */
public interface ResourceBundleMessage {

	/**
	 * Provides the key in string form.
	 * 
	 * @return the key in string form (hopefully from an enum)
	 */
	public String toString();

	/**
	 * The optional message provided by the Java class instead of the properties
	 * file. This message should appear exactly as it does in the properties
	 * file. This is available to help developers build messages while they
	 * developer without having to reference obscure properties files.
	 * 
	 * The message should be formatted using key/value pairs per the
	 * instructions on {@link KeyedMessageFormat}.
	 * 
	 * @return
	 */
	public String getValue();

	/**
	 * The optional parameter keys related to a resource bundle message. If
	 * there are no paremeters then an empty array should be provided.
	 * 
	 * @return the parameters related to the message.
	 */
	public ResourceBundleMessageParameterKey[] getParameterKeys();
}
