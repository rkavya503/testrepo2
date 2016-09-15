package com.akuacom.pss2.richsite.event.dbp;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openadr.dras.eventinfo.EventInfoInstance;
import org.openadr.dras.eventinfo.EventInfoValue;
import org.openadr.dras.utilitydrevent.UtilityDREvent;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.akuacom.pss2.core.SignalLevelMapper;
import com.akuacom.pss2.event.participant.EventParticipant;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.dbp.DBPEvent;
import com.akuacom.pss2.program.dbp.EventBidBlock;
import com.akuacom.pss2.richsite.event.EventDataModel;
import com.akuacom.pss2.richsite.event.JSFScopeManager;
import com.akuacom.pss2.richsite.util.AkuacomJSFUtil;

public class DBPNoBidEventDataModel extends EventDataModel implements Serializable{
	private static final long serialVersionUID = -3332438914978701582L;

	/** The log */
	private static final Logger log = Logger.getLogger(DBPNoBidEventDataModel.class.getName());
	
	/** The files which combine with JSF presentation layer page */
	private List<File> files = new ArrayList<File>();
	
	/** The upload file available size */
	private int uploadsAvailable = 1;
	
	/** is allow the auto upload */
	private boolean autoUpload = true;
	
	/** is allow the auto clear */
	private boolean autoClear = true;
	
	/** is use Flash */
	private boolean useFlash = false;
	
	/** The file name of the first file in the files list */
	private String uploadFileName="";
	/** The renew. */
	private String renew;
	
	private List<CurrentBidVO> currentBids = new ArrayList<CurrentBidVO>();
	private List<String> blockTimes = new ArrayList<String>();
	private List<Double> reductionTotals= new ArrayList<Double>();
	
	private DBPEvent event;
	private UtilityDREvent utilityDREvent;
	
	/**
	 * Constructor
	 */
	public DBPNoBidEventDataModel(){
		super();
	}
	
	public String SUCCESS="success";
	
	@Override
	public String confirm(){
		File uploadFile=null;
		if (this.files!=null && this.files.size()>0) {
			uploadFile=this.files.get(0);
		}
		
		if (uploadFile==null) {
			AkuacomJSFUtil.addErrorMessage("No upload file is available");
			return SUCCESS;
		}
		return super.confirm();
	}

	//----------------------------------------------------------------Business Logic Method----------------------------------------------------------------

	public void buildModel(DBPEvent event, UtilityDREvent utilityDREvent)throws Exception{
		
		this.setEvent(event);
		
		this.setUtilityDREvent(utilityDREvent);
		
		getManager().transferEventToEventDataModel(event,this);
		
		buildBlockTimes(event);
		
		
		if (event.getIssuedTime().getTime() < System.currentTimeMillis()){
			currentBids = new ArrayList<CurrentBidVO>();
			
			List<EventParticipant> eventParticipants = event.getParticipants();
//			double[] reductionTotals = new double[event.getBidBlocks().size()];
			this.getReductionTotals().clear();
			for(int i=0;i<event.getBidBlocks().size();i++){
				this.getReductionTotals().add(0.0);
			}
			
            for (EventParticipant eventParticipant : eventParticipants) {
                final Participant vo1 = eventParticipant.getParticipant();
                final CurrentBidVO vo = new CurrentBidVO();
                vo.setAccountNumber(eventParticipant.getParticipant().getAccountNumber());
                final String participantName = eventParticipant.getParticipant().getParticipantName();
                vo.setParticipantName(participantName);
                final List<Double> reductions = new ArrayList<Double>();                
                List<EventInfoInstance> eventInfoInstances = utilityDREvent.getEventInformation().getEventInfoInstance();
                for (int j = 0; j < event.getBidBlocks().size(); j++) {
                	double kw = 0.0;
                	
                	if (eventInfoInstances != null && eventInfoInstances.size() > 0) {
                		
                		EventInfoInstance info =  null;
                		for(EventInfoInstance info1 : eventInfoInstances)
                		{
                			if(info1.getParticipants().getAccountID().get(0).equalsIgnoreCase(vo1.getAccountNumber()))
                			{
                				info = info1;
                				break;
                			}
                		}
                		List<EventInfoValue> values = info.getValues().getValue();
                		kw = values.get(j).getValue();            		
                		reductions.add(kw);
                		double reductionTotal=this.getReductionTotals().get(j);
                        reductionTotal += kw;
                        this.getReductionTotals().remove(j);
                        this.getReductionTotals().add(j, reductionTotal);
                    }
                }                
                vo.setReductions(reductions);
                currentBids.add(vo);              
            }
            
		}else{
			for (int j = 0; j < event.getBidBlocks().size(); j++){
				double reductionTotal=0.0;
				this.getReductionTotals().add(reductionTotal);
			}
		}
	}
	
