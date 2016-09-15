/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.ProgramSignalEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.program.signal;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalEntry;

/**
 * The Class ProgramSignal.
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "program_signal")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ProgramSignal extends VersionedEntity implements Signal {

    private static final long serialVersionUID = -2393868020921508769L;

    /** The signal. */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "signal_def_uuid")
    private SignalDef signal;

    /** The program . */
    @ManyToOne
    @JoinColumn(name = "program_uuid")
    private Program program;

    /**
     * Program Signal Entries
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "programSignal")
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.REMOVE,
            org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<ProgramSignalEntry> programSignalEntries;

    /**
     * Gets the signal.
     * 
     * @return the signal
     */
    public SignalDef getSignalDef() {
        return signal;
    }

    /**
     * Sets the signal.
     * 
     * @param signal
     *            the new signal
     */
    public void setSignalDef(SignalDef signal) {
        this.signal = signal;
    }

    /**
     * Gets the program .
     * 
     * @return the program
     */
    public Program getProgram() {
        return program;
    }

    /**
     * Sets the program .
     * 
     * @param program
     *            the new program
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    public Set<ProgramSignalEntry> getProgramSignalEntries() {
        return this.programSignalEntries;
    }

    public void setProgramSignalEntries(
            Set<ProgramSignalEntry> programSignalEntries) {
        this.programSignalEntries = programSignalEntries;
    }

    @Override
    public Set<? extends SignalEntry> getSignalEntries() {
        return this.getProgramSignalEntries();
    }

    @Override
    public void setSignalEntries(Set<? extends SignalEntry> programSignalEntries) {
        Set<ProgramSignalEntry> newSE = new HashSet<ProgramSignalEntry>();

        for (SignalEntry se : programSignalEntries) {
            if (se instanceof ProgramSignalEntry) {
                newSE.add((ProgramSignalEntry) se);
            } else {
                throw new RuntimeException(
                        "Only ProgramSignalEntry may be added to ProgramSignal signal entries");
            }
        }

        this.setProgramSignalEntries(newSE);
    }

    public ProgramSignal copy(ProgramSignal existing, Program parent) {
        ProgramSignal cloned = new ProgramSignal();
        cloned.setSignalDef(existing.getSignalDef());
        cloned.setProgram(parent);
        if (existing.getSignalEntries() == null) {
            return cloned;
        }
        Set<SignalEntry> newList = new HashSet<SignalEntry>();
        Set<? extends SignalEntry> list = existing.getSignalEntries();
        for (SignalEntry entry : list) {
            ProgramSignalEntry newOne = ((ProgramSignalEntry) entry).copy(
                    (ProgramSignalEntry) entry, cloned);
            newList.add(newOne);
        }
        cloned.setSignalEntries(newList);
        return cloned;
    }
}
