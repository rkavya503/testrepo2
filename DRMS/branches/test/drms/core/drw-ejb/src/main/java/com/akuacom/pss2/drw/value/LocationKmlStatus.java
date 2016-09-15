package com.akuacom.pss2.drw.value;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.commons.lang.time.DateUtils;

import com.akuacom.pss2.core.EJBFactory;
import com.akuacom.utils.DateUtil;

public class LocationKmlStatus implements Serializable{
	
	private static final long serialVersionUID = -6009713919877547115L;
	
	private String type;
	private String number;
	private String name;
	private String kmlAvailable;
	private Date creationTime;
	private Long size;
	private String strSize;
	private String strCreationTime;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKmlAvailable() {
		return kmlAvailable;
	}
	public void setKmlAvailable(String kmlAvailable) {
		this.kmlAvailable = kmlAvailable;
	}
	
	public int getRowIndex(){
		return 0;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getStrSize() {
		if(size==null){
			return "0k";
		}
		return size/1000 +"k";
	}
	public String getStrCreationTime() {
		if(creationTime==null) return "-";
		//performance tuning
	  //  SystemManager systemManager = EJBFactory.getBean(SystemManager.class);

      //  final String dateFormat=systemManager.getPss2Features().getDateFormat();
		String dateFormat = "MM/dd/yyyy";
        
        final Boolean time24hours=true;//systemManager.getPss2Features().isTime24Hours();
    	String shortTimeFormat="HH:mm";
    	
        if (time24hours!=null && !time24hours) {
        	shortTimeFormat="hh:mm a";
        }
        String shortDateTimeFormat=dateFormat+" "+shortTimeFormat;
        
        return DateUtil.format(creationTime, shortDateTimeFormat);
		
	}
	
}
