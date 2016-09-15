/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.EJB3Factory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.ejb.client;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * This is a hack due to JBoss 4.2.x doesn't support @EJB injection outside the
 * ejb container. Doh!
 * 
 * TODO: Make this generic so it is useful to all apps, not just pss2.
 * 
 * @author Dichen Mao
 */
@SuppressWarnings({"unchecked"})
public class EJB3Factory {
    
    public static final String LOCAL_SUFFIX = "/local";

    public static final String REMOTE_SUFFIX = "/remote";

    private static final String BEAN_SUFFIX = "Bean";

    /** The log. */
    private static Logger log = Logger.getLogger(EJB3Factory.class);
    
    /** The Constant LOCAL. */
    private static final String LOCAL = "Local";

    private static final String PROGRAM_EJB = "ProgramEJB";
    
    public static final String EARNAME="pss2";

    /**
     * Gets the bean.
     * 
     * @param t the t
     * 
     * @return the bean
     */
    public static <T> T getBean(Class<T> t) {
        return getBean(t, "pss2");
    }

    public static <T> T getLocalBean(Class<T> t) {
        return getLocalBean(t, "pss2");
    }

    /**
     * Gets the bean.
     * 
     * @param t the t
     * @param earName the ear name
     * 
     * @return the bean
     */
    public static <T> T getBean(Class<T> t, String earName) {
        try {
            return getBean(t, earName, new InitialContext());
        } catch (NamingException e) {
            log.error("Failed to get EJB remote interface for: " + t.getSimpleName() + " with: " + earName, e);
        }
        return null;
    }

    public static <T> T getLocalBean(Class<T> t, String earName) {
        try {
            return getLocalBean(t, earName, new InitialContext());
        } catch (NamingException e) {
            log.error("Failed to get EJB local interface for: " + t.getSimpleName() + " with: " + earName, e);
        }
        return null;
    }

    /**
     * Gets the bean.
     * 
     * @param t the t
     * @param earName the ear name
     * @param ctx the ctx
     * 
     * @return the bean
     */
    public static <T> T getBean(Class<T> t, String earName, Context ctx) {
        final String name = t.getSimpleName();
        String s;
        if (name.endsWith(LOCAL)) {
            s = earName + "/" + name.substring(0, name.length() - LOCAL.length()) + BEAN_SUFFIX + LOCAL_SUFFIX;
        } else if (name.endsWith(PROGRAM_EJB)) {
            s = earName + "/" + name + BEAN_SUFFIX + REMOTE_SUFFIX;
        } else if (name.endsWith(BEAN_SUFFIX)){
            s = earName + "/" + name + REMOTE_SUFFIX;
        }else {
        
            s = earName + "/" + name + BEAN_SUFFIX + REMOTE_SUFFIX;
        }
        try {
            T ret = (T)ctx.lookup(s);
            return ret;
            
        } catch (NamingException e) {
            log.error("Failed to get EJB remote interface for: " + t.getSimpleName() + " at " + s);
            s = earName + "/" + name + BEAN_SUFFIX + LOCAL_SUFFIX;
            try {
                T ret = (T)ctx.lookup(s);
                return ret;
                
            } catch (NamingException e2) {
                log.error("Failed to get EJB local interface for: " + t.getSimpleName() + " at " + s, e2);
            }
        }
        return null;
    }

    public static <T> T getLocalBean(Class<T> t, String earName, Context ctx) {
        final String name = t.getSimpleName();
        String s;
        if (name.endsWith(LOCAL)) {
            s = earName + "/" + name.substring(0, name.length() - LOCAL.length()) + BEAN_SUFFIX + LOCAL_SUFFIX;
        } else if (name.endsWith(PROGRAM_EJB)) {
            s = earName + "/" + name + BEAN_SUFFIX + LOCAL_SUFFIX;
        } else if (name.endsWith(BEAN_SUFFIX)){
            s = earName + "/" + name + LOCAL_SUFFIX;
        }else {
        
            s = earName + "/" + name + BEAN_SUFFIX + REMOTE_SUFFIX;
        }
        try {
            return (T)ctx.lookup(s);
        } catch (NamingException e) {
            log.error("Failed to get EJB remote interface for: " + t.getSimpleName(), e);
        }
        return null;
    }

    public static <T> T getBean(String className, String earName, boolean local, Context ctx) {
        String s;
        if (local) {
            s = earName + "/" + className + LOCAL_SUFFIX;
        } else {
            s = earName + "/" + className + REMOTE_SUFFIX;
        }
        try {
            T ret = (T)ctx.lookup(s);
            return ret;
        } catch (NamingException e) {
            log.error("Failed to get EJB with name: " + className + " at " + s, e);
        }
        return null;
    }
    
    protected static String getEarName() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        Pattern p = Pattern.compile("(?i)/([^\\\\/:\\*\\?<>\\|]+)\\.ear/");
        Matcher m = p.matcher(url.getPath());
        if (m.find()) {
        return m.group(1);
        } else {
        return "";
        }
       }
}
