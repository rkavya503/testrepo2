package com.akuacom.pss2.openadr2.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;

import com.akuacom.pss2.system.property.CoreProperty;
import com.honeywell.dras.vtn.api.common.Response;
import com.honeywell.dras.vtn.api.report.CancelReport;
import com.honeywell.dras.vtn.api.report.CanceledReport;
import com.honeywell.dras.vtn.api.report.CreateReport;
import com.honeywell.dras.vtn.api.report.CreatedReport;
import com.honeywell.dras.vtn.api.report.PendingReports;
import com.honeywell.dras.vtn.api.report.RegisterReport;
import com.honeywell.dras.vtn.api.report.RegisteredReport;
import com.honeywell.dras.vtn.api.report.ReportDescription;
import com.honeywell.dras.vtn.api.report.UpdateReport;
import com.honeywell.dras.vtn.api.report.UpdatedReport;

public class ReportHelper {
	
	public static Response initializeResponse(String requestId,String venId,String schemaVersion){
		Response response = new Response();
		response.setRequestId(requestId);
		response.setResponseCode("200");
		response.setResponseDescription("OK");
		response.setVenId(venId);
		response.setSchemaVersion(schemaVersion);
		return response;
	}
	
	public static Response initializeErrorResponse(String requestId){
		Response response = new Response();
		response.setRequestId(requestId);
		response.setResponseCode("400");
		response.setResponseDescription("ERROR");
		return response;
	}
	
	public static Response initializeInvalidVenResponse(String requestId){
		Response response = new Response();
		response.setRequestId(requestId);
		response.setResponseCode("463");
		response.setResponseDescription("ERROR");
		return response;
	}
	
	public static Response initializeInvalidReportRequestIdResponse(String requestId){
		Response response = new Response();
		response.setRequestId(requestId);
		response.setResponseCode("452");
		response.setResponseDescription("ERROR");
		return response;
	}
	
	public static RegisteredReport createRegisteredReportResponse(RegisterReport registerReport){
		RegisteredReport registeredReport = new RegisteredReport();
		registeredReport.setVenId(registerReport.getVenId());
		registeredReport.setResponse(ReportHelper.initializeResponse(registerReport.getRequestId(),registerReport.getVenId(),registerReport.getSchemaVersion()));
		registeredReport.setRequestId(registerReport.getRequestId());
		registeredReport.setSchemaVersion(registerReport.getSchemaVersion());
		return registeredReport;
	}
	
	public static RegisteredReport createInvalidVenRegisteredReportResponse(RegisterReport registerReport){
		RegisteredReport registeredReport = new RegisteredReport();
		registeredReport.setVenId(registerReport.getVenId());
		registeredReport.setResponse(ReportHelper.initializeInvalidVenResponse(registerReport.getRequestId()));
		registeredReport.setRequestId(registerReport.getRequestId());
		registeredReport.setSchemaVersion(registerReport.getSchemaVersion());
		return registeredReport;
	}
	
	public static CreatedReport createCreatedReportResponse(CreateReport createReport){
		CreatedReport createdReport = new CreatedReport();
		createdReport.setVenId(createReport.getVenId());
		createdReport.setResponse(ReportHelper.initializeResponse(createReport.getRequestId(),createReport.getVenId(),createReport.getSchemaVersion()));
		createdReport.setRequestId(createReport.getRequestId());
		createdReport.setPendingReports(new PendingReports());
		createdReport.setSchemaVersion(createReport.getSchemaVersion());
		return createdReport;
	}
	
	public static CreatedReport createInvalidVenCreatedReportResponse(CreateReport createReport){
		CreatedReport createdReport = new CreatedReport();
		createdReport.setVenId(createReport.getVenId());
		createdReport.setResponse(ReportHelper.initializeInvalidVenResponse(createReport.getRequestId()));
		createdReport.setRequestId(createReport.getRequestId());
		return createdReport;
	}
	
	public static UpdatedReport createUpdatedReportResponse(UpdateReport updateReport){
		UpdatedReport updatedReport = new UpdatedReport();
		updatedReport.setVenId(updateReport.getVenId());
		updatedReport.setResponse(ReportHelper.initializeResponse(updateReport.getRequestId(),updateReport.getVenId(),updateReport.getSchemaVersion()));
		updatedReport.setRequestId(updateReport.getRequestId());
		updatedReport.setSchemaVersion(updateReport.getSchemaVersion());
		return updatedReport;
	}
	
	public static UpdatedReport createInvalidVenUpdatedReportResponse(UpdateReport updateReport){
		UpdatedReport updatedReport = new UpdatedReport();
		updatedReport.setVenId(updateReport.getVenId());
		updatedReport.setResponse(ReportHelper.initializeInvalidVenResponse(updateReport.getRequestId()));
		updatedReport.setRequestId(updateReport.getRequestId());
		updatedReport.setSchemaVersion(updateReport.getSchemaVersion());
		return updatedReport;
	}
	
