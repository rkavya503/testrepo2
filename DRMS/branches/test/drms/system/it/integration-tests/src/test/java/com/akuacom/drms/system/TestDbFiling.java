package com.akuacom.drms.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.annotations.DumpProfile;
import com.akuacom.annotations.LoadDbAfter;
import com.akuacom.annotations.LoadDbBefore;
import com.akuacom.annotations.SaveDbBefore;
import com.akuacom.annotations.Trace;
import com.akuacom.pss2.program.ProgramEAO;
import com.akuacom.utils.lang.ClassPathUtil;
import com.akuacom.utils.lang.Dbg;
import com.akuacom.ejb.jboss.test.JBossFixture;;

@Trace
// see Tracing.aj
// NOTE this test is not thread safe; if another thread modifies the program table
// while this test is running, it may fail
public class TestDbFiling extends JBossFixture {
    ProgramEAO programEAO = lookupSessionRemote(ProgramEAO.class);
    // leading LOOK_IN_CLASSPATH_PREFIX ('^') character tells DbFiling aspect to
    // search class path for files with these names
    // (ClassPathUtil.LOOK_IN_CLASSPATH_PREFIX character (^) is removed)
    final static private String EMPTY_FILE = ClassPathUtil.LOOK_IN_CLASSPATH_PREFIX
            + "programEmpty.sql"; // program table with no rows
    final static private String FULL_FILE = ClassPathUtil.LOOK_IN_CLASSPATH_PREFIX
            + "programFull.sql"; // program table with ~20 rows
    final static private String TEMP_FILE = "./tempPrograms.sql";

    long initialCount;

    public TestDbFiling() {
    }

    @BeforeClass
    public static void verifySqlFiles() {
        System.out.println(System.getProperty("java.class.path"));
        File empty = new File(ClassPathUtil.resolve(EMPTY_FILE));
        Dbg.info(empty + " exists " + empty.exists());
        assertTrue(empty.exists());
        File full = new File(ClassPathUtil.resolve(FULL_FILE));
        Dbg.info(full + " exists " + full.exists());
        assertTrue(full.exists());
        File temp = new File(TEMP_FILE);
        if (temp.exists()) {
            assertTrue(temp.delete());
        }
    }

    @SaveDbBefore(tables = "program", filePath = TEMP_FILE)
    public void assertSave() {
        File temp = new File(TEMP_FILE);
        assertTrue(temp.exists());
        initialCount = programEAO.count();
        Dbg.info("initialCount is " + initialCount);
    }

    @LoadDbBefore(files = EMPTY_FILE)
    @DumpProfile
    public void assertEmpty() {
        assertEquals(0, programEAO.count());
    }

    @LoadDbBefore(files = FULL_FILE)
    @LoadDbAfter(files = TEMP_FILE)
    public void assertFull() {
        assertNotSame(0, programEAO.count());
    }

    @DumpProfile
    public void assertRestored() {
        File temp = new File(TEMP_FILE);
        assertTrue(temp.exists());
        assertTrue(temp.delete());
        long nowCount =  programEAO.count();
        Dbg.info("now count is " + nowCount);
        assertEquals(initialCount, nowCount);
    }

    @Ignore
    @Test
    public void testDbFiling() {
        assertSave();
        assertEmpty();
        assertFull();
        assertRestored();
    }
}
