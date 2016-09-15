package com.akuacom.pss2.drw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.akuacom.pss2.core.ErrorUtil;
import com.akuacom.pss2.drw.CFEventManager;
import com.akuacom.pss2.drw.DREvent2013Manager;
import com.akuacom.pss2.drw.constant.DRWConstants;
import com.akuacom.pss2.drw.core.Location;
import com.akuacom.pss2.drw.core.Program;
import com.akuacom.pss2.drw.model.factory.EventCacheFactory;
import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.BlockValue;


public class EventCache {
	
	private static Logger log = Logger.getLogger(EventCache.class);
	
	private EventCache(){
		initLibraryCache();
		
		//SDP Residential Active Legends&Events&Kmls
		actSDPResiEventLegends = new EventLegendCache();
		actSDPResiKml =EventCacheFactory.createKMLCache_ACTIVE_SDPR();
		sdprKmls = EventCacheFactory.createKMLCache_PRODUCT_SDPR();
		actSdprEvents = EventCacheFactory.createBaseEventCache_ACTIVE_SDPR();
		actSDPResiEventLegends.addObserver(actSDPResiKml);
		actSDPResiEventLegends.addObserver(sdprKmls);
		actSDPResiEventLegends.addObserver(actSdprEvents);
		
		//SDP Commercial Active Legends&Events&Kmls
		actSDPComeEventLegends = new EventLegendCache();
		actSDPComeKml =EventCacheFactory.createKMLCache_ACTIVE_SDPC();
		sdpcKmls = EventCacheFactory.createKMLCache_PRODUCT_SDPC();
		actSdpcEvents = EventCacheFactory.createBaseEventCache_ACTIVE_SDPC();
		actSDPComeEventLegends.addObserver(actSDPComeKml);
		actSDPComeEventLegends.addObserver(sdpcKmls);
		actSDPComeEventLegends.addObserver(actSdpcEvents);
		
		//API Commercial Active Legends&Events&Kmls
		actAPIEventLegends = new EventLegendCache();
		actApiKml =  EventCacheFactory.createKMLCache_ACTIVE_API();
		apiKmls = EventCacheFactory.createKMLCache_PRODUCT_API();
		actApiEvents = EventCacheFactory.createBaseEventCache_ACTIVE_API();
		actAPIEventLegends.addObserver(actApiKml);
		actAPIEventLegends.addObserver(apiKmls);
		actAPIEventLegends.addObserver(actApiEvents);
		
		//BIP Commercial Active Legends&Events&Kmls
		actBIPEventLegends = new EventLegendCache();
		actBipKml =  EventCacheFactory.createKMLCache_ACTIVE_BIP();
		bipKmls = EventCacheFactory.createKMLCache_PRODUCT_BIP();
		actBipEvents = EventCacheFactory.createBaseEventCache_ACTIVE_BIP();
		actBIPEventLegends.addObserver(actBipKml);
		actBIPEventLegends.addObserver(bipKmls);
		actBIPEventLegends.addObserver(actBipEvents);

		//CBP Commercial Active Legends&Events&Kmls
		actCBPEventLegends = new EventLegendCache();
		actCbpKml =  EventCacheFactory.createKMLCache_ACTIVE_CBP();
		cbpKmls = EventCacheFactory.createKMLCache_PRODUCT_CBP();
		actCbpEvents = EventCacheFactory.createBaseEventCache_ACTIVE_CBP();
		actCBPEventLegends.addObserver(actCbpKml);
		actCBPEventLegends.addObserver(cbpKmls);
		actCBPEventLegends.addObserver(actCbpEvents);
		
		//SDP Residential Schedule Legends&Events&Kmls
		scheSDPResiEventLegends = new EventLegendCache();
		scheSDPResiKml =EventCacheFactory.createKMLCache_SCHEDULED_SDPR();
		scheSdprEvents = EventCacheFactory.createBaseEventCache_SCHEDULED_SDPR();
		scheSDPResiEventLegends.addObserver(scheSDPResiKml);
		scheSDPResiEventLegends.addObserver(sdprKmls);
		scheSDPResiEventLegends.addObserver(scheSdprEvents);
		
		//SDP Commercial Schedule Legends&Events&Kmls
		scheSDPComeEventLegends = new EventLegendCache();
		scheSDPComeKml =EventCacheFactory.createKMLCache_SCHEDULED_SDPC();
		scheSdpcEvents = EventCacheFactory.createBaseEventCache_SCHEDULED_SDPC();
		scheSDPComeEventLegends.addObserver(scheSDPComeKml);
		scheSDPComeEventLegends.addObserver(sdpcKmls);
		scheSDPComeEventLegends.addObserver(scheSdpcEvents);
		
		//API Commercial Schedule Legends&Events&Kmls
		scheAPIEventLegends = new EventLegendCache();
		scheApiKml =  EventCacheFactory.createKMLCache_SCHEDULED_API();
		scheApiEvents = EventCacheFactory.createBaseEventCache_SCHEDULED_API();
		scheAPIEventLegends.addObserver(scheApiKml);
		scheAPIEventLegends.addObserver(apiKmls);
		scheAPIEventLegends.addObserver(scheApiEvents);
		
		//BIP Commercial Schedule Legends&Events&Kmls
		scheBIPEventLegends = new EventLegendCache();
		scheBipKml =  EventCacheFactory.createKMLCache_SCHEDULED_BIP();
		scheBipEvents = EventCacheFactory.createBaseEventCache_SCHEDULED_BIP();
		scheBIPEventLegends.addObserver(scheBipKml);
		scheBIPEventLegends.addObserver(bipKmls);
		scheBIPEventLegends.addObserver(scheBipEvents);

		//CBP Commercial Schedule Legends&Events&Kmls
		scheCBPEventLegends = new EventLegendCache();
		scheCbpKml =  EventCacheFactory.createKMLCache_SCHEDULED_CBP();
		scheCbpEvents = EventCacheFactory.createBaseEventCache_SCHEDULED_CBP();
		scheCBPEventLegends.addObserver(scheCbpKml);
		scheCBPEventLegends.addObserver(cbpKmls);
		scheCBPEventLegends.addObserver(scheCbpEvents);	
	}
	
