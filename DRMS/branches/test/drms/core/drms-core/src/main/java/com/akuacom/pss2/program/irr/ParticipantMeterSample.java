/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.program.irr;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author IPPC
 */
public class ParticipantMeterSample implements Serializable {
    private static final long serialVersionUID = 8550659159080916848L;

    private double meterRate;
    private Calendar sampleTime;
    private String participantName;

    public ParticipantMeterSample() {     }

    public ParticipantMeterSample(String participantName, Calendar sampleTime, double meterRate) {
        this.sampleTime = (Calendar)sampleTime.clone();
        this.meterRate = meterRate;
        this.participantName = participantName;
    }

    /**
     * @return the rate
     */
    public double getMeterRate() {
        return meterRate;
    }

    /**
     * @param rate the rate to set
     */
    public void setMeterRate(double rate) {
        this.meterRate = rate;
    }

    /**
     * @return the participantName
     */
    public String getParticipantName() {
        return participantName;
    }

    /**
     * @param participantName the participantName to set
     */
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    /**
     * @return the sampleTime
     */
    public Calendar getSampleTime() {
        return sampleTime;
    }

    /**
     * @param sampleTime the sampleTime to set
     */
    public void setSampleTime(Calendar sampleTime) {
        this.sampleTime = sampleTime;
    }

}
