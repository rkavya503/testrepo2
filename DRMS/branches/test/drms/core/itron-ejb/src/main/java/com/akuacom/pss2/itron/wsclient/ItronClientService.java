package com.akuacom.pss2.itron.wsclient;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface ItronClientService {
    public boolean sendBids(String host, int eventNumber, Map<String, List<Double>> reductionMap, Map<String, List<Boolean>> activeMap,  Map<String, Integer> adjustmentMap);
}
