package com.akuacom.pss2.openadr2.report;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.data.DataManager;
import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.pss2.data.PDataSet;
import com.akuacom.pss2.data.PDataSource;
import com.akuacom.pss2.openadr2.endpoint.EndpointManager;
import com.akuacom.pss2.openadr2.endpoint.EndpointMapping;
import com.akuacom.pss2.openadr2.report.telemetry.InvalidResourceStatusData;
import com.akuacom.pss2.openadr2.report.telemetry.ResourceStatusDTO;
import com.akuacom.pss2.openadr2.report.telemetry.VenResourceStatusManager;
import com.akuacom.utils.lang.TimingUtil;



public @Stateless class TelemetryProcessorBean implements TelemetryProcessor.L,TelemetryProcessor.R {

	@EJB
	DataManager.L dataManager;
	
	@EJB
	EndpointManager.L endPointManager;
	@EJB
	private VenResourceStatusManager.L venResStatusManager;
	
	private Logger log = Logger.getLogger(TelemetryProcessorBean.class);
	private static final double BASE_VALUE = 2.0;
    private static final double PEAK_INCREMENT_MIN = 16.0;
    private static final double PEAK_INCREMENT_MAX = 24.0;
	
    @Override
	public void resolvePayload(com.honeywell.dras.vtn.api.report.Payload payload, TelemetryDataBusData data) {		
		  try {
			  String venId = data.getOwnerEntityID().trim();
			  EndpointMapping endPointMapping  = endPointManager.findEndpointParticipantLinkByVenId(venId);
			  if(endPointMapping != null){
			  String participantClientName = endPointMapping.getParticipantName();
			  String participantName = participantClientName.substring(0, participantClientName.indexOf("."));
			  if(payload.getValue()!=null){

				  
				  PDataSource dataSource = dataManager.getDataSourceByNameAndOwner("meter1", participantName);

				  if (dataSource == null) {
					  PDataSource ds = new PDataSource();
					  ds.setName("meter1");
					  ds.setOwnerID(participantName);
					  dataSource = dataManager.createPDataSource(ds);
				  }

				  final PDataSet dataSet = dataManager.getDataSetByName("Usage");

				  final List<PDataEntry> dataEntryList = new ArrayList<PDataEntry>();
				  PDataEntry dataEntry = new PDataEntry();
				  dataEntry.setDataSet(dataSet);
				  dataEntry.setDatasource(dataSource);
				  dataEntry.setActual(true);
				  log.info("dataentry time : "+data.getEntryTime());
				  dataEntry.setTime(TimingUtil.clipMillis(data.getEntryTime()));
				  dataEntry.setRawTime(TimingUtil.clipMillis(data.getEntryTime()));
				  dataEntryList.add(dataEntry);
				  dataEntry.setValue((double)payload.getValue());
				  final Set<PDataEntry> set = new HashSet<PDataEntry>(dataEntryList);
				  dataManager.createDataEntries(set);        

			  }

			  if(payload.getResourceStatus() != null)
			  {
				 Boolean status = payload.getResourceStatus().getOnline();
				 log.info("Resource status received from ven : "+venId+" "+payload.getResourceStatus().getOnline().toString()); 
				 processResourceOnlineStatus(status,participantName,participantClientName,venId);
			  }
			 }
	        } catch (Exception e) {
	            log.error("error processing telemetry data!!!!");
	        }
		
	}
	
    @Override
	public void resolveMultipleDataPayload(com.honeywell.dras.vtn.api.report.Payload payload, TelemetryDataBusData data) {		
		  try {
			  String venId = data.getOwnerEntityID().trim();
			  EndpointMapping endPointMapping  = endPointManager.findEndpointParticipantLinkByVenId(venId);
			  if(endPointMapping != null){
			  String participantClientName = endPointMapping.getParticipantName();
			  String participantName = participantClientName.substring(0, participantClientName.indexOf("."));
			  if(payload.getValue()!=null){
				  
				  final com.akuacom.pss2.system.SystemManager systemManager = EJBFactory
			                .getBean(com.akuacom.pss2.system.SystemManager.class);
			        Boolean multipleDataEnable=systemManager.getPss2Features().isOpenadrMultipleDataEnabled();
					String baselineUsageEquivalentValue = systemManager.getPss2Features().getBaselineUsageEquivalentValue();
				  
				  if(multipleDataEnable){
					  
					  if(baselineUsageEquivalentValue.equalsIgnoreCase(payload.getrId())){
						  resolvePayload(payload,data);
						} else {
							PDataSource dataSource = dataManager
									.getDataSourceByNameAndOwner(
											payload.getrId(), participantName);

							if (dataSource == null) {
								PDataSource ds = new PDataSource();
								ds.setName(payload.getrId());
								ds.setOwnerID(participantName);
								dataSource = dataManager.createPDataSource(ds);
							}

							PDataSet dataSet = dataManager
									.getDataSetByName(payload.getrId());

							if (dataSet == null) {
								PDataSet dSet = new PDataSet();
								dSet.setName(payload.getrId());
								dSet.setUnit("kw");
								dSet.setPeriod(300L);
								dSet.setDescription(payload.getrId());
								dataSet = dataManager.createPDataSet(dSet);
							}

							final List<PDataEntry> dataEntryList = new ArrayList<PDataEntry>();
							PDataEntry dataEntry = new PDataEntry();
							dataEntry.setDataSet(dataSet);
							dataEntry.setDatasource(dataSource);
							dataEntry.setActual(true);
							log.info("dataentry time : " + data.getEntryTime());
							dataEntry.setTime(TimingUtil.clipMillis(data
									.getEntryTime()));
							dataEntry.setRawTime(TimingUtil.clipMillis(data
									.getEntryTime()));
							dataEntryList.add(dataEntry);
							dataEntry.setValue((double) payload.getValue());
							final Set<PDataEntry> set = new HashSet<PDataEntry>(
									dataEntryList);
							dataManager.createDataEntries(set);
						}      

			  }else{
				  if(baselineUsageEquivalentValue.equalsIgnoreCase(payload.getrId())){
					  resolvePayload(payload,data);
				  }
			  }

			  if(payload.getResourceStatus() != null)
			  {
				 Boolean status = payload.getResourceStatus().getOnline();
				 log.info("Resource status received from ven : "+venId+" "+payload.getResourceStatus().getOnline().toString()); 
				 processResourceOnlineStatus(status,participantName,participantClientName,venId);
			  }
			 }
		  }  
	        } catch (Exception e) {
	            log.error("error processing telemetry data!!!!");
	        }
		
	}
	
    private void processResourceOnlineStatus(Boolean resStatus , String participantName , String resName,String venId){
    	ResourceStatusDTO dto = new ResourceStatusDTO();
    	dto.setOnlineStatus(resStatus);
    	dto.setParticipantName(participantName);
    	dto.setResourceName(resName);
    	dto.setReportedTime(new Date());
    	dto.setVenId(venId);
    	try {
			this.venResStatusManager.createResourceStatus(dto);
		} catch (InvalidResourceStatusData e) {
			log.error(e.getStackTrace());
		}
    	
    }

}
