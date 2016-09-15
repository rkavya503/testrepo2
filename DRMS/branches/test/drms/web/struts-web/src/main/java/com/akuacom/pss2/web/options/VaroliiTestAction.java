package com.akuacom.pss2.web.options;

import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.email.NotificationMethod;
import com.akuacom.pss2.email.NotificationParametersVO;
import com.akuacom.pss2.email.NotifierBean;
import com.akuacom.pss2.program.dbp.EventBidBlock;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;

public class VaroliiTestAction extends Action {
    private static final Logger log = Logger.getLogger(VaroliiTestAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        VaroliiTestForm form = (VaroliiTestForm) actionForm;
        boolean submitted = getSubmitted(form);
        if (submitted) {
            sendVaroliiNotification(form.getContactType(), form.getContact());
        }
        return mapping.findForward("success");
    }

    private boolean getSubmitted(VaroliiTestForm form) {
        String submitted = form.getSubmitted();
        return Boolean.parseBoolean(submitted);
    }

    private void sendVaroliiNotification(String contactType, String contact) {
        try {
            // prepare contacts
            final ArrayList<Contact> contacts = new ArrayList<Contact>();
            final Contact c = new Contact();
            c.setAddress(contact);
            c.setType(contactType);
            contacts.add(c);

            // prepare NM
            final NotificationMethod nm = new NotificationMethod();
            nm.setMedia(NotificationMethod.ENVOY_MESSAGE);
            nm.setEmail(true);  // dummy setting to bypass some delivery check.
            nm.setFax(true);
            nm.setEpage(true);
            nm.setVoice(true);

            // prepare values
            final NotificationParametersVO vo = new NotificationParametersVO();
            vo.setEventId("123456");
            vo.setMeterName("test meter");
            vo.setProgramName("Auto-DBP - Day-ahead WG2 Aggregate");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.HOUR_OF_DAY, 17);
            vo.setRespondBy(cal.getTime());
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, 12);
            vo.setEventStartDate(cal.getTime());
            cal.set(Calendar.HOUR_OF_DAY, 20);
            vo.setEventEndDate(cal.getTime());
            vo.setTimeZone("PST");
            vo.setUnitPrice("0.5");
            ArrayList<EventBidBlock> blocks = new ArrayList<EventBidBlock>();
            for (int i = 0; i < 8; i++) {
                EventBidBlock block = new EventBidBlock();
                block.setStartTime(1200 + i * 100);
                block.setEndTime(block.getStartTime() + 100);
                blocks.add(block);
            }
            vo.setEntries(blocks);

            // now send the message
            new NotifierBean().sendEnvoyNotification(contacts, "admin", "test", nm, vo, true);
        } catch (JMSException e) {
            log.error("Varolii Test failed", e);
        }
    }
}
