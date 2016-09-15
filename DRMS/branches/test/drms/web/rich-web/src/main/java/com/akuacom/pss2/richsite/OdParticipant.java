package com.akuacom.pss2.richsite;

import java.util.Random;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.participant.ProgramParticipant;
import com.akuacom.utils.JsUtil;
import com.akuacom.utils.lang.MathUtil;

/**
 * wrapper for Participant class for Op-dash prototype
 * TODO merge with Participant when prototype stabilizes
 *
 */
public class OdParticipant {
    private static Random rnd = new Random(System.currentTimeMillis());
    private final static boolean Randomize = true;
    private final static double Factor = 1./5;
    private Participant p;
    private double[] baseline;
    private double[] actual;
    private boolean inIt;
    private boolean offline;
    private OdProgram program;
    
    static double[][] NormalizedDailyUsage = {{0, 0.57},{.5,0.555},{1,0.535},
        {1.5,0.525},{2,0.515},{2.5,.505},{3,.51},{3.5,.52},{4,.535},{4.5,.56},
        {5,.58},{5.5,.62},{6,.655}, {6.5,.695},{7,.725},{7.5,.79},{8,.815},{8.5,.835},
        {9,.89},{9.5,.91},{10,.93},{10.5,.96},{11,.97},{11.5,.97},{12,.98},{12.5,.99},
        {13,1},{13.5,.98},{14,.97},{14.5,.97},{15,.96},{15.5,.95},{16,.95},{16.5,.94},
        {17,.94},{17.5,.93},{18,.91},{18.5,.925},{19,.95},{19.5,.925},{20,.9},{20.5,.84},
        {21,.795},{21.5,.75},{22,.705},{22.5,.655},{23,.615},{23.5,.595}};

   
    public OdParticipant() {
        initLines();
    }   
    public OdParticipant(ProgramParticipant pp) {
        this.p=pp.getParticipant();
        initLines();
        if(rnd.nextDouble() < .9) {
            offline=false;
        }
        inIt = pp.getOptStatus() == 0;
    }   
    
    public String getName() {
        return p.getParticipantName();
    }
    public void setName(String name) {
    }
    public double[] getBaseline() {
        return baseline;
    }
    public void setBaseline(double[] baseline) {
        this.baseline = baseline;
    }
    public double[] getActual() {
        return actual;
    }
    public void setActual(double[] actual) {
        this.actual = actual;
    }
    public double getShed() {
        return p.getShedPerHourKW();
    }
    public void setShed(double shed) {
        p.setShedPerHourKW(shed);
    }
    public double getLatitude() {
        return p.getLatitude();
    }
    public double getLongitude() {
        return p.getLongitude();
    }
    
    public boolean isOffline() {
        return offline;
    }
    public void setOffline(boolean offline) {
        this.offline=offline;
    }
    public boolean isInIt() {
        return inIt;
    }
    public void setInIt(boolean inIt) {
        this.inIt = inIt;
    }
    
    public OdProgram getProgram() {
        return program;
    }
    
    public void setProgram(OdProgram program) {
        this.program = program;
    }
    
    public String getOptButton() {
        return "<button type=\"button\" value=\"Change\" onclick=\"akuacom.data.chgOpt(progs['" + JsUtil.asJSVar(program.getProgramName()) + "'],'" + getName() + "'); "
        +"akuacom.data.optOut('"+ JsUtil.asJSVar(program.getProgramName()) + "', '" 
        + OdProgram.DIV_OPTOUT+ JsUtil.asJSVar(program.getProgramName()) + "')\" />";
    }
    
    private void initLines() {
        baseline = new double[96];
        actual =  new double[96];
        double[] now, next;
        for(int i =0 ; i < 96; i++) {
            now = NormalizedDailyUsage[i/4];
            next = i == 95 ? NormalizedDailyUsage[0] : NormalizedDailyUsage[i/4 + 1];
            if(now[0] == next[0]) {
                baseline[i] = now[1];
            } else {
                baseline[i] =MathUtil.linterp(now[1],next[1],now[0], next[0], i * .25);
            }
            actual[i] = baseline[i];
        }
        
        if(Randomize) {
            double max = 0;
            for(int i = 0; i < baseline.length; i++) {
                baseline[i] += Factor * rnd.nextGaussian()*baseline[i];
                if(baseline[i]>max) {
                    max = baseline[i];
                }
            }
            // reset shed to max baseline
            setShed( getShed() * max);
            
            for(int i = 0; i < actual.length; i++) {
                actual[i] += Factor * rnd.nextGaussian()*actual[i];
            }
        }
       
    }

}
