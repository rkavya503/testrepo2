/**
 * 
 */
package com.akuacom.ejb;

import javax.ejb.ApplicationException;
import javax.ejb.NoSuchEntityException;
import javax.persistence.NoResultException;

import com.akuacom.common.exception.BaseException;
import com.akuacom.common.resource.DynamicResourceBundleMessage;
import com.akuacom.ejb.EJBExceptionMessage.Param;

/**
 * Indicates an Entity was not found in the system when given an identifier.
 * 
 * @author roller
 * 
 */
@ApplicationException
public class EntityNotFoundException extends BaseException {

	private static final long serialVersionUID = -7439480750121243558L;

	public EntityNotFoundException(Object identifer) {
		this(identifer, (Exception)null);
	}

	protected EntityNotFoundException(String identifierKey, Object identifier, Exception cause) {
		super(new DynamicResourceBundleMessage(
				EJBExceptionMessage.ENTITY_NOT_FOUND).addParameter(
				EJBExceptionMessage.Param.IDENTIFIER, identifier).addParameter(Param.IDENTIFIER_KEY,identifierKey), cause);

	}

	public EntityNotFoundException(Object identifer, Exception cause) {
		this("id",identifer,(Exception)cause);
	}
	
	public EntityNotFoundException(Object identifer, NoSuchEntityException cause) {
		this(identifer,(Exception)cause);
	}
	/**
	 * Similar to the other constuctors, but customized for the common
	 * NoResultException to encourage people to translate that runtime exception
	 * into this Application Exception.
	 * 
	 * @param identifer
	 * @param e
	 */
	public EntityNotFoundException(Object identifer, NoResultException cause) {
		this(identifer, (Exception)cause);
	}

	public EntityNotFoundException(String identifierKey, Object identifier,
			NoResultException cause) {
		this(identifierKey,identifier,(Exception)cause);
	}

}
