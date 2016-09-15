package com.akuacom.pss2.email;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.contact.Contact;
import com.akuacom.pss2.program.dbp.EventBidBlock;
import com.akuacom.pss2.system.property.CoreProperty;
import com.akuacom.pss2.system.property.CorePropertyEAO;
import com.akuacom.pss2.system.property.PSS2Features;
import com.akuacom.pss2.system.property.PSS2Properties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.akuacom.pss2.email.NotificationParametersVO.Transport.*;

public class VaroliiUtil {
public static String SMS_STOP_MESSAGE = "SMS_STOP_MESSAGE";
public static String SMS_HELP_MESSAGE = "SMS_HELP_MESSAGE";
public static String SMS_STOP_MESSAGE_DESC = "PG&amp;E: You have unsubscribed from this Demand Response mobile alert. For more information visit http://pge-adr.com or call Energy Solutions 1-855-866-2205.";
public static String SMS_HELP_MESSAGE_DESC = "PG&amp;E: Auto DBP Alerts. Reply STOP to unsubscribe. For more information visit http://pge-adr.com or call Energy Solutions at 1-855-866-2205.";
    /**
     * Gets the message xml.
     *
     * @param vo the vo
     * @param subject the subject
     *
     * @return the message xml
     */
    static String getMessageXml(NotificationParametersVO vo, String subject) {
        StringBuilder builder = new StringBuilder("");
        String eventCondition = vo.getEventCondition();
        String theme = vo.getTheme();
        builder.append("<Message>\n");
        builder.append("<subject>").append(subject).append("</subject>\n");
        builder.append(getMessageArgXml("SENDER", vo.getSender()));
        builder.append(getMessageArgXml("EMAIL_ADDR", vo.getEmail()));
        builder.append(getMessageArgXml("THEME", theme));
        builder.append(getMessageArgXml("EventID", vo.getEventId()));
        builder.append(getMessageArgXml("IsTestEvent", vo.isTestEvent() + ""));
        builder.append(getMessageArgXml("EventCondition", eventCondition));
        String cdataMeterName = "<![CDATA[" + vo.getMeterName()+"]]>";
        builder.append(getMessageArgXml("MeterName", cdataMeterName));
        builder.append(getMessageArgXml("ProgramName", vo.getProgramName()));
        builder.append(getMessageArgXml("ProgramType", vo.getProgramType()));
        builder.append(getMessageArgXml("SettlementType", vo.getSettlementType()));
        final Date startDate = vo.getEventStartDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        builder.append(getMessageArgXml("EventStartDate", simpleDateFormat.format(startDate)));
        final Date endDate = vo.getEventEndDate();
        builder.append(getMessageArgXml("EventEndDate", simpleDateFormat.format(endDate)));
        builder.append(getMessageArgXml("TimeZone", vo.getTimeZone()));
        builder.append(getMessageArgXml("RespondBy", simpleDateFormat.format(vo.getRespondBy())));
        builder.append(getMessageArgXml("URL", vo.getUrl()));
        builder.append(getMessageArgXml("EnergyUnit", vo.getEnergyUnit()));
        builder.append(getMessageArgXml("EnergyPriceUnit", vo.getEnergyPriceUnit()));
        final List<EventBidBlock> bidBlocks = vo.getEntries();
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        final int startHour = Integer.parseInt(hourFormat.format(startDate));
        final int endHour = Integer.parseInt(hourFormat.format(endDate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        for (int i = 0, bidBlocksSize = bidBlocks.size(); i < bidBlocksSize; i++) {
            EventBidBlock bidBlock = bidBlocks.get(i);
            // note that the logic here assumes PGE DBP event is hour bound.
            if (startHour <= bidBlock.getStartTime() / 100 && endHour >= bidBlock.getEndTime() / 100 ) {
                cal.set(Calendar.HOUR_OF_DAY, bidBlock.getStartTime() / 100);
                cal.set(Calendar.MINUTE, bidBlock.getStartTime() % 100);
                builder.append(getMessageArgXml("BlockStartDate" + (i + 1), simpleDateFormat.format(cal.getTime())));
                cal.set(Calendar.HOUR_OF_DAY, bidBlock.getEndTime() / 100);
                cal.set(Calendar.MINUTE, bidBlock.getEndTime() % 100);
                builder.append(getMessageArgXml("BlockEndDate" + (i + 1), simpleDateFormat.format(cal.getTime())));
                builder.append(getMessageArgXml("BlockPrice" + (i + 1), vo.getUnitPrice()));
            }
        }
        builder.append("</Message>");

        return builder.toString();
    }

    /**
     * Gets the message arg xml.
     *
     * @param name the name
     * @param value the value
     *
     * @return the message arg xml
     */
    static String getMessageArgXml(String name, String value) {
        return "<MessageArg>\n<Name>" + name + "</Name>\n<Value>" + value + "</Value>\n</MessageArg>\n";
    }

    /**
     * Gets the block xml.
     *
     * @param contacts the contacts
     * @param nm the nm
     *
     * @return the block xml
     */
    static String getBlockXml(List<Contact> contacts, NotificationMethod nm) {
        final StringBuilder builder = new StringBuilder("<Block>\n");
        int i = 1;
        for (Contact c : contacts) {
            final String address = c.getAddress();
            final String type = c.getType();
            if (address != null && address.length() > 0 && isDeliverable(type, nm)) {
                builder.append("<DeliveryRequest>\n" +
                        "<MessagePath>Message[1]</MessagePath>\n" +
                        "<ContactMethodPath>Contact[1]/ContactMethod[").append(i).append("]</ContactMethodPath>\n" +
                        "</DeliveryRequest>\n");
                i++;
            }
        }
        builder.append("</Block>\n");

        return builder.toString();
    }
    
    static String getBlockXmlWithDeliveryReqArg(List<Contact> contacts, NotificationMethod nm) {
        final StringBuilder builder = new StringBuilder("<Block>\n");
        int i = 1;
        for (Contact c : contacts) {
            final String address = c.getAddress();
            final String type = c.getType();
            if (address != null && address.length() > 0 && isDeliverable(type, nm)) {
                if (type.equals(Contact.SMS)) {
                    builder.append("<DeliveryRequest>\n" +
                            "<MessagePath>Message[1]</MessagePath>\n" +
                            "<ContactMethodPath>Contact[1]/ContactMethod[").append(i).append("]</ContactMethodPath>\n" +
                            "<DeliveryRequestArg>\n"+ 
                            "<Name>").append(SMS_STOP_MESSAGE).append("</Name>\n"+
                    		"<Value>").append(SMS_STOP_MESSAGE_DESC).append("</Value>\n"
                    		+ "</DeliveryRequestArg>\n"+
                            "<DeliveryRequestArg>\n"+ 
                            "<Name>").append(SMS_HELP_MESSAGE).append("</Name>\n"+
                    		"<Value>").append(SMS_HELP_MESSAGE_DESC).append("</Value>\n"
                    		+ "</DeliveryRequestArg>\n"+
                            "</DeliveryRequest>\n");
                } else {
                	builder.append("<DeliveryRequest>\n" +
                            "<MessagePath>Message[1]</MessagePath>\n" +
                            "<ContactMethodPath>Contact[1]/ContactMethod[").append(i).append("]</ContactMethodPath>\n" +
                            "</DeliveryRequest>\n");
                }
                i++;
            }
        }
        builder.append("</Block>\n");

        return builder.toString();
    }

    /**
     * Checks if is deliverable.
     *
     * @param type the type
     * @param nm the nm
     *
     * @return true, if is deliverable
     */
    static boolean isDeliverable(String type, NotificationMethod nm) {
        boolean result = false;
        if (type.equals(Contact.EMAIL_ADDRESS) && nm.isEmail()) {
            result = true;
        } else if (type.equals(Contact.FAX_NUMBER) && nm.isFax()) {
            result = true;
        } else if (type.equals(Contact.PHONE_NUMBER) && nm.isVoice()) {
            result = true;
        } else if (type.equals(Contact.PAGER) && nm.isEpage()) {
            result = true;
        } else if (type.equals(Contact.SMS) && nm.isSms()) {
            result = true;
        }
        return result;
    }

    /**
     * Gets the contact xml.
     *
     * @param contacts the contacts
     *
     * @return the contact xml
     */
    static String getContactXml(List<Contact> contacts) {
        final StringBuilder builder = new StringBuilder("<Contact>\n");
        builder.append("<FirstName>dras</FirstName>\n");
        builder.append("<LastName>client</LastName>\n");
        for (Contact c : contacts) {
            builder.append(getContactMethodXml(c.getAddress(), c.getType()));
        }
        builder.append("</Contact>\n");

        return builder.toString();
    }

    /**
     * Gets the contact method xml.
     *
     * @param contact the contact
     * @param contactType the contact type
     *
     * @return the contact method xml
     */
    static String getContactMethodXml(String contact, String contactType) {
        StringBuilder builder = new StringBuilder();
        if (contact != null && contact.length() > 0) {
			final String transport = getTransport(contactType);
			boolean isEmailType = isEmailType(transport);
			boolean isSMSType = isSMSType(transport);
			boolean isPagerType = isPagerType(transport);
			
			if (isSMSType) {
				builder = smsTemplate(builder, contact, transport);
			} else if (isEmailType){
				builder = emailTemplate(builder, contact, transport);
			} else if (isPagerType){
				builder = pagerTemplate(builder, contact, transport);
			} else {
				builder = otherTransportTemplate(builder, contact, transport);
			}
        }
        return builder.toString();
    }

    /**
     * Gets the transport.
     *
     * @param contactType1 the contact type1
     *
     * @return the transport
     */
    static String getTransport(String contactType1) {
        if (Contact.EMAIL_ADDRESS.equals(contactType1)) {
            return email.toString();
        } else if (Contact.PHONE_NUMBER.equals(contactType1)) {
            return phone.toString();
        } else if (Contact.FAX_NUMBER.equals(contactType1)) {
            return fax.toString();
        } else if (Contact.PAGER.equals(contactType1)) {
            return pager.toString();
        } else if (Contact.SMS.equals(contactType1)) {
            return sms.toString();
        } else {
            return email.toString();
        }
    }

    static String getHecoEventStartMessageXml(NotificationParametersVO vo, String subject) {
        StringBuilder builder = new StringBuilder("");

        builder.append("<Message>\n");
        builder.append("<subject>").append(subject).append("</subject>\n");
        builder.append(getMessageArgXml("EMAIL_ADDR", vo.getEmail()));
        builder.append(getMessageArgXml("THEME", vo.getTheme()));
        builder.append(getMessageArgXml("PROGRAM_NAME", vo.getProgramName()));

        final Date endDate = vo.getEventEndDate();
        final Date startDate = vo.getEventStartDate();
        long duration = (endDate.getTime() - startDate.getTime()) / 1000 / 60; // in minutes

        builder.append(getMessageArgXml("DURATION_HOURS", (duration / 60) + ""));
        builder.append(getMessageArgXml("DURATION_MINUTES", (duration % 60) + ""));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mma");

        builder.append(getMessageArgXml("END_DATE", dateFormat.format(endDate)));
        builder.append(getMessageArgXml("END_TIME", timeFormat.format(endDate)));
        builder.append(getMessageArgXml("START_DATE", dateFormat.format(startDate)));
        builder.append(getMessageArgXml("START_TIME", timeFormat.format(startDate)));
        builder.append("</Message>");

        return builder.toString();
    }

    static String getHecoNotificationMessageXml(NotificationParametersVO vo, String subject) {
        StringBuilder builder = new StringBuilder("");

        builder.append("<Message>\n");
        builder.append("<subject>").append(subject).append("</subject>\n");
        builder.append(getMessageArgXml("EMAIL_ADDR", vo.getEmail()));
        builder.append(getMessageArgXml("THEME", vo.getTheme()));
        builder.append(getMessageArgXml("PROGRAM_NAME", vo.getProgramName()));

        builder.append("</Message>");

        return builder.toString();
    }

    static String getHecoContactXml(List<Contact> contacts, String userId) {
        final StringBuilder builder = new StringBuilder("<Contact>\n");
        builder.append("<UserReference>" + userId + "</UserReference>\n");
        builder.append("<FirstName>dras</FirstName>\n");
        builder.append("<LastName>client</LastName>\n");
        for (Contact c : contacts) {
            builder.append(getContactMethodXml(c.getAddress(), c.getType()));
        }
        builder.append("</Contact>\n");

        return builder.toString();
    }

    static String getHecoContactMethodXml(String contact, String contactType) {
        StringBuilder builder = new StringBuilder();
        if (contact != null && contact.length() > 0) {
            final String transport = getTransport(contactType);
            builder.append("<ContactMethod>\n<Transport>").append(transport).append("</Transport>\n");

            if (email.toString().equals(transport)) {
                builder.append("<EmailAddress>").append(contact).append("</EmailAddress>\n");
            } else {
                builder.append("<PhoneNum>").append(contact).append("</PhoneNum>\n");
            }
            builder.append("</ContactMethod>\n");
        }
        return builder.toString();
    }

    /** The Constant PGE_XML_HEADER_STRING. */
    private static final String PGE_XML_HEADER_STRING = "<?xml version=\"1.0\"?>\n" +
            "<Request version=\"EXAPI 2.0\">\n" +
            "  <username>pge_autodbpadmin</username>\n" +
            "  <Password>pgeadmin</Password>\n" +
            "  <Domain>PGE</Domain>\n" +
            "  <OemId>pge_autodbp_oem</OemId>\n" +
            "  <OemPassword>pge$oem</OemPassword>\n" +
            "  <RequestType>commit</RequestType>\n" +
            "  <Job>\n";

    /** The Constant HECO_XML_HEADER_STRING. */
    private static final String HECO_XML_HEADER_STRING = "<?xml version=\"1.0\"?>\n" +
            "<Request version=\"EXAPI 2.0\" errorstyle=\"SEPARATE\" prunelist=\"//Message,//DeliveryRequest,//Contact,//Block\">\n" +
            "<Username>hecoadmin</Username>\n" +
            "<Password>hec0!$7aPi</Password>\n" +
            "<Domain>HECO</Domain>\n" +
            "<OemId>heco_oem</OemId>\n" +
            "<OemPassword>4?Ac@p#E</OemPassword>\n" +
            "<RequestType>Commit</RequestType>\n" +
            "<Job>\n";

    static String getHeadXml() {
        // get utility name
        CorePropertyEAO corePropertyEAO = EJB3Factory.getBean(CorePropertyEAO.class);
        List<CoreProperty> coreProperties = corePropertyEAO.getAll();
        PSS2Properties properties = new PSS2Properties(coreProperties);
        String utilityName = properties.getUtilityName();

        if ("pge".equals(utilityName)) {
            return PGE_XML_HEADER_STRING;
        } else if ("heco".equals(utilityName)) {
            PSS2Features features = new PSS2Features(coreProperties);
            Boolean productionServer = features.isProductionServer();
            if (!productionServer) {
                return HECO_XML_HEADER_STRING;
            } else {
                VaroliiProperties p = VaroliiProperties.getInstance();
                String result = "<?xml version=\"1.0\"?>\n" +
                        "<Request version=\"EXAPI 2.0\" errorstyle=\"SEPARATE\" prunelist=\"//Message,//DeliveryRequest,//Contact,//Block\">\n" +
                        "<Username>" + p.getUserName() + "</Username>\n" +
                        "<Password>" + p.getPassword() + "</Password>\n" +
                        "<Domain>" + p.getDomain() + "</Domain>\n" +
                        "<OemId>" + p.getOemId() + "</OemId>\n" +
                        "<OemPassword>" + p.getOemPassword() + "</OemPassword>\n" +
                        "<RequestType>Commit</RequestType>\n" +
                        "<Job>\n";

                return result;
            }
        } else {
            throw new RuntimeException("Varolii messaging is not supported on this instance: " + utilityName);
        }
    }
    
/*    private static String setTransportValue(String contactType){
    	String transport = "";
    	if ( !Contact.PAGER.equals(contactType) ) {
    		transport = getTransport(contactType);
    	} else {
    		transport = getTransport(Contact.SMS);
    	}
    	return transport;
    }*/
    
    private static boolean isEmailType(String transport){
    	boolean isEmailType = email.toString().equals(transport) ? true : false;
    	return isEmailType;
    }
    
    private static boolean isSMSType(String transport){
    	boolean isSMSType = sms.toString().equals(transport) ? true : false;
    	return isSMSType;
    }

    private static boolean isPagerType(String transport){
    	boolean isPagerType = pager.toString().equals(transport) ? true : false;
    	return isPagerType;
    }
    
    private static StringBuilder smsTemplate(StringBuilder builder, String contact, String transport){
    	builder.append("<ContactMethod>\n<Transport>").append(transport).append("</Transport>\n");
    	builder.append("<Qualifier>none</Qualifier>\n");
//    	builder.append("<PhoneNum><![CDATA[").append(contact).append("]]></PhoneNum>\n");
    	builder.append("<PhoneNum>").append(contact).append("</PhoneNum>\n");
        builder.append("</ContactMethod>\n");
        return builder;
    }
    
    private static StringBuilder emailTemplate(StringBuilder builder, String contact, String transport){
    	builder.append("<ContactMethod label=\"email\">\n<Transport>").append(transport).append("</Transport>\n");
    	builder.append("<HtmlStatus>both</HtmlStatus>\n");
    	builder.append("<Qualifier>home</Qualifier>\n<Ordinal>0</Ordinal>\n");
    	builder.append("<EmailAddress>").append(contact).append("</EmailAddress>\n");
    	builder.append("</ContactMethod>\n");
    	return builder;
    }
    
    private static StringBuilder pagerTemplate(StringBuilder builder, String contact, String transport){
    	builder.append("<ContactMethod>\n<Transport>").append(transport).append("</Transport>\n");
    	builder.append("<Qualifier>none</Qualifier>\n<Ordinal>0</Ordinal>\n");
    	builder.append("<PhoneNum>").append(contact).append("</PhoneNum>\n");
        builder.append("</ContactMethod>\n");
    	return builder;
    }
    
    private static StringBuilder otherTransportTemplate(StringBuilder builder, String contact, String transport){
    	builder.append("<ContactMethod>\n<Transport>").append(transport).append("</Transport>\n");
    	builder.append("<Qualifier>office</Qualifier>\n<Ordinal>0</Ordinal>\n");
    	builder.append("<PhoneNum>").append(contact).append("</PhoneNum>\n");
        builder.append("</ContactMethod>\n");
    	return builder;
    }
    
}
