package com.akuacom.pss2.event.signal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
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
@Table(name = "event_signal_entry")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class EventSignalEntry extends VersionedEntity implements SignalEntry {

	private static final long serialVersionUID = -498074080521183177L;

	/** The expired. */
	@Column(columnDefinition="tinyint(3)")
	@DubiousColumnDefinition
    private boolean expired;
    
    @Column(name="`time`")
    private Date time;
    
    @ManyToOne
	@JoinColumn(name="eventSignalUuid", nullable=false)
    private EventSignal eventSignal;
    
    private String stringValue;
    
    private Double numberValue;

    //bi-directional many-to-one association to EventSignal
	public EventSignal getEventSignal() {
		return this.eventSignal;
	}

	public void setEventSignal(EventSignal eventSignal) {
		this.eventSignal = eventSignal;
	}
	
	@Override
	public Signal getParentSignal() {
		return this.getEventSignal();
	}

	@Override
	public void setParentSignal(Signal signal) {
		if (!(signal instanceof EventSignal)) {
			throw new RuntimeException("Only EventSignal may be set as parent of EventSignalEntry");
		}
		this.setEventSignal((EventSignal)signal);
	}

	@Override
	public Date getTime() {
		return time;
	}

	@Override
	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public Boolean getExpired() {
		return this.expired;
	}

	@Override
	public void setExpired(Boolean isExpired)
	{
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
	
	@Override
	public int compareTo(SignalEntry o) {
		if (o == null) {
			return 1;
		}
		return time.compareTo(o.getTime());
	}
}
