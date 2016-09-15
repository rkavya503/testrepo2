/**
 * 
 */
package com.akuacom.ejb;

import javax.persistence.EntityExistsException;

import com.akuacom.common.exception.BaseException;
import com.akuacom.common.resource.DynamicResourceBundleMessage;

/**
 * Indicates the key provided is duplicated when attempting to create or update
 * an entity. Typically this is a wrapper for {@link EntityExistsException}, but
 * provides our messaging. If you know the identifier create a custom exception
 * and provide the duplicate id. NoSuchEntityException does not provide this
 * level of detail.
 * 
 * @author roller
 * 
 */
public class DuplicateKeyException extends BaseException {

	private static final long serialVersionUID = 5060176943673344821L;

	/**
	 * 
	 */
	public DuplicateKeyException() {
		this(null);
	}

	public DuplicateKeyException(String uuid) {
		super(new DynamicResourceBundleMessage(
				EJBExceptionMessage.DUPLICATE_KEY).addParameter(
				EJBExceptionMessage.Param.IDENTIFIER, uuid));
	}

}
