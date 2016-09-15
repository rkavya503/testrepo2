// $Revision: 1.10 $ $Date: 2010-04-27 22:42:29 $
package com.akuacom.pss2.program.rtp;

import javax.ejb.Stateless;

@Stateless
public class RTPHASPProgramEJBBean extends RTPProgramEJBBean implements
        RTPHASPProgramEJB.R, RTPHASPProgramEJB.L {

    @Override
    protected String getThisProgramName() {
        return "RTP HASP";
    }

    @Override
    protected String getPriceConnectorClassName() {
        return "com.akuacom.pss2.oasis.client.OASISHASPClient";
    }

}
