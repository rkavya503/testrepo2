/**
 * 
 */
package com.akuacom.pss2.system.property;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;
import com.akuacom.ejb.EntityNotFoundException;

/**
 * Used for CRUD operations pertaining to {@link CoreProperty}
 * 
 * @author roller
 * 
 */
// TODO: change this to local once we get ejb container unit testing working

public interface CorePropertyEAO extends BaseEAO<CoreProperty> {
    @Remote
    public interface R extends CorePropertyEAO {    }
    @Local
    public interface L extends CorePropertyEAO {    }

	/**
	 * Given a property name this will return the expected property.
	 * 
	 * @param propertyName
	 * @return
	 * @throws EntityNotFoundException
	 */
	CoreProperty getByPropertyName(String propertyName)
			throws EntityNotFoundException;


}
