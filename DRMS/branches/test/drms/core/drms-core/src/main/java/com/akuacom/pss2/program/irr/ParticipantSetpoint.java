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
public class ParticipantSetpoint implements Serializable {
    private static final long serialVersionUID = -2632941477942527919L;

    private double Setpoint;
    private Calendar setpointTime;
    private String participantName;
    private double pseudoGen;
    private Calendar pseudoGenTime;

    public ParticipantSetpoint() {     }

    public ParticipantSetpoint(String participantName, double setpoint, Calendar setpointTime) {
        this.Setpoint = setpoint;
        this.participantName = participantName;
        setSetpointTime(setpointTime);
    }

    /**
     * @return the Setpoint
     */
    public double getSetpoint() {
        return Setpoint;
    }

    /**
     * @param Setpoint the Setpoint to set
     */
    public void setSetpoint(double Setpoint, Calendar setpointTime) {
        this.Setpoint = Setpoint;
        setSetpointTime(setpointTime);
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
     * @return the pseudoGen
     */
    public double getPseudoGen() {
        return pseudoGen;
    }

    /**
     * @param pseudogen the pseudoGen to set
     */
    public void setPseudoGen(double pseudoGen, Calendar pseudoGenTime) {
        this.pseudoGen = pseudoGen;
        setPseudoGenTime(pseudoGenTime);
    }

    /**
     * @return the setpointTime
     */
    public Calendar getSetpointTime() {
        return setpointTime;
    }

    /**
     * @param setpointTime the setpointTime to set
     */
    public void setSetpointTime(Calendar setpointTime) {
        this.setpointTime = (Calendar)setpointTime.clone();
    }

    /**
     * @return the pseudoGenTime
     */
    public Calendar getPseudoGenTime() {
        return pseudoGenTime;
    }

    /**
     * @param pseudoGenTime the pseudoGenTime to set
     */
    public void setPseudoGenTime(Calendar pseudoGenTime) {
        this.pseudoGenTime = (Calendar)pseudoGenTime.clone();
    }

}
