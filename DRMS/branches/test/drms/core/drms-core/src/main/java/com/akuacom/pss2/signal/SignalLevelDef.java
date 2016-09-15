/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.SignalLevelEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.signal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

/**
 * The Class SignalLevelDAO.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "signal_level_def")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
    @NamedQuery(name = "SignalLevelDef.findBySignalAndLevelName",
        query = "select s from SignalLevelDef s where s.signal.signalName = :signalName and s.stringValue = :levelName",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class SignalLevelDef extends VersionedEntity {
    
	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = 2749725745890130096L;

	/** The string value. */
    private String stringValue;
    
    
    /** The default value. */
    private boolean defaultValue;
    
    /** The signal. */
    @ManyToOne
    @JoinColumn(name = "signal_def_uuid")
    private SignalDef signal;

    /**
     * Gets the string value.
     * 
     * @return the string value
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Sets the string value.
     * 
     * @param stringValue the new string value
     */
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }


    /**
     * Checks if is default value.
     * 
     * @return true, if is default value
     */
    public boolean isDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the default value.
     * 
     * @param defaultValue the new default value
     */
    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the signal.
     * 
     * @return the signal
     */
    public SignalDef getSignal() {
        return signal;
    }

    /**
     * Sets the signal.
     * 
     * @param signal the new signal
     */
    public void setSignal(SignalDef signal) {
        this.signal = signal;
    }
}
