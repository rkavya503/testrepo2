package com.akuacom.pss2.program.participant;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.pss2.core.ProgramEJB;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.program.ProgramManager;
import com.akuacom.pss2.rule.Rule;
import com.akuacom.pss2.timer.TimerManager;

/**
 * The Data Structure that fuses {@link Program} and {@link Participant}
 * together.
 * 
 * The sole manager for {@link ProgramParticipantEAO}.
 * 
 * Uses {@link ProgramManager} and {@link ParticipantManager} to satisfy many of
 * the requests.
 * 
 * 
 * 
 * @author Aaron Roller
 * 
 */

public interface ProgramParticipantManager extends TimerManager {

    @Remote
    public interface R extends ProgramParticipantManager {}

    @Local
    public interface L extends ProgramParticipantManager {}

    ProgramParticipant getClientProgramParticipants(String programName,
            String participantName, boolean isClient);

    void addProgramParticipant(String programName, String participantName,
            boolean isClient);

    void addProgramParticipant(String programName, List<Participant> participants,
            boolean isClient);
    
    
    void addParticipantToProgram(Program program,
    		Participant part, boolean isClient, ProgramEJB programEJB);
    
    void addParticipantToProgram(Program program,
    		List<Participant> part, boolean isClient, ProgramEJB programEJB);
    
    ProgramParticipant setProgramParticipant(ProgramParticipant pp,
            boolean isClient);
    
    void setProgramParticipants(List<ProgramParticipant> ppList);
    /**
     * Update the participant's custom program information.
     * 
     * @param programName
     *            the name (key) of the program
     * @param partipantName
     *            the name (key) of the participant
     * @param pp
     *            the custom program information
     * 
     * @return the program participant
     */
    ProgramParticipant updateProgramParticipant(String programName,
            String partipantName, boolean isClient, ProgramParticipant pp);

    // TODO: change name to getProgramParticipant
    /**
     * Get a list of participant names (keys) given a program name (key).
     * 
     * @param programName
     *            the program name
     * 
     * @return the participants for program
     */
    List<String> getParticipantsForProgram(String programName);

    // TODO: change name to getProgramParticipantAsObject
    /**
     * Get a list of participants given a program name (key).
     * 
     * @param programName
     *            the program name
     * 
     * @return the participants for program as object
     */
    List<Participant> getParticipantsForProgramAsObject(String programName);
    
    public List<ProgramParticipant> getProgramParticipantsForProgramAsObject(String programName);

    // TODO: rename removeParticipantsFromProgram
    /**
     * Removes the participants to program.
     * 
     * @param programName
     *            the program name
     * @param pariticipantNames
     *            the pariticipant names
     */
    void removeParticipantsFromProgram(String programName,
            List<String> pariticipantNames, boolean isClient);

    /**
     * Get the participant's custom program information.
     * 
     * @param programName
     *            the program name
     * @param partipantName
     *            the partipant name
     * 
     * @return the program participant
     */
    ProgramParticipant getProgramParticipant(String programName,
            String partipantName, boolean isClient);

    /**
     * Get get a list of ProgramParticipants that are clients for a Program Participant.
     * 
     * @param programName
     *            the program name
     * @param partipantName
     *            the partipant name
     * @param isClient true if participant is client false otherwise            
     * 
     * @return  a list of ProgramParticipants that are clients for a Program Participant.
     */
    List<ProgramParticipant> getProgramParticipantsByParent(String programName,
            String parentPartipantName, boolean isClient);
    
    
    
    // TODO: change name to addProgramParticipant()
    /**
     * Add a participant to a program.
     * 
     * @param programName
     *            the name of the program
     * @param participantName
     *            the name of the participant
     */
    void addParticipantToProgram(String programName, String participantName,
            boolean isClient);

    // web method
    /**
     * Add a list of participants given their names (keys) to a program given
     * its name (key).
     * 
     * @param programName
     *            the program name
     * @param pariticipantNames
     *            the pariticipant names
     * 
     * @see
     */
    void addParticipantsToProgram(String programName,
            List<String> pariticipantNames, boolean isClient);

    //List<ProgramParticipant> findByParticipant(String participantName,
     //       boolean isClient);

    public void addRules(String clientName, String programName,
        List<ProgramParticipantRule> rules, Rule.Source source);

    public List<ProgramParticipantRule> getRules(String clientName,
        String programName);
    
    
    	

}
