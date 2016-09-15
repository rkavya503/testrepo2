package com.akuacom.pss2.drw.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.drw.util.DRWUtil;
import com.akuacom.pss2.drw.value.EventValue;


public class BaseEventDataModel implements Serializable{

	private static final long serialVersionUID = 12304978574034L;
	
	private String key;
	
	private String eventDateString;
	private String eventStartTimeString;
	private String eventEndTimeString;
	private String longProgramName;
	private String programName;
	private String programClass;
	private EventValue event;
	private String product;
	
	private String startDateString;
	private String endDateString;
	private Date startDate;
	private Date endDate;
	
	
	private String countyString;
	private String cityString;
	private String zipCodeString;
	
	private String blockS;
	private String blockN;
	
	private String blockString;
	
	private String tbdFlag;
	private String tbdEndDateString;
	private String tbdEndTimeString;
	
	private boolean isAPIHistoryFlag = false;
	
	private String legendDisplayString="";
	private String eventDisplayID="";
	
	private String blockStringWithoutCombine;
	/**
	 * @return the tbdEndDateString
	 */
	public String getTbdEndDateString() {
		
		String result = getEndDateString();
		if(result!=null){
			if(result.equalsIgnoreCase("")||result.equalsIgnoreCase("TBD")){
				tbdEndDateString = "TBD";
			}else{
				tbdEndDateString = result;
			}
		}else{
			tbdEndDateString="TBD";
		}
		
		return tbdEndDateString;
	}
	/**
	 * @param tbdEndDateString the tbdEndDateString to set
	 */
	public void setTbdEndDateString(String tbdEndDateString) {
		this.tbdEndDateString = tbdEndDateString;
	}
	/**
	 * @return the tbdEndTimeString
	 */
	public String getTbdEndTimeString() {
		String result = getEventEndTimeString();
		if(result!=null){
			if(result.equalsIgnoreCase("")||result.equalsIgnoreCase("TBD")){
				tbdEndTimeString = "TBD";
			}else{
				tbdEndTimeString = result;
			}
		}else{
			tbdEndTimeString="TBD";
		}
		
		return tbdEndTimeString;
	}
	/**
	 * @param tbdEndTimeString the tbdEndTimeString to set
	 */
	public void setTbdEndTimeString(String tbdEndTimeString) {
		this.tbdEndTimeString = tbdEndTimeString;
	}
	public BaseEventDataModel(){
		super();
	}
	public BaseEventDataModel(EventValue event){
		super();
		setEvent(event);
		this.setStartDate(event.getStartTime());
		this.setEndDate(event.getEndTime());
		this.setLongProgramName(event.getLongProgramName());
		this.setProgramName(event.getProduct());
		this.setProgramClass(event.getProgramClass());
		
	}
	public BaseEventDataModel(boolean isMock){
		super();
		if(isMock){
			setProgramClass("programClass");
			setLongProgramName("longProgramName");
			EventValue event = new EventValue();
			event.setStartTime(new Date());
			event.setEndTime(new Date());
			event.setBlock("a,b,c,d,e,f,g,ha,b,c,d,e,f,g,ha,b,c,d,e,f,g,h");
			event.setProduct("product");
			setEvent(event);
			this.setStartDate(new Date());
			this.setEndDate(new Date());
			
		}
	}
	public BaseEventDataModel(boolean isMock,String program,String product){
		super();
		if(isMock){
			if(program==null){
				setProgramClass("programClass");
			}else{
				setProgramClass(program);
			}
			if(product==null){
				setLongProgramName("longProgramName");
			}else{
				setLongProgramName(product);
			}
			EventValue event = new EventValue();
			event.setStartTime(new Date());
			event.setEndTime(new Date());
			setEvent(event);
			this.setStartDate(new Date());
			this.setEndDate(new Date());
		}
	}
	