	private void initLibraryCache(){
		try {
			CFEventManager manager=DRWUtil.getCFEventManager();
			cityList=manager.getCityName();
			countyList=manager.getCountyName();
			allBlocks = manager.getAllBlock();
			List<Location> locations = manager.getLocationEntity();
			if(locationCache==null){
				locationCache = new LocationCache();
			}
			locationCache.buildLocationCache(locations);
//			setSlapBlockMap(retrieveSlapBlockMap());
		}catch(Exception e) {
			log.error(ErrorUtil.getErrorMessage(e));
		}
	}
	
	private static EventCache instance = new EventCache();
	
	public static EventCache getInstance(){
		return instance;
	}
	private EventLegendCache actSDPResiEventLegends;
	private EventLegendCache actSDPComeEventLegends;
	private EventLegendCache actAPIEventLegends;
	private EventLegendCache actBIPEventLegends;
	private EventLegendCache actCBPEventLegends;
	
	private EventLegendCache scheSDPResiEventLegends;
	private EventLegendCache scheSDPComeEventLegends;
	private EventLegendCache scheAPIEventLegends;
	private EventLegendCache scheBIPEventLegends;
	private EventLegendCache scheCBPEventLegends;
	
	private KmlCache actSDPResiKml;
	private KmlCache actSDPComeKml;
	private KmlCache actApiKml;
	private KmlCache actCbpKml;
	private KmlCache actBipKml;
	private KmlCache scheSDPResiKml;
	private KmlCache scheSDPComeKml;
	private KmlCache scheApiKml;
	private KmlCache scheCbpKml;
	private KmlCache scheBipKml;
	
	private BaseEventDataModelCache actApiEvents;
	private BaseEventDataModelCache actBipEvents;
	private BaseEventDataModelCache actCbpEvents;
	private BaseEventDataModelCache actSdprEvents;
	private BaseEventDataModelCache actSdpcEvents;
	private BaseEventDataModelCache scheApiEvents;
	private BaseEventDataModelCache scheBipEvents;
	private BaseEventDataModelCache scheCbpEvents;
	private BaseEventDataModelCache scheSdprEvents;
	private BaseEventDataModelCache scheSdpcEvents;
	
	private KmlCache apiKmls;
	private KmlCache bipKmls;
	private KmlCache cbpKmls;
	private KmlCache sdprKmls;
	private KmlCache sdpcKmls;
	

	//Residential
	private List<BaseEventDataModel> activeSAIResiEvents = new ArrayList<BaseEventDataModel>();
	private List<BaseEventDataModel> scheduleSAIResiEvents = new ArrayList<BaseEventDataModel>();
	
	private List<BaseEventDataModel> activeSPDResiEvents = new ArrayList<BaseEventDataModel>();
	private List<BaseEventDataModel> scheduleSPDResiEvents = new ArrayList<BaseEventDataModel>();
	
	//Commercial
	
	private List<BaseEventDataModel> activeSAICommEvents = new ArrayList<BaseEventDataModel>();
	private List<BaseEventDataModel> scheduleSAICommEvents = new ArrayList<BaseEventDataModel>();
		
	private List<BaseEventDataModel> activeDBPCommEvents = new ArrayList<BaseEventDataModel>();
	private List<BaseEventDataModel> scheduleDBPCommEvents = new ArrayList<BaseEventDataModel>();
	
	private List<BaseEventDataModel> activeDRCCommEvents = new ArrayList<BaseEventDataModel>();
	private List<BaseEventDataModel> scheduleDRCCommEvents = new ArrayList<BaseEventDataModel>();
	
	private List<RTPEventDataModel> forcastRTPEvents = new ArrayList<RTPEventDataModel>();
	
	private RTPEventDataModel currentRTP = new RTPEventDataModel();
	
	boolean kioskActiveEventsUpdateFlag = true;
	boolean kioskScheduledEventsUpdateFlag = true;
	List<KioskEventDataModel> kioskActiveEvents = new ArrayList<KioskEventDataModel>();
	List<KioskEventDataModel> kioskScheduledEvents = new ArrayList<KioskEventDataModel>();
	
	//Programs
	private List<Program> programsCache = new ArrayList<Program>();
	
