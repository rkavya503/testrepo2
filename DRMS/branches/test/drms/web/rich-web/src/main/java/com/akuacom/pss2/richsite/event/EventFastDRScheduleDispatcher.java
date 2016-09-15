package com.akuacom.pss2.richsite.event;

import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.util.EventUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Amao
 * Date: 4/1/12
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventFastDRScheduleDispatcher extends EventCPPScheduleDispatcher {
    @Override
    public String submitToDB(EventDataModel eventDataModel, EventDataModelManager manager) {
        eventDataModel.setEventName(EventUtil.getEventName());
        eventDataModel.setReceivedTime(eventDataModel.getIssuedTime());
        List<EventParticipantDataModel> models = eventDataModel.getAllParticipantsInProgram();
        List<EventParticipant> list = new ArrayList<EventParticipant>();
        for (EventParticipantDataModel eventParticipantDataModel : models) {
            if (eventParticipantDataModel.isSelect()) {
                EventParticipant ep = new EventParticipant();
                ep.setParticipant(eventParticipantDataModel.getParticipant());
                list.add(ep);
            }
        }
        eventDataModel.setEventParticipants(list);
        return super.submitToDB(eventDataModel, manager);
    }
}
