/**   
* @Title: ParticipantShedValueEditBean.java 
* @Package com.akuacom.pss2.richsite.participant 
* @Description: TODO 
* @author liu   
* @date Oct 29, 2012 11:45:49 AM   
*/ 
package com.akuacom.pss2.richsite.participant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.participant.ParticipantManager;
import com.akuacom.pss2.participant.ParticipantShedEntry;
import com.akuacom.pss2.richsite.FDUtils;


public class ParticipantShedValueEditBean implements Serializable {
	public static final long serialVersionUID = 4467328472397790246L;

	/** The log */
	private static final Logger log = Logger.getLogger(ParticipantShedValueEditBean.class.getName());
	public static final String SHED_TYPE_SIMPLE="SIMPLE";
	public static final String SHED_TYPE_ADVANCED="ADVANCED";
	
	private String participantShedType="SIMPLE";
	private String participantName="";
	private String participantAccountNumber="";
	private String timeBlockString="";
	private String copyValue;
    private List<String> shedType = new ArrayList<String>();
    private Participant participant;
    /** The files which combine with JSF presentation layer page */
	private List<File> files = new ArrayList<File>();
	/** is allow the auto upload */
	private boolean autoUpload = true;	
	/** The file name of the first file in the files list */
	private String uploadFileName="";
	/** The renewFlag. */
	private boolean renewFlag = true;
	/** The upload file available size */
	private int uploadsAvailable = 1;
	private String uploadFileWarning="";
	private boolean uploadFileWarningFlag=false;
    /** The entries. */
	private List<ParticipantShedEntryBean> entries = new ArrayList<ParticipantShedEntryBean>();
    
	
	
	public ParticipantShedValueEditBean(){
		initialize();
	}
	private void initialize(){
		participantName = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("participantName");
		shedType.clear();
		shedType.add(SHED_TYPE_SIMPLE);
		shedType.add(SHED_TYPE_ADVANCED);
		loadParticipant();
		loadShedEntries();
	}
	private void reload(){
		loadParticipant();
		loadShedEntries();
	}
	private List<ParticipantShedEntryBean> buildSimpleEntries(String value){
		List<ParticipantShedEntryBean> simpleEntries = new ArrayList<ParticipantShedEntryBean>();
        ParticipantShedEntryBean bean = new ParticipantShedEntryBean();
        bean.setHourIndex(0);
        bean.setType(SHED_TYPE_SIMPLE);
        bean.setValue(value);
        simpleEntries.add(bean);
        return simpleEntries;
	}
	private List<ParticipantShedEntryBean> buildAdvancedEntries(){
		List<ParticipantShedEntryBean> advancedEntries = new ArrayList<ParticipantShedEntryBean>();
		for (int i = 0; i < 24; i++) {
        	ParticipantShedEntryBean bean = new ParticipantShedEntryBean();
        	bean.setHourIndex(i);
            bean.setType(SHED_TYPE_ADVANCED);
            bean.setValue("");
            advancedEntries.add(bean);
        }
		return advancedEntries;
	}
	private void loadShedEntries(){
		//load shed entries
		if(participant!=null){
			if(participant.getShedType()!=null&&participant.getShedType().equalsIgnoreCase(SHED_TYPE_ADVANCED)){
				//advanced type
				if(participant.getShedEntries()!=null){
					List<ParticipantShedEntry> shedEntries = new ArrayList<ParticipantShedEntry>(participant.getShedEntries());
					if(shedEntries.size()>0){
						List<ParticipantShedEntryBean> advancedEntries = new ArrayList<ParticipantShedEntryBean>();
						for(ParticipantShedEntry entry:shedEntries){
							ParticipantShedEntryBean bean = ParticipantShedEntryBean.transfer(entry);
							advancedEntries.add(bean);
						}
						entries=advancedEntries;
					}else{
						entries = buildAdvancedEntries();
					}
				}else{
					entries = buildAdvancedEntries();
				}
			}else{
				//simple type
				double shed = participant.getShedPerHourKW();
				entries = buildSimpleEntries(String.valueOf(shed));
			}
		}else{
			entries = buildSimpleEntries("");
		}
	}
	private void loadParticipant(){
		ParticipantManager pm = (ParticipantManager) EJBFactory.getBean(ParticipantManager.class);
		if(participantName!=null&&(!participantName.equalsIgnoreCase(""))){
			participant = pm.getParticipantAndShedsOnly(participantName);
			this.participantAccountNumber = participant.getAccountNumber();
			participantShedType = participant.getShedType();
		}		
	}
	/**
	 * Gets the available types.
	 * 
	 * @return the available types
	 */
	public List<SelectItem> getAvailableTypes(){
		List<SelectItem> types = new ArrayList<SelectItem>();
		for(String type: shedType){
			types.add(new SelectItem(type));
		}
		return types;
	}
	
