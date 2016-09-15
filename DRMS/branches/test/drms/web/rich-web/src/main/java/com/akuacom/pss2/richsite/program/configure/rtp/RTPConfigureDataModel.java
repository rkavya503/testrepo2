package com.akuacom.pss2.richsite.program.configure.rtp;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.openadr.dras.akuartpconfig.AkuaRTPConfig;
import org.openadr.dras.akuartpconfig.ListOfRTPConfigs;
import org.richfaces.model.UploadItem;

import com.akuacom.pss2.richsite.program.configure.ProgramConfigureDataModel;
import com.akuacom.pss2.richsite.program.configure.factory.ProgramConfigureFactory;

public class RTPConfigureDataModel implements Serializable {
	//-------------------------------Attributes-------------------------------------------------

	/** RTP warning message*/
	public static final String WARNING_NO_RTP="There is no RTP table loaded for this program. You can click 'Add' to import a RTP table. ";

	/** ProgramConfigureDataModel instance*/
	private ProgramConfigureDataModel programConfigureDataModel;

	/** RTPConfigureDataModelManager instance*/
	private RTPConfigureDataModelManager manager = ProgramConfigureFactory.getInstance().getRTPConfigureDataModelManager();

	/** RTPLine list*/
	private List<RTPLine> lines = new ArrayList<RTPLine>();

	/** minTs*/
	private List<Double> minTs = new ArrayList<Double>();

	/** names*/
	private List<String> names = new ArrayList<String>();

	/** seasons*/
	private List<String> seasones = new ArrayList<String>();

	/** ListOfRTPConfigs list*/
	private ListOfRTPConfigs list;
	

	/** warningMessage*/
	private String warningMessage=WARNING_NO_RTP;

	//Fix this base on the FLEX design also cause the JSF dynamic columns component is unstable.  

	/** Fix this base on the FLEX design also cause the JSF dynamic columns component is unstable-header0*/
	private String header0="value0";

	/** Fix this base on the FLEX design also cause the JSF dynamic columns component is unstable-header1*/
	private String header1="value1";

	/** Fix this base on the FLEX design also cause the JSF dynamic columns component is unstable-header2*/
	private String header2="value2";

	/** Fix this base on the FLEX design also cause the JSF dynamic columns component is unstable-header3*/
	private String header3="value3";

	/** Fix this base on the FLEX design also cause the JSF dynamic columns component is unstable-header4*/
	private String header4="value4";

	/** Fix this base on the FLEX design also cause the JSF dynamic columns component is unstable-header5*/
	private String header5="value5";

	/** Fix this base on the FLEX design also cause the JSF dynamic columns component is unstable-header6*/
	private String header6="value6";

	/** Fix this base on the FLEX design also cause the JSF dynamic columns component is unstable-header7*/
	private String header7="value7";

	/** Fix this base on the FLEX design also cause the JSF dynamic columns component is unstable-header8*/
	private String header8="value8";
	
	/** Boolean flag for dynamic display RTP exist warning message*/
	private boolean rtpNotExistWarnFlag = false;
	
	public RTPConfigureDataModel(ProgramConfigureDataModel programConfigureDataModel){
		this.programConfigureDataModel = programConfigureDataModel;
		buildModel();
	}
	
	//-------------------------------Business Logic Method--------------------------------------
	
