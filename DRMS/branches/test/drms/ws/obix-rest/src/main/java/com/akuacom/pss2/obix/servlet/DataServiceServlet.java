package com.akuacom.pss2.obix.servlet;

import static com.akuacom.utils.lang.ThreadUtil.T;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import scala.Tuple2;

import com.akuacom.annotations.EpicMethod;
import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.DataManagerBean;
import com.akuacom.pss2.obix.dataservice.DataServiceBean;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantManagerBean;
import com.akuacom.utils.NumberFormats;
import com.akuacom.utils.lang.ClassUtil;
import com.akuacom.utils.lang.ExceptionUtil;
import com.akuacom.utils.lang.FieldUtil;
import com.akuacom.utils.lang.ThreadUtil;
import com.akuacom.utils.lang.Util;

/**
 * 
Operation:

On start up, the init method of the DataServiceServlet creates a pool of ObixThreads, 
whose size is proportional to the number of cores.  

As each obix client submission comes in,  the DataServiceServlet adds the raw XML 
string to a queue and wakes up any sleeping ObixThreads.   Each woken thread in turn pops
a submission off the queue and processes it.  After processing (which involves first 
removing the mostly redundant information from the submission and then saving any
new data), each ObixThread tries to get another XML submission,  but if the queue 
is empty, goes back to sleep.   There is another thread, ObixDupThread, that occasionally
wakes up and searches for synthetic data (that was generated to fill in missing data for a
report and distinguished by a value of actual=0) and deletes it; this functionality may now 
be obsolete.

Ideally, this setup will produce a saw-horse-shaped load graph, where load peaks 
right after a common submission time (when every obix client 'phones home') and 
then drops back to idle after all the client data is processed; as long as there 
is enough time between scheduled submission times, the system will recover and 
be stable.

Evolution:

Ensuring that a given thread was actually in the state it claimed to be was quite
problematic; specifically, there seemed to be a race condition between waking
a thread and updating its status to show that it was in fact now doing work.  
The current version seems to be stable but is probably overdoing the tests, especially 
the test where pieces of a thread's stack trace are examined -- code that ugly can't
be necessary.  I believe that a much better implementation would be achieved 
using scala/erlang actors...

Authentication happens via the AcctMgr ear, which means that a giving submission 
from an obix client goes to the DataServiceServlet, then, via soap, to a servlet 
in the AcctMgr ear for authentication, then back to the DataServiceServlet.  If the 
authentication could happen without leaving the war that contains the DataServiceServlet, 
more socket resources would be available for scaling.

The obix clients could help a great deal if they staggered their communication times 
so that it was much less likely that a  bunch of them would all talk to the DRMS 
simultaneously, but this would require some politics.   Likewise,  most of the data 
sent by a client is redundant; if this redundant information were ordered and we used 
our own XML parser, we could stop processing once we got to stale data.


 *
 */
public class DataServiceServlet extends HttpServlet {
    private final static long serialVersionUID = 1L;
    private final static boolean DEBUG = false;
    private final static boolean METRICS = System.getProperty("metrics") != null;
    
    static Logger log = Logger.getLogger(DataServiceServlet.class);

    private static DataServiceBean dataService = new DataServiceBean();

    private final static String SPOOFED_RESPONSE = "<obj name=\"usage\" href=\"http://localhost:8080/obixserver/obix/dataService/\" />";
    private final static String OBIX_THREAD = "oBIX Thread";
    private final static String OBIX_DUP_THREAD = "oBIX DUP Thread";
    private static int Procs = Runtime.getRuntime().availableProcessors();
    private final static int OBIX_THREAD_COUNT = Procs > 7 ? Procs / 2 : Procs;

    /**
     * postCount is number of oBIX client posts
     */
    private long postCount = 0;  
    private long start = System.currentTimeMillis();
    
    private boolean available = false;
    /**
     * postDataLength is total number of chars posted by oBIX clients
     */
    private long postDataLength = 0;

    /**
     * the stack of (ownerId, transmitted XML) tuples
     */
    static List<Tuple2<String, String>> oBixXmlDeque = Collections
            .synchronizedList(new LinkedList<Tuple2<String, String>>());
    
    /**
     * worker threads
     */
    ObixThread[] oBixThreads = new ObixThread[OBIX_THREAD_COUNT];
    
    /**
     * each ObixThread pops obix transmissions (as XML document Strings) off the stacj
     *
     */
    private static class ObixThread extends Thread implements Serializable {
        private static final long serialVersionUID = 1L;
        private volatile boolean threadSuspended = true;
        
        public ObixThread(String name) {
            super(name);
        }

        @EpicMethod
        private void doWait() throws InterruptedException {
            wait();
        }

        @EpicMethod
        public void run() {
            if(DEBUG)log.info(toString() + " started");
            while (true) {
                try {
                    if (threadSuspended) {
                        synchronized (this) {
                            while (threadSuspended) {
                                // if(DEBUG)log.info( ObixThread.this + " sleeping");
                                doWait();
                            }
                        }
                    }
                } catch (InterruptedException eaten) {
                    threadSuspended = false;
                }

                int size = oBixXmlDeque == null ? -1 : oBixXmlDeque.size();
                if (size > 0 ) {
                    Tuple2<String, String> tup = null;
                    synchronized(oBixXmlDeque) {
                        try {
                            tup = oBixXmlDeque.remove(0);  
                        }catch(IndexOutOfBoundsException ioobeEaten) {}
                    }
                    if(DEBUG)log.info(T() + " got " + tup._1 + "/\n"+ tup._2 +  "\n from stack");
                    if(tup != null) {
                        try {
                            dataService.service(tup._1, tup._2);
                        } catch (Exception e) {
                            if(!ExceptionUtil.rootQ(e, InterruptedException.class) ) {
                                log.error(ObixThread.this + " got ex path " + ExceptionUtil.path(e));
                            }
                            if(ExceptionUtil.rootQ(e, OutOfMemoryError.class)) {
                                log.error(ObixThread.this + " halting due to low memory");
                                break;
                            }
                        }
                    }
                } else {
                    threadSuspended = true;
                }
            }
        }
    }

