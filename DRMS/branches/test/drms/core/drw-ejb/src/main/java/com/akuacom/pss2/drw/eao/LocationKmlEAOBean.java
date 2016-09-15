package com.akuacom.pss2.drw.eao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.akuacom.pss2.drw.core.LocationKmlEntry;
import com.akuacom.pss2.drw.exception.DuplicatedKeyException;

@Stateless
public class LocationKmlEAOBean extends BaseEAOBean<LocationKmlEntry> implements LocationKmlEAO.R, LocationKmlEAO.L {

	public LocationKmlEAOBean() {
		super(LocationKmlEntry.class);
	}

	@Override
	public List<LocationKmlEntry> getLocationKmlByNum(String num) {
		Query query = em.createNamedQuery("LocationKmlEntry.findByLocationNum");
		query.setParameter("locationNum", num);
		return query.getResultList();
	}

	@Override
	protected void checkUniqueKey(LocationKmlEntry entity)
			throws DuplicatedKeyException {
	}



}
