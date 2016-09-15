package com.akuacom.pss2.richsite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.akuacom.annotations.EpicMethod;
import com.akuacom.pss2.system.Task;
import com.akuacom.pss2.system.TaskGenEAO;
import com.akuacom.utils.lang.ThreadUtil;


public class Scheduler extends HttpServlet {
    
    public static interface Listener {
        void trigger(Task t);
    }
    
    private static final long serialVersionUID = 1L;
    public static Scheduler Instance;
    public final static String SCHEDULER_THREAD = "schedulerThread";
    final static long SCHEDULER_PERIOD_MS = TimeUnit.MILLISECONDS.convert(30l, TimeUnit.MINUTES);
    long lastRestart;

    public Thread schedulerThread = null;

    List<Task> active = new ArrayList<Task>();
    Map<Task, Long> recentlyTriggered = new HashMap<Task, Long>();

    @EJB
    TaskGenEAO.L taskEao;
    Logger log = Logger.getLogger(Scheduler.class);
    
    private Map<Task.Type,Set<Listener>> typeListeners = Collections.synchronizedMap(new HashMap<Task.Type,Set<Listener>>());
    

    public static Scheduler getInstance() {
        return Instance;
    }
    
    @Override
    protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.debug("\n\n\n\ninit scheduler servlet\n\n\n\n");
        super.init(config);
        Thread oldScheduler = ThreadUtil.find(SCHEDULER_THREAD);
        if (oldScheduler != null) {
            try {
                oldScheduler.interrupt();
            } catch (Exception e) {
            }
        }
        lastRestart = System.currentTimeMillis();
        updateTasks();
        startScheduler();
        Instance = this;
    }

    private void updateTasks() { 
        if(taskEao.available()) {
            active = taskEao.findActive(System.currentTimeMillis());
            log.debug("found " + active.size() + " active tasks");
        }else {
            //log.error("fetching active tasks, eao reported unavailable");
        }
    }

    public boolean addListener(Task.Type type, Listener listener) {
        Set<Listener> listeners = typeListeners.get(type);
        if(listeners == null) {
            listeners = new HashSet<Listener>();
            typeListeners.put(type, listeners);
        }
       
        return listeners.add(listener);
    }
    
    public boolean removeListener(Task.Type type, Listener listener) {
        Set<Listener> listeners = typeListeners.get(type);
        if(listeners == null) {
            listeners = new HashSet<Listener>();
            typeListeners.put(type, listeners);
        }
        return listeners.remove(listener);
    }

    public void check() {
        long now = System.currentTimeMillis();
        long elapsed;
        long elapsedPeriods;
        long lastCycle;
        long period;
        long lastTaskTrigger;
        long stopMillis;
        long startMillis;
        
        updateTasks();

        for (Task t : active) {
            startMillis = t.getStartMillis();
            if(startMillis > now) {
                log.warn("got future task in active");
                continue; 
            }
            stopMillis = t.getStopMillis();
            elapsed = now - startMillis;
            period = t.getPeriod();
            elapsedPeriods = elapsed / period;
            lastCycle = period != 0 ? elapsedPeriods * period + startMillis : stopMillis;
            lastTaskTrigger = t.getLastTrigger();
            if(lastTaskTrigger < lastCycle ) {
                try {
                    if (t.isRecurring() ) {
                        // lastTrigger is stale
                        if(stopMillis < 0 || stopMillis > lastCycle) {
                            // trigger if never-ending of not expired
                            t.setLastTrigger(now);
                            if(taskEao.available()) {
                                taskEao.update(t);
                            }
                            trigger(t);
                        }
                    } else if(lastTaskTrigger == 0 && stopMillis < now) {
                        t.setLastTrigger(now);
                        if(taskEao.available()) {
                            taskEao.update(t);
                        }
                        trigger(t);
                    }
                }catch(Exception e) {
                    log.error("updating Tasks", e);
                }
            } 
        }
    }

    public void trigger(Task t) {
        Set<Listener> listeners = typeListeners.get(t.getType());
        if(listeners != null) {
            for(Listener l : listeners) {
                l.trigger(t);
            }
        }
    }

    public void startScheduler() {
        if (taskEao.available() && schedulerThread == null) {
            schedulerThread = new Thread(SCHEDULER_THREAD) {
                @EpicMethod
                public void run() {
                    while (true) {
                        try {
                            check();
                            Thread.sleep(SCHEDULER_PERIOD_MS);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            };
            schedulerThread.start();
        }
    }

    public void stopSheduler() {
        if (schedulerThread != null) {
            System.out.println("interrupting " + schedulerThread.toString());
            schedulerThread.interrupt();
            schedulerThread = null;
        }
    }

    public void finalize() {
        stopSheduler();
    }

}