	/**
	 * Type listener.
	 * 
	 * @param e the e
	 */
	public void typeListener(ActionEvent e){
		
		if(participant!=null){
			String type = participant.getShedType();
			if(type!=null&&type.equalsIgnoreCase(participantShedType)){
				//retrieve the partiticpant entry
				if(participantShedType.equalsIgnoreCase(SHED_TYPE_ADVANCED)){
					List<ParticipantShedEntry> shedEntries = new ArrayList<ParticipantShedEntry>(participant.getShedEntries());
					if(shedEntries.size()>0){
						List<ParticipantShedEntryBean> advancedEntries = new ArrayList<ParticipantShedEntryBean>();
						for(ParticipantShedEntry entry:shedEntries){
							ParticipantShedEntryBean bean = ParticipantShedEntryBean.transfer(entry);
							advancedEntries.add(bean);
						}
						entries=advancedEntries;
					}
				}else{
					//simple type
					double shed = participant.getShedPerHourKW();
					entries = buildSimpleEntries(String.valueOf(shed));
				}
			}else{
				//initial the entry
				if(participantShedType.equalsIgnoreCase(SHED_TYPE_ADVANCED)){
					entries = buildAdvancedEntries();
				}else{
					entries = buildSimpleEntries("");
				}
			}
		}
	}
	/**
	 * Listener for file upload function
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void uploadListener(UploadEvent event) throws Exception {
		try{
			uploadFileWarningFlag=false;
			uploadFileWarning="";
			UploadItem item = event.getUploadItem();
			if(item!=null){
				String fileName = item.getFileName();
				if(!fileName.endsWith(".csv")){
					uploadFileWarningFlag = true;
					uploadFileWarning="file extension must be csv";
					clearUploadFilesAction();
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
	public void clearUploadFilesAction()throws IOException{
		files.clear();
		setUploadFileName("");
        setUploadsAvailable(1);
        this.setRenewFlag(false);
        
        //clear the messages
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Iterator<FacesMessage> iter= facesContext.getMessages();
        if(iter.hasNext() == true)
        {
            iter.remove();            
            facesContext.renderResponse();
        }
        uploadFileWarning="";
        
	}
	public void uploadAction()throws IOException{
		BufferedReader br = null;
    	try{
    		if(this.files.size()>0){
    			String filePath = this.files.get(0).getAbsolutePath();
    			br =  new BufferedReader( new FileReader(filePath) ); 
    			String line = ""; 
    	        String[] lineValue = null;
    	        int lineNum = 0; 
    	        List<String[]> result = new ArrayList<String[]>(); 
    	        while((line = br.readLine()) != null) { 
    	        	   // skip the first line
    	        	  if(lineNum>0){
    	        		  // break comma separated file line by line 
    	        		  lineValue = line.split(",");
    	                  result.add(lineValue);
    	                  //only load the first record in the file
    	                  break;
    	        	  }                              
    	               lineNum++; 
    	         }
    	        if(validateUploadFileContent(lineValue)){
    	        	uploadDataToDB(lineValue);
    	        	FacesContext facesContext = FacesContext.getCurrentInstance();
    	    		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Configuration successfully saved...", null);
    	    		facesContext.addMessage(null, facesMessage);
    	    		//reload presentation layer page 
    	    		reload();
    			}
    		}
    	}catch(Exception e){
    		
    	}
	}
	public void uploadAndBackAction()throws IOException{
		uploadAction();
		exitToParticipantEditPage();
	}
	private boolean validateUploadFileContent(String[] lineValue){
		boolean valid = true;
		if(lineValue.length!=27){
			// file length is not correct
			valid = false;
			uploadFileWarningFlag = true;
			uploadFileWarning="file content is not correct";
			return valid;
		}
		for(int i = 0 ;i<lineValue.length;i++){
			String content = lineValue[i];
			if(i==0){
				if(!content.equalsIgnoreCase(participantAccountNumber)){
					valid = false;
					uploadFileWarningFlag = true;
					uploadFileWarning="file content is not correct: PARTICIPANT ACCOUNT NUMBER";
					return valid;
				}
			}
			else if(i==1){
				if(!(content.equalsIgnoreCase(SHED_TYPE_ADVANCED)||content.equalsIgnoreCase(SHED_TYPE_SIMPLE))){
					valid = false;
					uploadFileWarningFlag = true;
					uploadFileWarning="file content is not correct: SHED TYPE must be SIMPLE or ADVANCED";
					return valid;
				}
			}
			else{
				try{
					double value = Double.parseDouble(content);
					if(value<0){
						valid = false;
						uploadFileWarningFlag = true;
						uploadFileWarning="file content is not correct: SHED VALUE must >=0";
						return valid;
					}
				}catch(NumberFormatException e){
					valid = false;
					uploadFileWarningFlag = true;
					uploadFileWarning="file content is not correct: SHED VALUE must be number";
					return valid;
				}
			}
		}
		return valid;		
	}
	private void uploadDataToDB(String[] lineValue){
		ParticipantManager pm = (ParticipantManager) EJBFactory.getBean(ParticipantManager.class);
		String type = lineValue[1];
		if(type.equalsIgnoreCase(SHED_TYPE_SIMPLE)){
			String value = lineValue[2];
			participant.setShedType(SHED_TYPE_SIMPLE);
			participant.setShedPerHourKW(Double.parseDouble(value));
			pm.updateParticipant(participant);
		}else if(type.equalsIgnoreCase(SHED_TYPE_ADVANCED)){
			participant.setShedType(SHED_TYPE_ADVANCED);
			List<ParticipantShedEntry> shedEntries = new ArrayList<ParticipantShedEntry>(participant.getShedEntries());
			Set<ParticipantShedEntry> result = new HashSet<ParticipantShedEntry>();
			if(shedEntries.size()==24){
					//update
					for(ParticipantShedEntry entry:shedEntries){
						for(int i = 3;i<27;i++){
							if((i-3)==entry.getHourIndex()){
								entry.setValue(Double.parseDouble(lineValue[i]));
							}
						}
						result.add(entry);
					}
					participant.setShedEntries(result);
					pm.updateParticipant(participant);
			}else if(shedEntries.size()==0){
				//new
				for(int i = 3;i<27;i++){
					ParticipantShedEntry entry = new ParticipantShedEntry();
					entry.setHourIndex(i-3);
					entry.setParticipant(participant);
					entry.setValue(Double.parseDouble(lineValue[i]));
					result.add(entry);
				}
				participant.setShedEntries(result);
				pm.updateParticipant(participant);
			}
		}
	}	
	
	/**
	 * Checks if is show copy paste.
	 * 
	 * @return true, if is show copy paste
	 */
	public boolean isShowCopyPaste(){
		return participantShedType.equalsIgnoreCase(SHED_TYPE_ADVANCED);
	}
	/**
	 * Copy entry action.
	 * 
	 * @return the string
	 */
	public String copyEntryAction(){
		for(ParticipantShedEntryBean entry: entries){
			if(entry.getTimeBockString().equals(timeBlockString)){
				copyValue = new String(entry.getValue());
				break;
			}
		}
		return null;
	}
	/**
	 * Paste entry action.
	 * 
	 * @return the string
	 */
	public String pasteEntryAction(){
		for(ParticipantShedEntryBean entry: entries){
			if(entry.getTimeBockString().equals(timeBlockString)){
				entry.setValue(copyValue);
				break;
			}
		}
		return null;
	}
	/**
	 * Update strategy action.
	 * 
	 * @return the string
	 */
	public void updateAction()throws IOException{
		if(validateStrategy()){
			mergeShedValue();
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Configuration successfully saved...", null);
			facesContext.addMessage(null, facesMessage);
		}
	}
	public void updateAndBackAction()throws IOException{
		if(validateStrategy()){
			mergeShedValue();
			exitToParticipantEditPage();
		}
	}
	private void mergeShedValue(){
		//retrieve participant from the database
		//set the new value of it and merge 
		ParticipantManager pm = (ParticipantManager) EJBFactory.getBean(ParticipantManager.class);
		if(participantName!=null&&(!participantName.equalsIgnoreCase(""))){
			participant = pm.getParticipantAndShedsOnly(participantName);
			if(participant!=null){
				if(participantShedType.equalsIgnoreCase(SHED_TYPE_ADVANCED)){
					participant = transferShedToParticipant(participant);
					participant.setShedType(SHED_TYPE_ADVANCED);
					pm.updateParticipant(participant);
				}else{
					if(entries.size()>0){
						double value = Double.parseDouble(entries.get(0).getValue());
						participant.setShedType(SHED_TYPE_SIMPLE);
						participant.setShedPerHourKW(value);
						pm.updateParticipant(participant);
					}					
				}
			}
		}
	}
	private Participant transferShedToParticipant(Participant participant){
		if(participantShedType.equalsIgnoreCase(SHED_TYPE_ADVANCED)&&entries.size()==24){
			List<ParticipantShedEntry> shedEntries = new ArrayList<ParticipantShedEntry>(participant.getShedEntries());
			Set<ParticipantShedEntry> result = new HashSet<ParticipantShedEntry>();
			if(shedEntries.size()==24){
				//update
				for(ParticipantShedEntry entry:shedEntries){
					for(ParticipantShedEntryBean bean:entries){
						if(bean.getHourIndex()==entry.getHourIndex()){
							entry.setValue(Double.parseDouble(bean.getValue()));
						}
					}
					result.add(entry);
				}
				participant.setShedEntries(result);
			}else if(shedEntries.size()==0){
				//new
				for(ParticipantShedEntryBean bean:entries){
					ParticipantShedEntry entry = new ParticipantShedEntry();
					entry.setHourIndex(bean.getHourIndex());
					entry.setParticipant(participant);
					entry.setValue(Double.parseDouble(bean.getValue()));
					result.add(entry);
				}
				participant.setShedEntries(result);
			}
		}
		return participant;
	}
		
