package com.akuacom.pss2.email;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.contact.ContactEAO;
import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;

@SuppressWarnings({"EjbProhibitedPackageUsageInspection"})
public class ExceptionNotifierInterceptor {

    @AroundInvoke
    public Object exceptionInterceptor(InvocationContext ctx) throws java.lang.Exception {
        try {
            return ctx.proceed();
        } catch (Exception e) {
            Date now = new Date();
            Object target = ctx.getTarget();
            Object[] parameters = ctx.getParameters();
            Event event = null;
            for (Object parameter : parameters) {
                if (parameter instanceof Event) {
                    event = (Event) parameter;
                    break;
                }
            }

            try {
                // use reflection to get session context
                Method method = target.getClass().getMethod("getSessionContext");
                SessionContext sessionContext = (SessionContext) method.invoke(target);
                // NOTE: assuming WS calls don't have website role.
                boolean website = sessionContext.isCallerInRole("website");
                if (!website) { // ignore exception that caused by ui users
                    CorePropertyEAO core = EJB3Factory.getBean(CorePropertyEAO.class);
                    CoreProperty utilityName = core.getByPropertyName("utilityName");

                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                    String title = "Critical Exception Happened on " + utilityName.getStringValue() + " at " + format.format(now);
                    String content = getStackTraceString(e);
                    if (event != null) {
                        title = title + " for program " + event.getProgramName() + " and event " + event.getEventName();
                        content = event.toString() + "\\n" + content;
                    }

                    sendOperatorMail(title, content);
//                    Notifier notifier = EJB3Factory.getBean(Notifier.class);
//                    notifier.sendOperatorMail(title, content);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            throw e;
        }
    }

    private String getStackTraceString(Exception e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        pw.flush();
        return out.toString();
    }

    public void sendOperatorMail(String subject, String content) {
        try {
            ContactEAO contactEAO = EJB3Factory.getBean(ContactEAO.class);
            List<Contact> contacts = contactEAO.findCoreContacts();

            String globalContacts = "";
            for (Contact contact : contacts) {
                if (globalContacts.equalsIgnoreCase("")) {
                    globalContacts = contact.getAddress();
                } else {
                    globalContacts = globalContacts + "," + contact.getAddress();
                }
            }
            //noinspection deprecation
            MailUtil.sendMail(globalContacts, "noreply@openadr.com", subject, content, null, "N/A", null, null, 10, null);
        }
        catch (Exception e) {
            //
        }
    }
}
