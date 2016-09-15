package com.akuacom.pss2.core.security;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;


public class SimpleAuthorizationHandler implements AuthorizationHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean isAuthPermission(HttpServletRequest request) {
		 final Principal userPrincipal = request.getUserPrincipal();
		 String user = (String) request.getParameter("user");
		 
		 if (request.isUserInRole("Admin")||request.isUserInRole("Operator") 
				 || request.isUserInRole("Readonly") || request.isUserInRole("Dispatcher")) {
             return true;
         }
         String authenticatedUser = null;//the name of the current authenticated user
         if (userPrincipal != null) {
        	 authenticatedUser = userPrincipal.getName();
         }
         if(authenticatedUser==null||!authenticatedUser.equalsIgnoreCase(user)) return false;
         
         return true;
	}

}
