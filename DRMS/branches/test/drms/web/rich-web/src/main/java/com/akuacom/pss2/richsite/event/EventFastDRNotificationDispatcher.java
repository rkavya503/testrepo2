package com.akuacom.pss2.richsite.event;

import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.util.EventUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventFastDRNotificationDispatcher extends EventCPPScheduleDispatcher {
    private static final Logger log = Logger.getLogger(EventFastDRNotificationDispatcher.class);

    @Override
    public String submitToDB(EventDataModel model, EventDataModelManager manager) {
        model.setEventName(EventUtil.getEventName());
        model.setReceivedTime(model.getIssuedTime());
        Date endTime = new Date(model.getStartTime().getTime() + 300 * 1000);
        model.setEndTime(endTime);
        List<EventParticipantDataModel> models = model.getAllParticipantsInProgram();
        List<EventParticipant> list = new ArrayList<EventParticipant>();
        for (EventParticipantDataModel eventParticipantDataModel : models) {
            if (eventParticipantDataModel.isSelect()) {
                EventParticipant ep = new EventParticipant();
                ep.setParticipant(eventParticipantDataModel.getParticipant());
                list.add(ep);
            }
        }
        model.setEventParticipants(list);
        return super.submitToDB(model, manager);
    }
}
