package com.akuacom.pss2.email;

import javax.ejb.Stateless;

import com.akuacom.ejb.BaseEAOBean;

@Stateless
public class PeakChoiceMessageEAOBean extends BaseEAOBean<PeakChoiceMessageEntity> implements PeakChoiceMessageEAO.R, PeakChoiceMessageEAO.L {
    public PeakChoiceMessageEAOBean() {
        super(PeakChoiceMessageEntity.class);
    }
}
