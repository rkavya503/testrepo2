/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.filter.AccessFilter.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.security.AggregatorAuthorizationHandler;
import com.akuacom.pss2.core.security.AuthorizationHandler;
import com.akuacom.pss2.core.security.SimpleAuthorizationHandler;

/**
 * The Class AccessFilter.
 */
public class FacAccessFilter implements Filter {
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(FacAccessFilter.class);
    public static final String AUTHORIZATION_HANDLER = "AuthorizationHandler";
    
    /** The url role map. */

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        // empty
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig filterConfig) throws ServletException {
    	 // empty
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // only filter on HTTP request, of course
        if (servletRequest instanceof HttpServletRequest) {
	            boolean access = false;
	
	            final HttpServletRequest request = (HttpServletRequest)servletRequest;
	            final String contextPath = request.getContextPath();
	            final String requestURI = request.getRequestURI();
	
	            //IDOR check
	        	AuthorizationHandler agg = null;
	        	Object obj = ((HttpServletRequest)servletRequest).getSession().getAttribute(AUTHORIZATION_HANDLER); 
	        	//concurrent?
	        	if(obj==null){
	        		if(((HttpServletRequest) servletRequest).getUserPrincipal()!=null){
	        			agg = new AggregatorAuthorizationHandler(new SimpleAuthorizationHandler(),((HttpServletRequest) servletRequest).getUserPrincipal().getName());
		        		 ((HttpServletRequest)servletRequest).getSession().setAttribute(AUTHORIZATION_HANDLER, agg);
	        		}else{
	        			agg = null;
	        		}
	        		
	        	}else{
	        		  agg = (AuthorizationHandler) obj;
	        	}
	            access = agg==null?Boolean.TRUE:agg.isAuthPermission(request);
	            
	            // if no match kick back to home.
	            if (!access) {
	                final Principal userPrincipal = request.getUserPrincipal();
	                String user = null;
	                if (userPrincipal != null) {
	                    user = userPrincipal.getName();
	                }
	                log.debug("Invalid uri request: " + requestURI + " for user: " + user);
	                final HttpServletRequest servlet = (HttpServletRequest) servletRequest;
	                servlet.getSession().invalidate();
	                final HttpServletResponse response = (HttpServletResponse) servletResponse;
	                response.sendRedirect(contextPath);
	                return;
	            }
            }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
