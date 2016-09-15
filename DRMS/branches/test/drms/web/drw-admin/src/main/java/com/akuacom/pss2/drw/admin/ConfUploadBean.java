package com.akuacom.pss2.drw.admin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.akuacom.pss2.drw.DREventManager;
import com.akuacom.pss2.drw.UploadConfManager;
import com.akuacom.pss2.drw.event.creation.CommonValidator;
import com.akuacom.pss2.drw.event.creation.CommonValidator.MSG;

/**
 *SCE Configuration data upload function handler 
 */
public class ConfUploadBean implements Serializable {
	
	private static final Logger log = Logger.getLogger(ConfUploadBean.class);
	
	UploadConfManager manager;
	
	private DREventManager eventManager;
	
	public DREventManager getEvtManager(){
		if(eventManager==null) {
			eventManager = ServiceLocator.findHandler(DREventManager.class,
					"dr-pro/DREventManager/remote");
		}
		
		return eventManager;
	}
	
	private UploadConfManager getDataManager() {
		if(manager == null){
			manager = ServiceLocator.findHandler(UploadConfManager.class,
					"dr-pro/UploadConfManager/remote");
		}
		return manager;
	}
	private static String[] titles = {"SUBSTATION_NUM","SUB_NAME","MAILING_CITY_NAME","ZIP_CODE","COUNTY_NO","COUNTY_NAM","Block","slap_id","slap_name","Alhambra_a_bank_subst_no","alhambra_a_bank_name1","Alhambra_sub_name1"};
	private static final long serialVersionUID = 5521532458435212419L;
    private int uploadsAvailable = 1;
    private boolean autoUpload = false;
    private boolean useFlash = false;
    private boolean enabled=false;
    private String filePath;
    
    private static final String SESSION_ATTRIBUTE_NAME = "uploadFileName";
    
    public String getFileName() {
    	ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
    	HttpServletRequest request = (HttpServletRequest)ectx.getRequest();
    	HttpSession session = request.getSession(false);
    	
	     String name = (String) session.getAttribute(SESSION_ATTRIBUTE_NAME);
    	return name;
	}


    public ConfUploadBean() {
    }

    public void listener(UploadEvent event) throws Exception{
    	UploadItem item = event.getUploadItem();
    	filePath = item.getFile().getAbsolutePath();
    	uploadsAvailable--;
    }  
	protected void report(MSG msg){
		if(msg!=null){
			switch(msg.type){
			case CommonValidator.MSG_ERROR:
				FDUtils.addMsgError(msg.body);
				break;
			case CommonValidator.MSG_INFO:
				FDUtils.addMsgInfo(msg.body);
				break;
			case CommonValidator.MSG_WARN:
				FDUtils.addMsgWarn(msg.body);
				break;
			}
		}
	}
    
    public void fileChoosenAction()
	{
    	long activeCounts = getDataManager().checkActiveEvents();
    	if(activeCounts>0){
    		report(new MSG(CommonValidator.MSG_ERROR,"The selected file could not be uploaded, because Active/Scheduled events(SDP/API) exist."));
    		log.debug("Active or scheduled events exist;-------------------------HALT-----------------------------------");
    		return;
    	}
    	enabled = true;
    	BufferedReader br = null;
    	try{
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
        	  }                              
               lineNum++; 
         } 
         log.debug("Successfully executed;-------------------------INIT-----------------------------------");
         getDataManager().dropTemp();
         log.debug("Successfully executed;-------------------------DROP TEMP TABLE-----------------------------------");
         getDataManager().createTemp();
         log.debug("Successfully executed;-------------------------CREATE TEMP TABLE-----------------------------------");
         getDataManager().batchInsert(result);
         log.debug("Successfully executed;-------------------------insert temp data----------------------------");
         getDataManager().clearLocation();
         getDataManager().clearZipcode();
         log.debug("Successfully executed;-------------------------original data clear-----------------------------");
         getDataManager().loadSlap();
         log.debug("Successfully executed;-------------------------load slap location info------------------------------");
         getDataManager().loadAbank();
         log.debug("Successfully executed;-------------------------load abank location info-----------------------------");
         getDataManager().loadSubstation();
         log.debug("Successfully executed;-------------------------load substation location info-------------------------------");
         getDataManager().loadSlapZip();
         log.debug("Successfully executed;-------------------------load slap zip--------------------------");
         getDataManager().loadAbankZip();
         log.debug("Successfully executed;-------------------------load abank zip-------------------------");
         getDataManager().loadSubZip();
         log.debug("Successfully executed;-------------------------load sub zip---------------------------");
         getDataManager().dropTemp();
         log.debug("Successfully executed;-------------------------DROP Temp table-----------------------------------");
         log.debug("Successfully executed;-------------------------DONE-----------------------------------");
         report(new MSG(CommonValidator.MSG_INFO,"The selected file has been already uploaded. "+(lineNum-1)+ " rows were successfully imported."));
         
         getEvtManager().publishLocationMessage("Location");
    	}catch(Exception ex){
    		
    	}finally{
    		try {
				br.close();
			} catch (IOException e) {
				log.error("Error occured in configuration data upload"+e);
				e.printStackTrace();
			}
    	}
                           
	}
      
    public String clearUploadData() {
        setUploadsAvailable(1);
        return null;
    }
    
    public long getTimeStamp(){
        return System.currentTimeMillis();
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

    public boolean isUseFlash() {
        return useFlash;
    }

    public void setUseFlash(boolean useFlash) {
        this.useFlash = useFlash;
    }

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
