/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.participantaccount.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.participantaccount;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.participantaccount package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.participantaccount
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ContactInformation.VoiceNumbers }
     * 
     * @return the voice numbers
     */
    public ContactInformation.VoiceNumbers createContactInformationVoiceNumbers() {
        return new ContactInformation.VoiceNumbers();
    }

    /**
     * Create an instance of {@link ParticipantAccount.Programs }
     * 
     * @return the programs
     */
    public ParticipantAccount.Programs createParticipantAccountPrograms() {
        return new ParticipantAccount.Programs();
    }

    /**
     * Create an instance of {@link ContactInformation }.
     * 
     * @return the contact information
     */
    public ContactInformation createContactInformation() {
        return new ContactInformation();
    }

    /**
     * Create an instance of {@link ListOfParticipantAccountIDs }.
     * 
     * @return the list of participant account i ds
     */
    public ListOfParticipantAccountIDs createListOfParticipantAccountIDs() {
        return new ListOfParticipantAccountIDs();
    }

    /**
     * Create an instance of {@link ParticipantAccount.ParticipantGroups }
     * 
     * @return the participant groups
     */
    public ParticipantAccount.ParticipantGroups createParticipantAccountParticipantGroups() {
        return new ParticipantAccount.ParticipantGroups();
    }

    /**
     * Create an instance of {@link ParticipantAccount.UserGroups }
     * 
     * @return the user groups
     */
    public ParticipantAccount.UserGroups createParticipantAccountUserGroups() {
        return new ParticipantAccount.UserGroups();
    }

    /**
     * Create an instance of {@link ListOfParticipantAccounts }.
     * 
     * @return the list of participant accounts
     */
    public ListOfParticipantAccounts createListOfParticipantAccounts() {
        return new ListOfParticipantAccounts();
    }

    /**
     * Create an instance of {@link ParticipantAccount.DRASClients }
     * 
     * @return the DRAS clients
     */
    public ParticipantAccount.DRASClients createParticipantAccountDRASClients() {
        return new ParticipantAccount.DRASClients();
    }

    /**
     * Create an instance of {@link ContactInformation.EmailAddresses }
     * 
     * @return the email addresses
     */
    public ContactInformation.EmailAddresses createContactInformationEmailAddresses() {
        return new ContactInformation.EmailAddresses();
    }

    /**
     * Create an instance of {@link ParticipantAccount }.
     * 
     * @return the participant account
     */
    public ParticipantAccount createParticipantAccount() {
        return new ParticipantAccount();
    }

    /**
     * Create an instance of {@link ContactInformation.PagerNumbers }
     * 
     * @return the pager numbers
     */
    public ContactInformation.PagerNumbers createContactInformationPagerNumbers() {
        return new ContactInformation.PagerNumbers();
    }

    /**
     * Create an instance of {@link ContactInformation.FaxNumbers }
     * 
     * @return the fax numbers
     */
    public ContactInformation.FaxNumbers createContactInformationFaxNumbers() {
        return new ContactInformation.FaxNumbers();
    }

    /**
     * Create an instance of {@link ParticipantAccount.UtilityGroups }
     * 
     * @return the utility groups
     */
    public ParticipantAccount.UtilityGroups createParticipantAccountUtilityGroups() {
        return new ParticipantAccount.UtilityGroups();
    }

    /**
     * Create an instance of {@link ParticipantAccount.StandingBids }
     * 
     * @return the standing bids
     */
    public ParticipantAccount.StandingBids createParticipantAccountStandingBids() {
        return new ParticipantAccount.StandingBids();
    }

}
