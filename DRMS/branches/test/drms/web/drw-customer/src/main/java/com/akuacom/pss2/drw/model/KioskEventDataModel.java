package com.akuacom.pss2.drw.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.akuacom.pss2.drw.constant.DRWConstants;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.LocationValue;

public class KioskEventDataModel implements Serializable {
	
	private static final long serialVersionUID = 2070208949837070210L;
	
	private String iconLocation;
	private String program;
	private String product;
	private String dispatchType;
	private String dispatchLocation;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	
	private EventValue event;
	
	private boolean drwEventFlag;
	
	public KioskEventDataModel(EventValue event){
		this.event=event;
		if(event!=null){
			this.product=event.getProduct();
			this.program=event.getLongProgramName();
			if (event.getDispatchLocations()!=null && event.getDispatchLocations().contains("ALL")) {
				this.dispatchType="SLAP";
				this.dispatchLocation="ALL";
			} else {
				if (event.getDispatchTypes()!=null && event.getDispatchTypes().size()>0) {
					this.dispatchType=convertToString(this.event.getDispatchTypes());
				}
				if (event.getDispatchLocations()!=null && event.getDispatchLocations().size()>0) {
					this.dispatchLocation=convertToString(this.event.getDispatchLocations());
				}
			}
		}
		
	}
	
    private String convertToString(List<String> input) {
    	StringBuilder builder=new StringBuilder();
    	for (String s:input) {
    		builder.append(s);
    		builder.append(",");
    	}
    	return builder.substring(0, builder.length()-1);
    }

	/**
	 * @return the iconLocation
	 */
	public String getIconLocation() {
		if(product!=null){
			if(product.indexOf("SDP")>=0||product.indexOf("Residential")>=0){
				iconLocation = DRWConstants.EVENT_ICON_FOLDER+DRWConstants.EVENT_ICON_SDPR;
			}else if(product.indexOf("APS")>=0||product.indexOf("Commercial")>=0){
				iconLocation = DRWConstants.EVENT_ICON_FOLDER+DRWConstants.EVENT_ICON_SDPC;
			}else if(product.indexOf("API")>=0||product.indexOf("AP-I")>=0){
				iconLocation = DRWConstants.EVENT_ICON_FOLDER+DRWConstants.EVENT_ICON_API;
			}else if(product.indexOf("BIP")>=0){
				iconLocation = DRWConstants.EVENT_ICON_FOLDER+DRWConstants.EVENT_ICON_BIP;
			}else if(product.indexOf("SAI")>=0||product.indexOf("CPP")>=0){
				iconLocation = DRWConstants.EVENT_ICON_FOLDER+DRWConstants.EVENT_ICON_SAI;
			}else if(product.indexOf("CBP")>=0){
				iconLocation = DRWConstants.EVENT_ICON_FOLDER+DRWConstants.EVENT_ICON_CBP;
			}else if(product.indexOf("DBP")>=0){
				iconLocation = DRWConstants.EVENT_ICON_FOLDER+DRWConstants.EVENT_ICON_DBP;
			} else if(product.indexOf("DRC")>=0){
				iconLocation = DRWConstants.EVENT_ICON_FOLDER+DRWConstants.EVENT_ICON_DRC;
			} else if(product.indexOf("SPD")>=0){
				iconLocation = DRWConstants.EVENT_ICON_FOLDER+DRWConstants.EVENT_ICON_SPD;
			}
		}
		return iconLocation;
	}
	/**
	 * @param iconLocation the iconLocation to set
	 */
	public void setIconLocation(String iconLocation) {
		this.iconLocation = iconLocation;
	}
	/**
	 * @return the program
	 */
	public String getProgram() {
		if(program!=null){
			if(program.equalsIgnoreCase("Summer Discount Plan(SDP) (Residential Air Conditioning Plan)")){
				program = "Summer Discount Plan(SDP) - Residential";
				drwEventFlag = true;
			}else if(program.indexOf("SAI")>=0){
				drwEventFlag = false;
				program = "Summer Advantage Incentive (SAI)";
			}else if(program.indexOf("CBP")>=0){
				drwEventFlag=true;
				program = "Capacity Bidding Program";
			}else if(program.indexOf("DBP")>=0){
				drwEventFlag=false;
				program = "Demand Bidding Program (DBP)";
			}else if(program.indexOf("DRC")>=0){
				drwEventFlag=false;
				program = "Demand Response Contracts";
			}else if(program.indexOf("SPD")>=0){
				drwEventFlag=false;
				program = "Save Power Day";
			}else if(program.indexOf("Agricultural and Pumping Interruptible Program")>=0){
				drwEventFlag=true;
				program = DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_API;
			}else if(program.indexOf("BIP")>=0){
				drwEventFlag=true;
				program = DRWConstants.HISTORY_PROGRAM_DISPLAY_NAME_BIP;
			}
		}
		
		return program;
	}
	/**
	 * @param program the program to set
	 */
	public void setProgram(String program) {
		this.program = program;
	}
	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the dispatchLocation
	 */
	public String getDispatchLocation() {
		this.getProgram();
		if(drwEventFlag){
			if(dispatchLocation==null||dispatchLocation.trim().equals("")){
				dispatchLocation="All";
			}
		}
		
		return dispatchLocation;
	}

	/**
	 * @param dispatchLocation the dispatchLocation to set
	 */
	public void setDispatchLocation(String dispatchLocation) {
		this.dispatchLocation = dispatchLocation;
	}