	/**
	 * Function for construct the relative backingBean model
	 */
	public void buildModel(){
		if(this.programConfigureDataModel!=null){
			try {
				loadRTPConfigs();
				this.setRtpNotExistWarnFlag(this.lines.size() < 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String loadRTPConfigs() throws Exception{
		if(this.programConfigureDataModel!=null){
			programConfigureDataModel.setRenewFlag(false);	
			manager.getRTPConfigs(this);
		}
//		return ProgramConfigureDataModel.EditPageUrl;
		return "";
	}
	
	/**
	 * Listener for file upload function
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void listener(org.richfaces.event.UploadEvent event) {
		if(this.programConfigureDataModel!=null){
			try{
				programConfigureDataModel.setRenewFlag(false);	
				UploadItem item = event.getUploadItem();
				File file = item.getFile();
				String resultMessage = manager.saveRTPFileIntoDB(this,file);
				if(resultMessage.equalsIgnoreCase("SUCCESS")){
					loadRTPConfigs();
					setRtpNotExistWarnFlag(false);
					setWarningMessage("");
				}else{
					setRtpNotExistWarnFlag(true);
					setWarningMessage(resultMessage);
				}
			} catch(Exception e){
				setRtpNotExistWarnFlag(true);
				setWarningMessage(e.getMessage());
			}
		}	
	}
	
	public void buildRTPForm(){
		List<RTPLine> lines = new ArrayList<RTPLine>();
		RTPLine tline= new RTPLine();
		RTPLine rtpline= new RTPLine();
		boolean first = true;
		int count = 0;
		for(int i=0;i<list.getPriceTable().size();i++){
			AkuaRTPConfig config = list.getPriceTable().get(i);
			if(first){
				minTs.add(config.getStartTemperature());
				names.add(config.getName());
				seasones.add(config.getSeasonName());
			
				tline.getValues().add(config.getStartTemperature().toString());
			}
			if(count == 9){
				count = 0;
				rtpline = new RTPLine();
				first = false;
			}
			rtpline.getValues().add(config.getRate().toString());
			//		rtpline.canEdit = true;
			//		rtpline.canDelete = false;
			if(count == 8){
				if(first){
					lines.add(tline);
				}
				rtpline.setEndTime(config.getEndTime());//Transfer issue
				lines.add(rtpline);
			}
			count++;
		}
		this.lines = lines;
		buildHeader();
	}
	
	//base on the flex design
	private void buildHeader(){
		if(names.size()>8){
			header0 = names.get(0);
			header1 = names.get(1);
			header2 = names.get(2);
			header3 = names.get(3);
			header4 = names.get(4);
			header5 = names.get(5);
			header6 = names.get(6);
			header7 = names.get(7);
			header8 = names.get(8);
		}
	}
	//-------------------------------Getter and Setter------------------------------------------
	
	public List<RTPLine> getLines() {
		return lines;
	}

	public void setLines(List<RTPLine> lines) {
		this.lines = lines;
	}

	public List<Double> getMinTs() {
		return minTs;
	}

	public void setMinTs(List<Double> minTs) {
		this.minTs = minTs;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public List<String> getSeasones() {
		return seasones;
	}

	public void setSeasones(List<String> seasones) {
		this.seasones = seasones;
	}

	public String getHeader0() {
		return header0;
	}

	public void setHeader0(String header0) {
		this.header0 = header0;
	}

	public String getHeader1() {
		return header1;
	}

	public void setHeader1(String header1) {
		this.header1 = header1;
	}

	public String getHeader2() {
		return header2;
	}

	public void setHeader2(String header2) {
		this.header2 = header2;
	}

	public String getHeader3() {
		return header3;
	}

	public void setHeader3(String header3) {
		this.header3 = header3;
	}

	public String getHeader4() {
		return header4;
	}

	public void setHeader4(String header4) {
		this.header4 = header4;
	}

	public String getHeader5() {
		return header5;
	}

	public void setHeader5(String header5) {
		this.header5 = header5;
	}

	public String getHeader6() {
		return header6;
	}

	public void setHeader6(String header6) {
		this.header6 = header6;
	}

	public String getHeader7() {
		return header7;
	}

	public void setHeader7(String header7) {
		this.header7 = header7;
	}

	public String getHeader8() {
		return header8;
	}

	public void setHeader8(String header8) {
		this.header8 = header8;
	}
	public ProgramConfigureDataModel getProgramConfigureDataModel() {
		return programConfigureDataModel;
	}
	public void setProgramConfigureDataModel(
			ProgramConfigureDataModel programConfigureDataModel) {
		this.programConfigureDataModel = programConfigureDataModel;
	}	
	public ListOfRTPConfigs getList() {
		return list;
	}

	public void setList(ListOfRTPConfigs list) {
		this.list = list;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}
	
	public boolean isRtpNotExistWarnFlag() {
		return rtpNotExistWarnFlag;
	}

	public void setRtpNotExistWarnFlag(boolean rtpNotExistWarnFlag) {
		this.rtpNotExistWarnFlag = rtpNotExistWarnFlag;
	}
}
