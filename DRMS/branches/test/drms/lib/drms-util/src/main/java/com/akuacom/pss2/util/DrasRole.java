/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.util.DrasRole.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.util;

/**
 * Defines all roles used in pss2 system.
 * 
 * @author Dichen Mao
 * @since 4.2
 */
public enum DrasRole {
    
    /** The Admin. */
    Admin, 
 
 /** The Operator. */
 Operator, 
 
 /** The Facility manager. */
 FacilityManager, 
 
 /** The Utility operator. */
 UtilityOperator,
 
 /** The Readonly Role. */
 Readonly,
 
 /** The Dispatcher ROle. */
 Dispatcher
 
}
