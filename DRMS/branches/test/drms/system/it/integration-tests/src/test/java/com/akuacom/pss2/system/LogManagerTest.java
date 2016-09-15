package com.akuacom.pss2.system;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.akuacom.ejb.jboss.test.JBossFixture;

/**
 * 
 * 
 */
public class LogManagerTest extends JBossFixture {

    protected static LogManager logManager;
    Logger log = Logger.getLogger(LogManagerTest.class.getSimpleName());

    @BeforeClass
    public  static void setUp() {
        
        if (logManager == null) {
            logManager = lookupSessionRemote(LogManager.class);
        }
        
    }

    private void doNothing() {}
    
    @Test
    public void testGetLog() {
        String logStr100 = logManager.getLog(0, 100);
        log.info("logStr100\n" + logStr100);
        long initialSize = logManager.logLength();
        log.info("logfile.length " + initialSize);
        File f = null;
        try {
            f = File.createTempFile("testlog", "tmp");
            logManager.partition(.5, f.getAbsolutePath(), true, false);
            long newSize = logManager.logLength();
            log.info("newSize " + newSize);
            String newLogStr100 = logManager.getLog(0, 100);
            log.info(newLogStr100);
            assertTrue(!logStr100.equals(newLogStr100));
        }catch(Exception e) {
           e.printStackTrace();
        }finally {
            if(f!=null) {
                f.delete();
                new File(f.getParent()+File.separator+"testlog.zip").delete();
            }
        }
    }
}