	private void buildBlockTimes(DBPEvent event){
		blockTimes = new ArrayList<String>();
		List<EventBidBlock> timeBlocks = event.getBidBlocks();
		for (EventBidBlock timeBlock : timeBlocks){
			String blockTime = SignalLevelMapper.getTimeBlock(timeBlock);
			blockTimes.add(blockTime);
		}
	}
	
	
//	private String uploadFileWarning="file name must end in 'csv'";
	private String uploadFileWarning="";
	private boolean uploadFileWarningFlag=false;
	/**
	 * Listener for file upload function
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void listener(UploadEvent event) throws Exception {
		try{
			uploadFileWarningFlag=false;
			uploadFileWarning="";
			UploadItem item = event.getUploadItem();
			if(item!=null){
				String fileName = item.getFileName();
				if(!fileName.endsWith(".csv")){
					uploadFileWarningFlag = true;
					uploadFileWarning="file extension must be csv";
					clearUploadFiles();
					return ;
				}
			}
			File file = item.getFile();
			if(file!=null){
				files.clear();
				files.add(file);
				setUploadsAvailable(0);
				setUploadFileName(item.getFileName());
			}	
			this.setRenewFlag(false);
		}catch(Exception e){
			log.error(e.getMessage());
			throw e;
		}
			
		
	}

	/**
	 * JSF framework for get Presentation Layer request for clear upload files which store at the server 
	 */	
	public void clearUploadFiles(){
		files.clear();
		setUploadFileName("");
        setUploadsAvailable(1);
        this.setRenewFlag(false);
	}
	
	@Override
	public String getRenew() {
		//The renew flag is false means the JSF presentation layer keep this object as Session scope 
		if(!this.isRenewFlag()){
			this.uploadFileWarningFlag=false;
			uploadFileWarning="";
			this.setRenewFlag(true);
			JSFScopeManager.getInstance().addOrUpdateSession("dbpNoBidEventDataModel", this);
			return renew;
		}
		//The renew flag is true means the JSF presentation layer keep this object as Request scope,
		//should remove the object in the JSF session and keep the new object into it.
		else{
			DBPNoBidEventDataModel model = new DBPNoBidEventDataModel();
			model.setProgramName(this.getProgramName());
			model.setUploadFileWarning("");
			model.setUploadFileWarningFlag(false);
			JSFScopeManager.getInstance().addOrUpdateSession("dbpNoBidEventDataModel", model);
			return renew;
		}
	}
	//------------------------------------------------------------------Getter and Setter--------------------------------------------------------------
	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public int getUploadsAvailable() {
		return uploadsAvailable;
	}

	public void setUploadsAvailable(int uploadsAvailable) {
		this.uploadsAvailable = uploadsAvailable;
	}

	public boolean isAutoUpload() {
		return autoUpload;
	}

	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	public boolean isAutoClear() {
		return autoClear;
	}

	public void setAutoClear(boolean autoClear) {
		this.autoClear = autoClear;
	}

	public boolean isUseFlash() {
		return useFlash;
	}

	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public List<CurrentBidVO> getCurrentBids() {
		return currentBids;
	}

	public void setCurrentBids(List<CurrentBidVO> currentBids) {
		this.currentBids = currentBids;
	}


	public List<Double> getReductionTotals() {
		return reductionTotals;
	}

	public void setReductionTotals(List<Double> reductionTotals) {
		this.reductionTotals = reductionTotals;
	}

	public List<String> getBlockTimes() {
		return blockTimes;
	}

	public void setBlockTimes(List<String> blockTimes) {
		this.blockTimes = blockTimes;
	}

	public DBPEvent getEvent() {
		return event;
	}

	public void setEvent(DBPEvent event) {
		this.event = event;
	}

	public UtilityDREvent getUtilityDREvent() {
		return utilityDREvent;
	}

	public void setUtilityDREvent(UtilityDREvent utilityDREvent) {
		this.utilityDREvent = utilityDREvent;
	}

	public String getUploadFileWarning() {
		return uploadFileWarning;
	}

	public void setUploadFileWarning(String uploadFileWarning) {
		this.uploadFileWarning = uploadFileWarning;
	}

	public boolean isUploadFileWarningFlag() {
		return uploadFileWarningFlag;
	}

	public void setUploadFileWarningFlag(boolean uploadFileWarningFlag) {
		this.uploadFileWarningFlag = uploadFileWarningFlag;
	}
	
	
}
