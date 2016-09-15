// $Revision: 1.9 $ $Date: 2010-04-27 22:42:29 $
package com.akuacom.pss2.program.rtp;

import javax.ejb.Stateless;

@Stateless
public class RTP_IESOMarketRTP_ProgramEJBBean extends RTPProgramEJBBean
        implements RTPDAMProgramEJB.R, RTPDAMProgramEJB.L {

    @Override
    protected String getThisProgramName() {
        return "IESO MarketRTP";
    }

    @Override
    protected String getPriceConnectorClassName() {
        return "com.akuacom.pss.ieso.MarketRTPClient";
    }

}
