package com.akuacom.ejb;

import java.util.UUID;

import org.junit.Test;

import com.akuacom.utils.lang.Dbg;
import com.akuacom.utils.lang.TimingUtil;

public class UuidTest {

    public final static long UUID_COUNT = 100000;
    
    @Test
    public void createUuids() {
        String dummy;
        long ltime = System.currentTimeMillis();
        for(int i = 0; i < UUID_COUNT; i++ ){
            dummy = UUID.randomUUID().toString();
            dummy = dummy.substring(0,8) + dummy.substring(9,13) + dummy.substring(14,18) + dummy.substring(19,23) + dummy.substring(24);
        }
        Dbg.o("creating " + UUID_COUNT + " uuids", ltime);
    }
}
