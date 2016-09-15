/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.entities.SignalEAO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.data;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;

/**
 * The Class DataSource.
 */
@Entity
@Table(name = "datasource")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NamedQueries({
        @NamedQuery(name = "PDataSource.findByOwnerAndType", query = "select ds from PDataSource ds where ds.ownerID = :ownerID and ds.ownerType = :ownerType", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "PDataSource.findByNameOwnerAndType", query = "select ds from PDataSource ds where ds.name = :name and ds.ownerID = :ownerID and ds.ownerType = :ownerType", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "PDataSource.findByNameAndOwner", query = "select ds from PDataSource ds where ds.name = :name and ds.ownerID = :ownerID", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "PDataSource.findByNameAndOwners", query = "select ds.UUID from PDataSource ds where ds.name = :name and ds.ownerID in (:ownerIDs)", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "PDataSource.findByOwner", query = "select ds from PDataSource ds where ds.ownerID in (:ownerIDs)", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "PDataSource.getOwnerIdsFromDataSourceIds", query = "select ds.ownerID from PDataSource ds where ds.UUID in (:ids)", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "PDataSource.getUuidAndOwnerIdFromOwnerIds", query = "select new scala.Tuple2(ds.UUID, ds.ownerID) from PDataSource ds where ds.ownerID in (:ownerIDs)", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") }),
        @NamedQuery(name = "PDataSource.getBySiteID", 
        	query = "select ds from PDataSource ds where ds.siteID is not null and ds.siteID !=''"),
        @NamedQuery(name = "PDataSource.getMissingOwnerIds", 
                query = "select p.participantName from Participant p where not exists (select ds.ownerID from PDataSource ds where ds.ownerID  = p.participantName)", hints = { @QueryHint(name = "org.hibernate.cacheable", value = "true") })
        
})
public class PDataSource extends BaseEntity {

    private static final long serialVersionUID = 2070056470982359874L;

    public static final int TYPE_PARTICIPANT = 0;
    public static final int TYPE_EVENT       = 1;
    public static final int TYPE_PROGRAM     = 2;
    
    private String ownerID;

    private String name;

    private boolean enabled;

    private int ownerType;
    
    private String siteID;
    private String serviceProvider;
    
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "datasource")
    private Set<PDataEntry> dataEntryList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public Set<PDataEntry> getDataEntryList() {
        return dataEntryList;
    }

    public void setDataEntryList(Set<PDataEntry> dataEntryList) {
        this.dataEntryList = dataEntryList;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the ownerType
     */
    public int getOwnerType() {
        return ownerType;
}

    /**
     * @param ownerType the ownerType to set
     */
    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
    }

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
    

}