	public String getEventDateString() {
		if(getEvent()!=null&&getEvent().getStartTime()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			eventDateString = sdf.format(getEvent().getStartTime());
		}
		return eventDateString;
	}
	public void setEventDateString(String eventDateString) {
		this.eventDateString = eventDateString;
	}
	public String getEventStartTimeString() {
		if(getStartDate()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mma");
			eventStartTimeString = sdf.format(getStartDate());
		}
		return eventStartTimeString;
	}
	public void setEventStartTimeString(String eventStartTimeString) {
		this.eventStartTimeString = eventStartTimeString;
	}
	public String getEventEndTimeString() {
		//DRMS-7583
		if(event!=null){
			if(event.getTbdFlag()!=null&&(!event.getTbdFlag().equalsIgnoreCase(""))){
				if(event.getTbdFlag().equalsIgnoreCase("1")){
					eventEndTimeString="TBD";
					return eventEndTimeString;
				}
			}
		}
		
		if(getEndDate()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mma");
			eventEndTimeString = sdf.format(getEndDate());
		}
		return eventEndTimeString;
	}
	public void setEventEndTimeString(String eventEndTimeString) {
		this.eventEndTimeString = eventEndTimeString;
	}

	/**
	 * @return the longProgramName
	 */
	public String getLongProgramName() {
		return longProgramName;
	}

	/**
	 * @return the programName
	 */
	public String getProgramName() {
		return programName;
	}

	/**
	 * @return the programClass
	 */
	public String getProgramClass() {
		return programClass;
	}

	/**
	 * @return the event
	 */
	public EventValue getEvent() {
		return event;
	}

	/**
	 * @param longProgramName the longProgramName to set
	 */
	public void setLongProgramName(String longProgramName) {
		this.longProgramName = longProgramName;
	}

	/**
	 * @param programName the programName to set
	 */
	public void setProgramName(String programName) {
		this.programName = programName;
	}

	/**
	 * @param programClass the programClass to set
	 */
	public void setProgramClass(String programClass) {
		this.programClass = programClass;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(EventValue event) {
		this.event = event;
	}
	/**
	 * @return the startDateString
	 */
	public String getStartDateString() {
		if(getStartDate()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			startDateString = sdf.format(getStartDate());
		}
		return startDateString;
	}

	/**
	 * @param startDateString the startDateString to set
	 */
	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}

	/**
	 * @return the endDateString
	 */
	public String getEndDateString() {
		//DRMS-7583
		if(event!=null){
			if(event.getTbdFlag()!=null&&(!event.getTbdFlag().equalsIgnoreCase(""))){
				if(event.getTbdFlag().equalsIgnoreCase("1")){
					endDateString="TBD";
					return endDateString;
				}
			}
		}
		
		
		if(getEndDate()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			endDateString = sdf.format(getEndDate());
		}
		return endDateString;
	}

