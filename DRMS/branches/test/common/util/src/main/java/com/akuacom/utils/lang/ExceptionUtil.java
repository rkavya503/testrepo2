package com.akuacom.utils.lang;

import com.akuacom.utils.lang.LoggingTuples;
import static com.akuacom.utils.lang.LoggingTuples.EndToken;
import static com.akuacom.utils.lang.LoggingTuples.MessageToken;
import static com.akuacom.utils.lang.LoggingTuples.StartToken;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import scala.Tuple2;

public class ExceptionUtil {
    public static Throwable root(Exception e) {
        Throwable t = e;
        while(t.getCause() != null) {
            t = t.getCause();
        }
        return t;
    }
    
    public static boolean rootQ(Exception e, Class<? extends Throwable> root) {
        Throwable t = e;
        while(t.getCause() != null) {
            t = t.getCause();
        }
        return root.isAssignableFrom(t.getClass());
    }
    public static String path(Throwable t) {
        StringBuffer sb = new StringBuffer(t.toString());
        while(t.getCause() != null) {
            sb.append(">");
            sb.append(t.getCause());
            t = t.getCause();
        }
        return sb.toString();
    }
    
    public static String logOnce(Throwable t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        t.printStackTrace(new PrintStream(baos));
        String val = baos.toString();
        Tuple2<Integer,Integer> hashLength = null;
        if(val != null) {
            hashLength = new Tuple2<Integer,Integer>(val.hashCode(), val.length());
            if(LoggingTuples.indexOf(hashLength) > -1) {
                return StartToken+ hashLength+ EndToken;
            } else {
                LoggingTuples.add(hashLength);
            }
        }
        return hashLength+MessageToken+val;
    }

}
