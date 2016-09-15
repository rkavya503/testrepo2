/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.programconstraint.ConstraintFilter.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.programconstraint;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConstraintFilter.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ConstraintFilter">
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="ACCEPT"/>
 * &lt;enumeration value="REJECT"/>
 * &lt;enumeration value="FORCE"/>
 * &lt;enumeration value="RESTRICT"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ConstraintFilter")
@XmlEnum
public enum ConstraintFilter {

    /** The ACCEPT. */
    ACCEPT,
    
    /** The REJECT. */
    REJECT,
    
    /** The FORCE. */
    FORCE,
    
    /** The RESTRICT. */
    RESTRICT;

    /**
     * Value.
     * 
     * @return the string
     */
    public String value() {
        return name();
    }

    /**
     * From value.
     * 
     * @param v the v
     * 
     * @return the constraint filter
     */
    public static ConstraintFilter fromValue(String v) {
        return valueOf(v);
    }

}
