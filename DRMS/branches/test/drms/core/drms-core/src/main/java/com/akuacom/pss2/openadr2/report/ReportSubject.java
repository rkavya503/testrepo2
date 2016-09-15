package com.akuacom.pss2.openadr2.report;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.akuacom.ejb.VersionedEntity;
@Entity
@NamedQueries({})
@Table(name="reportsubject")
@XmlRootElement
public class ReportSubject extends VersionedEntity {
	
	private static final long serialVersionUID = 1L;

    protected String type;    
	protected String device;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDevice() {
		return device;
	}
	
	public void setDevice(String device) {
		this.device = device;
	}	
}
