package com.akuacom.pss2.itron.wsclient;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

@Stateless
public class ItronClientServiceBean implements ItronClientService {
    @Override
    public boolean sendBids(String host, int eventNumber, Map<String, List<Double>> reductionMap, Map<String, List<Boolean>> activeMap,  Map<String, Integer> adjustmentMap) {
        ItronWSClient client = ItronWSClient.INSTANCE;
        if (client.getItronWSHostname() == null) {
            client.setItronWSHostname(host);
        }
        client.resetConfigFilename();
        return client.sendBidsToWS(eventNumber, reductionMap, activeMap,  adjustmentMap);
    }
}
