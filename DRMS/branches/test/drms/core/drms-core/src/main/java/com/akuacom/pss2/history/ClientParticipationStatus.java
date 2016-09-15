/**
 * 
 */
package com.akuacom.pss2.history;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * the enum EventParticipantion
 *
 */
public enum ClientParticipationStatus {
	
	EVENT_COMPLETED ("Finish", 0),
	ACTIVE_EVENT_CANCELLED ("Cancel", 10),
	ACTIVE_EVENT_OPT_OUT ("Event Opt-out", 20),
	INACTIVE_EVENT_CANCELLED ("Cancel", 25),
	
	//
	PROGRAM_PARTICIPANT_CONSTRAINTS_ACTIVE_WINDOW_VIOLATION ("Active Window Violation", 30),
	PROGRAM_PARTICIPANT_CONSTRAINTS_DURATION_WINDOW_VIOLATION ("Duration Window Violation", 35),
	INACTIVE_EVENT_OPT_OUT ("Event Opt-out", 40),
	PROGRAM_PARTICIPANT_CONSTRAINTS_INVALID_DATE_VIOLATION ("Invalid Date", 45),
	PROGRAM_PARTICIPANT_CONSTRAINTS_NOTIFY_WINDOW_VIOLATION ("Notify Window Violation", 50),
	CLIENT_OFFLINE ("Offline", 55),
	PARTICIPANT_OPT_OUT ("Participant Opt-out", 60),
	PARTICIPANT_OPT_IN ("Participant Opt-out", 61),
	PROGRAM_PARTICIPANT_OPT_OUT ("Program Opt-out", 65);
	
	private String description;
	private int value;
	
	ClientParticipationStatus(String description, int value) {
		this.description=description;
		this.value=value;
	}

	public String getDescription() {
		return description;
	}

	public int getValue() {
		return value;
	}
	
	private static final Map<Integer, ClientParticipationStatus> lookup	= new HashMap<Integer, ClientParticipationStatus>();
	static {
		for (ClientParticipationStatus s : EnumSet.allOf(ClientParticipationStatus.class)) {
			lookup.put(s.getValue(), s);
		}
	}

	public static ClientParticipationStatus get(int value) {
		return lookup.get(value);
	}
}
