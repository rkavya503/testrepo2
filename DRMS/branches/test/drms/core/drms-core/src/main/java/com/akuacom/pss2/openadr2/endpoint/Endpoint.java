package com.akuacom.pss2.openadr2.endpoint;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.akuacom.ejb.VersionedEntity;

/**
 * Entity implementation class for Entity: Party
 * 
 */
@Entity
@Table(name = "openadr2_endpoint")
@NamedQueries({ 
	@NamedQuery(name = "Endpoint.findByName", query = "SELECT ep FROM Endpoint ep WHERE ep.venName = :name"),
	@NamedQuery(name = "Endpoint.findByVenId", query = "SELECT ep FROM Endpoint ep WHERE ep.venId = :venId and ep.canceled =false"),
	@NamedQuery(name = "Endpoint.findByVenIdAndRegistrationId", query = "SELECT ep FROM Endpoint ep WHERE ep.venId = :venId and ep.registrationId = :registrationId"),
	@NamedQuery(name = "Endpoint.findAll", query = "SELECT ep FROM Endpoint ep WHERE ep.canceled = false")
})
@XmlRootElement
public class Endpoint extends VersionedEntity {

	private static final long serialVersionUID = -6493938098737415549L;
	
    // from BaseClass
    private String schemaVersion;
    
	private String fingerprint;
    
    @Column(nullable=false, unique=true)
	private String venId;
    
    private String certCommonName;
	
    
	// from CreatePartyReg
	@Enumerated(value = EnumType.STRING)
	private String profileName;
	
	@Enumerated(value = EnumType.STRING)
	private String transport;
	
	private String transportAddress;
	
	private Boolean reportOnly;
	private Boolean xmlSignature;
	private Boolean httpPullMode;
	
	private Boolean canceled = false;
	
	private String venName;
	
	private String registrationId;
    
    @Enumerated(value = EnumType.STRING)
    private CommStatusEnum commStatus = CommStatusEnum.UNKNOWN;
    private Date commStatusTimeStamp;
    
    private Long maxRetries = new Long(5);
    
    /**
     * Delay in seconds between retries sending to this VEN / Endpoint
     */
    private Long delayBetweenRetries = new Long(30);
    
    public Endpoint() {
		super();
	}
    
	public CommStatusEnum getCommStatus() {
		return commStatus;
	}

	public void setCommStatus(CommStatusEnum commStatus) {
		this.commStatus = commStatus;
	}

	public Date getCommStatusTimeStamp() {
		return commStatusTimeStamp;
	}

	public void setCommStatusTimeStamp(Date commStatusTimeStamp) {
		this.commStatusTimeStamp = commStatusTimeStamp;
	}
	
	public Long getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(Long maxRetries) {
		this.maxRetries = maxRetries;
	}

	public Long getDelayBetweenRetries() {
		return delayBetweenRetries;
	}

	public void setDelayBetweenRetries(Long delayBetweenRetries) {
		this.delayBetweenRetries = delayBetweenRetries;
	}

	public String getSchemaVersion() {
		return schemaVersion;
	}

	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getVenId() {
		return venId;
	}

	public void setVenId(String venId) {
		this.venId = venId;
	}

	public String getCertCommonName() {
		return certCommonName;
	}

	public void setCertCommonName(String certCommonName) {
		this.certCommonName = certCommonName;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getTransportAddress() {
		return transportAddress;
	}

	public void setTransportAddress(String transportAddress) {
		this.transportAddress = transportAddress;
	}

	public Boolean getReportOnly() {
		return reportOnly;
	}

	public void setReportOnly(Boolean reportOnly) {
		this.reportOnly = reportOnly;
	}

	public Boolean getXmlSignature() {
		return xmlSignature;
	}

	public void setXmlSignature(Boolean xmlSignature) {
		this.xmlSignature = xmlSignature;
	}

	public Boolean getHttpPullMode() {
		return httpPullMode;
	}

	public void setHttpPullMode(Boolean httpPullMode) {
		this.httpPullMode = httpPullMode;
	}

	public Boolean getCanceled() {
		return canceled;
	}

	public void setCanceled(Boolean canceled) {
		this.canceled = canceled;
	}

	public String getVenName() {
		return venName;
	}

	public void setVenName(String venName) {
		this.venName = venName;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	@Override
	public String toString() {
		return "Endpoint [commStatus="
				+ commStatus + ", commStatusTimeStamp=" + commStatusTimeStamp
				+ ", maxRetries=" + maxRetries + ", schemaVersion="
				+ schemaVersion + ", fingerprint=" + fingerprint + ", venId="
				+ venId + ", profileName=" + profileName + ", transport="
				+ transport + ", transportAddress=" + transportAddress
				+ ", reportOnly=" + reportOnly + ", xmlSignature="
				+ xmlSignature + ", httpPullMode=" + httpPullMode
				+ ", venName=" + venName + ", registrationId=" + registrationId
				+ ", delayBetweenRetries=" + delayBetweenRetries + "]";
	}
}
