/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.util.TabMap.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.util;

import java.util.Map;
import java.util.HashMap;

/**
 * The Class TabMap.
 */
public class TabMap {

    /** The Constant map. */
    private final static Map<String, Tab> map = new HashMap<String, Tab>();

    static {
        map.put("clientList", Tab.Clients);
        map.put("clientsMap", Tab.Clients);
        map.put("participantsMap", Tab.Participants);
        map.put("commDevDetail", Tab.Participants);
        map.put("sceAccounts", Tab.Clients);
        map.put("logList", Tab.Log);
        map.put("logDetail", Tab.Log);
        map.put("program", Tab.Program);
        map.put("eventList", Tab.Program);
        map.put("uoProgram", Tab.Program);
        map.put("uoEvent", Tab.Event);
        map.put("uoParticipant", Tab.Clients);
        map.put("eventDetail", Tab.Event);
        map.put("cppEventDetail", Tab.Event);
        map.put("dbpEventDetail", Tab.Event);
        map.put("demoCPPEventDetail", Tab.Event);
        map.put("issueEventDetail", Tab.Event);
        map.put("dbpNoBidEventDetail", Tab.Event);
        map.put("testEventDetail", Tab.Event);
        map.put("signalList", Tab.Program);
        map.put("signalDetail", Tab.Program);
        map.put("signalEntry", Tab.Program);
        map.put("options", Tab.Options);
        map.put("programMatrix", Tab.Options);
        map.put("optionsSignals", Tab.Options);
        map.put("optionsProperties", Tab.Options);
        map.put("optionsAbout", Tab.Options);
        map.put("optionsContacts", Tab.Options);
        map.put("nssettings", Tab.Options);
        map.put("dbpSimulator", Tab.Options);
        map.put("varoliiTest", Tab.Options);

        map.put("about", Tab.About);
        map.put("home", Tab.Home);
        map.put("report", Tab.Report);
        map.put("eventSignalReport", Tab.Log);
        map.put("eventListReport", Tab.Log);
        map.put("accountsReport", Tab.Log);
        map.put("eventParticipationReport", Tab.Log);
        map.put("clientParticipationReport", Tab.Log);
        map.put("eventParticipantListReport", Tab.Log);
        map.put("offlineReport", Tab.Log);
        map.put("emails", Tab.Log);        
        map.put("weatherReport", Tab.Log);
        map.put("weatherDetail", Tab.Log);
        map.put("news", Tab.News);
        map.put("editBid", Tab.Event);
        map.put("opdash", Tab.Dashboard);
        map.put("dbpEventCreationReport", Tab.Log);
    }

    /**
     * Gets the active tab.
     * 
     * @param uri the uri
     * 
     * @return the active tab
     */
    public static Tab getActiveTab(String uri) {
        Tab s = map.get(uri);
        if (s == null) {
            s = Tab.About;
        }
        return s;
    }
}
