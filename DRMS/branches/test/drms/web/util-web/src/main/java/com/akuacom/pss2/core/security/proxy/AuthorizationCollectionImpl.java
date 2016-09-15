package com.akuacom.pss2.core.security.proxy;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.data.usage.UsageDataManager;

public class AuthorizationCollectionImpl implements AuthorizationCollection {
	private Set innerPermittedResource = null;
	private String authenticatedUser;
	
	public AuthorizationCollectionImpl(String authenticatedUser){
		this.authenticatedUser = authenticatedUser;
		refresh();
	}

	@Override
	public Set getPermittedResource(HttpServletRequest request) {
		return innerPermittedResource;
	}

	@Override
	public synchronized void refresh() {
		//UsageDataManager
		final UsageDataManager manager = EJB3Factory.getBean(UsageDataManager.class);
		this.innerPermittedResource = new HashSet(manager.findAllParticipantNames(authenticatedUser, new Date()));
	}

}
