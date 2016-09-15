package com.akuacom.pss2.core.security.proxy;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizationCollection {

	Set getPermittedResource(HttpServletRequest request);
	
	void refresh();
}
