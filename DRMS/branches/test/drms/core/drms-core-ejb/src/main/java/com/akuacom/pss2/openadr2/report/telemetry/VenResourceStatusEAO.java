package com.akuacom.pss2.openadr2.report.telemetry;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;


public interface VenResourceStatusEAO {
	
	@Remote
    public interface R extends VenResourceStatusEAO {}
    @Local
    public interface L extends VenResourceStatusEAO {}
	public void create(VenResourceStatus venResourceStatus);
	public List<VenResourceStatus> findByEntryRange(GetResourceStatusRequest req) throws InvalidResourceStatusData;
	public VenResourceStatus findLatestEntry(String venId , String resourceId) throws InvalidResourceStatusData;

}
