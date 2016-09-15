/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.kwickview;

import com.schneider_electric.webservices.ArrayOfCurtailmentEvent;
import com.schneider_electric.webservices.CurtailmentEvent;
import com.schneider_electric.webservices.CurtailmentResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author spierson
 */
public class KWickviewResponse {
    private List<KWickviewEvent> events = new ArrayList<KWickviewEvent>();
    private String requestID;
    private int resultCode;
    
    public KWickviewResponse(CurtailmentResponse wireResponse) {
        this.requestID = wireResponse.getRequestID();
        this.resultCode = wireResponse.getResultCode();
        
        ArrayOfCurtailmentEvent evts = wireResponse.getEvents();
        if (evts != null) {
            for (CurtailmentEvent evt : evts.getCurtailmentEvent()) {
                events.add(new KWickviewEvent(evt));
            }
        }
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder("KWickviewResponse: ");
        sb.append(" reqID="+this.getRequestID());
        sb.append(" resultCode="+this.getResultCode());
        sb.append(" events="+this.getEvents());
        return sb.toString();
    }

    /**
     * @return the events
     */
    public List<KWickviewEvent> getEvents() {
        return events;
    }

    /**
     * @return the requestID
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * @return the resultCode
     */
    public int getResultCode() {
        return resultCode;
    }
}
