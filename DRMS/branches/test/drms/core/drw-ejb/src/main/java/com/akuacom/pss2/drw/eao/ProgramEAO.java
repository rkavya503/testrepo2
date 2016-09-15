/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.persistence.NonUniqueResultException;

import com.akuacom.pss2.drw.core.Program;

/**
 * the interface ProgramEAO
 */
public interface ProgramEAO extends BaseEAO<Program>{
    @Remote
    public interface R extends ProgramEAO {}
    @Local
    public interface L extends ProgramEAO {}
    
	Program findByUtilityName(String utilityName) throws NonUniqueResultException;

}
