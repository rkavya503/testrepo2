/*
 * www.akuacom.com - Automating Demand Response
 * 
 * org.openadr.dras.bid.ObjectFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package org.openadr.dras.bid;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.openadr.dras.bid package.
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openadr.dras.bid
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Bid.BidBlocks }
     * 
     * @return the bid blocks
     */
    public Bid.BidBlocks createBidBidBlocks() {
        return new Bid.BidBlocks();
    }

    /**
     * Create an instance of {@link BidBlock.TimePeriod }
     * 
     * @return the time period
     */
    public BidBlock.TimePeriod createBidBlockTimePeriod() {
        return new BidBlock.TimePeriod();
    }

    /**
     * Create an instance of {@link BidBlock }.
     * 
     * @return the bid block
     */
    public BidBlock createBidBlock() {
        return new BidBlock();
    }

    /**
     * Create an instance of {@link Bid }.
     * 
     * @return the bid
     */
    public Bid createBid() {
        return new Bid();
    }

    /**
     * Create an instance of {@link ListOfBids }.
     * 
     * @return the list of bids
     */
    public ListOfBids createListOfBids() {
        return new ListOfBids();
    }

}
