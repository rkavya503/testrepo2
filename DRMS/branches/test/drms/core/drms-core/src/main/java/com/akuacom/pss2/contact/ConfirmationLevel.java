package com.akuacom.pss2.contact;

/**
 * Defines level of client (REST, SOAP, BACNet) confirmation the system requires.  
 * None means no confirmation is expected from the client. Medium means the 
 * confirmation is option and does not need to be correct.  Full means that the
 * client is required to send a valid confirmation or their status will be set
 * to ERROR.
 * 
 * In all cases, a record is created in the event_state table when the client
 * initially requests event data
 * 
 */
public enum ConfirmationLevel {
	NONE,
	MEDIUM,
	FULL
}