	public static UpdatedReport createInvalidReportRequestIdUpdatedReportResponse(UpdateReport updateReport){
		UpdatedReport updatedReport = new UpdatedReport();
		updatedReport.setVenId(updateReport.getVenId());
		updatedReport.setResponse(ReportHelper.initializeInvalidReportRequestIdResponse(updateReport.getRequestId()));
		updatedReport.setRequestId(updateReport.getRequestId());
		updatedReport.setSchemaVersion(updateReport.getSchemaVersion());
		return updatedReport;
	}
	
	public static CanceledReport createCanceledReportResponse(CancelReport cancelReport){
		CanceledReport canceledReport = new CanceledReport();
		canceledReport.setVenId(cancelReport.getVenId());
		canceledReport.setResponse(ReportHelper.initializeResponse(cancelReport.getRequestId(),cancelReport.getVenId(),cancelReport.getSchemaVersion()));
		canceledReport.setRequestId(cancelReport.getRequestId());
		return canceledReport;
	}
	
	public static CanceledReport createInvalidVenCanceledReportResponse(CancelReport cancelReport){
		CanceledReport canceledReport = new CanceledReport();
		canceledReport.setVenId(cancelReport.getVenId());
		canceledReport.setResponse(ReportHelper.initializeInvalidVenResponse(cancelReport.getRequestId()));
		canceledReport.setRequestId(cancelReport.getRequestId());
		return canceledReport;
	}
	
	public static Report configureBasicReportParameters(Report configureReport,com.honeywell.dras.vtn.api.report.Report report){
		configureReport.setStart(report.getStart());
		if(null != report.getDuration() ){
			configureReport.setDuration(report.getDuration());
		}
		configureReport.setReportId(report.getReportId());
		configureReport.setReportName(report.getReportName());
		configureReport.setReportRequestId(report.getReportRequestId());
		configureReport.setReportSpecifierId(report.getReportSpecifierId());
		configureReport.setCreatedDate(report.getCreatedDate());
		//Set toCreate as true after registering for create report
		configureReport.setToCreate(true);
		return configureReport;
	}
	
	public static Set<Report> extractRegisterReportPayload(List<com.honeywell.dras.vtn.api.report.Report> reportList, HashMap<String, Long> props){
		Set<Report> reports = new HashSet<Report>();
		if(null != reportList){
			for(com.honeywell.dras.vtn.api.report.Report report:reportList){
				Report configureReport = new Report();
	
				//Report parameters
				configureReport = ReportHelper.configureBasicReportParameters(configureReport, report);
				
				//Report Description parameters
				Set<com.akuacom.pss2.openadr2.report.ReportDescription> lstConfigureReportDescription = new HashSet<com.akuacom.pss2.openadr2.report.ReportDescription>();
				if(null != report && null != report.getReportDescriptionList()){
					for(com.honeywell.dras.vtn.api.report.ReportDescription reportDescription: report.getReportDescriptionList()){
						com.akuacom.pss2.openadr2.report.ReportDescription configureReportDescription = new com.akuacom.pss2.openadr2.report.ReportDescription();
						
						ItemBase configureItemBase = new ItemBase();
						if(null != reportDescription && null != reportDescription.getItemBase()){
							configureItemBase.setItemDescription(reportDescription.getItemBase().getItemDescription());
							configureItemBase.setItemUnits(reportDescription.getItemBase().getItemUnits());
							configureItemBase.setSiScaleCode(reportDescription.getItemBase().getSiScaleCode());
							configureReportDescription.setItemBase(configureItemBase);
						}
						configureReportDescription.setMarketContext(reportDescription.getMarketContext());
						configureReportDescription.setReadingType(reportDescription.getReadingType());
		
						//Resolve ReportDataSource
						List<ReportDataSource> lstConfigureReportDataSource = resolveReportDataSource(reportDescription);
						
						configureReportDescription.setReportDataSource(lstConfigureReportDataSource);
						
						configureReportDescription.setReportId(reportDescription.getReportId());
		
						configureReportDescription.setReportType(reportDescription.getReportType());
						
						//Resolve ReportSubject
						List<ReportSubject> lstConfigureReportSubject =  resolveReportSubject(reportDescription);
						
						//configureReportDescription.setReportSubject(lstConfigureReportSubject);
						
						configureReportDescription.setrId(reportDescription.getrId());
						
						
						
						configureReportDescription.setSamplingMaxPeriod(reportDescription.getSamplingMaxPeriod());
						configureReportDescription.setSamplingMinPeriod(reportDescription.getSamplingMinPeriod());
						
						//TODO: Setting the uiGranularity and uiReportBackDuration to Max Sampling period
						Long granularity = 0L;
						Long reportBackDuration = 0L;
						if(reportDescription.getReportType().equalsIgnoreCase("x-resourceStatus")){
							 granularity = reportBackDuration = props.get("openadr.statusgranularityreportbackduration");	
						}else{
							 granularity = props.get("openadr.granularity");
							 reportBackDuration = props.get("openadr.reportbackduration");						
						}
						if(granularity >= reportDescription.getSamplingMinPeriod() && granularity <=  reportDescription.getSamplingMaxPeriod()) {
							configureReportDescription.setUiGranularity(granularity.longValue());
						} else {
							configureReportDescription.setUiGranularity(reportDescription.getSamplingMaxPeriod());
						}						
						if(reportBackDuration >= reportDescription.getSamplingMinPeriod() && reportBackDuration <=  reportDescription.getSamplingMaxPeriod()) {
							configureReportDescription.setUiReportBackDuration(reportBackDuration.longValue());
						} else {
							configureReportDescription.setUiReportBackDuration(reportDescription.getSamplingMaxPeriod());
						}
						
						//Set toCreate as true after registering for create report
						configureReportDescription.setToCreate(true);
						lstConfigureReportDescription.add(configureReportDescription);
					}
				}
				configureReport.setReportDescription(lstConfigureReportDescription);	
				
				reports.add(configureReport);
			}
		}
		return reports;		
	}
	
