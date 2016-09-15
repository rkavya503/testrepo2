/**
 * 
 */
package com.akuacom.pss2.richsite.event.creation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import com.akuacom.pss2.event.Event;
import com.akuacom.pss2.util.LogUtils;

/**
 * the class UploadFile
 */
public class UploadFile  implements Serializable {
	
	private static final long serialVersionUID = -3970200231565493612L;

	private static final Logger log = Logger.getLogger(UploadFile.class.getName());

	private boolean available=false;
	
	private String filename;
	private File file;

	private String message;
	private MeterFileParser parser;

	private EventCreationModel creationModel;
	
	public UploadFile(EventCreationModel creationModel) {
		this.creationModel= creationModel;
	}

	public Event parseUploadFile(Event event) {
		parser=new MeterFileParser(getFileContents(), event.getProgramName(), 
				event.getStartTime(), event.getEndTime());
//		event.setLocations(parser.getLocations());
		return event;
	}

	public String getFileContents() {
		try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
			
			inputStream.close();
			return sb.toString();
		} catch (FileNotFoundException e) {
			log.error(LogUtils.createExceptionLogEntry("", LogUtils.CATAGORY_EVENT, e));
		} catch (IOException e) {
			log.error(LogUtils.createExceptionLogEntry("", LogUtils.CATAGORY_EVENT, e));
		}

		return null;
	}
	
	public void uploadChanged(UploadEvent event){
		UploadItem item = event.getUploadItem();
		if (item != null && item.getFile() != null) {
			if (!item.getFileName().endsWith(".csv")) {
				message="only csv file is available";
				return;
			}
			if (item.getFileSize()==0) {
				clearUpload();
				message="empty file";
				return;
			}
			filename = item.getFileName();
			file = item.getFile();
			creationModel.getParticipantSelection().setAllCandidates(null);
			available=true;
			message="";
			
			//parse location info for CBP programs
			if (creationModel.isCbpProgramEJB()) {
				MeterFileParser parser=new MeterFileParser();
				parser.parseLocation(this.getFileContents(), creationModel.getProgramName());
				if (parser.getLocations().size()!=0) {
					for (EventLocation el:creationModel.getLocationList()) {
						if (parser.getLocations().contains(el.getID()))
							el.setEnrolled(true);
						else
							el.setEnrolled(false);
					}
				}
				
			}
		}
	}
	
	public void clearUpload() {
		filename=null;
		file=null;
		available=false;
		message="";
		parser=null;
		
		if (creationModel.getEvent()!=null && creationModel.getEvent().getEventParticipants()!=null) {
			creationModel.getEvent().getEventParticipants().clear();
		}
		
		if (creationModel.getLocationList()!=null) {
			for (EventLocation location: creationModel.getLocationList()) {
				if (location.isEnrolled())
					location.setEnrolled(false);
			}
		}
	}
	
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public MeterFileParser getParser() {
		return parser;
	}

	public void setParser(MeterFileParser parser) {
		this.parser = parser;
	}

}
