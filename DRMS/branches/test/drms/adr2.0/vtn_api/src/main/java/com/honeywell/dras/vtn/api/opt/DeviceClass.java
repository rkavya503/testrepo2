package com.honeywell.dras.vtn.api.opt;

import java.util.ArrayList;
import java.util.List;

public class DeviceClass {
	
	private List<String> aggregatedPnodeList;
	private List<String> endDeviceAssetList;
	private List<String> groupIdList;
	private List<String> groupNameList;
	private List<String> meterAssetList;
	private List<String> partyIdList;
	private List<String> pnodeList;
	private List<String> resourceIdList;
	private List<ServiceArea> serviceAreaList;
	private List<ServiceDeliveryPoint> serviceDeliveryPointList;
	private List<ServiceLocation> serviceLocationList;
	private List<TransportInterface> transportInterfaceList;
	private List<String> venIdList;
	
	public List<String> getAggregatedPnodeList() {
		return aggregatedPnodeList;
	}
	public void setAggregatedPnodeList(List<String> aggregatedPnodeList) {
		this.aggregatedPnodeList = aggregatedPnodeList;
	}
	public List<String> getEndDeviceAssetList() {
		return endDeviceAssetList;
	}
	public void setEndDeviceAssetList(List<String> endDeviceAssetList) {
		this.endDeviceAssetList = endDeviceAssetList;
	}
	public List<String> getGroupIdList() {
		return groupIdList;
	}
	public void setGroupIdList(List<String> groupIdList) {
		this.groupIdList = groupIdList;
	}
	public List<String> getGroupNameList() {
		return groupNameList;
	}
	public void setGroupNameList(List<String> groupNameList) {
		this.groupNameList = groupNameList;
	}
	public List<String> getMeterAssetList() {
		return meterAssetList;
	}
	public void setMeterAssetList(List<String> meterAssetList) {
		this.meterAssetList = meterAssetList;
	}
	public List<String> getPartyIdList() {
		return partyIdList;
	}
	public void setPartyIdList(List<String> partyIdList) {
		this.partyIdList = partyIdList;
	}
	public List<String> getPnodeList() {
		return pnodeList;
	}
	public void setPnodeList(List<String> pnodeList) {
		this.pnodeList = pnodeList;
	}
	public List<String> getResourceIdList() {
		if (resourceIdList == null) {
			resourceIdList = new ArrayList<String>();
		}
		return resourceIdList;
	}
	public void setResourceIdList(List<String> resourceIdList) {
		this.resourceIdList = resourceIdList;
	}
	public List<ServiceArea> getServiceAreaList() {
		return serviceAreaList;
	}
	public void setServiceAreaList(List<ServiceArea> serviceAreaList) {
		this.serviceAreaList = serviceAreaList;
	}
	public List<ServiceDeliveryPoint> getServiceDeliveryPointList() {
		return serviceDeliveryPointList;
	}
	public void setServiceDeliveryPointList(List<ServiceDeliveryPoint> serviceDeliveryPointList) {
		this.serviceDeliveryPointList = serviceDeliveryPointList;
	}
	public List<ServiceLocation> getServiceLocationList() {
		return serviceLocationList;
	}
	public void setServiceLocationList(List<ServiceLocation> serviceLocationList) {
		this.serviceLocationList = serviceLocationList;
	}
	public List<TransportInterface> getTransportInterfaceList() {
		return transportInterfaceList;
	}
	public void setTransportInterfaceList(List<TransportInterface> transportInterfaceList) {
		this.transportInterfaceList = transportInterfaceList;
	}
	public List<String> getVenIdList() {
		if (venIdList == null) {
			venIdList = new ArrayList<String>();
		}
		return venIdList;
	}
	public void setVenIdList(List<String> venIdList) {
		this.venIdList = venIdList;
	}
	
	
	
	
	

}
