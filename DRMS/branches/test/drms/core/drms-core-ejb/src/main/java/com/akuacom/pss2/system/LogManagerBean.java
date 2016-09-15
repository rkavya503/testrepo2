package com.akuacom.pss2.system;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.akuacom.utils.lang.FileUtil;

@Stateless
public class LogManagerBean implements LogManager.L, LogManager.R {
    Logger log = Logger.getLogger(LogManagerBean.class);
    final String logDir = System.getProperty("jboss.server.log.dir");
    final String logPath = logDir + ( (logDir != null && logDir.endsWith(File.separator) ) ? "" : File.separator) +  "server.log";

    final static String LOG_UNAVAILABLE = "Log Unavailable";

    public LogManagerBean() {}
    
    @Override
    public String getLogDir() {
        return logDir;
    }

    @Override
    public String getLog(int position, int size) {
        try {
            return FileUtil.fromFile(new File(logPath), position, size);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return LOG_UNAVAILABLE;
        }
    }

    @Override
    public long logLength() {
        try {
            return new File(logPath).length();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1;
        }
    }

    static final int BUFFER = 2048;

    public String zipPath(String path) {
        return path.indexOf(".") == -1 ? path : path.substring(0, path.lastIndexOf(".")) + ".zip";
    }

    public void zip(String src) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipPath(src));
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            FileInputStream fi = new FileInputStream(src);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(src);
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void truncate(double pct) {
        try {
            File f = File.createTempFile("logmanager", ".trunc");
            partition(pct, f.getAbsolutePath(), false, true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void partition(double pct, String remnantPath, boolean zip, boolean delete) {
        try {
            File remnant = remnantPath == null ? null : new File(remnantPath);
            FileUtil.truncateFromBeginning(new File(logPath), pct, remnant);
            if (remnant != null ) {
                if(delete) {
                    remnant.delete();
                } else if(zip) {
                    zip(remnantPath);
                    remnant.delete();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
