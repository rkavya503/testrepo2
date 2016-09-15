/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.itron.pge.dbp.in.ManualBidUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.itron.pge.dbp.in;

// import com.akuacom.pss2.itron.pge.dbp.out.ItronWSClient;
import com.akuacom.pss2.program.dbp.BidEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 * The Class ManualBidUtil.
 */
@SuppressWarnings({"unchecked"})
public class ManualBidUtil {
    
    /**
     * Gets the result text.
     * 
     * @param eventId the event id
     * @param accountId the account id
     * @param bids the bids
     * 
     * @return the result text
     */
    public static String getResultText(String eventId, String accountId, String bids) {
        Map<String, List<BidEntry>> map = getMap(accountId, bids);

//        ItronWSClient client = ItronWSClient.INSTANCE;
//        client.resetConfigFilename();
//        client.sendBidsToWS(Integer.parseInt(eventId), map);
//        return client.getResultText();
        return "";
    }

    /**
     * Gets the map.
     * 
     * @param accountId the account id
     * @param bids the bids
     * 
     * @return the map
     */
    public static Map<String, List<BidEntry>> getMap(String accountId, String bids) {
        Map<String, List<BidEntry>> map = new HashMap<String, List<BidEntry>>();

        List<BidEntry> list = new ArrayList<BidEntry>();
        String[] strings = bids.split(",");
        for (String bid : strings) {
            double d = Double.parseDouble(bid);
            // manual rule: any bid that with KW > 0 is accepted
            byte accepted = d > 0.001 ? (byte) 1 : 0;

            BidEntry entry = new BidEntry(null, null, d, 0, BidEntry.TYPE_DEFAULT, accepted);
            list.add(entry);
        }
        map.put(accountId, list);
        return map;
    }

    /**
     * Adds the bid.
     * 
     * @param map the map
     * @param accountId the account id
     * @param bids the bids
     */
    public static void addBid(Map map, String accountId, String bids) {
        Map<String, List<BidEntry>> m = getMap(accountId, bids);
        Map<String, List<BidEntry>> result = (Map<String, List<BidEntry>>)map;
        result.put(accountId, m.get(accountId));
    }

    /**
     * Gets the bids map text.
     * 
     * @param eventId the event id
     * @param map the map
     * 
     * @return the bids map text
     */
    public static String getBidsMapText(String eventId, Map map) {
        Map<String, List<BidEntry>> m = (Map<String, List<BidEntry>>)map;
        StringBuffer sb = new StringBuffer();
        sb.append("Event ID: ").append(eventId).append("\n");

        Set set = m.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            sb.append("Account ID:").append(s).append("\n");
            List<BidEntry> list = m.get(s);
            sb.append("Bids: ");
            for (int i = 0; i < list.size(); i++) {
                BidEntry entry = list.get(i);
                String value = entry.isActive() ? entry.getReductionKW() + "" : "0";
                sb.append(value).append(",");
            }
            sb.append("\n<br/>");
        }
        return sb.toString();
    }
}
