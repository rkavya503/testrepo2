package com.akuacom.pss2.richsite.program.configure.bid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import org.openadr.dras.akuautilityprogram.AkuaBidConfig;
import com.akuacom.pss2.richsite.program.configure.ProgramConfigureDataModel;
import com.akuacom.pss2.richsite.program.configure.factory.ProgramConfigureFactory;

public class BidConfigureDataModel implements Serializable{

	private static final long serialVersionUID = -7079379412848195953L;
	
	//-------------------------------Attributes-------------------------------------------------
	
	/** ProgramConfigureDataModel reference*/
	private ProgramConfigureDataModel programConfigureDataModel;
	
	/** BidConfigureDataModelManager reference*/
	private BidConfigureDataModelManager manager =ProgramConfigureFactory.getInstance().getBidConfigureDataModelManager();
	
	/** AkuaBidConfig entity instance*/
	private AkuaBidConfig akuaBidConfig;
	
	/** respond by time string value for UI*/
	private String respondByTimeString="";
	
	/** bid blocks JSF component for List<SelectItem>*/
	private List<SelectItem> bidBlocksSelectItems = new ArrayList<SelectItem>();	//Data for presentation layer
	
	/** start time string value for UI*/
	private String startTimeString="";
	
	/** end time string value for UI*/
	private String endTimeString="";
	
	/**
	 * Constructor
	 * @param programConfigureDataModel ProgramConfigureDataModel instance
	 */
	public BidConfigureDataModel(ProgramConfigureDataModel programConfigureDataModel){
		super();
		this.programConfigureDataModel=programConfigureDataModel;
	}
	
	//-------------------------------Business Logic Method--------------------------------------
	
	/**
	 * Function for construct the relative backingBean model
	 */
	public void buildModel(){
		if(this.programConfigureDataModel!=null){
			manager.getBidConfigs(this);
		}
	}
	
	/**
	 * Function for add UI bid block into bid blocks list 
	 * @return
	 */
	public String addDisplayBidBlocks(){
		if(this.programConfigureDataModel!=null){
			this.programConfigureDataModel.setRenewFlag(false);
			String bidBlockString = buildProgramBidBlockString();
			bidBlocksSelectItems.add(new SelectItem(bidBlockString,bidBlockString));
		}
//		return ProgramConfigureDataModel.EditPageUrl;
		return "";
	}
	
	/**
	 * Function for construct UI bid block string base on start time and end time
	 * @return Program Bid Block String
	 */
	private String buildProgramBidBlockString(){
		StringBuffer sb = new StringBuffer(getStartTimeString());
		sb.append("~");
		sb.append(getEndTimeString());
		return sb.toString();
	}
	
	/**
	 * Function for get start time string value from JSF selectItem object
	 * @param item JSF selectItem object
	 * @return start time string 
	 */
	public String getStartTimeString(SelectItem item){
		if(item.getLabel().contains("~")){
			String[] time =item.getLabel().split("~");
			return time[0];
		}else{
			return "0:0";
		}
	}
	
	/**
	 * Function for get end time string value from JSF selectItem object
	 * @param item JSF selectItem object
	 * @return end time string
	 */
	public String getEndTimeString(SelectItem item){
		if(item.getLabel().contains("~")){
			String[] time =item.getLabel().split("~");
			if(time.length>1){
				return time[1];
			}
		}
		return "0:0";
	}
	
	/**
	 * Function for clean up 
	 */
	public String clearDisplayBidBlocks(){
		if(this.programConfigureDataModel!=null){
			this.programConfigureDataModel.setRenewFlag(false);
			setBidBlocksSelectItems(new ArrayList<SelectItem>());
		}
//		return ProgramConfigureDataModel.EditPageUrl;
		return "";
	}

	//-------------------------------Getter and Setter------------------------------------------
	
	public ProgramConfigureDataModel getProgramConfigureDataModel() {
		return programConfigureDataModel;
	}
	public void setProgramConfigureDataModel(
			ProgramConfigureDataModel programConfigureDataModel) {
		this.programConfigureDataModel = programConfigureDataModel;
	}
	public AkuaBidConfig getAkuaBidConfig() {
		if(akuaBidConfig==null){
			akuaBidConfig = new AkuaBidConfig();
		}
		return akuaBidConfig;
	}
	public void setAkuaBidConfig(AkuaBidConfig akuaBidConfig) {
		this.akuaBidConfig = akuaBidConfig;
	}
	public String getRespondByTimeString() {
		return respondByTimeString;
	}
	public void setRespondByTimeString(String respondByTimeString) {
		this.respondByTimeString = respondByTimeString;
	}
	public List<SelectItem> getBidBlocksSelectItems() {
		return bidBlocksSelectItems;
	}
	public void setBidBlocksSelectItems(List<SelectItem> bidBlocksSelectItems) {
		this.bidBlocksSelectItems = bidBlocksSelectItems;
	}
	public String getStartTimeString() {
		return startTimeString;
	}
	public void setStartTimeString(String startTimeString) {
		this.startTimeString = startTimeString;
	}
	public String getEndTimeString() {
		return endTimeString;
	}
	public void setEndTimeString(String endTimeString) {
		this.endTimeString = endTimeString;
	}
	
}
