/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.utilityprogram.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.utilityprogram;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.utilityprogram package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.utilityprogram
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UtilityProgram }.
     * 
     * @return the utility program
     */
    public UtilityProgram createUtilityProgram() {
        return new UtilityProgram();
    }

    /**
     * Create an instance of {@link ParticipantList.Groups }
     * 
     * @return the groups
     */
    public ParticipantList.Groups createParticipantListGroups() {
        return new ParticipantList.Groups();
    }

    /**
     * Create an instance of {@link UtilityProgram.BiddingConfiguration }
     * 
     * @return the bidding configuration
     */
    public UtilityProgram.BiddingConfiguration createUtilityProgramBiddingConfiguration() {
        return new UtilityProgram.BiddingConfiguration();
    }

    /**
     * Create an instance of {@link ParticipantList.Locations }
     * 
     * @return the locations
     */
    public ParticipantList.Locations createParticipantListLocations() {
        return new ParticipantList.Locations();
    }

    /**
     * Create an instance of {@link ListOfPrograms }.
     * 
     * @return the list of programs
     */
    public ListOfPrograms createListOfPrograms() {
        return new ListOfPrograms();
    }

    /**
     * Create an instance of {@link ListOfProgramNames }.
     * 
     * @return the list of program names
     */
    public ListOfProgramNames createListOfProgramNames() {
        return new ListOfProgramNames();
    }

    /**
     * Create an instance of {@link ParticipantList.Accounts }
     * 
     * @return the accounts
     */
    public ParticipantList.Accounts createParticipantListAccounts() {
        return new ParticipantList.Accounts();
    }

    /**
     * Create an instance of {@link UtilityProgram.EventInfoTypes }
     * 
     * @return the event info types
     */
    public UtilityProgram.EventInfoTypes createUtilityProgramEventInfoTypes() {
        return new UtilityProgram.EventInfoTypes();
    }

    /**
     * Create an instance of {@link ListOfIDs }.
     * 
     * @return the list of i ds
     */
    public ListOfIDs createListOfIDs() {
        return new ListOfIDs();
    }

    /**
     * Create an instance of {@link ProgramInfo }.
     * 
     * @return the program info
     */
    public ProgramInfo createProgramInfo() {
        return new ProgramInfo();
    }

    /**
     * Create an instance of {@link ParticipantList }.
     * 
     * @return the participant list
     */
    public ParticipantList createParticipantList() {
        return new ParticipantList();
    }

}
