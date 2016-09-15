/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.filter.AccessFilter.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.filter;

import com.akuacom.pss2.core.CoreConfig;

import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.security.Principal;

/**
 * The Class AccessFilter.
 */
public class AccessFilter implements Filter {
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(AccessFilter.class);
    
    /** The url role map. */
    private Map<String, Collection<String>> urlRoleMap = new HashMap<String, Collection<String>>();

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
        try {
            Properties config = CoreConfig.loadAccessProperties();
            final Enumeration<?> enumeration = config.propertyNames();
            while (enumeration.hasMoreElements()) {
                final String key = (String)enumeration.nextElement();
                final String value = (String)config.get(key);
                if (value != null) {
                    final String[] strings = value.split(",");
                    Collection<String> col = new ArrayList<String>();
                    col.addAll(Arrays.asList(strings));
                    urlRoleMap.put(key, col);
                }
            }
        } catch (IOException e) {
            log.fatal("access filter failed to init: ", e);
        }
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

            // ignore request to context home, i.e. http://hostname/contextPath and http://hostname/contextPath/
            if (requestURI.length() > contextPath.length() + 1) {
                final int i = contextPath.length();
                final int j = requestURI.indexOf("?");
                String action;
                if (j != -1 ) {
                    action = requestURI.substring(i + 1, j);    // extract action string
                } else {
                    action = requestURI.substring(i + 1);
                }

                // match any role that is allowed for given action defined in the url-role map
                final Collection<String> roles = urlRoleMap.get(action);
                if (roles != null) {
                    for (String role : roles) {
                        if (request.isUserInRole(role)) {
                            access = true;
                            break;
                        }
                    }
                }
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
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
