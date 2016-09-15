/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.persistence.NonUniqueResultException;

import com.akuacom.pss2.drw.core.DRWebsiteProperty;

/**
 * the interface DRWebsitePropertyEAO
 */
public interface DRWebsitePropertyEAO extends BaseEAO<DRWebsiteProperty>{
    @Remote
    public interface R extends DRWebsitePropertyEAO {}
    @Local
    public interface L extends DRWebsitePropertyEAO {}
    
    DRWebsiteProperty findByPropertyName(String propertyName) throws NonUniqueResultException;

}
