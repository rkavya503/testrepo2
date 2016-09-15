package com.akuacom.pss2.email;

import javax.ejb.Local;
import javax.ejb.Remote;


public interface PeakChoiceEmailReader {
    @Remote
    public interface R extends PeakChoiceEmailReader {}
    @Local
    public interface L extends PeakChoiceEmailReader {}

    void poll();
}