	private static List<ReportDataSource> resolveReportDataSource(ReportDescription reportDescription){
		List<ReportDataSource> lstConfigureReportDataSource = new ArrayList<ReportDataSource>();
		if(reportDescription.getReportDataSource()!=null){ 
			//TODO considering all types as "Resource"
				if(reportDescription.getReportDataSource().getAggregatedPnodeList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportDataSource: getAggregatedPnodeList...");
					for(String resource: reportDescription.getReportDataSource().getAggregatedPnodeList()){
						ReportDataSource configureReportDataSource = new ReportDataSource();
						configureReportDataSource.setDevice(resource);
						configureReportDataSource.setType("Resource");
						lstConfigureReportDataSource.add(configureReportDataSource);
					}
				}
				if(reportDescription.getReportDataSource().getEndDeviceAssetList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportDataSource: getEndDeviceAssetList...");
					for(String resource: reportDescription.getReportDataSource().getEndDeviceAssetList()){
						ReportDataSource configureReportDataSource = new ReportDataSource();
						configureReportDataSource.setDevice(resource);
						configureReportDataSource.setType("Resource");
						lstConfigureReportDataSource.add(configureReportDataSource);
					}
				}
				if(reportDescription.getReportDataSource().getGroupIdList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportDataSource: getGroupIdList...");
					for(String resource: reportDescription.getReportDataSource().getGroupIdList()){
						ReportDataSource configureReportDataSource = new ReportDataSource();
						configureReportDataSource.setDevice(resource);
						configureReportDataSource.setType("Resource");
						lstConfigureReportDataSource.add(configureReportDataSource);
					}
				}
				if(reportDescription.getReportDataSource().getGroupNameList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportDataSource: getGroupNameList...");
					for(String resource: reportDescription.getReportDataSource().getGroupNameList()){
						ReportDataSource configureReportDataSource = new ReportDataSource();
						configureReportDataSource.setDevice(resource);
						configureReportDataSource.setType("Resource");
						lstConfigureReportDataSource.add(configureReportDataSource);
					}
				}
				if(reportDescription.getReportDataSource().getMeterAssetList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportDataSource: getMeterAssetList...");
					for(String resource: reportDescription.getReportDataSource().getMeterAssetList()){
						ReportDataSource configureReportDataSource = new ReportDataSource();
						configureReportDataSource.setDevice(resource);
						configureReportDataSource.setType("Resource");
						lstConfigureReportDataSource.add(configureReportDataSource);
					}
				}
				if(reportDescription.getReportDataSource().getPartyIdList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportDataSource: getPartyIdList...");
					for(String resource: reportDescription.getReportDataSource().getPartyIdList()){
						ReportDataSource configureReportDataSource = new ReportDataSource();
						configureReportDataSource.setDevice(resource);
						configureReportDataSource.setType("Resource");
						lstConfigureReportDataSource.add(configureReportDataSource);
					}
				}
				if(reportDescription.getReportDataSource().getPnodeList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportDataSource: getPnodeList...");
					for(String resource: reportDescription.getReportDataSource().getPnodeList()){
						ReportDataSource configureReportDataSource = new ReportDataSource();
						configureReportDataSource.setDevice(resource);
						configureReportDataSource.setType("Resource");
						lstConfigureReportDataSource.add(configureReportDataSource);
					}
				}
				if(reportDescription.getReportDataSource().getResourceIdList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportDataSource: getResourceIdList...");
					for(String resource: reportDescription.getReportDataSource().getResourceIdList()){
						ReportDataSource configureReportDataSource = new ReportDataSource();
						configureReportDataSource.setDevice(resource);
						configureReportDataSource.setType("Resource");
						lstConfigureReportDataSource.add(configureReportDataSource);
					}
				}
				if(reportDescription.getReportDataSource().getVenIdList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportDataSource: getVenIdList...");
					for(String resource: reportDescription.getReportDataSource().getVenIdList()){
						ReportDataSource configureReportDataSource = new ReportDataSource();
						configureReportDataSource.setDevice(resource);
						configureReportDataSource.setType("Resource");
						lstConfigureReportDataSource.add(configureReportDataSource);
					}
				}
		}
		return lstConfigureReportDataSource;
	}
	