	private List<String> productCBPList= new ArrayList<String>();
	private List<String> productSAIList= new ArrayList<String>();
	private List<String> productDBPList= new ArrayList<String>();
	private List<String> productDRCList= new ArrayList<String>();
	private List<String> productRTPList= new ArrayList<String>();
	private List<String> productSDPList= new ArrayList<String>();
	private List<String> productAPIList= new ArrayList<String>();
	private List<String> productBIPList= new ArrayList<String>();
	
	
	//List View 
	
	/** The county list **/
	private List<String> countyList = new ArrayList<String>();
	
	private List<String> cityList = new ArrayList<String>();
	
	/** The city list map **/
	private Map<String,List<String>> cityMap = new HashMap<String,List<String>>();
		
	List<String> allBlocks;
	
	private LocationCache locationCache;
	
	private Map<String,List<Number>> slapBlockMap;
	private List<BlockValue> blockKMLs;//block 1<-->1 abank 1<-->* kml
	

	/**
	 * @return the activeSAIResiEvents
	 */
	public List<BaseEventDataModel> getActiveSAIResiEvents() {
		return activeSAIResiEvents;
	}

	/**
	 * @param activeSAIResiEvents the activeSAIResiEvents to set
	 */
	public void setActiveSAIResiEvents(List<BaseEventDataModel> activeSAIResiEvents) {
		this.activeSAIResiEvents = activeSAIResiEvents;
	}

	/**
	 * @return the scheduleSAIResiEvents
	 */
	public List<BaseEventDataModel> getScheduleSAIResiEvents() {
		return scheduleSAIResiEvents;
	}

	/**
	 * @param scheduleSAIResiEvents the scheduleSAIResiEvents to set
	 */
	public void setScheduleSAIResiEvents(
			List<BaseEventDataModel> scheduleSAIResiEvents) {
		this.scheduleSAIResiEvents = scheduleSAIResiEvents;
	}

	/**
	 * @return the activeSAICommEvents
	 */
	public List<BaseEventDataModel> getActiveSAICommEvents() {
		return activeSAICommEvents;
	}

	/**
	 * @param activeSAICommEvents the activeSAICommEvents to set
	 */
	public void setActiveSAICommEvents(List<BaseEventDataModel> activeSAICommEvents) {
		this.activeSAICommEvents = activeSAICommEvents;
	}

	/**
	 * @return the scheduleSAICommEvents
	 */
	public List<BaseEventDataModel> getScheduleSAICommEvents() {
		return scheduleSAICommEvents;
	}

	/**
	 * @param scheduleSAICommEvents the scheduleSAICommEvents to set
	 */
	public void setScheduleSAICommEvents(
			List<BaseEventDataModel> scheduleSAICommEvents) {
		this.scheduleSAICommEvents = scheduleSAICommEvents;
	}

	/**
	 * @return the activeDBPCommEvents
	 */
	public List<BaseEventDataModel> getActiveDBPCommEvents() {
		return activeDBPCommEvents;
	}

	/**
	 * @param activeDBPCommEvents the activeDBPCommEvents to set
	 */
	public void setActiveDBPCommEvents(List<BaseEventDataModel> activeDBPCommEvents) {
		this.activeDBPCommEvents = activeDBPCommEvents;
	}

	/**
	 * @return the scheduleDBPCommEvents
	 */
	public List<BaseEventDataModel> getScheduleDBPCommEvents() {
		return scheduleDBPCommEvents;
	}

	/**
	 * @param scheduleDBPCommEvents the scheduleDBPCommEvents to set
	 */
	public void setScheduleDBPCommEvents(
			List<BaseEventDataModel> scheduleDBPCommEvents) {
		this.scheduleDBPCommEvents = scheduleDBPCommEvents;
	}

	/**
	 * @return the activeDRCCommEvents
	 */
	public List<BaseEventDataModel> getActiveDRCCommEvents() {
		return activeDRCCommEvents;
	}

	/**
	 * @param activeDRCCommEvents the activeDRCCommEvents to set
	 */
	public void setActiveDRCCommEvents(List<BaseEventDataModel> activeDRCCommEvents) {
		this.activeDRCCommEvents = activeDRCCommEvents;
	}

	/**
	 * @return the scheduleDRCCommEvents
	 */
	public List<BaseEventDataModel> getScheduleDRCCommEvents() {
		return scheduleDRCCommEvents;
	}

	/**
	 * @param scheduleDRCCommEvents the scheduleDRCCommEvents to set
	 */
	public void setScheduleDRCCommEvents(
			List<BaseEventDataModel> scheduleDRCCommEvents) {
		this.scheduleDRCCommEvents = scheduleDRCCommEvents;
	}

	/**
	 * @return the forcastRTPEvents
	 */
	public List<RTPEventDataModel> getForcastRTPEvents() {
		return forcastRTPEvents;
	}

	/**
	 * @param forcastRTPEvents the forcastRTPEvents to set
	 */
	public void setForcastRTPEvents(List<RTPEventDataModel> forcastRTPEvents) {
		this.forcastRTPEvents = forcastRTPEvents;
	}

