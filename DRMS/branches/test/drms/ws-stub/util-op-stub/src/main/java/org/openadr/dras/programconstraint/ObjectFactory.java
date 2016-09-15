/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.programconstraint.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.programconstraint;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.programconstraint package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.programconstraint
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EventWindow }.
     * 
     * @return the event window
     */
    public EventWindow createEventWindow() {
        return new EventWindow();
    }

    /**
     * Create an instance of {@link ProgramConstraint }.
     * 
     * @return the program constraint
     */
    public ProgramConstraint createProgramConstraint() {
        return new ProgramConstraint();
    }

    /**
     * Create an instance of {@link NotificationWindow }.
     * 
     * @return the notification window
     */
    public NotificationWindow createNotificationWindow() {
        return new NotificationWindow();
    }

    /**
     * Create an instance of {@link ProgramConstraint.ValidDateTimes }
     * 
     * @return the valid date times
     */
    public ProgramConstraint.ValidDateTimes createProgramConstraintValidDateTimes() {
        return new ProgramConstraint.ValidDateTimes();
    }

    /**
     * Create an instance of {@link ProgramConstraint.BlackOutDateTimes }
     * 
     * @return the black out date times
     */
    public ProgramConstraint.BlackOutDateTimes createProgramConstraintBlackOutDateTimes() {
        return new ProgramConstraint.BlackOutDateTimes();
    }

    /**
     * Create an instance of {@link DateTimeWindow }.
     * 
     * @return the date time window
     */
    public DateTimeWindow createDateTimeWindow() {
        return new DateTimeWindow();
    }

    /**
     * Create an instance of {@link ListOfProgramConstraints }.
     * 
     * @return the list of program constraints
     */
    public ListOfProgramConstraints createListOfProgramConstraints() {
        return new ListOfProgramConstraints();
    }

}
