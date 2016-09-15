/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.SignalEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.signal;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.akuacom.ejb.VersionedEntity;

/**
 * The Class SignalDAO.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "signal_def")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
    @NamedQuery(name = "SignalDef.findAll",
        query = "select s from SignalDef s left join fetch s.signalLevels",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "SignalDef.findSignalPerf",
        query = "select s from SignalDef s",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
        
    @NamedQuery(name = "SignalDef.findSignalPerfByNames",
        query = "select s from SignalDef s where s.signalName IN (:signalNames)",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(name = "SignalDef.findBySignalName",
        query = "select s from SignalDef s left join fetch s.signalLevels where s.signalName = :signalName",
        hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class SignalDef extends VersionedEntity {
    
    /**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = -5366862208120816434L;

	/** The signal name. */
    private String signalName;
    
    /** The type. */
    private String type;
    
    /**
     * Is this a level signal? If not, then a number signal.
     */
    private Boolean levelSignal;
    
    /**
     * Is this an input signal which can be anything - price, usage, etc.
     * or an output signal like mode and pending.
     */
    private Boolean inputSignal;
    
    /** The signal levels. */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "signal")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.REMOVE,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<SignalLevelDef> signalLevels;


    
    /**
     * Gets the signal name.
     * 
     * @return the signal name
     */
    public String getSignalName() {
        return signalName;
    }

    /**
     * Sets the signal name.
     * 
     * @param signalName the new signal name
     */
    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type the new type
     */
    public void setType(String type) {
        this.type = type;
    }

    public Boolean isLevelSignal() {
		if(levelSignal == null){
			return new Boolean(false);
		}
    	return levelSignal;
	}

	public void setLevelSignal(Boolean levelSignal) {
		this.levelSignal = levelSignal;
	}

	public Boolean isInputSignal() {
		if(inputSignal == null){
			return new Boolean(false);
		}

		return inputSignal;
	}

	public void setInputSignal(Boolean inputSignal) {
		this.inputSignal = inputSignal;
	}

	/**
     * Gets the signal levels.
     * 
     * @return the signal levels
     */
    public Set<SignalLevelDef> getSignalLevels() {
        return signalLevels;
    }
    
    
    /**
     * Sets the signal levels.
     * 
     * @param signalLevels the new signal levels
     */
    public void setSignalLevels(Set<SignalLevelDef> signalLevels) {
        this.signalLevels = signalLevels;
    }
    
    public Double getNumberDefault() {
    	return new Double(0.0);
    }
    
    public String getLevelDefault() {
    	String res = "";
    	
    	if (this.getSignalLevels() != null) {
	    	for (SignalLevelDef def : this.getSignalLevels()) {
	    		if (def != null && def.isDefaultValue()) {
	    			res = def.getStringValue();
	    			break;
	    		}
	    	}
    	}
    	
    	return res;
    }
}
