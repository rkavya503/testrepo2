package com.akuacom.pss2.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.utils.ZipcodeUtil;
import com.akuacom.utils.ZipcodeUtil.ZipDetail;

/**
 * 
 * Adds geographic data to locate participants on map controls
 *
 */
public class PrepForOperatorDashboardTest extends JBossFixture {
    boolean ignore;
    static Logger log = Logger.getLogger(PrepForOperatorDashboardTest.class.getSimpleName());
    protected static ParticipantManager pm;
 
    @Before
    public void checkProgram() {
        ignore = !"true".equals(System.getProperty("com.akuacom.prepForOpdash"));
        if(!ignore) {
            pm = lookupSessionRemote(ParticipantManager.class);
        }
    }
    

    @Test
    public void testLocateClients() {
        Map<Integer,ZipDetail> latLons =  ZipcodeUtil.getZipcodes();
        List<Integer> zips = new ArrayList<Integer>(latLons.keySet());
        
        if (ignore || latLons.isEmpty()) {
            System.out.println("ignore " + ignore + "; latLons " + latLons.size());
            return;
        }
        
        Random rnd = new Random(System.currentTimeMillis());
        
        for(Participant p : pm.getAllParticipants()) {
            ZipDetail detail = latLons.get(zips.get(rnd.nextInt(zips.size())));
            p.setAddress(detail.poName);
            p.setLatitude(detail.latitude);
            p.setLongitude(detail.longitude);
            if( rnd.nextInt(101) < 30) {
                p.setStatus( 2 );
            }else {
                p.setStatus(0);
            }
            p.setShedPerHourKW(rnd.nextDouble() * 10);
            pm.updateParticipant(p);
        }
        
    }
}
