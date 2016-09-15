package com.akuacom.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.akuacom.utils.lang.FileUtil;

public class ZipcodeUtil {

    final private static String SER_FILE = "zipnov99.ser";
    final private static String CSV_FILE = "zipnov99.csv";
    static Logger log = Logger.getLogger(ZipcodeUtil.class.getSimpleName());

    public static class ZipDetail implements Serializable {
        private static final long serialVersionUID = 1L;
        public final int zipcode;
        public final double latitude,longitude;
        public String zipClass;
        public String poName;
        public final int state;
        public final int county;
        public ZipDetail(String...vals) {
           zipcode = Integer.parseInt(vals[0]);
           latitude =Double.parseDouble(vals[1]);
           longitude=Double.parseDouble(vals[2]);
           zipClass =vals[3];
           poName = vals[4];
           state = Integer.parseInt(vals[5]);
           county = Integer.parseInt(vals[6]);
        }
    }

    private static Map<Integer, ZipDetail> zipcodeDetails = null;

    private static void initialize() {
        try {
            InputStream is = ZipcodeUtil.class.getResourceAsStream(SER_FILE);
            if (is != null) {
                zipcodeDetails = (Map<Integer, ZipDetail>) FileUtil
                        .fromStream(false, is, Map.class);
                log.info("loaded " + zipcodeDetails.keySet().size() + " from jar file");
                
            } else {
                if (new File(SER_FILE).exists()) {
                    zipcodeDetails = (Map<Integer, ZipDetail>) FileUtil
                            .fromFile(SER_FILE, Map.class);
                    log.info("loaded " + zipcodeDetails.keySet().size() + " from reg file");
                } else if (new File(CSV_FILE).exists()) {
                    zipcodeDetails = new HashMap<Integer, ZipDetail>();
                    BufferedReader br = new BufferedReader(new FileReader(
                            CSV_FILE));
                    String line;
                    String[] vals;
                    boolean first = true;
                    ZipDetail ze;
                    while ((line = br.readLine()) != null) {
                        line = line.replace("\"", "");
                        vals = line.split(",");
                        if (first) {
                            first = false;
                            continue;
                        }
                        ze = new ZipDetail(vals);
                        zipcodeDetails.put(ze.zipcode, ze);
                     }
                    
                    log.info("loaded " + zipcodeDetails.keySet().size() + " from csv file");
                    FileUtil.toFile(zipcodeDetails, SER_FILE);
                    log.info("saved " + zipcodeDetails.keySet().size() + " to ser file");
                    br.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.severe(e.getMessage());
        }
    }

    public static synchronized  Map<Integer, ZipDetail> getZipcodes() {
        if(zipcodeDetails == null) {
            initialize();
        }
        return zipcodeDetails;
    }

    public static synchronized ZipDetail details(int zipcode) {
        if(zipcodeDetails == null) {
            initialize();
        }
        return zipcodeDetails.get(zipcode);
    }

}
