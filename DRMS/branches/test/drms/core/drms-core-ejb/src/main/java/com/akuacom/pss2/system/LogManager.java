package com.akuacom.pss2.system;

import javax.ejb.Local;
import javax.ejb.Remote;

public interface LogManager {
    @Remote
    public interface R extends LogManager {}
    @Local
    public interface L extends LogManager {}

    String getLogDir();
    String getLog(int position, int size);
    long logLength();
    void truncate(double pct);
    void partition(double pct, String remnantPath, boolean zip, boolean delete);
}
