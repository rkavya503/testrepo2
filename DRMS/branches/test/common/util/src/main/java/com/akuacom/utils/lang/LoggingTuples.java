package com.akuacom.utils.lang;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import scala.Tuple2;
import scala.Tuple3;

public class LoggingTuples {
    
    public final static String StartToken = "[(<";
    public final static String EndToken = ">)]";
    public final static String MessageToken = ")==>";
    
    private final static List<Tuple3<Integer,Integer, Long>> MessageHashesAndLengthsAges = new ArrayList<Tuple3<Integer,Integer,Long>>();
    
    public static synchronized int size() {
        return MessageHashesAndLengthsAges.size();
    }
    
    public static synchronized int indexOf(Tuple2<Integer,Integer> key) {
        if(!MessageHashesAndLengthsAges.isEmpty()) {
            Tuple3<Integer,Integer, Long> t;
            for(int i = MessageHashesAndLengthsAges.size() -1; i >= 0; i--) {
                t = MessageHashesAndLengthsAges.get(i);
                if(t._1().equals( key._1()) && t._2().equals(key._2())) {
                    return i;
                }
            }
        } 
        return -1;
    }
    
    public static synchronized boolean add(Tuple2<Integer,Integer> key, int... idxs) {
        int idx = (idxs != null && idxs.length == 1)
                        ? idxs[0] : indexOf(key);
        Tuple3<Integer,Integer,Long> t = new Tuple3<Integer,Integer,Long>(key._1(), key._2(), System.currentTimeMillis());
        if(idx < 0) {
            MessageHashesAndLengthsAges.add(t);
        } else {
            MessageHashesAndLengthsAges.set(idx,t );
        }
        return idx == -1;
    }
    
    public static synchronized int clear(long ageMillis) {
        if(ageMillis < 0) {
            try {
                return MessageHashesAndLengthsAges.size();
            } finally {
                MessageHashesAndLengthsAges.clear();
            }
        }            
            
        int orgSize=-1;
        orgSize = MessageHashesAndLengthsAges.size();
        Iterator<Tuple3<Integer, Integer, Long>> i = MessageHashesAndLengthsAges.iterator();
        Tuple3<Integer, Integer, Long> entry;
        long now = System.currentTimeMillis();
        while(i.hasNext()) {
            entry = i.next();
            if(now - entry._3() > ageMillis) {
                i.remove();
            }
        }
        return orgSize - MessageHashesAndLengthsAges.size();
    }
    
    // n is (0,1]
    public static synchronized int clear(double n) {
        int oldSize = MessageHashesAndLengthsAges.size();
        int chunk = Math.round(oldSize * (float)n);
        chunk = chunk > oldSize ? oldSize : chunk;
        List<Tuple3<Integer,Integer, Long>> survivors = new ArrayList<Tuple3<Integer,Integer, Long>>(MessageHashesAndLengthsAges.subList(oldSize-chunk-1, oldSize-1)); 
        MessageHashesAndLengthsAges.clear();
        MessageHashesAndLengthsAges.addAll(survivors);
        return chunk;
    }
    
    public static String dump() {
        return Dbg.oS(MessageHashesAndLengthsAges);
    }
    
}
