package com.akuacom.utils.config;

import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

import com.akuacom.utils.lang.FieldUtil;


public class RuntimeSwitches {
    private final static Logger log = Logger.getLogger(RuntimeSwitches.class);
    public final static String OUTPUT_PROFILING_INFO_PROP = "com.akuacom.pss2.profiling";
    public final static String TRACE_EVERYTHING_PROP = "com.akuacom.pss2.traceAll";
    public final static String ENABLE_QODS_PROP = "com.akuacom.pss2.qod";
    public final static String AGGRESSIVE_PRUNING_PROP = "com.akuacom.pss2.aggressive";
    public final static String CSV_PROP = "com.akuacom.pss2.csv";
    public final static String EXTRA_UI_PROP = "com.akuacom.pss2.extra";
    public final static String DISCOVERABLE_DATASOURCES_PROP = "com.akuacom.pss2.discoverable";
	public static boolean OUTPUT_PROFILING_INFO = "true".equals(System.getProperty(OUTPUT_PROFILING_INFO_PROP));
	public static boolean OUTPUT_PROFILE_AFTER_TEST = false;
	public static boolean TRACE_EVERYTHING = "true".equals(System.getProperty(TRACE_EVERYTHING_PROP));
    public static boolean TRACE_ANNOS = true;
    public static boolean CALLER_ANNOS = false;
    public static boolean EXTRA_UI = "yes".equals(System.getProperty(EXTRA_UI_PROP));
    public static boolean CSV = "true".equals(System.getProperty(CSV_PROP));
	public static boolean DISCOVERABLE_DATASOURCES = "true".equals(System.getProperty(DISCOVERABLE_DATASOURCES_PROP));
	
    public static boolean ENABLE_QODS = "true".equals(System.getProperty(ENABLE_QODS_PROP));
    
    public static boolean AGGRESSIVE_PRUNING = "true".equals(System.getProperty(AGGRESSIVE_PRUNING_PROP));
    
    static {
        log.info("\n\n\n" +
        FieldUtil.dumpButNot(RuntimeSwitches.class, Modifier.PRIVATE, Modifier.FINAL)
        +"\n\n");
    }
    
}