	/**
	 * @return the currentRTP
	 */
	public RTPEventDataModel getCurrentRTP() {
		if (forcastRTPEvents!=null && forcastRTPEvents.size()>0)
			currentRTP=forcastRTPEvents.get(0);
		return currentRTP;
	}

	/**
	 * @param currentRTP the currentRTP to set
	 */
	public void setCurrentRTP(RTPEventDataModel currentRTP) {
		this.currentRTP = currentRTP;
	}

	/**
	 * @param programsCache the programsCache to set
	 */
	public void setProgramsCache(List<Program> programsCache) {
		this.programsCache = programsCache;
	}

	/**
	 * @return the programsCache
	 */
	public List<Program> getProgramsCache() {
		return programsCache;
	}

	/**
	 * @return the productCBPList
	 */
	public List<String> getProductCBPList() {
		Map map = new TreeMap();
		for(String product:productCBPList){
			if(!map.containsKey(product)){
				map.put(product, product);
			}
		}
		Set<String> set = map.keySet();
		productCBPList.clear();
		for(String value:set){
			productCBPList.add(value);
		}
		return productCBPList;
	}

	/**
	 * @param productCBPList the productCBPList to set
	 */
	public void setProductCBPList(List<String> productCBPList) {
		this.productCBPList = productCBPList;
	}

	/**
	 * @return the productSAIList
	 */
	public List<String> getProductSAIList() {
		return productSAIList;
	}

	/**
	 * @param productSAIList the productSAIList to set
	 */
	public void setProductSAIList(List<String> productSAIList) {
		this.productSAIList = productSAIList;
	}

	/**
	 * @return the productDBPList
	 */
	public List<String> getProductDBPList() {
		return productDBPList;
	}

	/**
	 * @param productDBPList the productDBPList to set
	 */
	public void setProductDBPList(List<String> productDBPList) {
		this.productDBPList = productDBPList;
	}

	/**
	 * @return the productDRCList
	 */
	public List<String> getProductDRCList() {
		return productDRCList;
	}

	/**
	 * @param productDRCList the productDRCList to set
	 */
	public void setProductDRCList(List<String> productDRCList) {
		this.productDRCList = productDRCList;
	}

	/**
	 * @return the productRTPList
	 */
	public List<String> getProductRTPList() {
		return productRTPList;
	}

	/**
	 * @param productRTPList the productRTPList to set
	 */
	public void setProductRTPList(List<String> productRTPList) {
		this.productRTPList = productRTPList;
	}


	/**
	 * @return the productSDPList
	 */
	public List<String> getProductSDPList() {
		return productSDPList;
	}

	/**
	 * @param productSDPList the productSDPList to set
	 */
	public void setProductSDPList(List<String> productSDPList) {
		this.productSDPList = productSDPList;
	}

	/**
	 * @return the productAPIList
	 */
	public List<String> getProductAPIList() {
		return productAPIList;
	}

	/**
	 * @param productAPIList the productAPIList to set
	 */
	public void setProductAPIList(List<String> productAPIList) {
		this.productAPIList = productAPIList;
	}

	/**
	 * @return the productBIPList
	 */
	public List<String> getProductBIPList() {
		return productBIPList;
	}

	/**
	 * @param productBIPList the productBIPList to set
	 */
	public void setProductBIPList(List<String> productBIPList) {
		this.productBIPList = productBIPList;
	}

	/**
	 * @return the countyList
	 */
	public List<String> getCountyList() {

		return countyList;
	}

	/**
	 * @return the cityMap
	 */
	public Map<String, List<String>> getCityMap() {
		return cityMap;
	}

	/**
	 * @param cityMap the cityMap to set
	 */
	public void setCityMap(Map<String, List<String>> cityMap) {
		this.cityMap = cityMap;
	}

	/**
	 * @param countyList the countyList to set
	 */
	public void setCountyList(List<String> countyList) {
		this.countyList = countyList;
	}

	/**
	 * @return the kioskActiveEvents
	 */
	public List<KioskEventDataModel> getKioskActiveEvents() {
		return kioskActiveEvents;
	}

	/**
	 * @param kioskActiveEvents the kioskActiveEvents to set
	 */
	public void setKioskActiveEvents(List<KioskEventDataModel> kioskActiveEvents) {
			this.kioskActiveEvents = kioskActiveEvents;
	}
	/**
	 * @return the kioskScheduledEvents
	 */
	public List<KioskEventDataModel> getKioskScheduledEvents() {
		return kioskScheduledEvents;
	}
	/**
	 * @param kioskScheduledEvents the kioskScheduledEvents to set
	 */
	public void setKioskScheduledEvents(
			List<KioskEventDataModel> kioskScheduledEvents) {
			this.kioskScheduledEvents = kioskScheduledEvents;
	}

	/**
	 * @return the kioskActiveEventsUpdateFlag
	 */
	public boolean isKioskActiveEventsUpdateFlag() {
		return kioskActiveEventsUpdateFlag;
	}

	/**
	 * @param kioskActiveEventsUpdateFlag the kioskActiveEventsUpdateFlag to set
	 */
	public void setKioskActiveEventsUpdateFlag(boolean kioskActiveEventsUpdateFlag) {
		this.kioskActiveEventsUpdateFlag = kioskActiveEventsUpdateFlag;
	}

