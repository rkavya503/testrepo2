package com.akuacom.pss2.web.event.dbp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class EditBidForm extends ActionForm {
    private String programName;
    private String eventName;
    private String participantName;
    private Map<String, String> bidMap;

    @Override
    public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        bidMap = new HashMap<String, String>();
    }

    public String getBid(String key) {
        return bidMap.get(key);
    }

    public void setBid(String key, String value) {
        bidMap.put(key, value);
    }

    public Map<String, String> getBidMap() {
        return bidMap;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }
}
