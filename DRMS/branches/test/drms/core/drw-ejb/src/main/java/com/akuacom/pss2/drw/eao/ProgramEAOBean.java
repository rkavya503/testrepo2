/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.exception.DuplicatedKeyException;


/**
 * the class ProgramEAOBean
 */
@Stateless
public class ProgramEAOBean extends BaseEAOBean<Program> implements ProgramEAO.L, ProgramEAO.R {

	public ProgramEAOBean() {
		super(Program.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Program findByUtilityName(String utilityName) throws NonUniqueResultException{
		Program program=null;
		Query query = em.createNamedQuery("Program.findByUtilityName");
		List<Program> result=query.getResultList();
		if (result!=null) {
			if (result.size()==1) 
				program=result.get(0);
			else
				throw new NonUniqueResultException(query.toString());
		}
		
		return program;
	}
	
	@Override
	protected void checkUniqueKey(Program entity) throws DuplicatedKeyException {
		
	}

}
