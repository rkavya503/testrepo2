package com.akuacom.pss2.core.security;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizationHandler extends Serializable {
	
	boolean isAuthPermission(HttpServletRequest request); 
	
}
