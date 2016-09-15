package com.akuacom.pss2.system;

import javax.ejb.Local;
import javax.ejb.Remote;

public interface QodService extends QuotationGenEAO {
    @Remote
    public interface R extends QodService {}
    @Local
    public interface L extends QodService {}
 
    void initialize();
 
    Quotation nextQod();
    String nextQodHtml();
}
