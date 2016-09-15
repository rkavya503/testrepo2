/**
 * 
 */
package com.akuacom.pss2.drw.core;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * the class BaseLibraryEntirty
 * 
 */
@MappedSuperclass
public abstract class AbstractLibraryEntirty extends AbstractBaseEntity {

	private static final long serialVersionUID = 5128582947489672641L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ID;

	public Long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	@Override
	public int hashCode() {
		int hashCode=0;
		if (ID==null)
			hashCode=super.hashCode();
		else
			hashCode=ID.hashCode();
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && obj instanceof AbstractLibraryEntirty) {
			Long myId = this.getID();
			Long theirId = ((AbstractLibraryEntirty) obj).getID();
			if (myId != null && theirId != null) {
				result = myId.equals(theirId);
			}
		}
		return result;
	}

}
