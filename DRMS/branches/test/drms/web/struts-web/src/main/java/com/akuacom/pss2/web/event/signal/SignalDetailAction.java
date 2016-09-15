/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.event.signal.SignalDetailAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.event.signal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.akuacom.pss2.event.EventManager;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.event.participant.signal.EventParticipantSignalEntry;
import com.akuacom.pss2.signal.SignalDef;
import com.akuacom.pss2.signal.SignalLevelDef;
import com.akuacom.pss2.signal.SignalManager;
import com.akuacom.pss2.util.EventInfoInstance;
import com.akuacom.pss2.web.util.EJBFactory;
import com.akuacom.utils.Tag;

/**
 * The Class SignalDetailAction.
 */
@SuppressWarnings({"UnusedDeclaration"})
public class SignalDetailAction extends DispatchAction {

    /**
     * Creates the.
     * 
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward create(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SignalDetailForm form = (SignalDetailForm) actionForm;

        final List<Tag> signals = new ArrayList<Tag>();
        final Map<String, String[]> signalLevelMap = new HashMap<String, String[]>();
        SignalManager signalManager = EJBFactory.getBean(SignalManager.class);
        final List<SignalDef> list = signalManager.getSignals();
        for (SignalDef signal : list) {
            final String signalName = signal.getSignalName();
            signals.add(new Tag(signalName, signal.getType()));
            if (signal.isLevelSignal()) {
                Set<SignalLevelDef> levels = signal.getSignalLevels();
                String[] levelStrings = new String[levels.size()];
                int i = 0;
                for(SignalLevelDef level: levels)
                {
                	levelStrings[i++] = level.getStringValue();
                }
                signalLevelMap.put(signalName, levelStrings);
            }
        }
        form.setSignals(signals);
        form.setSignalLevelMap(signalLevelMap);

        String js = generateLevelJavascript(signals, signalLevelMap);
        request.setAttribute("js", js);

        // sync name and value for display.
        for (Tag tag : signals) {
            tag.setValue(tag.getName());
        }

        return actionMapping.findForward("success");
    }

    /**
     * Save.
     * 
     * @param actionMapping the action mapping
     * @param actionForm the action form
     * @param request the request
     * @param response the response
     * 
     * @return the action forward
     * 
     * @throws Exception the exception
     */
    public ActionForward save(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SignalDetailForm form = (SignalDetailForm) actionForm;

        final String programName = form.getProgramName();
        final String eventName = form.getEventName();
        final String participantName = form.getParticipantName();
        final String signalName = form.getSignal();
        final String level = form.getLevel();
        final String price = form.getPrice();
        final Date time = new SimpleDateFormat("MM/dd/yyyy H m s").parse(form.getDate() + " " + form.getHour() + " " + form.getMin() + " " + form.getSec());

        final EventParticipantSignalEntry entry = new EventParticipantSignalEntry();
        if (level != null && !"".equals(level)) {
            entry.setLevelValue(level);
        } else {
            final double v = Double.parseDouble(price);
            entry.setNumberValue(v);
        }
        entry.setTime(time);

        final EventManager eventManager = EJBFactory.getEventManager();
        final EventParticipant eventParticipant = eventManager.getEventParticipant(eventName, participantName, true);
        eventManager.addEventParticipantSignalEntry(eventParticipant, signalName, entry);
        
        return actionMapping.findForward("parent");
    }

    /**
     * Build a JavaScript array here, indexed by natural order in the list.
     * 
     * @param list list of signal names
     * @param map arrays of level names keyed by signal names
     * 
     * @return javascript array
     */
    private String generateLevelJavascript(List<Tag> list, Map<String, String[]> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        int i = 0;
        for (Tag tag : list) {
            if( i > 0) sb.append(",");
            sb.append("[ ");
            if (tag.getValue().equals(EventInfoInstance.SignalType.LOAD_LEVEL.name())) {
                String[] strings = map.get(tag.getName());
                for (int j = 0; j < strings.length; j++) {
                    if (j > 0) sb.append(", ");
                    String string = strings[j];
                    sb.append("\"").append(string).append("\"");
                }
            } else {
                sb.append("\"\"");
            }     
            sb.append(" ]");
            i++;
        }
        sb.append(" ]");

        return sb.toString();
    }
}
