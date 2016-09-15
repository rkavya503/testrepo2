package com.akuacom.pss2.program.signal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.pss2.annotations.DubiousColumnDefinition;
import com.akuacom.pss2.signal.Signal;
import com.akuacom.pss2.signal.SignalEntry;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "program_signal_entry")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ProgramSignalEntry extends VersionedEntity implements SignalEntry {

    private static final long serialVersionUID = 548509451273021333L;

    /** The signal. */
    @ManyToOne
    @JoinColumn(name = "programSignalUuid")
    private ProgramSignal programSignal;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(columnDefinition = "tinyint(3)")
    @DubiousColumnDefinition
    private Boolean expired;

    private String stringValue;

    private Double numberValue;

    public ProgramSignal getProgramSignal() {
        return programSignal;
    }

    public void setProgramSignal(ProgramSignal programSignal) {
        this.programSignal = programSignal;
    }

    @Override
    public Signal getParentSignal() {
        return this.getProgramSignal();
    }

    @Override
    public void setParentSignal(Signal signal) {
        if (!(signal instanceof ProgramSignal)) {
            throw new RuntimeException(
                    "Only ProgramSignal may be set as parent of ProgramSignalEntry");
        }
        this.setProgramSignal((ProgramSignal) signal);
    }

    public Boolean getExpired() {
        return expired;
    }

    @Override
    public void setExpired(Boolean isExpired) {
        this.expired = isExpired;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getLevelValue() {
        return this.getStringValue();
    }

    public void setLevelValue(String stringValue) {
        this.setStringValue(stringValue);
    }

    public Double getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(Double numberValue) {
        this.numberValue = numberValue;
    }

    public String getValueAsString() {
        if (this.getStringValue() != null) {
            return this.getStringValue();
        } else if (this.getNumberValue() != null) {
            return this.getNumberValue().toString();
        } else {
            return "";
        }
    }

    public ProgramSignalEntry copy(ProgramSignalEntry existing,
            ProgramSignal parent) {
        ProgramSignalEntry cloned = new ProgramSignalEntry();
        cloned.setProgramSignal(parent);
        cloned.setExpired(existing.getExpired());
        cloned.setStringValue(existing.getStringValue());
        cloned.setNumberValue(existing.getNumberValue());
        cloned.setTime(existing.getTime());
        cloned.setTime(existing.getTime());
        cloned.setParentSignal(existing.getParentSignal());

        return cloned;
    }

    public int compareTo(SignalEntry o) {
        return this.getTime().compareTo(o.getTime());
    }

    @Override
    public Date getTime() {
        return time;
    }

    @Override
    public void setTime(Date time) {
        this.time = time;

    }
}