	/**
	 * @return the kioskScheduledEventsUpdateFlag
	 */
	public boolean isKioskScheduledEventsUpdateFlag() {
		return kioskScheduledEventsUpdateFlag;
	}

	/**
	 * @param kioskScheduledEventsUpdateFlag the kioskScheduledEventsUpdateFlag to set
	 */
	public void setKioskScheduledEventsUpdateFlag(
			boolean kioskScheduledEventsUpdateFlag) {
		this.kioskScheduledEventsUpdateFlag = kioskScheduledEventsUpdateFlag;
	}

	/**
	 * @param cityList the cityList to set
	 */
	public void setCityList(List<String> cityList) {
		this.cityList = cityList;
	}

	/**
	 * @return the cityList
	 */
	public List<String> getCityList() {
		return cityList;
	}



	/**
	 * @return the activeSPDResiEvents
	 */
	public List<BaseEventDataModel> getActiveSPDResiEvents() {
		return activeSPDResiEvents;
	}

	/**
	 * @param activeSPDResiEvents the activeSPDResiEvents to set
	 */
	public void setActiveSPDResiEvents(List<BaseEventDataModel> activeSPDResiEvents) {
		this.activeSPDResiEvents = activeSPDResiEvents;
	}

	/**
	 * @return the scheduleSPDResiEvents
	 */
	public List<BaseEventDataModel> getScheduleSPDResiEvents() {
		return scheduleSPDResiEvents;
	}

	/**
	 * @param scheduleSPDResiEvents the scheduleSPDResiEvents to set
	 */
	public void setScheduleSPDResiEvents(
			List<BaseEventDataModel> scheduleSPDResiEvents) {
		this.scheduleSPDResiEvents = scheduleSPDResiEvents;
	}


	/**
	 * @return the allBlocks
	 */
	public List<String> getAllBlocks() {
		return allBlocks;
	}

	/**
	 * @param allBlocks the allBlocks to set
	 */
	public void setAllBlocks(List<String> allBlocks) {
		this.allBlocks = allBlocks;
	}

	/**
	 * @return the locationCache
	 */
	public LocationCache getLocationCache() {
		return locationCache;
	}

	/**
	 * @param locationCache the locationCache to set
	 */
	public void setLocationCache(LocationCache locationCache) {
		this.locationCache = locationCache;
	}

	/**
	 * @return the slapBlockMap
	 */
	public Map<String, List<Number>> getSlapBlockMap() {
		return slapBlockMap;
	}

	/**
	 * @param slapBlockMap the slapBlockMap to set
	 */
	public void setSlapBlockMap(Map<String, List<Number>> slapBlockMap) {
		this.slapBlockMap = slapBlockMap;
	}
	
