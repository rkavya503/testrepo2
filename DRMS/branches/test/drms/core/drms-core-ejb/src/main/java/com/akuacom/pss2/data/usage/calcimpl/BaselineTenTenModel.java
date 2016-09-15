package com.akuacom.pss2.data.usage.calcimpl;

import javax.ejb.Local;
import javax.ejb.Remote;


public interface BaselineTenTenModel extends BaselineMod {
    @Remote
    public interface R extends BaselineTenTenModel {}
    @Local
    public interface L extends BaselineTenTenModel {}

}