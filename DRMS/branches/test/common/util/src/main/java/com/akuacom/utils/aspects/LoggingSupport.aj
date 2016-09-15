package com.akuacom.utils.aspects;

import org.apache.log4j.Logger;

import com.akuacom.annotations.Logs;

public aspect LoggingSupport extends Common {
    interface i {
        static Logger log = Logger.getLogger(i.class);
    }
    declare parents : (@Logs *) implements LoggingSupport.i;
}