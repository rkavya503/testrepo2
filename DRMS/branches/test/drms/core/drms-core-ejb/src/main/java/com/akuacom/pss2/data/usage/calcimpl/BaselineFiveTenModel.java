package com.akuacom.pss2.data.usage.calcimpl;

import javax.ejb.Local;
import javax.ejb.Remote;


public interface BaselineFiveTenModel extends BaselineMod {
    @Remote
    public interface R extends BaselineFiveTenModel {}
    @Local
    public interface L extends BaselineFiveTenModel {}

}