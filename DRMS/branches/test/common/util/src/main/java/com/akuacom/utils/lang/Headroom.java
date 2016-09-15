package com.akuacom.utils.lang;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import scala.Tuple2;

import com.akuacom.annotations.EpicMethod;
import com.akuacom.annotations.Trace;

@Trace
public class Headroom {
    
    public static interface Listener {
        void headroom(Set<Tuple2<String, Double>> tuples);
    }
    
    final static Logger log = Logger.getLogger(Headroom.class);
    
    public final static String HEADROOM_THREAD = "headThread";
    final static int HEADROOM_PERIOD_MS = 20 * TimingUtil.SECOND_MS;

    public Thread headroomThread = null;

    private Set<Tuple2<String, Double>> headrooms = Collections
            .<Tuple2<String, Double>> synchronizedSet(new HashSet<Tuple2<String, Double>>());
    
    private Set<Listener> headroomListener = Collections.synchronizedSet(new HashSet<Listener>());
    
    private double threshold;
    
    public Headroom() {
        threshold = .2;
    }
    
    public Headroom(double threshold) {
        this.threshold = threshold;
    }
    
    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public Set<Tuple2<String, Double>> getHeadrooms() {
        return headrooms;
    }

    public void setHeadrooms(Set<Tuple2<String, Double>> headrooms) {
        this.headrooms = headrooms;
    }

    public void addHeadroomPath(String path) {
        File f = new File(path);
        if(!f.exists() || !f.isDirectory()) {
            throw new IllegalArgumentException(path  + " must exist and be a directory");
        }
        for( Tuple2<String,Double> tup : new HashSet <Tuple2<String,Double>>(headrooms)) {
            if(tup._1.equals(path)) {
                log.debug("already monitoring headroom at " + path);
                return;
            }
        }
        Tuple2<String,Double> nuple= new  Tuple2<String,Double>(path, -1.);
        headrooms.add(nuple);
    }
    
    public boolean addListener(Listener listener) {
        return headroomListener.add(listener);
    }
    
    public boolean removeListener(Listener listener) {
        return headroomListener.remove(listener);
    }

    public void announce() {
        
        Set<Tuple2<String,Double>> interest = new HashSet<Tuple2<String,Double>>();

        for(Tuple2<String, Double> hr : headrooms) {
            if(hr._2 > 0 && hr._2 < threshold) {
                interest.add(hr);
            }
        }
        log.debug("interest " + Dbg.oS(interest));
       
        if(!interest.isEmpty()) {
            for(Listener hl : headroomListener) {
                hl.headroom(interest);
            }
        }

    }
    
    public void check() {

        Set<Tuple2<String,Double>> replacements = new HashSet<Tuple2<String,Double>>();
        for(Tuple2<String, Double> hr : headrooms) {
            replacements.add(new Tuple2<String, Double>(hr._1, FileUtil.freeSpaceN(hr._1)));
        }
        headrooms = Collections.synchronizedSet(replacements);
    	log.debug("headrooms " + Dbg.oS(headrooms));
    }
    
    
    public void startHeadroomWatcher() {
        if (headroomThread == null) {
            headroomThread = new Thread(HEADROOM_THREAD) {
                @EpicMethod
                public void run() {
                    while (true) {
                        System.gc();
                        try {
                            check();
                            announce();
                            Thread.sleep(HEADROOM_PERIOD_MS);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            };
            headroomThread.start();
        }
    }

    public void stopHeadroomWatcher() {
        if (headroomThread != null) {
            System.out.println("interrupting " + headroomThread.toString());
            headroomThread.interrupt();
            headroomThread = null;
        }
    }
    
    public void finalize() {
        stopHeadroomWatcher();
    }
    
}
