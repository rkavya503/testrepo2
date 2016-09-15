package com.akuacom.pss2.richsite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

import scala.Product;
import scala.Tuple3;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantGenEAO;
import com.akuacom.utils.JsUtil;

/**
 * supports OpDash prototype, will likely be removed once prototype is stable
 *
 */
public class ParticipantsBean {
    static Logger log = Logger.getLogger(QuotationServer.class); 
    @EJB
    ParticipantGenEAO.L peao;

    public ParticipantsBean() {}

    public List<Participant> getParticipants() {
        return peao.findAll();
    }
    
    public List<Tuple3<String,Double,Double>> getCoords() {
        List<Tuple3<String,Double,Double>> coords = peao.getEm().createNamedQuery("Participant.getCoords").getResultList();
        return coords;
    }

    public String getCoordsStr() {
        List<Tuple3<String,Double,Double>> coords =getCoords();
        List<Double[]>ca = new ArrayList(coords.size());
        for(Tuple3<String,Double,Double> tup : coords) {
            ca.add(new Double[]{tup._2(), tup._3()});
        }
        return JsUtil.toJsArray(ca);
    }
    public String getAmCoordsStr() {
        List<Tuple3<String,Double,Double>> coords =getCoords();
        List<String>ca = new ArrayList(coords.size());
        for(Tuple3<String,Double,Double> tup : coords) {
            ca.add("{latitude:" + tup._2() +", longitude:"+ tup._3() + "}");
        }
        return JsUtil.toJsArray(ca);
    }
    
    public String getMxCoordsStr() {
        List<Tuple3<String,Double,Double>> coords =getCoords();
        List<String>ca = new ArrayList(coords.size());
        for(Tuple3<String,Double,Double> tup : coords) {
            ca.add("[\""+tup._1()+"\"," + tup._2() +", " + tup._3()+"]");
        }
        return JsUtil.toJsArray(ca);
    }

    public String getJsonCoordsStr() {
        List<Tuple3<String,Double,Double>> coords =getCoords();
        return JsUtil.toJson(coords, new Tuple3("id", "latitude", "longitude"));
    }
}