	/**
	 * @return the dispatchType
	 */
	public String getDispatchType() {
		if("Block".equalsIgnoreCase(dispatchType)){
			dispatchType="SLAP";
		}
		return dispatchType;
	}
	/**
	 * @param dispatchType the dispatchType to set
	 */
	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		
		if(getEvent()!=null&&getEvent().getStartTime()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
			startDate = sdf.format(getEvent().getStartTime());
		}
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		if(getEvent()!=null&&getEvent().getEndTime()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
			endDate = sdf.format(getEvent().getEndTime());
		}
		if(endDate==null||endDate.equalsIgnoreCase("")){
			endDate="TBD";
		}
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		if(getEvent()!=null&&getEvent().getStartTime()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mma");
			startTime = sdf.format(getEvent().getStartTime());
		}
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		if(getEvent()!=null&&getEvent().getEndTime()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mma");
			endTime = sdf.format(getEvent().getEndTime());
		}
		if(endTime==null||endTime.equalsIgnoreCase("")){
			endTime="TBD";
		}
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the event
	 */
	public EventValue getEvent() {
		return event;
	}
	/**
	 * @param event the event to set
	 */
	public void setEvent(EventValue event) {
		this.event = event;
	}
	
	public static KioskEventDataModel getMockInstance(String mockType){
		EventValue event = new EventValue();
		event.setStartTime(new Date());
		event.setEndTime(new Date());
		if(mockType.equalsIgnoreCase("SDPR")){
			event.setProduct("Residential");
			event.setLongProgramName("Summer Discount Plan(SDP) (Residential Air Conditioning Plan)");
			event.setDispatchLocation("DispatchLocation");
			event.setDispatchType("ABANK");
		}else if(mockType.equalsIgnoreCase("SDPC")){
			event.setProduct("Commercial");
			event.setLongProgramName("Summer Discount Plan(SDP) - Commercial");
			event.setDispatchLocation("DispatchLocation");
			event.setDispatchType("SLAP");
		}else if(mockType.equalsIgnoreCase("API")){
			event.setProduct("AP-I");
			event.setLongProgramName("Agricultural & Pumping Interruptible Program");
			event.setDispatchLocation("DispatchLocation");
			event.setDispatchType("BLOCK");
		}else if(mockType.equalsIgnoreCase("BIP")){
			event.setProduct("BIP");
			event.setLongProgramName("Time-of-Use Base Interruptible Program");
			event.setDispatchLocation("DispatchLocation");
			event.setDispatchType("SLAP");
		}else if(mockType.equalsIgnoreCase("SAI")){
			event.setProduct("SAI");
			event.setLongProgramName("Summer Advantage Incentive (SAI)");
		}else if(mockType.equalsIgnoreCase("CBP")){
			event.setProduct("CBP");
			event.setLongProgramName("Capacity Bidding Program");
		}else if(mockType.equalsIgnoreCase("DBP")){
			event.setProduct("DBP");
			event.setLongProgramName("Demand Bidding Program (DBP)");
		}else if(mockType.equalsIgnoreCase("DRC")){
			event.setProduct("DRC");
			event.setLongProgramName("Demand Response Contracts");
		}
		
		KioskEventDataModel model = new KioskEventDataModel(event);
		
		return model;
	}
	@Override
	public boolean equals(Object o){
		try{
			if(o instanceof KioskEventDataModel){
				KioskEventDataModel obj = (KioskEventDataModel) o;
				boolean flag = true;
				if(this.getProgram()!=null){
					flag = this.getProgram().equalsIgnoreCase(obj.getProgram());
				}else{
					flag = (obj.getProgram() ==null);
				}
				if(flag){
					if(this.getProduct()!=null){
						flag = this.getProduct().equalsIgnoreCase(obj.getProduct());
					}else{
						flag = (obj.getProduct() ==null);
					}
					if(flag){
						if(this.getDispatchType()!=null){
							flag = this.getDispatchType().equalsIgnoreCase(obj.getDispatchType());
						}else{
							flag = (obj.getDispatchType() ==null);
						}
						if(flag){
							if(this.getDispatchLocation()!=null){
								flag = this.getDispatchLocation().equalsIgnoreCase(obj.getDispatchLocation());
							}else{
								flag = (obj.getDispatchLocation() ==null);
							}
							if(flag){
								if(this.getStartDate()!=null){
									flag = this.getStartDate().equalsIgnoreCase(obj.getStartDate());
								}else{
									flag = (obj.getStartDate() ==null);
								}
								if(flag){
									if(this.getEndDate()!=null){
										flag = this.getEndDate().equalsIgnoreCase(obj.getEndDate());
									}else{
										flag = (obj.getEndDate() ==null);
									}
									if(flag){
										if(this.getStartTime()!=null){
											flag = this.getStartTime().equalsIgnoreCase(obj.getStartTime());
										}else{
											flag = (obj.getStartTime() ==null);
										}
										if(flag){
											if(this.getEndTime()!=null){
												flag = this.getEndTime().equalsIgnoreCase(obj.getEndTime());
											}else{
												flag = (obj.getEndTime() ==null);
											}
											return flag;
										}else{
											return false;
										}
									}else{
										return false;
									}
								}else{
									return false;
								}
							}else{
								return false;
							}
						}else{
							return false;
						}
					}else{
						return false;
					}
				}else{
					return false;
				}
			  }else{
				return false;
			  }	
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * @return the drwEventFlag
	 */
	public boolean isDrwEventFlag() {
		return drwEventFlag;
	}

	/**
	 * @param drwEventFlag the drwEventFlag to set
	 */
	public void setDrwEventFlag(boolean drwEventFlag) {
		this.drwEventFlag = drwEventFlag;
	}
	
}
