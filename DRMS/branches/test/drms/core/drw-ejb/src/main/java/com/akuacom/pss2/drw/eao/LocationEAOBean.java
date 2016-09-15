/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.exception.DuplicatedKeyException;


/**
 * the class ProgramEAOBean
 */
@Stateless
public class LocationEAOBean extends BaseEAOBean<Location> implements LocationEAO.L, LocationEAO.R {

	public LocationEAOBean() {
		super(Location.class);
	}

	@Override
	protected void checkUniqueKey(Location entity) throws DuplicatedKeyException {
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Location> getLocationByType(String type){
		Query query=em.createNamedQuery("Location.findByType");
		query.setParameter("type", type);
		return query.getResultList();
	}

	@Override
	public Location getLocationByTypeAndNumber(String type, String number){
		Query query=em.createNamedQuery("Location.findByTypeAndNumber");
		query.setParameter("type", type);
		query.setParameter("number", number);
		List<Location> result=query.getResultList();
		if (result!=null && result.size()>0)
			return result.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Location> getLocation(String type, String number, String name){
		StringBuilder builder=new StringBuilder();
		builder.append("select e from Location e where 1=1 ");
		if (type!=null && type.trim().length()!=0)
			builder.append(" and e.type = :type");
		if (number!=null && number.trim().length()!=0)
			builder.append(" and e.number like :number");
		if (name!=null && name.trim().length()!=0)
			builder.append(" and e.name like :name");
		
		Query query=em.createQuery(builder.toString());
		query.setParameter("type", type);
		query.setParameter("number", number+"%");
		query.setParameter("name", name+"%");
		return query.getResultList();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Location> getLocation(List<String> type, String number,
			String name) {
		StringBuilder builder=new StringBuilder();
		builder.append("select e from Location e where 1=1 ");
		if (type!=null && !type.isEmpty())
			builder.append(" and e.type in (:type)");
		if (number!=null && number.trim().length()!=0)
			builder.append(" and e.number like :number");
		if (name!=null && name.trim().length()!=0)
			builder.append(" and e.name like :name");
		
		Query query=em.createQuery(builder.toString());
		if (type!=null && !type.isEmpty())
			query.setParameter("type", type);
		if (number!=null && number.trim().length()!=0)
			query.setParameter("number", number+"%");
		if (name!=null && name.trim().length()!=0)
			query.setParameter("name", name+"%");
		
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getBlock(String product, boolean status) {
		Query query=em.createNamedQuery("Block.getByProduct");
		query.setParameter("productName", product);
		query.setParameter("status", status);
		return query.getResultList();
	}
}
