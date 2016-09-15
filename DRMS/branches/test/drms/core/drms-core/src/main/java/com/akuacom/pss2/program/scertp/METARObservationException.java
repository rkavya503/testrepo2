package com.akuacom.pss2.program.scertp;

public class METARObservationException extends Exception {

	METARObservationException(String metar, Exception rootCause) {
		super(metar, rootCause);
	}
}
