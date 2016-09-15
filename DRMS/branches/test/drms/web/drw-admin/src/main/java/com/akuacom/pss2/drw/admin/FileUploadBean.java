package com.akuacom.pss2.drw.admin;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.akuacom.pss2.drw.LocationKmlManager;

/**
 *
 */
public class FileUploadBean implements Serializable {
	
	LocationKmlManager manager;
	
	private LocationKmlManager getDataManager() {
		if(manager == null){
			manager = ServiceLocator.findHandler(LocationKmlManager.class,
					"dr-pro/LocationKmlManager/remote");
		}
		return manager;
	}
    
	private static final long serialVersionUID = 5521532458435212419L;
    private int uploadsAvailable = 1;
    private boolean autoUpload = false;
    private boolean useFlash = false;
    private DefaultProgressUpdater progressUpdater;
    private boolean enabled=false;
    private String filePath;
    private String locationType;
    
    private static final String SESSION_ATTRIBUTE_NAME = "uploadFileName";
    
    public String getFileName() {
    	ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
    	HttpServletRequest request = (HttpServletRequest)ectx.getRequest();
    	HttpSession session = request.getSession(false);
    	
	     String name = (String) session.getAttribute(SESSION_ATTRIBUTE_NAME);
    	return name;
	}


    public FileUploadBean() {
    }

    public void listener(UploadEvent event) throws Exception{
    	UploadItem item = event.getUploadItem();
    	filePath = item.getFile().getAbsolutePath();
    	uploadsAvailable--;
    }  
    
    public void fileChoosenAction()
	{
    	ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
    	HttpServletRequest request = (HttpServletRequest)ectx.getRequest();
    	HttpSession session = request.getSession(false);
    	session.removeAttribute("curvalue");
    	enabled = true;
    	int totalLinesCount = 0;
    	LineNumberReader br;
		try {
			br = new LineNumberReader(new FileReader(filePath));
			br.skip(Long.MAX_VALUE);
			totalLinesCount = br.getLineNumber();
			br.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
    	progressUpdater  = new DefaultProgressUpdater(totalLinesCount);
    	
		//create a task
        FileHandler task = new FileHandler(getDataManager(), progressUpdater, filePath, session, getLocationType());
		
		ExecutorService threadExecutor = Executors.newFixedThreadPool(5);
		
		//create a task
		threadExecutor.execute(task);
		threadExecutor.shutdown();
		
    	System.out.println("invoke end...");
                           
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

    
	public long getIncValue() {
		if(this.isEnabled()){
			ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
        	HttpServletRequest request = (HttpServletRequest)ectx.getRequest();
        	HttpSession session = request.getSession(false);
        	double p= 0;
        	if(session.getAttribute("curvalue")!=null){
        		p=  (Double)session.getAttribute("curvalue");
        	}
        	
			long lp = (new Double(p)).longValue();
			if(lp<=0) lp =1;
			if(p>=100){
				lp = 101;
			}
			return lp;
		}
		return -1;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getLocationType() {
		return locationType;
	}


	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

}
