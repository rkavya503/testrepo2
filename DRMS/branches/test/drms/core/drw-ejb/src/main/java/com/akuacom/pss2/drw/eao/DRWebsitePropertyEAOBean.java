/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import com.akuacom.pss2.drw.core.DRWebsiteProperty;
import com.akuacom.pss2.drw.exception.DuplicatedKeyException;


/**
 * the class DRWebsitePropertyEAOBean
 */
@Stateless
public class DRWebsitePropertyEAOBean extends BaseEAOBean<DRWebsiteProperty> implements DRWebsitePropertyEAO.L, DRWebsitePropertyEAO.R {

	public DRWebsitePropertyEAOBean() {
		super(DRWebsiteProperty.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DRWebsiteProperty findByPropertyName(String propertyName) throws NonUniqueResultException{
		DRWebsiteProperty entity=null;
		Query query = em.createNamedQuery("DRWebsiteProperty.findByPropertyName");
		query.setParameter("propertyName",propertyName);
		List<DRWebsiteProperty> result=query.getResultList();
		if (result!=null) {
			if (result.size()==1) 
				entity=result.get(0);
			else
				throw new NonUniqueResultException(query.toString());
		}
		
		return entity;
	}

	/* (non-Javadoc)
	 * @see com.akuacom.pss2.drw.eao.BaseEAOBean#checkUniqueKey(com.akuacom.pss2.drw.core.AbstractBaseEntity)
	 */
	@Override
	protected void checkUniqueKey(DRWebsiteProperty entity)
			throws DuplicatedKeyException {
		// TODO Auto-generated method stub
		
	}
}
