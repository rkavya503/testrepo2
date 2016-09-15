/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.responseschedule.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.responseschedule;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.responseschedule package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.responseschedule
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Rule }.
     * 
     * @return the rule
     */
    public Rule createRule() {
        return new Rule();
    }

    /**
     * Create an instance of {@link OperationStateSpec.Rules }
     * 
     * @return the rules
     */
    public OperationStateSpec.Rules createOperationStateSpecRules() {
        return new OperationStateSpec.Rules();
    }

    /**
     * Create an instance of {@link OperationStateSpec }.
     * 
     * @return the operation state spec
     */
    public OperationStateSpec createOperationStateSpec() {
        return new OperationStateSpec();
    }

    /**
     * Create an instance of {@link ResponseSchedule.OperationStates }
     * 
     * @return the operation states
     */
    public ResponseSchedule.OperationStates createResponseScheduleOperationStates() {
        return new ResponseSchedule.OperationStates();
    }

    /**
     * Create an instance of {@link ResponseSchedule }.
     * 
     * @return the response schedule
     */
    public ResponseSchedule createResponseSchedule() {
        return new ResponseSchedule();
    }

}