    protected String threadStatus() {
        String status = "";
        for (int i = 0; i < OBIX_THREAD_COUNT; i++) {
            if (oBixThreads[i].getState() == Thread.State.BLOCKED) {
                if(DEBUG)log.info(oBixThreads[i] + " is blocked");
            }
            status += oBixThreads[i] + "->" + oBixThreads[i].getState();
            if (i < OBIX_THREAD_COUNT - 1) {
                status += ", ";
            }
        }
        return status;
    }
    
    private void initObixThreads() {
        ObixThread oldObix;
        String name;
        for (int i = 0; i < OBIX_THREAD_COUNT; i++) {
            name = OBIX_THREAD + "-" + i;
            oldObix = ThreadUtil.find(name);
            if (oldObix != null) {
                if(DEBUG)log.info("replacing " + oldObix);
                try {
                    oldObix.interrupt();
                } catch (Exception eaten) {
                }
            }
            oBixThreads[i] = new ObixThread(name);
            oBixThreads[i].start();
        }

        oldObix = ThreadUtil.find(OBIX_DUP_THREAD);
        if (oldObix != null) {
            FieldUtil.set(oldObix, "running", false);
            oldObix.interrupt();
            //oBixDupThread = null;
        }
        //NO NEED anymore, there is no dupilcate key constraints on this table
        //oBixDupThread = new ObixDupThread(OBIX_DUP_THREAD);
        //oBixDupThread.setServlet(this);
        //oBixDupThread.start();
    }
    
    
    final static String StrObixRunTrace = ".*\\QObixThread.doWait(DataServiceServlet.java:\\E\\d\\d\\d?\\)\\}";
    final static Pattern ObixRunTrace = Pattern.compile(StrObixRunTrace);
    static Method DoWait;
    static {
        try {
            DoWait = ObixThread.class.getDeclaredMethod("doWait",Util.VOID_SIG);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private synchronized void awakenObixThreads() {
        ObixThread oldObix;
        for (int i = 0; i < OBIX_THREAD_COUNT; i++) {
            oldObix = oBixThreads[i];
            // TODO replace with actors
            try {
                if (oldObix != null && oldObix.threadSuspended ) {
                    synchronized(oldObix) {
                        if(oldObix.threadSuspended) {
                            oldObix.threadSuspended = false;
                            oldObix.interrupt();
                        }
                    }
                }
            } catch (Exception outer) {
                log.error(outer);
            }
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ParticipantManager.L pm = (ParticipantManager.L) EJBFactory
                .getLocalBean(ParticipantManagerBean.class);
        log.info(ClassUtil.dumpIfs(pm));
        dataService.setParticipantManager(pm);

        DataManager.L dm = (DataManager.L) EJBFactory
                .getLocalBean(DataManagerBean.class);
        log.info(ClassUtil.dumpIfs(dm));

        dataService.setDataManager(dm);

        initObixThreads();
        
        available = true;
    }

    @Override
    protected void doDelete(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        notAllowed(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        notAllowed(request, response);
    }

    @Override
    protected void doHead(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        notAllowed(request, response);
    }

    @Override
    protected void doOptions(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        notAllowed(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        notAllowed(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        if(!available) {
            unavailable(request, response);
            return;
        }
            final ServletInputStream inputStream = request.getInputStream();
            final InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream);
            final BufferedReader br = new BufferedReader(inputStreamReader);

            final StringBuffer sb = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

            if(METRICS) {
                postDataLength += sb.length();
                postCount++;
                
                if(postCount % 10 == 0) {
                    logMetrics();
                }
            }
            oBixXmlDeque.add(new Tuple2<String, String>(request
                    .getUserPrincipal().getName(), sb.toString()));
            awakenObixThreads();
            final ServletOutputStream os = response.getOutputStream();
            // immediately return a fake 'success'; data will be processed asynchronously
            // by the ObixThreads
            os.print(SPOOFED_RESPONSE);
            os.flush();
    }
    
    private void logMetrics() {
        String ratio = postCount == 0 ? "0.000"
                : NumberFormats.decimal3(1.
                        * postDataLength
                        / postCount);
    
        double rate =  1000.* postCount / (System.currentTimeMillis() - start);
        log.info( "total posts " + postCount + "; avg rate " + rate + " subs/sec; data rate " + ratio + " bytes/sub; stack now at " + oBixXmlDeque.size());
    }

    @Override
    protected void doTrace(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        notAllowed(request, response);
    }

    private void notAllowed(HttpServletRequest request,
            HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    private void unavailable(HttpServletRequest request,
            HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }

    public void finalize() {
        Thread oldObix;
        String name;
        for (int i = 0; i < OBIX_THREAD_COUNT; i++) {
            name = OBIX_THREAD + "-" + i;
            oldObix = ThreadUtil.find(name);
            if (oldObix != null) {
                if(DEBUG)log.info("interrupting " + oldObix);
                try {
                    oldObix.interrupt();
                } catch (Exception eaten) {
                }
            }
        }
    }
}
