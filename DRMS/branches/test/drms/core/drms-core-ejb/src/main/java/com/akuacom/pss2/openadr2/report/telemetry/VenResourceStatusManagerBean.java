package com.akuacom.pss2.openadr2.report.telemetry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;


@Stateless
public class VenResourceStatusManagerBean  implements VenResourceStatusManager.L,VenResourceStatusManager.R{
	
	@EJB
	private VenResourceStatusEAO.L venResourceStatusEao;
	
	private Logger log = Logger.getLogger(VenResourceStatusManagerBean.class);
	
	
	public void createResourceStatus(ResourceStatusDTO req) throws InvalidResourceStatusData{
		VenResourceStatus entity = createEntity(req);
		String venId = entity.getVenId();
		String resourceName = entity.getResourceName();
		VenResourceStatus  oldEntity = this.venResourceStatusEao.findLatestEntry(venId,resourceName);
		if(null != oldEntity && oldEntity.isResourceOnline() == entity.isResourceOnline()){
			return;
		}
		venResourceStatusEao.create(entity);
	}
	
	public List<ResourceStatusDTO> getResourceStatusByTimeRange(GetResourceStatusRequest req){
		List<ResourceStatusDTO> res = new ArrayList<ResourceStatusDTO>();
		if(req == null ){
			return res;
		}
		List<VenResourceStatus> entityList = new ArrayList<VenResourceStatus>();
		try {
			entityList = venResourceStatusEao.findByEntryRange(req);
		} catch (InvalidResourceStatusData e) {
			log.error(e.getStackTrace());
		}
		for(VenResourceStatus vrs : entityList){
			ResourceStatusDTO dto = new ResourceStatusDTO();
			dto.setOnlineStatus(vrs.isResourceOnline());
			dto.setParticipantName(vrs.getParticipantName());
			dto.setReportedTime(vrs.getReportedTime());
			dto.setResourceName(vrs.getResourceName());
			dto.setVenId(vrs.getVenId());
			res.add(dto);
		}
		return res;
	}
	public ResourceStatusDTO getLatestResourceStatus(GetResourceStatusRequest req){
		VenResourceStatus entity = null ;
		ResourceStatusDTO dto = null;
		try {
			entity = this.venResourceStatusEao.findLatestEntry(req.getVenId(), req.getResourceId());
		} catch (Exception e) {
			log.error(e.getStackTrace());
		}
		if(null != entity){
			dto = new ResourceStatusDTO();
			dto.setOnlineStatus(entity.isResourceOnline());
			dto.setParticipantName(entity.getParticipantName());
			dto.setResourceName(entity.getResourceName());
			dto.setReportedTime(entity.getReportedTime());
		}
		return  dto;
	}
	
	private VenResourceStatus createEntity(ResourceStatusDTO req) throws InvalidResourceStatusData{
		VenResourceStatus entity = new VenResourceStatus();
		if(!isAllRequiredDataPresentToPersistResourceStatus(req,entity)){
			throw new InvalidResourceStatusData("Invalid resource status data, can not persist");
		}
		return  entity;
	}
	private boolean isAllRequiredDataPresentToPersistResourceStatus(ResourceStatusDTO req,VenResourceStatus entity){
		if(null == req){
			return false;
		}
		String venId = req.getVenId();
		String resourceId = req.getResourceName();
		String participationName = req.getParticipantName();
		Boolean resOnlineStatus = req.getOnlineStatus();
		Date reportedTime = req.getReportedTime();
		
		boolean isDataValid = !(null == venId || venId.isEmpty() || null == resourceId || resourceId.isEmpty()
				|| null == participationName || participationName.isEmpty() || null == resOnlineStatus || null == reportedTime);
		
		if(isDataValid){
			entity.setCreationTime(new Date());
			entity.setVenId(venId);
			entity.setResourceName(resourceId);
			entity.setParticipantName(participationName);
			entity.setResourceOnline(resOnlineStatus);
			entity.setReportedTime(reportedTime);
		}
		return isDataValid;
	}
	

}
