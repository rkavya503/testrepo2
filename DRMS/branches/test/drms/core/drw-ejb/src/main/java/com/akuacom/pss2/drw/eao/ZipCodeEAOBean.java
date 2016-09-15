/**
 * 
 */
package com.akuacom.pss2.drw.eao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.core.ZipCode;
import com.akuacom.pss2.drw.exception.DuplicatedKeyException;


/**
 * the class ZipCodeEAOBean
 */
@Stateless
public class ZipCodeEAOBean extends BaseEAOBean<ZipCode> implements ZipCodeEAO.L, ZipCodeEAO.R {

	public ZipCodeEAOBean() {
		super(ZipCode.class);
	}

	@Override
	protected void checkUniqueKey(ZipCode entity) throws DuplicatedKeyException {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ZipCode> getZipCodeByLocationType(String locationType) {
		List<ZipCode> zipCodeList=new ArrayList<ZipCode>();
		Query query = em.createNamedQuery("ZipCode.getZipCodeByLocationType");
		query.setParameter("locationType", locationType);
		zipCodeList=query.getResultList();
		
		return zipCodeList;
	}
			

	@SuppressWarnings("unchecked")
	@Override
	public List<Location> getLocationByZipCode(String ZipCode) {
		List<Location> locationList=new ArrayList<Location>();
		Query query = em.createNamedQuery("ZipCode.getLocationByZipCode");
		query.setParameter("zipCode", ZipCode);
		List<ZipCode> result=query.getResultList();
		for (ZipCode code:result) {
			Location location=new Location();
			location.setType(code.getLocationType());
			location.setNumber(code.getLocationNumber());
			locationList.add(location);
		}
		
		return locationList;
	}

	@Override
	public List<ZipCode> getZipCodeByLocation(Location location) {
		return getZipCodeByLocation(location.getType(), location.getNumber());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ZipCode> getZipCodeByLocation(String locationType,
			String locationNumber) {
		List<ZipCode> zipCodeList=new ArrayList<ZipCode>();
		Query query = em.createNamedQuery("ZipCode.getZipCodeByLocation");
		query.setParameter("locationType", locationType);
		query.setParameter("locationNumber", locationNumber);
		
		zipCodeList=query.getResultList();
		
		return zipCodeList;
	}

}