	public Map<String,List<Number>> retrieveSlapBlockMap(){
		Map<String,List<Number>> map= new HashMap<String,List<Number>>();
		List<Number> scec = new ArrayList<Number>();

		List<Number> scen = new ArrayList<Number>();

		List<Number> scnw = new ArrayList<Number>();

		List<Number> scew = new ArrayList<Number>();

		List<Number> schd = new ArrayList<Number>();

		List<Number> scld = new ArrayList<Number>();
		
		LocationCache locationCache = EventCache.getInstance().getLocationCache();
		if(locationCache!=null){
			List<LocationCache> slapCache = locationCache.getSlapLocations();
			for(LocationCache slap:slapCache){
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCEC)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						Long blockNumber =Long.valueOf(block.getBlock());
						scec.add(blockNumber);
					}
				}
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCEN)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						Long blockNumber =Long.valueOf(block.getBlock());
						scen.add(blockNumber);
					}
				}
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCNW)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						Long blockNumber =Long.valueOf(block.getBlock());
						scnw.add(blockNumber);
					}
				}
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCEW)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						Long blockNumber =Long.valueOf(block.getBlock());
						scew.add(blockNumber);
					}
				}
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCLD)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						Long blockNumber =Long.valueOf(block.getBlock());
						scld.add(blockNumber);
					}
				}
				if(slap.getNumber().equalsIgnoreCase(DRWUtil.SCHD)){
					List<LocationCache> blockCache = slap.getBlockLocations();
					for(LocationCache block:blockCache){
						Long blockNumber =Long.valueOf(block.getBlock());
						schd.add(blockNumber);
					}
				}
			}
		}
		


		map.put("SCEC", scec);
		map.put("SCEN", scen);
		map.put("SCNW", scnw);
		map.put("SCEW", scew);
		map.put("SCHD", schd);
		map.put("SCLD", scld);
		return map;
	}


	
	/**
	 * @return the blockKMLs
	 */
	public List<BlockValue> getBlockKMLs() {
		return blockKMLs;
	}

	/**
	 * @param blockKMLs the blockKMLs to set
	 */
	public void setBlockKMLs(List<BlockValue> blockKMLs) {
		this.blockKMLs = blockKMLs;
	}

	public List<BlockValue> retrieveBlockKMLMap(){
		List<BlockValue> result = new ArrayList<BlockValue>();
		if(getAllBlocks().size()>0){
			DREvent2013Manager manager=DRWUtil.getDREvent2013Manager();
			result = manager.getKMLS(getAllBlocks());
		}
		return result;
	}
	
	
	//get block from cache
	public List<String> getBlockKML(boolean isSLAP,String slapNumber,String blockInput){
		List<String> result = new ArrayList<String>();
		if(blockKMLs.size()<=0){
			blockKMLs = retrieveBlockKMLMap();
		}
		if(isSLAP){
			//retrieve kml for slap
			List<Number> blocks = slapBlockMap.get(slapNumber);
			for(BlockValue block:blockKMLs){
				String blockS =  block.getBlockNumber();
				for(Number blockNumber:blocks){
					String blockString = String.valueOf(blockNumber);
					if(blockS!=null&&blockS.equalsIgnoreCase(blockString)){
						
						result.addAll(block.getKmls());	
						
					}
				}
			}
		}else{
			for(BlockValue block:blockKMLs){
				String blockS =  block.getBlockNumber();
				if(blockS!=null&&blockS.equalsIgnoreCase(blockInput)){
					result.addAll(block.getKmls());	
				}
			}
		}
		return result;		
	}
	
	public String getSLAPGroupName(String abankNumber){
		String result ="";
		long block = Long.parseLong(abankNumber);
		List<Number> blocks = new ArrayList<Number>();
		if(slapBlockMap.size()<=0){
			slapBlockMap = retrieveSlapBlockMap();
		}
		blocks =slapBlockMap.get(DRWConstants.SCEC);
		for(Number blockCompare:blocks){
			long b = (Long) blockCompare;
			if(block == b){
				return DRWConstants.SCEC;
			}
		}
		blocks =slapBlockMap.get(DRWConstants.SCEW);
		for(Number blockCompare:blocks){
			long b = (Long) blockCompare;
			if(block == b){
				return DRWConstants.SCEW;
			}
		}
		blocks =slapBlockMap.get(DRWConstants.SCEN);
		for(Number blockCompare:blocks){
			long b = (Long) blockCompare;
			if(block == b){
				return DRWConstants.SCEN;
			}
		}
		blocks =slapBlockMap.get(DRWConstants.SCNW);
		for(Number blockCompare:blocks){
			long b = (Long) blockCompare;
			if(block == b){
				return DRWConstants.SCNW;
			}
		}
		blocks =slapBlockMap.get(DRWConstants.SCHD);
		for(Number blockCompare:blocks){
			long b = (Long) blockCompare;
			if(block == b){
				return DRWConstants.SCHD;
			}
		}
		blocks =slapBlockMap.get(DRWConstants.SCLD);
		for(Number blockCompare:blocks){
			long b = (Long) blockCompare;
			if(block == b){
				return DRWConstants.SCLD;
			}
		}
		return result;
	}

	/**
	 * @return the actApiEvents
	 */
	public BaseEventDataModelCache getActApiEvents() {
		return actApiEvents;
	}

	/**
	 * @param actApiEvents the actApiEvents to set
	 */
	public void setActApiEvents(BaseEventDataModelCache actApiEvents) {
		this.actApiEvents = actApiEvents;
	}

	/**
	 * @return the apiKmls
	 */
	public KmlCache getApiKmls() {
		return apiKmls;
	}

	/**
	 * @param apiKmls the apiKmls to set
	 */
	public void setApiKmls(KmlCache apiKmls) {
		this.apiKmls = apiKmls;
	}

	/**
	 * @return the actSDPResiEventLegends
	 */
	public EventLegendCache getActSDPResiEventLegends() {
		return actSDPResiEventLegends;
	}

	/**
	 * @param actSDPResiEventLegends the actSDPResiEventLegends to set
	 */
	public void setActSDPResiEventLegends(EventLegendCache actSDPResiEventLegends) {
		this.actSDPResiEventLegends = actSDPResiEventLegends;
	}

	/**
	 * @return the actSDPComeEventLegends
	 */
	public EventLegendCache getActSDPComeEventLegends() {
		return actSDPComeEventLegends;
	}

	/**
	 * @param actSDPComeEventLegends the actSDPComeEventLegends to set
	 */
	public void setActSDPComeEventLegends(EventLegendCache actSDPComeEventLegends) {
		this.actSDPComeEventLegends = actSDPComeEventLegends;
	}


	/**
	 * @return the scheSDPResiEventLegends
	 */
	public EventLegendCache getScheSDPResiEventLegends() {
		return scheSDPResiEventLegends;
	}

	/**
	 * @param scheSDPResiEventLegends the scheSDPResiEventLegends to set
	 */
	public void setScheSDPResiEventLegends(EventLegendCache scheSDPResiEventLegends) {
		this.scheSDPResiEventLegends = scheSDPResiEventLegends;
	}

	/**
	 * @return the scheSDPComeEventLegends
	 */
	public EventLegendCache getScheSDPComeEventLegends() {
		return scheSDPComeEventLegends;
	}

	/**
	 * @param scheSDPComeEventLegends the scheSDPComeEventLegends to set
	 */
	public void setScheSDPComeEventLegends(EventLegendCache scheSDPComeEventLegends) {
		this.scheSDPComeEventLegends = scheSDPComeEventLegends;
	}


	/**
	 * @return the actSDPResiKml
	 */
	public KmlCache getActSDPResiKml() {
		return actSDPResiKml;
	}

	/**
	 * @param actSDPResiKml the actSDPResiKml to set
	 */
	public void setActSDPResiKml(KmlCache actSDPResiKml) {
		this.actSDPResiKml = actSDPResiKml;
	}

	/**
	 * @return the actSDPComeKml
	 */
	public KmlCache getActSDPComeKml() {
		return actSDPComeKml;
	}

	/**
	 * @param actSDPComeKml the actSDPComeKml to set
	 */
	public void setActSDPComeKml(KmlCache actSDPComeKml) {
		this.actSDPComeKml = actSDPComeKml;
	}

	/**
	 * @return the actApiKml
	 */
	public KmlCache getActApiKml() {
		return actApiKml;
	}

	/**
	 * @param actApiKml the actApiKml to set
	 */
	public void setActApiKml(KmlCache actApiKml) {
		this.actApiKml = actApiKml;
	}

	/**
	 * @return the actCbpKml
	 */
	public KmlCache getActCbpKml() {
		return actCbpKml;
	}

	/**
	 * @param actCbpKml the actCbpKml to set
	 */
	public void setActCbpKml(KmlCache actCbpKml) {
		this.actCbpKml = actCbpKml;
	}


	/**
	 * @return the scheSDPResiKml
	 */
	public KmlCache getScheSDPResiKml() {
		return scheSDPResiKml;
	}

	/**
	 * @param scheSDPResiKml the scheSDPResiKml to set
	 */
	public void setScheSDPResiKml(KmlCache scheSDPResiKml) {
		this.scheSDPResiKml = scheSDPResiKml;
	}

	/**
	 * @return the scheSDPComeKml
	 */
	public KmlCache getScheSDPComeKml() {
		return scheSDPComeKml;
	}

	/**
	 * @param scheSDPComeKml the scheSDPComeKml to set
	 */
	public void setScheSDPComeKml(KmlCache scheSDPComeKml) {
		this.scheSDPComeKml = scheSDPComeKml;
	}

	/**
	 * @return the scheApiKml
	 */
	public KmlCache getScheApiKml() {
		return scheApiKml;
	}

	/**
	 * @param scheApiKml the scheApiKml to set
	 */
	public void setScheApiKml(KmlCache scheApiKml) {
		this.scheApiKml = scheApiKml;
	}

	/**
	 * @return the scheCbpKml
	 */
	public KmlCache getScheCbpKml() {
		return scheCbpKml;
	}

	/**
	 * @param scheCbpKml the scheCbpKml to set
	 */
	public void setScheCbpKml(KmlCache scheCbpKml) {
		this.scheCbpKml = scheCbpKml;
	}


	/**
	 * @return the actBipEvents
	 */
	public BaseEventDataModelCache getActBipEvents() {
		return actBipEvents;
	}

	/**
	 * @param actBipEvents the actBipEvents to set
	 */
	public void setActBipEvents(BaseEventDataModelCache actBipEvents) {
		this.actBipEvents = actBipEvents;
	}

	/**
	 * @return the actCbpEvents
	 */
	public BaseEventDataModelCache getActCbpEvents() {
		return actCbpEvents;
	}

	/**
	 * @param actCbpEvents the actCbpEvents to set
	 */
	public void setActCbpEvents(BaseEventDataModelCache actCbpEvents) {
		this.actCbpEvents = actCbpEvents;
	}

	/**
	 * @return the actSdprEvents
	 */
	public BaseEventDataModelCache getActSdprEvents() {
		return actSdprEvents;
	}

	/**
	 * @param actSdprEvents the actSdprEvents to set
	 */
	public void setActSdprEvents(BaseEventDataModelCache actSdprEvents) {
		this.actSdprEvents = actSdprEvents;
	}

	/**
	 * @return the actSdpcEvents
	 */
	public BaseEventDataModelCache getActSdpcEvents() {
		return actSdpcEvents;
	}

	/**
	 * @param actSdpcEvents the actSdpcEvents to set
	 */
	public void setActSdpcEvents(BaseEventDataModelCache actSdpcEvents) {
		this.actSdpcEvents = actSdpcEvents;
	}

	/**
	 * @return the scheApiEvents
	 */
	public BaseEventDataModelCache getScheApiEvents() {
		return scheApiEvents;
	}

	/**
	 * @param scheApiEvents the scheApiEvents to set
	 */
	public void setScheApiEvents(BaseEventDataModelCache scheApiEvents) {
		this.scheApiEvents = scheApiEvents;
	}

	/**
	 * @return the scheBipEvents
	 */
	public BaseEventDataModelCache getScheBipEvents() {
		return scheBipEvents;
	}

	/**
	 * @param scheBipEvents the scheBipEvents to set
	 */
	public void setScheBipEvents(BaseEventDataModelCache scheBipEvents) {
		this.scheBipEvents = scheBipEvents;
	}

	/**
	 * @return the scheCbpEvents
	 */
	public BaseEventDataModelCache getScheCbpEvents() {
		return scheCbpEvents;
	}

	/**
	 * @param scheCbpEvents the scheCbpEvents to set
	 */
	public void setScheCbpEvents(BaseEventDataModelCache scheCbpEvents) {
		this.scheCbpEvents = scheCbpEvents;
	}

	/**
	 * @return the scheSdprEvents
	 */
	public BaseEventDataModelCache getScheSdprEvents() {
		return scheSdprEvents;
	}

	/**
	 * @param scheSdprEvents the scheSdprEvents to set
	 */
	public void setScheSdprEvents(BaseEventDataModelCache scheSdprEvents) {
		this.scheSdprEvents = scheSdprEvents;
	}

	/**
	 * @return the scheSdpcEvents
	 */
	public BaseEventDataModelCache getScheSdpcEvents() {
		return scheSdpcEvents;
	}

	/**
	 * @param scheSdpcEvents the scheSdpcEvents to set
	 */
	public void setScheSdpcEvents(BaseEventDataModelCache scheSdpcEvents) {
		this.scheSdpcEvents = scheSdpcEvents;
	}

	/**
	 * @return the bipKmls
	 */
	public KmlCache getBipKmls() {
		return bipKmls;
	}

	/**
	 * @param bipKmls the bipKmls to set
	 */
	public void setBipKmls(KmlCache bipKmls) {
		this.bipKmls = bipKmls;
	}

	/**
	 * @return the cbpKmls
	 */
	public KmlCache getCbpKmls() {
		return cbpKmls;
	}

	/**
	 * @param cbpKmls the cbpKmls to set
	 */
	public void setCbpKmls(KmlCache cbpKmls) {
		this.cbpKmls = cbpKmls;
	}

	/**
	 * @return the sdprKmls
	 */
	public KmlCache getSdprKmls() {
		return sdprKmls;
	}

	/**
	 * @param sdprKmls the sdprKmls to set
	 */
	public void setSdprKmls(KmlCache sdprKmls) {
		this.sdprKmls = sdprKmls;
	}

	/**
	 * @return the sdpcKmls
	 */
	public KmlCache getSdpcKmls() {
		return sdpcKmls;
	}

	/**
	 * @param sdpcKmls the sdpcKmls to set
	 */
	public void setSdpcKmls(KmlCache sdpcKmls) {
		this.sdpcKmls = sdpcKmls;
	}

	/**
	 * @return the actAPIEventLegends
	 */
	public EventLegendCache getActAPIEventLegends() {
		return actAPIEventLegends;
	}

	/**
	 * @param actAPIEventLegends the actAPIEventLegends to set
	 */
	public void setActAPIEventLegends(EventLegendCache actAPIEventLegends) {
		this.actAPIEventLegends = actAPIEventLegends;
	}

	/**
	 * @return the actBIPEventLegends
	 */
	public EventLegendCache getActBIPEventLegends() {
		return actBIPEventLegends;
	}

	/**
	 * @param actBIPEventLegends the actBIPEventLegends to set
	 */
	public void setActBIPEventLegends(EventLegendCache actBIPEventLegends) {
		this.actBIPEventLegends = actBIPEventLegends;
	}

	/**
	 * @return the actCBPEventLegends
	 */
	public EventLegendCache getActCBPEventLegends() {
		return actCBPEventLegends;
	}

	/**
	 * @param actCBPEventLegends the actCBPEventLegends to set
	 */
	public void setActCBPEventLegends(EventLegendCache actCBPEventLegends) {
		this.actCBPEventLegends = actCBPEventLegends;
	}

	/**
	 * @return the scheAPIEventLegends
	 */
	public EventLegendCache getScheAPIEventLegends() {
		return scheAPIEventLegends;
	}

	/**
	 * @param scheAPIEventLegends the scheAPIEventLegends to set
	 */
	public void setScheAPIEventLegends(EventLegendCache scheAPIEventLegends) {
		this.scheAPIEventLegends = scheAPIEventLegends;
	}

	/**
	 * @return the scheBIPEventLegends
	 */
	public EventLegendCache getScheBIPEventLegends() {
		return scheBIPEventLegends;
	}

	/**
	 * @param scheBIPEventLegends the scheBIPEventLegends to set
	 */
	public void setScheBIPEventLegends(EventLegendCache scheBIPEventLegends) {
		this.scheBIPEventLegends = scheBIPEventLegends;
	}

	/**
	 * @return the scheCBPEventLegends
	 */
	public EventLegendCache getScheCBPEventLegends() {
		return scheCBPEventLegends;
	}

	/**
	 * @param scheCBPEventLegends the scheCBPEventLegends to set
	 */
	public void setScheCBPEventLegends(EventLegendCache scheCBPEventLegends) {
		this.scheCBPEventLegends = scheCBPEventLegends;
	}

	/**
	 * @return the actBipKml
	 */
	public KmlCache getActBipKml() {
		return actBipKml;
	}

	/**
	 * @param actBipKml the actBipKml to set
	 */
	public void setActBipKml(KmlCache actBipKml) {
		this.actBipKml = actBipKml;
	}

	/**
	 * @return the scheBipKml
	 */
	public KmlCache getScheBipKml() {
		return scheBipKml;
	}

	/**
	 * @param scheBipKml the scheBipKml to set
	 */
	public void setScheBipKml(KmlCache scheBipKml) {
		this.scheBipKml = scheBipKml;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
