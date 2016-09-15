/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.EventFactory.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import java.util.Comparator;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.event.EventTemplate;
import com.akuacom.pss2.event.TimeBlock;

/**
 * A factory for returning event related view objects from Event DAO objects and
 * vice versa.
 */
public class EventFactory {

    /**
     * Assembles EventTemplate object out of DAO.
     * 
     * @param dao NOTE: this object has to be inside em context. otherwise, a runtime exception will be thrown.
     * 
     * @return event
     */
    public static EventTemplate getEventTemplate(Event dao) {
        EventTemplate eventTemplate;
        eventTemplate = (EventTemplate) dao;
        return eventTemplate;
    }

    /**
     * Gets the event dao from template.
     * 
     * @param eventTemplate the event template
     * 
     * @return the event dao from template
     */
    public static Event getEventDAOFromTemplate(EventTemplate eventTemplate) {
        Event dao = eventTemplate;
        return dao;
    }

    /**
     * The Class TimeBlockComparator.
     */
    static class TimeBlockComparator implements Comparator<TimeBlock> {
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(TimeBlock o1, TimeBlock o2) {
            return o1.getStartHour() * 100 + o1.getStartMinute()
                    - (o2.getStartHour() * 100 + o2.getStartMinute());
        }
    }
}
