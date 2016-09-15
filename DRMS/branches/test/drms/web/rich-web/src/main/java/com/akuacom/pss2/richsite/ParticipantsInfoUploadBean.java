
package com.akuacom.pss2.richsite;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.program.sceftp.SCEFTPManager;
import com.akuacom.pss2.program.sceftp.SCEParticipantFileParser;




public class ParticipantsInfoUploadBean implements Serializable {
	private static final Logger log = Logger.getLogger(ParticipantsInfoUploadBean.class);
	private static final long serialVersionUID = -4391276438554592631L;
	private int uploadsAvailable = 1;
    private boolean autoUpload = false;
    private boolean useFlash = false;
    private String filePath;
    private List<String> validationMessages = new ArrayList<String>();
	private String fileName;
	
	private String message;
	@EJB
    SCEFTPManager.L manager;
	
	public ParticipantsInfoUploadBean(){
		clear();
	}
	public void listener(UploadEvent event) throws Exception{
    	UploadItem item = event.getUploadItem();
    	log.warn("Participants information upload listener invoked..."+item.getFileName());
    	filePath = item.getFile().getAbsolutePath();
    	setFileName(item.getFileName());
    	setMessage("Upload participant information file to server.");
    	uploadsAvailable--;
    }
	
	public void uploadCompleteAction(){
		log.warn("Participants information upload complete action...");
		
		if(filePath!=null){
			try{
				String fileContent = fileToString(filePath);
				//System.out.println(fileContent);
				//validationMessages.add(fileContent);
				log.warn("Now validating the upload file...");
				
				SCEParticipantFileParser parser=new SCEParticipantFileParser(fileContent, fileName);
				parser.validation();
				manager.processParserFile(parser);
				processEnd();
			}catch (ProgramValidationException e) {
				List<ProgramValidationMessage> errors=new ArrayList<ProgramValidationMessage>();
				errors = e.getErrors();
				validationMessages.clear();
				for(ProgramValidationMessage message:errors){
					validationMessages.add(message.getDescription());
				}
				if(errors.size()>0){
					setMessage(errors.get(0).getDescription());	
				}
				
				manager.sendExceptionNotifications(e, fileName);
			} catch(Exception e) {
				validationMessages.clear();
				validationMessages.add(e.getMessage());
				setMessage(e.getMessage());
				manager.sendExceptionNotifications(e, fileName);
			}
			
		}
	}
	
	public void processEnd(){
		log.warn("Participants information upload complete action...processEnd");	
		validationMessages.clear();
		validationMessages.add("Participants information upload complete");
		setMessage("Participants information upload complete.");
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
	 * @return the useFlash
	 */
	public boolean isUseFlash() {
		return useFlash;
	}

	/**
	 * @param useFlash the useFlash to set
	 */
	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
	} 
	
	

	/**
	 * @return the validationMessages
	 */
	public List<String> getValidationMessages() {
		return validationMessages;
	}
	/**
	 * @param validationMessages the validationMessages to set
	 */
	public void setValidationMessages(List<String> validationMessages) {
		this.validationMessages = validationMessages;
	}
	
	 /**
     * Description of the Method
     *
     * @param file
     *          The file to be turned into a String
     * @return  The file as String encoded in the platform
     * default encoding
     */
    public static String fileToString(String file) {
        String result = null;
        DataInputStream in = null;

        try {
        	 File f = new File(file);
            byte[] buffer = new byte[(int) f.length()];
            in = new DataInputStream(new FileInputStream(f));
            in.readFully(buffer);
            result = new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException("IO problem in fileToString", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) { /* ignore it */
            }
        }
        return result;
    }

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	
	public void uploadPrepareAction(){
		setMessage("Prepare upload file...");
	}
	public void uploadAction(){
		setMessage("Uploading file and validating the content...");
	}
	
	public void clear(){
		uploadsAvailable= 1 ;
		setMessage("Process not started, please select a file to upload...");
	}
}
