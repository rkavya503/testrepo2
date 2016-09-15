/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.BaseEntity.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.ejb;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 * The Class BaseEntity.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -8922001431224779446L;
	final public static int UUID_SIZE=32;

	/** The UUID. */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String UUID;

	
	/** The creation time. */
	@Temporal( TemporalType.TIMESTAMP)
	protected Date creationTime = new Date();

    /**
	 * Gets the uUID.
	 * 
	 * @return the uUID
	 */
	public String getUUID() {
		return UUID;
	}

	/**
	 * Sets the uUID.
	 * 
	 * @param UUID
	 *            the new uUID
	 */
	public void setUUID(String UUID) {
		this.UUID = setIfNotNull(UUID, this.UUID);
	}

	/**
	 * Provided to package friends to allow the removal of the UUID. Typically
	 * only useful when wanting to create a copy of an entity.
	 * 
	 */
	/* friendly */void deleteUUID() {
		this.UUID = null;
	}	

    /**
	 * Gets the creation time.
	 * 
	 * @return the creation time
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 * 
	 * @param creationTime
	 *            the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = setIfNotNull(creationTime, this.creationTime);
	}
	
	/**
	 * Creates the version info.
     */
    @PrePersist
    public void createCreationTime() {
        setCreationTime(new Date());
    }
    

    protected static <T> T setIfNotNull(T theirValue, T myValue) {
		if (theirValue != null) {
			return theirValue;
		} else {
			return myValue;
		}

	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && obj instanceof BaseEntity) {
			// ensures the ids are the exact same
			String myId = this.getUUID();
			String theirId = ((BaseEntity) obj).getUUID();
			if (myId != null && theirId != null) {
				result = myId.equals(theirId);
			}
		}
		return result;
	}

	@Override
	public String toString() {
	
		return getClass().getSimpleName() + " : " + getUUID();
	}

	@Override
	public int hashCode() {

		String id = getUUID();
		if (id == null) {
			return super.hashCode();
		} else {
			return id.hashCode();
		}
	}
	
	static class Details
	{
		final static int UUID_LENGTH = 32;
	}

}
