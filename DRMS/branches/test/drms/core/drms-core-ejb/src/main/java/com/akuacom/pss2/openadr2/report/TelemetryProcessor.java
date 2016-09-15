package com.akuacom.pss2.openadr2.report;
import javax.ejb.Local;
import javax.ejb.Remote;

import com.honeywell.dras.vtn.api.report.Payload;

public interface TelemetryProcessor {

	@Remote
    public interface R extends TelemetryProcessor {}
    @Local
    public interface L extends TelemetryProcessor {}
    
    public void resolvePayload(Payload payload, TelemetryDataBusData data);
    void resolveMultipleDataPayload(Payload payload,TelemetryDataBusData data);
}