	/**
	 * @param endDateString the endDateString to set
	 */
	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}	
	
	public BaseEventDataModel clone(){
		return new BaseEventDataModel();
	}
	/**
	 * @return the product
	 */
	public String getProduct() {
		if(event!=null&&event.getProduct()!=null){
			product = event.getProduct();
		}else{
			product="";
		}
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}
	/**
	 * @return the countyString
	 */
	public String getCountyString() {
		countyString="";
		if(event!=null&&event.getCounties()!=null){
			
			DRWUtil.sortList(event.getCounties());
			
			for(String county:event.getCounties()){
				if(county!=null&&(!county.trim().equalsIgnoreCase(""))){
					countyString+=county+", ";	
				}
			}
			if(countyString.endsWith(", ")){
				countyString=countyString.substring(0, countyString.length()-2);
			}
		}else{
			countyString="";
		}
		return countyString;
	}
	/**
	 * @return the cityString
	 */
	public String getCityString() {
		cityString="";
		if(event!=null&&event.getCities()!=null){
			
			DRWUtil.sortList(event.getCities());
			
			for(String city:event.getCities()){
				if(city!=null&&(!city.trim().equalsIgnoreCase(""))){
					cityString+=city+", ";
				}
			}
			if(cityString.endsWith(", ")){
				cityString=cityString.substring(0, cityString.length()-2);
			}
		}else{
			cityString="";
		}
		return cityString;
	}
	/**
	 * @return the zipCodeString
	 */
	public String getZipCodeString() {
		zipCodeString="";
		if(event!=null&&event.getZipCodes()!=null){
			
			DRWUtil.sortList(event.getZipCodes());
			
			for(String zipCode:event.getZipCodes()){
				if(zipCode!=null&&(!zipCode.trim().equalsIgnoreCase(""))){
					zipCodeString+=zipCode+", ";	
				}
			}
			if(zipCodeString.endsWith(", ")){
				zipCodeString=zipCodeString.substring(0, zipCodeString.length()-2);
			}
		}else{
			zipCodeString="";
		}
		return zipCodeString;
	}
	/**
	 * @return the blockS
	 */
	public String getBlockS() {
		blockS=null;
		List<String> list = this.getEvent().getBlocks();
		list = DRWUtil.trimList(list);
		if (list!=null&&list.size()>0) {
			DRWUtil.sortList(list);
			blockS=null;
			for (String b:list){
				if((b!=null)&&(!b.trim().equalsIgnoreCase(""))){
					if (blockS==null)
						blockS=b;
					else{
						if(b.toString().equalsIgnoreCase("")){
							
						}else{
							blockS+=", "+b;	
						}
						
					}
						
						
				}
			}
		}else{
			blockS = this.getEvent().getBlock();
		}
		return blockS;
	}
	/**
	 * @return the blockN
	 */
	public String getBlockN() {
		blockN=null;
		List<String> list = this.getEvent().getBlocks();
		List<Number> listN = new ArrayList<Number>();
		if (list!=null&&list.size()>0) {
			for (String b:list){
				
				Number num = DRWUtil.toNumber(b);
				if(num!=null){
					listN.add(num);
				}
			}
		}
		DRWUtil.sortNumberList(listN);
		blockN = DRWUtil.getBlockDisplayString(listN);
//		for (Number b:listN){
//			if(b!=null){
//				if (blockN==null)
//					blockN=String.valueOf(b);
//				else
//					blockN+=", "+b;
//			}
//		}
		return blockN;
	}

	/**
	 * @param blockString the blockString to set
	 */
	public void setBlockString(String blockString) {
		this.blockString = blockString;
	}
	/**
	 * @return the blockString
	 */
	public String getBlockString() {
		blockString=null;
		List<String> list = this.getEvent().getBlocks();
		boolean isAll = DRWUtil.isAllBlocks(list);
		if(isAll){
			return "All Blocks";
		}
		if(list.size()>0){//API
				String str = list.get(0);
				if(DRWUtil.toNumber(str)!=null){
					//number
					blockString = getBlockN();
				}else{
					//String
					blockString = getBlockS();
				}
		}else{//BIP
			blockString =  this.getEvent().getBlock();
		}
		//DRMS-7470
		if(isAPIHistoryFlag){
			Date compareDate = new Date("2012/01/01 00:00:00");
			if(this.getEvent().getStartTime().before(compareDate)){
				blockString="All Blocks";
			}
		}
		if(blockString!=null&&blockString.endsWith(", ")){
			blockString=blockString.substring(0, blockString.length()-2);
		}else if(blockString==null||blockString.equalsIgnoreCase("")){
			blockString = "All Blocks";
		}
		return blockString;
	}
	/**
	 * @param tbdFlag the tbdFlag to set
	 */
	public void setTbdFlag(String tbdFlag) {
		this.tbdFlag = tbdFlag;
	}
	/**
	 * @return the tbdFlag
	 */
	public String getTbdFlag() {
		return tbdFlag;
	}
	/**
	 * @param isAPIHistoryFlag the isAPIHistoryFlag to set
	 */
	public void setAPIHistoryFlag(boolean isAPIHistoryFlag) {
		this.isAPIHistoryFlag = isAPIHistoryFlag;
	}
	/**
	 * @return the isAPIHistoryFlag
	 */
	public boolean isAPIHistoryFlag() {
		return isAPIHistoryFlag;
	}
	/**
	 * @return the legendDisplayString
	 */
	public String getLegendDisplayString() {
		legendDisplayString="";
		if(event!=null&&getStartDate()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
			String startDateString = sdf.format(getStartDate());
			SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
			String startTimeString = sdf2.format(getStartDate());
			
			String endTimeString=getTbdEndDateString();
			if("TBD".equalsIgnoreCase(endTimeString)){
				
			}else{
				if(getEndDate()!=null){
					SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm a");
					endTimeString = sdf3.format(getEndDate());
				}
			}
			legendDisplayString=startDateString+": "+startTimeString+" - "+endTimeString;
		}
		
		return legendDisplayString;
	}
	/**
	 * @param legendDisplayString the legendDisplayString to set
	 */
	public void setLegendDisplayString(String legendDisplayString) {
		this.legendDisplayString = legendDisplayString;
	}

	
	
	/**
	 * @return the eventDisplayID
	 */
	public String getEventDisplayID() {
		eventDisplayID = legendDisplayString + Math.random()*10000;
		return eventDisplayID;
	}
	/**
	 * @param eventDisplayID the eventDisplayID to set
	 */
	public void setEventDisplayID(String eventDisplayID) {
		this.eventDisplayID = eventDisplayID;
	}
	
	

	
	private String eventIDListString="";
	
	/**
	 * @return the eventIDListString
	 */
	public String getEventIDListString() {
		eventIDListString="";
		if(eventIDListString!=null&&event.getEventIDList()!=null){
			for(String eventID:event.getEventIDList()){
				eventIDListString+=eventID+",";
			}
			if(eventIDListString.endsWith(",")&&(eventIDListString.length()>1)){
				eventIDListString=eventIDListString.substring(0, eventIDListString.length()-1);
			}
		}else{
			eventIDListString="";
		}
		return eventIDListString;
	}
	/**
	 * @param eventIDListString the eventIDListString to set
	 */
	public void setEventIDListString(String eventIDListString) {
		this.eventIDListString = eventIDListString;
	}
	/**
	 * @return the blockStringWithoutCombine
	 */
	public String getBlockStringWithoutCombine() {
		
		blockStringWithoutCombine=null;
		List<String> list = this.getEvent().getBlocks();
//		boolean isAll = DRWUtil.isAllBlocks(list);
//		if(isAll){
//			return "All Blocks";
//		}
		if(list.size()>0){//API
				String str = list.get(0);
				if(DRWUtil.toNumber(str)!=null){
					//number
					List<Number> listN = new ArrayList<Number>();
					if (list!=null&&list.size()>0) {
						for (String b:list){
							
							Number num = DRWUtil.toNumber(b);
							if(num!=null){
								listN.add(num);
							}
						}
					}
					DRWUtil.sortNumberList(listN);
					for (Number b:listN){
						if(b!=null){
							if (blockStringWithoutCombine==null)
								blockStringWithoutCombine=String.valueOf(b);
							else
								blockStringWithoutCombine+=", "+b;
						}
					}
				}else{
					//String
					blockStringWithoutCombine = getBlockS();
				}
		}else{//BIP
			blockStringWithoutCombine =  this.getEvent().getBlock();
		}	
		//DRMS-7470
		if(isAPIHistoryFlag){
			Date compareDate = new Date("2012/01/01 00:00:00");
			if(this.getEvent().getStartTime().before(compareDate)){
				blockStringWithoutCombine="All Blocks";
			}
		}
		if(blockStringWithoutCombine!=null&&blockStringWithoutCombine.endsWith(", ")){
			blockStringWithoutCombine=blockStringWithoutCombine.substring(0, blockStringWithoutCombine.length()-2);
		}else if(blockStringWithoutCombine==null||blockStringWithoutCombine.equalsIgnoreCase("")){
			blockStringWithoutCombine = "All Blocks";
		}
		return blockStringWithoutCombine;
		
	}
	/**
	 * @param blockStringWithoutCombine the blockStringWithoutCombine to set
	 */
	public void setBlockStringWithoutCombine(String blockStringWithoutCombine) {
		this.blockStringWithoutCombine = blockStringWithoutCombine;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	
}
