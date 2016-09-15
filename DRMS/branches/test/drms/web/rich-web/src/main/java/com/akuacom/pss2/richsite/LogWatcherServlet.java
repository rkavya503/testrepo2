package com.akuacom.pss2.richsite;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
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

import scala.Tuple2;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.richsite.LogConfiguration.Frequency;
import com.akuacom.pss2.system.LogManager;
import com.akuacom.pss2.system.LogManagerBean;
import com.akuacom.pss2.system.Task;
import com.akuacom.pss2.system.TaskGenEAO;
import com.akuacom.pss2.system.TaskGenEAOBean;
import com.akuacom.utils.NumberFormats;
import com.akuacom.utils.lang.ClassUtil;
import com.akuacom.utils.lang.Dbg;
import com.akuacom.utils.lang.FileUtil;
import com.akuacom.utils.lang.Headroom;
import com.akuacom.utils.lang.LoggingTuples;
import com.akuacom.utils.lang.TimingUtil;
import com.akuacom.utils.lang.Util;

public class LogWatcherServlet extends HttpServlet implements
        Headroom.Listener, Scheduler.Listener {

    private static final long serialVersionUID = 1L;
    public static LogWatcherServlet Instance;
    final int MINIMUM_TRUNCATION_PERIOD = 5 * TimingUtil.MINUTE_MS;

    private String hostname;
    
    @EJB
    LogManager.L logManager;
    @EJB
    TaskGenEAO.L taskEao;
    
    Logger log = Logger.getLogger(LogWatcherServlet.class);

    Thread logfileWatcherThread;
    long lastSize = 0;

    Headroom headroom = new Headroom(.2); // .2 => triggers when <=
                                          // 20% free space remaining on path
    boolean updated = false;

    LogConfiguration logConfiguration;
    
    long lastTruncation;
    
    public LogWatcherServlet() {
        log.info("\n\n\nnew LogWatcherServlet");
    }

    @Override
    protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        Instance = this;
        
        log.info("\n\n\n\ninit logwatcher servlet\n\n\n\n");
        super.init(config);
        try {
            if(logManager == null) {
                logManager = EJB3Factory.getLocalBean(LogManagerBean.class);
                log.info(ClassUtil.dumpIfs(logManager));
                log.info(ClassUtil.dumpClasses(logManager));
            }
            Task wt = new Task();
            Class wc = wt.getClass();
            if(taskEao == null) {
                taskEao = EJB3Factory.getLocalBean(TaskGenEAOBean.class);
            }
            
            headroom.addHeadroomPath(System.getProperty("user.dir"));
            headroom.addListener(this);
            headroom.check();
            log.info("headrooms " + Dbg.oS(headroom.getHeadrooms()));
            try {
                marshallLogConfiguration();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InetAddress[] ias = InetAddress.getAllByName(InetAddress.getLocalHost().getCanonicalHostName());
                for(InetAddress ia : ias) {
                    if(!ia.isLoopbackAddress()) {
                        hostname = ia.toString();
                        break;
                    }
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            if(logConfiguration != null) {
                if(logConfiguration.isTrackingMemory()) {
                    Util.startMemwatcher();
                } else {
                    Util.stopMemwatcher();
                }
            }
        }catch(Exception e) {
            String msg = e.toString();
            if(msg.indexOf("is not mapped") != -1) {
                log.info("Task and Quotation are not mapped");
            } else {
                log.error(e);
            }
        }
        
    }

    public void finalize() {
        if (logfileWatcherThread != null) {
            logfileWatcherThread.interrupt();
        }
        headroom.stopHeadroomWatcher();
    }

    @Override
    public void headroom(Set<Tuple2<String, Double>> tuples) {
        if(!tuples.isEmpty()) {
            log.info("free space below threshold: " + Dbg.oS(tuples));
            whackLog();
        }

    }

    public LogConfiguration getLogConfiguration() {
        return logConfiguration;
    }

    public void setLogConfiguration(LogConfiguration logConfiguration) {
        this.logConfiguration = logConfiguration;
    }

    public String whackLog() {
        whackLog(.5, logConfiguration);
        return "success";
    }

    public void whackLog(double pct, LogConfiguration cfg) {
        long now = System.currentTimeMillis();
        if(lastTruncation + MINIMUM_TRUNCATION_PERIOD < now) {
            logManager.partition(pct, cfg.getRemnantFile(),
                cfg.getCurrentDispatch() == LogConfiguration.Dispatch.ZipCopy
                        .ordinal(), cfg.getCurrentDispatch() == LogConfiguration.Dispatch.Delete.ordinal());
            lastTruncation = now;
            // 
            if(logConfiguration.isAggressivePruning()) {
                log.info("resetting aggprun log index ");
                LoggingTuples.clear(pct);
            }
        }
    }

    public static LogWatcherServlet getInstance() {
        return Instance;
    }

    private File getConfigurationFile() throws IOException {
        File temp = File.createTempFile("blah", "blah");
        File dir = temp.getParentFile();
        if (dir.isDirectory()) {
            return new File(dir.getAbsolutePath() + File.separator
                    + LogConfiguration.class.getSimpleName() + ".xml");
        }

        return new File(LogConfiguration.class.getSimpleName() + ".xml");
    }
    
    
    public String getHeadroomStr() {
        headroom.check();
        StringBuffer sb = new StringBuffer();
        for(Tuple2<String,Double> t : headroom.getHeadrooms()) {
            sb.append(t._1);
            sb.append(" -> ");
            sb.append(NumberFormats.decimal3(100 * t._2));
            sb.append("% free space still available");
            sb.append("\n");
        }
        return sb.toString();
    }
    public void setHeadroomStr() {}

    public String getLogLength() {
        long length = logManager.logLength();
        double lengthMb = length / 1000000.;
        return Util.memnf.format(lengthMb) + "Mb";
    }
    
    public void setLogLength() {}
    
    private void marshallLogConfiguration() throws IOException {
        File temp = getConfigurationFile();
        if (temp.length() > 0) {
            logConfiguration = FileUtil.fromFile(temp.getAbsolutePath(),
                    LogConfiguration.class);
            log.debug("marshalled " + logConfiguration);
        } else {
            log.debug("no config file, creating");
            logConfiguration = new LogConfiguration();
        }
    }

    public void updateLogConfiguration(Map<String, Object> map) {
        try {
            log.debug("updating " + this);
            ClassUtil.fromMap(logConfiguration, map);
            File temp = getConfigurationFile();
            FileUtil.toFile(logConfiguration, temp.getAbsolutePath());
            Frequency freq = LogConfiguration.Frequency.values()[logConfiguration
                    .getCurrentFrequency()];
            Task logWatcherT = null;
            try {
                logWatcherT = taskEao.findByNameAndType(hostname, Task.Type.LogTruncation);
            } catch(Exception e) {
                log.error("fetching tasks " +  e.toString());
            }
            if( logWatcherT  == null) {
                logWatcherT = new Task();
                logWatcherT.setType(Task.Type.LogTruncation);
                logWatcherT.setStartMillis(System.currentTimeMillis());
                logWatcherT.setStopMillis(-1);
                logWatcherT.setName(hostname);
                try {
                    logWatcherT = taskEao.create(logWatcherT);
                }catch(Exception e) {
                    log.error("creating task " +  e.toString());
                }
            }
            long period = logWatcherT.getPeriod();
            if (freq != Frequency.Never) {
                switch (freq) {
                case Daily:
                    period =  TimeUnit.MILLISECONDS.convert(1l,TimeUnit.DAYS);
                    break;
                case Weekly:
                    period = TimeUnit.MILLISECONDS.convert(7l,TimeUnit.DAYS);
                    break;
                case BiWeekly:
                    period = TimeUnit.MILLISECONDS.convert(14l,TimeUnit.DAYS);
                    break;
                case Monthly: // 30.42 days
                    period = TimeUnit.MILLISECONDS.convert(30l,TimeUnit.DAYS) + TimeUnit.MILLISECONDS.convert(10l,TimeUnit.HOURS);
                }
            }
            logWatcherT.setPeriod(period);
            try {
                taskEao.update(logWatcherT);
            }catch(Exception e) {
                log.error("creating task " +  e.toString());
            }
            
            if(logConfiguration.isTrackingMemory()) {
                Util.startMemwatcher();
            } else {
                Util.stopMemwatcher();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getMap() {
        try {
            log.info("getting logConfiguration map");
            // force update for ui via LogConfiguration
            headroom.check();
            headroom.announce();
            if (logConfiguration == null) {
                marshallLogConfiguration();
            }
            log.debug("have logConfiguration " + logConfiguration);
            return ClassUtil.toMap(logConfiguration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.<String, Object> emptyMap();
    }

    @Override
    public void trigger(Task t) {
        log.info("triggered by " + t);
        whackLog(.999, logConfiguration);
    }

}