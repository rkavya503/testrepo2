/**
 * 
 */
package com.akuacom.pss2.drw.core;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * the class BaseEntity
 *
 */
@MappedSuperclass
public abstract class AbstractApplicationEntity extends AbstractBaseEntity {

	private static final long serialVersionUID = -325159383740898705L;
	
	/** The UUID. */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String UUID;

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	@Override
	public int hashCode() {
		int hashCode=0;
		if (UUID==null)
			hashCode=super.hashCode();
		else
			hashCode=UUID.hashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && obj instanceof AbstractApplicationEntity) {
			String myId = this.getUUID();
			String theirId = ((AbstractApplicationEntity) obj).getUUID();
			if (myId != null && theirId != null) {
				result = myId.equals(theirId);
			}
		}
		return result;
	}
}
