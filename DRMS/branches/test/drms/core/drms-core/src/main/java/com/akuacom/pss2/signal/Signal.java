package com.akuacom.pss2.signal;

import java.util.Set;

/**
 * Represents a particular type of signal such as mode, pending, price absolute, etc.
 * 
 * This is more of a joining class between the signal def that defines what type
 * of signal this is and a list of signal entries or data points.
 * 
 * Implementations of this class such as ProgramSignal, EventSignal and
 * EventParticipantSignal also join to the corresponding owning object (e.g. program)
 *
 */
public interface Signal {
	public SignalDef getSignalDef();
	public void setSignalDef(SignalDef signalDef);
    public Set<? extends SignalEntry> getSignalEntries();
    public void setSignalEntries(Set<? extends SignalEntry> entries);
}