	private void exitToParticipantEditPage() throws IOException{
		FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
        response.sendRedirect("/pss2.website/commDevDetail.do?dispatch=edit&userName="+participantName);
        fc.responseComplete();
	}
	/**
	 * Cancel strategy action.
	 * 
	 * @return the string
	 * @throws IOException 
	 */
	public void cancelAction() throws IOException{
		exitToParticipantEditPage();
	}
	private boolean validateStrategy(){
		boolean valid = true;
		// do validation for extension
		for(int i=0;i<entries.size();i++){
			ParticipantShedEntryBean bean = entries.get(i);
			try {
				double value = Double.parseDouble(bean.getValue());
				if(value<0){
					FDUtils.addMsgError("The shed value in the time block ["+bean.getTimeBockString()+"] must >=0.");
					valid=false;
				}
			}
			catch (NumberFormatException e){
				FDUtils.addMsgError("The shed value in the time block ["+bean.getTimeBockString()+"] must be number.");
				valid = false;
			}
		}
		return valid;		
	}
	/***********************************************************Getter & Setter***************************************************/
	/**
	 * @param participantShedType the participantShedType to set
	 */
	public void setParticipantShedType(String participantShedType) {
		this.participantShedType = participantShedType;
	}

	/**
	 * @return the participantShedType
	 */
	public String getParticipantShedType() {
		return participantShedType;
	}

