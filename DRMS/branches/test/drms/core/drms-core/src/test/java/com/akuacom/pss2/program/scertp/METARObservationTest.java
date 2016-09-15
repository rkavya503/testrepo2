/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.akuacom.pss2.program.scertp;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author spierson
 */
public class METARObservationTest {

    private String [] empties = {
        null,
        "",
        "  "
        };

    private String[] testMetars = {
        "KCQT 242347Z AUTO VRB04KT 10SM CLR 30/11 A2977 RMK AO2 SLPNO T03000106 10344 20300 56010 TSNO",
        "KCQT 242247Z AUTO VRB03KT 10SM CLR 32/10 A2977 RMK AO2 SLPNO T03170100 TSNO",
        "KCQT 242147Z AUTO 00000KT 10SM CLR 32/11 A2978 RMK AO2 SLPNO T03220111 TSNO",
        "KCQT 242047Z AUTO VRB03KT 10SM CLR 33/10 A2980 RMK AO2 SLPNO T03280100 58010 TSNO",
        "KCQT 241947Z AUTO 27005KT 10SM CLR 34/11 A2981 RMK AO2 SLPNO T03440111 TSNO",
        "KCQT 241847Z AUTO VRB03KT 10SM CLR 33/13 A2982 RMK AO2 SLPNO T03280128 TSNO",
        "KCQT 241747Z AUTO 00000KT 10SM CLR 32/13 A2983 RMK AO2 SLPNO 6//// T03220133 TSNO",
        "KCQT 241647Z AUTO 00000KT 10SM CLR 29/13 A2983 RMK AO2 SLPNO T02940133 TSNO",
        "KCQT 241547Z AUTO 00000KT 10SM CLR 27/14 A2983 RMK AO2 SLPNO T02670144 TSNO",
        "KCQT 241347Z AUTO CLR A2983 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 241247Z AUTO CLR A2982 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 241147Z AUTO CLR A2982 RMK AO2 SLPNO 56003 FZRANO TSNO $",
        "KCQT 241047Z AUTO CLR A2982 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 240947Z AUTO CLR A2982 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 240847Z AUTO CLR A2983 RMK AO2 SLPNO 58007 FZRANO TSNO $",
        "KCQT 240747Z AUTO CLR A2984 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 240647Z AUTO CLR A2985 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 240547Z AUTO CLR A2985 RMK AO2 SLPNO 51010 FZRANO TSNO $",
        "KCQT 240447Z AUTO CLR A2985 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 240347Z AUTO CLR A2984 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 240247Z AUTO CLR A2982 RMK AO2 SLPNO 53003 FZRANO TSNO $",
        "KCQT 240147Z AUTO CLR A2980 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 240047Z AUTO CLR A2980 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 232347Z AUTO CLR A2981 RMK AO2 SLPNO 57014 FZRANO TSNO $",
        "KCQT 232247Z AUTO CLR A2982 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 232147Z AUTO CLR A2984 RMK AO2 SLPNO FZRANO TSNO $",
        "KCQT 232047Z AUTO CLR A2985 RMK AO2 SLPNO 56009 FZRANO TSNO $",
        "KCQT 231947Z AUTO 00000KT 10SM CLR 36/10 A2986 RMK AO2 SLP106 T03610100 TSNO $",
        "KCQT 231847Z AUTO 00000KT 10SM CLR 36/08 A2987 RMK AO2 SLP110 T03560083 TSNO $",
        "KCQT 231747Z AUTO 00000KT 10SM CLR 32/10 A2988 RMK AO2 SLP115 T03220100 10322 20183 58003 TSNO $",
        "KCQT 231647Z AUTO 00000KT 8SM CLR 28/14 A2989 RMK AO2 SLP118 T02830139 TSNO $",
        "KCQT 231547Z AUTO 00000KT 7SM CLR 24/14 A2989 RMK AO2 SLP118 T02440144 TSNO $",
        "KCQT 231447Z AUTO 00000KT 9SM CLR 21/13 A2989 RMK AO2 SLP119 T02110133 51010 TSNO $",
        "KCQT 231347Z AUTO 00000KT 9SM CLR 19/14 A2988 RMK AO2 SLP117 T01890139 TSNO $",
        "KCQT 231247Z AUTO 00000KT 10SM CLR 18/13 A2987 RMK AO2 SLP113 T01830133 TSNO $",
    };

    @Test
    public void decodeTest()  throws Exception {

        for (String metar : empties){
            METARObservation m = new METARObservation(metar);
            assertTrue(m.isNil()==true );
        }
        
        for (String metar : testMetars){
            METARObservation m = new METARObservation(metar);
            assertTrue(m.getStationID().equals("KCQT"));
            assertTrue(m.getObservationTime() != null);
            m.getCurrentTempC();
            m.getCurrentTempF();
            m.getPrevDayHighC();
            m.getPrevDayHighF();
            m.getSixHourHighC();
            m.getSixHourHighF();

            m.setCurrentTempC(0);
            assertTrue(m.getCurrentTempF()==32);

            m.setPrevDayHighC(-14.0);
            assertTrue(m.getPrevDayHighC()==-14);

            m.setSixHourHighC(45.0);
            assertTrue(m.getSixHourHighF()==113);

            m.setStationID("ABCD");
            assertTrue(m.getStationID().equals("ABCD"));
        }
    }
}