	private static List<ReportSubject> resolveReportSubject(ReportDescription reportDescription){
		List<ReportSubject> lstConfigureReportSubject = new ArrayList<ReportSubject>();
		if(reportDescription.getReportSubject()!=null){ 
			//TODO considering all types as "Resource"
				if(reportDescription.getReportSubject().getAggregatedPnodeList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportSubject: getAggregatedPnodeList...");
					for(String resource: reportDescription.getReportSubject().getAggregatedPnodeList()){
						ReportSubject configureReportSubject = new ReportSubject();
						configureReportSubject.setDevice(resource);
						configureReportSubject.setType("Resource");
						lstConfigureReportSubject.add(configureReportSubject);
					}
				}
				if(reportDescription.getReportSubject().getEndDeviceAssetList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportSubject: getEndDeviceAssetList...");
					for(String resource: reportDescription.getReportSubject().getEndDeviceAssetList()){
						ReportSubject configureReportSubject = new ReportSubject();
						configureReportSubject.setDevice(resource);
						configureReportSubject.setType("Resource");
						lstConfigureReportSubject.add(configureReportSubject);
					}
				}
				if(reportDescription.getReportSubject().getGroupIdList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportSubject: getGroupIdList...");
					for(String resource: reportDescription.getReportSubject().getGroupIdList()){
						ReportSubject configureReportSubject = new ReportSubject();
						configureReportSubject.setDevice(resource);
						configureReportSubject.setType("Resource");
						lstConfigureReportSubject.add(configureReportSubject);
					}
				}
				if(reportDescription.getReportSubject().getGroupNameList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportSubject: getGroupNameList...");
					for(String resource: reportDescription.getReportSubject().getGroupNameList()){
						ReportSubject configureReportSubject = new ReportSubject();
						configureReportSubject.setDevice(resource);
						configureReportSubject.setType("Resource");
						lstConfigureReportSubject.add(configureReportSubject);
					}
				}
				if(reportDescription.getReportSubject().getMeterAssetList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportSubject: getMeterAssetList...");
					for(String resource: reportDescription.getReportSubject().getMeterAssetList()){
						ReportSubject configureReportSubject = new ReportSubject();
						configureReportSubject.setDevice(resource);
						configureReportSubject.setType("Resource");
						lstConfigureReportSubject.add(configureReportSubject);
					}
				}
				if(reportDescription.getReportSubject().getPartyIdList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportSubject: getPartyIdList...");
					for(String resource: reportDescription.getReportSubject().getPartyIdList()){
						ReportSubject configureReportSubject = new ReportSubject();
						configureReportSubject.setDevice(resource);
						configureReportSubject.setType("Resource");
						lstConfigureReportSubject.add(configureReportSubject);
					}
				}
				if(reportDescription.getReportSubject().getPnodeList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportSubject: getPnodeList...");
					for(String resource: reportDescription.getReportSubject().getPnodeList()){
						ReportSubject configureReportSubject = new ReportSubject();
						configureReportSubject.setDevice(resource);
						configureReportSubject.setType("Resource");
						lstConfigureReportSubject.add(configureReportSubject);
					}
				}
				if(reportDescription.getReportSubject().getResourceIdList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportSubject: getResourceIdList...");
					for(String resource: reportDescription.getReportSubject().getResourceIdList()){
						ReportSubject configureReportSubject = new ReportSubject();
						configureReportSubject.setDevice(resource);
						configureReportSubject.setType("Resource");
						lstConfigureReportSubject.add(configureReportSubject);
					}
				}
				if(reportDescription.getReportSubject().getVenIdList()!=null){
					Logger.getLogger(ReportHelper.class).info("Storing ReportSubject: getVenIdList...");
					for(String resource: reportDescription.getReportSubject().getVenIdList()){
						ReportSubject configureReportSubject = new ReportSubject();
						configureReportSubject.setDevice(resource);
						configureReportSubject.setType("Resource");
						lstConfigureReportSubject.add(configureReportSubject);
					}
				}
		}
		return lstConfigureReportSubject;
	}
}
