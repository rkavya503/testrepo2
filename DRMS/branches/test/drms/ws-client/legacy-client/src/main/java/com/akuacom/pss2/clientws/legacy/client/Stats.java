/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.legacy.client.Stats.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

package com.akuacom.pss2.clientws.legacy.client;

import java.util.Date;
import java.util.logging.Logger;

// statistics class
// 
// everything is sychronized here because the gets and sets are accessed
// from both the timer thread and the swing thread.

/**
 * The Class Stats.
 */
public class Stats {
    // singleton
    /** The Constant INSTANCE. */
    public static final Stats INSTANCE = new Stats();

    /** The throughput mps. */
    private long throughputMPS = 0;

    /** The msgs sent. */
    private long msgsSent = 0;

    /** The msgs received. */
    private long msgsReceived = 0;

    /** The send errors. */
    private long sendErrors = 0;

    /** The ave latency ms. */
    private long aveLatencyMS = 0;

    /** The min latency ms. */
    private long minLatencyMS = Long.MAX_VALUE;

    /** The max latency ms. */
    private long maxLatencyMS = 0;

    /** The total latency ms. */
    private long totalLatencyMS = 0;

    /** The start time ms. */
    private long startTimeMS = -1;

    /** The sent time stamp ms. */
    private long sentTimeStampMS;

    /** The Constant SKIP_FIRST_MESSAGES. */
    private static final int SKIP_FIRST_MESSAGES = 1;

    /** The logger. */
    private Logger logger = PSS2WSClient.INSTANCE.logger;

    // enforce singleton
    /**
     * Instantiates a new stats.
     */
    private Stats() {
    }

    // called just prior to sending a message to the web service
    /**
     * Pre send.
     */
    public synchronized void preSend() {
        msgsSent++;
        sentTimeStampMS = new Date().getTime();
        if (startTimeMS == -1) {
            startTimeMS = sentTimeStampMS;
        }
    }

    // called is a send succeeds
    /**
     * Send success.
     */
    public synchronized void sendSuccess() {
        long latency = new Date().getTime() - sentTimeStampMS;
        logger.fine("latency = " + latency);
        msgsReceived++;
        if (msgsReceived > SKIP_FIRST_MESSAGES) {
            if (latency < minLatencyMS) {
                minLatencyMS = latency;
            }
            if (latency > maxLatencyMS) {
                maxLatencyMS = latency;
            }
            totalLatencyMS += latency;
            aveLatencyMS = (long) ((double) totalLatencyMS
                    / (double) (msgsReceived - SKIP_FIRST_MESSAGES) + 0.5);
            throughputMPS = (long) (msgsReceived / getRunningTimeS() + 0.5);
        }
    }

    // called if a send fails
    /**
     * Send failure.
     */
    public synchronized void sendFailure() {
        sendErrors++;
    }

    // dump the stats to the log
    /**
     * Log.
     */
    public synchronized void log() {
        String output = "statistics" + "\nrunning time:      "
                + getRunningTimeS() + " s" + "\nmessages sent:     " + msgsSent
                + "\nmessages received: " + msgsReceived
                + "\nsend errors:       " + sendErrors
                + "\nthroughput:        " + throughputMPS + " mps"
                + "\naverage latency:   " + aveLatencyMS + " ms"
                + "\nminimum latency:   " + minLatencyMS + " ms"
                + "\nmaximum latency:   " + maxLatencyMS + " ms";
        logger.info(output);
    }

    /**
     * Gets the msgs sent.
     * 
     * @return the msgs sent
     */
    public synchronized long getMsgsSent() {
        return msgsSent;
    }

    /**
     * Gets the msgs received.
     * 
     * @return the msgs received
     */
    public synchronized long getMsgsReceived() {
        return msgsReceived;
    }

    /**
     * Gets the send errors.
     * 
     * @return the send errors
     */
    public synchronized long getSendErrors() {
        return sendErrors;
    }

    /**
     * Gets the ave latency ms.
     * 
     * @return the ave latency ms
     */
    public synchronized long getAveLatencyMS() {
        return aveLatencyMS;
    }

    /**
     * Gets the min latency ms.
     * 
     * @return the min latency ms
     */
    public synchronized long getMinLatencyMS() {
        return minLatencyMS;
    }

    /**
     * Gets the max latency ms.
     * 
     * @return the max latency ms
     */
    public synchronized long getMaxLatencyMS() {
        return maxLatencyMS;
    }

    /**
     * Gets the total latency ms.
     * 
     * @return the total latency ms
     */
    public synchronized long getTotalLatencyMS() {
        return totalLatencyMS;
    }

    /**
     * Gets the start time ms.
     * 
     * @return the start time ms
     */
    public synchronized long getStartTimeMS() {
        return startTimeMS;
    }

    /**
     * Gets the running time s.
     * 
     * @return the running time s
     */
    public synchronized double getRunningTimeS() {
        long nowMS = new Date().getTime();
        return (nowMS - startTimeMS) / 1000.0;
    }

    /**
     * Gets the throughput mps.
     * 
     * @return the throughput mps
     */
    public synchronized long getThroughputMPS() {
        return throughputMPS;
    }
}
