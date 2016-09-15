/**
 * 
 */
package com.akuacom.pss2.drw.core;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * the class BaseEntity
 *
 */
@MappedSuperclass
public abstract class AbstractBaseEntity implements Serializable {

	private static final long serialVersionUID = -325159383740898705L;
	
	/** The creation time. */
	@Temporal( TemporalType.TIMESTAMP)
	protected Date creationTime = new Date();

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
}
