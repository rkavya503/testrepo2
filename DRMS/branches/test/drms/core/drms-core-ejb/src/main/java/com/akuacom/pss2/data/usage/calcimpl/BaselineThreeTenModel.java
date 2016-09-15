package com.akuacom.pss2.data.usage.calcimpl;

import javax.ejb.Local;
import javax.ejb.Remote;


public interface BaselineThreeTenModel extends BaselineMod {
    @Remote
    public interface R extends BaselineThreeTenModel {}
    @Local
    public interface L extends BaselineThreeTenModel {}

}