	/**
	 * @return the shedType
	 */
	public List<String> getShedType() {
		return shedType;
	}

	/**
	 * @param shedType the shedType to set
	 */
	public void setShedType(List<String> shedType) {
		this.shedType = shedType;
	}

	/**
	 * @return the entries
	 */
	public List<ParticipantShedEntryBean> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<ParticipantShedEntryBean> entries) {
		this.entries = entries;
	}

	/**
	 * @return the timeBlockString
	 */
	public String getTimeBlockString() {
		return timeBlockString;
	}

	/**
	 * @param timeBlockString the timeBlockString to set
	 */
	public void setTimeBlockString(String timeBlockString) {
		this.timeBlockString = timeBlockString;
	}

	/**
	 * @return the copyValue
	 */
	public String getCopyValue() {
		return copyValue;
	}

	/**
	 * @param copyValue the copyValue to set
	 */
	public void setCopyValue(String copyValue) {
		this.copyValue = copyValue;
	}

	/**
	 * @return the participantName
	 */
	public String getParticipantName() {
		return participantName;
	}

	/**
	 * @param participantName the participantName to set
	 */
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	/**
	 * @return the participant
	 */
	public Participant getParticipant() {
		return participant;
	}
	/**
	 * @param participant the participant to set
	 */
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
	/**
	 * @return the files
	 */
	public List<File> getFiles() {
		return files;
	}
	/**
	 * @param files the files to set
	 */
	public void setFiles(List<File> files) {
		this.files = files;
	}
	/**
	 * @return the autoUpload
	 */
	public boolean isAutoUpload() {
		return autoUpload;
	}
	/**
	 * @param autoUpload the autoUpload to set
	 */
	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}
	/**
	 * @return the uploadFileName
	 */
	public String getUploadFileName() {
		return uploadFileName;
	}
	/**
	 * @param uploadFileName the uploadFileName to set
	 */
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	/**
	 * @return the renewFlag
	 */
	public boolean isRenewFlag() {
		return renewFlag;
	}
	/**
	 * @param renewFlag the renewFlag to set
	 */
	public void setRenewFlag(boolean renewFlag) {
		this.renewFlag = renewFlag;
	}
	/**
	 * @return the uploadsAvailable
	 */
	public int getUploadsAvailable() {
		return uploadsAvailable;
	}
	/**
	 * @param uploadsAvailable the uploadsAvailable to set
	 */
	public void setUploadsAvailable(int uploadsAvailable) {
		this.uploadsAvailable = uploadsAvailable;
	}
	/**
	 * @return the uploadFileWarning
	 */
	public String getUploadFileWarning() {
		return uploadFileWarning;
	}
	/**
	 * @param uploadFileWarning the uploadFileWarning to set
	 */
	public void setUploadFileWarning(String uploadFileWarning) {
		this.uploadFileWarning = uploadFileWarning;
	}
	/**
	 * @return the uploadFileWarningFlag
	 */
	public boolean isUploadFileWarningFlag() {
		return uploadFileWarningFlag;
	}
	/**
	 * @param uploadFileWarningFlag the uploadFileWarningFlag to set
	 */
	public void setUploadFileWarningFlag(boolean uploadFileWarningFlag) {
		this.uploadFileWarningFlag = uploadFileWarningFlag;
	}
	/**
	 * @return the participantAccountNumber
	 */
	public String getParticipantAccountNumber() {
		return participantAccountNumber;
	}
	/**
	 * @param participantAccountNumber the participantAccountNumber to set
	 */
	public void setParticipantAccountNumber(String participantAccountNumber) {
		this.participantAccountNumber = participantAccountNumber;
	}


}
