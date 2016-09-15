package com.akuacom.utils.aspects;

import com.akuacom.utils.lang.LoggingTuples;
import static com.akuacom.utils.lang.LoggingTuples.*;
import com.akuacom.utils.config.RuntimeSwitches;
import scala.Tuple2;
import org.apache.log4j.Logger;
/**
 * This aspect manages logging
 * 
 * com.akuacom.utils.aspects.Logging.aj - Copyright(c)1994 to 2011 by Akuacom .
 * All rights reserved. Redistribution and use in source and binary forms, with
 * or without modification, is prohibited.
 * 
 * log ids must be unique
 * must be able to reset the cache when
 */
public aspect Logging extends Common {
    pointcut logDebugOrInfo() :
        akuacomScope()
        && (call(public void Logger.info(Object))
            || call(public void Logger.debug(Object))
            );
    
    pointcut logWarnOrError() :
        akuacomScope()
        && (call(public void Logger.warn(Object))
            || call(public void Logger.error(Object))
            );
    
    pointcut logCtx(Object msg) :
        (logDebugOrInfo() || logWarnOrError())
        && args(msg);
    
    void around(Object msg) : logCtx(msg) {
        if(msg == null) {
            proceed(msg);
        }
        String m = "" + msg;
        if( m.length() < 15 || !RuntimeSwitches.AGGRESSIVE_PRUNING
                || m.startsWith(StartToken) && m.endsWith(EndToken)
                || m.indexOf(MessageToken) != -1)  {
            proceed(msg);
        } else if (RuntimeSwitches.AGGRESSIVE_PRUNING){
            String s = m.toString();
            Tuple2<Integer,Integer> hashLength = new Tuple2<Integer,Integer>(s.hashCode(),s.length());
            int idx = LoggingTuples.indexOf(hashLength); 
            LoggingTuples.add(hashLength, idx);
            if(idx == -1) {
                proceed(hashLength + MessageToken + m);
            } else {
                proceed(StartToken + hashLength +EndToken);
            }
        }
    }
    
}