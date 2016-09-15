/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.SlowMethodInterceptor.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import com.akuacom.pss2.util.Environment;

import org.apache.log4j.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * An EJB method interceptor used to report on bean methods that are taking
 * longer than a threshold amount of time to execute.
 */
public class SlowMethodInterceptor {
    
    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(SlowMethodInterceptor.class);

    /** The threshold. */
    private final long threshold = Environment.getSlowLogThreshold();

    /**
     * Slow method interceptor which wraps each EJB method invocation to measure
     * the amount of time the method took to execute.  Times greater than a
     * threshold are logged.
     * 
     * @param ctx the ctx
     * 
     * @return the object
     * 
     * @throws Exception the exception
     */
    @AroundInvoke
    public Object slowMethodInterceptor(InvocationContext ctx) throws java.lang.Exception {
        long startTime = System.currentTimeMillis();
        try {
            return ctx.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            if (duration > threshold) {
            	logger.debug(ctx.getMethod() + " took " + duration + " (ms)");
            }
        }
    }
}
