package com.akuacom.pss2.openadr2.endpoint;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import com.akuacom.ejb.VersionedEntity;

@MappedSuperclass
public class Link extends VersionedEntity {
	 
	private static final long serialVersionUID = -7106034385036289465L;
	private Date linkexpiry;
	private boolean linkactive;
	private Date linkactivateddate;
	private Date linkdeactivateddate;
	
	public Date getLinkexpiry() {
		return linkexpiry;
	}
	public void setLinkexpiry(Date linkexpiry) {
		this.linkexpiry = linkexpiry;
	}
	public boolean isLinkactive() {
		return linkactive;
	}
	public void setLinkactive(boolean linkactive) {
		this.linkactive = linkactive;
	}
	public Date getLinkactivateddate() {
		return linkactivateddate;
	}
	public void setLinkactivateddate(Date linkactivateddate) {
		this.linkactivateddate = linkactivateddate;
	}
	public Date getLinkdeactivateddate() {
		return linkdeactivateddate;
	}
	public void setLinkdeactivateddate(Date linkdeactivateddate) {
		this.linkdeactivateddate = linkdeactivateddate;
	}
	
	
}
