package com.akuacom.pss2.openadr2.report.telemetry;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

public interface VenResourceStatusManager {
	@Remote
    public interface R extends VenResourceStatusManager {}
    @Local
    public interface L extends VenResourceStatusManager {}
    public void createResourceStatus(ResourceStatusDTO req) throws InvalidResourceStatusData;
    public List<ResourceStatusDTO> getResourceStatusByTimeRange(GetResourceStatusRequest req);
    public ResourceStatusDTO getLatestResourceStatus(GetResourceStatusRequest req);
}
