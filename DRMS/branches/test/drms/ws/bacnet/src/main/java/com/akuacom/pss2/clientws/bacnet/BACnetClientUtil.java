/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.clientws.bacnet.BACnetClientUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.clientws.bacnet;

import java.text.SimpleDateFormat;
import java.util.List;

import com.akuacom.pss2.util.EventInfoInstance;
import com.akuacom.pss2.util.EventInfoValue;
import com.akuacom.pss2.util.EventState;
import com.akuacom.pss2.util.ModeSlot;

/**
 * The Class BACnetClientUtil.
 */
public class BACnetClientUtil {

    /**
     * Gets the bA cnet data.
     * 
     * @param participantName
     *            the participant name
     * @param eventStates
     *            the event states
     * 
     * @return the bA cnet data
     */
    @SuppressWarnings({ "StringConcatenationInsideStringBufferAppend" })
    public static String getBACnetData(String participantName,
            List<EventState> eventStates) {
        StringBuilder rvSB = new StringBuilder();

        boolean inEvent = false;

        rvSB.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        rvSB.append("<CSML xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xsi:noNamespaceSchemaLocation=\"file:DR-034A-22w Schema.xsd\" defaultLocale=\"en\">>\n");
        for (EventState eventState : eventStates) {
            rvSB.append("<Sequence name=\"eventState\" type=\"0-DRAS-EventState-2008-1\">\n");
            rvSB.append("<String name=\"programName\" value=\""
                    + eventState.getProgramName() + "\" />\n");
            rvSB.append("<Unsigned name=\"eventModNumber\" value=\""
                    + eventState.getEventModNumber() + "\" />\n");
            rvSB.append("<String name=\"eventIdentifier\" value=\""
                    + eventState.getEventIdentifier() + "\" />\n");
            rvSB.append("<String name=\"drasClientID\" value=\""
                    + eventState.getDrasClientID() + "\" />\n");
            rvSB.append("<Unsigned name=\"eventStateID\" value=\""
                    + eventState.getEventStateID() + "\" />\n");
            rvSB.append("<String name=\"schemaVersion\" value=\"20080509\" />\n");
            rvSB.append("<Sequence name=\"simpleDRModeData\">\n");
            rvSB.append("<Enumerated name=\"EventStatus\" value=\""
                    + eventState.getEventStatus() + "\" />\n");
            rvSB.append("<Enumerated name=\"OperationModeValue\" value=\""
                    + eventState.getOperationModeValue() + "\" />\n");
            rvSB.append("<Real name=\"currentTime\" value=\""
                    + eventState.getCurrentTimeS() + "\" />\n");
            if (!eventState.getEventIdentifier().equals("")) {
                inEvent = true;
                rvSB.append("<Array name=\"operationModeSchedule\">\n");
                for (ModeSlot modeSlot : eventState.getOperationModeSchedule()) {
                    rvSB.append("<Sequence>\n");
                    rvSB.append("<Enumerated name=\"OperationModeValue\" value=\""
                            + modeSlot.getOperationModeValue() + "\" />\n");
                    rvSB.append("<Unsigned name=\"modeTimeSlot\" value=\""
                            + modeSlot.getTimeSlotS() + "\" />\n");
                    rvSB.append("</Sequence>\n");
                }
                rvSB.append("</Array>\n");
            }
            rvSB.append("</Sequence>\n");
            if (!eventState.getEventIdentifier().equals("")) {

                rvSB.append("<Sequence name=\"drEventData\">\n");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.S");
                rvSB.append("<DateTime name=\"notificationTime\" value=\""
                        + simpleDateFormat.format(eventState
                        .getNotificationTime()) + "\" />\n");
                rvSB.append("<DateTime name=\"startTime\" value=\""
                        + simpleDateFormat.format(eventState.getStartTime())
                        + "\" />\n");
                rvSB.append("<DateTime name=\"endTime\" value=\""
                        + simpleDateFormat.format(eventState.getEndTime())
                        + "\" />\n");

                if (eventState.getEventInfoInstances().size() != 0) {
                    rvSB.append("<Array name=\"eventInfoInstances\">\n");
                    for (EventInfoInstance eventInfoInstance : eventState
                            .getEventInfoInstances()) {
                        rvSB.append("<Sequence>\n");
                        rvSB.append("<Enumerated name=\"eventInfoTypeID\" value=\""
                                + eventInfoInstance.getSignalType() + "\" />\n");
                        rvSB.append("<String name=\"eventInfoName\" value=\""
                                + eventInfoInstance.getSignalName() + "\" />\n");
                        rvSB.append("<Array name=\"eventInfoValues\">\n");
                        for (EventInfoValue eventInfoValue : eventInfoInstance
                                .getEventInfoValues()) {
                            rvSB.append("<Sequence>\n");
                            rvSB.append("<Real name=\"value\" value=\""
                                    + eventInfoValue.getValue() + "\" />\n");
                            rvSB.append("<Unsigned name=\"timeOffset\" value=\""
                                    + (int) eventInfoValue.getTimeOffsetS()
                                    + "\" />\n");
                            rvSB.append("</Sequence>\n");
                        }
                        rvSB.append("</Array>\n");
                        rvSB.append("</Sequence>\n");
                    }
                    rvSB.append("</Array>\n");
                }

                rvSB.append("</Sequence>\n");
            }
            rvSB.append("</Sequence>\n");
        }
        rvSB.append("</CSML>\n");

        return rvSB.toString();
    }

}
