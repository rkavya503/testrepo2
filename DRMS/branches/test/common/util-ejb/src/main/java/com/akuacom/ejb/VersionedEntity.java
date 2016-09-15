package com.akuacom.ejb;

import java.util.Date;

import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.persistence.Version;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class VersionedEntity extends BaseEntity {
    /**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = -1269714082362765627L;
	/** The version. */
	//@Version
    private Integer version = 0;
    /** The modified time. */
    @Temporal( TemporalType.TIMESTAMP)
    private Date modifiedTime;
    /** The creator. */
    private String creator;
    /** The modifier. */
    private String modifier;
    
    public VersionedEntity() {}

    /**
	 * Gets the version.
     *
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
	 * Sets the version.
     *
     * @param version
     *            the new version
     */
    public void setVersion(Integer version) {
        this.version = setIfNotNull(version, this.version);
    }

    /**
	 * Gets the modified time.
     *
     * @return the modified time
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
	 * Sets the modified time.
     *
     * @param modifiedTime
     *            the new modified time
     */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = setIfNotNull(modifiedTime, this.modifiedTime);
    }

    /**
	 * Gets the creator.
     *
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
	 * Sets the creator.
     *
     * @param creator
     *            the new creator
     */
    public void setCreator(String creator) {
        this.creator = setIfNotNull(creator, this.creator);
    }

    /**
	 * Gets the modifier.
     *
     * @return the modifier
     */
    public String getModifier() {
        return modifier;
    }

    /**
	 * Sets the modifier.
     *
     * @param modifier
     *            the new modifier
     */
    public void setModifier(String modifier) {
        this.modifier = setIfNotNull(modifier, this.modifier);
    }

    /**
	 * Username for logging.
	 *
	 * @return the string
	 */
	private String usernameForLogging() {
		String res = "system";
		try {
			InitialContext ic = new InitialContext();
			SessionContext sctxLookup = (SessionContext) ic
					.lookup("java:comp/EJBContext");
			if (sctxLookup != null && sctxLookup.getCallerPrincipal() != null)
				res = sctxLookup.getCallerPrincipal().getName();
		} catch (Exception ex) {
			// ignore
		}

		return res;
	}

    /**
	 * Creates the version info.
     */
    @PrePersist
    public void createVersionInfo() {
        setVersion(0);
        setCreationTime(new Date());
        setCreator(usernameForLogging());
    }

    /**
	 * Update version info.
     */
    @PreUpdate
    public void updateVersionInfo() {
        setVersion(version + 1);
        setModifiedTime(new Date());
        setModifier(usernameForLogging());
    }
}
