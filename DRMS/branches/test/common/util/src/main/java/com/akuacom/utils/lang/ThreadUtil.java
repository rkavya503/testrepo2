package com.akuacom.utils.lang;

import java.lang.reflect.Method;

import com.akuacom.annotations.Logs;


// the LoggingSupport advice will weave (upon seeing the @Logs anno) the code necessary to support logging calls such as the one in dump() 
@Logs 
public class ThreadUtil {

    public static ThreadGroup getRootThreadGroup( ) {
        ThreadGroup tg = Thread.currentThread( ).getThreadGroup( );
        ThreadGroup ptg;
        while ( (ptg = tg.getParent( )) != null )
            tg = ptg;
        
        return tg;
    }

    public static <T extends Thread> T find(String name) {
        ThreadGroup rt = getRootThreadGroup();
        Thread[] threads = new Thread[100];
        rt.enumerate(threads);
        for( int i = 0; i < threads.length; i++) {
            if(threads[i] != null && threads[i].getName().equals(name)) {
                return (T) threads[i];
            }
        }
        return null;
    }

    public static String T() {
        return Thread.currentThread().getName() + " ";
    }
    
    public static String dumpStack(Thread t) {
        return Dbg.oSA(t.getStackTrace());
    }
    
    public static String current(Thread t) {
        StackTraceElement[] sts = t.getStackTrace();
        return sts[sts.length-1].toString();
    }
    
    public static boolean cflowQ(Thread t, Method m) {
        String mstr = m.getDeclaringClass().getSimpleName() + "." + m.getName(); 
        for(StackTraceElement st : t.getStackTrace()) {
            if(st.toString().indexOf(mstr) != -1) {
                return true;
            }
        }
        return false;
    }
    
    public static String dump() {
        ThreadGroup r = getRootThreadGroup();
        StringBuffer sb = new StringBuffer();
        Thread[]threads = new Thread[r.activeCount()* 3 / 2];
        r.enumerate(threads);
        int count = 0;
        for(Thread t : threads) {
            if(t != null) {
                count++;
                sb.append(t.getName() + " -> " + t.getState() + "\n\n" + Dbg.oSAln(t.getStackTrace()));
            } 
        }
       // log.info("count " + count);
        
        return sb.toString();
    }
}