/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.signal;

import java.util.Date;

/**
 *
 * @author spierson
 *
 * This is a utility transport class
 * used to convey the useful parts of a SignalEntry to client code (like UI)
 */
    public abstract class SignalEntryValue {
        public SignalEntryValue(String signalName, Date startTime, String levelValue) {}
        public SignalEntryValue(String signalName, Date startTime, double numberValue) {}
        public abstract String getSignalName();
        public abstract Date   getStartTime();
        public abstract double getNumberValue();
        public abstract String getLevelValue();
        public abstract String getValueAsString();
    }
