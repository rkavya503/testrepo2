package com.akuacom.pss2.email;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.akuacom.ejb.BaseEAO;


public interface PeakChoiceMessageEAO extends BaseEAO<PeakChoiceMessageEntity> {
    @Remote
    public interface R extends PeakChoiceMessageEAO {}
    @Local
    public interface L extends PeakChoiceMessageEAO {}
}
