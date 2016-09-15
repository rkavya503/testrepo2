package com.akuacom.pss2.itron.wsclient;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.rpc.Stub;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.apache.log4j.Logger;

import com.akuacom.pss2.itron.webservices.CurtailmentOperationsLocator;
import com.akuacom.pss2.itron.webservices.CurtailmentOperationsSoap;
import com.akuacom.pss2.itron.webservices.CurtailmentOperationsSoapStub;
import com.akuacom.pss2.itron.webservices.EventNoticeResponse;
import com.akuacom.pss2.itron.webservices.EventNoticeResponseBlock;
import com.akuacom.pss2.itron.webservices.EventNoticeResponseList;
import com.akuacom.pss2.itron.webservices.Message;
import com.akuacom.pss2.itron.webservices.MessageDataItem;
import com.akuacom.pss2.itron.webservices.RespondToEventNoticesResult;
import com.akuacom.pss2.itron.webservices.ResponseAction;

public class ItronWSClient {

    // singleton
    public static final ItronWSClient INSTANCE = new ItronWSClient();

    // default configuration file name
    private static String jbossdir = System
            .getProperty("jboss.server.home.dir");
    private static String confdir = jbossdir + File.separator + "conf"
            + File.separator;
    private static final String CONFIG_FILE_NAME = confdir + "ItronWS.wsdd";
    private static final Logger log = Logger.getLogger(ItronWSClient.class
            .getName());

    private String configFilename;
    private String resultText;
    private String itronWSHostname;

    // private to enforce singleton
    private ItronWSClient() {
        resetConfigFilename();
    }

    public void setConfigFilename(String configFilename) {
        this.configFilename = configFilename;
    }

    public void resetConfigFilename() {
        configFilename = CONFIG_FILE_NAME;
    }

    /**
     * This is the only method that is called to send the bids to an event to
     * Itron WS
     * 
     * @param eventId
     *            ITRON id for the event, sent by itron in the event message
     * @param reductionMap  reductions, key by part id
     * @param activeMap     active flags, key by part id
     * @return true if response is received and the connection closed gracefully
     */
    public boolean sendBidsToWS(int eventId, Map<String, List<Double>> reductionMap, Map<String, List<Boolean>> activeMap,  Map<String, Integer> adjustmentMap) {
//        log.debug(LogUtils.createLogEntry("", this.getClass().getName(),
//                "******* ItronWSClient ***********",
//                "******* ItronWSClient ***********"));

        // first get stub
        CurtailmentOperationsSoapStub itronStub = createStub();

        // now create response list
        EventNoticeResponseList list = createResponseList(eventId, reductionMap, activeMap, adjustmentMap);

        // send bids to WS
        if (list != null) {
            final AsyncSender sender = new AsyncSender();
            sender.stub = itronStub;
            sender.list = list;
            new Thread(sender).start();
        } else {
//            log.error(LogUtils.createLogEntry("", this.getClass().getName(),
//                    "Error creating bid list! No bids sent!",
//                    "Error creating bid list! No bids sent!"));
            return false;
        }
        return true;
    }

    private void async(CurtailmentOperationsSoapStub itronStub,
            EventNoticeResponseList list) {
        try {
//            log.debug(LogUtils.createLogEntry("", LogUtils.CATAGORY_EVENT,
//                    "Submitting Bids to Itron", ""));
            RespondToEventNoticesResult result = itronStub
                    .respondToEventNotices(list);
            // print response from WS
            showWSresponse(result);
        } catch (Throwable t) {
//            log.error(LogUtils.createLogEntry("", this.getClass().getName(),
//                    t.getMessage(), t.getMessage()));
        }
    }

    /**
     * 
     * @return
     */
    public String getItronWSHostname() {

        return itronWSHostname;
    }

    /**
     * 
     * @param itronWSHostname
     */
    public void setItronWSHostname(String itronWSHostname) {
        this.itronWSHostname = itronWSHostname;
    }

    private CurtailmentOperationsSoapStub createStub() {
        try {
            EngineConfiguration config = new FileProvider(configFilename);
            CurtailmentOperationsLocator locator = new CurtailmentOperationsLocator(
                    config);
            locator.setCurtailmentOperationsSoapEndpointAddress(itronWSHostname);
            Stub stub = (Stub) locator.getPort(CurtailmentOperationsSoap.class);
            return (CurtailmentOperationsSoapStub) stub;
        } catch (Throwable t) {
            log.error("Error creating stub:" + t.getMessage(), t);
//            log.debug(LogUtils.createLogEntry("", this.getClass().getName(),
//                    t.getMessage(), t.getMessage()));
        }
        return null;
    }

