package com.akuacom.pss2.contact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.VersionedEntity;

@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "contacts_offline_error")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries( {
	@NamedQuery(name = "ContactsOfflineError.findAll", 
			query = "select c from ContactsOfflineError c",
	        hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
	@NamedQuery(name = "ContactsOfflineError.findByProgramAndParticipantAndContactsUuid", 
			query = "select c from ContactsOfflineError c where c.programName = :programName and c.participantUuid = :participantUuid and c.contactsUuid = :contactsUuid",
	        hints={@QueryHint(name="org.hibernate.cacheable", value="true")})
})
public class ContactsOfflineError extends VersionedEntity {
	
	private static final long serialVersionUID = -6251602181823604185L;

	/** The offline error. */
    private Boolean offlineError = new Boolean(false);

    private String programName;
    
    private String contactsUuid;
    
	@Column(name = "participant_uuid")
    private String participantUuid;

	public boolean isOfflineError() {
		return offlineError;
	}

	public void setOfflineError(boolean offlineError) {
		this.offlineError = offlineError;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getContactsUuid() {
		return contactsUuid;
	}

	public void setContactsUuid(String contactsUuid) {
		this.contactsUuid = contactsUuid;
	}

	public String getParticipantUuid() {
		return participantUuid;
	}

	public void setParticipantUuid(String participantUuid) {
		this.participantUuid = participantUuid;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ContactsOfflineError)) {
			return false;
		}
		
		ContactsOfflineError coe = (ContactsOfflineError)obj;
		
		if (this.getUUID() != null && coe.getUUID() != null && !this.getUUID().equals(coe.getUUID())) {
			return false;
		}
		
		boolean res = (coe.getContactsUuid() == null 
				? this.getContactsUuid() == null : coe.getContactsUuid().equals(this.getContactsUuid()));
		if (res) {
			res = (coe.getParticipantUuid() == null 
					? this.getParticipantUuid() == null : coe.getParticipantUuid().equals(this.getParticipantUuid()));
			
			if (res) {
				res = (coe.getProgramName() == null 
						? this.getProgramName() == null : coe.getProgramName().equals(this.getProgramName()));
			}
		}
		
		return res;
	}
}