    private EventNoticeResponseList createResponseList(int eventId,
            Map<String, List<Double>> reductionMap, Map<String, List<Boolean>> activeMap,  Map<String, Integer> adjustmentMap) {
        String subject = "Response List for eventId: " + eventId;
        StringBuilder sb = new StringBuilder();
        Set<String> strings = reductionMap.keySet();
        for (String s : strings) {
            sb.append("account number = ");
            sb.append(s);
            sb.append("\n");
            List<Double> list = reductionMap.get(s);
            List<Boolean> listB = activeMap.get(s);
            if (list != null) {
                for (int i = 0, listSize = list.size(); i < listSize; i++) {
                    sb.append("bid = ");
                    sb.append(list.get(i));
                    sb.append(" KW, acceptance = ");
                    sb.append(listB.get(i));
                    sb.append("\n");
                }
            }
        }
//        log.debug(LogUtils.createLogEntry("", this.getClass().getName(),
//                subject, sb.toString()));

        EventNoticeResponseList responseList = new EventNoticeResponseList();
        responseList.setTransactionID(generateTransactionID());

        final Set<String> accountSet = reductionMap.keySet();
        EventNoticeResponse[] responses = new EventNoticeResponse[accountSet
                .size()];
        int i = 0;
        for (String accountID : accountSet) {
            EventNoticeResponse response = new EventNoticeResponse();
            response.setCurtailmentEventID(eventId);
            response.setAccountID(accountID);
            response.setResponseAction(ResponseAction.SubmitBid);
            response.setUseDefaultReductionForAllBlocks(false);
            response.setApplyBaselineLoadAdjustment(createApplyBaselineLoadAdjustment(accountID,adjustmentMap));
            // block populating strategy goes here
            response.setResponseBlocks(createBlocks(reductionMap.get(accountID), activeMap.get(accountID)));
            responses[i++] = response;
        }

        // now set all responses to list
        responseList.setEventNoticeResponses(responses);

        return responseList;
    }

    // create id from today's date
    private String generateTransactionID() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        String datestr = Integer.toString(cal.get(GregorianCalendar.MONTH))
                + Integer.toString(cal.get(GregorianCalendar.DAY_OF_MONTH))
                + Integer.toString(cal.get(GregorianCalendar.HOUR_OF_DAY));
        return "AKUA_DRAS_" + datestr;
    }

    private EventNoticeResponseBlock[] createBlocks(List<Double> listD, List<Boolean> listB) {
        final EventNoticeResponseBlock[] blocks = new EventNoticeResponseBlock[listD.size()];
        for (int i = 0; i < listD.size(); i++) {
            final EventNoticeResponseBlock block = new EventNoticeResponseBlock();
            block.setBlockNumber(i + 1);
            block.setCommittedReduction(listD.get(i));
            block.setBlockAccepted(listB.get(i));
            blocks[i] = block;
        }
        return blocks;
    }

   private boolean createApplyBaselineLoadAdjustment(String accountID, Map<String, Integer> adjustmentMap) {
    	
        Integer applyAdjustmentValue = adjustmentMap.get(accountID);
        if(applyAdjustmentValue == 1)
        	return true;
        else 
        	return false;
    }
    /**
     * Print out itron response
     * 
     * @param result
     *            POJO received from itron
     */
    private void showWSresponse(RespondToEventNoticesResult result) {
        StringBuffer sb = new StringBuffer();
        sb.append("RespondToEventNoticesResult: " + result.getResultCode()
                + ". \n");
        sb.append("Transaction ID: " + result.getTransactionID() + ". \n");
        Message[] messages = result.getMessages();
        for (Message message : messages) {
            sb.append("Begin Message: " + ". \n");
            sb.append("ID: " + message.getID() + ". \n");
            sb.append("Severity: " + message.getSeverity() + ". \n");
            // sb.append("Message Time: " + message.getMessageTime() + "\n");
            sb.append("Text: " + message.getText() + ". \n");
            sb.append("Begin Message Data: " + ". \n");
            MessageDataItem[] items = message.getMessageData();
            if (items != null) {
                for (MessageDataItem item : items) {
                    sb.append("Name: " + item.getName() + ", Value:"
                            + item.getValue() + ". \n");
                }
            }
            sb.append("End Message Data. \n");
            sb.append("End Message. \n");
        }
        resultText = sb.toString();
//        log.debug(LogUtils.createLogEntry("", this.getClass().getName(),
//                resultText, resultText));
    }

    public String getResultText() {
        return resultText;
    }

    private class AsyncSender implements Runnable {

        CurtailmentOperationsSoapStub stub;

        // now create response list
        EventNoticeResponseList list;

        @Override
        public void run() {
            async(stub, list);
        }
    }

}
